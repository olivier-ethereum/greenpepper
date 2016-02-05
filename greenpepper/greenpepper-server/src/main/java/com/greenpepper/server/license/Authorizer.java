package com.greenpepper.server.license;

import java.util.Date;

import com.greenpepper.server.domain.Repository;

/**
 * <p>Authorizer interface.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public interface Authorizer {

	/**
	 * <p>initialize.</p>
	 *
	 * @param versionDate a {@link java.util.Date} object.
	 * @throws java.lang.Exception if any.
	 */
	void initialize(Date versionDate)
			throws Exception;

	/**
	 * ReInitializes the Authorizer with the new persisted license. </p>
	 *
	 * @param newLicence a {@link java.lang.String} object.
	 * @throws java.lang.Exception if any.
	 */
	void reInitialize(String newLicence)
			throws Exception;

	/**
	 * Verifies that the license supports the repository has the rgiht permission. </p>
	 *
	 * @param repository a {@link com.greenpepper.server.domain.Repository} object.
	 * @param permission a {@link com.greenpepper.server.license.Permission} object.
	 * @throws com.greenpepper.server.license.GreenPepperLicenceException if any.
	 */
	void verify(Repository repository, Permission permission)
			throws GreenPepperLicenceException;

	/**
	 * <p>getLicenseBean.</p>
	 *
	 * @return a {@link com.greenpepper.server.license.LicenseBean} object.
	 */
	LicenseBean getLicenseBean();

	/**
	 * <p>isCommercialLicense.</p>
	 *
	 * @return a boolean.
	 */
	boolean isCommercialLicense();
}
