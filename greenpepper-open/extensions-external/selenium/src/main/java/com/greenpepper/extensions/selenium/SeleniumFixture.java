package com.greenpepper.extensions.selenium;

import com.greenpepper.reflect.Fixture;
import com.greenpepper.reflect.Message;
import com.greenpepper.reflect.NoSuchMessageException;
import com.thoughtworks.selenium.CommandProcessor;

/**
 * <p>SeleniumFixture class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class SeleniumFixture implements Fixture {

	private final CommandProcessor processor;
	
	/**
	 * <p>Constructor for SeleniumFixture.</p>
	 *
	 * @param processor a {@link com.thoughtworks.selenium.CommandProcessor} object.
	 */
	public SeleniumFixture(CommandProcessor processor) {
		this.processor = processor;
	}

	/** {@inheritDoc} */
	public boolean canCheck(String message) {
		return message != null;
	}

	/** {@inheritDoc} */
	public boolean canSend(String message) {
		return canCheck(message);
	}

	/** {@inheritDoc} */
	public Message check(String message) throws NoSuchMessageException {
		return new SeleniumCommand(processor, message);
	}

	/** {@inheritDoc} */
	public Fixture fixtureFor(Object target) {
		if(!(target instanceof CommandProcessor))
			throw new IllegalArgumentException("Can only get a SeleniumFixture for an instance of CommandProcessor.");
		
		return new SeleniumFixture((CommandProcessor)target);
	}

	/**
	 * <p>getTarget.</p>
	 *
	 * @return a {@link com.thoughtworks.selenium.CommandProcessor} object.
	 */
	public CommandProcessor getTarget() {
		return processor;
	}

	/** {@inheritDoc} */
	public Message send(String message) throws NoSuchMessageException {
		return check(message);
	}

}
