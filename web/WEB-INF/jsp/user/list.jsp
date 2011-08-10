<html>

<head>
 
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
$(function(){ 
  $("#grid_id").jqGrid({
    url:'/wasp/user/listJSON.do',
    datatype: 'json',
    mtype: 'GET',
    colNames:['Login','First Name','Last Name', 'Email','Locale','Active'],
    colModel :[ 
      {name:'login', index:'login', width:80, align:'right',editable:false,editoptions:{readonly:true,size:10}},
      {name:'firstName', width:100, align:'right',editable:true,editoptions:{size:20}}, 
      {name:'lastName', width:100, align:'right',editable:true,editoptions:{size:20}},    
      {name:'email', index:'email', width:255, align:'right',editable:true,editoptions:{size:100}}, 
      {name:'locale', index:'locale', width:70, align:'center',editable:false,editoptions:{readonly:true,size:10}}, 
      {name:'isActive', index:'isActive', width:70, align:'center',editable:false,editoptions:{readonly:true,size:10}}      
    ],
    pager: '#gridpager',
    rowNum:50,    
    sortname: 'login',
    sortorder: 'desc',
    viewrecords: true,
    gridview: true,
	caption: '',
    autowidth: true,
	loadui: 'block',
	scroll: true,
	emptyrecords: 'No users',
	height: 300,

	caption: "User List"
  }).navGrid('#gridpager',{view:true, del:true}, 
		  {}, // use default settings for edit
		  {}, // use default settings for add
		  {},  // delete instead that del:false we need this
		  {multipleSearch : true}, // enable the advanced searching
		  {closeOnEscape:true} /* allow the view dialog to be closed when user press ESC key*/
		  );

}); 
</script>
  
</head>
<body>
<%@ include file="/WEB-INF/jsp/include.jsp" %>

<h1>User List</h1>
 <a href="/wasp/user/create/form.do">create</a>
 
</br>
 
<table id="grid_id"></table> 
<div id="gridpager"></div>

</table>
</body>
</html>