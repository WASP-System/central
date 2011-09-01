<html>

<head> 
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
  <script src="/wasp/scripts/jqgrid/grid.locale-<%= ((HttpServletRequest)pageContext.getRequest()).getSession().getAttribute("jqLang") %>.js" type="text/javascript"></script>
  <script src="/wasp/scripts/jqgrid/jquery.jqGrid.min.js" type="text/javascript"></script>
  
  <script type="text/javascript">

  $.jgrid.no_legacy_api = true;
 
  $.jgrid.useJSON = true;

  
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
 
  

  var _beforeShowAddForm = function(formId) {
	  
  }
  
  var _beforeShowEditForm = function(formId) {
	  
  }
 
  _del_function = function (id) {
	     alert("Lab cannot be deleted once created. Instead, use the 'edit' button to mark the '"+$("#grid_id").getRowData(id).login+"' lab as inactive.");
	     return false;
 };	
	
	
 _errorTextFormat = function(response) {
		return response.responseText;
  }

	
  _afterSubmit = function(response, data) {
	  
	   document.getElementById('statusMessage').innerText=response.responseText;
	  
	 // alert('status set to '+document.getElementById('statusMessage').innerText);
	   setTimeout(function() {
	  	
	    $('#statusMessage').fadeOut('slow',

		function() {       		
	    	document.getElementById('statusMessage').innerText="";
	    	 $('#statusMessage').show();
 			//annimation complete
			});
		},5000);

     //reload grid
     //grid.trigger("reloadGrid");
  
    //if (document.forms[0].password && document.forms[0].password.value) {
 	//   document.forms[0].password.value='';
   //  }
	
	   return [true,''];     
 }

  
  var _url='/wasp/<tiles:insertAttribute name="area" />/listJSON.do?selId=${param.selId}';
  var _editurl='/wasp/<tiles:insertAttribute name="area" />/detail_rw/updateJSON.do';
  
  var _editAttr={width:'auto',closeAfterEdit:true,closeOnEscape:true,afterSubmit:_afterSubmit,errorTextFormat:_errorTextFormat,beforeShowForm:_beforeShowEditForm};
  
  var _addAttr={
	serializeEditData: function(data){ return $.param($.extend({}, data, {id:0}));},//pass '0' on add instead of empty string
	closeAfterAdd:true,closeOnEscape:true,errorTextFormat:_errorTextFormat,afterSubmit:_afterSubmit,beforeShowForm:_beforeShowAddForm,width:'auto'};
  
  var _delAttr={};
  
  var _searchAttr={drag:true,resize:true,modal:true,caption:'Lookup',closeOnEscape:true,sopt:['eq','ne']};
  
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
 
 
$(function(){
		
$("#grid_id").jqGrid({
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

}); 

</script>
</head>
<body>

<tiles:insertAttribute name="banner-content" />

<tiles:insertAttribute name="primary-content" />

<tiles:insertAttribute name="footer-content" />

</body>
</html>
