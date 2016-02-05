package com.greenpepper.samples.application.bank;

/**
 * <p>Abstract BankAccount class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public abstract class BankAccount
{
	private AccountType type;
    private Money balance = Money.ZERO;
    private String number;
    private Owner owner;
    
    public boolean frozen;

    /**
     * <p>Constructor for BankAccount.</p>
     *
     * @param accountType a {@link com.greenpepper.samples.application.bank.AccountType} object.
     * @param number a {@link java.lang.String} object.
     * @param owner a {@link com.greenpepper.samples.application.bank.Owner} object.
     */
    public BankAccount(AccountType accountType, String number, Owner owner)
    {
        this.number = number;
        this.type = accountType;
        this.owner = owner;
    }

    /**
     * <p>checkFunds.</p>
     *
     * @param amount a {@link com.greenpepper.samples.application.bank.Money} object.
     * @throws java.lang.Exception if any.
     */
    public abstract void checkFunds(Money amount) throws Exception;

	/**
	 * <p>withdraw.</p>
	 *
	 * @param amount a {@link com.greenpepper.samples.application.bank.Money} object.
	 * @param withdrawType a {@link com.greenpepper.samples.application.bank.WithdrawType} object.
	 * @return a {@link com.greenpepper.samples.application.bank.Money} object.
	 * @throws java.lang.Exception if any.
	 */
	public Money withdraw(Money amount, WithdrawType withdrawType) throws Exception {

        Money limit = type.limitFor(withdrawType);
		if (!AccountType.isNoLimit(limit) && amount.strictlyGreaterThan(limit))
		{
			throw new Exception("Limit overpassed");
		}
		Money fees = type.feesFor(withdrawType);
		return withdraw(amount.plus(fees));
	}

    /**
     * <p>withdraw.</p>
     *
     * @param amount a {@link com.greenpepper.samples.application.bank.Money} object.
     * @return a {@link com.greenpepper.samples.application.bank.Money} object.
     * @throws java.lang.Exception if any.
     */
    public Money withdraw(Money amount) throws Exception
    {
        checkNotFrozen();
        checkFunds(amount);
        balance = balance.minus(amount);
        return balance;
    }

    private void checkNotFrozen() throws Exception
    {
        if (frozen) throw new Exception("Acccount frozen!");
    }

    /**
     * <p>Getter for the field <code>number</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getNumber()
    {
        return number;
    }

    /**
     * <p>deposit.</p>
     *
     * @param amount a {@link com.greenpepper.samples.application.bank.Money} object.
     * @return a {@link com.greenpepper.samples.application.bank.Money} object.
     */
    public Money deposit(Money amount)
    {
        balance = balance.plus(amount);
        return balance;
    }

    /**
     * <p>Getter for the field <code>balance</code>.</p>
     *
     * @return a {@link com.greenpepper.samples.application.bank.Money} object.
     */
    public Money getBalance()
    {
        return balance;
    }

    /**
     * <p>isFrozen.</p>
     *
     * @return a boolean.
     */
    public boolean isFrozen()
    {
        return frozen;
    }

    /**
     * <p>freeze.</p>
     */
    public void freeze()
    {
        frozen = true;
    }

	/**
	 * <p>Getter for the field <code>type</code>.</p>
	 *
	 * @return a {@link com.greenpepper.samples.application.bank.AccountType} object.
	 */
	public AccountType getType()
    {
		return type;
	}

    /**
     * <p>Getter for the field <code>owner</code>.</p>
     *
     * @return a {@link com.greenpepper.samples.application.bank.Owner} object.
     */
    public Owner getOwner()
    {
        return owner;
    }

    /**
     * <p>getOwnerName.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getOwnerName()
    {
        return owner.getFullName();
    }
}
