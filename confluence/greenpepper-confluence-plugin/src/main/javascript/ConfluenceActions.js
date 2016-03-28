GP.ConfluenceActions =
{
    refreshHeader: function (list) {
        this.query("greenpepper_header_display", list.params.ctx + "/ajax/greenpepper/GreenPepperHeader.action", list, {
            spaceKey: GP.View.getInnerValue('conf_spaceKey_display' + list.id),
            pageId: GP.View.getInnerValue('conf_pageId_display' + list.id),
            implemented: GP.View.getInnerValue('conf_implemented_display' + list.id),
            showList: GP.View.isVisible('conf_specificationList_display' + list.id)
        });
    },
    refreshChildren: function (list, allChildren) {
        this.query("conf" + list.id, list.params.ctx + "/ajax/greenpepper/RefreshChildren.action", list, {
            spaceKey: GP.View.getInnerValue('conf_spaceKey_display' + list.id),
            pageId: GP.View.getInnerValue('conf_pageId_display' + list.id),
            forcedSuts: GP.View.getInnerValue('conf_forcedSuts_display' + list.id),
            showList: GP.View.isVisible('conf_specificationList_display' + list.id),
            allChildren: allChildren,
            sortType: AJS.$('#sortType' + list.id).val(),
            reverse: AJS.$('#reverse' + list.id).val()
        });
    },
    refreshLabels: function (list) {
        this.query("conf" + list.id, list.params.ctx + "/ajax/greenpepper/RefreshLabels.action", list, {
            spaceKey: GP.View.getInnerValue('conf_spaceKey_display' + list.id),
            showList: GP.View.isVisible('conf_specificationList_display' + list.id),
            forcedSuts: GP.View.getInnerValue('conf_forcedSuts_display' + list.id),
            labels: GP.View.getInnerValue('conf_labels_display' + list.id),
            sortType: AJS.$('#sortType' + list.id).val(),
            reverse: AJS.$('#reverse' + list.id).val(),
            openInSameWindow: AJS.$('#openInSameWindow' + list.id).val()
        });
    },
    updateExecuteChildren: function (specification) {
        this.query("conf_" + specification.params.bulkUID + "_" + specification.params.executionUID, specification.params.ctx + "/ajax/greenpepper/UpdateExecuteChildren.action", specification, {doExecuteChildren: AJS.$('#conf_childrenInput').is(':checked')});
    },
    setAsImplemented: function (specification) {
        this.query("greenpepper_header_display", specification.params.ctx + "/ajax/greenpepper/SetAsImplemented.action", specification);
    },
    revert: function (specification, implemented) {
        this.query("greenpepper_header_display", specification.params.ctx + "/ajax/greenpepper/Revert.action", specification, {
            implemented: implemented,
            retrieveBody: implemented
        });
    },
    retrieveHeader: function (specification, implemented) {
        this.query("greenpepper_header_display", specification.params.ctx + "/ajax/greenpepper/GreenPepperHeader.action", specification, {
            implemented: implemented,
            retrieveBody: true
        });
    },
    retrieveBody: function (specification, implemented) {
        this.query("body", specification.params.ctx + "/ajax/greenpepper/RetrieveBody.action", specification, {implemented: implemented});
    },
    editSelectedSut: function (specification) {
        this.query("conf_sut_display" + specification.id, specification.params.ctx + "/ajax/greenpepper/GetSutSelection.action", specification, {
            isEditMode: true,
            isSutEditable: true
        });
    },
    cancelEditSutSelection: function (specification) {
        this.query("conf_sut_display" + specification.id, specification.params.ctx + "/ajax/greenpepper/GetSutSelection.action", specification, {
            isEditMode: false,
            isSutEditable: true
        });
    },
    updateSelectedSut: function (specification) {
        this.query("conf_sut_display" + specification.id, specification.params.ctx + "/ajax/greenpepper/UpdateSelectedSut.action", specification, {
            refreshAll: true,
            isSutEditable: true,
            selectedSystemUnderTestInfo: $F('conf_suts_select' + specification.id),
            showList: GP.View.isVisible('conf_specificationList_display_' + specification.params.bulkUID + "_" + specification.params.executionUID)
        });
    },
    getConfiguration: function (specification) {
        this.query("conf_configPopup_display" + specification.id, specification.params.ctx + "/ajax/greenpepper/RetrieveSutConfiguration.action", specification);
    },
    addSpecSut: function (specification, sutProjectName, sutName) {
        this.query("conf_configPopup_display" + specification.id, specification.params.ctx + "/ajax/greenpepper/AddSpecSystemUnderTest.action", specification, {
            sutProjectName: sutProjectName,
            sutName: sutName,
            refreshAll: true
        });
    },
    removeSpecSut: function (specification, sutProjectName, sutName) {
        this.query("conf_configPopup_display" + specification.id, specification.params.ctx + "/ajax/greenpepper/RemoveSpecSystemUnderTest.action", specification, {
            sutProjectName: sutProjectName,
            sutName: sutName,
            refreshAll: true
        });
    },
    getReferenceList: function (specification, isEditMode) {
        this.query("conf_referenceList_display" + specification.id, specification.params.ctx + "/ajax/greenpepper/RetrieveReferenceList.action", specification, {isEditMode: isEditMode});
    },
    addReference: function (specification) {
        this.query("conf_referenceList_display" + specification.id, specification.params.ctx + "/ajax/greenpepper/AddReference.action", specification, {
            refreshAll: true,
            sutInfo: $F('sut_select'),
            repositoryUid: $F('repository_select'),
            requirementName: $F('reqName_field'),
            sections: $F('reqSections_field'),
            isEditMode: true
        });
    },
    removeReference: function (specification, sutProjectName, sutName, repositoryUid, requirementName, sections) {
        this.query("conf_referenceList_display" + specification.id, specification.params.ctx + "/ajax/greenpepper/RemoveReference.action", specification, {
            sutProjectName: sutProjectName,
            sutName: sutName,
            repositoryUid: repositoryUid,
            requirementName: requirementName,
            sections: sections
        });
    },
    run: function (specification, implemented) {
        this.query("conf_results" + specification.id, specification.params.ctx + "/ajax/greenpepper/Run.action", specification, {
            implemented: (implemented ? true : false),
            sutInfo: AJS.$('#conf_sutInfo_display' + specification.id).html()
        });
    },

    query: function (elementId, url, obj, addParams) {
        var extendedParams = addParams ? AJS.$.extend({}, obj.params, addParams) : obj.params;
        var preparedParams = this.paramsToObject(extendedParams);
        AJS.$.ajax({
            url: url,
            type: 'POST',
            data: preparedParams,
            dataType: 'html',
            error: obj.notifyError.bind(obj),
            complete: function (jqXHR) {
                obj.notifyDoneWorking();
                AJS.$('#' + elementId).html(jqXHR.responseText);
            }
        });
    },

    getLicensePane: function(params) { this.confQuery("license_display", "/ajax/greenpepper/GetLicensePane.action", params); },
    uploadLicense: function(params){ this.confQuery("license_display", "/ajax/greenpepper/UploadLicense.action", params); },

    getRunnersPane: function (params) {
        this.confQuery("runnersPane_display", "/ajax/greenpepper/GetRunnersPane.action", params);
    },
    getRunner: function (params) {
        this.confQuery("runnersPane_display", "/ajax/greenpepper/GetRunnersPane.action", params);
    },
    addRunner: function (params) {
        this.confQuery("runnersPane_display", "/ajax/greenpepper/AddRunner.action", params);
    },
    removeRunner: function (params) {
        this.confQuery("runnersPane_display", "/ajax/greenpepper/RemoveRunner.action", params);
    },
    editRunnerProperties: function (params) {
        this.confQuery("runnersPane_display", "/ajax/greenpepper/EditRunnerProperties.action", params);
    },
    updateRunnerProperties: function (params) {
        this.confQuery("runnersPane_display", "/ajax/greenpepper/UpdateRunnerProperties.action", params);
    },
    editRunnerClasspaths: function (params) {
        this.confQuery("runnersPane_display", "/ajax/greenpepper/EditRunnerClasspaths.action", params);
    },
    editRunnerClasspath: function (params) {
        this.confQuery("runnersPane_display", "/ajax/greenpepper/EditRunnerClasspath.action", params);
    },

    getRegistration: function (params) {
        this.confQuery("registrationPane_display", "/ajax/greenpepper/GetRegistration.action", params);
    },
    editRegistration: function (params) {
        this.confQuery("registrationPane_display", "/ajax/greenpepper/EditRegistration.action", params);
    },
    refreshRegistration: function (params) {
        this.confQuery("registrationPane_display", "/ajax/greenpepper/RefreshEditRegistration.action", params);
    },
    register: function (params) {
        this.confQuery("registrationPane_display", "/ajax/greenpepper/Register.action", params);
    },
    updateRegistration: function (params) {
        this.confQuery("registrationPane_display", "/ajax/greenpepper/UpdateRegistration.action", params);
    },

    getFileSystemRegistration: function (params) {
        this.confQuery("fileSystemPane_display", "/ajax/greenpepper/GetFileSystemRegistration.action", params);
    },
    editFileSystem: function (params) {
        this.confQuery("fileSystemPane_display", "/ajax/greenpepper/EditFileSystem.action", params);
    },
    addFileSystem: function (params) {
        this.confQuery("fileSystemPane_display", "/ajax/greenpepper/AddFileSystem.action", params);
    },
    removeFileSystem: function (params) {
        this.confQuery("fileSystemPane_display", "/ajax/greenpepper/RemoveFileSystem.action", params);
    },

    getSutsPane: function (params) {
        this.confQuery("sutsPane_display", "/ajax/greenpepper/GetSutsPane.action", params);
    },
    getSut: function (params) {
        this.confQuery("sutsPane_display", "/ajax/greenpepper/GetSutsPane.action", params);
    },
    addSut: function (params) {
        this.confQuery("sutsPane_display", "/ajax/greenpepper/AddSystemUnderTest.action", params);
    },
    removeSut: function (params) {
        this.confQuery("sutsPane_display", "/ajax/greenpepper/RemoveSystemUnderTest.action", params);
    },
    editSutProperties: function (params) {
        this.confQuery("sutsPane_display", "/ajax/greenpepper/EditSutProperties.action", params);
    },
    updateSutProperties: function (params) {
        this.confQuery("sutsPane_display", "/ajax/greenpepper/UpdateSystemUnderTest.action", params);
    },
    editSutClasspaths: function (params) {
        this.confQuery("sutsPane_display", "/ajax/greenpepper/EditSutClasspaths.action", params);
    },
    editSutClasspath: function (params) {
        this.confQuery("sutsPane_display", "/ajax/greenpepper/EditSutClasspath.action", params);
    },
    editSutFixtures: function (params) {
        this.confQuery("sutsPane_display", "/ajax/greenpepper/EditSutFixtures.action", params);
    },
    editSutFixture: function (params) {
        this.confQuery("sutsPane_display", "/ajax/greenpepper/EditSutFixture.action", params);
    },
    setSutAsDefault: function (params) {
        this.confQuery("sutsPane_display", "/ajax/greenpepper/SetAsDefault.action", params);
    },
    getGpProjectPane: function (params) {
        this.confQuery("GpProjectPane_display", "/ajax/greenpepper/GetGpProjectPane.action", params);
    },

    getInstallWizardPane: function (params) {
        this.confQuery("dbmsConfigPane_display", "/ajax/greenpepper/GetDbmsPane.action", params);
    },
    changeInstallationType: function (params) {
        this.confQuery("dbmsChoice_display", "/ajax/greenpepper/ChangeInstallType.action", params);
    },
    updateDbmsConfiguration: function (params) {
        this.confQuery("dbmsConfigPane_display", "/ajax/greenpepper/EditDbmsConfiguration.action", params);
    },
    testDbmsConnection: function (params) {
        this.confQuery("testConnection_display", "/ajax/greenpepper/TestDbmsConnection.action", params);
    },

    getDemoPane: function (params) {
        this.confQuery("demoPane_display", "/ajax/greenpepper/GetDemoPane.action", params);
    },
    createDemoSpace: function (params) {
        this.confQuery("demoPane_display", "/ajax/greenpepper/CreateDemoSpace.action", params);
    },
    removeDemoSpace: function (params) {
        this.confQuery("demoPane_display", "/ajax/greenpepper/RemoveDemoSpace.action", params);
    },

    confQuery: function (elementId, actionName, params) {
        var extendedParams = params;
        if (params.id) {
            extendedParams = AJS.$.extend({}, extendedParams, {spaceKey: params.id});
        }
        var preparedParams = this.paramsToObject(extendedParams);
        AJS.$.ajax({
            url: preparedParams.ctx + actionName,
            type: 'POST',
            data: preparedParams,
            dataType: 'html',
            complete: function (jqXHR) {
                AJS.$('#' + elementId).html(jqXHR.responseText);
            }
        });
    },

    paramsToObject: function (params) {
        var paramObject = {};
        AJS.$.each(params, function (key, value) {

            if (value != null && typeof value != 'undefined')
                paramObject[key] = value;

        });
        return paramObject;
    }
};