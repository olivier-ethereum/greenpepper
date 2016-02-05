package com.greenpepper.confluence.macros;

import java.util.Map;

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
    
    /** {@inheritDoc} */
    @Override
    public String execute(@SuppressWarnings("rawtypes") Map parameters, String body, RenderContext renderContext) throws MacroException 
    {
        try
        {
            String xhtmlRendered = gpUtil.getWikiStyleRenderer().convertWikiToXHtml(renderContext, body);
            LOGGER.trace("rendering : \n - source \n {} \n - output \n {}", body, xhtmlRendered);
            return xhtmlRendered;
        }
        catch (Exception e)
        {
            return getErrorView("greenpepper.info.macroid", e.getMessage());
        }
    }
}
