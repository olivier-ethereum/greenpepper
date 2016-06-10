package com.greenpepper.samples.application.mortgage;

/**
 * <p>MortgageBalanceCalculator class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public final class MortgageBalanceCalculator
{
    /** Constant <code>MAX_NOTARY_FEE=1200</code> */
    public static final float MAX_NOTARY_FEE = 1200;
    /** Constant <code>MIN_CASH_DOWN_RATIO=0.25f</code> */
    public static final float MIN_CASH_DOWN_RATIO = 0.25f;
    /** Constant <code>MIN_CASH_DOWN_FEE_RATIO=0.025f</code> */
    public static final float MIN_CASH_DOWN_FEE_RATIO = 0.025f;
    /** Constant <code>MAX_MORTGAGE_RATIO=0.75f</code> */
    public static final float MAX_MORTGAGE_RATIO = 0.75f;

    private MortgageBalanceCalculator() {}

    /**
     * <p>isInsufficient.</p>
     *
     * @param morgageAllowance a float.
     * @param cashDown a float.
     * @return a boolean.
     */
    public static boolean isInsufficient(float morgageAllowance, float cashDown)
    {
        return (cashDown / morgageAllowance) < MIN_CASH_DOWN_RATIO;
    }

    /**
     * <p>evaluate.</p>
     *
     * @param mortgageAllowance a float.
     * @param cashDown a float.
     * @return a float.
     */
    public static float evaluate(float mortgageAllowance, float cashDown)
    {
        float mortgageBalance = mortgageAllowance - cashDown;
        if(isInsufficient(mortgageAllowance, cashDown))
        {
            mortgageBalance = mortgageBalance * ( 1 + MIN_CASH_DOWN_FEE_RATIO );
        }
        
        return mortgageBalance;
    }

    /**
     * <p>reevaluateBankPayYourBillsOption.</p>
     *
     * @param mortgageAllowance a float.
     * @param notaryFees a float.
     * @param bienvenueTax a float.
     * @return a float.
     */
    public static float reevaluateBankPayYourBillsOption(float mortgageAllowance, float notaryFees, float bienvenueTax)
    {
        return mortgageAllowance + getPayedNotaryFee(notaryFees) + bienvenueTax;
    }

    /**
     * <p>getMortgageAllowance.</p>
     *
     * @param commercialValue a float.
     * @param purchasedPrice a float.
     * @return a float.
     */
    public static float getMortgageAllowance(float commercialValue, float purchasedPrice)
    {
        return Math.min(commercialValue * MAX_MORTGAGE_RATIO, purchasedPrice);
    }

    /**
     * <p>getPayedNotaryFee.</p>
     *
     * @param bigNotaryFee a float.
     * @return a float.
     */
    public static float getPayedNotaryFee(float bigNotaryFee)
    {
        return Math.min(MAX_NOTARY_FEE, bigNotaryFee);
    }
}
