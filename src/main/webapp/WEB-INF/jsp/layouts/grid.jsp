<%@page contentType="text/html" pageEncoding="UTF-8"%>
 <%--  Template for pages containing JQGrid table  --%> 
<html>

<head>
 <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />  
 <%@ include file="/WEB-INF/jsp/taglib.jsp" %>
 
 <title> 	 	
     <wasp:pageTitle/> 
 </title>
  
<link rel="stylesheet" type="text/css" media="screen" href="/wasp/css/jquery/jquery-ui.css" />
<link rel="stylesheet" type="text/css" media="screen" href="/wasp/css/jquery/ui.jqgrid.css" />
<style>

html, body { 
	margin: 0;			/* Remove body margin/padding */
	padding: 0;
	overflow: hidden;	/* Remove scroll bars on browser window */	
    font-size: 100%;
}

</style>	
	
  <script src="/wasp/scripts/jquery/jquery-1.6.2.js" type="text/javascript"></script>
  <script src="/wasp/scripts/jquery/ajaxfileupload.js" type="text/javascript"></script>
  
   <%--  include locale-specific jqgrid file.  jqLang is set in UserLocaleInterceptor class --%> 
  <script src="/wasp/scripts/jqgrid/grid.locale-<%= ((HttpServletRequest)pageContext.getRequest()).getSession().getAttribute("jqLang") %>.js" type="text/javascript"></script>
  <script src="/wasp/scripts/jqgrid/jquery.jqGrid.min.js" type="text/javascript"></script>
  
  <script type="text/javascript">

  <%-- fires before showing the form with the new data after user clicked "add" button; receives as Parameter the id of the constructed form. --%>  
  var _beforeShowAddForm = function(formId) {
	   
  }
  
  <%-- fires before showing the form with the new data after user clicked "edit" button; receives as Parameter the id of the constructed form. --%>
  var _beforeShowEditForm = function(formId) {
	  
  }
 
 
  <%--  replaces the build in del function to prevent row deletion. Parameter passed to this function is the id of the edited row --%>
  var _del_function = function (id) {
	     alert("Record cannot be deleted. Instead, use the 'edit' button to mark the '"+$("#grid_id").getRowData(id).login+"' record as inactive.");
	     return false;
 };	
	
	
 <%--  The event  fire when error occurs from the ajax call and can be used for better formatting of the error messages. 
