<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<script type="text/javascript">

$(document).ready(function() {
	$( "#strategy" ).change(function() {
		  //alert( "Handler for .change() called." );
		  
		  if($( this ).val()=='-1'){
			  $("#workflowRowId").css("display", "none"); 
			  $("#continueButtonDivId").css("display", "none"); 
		  }
		  else{
			  
			  $.getJSON("<c:url value="/jobsubmit/getWorkflowsForAStrategy.do" />", { strategy: $( this ).val() }, function( data ) {
				  //var items = [];
				 // var response = $.parseJSON(data);
				 // alert(response.rows.length);
					
				 var numberOfEntries = 0;
				 //unable to find a way to get this number directly. $.parseJSON(data) seems to screw up the data 
				 $.each( data, function( key, val ) {
					 numberOfEntries++ ;
				  });
				 
				  $("#workflowId").empty();
				  
				  if(numberOfEntries == 0){
					  	$("#workflowId").append("<option value='-1'>Unable to access workflows</option>"); 
				  }
				  else if(numberOfEntries > 1){
				  	$("#workflowId").append("<option value='-1'><fmt:message key="wasp.default_select.label"/></option>"); 
				  }
				  
				  $.each( data, function( key, val ) {
				  //  items.push( "<li id='" + key + "'>" + val + "</li>" );
				  //alert("key: " + key + "; value: " + val );
					  //$("#workflowId").append("<option value='test'>this is the sssssSECOND test</option>");
					  $("#workflowId").append("<option value='"+key+"'>"+val+"</option>");  
						 
				  });
				 
				 // $( "<ul/>", {
				 //   "class": "my-new-list",
				  //  html: items.join( "" )
				 // }).appendTo( "body" );
				  //$("#workflowId").empty().append("<option value='-1'><fmt:message key="wasp.default_select.label"/></option>"); 
				  
				  //////////$("#workflowId").append("<option value='test'>this is the sssssSECOND test</option>"); 
				 
				  //$("#workflowRowId").css("display", "table-row"); 
				  //if($( this ).val()!='-1' && numberOfEntries == 1){
				  //	$("#continueButtonDivId").css("display", "inline");
				 // } 
				  
				  
				});
			  
			  
			  
			  
			  //$("#workflowId").empty().append("<option value='-1'><fmt:message key="wasp.default_select.label"/></option>"); 
			 // $("#workflowId").append("<option value='test'>this is the sSECOND test</option>"); 
			  $("#workflowRowId").css("display", "table-row"); 
			  
			  if($( this ).val()!='-1'){
			  	$("#continueButtonDivId").css("display", "inline");
			  }
		  } 
		  
		  
		  
//		  $.getJSON("<c:url value="/jobsubmit/getWorkflowsForAStrategy.do" />", function( data ) {
			  //var items = [];
			 // $.each( data, function( key, val ) {
			  //  items.push( "<li id='" + key + "'>" + val + "</li>" );
			  //});
			 
			 // $( "<ul/>", {
			 //   "class": "my-new-list",
			  //  html: items.join( "" )
			 // }).appendTo( "body" );
//			});
		  
		  
		  
	});

	/*
	$("html, body").animate({ scrollTop: 0 }, "fast");

	$(function() {
		    $( "#tabs" ).tabs();
	}); 
	
	//http://api.jqueryui.com/dialog/
	$("#modalDialog").dialog({
        autoOpen: false,
        modal: true,
        height: 800,
        width: 800,
        position: { my: "right top", at: "right top", of: window } //http://docs.jquery.com/UI/API/1.8/Position
    });
	$("#modalessDialog").dialog({
        autoOpen: false,
        modal: false,
        height: 800,
        width: 650,
        position: { my: "right top", at: "right top", of: window } 
    }); 
	
	$("#smallModalessDialog").dialog({
        autoOpen: false,
        modal: false,
        height: 500,
        width: 500,
        position: { my: "right top", at: "right top", of: window } 
    });
	*/
});
</script>


<h1><fmt:message key="jobDraft.create.label" /></h1>

<div class="instructions">
<fmt:message key="jobDraft.create_instructions.label"/>
</div>

<form:form  cssClass="FormGrid" commandName="jobDraft">
<table class="EditTable ui-widget ui-widget-content">
  <tr class="FormData">
    <td class="CaptionTD"><fmt:message key="jobDraft.name.label"/>:</td>
    <td class="DataTD">
      <input class="FormElement ui-widget-content ui-corner-all" name="name" value="<c:out value="${jobDraft.name}"/>">
    </td>
    <td class="CaptionTD error"><form:errors path="name" /></td>
  </tr>
  <tr class="FormData">
    <td class="CaptionTD"><fmt:message key="jobDraft.labId.label"/>:</td>
    <td class="DataTD">
      <select class="FormElement ui-widget-content ui-corner-all" name="labId">
        <option value='-1'><fmt:message key="wasp.default_select.label"/></option>
        <c:forEach var="lab" items="${labs}">
          <option value="${lab.labId}" <c:if test="${lab.labId == jobDraft.labId}"> selected</c:if>><c:out value="${lab.getUser().getNameFstLst()}"/> Lab</option>
        </c:forEach>
      </select>
    </td>
    <td class="CaptionTD error"><form:errors path="labId" /></td>
  </tr>
  
  
  <tr class="FormData">
    <td class="CaptionTD">Library Strategy:</td>
    <td class="DataTD">
      <select class="FormElement ui-widget-content ui-corner-all" id="strategy" name="strategy">
        <option value='-1'><fmt:message key="wasp.default_select.label"/></option>
        <c:forEach var="strategy" items="${libraryStrategies}">
          <option value="${strategy.getType()}.${strategy.getStrategy()}"><c:out value="${strategy.getDisplayStrategy()}"/></option>
        </c:forEach>
      </select>
    </td>
    <td class="CaptionTD error"><%-- <form:errors path="strategy" />--%></td>
  </tr>
  
  
  <tr id="workflowRowId" class="FormData" style="display:none">
    <td class="CaptionTD"><fmt:message key="jobDraft.workflowId.label"/>:</td>
    <td class="DataTD">
    
          <select class="FormElement ui-widget-content ui-corner-all" id="workflowId" name="workflowId">
        	<%-- 
        	<option value='-1'><fmt:message key="wasp.default_select.label"/></option>
				<div id="workflowOptionsDivId">
				<option value='test'>this is a test</option>
    			</div>
    		--%>
    		</select>
    <%-- 
      <c:forEach var="workflow" items="${assayWorkflows}">
        <div class="radioelement">
          <input class="FormElement ui-widget-content ui-corner-all" type="radio" name="workflowId" value="${workflow.key}" <c:if test="${workflow.key == jobDraft.workflowId}"> checked</c:if> >
          <span><c:out value="${workflow.value}" /></span>
        </div>
      </c:forEach>
    --%>
    </td>
    <td class="CaptionTD error"><form:errors path="workflowId" /></td>
  </tr>
  
  
  </table>

  <div class="submit">
    <c:if test="${jobDraft != null && jobDraft.jobDraftId != null }">
      <input class="fm-button" type="button" value="<fmt:message key="jobDraft.finishLater.label" />" onClick="window.location='<c:url value="/dashboard.do"/>'" /> 
    </c:if>
    <div id="continueButtonDivId" style="display:none">
     <input  class="FormElement ui-widget-content ui-corner-all" type="submit" value="<fmt:message key="jobDraft.continue.label"/>">
	</div>
  </div>

  </form:form>



