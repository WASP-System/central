<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
<!-- style adds vertical scrollbar to the autocomplete; code taken from  http://jqueryui.com/demos/autocomplete/#maxheight  -->
<!-- overflow-x: auto; was previously set to hidden -->

<%--  TODO: Declare style in css file (e.g. /src/main/webapp/css/base.css), not in .jsp and reuse where possible !!!! --%>
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


<script type="text/javascript">
   //http://trirand.com/blog/jqgrid/jqgrid.html version 3.7 toolbar search 
  //http://www.ok-soft-gmbh.com/jqGrid/FillToolbarSearchFilter.htm   was really good 
$(document).ready(function() { 
//set column properties for the filterToolbar search 

	jQuery("#grid_id").jqGrid('setColProp', 'currentStatus',
		{
			search:true,
			sopt:['eq'],
			editable: true, edittype: "select", stype: 'select',
			//searchoptions: { sopt: ['eq'], value: ':<fmt:message key="run.readTypeAll.label" />;single:<fmt:message key="run.readTypeSingle.label" />;paired:<fmt:message key="run.readTypePaired.label" />' }
			searchoptions: { sopt: ['eq'], value: "<c:out value="${allJobStatusForDropDownBox}" />" }

		}); 
		
	jQuery("#grid_id").jqGrid('setColProp', 'submitter',
	{
		search:true,
		sopt:['eq'],
		searchoptions: {
			dataInit: function(elem) {	
				setTimeout(
					function(){ 
						$.getJSON("<wasp:relativeUrl value='autocomplete/getAllUserNamesAndLoginForDisplay.do' />", 
						{ adminNameFragment: "" }, 
						function(data) { 
							jQuery(elem).autocomplete(data);
						} );
					}, 200
				);
			}
		}
	});

	jQuery("#grid_id").jqGrid('setColProp', 'lab',
	{
		search:true,
		sopt:['eq'],
		searchoptions: {
			dataInit: function(elem) {	
				setTimeout(
					function(){ 
						$.getJSON("<wasp:relativeUrl value='autocomplete/getPiNamesAndLoginForDisplay.do' />",
						//Not used $.getJSON("<wasp:relativeUrl value='autocomplete/getPiForAutocomplete.do' />", 
						{ piNameFragment: "" }, 
						function(data) { 
							jQuery(elem).autocomplete(data);
						} );
					}, 200
				);
			}
		}
	});

	jQuery("#grid_id").jqGrid('setColProp', 'submitted_on',
	{
		search:true,
		sopt:['eq'],
		searchoptions: {
			dataInit: function(elem) {	
				jQuery(elem).datepicker({ dateFormat: "yy/mm/dd" });
			}
		}
	});

//function to validate the user-entered data 
validate = function(){
 
	var jobId = $('#gs_jobId').val();
	if(typeof(jobId) !== 'undefined' && jobId != null && jobId.length>0){
		var numberFormat=new RegExp("^[0-9]+$");
		if(!numberFormat.test(jobId)){
			//alert("Required jobId format: all digits");
			alert("<fmt:message key="job2quote.job2quote_jobIdAlert.label"/>");
			return true;//do not perform search 
		}
	}
	
	var properNameAndLoginFormat=new RegExp("^.*?\\({1}([-\\w+]+)\\){1}$");
	
	var submitter = $('#gs_submitter').val();//may not always be defined 
	if(typeof(submitter) !== 'undefined' && submitter != null && submitter.length>0){
		//could have subsituted if(typeof(submitter) !== 'undefined' && submitter != null && submitter.length>0) with if(submitter && submitter.length>0)  
		if(!properNameAndLoginFormat.test(submitter)){
			//alert("Required Submitter format: firstname lastname (login). It is best to select name from list.");
			alert("<fmt:message key="job2quote.job2quote_submitterAlert.label"/>");
			return true;//do not perform search 
		}
	}
	
	var pi = $('#gs_lab').val();	//may not always be defined 
	if(typeof(pi) !== 'undefined' && pi != null && pi.length>0){
		//could have subsituted if(typeof(pi) !== 'undefined' && pi != null && pi.length>0) with if(pi && pi.length>0)  
		if(!properNameAndLoginFormat.test(pi)){
			//alert("Required PI format: firstname lastname (login). It is best to select name from list.");
			alert("<fmt:message key="job2quote.job2quote_pIAlert.label"/>");
			return true;//do not perform search 
		}
	}
	
	var submitted_on = $('#gs_submitted_on').val();		 
	if(typeof(submitted_on) !== 'undefined' && submitted_on != null && submitted_on.length>0){
		var dateFormat=new RegExp("^[1-2][0-9][0-9][0-9]/[0-1][0-9]/[0-3][0-9]$");
		if(!dateFormat.test(submitted_on)){
			//alert("Required date format: YYYY/MM/DD. It is best to use calendar to select date.");
			alert("<fmt:message key="job2quote.job2quote_dateAlert.label"/>");
			return true;//do not perform search 
		}
	}

	return false;//perform search 
};

//add filtertoolbar to grid 
jQuery("#grid_id").jqGrid('filterToolbar', {stringResult:false, searchOnEnter:true, defaultSearch:"eq", beforeSearch: validate }); 
//add search icon to navgrid and link it's being clicked to filterToolbar (so that filterToolbar search begins when the search icon is pressed (or the default, which is when ENTER is pressed)) 
jQuery("#grid_id").jqGrid('navButtonAdd','#gridpager',{caption:"",title:"<fmt:message key="grid.icon_search.label" />", buttonicon :'ui-icon-search', onClickButton:function(){ $("#grid_id")[0].triggerToolbar(); } }); 

//waspTooltip();

});//end document.ready() 
  
</script>
