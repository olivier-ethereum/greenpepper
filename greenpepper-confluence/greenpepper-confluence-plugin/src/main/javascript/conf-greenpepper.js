var conf_greenPepper = new function()
{
	this.bulks= $M();
	
	this.loadHeader=function(ctx, spaceKey, pageId)
	{ 
		new Ajax.Updater('greenpepper_header_display', ctx + "/greenpepper/GreenPepperHeader.action", {method: 'post', parameters: "decorator=none&spaceKey=" + spaceKey + "&pageId=" + pageId, evalScripts: true});
	};
	this.greenPepperize=function(ctx, bUID, exeUID, spaceKey, pageId, refresh)
	{
		if($('conf_exeflag') && !$('conf_exeflag').checked)
		{
			this.getBulk(bUID).removeList(exeUID); 
			GP.View.switchView('body', 'conf_results_'+bUID+'_'+exeUID+'_0');
		}
		GP.View.switchView('conf_waiting_display_'+bUID+'_'+exeUID, 'conf_actionError_display_'+ bUID + '_'  + exeUID);
		new Ajax.Updater('greenpepper_header_display', ctx + "/greenpepper/GreenPepperize.action", {method: 'post', parameters: "decorator=none&spaceKey=" + spaceKey + "&pageId=" + pageId + "&refreshAll=true&pepperize=" + $('conf_exeflag').checked, evalScripts: true});
	};
	this.search=function(ctx, bUID, exeUID, openInSameWindow, forcedSuts, spaceKey)
	{ 
		if($F('conf_labels_' + bUID + '_' + exeUID) == ''){return;}
		if(!spaceKey){ spaceKey = $F('conf_spaces_select_' + bUID + '_' + exeUID); }
		var bulk = this.getBulk(bUID);
		if (bulk){ bulk.removeList(exeUID); }
		GP.View.hide('conf_statusbar_'+ bUID + '_'  + exeUID);
		GP.View.hide('conf_actionError_display_'+ bUID + '_'  + exeUID);
		GP.View.switchView('conf_waiting_display_'+bUID+'_'+exeUID, 'conf_actionError_display_'+ bUID + '_'  + exeUID);
		
		new Ajax.Updater('conf_' + bUID + '_' + exeUID, ctx + "/greenpepper/RefreshLabels.action", {method: 'post', parameters: "decorator=none&spaceKey=" + spaceKey + "&labels=" + $F('conf_labels_' + bUID + '_' + exeUID).replace(/&/g, "&amp;") + "&bulkUID=" + bUID + "&executionUID=" + exeUID + "&forcedSuts=" + forcedSuts +"&searchQuery=true" + "&openInSameWindow=" + openInSameWindow, evalScripts: true});
	};
	
	this.registerBulk=function(bUID, actions)
	{ 
		return (this.bulks[bUID] = new CONFBulk(bUID, actions));
	};
	this.registerList=function(bUID, exeUID, ctx, imgFile, actions)
	{ 
		var bulk = this.getBulk(bUID);
		if(!bulk){ bulk = this.registerBulk(bUID, actions); }
		return bulk.registerList($H({decorator:'none', bulkUID:bUID, executionUID:exeUID, imgFile:imgFile, ctx:ctx}));
	};
	this.registerSpecification=function(bUID, exeUID, ctx, imgFile, spaceKey, pageId, fieldId, actions, isMain)
	{
		var bulk = this.getBulk(bUID);
		if(!bulk){ bulk = this.registerBulk(bUID, actions); }
		var list = bulk.getList(exeUID); 
		if(!list){ list = bulk.registerList($H({decorator:'none', bulkUID:bUID, executionUID:exeUID, imgFile:imgFile, ctx:ctx})); }
		return list.registerSpecification($H({spaceKey:spaceKey, pageId:pageId, fieldId:fieldId, isMain:isMain?true:false}));
	};	

	this.getBulk=function(bUID){ return this.bulks[bUID]; };
	this.getList=function(bUID, exeUID){ return this.getBulk(bUID).getList(exeUID); };
	this.getSpecification=function(bUID, exeUID, fieldId)
	{ 
		var list = this.getList(bUID, exeUID); 
		return list.getSpecification(fieldId); 
	};
	this.runBulk=function(bUID){ try { (this.getBulk(bUID).runAll('HEADER')); }catch(ex){} };
	this.refreshAll=function(bUID, exeUID)
	{
		this.bulks.eachValue(function(value)
		{ 
			if(!bUID || (bUID && value.id != bUID))
			{ 
				value.refreshAll(); 
			}
			else
			{
				value.refreshAll(exeUID); 
			}
		}); 
	};
	this.openWindow=function(url)
	{
		var widthL; var heightL;
		var widthW = 800; var heightW = 600;
	  	if( typeof( window.innerWidth ) == 'number' ) 
	  	{
	    	widthL = (window.innerWidth - widthW)/2;
	    	heightL = (window.innerHeight - heightW)/2;
	  	} 
	  	else if( document.documentElement && ( document.documentElement.clientWidth || document.documentElement.clientHeight ) ) 
	  	{
	    	widthL = (document.documentElement.clientWidth - widthW)/2;
	    	heightL = (document.documentElement.clientHeight - heightW)/2;
	  	} 
	  	else if( document.body && ( document.body.clientWidth || document.body.clientHeight ) ) 
	  	{
	    	widthL = (document.body.clientWidth - widthW)/2;
	    	heightL = (document.body.clientHeight - heightW)/2;
	  	}
		window.open(url,"mywindow","left="+widthL+",top="+heightL+",width="+widthW+",height="+heightW+",resizable=1,toolbar=0,scrollbars=1");
	};
	this.isRunning=function()
	{ 
		var result = this.bulks.findValue(function(value){ 
			return value.isRunning(); 
		}); 
		return result;
	};
	this.notifyWorking=function(bUID, exeUID, fieldId)
	{ 
		var bulk = this.getBulk(bUID); 
		if(bulk)
		{
			bulk.working(); 
			var list = bulk.getList(exeUID);
			if(list)
			{
				list.working();
				if(fieldId)
				{
					var specification = list.getSpecification(fieldId);
					if(specification)
					{
						specification.working();
					}
				} 
			}
		}
	};
	this.notifyDoneWorking=function(bUID, exeUID, fieldId)
	{
		var bulk = this.getBulk(bUID); 
		if(bulk)
		{
			var list = bulk.getList(exeUID);
			if(list)
			{
				if(fieldId)
				{
					var specification = list.getSpecification(fieldId);
					if(specification)
					{
						specification.doneWorking();
					}
				}
				list.doneWorking(); 
			}
			bulk.doneWorking(); 
		}
	};
	this.notifyError=function(XMLHttpRequest, err, bUID, exeUID, fieldId)
	{ 
		var bulk = this.getBulk(bUID); 
		if(bulk)
		{
			var list = bulk.getList(exeUID);
			if(list)
			{
				if(fieldId)
				{
					var specification = list.getSpecification(fieldId);
					if(specification)
					{
						specification.error(XMLHttpRequest, err);
					}
				}
				list.error(XMLHttpRequest, err); 
			}
			bulk.error(XMLHttpRequest, err); 
		}
	};
	this.oneSutEditionAtATime=function()
	{		
		var iterator = $$('span[id="conf_sutInfo_cancelEditSelected"]');
		
		var browser=navigator.appName;
		try {
		  
		  for(i = 0; i < iterator.length ; i++)
		  {
		  	if ( browser == "Microsoft Internet Explorer" )
		  	{
				iterator[i].fireEvent("onclick"); 		  
		  	}
		    else
		    {
			    var evt = document.createEvent("MouseEvents");
	      		evt.initMouseEvent("click", true, true, window,
	      							0, 0, 0, 0, 0, false, false, false, false, 0, null);
	    		iterator[i].dispatchEvent(evt);
		    }
		  }
		  
		}
		catch (e) {
		  dump( 'Error: Document tree modified during iteration ' + e );
		}
		
	};
};

