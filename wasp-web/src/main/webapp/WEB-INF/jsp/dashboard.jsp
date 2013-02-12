<%@ include file="/WEB-INF/jsp/include.jsp"%>
<%@ include file="/WEB-INF/jsp/taglib.jsp"%>

<div>
	<h1><fmt:message key="dashboard.dashboard.label" /> - ${me.firstName} ${me.lastName}</h1>
</div>
<div id="tabs">
	<ul>
		<li><a href="#tabs-home"><fmt:message key="dashboard.myAccount.label" /></a></li>
		<sec:authorize access="hasRole('su')">
			<li><a href="#tabs-suUtils"><fmt:message key="dashboard.superuserUtils.label" /></a></li>
		</sec:authorize>
		<sec:authorize access="hasRole('da-*') or hasRole('su') or hasRole('ga')">
			<li><a href="#tabs-daAdmin"><fmt:message key="dashboard.deptAdmin.label" /></a></li>
		</sec:authorize>
		<sec:authorize access="hasRole('da-${labs[0].departmentId}') or hasRole('su') or hasRole('lu-*') or hasRole('ft-*') or hasRole('ga') or hasRole('fm')"> 
			<c:if test="${labCount > 0}">
				<li><a href="#tabs-labUtils"><fmt:message key="dashboard.labUtils.label" /></a></li>
			</c:if>
		</sec:authorize>
		<sec:authorize
			access="hasRole('jv-*') or hasRole('jd-*') or hasRole('su') or hasRole('ga') or hasRole('lu-*') or hasRole('fm') or hasRole('ft')">
			<li><a href="#tabs-jobUtils"><fmt:message key="dashboard.jobUtils.label" /></a></li>
		</sec:authorize>
		<sec:authorize access="hasRole('fm') or hasRole('ft')">
			<li><a href="#tabs-facilityUtils"><fmt:message key="dashboard.facilityUtils.label" /></a></li>
		</sec:authorize>
		<%-- <li><a href="#tabs-taskList">Tasks (<span class="taskAlert"><c:out value="${fn:length(tasks)}" /></span>)</a></li> --%>
		<%--<li><a href="#tabs-taskList"><fmt:message key="dashboard.tasks.label" /> (<span class="taskAlert">
		<c:if test="${isTasks == false}">0</c:if>
		<c:if test="${isTasks == true}">&gt;= 1</c:if>
		</span>)</a></li>--%>
		<c:if test="${isTasks == true}">
			<li><a href="#tabs-taskList"><fmt:message key="dashboard.myTasks.label" /></a></li>
		</c:if>
	</ul>
	<div id="tabs-home">
		<ul class="navTabs">
			<li>
				<a href='<c:url value="/user/me_ro.do"/>'><fmt:message key="dashboard.myProfile.label" /></a>
			</li>
			<sec:authorize access="not hasRole('ldap')">
				<li>
					<a href='<c:url value="/user/mypassword.do"/>'><fmt:message key="dashboard.myPassword.label" /></a>
				</li>
			</sec:authorize>
			<!-- commented out by Dubin, 01-29-13; does not appear to ever be used. doReauth() seems to be called programmatically where needed. 
			<li>
				<a href='<c:url value="/auth/reauth.do"/>'><fmt:message key="dashboard.refreshAuth.label" /></a>
			</li>
			-->
			<!-- commented out by Dubin, 02-01-13; replaced by the two list items immediately below this one
			<li>
				<a href='<c:url value="/lab/newrequest.do"/>'><fmt:message key="dashboard.requestAccessToLab.label" /></a> (<fmt:message key="dashboard.requestAccessNote.label" />)
			</li>
			-->
			<li>
				<a href='<c:url value="/lab/joinAnotherLab.do"/>'><fmt:message key="dashboard.joinAnotherLab.label" /></a>
			</li>
			<sec:authorize access="not hasRole('pi-*')">
				<li>
					<a href='<c:url value="/lab/upgradeStatusToPI.do"/>'><fmt:message key="dashboard.upgradeStatusToPI.label" /></a>
				</li>
			</sec:authorize>
		</ul>
	</div>


	<sec:authorize access="hasRole('su')">
		<div id="tabs-suUtils">
			<ul class="navTabs">
				<li>
					<a href='<c:url value="/user/list.do"/>'><fmt:message key="dashboard.userUtils.label" /></a>
				</li>
				<li>
					<a href='<c:url value="/sysrole/list.do"/>'><fmt:message key="dashboard.systemUsers.label" /></a>
				</li>
				<!-- <li><a href="<c:url value="/department/list.do"/>">Department Utils</a></li> -->
				<li>
					<a href='<c:url value="/lab/list.do"/>'><fmt:message key="dashboard.labUtils.label" /></a>
				</li>
				<li>
					<a href='<c:url value="/workflow/list.do"/>'><fmt:message key="dashboard.workflowUtils.label" /></a>
				</li>
				<li>
					<a href='<c:url value="/plugin/listAll.do"/>'><fmt:message key="plugin.list.label" /></a>
				</li>
			</ul>
		</div>
	</sec:authorize>

	<sec:authorize	access="hasRole('da-*') or hasRole('su') or hasRole('ga')">
		<div id="tabs-daAdmin">
			<ul class="navTabs">
				<li>
					<a href='<c:url value="/department/list.do"/>'><fmt:message key="dashboard.deptManagement.label" /></a>&nbsp;
				</li>
				<sec:authorize	access="hasRole('da-*')">
				<li>
					<a href='<c:url value="/lab/list.do"/>'><fmt:message key="dashboard.labUtils.label" /></a>
				</li>
				<li>
						<a href='<c:url value="/job2quote/list_all.do"/>'><fmt:message key="dashboard.jobQuotes.label" /></a>
					</li>
				</sec:authorize>
			</ul>
		</div>
	</sec:authorize>



	<sec:authorize access="hasRole('da-${labs[0].departmentId}') or hasRole('ft') or hasRole('su') or hasRole('ga') or hasRole('lu-${labs[0].labId}')">
		<c:if test="${labCount > 0}">
		   <div id="tabs-labUtils">				
				<ul class="navTabs">
					<c:forEach items="${labs}" var="l">
						<br /><b><c:out value="${l.name}" /> </b>
						<c:set var="labId" value="${l.labId}" />
						<li>
							<a href='<c:url value="/lab/detail_ro/${l.departmentId}/${l.labId}.do"/>'><fmt:message key="dashboard.labDetails.label" /></a>
						</li>
						<!--  
						<sec:authorize access="not hasRole('lm-${l.labId}' )">
							<li>
								<a href='<c:url value="/lab/user_list/${l.labId}.do"/>'><fmt:message key="dashboard.labMembers.label" /></a>
							</li>
						</sec:authorize>
						-->
						<sec:authorize access="hasRole('lu-${l.labId}' )">
							<li>
								<a href='<c:url value="/lab/user_manager/${l.labId}.do"/>'><fmt:message key="dashboard.userManager.label" /></a>
							</li>							
						</sec:authorize>
					</c:forEach>
				</ul>
			</div>
		</c:if>
	</sec:authorize>

	<div>
		<sec:authorize access="hasRole('jv-*') or hasRole('jd-*') or hasRole('su') or hasRole('ga') or hasRole('lu-*') or hasRole('fm') or hasRole('ft')">
			<div id="tabs-jobUtils">
				<ul class="navTabs">
					<sec:authorize access="hasRole('jv-*')">
						<li>
						<!--  	8/16/12 this call no longer requires the ?userId, since the controller JobController.getListJSON automatically inquires who the viewer is, based on roles, and shows only what the user can see <a href='<c:url value="/job/list.do?userId=${me.getUserId()}"/>'>My	Viewable Jobs (<c:out value="${jobViewableCount}" />)</a>  -->
						<a href='<c:url value="/job/list.do"/>'><fmt:message key="dashboard.myViewableJobs.label" /> (<c:out value="${jobViewableCount}" />)</a>
						</li>
					</sec:authorize>
					<sec:authorize access="hasRole('jd-*')">
						<li>
							<a href='<c:url value="/jobsubmit/list.do?userId=${me.getUserId()}"/>'><fmt:message key="dashboard.draftedJobs.label" /> (<c:out value="${jobDraftCount}" />)</a>
						</li>
					</sec:authorize>
					<sec:authorize
						access="hasRole('su') or hasRole('ga') or hasRole('fm') or hasRole('ft')">
						<li>
							<a href='<c:url value="/job/list.do"/>'><fmt:message key="dashboard.viewAllJobs.label" /> (<c:out value="${jobsAllCount}" />)</a>
						</li>
					</sec:authorize>
					<sec:authorize access="hasRole('lu-*')">
						<li>
							<a href='<c:url value="/jobsubmit/create.do" />'><fmt:message key="dashboard.submitJob.label" /></a>
						</li>
					</sec:authorize>
				</ul>
			</div>
		</sec:authorize>

	</div>

	<br />
	<sec:authorize access="hasRole('su') or hasRole('fm') or hasRole('ft')">
		<div id="tabs-facilityUtils">
			<h2><fmt:message key="dashboard.jobQuoting.label" /></h2>
			<ul class="navTabs">
				<sec:authorize access="hasRole('su') or hasRole('fm')">
					<li>
						<a href='<c:url value="/job2quote/list_all.do"/>'><fmt:message key="dashboard.listAllJobQuotes.label" /></a>
					</li>
				</sec:authorize>
			</ul>
			<br />
			<h2><fmt:message key="dashboard.sampleUtils.label" /></h2>
			<ul class="navTabs">
				<li>
					<a href='<c:url value="/sample/list.do"/>'><fmt:message key="dashboard.listOfAllSamples.label" /></a>
				</li>

				<li>
					<a href='<c:url value="/task/samplereceive/list.do"/>'><fmt:message key="dashboard.sampleReceiver.label" /></a>
				</li>
				
				<li>
					<a href='<c:url value="/sample/listControlLibraries.do"/>'><fmt:message key="dashboard.controlLibraries.label" /></a>
				</li>
				
			</ul>
			<br />
			<h2><fmt:message key="dashboard.platformUnitUtils.label" /></h2>
			<ul class="navTabs">
				<li>
					<a href='<c:url value="/facility/platformunit/createUpdatePlatformUnit.do?sampleSubtypeId=0&sampleId=0" />'><fmt:message key="dashboard.newPlatformUnit.label" /></a> 
					/ 
					<a href='<c:url value="/facility/platformunit/list.do" />'><fmt:message key="dashboard.allPlatformUnits.label" /></a>
				</li>
				<%--
				<li>
			 		<a href='<c:url value="/facility/platformunit/limitPriorToPlatUnitAssign.do" />'>List / Create</a>  
				</li>
				--%>
				<li>
					<a href='<c:url value="/facility/platformunit/limitPriorToAssign.do?resourceCategoryId=0" />'><fmt:message key="dashboard.assignLibrariesToPU.label" /></a>
				</li>
			</ul>
			<br />
			<h2><fmt:message key="dashboard.miscUtils.label" /></h2>
			<ul class="navTabs">
				<li>
					<a href='<c:url value="/resource/list.do"/>'><fmt:message key="dashboard.listOfAllMachines.label" /></a>
				</li>
				<li>
					<a href='<c:url value="/run/list.do"/>'><fmt:message key="dashboard.listOfAllRuns.label" /></a>
				</li>
			</ul>
		</div>
	</sec:authorize>
	<c:if test="${isTasks == true}">
		<div id="tabs-taskList">
			<ul class="navTabs">
				<div class="instructions">
					<fmt:message key="task.instructions2.label" />
				</div>
				<c:if test="${isTasks == false}"><fmt:message key="task.none.label" /></c:if>
				<c:if test="${isTasks == true}">
					<c:forEach items="${taskHyperlinks}" var="hyperlink">
						<li>
							<a href='<c:url value="${hyperlink.getTargetLink()}"/>'>${hyperlink.getLabel()}</a>
						</li>
			
					</c:forEach>
				</c:if>
				
			</ul>
		</div>
	</c:if>
</div>








