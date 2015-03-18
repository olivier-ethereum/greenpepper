package com.greenpepper.confluence.macros.migrator;

import com.atlassian.confluence.content.render.xhtml.ConversionContext;
import com.atlassian.confluence.content.render.xhtml.definition.MacroBody;
import com.atlassian.confluence.content.render.xhtml.definition.RichTextMacroBody;
import com.atlassian.confluence.macro.xhtml.MacroMigration;
import com.atlassian.confluence.renderer.PageContext;
import com.atlassian.confluence.xhtml.api.MacroDefinition;
import com.atlassian.renderer.WikiStyleRenderer;
import com.atlassian.spring.container.ContainerManager;

public class GreenPepperMacroWithBodyMigrator implements MacroMigration {

	public MacroDefinition migrate(MacroDefinition macroDefinition,
			ConversionContext context) {
		MacroBody body = macroDefinition.getBody();
		
		WikiStyleRenderer wikiStyleRenderer = (WikiStyleRenderer) ContainerManager.getComponent("wikiStyleRenderer");
		String content= wikiStyleRenderer.convertWikiToXHtml(new PageContext(context.getEntity()), body.getBody());
		
		MacroBody newBody = new RichTextMacroBody(content);
		macroDefinition.setBody(newBody);
		
		return macroDefinition;
	}

}
