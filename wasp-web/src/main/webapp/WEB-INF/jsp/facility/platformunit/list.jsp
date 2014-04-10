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
						$.getJSON("<wasp:relativeUrl value='autocomplete/getPlatformUnitNamesForDisplay.do' />", 
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
						$.getJSON("<wasp:relativeUrl value='autocomplete/getPlatformUnitBarcodesForDisplay.do' />", 
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
								$.getJSON("<wasp:relativeUrl value='autocomplete/getPlatformUnitSubtypesForDisplay.do' />", 
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
								$.getJSON("<wasp:relativeUrl value='autocomplete/getReadTypesForDisplay.do' />", 
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
								$.getJSON("<wasp:relativeUrl value='autocomplete/getMpsResourceCategoryNamesForDisplay.do' />", 
								{ str: "" }, 
								function(data) { 
									jQuery(elem).autocomplete(data);
								} );
							}, 200
						);
					}
				}
			});

	jQuery("#grid_id").jqGrid('setColProp', 'readLength',
			{
				search:true,
				sopt:['eq'],
				//searchrules:{integer:true} //won't work in filterToolbar, just a search dialog box 
			});

	jQuery("#grid_id").jqGrid('setColProp', 'cellcount',
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
						jQuery(elem).datepicker({ dateFormat: "yy/mm/dd" });
					}
				}
			});

	//function to validate the user-entered data 
	validate = function(){

		var readLength = $('#gs_readLength').val();
		readLength = readLength.replace(/^\s+|\s+$/g,'');//trim 
		var cellcount = $('#gs_cellcount').val();
		cellcount = cellcount.replace(/^\s+|\s+$/g,'');//trim 
		var numberFormat=new RegExp("^[0-9]+$");
		if(typeof(readLength) !== 'undefined' && readLength != null && readLength.length>0){
			if(!numberFormat.test(readLength)){
				alert("<fmt:message key="grid.readLengthInteger.label" />");
				return true;//do not perform search 
			}
		}
		if(typeof(cellcount) !== 'undefined' && cellcount != null && cellcount.length>0){
			if(!numberFormat.test(cellcount)){
				alert("<fmt:message key="grid.cellsInteger.label" />");
				return true;//do not perform search 
			}
		}
		
		var date = $('#gs_date').val();	
		date = date.replace(/^\s+|\s+$/g,'');//trim 
		if(typeof(date) !== 'undefined' && date != null && date.length>0){
			var dateFormat=new RegExp("^[1-2][0-9][0-9][0-9]/[0-1][0-9]/[0-3][0-9]$");
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
jQuery("#grid_id").jqGrid('navButtonAdd','#gridpager',{caption:"", position: "first", title:"<fmt:message key="grid.icon_new.label" />", buttonicon :'ui-icon-plus', onClickButton:function(){ location.href="<wasp:relativeUrl value='facility/platformunit/createUpdatePlatformUnit.do?sampleSubtypeId=0&sampleId=0' />"; } }); 



});//end document.ready() 
  
</script>

 
<center>  
<br /><br />
<table id="grid_id"></table> 
<div id="gridpager"></div>
</center> 
