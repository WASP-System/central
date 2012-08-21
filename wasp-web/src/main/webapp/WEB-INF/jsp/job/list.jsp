<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
<!-- style adds vertical scrollbar to the autocomplete; code taken from  http://jqueryui.com/demos/autocomplete/#maxheight  -->
<!-- overflow-x: auto; was previously set to hidden -->
<style>
	.ui-autocomplete {
		max-height: 400px;
		overflow-y: auto;
		/* prevent horizontal scrollbar */
		overflow-x: auto; 
		/* add padding to account for vertical scrollbar */
		padding-right: 20px;
		white-space:nowrap;
	}
	/* IE 6 doesn't support max-height
	 * we use height instead, but this forces the menu to always be this tall
	 */
	* html .ui-autocomplete {
		height: 100px;
	}
</style>

<script type="text/javascript" src="/wasp/scripts/jquery/jquery-ui-1.8.18.custom.min.js"></script> 

<c:if test='${viewerIsFacilityMember=="true"}'>
<script type="text/javascript">
   //http://trirand.com/blog/jqgrid/jqgrid.html version 3.7 toolbar search 
  //http://www.ok-soft-gmbh.com/jqGrid/FillToolbarSearchFilter.htm   was really good 
$(document).ready(function() { 
//set column properties for the filterToolbar search 
var url_string = window.location.href; 
if(url_string.indexOf("userId") == -1){ //url does NOT contain the string userId, so permit search of submitter and pi using filterToolbar and autocomplete 

	jQuery("#grid_id").jqGrid('setColProp', 'submitter',
	{
		search:true,
		sopt:['eq'],
		searchoptions: {
			dataInit: function(elem) {	
				setTimeout(
					function(){ 
						$.getJSON("/wasp/autocomplete/getAllUserNamesAndLoginForDisplay.do", 
						{ adminNameFragment: "" }, 
						function(data) { 
							jQuery(elem).autocomplete(data);
						} );
					}, 200
				);
			}
		}
	});

	jQuery("#grid_id").jqGrid('setColProp', 'pi',
	{
		search:true,
		sopt:['eq'],
		searchoptions: {
			dataInit: function(elem) {	
				setTimeout(
					function(){ 
						$.getJSON("/wasp/autocomplete/getPiNamesAndLoginForDisplay.do",
						//Not used $.getJSON("/wasp/autocomplete/getPiForAutocomplete.do", 
						{ piNameFragment: "" }, 
						function(data) { 
							jQuery(elem).autocomplete(data);
						} );
					}, 200
				);
			}
		}
	});
}//end if 
jQuery("#grid_id").jqGrid('setColProp', 'name',
{
	search:true,
	sopt:['eq'],
	searchoptions: {
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
jQuery("#grid_id").jqGrid('setColProp', 'createts',
{
	search:true,
	sopt:['eq'],
	searchoptions: {
		dataInit: function(elem) {	
			jQuery(elem).datepicker();
		}
	}
});
 
//function to validate the user-entered data 
validate = function(){

	var jobId = $('#gs_jobId').val();
	if(typeof(jobId) !== 'undefined' && jobId != null && jobId.length>0){
		var numberFormat=new RegExp("^[0-9]+$");
		if(!numberFormat.test(jobId)){
			alert("Required jobId format: all digits");
			return true;//do not perform search 
		}
	}
	
	var name = $('#gs_name').val(); 
	
	var properNameAndLoginFormat=new RegExp("^.*?\\({1}([-\\w+]+)\\){1}$");
	
	var submitter = $('#gs_submitter').val();//may not always be defined 
	if(typeof(submitter) !== 'undefined' && submitter != null && submitter.length>0){
		//could have subsituted if(typeof(submitter) !== 'undefined' && submitter != null && submitter.length>0) with if(submitter && submitter.length>0)  
		if(!properNameAndLoginFormat.test(submitter)){
			alert("Required Submitter format: firstname lastname (login). It is best to select name from list.");
			return true;//do not perform search 
		}
	}
	
	var pi = $('#gs_pi').val();	//may not always be defined 
	if(typeof(pi) !== 'undefined' && pi != null && pi.length>0){
		//could have subsituted if(typeof(pi) !== 'undefined' && pi != null && pi.length>0) with if(pi && pi.length>0)  
		if(!properNameAndLoginFormat.test(pi)){
			alert("Required PI format: firstname lastname (login). It is best to select name from list.");
			return true;//do not perform search 
		}
	}
	var createts = $('#gs_createts').val();		 
	if(typeof(createts) !== 'undefined' && createts != null && createts.length>0){
		var dateFormat=new RegExp("^[0-1][0-9]/[0-3][0-9]/[1-2][0-9][0-9][0-9]$");
		if(!dateFormat.test(createts)){
			alert("Required date format: MM/DD/YYYY. It is best to use calendar to select date.");
			return true;//do not perform search 
		}
	}
	return false;//perform search 
};

//add filtertoolbar to grid 
jQuery("#grid_id").jqGrid('filterToolbar', {stringResult:false, searchOnEnter:true, defaultSearch:"eq", beforeSearch: validate }); 
//add search icon to navgrid and link it's being clicked to filterToolbar (so that filterToolbar search begins when the search icon is pressed (or the default, which is when ENTER is pressed)) 
jQuery("#grid_id").jqGrid('navButtonAdd','#gridpager',{caption:"",title:"Search", buttonicon :'ui-icon-search', onClickButton:function(){ $("#grid_id")[0].triggerToolbar(); } }); 

});//end document.ready() 
  
</script>
</c:if> 
 
<center>  
<br /><br />
<table id="grid_id"></table> 
<div id="gridpager"></div>
</center> 


<!--  
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
});
jQuery("#list").jqGrid("navGrid","#pager", {edit:false, add:false, del:false, search:false, beforeRefresh: 
	function () { 
		var url = window.location.href; 
		if(url.indexOf("userId") != -1){//url contains this string (indicating coming from jobGrid), upon refresh, change url to remove; will cause a COMPLETE refresh of page rather than a JSON call 
   			window.location.replace("list.do"); //completely refresh the page, without the userId and labId request parameters 
		}
	}
});
 

});
</script>

