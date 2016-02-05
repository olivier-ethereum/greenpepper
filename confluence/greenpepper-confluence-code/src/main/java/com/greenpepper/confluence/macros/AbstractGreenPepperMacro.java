package com.greenpepper.confluence.macros;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.atlassian.confluence.content.render.xhtml.ConversionContext;
import com.atlassian.confluence.core.ContentEntityObject;
import com.atlassian.confluence.macro.Macro;
import com.atlassian.confluence.macro.MacroExecutionException;
import com.atlassian.confluence.pages.Page;
import com.atlassian.confluence.renderer.PageContext;
import com.atlassian.confluence.renderer.radeox.macros.MacroUtils;
import com.atlassian.confluence.spaces.Space;
import com.atlassian.confluence.util.SpaceComparator;
import com.atlassian.confluence.util.velocity.VelocityUtils;
import com.atlassian.renderer.RenderContext;
import com.atlassian.renderer.v2.RenderMode;
import com.atlassian.renderer.v2.macro.BaseMacro;
import com.atlassian.renderer.v2.macro.MacroException;
import com.greenpepper.confluence.velocity.ConfluenceGreenPepper;
import com.greenpepper.server.GreenPepperServerException;
import com.greenpepper.util.StringUtil;

/**
 * <p>Abstract AbstractGreenPepperMacro class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public abstract class AbstractGreenPepperMacro extends BaseMacro implements Macro
{
    protected ConfluenceGreenPepper gpUtil = new ConfluenceGreenPepper();

// Macros v4    
	/**
	 * <p>getBodyType.</p>
	 *
	 * @return a BodyType object.
	 */
	public BodyType getBodyType() {
		 return BodyType.NONE;
	}

	/**
	 * <p>getOutputType.</p>
	 *
	 * @return a OutputType object.
	 */
	public OutputType getOutputType() {
		 return OutputType.BLOCK;
	}

	/** {@inheritDoc} */
	public String execute(Map<String, String> parameters, String body,
			ConversionContext context) throws MacroExecutionException {
	      try
	        {
	            return execute(parameters, body, context.getPageContext());
	        }
	        catch (MacroException e)
	        {
	            throw new MacroExecutionException(e);
	        } 	
	 }
