<%@ include file="/WEB-INF/jsp/include.jsp"%>
<%@ include file="/WEB-INF/jsp/taglib.jsp"%>
<br />
<div>
	<h1><fmt:message key="home.waspHomePage.label" /></h1>
	<h2><fmt:message key="home.welcomeBack.label" />, ${me.firstName} ${me.lastName}</h2>
	<c:if test="${isTasks == true}">
		<br />
		<h2 style="color:red">*****<fmt:message key="home.tasksAwaiting.label" />*****</h2>
		<!--<c:forEach items="${taskHyperlinks}" var="hyperlink">
			<li>
				<a href='<c:url value="${hyperlink.getTargetLink()}"/>'>${hyperlink.getLabel()}</a>
			</li>	
		</c:forEach>-->
	</c:if>
</div>