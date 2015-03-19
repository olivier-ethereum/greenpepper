package com.greenpepper.confluence.macros.migrator;

import java.util.HashMap;
import java.util.Map;

import com.atlassian.confluence.content.render.xhtml.ConversionContext;
import com.atlassian.confluence.macro.xhtml.MacroMigration;
import com.atlassian.confluence.xhtml.api.MacroDefinition;
import com.greenpepper.confluence.macros.GreenPepperImport;
import com.greenpepper.confluence.velocity.ConfluenceGreenPepper;

public class GreenPepperImportMigrator implements MacroMigration {

	public MacroDefinition migrate(MacroDefinition macroDefinition,
			ConversionContext context) {
		final String imports = getV3Imports(macroDefinition);
		
		Map<String, String> params = new HashMap<String, String>(1) {
			{
				put(GreenPepperImport.IMPORTS_PARAM, imports);
			}
		};
		macroDefinition.setParameters(params);

		return macroDefinition;
	}

	private String getV3Imports(MacroDefinition macroDefinition) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(macroDefinition.getDefaultParameterValue());
		int index = 1;
		Map<String, String> parameters = macroDefinition.getParameters();
		String importParam = (String)parameters.get(""+index);
		
		while(importParam != null)
		{
			buffer.append(",");
			buffer.append(ConfluenceGreenPepper.clean(importParam));
			importParam = (String)parameters.get(""+ ++index);
		}
		return buffer.toString();
	}
}
