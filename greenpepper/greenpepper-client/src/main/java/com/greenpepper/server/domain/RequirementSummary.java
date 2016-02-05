package com.greenpepper.server.domain;

import static com.greenpepper.server.rpc.xmlrpc.XmlRpcDataMarshaller.SUMMARY_ERRORS_IDX;
import static com.greenpepper.server.rpc.xmlrpc.XmlRpcDataMarshaller.SUMMARY_EXCEPTION_IDX;
import static com.greenpepper.server.rpc.xmlrpc.XmlRpcDataMarshaller.SUMMARY_FAILIURES_IDX;
import static com.greenpepper.server.rpc.xmlrpc.XmlRpcDataMarshaller.SUMMARY_SUCCESS_IDX;
import static com.greenpepper.server.rpc.xmlrpc.XmlRpcDataMarshaller.SUMMARY_REFERENCES_IDX;

import java.util.Vector;

/**
 * <p>RequirementSummary class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class RequirementSummary implements Marshalizable
{
    private int referencesSize = 0;
    private int success = 0;
    private int errors = 0;
    private int failures = 0;
    private int exceptions = 0;
    
    /**
     * <p>Constructor for RequirementSummary.</p>
     */
    public RequirementSummary(){ }

    /**
     * <p>Getter for the field <code>errors</code>.</p>
     *
     * @return a int.
     */
    public int getErrors()
    {
        return errors;
    }

    /**
     * <p>Setter for the field <code>errors</code>.</p>
     *
     * @param errors a int.
     */
    public void setErrors(int errors)
    {
        this.errors = errors;
    }

    /**
     * <p>Getter for the field <code>exceptions</code>.</p>
     *
     * @return a int.
     */
    public int getExceptions()
    {
        return exceptions;
    }

    /**
     * <p>Setter for the field <code>exceptions</code>.</p>
     *
     * @param exceptions a int.
     */
    public void setExceptions(int exceptions)
    {
        this.exceptions = exceptions;
    }

    /**
     * <p>Getter for the field <code>failures</code>.</p>
     *
     * @return a int.
     */
    public int getFailures()
    {
        return failures;
    }

    /**
     * <p>Setter for the field <code>failures</code>.</p>
     *
     * @param failures a int.
     */
    public void setFailures(int failures)
    {
        this.failures = failures;
    }

    /**
     * <p>Getter for the field <code>success</code>.</p>
     *
     * @return a int.
     */
    public int getSuccess()
    {
        return success;
    }

    /**
     * <p>Setter for the field <code>success</code>.</p>
     *
     * @param success a int.
     */
    public void setSuccess(int success)
    {
        this.success = success;
    }

    /**
     * <p>Getter for the field <code>referencesSize</code>.</p>
     *
     * @return a int.
     */
    public int getReferencesSize()
    {
        return referencesSize;
    }

    /**
     * <p>Setter for the field <code>referencesSize</code>.</p>
     *
     * @param referencesSize a int.
     */
    public void setReferencesSize(int referencesSize)
    {
        this.referencesSize = referencesSize;
    }

    /**
     * <p>addErrors.</p>
     *
     * @param errors a int.
     */
    public void addErrors(int errors)
    {
        this.errors += errors;
    }

    /**
     * <p>addSuccess.</p>
     *
     * @param success a int.
     */
    public void addSuccess(int success)
    {
        this.success += success;
    }

    /**
     * <p>addFailures.</p>
     *
     * @param failures a int.
     */
    public void addFailures(int failures)
    {
        this.failures += failures;
    }

    /**
     * <p>addException.</p>
     *
     * @param hasException a boolean.
     */
    public void addException(boolean hasException)
    {
        if(hasException)exceptions++;
    }
    
    /**
     * <p>getTestsIgnored.</p>
     *
     * @return a boolean.
     */
    public boolean getTestsIgnored()
    {
        return success == 0 && !getTestsFailed();
    }
    
    /**
     * <p>getTestsFailed.</p>
     *
     * @return a boolean.
     */
    public boolean getTestsFailed()
    {
        return failures + errors + exceptions > 0;
    }
    
    /**
     * <p>getTestsSucceeded.</p>
     *
     * @return a boolean.
     */
    public boolean getTestsSucceeded()
    {
        return success > 0 && !getTestsFailed();
    }

    /**
     * <p>marshallize.</p>
     *
     * @return a {@link java.util.Vector} object.
     */
    public Vector<Object> marshallize()
    {
        Vector<Object> parameters = new Vector<Object>();
        parameters.add(SUMMARY_REFERENCES_IDX, referencesSize);
        parameters.add(SUMMARY_FAILIURES_IDX, failures);
        parameters.add(SUMMARY_ERRORS_IDX, errors);
        parameters.add(SUMMARY_SUCCESS_IDX, success);
        parameters.add(SUMMARY_EXCEPTION_IDX, exceptions);
        return parameters;
    }
}
