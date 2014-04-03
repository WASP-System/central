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



<script type="text/javascript">
   //http://trirand.com/blog/jqgrid/jqgrid.html version 3.7 toolbar search 
  //http://www.ok-soft-gmbh.com/jqGrid/FillToolbarSearchFilter.htm   was really good 
$(document).ready(function() { 
//set column properties for the filterToolbar search 

var url_string = window.location.href; 
if(url_string.indexOf("selId") == -1){ //url does NOT contain the string userId, so permit search of submitter and pi using filterToolbar and autocomplete 

	jQuery("#grid_id").jqGrid('setColProp', 'login',
	{
		search:true,
		sopt:['eq'],
		searchoptions: {
			dataInit: function(elem) {	
				setTimeout(
					function(){ 
						$.getJSON("<c:url value='autocomplete/getAllUserLoginsForDisplay.do' />", 
						{ str: "" }, 
						function(data) { 
							jQuery(elem).autocomplete(data);
						} );
					}, 200
				);
			}
		}
	});

	jQuery("#grid_id").jqGrid('setColProp', 'firstName',
	{
		search:true,
		sopt:['eq'],
		searchoptions: {
			dataInit: function(elem) {	
				setTimeout(
					function(){ 
						$.getJSON("<c:url value='autocomplete/getDistinctUserFirstNamesForDisplay.do' />",
						//Not used $.getJSON("<c:url value='autocomplete/getPiForAutocomplete.do' />", 
						{ str: "" }, 
						function(data) { 
							jQuery(elem).autocomplete(data);
						} );
					}, 200
				);
			}
		}
	});

jQuery("#grid_id").jqGrid('setColProp', 'lastName',
{
	search:true,
	sopt:['eq'],
	searchoptions: {
		dataInit: function(elem) {	
			setTimeout(
				function(){ 
					$.getJSON("<c:url value='autocomplete/getDistinctUserLastNamesForDisplay.do' />", 
					{ str: "" }, 
					function(data) { 
						jQuery(elem).autocomplete(data);
					} );
				}, 200
			);
		}
	}
});
jQuery("#grid_id").jqGrid('setColProp', 'email',
		{
			search:true,
			sopt:['eq'],
			searchoptions: {
				dataInit: function(elem) {	
					setTimeout(
						function(){ 
							$.getJSON("<c:url value='autocomplete/getAllUserEmailsForDisplay.do' />", 
							{ str: "" }, 
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

	var emailFormat=new RegExp("^\\s*[\\w\\-\\+_]+(\\.[\\w\\-\\+_]+)*\\@[\\w\\-\\+_]+\\.[\\w\\-\\+_]+(\\.[\\w\\-\\+_]+)*\\s*$");
	var email = $('#gs_email').val();//may not always be defined
	if(typeof(email) !== 'undefined' && email != null && email.length>0){
		//could have subsituted if(typeof(submitter) !== 'undefined' && submitter != null && submitter.length>0) with if(submitter && submitter.length>0)  
		if(!emailFormat.test(email)){
			alert("Email does Not appear to be in the proper format. Please confirm.");
			return true;//do not perform search 
		}
	}
	return false;//perform search 
};

//add filtertoolbar to grid 
jQuery("#grid_id").jqGrid('filterToolbar', {stringResult:false, searchOnEnter:true, defaultSearch:"eq", beforeSearch: validate }); 
//add search icon to navgrid and link it's being clicked to filterToolbar (so that filterToolbar search begins when the search icon is pressed (or the default, which is when ENTER is pressed)) 
jQuery("#grid_id").jqGrid('navButtonAdd','#gridpager',{caption:"",title:"<fmt:message key="grid.icon_search.label" />", buttonicon :'ui-icon-search', onClickButton:function(){ $("#grid_id")[0].triggerToolbar(); } }); 

} //end if  

});//end document.ready() 
  
</script>




<center>
<br /><br />
<table id="grid_id"></table> 
<div id="gridpager"></div>

</center>    