// End Macros V4
	
    /**
     * <p>isInline.</p>
     *
     * @return a boolean.
     */
    public boolean isInline()
    {
        return false;
    }
    
    /**
     * <p>hasBody.</p>
     *
     * @return a boolean.
     */
    public boolean hasBody()
    {
        return false;
    }
    
    /**
     * <p>getBodyRenderMode.</p>
     *
     * @return a {@link com.atlassian.renderer.v2.RenderMode} object.
     */
    public RenderMode getBodyRenderMode()
    {
        return RenderMode.NO_RENDER;
    }
    

    /**
     * <p>getSpaceKey.</p>
     *
     * @param parameters a {@link java.util.Map} object.
     * @return a {@link java.lang.String} object.
     * @throws com.greenpepper.server.GreenPepperServerException if any.
     */
    protected String getSpaceKey(@SuppressWarnings("rawtypes") Map parameters) throws GreenPepperServerException
    {
        String spaceKey = (String)parameters.get("spaceKey");
        if(!StringUtil.isEmpty(spaceKey))
        {
            spaceKey = spaceKey.trim();

			Space space = gpUtil.getSpaceManager().getSpace(spaceKey);
			if(space == null)
				throw new GreenPepperServerException("greenpepper.children.spacenotfound", spaceKey);

			checkSpace(space);
        }
        
        return spaceKey;
    }
    
    /**
     * <p>getSpaceKey.</p>
     *
     * @param parameters a {@link java.util.Map} object.
     * @param renderContext a {@link com.atlassian.renderer.RenderContext} object.
     * @param checkPermission a boolean.
     * @return a {@link java.lang.String} object.
     * @throws com.greenpepper.server.GreenPepperServerException if any.
     */
    protected String getSpaceKey(@SuppressWarnings("rawtypes") Map parameters, RenderContext renderContext, boolean checkPermission) throws GreenPepperServerException
    {
    	Space space;
        String spaceKey = (String)parameters.get("spaceKey");
        
        if(StringUtil.isEmpty(spaceKey))
        {
			space = getCurrentSpace(renderContext);
        }
        else
        {
            spaceKey = spaceKey.trim();
            space = gpUtil.getSpaceManager().getSpace(spaceKey);
            if(space == null)
                throw new GreenPepperServerException("greenpepper.children.spacenotfound", spaceKey);
        }
        
        if (checkPermission)
        	checkSpace(space);
        
        return space.getKey();
    }

	/**
	 * <p>getCurrentSpace.</p>
	 *
	 * @param renderContext a {@link com.atlassian.renderer.RenderContext} object.
	 * @return a {@link com.atlassian.confluence.spaces.Space} object.
	 */
	protected Space getCurrentSpace(RenderContext renderContext)
	{
		ContentEntityObject owner = ((PageContext)renderContext).getEntity();
		return ((Page)owner).getSpace();
	}

	/**
	 * <p>getPageTitle.</p>
	 *
	 * @param parameters a {@link java.util.Map} object.
	 * @param renderContext a {@link com.atlassian.renderer.RenderContext} object.
	 * @param spaceKey a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 * @throws com.greenpepper.server.GreenPepperServerException if any.
	 */
	@SuppressWarnings("unchecked")
    protected String getPageTitle(@SuppressWarnings("rawtypes") Map parameters, RenderContext renderContext, String spaceKey) throws GreenPepperServerException
    {
        return getPage(parameters, renderContext, spaceKey).getTitle().trim();
    }
    
    /**
     * <p>getPage.</p>
     *
     * @param parameters a {@link java.util.Map} object.
     * @param renderContext a {@link com.atlassian.renderer.RenderContext} object.
     * @param spaceKey a {@link java.lang.String} object.
     * @return a {@link com.atlassian.confluence.pages.Page} object.
     * @throws com.greenpepper.server.GreenPepperServerException if any.
     */
    protected Page getPage(Map<String,String> parameters, RenderContext renderContext, String spaceKey) throws GreenPepperServerException
    {
        String pageTitle = parameters.get("pageTitle");
        if(StringUtil.isEmpty(pageTitle))
        {
            ContentEntityObject owner = ((PageContext)renderContext).getEntity();
            return (Page)owner;
        }

        Page page = gpUtil.getPageManager().getPage(spaceKey, pageTitle);
        if(page == null)
            throw new GreenPepperServerException("greenpepper.children.pagenotfound", String.format("'%s' in space '%s'", pageTitle, spaceKey));
        
        return page;
    }

    /**
     * <p>getSpaces.</p>
     *
     * @return a {@link java.util.List} object.
     * @throws com.greenpepper.server.GreenPepperServerException if any.
     */
    protected List<Space> getSpaces() throws GreenPepperServerException
    {
        List<Space> spaces = new ArrayList<Space>();
        List<Space> potentialSpaces = gpUtil.getSpaceManager().getAllSpaces();
        
        for(Space space : potentialSpaces)
        {
            try { if(gpUtil.canView(space) && gpUtil.enable(space.getKey()) == null) spaces.add(space); }
            catch (GreenPepperServerException e){}
        } 
        
        if(spaces.isEmpty())
            throw new GreenPepperServerException("greenpepper.labels.registeredspacesempty", "No registered repository");

		Collections.sort(spaces, new SpaceComparator());
		
		return spaces;
    }
    
    /**
     * <p>getBulkUID.</p>
     *
     * @param parameters a {@link java.util.Map} object.
     * @return a {@link java.lang.String} object.
     */
    protected String getBulkUID(Map<String,String> parameters)
    {
        String group = (String)parameters.get("group");
        return StringUtil.isEmpty(group) ? "PAGE" : group;
    }
    
    /**
     * <p>isExpanded.</p>
     *
     * @param parameters a {@link java.util.Map} object.
     * @return a boolean.
     */
    protected boolean isExpanded(Map<String,String> parameters)
    {
        String all = parameters.get("expanded");
        return all != null && Boolean.valueOf(all);
    }

    /**
     * <p>getErrorView.</p>
     *
     * @param macroId a {@link java.lang.String} object.
     * @param errorId a {@link java.lang.String} object.
     * @return a {@link java.lang.String} object.
     */
    public static String getErrorView(String macroId, String errorId)
    {
        return getErrorView(macroId, errorId, null);
    }
    
    /**
     * <p>getErrorView.</p>
     *
     * @param macroId a {@link java.lang.String} object.
     * @param errorId a {@link java.lang.String} object.
     * @param message a {@link java.lang.String} object.
     * @return a {@link java.lang.String} object.
     */
    public static String getErrorView(String macroId, String errorId, String message)
    {
        Map<String,Object> contextMap = MacroUtils.defaultVelocityContext();
        contextMap.put("macroId", macroId);
        contextMap.put("errorId", errorId);
        contextMap.put("errorMessage", message != null ? message : "");
        return VelocityUtils.getRenderedTemplate("/templates/greenpepper/confluence/macros/greenPepperMacros-error.vm", contextMap);
    }

	private void checkSpace(Space space) throws GreenPepperServerException 
	{
		if(!gpUtil.canView(space))
            throw new GreenPepperServerException("greenpepper.macros.insufficientprivileges", ""); 

        String msg = gpUtil.enable(space.getKey());
        if(msg != null)
            throw new GreenPepperServerException("< " + space.getKey() + " > " + msg, "");
	}
}
