package com.greenpepper.confluence.macros;

import java.util.Map;

import com.atlassian.confluence.macro.Macro.BodyType;
import com.atlassian.confluence.macro.Macro.OutputType;
import com.atlassian.confluence.renderer.radeox.macros.MacroUtils;
import com.atlassian.confluence.util.velocity.VelocityUtils;
import com.atlassian.renderer.RenderContext;
import com.atlassian.renderer.v2.RenderMode;
import com.atlassian.renderer.v2.macro.MacroException;

/**
 * <p>GreenPepperExample class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class GreenPepperExample extends AbstractGreenPepperMacro
{
    /**
     * <p>hasBody.</p>
     *
     * @return a boolean.
     */
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
        return BodyType.RICH_TEXT;
    }
     
    /** {@inheritDoc} */
    @Override
    public OutputType getOutputType()
    {
        return OutputType.BLOCK;
    }
    
    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    public String execute(Map parameters, String body, RenderContext renderContext) throws MacroException 
    {
        try
        {
            Map contextMap = MacroUtils.defaultVelocityContext();
            contextMap.put("body", body);
            contextMap.put("title", (String) parameters.get("title"));
            contextMap.put("bgcolor", getHexColor((String) parameters.get("bgcolor")));
            return VelocityUtils.getRenderedTemplate("/templates/greenpepper/confluence/macros/greenPepperTest.vm", contextMap);
        }
        catch (Exception e)
        {
            return getErrorView("greenpepper.test.macroid", e.getMessage());
        }
    }
    
    private String getHexColor(String color)
    {
    	if(color == null) return null;
    	if(color.equals("red")) return "#FF0000";
    	if(color.equals("blue")) return "#0000FF";
    	if(color.equals("grey")) return "#FFFF00";
    	if(color.equals("lightgrey")) return "#D8D8D8";
    	if(color.equals("yellow")) return "#C0C0C0";
    	if(color.equals("green")) return "#00FF00";
    	return color;
    }
}
