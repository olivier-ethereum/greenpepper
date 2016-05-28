package com.greenpepper.extensions.selenium;

import com.greenpepper.reflect.Message;
import com.thoughtworks.selenium.CommandProcessor;

/**
 * <p>SeleniumCommand class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class SeleniumCommand extends Message {

	private String commandName;
	
	private CommandProcessor commandProcessor;
	
	/**
	 * <p>Constructor for SeleniumCommand.</p>
	 *
	 * @param commandProcessor a {@link com.thoughtworks.selenium.CommandProcessor} object.
	 * @param commandName a {@link java.lang.String} object.
	 */
	public SeleniumCommand(CommandProcessor commandProcessor, String commandName) {
		this.commandProcessor = commandProcessor;
		this.commandName = commandName;
	}

	/** {@inheritDoc} */
	@Override
	public int getArity() {
		return 0;
	}

	/** {@inheritDoc} */
	@Override
	public String send(String... args) throws Exception {
		return commandProcessor.doCommand(commandName, args);
	}

}
