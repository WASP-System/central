<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<form method="GET" action="<c:url value="/facility/platformunit/assign.do" />">
	
<select name="resourceCategoryId" size="1">
<option value="">--SELECT A MACHINE--
<c:forEach items="${resourceCategories}" var="rc">
<option value="<c:out value="${rc.resourceCategoryId}" />"><c:out value="${rc.name}" /> 
</c:forEach>
</select>
<input type="submit" value="Submit">
</form>