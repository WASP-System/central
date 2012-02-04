<%@ include file="/WEB-INF/jsp/taglib.jsp"%>
<c:forEach items="${_metaList}" var="_meta" varStatus="status">
  <c:set var="_myArea">${_area}.</c:set>
  <c:set var="_myCtxArea">${_area}.</c:set>
  <c:if test="${_metaArea != null}">
    <c:set var="_myCtxArea">${_metaArea}.</c:set>
  </c:if>

  <c:set var="labelKey" value="${fn:replace(_meta.k, _myArea, _myCtxArea)}.label" />


	<c:if test="${empty _meta.property.control}">['<fmt:message
			key="${labelKey}" />'],</c:if>
</c:forEach>
