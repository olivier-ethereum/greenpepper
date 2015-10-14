package com.greenpepper.confluence.macros;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.atlassian.confluence.pages.Page;
import com.atlassian.confluence.renderer.PageContext;
import com.atlassian.confluence.renderer.radeox.macros.MacroUtils;
import com.atlassian.confluence.util.velocity.VelocityUtils;
import com.atlassian.renderer.RenderContext;
import com.atlassian.renderer.v2.macro.MacroException;
import com.greenpepper.confluence.utils.MacroCounter;
import com.greenpepper.server.GreenPepperServerException;

public class GreenPepperInclude extends AbstractGreenPepperMacro 
{
	private static final String INCLUDED_PAGE_PARAM_NAME = "gp$included";

	@Override
    public boolean isInline()
    {
        return true;
    }

    public String execute(@SuppressWarnings("rawtypes") Map parameters, String body, RenderContext renderContext) throws MacroException
    {
		final PageContext context = (PageContext) renderContext;
        final boolean isRoot = ( getIncludedPagesParam( context ) == null );

        try
        {
            checkMandatoryPageTitleParameter( parameters );

            final String spaceKey = getSpaceKey( parameters, renderContext, false );
            final String pageTitle = getPageTitle( parameters, renderContext, spaceKey );
			final Page owner = (Page)context.getEntity();

            Page page = gpUtil.getPageManager().getPage( spaceKey, pageTitle );

            List<Page> includedPages = getSafeIncludedPagesParam( context, owner );

			if (includedPages.contains( page ))
            {
                throw new GreenPepperServerException( "greenpepper.include.recursivitydetection", "" );
            }

            try
            {
                includedPages.add( page );

                return render( parameters, context, pageTitle, page );
            }
            finally
            {
                includedPages.remove( page );
            }
        }
        catch (GreenPepperServerException gpe)
        {
            return getErrorView( "greenpepper.include.macroid", gpe.getId() , gpe.getLocalizedMessage() );
        }
        catch (Exception e)
        {
            return getErrorView( "greenpepper.include.macroid", e.getMessage() );
        }
		finally {
			if (isRoot) {
				cleanIncludedPagesParam(context);
			}
		}
    }

    private void checkMandatoryPageTitleParameter(@SuppressWarnings("rawtypes") Map parameters) throws GreenPepperServerException
    {
        if (!parameters.containsKey( "pageTitle" ))
        {
            throw new GreenPepperServerException( "greenpepper.children.pagenotfound", "" );
        }
    }

	@SuppressWarnings("unchecked")
	private List<Page> getIncludedPagesParam(PageContext context)
	{
		return (List<Page>) context.getParam( INCLUDED_PAGE_PARAM_NAME );
	}

    private List<Page> getSafeIncludedPagesParam(PageContext context, Page owner)
    {
		List<Page> pages = getIncludedPagesParam(context);

		if (pages == null)
        {
            pages = new ArrayList<Page>();
			pages.add( owner );
            context.addParam( INCLUDED_PAGE_PARAM_NAME, pages );
        }

        return pages;
    }

    private String render(@SuppressWarnings("rawtypes") Map parameters, PageContext context, String pageTitle, Page page)
    {
        Map<String,Object> contextMap = MacroUtils.defaultVelocityContext();

        String title = (String) parameters.get( "title" );
        contextMap.put( "title", title != null ? title : pageTitle );
        contextMap.put( "includeHtml", gpUtil.getViewRenderer().render(page));
        contextMap.put( "executionUID", "GP_INCLUDE_" + MacroCounter.instance().getNextCount() );
        contextMap.put( "expanded", isExpanded( parameters ) );

        return VelocityUtils.getRenderedTemplate( "/templates/greenpepper/confluence/macros/greenPepperInclude.vm", contextMap );
    }

	private void cleanIncludedPagesParam(PageContext context) {

		List<Page> pages = getIncludedPagesParam(context);

		if (pages != null) {
			pages.clear();
		}

		context.addParam( INCLUDED_PAGE_PARAM_NAME, null );
	}
}
