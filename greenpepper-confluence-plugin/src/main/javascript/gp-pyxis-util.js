/*
 * Copyright (c) 2007, Pyxis-Technologies inc.
 * All rights reserved.
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE REGENTS AND CONTRIBUTORS ``AS IS'' AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE REGENTS AND CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

	var GP = { VERSION: '1.0' };
	GP.View =
	{
		show: function(id){
			if (_prototype_$(id)){
				_prototype_$(id).opacity=1.0;
				Element.show(id);
			}
		},
		switchView: function(switchShow, switchHide){
			this.hide(switchHide);
			this.show(switchShow);
		},
		switchShadowedView: function(switchShow, switchHide){
			if (this.isVisible(switchHide)){
			this.hide(switchHide);
			this.show(switchShow);
			}
		},
		toPath: function(string){ return string.replace(/\\/g, "/");},
		hide: function(id){ if (_prototype_$(id))  Element.hide(id); },
		isVisible: function(id){return (_prototype_$(id) && Element.visible(id)); },
		getInnerValue: function(field){ return _prototype_$(field) ? _prototype_$(field).innerHTML : '';},
		setValue: function(id, value) {if (_prototype_$(id)) _prototype_$(id).value = value; },
		write: function(id, content){ if (_prototype_$(id)) _prototype_$(id).innerHTML = content; },
		appear: function(id, time){ if (_prototype_$(id)) new Effect.Appear(id, {duration:time,queue:'parallel'}); },
		fade: function(id, time){ if (_prototype_$(id)) new Effect.Fade(id, {duration:time,queue:'parallel'});},
		setClassName: function(id, className){  if (_prototype_$(id)) _prototype_$(id).className = className; },
		inputFocus: function(element, className){
			element.className = className;
			element.value = '';
		},
		verifyKeyCode: function(evt){
			var charCode = (evt.which) ? evt.which : evt.keyCode;
			if (charCode == 95 || charCode == 33 || charCode == 32 || charCode == 8){ return true; }
			//Permet les touches home, end, les fl�ches et le tab
			if (charCode ==  9 || (charCode >= 35 && charCode <= 40)){ return true; }
			if (charCode > 43 && charCode < 60){ return true; }
			if (charCode > 64 && charCode < 91){ return true; }
			if (charCode > 96 && charCode < 123){ return true; }
			return false;
		},
		findPos: function(obj){
			var curleft = curtop = 0;
			if (obj.offsetParent) {
				curleft = obj.offsetLeft
				curtop = obj.offsetTop
				while (obj = obj.offsetParent) {
					curleft += obj.offsetLeft
					curtop += obj.offsetTop
				}
			}
			return [curleft,curtop];
		},
		showPane: function(paneToShow, paneContainerID, panesTagType){
			if (_prototype_$(paneContainerID) && panesTagType != '' && _prototype_$(paneToShow)){
				_prototype_$$('#'+ _prototype_$(paneContainerID).id + ' ' + panesTagType).each(function(s){ jQuery(s).removeClass('active-pane'); });
				jQuery('#'+ _prototype_$(paneContainerID).id + ' #' + paneToShow).addClass('active-pane');
				this.show(paneToShow);
			}
		}
	};
	
	function $M(object) {
		var map = $H(object);
		Object.extend(map, GP.HashExtensions);  
		return map;
	};
	GP.HashExtensions =
	{
		remove:function(key) { try{ delete this[key]; }catch(e){} },
		size:function() { return this.keys().length; },
		isEmpty:function() { return this.size() === 0; },
		clear:function() {
			for (var key in this) {
				var value = this[key];
				if (typeof value == 'function'){ continue; }
					delete this[key];
			}
		},
		eachEntry:function(iterator) {
			this.each(function(entry, index) {
				iterator(entry[0], entry[1]);
			});
		},
		eachValue:function(iterator) {
			this.eachEntry(function(key, value) {
				if (typeof value != 'function'){iterator(value); }
			});
		},
		findValue:function(iterator) {
			var result = this.detect(function(entry, index) {
				return iterator(entry[1]);
			});
			return result ? result[1] : result;
		}
	};
	
	var BrowserDetect = {
		init: function () {
			this.browser = this.searchString(this.dataBrowser) || "An unknown browser";
			this.version = this.searchVersion(navigator.userAgent)
				|| this.searchVersion(navigator.appVersion)
				|| "an unknown version";
			this.OS = this.searchString(this.dataOS) || "an unknown OS";
		},
		searchString: function (data) {
			for (var i=0;i<data.length;i++)	{
				var dataString = data[i].string;
				var dataProp = data[i].prop;
				this.versionSearchString = data[i].versionSearch || data[i].identity;
				if (dataString) {
					if (dataString.indexOf(data[i].subString) != -1)
						return data[i].identity;
				}
				else if (dataProp)
					return data[i].identity;
			}
		},
		searchVersion: function (dataString) {
			var index = dataString.indexOf(this.versionSearchString);
			if (index == -1) return;
			return parseFloat(dataString.substring(index+this.versionSearchString.length+1));
		},
		dataBrowser: [
			{ 	string: navigator.userAgent,
				subString: "OmniWeb",
				versionSearch: "OmniWeb/",
				identity: "OmniWeb"
			},
			{
				string: navigator.vendor,
				subString: "Apple",
				identity: "Safari"
			},
			{
				prop: window.opera,
				identity: "Opera"
			},
			{
				string: navigator.vendor,
				subString: "iCab",
				identity: "iCab"
			},
			{
				string: navigator.vendor,
				subString: "KDE",
				identity: "Konqueror"
			},
			{
				string: navigator.userAgent,
				subString: "Firefox",
				identity: "Firefox"
			},
			{
				string: navigator.vendor,
				subString: "Camino",
				identity: "Camino"
			},
			{		// for newer Netscapes (6+)
				string: navigator.userAgent,
				subString: "Netscape",
				identity: "Netscape"
			},
			{
				string: navigator.userAgent,
				subString: "MSIE",
				identity: "Explorer",
				versionSearch: "MSIE"
			},
			{
				string: navigator.userAgent,
				subString: "Gecko",
				identity: "Mozilla",
				versionSearch: "rv"
			},
			{ 		// for older Netscapes (4-)
				string: navigator.userAgent,
				subString: "Mozilla",
				identity: "Netscape",
				versionSearch: "Mozilla"
			}
		],
		dataOS : [
			{
				string: navigator.platform,
				subString: "Win",
				identity: "Windows"
			},
			{
				string: navigator.platform,
				subString: "Mac",
				identity: "Mac"
			},
			{
				string: navigator.platform,
				subString: "Linux",
				identity: "Linux"
			}
		]
	
	};
	BrowserDetect.init();
	