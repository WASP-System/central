<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
<c:if test="${displayTheAnchor=='YES'}"><a href='<c:url value="/job/list.do"/>'>View All Jobs</a></c:if>

<script type="text/javascript" src="/wasp/scripts/jquery/jquery-ui-1.8.18.custom.min.js"></script> 


 <script type="text/javascript">
     $(document).ready(function() { 
    	   $("#jobName").keyup(function(){getAuthNames();});
	       $("#submitter").keyup(function(){getAuthNames();});
	       $("#pi").keyup(function(){getAuthNames();});
	       //$("#jqg2").keyup(function(){getAuthNames();});
	       //$("#jqg2").live('click', function(){alert("this is the alert");});
	       //$("#jqg2").live('keyup', function(){getAuthNames();});
	       //$('[id^="jqg"]').live('click', function(){alert("this is the alert");});
	       //$('input[id^="jqg"]').live('click', function(){alert("this is the alert");});
	       //$('input[id^="jqg"]').live('keyup', function(){alert("this is the alert");});
	       $('input[id^="jqg"]').live('keyup', function(){ var num = parseInt(this.id.replace("jqg", "")); getAuthNames2(num);});
	       //$("#searchhdfbox_grid_id a").live('click', function(){alert("this is the alert for the close x");});
	       //$("#fbox_grid_id_search").live('mouseenter', function(){var string = $("#jqg2").val() + "a"; $("#jqg2").val(string); });
	       
	       
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
  		}    
     }
     
 </script>

<br />
<center> 
<div>		
<!--  		<h1><fmt:message key="department.detail_administrators.label" /></h1>
		<div class="instructions">Search</div>
-->		
			
			
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


<table id="grid_id"></table> 
<div id="gridpager"></div>

    