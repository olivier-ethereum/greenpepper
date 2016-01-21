package com.greenpepper.server.rpc.xmlrpc;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Vector;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.greenpepper.server.GreenPepperServerErrorKey;
import com.greenpepper.server.GreenPepperServerException;
import com.greenpepper.server.domain.ClasspathSet;
import com.greenpepper.server.domain.DocumentNode;
import com.greenpepper.server.domain.EnvironmentType;
import com.greenpepper.server.domain.Execution;
import com.greenpepper.server.domain.Project;
import com.greenpepper.server.domain.Reference;
import com.greenpepper.server.domain.ReferenceNode;
import com.greenpepper.server.domain.Repository;
import com.greenpepper.server.domain.RepositoryType;
import com.greenpepper.server.domain.Requirement;
import com.greenpepper.server.domain.RequirementSummary;
import com.greenpepper.server.domain.Runner;
import com.greenpepper.server.domain.Specification;
import com.greenpepper.server.domain.SystemUnderTest;
import com.greenpepper.server.domain.SystemUnderTestByNameComparator;
import com.greenpepper.server.domain.component.ContentType;
import com.greenpepper.server.license.GreenPepperLicenceException;
import com.greenpepper.server.license.LicenseBean;
import com.greenpepper.server.license.LicenseErrorKey;
import com.greenpepper.util.FormatedDate;

/**
 * The XML-RPC Data Marshaller.
 * Provides static methods to pass from POJO to XML-RPC supported objects.
 * Provides static methods to pass from XML-RPC supported objects to POJO.
 * <p/>
 * Copyright (c) 2006 Pyxis technologies inc. All Rights Reserved.
 * @author JCHUET
 */
public class XmlRpcDataMarshaller
{
	public static final String MARSHALLING_VERSION = "Marshall v. 1.0";
    
    public static final int LICENSE_TYPE_IDX = 0;
    public static final int LICENSE_EXPIRY_DATE_IDX = 1;
    public static final int LICENSE_SUPPORT_EXPIRY_DATE_IDX = 2;
    public static final int LICENSE_MAX_USERS_IDX = 3;
    public static final int LICENSE_INFO_IDX = 4;
    public static final int LICENSE_VERSION_IDX = 5;
    public static final int LICENSE_EXTRA_IDX = 6;
    public static final int LICENSE_HOLDER_NAME_IDX = 7;
	public static final int LICENSE_EFFECTIVE_DATE_IDX = 8;

	public static final int PROJECT_NAME_IDX = 0;
    
    public static final int REPOSITORY_TYPE_NAME_IDX = 0;
    public static final int REPOSITORY_TYPE_REPOCLASSES_IDX = 1;
    public static final int REPOSITORY_TYPE_NAME_FORMAT_IDX = 2;
    public static final int REPOSITORY_TYPE_URI_FORMAT_IDX = 3;
    
    public static final int REPOSITORY_NAME_IDX = 0;
    public static final int REPOSITORY_UID_IDX = 1;
    public static final int REPOSITORY_PROJECT_IDX = 2;
    public static final int REPOSITORY_TYPE_IDX = 3;
    public static final int REPOSITORY_CONTENTTYPE_IDX = 4;
    public static final int REPOSITORY_BASE_URL_IDX = 5;
    public static final int REPOSITORY_BASEREPO_URL_IDX = 6;
    public static final int REPOSITORY_BASETEST_URL_IDX = 7;
    public static final int REPOSITORY_USERNAME_IDX = 8;
    public static final int REPOSITORY_PASSWORD_IDX = 9;
    public static final int REPOSITORY_MAX_USERS_IDX = 10;
    
    public static final int DOCUMENT_NAME_IDX = 0;
    public static final int DOCUMENT_REPOSITORY_IDX = 1;
    public static final int SPECIFICATION_SUTS_IDX = 2;

    public static final int RUNNER_NAME_IDX = 0;
    public static final int RUNNER_CMDLINE_IDX = 1;
    public static final int RUNNER_ENVTYPE_IDX = 2;
    public static final int RUNNER_SERVER_NAME_IDX = 3;
    public static final int RUNNER_SERVER_PORT_IDX = 4;
    public static final int RUNNER_MAINCLASS_IDX = 5;
    public static final int RUNNER_CLASSPATH_IDX = 6;
    public static final int RUNNER_SECURED_IDX = 7;

