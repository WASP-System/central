<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
      
<sec:authorize access="hasRole('su') or hasRole('lm-*') or hasRole('pi-*') or hasRole('fm-*') or hasRole('ft-*') or hasRole('ga-*')"> 
<h1><fmt:message key="pageTitle.task/fmapprove/list.label"/></h1>
 
<c:choose>
    <c:when test="${jobspendinglist.size()==0}">
    	<h2><fmt:message key="lmpendingtask.subtitle2_none.label" /></h2>    
    </c:when>
    <c:otherwise>
    	<h2><fmt:message key="lmpendingtask.subtitle2.label" /></h2>  
		<div id="accordion2">         
    	<c:forEach items="${jobspendinglist}" var="job">      
      		<h4><a href="#"><fmt:message key="lmpendingtask.jobId.label" />: J<c:out value="${job.jobId}" /> (<fmt:message key="lmpendingtask.submitter.label" />: <c:out value="${job.getUser().getNameFstLst()}" />; <fmt:message key="lmpendingtask.pi.label" />: <c:out value="${job.getLab().getUser().getNameFstLst()}" />)</a></h4>
      		<div>
      			<fmt:message key="lmpendingtask.jobId.label" />: J<c:out value="${job.jobId}" /><br />
      			<fmt:message key="lmpendingtask.jobName.label" />: <c:out value="${job.getName()}" /><br />
      			<fmt:message key="lmpendingtask.submitter.label" />: <c:out value="${job.getUser().getNameFstLst()}" /><br />
      			<fmt:message key="lmpendingtask.pi.label" />: <c:out value="${job.getLab().getUser().getNameFstLst()}" /><br />
      			<fmt:message key="lmpendingtask.workflow.label" />: <c:out value="${job.getWorkflow().getName()}" /><br />      			     			
      			<c:set var="extraJobDetailsMap" value="${jobExtraJobDetailsMap.get(job)}" />
      			<c:forEach items="${extraJobDetailsMap.keySet()}" var="detailKey">
      				<fmt:message key="${detailKey}" />: <c:out value="${extraJobDetailsMap.get(detailKey)}" /><br />
      			</c:forEach> 
      			<c:set var="approvalsMap" value="${jobApprovalsMap.get(job)}" />
      			<c:forEach items="${approvalsMap.keySet()}" var="detailKey2">
       				<fmt:message key="status.${detailKey2}.label" />: <fmt:message key="status.${approvalsMap.get(detailKey2)}.label" /><br />
      			</c:forEach>     			
      			<c:set var="submittedSamplesList" value="${jobSubmittedSamplesMap.get(job)}" />
      			<fmt:message key="lmpendingtask.samples.label" /> [<c:out value="${submittedSamplesList.size()}" />]:<br />	
      			<c:forEach items="${submittedSamplesList}" var="sample">
      				--<c:out value="${sample.getName()}" /> (<c:out value="${sample.getSampleType().getName()}" />, <c:out value="${sampleSpeciesMap.get(sample)}" />)<br />      				
      			</c:forEach>
      			
      			<%-- <br /> <div class="submit"><a href="<c:url value="/job/pendinglmapproval/approve/${job.lab.labId}/${job.jobId}.do"/>"><fmt:message key="lmpendingtask.approve.label" /></a> <a href="<c:url value="/job/pendinglmapproval/reject/${job.lab.labId}/${job.jobId}.do"/>"><fmt:message key="lmpendingtask.reject.label" /></a></div> --%>
      			
      			
      			<br />
      			<form action="<c:url value="/task/fmJobApprove.do"/>" id="theForm<c:out value="${job.jobId}" />" method="POST" onsubmit="return validate(this);">
 				<input class="FormElement ui-widget-content ui-corner-all" type="hidden" name="jobId" value="${job.jobId}"> 
 				<input class="FormElement ui-widget-content ui-corner-all" type="radio" id = "action" name = "action" value = "APPROVED"><fmt:message key="lmpendingtask.approve.label" /> &nbsp;
 				<input class="FormElement ui-widget-content ui-corner-all" onclick='selectedFail("theForm<c:out value="${job.jobId}" />");' type="radio" id = "action" name = "action" value = "REJECTED"><fmt:message key="lmpendingtask.reject.label" /><br />
 				<textarea id="comment" name="comment" cols="25" rows="2"></textarea><br />
					<input class="FormElement ui-widget-content ui-corner-all" type="reset" value="<fmt:message key="lmpendingtask.reset.label" />">
				<input class="FormElement ui-widget-content ui-corner-all" type="submit" value="<fmt:message key="lmpendingtask.submit.label" />">
				</form>
				
				
				
				   
      		</div>      
   		 </c:forEach>
    	</div> 
	</c:otherwise>
</c:choose>

</sec:authorize>   
    
    
  
