<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
      
<sec:authorize access="hasRole('su') or hasRole('lm-*}') or hasRole('pi-*') or hasRole('fm-*') or hasRole('ft-*') or hasRole('ga-*')"> 
<h1><fmt:message key="pageTitle.lab/pendinglmapproval/list.label"/></h1>
 
<c:choose>
    <c:when test="${newuserspendinglist.size()==0 && existinguserspendinglist.size()==0}">
    	<h2><fmt:message key="lmpendingtask.subtitle1_none.label" /></h2>
    </c:when>
    <c:otherwise>   
       <h2><fmt:message key="lmpendingtask.subtitle1.label" /></h2> 
	   <div id="accordion">
      		<c:forEach items="${newuserspendinglist}" var="up">
      		 	<h4><a href="#"><c:out value="${up.firstName} ${up.lastName}" /> (PI: <c:out value="${up.getLab().getUser().getNameFstLst()}" />) </a> </h4> <!--a href="<c:url value="/lab/userpending/approve/${lab.labId}/${up.userPendingId}.do"/>">APPROVE</a> <a href="<c:url value="/lab/userpending/reject/${lab.labId}/${up.userPendingId}.do"/>">REJECT</a-->    
            	<div> 
            	Name: <c:out value="${up.firstName} ${up.lastName}" /><br />
            	Email: <c:out value="${up.email}" /><br />
            	         	
      	      		<c:forEach items="${up.userPendingMeta}" var="meta"> 
        	 			<c:if test="${meta.k == 'userPending.building_room' || meta.k == 'userPending.address' || meta.k == 'userPending.phone'}">
 							<c:set var="optionName" value="${meta.k}.label" /><fmt:message key="${optionName}" />:&nbsp;<c:out value="${meta.v}" /><br />
        	 			</c:if>
      				</c:forEach>  
      				<br /><div class="submit"><a href="<c:url value="/lab/userpending/approve/${up.lab.labId}/${up.userPendingId}.do"/>">APPROVE</a> <a href="<c:url value="/lab/userpending/reject/${up.lab.labId}/${up.userPendingId}.do"/>">REJECT</a></div>    
      			</div>       
     		</c:forEach>
     		<c:forEach items="${existinguserspendinglist}" var="lu">
     			<h4><a href="#"><c:out value="${lu.user.firstName} ${lu.user.lastName}" /> (PI: <c:out value="${lu.getLab().getUser().getNameFstLst()}" />) </a>  </h4> <!-- a href="<c:url value="/lab/labuserpending/approve/${lab.labId}/${lu.labUserId}.do"/>">APPROVE</a> <a href="<c:url value="/lab/labuserpending/reject/${lab.labId}/${lu.labUserId}.do"/>">REJECT</a-->
      		    <div> 
      		    Name: <c:out value="${lu.user.firstName} ${lu.user.lastName}" /><br />
      		    Email: <c:out value="${lu.user.email}" /><br />
      				<c:forEach items="${lu.user.userMeta}" var="meta"> 
      				   <c:if test="${meta.k == 'user.building_room' || meta.k == 'user.address' || meta.k == 'user.phone'}">
          			      <c:set var="optionName" value="${meta.k}.label" /><fmt:message key="${optionName}" />:&nbsp;<c:out value="${meta.v}" /><br />
            		   </c:if>
            		</c:forEach>
      				<br />  <div class="submit"><a href="<c:url value="/lab/labuserpending/approve/${lu.lab.labId}/${lu.labUserId}.do"/>">APPROVE</a> <a href="<c:url value="/lab/labuserpending/reject/${lu.lab.labId}/${lu.labUserId}.do"/>">REJECT</a></div>
      			</div>      
    		</c:forEach> 
    	</div>   
    </c:otherwise>
</c:choose>
   
<c:choose>
    <c:when test="${jobspendinglist.size()==0}">
    	<h2><fmt:message key="lmpendingtask.subtitle2_none.label" /></h2>    
    </c:when>
    <c:otherwise>
    	<h2><fmt:message key="lmpendingtask.subtitle2.label" /></h2>  
		<div id="accordion2">         
    	<c:forEach items="${jobspendinglist}" var="job">      
      		<h4><a href="#">Job ID J<c:out value="${job.jobId}" /> (Submitter: <c:out value="${job.getUser().getNameFstLst()}" />; PI: <c:out value="${job.getLab().getUser().getNameFstLst()}" />)</a></h4>
      		<div>
      			Job ID: J<c:out value="${job.jobId}" /><br />
      			Job Name: <c:out value="${job.getName()}" /><br />
      			Submitter: <c:out value="${job.getUser().getNameFstLst()}" /><br />
      			PI: <c:out value="${job.getLab().getUser().getNameFstLst()}" /><br />
      			<c:set var="extraJobDetailsMap" value="${jobExtraJobDetailsMap.get(job)}" />
      			<c:forEach items="${extraJobDetailsMap}" var="detail">
      				<c:out value="${detail.getKey()}" />: <c:out value="${detail.getValue()}" /><br />
      			</c:forEach>      			
    			Workflow: <c:out value="${job.getWorkflow().getName()}" /><br />      			
      			<c:set var="submittedSamplesList" value="${jobSubmittedSamplesMap.get(job)}" />
      			Samples [<c:out value="${submittedSamplesList.size()}" />]:<br />	
      			<c:forEach items="${submittedSamplesList}" var="sample">
      				--<c:out value="${sample.getName()}" /> (<c:out value="${sample.getSampleType().getName()}" />, <c:out value="${sampleSpeciesMap.get(sample)}" />)<br />      				
      			</c:forEach>
      			
      			<br /> <div class="submit"><a href="<c:url value="/job/pendinglmapproval/approve/${job.lab.labId}/${job.jobId}.do"/>">APPROVE</a> <a href="<c:url value="/job/pendinglmapproval/reject/${job.lab.labId}/${job.jobId}.do"/>">REJECT</a></div>    
      		</div>      
   		 </c:forEach>
    	</div> 
	</c:otherwise>
</c:choose>

</sec:authorize>   
    
    
  
