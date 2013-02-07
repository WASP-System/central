<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<div>
	<h1><fmt:message key="plugin.list.label" /></h1>
</div>
<div>
<c:choose>
	<c:when test="${empty pluginDescriptionHyperlinks}">
		<fmt:message key="plugin.none.label" />
	</c:when>
	<c:otherwise>
		<ul class="navTabs">
		<c:forEach items="${pluginDescriptionHyperlinks}" var="hyperlink">
			<li>
				<a href='<c:url value="${hyperlink.getTargetLink()}"/>'>${hyperlink.getLocalizedLabel()}</a>
			</li>
		</c:forEach>
		</ul>
	</c:otherwise>
</c:choose>
</div>