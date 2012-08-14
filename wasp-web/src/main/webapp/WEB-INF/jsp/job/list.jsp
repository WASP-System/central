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
	width: 600,
   	colNames:['JobID','Job Name', 'Submitter','PI','Submitted','Result'],
	colModel: [
		{ name:'jobId', index:'jobId', width:250 },
		{ name:'name', index:'name', width:250 },
		{ name:'submitter', index:'submitter', width:250 },
		{ name:'pi', index:'pi', width:250 },
		{ name:'createts', index:'createts', width:200 },
		{ name:'viewfiles', index:'viewfiles', width:200, search: false, sortable: false }
	],
	sortname: 'jobId',
	viewrecords: true,
	rownumbers: true,
	sortorder: "desc",
	ignoreCase: true,
	pager: '#pager',
	height: "auto",
	caption: "Jobs (must internationalize)"
}).jqGrid('navGrid','#pager', {edit:false, add:false, del:false, search:false});
});
</script>

<sec:authorize access="hasRole('su') or hasRole('fm') or hasRole('ft') or hasRole('sa') or hasRole('ga') or hasRole('da')">
<script>
$(document).ready(function() { 
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
/* this works, but I cannot make the SQL work
jQuery("#list").jqGrid('setColProp', 'createts',
{
			searchoptions: {
				sopt:['eq'],
				dataInit: function(elem) {	
					jQuery(elem).datepicker();
				}
			}
});
*/
jQuery("#list").jqGrid('filterToolbar', {stringResult:false, searchOnEnter:true, defaultSearch:"cn"}); 
//if stringResult:true then jsp sends a requestParameter names filters that contains the search as JSON 
//else if false, you can capture the requestParameter in the controller based on the column's name/id   
 
 
});
     
 </script>
</sec:authorize> 
 
 


<center>
<table id="list"><tr><td/></tr></table>
<div id="pager"></div>
</center> 
