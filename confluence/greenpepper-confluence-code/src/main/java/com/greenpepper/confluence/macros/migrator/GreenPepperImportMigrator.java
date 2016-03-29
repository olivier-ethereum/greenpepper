package com.greenpepper.confluence.macros.migrator;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
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
		LOGGER.trace("Migrated Parameters to : {}", imports);
		
		Map<String, String> params = new HashMap<String, String>();
		params.put(GreenPepperImport.IMPORTS_PARAM, imports);

		MacroDefinition newMacroDefinition = new MacroDefinition();
		newMacroDefinition.setName(macroDefinition.getName());
		newMacroDefinition.setParameters(params);
		//MacroBody macroBody = new PlainTextMacroBody(imports.replaceAll(",", "\n"));
		//newMacroDefinition.setBody(macroBody);
		LOGGER.debug("Migrated Macro: {}", newMacroDefinition);
		return newMacroDefinition;
	}

	private String getV3Imports(MacroDefinition macroDefinition) {
		Map<String, String> parameters = macroDefinition.getParameters();
		String newParameters = StringUtils.join(parameters.values(),',');
		return newParameters;
	}
}