    public static final int ENVTYPE_NAME_IDX = 0;
    
    public static final int SUT_NAME_IDX = 0;
    public static final int SUT_PROJECT_IDX = 1;
    public static final int SUT_CLASSPATH_IDX = 2;
    public static final int SUT_FIXTURE_CLASSPATH_IDX = 3;
    public static final int SUT_FIXTURE_FACTORY_IDX = 4;
    public static final int SUT_FIXTURE_FACTORY_ARGS_IDX = 5;
    public static final int SUT_IS_DEFAULT_IDX = 6;
    public static final int SUT_RUNNER_IDX = 7;
	public static final int SUT_PROJECT_DEPENDENCY_DESCRIPTOR_IDX = 8;

	public static final int REFERENCE_REQUIREMENT_IDX = 0;
    public static final int REFERENCE_SPECIFICATION_IDX = 1;
    public static final int REFERENCE_SUT_IDX = 2;
    public static final int REFERENCE_SECTIONS_IDX = 3;
    public static final int REFERENCE_LAST_EXECUTION_IDX = 4;
    
    public static final int EXECUTION_RESULTS_IDX = 0;
    public static final int EXECUTION_ERRORID_IDX = 1;
    public static final int EXECUTION_FAILIURES_IDX = 2;
    public static final int EXECUTION_ERRORS_IDX = 3;
    public static final int EXECUTION_SUCCESS_IDX = 4;
    public static final int EXECUTION_IGNORED_IDX = 5;
    public static final int EXECUTION_EXECUTION_DATE_IDX = 6;
    
    public static final int SUMMARY_REFERENCES_IDX = 0;
    public static final int SUMMARY_FAILIURES_IDX = 1;
    public static final int SUMMARY_ERRORS_IDX = 2;
    public static final int SUMMARY_SUCCESS_IDX = 3;
    public static final int SUMMARY_EXCEPTION_IDX = 4;
    
    public final static int NODE_TITLE_INDEX = 0;
    public final static int NODE_EXECUTABLE_INDEX = 1;
    public final static int NODE_CAN_BE_IMPLEMENTED_INDEX = 2;
    public final static int NODE_CHILDREN_INDEX = 3;
    public final static int NODE_REPOSITORY_UID_INDEX = 4;
    public final static int NODE_SUT_NAME_INDEX = 5;
    public final static int NODE_SECTION_INDEX = 6;

    private final static Logger logger = LoggerFactory.getLogger(XmlRpcDataMarshaller.class);

    /**
     * Transforms the Collection of projects into a Vector of project parameters.
     * </p>
     * @param projects
     * @return the Collection of projects into a Vector of projects parameters
     */
    public static Vector<Object> toXmlRpcProjectsParameters(Collection<Project> projects)
    { 
        Vector<Object> projectsParams = new Vector<Object>();        
        for(Project project : projects)
        {
            projectsParams.add(project.marshallize());
        }
        
        return projectsParams;
    }

    /**
     * Transforms the Collection of runners into a Vector of runners parameters.
     * </p>
     * @param runners
     * @return the Collection of runners into a Vector of runners parameters
     */
    public static Vector<Object> toXmlRpcRunnersParameters(Collection<Runner> runners)
    { 
        Vector<Object> runnersParams = new Vector<Object>();        
        for(Runner runner : runners)
        {
            runnersParams.add(runner.marshallize());
        }
        
        return runnersParams;
    }

    /**
     * Transforms the Collection of EnvironmentTypes into a Vector of EnvironmentTypes parameters.
     * </p>
     * @param envTypes
     * @return the Collection of EnvironmentTypes into a Vector of EnvironmentTypes parameters
     */
    public static Vector<Object> toXmlRpcEnvironmentTypesParameters(Collection<EnvironmentType> envTypes)
    { 
        Vector<Object> envTypesParams = new Vector<Object>();        
        for(EnvironmentType envType : envTypes)
        {
        	envTypesParams.add(envType.marshallize());
        }
        
        return envTypesParams;
    }

    /**
     * Transforms the Collection of SystemUnderTests into a Vector of SystemUnderTests parameters.
     * </p>
     * @param suts
     * @return the Collection of SystemUnderTests into a Vector of SystemUnderTests parameters
     */
    public static Vector<Object> toXmlRpcSystemUnderTestsParameters(Collection<SystemUnderTest> suts)
    { 
        Vector<Object> sutsParams = new Vector<Object>();        
        for(SystemUnderTest sut : suts)
        {
            sutsParams.add(sut.marshallize());
        }
        
        return sutsParams;
    }

