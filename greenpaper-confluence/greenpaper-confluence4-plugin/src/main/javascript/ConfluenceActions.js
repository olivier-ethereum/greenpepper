GP.ConfluenceActions=
{
	refreshHeader: function(list){ new Ajax.Updater("greenpepper_header_display", list.params.ctx + "/greenpepper/GreenPepperHeader.action", this.query(list, {spaceKey:GP.View.getInnerValue('conf_spaceKey_display'+list.id), pageId:GP.View.getInnerValue('conf_pageId_display'+list.id), implemented:GP.View.getInnerValue('conf_implemented_display'+list.id), showList:GP.View.isVisible('conf_specificationList_display'+list.id)})); },  
	refreshChildren:function(list, allChildren){ new Ajax.Updater("conf"+list.id, list.params.ctx + "/greenpepper/RefreshChildren.action", this.query(list, {spaceKey:GP.View.getInnerValue('conf_spaceKey_display'+list.id), pageId:GP.View.getInnerValue('conf_pageId_display'+list.id), forcedSuts:GP.View.getInnerValue('conf_forcedSuts_display'+list.id), showList:GP.View.isVisible('conf_specificationList_display'+list.id), allChildren:allChildren, sortType:$('sortType'+list.id).value, reverse:$('reverse'+list.id).value})); },
	refreshLabels:function(list){ new Ajax.Updater("conf"+list.id, list.params.ctx + "/greenpepper/RefreshLabels.action", this.query(list, {spaceKey:GP.View.getInnerValue('conf_spaceKey_display'+list.id), showList:GP.View.isVisible('conf_specificationList_display'+list.id), forcedSuts:GP.View.getInnerValue('conf_forcedSuts_display'+list.id), labels:GP.View.getInnerValue('conf_labels_display'+list.id), sortType:$('sortType'+list.id).value, reverse:$('reverse'+list.id).value, openInSameWindow:$('openInSameWindow'+list.id).value})); },
	updateExecuteChildren: function(specification){ new Ajax.Updater("conf_"+specification.params.bulkUID+"_"+specification.params.executionUID, specification.params.ctx + "/greenpepper/UpdateExecuteChildren.action", this.query(specification, {doExecuteChildren:$('conf_childrenInput').checked})); },
	setAsImplemented: function(specification){ new Ajax.Updater("greenpepper_header_display", specification.params.ctx + "/greenpepper/SetAsImplemented.action", this.query(specification)); },
	revert: function(specification, implemented){ new Ajax.Updater("greenpepper_header_display", specification.params.ctx + "/greenpepper/Revert.action", this.query(specification, {implemented:implemented, retrieveBody:implemented})); },
	retrieveHeader: function(specification, implemented){ new Ajax.Updater("greenpepper_header_display", specification.params.ctx + "/greenpepper/GreenPepperHeader.action", this.query(specification, {implemented:implemented, retrieveBody:true})); },
	retrieveBody: function(specification, implemented){ new Ajax.Updater("body", specification.params.ctx + "/greenpepper/RetrieveBody.action", this.query(specification, {implemented:implemented})); },
	editSelectedSut: function(specification){ new Ajax.Updater("conf_sut_display"+specification.id, specification.params.ctx + "/greenpepper/GetSutSelection.action", this.query(specification, {isEditMode:true, isSutEditable:true})); },
	cancelEditSutSelection: function(specification){ new Ajax.Updater("conf_sut_display"+specification.id, specification.params.ctx + "/greenpepper/GetSutSelection.action", this.query(specification, {isEditMode:false, isSutEditable:true})); },
	updateSelectedSut: function(specification){ new Ajax.Updater("conf_sut_display"+specification.id, specification.params.ctx + "/greenpepper/UpdateSelectedSut.action", this.query(specification, {refreshAll:true, isSutEditable:true, selectedSystemUnderTestInfo:$F('conf_suts_select'+specification.id), showList:GP.View.isVisible('conf_specificationList_display_'+specification.params.bulkUID + "_"+specification.params.executionUID)})); },
	getConfiguration: function(specification){ new Ajax.Updater("conf_configPopup_display"+specification.id, specification.params.ctx + "/greenpepper/RetrieveSutConfiguration.action", this.query(specification)); },
	addSpecSut: function(specification, sutProjectName, sutName){ new Ajax.Updater("conf_configPopup_display"+specification.id, specification.params.ctx + "/greenpepper/AddSpecSystemUnderTest.action", this.query(specification, {sutProjectName:sutProjectName, sutName:sutName, refreshAll:true})); },
	removeSpecSut: function(specification, sutProjectName, sutName){ new Ajax.Updater("conf_configPopup_display"+specification.id, specification.params.ctx + "/greenpepper/RemoveSpecSystemUnderTest.action", this.query(specification, {sutProjectName:sutProjectName, sutName:sutName, refreshAll:true})); },
	getReferenceList: function(specification, isEditMode){ new Ajax.Updater("conf_referenceList_display"+specification.id, specification.params.ctx + "/greenpepper/RetrieveReferenceList.action", this.query(specification, {isEditMode:isEditMode})); },
	addReference: function(specification){ new Ajax.Updater("conf_referenceList_display"+specification.id, specification.params.ctx + "/greenpepper/AddReference.action", this.query(specification, {refreshAll:true, sutInfo:$F('sut_select'), repositoryUid:$F('repository_select'), requirementName:$F('reqName_field'), sections:$F('reqSections_field'), isEditMode:true})); },
	removeReference: function(specification, sutProjectName, sutName, repositoryUid, requirementName, sections){ new Ajax.Updater("conf_referenceList_display"+specification.id, specification.params.ctx + "/greenpepper/RemoveReference.action", this.query(specification, {sutProjectName:sutProjectName, sutName:sutName, repositoryUid:repositoryUid, requirementName:requirementName, sections:sections})); },
	run: function(specification, implemented){ new Ajax.Updater("conf_results"+specification.id, specification.params.ctx + "/greenpepper/Run.action", this.query(specification, {implemented:(implemented ? true : false), sutInfo:$('conf_sutInfo_display'+specification.id).innerHTML})); },
	
	query:	function(obj, addParams)
	{ 
		return {method:'post', parameters:(addParams ? obj.params.merge(addParams) : obj.params).toQueryString(), onComplete:obj.notifyDoneWorking.bind(obj), onException:obj.notifyError.bind(obj), onFailure:obj.notifyError.bind(obj), evalScripts:true}; 
	},

	getLicensePane: function(params) { this.confQuery("license_display", "/greenpepper/GetLicensePane.action", params); },
	uploadLicense: function(params){ this.confQuery("license_display", "/greenpepper/UploadLicense.action", params); },
	
	getRunnersPane:function(params) { this.confQuery("runnersPane_display", "/greenpepper/GetRunnersPane.action", params); },
	getRunner:function(params) { this.confQuery("runnersPane_display", "/greenpepper/GetRunnersPane.action", params); },
	addRunner:function(params) { this.confQuery("runnersPane_display", "/greenpepper/AddRunner.action", params); },
	removeRunner:function(params) { this.confQuery("runnersPane_display", "/greenpepper/RemoveRunner.action", params); },
	editRunnerProperties:function(params) { this.confQuery("runnersPane_display", "/greenpepper/EditRunnerProperties.action", params); },
	updateRunnerProperties:function(params) { this.confQuery("runnersPane_display", "/greenpepper/UpdateRunnerProperties.action", params); },
	editRunnerClasspaths:function(params) { this.confQuery("runnersPane_display", "/greenpepper/EditRunnerClasspaths.action", params); },
	editRunnerClasspath:function(params){ this.confQuery("runnersPane_display", "/greenpepper/EditRunnerClasspath.action", params); },
	
	getRegistration:function(params) { this.confQuery("registrationPane_display", "/greenpepper/GetRegistration.action", params); },
	editRegistration:function(params) { this.confQuery("registrationPane_display", "/greenpepper/EditRegistration.action", params); },
	refreshRegistration:function(params) { this.confQuery("registrationPane_display", "/greenpepper/RefreshEditRegistration.action", params); },
	register:function(params) { this.confQuery("registrationPane_display", "/greenpepper/Register.action", params); },
	updateRegistration:function(params) { this.confQuery("registrationPane_display", "/greenpepper/UpdateRegistration.action", params); },
	
	getFileSystemRegistration:function(params) { this.confQuery("fileSystemPane_display", "/greenpepper/GetFileSystemRegistration.action", params); },
	editFileSystem:function(params) { this.confQuery("fileSystemPane_display", "/greenpepper/EditFileSystem.action", params); },
	addFileSystem:function(params) { this.confQuery("fileSystemPane_display", "/greenpepper/AddFileSystem.action", params); },
	removeFileSystem:function(params) { this.confQuery("fileSystemPane_display", "/greenpepper/RemoveFileSystem.action", params); },
	
	getSutsPane:function(params) { this.confQuery("sutsPane_display", "/greenpepper/GetSutsPane.action", params); },
	getSut:function(params) { this.confQuery("sutsPane_display", "/greenpepper/GetSutsPane.action", params); },
	addSut:function(params) { this.confQuery("sutsPane_display", "/greenpepper/AddSystemUnderTest.action", params); },
	removeSut:function(params) { this.confQuery("sutsPane_display", "/greenpepper/RemoveSystemUnderTest.action", params); },
	editSutProperties:function(params) { this.confQuery("sutsPane_display", "/greenpepper/EditSutProperties.action", params); },
	updateSutProperties:function(params) { this.confQuery("sutsPane_display", "/greenpepper/UpdateSystemUnderTest.action", params); },
	editSutClasspaths:function(params) { this.confQuery("sutsPane_display", "/greenpepper/EditSutClasspaths.action", params); },
	editSutClasspath:function(params){ this.confQuery("sutsPane_display", "/greenpepper/EditSutClasspath.action", params); },
	editSutFixtures:function(params) { this.confQuery("sutsPane_display", "/greenpepper/EditSutFixtures.action", params); },
	editSutFixture:function(params) { this.confQuery("sutsPane_display", "/greenpepper/EditSutFixture.action", params); },
	setSutAsDefault:function(params){ this.confQuery("sutsPane_display", "/greenpepper/SetAsDefault.action", params); },
	getGpProjectPane:function(params){this.confQuery("GpProjectPane_display", "/greenpepper/GetGpProjectPane.action", params);},
	
	getInstallWizardPane:function(params){this.confQuery("dbmsConfigPane_display", "/greenpepper/GetDbmsPane.action", params); },
	changeInstallationType:function(params){ this.confQuery("dbmsChoice_display", "/greenpepper/ChangeInstallType.action", params); },
	updateDbmsConfiguration:function(params){ this.confQuery("dbmsConfigPane_display", "/greenpepper/EditDbmsConfiguration.action", params); },
	testDbmsConnection:function(params){ this.confQuery("testConnection_display", "/greenpepper/TestDbmsConnection.action", params); },

	getDemoPane:function(params) { this.confQuery("demoPane_display", "/greenpepper/GetDemoPane.action", params); },
	createDemoSpace:function(params){ this.confQuery("demoPane_display", "/greenpepper/CreateDemoSpace.action", params); },
	removeDemoSpace:function(params){ this.confQuery("demoPane_display", "/greenpepper/RemoveDemoSpace.action", params); },

	confQuery:	function(elementId, actionName, params)
	{ 
		if(params.id){ params = params.merge({spaceKey:params.id}); }
		new Ajax.Updater(elementId, params.ctx + actionName, { method: 'post', parameters: params.toQueryString(), evalScripts: true });
	}
};