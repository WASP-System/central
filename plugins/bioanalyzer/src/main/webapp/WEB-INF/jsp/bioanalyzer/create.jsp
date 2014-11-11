<%@ page session="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="wasp" uri="http://einstein.yu.edu/wasp" %>
<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
<script type="text/javascript" src="<wasp:relativeUrl value='scripts/jquery/jquery.table.addrow.js' />"></script>

<%-- message keys link to internationalized text. Conversions can be specified in the .properties files in /src/main/resources/i18n --%>

<h1>
	<fmt:message key="pageTitle.bioanalyzer/create.label" />
</h1>
<div class="instructions">
	<fmt:message key="bioanalyzer.create_instruction.label" />
</div>
<div id="container_div_for_adding_rows" >
<div style="float:left"> 
	<form:form  cssClass="FormGrid" >
	
	<table class="EditTable ui-widget ui-widget-content">
		  
	  <tr class="FormData">
	    <td class="CaptionTD"><fmt:message key="bioanalyzer.create_lab.label" />:</td>
	    <td class="DataTD" >
	    	<select class="FormElement ui-widget-content ui-corner-all" id="labId" name="labId">
	      		<option value='-1'><fmt:message key="wasp.default_select.label"/></option>
	      		<c:forEach var="lab" items="${labList}">
	      			<option value="${lab.getId()}" ><c:out value="${lab.getUser().getNameFstLst()}"/>
	      			    <c:set value="${labPiInstitutionMap.get(lab)}" var="institution"/>
	      				<c:if test="${not empty institution}">
	      					[<c:out value="${institution}"/>]
	      				</c:if>
	      			</option>
	      		</c:forEach>		      				      		
	      	</select>
	      	<span class="requiredField">*</span>
	    </td>
	    <td class="CaptionTD error"> <c:out value="${labError}"/> </td>
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
	  
	  <tr class="FormData">
	    <td class="CaptionTD"><fmt:message key="bioanalyzer.create_bioanalyzerChip.label" />:</td>
	    <td class="DataTD" >
	    	<select class="FormElement ui-widget-content ui-corner-all" id="bioanalyzerChip" name="bioanalyzerChip">
	      		<option value='-1'><fmt:message key="wasp.default_select.label"/></option>
	      		<c:forEach var="bioanalyzerChip" items="${availableBioanalyzerChipList}">
	      			<option value="${bioanalyzerChip}" ><c:out value="${bioanalyzerChip}"/></option>
	      		</c:forEach>		      				      		
	      	</select>
	      	<span class="requiredField">*</span>
	    </td>
	    <td class="CaptionTD error"> <c:out value="${chipError}"/> </td>
	  </tr>
	  
	  <tr class="FormData">
	    <td class="CaptionTD"><fmt:message key="bioanalyzer.create_workflow.label" />:</td>
	    <td class="DataTD" >
	    	<br />
	    	<select class="FormElement ui-widget-content ui-corner-all" id="workflowId" name="workflowId">
	      		<option value='-1'><fmt:message key="wasp.default_select.label"/></option>
	      		<c:forEach var="workflow" items="${workflowList}">
	      			<option value="${workflow.getId()}" ><c:out value="${workflow.getName()}"/></option>
	      		</c:forEach>		      				      		
	      	</select>
	      	<span class="requiredField">*</span>
	    </td>
	    <td class="CaptionTD error"> <c:out value="${workflowError}"/> </td>
	  </tr>
	  
	  <tr class="FormData">
	    <td class="CaptionTD"><fmt:message key="bioanalyzer.create_jobName.label" />:</td>
	    <td class="DataTD" >
	    	<input class="FormElement ui-widget-content ui-corner-all" name="jobName" value="<c:out value="${jobName}"/>">	     
	      	<span class="requiredField">*</span>
	    </td>
	    <td class="CaptionTD error"> <c:out value="${jobNameError}"/> </td>
	  </tr>
	  
	  </table>
	  
	  
	  
	  
	  
	  
	  
	  
	  <br />
	  
	  <table class="data" style="margin: 0px 0px" >
			<tr class="FormData">
				<td align='center' style="background-color:#FAF2D6; font-weight:bold" nowrap><fmt:message key="bioanalyzer.create_libraryName.label" /><span style="color:red">*</span></td>
				<td align='center' style="background-color:#FAF2D6; font-weight:bold" nowrap><fmt:message key="bioanalyzer.create_librarySize.label" /><span style="color:red">*</span></td>
				<td align='center' style="background-color:#FAF2D6; font-weight:bold" nowrap><fmt:message key="bioanalyzer.create_libraryConcentration.label" /><span style="color:red">*</span></td>
				<td align='center' style="background-color:#FAF2D6; font-weight:bold" nowrap><fmt:message key="bioanalyzer.create_libraryConcDeterminedByFluorometry.label" /><span style="color:red">*</span></td>
				<td align='center' style="background-color:#FAF2D6; font-weight:bold" nowrap><fmt:message key="bioanalyzer.create_libraryVolume.label" /><span style="color:red">*</span></td>
				<td align='center' style="background-color:#FAF2D6; font-weight:bold" nowrap><fmt:message key="bioanalyzer.create_libraryBuffer.label" /><span style="color:red">*</span></td>
				
				<td align='center' style="background-color:#FAF2D6; font-weight:bold" nowrap><fmt:message key="bioanalyzer.create_action.label" /></td>
			</tr>
			<tr>
				<td>					
					<input type="text" class="FormElement ui-widget-content ui-corner-all"   name='libraryName' id='libraryName' value="${libraryName}">
				</td>
				<td>					
					<input type="text" class="FormElement ui-widget-content ui-corner-all"   name='librarySize' id='librarySize' value="${librarySize}">
				</td>
				<td>					
					<input type="text" class="FormElement ui-widget-content ui-corner-all"   name='libraryConcentration' id='libraryConcentration' value="${libraryConcentration}">
				</td>
				<td>					
					<select class="FormElement ui-widget-content ui-corner-all" id="concDeterminedByFluorometry" name="concDeterminedByFluorometry">
	      				<option value='-1'><fmt:message key="wasp.default_select.label"/></option>
	      				<option value="yes" ><fmt:message key="bioanalyzer.create_libraryConcDeterminedByFluorometryYes.label" /></option>
	      				<option value="no" ><fmt:message key="bioanalyzer.create_libraryConcDeterminedByFluorometryNo.label" /></option>      				      		
	      			</select>
				</td>
				<td>					
					<select class="FormElement ui-widget-content ui-corner-all" id="libraryBuffer" name="libraryBuffer">
	      				<option value='-1'><fmt:message key="wasp.default_select.label"/></option>
	      				<option value="TE" >TE</option>
	      			</select>
				</td>
				<td>					
					<input type="text" class="FormElement ui-widget-content ui-corner-all"   name='libraryVolume' id='libraryVolume' value="${libraryVolume}">
				</td>	
				<td align='center'>
					<input type="button" class="delRow" value="<fmt:message key="jobsubmitManySamples.deleteRow.label" />"/>		
				</td>
			</tr>			
			<tr ><td colspan="7" align="center"><input style="width:300" type="button" class="addRow" value="Add Row"/> </td></tr>
		</table>
	  
	  <div class="submit">
	    <div id="continueButtonDivId"   <c:choose><c:when test="${empty assayWorkflows}">style="display:none"</c:when><c:otherwise>style="display:inline"</c:otherwise></c:choose> >
	     <input  class="FormElement ui-widget-content ui-corner-all" type="submit" value="<fmt:message key="jobDraft.continue.label"/>">
		</div>
	  </div>
	  
	  </form:form>
	  
</div>
</div>