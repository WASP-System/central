<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
<br /><br />
      <font color="red"><wasp:message /></font>
      <sec:authorize access="hasRole('lm-${lab.labId}') or hasRole('god') or hasRole('ga-*')"> 
      
      <h2>PI/Lab Manager Pending Tasks</h2>
      <h3>Pending Users</h3>
      <c:choose>
    <c:when test="${newuserspendinglist.size()==0 && existinguserspendinglist.size()==0}">
    <div>None</div>    
    </c:when>
    <c:otherwise>    
    <c:forEach items="${newuserspendinglist}" var="up">
      <div>
      <c:out value="${up.firstName} ${up.lastName}" /> (<c:out value="${up.email}" />) <a href="<c:url value="/lab/userpending/approve/${lab.labId}/${up.userPendingId}.do"/>">APPROVE</a> <a href="<c:url value="/lab/userpending/reject/${lab.labId}/${up.userPendingId}.do"/>">REJECT</a>     
      </div>
    </c:forEach>
     <c:forEach items="${existinguserspendinglist}" var="lu">
      <div>
      <c:out value="${lu.user.firstName} ${lu.user.lastName}" /> (<c:out value="${lu.user.email}" />) <a href="<c:url value="/lab/labuserpending/approve/${lab.labId}/${lu.labUserId}.do"/>">APPROVE</a> <a href="<c:url value="/lab/labuserpending/reject/${lab.labId}/${lu.labUserId}.do"/>">REJECT</a>
      </div>
    </c:forEach>  
    </c:otherwise>
    </c:choose>
      <h3>Pending Jobs</h3>
        <c:choose>
    <c:when test="${jobspendinglist.size()==0}">
    <div>None</div>    
    </c:when>
    <c:otherwise>    
    <c:forEach items="${jobspendinglist}" var="job">
      <div>
      Job J<c:out value="${job.jobId}" />: <c:out value="${job.name}" /> (Submitter: <c:out value="${job.user.firstName} ${job.user.lastName}" />) <a href="<c:url value="/job/pendinglmapproval/approve/${lab.labId}/${job.jobId}.do"/>">APPROVE</a> <a href="<c:url value="/job/pendinglmapproval/reject/${lab.labId}/${job.jobId}.do"/>">REJECT</a>     
      </div>
      <!--  
      <c:forEach items="${job.jobMeta}" var="meta">     
      <c:out value="${meta.k}" />:&nbsp;<c:out value="${meta.v}" />
      <br />
    </c:forEach>-->      
    </c:forEach> 
    </c:otherwise>
    </c:choose>
  
    </sec:authorize> 
   
 <br />
 
<br />