    /**
     * Transforms the Collection of Repositories into a Vector of Repositories parameters 
     * by repositoriy types.
     * </p>
     * @param repositories
     * @return the Collection of Repositories into a Vecotr of Repositories parameters by type.
     */
    public static Vector<Object> toXmlRpcRepositoriesParameters(Collection<Repository> repositories)
    {
        Vector<Object> repositoriesParams = new Vector<Object>();        
        for(Repository repo : repositories)
        {
            repositoriesParams.add(repo.marshallize());     
        }      
        
        return repositoriesParams;
    }

    /**
     * Transforms the Collection of Specifications into a Vector of Specification parameters.
     * </p>
     * @param specifications
     * @return the Collection of Specifications into a Vector of Specification parameters
     */
    public static Vector<Object> toXmlRpcSpecificationsParameters(Collection<Specification> specifications)
    { 
        Vector<Object> specificationsParams = new Vector<Object>();        
        for(Specification specification : specifications)
        {
            specificationsParams.add(specification.marshallize());
        }
        
        return specificationsParams;
    }

    /**
     * Transforms the Collection of References into a Vector of Reference parameters.
     * </p>
     * @param references
     * @return the Collection of References into a Vector of Reference parameters
     */
    public static Vector<Object> toXmlRpcReferencesParameters(Collection<Reference> references)
    { 
        Vector<Object> referencesParams = new Vector<Object>();        
        for(Reference reference : references)
        {
            referencesParams.add(reference.marshallize());
        }
        
        return referencesParams;
    }

    /**
     * Transforms the Vector of the Licence parameters into a LicenceBean Object.<br>
     * Structure of the parameters:<br>
     * Vector[type, exepryDate, maxUsers]
     * </p>
     * @param xmlRpcParameters
     * @return the Licence.
     */
    public static LicenseBean toLicense(Vector<Object> xmlRpcParameters)
    {
        LicenseBean license = new LicenseBean();
        if(!xmlRpcParameters.isEmpty())
        {
            license.setLicenseType((String)xmlRpcParameters.get(LICENSE_TYPE_IDX));
            license.setNoSupportAfter((String)xmlRpcParameters.get(LICENSE_SUPPORT_EXPIRY_DATE_IDX));
            license.setNotAfter((String)xmlRpcParameters.get(LICENSE_EXPIRY_DATE_IDX));
            license.setMaxUsers((Integer)xmlRpcParameters.get(LICENSE_MAX_USERS_IDX));
            license.setInfo((String)xmlRpcParameters.get(LICENSE_INFO_IDX));
            license.setVersion((String)xmlRpcParameters.get(LICENSE_VERSION_IDX));
            
            // Backward compability  --- wasnt supported in v1.2
            if(xmlRpcParameters.size() > 6) 
            {
            	license.setHolderName((String)xmlRpcParameters.get(LICENSE_HOLDER_NAME_IDX));
            }

			// Backward compability  --- wasnt supported in v1.3
			if (xmlRpcParameters.size() > 7)
			{
				license.setNotBefore((String)xmlRpcParameters.get(LICENSE_EFFECTIVE_DATE_IDX));
			}
		}
        
        return license;
    }

    /**
     * Transforms the Vector of the Project parameters into a Project Object.<br>
     * Structure of the parameters:<br>
     * Vector[name] 
     * </p>
     * @param xmlRpcParameters
     * @return the Project.
     */
    public static Project toProject(Vector<Object> xmlRpcParameters)
    {
        Project project = null;
        if(!xmlRpcParameters.isEmpty())
        {
            project = Project.newInstance((String)xmlRpcParameters.get(PROJECT_NAME_IDX));
        }
        
        return project;
    }

