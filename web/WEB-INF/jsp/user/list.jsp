<html>

<head> 
<%@ include file="/WEB-INF/jsp/include.jsp" %>
 
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
  <script src="/wasp/scripts/jqgrid/grid.locale-en.js" type="text/javascript"></script>
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
 
  //these will be populated by the wasp:field tags below
  var colNames=[];  
  var colModel=[];  
  var colErrors=[];
  
  <wasp:field name="login" object="user" />  
  <wasp:field name="password" object="user" />
  <wasp:field name="firstName" object="user" />
  <wasp:field name="lastName" object="user" />
  <wasp:field name="email" object="user" />
  <wasp:field name="locale" object="user" />
  <wasp:field name="isActive" object="user" />
  
  
  email.jq['editrules']={custom:true,custom_func:_validate_email};
  
  isActive.jq['edittype']='checkbox';
  isActive.jq['editoptions']={value:"1:0"}; 
  isActive.jq['formatter']='checkbox';
  isActive.jq['formatoptions']={disabled : true};
  isActive.jq['align']='center';
  isActive.jq['search']=false;
  
  locale.jq['edittype']='select';
  locale.jq['editoptions']={value:{}};
  locale.jq['search']=false;
  
  password.jq['hidden']=true;
  password.jq['edittype']='password';  
  password.jq['editrules']={edithidden:true};
  password.jq['formoptions']={};//{elmsuffix:'<font color=red>*</font>'};
  
  <c:forEach var="localeEntry" items="${locales}">     
  	locale.jq['editoptions']['value']['${localeEntry.key}']='${localeEntry.value}';
  </c:forEach>
    
  //get meta fiel definitions from servlet
  var _wasp_meta_fields=${fieldsArr};
  
  
  //add to jqGrid definitions
  for (key in _wasp_meta_fields) {
	  	var _wasp_field=_wasp_meta_fields[key];
	  	var _wasp_prop=_wasp_field['property'];
	  	var _field_name=_wasp_field['k'];
	  	
	  	colNames.push(_wasp_prop['label']);
	  	var constraint = _wasp_prop['constraint'];
	  	var required=constraint=='NotEmpty';
	  
	  	var editrules={edithidden:true};
	  	var formoptions={};
	  	if (required) {
	  		formoptions={elmsuffix:'<font color=red>*</font>'};
	  		editrules={edithidden:true,custom:true,custom_func:_validate_required};	
	  	}
	  	
	  
	  	colModel.push(
	  			{name:_field_name, width:80, align:'right',hidden:true,editable:true,editrules:editrules,formoptions:formoptions,editoptions:{size:20}}
	  	);
	  	
	  	colErrors.push(_wasp_prop['error']);

  }
  
 
  _del_function = function (id) {
	   alert("User cannot be deleted once created. Instead, use the 'edit' button to mark the '"+$("#grid_id").getRowData(id).login+"' user as inactive.");
	   return false;
	};	
	
	
	_errorTextFormat = function(response) {
		return response.responseText;
	}

	
   _afterSubmit = function(response, data) {
	
	   var myInfo = '<div class="ui-state-highlight ui-corner-all">'+

       '<span class="ui-icon ui-icon-info" style="float: left; margin-right: .3em;"></span>' +

       '<strong>Status:</strong> ' +

       response.responseText +

       '</div>';
       
       grid = $("#grid_id");

       var infoTR = $("table#TblGrid_"+grid[0].id+">tbody>tr.tinfo"); 

       var infoTD = infoTR.children("td.topinfo");
       
       infoTD.html(myInfo);
       
       infoTR.show();
       
       setTimeout(function() {

   		    infoTD.children("div").fadeOut('slow',

     		function() {       		
       			infoTR.hide();
   			});
   		},5000);
       

       //reload grid
       grid.trigger("reloadGrid");

  	   return [false,''];
       
   }

  
$(function(){ 
  $("#grid_id").jqGrid({
    url:'/wasp/user/listJSON.do',
    editurl:'/wasp/user/detail_rw/updateJSON.do',
    datatype: 'json',
    mtype: 'GET',
    colNames:colNames,
    colModel : colModel,
    pager: '#gridpager',
    rowNum:100,    
    rowList:[5,10,20],
    sortname: 'login',
    sortorder: 'desc',
    viewrecords: true,
    gridview: true,	
    autowidth: true,
	loadui: 'block',
	scroll: true,
	emptyrecords: 'No users',
	height: 'auto',
	caption: "User List",
    ondblClickRow: function(rowid) {
    	$("#grid_id").jqGrid('editGridRow',rowid,{closeAfterEdit:false,closeOnEscape:true,afterSubmit:_afterSubmit,errorTextFormat:_errorTextFormat});
   }
  }).navGrid('#gridpager',{view:true, del:true,delfunc:_del_function}, 
		  {closeAfterEdit:false,closeOnEscape:true,afterSubmit:_afterSubmit,errorTextFormat:_errorTextFormat}, // edit
		  {serializeEditData: function(data){ 
			    return $.param($.extend({}, data, {id:0}));
		  },closeAfterAdd:true,closeOnEscape:true,errorTextFormat:_errorTextFormat}, // add
		  {},  // delete
		  {drag:true,resize:true,modal:true,caption:'Lookup Users',closeOnEscape:true,sopt:['eq','ne']}, //search
		  {} /* allow the view dialog to be closed when user press ESC key*/
		  );

}); 



</script>
  
</head>
<body>


<table id="grid_id"></table> 
<div id="gridpager"></div>

</script>

</table>
</body>
</html>