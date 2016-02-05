package com.greenpepper.server;

/**
 * <p>GreenPepperServerErrorKey interface.</p>
 *
 * @author oaouattara
 * @version $Id: $Id
 */
public interface GreenPepperServerErrorKey
{
    /** Constant <code>SUCCESS="<success>"</code> */
    public static final String SUCCESS = "<success>";
    /** Constant <code>ERROR="<exception>"</code> */
    public static final String ERROR = "<exception>";

    /**
     * General errors
     */
    public static final String GENERAL_ERROR = "greenpepper.server.generalerror";
    /** Constant <code>CONFIGURATION_ERROR="greenpepper.server.configerror"</code> */
    public static final String CONFIGURATION_ERROR = "greenpepper.server.configerror";
    /** Constant <code>XML_RPC_URL_NOTFOUND="greenpepper.server.xmlrpcurlinvalid"</code> */
    public static final String XML_RPC_URL_NOTFOUND = "greenpepper.server.xmlrpcurlinvalid";
    /** Constant <code>XML_RPC_HANDLER_NOTFOUND="greenpepper.server.xmlrpchandlerinvalid"</code> */
    public static final String XML_RPC_HANDLER_NOTFOUND = "greenpepper.server.xmlrpchandlerinvalid";
    /** Constant <code>NO_CONFIGURATION="greenpepper.server.noconfiguration"</code> */
    public static final String NO_CONFIGURATION = "greenpepper.server.noconfiguration";
    /** Constant <code>CALL_FAILED="greenpepper.server.callfailed"</code> */
    public static final String CALL_FAILED = "greenpepper.server.callfailed";
    /** Constant <code>MARSHALL_NOT_SUPPORTED="greenpepper.server.marshallingnotsuppor"{trunked}</code> */
    public static final String MARSHALL_NOT_SUPPORTED = "greenpepper.server.marshallingnotsupported";

    /**
     * Failed to retrieve.
     */
    public static final String RETRIEVE_PROJECTS = "greenpepper.server.retrieveprojects";
    /** Constant <code>RETRIEVE_REPOSITORIES="greenpepper.server.retrieverepos"</code> */
    public static final String RETRIEVE_REPOSITORIES= "greenpepper.server.retrieverepos";
    /** Constant <code>RETRIEVE_SPECIFICATION_REPOS="greenpepper.server.retrievespecrepos"</code> */
    public static final String RETRIEVE_SPECIFICATION_REPOS = "greenpepper.server.retrievespecrepos";
    /** Constant <code>RETRIEVE_REQUIREMENT_REPOS="greenpepper.server.retrieverequirementr"{trunked}</code> */
    public static final String RETRIEVE_REQUIREMENT_REPOS = "greenpepper.server.retrieverequirementrepos";
    /** Constant <code>RETRIEVE_SUTS="greenpepper.server.retrievesuts"</code> */
    public static final String RETRIEVE_SUTS = "greenpepper.server.retrievesuts";
    /** Constant <code>RETRIEVE_COMPILATION="greenpepper.server.retrievecompil"</code> */
    public static final String RETRIEVE_COMPILATION = "greenpepper.server.retrievecompil";
    /** Constant <code>RETRIEVE_REFERENCES="greenpepper.server.retrievereferences"</code> */
    public static final String RETRIEVE_REFERENCES = "greenpepper.server.retrievereferences";
	/** Constant <code>RETRIEVE_EXECUTIONS="greenpepper.server.retrieveexecutions"</code> */
	public static final String RETRIEVE_EXECUTIONS = "greenpepper.server.retrieveexecutions";
    /** Constant <code>RETRIEVE_REFERENCE="greenpepper.server.retrievereference"</code> */
    public static final String RETRIEVE_REFERENCE = "greenpepper.server.retrievereference";
    /** Constant <code>RETRIEVE_FILE_FAILED="greenpepper.server.filefailed"</code> */
    public static final String RETRIEVE_FILE_FAILED = "greenpepper.server.filefailed";

