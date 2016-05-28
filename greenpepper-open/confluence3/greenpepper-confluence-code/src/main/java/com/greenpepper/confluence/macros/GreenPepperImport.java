package com.greenpepper.confluence.macros;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.atlassian.confluence.renderer.radeox.macros.MacroUtils;
import com.atlassian.confluence.util.velocity.VelocityUtils;
import com.atlassian.renderer.RenderContext;
import com.atlassian.renderer.v2.macro.MacroException;
import com.greenpepper.confluence.velocity.ConfluenceGreenPepper;

public class GreenPepperImport extends AbstractGreenPepperMacro
{        
    @SuppressWarnings("unchecked")
    public String execute(Map parameters, String body, RenderContext renderContext) throws MacroException 
    {
        try
        {
            Map contextMap = MacroUtils.defaultVelocityContext();
            contextMap.put("imports", getImportList(parameters));
            return VelocityUtils.getRenderedTemplate("/templates/greenpepper/confluence/macros/greenPepperImport.vm", contextMap);
        }
        catch (Exception e)
        {
            return getErrorView("greenpepper.import.macroid", e.getMessage());
        }
    }
    
    private List<String> getImportList(Map parameters)
    {
        int index = 0;
        List<String> imports  = new ArrayList<String>();
        String importParam = (String)parameters.get(""+index);
        while(importParam != null)
        {
            imports.add(ConfluenceGreenPepper.clean(importParam));
            importParam = (String)parameters.get(""+ ++index);
        }
        
        return imports;
    }
}
