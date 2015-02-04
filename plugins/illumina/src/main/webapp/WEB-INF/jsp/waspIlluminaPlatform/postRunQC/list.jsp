<%@ page session="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="wasp" uri="http://einstein.yu.edu/wasp" %>
<br />
<h1><fmt:message key="pageTitle.waspIlluminaPlatform/postrunqc/list.label"/></h1>

<ul class="navTabs">
	
	<c:if test="${isTasks == false}"><div class="instructions"><fmt:message key="task.none.label" /></div></c:if>
	<c:if test="${isTasks == true}">
		<div class="instructions"><fmt:message key="task.instructions2.label" /></div>
		<c:forEach items="${taskHyperlinks}" var="hyperlink">
			<li>
				<a href='<wasp:relativeUrl value="${hyperlink.getTargetLink()}"/>'>${hyperlink.getLabel()}</a>
			</li>	
		</c:forEach>
	</c:if>
</ul>