    /**
     * Project's errors.
     */
    public static final String PROJECT_NOT_FOUND = "greenpepper.server.projectnotfound";
	/** Constant <code>PROJECT_ALREADY_EXISTS="greenpepper.server.projectalreadyexist"</code> */
	public static final String PROJECT_ALREADY_EXISTS = "greenpepper.server.projectalreadyexist";
    /** Constant <code>PROJECT_DEFAULT_SUT_NOT_FOUND="greenpepper.server.defaultsutnotfound"</code> */
    public static final String PROJECT_DEFAULT_SUT_NOT_FOUND = "greenpepper.server.defaultsutnotfound";
	/** Constant <code>PROJECT_REMOVE_FAILED="greenpepper.server.removeprojectfailed"</code> */
	public static final String PROJECT_REMOVE_FAILED = "greenpepper.server.removeprojectfailed";
	/** Constant <code>PROJECT_REPOSITORY_ASSOCIATED="greenpepper.server.projectrepoassociate"{trunked}</code> */
	public static final String PROJECT_REPOSITORY_ASSOCIATED = "greenpepper.server.projectrepoassociated";
	/** Constant <code>PROJECT_SUTS_ASSOCIATED="greenpepper.server.projectsutsassociate"{trunked}</code> */
	public static final String PROJECT_SUTS_ASSOCIATED = "greenpepper.server.projectsutsassociated";

	/**
     * Repository's errors.
     */
    public static final String REPOSITORY_CLASS_NOT_FOUND = "greenpepper.server.repoclassnotfound";
    /** Constant <code>REPOSITORY_DOC_ASSOCIATED="greenpepper.server.repodocassociated"</code> */
    public static final String REPOSITORY_DOC_ASSOCIATED = "greenpepper.server.repodocassociated";
    /** Constant <code>REPOSITORY_NOT_FOUND="greenpepper.server.repositorynotfound"</code> */
    public static final String REPOSITORY_NOT_FOUND = "greenpepper.server.repositorynotfound";
    /** Constant <code>REPOSITORY_UPDATE_FAILED="greenpepper.server.repoupdatefailed"</code> */
    public static final String REPOSITORY_UPDATE_FAILED = "greenpepper.server.repoupdatefailed";
    /** Constant <code>REPOSITORY_TYPE_NOT_FOUND="greenpepper.server.rtypenotfound"</code> */
    public static final String REPOSITORY_TYPE_NOT_FOUND = "greenpepper.server.rtypenotfound";
    /** Constant <code>PROJECT_CREATE_FAILED="greenpepper.server.createprojectfailed"</code> */
    public static final String PROJECT_CREATE_FAILED = "greenpepper.server.createprojectfailed";
	/** Constant <code>PROJECT_UPDATE_FAILED="greenpepper.server.projectupdatefailed"</code> */
	public static final String PROJECT_UPDATE_FAILED = "greenpepper.server.projectupdatefailed";
    /** Constant <code>REPOSITORY_ALREADY_EXISTS="greenpepper.server.repoalreadyexists"</code> */
    public static final String REPOSITORY_ALREADY_EXISTS = "greenpepper.server.repoalreadyexists";
    /** Constant <code>REPOSITORY_REMOVE_FAILED="greenpepper.server.removerepofailed"</code> */
    public static final String REPOSITORY_REMOVE_FAILED = "greenpepper.server.removerepofailed";
    /** Constant <code>REPOSITORY_DOES_NOT_CONTAINS_SPECIFICATION="greenpepper.repositorynotspecification"</code> */
    public static final String REPOSITORY_DOES_NOT_CONTAINS_SPECIFICATION = "greenpepper.repositorynotspecification";
    /** Constant <code>REPOSITORY_GET_REGISTERED="greenpepper.server.retrieverepository"</code> */
    public static final String REPOSITORY_GET_REGISTERED = "greenpepper.server.retrieverepository";
    /** Constant <code>REPOSITORY_REGISTRATION_FAILED="greenpepper.server.registrationfailed"</code> */
    public static final String REPOSITORY_REGISTRATION_FAILED = "greenpepper.server.registrationfailed";
    /** Constant <code>REPOSITORY_UNREGISTRATION_FAILED="greenpepper.server.unregistrationfailed"</code> */
    public static final String REPOSITORY_UNREGISTRATION_FAILED = "greenpepper.server.unregistrationfailed";

