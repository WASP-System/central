<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
<script type="text/javascript" src="/wasp/scripts/jquery/jquery-ui-1.8.18.custom.min.js"></script> 
 <script type="text/javascript">
     $(document).ready(function() { 
    	  // ////$("#jobName").keyup(function(){getAuthNames();}); 
	     //  ////$("#submitter").keyup(function(){getAuthNames();});
	     //  ////$("#pi").keyup(function(){getAuthNames();});
	       //$("#jqg2").keyup(function(){getAuthNames();});
	       //$("#jqg2").live('click', function(){alert("this is the alert");});
	       //$("#jqg2").live('keyup', function(){getAuthNames();});
	       //$('[id^="jqg"]').live('click', function(){alert("this is the alert");});
	       //$('input[id^="jqg"]').live('click', function(){alert("this is the alert");});
	       //$('input[id^="jqg"]').live('keyup', function(){alert("this is the alert");});

//$('input[id^="jqg"]').live('keyup', function(){ var num = parseInt(this.id.replace("jqg", "")); getAuthNames2(num);});
	   	       
	       //$("#searchhdfbox_grid_id a").live('click', function(){alert("this is the alert for the close x");});
	       //$("#fbox_grid_id_search").live('mouseenter', function(){var string = $("#jqg2").val() + "a"; $("#jqg2").val(string); });	       
	      // $("#fbox_grid_id_search").live('mouseenter', function(){var string = $("#jqg2").val(); alert("the value of jqg2 = " + string); });
	       //$("#fbox_grid_id_search").live('mouseenter', function(){$("#jqg2").keydown(); });

//$("#gs_submitter").live('keyup', function(){getAuthNames();});





/*  

	       ///http://trirand.com/blog/jqgrid/jqgrid.html version 3.7 toolbar search 
	       //this first line (the immediate next line) is added by rob to populate the autocomplete on submitter 
	       var submitterAutocompleteList = new Array("Esther Berko", "John Greally", "Robert Dubin");
	       var getUniqueNames = function(columnName) {
	    	   var texts = jQuery("#toolbar").jqGrid('getCol',columnName), uniqueTexts = [],
	    	   textsLength = texts.length, text, textsMap = {}, i;
	    	   for (i=0;i<textsLength;i++) {
	    	   text = texts[i];
	    	   if (text !== undefined && textsMap[text] === undefined) {
	    	   // to test whether the texts is unique we place it in the map.
	    	   textsMap[text] = true;
	    	   uniqueTexts.push(text);
	    	   }
	    	   }
	    	   return uniqueTexts;
	    	   };
    	 jQuery("#toolbar").jqGrid({
    		   	url:'/wasp/job/listJSON.do?selId=${param.selId}&userId=${param.userId}&labId=${param.labId}', 
    			datatype: "json",
    			height: 255,
    			width: 600,
    		   	colNames:['JobID','Job Name', 'submitter','PI','Submitter On','Result'],
    		   	colModel:[
    		   		{name:'jobId',index:'jobId', width:65, sorttype:'int'},
    		   		{name:'jobname',index:'jobname', width:100},
    		   		{name:'submitter',index:'submitter', width:100},
    		   		{name:'pi',index:'pi', width:100},
    		   		{name:'createts',index:'createts', width:100},
    		   		{name:'viewfiles',index:'viewfiles', width:100}
    		   	],
    		   	rowNum:20,
    			rowTotal: 2000,
    			rowList : [20,30,50],
    			loadonce:false,
    		   	mtype: "GET",
    			rownumbers: true,
    			rownumWidth: 40,
    			gridview: true,
    		   	pager: '#ptoolbar',
    		   	sortname: 'jobId',
    		    viewrecords: true,
    		    sortorder: "asc",
    			caption: "Toolbar Searching"	
    		});

    		jQuery("#toolbar").jqGrid('navGrid','#ptoolbar',{del:false,add:false,edit:false,search:false});
    		//jQuery("#toolbar").jqGrid('filterToolbar',{stringResult: true,searchOnEnter : false, defaultSearch:"cn"}); 
    	     jQuery("#toolbar").jqGrid('filterToolbar',{stringResult: true,searchOnEnter : true, defaultSearch:"cn"});
    		
    		//taken from http://www.ok-soft-gmbh.com/jqGrid/FillToolbarSearchFilter.htm 
    		jQuery("#toolbar").jqGrid('setColProp', 'submitter',
				{
					searchoptions: {
					sopt:['cn'],
						dataInit: function(elem) {
							$(elem).autocomplete({
							source:getUniqueNames('submitter'), 
							//source:submitterAutocompleteList,//defined about 20 lines above 
							//source:"Robert Dubin",
							delay:0,
							minLength:0
						});
					}
				}
			}); 
    		
    		
    		
    	 //jQuery("#grid_id").jqGrid('navGrid','#gridpager',{del:false,add:false,edit:false,search:false});
 		//jQuery("#grid_id").jqGrid('filterToolbar',{stringResult: true,searchOnEnter : false});
*/  
 
  
 //http://www.ok-soft-gmbh.com/jqGrid/FillToolbarSearchFilter.htm 
    	 var submitterAutocompleteList = new Array("Esther Berko", "John Greally", "Robert Dubin"), mydata = [
{id:"1", Name:"Miroslav Klose", Category:"sport", Subcategory:"football"},
{id:"2", Name:"Michael Schumacher", Category:"sport", Subcategory:"formula 1"},
{id:"3", Name:"Albert Einstein", Category:"science", Subcategory:"physics"},
{id:"4", Name:"Blaise Pascal", Category:"science", Subcategory:"mathematics"}
],
grid = $("#list"),
getUniqueNames = function(columnName) {
var texts = grid.jqGrid('getCol',columnName), uniqueTexts = [],
textsLength = texts.length, text, textsMap = {}, i;
for (i=0;i<textsLength;i++) {
text = texts[i];
if (text !== undefined && textsMap[text] === undefined) {
// to test whether the texts is unique we place it in the map.
textsMap[text] = true;
uniqueTexts.push(text);
}
}
return uniqueTexts;
},
buildSearchSelect = function(uniqueNames) {
var values=":All";
$.each (uniqueNames, function() {
values += ";" + this + ":" + this;
});
return values;
},
setSearchSelect = function(columnName) {
grid.jqGrid('setColProp', columnName,
{
stype: 'select',
searchoptions: {
value:buildSearchSelect(getUniqueNames(columnName)),
sopt:['eq']
}
}
);
};
grid.jqGrid({
//data: mydata, 
//datatype: 'local', 
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
}).jqGrid('navGrid','#pager',
//{edit:false, add:false, del:false, search:false, refresh:false}); 
{edit:false, add:false, del:false, search:false});
//setSearchSelect('Category');
//setSearchSelect('Subcategory');
grid.jqGrid('setColProp', 'name',
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
grid.jqGrid('setColProp', 'submitter',
{
searchoptions: {
sopt:['cn'],
dataInit: function(elem) {	
/* 
$(elem).autocomplete({
//source:getUniqueNames('Name'), 
source:submitterAutocompleteList,
delay:0,
minLength:0
});
*/ 
	setTimeout(
				function(){ 
			$.getJSON("/wasp/autocomplete/getUserNamesAndLoginForDisplay.do", 
					{ adminNameFragment: "" }, 
					function(data) { 
						//jQuery(elm).autocomplete("option", "appendTo", "#result"); 
						//jQuery(elm).autocomplete("option", "source", data); 
						jQuery(elem).autocomplete(data);
					} );
				}, 200
	);
}
}
});
grid.jqGrid('setColProp', 'pi',
{
		searchoptions: {
		sopt:['cn'],
		dataInit: function(elem) {	
		/* 
		$(elem).autocomplete({
		//source:getUniqueNames('Name'), 
		source:submitterAutocompleteList,
		delay:0,
		minLength:0
		});
		*/ 
			setTimeout(
						function(){ 
					$.getJSON("/wasp/autocomplete/getPiNamesAndLoginForDisplay.do", 
							{ piNameFragment: "" }, 
							function(data) { 
								//jQuery(elm).autocomplete("option", "appendTo", "#result"); 
								//jQuery(elm).autocomplete("option", "source", data); 
								jQuery(elem).autocomplete(data);
							} );
						}, 200
			);
		}
		}
});
/* this works, but I cannot make the SQL work
grid.jqGrid('setColProp', 'createts',
{
			searchoptions: {
				sopt:['eq'],
				dataInit: function(elem) {	
					jQuery(elem).datepicker();
				}
			}
});
*/
grid.jqGrid('filterToolbar',
{stringResult:false, searchOnEnter:true, defaultSearch:"cn"}); 
//if stringResult:true then jsp sends a requestParameter names filters that contains the search as JSON 
//else if false, you can capture the requestParameter in the controller based on the column's name/id   
 
 
     });
     
/*       
     function getAuthNames(){        
    	 if( $("#gs_submitter").val().length == 1){
	        	$.getJSON("/wasp/autocomplete/getUserNamesAndLoginForDisplay.do", { adminNameFragment: $("#gs_submitter").val() }, function(data) { $("input#gs_submitter").autocomplete(data);} );
  		}
     }
      

	function getAuthNames(){        
    	 if( $("#jobName").val().length == 1){
	        	$.getJSON("/wasp/autocomplete/getUserNamesAndLoginForDisplay.do", { adminNameFragment: $("#jobName").val() }, function(data) { $("input#jobName").autocomplete(data);} );
  		}
    	 else if( $("#submitter").val().length == 1){
	        	$.getJSON("/wasp/autocomplete/getUserNamesAndLoginForDisplay.do", { adminNameFragment: $("#submitter").val() }, function(data) { $("input#submitter").autocomplete(data);} );
     	}
     	else if( $("#pi").val().length == 1){
        	$.getJSON("/wasp/autocomplete/getUserNamesAndLoginForDisplay.do", { adminNameFragment: $("#pi").val() }, function(data) { $("input#pi").autocomplete(data);} );
 		}
     	else if( $("#jqg2").val().length == 1){
        	$.getJSON("/wasp/autocomplete/getUserNamesAndLoginForDisplay.do", { adminNameFragment: $("#jqg2").val() }, function(data) { $("input#jqg2").autocomplete(data);} );
 		}     	
	 }
     function getAuthNames2(num){        
    	 //alert("num = " + num);
    	 if( $("#jqg"+num).val().length == 1){
    		 //alert("rob third and last alert"); 
 $.getJSON("/wasp/autocomplete/getUserNamesAndLoginForDisplay.do", { adminNameFragment: $("#jqg"+num).val() }, function(data) { $("input#jqg"+num).autocomplete(data);} );
    		 //setTimeout(
			//				function(){
			//					$.getJSON("/wasp/autocomplete/getUserNamesAndLoginForDisplay.do", { adminNameFragment: $("#jqg"+num).val() }, function(data) { $("input#jqg"+num).autocomplete(data);} );
			//},200); 
  		}    
     }
  */   
 </script>
 
 
 

<!-- 
<script type="text/javascript">
$(function(){ 
  $("#list").jqGrid({
    url:'/wasp/job/listJSON.do?selId=${param.selId}&userId=${param.userId}&labId=${param.labId}',
    datatype: 'JSON',
    mtype: 'GET',
    colNames:['JobID','Job Name', 'Submitter','PI','Submitter On','Result'],
    colModel :[ 
      {name:'jobId', index:'jobid', width:55}, 
      {name:'name', index:'name', width:90}, 
      {name:'submitter', index:'submitter', width:80, align:'right'}, 
      {name:'pi', index:'pi', width:80, align:'right'}, 
      {name:'createts', index:'createts', width:80, align:'right'}, 
      {name:'viewfiles', index:'viewfiles', width:150, sortable:false} 
    ],
    
    pager: '#pager', // pager: jQuery('#pager'), //similar to one below, but not needed for search, I think
    rowNum:10,
    rowList:[10,20,30],
    sortname: 'invid',
    sortorder: 'desc',
    viewrecords: true,
    gridview: true,
    caption: 'My first grid'
    	//});
  }).navGrid('#pager'); //this line was replaced with the line above, and it added all sorts of navigation capabilities, includeing search
}); 

</script>

<script type="text/javascript">
jQuery(document).ready(function(){ 
  jQuery("#editgrid").jqGrid({
      url:'/wasp/job/listJSON.do?selId=${param.selId}&userId=${param.userId}&labId=${param.labId}',
      datatype: "JSON",
      mtype: 'GET',
      colNames:['JobID','Job Name', 'Submitter2','PI','Submitter On','Result'],
      colModel:[
//{name:'jobid', index:'jobid', width:55}, 
//{name:'jobname', index:'jobname', width:90}, 
//{name:'submitter', index:'submitter', width:80, align:'right', search:true}, 
//{name:'pi', index:'pi', width:80, align:'right'}, 
//{name:'submittedon', index:'submittedon', width:80, align:'right'}, 
//{name:'result', index:'result', width:150, sortable:false} 

                {name:'jobid',index:'jobid', width:55, sortable:false, editable:false, editoptions:{readonly:true,size:10}},
                {name:'jobname',index:'jobname', width:200,search:true, stype:'text', editable:true, searchoptions:{dataInit:datePick2, attr:{title:'Select Date'}} },
                //{name:'submitter',index:'submitter', width:200,editable:true},
                {name:'submitter2',index:'submitter2', width:200,
                	searchoptions:{sopt:['eq','ne','lt','le','gt','ge'],

                		dataInit:function(elem){
                			
                
                		// Autocomplete plugin
                		$(elem).autocomplete(
                		["asdsdrendhar", "asssdfh", "Tony Tsdfasdfasdfasdf", "Alicia Tmm", "Vicky", "AASD",
                		"Dfgg", "erwe", "ghjgh", "sdfsdf", "asdfggg", "sdipyityt", "tasdfhaslfj"],
                		{
                		width: 320,
                		max: 4,
                		highlight: false,
                		multiple: true,
                		multipleSeparator: " ",
                		scroll: true,
                		scrollHeight: 300


                		});
                		}
                		}	
                
                },
                
                
                {name:'pi',index:'pi', width:100,editable:true},
                {name:'submittedon',index:'submittedon', width:300,editable:true},
               // {name:'result',index:'result', width:200,editable:false,edittype:'select',editoptions:{dataUrl: '/wasp/autocomplete/getUserNamesAndLoginForDisplay.do'}},
                {name:'result',index:'result', width:200, search:true, stype:'text', editable:true,
            	   searchoptions:{sopt:['eq','ne','lt','le','gt','ge'],
                	
                	  dataInit: function(elm){
                		  setTimeout(
  								function(){ 
								$.getJSON("/wasp/autocomplete/getUserNamesAndLoginForDisplay.do", 
										{ adminNameFragment: "" }, 
										function(data) { 
											//jQuery(elm).autocomplete("option", "appendTo", "#result");
											//jQuery(elm).autocomplete("option", "source", data);
											jQuery(elm).autocomplete(data);
										} );
  								}, 200);
					  }
                	}
                },
           ],
    /// jsonReader : {
    // /     repeatitems:false
    // },
      rowNum:10,
      rowList:[10,20,30],
      pager: jQuery('#editgridpager'),
      sortname: 'name',
      viewrecords: true,
      sortorder: "asc",
      caption:"Wines",
      editurl:"/wasp/job/listJSON.do?selId=${param.selId}&userId=${param.userId}&labId=${param.labId}"
 }).navGrid('#editgridpager');
});
datePick = function(elem)
{
   jQuery(elem).datepicker();
};
datePick2 = function(elem)
{
	$.getJSON("/wasp/autocomplete/getUserNamesAndLoginForDisplay.do", 
			{ adminNameFragment: "" }, 
			function(data) { 
				jQuery(elem).autocomplete(data);
			} );
};
</script>
 -->
<!--  
<br />
<center> 
<div>		
  		<h1><fmt:message key="department.detail_administrators.label" /></h1>
		<div class="instructions">Search</div>
		<table class="EditTable ui-widget ui-widget-content"> 
			<tr class="FormData">
				<form name="f0" action="<c:url value='/job/list.do'/>" method="GET">
				<td class="CaptionTD">JobID:</td>				
				<td class="DataTD"><input class="FormElement ui-widget-content ui-corner-all" id="jobId" name='jobId' value='' /></td>
				<td>				
	 				<div class="submit">	
						<input class="FormElement ui-widget-content ui-corner-all" type="submit" value="Go" /> 
					</div>
				</td>
				</form>	
				<td>|</td>
				<form name="f1" action="<c:url value='/job/list.do'/>" method="GET">
				<td class="CaptionTD">Job Name:</td>				
				<td class="DataTD"><input class="FormElement ui-widget-content ui-corner-all" id="jobName" name='jobName' value='' /></td>
				<td>				
	 				<div class="submit">	
						<input class="FormElement ui-widget-content ui-corner-all" type="submit" value="Go" /> 
					</div>
				</td>
				</form>	
				<td>|</td>
				<form name="f2" action="<c:url value='/job/list.do'/>" method="GET">
				<td class="CaptionTD">Submitter:</td>				
				<td class="DataTD"><input class="FormElement ui-widget-content ui-corner-all" id="submitter" name='submitter' value='' /></td>
				<td>				
	 				<div class="submit">	
						<input class="FormElement ui-widget-content ui-corner-all" type="submit" value="Go" /> 
					</div>
				</td>
				<td>|</td>
				</form>
				<form name="f3" action="<c:url value='/job/list.do'/>" method="GET">
				<td class="CaptionTD">PI:</td>				
				<td class="DataTD"><input class="FormElement ui-widget-content ui-corner-all" id="pi" name='pi' value='' /></td>
				<td>				
	 				<div class="submit">	
						<input class="FormElement ui-widget-content ui-corner-all" type="submit" value="Go" /> 
					</div>
				</td>
				</form>	
			</tr>
		</table>	
</div>
</center>
</br >
-->



<!-- search toolbar -->
<center>
<table id="list"><tr><td/></tr></table>
<div id="pager"></div>
</center> 
<!--
  <table id="toolbar"></table>
<div id="ptoolbar" ></div>
-->
<!--   
<table id="grid_id"></table> 
<div id="gridpager"></div>
-->
 <!--   
   
    <br/>
    <br />
    -------------------------------
    <br />
    <br />
 <table id="list"></table> 
<div id="pager"></div>

 <br/>
    <br />
    -------------------------------
    
    <br />
    <br />
 <table id="editgrid"></table> 
<div id="editgridpager"></div>
-->    