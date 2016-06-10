package com.greenpepper.samples.application.bank;

/**
 * <p>WithdrawType class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public enum WithdrawType
{
	 ATM ("ATM"),
	 INTERAC("Interact"),
	 PERSONAL_CHECK ("Personal Check");

	private final String description ;

	private WithdrawType (String description)
	{
		this.description = description;
	}

	/** {@inheritDoc} */
	@Override
	public String toString()
	{
		return description;
	}
}
