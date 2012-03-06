<%@page contentType="text/html" pageEncoding="UTF-8"%>
 <%--  Template for pages containing JQGrid table  --%> 
<%@ page import="edu.yu.einstein.wasp.dao.impl.DBResourceBundle" %>
<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="utf-8" />
	<title> 	 	
	    <wasp:pageTitle/> 
	</title>
	<link rel="stylesheet" type="text/css" media="screen" href="/wasp/css/reset.css" />
	<link rel="stylesheet" type="text/css" media="screen" href="/wasp/css/base.css" />
	<link rel="stylesheet" type="text/css" media="screen" href="/wasp/css/jquery/jquery-ui.css" />
	<link rel="stylesheet" type="text/css" media="screen" href="/wasp/css/jquery/ui.jqgrid.css" />
	
	<script src="/wasp/scripts/jquery/jquery-1.7.1.js" type="text/javascript"></script>
	<script src="/wasp/scripts/jquery/ajaxfileupload.js" type="text/javascript"></script>
	<script src="/wasp/scripts/jquery/jquery-ui-1.8.18.custom.min.js" type="text/javascript"></script> 

	<%--  include locale-specific jqgrid file.  jqLang is set in UserLocaleInterceptor class --%> 
	<script src="/wasp/scripts/jqgrid/grid.locale-<%= ((HttpServletRequest)pageContext.getRequest()).getSession().getAttribute("jqLang") %>.js" type="text/javascript"></script>
	<script src="/wasp/scripts/jqgrid/jquery.jqGrid.min.js" type="text/javascript"></script>
	
	<script type="text/javascript">

		<%-- fires before showing the form with the new data after user clicked "add" button; receives as Parameter the id of the constructed form. --%>  
		var _beforeShowAddForm = function(formId) {
		};
		
		<%-- fires before showing the form with the new data after user clicked "edit" button; receives as Parameter the id of the constructed form. --%>
		var _beforeShowEditForm = function(formId) {
		};
		var _afterShowEditForm = function(formId) {
		};
		
		<%--  replaces the build in del function to prevent row deletion. Parameter passed to this function is the id of the edited row --%>
		 var _del_function = function (id) {
			alert("Record cannot be deleted. Instead, use the 'edit' button to mark the '"+$("#grid_id").getRowData(id).login+"' record as inactive.");
			return false;
		};	
		
		<%--  replaces the build in del function to prevent row deletion. Parameter passed to this function is the id of the edited row --%>
		 var _del_function_resource = function (id) {
			alert("Record cannot be deleted. Instead, use the 'edit' button and set 'Decommission Date' to mark the '"+$("#grid_id").getRowData(id).name+"' record as inactive.");
			return false;
		};
		
		<%--  The event  fire when error occurs from the ajax call and can be used for better formatting of the error messages. 
		// To this event is passed response from the server. The event should return single message (not array), which then is displayed to the user. --%> 
		var _errorTextFormat = function(response) {
			return response.responseText;
		};
		
		<%--  fires after response has been received from server. used to display status from server 
		// Receives as parameters the data returned from the request and an array of the posted values of type id=value1,value2. 
		// When used this event should return array with the following items [success, message] 
		// where 
		// success is a boolean value if true the process continues, if false a error message appear and all other processing is stoped.  --%>
		var _afterSubmit = function(response, data) {
			waspFade('statusMessage',response.responseText);
		
			return [true,''];     
		};
		
		var _beforeCheckValues = function(data){
			_colNameValidatedIndexes = [];
			return data;
		};
		
		<%-- stores list of col names already validated with last index encountered (in case of multiple identical colnames - remember some may be hidden and so legit) --%>
		var _colNameValidatedIndexes = new Array();
	
		<%-- toggles on/off filter toolbar at the top --%>
		var _enableFilterToolbar=false;
		
		<%-- URL to fetch JSON-formatted data from server --%>
		var _url='/wasp/<tiles:insertAttribute name="area" />/listJSON.do?selId=${param.selId}';
		
		<%-- URL to submit CUD requests to the server --%>
		var _editurl='/wasp/<tiles:insertAttribute name="area" />/detail_rw/updateJSON.do';
		
		<%--  structure to define L&F of "edit row" functionality --%> 
		var _editAttr={
			width:'auto',
			closeAfterEdit:true,
			closeOnEscape:true,
			afterSubmit:_afterSubmit,
			beforeCheckValues:_beforeCheckValues,
			errorTextFormat:_errorTextFormat,
			beforeShowForm:_beforeShowEditForm,
			afterShowForm:_afterShowEditForm,
			reloadAfterSubmit:true,
			recreateForm:true
		};
		 
		<%--  structure to define L&F of "view row" functionality --%> 
		var _viewAttr={
			closeOnEscape:true
		};
		 
		<%-- structure to define L&F of "add row" functionality. see JQGrid documentation at http://www.trirand.com/jqgridwiki/doku.php?id=wiki:jqgriddocs for parameter descriptions --%>
		var _addAttr={
			serializeEditData: function(data){ return $.param($.extend({}, data, {id:0}));},//pass '0' on add instead of empty string
			closeAfterAdd:true,
			closeOnEscape:true,
			errorTextFormat:_errorTextFormat,
			afterSubmit:_afterSubmit,
			beforeCheckValues:_beforeCheckValues,
			beforeShowForm:_beforeShowAddForm,
			width:'auto',
			reloadAfterSubmit:true,
			recreateForm:true
		};
		 
		<%-- structure to define L&F of "delete row" functionality. see JQGrid documentation at http://www.trirand.com/jqgridwiki/doku.php?id=wiki:jqgriddocs for parameter descriptions --%>
		var _delAttr={};
		
		<%-- structure to define L&F of "search row" functionality. see JQGrid documentation at http://www.trirand.com/jqgridwiki/doku.php?id=wiki:jqgriddocs for parameter descriptions --%>
		var _searchAttr={
			drag:true,
			resize:true,
			modal:true,
			caption:'Lookup',
			closeOnEscape:true,
			sopt:['eq','ne'],
			multipleSearch: false, 
			closeAfterSearch: true 
		};
		
		<%-- structure to define L&F of "navigator" functionality. see JQGrid documentation at http://www.trirand.com/jqgridwiki/doku.php?id=wiki:jqgriddocs for parameter descriptions --%>
		var _navAttr={view:true,del:true,delfunc:_del_function};
		
		<%-- these objects will be populated by the wasp:field tags included via "grid-columns" tile --%>
		var colNames=[];  
		var colModel=[];  
		var colErrors=[];
		var colConstraint=[];
		var colMetaType=[];
		var colRange=[];
		
		<%-- disable support for legacy API --%>
		$.jgrid.no_legacy_api = true;
		
		<%-- default to JSON format --%>
		$.jgrid.useJSON = true;
		
		<%-- include column definitions --%>
		<tiles:insertAttribute name="grid-columns" />
		
		<%-- add meta fields --%>
		<c:forEach items="${_metaList}" var="_meta" varStatus="status">
		
			<%-- get field name --%>
			_field_name='${_meta.k}';
			
			<%-- get field properties --%>
			_wasp_prop='${_meta.property}';
		
			<%-- check if field is required --%>
			required='${_meta.property.constraint}'=='NotEmpty';
		
			<%-- define rules to edit the field --%>
			editrules={edithidden:true,custom:true,custom_func:_validate_basic};
			formoptions={};
			
			if ('${_meta.property.constraint}'.substring(0,6) == 'RegExp'){
				editrules={edithidden:true,custom:true,custom_func:_validate_regexp};	
			}
			else if(required){
				editrules={edithidden:true,custom:true,custom_func:_validate_required};	
			}
			
			if(required){
				formoptions={elmsuffix:'<span class="requiredField">*</span>'};
			}
		
			editoptions={size:20};
		 	edittype='text';
		 	
		   <%-- populate "select" inputs --%>	 
		   <c:if test="${not empty _meta.property.control}">
		       editoptions={size:20,value:{}};
		       edittype='select';
		  	              
		       <%-- this tag will define selectItems/itemValue/itemLabel request attributes --%>
		       <wasp:metaSelect control="${_meta.property.control}"/>
		                 
		 		selectItems=<wasp:json object="${selectItems}" />;
		
		 		editoptions['value']['']=' --- select --- ';
		 		for(sKey in selectItems) {
		 			_option=selectItems[sKey];
		 			_value=_option['${itemValue}'];
		 			_label=_option['${itemLabel}'];
		 			
		 			editoptions['value'][_value]=_label;
		 		}
		 		
		  </c:if>     
		  
			<%-- add autocomplete function for the "institution" input fields --%>
			if(_field_name.indexOf('institution') != -1){
				editoptions = { 
						dataInit: function(elm){
							setTimeout(
								function(){ 
									$.getJSON("/wasp/autocomplete/getInstitutesForDisplay.do", 
											{ instituteNameFragment: "" }, 
											function(data) { 
												jQuery(elm).autocomplete(data);
											} );
			                	}, 200);
						}
				};
			}
		
			<%-- add datepicker for the "date" input fields --%>
			if(_field_name.indexOf('date') != -1){
				editoptions = { 
						dataInit: function(elm){
							setTimeout(
								function(){ 
									jQuery(elm).datepicker({dateFormat:'yy-mm-dd'});
									jQuery('.ui-datepicker').css({'font-size':'80%'}); 
			                	}, 200);
						}
				};
			}
			
			
		
		<%-- list of column names --%>
			colNames.push('${_meta.property.label}');
		
			<%-- list of column properties. see JQGrid documentation at http://www.trirand.com/jqgridwiki/doku.php?id=wiki:jqgriddocs for parameter descriptions --%>
			colModel.push(
				{name:'${_meta.k}', width:80, edittype:edittype, align:'right',hidden:true,editable:true,editrules:editrules,formoptions:formoptions,editoptions:editoptions}
			);

			<%-- list of column validation errors --%>
			colErrors.push('${_meta.property.error}');
			
			<%-- list of column validation meta types --%>
			colMetaType.push('${_meta.property.metaType}');
			
			<%-- list of column validation max ranges --%>
			colRange.push('${_meta.property.range}');
			
			<%-- list of column constraints --%>
			colConstraint.push('${_meta.property.constraint}');
		
		</c:forEach>
	
		
		/* define custom formatter to format/unformat hyperlinks */
		jQuery.extend($.fn.fmatter , {
			linkFormatter : function(cellvalue, options, rowObject) {
				var cm = options.colModel;
				
				var name = cm.formatoptions.idName;
				var url = cm.formatoptions.baseLinkUrl;
				var idCol = cm.formatoptions.idCol;
				
				var id;
				if (idCol == -1)
					id = options.rowId;
				else
					id = rowObject[idCol];
				
				return "<a href='"+url+"?"+name+"="+id+"'>"+cellvalue+"</a>";
			}
		});
		jQuery.extend($.fn.fmatter.linkFormatter , {
			unformat : function(cellvalue, options) {
				return cellvalue.replace(/<.+>/g,"");
			}
		});
		/* define custom formatter ends here*/
		

		if (_url.indexOf('/uiField/')==-1) 
			_enableFilterToolbar=false;//not sure who/why force _enableFilterToolbar to false. add an exception for uiField. Sasha 12.1.12
	
		<%-- function to help with debugging --%>
		function odump(object, depth, max){
			depth = depth || 0;
			max = max || 2;
	
			if (depth > max)
				return false;
		
			var indent = "";
			for (var i = 0; i < depth; i++)
				indent += "  ";
		
			var output = "";  
			for (var key in object){
				output += "\n" + indent + key + ": ";
				switch (typeof object[key]){
					case "object": output += odump(object[key], depth + 1, max); break;
					case "function": output += "function"; break;
					default: {
						try {
							output += object[key]; break;  
						} catch(e) {
							output += key+' cant get value ';
							break;
						}
					}
				}
			}
			return output;
		}
	
		<%-- display message / fade it after 5 seconds. --%>
		function waspFade(el, msg) {
			$('#'+el).text(msg);
			setTimeout(function() {
				$('#'+el).fadeOut('slow',
					function() {
						$('#'+el).text('');
						$('#'+el).show();
					});
			},5000);
		}
	
	 
		<%-- displays AJAX protocol errors --%>
		function waspHandleError4(xhr, xml, status, ex)  {
			var error_msg = 'xhr:['+odump(xhr)+'] xml['+odump(xml)+'] status['+odump(status)+'] ex['+odump(ex)+']';               
			window.parent.document.write(error_msg);
		}
	
	
		<%-- displays AJAX protocol errors --%>
		function waspHandleError3(xhr, xml, textStatus)  {
			var error_msg = 'xhr:['+odump(xhr)+'] xml['+odump(xml)+'] status['+odump(status)+']';
			window.parent.document.write(error_msg);
		}
	
	   
		jQuery.ajaxSetup( {
			<%-- displays AJAX protocol errors --%>
			error: function(XMLHttpRequest, textStatus, errorThrown) {
				//alert(textStatus+'|'+errorThrown);
				console.log(textStatus+'|'+errorThrown);
			},
			async:false 
			} 
		);
	
		<%--  returns [true,""] array. usefull in various JQGrid callbacks --%>
		function _noop(value, colname) {
			return [true,""];
		}
	  
		<%-- validates columns minimally based on any supplied metadata properties but only if there is some data to validate --%>
		function _validate_basic(value, colname, colIndex ) {
			if (!value)  
				return [true,""];
			if (colIndex == undefined){
				colIndex=_get_next_colname_index(colname);
			}
			var numberRe = /^-?[0-9]+\.?[0-9]*$/;
			var integerRe = /^-?[0-9]+$/;
		
			if (colMetaType[colIndex] == "INTEGER") {
				if  (!value.match(integerRe) ){
					var errMsg=colname+': ${_metaDataMessages.metaType} (INTEGER)';
					return [false,errMsg];
				}
				value = parseFloat(value);
			}
			else if (colMetaType[colIndex] == "NUMBER"){
				if  (!value.match(numberRe) ){
					var errMsg=colname+': ${_metaDataMessages.metaType} (NUMBER)';
					return [false,errMsg];
				}
				value = parseFloat(value);
			}
			//document.write("<p> value=" + value + ", colname=" + colname + ", metaType=" + colMetaType[colIndex] + ", rangeMin=" + colRangeMin[colIndex] + ", rangeMax=" + colRangeMax[colIndex] + ", colIndex=" + colIndex + ", value.length=" + value.length +  "</p>"); 
			if  (colRange[colIndex]){
				var rangeSplit = colRange[colIndex].split(':');
				var rangeMin = 0;
				var rangeMax = 0;
				if (!rangeSplit[1]){
					rangeMax = rangeSplit[0];
				} else {
					rangeMin = rangeSplit[0];
					rangeMax = rangeSplit[1];
				}
				if ((colMetaType[colIndex] == "NUMBER" || colMetaType[colIndex] == "INTEGER") && value > rangeMax){
					var errMsg=colname+': ${_metaDataMessages.rangeMax} (' + rangeMax + ')';
					return [false,errMsg];
				} else if (colMetaType[colIndex] == "STRING" && value.length > rangeMax){
					var errMsg=colname+': ${_metaDataMessages.lengthMax} (' + rangeMax + ')';
					return [false,errMsg];
				} else if ((colMetaType[colIndex] == "NUMBER" || colMetaType[colIndex] == "INTEGER") && value < rangeMin){
					var errMsg=colname+': ${_metaDataMessages.rangeMin} (' + rangeMin + ')';
					return [false,errMsg];
				} else if (colMetaType[colIndex] == "STRING" && value.length < rangeMin){
					var errMsg=colname+': ${_metaDataMessages.lengthMin} (' + rangeMin + ')';
					return [false,errMsg];
				}
			}
			return [true,""];
		}
	
		<%-- validates required columns --%>
		function _validate_required(value, colname) {
			//alert("colname: "+colname);
			var errIdx=_get_next_colname_index(colname);
			if (_is_element_hidden(errIdx)){
				return [true,""];
			}
			if (!value) {
				var errMsg=colErrors[errIdx];
				return [false,errMsg];
			}
		
			return _validate_basic(value, colname, errIdx);
		}
	
		<%-- validates email columns --%>
		function _validate_email(value, colname) {
			var errIdx=_get_next_colname_index(colname);
			if (_is_element_hidden(errIdx)){
				return [true,""];
			}
			var re = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/; 		
		
			if (!value || !value.match(re)) {
				var errMsg=colErrors[errIdx];
				return [false,errMsg];
			}
		
			return _validate_basic(value, colname, errIdx);
		}
	
		<%-- validates regular expression --%>
		function _validate_regexp(value, colname) {
			var colIndex=_get_next_colname_index(colname);
			var re = /^RegExp:(.+)$/;
			var match = re.exec(colConstraint[colIndex]);
			if (match[1]){
				var regExpExtracted = new RegExp(match[1]);
				if (!value.match(regExpExtracted)) {
					var errMsg=colErrors[colIndex];
					return [false,errMsg];
				}
				return _validate_basic(value, colname, colIndex);
			}
		}
		
		function _is_element_hidden(index){
			var k = colModel[index].name;
			if (k.indexOf('.')!=-1){ // not static field
				var jqName='#tr_'+k.replace(".","\\.");
				//alert(jqName +' : '+ $(jqName).is(':hidden'));
				if ($(jqName).is(':hidden')){
					return true;
				}              		
			}   
			return false;
		}
		
		function _get_next_colname_index(colname){
			if (_colNameValidatedIndexes[colname] != undefined){
				var index = colNames.indexOf(colname, _colNameValidatedIndexes[colname] + 1);
				if (index != -1){
					_colNameValidatedIndexes[colname] = index;
				}
				//alert(colname+"="+ (_colNameValidatedIndexes[colname] + 1) +":"+index);
				return index;
			} else {
				index = colNames.indexOf(colname);
				if (index != -1){
					_colNameValidatedIndexes[colname] = index;
				}
				//alert(colname+"="+ (_colNameValidatedIndexes[colname] + 1) +":"+index);
				return index;
			}
		}
	
		<%-- returns cell value. --%> 
		function getCellValue(rowId, cellId) {
		   var varName='#' + rowId + '_' + cellId;
		  
		   var cell = jQuery(varName);
		   
		   var val = cell.val();
		  
		   return val;
		}
	  
		<%-- created main jqgrid object --%> 
		function createGrid() {
			$(function(){
				<%--  call to JQGrid API
				// see JQGrid documentation for parameter descriptions --%>
				var navGrid=$("#grid_id").jqGrid({
					url:_url,
					editurl:_editurl,
					datatype: 'json',
					//recordtext: "{2} rows",  
					mtype: 'GET',
					colNames: colNames,
					colModel: colModel,
					pager: '#gridpager',
					rowNum: 20,
					rowList: [10,20,30], 
					viewrecords: true,
					gridview: false,
					<tiles:insertAttribute name="subgrid-columns" />	// subgrid columns will appear here
		
					autowidth: true,
					//scroll: false,		// scroll:true will disable the pager on page
					height: 'auto', 
					loadui: 'block',
					scrollrows: false,
					loadonce: false, // false to enable paging/sorting on client side
					sortable: true, // true to enable sorting
		
					loadComplete: function(data) {
						//$("#grid_id").setGridParam({datatype:'local'});
		   
						//pre-select row if userdata.selId is defined
		
						// data.userdata is the same as jQuery("#grid_id").getGridParam('userData');
						var userdata = jQuery("#grid_id").getGridParam('userData');
						
						if (!userdata.selId) return;//no row to pre-select
						  
						var curPage = jQuery("#grid_id").getGridParam('page'); 
						//alert(curPage);
						if (curPage != userdata.page) {
						//if (curPage == 0) {
						     setTimeout(function(){
						        jQuery("#grid_id").setGridParam({ page: userdata.page }).trigger("reloadGrid");
						        jQuery("#grid_id").setSelection (userdata.selId, true);
						    },100);
						}
						else {
						    jQuery("#grid_id").setSelection (userdata.selId, true);
						}
					},
		
					onPaging : function(which_button) {
						$("#grid_id").setGridParam({datatype:'json'});
					},
		 
					ondblClickRow: function(rowid) {//enable "edit" on dblClick			
						$("#grid_id").jqGrid('editGridRow',rowid,_editAttr);
					},
					
					onSelectRow: function(rowId){ 
						setTimeout(function(){
							$("#grid_id").toggleSubGridRow(rowId);
						},200);
					},
		
					<tiles:insertAttribute name="grid-customAttributes" /> //add custom attributes if any
				}).navGrid('#gridpager',
							_navAttr, 
							_editAttr,   // edit
							_addAttr,    // add
							_delAttr,    // delete
							_searchAttr, // search
							_viewAttr);
		
				<%-- add custom toolbar buttons if any --%>
				<tiles:insertAttribute name="toolbar-buttons" />
		
				<%-- add filter toolbar --%>
				if (_enableFilterToolbar) {
					$('#grid_id').jqGrid('filterToolbar', { stringResult: true, searchOnEnter: false });
				}
			}); 
		}
		 
		createGrid();
	
	</script>
	
	<tiles:insertAttribute name="head-js" />
</head>


<body>

	<tiles:insertAttribute name="banner-content" />
	
	<tiles:insertAttribute name="body-content" />
	
	<tiles:insertAttribute name="footer-content" />

</body>
</html>
