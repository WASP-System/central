<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
<%--  TODO: Declare style in css file (e.g. /src/main/webapp/css/base.css), not in .jsp and reuse where possible !!!! --%>

<h1><fmt:message key="jobDraft.create.label" /></h1>

<div class="instructions">
<fmt:message key="jobDraft.create_instructions.label"/>
</div>

<div style="float:left"> 
	<form:form  cssClass="FormGrid" commandName="jobDraft">
	
	<table class="EditTable ui-widget ui-widget-content">
	
	  <tr class="FormData">
	    <td class="CaptionTD"><fmt:message key="jobDraft.name.label"/>:</td>
	    <td class="DataTD" colspan="1">
	      <input class="FormElement ui-widget-content ui-corner-all" name="name" value="<c:out value="${jobDraft.name}"/>">
	      <span class="requiredField">*</span>
	    </td>
	    <td class="CaptionTD error"><form:errors path="name" /></td>
	  </tr>

	  
	  <c:if test="${not empty strategies}">
		  <tr class="FormData">
		    <td class="CaptionTD"><fmt:message key="jobsubmitCreate.libraryStrategy.label" />:</td>
		    <td class="DataTD" >
		      <select class="FormElement ui-widget-content ui-corner-all" id="strategy" name="strategy">
		        <option value='-1'><fmt:message key="wasp.default_select.label"/></option>
		        <c:forEach var="strategy" items="${strategies}">
		          <option value="${strategy.getType()}.${strategy.getStrategy()}"  <c:if test="${strategy.getId() == thisJobDraftsStrategy.getId()}"> SELECTED</c:if>  ><c:out value="${strategy.getDisplayStrategy()}"/></option>
		        </c:forEach>
		      </select>
		      <span class="requiredField">*</span>
		      <wasp:tooltip value="${libraryStrategyTooltip}" /> <a id="viewDefinitionsAnchor" href="javascript:void(0);"><fmt:message key="jobsubmitCreate.viewHelp.label" /></a>
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
	        			<%-- options will be gotten via ajax call --%>
	    			</select>
	    			<span class="requiredField">*</span>
	 				<wasp:tooltip value="${assayWorkflowTooltip}" />
	    		</td>
	    		<td class="CaptionTD error"><form:errors path="workflowId" /></td>
	  		</tr>
	  	</c:when>
	  	<c:otherwise>
	  	    <%-- this actually never appears to be called, due to a javascript call in document.ready --%>
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
			    	 </select>
			    	 <span class="requiredField">*</span>
			    	 <wasp:tooltip value="${assayWorkflowTooltip}" />
			    </td>
			    <td class="CaptionTD error"><form:errors path="workflowId" /></td>
		  </tr>
	  	</c:otherwise>
	  </c:choose>  
	  <tr id="analysisSelectedRowId" class="FormData" style="display:none">
		    <td class="CaptionTD"><fmt:message key="jobDraft.analysisSelected.label"/>:</td>
		    <td class="DataTD" >
		          <select class="FormElement ui-widget-content ui-corner-all" id="isAnalysisSelected" name="isAnalysisSelected">
		          	<option value="false"  <c:if test="${!empty isAnalysisSelected && isAnalysisSelected == false}"> SELECTED</c:if>   ><fmt:message key="jobDraft.analysisSelectedFalse.label"/></option>
		        	<option value="true"  <c:if test="${empty isAnalysisSelected || isAnalysisSelected == true}"> SELECTED</c:if>   ><fmt:message key="jobDraft.analysisSelectedTrue.label" ><fmt:param value="${perLibraryAnalysisFee}"/></fmt:message></option>
		    	  </select>
		    	  <span class="requiredField">*</span>
		    	 <wasp:tooltip value="${selectAnalysisTooltip}" />
		    </td>
		    <td class="CaptionTD error">&nbsp;</td>
	  </tr>
	  <tr class="FormData">
	    <td class="CaptionTD"><fmt:message key="jobDraft.labId.label"/>:</td>
	    <td class="DataTD" >
	      <select class="FormElement ui-widget-content ui-corner-all" name="labId" id="labId">
	        <option value='-1'><fmt:message key="wasp.default_select.label"/></option>
	        <c:forEach var="lab" items="${labs}">
	          <option value="${lab.labId}" <c:if test="${lab.labId == jobDraft.labId}"> selected</c:if>><c:out value="${lab.getUser().getNameFstLst()}"/> <fmt:message key="jobsubmitCreate.lab.label" /></option>
	        </c:forEach>
	      </select>
	      <span class="requiredField">*</span>
	    </td>
	    <td class="CaptionTD error"><form:errors path="labId" /></td>
	  </tr>
	  <tr class="FormData" id="grantSelectRowId" style="display:none">
		    <td class="CaptionTD"><fmt:message key="jobDraft.selectGrant.label"/>:</td>
		    <td class="DataTD" >
		    	<select class="FormElement ui-widget-content ui-corner-all" id="selectGrantId" name="selectGrantId">
		    		<option value='-1'><fmt:message key="wasp.default_select.label"/></option>
		        	<c:forEach var="grant" items="${grantsAvailable}">
		          		<option value="${grant.getId()}"  <c:if test="${grant.getId() == thisJobDraftsGrant.getId()}"> SELECTED</c:if>  ><c:out value="${grant.getCode()}"/></option>
		        	</c:forEach>
		      	</select>
		      	<span class="requiredField">*</span>
		      	<a id="viewAddGrantAnchor" href="javascript:void(0);"><fmt:message key="jobsubmitCreate.viewAddGrant.label" /></a>
		      	<table class="EditTable ui-widget ui-widget-content " id="addGrantTable" style="display:none;margin-top:5px">
				   <tr>
				  	<th class="label-centered" style="background-color:#FAF2D6" colspan="3"><fmt:message key="jobDraft.grantDetails.label" /></th>
				  </tr>
				  <tr class="FormData" >
					    <td class="CaptionTD"><fmt:message key="jobDraft.grantCode.label" />:</td>
					    <td class="DataTD">
					    	<input type="text" size="15" name="newGrantCode" id="newGrantCode" />
					    	<span class="requiredField">*</span>
					    </td>
					    <td class="CaptionTD error" id="newGrantCodeError">&nbsp;</td>
				  </tr>
				  <tr class="FormData" >
					    <td class="CaptionTD"><fmt:message key="jobDraft.grantName.label" />:</td>
					    <td class="DataTD">
					    	<input type="text" size="15" name="newGrantName" id="newGrantName" />
					    </td>
					    <td class="CaptionTD error" >&nbsp;</td>
				  </tr>
				  <tr class="FormData" >
				    	<td class="CaptionTD"><fmt:message key="jobDraft.dateGrantExp.label" />:</td>
					    <td class="DataTD">
					    	<input type="text" size="15" name="newGrantExp" id="newGrantExp" />
					    	<span class="requiredField">*</span>
					    </td>
					    <td class="CaptionTD error" id="newGrantExpError">&nbsp;</td>
			      </tr>
				  <tr>
					    <td colspan="3">
					    	<input class="fm-button" type="button" value="<fmt:message key="jobDraft.addGrantButton.label" />" onClick="addNewGrant()" /> 
					    	<input class="fm-button" type="button" value="<fmt:message key="jobDraft.closeGrantButton.label" />" onClick="closeGrantAddTable()" /> 
					    </td>
				  </tr>
				  </table>
		    </td>
		    <td class="CaptionTD error"><c:out value="${grantSelectError}"/></td>
	  </tr>
	  </table>
	  
	
	  <div class="submit">
	    <c:if test="${jobDraft != null && jobDraft.jobDraftId != null }">
	      <input class="fm-button" type="button" value="<fmt:message key="jobDraft.terminateDiscard.label" />" onClick="if(confirm('<fmt:message key="jobDraft.terminateDiscardThisJobDraft.label" />')){window.location='<wasp:relativeUrl value="jobsubmit/terminateJobDraft/${jobDraft.jobDraftId}.do"/>'}" /> 
	      <input class="fm-button" type="button" value="<fmt:message key="jobDraft.finishLater.label" />" onClick="window.location='<wasp:relativeUrl value="dashboard.do"/>'" /> 
	    </c:if>
	    <div id="continueButtonDivId"   <c:choose><c:when test="${empty assayWorkflows}">style="display:none"</c:when><c:otherwise>style="display:inline"</c:otherwise></c:choose> >
	     <input  class="FormElement ui-widget-content ui-corner-all" type="submit" value="<fmt:message key="jobDraft.continue.label"/>">
		</div>
	  </div>
	
	</form:form>
