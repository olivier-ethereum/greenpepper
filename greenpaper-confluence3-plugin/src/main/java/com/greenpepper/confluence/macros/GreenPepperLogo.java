package com.greenpepper.confluence.macros;

import java.util.Map;

import com.atlassian.confluence.renderer.radeox.macros.MacroUtils;
import com.atlassian.confluence.util.velocity.VelocityUtils;
import com.atlassian.renderer.RenderContext;
import com.atlassian.renderer.v2.RenderMode;
import com.atlassian.renderer.v2.macro.MacroException;

public class GreenPepperLogo extends AbstractGreenPepperMacro
{
    public boolean isInline()
    {
        return true;
    }
    
    public boolean hasBody()
    {
        return false;
    }
    
    public RenderMode getBodyRenderMode()
    {
        return RenderMode.NO_RENDER;
    }
    
    @SuppressWarnings("unchecked")
    public String execute(Map parameters, String body, RenderContext renderContext) throws MacroException 
    {
        try
        {
            Map contextMap = MacroUtils.defaultVelocityContext();
            return VelocityUtils.getRenderedTemplate("/templates/greenpepper/confluence/macros/greenPepperLogo.vm", contextMap);
        }
        catch (Exception e)
        {
            return getErrorView("greenpepper.logo.macroid", e.getMessage());
        }
    }
}