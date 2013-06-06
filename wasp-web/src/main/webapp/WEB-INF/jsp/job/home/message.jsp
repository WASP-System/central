<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
<a class="button" href="javascript:void(0);"  onclick='parent.loadNewPage(this, "<c:url value="/job/${job.getId()}/samples.do" />");' >Back To: Samples, Libraries &amp; Runs</a>
<br />
<br />
<c:if test="${fn:length(successMessage)>0}">
		<h2 style="color:green;font-weight:bold"><c:out value="${successMessage}" /></h2>
</c:if>
<c:if test="${fn:length(errorMessage)>0}">
		<h2 style="color:red;font-weight:bold"><c:out value="${errorMessage}" /></h2>
</c:if>