    /**
     * Transforms the Vector of the RepositoryType parameters into a RepositoryType Object.<br>
     * Structure of the parameters:<br>
     * Vector[name, uriFormat] 
     * </p>
     * @param xmlRpcParameters
     * @return the RepositoryType.
     */
	public static RepositoryType toRepositoryType(Vector<Object> xmlRpcParameters)
    {
        RepositoryType repositoryType = null;
        if(!xmlRpcParameters.isEmpty())
        {
            repositoryType = RepositoryType.newInstance((String)xmlRpcParameters.get(REPOSITORY_TYPE_NAME_IDX));
            
            @SuppressWarnings("unchecked")
            Hashtable<String, String> params = (Hashtable<String, String>)xmlRpcParameters.get(REPOSITORY_TYPE_REPOCLASSES_IDX);
            for(String env : params.keySet())
            	repositoryType.registerClassForEnvironment(params.get(env), EnvironmentType.newInstance(env));

            repositoryType.setDocumentUrlFormat(toNullIfEmpty((String)xmlRpcParameters.get(REPOSITORY_TYPE_NAME_FORMAT_IDX)));
            repositoryType.setTestUrlFormat(toNullIfEmpty((String)xmlRpcParameters.get(REPOSITORY_TYPE_URI_FORMAT_IDX)));
        }
        
        return repositoryType;
    }

    /**
     * Transforms the Vector of the Repository parameters into a Repository Object.<br>
     * Structure of the parameters:<br>
     * Vector[name, Vector[project parameters], type, content type, uri]
     * </p>
     * @param xmlRpcParameters Vector[name, Vector[project parameters], type, content type, uri]
     * @return the Repository.
     */
    @SuppressWarnings("unchecked")
    public static Repository toRepository(Vector<Object> xmlRpcParameters)
    {
        Repository repository = null;
        if(!xmlRpcParameters.isEmpty())
        {
            repository = Repository.newInstance((String)xmlRpcParameters.get(REPOSITORY_UID_IDX));
            repository.setName((String)xmlRpcParameters.get(REPOSITORY_NAME_IDX));
            repository.setProject(toProject((Vector)xmlRpcParameters.get(REPOSITORY_PROJECT_IDX)));
            repository.setType(toRepositoryType((Vector)xmlRpcParameters.get(REPOSITORY_TYPE_IDX)));
            repository.setContentType(ContentType.getInstance((String)xmlRpcParameters.get(REPOSITORY_CONTENTTYPE_IDX)));
            repository.setBaseUrl((String)xmlRpcParameters.get(REPOSITORY_BASE_URL_IDX));
            repository.setBaseRepositoryUrl((String)xmlRpcParameters.get(REPOSITORY_BASEREPO_URL_IDX));
            repository.setBaseTestUrl((String)xmlRpcParameters.get(REPOSITORY_BASETEST_URL_IDX));
            repository.setUsername(toNullIfEmpty((String)xmlRpcParameters.get(REPOSITORY_USERNAME_IDX)));
            repository.setPassword(toNullIfEmpty((String)xmlRpcParameters.get(REPOSITORY_PASSWORD_IDX)));
            repository.setMaxUsers((Integer)xmlRpcParameters.get(REPOSITORY_MAX_USERS_IDX));
        }

        return repository;
    }

    /**
     * Transforms the Vector of the Requirement parameters into a Requirement Object.<br>
     * Structure of the parameters:<br>
     * Vector[name, Vector[repository parameters]]
     * </p>
     * @param xmlRpcParameters
     * @return the Requirement.
     */
    @SuppressWarnings("unchecked")
    public static Requirement toRequirement(Vector<Object> xmlRpcParameters)
    {
        Requirement requirement = null;
        if(!xmlRpcParameters.isEmpty())
        {
            String name = (String)xmlRpcParameters.get(DOCUMENT_NAME_IDX);
            requirement = Requirement.newInstance(name);
            requirement.setRepository(toRepository((Vector<Object>)xmlRpcParameters.get(DOCUMENT_REPOSITORY_IDX)));
        }
        
        return requirement;
    }

    /**
     * Transforms the Vector of the Specification parameters into a Specification Object.<br>
     * Structure of the parameters:<br>
     * Vector[name, Vector[repository parameters], Vector[SUT parameters]]
     * </p>
     * @param xmlRpcParameters
     * @return the Specification.
     */
    @SuppressWarnings("unchecked")
    public static Specification toSpecification(Vector<Object> xmlRpcParameters)
    {
        Specification specification = null;
        if(!xmlRpcParameters.isEmpty())
        {
            specification = Specification.newInstance((String)xmlRpcParameters.get(DOCUMENT_NAME_IDX));
            specification.setRepository(toRepository((Vector<Object>)xmlRpcParameters.get(DOCUMENT_REPOSITORY_IDX)));
            specification.setTargetedSystemUnderTests(toSystemUnderTestList((Vector<Object>)xmlRpcParameters.get(SPECIFICATION_SUTS_IDX)));
        }
        
        return specification;
    }