<c:set var="counter" scope="page" value="1"/>

      <font color="red"><wasp:message /></font>
      <sec:authorize access="hasRole('lm-${lab.labId}') or hasRole('god') or hasRole('ga-*')"> 
      
      <h2>PI/Lab Manager Pending Tasks</h2>
      <h3>Pending Users</h3>
      <c:choose>
    <c:when test="${newuserspendinglist.size()==0 && existinguserspendinglist.size()==0}">
    <div>None</div>    
    </c:when>
    <c:otherwise>    
    <c:forEach items="${newuserspendinglist}" var="up">
      <div>
      <c:out value="${up.firstName} ${up.lastName}" /> (<c:out value="${up.email}" />) <input type="button" id="robbutton<c:out value='${counter}' />" onclick = "show_data(<c:out value='${counter}' />);" value="show" /> <a href="<c:url value="/lab/userpending/approve/${lab.labId}/${up.userPendingId}.do"/>">APPROVE</a> <a href="<c:url value="/lab/userpending/reject/${lab.labId}/${up.userPendingId}.do"/>">REJECT</a>     
      </div>
      
      
      
      <div id='robdiv<c:out value='${counter}' />' style="display:none">       
      <c:forEach items="${up.userPendingMeta}" var="meta">        
       <c:out value="${meta.k}" />:&nbsp;<c:out value="${meta.v}" /><br />      
      </c:forEach>       
      <br /><br />
      </div>      
	  <c:set var="counter" scope="page" value="${counter + 1}"/>
      
      
      
    </c:forEach>
     <c:forEach items="${existinguserspendinglist}" var="lu">
      <div>
      <c:out value="${lu.user.firstName} ${lu.user.lastName}" /> (<c:out value="${lu.user.email}" />) <input type="button" id="robbutton<c:out value='${counter}' />" onclick = "show_data(<c:out value='${counter}' />);" value="show" /> <a href="<c:url value="/lab/labuserpending/approve/${lab.labId}/${lu.labUserId}.do"/>">APPROVE</a> <a href="<c:url value="/lab/labuserpending/reject/${lab.labId}/${lu.labUserId}.do"/>">REJECT</a>
      </div>
      
      
       <div id='robdiv<c:out value='${counter}' />' style="display:none">       
      <c:forEach items="${lu.user.userMeta}" var="meta">        
       <c:out value="${meta.k}" />:&nbsp;<c:out value="${meta.v}" /><br />      
      </c:forEach>       
      <br /><br />
      </div>      
	  <c:set var="counter" scope="page" value="${counter + 1}"/>
      
      
    </c:forEach>  
    </c:otherwise>
    </c:choose>
      <h3>Pending Jobs</h3>
        <c:choose>
    <c:when test="${jobspendinglist.size()==0}">
    <div>None</div>    
    </c:when>
    <c:otherwise>    
    <c:forEach items="${jobspendinglist}" var="job">
      <div>
      Job J<c:out value="${job.jobId}" />: <c:out value="${job.name}" /> (Submitter: <c:out value="${job.user.firstName} ${job.user.lastName}" />) <input type="button" id="robbutton<c:out value='${counter}' />" onclick = "show_data(<c:out value='${counter}' />);" value="show" />  <a href="<c:url value="/job/pendinglmapproval/approve/${lab.labId}/${job.jobId}.do"/>">APPROVE</a> <a href="<c:url value="/job/pendinglmapproval/reject/${lab.labId}/${job.jobId}.do"/>">REJECT</a>     
      </div>
       
       <div id='robdiv<c:out value='${counter}' />' style="display:none">  
      <c:forEach items="${job.jobMeta}" var="meta">     
      <c:out value="${meta.k}" />:&nbsp;<c:out value="${meta.v}" /><br />
      </c:forEach>
      <br /><br />
      </div>
      <c:set var="counter" scope="page" value="${counter + 1}"/>
       
    </c:forEach> 
    </c:otherwise>
    </c:choose>
  
    </sec:authorize> 
  
  
  <br />



      <font color="red"><wasp:message /></font>
      <sec:authorize access="hasRole('lm-${lab.labId}') or hasRole('god') or hasRole('ga-*')"> 
      
      <h2>PI/Lab Manager Pending Tasks</h2>
      <h3>Pending Users</h3>
      <c:choose>
    <c:when test="${newuserspendinglist.size()==0 && existinguserspendinglist.size()==0}">
    <div>None</div>    
    </c:when>
    <c:otherwise>    
    
    <table>
    <c:forEach items="${newuserspendinglist}" var="up">
   
     <tr><td>
      <div>
      <c:out value="${up.firstName} ${up.lastName}" /> (<c:out value="${up.email}" />) </td><td> <button id="robbutton<c:out value='${counter}' />">show</button></td><td> <a href="<c:url value="/lab/userpending/approve/${lab.labId}/${up.userPendingId}.do"/>">APPROVE</a></td><td> <a href="<c:url value="/lab/userpending/reject/${lab.labId}/${up.userPendingId}.do"/>">REJECT</a>   
       </div> 
     </td></tr>  
      
      <tr><td colspan = '4'>
      <div id='robdiv<c:out value='${counter}' />' style="display:none"> 
            
      <c:forEach items="${up.userPendingMeta}" var="meta"> 
            
       <c:out value="${meta.k}" />:&nbsp;<c:out value="${meta.v}" /><br />     
      </c:forEach>       
      <br /><br />
      
      </div>      
	  <c:set var="counter" scope="page" value="${counter + 1}"/>
      </td></tr> 
      
      
    </c:forEach>
    
    
     <c:forEach items="${existinguserspendinglist}" var="lu">
    <tr><td>  <div>
      <c:out value="${lu.user.firstName} ${lu.user.lastName}" /> (<c:out value="${lu.user.email}" />) </td><td> <button id="robbutton<c:out value='${counter}' />">show</button></td><td> <a href="<c:url value="/lab/labuserpending/approve/${lab.labId}/${lu.labUserId}.do"/>">APPROVE</a> </td><td> <a href="<c:url value="/lab/labuserpending/reject/${lab.labId}/${lu.labUserId}.do"/>">REJECT</a> 
      </div>
    </td></tr>  
      <tr><td colspan = '4'>
       <div id='robdiv<c:out value='${counter}' />' style="display:none">       
      <c:forEach items="${lu.user.userMeta}" var="meta">        
       <c:out value="${meta.k}" />:&nbsp;<c:out value="${meta.v}" /><br />      
      </c:forEach>       
      <br /><br />
      </div>      
	  <c:set var="counter" scope="page" value="${counter + 1}"/>
      </td></tr>
      
    </c:forEach> 
    </table> 
    </c:otherwise>
    </c:choose>
      <h3>Pending Jobs</h3>
        <c:choose>
    <c:when test="${jobspendinglist.size()==0}">
    <div>None</div>    
    </c:when>
    <c:otherwise> 
    <table>   
    <c:forEach items="${jobspendinglist}" var="job">
    <tr><td>
      <div>
      Job J<c:out value="${job.jobId}" />: <c:out value="${job.name}" /> (Submitter: <c:out value="${job.user.firstName} ${job.user.lastName}" />) </td><td> <button id="robbutton<c:out value='${counter}' />">show</button> <a href="<c:url value="/job/pendinglmapproval/approve/${lab.labId}/${job.jobId}.do"/>">APPROVE</a> <a href="<c:url value="/job/pendinglmapproval/reject/${lab.labId}/${job.jobId}.do"/>">REJECT</a> 
      </div>
     </td></tr>  
     <tr><td colspan = '2'>
       <div id='robdiv<c:out value='${counter}' />' style="display:none">
         
      <c:forEach items="${job.jobMeta}" var="meta">     
      <c:out value="${meta.k}" />:&nbsp;<c:out value="${meta.v}" /><br />
      </c:forEach>
      <br /><br />
      </div>
      <c:set var="counter" scope="page" value="${counter + 1}"/>
       </td></tr>
    </c:forEach>
    </table> 
    </c:otherwise>
    </c:choose>
  
    </sec:authorize>   
  
   
 <br />
      <font color="red"><wasp:message /></font>
      <sec:authorize access="hasRole('lm-${lab.labId}') or hasRole('god') or hasRole('ga-*')"> 
      <!-- hasRole('lm-${lab.labId}') -->
      <h2>PI/Lab Manager Pending Tasks</h2>
      <h3>Pending Users</h3>
      
