<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
<c:if test="${displayTheAnchor=='YES'}"><a href='<c:url value="/job/list.do"/>'>View All Jobs</a></c:if>

  
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

$('input[id^="jqg"]').live('keyup', function(){ var num = parseInt(this.id.replace("jqg", "")); getAuthNames2(num);});
	   	       
	       //$("#searchhdfbox_grid_id a").live('click', function(){alert("this is the alert for the close x");});
	       //$("#fbox_grid_id_search").live('mouseenter', function(){var string = $("#jqg2").val() + "a"; $("#jqg2").val(string); });
	      
	       
	      // $("#fbox_grid_id_search").live('mouseenter', function(){var string = $("#jqg2").val(); alert("the value of jqg2 = " + string); });
	       //$("#fbox_grid_id_search").live('mouseenter', function(){$("#jqg2").keydown(); });
	       
	       
     });
      
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
  
<table id="grid_id"></table> 
<div id="gridpager"></div>

    
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