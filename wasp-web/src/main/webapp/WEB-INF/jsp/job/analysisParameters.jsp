<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
<br />
<title><fmt:message key="pageTitle.job/analysisParameters.label"/></title>
<h1><fmt:message key="pageTitle.job/analysisParameters.label"/> - <a style="color: #801A00;" href="<wasp:relativeUrl value="sampleDnaToLibrary/listJobSamples/${job.jobId}.do" />">J<c:out value="${job.jobId}" /></a></h1>
<br/>
<div>
	<c:choose>
		<c:when test="${empty softwareList}">
			<h2 style="font-weight:bold"><fmt:message key="analysisParameters.no_software_requested.label"/></h2>
		</c:when>
		<c:otherwise>
			<h2 style="font-weight:bold"><fmt:message key="analysisParameters.software_requested.label"/>:</h2>
			<table class="EditTable ui-widget ui-widget-content">
    			<c:forEach items="${softwareList}" var="software">
   	  				<tr class="FormData"><td colspan="2" class="label-centered" style="font-weight:bold;text-decoration:underline"><c:out value="${software.getResourceType().getName()}" />: <c:out value="${software.name}" /></td></tr>
   	  				<c:set var="_area" value = "${parentArea}" scope="request"/>
   	  				<c:set value="${softwareAndSyncdMetaMap.get(software)}" var="_metaList" scope="request" />
	  				<c:import url="/WEB-INF/jsp/meta_ro.jsp" />
  				</c:forEach> 
			</table>
		</c:otherwise>
	</c:choose>
</div>

<div>
  <c:if test="${not empty samplePairsMap}">
      <br /><br />
	  <c:set var="samplePairsMap" value="${samplePairsMap}" />
	  <c:set var="columns" value="${fn:length(submittedSamplesList)}" />
	  <h2 style="font-weight:bold;"><fmt:message key="analysisParameters.analysis_pairs.label"/>:</h2>
	  <table class="EditTable ui-widget ui-widget-content">
	  <tr class="FormData"><th>&nbsp;</th><th colspan = "${columns}"><div style="font-weight:bold;font-style:italic;text-decoration : underline;"><fmt:message key="analysisParameters.test_samples.label"/></div></th></tr>
	  <tr class="FormData">
	    <th>&nbsp;</th>
	  	<c:forEach var="sample" items="${submittedSamplesList}">
	  		<th><c:out value="${sample.name}" /></th>
	  	</c:forEach>
	  </tr>	  
	  <tr class="FormData"><th><div style="font-weight:bold;font-style:italic;text-decoration : underline;"><fmt:message key="analysisParameters.control_samples.label"/></div></th><td colspan = "${columns}">&nbsp;</td></tr>  
	  <c:forEach var="sample" items="${submittedSamplesList}">
	   <c:set var="truefalseList" value="${samplePairsMap.get(sample)}" />
	   <c:if test="${truefalseList != null}">
	    <tr class="FormData">
	     <th><c:out value="${sample.name}" /></th>
	     	<c:forEach var="isPaired" items="${truefalseList}">
	  			<c:choose>
	  				<c:when test="${isPaired == 'd'}">
	  					<td align="center">&nbsp;</td>
	  				</c:when>
	  				<c:when test="${isPaired == 'f'}">
	  					<td align="center"><input type="checkbox" DISABLED /></td>
	  				</c:when>
	  				<c:otherwise>
	  					<td align="center"><input type="checkbox" DISABLED checked="checked"/></td>
	  				</c:otherwise>
	  			</c:choose>
			</c:forEach>
	  	</tr>
	  	</c:if>
	  </c:forEach>
	 
	  </table>
	  </c:if>
</div>

<div>
  <c:if test="${not empty controlIsReferenceList}">
  	<br /><br />
	  <c:set var="columns" value="${fn:length(submittedSamplesList)}" />
	  <h2 style="font-weight:bold;"><fmt:message key="analysisParameters.reference_sample_pairs.label"/>:</h2>
	  <table class="EditTable ui-widget ui-widget-content">
	  <tr class="FormData"><th>&nbsp;</th><th colspan = "${columns}"><div style="font-weight:bold;font-style:italic;text-decoration : underline;"><fmt:message key="analysisParameters.test_samples.label"/></div></th></tr>
	  <tr class="FormData">
	    <th>&nbsp;</th>
	  	<c:forEach var="sample" items="${submittedSamplesList}">
	  		<th><c:out value="${sample.name}" /></th>
	  	</c:forEach>
	  </tr>	  
	  <tr class="FormData"><th><div style="font-weight:bold;font-style:italic;text-decoration : underline;"><fmt:message key="analysisParameters.control_samples.label"/></div></th><td colspan = "${columns}">&nbsp;</td></tr>  
	    <tr class="FormData">
	     <th><fmt:message key="analysisParameters.reference.label"/></th>
	     	<c:forEach var="isPaired" items="${controlIsReferenceList}">
	  			<c:choose>
	  				<c:when test="${isPaired == 'd'}">
	  					<td align="center">&nbsp;</td>
	  				</c:when>
	  				<c:when test="${isPaired == 'f'}">
	  					<td align="center"><input type="checkbox" DISABLED /></td>
	  				</c:when>
	  				<c:otherwise>
	  					<td align="center"><input type="checkbox" DISABLED checked="checked"/></td>
	  				</c:otherwise>
	  			</c:choose>
			</c:forEach>
	  	</tr>
	  </table>
	  </c:if>
</div>

<div>
  <c:if test="${not empty testIsReferenceList}">
  	<br /><br />
	  <c:set var="columns" value="${fn:length(submittedSamplesList)}" />
	  <h2 style="font-weight:bold;"><fmt:message key="analysisParameters.sample_pairs_reference.label"/>:</h2>
	  <table class="EditTable ui-widget ui-widget-content">
	  <tr class="FormData"><th>&nbsp;</th><th colspan = "${columns}"><div style="font-weight:bold;font-style:italic;text-decoration : underline;"><fmt:message key="analysisParameters.control_samples.label"/></div></th></tr>
	  <tr class="FormData">
	    <th>&nbsp;</th>
	  	<c:forEach var="sample" items="${submittedSamplesList}">
	  		<th><c:out value="${sample.name}" /></th>
	  	</c:forEach>
	  </tr>	  
	  <tr class="FormData"><th><div style="font-weight:bold;font-style:italic;text-decoration : underline;"><fmt:message key="analysisParameters.test_samples.label"/></div></th><td colspan = "${columns}">&nbsp;</td></tr>  
	    <tr class="FormData">
	     <th><fmt:message key="analysisParameters.reference.label"/></th>
	     	<c:forEach var="isPaired" items="${testIsReferenceList}">
	  			<c:choose>
	  				<c:when test="${isPaired == 'd'}">
	  					<td align="center">&nbsp;</td>
	  				</c:when>
	  				<c:when test="${isPaired == 'f'}">
	  					<td align="center"><input type="checkbox" DISABLED /></td>
	  				</c:when>
	  				<c:otherwise>
	  					<td align="center"><input type="checkbox" DISABLED checked="checked"/></td>
	  				</c:otherwise>
	  			</c:choose>
			</c:forEach>
	  	</tr>	 
	  </table>
  </c:if>
</div>