package com.greenpepper.server.domain;

import static com.greenpepper.server.rpc.xmlrpc.XmlRpcDataMarshaller.EXECUTION_ERRORID_IDX;
import static com.greenpepper.server.rpc.xmlrpc.XmlRpcDataMarshaller.EXECUTION_ERRORS_IDX;
import static com.greenpepper.server.rpc.xmlrpc.XmlRpcDataMarshaller.EXECUTION_EXECUTION_DATE_IDX;
import static com.greenpepper.server.rpc.xmlrpc.XmlRpcDataMarshaller.EXECUTION_FAILIURES_IDX;
import static com.greenpepper.server.rpc.xmlrpc.XmlRpcDataMarshaller.EXECUTION_RESULTS_IDX;
import static com.greenpepper.server.rpc.xmlrpc.XmlRpcDataMarshaller.EXECUTION_SUCCESS_IDX;
import static com.greenpepper.server.rpc.xmlrpc.XmlRpcDataMarshaller.EXECUTION_IGNORED_IDX;
import com.greenpepper.server.rpc.xmlrpc.XmlRpcDataMarshaller;

import java.sql.Timestamp;
import java.util.Vector;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Index;

import com.greenpepper.report.XmlReport;
import com.greenpepper.util.FormatedDate;
import com.greenpepper.util.HtmlUtil;
import com.greenpepper.util.StringUtil;

