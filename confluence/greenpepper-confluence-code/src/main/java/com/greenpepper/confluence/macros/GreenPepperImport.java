package com.greenpepper.confluence.macros;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.atlassian.confluence.renderer.radeox.macros.MacroUtils;
import com.atlassian.confluence.util.velocity.VelocityUtils;
import com.atlassian.renderer.RenderContext;
import com.atlassian.renderer.v2.macro.MacroException;
import com.greenpepper.confluence.utils.MacroParametersUtils;
import com.greenpepper.confluence.velocity.ConfluenceGreenPepper;

/**
 * <p>GreenPepperImport class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class GreenPepperImport extends AbstractGreenPepperMacro
{
	/** Constant <code>IMPORTS_PARAM="imports"</code> */
	public static final String IMPORTS_PARAM = "imports";

	/** {@inheritDoc} */
	@SuppressWarnings("unchecked")
    public String execute(Map parameters, String body, RenderContext renderContext) throws MacroException 
	{
        try
        {
            Map contextMap = MacroUtils.defaultVelocityContext();
            contextMap.put(IMPORTS_PARAM, getImportList(parameters));
            return VelocityUtils.getRenderedTemplate("/templates/greenpepper/confluence/macros/greenPepperImport.vm", contextMap);
        }
        catch (Exception e)
        {
            return getErrorView("greenpepper.import.macroid", e.getMessage());
        }
    }
    
    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    public static String getErrorView(String macroId, String errorId)
    {
        Map contextMap = MacroUtils.defaultVelocityContext();
        contextMap.put("macroId", macroId);
        contextMap.put("errorId", errorId);
        return VelocityUtils.getRenderedTemplate("/templates/greenpepper/confluence/macros/greenPepperMacros-error.vm", contextMap);
    }

    private List<String> getImportList(Map<String,String> parameters)
    {
        List<String> imports = new ArrayList<String>();
        
        // v3
        int index = 0;
        String importParam = (String)parameters.get(String.valueOf(index));
        while(importParam != null)
        {
            imports.add(ConfluenceGreenPepper.clean(importParam));
            importParam = (String)parameters.get(String.valueOf(++index));
        }
        
        // v4
        String[] values = MacroParametersUtils.extractParameterMultiple(IMPORTS_PARAM, parameters);
        for (String value : values)
        if (values != null) {
        	imports.add(ConfluenceGreenPepper.clean(value));
        }
        
        return imports;
    }

    
}
