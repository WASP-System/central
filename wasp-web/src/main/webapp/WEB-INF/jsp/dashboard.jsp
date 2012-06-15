<%@ include file="/WEB-INF/jsp/include.jsp"%>
<%@ include file="/WEB-INF/jsp/taglib.jsp"%>

<div>
	<h1>Dashboard - ${me.firstName} ${me.lastName}</h1>
</div>
<div id="tabs">
	<ul>
		<li><a href="#tabs-home">My Account</a></li>
		<sec:authorize access="hasRole('su')">
			<li><a href="#tabs-suUtils">Superuser Utils</a></li>
		</sec:authorize>
		<sec:authorize access="hasRole('da-*') or hasRole('su') or hasRole('ga')">
			<li><a href="#tabs-daAdmin">Dept Admin</a></li>
		</sec:authorize>
		<sec:authorize access="hasRole('da-${labs[0].departmentId}') or hasRole('su') or hasRole('lu-*') or hasRole('ft-*') or hasRole('ga') or hasRole('fm')"> 
			<li><a href="#tabs-labUtils">Lab Utils</a></li>
		</sec:authorize>
		<sec:authorize
			access="hasRole('jv-*') or hasRole('jd-*') or hasRole('su') or hasRole('ga') or hasRole('lu-*') or hasRole('fm') or hasRole('ft')">
			<li><a href="#tabs-jobUtils">Job Utils</a></li>
		</sec:authorize>
		<sec:authorize access="hasRole('fm') or hasRole('ft')">
			<li><a href="#tabs-facilityUtils">Facility Utils</a></li>
		</sec:authorize>
		<%-- <li><a href="#tabs-taskList">Tasks (<span class="taskAlert"><c:out value="${fn:length(tasks)}" /></span>)</a></li> --%>
		<li><a href="#tabs-taskList">Tasks (<span class="taskAlert"><c:out value="${totalNumberOfTypesOfTasks}" /></span>)</a></li>
	</ul>
	<div id="tabs-home">
		<ul class="navTabs">
			<li>
				<a href='<c:url value="/user/me_ro.do"/>'>My Profile</a>
			</li>
			<sec:authorize access="not hasRole('ldap')">
				<li>
					<a href='<c:url value="/user/mypassword.do"/>'>My Password</a>
				</li>
			</sec:authorize>
			<li>
				<a href='<c:url value="/auth/reauth.do"/>'>Refresh Auth</a>
			</li>
			<li>
				<a href='<c:url value="/lab/newrequest.do"/>'>Request Access to a Lab</a> (Note: requests subject to verification)
			</li>
		</ul>
	</div>


	<sec:authorize access="hasRole('su')">
		<div id="tabs-suUtils">
			<ul class="navTabs">
				<li>
					<a href='<c:url value="/user/list.do"/>'>User Utils</a>
				</li>
				<li>
					<a href='<c:url value="/sysrole/list.do"/>'>System Users</a>
				</li>
				<!-- <li><a href="<c:url value="/department/list.do"/>">Department Utils</a></li> -->
				<li>
					<a href='<c:url value="/lab/list.do"/>'>Lab Utils</a>
				</li>
				<li>
					<a href='<c:url value="/workflow/list.do"/>'>Workflow Utils</a>
				</li>
			</ul>
		</div>
	</sec:authorize>

	<sec:authorize	access="hasRole('da-*') or hasRole('su') or hasRole('ga')">
		<div id="tabs-daAdmin">
			<ul class="navTabs">
				<li>
					<a href='<c:url value="/department/list.do"/>'>Department Management</a>&nbsp;
				</li>
			</ul>
		</div>
	</sec:authorize>



	<sec:authorize access="hasRole('da-${labs[0].departmentId}') or hasRole('ft') or hasRole('su') or hasRole('ga') or hasRole('lu-${labs[0].labId}')">
		<div id="tabs-labUtils">
				
				<ul class="navTabs">
					<c:forEach items="${labs}" var="l">
						<br /><b><c:out value="${l.name}" /> </b>
						<c:set var="labId" value="${l.labId}" />
						<li>
							<a href='<c:url value="/lab/detail_ro/${l.departmentId}/${l.labId}.do"/>'>Lab Details</a>
						</li>
						<sec:authorize access="not hasRole('lm-${l.labId}' )">
							<li>
								<a href='<c:url value="/lab/user_list/${l.labId}.do"/>'>Lab	Members</a>
							</li>
						</sec:authorize>
						<sec:authorize access="hasRole('lm-${l.labId}' )">
							<li>
								<a href='<c:url value="/lab/user_manager/${l.labId}.do"/>'>User Manager</a>
							</li>							
						</sec:authorize>
					</c:forEach>
			</ul>
		</div>
	</sec:authorize>

	<div>
		<sec:authorize access="hasRole('jv-*') or hasRole('jd-*') or hasRole('su') or hasRole('ga') or hasRole('lu-*') or hasRole('fm') or hasRole('ft')">
			<div id="tabs-jobUtils">
				<ul class="navTabs">
					<sec:authorize access="hasRole('jv-*')">
						<li>
							<a href='<c:url value="/job/list.do?userId=${me.getUserId()}"/>'>My	Viewable Jobs (<c:out value="${jobViewableCount}" />)</a>
						</li>
					</sec:authorize>
					<sec:authorize access="hasRole('jd-*')">
						<li>
							<a href='<c:url value="/jobsubmit/list.do?userId=${me.getUserId()}"/>'>Drafted Jobs (<c:out value="${jobDraftCount}" />)</a>
						</li>
					</sec:authorize>
					<sec:authorize
						access="hasRole('su') or hasRole('ga') or hasRole('fm') or hasRole('ft')">
						<li>
							<a href='<c:url value="/job/list.do"/>'>View All Jobs (<c:out value="${jobsAllCount}" />)</a>
						</li>
					</sec:authorize>
					<sec:authorize access="hasRole('lu-*')">
						<li>
							<a href='<c:url value="/jobsubmit/create.do" />'>Submit a Job</a>
						</li>
					</sec:authorize>
				</ul>
			</div>
		</sec:authorize>

	</div>

	<br />
	<sec:authorize access="hasRole('fm') or hasRole('ft')">
		<div id="tabs-facilityUtils">
			<h2>Job Quoting</h2>
			<ul class="navTabs">
				<sec:authorize access="hasRole('fm')">
					<li>
						<a href='<c:url value="/job2quote/list.do?showall=true"/>'>List of All Job Quotes</a>
					</li>
				</sec:authorize>
			</ul>
			<br />
			<h2>Sample Utils</h2>
			<ul class="navTabs">
				<li>
					<a href='<c:url value="/sample/list.do"/>'>List of All Samples</a>
				</li>

				<li>
					<a href='<c:url value="/task/samplereceive/list.do"/>'>Sample Receiver</a>
				</li>
				
				<li>
					<a href='<c:url value="/sample/listControlLibraries.do"/>'>Control Libraries</a>
				</li>
				
			</ul>
			<br />
			<h2>Platform Unit Utils</h2>
			<ul class="navTabs">
				<li>
					<a
						href='<c:url value="/facility/platformunit/limitPriorToPlatUnitAssign.do" />'>List / Create</a>
				</li>
				<li>
					<a href='<c:url value="/facility/platformunit/limitPriorToAssign.do?resourceCategoryId=0" />'>Platform Unit assignment</a>
				</li>
			</ul>
			<br />
			<h2>Misc Utils</h2>
			<ul class="navTabs">
				<li>
					<a href='<c:url value="/resource/list.do"/>'>List of All Machines</a>
				</li>
				<li>
					<a href='<c:url value="/run/list.do"/>'>List of All Runs</a>
				</li>
			</ul>
		</div>
	</sec:authorize>
	<div id="tabs-taskList">
		<ul class="navTabs">
			<c:forEach items="${tasks}" var="task">
				<li>
					<a href='<c:url value="${task.listMap}"/>'>${task.task.name}<%-- /${task.status}--%></a>&nbsp; (${task.stateCount})
				</li>
	
			</c:forEach>
			
			<sec:authorize	access="hasRole('da-*') or hasRole('su') or hasRole('ga-*') or hasRole('fm-*') or hasRole('ft-*') or hasRole('pi-*') or hasRole('lm-*') ">
			<c:if test="${numberOfLabManagerPendingTasks > 0}">
			<li>
				<a href='<c:url value="/lab/pendinglmapproval/list.do"/>'>Lab Management Tasks</a>&nbsp; (${numberOfLabManagerPendingTasks})
			</li>
			</c:if>
			<c:if test="${numberOfDepartmentAdminPendingTasks > 0}">
			<li>
				<a href='<c:url value="/department/dapendingtasklist.do"/>'>Department Administration Tasks</a>&nbsp; (${numberOfDepartmentAdminPendingTasks})
			</li>
			</c:if>
			</sec:authorize>
			
			
			
		</ul>
	</div>
</div>








