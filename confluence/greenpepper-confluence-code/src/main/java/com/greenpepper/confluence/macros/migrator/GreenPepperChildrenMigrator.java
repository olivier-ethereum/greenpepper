package com.greenpepper.confluence.macros.migrator;

import com.atlassian.confluence.content.render.xhtml.ConversionContext;
import com.atlassian.confluence.macro.xhtml.MacroMigration;
import com.atlassian.confluence.xhtml.api.MacroDefinition;

/**
 * <p>GreenPepperChildrenMigrator class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class GreenPepperChildrenMigrator implements MacroMigration {

	/** {@inheritDoc} */
	public MacroDefinition migrate(MacroDefinition macroDefinition,
			ConversionContext context) {

		return macroDefinition;
	}
}
