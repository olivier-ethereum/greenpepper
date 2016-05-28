package com.greenpepper.confluence.macros;

import java.util.Map;

import com.atlassian.confluence.renderer.radeox.macros.MacroUtils;
import com.atlassian.confluence.util.velocity.VelocityUtils;
import com.atlassian.renderer.RenderContext;

public class GreenPepperGroup extends AbstractGreenPepperMacro
{
    public boolean isInline()
    {
        return true;
    }
    
    @SuppressWarnings("unchecked")
    public String execute(Map parameters, String body, RenderContext renderContext) 
    {
        try
        {
            Map contextMap = MacroUtils.defaultVelocityContext();
            contextMap.put("title", (String)parameters.get("title"));
            contextMap.put("bulkUID", getBulkUID(parameters));
            
            return VelocityUtils.getRenderedTemplate("/templates/greenpepper/confluence/macros/greenPepperGroup.vm", contextMap);
        }
        catch (Exception e)
        {
            return getErrorView("greenpepper.group.macroid", e.getMessage());
        }
    }
}