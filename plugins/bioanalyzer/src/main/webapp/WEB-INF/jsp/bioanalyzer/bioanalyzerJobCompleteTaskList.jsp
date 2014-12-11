<%@ page session="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="wasp" uri="http://einstein.yu.edu/wasp" %>
<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<script>	
	$(document).ready(function() {
	    $("#accordion").accordion({
			collapsible: true,
			autoHeight: false,
			navigation: true,
			active: false,
			header: 'h4'			
		});
	  });
</script>

<%--  TODO: Declare style in css file (e.g. /src/main/webapp/css/base.css), not in .jsp and reuse where possible !!!! --%>

<c:import url="/WEB-INF/jsp/bioanalyzer/fadingMessage.jsp" />

<sec:authorize access="hasRole('su') or hasRole('fm-*') or hasRole('ft-*') or hasRole('ga-*')"> 

<h1><fmt:message key="pageTitle.bioanalyzer/bioanalyzerJobCompleteTaskList.label" /></h1>

<c:choose>
    <c:when test="${bioanalyzerJobsReadyToBeMarkedAsCompleted.size()==0}">
    	<h2><fmt:message key="bioanalyzer.bioanalyzerJobCompleteTaskList_noJobsReadyForCompletion.label" /></h2>    
    </c:when>
    <c:otherwise>
    	<h2><fmt:message key="bioanalyzer.bioanalyzerJobCompleteTaskList_jobsReadyForCompletion.label" /></h2>  
		<div id="accordion">         
    	<c:forEach items="${bioanalyzerJobsReadyToBeMarkedAsCompleted}" var="job">      
      		<h4><a href="#"><fmt:message key="jobapprovetask.jobId.label" />: J<c:out value="${job.jobId}" /> (<fmt:message key="jobapprovetask.submitter.label" />: <c:out value="${job.getUser().getNameFstLst()}" />; <fmt:message key="jobapprovetask.pi.label" />: <c:out value="${job.getLab().getUser().getNameFstLst()}" />)</a></h4>
      		<div>
      			<fmt:message key="jobapprovetask.jobId.label" />: J<c:out value="${job.jobId}" /><br />
      			<fmt:message key="jobapprovetask.jobName.label" />: <c:out value="${job.getName()}" /><br />
      			<fmt:message key="jobapprovetask.submitter.label" />: <c:out value="${job.getUser().getNameFstLst()}" /><br />
      			<fmt:message key="jobapprovetask.pi.label" />: <c:out value="${job.getLab().getUser().getNameFstLst()}" /><br />
      			<fmt:message key="jobapprovetask.workflow.label" />: <c:out value="${job.getWorkflow().getName()}" /><br />      			     			
      			<c:set var="extraJobDetailsMap" value="${jobExtraJobDetailsMap.get(job)}" />
      			<c:forEach items="${extraJobDetailsMap.keySet()}" var="detailKey">
      				<fmt:message key="${detailKey}" />: <c:out value="${extraJobDetailsMap.get(detailKey)}" /><br />
      			</c:forEach> 
      			<c:set var="approvalsMap" value="${jobApprovalsMap.get(job)}" />
      			<c:forEach items="${approvalsMap.keySet()}" var="detailKey2">
       				<fmt:message key="status.${detailKey2}.label" />: <fmt:message key="status.${approvalsMap.get(detailKey2)}.label" /><br />
      			</c:forEach>     			
      			<c:set var="submittedSamplesList" value="${jobSubmittedSamplesMap.get(job)}" />
      			<fmt:message key="jobapprovetask.samples.label" /> [<c:out value="${submittedSamplesList.size()}" />]:<br />	
      			<c:forEach items="${submittedSamplesList}" var="sample">
      				--<c:out value="${sample.getName()}" /> (<c:out value="${sample.getSampleType().getName()}" />, <c:out value="${sampleSpeciesMap.get(sample)}" />)<br />      				
      			</c:forEach>
      			<br />
      			<fmt:message key="bioanalyzer.bioanalyzerJobCompleteTaskList_facilityGeneratedBioanalzyerFileNames.label" /> [<c:out value="${bioanalyzerFileHandleList.size()}" />]:<br />
      			<c:forEach items="${bioanalyzerFileHandleList}" var="bioanalyzerFileHandle">
      				--<c:out value="${bioanalyzerFileHandle.getFileName()}" /> <br />      				
      			</c:forEach>   			
      			<br />
      			<form action="<wasp:relativeUrl value="bioanalyzer/task/${job.getId()}/markBioanalyzerJobAsCompletedTask.do"/>" method="POST" onsubmit='var ret = confirm("<fmt:message key="bioanalyzer.bioanalyzerJobCompleteTaskList_confirmMarkJobAsCompletedTask.label" />"); if(ret){openWaitDialog();} return ret;'>
 				<input class="FormElement ui-widget-content ui-corner-all" type="submit" value="<fmt:message key="bioanalyzer.bioanalyzerJobCompleteTaskList_markJobAsCompletedTask.label" />">
				</form>
				
				
				
				   
      		</div>      
   		 </c:forEach>
    	</div> 
	</c:otherwise>
</c:choose>

</sec:authorize>   
    
    