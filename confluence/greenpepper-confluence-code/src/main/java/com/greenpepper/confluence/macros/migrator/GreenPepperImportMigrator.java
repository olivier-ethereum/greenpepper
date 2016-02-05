package com.greenpepper.confluence.macros.migrator;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atlassian.confluence.content.render.xhtml.ConversionContext;
import com.atlassian.confluence.content.render.xhtml.definition.MacroBody;
import com.atlassian.confluence.content.render.xhtml.definition.PlainTextMacroBody;
import com.atlassian.confluence.macro.xhtml.MacroMigration;
import com.atlassian.confluence.xhtml.api.MacroDefinition;
import com.greenpepper.confluence.macros.GreenPepperImport;
import com.greenpepper.confluence.velocity.ConfluenceGreenPepper;

/**
 * <p>GreenPepperImportMigrator class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class GreenPepperImportMigrator implements MacroMigration {

    private static final Logger LOGGER = LoggerFactory.getLogger(GreenPepperImportMigrator.class);
    
	/** {@inheritDoc} */
	public MacroDefinition migrate(MacroDefinition macroDefinition,
			ConversionContext context) {
	    LOGGER.debug("Beginning migration of macro {} ", macroDefinition);
		final String imports = getV3Imports(macroDefinition);
		LOGGER.debug("Migrated Parameters to : {}", imports);
		
		Map<String, String> params = new HashMap<String, String>();
		params.put(GreenPepperImport.IMPORTS_PARAM, imports);
		
		macroDefinition.setParameters(params);
		MacroBody macroBody = new PlainTextMacroBody(imports.replaceAll(",", "\n"));
		macroDefinition.setBody(macroBody);
		return macroDefinition;
	}

	private String getV3Imports(MacroDefinition macroDefinition) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(macroDefinition.getDefaultParameterValue());
		int index = 1;
		Map<String, String> parameters = macroDefinition.getParameters();
		String importParam = (String)parameters.get(String.valueOf(index));
		
		while(importParam != null)
		{
		    LOGGER.debug("got value {}", importParam);
			buffer.append(",");
			buffer.append(ConfluenceGreenPepper.clean(importParam));
			importParam = (String)parameters.get(String.valueOf(++index));
		}
		return buffer.toString();
	}
}