//permit toolbar searching only by facility personnel 
<c:if test='${viewerIsFacilityMember=="true"}'>
<script type="text/javascript">
///http://trirand.com/blog/jqgrid/jqgrid.html version 3.7 toolbar search 
//http://www.ok-soft-gmbh.com/jqGrid/FillToolbarSearchFilter.htm   was really good 

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
					//Not used $.getJSON("/wasp/autocomplete/getPiForAutocomplete.do", 
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
 
jQuery("#list").jqGrid('filterToolbar', {stringResult:false, searchOnEnter:true, defaultSearch:"cn", 
	beforeSearch: function(){
		var jobId = $('#gs_jobId').val();
		if(typeof(jobId) !== 'undefined' && jobId != null && jobId.length>0){
			var numberFormat=new RegExp("^[0-9]+$");
			if(!numberFormat.test(jobId)){
				alert("Required jobId format: all digits");
				return true;//do not perform search 
			}
		}
		
		var name = $('#gs_name').val(); 
		
		var properNameAndLoginFormat=new RegExp("^.*?\\({1}([-\\w+]+)\\){1}$");
		
		var submitter = $('#gs_submitter').val();//may not always be defined 
		if(typeof(submitter) !== 'undefined' && submitter != null && submitter.length>0){
			//could have subsituted if(typeof(submitter) !== 'undefined' && submitter != null && submitter.length>0) with if(submitter && submitter.length>0)  
			if(!properNameAndLoginFormat.test(submitter)){
				alert("Required Submitter format: firstname lastname (login). It is best to select name from list.");
				return true;//do not perform search 
			}
		}
		
		var pi = $('#gs_pi').val();	//may not always be defined 
		if(typeof(pi) !== 'undefined' && pi != null && pi.length>0){
			//could have subsituted if(typeof(pi) !== 'undefined' && pi != null && pi.length>0) with if(pi && pi.length>0)  
			if(!properNameAndLoginFormat.test(pi)){
				alert("Required PI format: firstname lastname (login). It is best to select name from list.");
				return true;//do not perform search 
			}
		}
		var createts = $('#gs_createts').val();		 
		if(typeof(createts) !== 'undefined' && createts != null && createts.length>0){
			var dateFormat=new RegExp("^[0-1][0-9]/[0-3][0-9]/[1-2][0-9][0-9][0-9]$");
			if(!dateFormat.test(createts)){
				alert("Required date format: MM/DD/YYYY. It is best to use calendar to select date.");
				return true;//do not perform search 
			}
		}
		return false;//perform search 
	} 
}); 
//if stringResult:true then jsp sends a requestParameter names filters that contains the search as JSON 
//else if false, you can capture the requestParameter in the controller based on the column's name/id   

//jQuery("#list")[0].toggleToolbar();//hide the toolbar 
//jQuery("#list").jqGrid('navButtonAdd',"#pager",{caption:"",title:"Search", buttonicon :'ui-icon-search', onClickButton:function(){ $("#list")[0].toggleToolbar(); } }); //icon to add the search bar with search textbox  

jQuery("#list").jqGrid('navButtonAdd',"#pager",{caption:"",title:"Search", buttonicon :'ui-icon-search', onClickButton:function(){ $("#list")[0].triggerToolbar(); } }); //icon to trigger search via toolbar (can also trigger viat ENTER key)

});//end document.ready() 
</script>
</c:if> 

<center>  
<br /><br />
<table id="list"></table> 
<div id="pager"></div>
</center> 
-->