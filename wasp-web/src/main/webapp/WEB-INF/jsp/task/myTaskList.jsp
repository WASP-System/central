<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
<br />
<h1><fmt:message key="pageTitle.task/myTaskList.label"/></h1>

<ul class="navTabs">
	
	<c:if test="${isTasks == false}"><div class="instructions"><fmt:message key="task.none.label" /></div></c:if>
	<c:if test="${isTasks == true}">
		<div class="instructions"><fmt:message key="task.instructions2.label" /></div>
		<c:forEach items="${taskHyperlinks}" var="hyperlink">
			<li>
				<a href='<wasp:relativeUrl value="${hyperlink.getTargetLink()}"/>' onClick="openWaitDialog()">${hyperlink.getLabel()}</a>
			</li>	
		</c:forEach>
	</c:if>
</ul>

<%-- 
<br /><br />
<a href='<wasp:relativeUrl value="task/cellLibraryQC/list.do"/>'>FOR TESTING - cellLibraryQC</a>		
--%>		
	