    /**
     * Requirement's errors.
     */
    public static final String REQUIREMENT_NOT_FOUND = "greenpepper.server.requirementnotfound";
    /** Constant <code>REQUIREMENT_ALREADY_EXISTS="greenpepper.server.requirementalreadyex"{trunked}</code> */
    public static final String REQUIREMENT_ALREADY_EXISTS = "greenpepper.server.requirementalreadyexists";
    /** Constant <code>REQUIREMENT_REMOVE_FAILED="greenpepper.server.removerequirementfai"{trunked}</code> */
    public static final String REQUIREMENT_REMOVE_FAILED = "greenpepper.server.removerequirementfailed";
    
    /**
     * Specification's errors
     */
    public static final String SPECIFICATION_NOT_FOUND = "greenpepper.server.specificationnotfound";
    /** Constant <code>SPECIFICATIONS_NOT_FOUND="greenpepper.server.specificationsnotfou"{trunked}</code> */
    public static final String SPECIFICATIONS_NOT_FOUND = "greenpepper.server.specificationsnotfound";
    /** Constant <code>SPECIFICATION_CREATE_FAILED="greenpepper.server.createspecificationf"{trunked}</code> */
    public static final String SPECIFICATION_CREATE_FAILED = "greenpepper.server.createspecificationfailed";
    /** Constant <code>SPECIFICATION_REFERENCED="greenpepper.server.removereferencedspec"{trunked}</code> */
    public static final String SPECIFICATION_REFERENCED = "greenpepper.server.removereferencedspecification";
    /** Constant <code>SPECIFICATION_ALREADY_EXISTS="greenpepper.server.specificationalready"{trunked}</code> */
    public static final String SPECIFICATION_ALREADY_EXISTS = "greenpepper.server.specificationalreadyexists";
    /** Constant <code>SPECIFICATION_ADD_SUT_FAILED="greenpepper.server.addsutspecificationa"{trunked}</code> */
    public static final String SPECIFICATION_ADD_SUT_FAILED = "greenpepper.server.addsutspecificational";
    /** Constant <code>SPECIFICATION_REMOVE_SUT_FAILED="greenpepper.server.removesutspecificati"{trunked}</code> */
    public static final String SPECIFICATION_REMOVE_SUT_FAILED = "greenpepper.server.removesutspecificational";
    /** Constant <code>SPECIFICATION_UPDATE_FAILED="greenpepper.server.updatespecificationf"{trunked}</code> */
    public static final String SPECIFICATION_UPDATE_FAILED = "greenpepper.server.updatespecificationfailed";
    /** Constant <code>SPECIFICATION_REMOVE_FAILED="greenpepper.server.removespecificationf"{trunked}</code> */
    public static final String SPECIFICATION_REMOVE_FAILED = "greenpepper.server.removespecificationfailed";
    /** Constant <code>SPECIFICATION_RUN_FAILED="greenpepper.server.runspecificationfail"{trunked}</code> */
    public static final String SPECIFICATION_RUN_FAILED = "greenpepper.server.runspecificationfailed";
    /** Constant <code>SPECIFICATION_IMPLEMENTED_FAILED="greenpepper.server.implementedfailed"</code> */
    public static final String SPECIFICATION_IMPLEMENTED_FAILED = "greenpepper.server.implementedfailed";
        
    /**
     * Runners errors
     */
    public static final String RUNNER_ALREADY_EXISTS = "greenpepper.server.runneralreadyexists";
    /** Constant <code>RUNNERS_NOT_FOUND="greenpepper.server.runnersnotfound"</code> */
    public static final String RUNNERS_NOT_FOUND = "greenpepper.server.runnersnotfound";
    /** Constant <code>RUNNER_NOT_FOUND="greenpepper.server.runnernotfound"</code> */
    public static final String RUNNER_NOT_FOUND = "greenpepper.server.runnernotfound";
    /** Constant <code>RUNNER_CREATE_FAILED="greenpepper.server.runnercreatefailed"</code> */
    public static final String RUNNER_CREATE_FAILED = "greenpepper.server.runnercreatefailed";
    /** Constant <code>RUNNER_UPDATE_FAILED="greenpepper.server.runnerupdatefailed"</code> */
    public static final String RUNNER_UPDATE_FAILED = "greenpepper.server.runnerupdatefailed";
    /** Constant <code>RUNNER_REMOVE_FAILED="greenpepper.server.runnerremovefailed"</code> */
    public static final String RUNNER_REMOVE_FAILED = "greenpepper.server.runnerremovefailed";
    /** Constant <code>RUNNER_SUT_ASSOCIATED="greenpepper.server.runnersutassociated"</code> */
    public static final String RUNNER_SUT_ASSOCIATED = "greenpepper.server.runnersutassociated";
    /** Constant <code>ENVTYPES_NOT_FOUND="greenpepper.server.envtypesnotfound"</code> */
    public static final String ENVTYPES_NOT_FOUND =  "greenpepper.server.envtypesnotfound";
    /** Constant <code>ENVTYPE_NOT_FOUND="greenpepper.server.envtypenotfound"</code> */
    public static final String ENVTYPE_NOT_FOUND =  "greenpepper.server.envtypenotfound";
    
