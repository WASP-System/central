<%@ include file="/WEB-INF/jsp/taglib.jsp"%>
<c:forEach items="${_metaList}" var="_meta" varStatus="status">
	<c:set var="_myArea">${_area}.</c:set>
	<c:set var="labelKey"
		value="${_myArea}${fn:replace(_meta.k, _myArea, '\"\"')}.label" />
	<c:if test="${empty _meta.property.control}">['<fmt:message
			key="${labelKey}" />'],</c:if>
</c:forEach>