<div id="accordion">
      
      <c:choose>
    <c:when test="${newuserspendinglist.size()==0 && existinguserspendinglist.size()==0}">
    None<br />    
    </c:when>
    <c:otherwise>  
    <c:forEach items="${newuserspendinglist}" var="up">
      
      <h4><a href="#"><c:out value="${up.firstName} ${up.lastName}" /> (<c:out value="${up.email}" />) </a> </h4> <!--a href="<c:url value="/lab/userpending/approve/${lab.labId}/${up.userPendingId}.do"/>">APPROVE</a> <a href="<c:url value="/lab/userpending/reject/${lab.labId}/${up.userPendingId}.do"/>">REJECT</a-->    
      
      <div> 
      
      <c:forEach items="${up.userPendingMeta}" var="meta"> 
        
      <c:out value="${meta.k}" />:&nbsp;<c:out value="${meta.v}" /><br />
      
      </c:forEach>  
      
      <br /><a href="<c:url value="/lab/userpending/approve/${lab.labId}/${up.userPendingId}.do"/>">APPROVE</a> <a href="<c:url value="/lab/userpending/reject/${lab.labId}/${up.userPendingId}.do"/>">REJECT</a>   
      
      </div> 
      
     </c:forEach>
     
   <c:forEach items="${existinguserspendinglist}" var="lu">
     
      <h4><a href="#"><c:out value="${lu.user.firstName} ${lu.user.lastName}" /> (<c:out value="${lu.user.email}" />)</a>  </h4> <!-- a href="<c:url value="/lab/labuserpending/approve/${lab.labId}/${lu.labUserId}.do"/>">APPROVE</a> <a href="<c:url value="/lab/labuserpending/reject/${lab.labId}/${lu.labUserId}.do"/>">REJECT</a-->
      
       <div> 
      
      <c:forEach items="${lu.user.userMeta}" var="meta"> 
         
      <c:out value="${meta.k}" />:&nbsp;<c:out value="${meta.v}" /><br />
      
      </c:forEach>
      <br />  <a href="<c:url value="/lab/labuserpending/approve/${lab.labId}/${lu.labUserId}.do"/>">APPROVE</a> <a href="<c:url value="/lab/labuserpending/reject/${lab.labId}/${lu.labUserId}.do"/>">REJECT</a>
      </div>
      
      
    </c:forEach>  
    </c:otherwise>
    </c:choose>

 </div>   
        
      <h3>Pending Jobs</h3>
