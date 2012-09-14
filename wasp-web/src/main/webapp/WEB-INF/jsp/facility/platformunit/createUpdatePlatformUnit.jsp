<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
<br />
<title><fmt:message key="pageTitle.facility/platformunit/createUpdatePlatformUnit.label"/></title>
<h1><fmt:message key="pageTitle.facility/platformunit/createUpdatePlatformUnit.label"/></h1>
 
<table class="EditTable ui-widget ui-widget-content">
<tr class="FormData">
	<td class="CaptionTD">Choose A Machine:</td>
	<td  class="DataTD">
		<form method="GET" action="<c:url value="/facility/platformunit/createUpdatePlatformUnit.do" />">
			<input class="FormElement ui-widget-content ui-corner-all" type="hidden" name="sampleSubtypeId" value="<c:out value="0" />" />
			<select class="FormElement ui-widget-content ui-corner-all" name="resourceCategoryId" size="1" onchange="this.parentNode.submit()">
			<option value="0">--SELECT A MACHINE--
			<c:forEach items="${resourceCategories}" var="rc">
			<c:set var="selectedFlag" value=""/>
			<c:if test='${resourceCategoryId==rc.resourceCategoryId}'>
				<c:set var="selectedFlag" value="SELECTED"/>
			</c:if>
			<option value="<c:out value="${rc.resourceCategoryId}" />" <c:out value="${selectedFlag}" /> ><c:out value="${rc.name}" /> 
			</c:forEach>
			</select>
		</form>
	</td>
	<td></td>
</tr>

<c:if test='${resourceCategoryId > "0"}'>
<tr class="FormData">
	<td class="CaptionTD">Choose A Type Of Flow Cell:</td>
	<td class="DataTD">
	<form method="GET" action="<c:url value="/facility/platformunit/createUpdatePlatformUnit.do" />">
		<input class="FormElement ui-widget-content ui-corner-all" type="hidden" name="resourceCategoryId" value="<c:out value="${resourceCategoryId}" />" />
		<select class="FormElement ui-widget-content ui-corner-all" name="sampleSubtypeId" size="1" onchange="this.parentNode.submit()">
			<option value="0">--SELECT A TYPE OF FLOW CELL--			
			<c:forEach items="${sampleSubtypes}" var="sampleSubtype">
				<c:set var="selectedFlag2" value=""/>
				<c:if test='${sampleSubtypeId==sampleSubtype.sampleSubtypeId}'>
					<c:set var="selectedFlag2" value="SELECTED"/>
				</c:if>
				<option value="<c:out value="${sampleSubtype.sampleSubtypeId}"/>" <c:out value="${selectedFlag2}"/> ><c:out value="${sampleSubtype.name}" /> 
			</c:forEach>
		 </select>
	</form> 
	</td>
	<td></td>
</tr>
</c:if>

<c:if test='${resourceCategoryId > "0" && sampleSubtypeId > "0"}'>
  	<form:form  cssClass="FormGrid" commandName="sample" action="/wasp/facility/platformunit/createUpdatePlatformUnit.do">
  	<input class="FormElement ui-widget-content ui-corner-all" type="hidden" name="resourceCategoryId" id="resourceCategoryId" value="<c:out value="${resourceCategoryId}" />" />
  	<input class="FormElement ui-widget-content ui-corner-all" type="hidden" name="sampleSubtypeId" id="sampleSubtypeId" value="<c:out value="${sampleSubtypeId}" />" />
	<tr class="FormData">
        <td class="CaptionTD"><fmt:message key="platformunitInstance.name.label" />:</td>
        <td class="DataTD"><form:input cssClass="FormElement ui-widget-content ui-corner-all" path="name" /><span class="requiredField">*</span></td>
        <td class="CaptionTD error"><form:errors path="name" /></td>
	</tr>
	<tr class="FormData">
        <td class="CaptionTD"><fmt:message key="platformunitInstance.barcode.label" />:</td>
        <td class="DataTD"><input class="FormElement ui-widget-content ui-corner-all"  name="barcode" id="barcode" value="<c:out value="${barcode}" />" /><span class="requiredField">*</span></td>
        <td class="CaptionTD error"><c:out value="${barcodeError}" /> </td>
	</tr>

	<c:set var="_area" value = "sample" scope="request"/>
	<c:set var="_metaArea" value = "platformunitInstance" scope="request"/>
    <c:set var="_metaList" value = "${sample.sampleMeta}" scope="request" />
    <c:import url="/WEB-INF/jsp/meta_rw.jsp"/>
    
    <div class="submit">
    <tr><td colspan="3">
    	<input class="FormElement ui-widget-content ui-corner-all" type="submit" value="<fmt:message key='platformunitInstance.submit.label'/>" /> 
    </td></tr>
    </div>
    
  </form:form>
