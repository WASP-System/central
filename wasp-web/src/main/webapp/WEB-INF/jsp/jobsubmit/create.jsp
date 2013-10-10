<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<script type="text/javascript">

$(document).ready(function() {
	$( "#strategy" ).change(function() {
		  if($( this ).val()=='-1'){
			  $("#workflowRowId").css("display", "none"); 
			  $("#continueButtonDivId").css("display", "none"); 
		  }
		  else{			  
			  $.getJSON("<c:url value="/jobsubmit/getWorkflowsForAStrategy.do" />", { strategy: $( this ).val() }, function( data ) {
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
						  $("#workflowId").append("<option value='"+key+"'>"+val+"</option>");						 
					  });				  
				});//end of getJSON method 
			  
				$("#workflowRowId").css("display", "table-row");			  
			  	if($( this ).val()!='-1'){
			  		$("#continueButtonDivId").css("display", "inline");
			  	}
		  } 		  
	});

	/*
	$("html, body").animate({ scrollTop: 0 }, "fast");

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
  
  <c:if test="${not empty strategies}">
  <tr class="FormData">
    <td class="CaptionTD">Library Strategy:</td>
    <td class="DataTD">
      <select class="FormElement ui-widget-content ui-corner-all" id="strategy" name="strategy">
        <option value='-1'><fmt:message key="wasp.default_select.label"/></option>
        <c:forEach var="strategy" items="${strategies}">
          <option value="${strategy.getType()}.${strategy.getStrategy()}"  <c:if test="${strategy.getId() == thisJobDraftsStrategy.getId()}"> SELECTED</c:if>  ><c:out value="${strategy.getDisplayStrategy()}"/></option>
        </c:forEach>
      </select>
    </td>
    <td class="CaptionTD error"> <c:out value="${strategyError}"/> <%--this is not a real part of jobDraft object, so it's toxic here <form:errors path="strategy" />--%></td>
  </tr>
  </c:if>
  
  <c:choose>
  <c:when test="${empty assayWorkflows}">
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
  </c:when>
  <c:otherwise>
    <tr id="workflowRowId" class="FormData" >
    <td class="CaptionTD"><fmt:message key="jobDraft.workflowId.label"/>:</td>
    <td class="DataTD">
    
          <select class="FormElement ui-widget-content ui-corner-all" id="workflowId" name="workflowId">
        	<c:if test="${fn:length(assayWorkflows)>1}">
        		<option value='-1'><fmt:message key="wasp.default_select.label"/></option>
        	</c:if>
        	<c:forEach var="workflow" items="${assayWorkflows}">
        		<option value="${workflow.key}"  <c:if test="${workflow.key == jobDraft.workflowId}"> SELECTED</c:if>   >${workflow.value}</option>
        	</c:forEach>
        	
        	
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
  </c:otherwise>
  </c:choose>
  
  
  </table>

  <div class="submit">
    <c:if test="${jobDraft != null && jobDraft.jobDraftId != null }">
      <input class="fm-button" type="button" value="<fmt:message key="jobDraft.finishLater.label" />" onClick="window.location='<c:url value="/dashboard.do"/>'" /> 
    </c:if>
    <div id="continueButtonDivId"   <c:choose><c:when test="${empty assayWorkflows}">style="display:none"</c:when><c:otherwise>style="display:inline"</c:otherwise></c:choose> >
     <input  class="FormElement ui-widget-content ui-corner-all" type="submit" value="<fmt:message key="jobDraft.continue.label"/>">
	</div>
  </div>

  </form:form>



