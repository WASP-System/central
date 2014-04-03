<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
<br />
<c:choose>
	<c:when test='${sampleId == "0"}'>
		<h1><fmt:message key="platformunitInstance.headerCreate.label"/></h1>
	</c:when>
	<c:otherwise>
		<h1><fmt:message key="platformunitInstance.headerUpdate.label"/></h1>
	</c:otherwise>
</c:choose>

<table class="EditTable ui-widget ui-widget-content">
<tr class="FormData">
	<td class="CaptionTD"><fmt:message key="platformunitInstance.platUnitType.label"/>:</td>
	<td class="DataTD">
	<form method="GET" action="<wasp:relativeUrl value="/facility/platformunit/createUpdatePlatformUnit.do" />">
		<input class="FormElement ui-widget-content ui-corner-all" type="hidden" name="sampleId" value="<c:out value="${sampleId}" />" />
		<select class="FormElement ui-widget-content ui-corner-all" name="sampleSubtypeId" size="1" onchange="this.parentNode.submit()">
			<c:if test='${sampleSubtypeId <= 0}'>
				<option value="0"><fmt:message key="wasp.default_select.label"/>
			</c:if>			
			<c:forEach items="${sampleSubtypes}" var="sampleSubtype">
				<c:set var="selectedFlag2" value=""/>
				<c:if test='${sampleSubtypeId==sampleSubtype.sampleSubtypeId}'>
					<c:set var="selectedFlag2" value="SELECTED"/>
				</c:if>
				<option value="<c:out value="${sampleSubtype.sampleSubtypeId}"/>" <c:out value="${selectedFlag2}"/> ><c:out value="${sampleSubtype.name}" /> 
			</c:forEach>
		 </select>
		 <span class="requiredField">*</span>
	</form> 	
	</td>
	<td>&nbsp;</td>
</tr>
<c:if test='${sampleSubtypeId > "0"}'>
  	<form:form  cssClass="FormGrid" commandName="sample" action="<wasp:relativeUrl value='facility/platformunit/createUpdatePlatformUnit.do' />">
  	
  	<input class="FormElement ui-widget-content ui-corner-all" type="hidden" name="sampleId" id="sampleId" value="<c:out value="${sampleId}" />" />
  	<input class="FormElement ui-widget-content ui-corner-all" type="hidden" name="sampleSubtypeId" id="sampleSubtypeId" value="<c:out value="${sampleSubtypeId}" />" />

	<%-- removed as per Andy 9/28/12; name will be taken from barcode. Also had to add form:hidden to compensate 
		SAVE THIS CODE, JUST IN CASE WE WANT TO PUT NAME BACK ONTO THE FORM
	<tr class="FormData">
        <td class="CaptionTD"><fmt:message key="platformunitInstance.name.label" />:</td>
        <td class="DataTD"><form:input cssClass="FormElement ui-widget-content ui-corner-all" path="name" /><span class="requiredField">*</span></td>
        <td class="CaptionTD error"><form:errors path="name" /></td>
	</tr>
	--%>

	<%--next line needed to suppress validation requirement for sample.name not to be null - so do not remove next line; recall the we decided to use the barcodeName as sample.name--%>
	<form:hidden path="name"/>
	<tr class="FormData">
        <td class="CaptionTD"><fmt:message key="platformunitInstance.barcode.label" />:</td>
        <td class="DataTD"><input class="FormElement ui-widget-content ui-corner-all"  name="barcode" id="barcode" value="<c:out value="${barcode}" />" /><span class="requiredField">*</span></td>
        <td class="CaptionTD error"><c:out value="${barcodeError}" /> </td>
	</tr>
	<tr class="FormData">
        <td class="CaptionTD"><fmt:message key="platformunitInstance.numberOfCellsRequested.label" />:</td>
        <td class="DataTD">        	
        	<select class="FormElement ui-widget-content ui-corner-all" name="numberOfCellsRequested" size="1">
				<option value="0"><fmt:message key="wasp.default_select.label"/> <%-- --select-- --%>
				<c:forEach items="${numberOfCellsList}" var="item">
					<c:set var="selectedFlag3" value=""/>
					<c:if test='${numberOfCellsOnThisPlatformUnit==item}'>
						<c:set var="selectedFlag3" value="SELECTED"/>
					</c:if>
					<option value="<c:out value="${item}"/>" <c:out value="${selectedFlag3}"/> ><c:out value="${item}" /> 
				</c:forEach>
		 	</select>        
        	<span class="requiredField">*</span></td>
        <td class="CaptionTD error"><c:out value="${numberOfCellsRequestedError}" /> </td>
	</tr>
	
	<c:set var="_area" value = "sample" scope="request"/>
	<c:set var="_metaArea" value = "platformunitInstance" scope="request"/>
    <c:set var="_metaList" value = "${sample.sampleMeta}" scope="request" />
    <c:import url="/WEB-INF/jsp/meta_rw.jsp"/>
     
    <tr><td colspan="3">
    	<div class="submit">
   	    	<input class="fm-button" type="button" onClick="submit();" value="<fmt:message key='platformunitInstance.submit.label'/>" /> 
   	    	<input type="hidden" name="referer" value="${referer}" />
 			<c:choose>
    			<c:when test="${sampleId > 0}">
    				&nbsp;<input class="fm-button" type="button" onClick="location.href='createUpdatePlatformUnit.do?reset=reset&sampleId=${sampleId}&sampleSubtypeId=${sampleSubtypeId}';" value="<fmt:message key='platformunitInstance.reset.label'/>" /> 
    				&nbsp;<input class="fm-button" type="button" onClick="location.href='${referer}';" value="<fmt:message key='platformunitInstance.cancel.label'/>" /> 
    			</c:when>
    			<c:otherwise>
 	   				&nbsp;<input class="fm-button" type="button" onClick="location.href='<wasp:relativeUrl value="dashboard.do" />';" value="<fmt:message key='platformunitInstance.cancel.label'/>" /> 
     			</c:otherwise>
    		</c:choose>
    	</div>
    </td></tr>
  </form:form>
</c:if>
</table>
