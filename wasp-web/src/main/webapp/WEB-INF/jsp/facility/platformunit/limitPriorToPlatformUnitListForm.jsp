<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
<br />
<c:choose>
 <c:when test='${fn:length(resourceCategories) > "0"}'>
<form method="GET" action="<wasp:relativeUrl value="/facility/platformunit/list.do" />">
<select class="FormElement ui-widget-content ui-corner-all" name="resourceCategoryId" size="1">
<option value="0">--<fmt:message key="puLimitPriorToPUList.selectMachine.label"/>--
<c:forEach items="${resourceCategories}" var="rc">
<option value="<c:out value="${rc.resourceCategoryId}" />"><c:out value="${rc.name}" /> 
</c:forEach>
</select>
<input class="FormElement ui-widget-content ui-corner-all" type="submit" value="Submit">
</form>
</c:when>
<c:otherwise><fmt:message key="puLimitPriorToPUList.noMPSResourcesAvailable.label"/></c:otherwise>
</c:choose>
