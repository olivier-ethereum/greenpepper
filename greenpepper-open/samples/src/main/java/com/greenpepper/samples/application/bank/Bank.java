package com.greenpepper.samples.application.bank;

import java.util.Collection;
import java.util.HashMap;
import java.util.Collections;
import java.util.Map;

import com.google.inject.Singleton;

@Singleton
/**
 * <p>Bank class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class Bank
{
    private Map<String, BankAccount> accounts;

    /**
     * <p>Constructor for Bank.</p>
     */
    public Bank()
    {
        accounts = new HashMap<String, BankAccount>();
    }

    /**
     * <p>hasAccount.</p>
     *
     * @param accountNumber a {@link java.lang.String} object.
     * @return a boolean.
     */
    public boolean hasAccount(String accountNumber)
    {
        return accounts.containsKey(accountNumber);
    }

    /**
     * <p>getAccount.</p>
     *
     * @param accountNumber a {@link java.lang.String} object.
     * @return a {@link com.greenpepper.samples.application.bank.BankAccount} object.
     * @throws com.greenpepper.samples.application.bank.NoSuchAccountException if any.
     */
    public BankAccount getAccount(String accountNumber) throws NoSuchAccountException
    {
         if (!hasAccount(accountNumber))
             throw new NoSuchAccountException(accountNumber);
         return accounts.get(accountNumber);
    }

    /**
     * <p>openSavingsAccount.</p>
     *
     * @param number a {@link java.lang.String} object.
     * @param owner a {@link com.greenpepper.samples.application.bank.Owner} object.
     * @return a {@link com.greenpepper.samples.application.bank.SavingsAccount} object.
     */
    public SavingsAccount openSavingsAccount(String number, Owner owner)
    {
        if (hasAccount(number)) return null;

        SavingsAccount account = new SavingsAccount(number, owner);
        accounts.put(number, account);
        return account;
    }

    /**
     * <p>openCheckingAccount.</p>
     *
     * @param number a {@link java.lang.String} object.
     * @param owner a {@link com.greenpepper.samples.application.bank.Owner} object.
     * @return a {@link com.greenpepper.samples.application.bank.CheckingAccount} object.
     */
    public CheckingAccount openCheckingAccount(String number, Owner owner)
    {
        if (hasAccount(number)) return null;

        CheckingAccount account = new CheckingAccount(number, owner);
        accounts.put(number, account);
        return account;
    }

	/**
	 * <p>deposit.</p>
	 *
	 * @param amount a {@link com.greenpepper.samples.application.bank.Money} object.
	 * @param number a {@link java.lang.String} object.
	 * @return a {@link com.greenpepper.samples.application.bank.Money} object.
	 * @throws java.lang.Exception if any.
	 */
	public Money deposit(Money amount, String number) throws Exception
	{
		BankAccount account = accounts.get(number);
        return account.deposit(amount);
	}

	/**
	 * <p>withdraw.</p>
	 *
	 * @param amount a {@link com.greenpepper.samples.application.bank.Money} object.
	 * @param number a {@link java.lang.String} object.
	 * @param type a {@link com.greenpepper.samples.application.bank.WithdrawType} object.
	 * @return a {@link com.greenpepper.samples.application.bank.Money} object.
	 * @throws java.lang.Exception if any.
	 */
	public Money withdraw(Money amount, String number, WithdrawType type) throws Exception
	{
        BankAccount account = accounts.get(number);
        return account.withdraw(amount, type);
	}

    /**
     * <p>freezeAccount.</p>
     *
     * @param number a {@link java.lang.String} object.
     */
    public void freezeAccount(String number)
    {
        BankAccount account = accounts.get(number);
        account.freeze();
    }

	/**
	 * <p>Getter for the field <code>accounts</code>.</p>
	 *
	 * @return a {@link java.util.Collection} object.
	 */
	public Collection<BankAccount> getAccounts()
    {
		return Collections.unmodifiableCollection( accounts.values() );
	}
	
	/**
	 * <p>transfer.</p>
	 *
	 * @param numberFrom a {@link java.lang.String} object.
	 * @param numberTo a {@link java.lang.String} object.
	 * @param amountToTransfert a {@link com.greenpepper.samples.application.bank.Money} object.
	 * @throws java.lang.Exception if any.
	 */
	public void transfer(String numberFrom, String numberTo, Money amountToTransfert) throws Exception
	{
		if(!hasAccount( numberFrom ))
			throw new NoSuchAccountException( numberFrom );
		if(!hasAccount( numberTo ))
			throw new NoSuchAccountException( numberTo );
		
		BankAccount accountFrom=accounts.get( numberFrom );
		BankAccount accountTo=accounts.get( numberTo );
		
		if(accountFrom.getOwner().getFirstName().equals( accountTo.getOwner().getFirstName() )
				&& accountFrom.getOwnerName().equals( accountTo.getOwnerName() ))
		{
			accountFrom.withdraw( amountToTransfert );
			accountTo.deposit( amountToTransfert );
		}
		else
		{
			throw new Exception( "Can't transfer from not owned account !" );
		}
	}
}