</c:if>
</table>




<%-- form prior to 9/10/12; no longer being used
<c:if test='${resourceCategoryId > "0" && sampleSubtypeId > "0"}'>
 <form name="submitForm" id="submitForm" method="POST" action="<c:url value="/facility/platformunit/createUpdatePlatformUnit.do" />">
 <input class="FormElement ui-widget-content ui-corner-all" type="hidden" name="resourceCategoryId" id="resourceCategoryId" value="<c:out value="${resourceCategoryId}" />" />
 <input class="FormElement ui-widget-content ui-corner-all" type="hidden" name="sampleSubtypeId" id="sampleSubtypeId" value="<c:out value="${sampleSubtypeId}" />" />

<tr class="FormData">
	<td class="CaptionTD">Choose A Read Length:</td>
	<td class="DataTD">
		<select class="FormElement ui-widget-content ui-corner-all" name="readLength" id="readLength" size="1" >
			<option value="0">--SELECT A READ LENGTH--			
			<c:forEach items="${readLengths}" var="readLength">
				<c:set var="readLengthSplit" value="${fn:split(readLength, ':')}" />
				<option value="<c:out value="${readLengthSplit[0]}"/>"  ><c:out value="${readLengthSplit[1]}" /> 
			</c:forEach>
		 </select>
	</td>
</tr>
<tr class="FormData">
	<td class="CaptionTD">Choose A Read Type:</td>
	<td class="DataTD">
		 <select class="FormElement ui-widget-content ui-corner-all" name="readType" id="readType" size="1" >
			<option value="0">--SELECT A READ TYPE--			
			<c:forEach items="${readTypes}" var="readType">
				<c:set var="readTypeSplit" value="${fn:split(readType, ':')}" />
				<option value="<c:out value="${readTypeSplit[0]}"/>"  ><c:out value="${readTypeSplit[1]}" /> 
			</c:forEach>
		 </select>
	</td>
</tr>

 
<tr class="FormData"><td class="CaptionTD">Name: </td><td class="DataTD"><input type='text' name='name' id='name' size='25' maxlength='30' /></td></tr>

<tr class="FormData"><td class="CaptionTD">Barcode: </td><td class="DataTD"><input type='text' name='barcode' id='barcode' size='25' maxlength='30' /></td></tr>

<tr class="FormData">
	<td class="CaptionTD">Choose Number of Lanes:</td>
	<td class="DataTD">
		 <select class="FormElement ui-widget-content ui-corner-all" name="numberOfLanes" id="numberOfLanes" size="1" >
			<option value="0">--SELECT NUMBER OF LANES--			
			<c:forEach items="${numberOfLanesAvailableList}" var="numberOfLanes">
				<option value="<c:out value="${numberOfLanes}"/>"  ><c:out value="${numberOfLanes}" /> 
			</c:forEach>
		 </select>
	</td>
</tr>

<tr class="FormData"><td class="CaptionTD" style="vertical-align:top">Comments: </td><td class="DataTD"><textarea name='comments' id='comments' rows='5' cols='30' ></textarea></td></tr>

<tr class="FormData"><td class="CaptionTD">Name: </td><td class="DataTD"><input type='text' name='name' id='name' size='25' maxlength='30' /></td></tr>
<tr class="FormData"><td class="CaptionTD">Barcode: </td><td class="DataTD"><input type='text' name='barcode' id='barcode' size='25' maxlength='30' /></td></tr>

			<c:set var="_area" value = "sample" scope="request"/>
			<c:set var="_metaArea" value = "platformunitInstance" scope="request"/>
          	<c:set var="_metaList" value = "${sampleMeta}" scope="request" />
          	<c:import url="/WEB-INF/jsp/meta_rw.jsp"/>

<tr class="FormData">	
	<td class="DataTD" colspan='2'>		 
		  <input class="FormElement ui-widget-content ui-corner-all" type="submit" value="Submit">  
	</td>
</tr>	 
</form>
</c:if>
</table>
--%>