</div>

<!--
<div id="strategySummary" style="float:left; margin-left:10px; display:none"> 
  <table class="data" style="margin: 0px 0px">
 	<tr class="FormData">
 		<td class="label-centered" style="background-color:#FAF2D6"><fmt:message key="strategy.commonName.label"/></td>
 		<td  class="label-centered" style="background-color:#FAF2D6"><fmt:message key="strategy.strategy.label"/></td>
 		<td  class="label-centered" style="background-color:#FAF2D6"><fmt:message key="strategy.definition.label" /></td>
 		<td  class="label-centered" style="background-color:#FAF2D6">Workflows</td>
 	</tr>
 	<c:forEach items="${strategies}" var="strategy">
  		<tr>
  			<td style="font-size:x-small"><c:out value="${strategy.getDisplayStrategy()}" /></td>
  			<td style="font-size:x-small"><c:out value="${strategy.getStrategy()}" /></td>
  			<td style="font-size:x-small;width:250px"><c:out value="${strategy.getDescription()}" /></td>
  			<td style="font-size:x-small;width:250px">
  			    <c:set value="${strategyWorkflowListMap.get(strategy)}" var="workflowList"/>
  				<c:forEach items="${workflowList}" var="workflow">
  					<c:out value="${workflow.getName()}" /><br />
  				</c:forEach>
  			</td>
  		</tr>
	</c:forEach>
  </table>
