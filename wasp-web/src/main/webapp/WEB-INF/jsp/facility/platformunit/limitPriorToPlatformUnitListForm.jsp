<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
<wasp:message /><br />
<c:choose>
 <c:when test='${fn:length(resourceCategories) > "0"}'>
<form method="GET" action="<c:url value="/facility/platformunit/list.do" />">
<select name="resourceCategoryId" size="1">
<option value="0">--SELECT A MACHINE--
<c:forEach items="${resourceCategories}" var="rc">
<option value="<c:out value="${rc.resourceCategoryId}" />"><c:out value="${rc.name}" /> 
</c:forEach>
</select>
<input type="submit" value="Submit">
</form>
</c:when>
<c:otherwise>No MPS Resources Available</c:otherwise>
</c:choose>
