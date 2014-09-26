<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
<script type="text/javascript">

var savedAnalysisSelectedChoice = "";

function handleStrategyUpdate() {
	  if($("#strategy").val()=='-1'){
		  $("#workflowRowId").css("display", "none"); 
		  $("#continueButtonDivId").css("display", "none"); 
		  $("#analysisSelectedId").css("display", "none");
	  }
	  else{			  
		  $.getJSON("<wasp:relativeUrl value="jobsubmit/getWorkflowsForAStrategy.do" />", { strategy: $("#strategy").val() }, function( data ) {
				 var numberOfEntries = 0;
				 //unable to find a way to get this number directly. $.parseJSON(data) seems to screw up the data 
				 $.each( data, function( key, val ) {
					 numberOfEntries++ ;
				  });
				 
				  $("#workflowId").empty();
				  
				  if(numberOfEntries == 0){
					  	$("#workflowId").append("<option value='0'><fmt:message key="jobsubmitCreate.noWorkflowsFound.label" /></option>"); 
					  	savedAnalysisSelectedChoice = $('#isAnalysisSelected').val();
					  	$('#isAnalysisSelected').val('false');
					  	$("#analysisSelectedId").css("display", "none");
					  	
				  }
				  else if(numberOfEntries > 1){
				  	$("#workflowId").append("<option value='-1'><fmt:message key="wasp.default_select.label"/></option>"); 
				  	if (savedAnalysisSelectedChoice != ""){
				  		$('#isAnalysisSelected').val(savedAnalysisSelectedChoice);
				  		savedAnalysisSelectedChoice = "";
				  	}
				  	$("#analysisSelectedId").css("display", "table-row");
				  }
				  else {
					  if (savedAnalysisSelectedChoice != ""){
					  		$('#isAnalysisSelected').val(savedAnalysisSelectedChoice);
					  		savedAnalysisSelectedChoice = "";
					  }
					  $("#analysisSelectedId").css("display", "table-row");
				  }
				  
				  $.each( data, function( key, val ) {
					  $("#workflowId").append("<option value='"+key+"'>"+val+"</option>");						 
				  });	
				  
			}); //end of getJSON method 
		  	$("#workflowRowId").css("display", "table-row");			  
		  	if($("#strategy").val()!='-1'){
		  		$("#continueButtonDivId").css("display", "inline");
		  	}
	  } 		  
}

$(document).ready(function() {
	
	if ($("#strategy").val() != '-1'){
		handleStrategyUpdate();
	}
	
	$( "#strategy" ).change(function(){
		handleStrategyUpdate();
	});

	$( "#viewDefinitionsAnchor" ).click(function() {
		if($(this).text()=="<fmt:message key="jobsubmitCreate.viewHelp.label" />"){
			$("#strategySummary").css("display", "inline");
			$(this).text("<fmt:message key="jobsubmitCreate.hideHelp.label" />");
		}
		else{
			$("#strategySummary").css("display", "none");
			$(this).text("<fmt:message key="jobsubmitCreate.viewHelp.label" />");
		}
	});
});
</script>