@Entity
/**
 * <p>Execution class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
@Table(name="EXECUTION")
@SuppressWarnings("serial")
public class Execution extends AbstractUniqueEntity implements Comparable
{
    /** Constant <code>NOT_RUNNED="notrunned"</code> */
    public static final String NOT_RUNNED = "notrunned";
    /** Constant <code>IGNORED="ignored"</code> */
    public static final String IGNORED = "ignored";
    /** Constant <code>SUCCESS="success"</code> */
    public static final String SUCCESS = "success";
    /** Constant <code>FAILED="error"</code> */
    public static final String FAILED = "error";

    private Specification specification;
    private SystemUnderTest systemUnderTest;
    private String sections;
    
    private String results;
    private int success = 0;
    private int failures = 0;
    private int errors = 0;
    private int ignored = 0;
    private String executionErrorId;
    private Timestamp executionDate;
    private boolean executedRemotely;

    /**
     * <p>none.</p>
     *
     * @return a {@link com.greenpepper.server.domain.Execution} object.
     */
    public static Execution none()
    {
        return new Execution();
    }
    
    /**
     * <p>error.</p>
     *
     * @param specification a {@link com.greenpepper.server.domain.Specification} object.
     * @param systemUnderTest a {@link com.greenpepper.server.domain.SystemUnderTest} object.
     * @param sections a {@link java.lang.String} object.
     * @param errorId a {@link java.lang.String} object.
     * @return a {@link com.greenpepper.server.domain.Execution} object.
     */
    public static Execution error(Specification specification, SystemUnderTest systemUnderTest, String sections, String errorId)
    {
        Execution execution = new Execution();
        execution.setSystemUnderTest(systemUnderTest);
        execution.setExecutionDate(new Timestamp(System.currentTimeMillis()));
        execution.setSpecification(specification);
        execution.setExecutionErrorId(errorId);
        execution.setSections(sections);
        return execution;
    }
    
    /**
     * <p>newInstance.</p>
     *
     * @param specification a {@link com.greenpepper.server.domain.Specification} object.
     * @param systemUnderTest a {@link com.greenpepper.server.domain.SystemUnderTest} object.
     * @param xmlReport a {@link com.greenpepper.report.XmlReport} object.
     * @return a {@link com.greenpepper.server.domain.Execution} object.
     */
    public static Execution newInstance(Specification specification, SystemUnderTest systemUnderTest, XmlReport xmlReport)
    {
        if(xmlReport.getGlobalException() != null)
        {
            return error(specification, systemUnderTest, null, xmlReport.getGlobalException());
        }
        
        Execution execution = new Execution();
        execution.setExecutionDate(new Timestamp(System.currentTimeMillis()));
        execution.setSpecification(specification);
        execution.setSystemUnderTest(systemUnderTest);
        
        execution.setFailures( xmlReport.getFailure(0) );
        execution.setErrors( xmlReport.getError(0) );
        execution.setSuccess( xmlReport.getSuccess(0) );
        execution.setIgnored( xmlReport.getIgnored(0) );
        if(execution.wasRunned())
        {
            execution.setResults( xmlReport.getResults(0));
        }

		if (xmlReport.getSections(0) != null)
		{
			StringBuilder sections = new StringBuilder();
			int index = 0;

			while (xmlReport.getSections(index) != null)
			{
				if (index > 0) sections.append(',');
				sections.append(xmlReport.getSections(index));
				index++;
			}

			execution.setSections(sections.toString());
		}
        
        return execution;
    }

    /**
     * <p>newInstance.</p>
     *
     * @param specification a {@link com.greenpepper.server.domain.Specification} object.
     * @param systemUnderTest a {@link com.greenpepper.server.domain.SystemUnderTest} object.
     * @param xmlReport a {@link com.greenpepper.report.XmlReport} object.
     * @param sections a {@link java.lang.String} object.
     * @return a {@link com.greenpepper.server.domain.Execution} object.
     */
    public static Execution newInstance(Specification specification, SystemUnderTest systemUnderTest, XmlReport xmlReport, String sections)
    {
        Execution execution = Execution.newInstance(specification, systemUnderTest, xmlReport );
        execution.setSections(sections);
        return execution;
    }

    /**
     * <p>Getter for the field <code>specification</code>.</p>
     *
     * @return a {@link com.greenpepper.server.domain.Specification} object.
     */
    @ManyToOne( cascade = {CascadeType.PERSIST, CascadeType.MERGE} )
    @JoinColumn(name="SPECIFICATION_ID", nullable = false)
    public Specification getSpecification()
    {
        return specification;
    }

    /**
     * <p>Getter for the field <code>systemUnderTest</code>.</p>
     *
     * @return a {@link com.greenpepper.server.domain.SystemUnderTest} object.
     */
    @ManyToOne( cascade = {CascadeType.PERSIST, CascadeType.MERGE} )
    @JoinColumn(name="SUT_ID", nullable = false)
    public SystemUnderTest getSystemUnderTest()
    {
        return systemUnderTest;
    }

    /**
     * <p>Getter for the field <code>sections</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    @Basic
    @Column(name = "SECTIONS", nullable = true, length=50)
    public String getSections()
    {
        return sections;
    }

    /**
     * <p>Getter for the field <code>results</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    @Lob
    @Column(name = "RESULTS", nullable = true, length = 2147483647)
    public String getResults()
    {
        return results;
    }

    /**
     * <p>Getter for the field <code>executionErrorId</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    @Lob
    @Column(name = "ERRORID", nullable = true, length = 2147483647)
    public String getExecutionErrorId()
    {
        return executionErrorId;
    }

    /**
     * <p>Getter for the field <code>success</code>.</p>
     *
     * @return a int.
     */
    @Basic
    @Column(name = "SUCCESS_COUNT")
    public int getSuccess()
    {
        return success;
    }

    /**
     * <p>Getter for the field <code>ignored</code>.</p>
     *
     * @return a int.
     */
    @Basic
    @Column(name = "IGNORED_COUNT")
	public int getIgnored() 
	{
		return ignored;
	}

    /**
     * <p>Getter for the field <code>failures</code>.</p>
     *
     * @return a int.
     */
    @Basic
    @Column(name = "FAILURES_COUNT")
    public int getFailures()
    {
        return failures;
    }

    /**
     * <p>Getter for the field <code>errors</code>.</p>
     *
     * @return a int.
     */
    @Basic
    @Column(name = "ERRORS_COUNT")
    public int getErrors()
    {
        return errors;
    }

    /**
     * <p>Getter for the field <code>executionDate</code>.</p>
     *
     * @return a {@link java.sql.Timestamp} object.
     */
    @Basic
    @Column(name = "EXECUTION_DATE")
    @Index(name="executionDateIndex")
    public Timestamp getExecutionDate()
    {
        return executionDate;
    }

    /**
     * <p>Setter for the field <code>specification</code>.</p>
     *
     * @param specification a {@link com.greenpepper.server.domain.Specification} object.
     */
    public void setSpecification(Specification specification)
    {
        this.specification = specification;
    }

    /**
     * <p>Setter for the field <code>systemUnderTest</code>.</p>
     *
     * @param systemUnderTest a {@link com.greenpepper.server.domain.SystemUnderTest} object.
     */
    public void setSystemUnderTest(SystemUnderTest systemUnderTest)
    {
        this.systemUnderTest = systemUnderTest;
    }

    /**
     * <p>Setter for the field <code>sections</code>.</p>
     *
     * @param sections a {@link java.lang.String} object.
     */
    public void setSections(String sections)
    {
        this.sections = sections;
    }

    /**
     * <p>Setter for the field <code>results</code>.</p>
     *
     * @param results a {@link java.lang.String} object.
     */
    public void setResults(String results)
    {
        this.results = results;
    }

    /**
     * <p>Setter for the field <code>executionErrorId</code>.</p>
     *
     * @param executionErrorId a {@link java.lang.String} object.
     */
    public void setExecutionErrorId(String executionErrorId)
    {
        this.executionErrorId = executionErrorId;
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
	 * <p>Setter for the field <code>ignored</code>.</p>
	 *
	 * @param ignored a int.
	 */
	public void setIgnored(int ignored) 
	{
		this.ignored = ignored;
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
     * <p>Setter for the field <code>errors</code>.</p>
     *
     * @param errors a int.
     */
    public void setErrors(int errors)
    {
        this.errors = errors;
    }
    
    /**
     * <p>Setter for the field <code>executionDate</code>.</p>
     *
     * @param executionDate a {@link java.sql.Timestamp} object.
     */
    public void setExecutionDate(Timestamp executionDate)
    {
        this.executionDate = executionDate;
    }

    /**
     * <p>hasException.</p>
     *
     * @return a boolean.
     */
    public boolean hasException()
    {
        return !StringUtil.isEmpty(executionErrorId);
    }

    /**
     * <p>hasFailed.</p>
     *
     * @return a boolean.
     */
    public boolean hasFailed()
    {
        return hasException() || failures + errors > 0;
    }

    /**
     * <p>hasSucceeded.</p>
     *
     * @return a boolean.
     */
    public boolean hasSucceeded()
    {
        return  !hasFailed() && success > 0;
    }
    
    /**
     * <p>wasIgnored.</p>
     *
     * @return a boolean.
     */
    public boolean wasIgnored()
    {
        return  !hasFailed() && !hasSucceeded() && ignored != 0;
    }
    
    /**
     * <p>wasRunned.</p>
     *
     * @return a boolean.
     */
    public boolean wasRunned()
    {
        return  hasFailed() || hasSucceeded() || ignored != 0;
    }
    
    /**
     * <p>wasRemotelyExecuted.</p>
     *
     * @return a boolean.
     */
    public boolean wasRemotelyExecuted()
    {
    	return this.executedRemotely;
    }
    
    /**
     * <p>setRemotelyExecuted.</p>
     */
    public void setRemotelyExecuted()
    {
    	this.executedRemotely = true;
    }

    /**
     * <p>getStatus.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    @Transient
    public String getStatus()
    {
        if(hasFailed()) return FAILED; 
        if(hasSucceeded()) return SUCCESS; 
        if(ignored != 0) return IGNORED; 
        return NOT_RUNNED; 
    }
    
    /**
     * <p>getCleanedResults.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    @Transient
    public String getCleanedResults()
    {
        return HtmlUtil.cleanUpResults(results);
    }
    
    /**
     * <p>getFormatedExecutionDate.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    @Transient
    public String getFormatedExecutionDate()
    {
        FormatedDate d = new FormatedDate(executionDate);
        return d.getFormatedTimestamp();
    }

    /**
     * <p>marshallize.</p>
     *
     * @return a {@link java.util.Vector} object.
     */
    public Vector<Object> marshallize()
    {
        Vector<Object> parameters = new Vector<Object>();
        parameters.add(EXECUTION_RESULTS_IDX, StringUtils.defaultString(results));
        parameters.add(EXECUTION_ERRORID_IDX, StringUtils.defaultString(executionErrorId));
        parameters.add(EXECUTION_FAILIURES_IDX, failures);
        parameters.add(EXECUTION_ERRORS_IDX, errors);
        parameters.add(EXECUTION_SUCCESS_IDX, success);
        parameters.add(EXECUTION_IGNORED_IDX, ignored);
        parameters.add(EXECUTION_EXECUTION_DATE_IDX, getFormatedExecutionDate());
        return parameters;
    }

    /** {@inheritDoc} */
    public int compareTo(Object o)
    {
        return executionDate.compareTo(((Execution)o).executionDate);
    }
    
    /**
     * <p>isSimilar.</p>
     *
     * @param execution a {@link com.greenpepper.server.domain.Execution} object.
     * @return a boolean.
     */
    public boolean isSimilar(Execution execution)
    {
        return  execution != null &&
                errors == execution.getErrors() &&
                failures == execution.getFailures() &&
                success == execution.getSuccess() &&
                ignored == execution.getIgnored() &&
                StringUtil.isEquals(executionErrorId, execution.getExecutionErrorId()) &&
                StringUtil.isEquals(results, execution.getResults());
    }
    
    /** {@inheritDoc} */
    public boolean equals(Object o)
    {
        if(o == null || !(o instanceof Execution))
        {
            return false;
        }
        
        return super.equals(o);
    }
}
