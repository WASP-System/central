<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
      
<sec:authorize access="hasRole('su') or hasRole('lm-*') or hasRole('pi-*') or hasRole('fm-*') or hasRole('ft-*') or hasRole('ga-*')"> 
<h1><fmt:message key="pageTitle.task/piapprove/list.label"/></h1>
 
<c:choose>
    <c:when test="${newuserspendinglist.size()==0 && existinguserspendinglist.size()==0}">
    	<h2><fmt:message key="lmpendingtask.subtitle1_none.label" /></h2>
    </c:when>
    <c:otherwise>   
       <h2><fmt:message key="lmpendingtask.subtitle1.label" /></h2> 
	   <div id="accordion">
      		<c:forEach items="${newuserspendinglist}" var="up">
      		 	<h4><a href="#"><c:out value="${up.firstName} ${up.lastName}" /> (<fmt:message key="lmpendingtask.pi.label" />: <c:out value="${up.getLab().getUser().getNameFstLst()}" />) </a> </h4>     
            	<div> 
            	<fmt:message key="lmpendingtask.name.label" />: <c:out value="${up.firstName} ${up.lastName}" /><br />
            	<fmt:message key="lmpendingtask.email.label" />: <c:out value="${up.email}" /><br />
            	         	
      	      		<c:forEach items="${up.userPendingMeta}" var="meta"> 
        	 			<c:if test="${meta.k == 'userPending.building_room' || meta.k == 'userPending.address' || meta.k == 'userPending.phone'}">
 							<c:set var="optionName" value="${meta.k}.label" /><fmt:message key="${optionName}" />:&nbsp;<c:out value="${meta.v}" /><br />
        	 			</c:if>
      				</c:forEach>  
      				<br /><div class="submit"><a href="<wasp:relativeUrl value="lab/userpending/approve/${up.lab.labId}/${up.userPendingId}.do"/>"><fmt:message key="lmpendingtask.approve.label" /></a> <a href="<wasp:relativeUrl value="lab/userpending/reject/${up.lab.labId}/${up.userPendingId}.do"/>"><fmt:message key="lmpendingtask.reject.label" /></a></div>    
      			</div>       
     		</c:forEach>
     		<c:forEach items="${existinguserspendinglist}" var="lu">
     			<h4><a href="#"><c:out value="${lu.user.firstName} ${lu.user.lastName}" /> (<fmt:message key="lmpendingtask.pi.label" />: <c:out value="${lu.getLab().getUser().getNameFstLst()}" />) </a>  </h4> 
      		    <div> 
      		    <fmt:message key="lmpendingtask.name.label" />: <c:out value="${lu.user.firstName} ${lu.user.lastName}" /><br />
      		    <fmt:message key="lmpendingtask.email.label" />: <c:out value="${lu.user.email}" /><br />
      				<c:forEach items="${lu.user.userMeta}" var="meta"> 
      				   <c:if test="${meta.k == 'user.building_room' || meta.k == 'user.address' || meta.k == 'user.phone'}">
          			      <c:set var="optionName" value="${meta.k}.label" /><fmt:message key="${optionName}" />:&nbsp;<c:out value="${meta.v}" /><br />
            		   </c:if>
            		</c:forEach>
      				<br />  <div class="submit"><a href="<wasp:relativeUrl value="lab/labuserpending/approve/${lu.lab.labId}/${lu.labUserId}.do"/>"><fmt:message key="lmpendingtask.approve.label" /></a> <a href="<wasp:relativeUrl value="lab/labuserpending/reject/${lu.lab.labId}/${lu.labUserId}.do"/>"><fmt:message key="lmpendingtask.reject.label" /></a></div>
      			</div>      
    		</c:forEach> 
    	</div>   
    </c:otherwise>
</c:choose>
   
<c:choose>
    <c:when test="${jobspendinglist.size()==0}">
    	<h2><fmt:message key="jobapprovetask.subtitle2_none.label" /></h2>    
    </c:when>
    <c:otherwise>
    	<h2><fmt:message key="jobapprovetask.subtitle2.label" /></h2>  
		<div id="accordion2">         
    	<c:forEach items="${jobspendinglist}" var="job">      
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
      			<form action="<wasp:relativeUrl value="task/piJobApprove/${job.lab.labId}.do"/>" id="theForm<c:out value="${job.jobId}" />" method="POST" onsubmit="return validate(this);">
 				<input class="FormElement ui-widget-content ui-corner-all" type="hidden" name="jobId" value="${job.jobId}"> 
 				<input class="FormElement ui-widget-content ui-corner-all" type="radio" id = "action" name = "action" value = "APPROVED"><fmt:message key="jobapprovetask.approve.label" /> &nbsp;
 				<input class="FormElement ui-widget-content ui-corner-all" onclick='selectedFail("theForm<c:out value="${job.jobId}" />");' type="radio" id = "action" name = "action" value = "REJECTED"><fmt:message key="jobapprovetask.reject.label" /><br />
 				<textarea id="comment" name="comment" cols="25" rows="2"></textarea><br />
					<input class="FormElement ui-widget-content ui-corner-all" type="reset" value="<fmt:message key="jobapprovetask.reset.label" />">
				<input class="FormElement ui-widget-content ui-corner-all" type="submit" value="<fmt:message key="jobapprovetask.submit.label" />">
				</form>
      		</div>      
   		 </c:forEach>
    	</div> 
	</c:otherwise>
</c:choose>

</sec:authorize>   
    
    
  
