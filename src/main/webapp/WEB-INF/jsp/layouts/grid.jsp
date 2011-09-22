<%@page contentType="text/html" pageEncoding="UTF-8"%> 
<html>

<head>
 <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />  
<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<link rel="stylesheet" type="text/css" media="screen" href="/wasp/css/jquery/jquery-ui.css" />
<link rel="stylesheet" type="text/css" media="screen" href="/wasp/css/jquery/ui.jqgrid.css" />
<style>

html, body { 

	margin: 0;			/* Remove body margin/padding */

	padding: 0;

	overflow: hidden;	/* Remove scroll bars on browser window */	

    font-size: 75%;
}

</style>	
	
  <script src="/wasp/scripts/jquery/jquery-1.6.2.js" type="text/javascript"></script>
  <script src="/wasp/scripts/jquery/ajaxfileupload.js" type="text/javascript"></script>
  <script src="/wasp/scripts/jqgrid/grid.locale-<%= ((HttpServletRequest)pageContext.getRequest()).getSession().getAttribute("jqLang") %>.js" type="text/javascript"></script>
  <script src="/wasp/scripts/jqgrid/jquery.jqGrid.min.js" type="text/javascript"></script>
  
  <script type="text/javascript">

  $.jgrid.no_legacy_api = true;
 
  $.jgrid.useJSON = true;

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
  
 
  
  function waspHandleError4(xhr, xml, status, ex)  {
     var error_msg = 'xhr:['+odump(xhr)+'] xml['+odump(xml)+'] status['+odump(status)+'] ex['+odump(ex)+']';           
     //alert(error_msg);
     window.parent.document.write(error_msg);
 }


 function waspHandleError3(xhr, xml, textStatus)  {
 	 var error_msg = 'xhr:['+odump(xhr)+'] xml['+odump(xml)+'] status['+odump(status)+']';
 	 
 }

   
 jQuery.ajaxSetup( {
 	  
 	  error: function(XMLHttpRequest, textStatus, errorThrown) {
 		  alert(textStatus+'|'+errorThrown);
 	  },
 	  async:false 
   } 
  );
 
  function _noop(value, colname) {
	  return [true,""];
  }
  
  function _validate_required(value, colname) {
	 
	  
	  if (value)  
	     return [true,""];
	  else { 
		
		 var errIdx=colNames.indexOf(colname);
		
		 var errMsg=colErrors[errIdx];
		
	     return [false,errMsg];
	  }
   }
  
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
 
  
  function getCellValue(rowId, cellId) {
	    var cell = jQuery('#' + rowId + '_' + cellId);
	    
	    var val = cell.val();
	   
	    return val;
	}
	
	
  

  var _beforeShowAddForm = function(formId) {
	   
  }
  
  var _beforeShowEditForm = function(formId) {
	 // alert($('#attrValue').val());
	  //alert(document.getElementById(formId[0].id).login );
  }
 
  _del_function = function (id) {
	     alert("Lab cannot be deleted once created. Instead, use the 'edit' button to mark the '"+$("#grid_id").getRowData(id).login+"' lab as inactive.");
	     return false;
 };	
	
	
 _errorTextFormat = function(response) {
		return response.responseText;
  }

	
  _afterSubmit = function(response, data) {
	  
       waspFade('statusMessage',response.responseText);
	
	   return [true,''];     
 }

  
  var _url='/wasp/<tiles:insertAttribute name="area" />/listJSON.do?selId=${param.selId}';
  var _editurl='/wasp/<tiles:insertAttribute name="area" />/detail_rw/updateJSON.do';
  
  var _editAttr={
		  width:'auto',closeAfterEdit:true,closeOnEscape:true,afterSubmit:_afterSubmit,errorTextFormat:_errorTextFormat,beforeShowForm:_beforeShowEditForm,reloadAfterSubmit:true,recreateForm:true
  };
  
  var _addAttr={
	serializeEditData: function(data){ return $.param($.extend({}, data, {id:0}));},//pass '0' on add instead of empty string
	closeAfterAdd:true,closeOnEscape:true,errorTextFormat:_errorTextFormat,afterSubmit:_afterSubmit,beforeShowForm:_beforeShowAddForm,width:'auto',reloadAfterSubmit:true,recreateForm:true
  };
  
  var _delAttr={};
  
  var _searchAttr={drag:true,resize:true,modal:true,caption:'Lookup',closeOnEscape:true,sopt:['eq','ne'],multipleSearch: false, closeAfterSearch: true };
  
  var _navAttr={view:true,del:true,delfunc:_del_function};
  
  //these will be populated by the wasp:field tags below
  var colNames=[];  
  var colModel=[];  
  var colErrors=[];
  
  <tiles:insertAttribute name="grid-columns" />
  	