// To this event is passed response from the server. The event should return single message (not array), which then is displayed to the user. --%> 
 var _errorTextFormat = function(response) {
		return response.responseText;
  }

	
  <%--  fires after response has been received from server. used to display status from server 
  // Receives as parameters the data returned from the request and an array of the posted values of type id=value1,value2. 
  // When used this event should return array with the following items [success, message] 
  // where 
  // success is a boolean value if true the process continues, if false a error message appear and all other processing is stoped.  --%>
  var _afterSubmit = function(response, data) {
	  
       waspFade('statusMessage',response.responseText);
	
	   return [true,''];     
 }

  <%-- toggles on/off filter toolbar at the top --%>
  var _enableFilterToolbar=false;
  
  <%-- URL to fetch JSON-formatted data from server --%>
  var _url='/wasp/<tiles:insertAttribute name="area" />/listJSON.do?selId=${param.selId}';
  
  <%-- URL to submit CUD requests to the server --%>
  var _editurl='/wasp/<tiles:insertAttribute name="area" />/detail_rw/updateJSON.do';
  
  <%--  structure to define L&F of "edit row" functionality --%> 
  var _editAttr={
		  width:'auto',closeAfterEdit:true,closeOnEscape:true,afterSubmit:_afterSubmit,errorTextFormat:_errorTextFormat,beforeShowForm:_beforeShowEditForm,reloadAfterSubmit:true,recreateForm:true
  };
  
  <%-- structure to define L&F of "add row" functionality. see JQGrid documentation at http://www.trirand.com/jqgridwiki/doku.php?id=wiki:jqgriddocs for parameter descriptions --%>
  var _addAttr={
	serializeEditData: function(data){ return $.param($.extend({}, data, {id:0}));},//pass '0' on add instead of empty string
	closeAfterAdd:true,closeOnEscape:true,errorTextFormat:_errorTextFormat,afterSubmit:_afterSubmit,beforeShowForm:_beforeShowAddForm,width:'auto',reloadAfterSubmit:true,recreateForm:true
  };
  
  <%-- structure to define L&F of "delete row" functionality. see JQGrid documentation at http://www.trirand.com/jqgridwiki/doku.php?id=wiki:jqgriddocs for parameter descriptions --%>
  var _delAttr={};
  
  <%-- structure to define L&F of "search row" functionality. see JQGrid documentation at http://www.trirand.com/jqgridwiki/doku.php?id=wiki:jqgriddocs for parameter descriptions --%>
  var _searchAttr={drag:true,resize:true,modal:true,caption:'Lookup',closeOnEscape:true,sopt:['eq','ne'],multipleSearch: false, closeAfterSearch: true };

  <%-- structure to define L&F of "navigator" functionality. see JQGrid documentation at http://www.trirand.com/jqgridwiki/doku.php?id=wiki:jqgriddocs for parameter descriptions --%>
  var _navAttr={view:true,del:true,delfunc:_del_function};
  
  <%-- these objects will be populated by the wasp:field tags included via "grid-columns" tile --%>
  var colNames=[];  
  var colModel=[];  
  var colErrors=[];
  
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
	editrules={edithidden:true};
	formoptions={};
	if(required){
		formoptions={elmsuffix:'<font color=red>*</font>'};
		editrules={edithidden:true,custom:true,custom_func:_validate_required};	
	}

	editoptions={size:20};
 	edittype='text';
 	
   <%-- poluate "select" inputs --%>	 
   <c:if test="${not empty _meta.property.control}">
       editoptions={size:20,value:{}};
       edittype='select';
  		<c:if test="${_meta.property.control.items != null}">  	
  			<c:set var="selectItems" scope="request" value="${requestScope[_meta.property.control.items]}"/>
  			<c:set var="itemValue" scope="request">${_meta.property.control.itemValue}</c:set>
  			<c:set var="itemLabel" scope="request">${_meta.property.control.itemLabel}</c:set>   	
  		</c:if>
 		<c:if test="${_meta.property.control.items == null}">
	  		<c:set var="selectItems" scope="request" value="${_meta.property.control.options}" />
 			<c:set var="itemValue" scope="request">value</c:set>
 			<c:set var="itemLabel" scope="request">label</c:set>  	
 		</c:if>
  	              
 		selectItems=<wasp:json object="${selectItems}" />;

 		editoptions['value']['']=' --- select --- ';
 		for(sKey in selectItems) {
 			_option=selectItems[sKey];
 			_value=_option['${itemValue}'];
 			_label=_option['${itemLabel}'];
 			
 			editoptions['value'][_value]=_label;
 		}
 		
  </c:if>          
 		
  <%-- list of column names --%>
  colNames.push('${_meta.property.label}');

  <%-- list of column properties. see JQGrid documentation at http://www.trirand.com/jqgridwiki/doku.php?id=wiki:jqgriddocs for parameter descriptions --%>
  colModel.push(
		{name:'${_meta.k}', width:80, edittype:edittype, align:'right',hidden:true,editable:true,editrules:editrules,formoptions:formoptions,editoptions:editoptions}
  );
	
  <%-- list of column validation errors --%>
  colErrors.push('${_meta.property.error}');

</c:forEach>

_enableFilterToolbar=false;

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
 		  alert(textStatus+'|'+errorThrown);
 	  },
 	  async:false 
   } 
  );
 
  <%--  returns [true,""] array. usefull in various JQGrid callbacks --%>
  function _noop(value, colname) {
	  return [true,""];
  }
  
  <%-- validates required columns --%>
  function _validate_required(value, colname) {
	 
	  
	  if (value)  
	     return [true,""];
	  else { 
		
		 var errIdx=colNames.indexOf(colname);
		
		 var errMsg=colErrors[errIdx];
		
	     return [false,errMsg];
	  }
   }
  
  <%-- validates email columns --%>
  function _validate_email(value, colname) {
		 
	  var re = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/; 		
	  
	  if (value && value.match(re))  
	     return [true,""];
	  else { 
		
		 var errIdx=colNames.indexOf(colname);
		
		 var errMsg=colErrors[errIdx];
		
	     return [false,errMsg];
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
  rowNum: 30,
  rowList: [10,20,30], 
  viewrecords: true,
  gridview: false,

  <tiles:insertAttribute name="subgrid-columns" />	// subgrid columns will appear here

  autowidth: true,
  //scroll: false,		// scroll:true will disable the pager on page
  height: '85%', 
  loadui: 'block',
  scrollrows: false,
  loadonce: false, // false to enable paging/sorting on client side
  sortable: false, // true to enable sorting
  
  loadComplete: function(data) {//pre-select row if userdata.selId is defined
    // data.userdata is the same as jQuery("#grid_id").getGridParam('userData');
    var userdata = jQuery("#grid_id").getGridParam('userData');

    if (!userdata.selId) return;//no row to pre-select
      
    var curPage = jQuery("#grid_id").getGridParam('page'); 
    if (curPage !== userdata.page) {
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
  
  <tiles:insertAttribute name="grid-customAttributes" /> //add custom attributes if any
}).navGrid('#gridpager',
		  _navAttr, 
		  _editAttr,   // edit
		  _addAttr,    // add
		  _delAttr,    // delete
		  _searchAttr, // search
		  _editAttr
);

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
