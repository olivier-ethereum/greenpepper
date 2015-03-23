package com.greenpepper.server.license;

import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import com.greenpepper.server.GreenPepperServer;
import com.greenpepper.server.database.hibernate.HibernateSessionService;
import com.greenpepper.server.domain.Repository;
import com.greenpepper.util.FormatedDate;

public class OpenSourceAuthorizer implements Authorizer {

    public OpenSourceAuthorizer(HibernateSessionService service, Properties sProperties) {
    }

    @Override
    public void initialize(Date versionDate) throws Exception {

    }

    @Override
    public void reInitialize(String newLicence) throws Exception {
    }

    @Override
    public void verify(Repository repository, Permission permission) throws GreenPepperLicenceException {
    }

    @Override
    public LicenseBean getLicenseBean() {
        Calendar debut = Calendar.getInstance();
        debut.set(2000, 0, 1);
        Calendar fin = Calendar.getInstance();
        fin.set(2999, 11, 31);
        License license = License.openSource("Strator", debut.getTime(), fin.getTime());
        LicenseBean bean = new LicenseBean();
        bean.setInfo(license.getInfo());
        bean.setLicenseType(license.getLicenseType());
        bean.setMaxUsers(license.getMaxUsers());

        bean.setNotBefore(new FormatedDate(license.getNotBefore()).getFormatedDate());
        bean.setNoSupportAfter(new FormatedDate(license.getNoSupportAfter()).getFormatedDate());
        bean.setNotAfter(new FormatedDate(license.getNotAfter()).getFormatedDate());
        bean.setVersion(GreenPepperServer.VERSION);
        bean.setHolderName(license.getHolderName());
        return bean;
    }

    @Override
    public boolean isCommercialLicense() {
        return false;
    }

}