</div>
-->

<div id="strategySummary" style="float:left; margin-left:10px; display:none"> 
  <table class="data" style="margin: 0px 0px">
 	<tr class="FormData">
 		<td class="label-centered" style="background-color:#FAF2D6"><fmt:message key="jobsubmitCreate.libraryStrategy.label" /></td>
 		<td  class="label-centered" style="background-color:#FAF2D6"><fmt:message key="jobsubmitCreate.strategyDefinition.label" /></td>
 		<td  class="label-centered" style="background-color:#FAF2D6"><fmt:message key="jobsubmitCreate.strategyWorkflows.label" /></td>
 	</tr>
 	<c:forEach items="${strategies}" var="strategy">
  		<tr>
  			<c:choose>
  				<c:when test="${not empty strategiesWithWorkflowsOtherThanGeneric.get(strategy)}">
  					<td style="font-size:x-small; color:red; font-weight:bold"><c:out value="${strategy.getDisplayStrategy()}" /></td>
  				</c:when>
  				<c:otherwise>
  					<td style="font-size:x-small"><c:out value="${strategy.getDisplayStrategy()}" /></td>
  				</c:otherwise>
  			</c:choose>
  			<td style="font-size:x-small;width:175px"><u><c:out value="${strategy.getStrategy()}" /></u>: <c:out value="${strategy.getDescription()}" /></td>
  			<td style="font-size:x-small">
  			    <c:set value="${strategyWorkflowListMap.get(strategy)}" var="workflowList"/>
  				<c:forEach items="${workflowList}" var="workflow">
  					<c:choose>
  						<c:when test="${workflow.getName() != 'Generic DNA Seq' }">
  						     <span style="color:red; font-weight:bold"><c:out value="${workflow.getName()}" /><br /></span>
  						</c:when>
  						<c:otherwise>
  							<c:out value="${workflow.getName()}" />
  						</c:otherwise>
  					</c:choose>
  				</c:forEach>
  			</td>
  		</tr>
	</c:forEach>
  </table>
</div>

<div style="clear:both"></div>
