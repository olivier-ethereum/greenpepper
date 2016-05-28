package com.greenpepper.confluence.macros;

import java.util.Map;

import com.atlassian.confluence.content.render.xhtml.ConversionContext;
import com.atlassian.confluence.macro.MacroExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atlassian.renderer.RenderContext;
import com.atlassian.renderer.v2.RenderMode;
import com.atlassian.renderer.v2.macro.MacroException;

/**
 * <p>GreenPepperWiki class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 * @since 4.0-beta3
 */
public class GreenPepperWiki extends AbstractGreenPepperMacro
{
    
    private static final Logger LOGGER = LoggerFactory.getLogger(GreenPepperWiki.class);
    
    /** {@inheritDoc} */
    @Override
    public boolean hasBody()
    {
        return true;
    }
    
    /**
     * <p>getBodyRenderMode.</p>
     *
     * @return a {@link com.atlassian.renderer.v2.RenderMode} object.
     */
    public RenderMode getBodyRenderMode()
    {
        return RenderMode.ALL;
    }
    
    /** {@inheritDoc} */
    @Override
    public BodyType getBodyType()
    {
        return BodyType.PLAIN_TEXT;
    }
    
    /**
     * Confluence 2 and 3
     *
     * @param parameters
     * @param body
     * @param renderContext
     * @return
     * @throws MacroException
     */
    @Override
    public String execute(@SuppressWarnings("rawtypes") Map parameters, String body, RenderContext renderContext) throws MacroException 
    {
        try
        {
            return body;
        }
        catch (Exception e)
        {
            return getErrorView("greenpepper.info.macroid", e.getMessage());
        }
    }

    /**
     * Confluence 4+
     * @param parameters
     * @param body
     * @param context
     * @return
     * @throws MacroExecutionException
     */
    @Override
    public String execute(Map<String, String> parameters, String body,
                          ConversionContext context) throws MacroExecutionException {
        try
        {
            String xhtmlRendered = gpUtil.getWikiStyleRenderer().convertWikiToXHtml(context.getPageContext(), body);
            LOGGER.trace("rendering : \n - source \n {} \n - output \n {}", body, xhtmlRendered);
            return xhtmlRendered;
        }
        catch (Exception e)
        {
            return getErrorView("greenpepper.info.macroid", e.getMessage());
        }
    }
}
