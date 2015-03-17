package com.greenpepper.confluence.macros;

import java.util.Map;

import com.atlassian.confluence.renderer.radeox.macros.MacroUtils;
import com.atlassian.confluence.util.velocity.VelocityUtils;
import com.atlassian.renderer.RenderContext;
import com.greenpepper.confluence.actions.execution.ChildrenExecutionAction;
import com.greenpepper.confluence.utils.MacroCounter;
import com.greenpepper.server.GreenPepperServerException;

public class GreenPepperChildren extends AbstractGreenPepperMacro
{
    public boolean isInline()
    {
        return false;
    }

    @SuppressWarnings("unchecked")
    public String execute(Map parameters, String body, RenderContext renderContext)
    {
        try
        {
        	boolean openInSameWindow;
        	String boolValueOpenInSameWindow = (String) parameters.get("openInSameWindow");
        	if(boolValueOpenInSameWindow == null)
        	{
        		openInSameWindow = false;
        	}
        	else
        	{
        		openInSameWindow = Boolean.valueOf(boolValueOpenInSameWindow);
        	}
        		    	
        	String view = "/templates/greenpepper/confluence/macros/greenPepperList.vm";
            Map contextMap = MacroUtils.defaultVelocityContext();
            contextMap.put("title", (String)parameters.get("title"));
            
            String spaceKey = getSpaceKey(parameters, renderContext, true);
            boolean allChildren = withAllChildren(parameters);
            ChildrenExecutionAction action = new ChildrenExecutionAction();
            action.setBulkUID(getBulkUID(parameters));
            action.setExecutionUID("CHILDREN_" + (allChildren ? "ALL_" :"") + MacroCounter.instance().getNextCount());
            action.setSpaceKey(spaceKey);
            action.setPage(getPage(parameters, renderContext, spaceKey));
            action.setShowList(isExpanded(parameters));
            action.setForcedSuts((String)parameters.get("suts"));
            action.setAllChildren(allChildren);
            action.setSortType((String) parameters.get("sort"));
            action.setReverse(Boolean.valueOf((String) parameters.get("reverse")).booleanValue());
            
            contextMap.put("openInSameWindow", openInSameWindow);
            contextMap.put("action", action);
            contextMap.put("view", "/templates/greenpepper/confluence/execution/children-execution.vm");
            
            return VelocityUtils.getRenderedTemplate(view, contextMap);
        }
        catch (GreenPepperServerException gpe)
        {
            return getErrorView("greenpepper.children.macroid", gpe.getId());
        }
        catch (Exception e)
        {
            return getErrorView("greenpepper.children.macroid", e.getMessage());
        }
    }
    
    private boolean withAllChildren(Map parameters)
    {
        String all = (String)parameters.get("all");
        return all != null && Boolean.valueOf(all);
    }
}
