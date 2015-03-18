package com.greenpepper.confluence.macros;

import java.util.Map;

import com.atlassian.confluence.renderer.radeox.macros.MacroUtils;
import com.atlassian.confluence.util.velocity.VelocityUtils;
import com.atlassian.renderer.RenderContext;
import com.atlassian.renderer.v2.macro.MacroException;
import com.greenpepper.confluence.actions.execution.LabelExecutionAction;
import com.greenpepper.confluence.utils.MacroCounter;
import com.greenpepper.server.GreenPepperServerException;

public class GreenPepperLabels extends AbstractGreenPepperMacro
{
    @SuppressWarnings("unchecked")
    public String execute(Map parameters, String body, RenderContext renderContext) throws MacroException 
    {
        String view;
        try
        {      	
        	Map contextMap = MacroUtils.defaultVelocityContext();
            contextMap.put("title", (String)parameters.get("title"));
        	
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
        	contextMap.put("openInSameWindow", openInSameWindow);
            
            String spaceKey = getSpaceKey(parameters);
            String labels = (String)parameters.get("labels");
            
            if(spaceKey != null && labels != null)
            {
                view = "/templates/greenpepper/confluence/macros/greenPepperList.vm";
                LabelExecutionAction action = new LabelExecutionAction();
                action.setBulkUID(getBulkUID(parameters));
                action.setExecutionUID("LABEL_" + MacroCounter.instance().getNextCount());
                action.setForcedSuts((String)parameters.get("suts"));
                action.setShowList(isExpanded(parameters));
                action.setSpaceKey(spaceKey);
                action.setLabels(labels);
                action.setSortType((String) parameters.get("sort"));
                action.setReverse(Boolean.valueOf((String) parameters.get("reverse")).booleanValue());
                
                contextMap.put("action", action);
                contextMap.put("view", "/templates/greenpepper/confluence/execution/label-execution.vm");
                
            }
            else
            {
                view = "/templates/greenpepper/confluence/macros/greenPepperLabels-search.vm";
                contextMap.put("executionUID", "LABEL_SEARCH_" + MacroCounter.instance().getNextCount());
                contextMap.put("forcedSuts", (String)parameters.get("suts"));
                contextMap.put("sortType", (String) parameters.get("sort"));
                contextMap.put("bulkUID", getBulkUID(parameters));
                contextMap.put("spaceKey", spaceKey);
                contextMap.put("spaces", getSpaces());
				contextMap.put("currentSpaceKey", getSpaceKey(parameters, renderContext, false));
			}
            
            return VelocityUtils.getRenderedTemplate(view, contextMap);
        }
        catch (GreenPepperServerException gpe)
        {
            return getErrorView("greenpepper.labels.macroid", gpe.getId());
        }
        catch (Exception e)
        {
            return getErrorView("greenpepper.labels.macroid", e.getMessage());
        }
    }
}