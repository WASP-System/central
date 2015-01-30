<%@ page session="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="wasp" uri="http://einstein.yu.edu/wasp" %>
<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

	<div>
		<c:if test="${not empty controlList}">
			<h2 style="font-weight:bold"><fmt:message key="listJobSamples.samplePairingRequested.label"/>:</h2>		
			<table class="data">
				<tr class="FormData">
					<td class="label-centered" style="background-color:#FAF2D6"><fmt:message key="chipseq.chipseqInput.label"/></td>
					<td class="label-centered" style="background-color:#FAF2D6"><fmt:message key="chipseq.chipseqIP.label"/></td>
				</tr>
			<c:forEach var="controlSample" items="${controlList}">
				<tr class="FormData">
					<td class="value-centered" ><c:out value="${controlSample.getName()}" /></td>   	  				
				 	<c:set var="testList" value="${samplePairsMap.get(controlSample)}" />
				 	<td class="value-centered" >
				 	<c:forEach var="testSample" items="${testList}">
				 		<c:out value="${testSample.getName()}" /><br />
				 	</c:forEach>
				 	</td>
				 </tr>
			</c:forEach>
			</table>
		</c:if>
	</div>