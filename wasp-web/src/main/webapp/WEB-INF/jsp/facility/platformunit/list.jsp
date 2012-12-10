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
						$.getJSON("/wasp/autocomplete/getPlatformUnitNamesForDisplay.do", 
						{ str: "" }, 
						function(data) { 
							jQuery(elem).autocomplete(data);
						} );
					}, 200
				);
			}
		}
	});


	jQuery("#grid_id").jqGrid('setColProp', 'barcode',
	{
		search:true,
		sopt:['eq'],
		searchoptions: {
			dataInit: function(elem) {	
				setTimeout(
					function(){ 
						$.getJSON("/wasp/autocomplete/getPlatformUnitBarcodesForDisplay.do", 
						{ str: "" }, 
						function(data) { 
							jQuery(elem).autocomplete(data);
						} );
					}, 200
				);
			}
		}
	});

	jQuery("#grid_id").jqGrid('setColProp', 'sampleSubtypeName',
			{
				search:true,
				sopt:['eq'],
				searchoptions: {
					dataInit: function(elem) {	
						setTimeout(
							function(){ 
								$.getJSON("/wasp/autocomplete/getPlatformUnitSubtypesForDisplay.do", 
								{ str: "" }, 
								function(data) { 
									jQuery(elem).autocomplete(data);
								} );
							}, 200
						);
					}
				}
			});

	jQuery("#grid_id").jqGrid('setColProp', 'readType',
			{
				search:true,
				sopt:['eq'],
				searchoptions: {
					dataInit: function(elem) {	
						setTimeout(
							function(){ 
								$.getJSON("/wasp/autocomplete/getReadTypesForDisplay.do", 
								{ str: "" }, 
								function(data) { 
									jQuery(elem).autocomplete(data);
								} );
							}, 200
						);
					}
				}
			});
	
	jQuery("#grid_id").jqGrid('setColProp', 'resourceCategoryName',
			{
				search:true,
				sopt:['eq'],
				searchoptions: {
					dataInit: function(elem) {	
						setTimeout(
							function(){ 
								$.getJSON("/wasp/autocomplete/getMpsResourceCategoryNamesForDisplay.do", 
								{ str: "" }, 
								function(data) { 
									jQuery(elem).autocomplete(data);
								} );
							}, 200
						);
					}
				}
			});

	jQuery("#grid_id").jqGrid('setColProp', 'readlength',
			{
				search:true,
				sopt:['eq'],
				//searchrules:{integer:true} //won't work in filterToolbar, just a search dialog box 
			});

	jQuery("#grid_id").jqGrid('setColProp', 'lanecount',
			{
				search:true,
				sopt:['eq'],
				//searchrules:{integer:true} //won't work in filterToolbar, just a search dialog box 
			});
	
	jQuery("#grid_id").jqGrid('setColProp', 'date',
			{
				search:true,
				sopt:['eq'],
				//searchrules:{date:true}, //won't work in filterToolbar, just a search dialog box 
				searchoptions: {
					dataInit: function(elem) {	
						jQuery(elem).datepicker();
					}
				}
			});

	//function to validate the user-entered data 
	validate = function(){

		var readlength = $('#gs_readlength').val();
		readlength = readlength.replace(/^\s+|\s+$/g,'');//trim 
		var lanecount = $('#gs_lanecount').val();
		lanecount = lanecount.replace(/^\s+|\s+$/g,'');//trim 
		var numberFormat=new RegExp("^[0-9]+$");
		if(typeof(readlength) !== 'undefined' && readlength != null && readlength.length>0){
			if(!numberFormat.test(readlength)){
				alert("<fmt:message key="grid.readLengthInteger.label" />");
				return true;//do not perform search 
			}
		}
		if(typeof(lanecount) !== 'undefined' && lanecount != null && lanecount.length>0){
			if(!numberFormat.test(lanecount)){
				alert("<fmt:message key="grid.lanesInteger.label" />");
				return true;//do not perform search 
			}
		}
		
		var date = $('#gs_date').val();	
		date = date.replace(/^\s+|\s+$/g,'');//trim 
		if(typeof(date) !== 'undefined' && date != null && date.length>0){
			var dateFormat=new RegExp("^[0-1][0-9]/[0-3][0-9]/[1-2][0-9][0-9][0-9]$");
			if(!dateFormat.test(date)){
				alert("<fmt:message key="grid.dateFormat.label" />");
				return true;//do not perform search 
			}
		}
		return false;//perform search 
	};
	
	
	
//add filtertoolbar to grid 
jQuery("#grid_id").jqGrid('filterToolbar', {stringResult:false, searchOnEnter:true, defaultSearch:"eq", beforeSearch: validate }); 
//add search icon to navgrid and link it's being clicked to filterToolbar (so that filterToolbar search begins when the search icon is pressed (or the default, which is when ENTER is pressed)) 
jQuery("#grid_id").jqGrid('navButtonAdd','#gridpager',{caption:"",title:"<fmt:message key="grid.icon_search.label" />", buttonicon :'ui-icon-search', onClickButton:function(){ $("#grid_id")[0].triggerToolbar(); } }); 

//navigate to page to add a new platform unit 
jQuery("#grid_id").jqGrid('navButtonAdd','#gridpager',{caption:"", position: "first", title:"<fmt:message key="grid.icon_new.label" />", buttonicon :'ui-icon-plus', onClickButton:function(){ location.href="/wasp/facility/platformunit/createUpdatePlatformUnit.do?sampleSubtypeId=0&sampleId=0"; } }); 



});//end document.ready() 
  
</script>

 
<center>  
<br /><br />
<table id="grid_id"></table> 
<div id="gridpager"></div>
</center> 