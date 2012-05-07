<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
<%-- 
<title><fmt:message key="pageTitle.task/samplereceive/list.label"/></title>
<h1><fmt:message key="task.samplereceive.title_label" /></h1>
--%>
<title>Control Libraries</title>
<h1>Control Libraries</h1>

<c:choose>
<c:when test="${controlLibraryList.size()==0}">
<h2>No Control Libraries Exit</h2>
<input class="ui-widget ui-widget-content fm-button" type="button" value="Create New Control" onclick='location.href="<c:url value="/sample/createUpdateLibraryControl/0.do" />"' />
</c:when>
<c:otherwise>
<input class="ui-widget ui-widget-content fm-button" type="button" value="Create New Control" onclick='location.href="<c:url value="/sample/createUpdateLibraryControl/0.do" />"' />
<table class="EditTable ui-widget ui-widget-content">
<tr class="FormData"><td class="label-centered">Control Name</td><td class="label-centered">Adaptor Set</td><td class="label-centered">Adaptor</td><td class="label-centered">Is Active?</td></tr>
<tr><td colspan="4" style='background-color:black'></td></tr>
<c:forEach items="${controlLibraryList}" var="controlLibrary">
	<c:set var="adaptor" value="${libraryAdaptorMap.get(controlLibrary)}" scope="page" />
	<tr>
		<td><c:out value="${controlLibrary.getName()}"/> <a href="<c:url value="/sample/createUpdateLibraryControl/${controlLibrary.getSampleId()}.do" />">[edit]</a></td>
		<td><c:out value="${adaptor.getAdaptorset().getName()}"/></td>
		<td>Index <c:out value="${adaptor.getBarcodenumber()}"/>&nbsp;(<c:out value="${adaptor.getBarcodesequence()}"/>)</td>
		<c:set var="active" value='${controlLibrary.getIsActive()==1?"Active":"Inactive"}' scope="page" />
		<td><c:out value="${active}"/></td>
	</tr>
</c:forEach>

</table>
</c:otherwise>
</c:choose>