var CONFBulk = Class.create();
CONFBulk.prototype = 
{
	initialize:function(bUID, actions)
	{
		this.actions=actions;
		this.lists= $M();
		this.id=bUID;
	},
	
	getList:function(exeUID){ return this.lists[exeUID]; },
	removeList:function(exeUID){ this.lists.remove(exeUID); },
	registerList:function(params){ return (this.lists[params.executionUID] = new CONFSpecificationList(params, this.actions)); },
	isRunning:function()
	{	
		return this.lists.findValue(function(value){ 
			return value.isRunning(); 
		}); 
	},
	runAll:function(skipExeUID)
	{ 
		GP.View.appear('conf_waiting_display_'+this.id, 1);
		this.lists.eachValue(function(value)
		{ 
			if(!skipExeUID || (skipExeUID && value.params.executionUID != skipExeUID))
			{ 
				value.runAll(); 
			}
		}); 
	},
	refreshAll:function(exeUID)
	{ 
		this.lists.eachValue(function(value)
		{ 
			if(!exeUID || (exeUID && value.params.executionUID != exeUID))
			{ 
				value.refresh(); 
			}
		}); 
	},
	working:function()
	{ 
		if(!this.isRunning())
		{
			GP.View.switchShadowedView('conf_exeButton_shadow_'+this.id, 'conf_exeButton_'+this.id);
		}
	},
	doneWorking:function()
	{
		if(!this.isRunning())
		{
			GP.View.fade('conf_waiting_display_'+this.id, 0.3);
			GP.View.switchShadowedView('conf_exeButton_'+this.id, 'conf_exeButton_shadow_'+this.id);
		}
	 },
	error:function(XMLHttpRequest, err){ this.doneWorking(); },
	
	notifyWorking:function(){ conf_greenPepper.notifyWorking(this.id); },
	notifyDoneWorking:function(){ conf_greenPepper.notifyDoneWorking(this.id); },	
	notifyError:function(XMLHttpRequest, err){ conf_greenPepper.notifyError(XMLHttpRequest, err, this.id); }
};

