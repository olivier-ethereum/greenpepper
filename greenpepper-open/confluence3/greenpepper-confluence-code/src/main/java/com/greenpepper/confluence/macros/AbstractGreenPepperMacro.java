package com.greenpepper.confluence.macros;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.atlassian.confluence.core.ContentEntityObject;
import com.atlassian.confluence.pages.Page;
import com.atlassian.confluence.renderer.PageContext;
import com.atlassian.confluence.renderer.radeox.macros.MacroUtils;
import com.atlassian.confluence.spaces.Space;
import com.atlassian.confluence.util.SpaceComparator;
import com.atlassian.confluence.util.velocity.VelocityUtils;
import com.atlassian.renderer.RenderContext;
import com.atlassian.renderer.v2.RenderMode;
import com.atlassian.renderer.v2.macro.BaseMacro;
import com.greenpepper.confluence.velocity.ConfluenceGreenPepper;
import com.greenpepper.server.GreenPepperServerException;
import com.greenpepper.util.StringUtil;

public abstract class AbstractGreenPepperMacro extends BaseMacro
{
    protected ConfluenceGreenPepper gpUtil = new ConfluenceGreenPepper();
    
    public boolean isInline()
    {
        return false;
    }
    
    public boolean hasBody()
    {
        return false;
    }
    
    public RenderMode getBodyRenderMode()
    {
        return RenderMode.NO_RENDER;
    }
    
    protected String getSpaceKey(@SuppressWarnings("rawtypes") Map parameters) throws GreenPepperServerException
    {
        String spaceKey = (String)parameters.get("spaceKey");
        if(spaceKey != null)
        {
            spaceKey = spaceKey.trim();

			Space space = gpUtil.getSpaceManager().getSpace(spaceKey);
			if(space == null)
				throw new GreenPepperServerException("greenpepper.children.spacenotfound", spaceKey);

			checkSpace(space);
        }
        
        return spaceKey;
    }
    
    protected String getSpaceKey(@SuppressWarnings("rawtypes") Map parameters, RenderContext renderContext, boolean checkPermission) throws GreenPepperServerException
    {
    	Space space;
        String spaceKey = (String)parameters.get("spaceKey");
        if(spaceKey == null)
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

	protected Space getCurrentSpace(RenderContext renderContext)
	{
		ContentEntityObject owner = ((PageContext)renderContext).getEntity();
		return ((Page)owner).getSpace();
	}

	protected String getPageTitle(@SuppressWarnings("rawtypes") Map parameters, RenderContext renderContext, String spaceKey) throws GreenPepperServerException
    {
        return getPage(parameters, renderContext, spaceKey).getTitle().trim();
    }
    
    protected Page getPage(@SuppressWarnings("rawtypes") Map parameters, RenderContext renderContext, String spaceKey) throws GreenPepperServerException
    {
        String pageTitle = (String)parameters.get("pageTitle");
        if(pageTitle == null)
        {
            ContentEntityObject owner = ((PageContext)renderContext).getEntity();
            return (Page)owner;
        }

        Page page = gpUtil.getPageManager().getPage(spaceKey, pageTitle);
        if(page == null)
            throw new GreenPepperServerException("greenpepper.children.pagenotfound", String.format("'%s' in space '%s'", pageTitle, spaceKey));
        
        return page;
    }

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
    
    protected String getBulkUID(@SuppressWarnings("rawtypes") Map parameters)
    {
        String group = (String)parameters.get("group");
        return StringUtil.isEmpty(group) ? "PAGE" : group;
    }
    
    protected boolean isExpanded(@SuppressWarnings("rawtypes") Map parameters)
    {
        String all = (String)parameters.get("expanded");
        return all != null && Boolean.valueOf(all);
    }
    
    public static String getErrorView(String macroId, String errorId)
    {
        return getErrorView(macroId, errorId, null);
    }
    
    public static String getErrorView(String macroId, String errorId, String message) {
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