    /**
     * Transforms the Vector of the Runner parameters into a Runner Object.<br>
     * </p>
     * @param xmlRpcParameters Runner['name','cmd',['envtypename'],'servername','serverport','mainclass',['cp1','cp2'],'secured']
     * @return the Runner.
     */
    @SuppressWarnings("unchecked")
    public static Runner toRunner(Vector<Object> xmlRpcParameters)
    {
        Runner runner = null;
        if(!xmlRpcParameters.isEmpty())
        {
            runner = Runner.newInstance((String)getParameter(RUNNER_NAME_IDX, xmlRpcParameters));            
            runner.setCmdLineTemplate(toNullIfEmpty((String)getParameter(RUNNER_CMDLINE_IDX, xmlRpcParameters))); 
            runner.setEnvironmentType(toEnvironmentType((Vector)getParameter(RUNNER_ENVTYPE_IDX, xmlRpcParameters)));       
            runner.setServerName(toNullIfEmpty((String)getParameter(RUNNER_SERVER_NAME_IDX, xmlRpcParameters)));
            runner.setServerPort(toNullIfEmpty((String)getParameter(RUNNER_SERVER_PORT_IDX, xmlRpcParameters)));
            runner.setMainClass(toNullIfEmpty((String)getParameter(RUNNER_MAINCLASS_IDX, xmlRpcParameters)));
            ClasspathSet classpaths = new ClasspathSet((Vector)getParameter(RUNNER_CLASSPATH_IDX, xmlRpcParameters));
            runner.setClasspaths(classpaths);
            runner.setSecured((Boolean)getParameter(RUNNER_SECURED_IDX, xmlRpcParameters));
        }
        
        return runner;
    }
    
    private static Object getParameter(int index, Vector<Object> parameters)
    {
    	if (index > parameters.size() - 1) return null;
    	return parameters.get(index);
    }
    /**
     * Transforms the Vector of the EnvironmentType parameters into a EnvironmentType Object.<br>
     * </p>
     * @param xmlRpcParameters
     * @return the EnvironmentType.
     */
    public static EnvironmentType toEnvironmentType(Vector<Object> xmlRpcParameters)
    {
    	EnvironmentType type = null;
        if(!xmlRpcParameters.isEmpty())
        	type = EnvironmentType.newInstance((String)xmlRpcParameters.get(ENVTYPE_NAME_IDX));
        
        return type;
    }

    /**
     * Transforms the Vector of the SystemUnderTest parameters into a SystemUnderTest Object.<br>
     * Structure of the parameters:<br>
     * Vector[name, Vector[project parameters], Vector[seeds classPaths], Vector[fixture classPaths], fixturefactory, fixturefactoryargs, isdefault, Runner['name','cmd',['envtypename'],'servername','serverport','mainclass',['cp1','cp2'],'secured'], projectdependencydescriptor]
     * </p>
     * @param xmlRpcParameters
     * @return the SystemUnderTest.
     */
    @SuppressWarnings("unchecked")
    public static SystemUnderTest toSystemUnderTest(Vector<Object> xmlRpcParameters)
    {
        SystemUnderTest sut = null;
        if(!xmlRpcParameters.isEmpty())
        {
            ClasspathSet sutClasspaths = new ClasspathSet((Vector)xmlRpcParameters.get(SUT_CLASSPATH_IDX));
            ClasspathSet fixtureClasspaths = new ClasspathSet((Vector)xmlRpcParameters.get(SUT_FIXTURE_CLASSPATH_IDX));

            sut = SystemUnderTest.newInstance((String)xmlRpcParameters.get(SUT_NAME_IDX));
            sut.setProject(toProject((Vector<Object>)xmlRpcParameters.get(SUT_PROJECT_IDX)));
            sut.setSutClasspaths(sutClasspaths);
            sut.setFixtureClasspaths(fixtureClasspaths);
            sut.setFixtureFactory(toNullIfEmpty((String)xmlRpcParameters.get(SUT_FIXTURE_FACTORY_IDX)));
            sut.setFixtureFactoryArgs(toNullIfEmpty((String)xmlRpcParameters.get(SUT_FIXTURE_FACTORY_ARGS_IDX)));
            sut.setIsDefault((Boolean)xmlRpcParameters.get(SUT_IS_DEFAULT_IDX));
            sut.setRunner(toRunner((Vector<Object>)xmlRpcParameters.get(SUT_RUNNER_IDX)));
			sut.setProjectDependencyDescriptor(toNullIfEmpty((String)xmlRpcParameters.get(SUT_PROJECT_DEPENDENCY_DESCRIPTOR_IDX)));
		}
        
        return sut;
    }