var CONFSpecificationList = Class.create();
CONFSpecificationList.prototype = 
{
	initialize:function(params, actions)
	{
		this.count=0;
		this.specifications= $M();
		this.params=params; 
		this.actions=actions;
		this.id='_'+this.params.bulkUID+'_'+this.params.executionUID;
		this.statusBar=new StatusChart('conf_statusbar'+this.id, this.params.imgFile, 16, 2);
	},
	isHeader:function(){return new RegExp("HEADER", "i").exec(this.id) != null;},
	isLabel:function(){return new RegExp("LABEL", "i").exec(this.id) != null;},
	isChildren:function(){return new RegExp("CHILDREN", "i").exec(this.id) != null;},
	
	getSpecification:function(fieldId){ return this.specifications[fieldId]; },
	hasSpecification:function(spaceKey, pageId)
	{
		if(spaceKey == 'ANYONE' && pageId == 'ANYONE'){return true;}
		var result = this.specifications.findValue(function(value){ 
			return value.params.spaceKey == spaceKey && value.params.pageId == pageId; 
		}); 
		return result;
	},
	registerSpecification:function(specParams)
	{ 
		var specification = this.specifications[specParams.fieldId]=new CONFSpecification(this.params.merge(specParams), this.actions);
		return specification;
	},

	clear:function()
	{
		this.specifications.clear();
		this.statusBar.reset();
	},

	show:function()
	{
		GP.View.hide('conf_actionError_display'+this.id);
		GP.View.show('conf_specificationList_display'+this.id);
		GP.View.switchView('conf_colapse'+this.id, 'conf_expand'+this.id);
	},

	hide:function()
	{
		GP.View.hide('conf_actionError_display'+this.id);
		GP.View.hide('conf_specificationList_display'+this.id);
		GP.View.switchView('conf_expand'+this.id, 'conf_colapse'+this.id);
	},
	refresh:function()
	{
		this.notifyWorking();
		GP.View.hide('conf_statusbar'+this.id);
		this.clear();
		if(this.isHeader())
		{ 
			GP.View.switchView('body', 'conf_results'+this.id+'_0'); 
			this.actions.refreshHeader(this);
		}
		else if(this.isLabel())
		{ 
			this.actions.refreshLabels(this); 
		}
		else if(this.isChildren())
		{ 
			this.actions.refreshChildren(this, new RegExp("ALL", "i").exec(this.id) != null);
		}
	},
	
	isRunning:function()
	{	
		return this.count>0;
	},

	runAll:function()
	{
		this.statusBar.reset(true);
		this.specifications.eachValue(function(value){ value.prepareRun(); });
		this.specifications[0].run();
	},

	showAll:function()
	{
		this.show();
		this.specifications.eachValue(function(value){ value.showResults(); });
	},

	hideAll:function()
	{
		this.specifications.eachValue(function(value){ value.hideResults(); });
	},

	registerResults:function(hasException, success, failures, errors)
	{
		this.statusBar.register(hasException, success, failures, errors, this.specifications.size());
	},

	unregisterResults:function(hasException, success, failures, errors)
	{
		this.statusBar.unregister(hasException, success, failures, errors);
	},

	working:function()
	{
		if(!this.isRunning())
		{
			GP.View.show('conf_waiting_display'+this.id);
			GP.View.switchShadowedView('conf_exeButton_shadow'+this.id, 'conf_exeButton'+this.id);
			GP.View.switchShadowedView('conf_showAll_link_shadow'+this.id, 'conf_showAll_link'+this.id);
			GP.View.switchShadowedView('conf_hideAll_link_shadow'+this.id, 'conf_hideAll_link'+this.id);

			GP.View.hide('conf_actionError_display'+this.id);
			GP.View.hide('conf_error_display'+this.id);
		}
		
		this.count=this.count+1;
	},
	doneWorking:function()
	{
		this.count=Math.max(0, this.count-1);
		if(!this.isRunning())
		{
			GP.View.fade('conf_waiting_display'+this.id, 0.3);
			GP.View.switchShadowedView('conf_showAll_link'+this.id, 'conf_showAll_link_shadow'+this.id);
			GP.View.switchShadowedView('conf_hideAll_link'+this.id, 'conf_hideAll_link_shadow'+this.id);
			GP.View.switchShadowedView('conf_exeButton'+this.id, 'conf_exeButton_shadow'+this.id);
		}
	},
	error:function(XMLHttpRequest, err)
	{
		GP.View.write('conf_error_display'+this.id, 'System error: '+err);
		GP.View.switchView('conf_error_display'+this.id, 'conf_waiting_display'+this.id);
		this.doneWorking();
	},
	
	notifyWorking:function(){ conf_greenPepper.notifyWorking(this.params.bulkUID, this.params.executionUID); },
	notifyDoneWorking:function(){ conf_greenPepper.notifyDoneWorking(this.params.bulkUID, this.params.executionUID); },
	notifyError:function(XMLHttpRequest, err){ conf_greenPepper.notifyError(XMLHttpRequest, err, this.params.bulkUID, this.params.executionUID); }
};

