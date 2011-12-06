<%@ include file="/WEB-INF/jsp/taglib.jsp" %>


<h1>Sample List</h1> 

<table cellpadding="0" cellspacing="0" border="0">
<tr>
	<th>Name</th>
	<th>Type</th>
	<th>Subtype</th>
	<th>Job</th>
	<th>Submitter</th>
	<th>Is Received</th>
	<th>Runs</th>
</tr>
<c:forEach items="${sample}" var="s">
	
	<tr>
		<td>${s.name}</td>
		<td>${s.typeSample.name}</td>
		<td>${s.subtypeSample.name}</td>
		<td>${s.job.name}</td>
		<td>${s.user.lastName}, ${s.user.firstName}</td>
		<td>${s.isReceived == 1}</td>
		<td>
	<c:forEach items="${s.run}" var="r">
		<a href="/wasp/run/detail/<c:out value="${r.runId}" />.do"><c:out value="${r.name}" /></a></br>
	</c:forEach>
		</td>
	</tr>
</c:forEach>
</table>