//add meta fields
 <c:forEach items="${_metaList}" var="_meta" varStatus="status">

	_field_name='${_meta.k}';
	_wasp_prop='${_meta.property}';

	required='${_meta.property.constraint}'=='NotEmpty';

	editrules={edithidden:true};
	formoptions={};
	if(required){
		formoptions={elmsuffix:'<fontcolor=red>*</font>'};
		editrules={edithidden:true,custom:true,custom_func:_validate_required};	
	}

	editoptions={size:20};
 	edittype='text';
 	   
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
 		//
  </c:if>          
 		
  //populate jq structures
  colNames.push('${_meta.property.label}');

	colModel.push(
			{name:'${_meta.k}', width:80, edittype:edittype, align:'right',hidden:true,editable:true,editrules:editrules,formoptions:formoptions,editoptions:editoptions}
	);
	
	colErrors.push('${_meta.property.error}');

</c:forEach>

 function createGrid() {
 
$(function(){
		
var navGrid=$("#grid_id").jqGrid({
  url:_url,
  editurl:_editurl,
  datatype: 'json',
  recordtext: "{2} rows",  
  mtype: 'GET',
  colNames:colNames,
  colModel : colModel,
  pager: '#gridpager',
  rowNum:200,    
      
  viewrecords: true,
  gridview: true,
  <tiles:insertAttribute name="subgrid-columns" />
  autowidth: true,

  scroll: true,	
  height: '640', 
  loadui: 'block',
  scrollrows:true,
  loadonce: false, // to enable sorting on client side
  sortable: false, //to enable sorting
  
  loadComplete: function(data) {//pre-select row if userdata.selId is defined
	    
	        // data.userdata is the same as jQuery("#grid_id").getGridParam('userData');
	        var userdata = jQuery("#grid_id").getGridParam('userData');
	        
	        if (!userdata.selId) return;//no row to pre-select
	        
	        var curPage = jQuery("#grid_id").getGridParam('page'); // is always 1
	        if (curPage !== userdata.page) {
	            setTimeout(function(){
	                jQuery("#grid_id").setGridParam(
	                    { page: userdata.page }).trigger("reloadGrid");
	                jQuery("#grid_id").setSelection (userdata.selId, true);
	            },100);
	        }
	        else {
	            jQuery("#grid_id").setSelection (userdata.selId, true);
	        }
	    
  },
  ondblClickRow: function(rowid) {			
  	$("#grid_id").jqGrid('editGridRow',rowid,_editAttr);
 }
}).navGrid('#gridpager',
		  _navAttr, 
		  _editAttr,   // edit
		  _addAttr,    // add
		  _delAttr,    // delete
		  _searchAttr, // search
		  _editAttr
);

<c:forEach items="${_metaBySubtypeList}" var="_entry" varStatus="_substatus">
<c:set var="_subtype" value="${_entry.key}"/>
<c:set var="_validMetaFields" value="${_entry.value}"/>

navGrid.navButtonAdd("#gridpager",{
	   caption:"${_subtype.name}", 	 
	   title: "Add Sample of '${_subtype.name}' subtype",
	   onClickButton: function(){				   

		   var  _myAddAttr = jQuery.extend({subtypeSampleId:${_subtype.subtypeSampleId}}, _addAttr);
		   
		   $("#grid_id").jqGrid('editGridRow',"new", _myAddAttr); 
	   }, 
	   position:"first"
});
</c:forEach>

if (_enableFilterToolbar) {
	$('#grid_id').jqGrid('filterToolbar', { stringResult: true, searchOnEnter: false });
}

}); 

}
createGrid();

</script>
</head>
<body>

<tiles:insertAttribute name="banner-content" />

<tiles:insertAttribute name="primary-content" />

<tiles:insertAttribute name="footer-content" />

</body>
</html>