    /**
     * Transforms the Vector of the Reference parameters into a Reference Object.<br>
     * @param xmlRpcParameters
     * @return the Reference.
     */
    @SuppressWarnings("unchecked")
    public static Reference toReference(Vector<Object> xmlRpcParameters)
    {
        Reference reference = null;
        if(!xmlRpcParameters.isEmpty())
        {
            Requirement requirement = toRequirement((Vector<Object>)xmlRpcParameters.get(REFERENCE_REQUIREMENT_IDX));
            Specification specification = toSpecification((Vector<Object>)xmlRpcParameters.get(REFERENCE_SPECIFICATION_IDX));
            SystemUnderTest sut = toSystemUnderTest((Vector<Object>)xmlRpcParameters.get(REFERENCE_SUT_IDX));
            String sections = toNullIfEmpty((String)xmlRpcParameters.get(REFERENCE_SECTIONS_IDX));
            reference = Reference.newInstance(requirement, specification, sut, sections);
            Execution exe = toExecution((Vector<Object>)xmlRpcParameters.get(REFERENCE_LAST_EXECUTION_IDX));
            reference.setLastExecution(exe);
        }

        return reference;
    }
    
    public static Execution toExecution(Vector<Object> xmlRpcParameters)
    {
        Execution execution = new Execution();        
        execution.setResults(toNullIfEmpty((String)xmlRpcParameters.get(EXECUTION_RESULTS_IDX)));
        execution.setExecutionErrorId(toNullIfEmpty((String)xmlRpcParameters.get(EXECUTION_ERRORID_IDX)));
        execution.setFailures((Integer)xmlRpcParameters.get(EXECUTION_FAILIURES_IDX));
        execution.setErrors((Integer)xmlRpcParameters.get(EXECUTION_ERRORS_IDX));
        execution.setSuccess((Integer)xmlRpcParameters.get(EXECUTION_SUCCESS_IDX)); 
        execution.setIgnored((Integer)xmlRpcParameters.get(EXECUTION_IGNORED_IDX));            
        FormatedDate date = new FormatedDate((String)xmlRpcParameters.get(EXECUTION_EXECUTION_DATE_IDX));
        execution.setExecutionDate(date.asTimestamp());  
        
        return execution;
    }

    public static RequirementSummary toRequirementSummary(Vector<Object> xmlRpcParameters)
    {
        RequirementSummary summary = new RequirementSummary();
        summary.setReferencesSize((Integer)xmlRpcParameters.get(SUMMARY_REFERENCES_IDX));
        summary.setFailures((Integer)xmlRpcParameters.get(SUMMARY_FAILIURES_IDX));
        summary.setErrors((Integer)xmlRpcParameters.get(SUMMARY_ERRORS_IDX));
        summary.setSuccess((Integer)xmlRpcParameters.get(SUMMARY_SUCCESS_IDX));
        summary.setExceptions((Integer)xmlRpcParameters.get(SUMMARY_EXCEPTION_IDX));
        
        return summary;
    }
    
    /**
     * Rebuild a List of projects based on the vector of projects parameters.
     * </p>
     * @param projectsParams
     * @return a List of projects based on the vector of projects parameters.
     * @see #toProject(Vector)
     */
    @SuppressWarnings("unchecked")
    public static Set<Project> toProjectList(Vector<Object> projectsParams)
    {
        Set<Project> projects = new TreeSet<Project>();
        for(Object projectParams : projectsParams)
        {
            projects.add(toProject((Vector<Object>)projectParams));
        }
        
        return projects;
    }
    
    /**
     * Rebuild a List of repositories based on the vector of repositories parameters.
     * </p>
     * @param repositoriesParams
     * @return a List of repositories based on the vector of repositories parameters.
     * @see #toRepository(Vector)
     */
    @SuppressWarnings("unchecked")
    public static Set<Repository> toRepositoryList(Vector<Object> repositoriesParams)
    {
        Set<Repository> repositories = new TreeSet<Repository>();
        for(Object repositoryParams : repositoriesParams)
        {
            repositories.add(toRepository((Vector<Object>)repositoryParams));
        }
        
        return repositories;
    }
    
