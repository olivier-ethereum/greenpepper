package com.greenpepper.samples.application.bank;


/**
 * <p>CheckingAccount class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class CheckingAccount extends BankAccount
{
    private Money maxCredit = Money.ZERO;
    
    /**
     * <p>Constructor for CheckingAccount.</p>
     *
     * @param number a {@link java.lang.String} object.
     * @param owner a {@link com.greenpepper.samples.application.bank.Owner} object.
     */
    public CheckingAccount(String number, Owner owner)
    {
        super(AccountType.CHECKING, number, owner);
    }
    
    /** {@inheritDoc} */
    public void checkFunds(Money amount) throws Exception
    {
        if (getBalance().plus(maxCredit).lowerThan(amount)) throw new Exception("Not enougth credit !");
    }
    
    /**
     * <p>setCreditLine.</p>
     *
     * @param credit a {@link com.greenpepper.samples.application.bank.Money} object.
     */
    public void setCreditLine(Money credit)
    {
        this.maxCredit = credit;
    }

    /**
     * <p>limitFor.</p>
     *
     * @param type a {@link com.greenpepper.samples.application.bank.WithdrawType} object.
     * @return a {@link com.greenpepper.samples.application.bank.Money} object.
     */
    public Money limitFor(WithdrawType type)
    {
    	return AccountType.CHECKING.limitFor(type);
    }

}
