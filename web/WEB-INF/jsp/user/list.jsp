<html>

<head>
 
  <link rel="stylesheet" type="text/css" media="screen" href="/wasp/css/jquery/jquery-ui.css" />
  <link rel="stylesheet" type="text/css" media="screen" href="/wasp/css/jquery/ui.jqgrid.css" />
  <style>
	html, body {
    margin: 0;
    padding: 0;
    font-size: 75%;
	}
	</style>
  
  <script src="/wasp/scripts/jquery/jquery-1.6.2.js" type="text/javascript"></script>
  <script src="/wasp/scripts/jqgrid/grid.locale-en.js" type="text/javascript"></script>
  <script src="/wasp/scripts/jqgrid/jquery.jqGrid.min.js" type="text/javascript"></script>
  
  <script type="text/javascript">
$(function(){ 
  $("#list").jqGrid({
    url:'/wasp/user/listJSON.do',
    datatype: 'json',
    mtype: 'GET',
    colNames:['Login','First Name','Last Name', 'Email','Locale','Active'],
    colModel :[ 
      {name:'login', index:'login', width:80, align:'right'},
      {name:'firstName', width:100, align:'right'}, 
      {name:'lastName', width:100, align:'right'},    
      {name:'email', index:'email', width:255, align:'right'}, 
      {name:'locale', index:'locale', width:70, align:'center'}, 
      {name:'isActive', index:'isActive', width:70, align:'center'}      
    ],
    pager: '#pager',
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
	height: 300
  }); 
}); 
</script>
  
</head>
<body>
<%@ include file="/WEB-INF/jsp/include.jsp" %>

<h1>User List</h1>
 <a href="/wasp/user/create/form.do">create</a>
<table id="list"><tr><td/></tr></table> 
<div id="pager"></div>

</table>
</body>
</html>