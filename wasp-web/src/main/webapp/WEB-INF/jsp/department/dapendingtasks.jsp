<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<sec:authorize access="hasRole('da-*') or hasRole('su') or hasRole('ga-*') or hasRole('ft-*') or hasRole('fm-*')">
<title><fmt:message key="dapendingtask.title.label"/></title>
<h1><fmt:message key="dapendingtask.title.label"/></h1>

 <c:choose>
    <c:when test="${labspendinglist.size()==0}">
    	<h2><fmt:message key="dapendingtask.subtitle1_none.label" /></h2>
    </c:when>
    <c:otherwise>   
       <h2><fmt:message key="dapendingtask.subtitle1.label" /></h2> 
	   <div id="accordion">
      		<c:forEach items="${labspendinglist}" var="labPending">
      			<c:choose>
      			<c:when test="${labPending.getUserpendingId()==NULL}" > <%-- an already registered user requesting a new lab with him/her-self as PI, so use attribute labpending.primaryuserid --%>
      				<c:set var="piPending" value="${labPending.getUser()}" /> 
      			</c:when>
      			<c:otherwise>
      				<c:set var="piPending" value="${labPending.getUserPending()}" />
      			</c:otherwise>
      			</c:choose>
      		 	<h4><a href="#"><fmt:message key="dapendingtask.newPI.label"/>: <c:out value="${piPending.getFirstName()}" /> <c:out value="${piPending.getLastName()}" /> (<fmt:message key="dapendingtask.email.label"/>: <c:out value="${piPending.getEmail()}" />)</a> </h4>     
            	<div> 
            		<fmt:message key="dapendingtask.pi.label"/>: <c:out value="${piPending.getFirstName()}" /> <c:out value="${piPending.getLastName()}" /><br />
            		<fmt:message key="dapendingtask.email.label"/>: <c:out value="${piPending.getEmail()}" /><br /> 
            		<fmt:message key="dapendingtask.department.label"/>: <c:out value="${labPending.getDepartment().getName()}" /><br />          		
            		<c:set var="departmentId" value="0" />             		
            		<c:choose>
      					<c:when test="${labPending.getUserpendingId()==NULL}" > <%-- an already registered user requesting a new lab with him/her-self as PI, so use attribute labpending.primaryuserid --%>
      						<c:set var="metaList" value="${piPending.getUserMeta()}" /> 
      					</c:when>
      					<c:otherwise>
      						<c:set var="metaList" value="${piPending.getUserPendingMeta()}" />
      					</c:otherwise>
      				</c:choose>
            		<%-- 	<c:forEach items="${piPending.getUserPendingMeta()}" var="meta">     --%>  	
      	      	    <c:forEach items="${metaList}" var="meta">
        	 			<c:if test="${fn:contains(meta.k, 'institution') || fn:contains(meta.k, 'building_room') || fn:contains(meta.k, 'address') || fn:contains(meta.k, 'city') || fn:contains(meta.k, 'state') || fn:contains(meta.k, 'country') || fn:contains(meta.k, 'phone')}">
 							<c:set var="optionName" value="${meta.k}.label" /><fmt:message key="${optionName}" />:&nbsp;<c:out value="${meta.v}" /><br />
        	 			</c:if>
      				</c:forEach>  
      				<br /><div class="submit"><a href="<c:url value="/lab/pending/approve/${labPending.getDepartmentId()}/${labPending.getLabPendingId()}.do"/>"><fmt:message key="dapendingtask.approve.label"/></a> <a href="<c:url value="/lab/pending/reject/${labPending.getDepartmentId()}/${labPending.getLabPendingId()}.do"/>"><fmt:message key="dapendingtask.reject.label"/></a></div>    
      			</div>       
     		</c:forEach>     		
    	</div>   
    </c:otherwise>
</c:choose> 
    
