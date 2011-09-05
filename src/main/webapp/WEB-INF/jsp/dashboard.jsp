<%@ include file="/WEB-INF/jsp/include.jsp" %>
<div><font color="red"><wasp:message /></font></div>
<div><a href="<c:url value="/user/me.do"/>">My Profile</a></div>
<div><a href="<c:url value="/user/mypassword.do"/>">My Password</a></div>
<div><a href="<c:url value="/auth/reauth.do"/>">Refresh Auth</a></div>
<div><a href="<c:url value="/lab/newrequest.do"/>">Request Access to a Lab</a></div>


<sec:authorize access="hasRole('god')">
  <div>
  <h1>Super User Utils</h1>
  <div><a href="<c:url value="/user/list.do"/>">User Utils</a></div>
  <div><a href="<c:url value="/sysrole/list.do"/>">System Users</a></div>
  <div><a href="<c:url value="/department/list.do"/>">Department Utils</a></div>
  <div><a href="<c:url value="/lab/list.do"/>">- Lab Utils</a></div>
  <div><a href="<c:url value="/sample/list.do"/>">Sample Utils</a></div>
  <div><a href="<c:url value="/resource/list.do"/>">Resource Utils</a></div>
  <div><a href="<c:url value="/run/list.do"/>">- Run Utils</a></div>
  <div><a href="<c:url value="/task/list.do"/>">Task Utils</a></div>
  </div>
</sec:authorize>

<sec:authorize access="hasRole('du-*')">
  <div>
  <h1>Department Admin</h1>
  <c:forEach items="${departments}" var="d">
    <div>
    <b><c:out value="${d.name}" /></b>
    <c:set var="departmentId" value="${d.departmentId}" />

    <div><a href="<c:url value="/department/detail/${departmentId}.do"/>">Department Detail</a></div>
    <div><a href="<c:url value="/department/pendinglab/list/${departmentId}.do"/>">Pending Lab Approval</a></div>
    <div><a href="<c:url value="/task/daapproval/list/${departmentId}.do"/>">Pending Department Admin Job Approval</a></div>
  </c:forEach>
  </div>
</sec:authorize>



<sec:authorize access="hasRole('lu-*')">
  <div>
  <h1>Lab Utils</h1>
  <c:forEach items="${labs}" var="l">
    <div>
    <b><c:out value="${l.name}" /></b>
    <c:set var="labId" value="${l.labId}" />
    <div><a href="<c:url value="/lab/detail_ro/${labId}.do"/>">View</a></div>

    <sec:authorize access="hasRole('lm-${labId}' )">
      <div><a href="<c:url value="/lab/pendinguser/list/${labId}.do"/>">Pending User Approval</a></div>
      <div><a href="<c:url value="/lab/user/${labId}.do"/>">User Manager</a></div>
      <div><a href="<c:url value="/task/lmapproval/list/${labId}.do"/>">Pending Lab Manager Job Approval</a></div>
    </sec:authorize>

    </div>
  </c:forEach>

  </div>
</sec:authorize>


<sec:authorize access="hasRole('jv-*')">
  <div>
  <h1>Viewable Jobs</h1>
  <c:forEach items="${jobs}" var="j">
    <c:set var="jobId" value="${j.jobId}" />

    <div>
      <b><c:out value="${j.lab.name}" /></b> - 
      <a href="<c:url value="/job/detail/${jobId}.do"/>">
      <b><c:out value="${j.name}" /></b>
      </a>
    </div>
  </c:forEach>
</sec:authorize>

<sec:authorize access="hasRole('jd-*')">
  <div>
  <h1>Drafted Jobs</h1>
  <c:forEach items="${jobdrafts}" var="j">
    <c:set var="jobDraftId" value="${j.jobDraftId}" />

    <div>
      <b><c:out value="${j.lab.name}" /></b> - 
      <a href="<c:url value="/jobsubmit/verify/${jobDraftId}.do"/>">
      <b><c:out value="${j.name}" /></b>
      </a>
    </div>
  </c:forEach>
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

<sec:authorize access="hasRole('ft')">
  <div>
  <h1>Platform Unit</h1>
  <div><a href="<c:url value="/facility/platformunit/list.do"/>">List</a></div>
  <div><a href="<c:url value="/facility/platformunit/create.do"/>">Create</a></div>
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