    /**
     * System under test's error.
     */
    public static final String SUT_NOT_FOUND = "greenpepper.server.sutnotfound";
    /** Constant <code>SUT_REFERENCE_ASSOCIATED="greenpepper.server.sutwithreferences"</code> */
    public static final String SUT_REFERENCE_ASSOCIATED = "greenpepper.server.sutwithreferences";
    /** Constant <code>SUT_SPECIFICATION_ASSOCIATED="greenpepper.server.sutwithspecification"{trunked}</code> */
    public static final String SUT_SPECIFICATION_ASSOCIATED = "greenpepper.server.sutwithspecifications";
    /** Constant <code>SUT_EXECUTION_ASSOCIATED="greenpepper.server.sutwithexecutions"</code> */
    public static final String SUT_EXECUTION_ASSOCIATED = "greenpepper.server.sutwithexecutions";
    /** Constant <code>SUT_CREATE_FAILED="greenpepper.server.createsutfailed"</code> */
    public static final String SUT_CREATE_FAILED = "greenpepper.server.createsutfailed";
    /** Constant <code>SUT_SET_DEFAULT_FAILED="greenpepper.server.setdefaultsutfailed"</code> */
    public static final String SUT_SET_DEFAULT_FAILED = "greenpepper.server.setdefaultsutfailed";
    /** Constant <code>SUT_ALREADY_EXISTS="greenpepper.server.sutalreadyexists"</code> */
    public static final String SUT_ALREADY_EXISTS = "greenpepper.server.sutalreadyexists";
    /** Constant <code>SUT_UPDATE_FAILED="greenpepper.server.updatesutfailed"</code> */
    public static final String SUT_UPDATE_FAILED = "greenpepper.server.updatesutfailed";
    /** Constant <code>SUT_DELETE_FAILED="greenpepper.server.deletesutfailed"</code> */
    public static final String SUT_DELETE_FAILED = "greenpepper.server.deletesutfailed";

    /**
     * Reference's errors.
     */
    public static final String REFERENCE_NOT_FOUND = "greenpepper.server.referencenotfound";
    /** Constant <code>REFERENCE_CREATE_FAILED="greenpepper.server.createreferencefaile"{trunked}</code> */
    public static final String REFERENCE_CREATE_FAILED = "greenpepper.server.createreferencefailed";
    /** Constant <code>REFERENCE_UPDATE_FAILED="greenpepper.server.updatereferencefaile"{trunked}</code> */
    public static final String REFERENCE_UPDATE_FAILED = "greenpepper.server.updatereferencefailed";
    /** Constant <code>REFERENCE_REMOVE_FAILED="greenpepper.server.removereferencefaile"{trunked}</code> */
    public static final String REFERENCE_REMOVE_FAILED = "greenpepper.server.removereferencefailed";
    /** Constant <code>RUN_REFERENCE_FAILED="greenpepper.server.runreferencefailed"</code> */
    public static final String RUN_REFERENCE_FAILED = "greenpepper.server.runreferencefailed";
	/** Constant <code>REFERENCE_CREATE_ALREADYEXIST="greenpepper.server.createreferencealrea"{trunked}</code> */
	public static final String REFERENCE_CREATE_ALREADYEXIST = "greenpepper.server.createreferencealreadyexist";

	/**
	 * Execution's errors
	 */
	public static final String EXECUTION_CREATE_FAILED = "greenpepper.server.createexecutionfailed";

    /** ????? */
    public static final String RESOLVED_URI_FAILED = "greenpepper.server.failedtoresolveuri";
}
