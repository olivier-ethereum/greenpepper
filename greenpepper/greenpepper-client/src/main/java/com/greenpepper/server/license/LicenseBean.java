package com.greenpepper.server.license;

import java.util.Vector;

import com.greenpepper.server.domain.Marshalizable;
import static com.greenpepper.server.rpc.xmlrpc.XmlRpcDataMarshaller.LICENSE_TYPE_IDX;
import static com.greenpepper.server.rpc.xmlrpc.XmlRpcDataMarshaller.LICENSE_EXPIRY_DATE_IDX;
import static com.greenpepper.server.rpc.xmlrpc.XmlRpcDataMarshaller.LICENSE_SUPPORT_EXPIRY_DATE_IDX;
import static com.greenpepper.server.rpc.xmlrpc.XmlRpcDataMarshaller.LICENSE_MAX_USERS_IDX;
import static com.greenpepper.server.rpc.xmlrpc.XmlRpcDataMarshaller.LICENSE_EXTRA_IDX;
import static com.greenpepper.server.rpc.xmlrpc.XmlRpcDataMarshaller.LICENSE_INFO_IDX;
import static com.greenpepper.server.rpc.xmlrpc.XmlRpcDataMarshaller.LICENSE_VERSION_IDX;
import static com.greenpepper.server.rpc.xmlrpc.XmlRpcDataMarshaller.LICENSE_HOLDER_NAME_IDX;
import static com.greenpepper.server.rpc.xmlrpc.XmlRpcDataMarshaller.LICENSE_EFFECTIVE_DATE_IDX;
import com.greenpepper.util.StringUtil;

/**
 * <p>LicenseBean class.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public class LicenseBean implements Marshalizable
{
    private String licenseType;
	private String notBefore;
	private String noSupportAfter;
    private String notAfter;
    private String version;
    private int maxUsers;
    private String holderName;
    private String info;

	/**
	 * <p>Getter for the field <code>licenseType</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getLicenseType()
    {
        return licenseType;
    }
    
    /**
     * <p>Setter for the field <code>licenseType</code>.</p>
     *
     * @param licenseType a {@link java.lang.String} object.
     */
    public void setLicenseType(String licenseType)
    {
        this.licenseType = licenseType;
    }

	/**
	 * <p>hasMaxUsers.</p>
	 *
	 * @return a boolean.
	 */
	public boolean hasMaxUsers()
	{
		return maxUsers > 0;
	}
	
	/**
	 * <p>Getter for the field <code>maxUsers</code>.</p>
	 *
	 * @return a int.
	 */
	public int getMaxUsers()
    {
        return maxUsers;
    }
    
    /**
     * <p>Setter for the field <code>maxUsers</code>.</p>
     *
     * @param maxUsers a int.
     */
    public void setMaxUsers(int maxUsers)
    {
        this.maxUsers = maxUsers;
    }

	/**
	 * <p>Getter for the field <code>notBefore</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getNotBefore()
	{
		return notBefore;
	}

	/**
	 * <p>Setter for the field <code>notBefore</code>.</p>
	 *
	 * @param notBefore a {@link java.lang.String} object.
	 */
	public void setNotBefore(String notBefore)
	{
		this.notBefore = notBefore;
	}

	/**
	 * <p>Getter for the field <code>notAfter</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getNotAfter()
    {
        return notAfter;
    }
    
    /**
     * <p>Setter for the field <code>notAfter</code>.</p>
     *
     * @param notAfter a {@link java.lang.String} object.
     */
    public void setNotAfter(String notAfter)
    {
        this.notAfter = notAfter;
    }

    /**
     * <p>Getter for the field <code>noSupportAfter</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getNoSupportAfter()
    {
        return noSupportAfter;
    }

    /**
     * <p>Getter for the field <code>info</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getInfo()
    {
        return info;
    }

    /**
     * <p>Setter for the field <code>info</code>.</p>
     *
     * @param info a {@link java.lang.String} object.
     */
    public void setInfo(String info)
    {
        this.info = info;
    }

    /**
     * <p>Setter for the field <code>noSupportAfter</code>.</p>
     *
     * @param noSupportAfter a {@link java.lang.String} object.
     */
    public void setNoSupportAfter(String noSupportAfter)
    {
        this.noSupportAfter = noSupportAfter;
    }
    
    /**
     * <p>hasLicenseType.</p>
     *
     * @return a boolean.
     */
    public boolean hasLicenseType()
    {
        return !StringUtil.isEmpty(licenseType);
    }
    
    /**
     * <p>hasNotAfter.</p>
     *
     * @return a boolean.
     */
    public boolean hasNotAfter()
    {
        return !StringUtil.isEmpty(notAfter);
    }
    
    /**
     * <p>hasNoSupportAfter.</p>
     *
     * @return a boolean.
     */
    public boolean hasNoSupportAfter()
    {
        return !StringUtil.isEmpty(noSupportAfter);
    }

	/**
	 * <p>hasNotBefore.</p>
	 *
	 * @return a boolean.
	 */
	public boolean hasNotBefore()
	{
		return !StringUtil.isEmpty(notBefore);
	}

	/**
	 * <p>Getter for the field <code>version</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getVersion()
    {
        return version;
    }

    /**
     * <p>Setter for the field <code>version</code>.</p>
     *
     * @param version a {@link java.lang.String} object.
     */
    public void setVersion(String version)
    {
        this.version = version;
    }

    /**
     * <p>hasHolderName.</p>
     *
     * @return a boolean.
     */
    public boolean hasHolderName()
    {
        return !StringUtil.isEmpty(holderName);
    }
    
	/**
	 * <p>Getter for the field <code>holderName</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getHolderName() {
		return holderName;
	}

	/**
	 * <p>Setter for the field <code>holderName</code>.</p>
	 *
	 * @param holderName a {@link java.lang.String} object.
	 */
	public void setHolderName(String holderName) {
		this.holderName = holderName;
	}

	/**
	 * <p>marshallize.</p>
	 *
	 * @return a {@link java.util.Vector} object.
	 */
	public Vector<Object> marshallize()
    {
        Vector<Object> parameters = new Vector<Object>();
        parameters.add(LICENSE_TYPE_IDX, licenseType);
        parameters.add(LICENSE_EXPIRY_DATE_IDX, notAfter);
        parameters.add(LICENSE_SUPPORT_EXPIRY_DATE_IDX, noSupportAfter);
        parameters.add(LICENSE_MAX_USERS_IDX, maxUsers);
        parameters.add(LICENSE_INFO_IDX, info);
        parameters.add(LICENSE_VERSION_IDX, version);
        parameters.add(LICENSE_EXTRA_IDX, 0);
        parameters.add(LICENSE_HOLDER_NAME_IDX, holderName);
		parameters.add(LICENSE_EFFECTIVE_DATE_IDX, notBefore);
		return parameters;
    }
}
