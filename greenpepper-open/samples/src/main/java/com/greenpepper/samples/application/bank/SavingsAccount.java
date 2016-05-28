package com.greenpepper.samples.application.bank;

/**
 * <p>SavingsAccount class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class SavingsAccount extends BankAccount
{
    /**
     * <p>Constructor for SavingsAccount.</p>
     *
     * @param number a {@link java.lang.String} object.
     * @param owner a {@link com.greenpepper.samples.application.bank.Owner} object.
     */
    public SavingsAccount(String number, Owner owner)
    {
        super(AccountType.SAVINGS, number, owner);
    }
    
    /** {@inheritDoc} */
    public void checkFunds(Money amount) throws Exception
    {
        if (getBalance().lowerThan(amount)) throw new Exception("Not enougth money !");
    }
}
