<%@ include file="/WEB-INF/jsp/taglib.jsp"%>


<h1>Resource List</h1>


<a href="/wasp/resource/create.do">Create New Resource</a>

<p>
<table cellpadding="0" cellspacing="0" border="0">
	<c:forEach items="${resource}" var="r">
		<tr>
			<td>
				<a href="/wasp/resource/detail_ro/<c:out value="${r.resourceId}" />.do">
					<c:out value="${r.name}" />
				</a>
			</td>
		</tr>
	</c:forEach>
</table>

