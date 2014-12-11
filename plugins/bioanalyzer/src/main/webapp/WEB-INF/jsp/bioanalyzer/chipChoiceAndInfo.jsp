<%@ page session="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="wasp" uri="http://einstein.yu.edu/wasp" %>
<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<%-- message keys link to internationalized text. Conversions can be specified in the .properties files in /src/main/resources/i18n --%>

<h1>
	<fmt:message key="pageTitle.bioanalyzer/chipChoiceAndInfo.label" /> 
</h1>

<%@ include file="/WEB-INF/jsp/jobsubmit/jobsubmitinfo.jsp" %>

<div class="instructions">
	<fmt:message key="bioanalyzer.chipChoiceAndInfo_instruction.label" />
</div>
<div style="border:1px solid red; padding-left: 15px;">
	<h2><fmt:message key="bioanalyzer.chipChoiceAndInfo_availableChips.label" /></h2>
	<span style="font-weight:bold"><fmt:message key="bioanalyzer.chipChoiceAndInfo_bioanalyzerChipHighSensitivity.label" />:</span> <fmt:message key="bioanalyzer.chipChoiceAndInfo_bioanalyzerChipHighSensitivityUse.label" /><br />
	<span style="margin-left:30px"><fmt:message key="bioanalyzer.chipChoiceAndInfo_bioanalyzerChipHighSensitivityAssays.label" /><br /><br />
	<span style="font-weight:bold"><fmt:message key="bioanalyzer.chipChoiceAndInfo_bioanalyzerChip7500.label" />:</span> <fmt:message key="bioanalyzer.chipChoiceAndInfo_bioanalyzerChip7500Use.label" /><br />
	<span style="margin-left:30px"><fmt:message key="bioanalyzer.chipChoiceAndInfo_bioanalyzerChip7500Assays.label" /><br /><br />
	<span style="font-weight:bold"><fmt:message key="bioanalyzer.chipChoiceAndInfo_bioanalyzerChip1000.label" />:</span> <fmt:message key="bioanalyzer.chipChoiceAndInfo_bioanalyzerChip1000Use.label" /><br />
	<span style="margin-left:30px"><fmt:message key="bioanalyzer.chipChoiceAndInfo_bioanalyzerChip1000Assays.label" /><br /><br />
	<span style="font-weight:bold"><fmt:message key="bioanalyzer.chipChoiceAndInfo_chipCost.label" />:</span> <fmt:message key="bioanalyzer.chipChoiceAndInfo_chipCostNumber.label" /><br /><br />
	<span style="font-weight:bold; color:red"><fmt:message key="bioanalyzer.chipChoiceAndInfo_costSurcharge.label" /><br /><br />
</div>
<br />
<div style="float:left"> 
	<form:form  cssClass="FormGrid" >	
	<table class="EditTable ui-widget ui-widget-content">  
	  
	  <tr class="FormData">
	    <td class="CaptionTD"><fmt:message key="bioanalyzer.chipChoiceAndInfo_bioanalyzerChip.label" />:</td>
	    <td class="DataTD" >
	    	<select class="FormElement ui-widget-content ui-corner-all" id="bioanalyzerChip" name="bioanalyzerChip">
	      		<option value='-1'><fmt:message key="wasp.default_select.label"/></option>
	      		<c:forEach var="bioanalyzerChip" items="${availableBioanalyzerChipList}">
	      			<option value="${bioanalyzerChip}"  <c:if test="${bioanalyzerChip == userSelectedBioanalyzerChip}"> SELECTED</c:if>  ><c:out value="${bioanalyzerChip}"/></option>
	      		</c:forEach>		      				      		
	      	</select>
	      	<span class="requiredField">*</span>
	    </td>
	    <td class="CaptionTD error"> <c:out value="${chipError}"/> </td>
	  </tr>
	  <!--  
	  <tr class="FormData">
	    <td class="CaptionTD"><fmt:message key="bioanalyzer.chipChoiceAndInfo_assayLibrariesAreFor.label" />:</td>
	    <td class="DataTD" >
	    	<select class="FormElement ui-widget-content ui-corner-all" name="workflowIdLibrariesAreDesignedFor">
	      		<option value='-1'><fmt:message key="wasp.default_select.label"/></option>
	      		<c:forEach var="workflow" items="${workflowList}">
	      			<option value="${workflow.getId()}" <c:if test="${workflow.getId() == userSelectedWorkflowIdLibrariesAreDesignedFor}"> SELECTED</c:if>  ><c:out value="${workflow.getName()}"/></option>
	      		</c:forEach>		      				      		
	      	</select>
	      	<span class="requiredField">*</span>
	    </td>
	    <td class="CaptionTD error"> <c:out value="${workflowError}"/> </td>
	  </tr>
	  -->
	  <tr class="FormData">
	    <td class="CaptionTD"><fmt:message key="bioanalyzer.chipChoiceAndInfo_assayLibrariesAreFor.label" />:</td>
	    <td class="DataTD" >
	    	<input class="FormElement ui-widget-content ui-corner-all" name="assayLibrariesAreFor" value="<c:out value="${assayLibrariesAreFor}"/>">	     
	      	<span class="requiredField">*</span> 
	      	<wasp:tooltip value="${assayLibrariesAreForToolTip}" />		   
	    </td>
	    <td class="CaptionTD error"> <c:out value="${assayLibrariesAreForError}"/> </td>
	 </tr>  
	</table>
	  
	<div class="submit">
		<input class="fm-button" type="button" value="<fmt:message key="jobDraft.finishLater.label" />" onClick="window.location='<wasp:relativeUrl value="dashboard.do"/>'" /> 
		<input  class="FormElement ui-widget-content ui-corner-all" type="submit" value="<fmt:message key="bioanalyzer.chipChoiceAndInfo_continue.label"/>">
	</div>	  
  </form:form>	  
</div>