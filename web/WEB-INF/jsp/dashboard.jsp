<%@ include file="/WEB-INF/jsp/include.jsp" %>

<div><a href="<c:url value="/user/me.do"/>">My Profile</a></div>
<div><a href="<c:url value="/user/mypassword.do"/>">My Password</a></div>
<div><a href="<c:url value="/lab/request.do"/>">Request Access to a Lab</a></div>


<sec:authorize access="hasRole('god')">
  <div>
  <h1>Super User Utils</h1>
  <div><a href="<c:url value="/user/list.do"/>">User Utils</a></div>
  <div><a href="<c:url value="/department/list.do"/>">Department Utils</a></div>
  <div><a href="<c:url value="/lab/list.do"/>">- Lab Utils</a></div>
  <div><a href="<c:url value="/sample/list.do"/>">Sample Utils</a></div>
  <div><a href="<c:url value="/resource/list.do"/>">Resource Utils</a></div>
  <div><a href="<c:url value="/run/list.do"/>">- Run Utils</a></div>
  <div><a href="<c:url value="/task/list.do"/>">Task Utils</a></div>
  </div>
</sec:authorize>


<sec:authorize access="hasRole('lm-*')">
  <div>
  <h1>Lab Manager Utils</h1>
  <c:forEach items="${roles}" var="r">
    <c:if test="${fn:startsWith(r,'lm-')}">
      <c:if test="${r != 'lm-*'}">
        <c:set var="labId" value="${fn:substring(r,3,-1)}" />
  
        <div>
          <div>TODO LABNAME</div>
          <div><a href="<c:url value="/lab/pendinguser/list/${labId}.do"/>">Pending User Approval</a></div>
          <div><a href="<c:url value="/lab/user/${labId}.do"/>">User Manager</a></div>
          <div><a href="<c:url value="/task/lmapproval/list/${labId}.do"/>">Pending Lab Manager Job Approval</a></div>
        </div>
      </c:if>
    </c:if>
  </c:forEach>
  <div>
</sec:authorize>

<sec:authorize access="hasRole('da-*')">
  <div>
  <h1>Department Admin</h1>
  <c:forEach items="${roles}" var="r">
    <c:if test="${fn:startsWith(r,'da-')}">
      <c:if test="${r != 'da-*'}">
        TODO DEPARTMENTNAME
        <c:set var="departmentId" value="${fn:substring(r,3,-1)}" />
        <div><a href="<c:url value="/department/pendinglab/list/${departmentId}.do"/>">Pending Lab Approval</a></div>
        <div><a href="<c:url value="/task/daapproval/list/${departmentId}.do"/>">Pending Department Admin Job Approval</a></div>
      </c:if>
    </c:if>
  </c:forEach>
  </div>
</sec:authorize>


<sec:authorize access="hasRole('jv-*')">
  <div>
  <h1>Viewable Jobs</h1>
  <c:forEach items="${roles}" var="r">
    <c:if test="${fn:startsWith(r,'jv-')}">
      <c:if test="${r != 'jv-*'}">
        <c:set var="jobId" value="${fn:substring(r,3,-1)}" />
  
        <div><a href="<c:url value="/job/detail/${jobId}.do"/>">TODO JOB NAME</a></div>
      </c:if>
    </c:if>
  </c:forEach>
  <div>[Submit a Job]</div>
  </div>
</sec:authorize>

<sec:authorize access="hasRole('fm')">
  <div>
  <h1>Facility Manager Utils</h1>
  <div><a href="<c:url value="/task/fmrequote/list.do"/>">Requote Pending Jobs</a></div>
  <div><a href="<c:url value="/task/fmpayment/list.do"/>">Receive Payment for Jobs</a></div>
  </div>
</sec:authorize>

<sec:authorize access="hasRole('fm')">
  <div>
  <h1>Incoming Sample Manager</h1>
  <div><a href="<c:url value="/task/samplereceive/list.do"/>">Sample Receiver</a></div>
  </div>
</sec:authorize>

<hr>

<div>
<h1>Systems Admin Utils</h1>
<div>[View Task]</div>
<div>- [View Job Task]</div>
<div>- [View Job Task]</div>
<div>- [View Job Task]</div>
<div>- [View Job Task]</div>
<div>- [View Job Task]</div>
<div>[View Task]</div>
<div>- [View Job Task]</div>
<div>- [View Job Task]</div>
<div>- [View Job Task]</div>
<div>- [View Job Task]</div>
<div>[View Task]</div>
<div>- [View Job Task]</div>
<div>- [View Job Task]</div>
<div>[View Task]</div>
<div>- [View Job Task]</div>
<div>- [View Job Task]</div>
<div>- [View Job Task]</div>
<div>- [View Job Task]</div>
</div>


<div>[View Labs]</div>
<div> - Lab</div>
<div> - Lab</div>
<div> - Lab</div>
<div> - Lab</div>
<div> - Lab</div>
<div>[View Users]</div>
<div> - User</div>
<div> - User</div>
<div> - User</div>
<div> - User</div>
<div> - User</div>
<div>[Awaiting FM Approval]</div>
<div>- [View Job]</div>
<div>- [View Job]</div>
<div>- [View Job]</div>
<div>- [View Job]</div>
<div>- [View Job]</div>
<div>- [View Job]</div>
</div>


<div>
<h1>Facility Tech Utils</h1>
<h2>[Resource 1]</h2>
<div>[Custom Task 1]</div>
<div>- [Jobs Waiting]</div>
<div>- [Jobs Waiting]</div>
<div>- [Jobs Waiting]</div>
<div>- [Jobs Waiting]</div>
<div>- [Jobs Waiting]</div>
<div>- [Jobs Waiting]</div>
<div>[Custom Task 2]</div>
<div>- [Jobs Waiting]</div>
<div>- [Jobs Waiting]</div>
<div>- [Jobs Waiting]</div>
</div>






