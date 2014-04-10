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

	jQuery("#grid_id").jqGrid('setColProp', 'name',
	{
		search:true,
		sopt:['eq'],
		searchoptions: {
			dataInit: function(elem) {	
				setTimeout(
					function(){ 
						$.getJSON("<wasp:relativeUrl value='autocomplete/getSampleNamesFromJobsForDisplay.do' />", 
						{ str: "" }, 
						function(data) { 
							jQuery(elem).autocomplete(data);
						} );
					}, 200
				);
			}
		}
	});


	jQuery("#grid_id").jqGrid('setColProp', 'type',
	{
		search:true,
		sopt:['eq'],
		searchoptions: {
			dataInit: function(elem) {	
				setTimeout(
					function(){ 
						$.getJSON("<wasp:relativeUrl value='autocomplete/getSampleTypesThatAreBiomaterialsForDisplay.do' />", 
						{ str: "" }, 
						function(data) { 
							jQuery(elem).autocomplete(data);
						} );
					}, 200
				);
			}
		}
	});
/*
	jQuery("#grid_id").jqGrid('setColProp', 'subtype',
			{
				search:true,
				sopt:['eq'],
				searchoptions: {
					dataInit: function(elem) {	
						setTimeout(
							function(){ 
								$.getJSON("<wasp:relativeUrl value='autocomplete/getSequenceRunNamesForDisplay.do' />", 
								{ str: "" }, 
								function(data) { 
									jQuery(elem).autocomplete(data);
								} );
							}, 200
						);
					}
				}
			});
*/
	jQuery("#grid_id").jqGrid('setColProp', 'jobId',
			{
				search:true,
				sopt:['eq'],
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
	
	jQuery("#grid_id").jqGrid('setColProp', 'pi',
			{
				search:true,
				sopt:['eq'],
				searchoptions: {
					dataInit: function(elem) {	
						setTimeout(
							function(){ 
								$.getJSON("<wasp:relativeUrl value='autocomplete/getPiNamesAndLoginForDisplay.do' />",  
								{ piNameFragment: "" }, 
								function(data) { 
									jQuery(elem).autocomplete(data);
								} );
							}, 200
						);
					}
				}
			});

	

	
	//function to validate the user-entered data 
	validate = function(){
		
		var jobId = $('#gs_jobId').val();
		if(typeof(jobId) !== 'undefined' && jobId != null && jobId.length>0){
			var numberFormat=new RegExp("^[0-9]+$");
			if(!numberFormat.test(jobId)){
				alert("<fmt:message key="grid.jobIdFormat.label"/>");
				return true;//do not perform search 
			}
		}
		
		var properNameAndLoginFormat=new RegExp("^.*?\\({1}([-\\w+]+)\\){1}$");
		
		var submitter = $('#gs_submitter').val();//may not always be defined 
		if(typeof(submitter) !== 'undefined' && submitter != null && submitter.length>0){
			//could have subsituted if(typeof(submitter) !== 'undefined' && submitter != null && submitter.length>0) with if(submitter && submitter.length>0)  
			if(!properNameAndLoginFormat.test(submitter)){
				alert("<fmt:message key="grid.submitterFormat.label"/>");
				return true;//do not perform search 
			}
		}
		
		var pi = $('#gs_pi').val();	//may not always be defined 
		if(typeof(pi) !== 'undefined' && pi != null && pi.length>0){
			//could have subsituted if(typeof(pi) !== 'undefined' && pi != null && pi.length>0) with if(pi && pi.length>0)  
			if(!properNameAndLoginFormat.test(pi)){
				alert("<fmt:message key="grid.piFormat.label"/>");
				return true;//do not perform search 
			}
		}
/*
		var readLength = $('#gs_readLength').val();
		readLength = readLength.replace(/^\s+|\s+$/g,'');//trim 
		var numberFormat=new RegExp("^[0-9]+$");
		if(typeof(readLength) !== 'undefined' && readLength != null && readLength.length>0){
			if(!numberFormat.test(readLength)){
				alert("Readlength must be an integer");
				return true;//do not perform search 
			}
		}
		
		var dateRunStarted = $('#gs_dateRunStarted').val();	
		dateRunStarted = dateRunStarted.replace(/^\s+|\s+$/g,'');//trim 
		if(typeof(dateRunStarted) !== 'undefined' && dateRunStarted != null && dateRunStarted.length>0){
			var dateFormat=new RegExp("^[1-2][0-9][0-9][0-9]/[0-1][0-9]/[0-3][0-9]$");
			if(!dateFormat.test(dateRunStarted)){
				alert("Required date format: MM/DD/YYYY. It is best to use calendar to select date.");
				return true;//do not perform search 
			}
		}
		var dateRunEnded = $('#gs_dateRunEnded').val();	
		dateRunEnded = dateRunEnded.replace(/^\s+|\s+$/g,'');//trim 
		if(typeof(dateRunEnded) !== 'undefined' && dateRunEnded != null && dateRunEnded.length>0){
			var dateFormat=new RegExp("^[1-2][0-9][0-9][0-9]/[0-1][0-9]/[0-3][0-9]$");
			if(!dateFormat.test(dateRunEnded)){
				alert("Required date format: YYYY/MM/DD. It is best to use calendar to select date.");
				return true;//do not perform search 
			}
		}
*/		 
		return false;//perform search 
	};
	
	
	
//add filtertoolbar to grid 
jQuery("#grid_id").jqGrid('filterToolbar', {stringResult:false, searchOnEnter:true, defaultSearch:"eq", beforeSearch: validate }); 
//add search icon to navgrid and link it's being clicked to filterToolbar (so that filterToolbar search begins when the search icon is pressed (or the default, which is when ENTER is pressed)) 
jQuery("#grid_id").jqGrid('navButtonAdd','#gridpager',{caption:"",title:"<fmt:message key="grid.icon_search.label" />", buttonicon :'ui-icon-search', onClickButton:function(){ $("#grid_id")[0].triggerToolbar(); } }); 

//add a new run - we cannot have Add New Run Button on this grid, since new runs are dependent on platformUnit 
//this next line is simply example code located on the platformUnit grid: to navigate to page to add a platform unit 
///jQuery("#grid_id").jqGrid('navButtonAdd','#gridpager',{caption:"", position: "first", title:"New", buttonicon :'ui-icon-plus', onClickButton:function(){ location.href="<wasp:relativeUrl value='facility/platformunit/createUpdatePlatformUnit.do?sampleSubtypeId=0&sampleId=0' />"; } }); 



});//end document.ready() 
  
</script>

 
<center>  
<br /><br />
<table id="grid_id"></table> 
<div id="gridpager"></div>
</center> 

