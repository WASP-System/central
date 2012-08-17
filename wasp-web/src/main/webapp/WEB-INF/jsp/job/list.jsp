<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
<script type="text/javascript" src="/wasp/scripts/jquery/jquery-ui-1.8.18.custom.min.js"></script> 
 <script type="text/javascript">
///http://trirand.com/blog/jqgrid/jqgrid.html version 3.7 toolbar search 
//http://www.ok-soft-gmbh.com/jqGrid/FillToolbarSearchFilter.htm   was really good 
$(document).ready(function() { 

jQuery("#list").jqGrid({
	url:'/wasp/job/listJSON.do?selId=${param.selId}&userId=${param.userId}&labId=${param.labId}',
    datatype: 'JSON',
    mtype: 'GET',
    height: 255,
	width: 800,
   	colNames:['JobID','Job Name', 'Submitter','PI','Submitted','Result'],
	colModel: [
		{ name:'jobId', index:'jobId' },
		{ name:'name', index:'name' },
		{ name:'submitter', index:'submitter' },
		{ name:'pi', index:'pi' },
		{ name:'createts', index:'createts' },
		{ name:'viewfiles', index:'viewfiles', search: false, sortable: false }
	],
	sortname: 'jobId',
	sortorder: "desc",
	viewrecords: true,
	rownumbers: true,	
	ignoreCase: true,
	pager: '#pager',
	height: "auto",
	caption: "Job List",
	//next eight lines stolen from AJ's job grid to incorporate subgrid about a job's samples 
	subGrid: true,
	subGridUrl:'/wasp/job/subgridJSON.do',
	subGridModel: [
		{	name : ['Sample Name','Type','Subtype','Received Status'],
			width : ['auto', 'auto', 'auto', 'auto'],
			align : ['center', 'center', 'center', 'center']
		}
	]	
}).jqGrid('navGrid','#pager', {edit:false, add:false, del:false, search:false, beforeRefresh: 
	function () { 
		var url = window.location.href; 
		if(url.indexOf("userId") != -1){//url contains this string (indicating coming from jobGrid), upon refresh, change url to remove; will cause a COMPLETE refresh of page rather than a JSON call 
   			window.location.replace("list.do"); //completely refresh the page, without the userId and labId request parameters 
		}
	}
});
 

});
</script>

<!-- permit toolbar searching only by facility personnel -->
<c:if test='${viewerIsFacilityMember=="true"}'>
<script>
$(document).ready(function() { 
	
var url = window.location.href; 
			
jQuery("#list").jqGrid('setColProp', 'name',
{
searchoptions: {
sopt:['cn'],
dataInit: function(elem) {	
	setTimeout(
				function(){ 
			$.getJSON("/wasp/autocomplete/getJobNamesForDisplay.do", 
					{ jobName: "" }, 
					function(data) { 
						jQuery(elem).autocomplete(data);
					} );
				}, 200
	);
}
}
});

if(url.indexOf("userId") == -1){ //url does NOT contain the string userId, so permit search with autocomplete 

jQuery("#list").jqGrid('setColProp', 'submitter',
{
searchoptions: {
sopt:['cn'],
dataInit: function(elem) {	
setTimeout(
				function(){ 
			$.getJSON("/wasp/autocomplete/getUserNamesAndLoginForDisplay.do", 
					{ adminNameFragment: "" }, 
					function(data) { 
						jQuery(elem).autocomplete(data);
					} );
				}, 200
	);
}
}
});

jQuery("#list").jqGrid('setColProp', 'pi',
{
		searchoptions: {
		sopt:['cn'],
		dataInit: function(elem) {	
		setTimeout(
						function(){ 
					$.getJSON("/wasp/autocomplete/getPiNamesAndLoginForDisplay.do", 
							{ piNameFragment: "" }, 
							function(data) { 
								jQuery(elem).autocomplete(data);
							} );
						}, 200
			);
		}
		}
});

}else {//url does contains the string userId (indicating coming from jobGrid, so prohibit search on these columns 
	jQuery("#list").jqGrid('setColProp', 'submitter', {search:false});
	jQuery("#list").jqGrid('setColProp', 'pi', {search:false});
}


jQuery("#list").jqGrid('setColProp', 'createts',
{
			searchoptions: {
				sopt:['eq'],
				dataInit: function(elem) {	
					jQuery(elem).datepicker();
				}
			}
});
 
jQuery("#list").jqGrid('filterToolbar', {stringResult:false, searchOnEnter:true, defaultSearch:"cn"}); 
//if stringResult:true then jsp sends a requestParameter names filters that contains the search as JSON 
//else if false, you can capture the requestParameter in the controller based on the column's name/id   

//jQuery("#list")[0].toggleToolbar();//hide the toolbar 
//jQuery("#list").jqGrid('navButtonAdd',"#pager",{caption:"",title:"Search", buttonicon :'ui-icon-search', onClickButton:function(){ $("#list")[0].toggleToolbar(); } }); //icon to add the search bar with search textbox  

jQuery("#list").jqGrid('navButtonAdd',"#pager",{caption:"",title:"Search", buttonicon :'ui-icon-search', onClickButton:function(){ $("#list")[0].triggerToolbar(); } }); //icon to trigger search via toolbar (can also trigger viat ENTER key)


});
     
 </script>
</c:if> 
 
<center>
<table id="list"><tr><td/></tr></table>
<div id="pager"></div>
<!--  
<br /><br />
<table id="grid_id"></table> 
<div id="gridpager"></div>
-->
</center> 
