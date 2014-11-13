<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<script type="text/javascript">

//var savedAnalysisSelectedChoice = "";

function addNewGrant(){
		
	$("#newGrantCodeError").html("");
	$("#newGrantExpError").html(""); 
	$.getJSON("<wasp:relativeUrl value="jobsubmit/addGrant.do" />", { 
			newGrantCode: $("#newGrantCode").val(),
			newGrantName: $("#newGrantName").val(),
			newGrantExp: $("#newGrantExp").val(),
			selectedLabId: $("#labId").val()
		}, function( data ) {
		 //unable to find a way to get this number directly. $.parseJSON(data) seems to screw up the data 
		 $.each( data, function( key, val ) {
			  if (key == "errors" && val == "false"){
				  closeGrantAddTable(); 
				  populateGrants();
				  return;
			  }
			  if (key == "newGrantCodeError")
				  $("#newGrantCodeError").html(val);
			  else if (key == "newGrantExpError")
				  $("#newGrantExpError").html(val); 
		  });
		 
		  
		  
	}); //end of getJSON method 
}

function populateGrants(){
	  if($("#labId").val()=='-1'){
		hideChooseGrantRow();
		closeGrantAddTable(); 
		hideContinueButton(); 
		$( "#viewAddGrantAnchor" ).text("<fmt:message key="jobsubmitCreate.viewAddGrant.label" />");
	  }
	  else{
		$.getJSON("<wasp:relativeUrl value="bioanalyzer/getGrantsForLab.do" />", { selectedLabId: $("#labId").val() }, function( data ) {
			fillGrantList(data);
			showChooseGrantRow();
			showContinueButton();
		}); //end of getJSON method 
	  }
}

function fillGrantList(data){
	var numberOfEntries = 0;
	
	 $.each( data, function( key, val ) {
		 numberOfEntries++ ;
	  });
	  var currentSelected = $("#selectGrantId").val();
	  $("#selectGrantId").empty();
	  
	  if(numberOfEntries == 0){
		  	$("#selectGrantId").append("<option value='0'><fmt:message key="jobsubmitCreate.noGrantsFound.label" /></option>"); 
	  }
	  else {
		  if(numberOfEntries > 1){
		  	$("#selectGrantId").append("<option value='-1'><fmt:message key="wasp.default_select.label"/></option>"); 
		  }
	  }
	 
	  $.each( data, function( key, val ) {
		  $("#selectGrantId").append("<option value='"+key+"'>"+val+"</option>");						 
	  });	
	  $("#selectGrantId").val(currentSelected);
}



function showChooseGrantRow(){
	$("#grantSelectRowId").css("display", "table-row");
}

function hideChooseGrantRow(){
	$("#grantSelectRowId").css("display", "none");
}

function showContinueButton(){
	$("#continueButtonDivId").css("display", "inline");
}

function hideContinueButton(){
	$("#continueButtonDivId").css("display", "none");
}

function closeGrantAddTable(){
	$("#addGrantTable").css("display", "none"); 
	$("#viewAddGrantAnchor").text("<fmt:message key="jobsubmitCreate.viewAddGrant.label" />");
	$("#newGrantCode").val("");
	$("#newGrantName").val("");
	$("#newGrantExp").val("");
	$("#newGrantCodeError").html("");
	$("#newGrantExpError").html("");
}

function openGrantAddTable(){
	$("#addGrantTable").css("display", "table");
	$("#viewAddGrantAnchor").text("<fmt:message key="jobsubmitCreate.hideAddGrant.label" />");
}

$(document).ready(function() {
	
	//needed for the addrow and delete row functionality:
	$(".addRow").btnAddRow();
	$(".delRow").btnDelRow();		
		
	if ($("#labId").val() != '-1'){
		populateGrants();
	}
	
	$( "#labId" ).change(function(){
		populateGrants();
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
	
	$( "#viewAddGrantAnchor" ).click(function() {
		if($(this).text()=="<fmt:message key="jobsubmitCreate.viewAddGrant.label" />"){
			$("#addGrantTable").css("display", "table");
			$(this).text("<fmt:message key="jobsubmitCreate.hideAddGrant.label" />");
		}
		else{
			closeGrantAddTable()
		}
	});
	
	 $( "#newGrantExp" ).datepicker({ dateFormat: "yy-mm-dd" });
	 
	 	 
});
</script>
