package com.greenpepper.confluence.macros.migrator;

import com.atlassian.confluence.content.render.xhtml.ConversionContext;
import com.atlassian.confluence.macro.xhtml.MacroMigration;
import com.atlassian.confluence.xhtml.api.MacroDefinition;

public class GreenPepperChildrenMigrator implements MacroMigration {

	public MacroDefinition migrate(MacroDefinition macroDefinition,
			ConversionContext context) {

		return macroDefinition;
	}
}