<c:choose>
    <c:when test="${jobspendinglist.size()==0}">
    	<h2><fmt:message key="dapendingtask.subtitle2_none.label" /></h2>    
    </c:when>
    <c:otherwise>
    	<h2><fmt:message key="dapendingtask.subtitle2.label" /></h2>  
		<div id="accordion2">         
    	<c:forEach items="${jobspendinglist}" var="job">      
      		<h4><a href="#"><fmt:message key="dapendingtask.jobID.label"/>: J<c:out value="${job.jobId}" /> (<fmt:message key="dapendingtask.submitter.label"/>: <c:out value="${job.getUser().getNameFstLst()}" />; <fmt:message key="dapendingtask.pi.label"/>: <c:out value="${job.getLab().getUser().getNameFstLst()}" />)</a></h4>
      		<div>
      			<fmt:message key="dapendingtask.jobID.label"/>: J<c:out value="${job.jobId}" /><br />
      			<fmt:message key="dapendingtask.jobName.label"/>: <c:out value="${job.getName()}" /><br />
      			<fmt:message key="dapendingtask.submitter.label"/>: <c:out value="${job.getUser().getNameFstLst()}" /><br />
      			<fmt:message key="dapendingtask.pi.label"/>: <c:out value="${job.getLab().getUser().getNameFstLst()}" /><br />
      			<fmt:message key="dapendingtask.workflow.label"/>: <c:out value="${job.getWorkflow().getName()}" /><br />      			
      			<c:set var="extraJobDetailsMap" value="${jobExtraJobDetailsMap.get(job)}" />
      			<c:forEach items="${extraJobDetailsMap.keySet()}" var="detailKey">
      				<fmt:message key="${detailKey}" />: <c:out value="${extraJobDetailsMap.get(detailKey)}" /><br />
      			</c:forEach> 
      			<c:set var="approvalsMap" value="${jobApprovalsMap.get(job)}" />
      			<c:forEach items="${approvalsMap.keySet()}" var="detailKey2">
       				<fmt:message key="status.${detailKey2}.label" />: <fmt:message key="status.${approvalsMap.get(detailKey2)}.label" /><br />
      			</c:forEach>     			
      			<c:set var="submittedSamplesList" value="${jobSubmittedSamplesMap.get(job)}" />
      			<fmt:message key="dapendingtask.samples.label"/> [<c:out value="${submittedSamplesList.size()}" />]:<br />	
      			<c:forEach items="${submittedSamplesList}" var="sample">
      				--<c:out value="${sample.getName()}" /> (<c:out value="${sample.getSampleType().getName()}" />, <c:out value="${sampleSpeciesMap.get(sample)}" />)<br />      				
      			</c:forEach>
      			
      			<%-- <br /> <div class="submit"><a href="<c:url value="/job/pendingdaapproval/approve/${job.lab.departmentId}/${job.jobId}.do"/>"><fmt:message key="dapendingtask.approve.label"/></a> <a href="<c:url value="/job/pendingdaapproval/reject/${job.lab.departmentId}/${job.jobId}.do"/>"><fmt:message key="dapendingtask.reject.label"/></a></div>  --%>
      			
      			<br />
      			<form action="<c:url value="/task/daJobApprove/${job.lab.departmentId}.do"/>" id="theForm<c:out value="${job.jobId}" />" method="POST" onsubmit="return validate(this);">
 				<input class="FormElement ui-widget-content ui-corner-all" type="hidden" name="jobId" value="${job.jobId}"> 
 				<input class="FormElement ui-widget-content ui-corner-all" type="radio" id = "action" name = "action" value = "APPROVED"><fmt:message key="dapendingtask.approve.label" /> &nbsp;
 				<input class="FormElement ui-widget-content ui-corner-all" onclick='selectedFail("theForm<c:out value="${job.jobId}" />");' type="radio" id = "action" name = "action" value = "REJECTED"><fmt:message key="dapendingtask.reject.label" /><br />
 				<textarea id="comment" name="comment" cols="25" rows="2"></textarea><br />
					<input class="FormElement ui-widget-content ui-corner-all" type="reset" value="<fmt:message key="dapendingtask.reset.label" />">
				<input class="FormElement ui-widget-content ui-corner-all" type="submit" value="<fmt:message key="dapendingtask.submit.label" />">
				</form>  
      		</div>      
   		 </c:forEach>
    	</div> 
	</c:otherwise>
</c:choose>
</sec:authorize>