    /**
     * Rebuild a List of runners based on the vector of runners parameters.
     * </p>
     * @param runnersParams
     * @return a List of runners based on the vector of runners parameters.
     * @see #toRunner(Vector)
     */
    @SuppressWarnings("unchecked")
    public static TreeSet<Runner> toRunnerList(Vector<Object> runnersParams)
    {
        TreeSet<Runner> runners = new TreeSet<Runner>();
        for(Object runnerParams : runnersParams)
        {
            runners.add(toRunner((Vector<Object>)runnerParams));
        }
        
        return runners;
    }
    
    /**
     * Rebuild a List of Environment types based on the vector of Environment types parameters.
     * </p>
     * @param envTypesParams
     * @return a List of Environment types based on the vector of Environment types parameters.
     * @see #toEnvironmentType(Vector)
     */
    @SuppressWarnings("unchecked")
    public static TreeSet<EnvironmentType> toEnvironmentTypeList(Vector<Object> envTypesParams)
    {
        TreeSet<EnvironmentType> envTypes = new TreeSet<EnvironmentType>();
        for(Object envTypeParams : envTypesParams)
        {
        	envTypes.add(toEnvironmentType((Vector<Object>)envTypeParams));
        }
        
        return envTypes;
    }
    
    /**
     * Rebuild a List of systemUnderTests based on the vector of systemUnderTests parameters.
     * </p>
     * @param sutsParams
     * @return a List of systemUnderTests based on the vector of systemUnderTests parameters.
     * @see #toSystemUnderTest(Vector)
     */
    @SuppressWarnings("unchecked")
    public static SortedSet<SystemUnderTest> toSystemUnderTestList(Vector<Object> sutsParams)
    {
        SortedSet<SystemUnderTest> suts = new TreeSet<SystemUnderTest>(new SystemUnderTestByNameComparator());
        for(Object sutParams : sutsParams)
        {
            suts.add(toSystemUnderTest((Vector<Object>)sutParams));
        }
        
        return suts;
    }
    
    /**
     * Rebuild a List of specifications based on the vector of specifications parameters.
     * </p>
     * @param specificationsParams
     * @return a List of specifications based on the vector of specifications parameters.
     * @see #toSpecification(Vector)
     */
    @SuppressWarnings("unchecked")
    public static Set<Specification> toSpecificationList(Vector<Object> specificationsParams)
    {
        Set<Specification> specifications = new TreeSet<Specification>();
        for(Object specificationParams : specificationsParams)
        {
            specifications.add(toSpecification((Vector<Object>)specificationParams));
        }
        
        return specifications;
    }
    
    /**
     * Rebuild a List of References with theire last execution based on the vector of References parameters.
     * </p>
     * @param referencesParams
     * @return a List of References based on the vector of References parameters.
     * @throws GreenPepperServerException 
     * @see #toReference(Vector)
     */
    @SuppressWarnings("unchecked")
    public static Set<Reference> toReferencesList(Vector<Object> referencesParams) throws GreenPepperServerException
    {
        Set<Reference> references = new TreeSet<Reference>();
        for(Object referenceParams : referencesParams)
        {
            references.add(toReference((Vector<Object>)referenceParams));
        }
        
        return references;
    }

    /**
     * Rebuilds a DocumentNode based on the given vector. 
     * </p>
     * @param documentNodeParams
     * @return a DocumentNode based on the given vector. 
     */
    public static DocumentNode toDocumentNode(Vector documentNodeParams)
    {
        DocumentNode node = new DocumentNode((String) documentNodeParams.get(NODE_TITLE_INDEX));
        node.setIsExecutable((Boolean) documentNodeParams.get(NODE_EXECUTABLE_INDEX));
        node.setCanBeImplemented((Boolean) documentNodeParams.get(NODE_CAN_BE_IMPLEMENTED_INDEX));
        
        Hashtable children = (Hashtable) documentNodeParams.get(NODE_CHILDREN_INDEX);
        Collection<Vector<?>> values = children.values();
        for (Vector<?> nodeParams : values) {
            
        	if(nodeParams.size() > 4)
        	{
        		node.addChildren(toReferenceNode(nodeParams));
        	}
        	else
        	{
        		node.addChildren(toDocumentNode(nodeParams));
        	}
        }

        return node;
    }
    
