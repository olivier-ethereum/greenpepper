package com.greenpepper.samples.application.bank;

/**
 * <p>LimitOfWithdrawal class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class LimitOfWithdrawal
{	
	private AccountType accountType; 	 
	private WithdrawType withdrawType;
	
	/**
	 * <p>Getter for the field <code>accountType</code>.</p>
	 *
	 * @return a {@link com.greenpepper.samples.application.bank.AccountType} object.
	 */
	public AccountType getAccountType() 
	{
		return accountType;
	}

	/**
	 * <p>Setter for the field <code>accountType</code>.</p>
	 *
	 * @param accountType a {@link com.greenpepper.samples.application.bank.AccountType} object.
	 */
	public void setAccountType(AccountType accountType) 
	{
		this.accountType = accountType;
	}

	/**
	 * <p>Getter for the field <code>withdrawType</code>.</p>
	 *
	 * @return a {@link com.greenpepper.samples.application.bank.WithdrawType} object.
	 */
	public WithdrawType getWithdrawType() 
	{
		return withdrawType;
	}

	/**
	 * <p>Setter for the field <code>withdrawType</code>.</p>
	 *
	 * @param withdrawType a {@link com.greenpepper.samples.application.bank.WithdrawType} object.
	 */
	public void setWithdrawType(WithdrawType withdrawType) 
	{
		this.withdrawType = withdrawType;
	}

	/**
	 * <p>getLimit.</p>
	 *
	 * @return a {@link com.greenpepper.samples.application.bank.Money} object.
	 */
	public Money getLimit() 
	{
		return accountType.limitFor(withdrawType);
	}

}
