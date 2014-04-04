<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
<br />
<h1><fmt:message key="sample.controlLib_controlLibraries.label" /></h1>

<c:choose>
<c:when test="${controlLibraryList.size()==0}">
<h2><fmt:message key="sample.controlLib_noneExist.label" /></h2>
<input class="ui-widget ui-widget-content fm-button" type="button" value="<fmt:message key="sample.controlLib_createButton.label" />" onclick='location.href="<wasp:relativeUrl value="sample/createUpdateLibraryControl/0.do" />"' />
</c:when>
<c:otherwise>
<input class="ui-widget ui-widget-content fm-button" type="button" value="<fmt:message key="sample.controlLib_createButton.label" />" onclick='location.href="<wasp:relativeUrl value="sample/createUpdateLibraryControl/0.do" />"' />
<table class="EditTable ui-widget ui-widget-content">
<tr class="FormData"><th class="label-centered" style="font-weight:bold; background-color:#FAF2D6"><fmt:message key="sample.controlLib_name.label" /></th><th class="label-centered" style="font-weight:bold; background-color:#FAF2D6"><fmt:message key="sample.controlLib_adaptorSet.label" /></th><th class="label-centered" style="font-weight:bold; background-color:#FAF2D6"><fmt:message key="sample.controlLib_adaptor.label" /></th><th class="label-centered" style="font-weight:bold; background-color:#FAF2D6"><fmt:message key="sample.controlLib_isActive.label" /></th></tr>
<tr><td colspan="4" style='background-color:black'></td></tr>
<c:forEach items="${controlLibraryList}" var="controlLibrary">
	<c:set var="adaptor" value="${libraryAdaptorMap.get(controlLibrary)}" scope="page" />
	<tr>
		<td><c:out value="${controlLibrary.getName()}"/> <a href="<wasp:relativeUrl value="sample/createUpdateLibraryControl/${controlLibrary.getSampleId()}.do" />">[<fmt:message key="sample.controlLib_edit.label" />]</a></td>
		<td><c:out value="${adaptor.getAdaptorset().getName()}"/></td>
		<td><fmt:message key="sample.controlLib_index.label" /> <c:out value="${adaptor.getBarcodenumber()}"/>&nbsp;(<c:out value="${adaptor.getBarcodesequence()}"/>)</td>
		<c:set var="active" value='${controlLibrary.getIsActive()==1?"Active":"Inactive"}' scope="page" />
		<td><c:out value="${active}"/></td>
	</tr>
</c:forEach>

</table>
</c:otherwise>
</c:choose>