var CONFSpecification = Class.create();
CONFSpecification.prototype = 
{
	initialize:function(params, actions)
	{
		this.params=params;
		this.actions=actions;
		this.hasException = false; this.success = 0; this.failures = 0; this.errors = 0; this.ignored = 0;
		this.id="_"+this.params.bulkUID+"_"+this.params.executionUID+"_"+this.params.fieldId;
	},
	isMain:function()
	{
		return this.id == '_PAGE_HEADER_0'
	},
	wasRunned:function()
	{ 
		return this.hasException || this.success != 0 || this.failures != 0 || this.errors != 0 || this.ignored != 0; 
	},
	wasIgnored:function()
	{ 
		return !this.hasException && this.success == 0 && this.failures == 0 && this.errors == 0 && this.ignored != 0; 
	},
	wasSuccessfull:function()
	{ 
		return !this.hasException && this.failures == 0 && this.errors == 0 && this.success != 0; 
	},
	hasFailed:function()
	{ 
		return this.hasException || this.failures != 0 || this.errors != 0; 
	},
	showResults:function()
	{
		GP.View.hide('conf_actionError_display_'+this.params.bulkUID + '_'  + this.params.executionUID);
		if(!this.wasRunned()){ return; }
		GP.View.show('conf_results'+this.id);
		GP.View.switchView('conf_hideResult_link'+this.id, 'conf_showResult_link'+this.id);
		if($('conf_specification'+this.id)){$('conf_specification'+this.id).style.border = '2px solid rgb(187, 187, 187)';}
		if(this.isMain()) GP.View.switchView('conf_results'+this.id, 'body');
	},
	hideResults:function()
	{
		GP.View.hide('conf_actionError_display_'+this.params.bulkUID + '_'  + this.params.executionUID);
		if(!this.wasRunned()){ return; }
		GP.View.switchView('conf_showResult_link'+this.id, 'conf_hideResult_link'+this.id);
		GP.View.hide('conf_results'+this.id);
		if($('conf_specification'+this.id)){$('conf_specification'+this.id).style.border = '0px solid #bbbbbb';}
		if(this.isMain()) GP.View.switchView('body', 'conf_results'+this.id);
	},
	prepareRun:function()
	{
		this.hideResults();
		this.notifyWorking();
		GP.View.setClassName('conf_statusBullet'+this.id, "conf_blankBullet");
		GP.View.show('conf_statusbar_'+this.params.bulkUID + '_'  + this.params.executionUID);
	},
	run:function(implemented)
	{
		this.actions.run(this, implemented);
	},
	registerResults:function(hasException, success, failures, errors, ignored)
	{
		this.hasException = hasException; this.success = success; this.failures = failures; this.errors = errors; this.ignored = ignored;
		if(this.wasRunned()){ GP.View.switchView('conf_statusbar_link_'+this.params.bulkUID + '_'  + this.params.executionUID, 'conf_statusbar_link_shadow_'+this.params.bulkUID + '_'  + this.params.executionUID); GP.View.show('conf_showResult_link'+this.id); }
		if(this.wasSuccessfull()){ GP.View.setClassName('conf_statusBullet'+this.id, "conf_successBullet"); }
		if(this.hasFailed()){ GP.View.setClassName('conf_statusBullet'+this.id, "conf_failedBullet"); }
		if(this.wasIgnored()){ GP.View.setClassName('conf_statusBullet'+this.id, "conf_ignoredBullet"); }
		conf_greenPepper.getList(this.params.bulkUID, this.params.executionUID).registerResults(hasException, success, failures, errors);
	},

	openConfigPopup:function()
	{
		this.notifyWorking();
		var popup = $('conf_configPopup'+this.id);
	    var pos = GP.View.findPos($('conf_configPopup_link'+this.id)); popup.style.top = pos[1] - 15; popup.style.left = pos[0] + 120;
	    popup.style.position = "absolute"; popup.style.visibility = "visible";
		popup.style.zIndex = "1000";
		GP.View.appear('conf_configPopup'+this.id, 0.7);
		this.actions.getConfiguration(this);
	},
	closeConfigPopup:function()
	{ 
		GP.View.fade('conf_configPopup'+this.id, 0.5); 
	},
	addSpecSut:function(sutProjectName, sutName)
	{ 
		this.notifyWorking();
		this.actions.addSpecSut(this, sutProjectName, sutName); 
	},
	removeSpecSut:function(sutProjectName, sutName)
	{ 
		this.notifyWorking(); 
		this.actions.removeSpecSut(this, sutProjectName, sutName); 
	},
	getReferenceList:function(isEditMode)
	{ 
		this.notifyWorking();
		this.actions.getReferenceList(this, isEditMode); 
	},
	addReference:function()
	{ 
		if($F('reqName_field') == ''){return;} 
		this.notifyWorking(); 
		this.actions.addReference(this); 
	},
	removeReference:function(sutProjectName, sutName, repositoryUid, requirementName, sections)
	{ 
		this.notifyWorking(); 
		this.actions.removeReference(this, sutProjectName, sutName, repositoryUid, requirementName, sections); 
	},
	editSelectedSut:function()
	{ 
		this.notifyWorking(); 
		this.actions.editSelectedSut(this); 
	},	
	cancelEditSutSelection:function()
	{ 
		this.notifyWorking(); 
		this.actions.cancelEditSutSelection(this);
	},
	updateSelectedSut:function()
	{ 
		this.notifyWorking(); 
		this.actions.updateSelectedSut(this); 
	},
	updateExecuteChildren:function(isEditMode)
	{ 
		this.notifyWorking(); 
		GP.View.switchView('body', 'conf_results'+this.id);
		conf_greenPepper.getList(this.params.bulkUID, this.params.executionUID).clear();
		this.actions.updateExecuteChildren(this);
	},
	setAsImplemented:function()
	{
		this.notifyWorking();
		this.hideResults();
		conf_greenPepper.getList(this.params.bulkUID, this.params.executionUID).statusBar.reset();
		this.actions.setAsImplemented(this);
	},
	revert:function(implemented)
	{
		this.notifyWorking();
		this.hideResults();
		conf_greenPepper.getList(this.params.bulkUID, this.params.executionUID).statusBar.reset();
		this.actions.revert(this, implemented);
	},
	retrieveHeader:function(implemented) 
	{ 
		this.notifyWorking(); 
		conf_greenPepper.getList(this.params.bulkUID, this.params.executionUID).clear();
		this.actions.retrieveHeader(this, implemented); 
	},
	retrieveBody:function(implemented) 
	{ 
		this.actions.retrieveBody(this, implemented); 
	},
	
	working:function()
	{
		if(this.isMain())
		{
			GP.View.hide('conf_referenceList_error');
		}
	},
	doneWorking:function(){ /*do nothing*/ },
	error:function(XMLHttpRequest, err){ this.doneWorking(); },

	notifyWorking:function(){ conf_greenPepper.notifyWorking(this.params.bulkUID, this.params.executionUID, this.params.fieldId); },
	notifyDoneWorking:function(){ conf_greenPepper.notifyDoneWorking(this.params.bulkUID, this.params.executionUID, this.params.fieldId); },
	notifyError:function(XMLHttpRequest, err){ conf_greenPepper.notifyError(XMLHttpRequest, err, this.params.bulkUID, this.params.executionUID, this.params.fieldId); }
};