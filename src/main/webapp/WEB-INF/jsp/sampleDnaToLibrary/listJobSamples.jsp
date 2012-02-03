<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

Job ID: J<c:out value="${job.jobId}" /><br />
Job Name: <c:out value="${job.name}" /><br /><br />
User Submitted Samples: <c:if test="${userSuppliedSampleList.isEmpty()}">None</c:if>	<br />
<c:forEach items="${userSuppliedSampleList}" var="sample">
<c:out value="${sample.name}" /> (<c:out value="${sample.typeSample.name}" />)		
	<c:choose>
		<c:when test="${sample.isReceived == 1}">&nbsp;<a href="/wasp/sampleDnaToLibrary/detail/<c:out value="${sample.sampleId}" />.do">Create New Library</a><br /></c:when>
		<c:otherwise>&nbsp;Sample Not Received<br /></c:otherwise>
	</c:choose>
	<c:forEach items="${sample.sampleSourceViaSourceSampleId}" var="facilityGeneratedLib">
		<c:if test="${facilityGeneratedLib.sample.typeSample.typeSampleId == 3}">
			&nbsp;&nbsp;&nbsp;<c:out value="${facilityGeneratedLib.sample.name}" /> (<c:out value="${facilityGeneratedLib.sample.typeSample.name}" />) <a href="/wasp/sampleDnaToLibrary/detail/<c:out value="${facilityGeneratedLib.sample.sampleId}" />.do">View Library</a> <a href="">Add To Flow Cell</a><br />
		</c:if>
	</c:forEach>
</c:forEach>

<br />User Submitted Libraries: <c:if test="${userSuppliedLibraryList.isEmpty()}">None</c:if>	<br />
<c:forEach items="${userSuppliedLibraryList}" var="library">
<c:out value="${library.name}" /> (<c:out value="${library.typeSample.name}" />)		
	<c:choose>
		<c:when test="${library.isReceived == 1}">&nbsp;<a href="">Add To Flow Cell</a><br /></c:when>
		<c:otherwise>&nbsp;Library Not Received<br /></c:otherwise>
	</c:choose>
</c:forEach>