	private static ReferenceNode toReferenceNode(Vector referenceNodeParams) 
	{
		ReferenceNode node = new ReferenceNode((String) referenceNodeParams.get(NODE_TITLE_INDEX),
											   (String) referenceNodeParams.get(NODE_REPOSITORY_UID_INDEX), 
											   (String) referenceNodeParams.get(NODE_SUT_NAME_INDEX),
											   (String) referenceNodeParams.get(NODE_SECTION_INDEX));
		
        node.setIsExecutable((Boolean) referenceNodeParams.get(NODE_EXECUTABLE_INDEX));
        node.setCanBeImplemented((Boolean) referenceNodeParams.get(NODE_CAN_BE_IMPLEMENTED_INDEX));
		
		return node;
	}

    /**
     * Wraps the error message id into a String.<br>
     * Structure of the error:<br>
     * TAG_ERROR errorId
     * </p>
     * @param msgId
     * @returna the error message id as a String.
     */
    public static String errorAsString(String msgId)
    {
        return GreenPepperServerErrorKey.ERROR + msgId;
    }
    
    /**
     * Wraps the error message id into a Vector of Vector of String.<br>
     * Structure of the error:<br>
     * Vector[Vector[TAG_ERROR errorId]]
     * </p>
     * @param msgId
     * @returna the error message id as a Vector.
     */
    public static Vector<Object> errorAsVector(String msgId)
    {
        Vector<Object> err = new Vector<Object>();
        err.add(errorAsString(msgId));
        return err;
    }
    
    /**
     * Wraps the error message id into a Hashtable where exception is the key and
     * the value is a Vector of Vector of String.<br>
     * Structure of the error:<br>
     * Hashtable[TAG_ERROR, Vector[Vector[TAG_ERROR errorId]]]
     * </p>
     * @param msgId
     * @returna the error message id as a Hashtable.
     */
    public static Hashtable<String,Vector<Object>> errorAsHastable(String msgId)
    {
        Hashtable<String,Vector<Object>> table = new Hashtable<String,Vector<Object>>();
        table.put(GreenPepperServerErrorKey.ERROR, errorAsVector(msgId));
        return table;
    }
    
    /**
     * Checks if the XML-RPC response is an GreenPepper server Exception.
     * If so an GreenPepperServerException will be thrown with the error id found.
     * </p>
     * @param xmlRpcResponse
     * @throws GreenPepperServerException
     */
    public static void checkForErrors(Object xmlRpcResponse) throws GreenPepperServerException
    {
        if (xmlRpcResponse instanceof Vector)
        {
            Vector temp = (Vector)xmlRpcResponse;
            if(!temp.isEmpty())
            {
                checkErrors(temp.elementAt(0));
            }
        }
        else if (xmlRpcResponse instanceof Hashtable)
        {
            Hashtable<String, ?> table = (Hashtable<String, ?>)xmlRpcResponse;
            if(!table.isEmpty())
            {
                checkForErrors(table.get(GreenPepperServerErrorKey.ERROR));
            }
        }
        else
        {
            checkErrors(xmlRpcResponse);
        }
    }

    public static Object padNull(String str)
    {
        return (str == null) ? "" : str;
    }

	public static String toNullIfEmpty(String str)
	{
		return StringUtils.trimToNull(str);
	}

	/**
     * Checks if the message is an GreenPepper server tagged Exception.
     * If so an GreenPepperServerException will be thrown with the error id found.
     * </p>
     * @param object the error id found.
     * @throws GreenPepperServerException
     */
    private static void checkErrors(Object object) throws GreenPepperServerException
    {
        if (object instanceof Exception)
        {
            throw new GreenPepperServerException(GreenPepperServerErrorKey.CALL_FAILED, ((Exception)object).getMessage());
        }
        
        if (object instanceof String)
        {
            String msg = (String)object;
            if (!StringUtils.isEmpty(msg) && msg.indexOf(GreenPepperServerErrorKey.ERROR) > -1)
            {
                String errorId = msg.replace(GreenPepperServerErrorKey.ERROR, "");
                if(errorId.startsWith(LicenseErrorKey.LICENSE))
                {
                    throw new GreenPepperLicenceException(errorId, errorId);
                }
                
                throw new GreenPepperServerException(errorId, errorId);
            }
        }
    }
}