<div id="accordion2">
        <c:choose>
    <c:when test="${jobspendinglist.size()==0}">
    None<br />    
    </c:when>
    <c:otherwise>    
    <c:forEach items="${jobspendinglist}" var="job">
      
      <h4><a href="#">Job <c:out value="${job.jobId}" />: <c:out value="${job.name}" /> (Submitter: <c:out value="${job.user.firstName} ${job.user.lastName}" />)</a></h4><!-- a href="<c:url value="/job/pendinglmapproval/approve/${lab.labId}/${job.jobId}.do"/>">APPROVE</a> <a href="<c:url value="/job/pendinglmapproval/reject/${lab.labId}/${job.jobId}.do"/>">REJECT</a-->     
      
      <div>
      <c:forEach items="${job.jobMeta}" var="meta">     
      <c:out value="${meta.k}" />:&nbsp;<c:out value="${meta.v}" /><br />
    </c:forEach>
      <br /> <a href="<c:url value="/job/pendinglmapproval/approve/${lab.labId}/${job.jobId}.do"/>">APPROVE</a> <a href="<c:url value="/job/pendinglmapproval/reject/${lab.labId}/${job.jobId}.do"/>">REJECT</a>     
      </div>
      
    </c:forEach> 
    </c:otherwise>
    </c:choose>
</div>
    </sec:authorize>   
    
    
    <br /><br />
<table border="1">
<tr><td colspan="3">Enter Your Information</td></tr>
<tr><td>Email</td><td><input type="text" size="24"/></td>
<td><input type="button" class="delRow" value="Delete Row"/></td></tr>
<tr><td>Email</td><td><input type="text" size="24"/></td>
<td><input type="button" class="delRow" value="Delete Row"/></td></tr>
<tr><td colspan="3" align="right">
<input type="button" class="addRow" value="Add Row"/></td></tr>
</table>
<script type="text/javascript" src="/wasp/scripts/jquery/jquery.table.addrow.js"></script>
<script type="text/javascript">
(function($){
	$(document).ready(function(){
		$(".addRow").btnAddRow();
		$(".delRow").btnDelRow();
});
})(jQuery);
</script>