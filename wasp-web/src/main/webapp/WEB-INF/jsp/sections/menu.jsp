<%@ include file="/WEB-INF/jsp/taglib.jsp" %>     

		<ul class="main_menu">
			<li class="main_menu-root">
				<a href='<wasp:relativeUrl value="dashboard.do"/>'><fmt:message key="menu.home.label" /></a>
			</li>
			<li class="main_menu-root">
				<a href="#"><fmt:message key="menu.user.label" /></a>
				<ul>
					<li><a href='<wasp:relativeUrl value="user/me_ro.do"/>'><fmt:message key="menu.myProfile.label" /></a></li>
					<sec:authorize access="not hasRole('ldap')">
						<li><a href='<wasp:relativeUrl value="user/mypassword.do"/>'><fmt:message key="menu.changePassword.label" /></a></li>
					</sec:authorize>
					<sec:authorize access="not hasRole('su') and not hasRole('da-*') and not hasRole('ga') ">						
						<li><a href='<wasp:relativeUrl value="lab/joinAnotherLab.do"/>'><fmt:message key="menu.joinAnotherLab.label" /></a></li>
						<sec:authorize access="not hasRole('pi-*')">
							<li><a href='<wasp:relativeUrl value="lab/upgradeStatusToPI.do"/>'><fmt:message key="menu.upgradeToPI.label" /></a></li>
						</sec:authorize>
					</sec:authorize>
				</ul>
			</li>
			<sec:authorize access="not hasRole('su') and ( hasRole('lu-*') or hasRole('lx-*') or hasRole('lp-*') )">
				<li class="main_menu-root">
					<a href="<wasp:relativeUrl value="lab/viewerLabList.do"/>"><fmt:message key="menu.labs.label" /></a>
					<!--  
					<ul>
						<li><a href="#">Status Bar</a></li>
					</ul>
					-->
				</li>
			</sec:authorize>
			<sec:authorize access="hasRole('su') or  hasRole('lu-*') or (   hasRole('lx-*') and hasRole('jv-*')   ) ">
				<li class="main_menu-root">
					<a href="#"><fmt:message key="menu.jobs.label" /></a>
					<ul>
						<sec:authorize access="hasRole('su') or hasRole('fm')">
							<li><a href='<wasp:relativeUrl value="job/list.do"/>'><fmt:message key="menu.allJobs.label" /></a></li>
						</sec:authorize>
						<sec:authorize access="not hasRole('su') and hasRole('jv-*')">
							<li><a href='<wasp:relativeUrl value="job/list.do"/>'><fmt:message key="menu.mySubmittedJobs.label" /></a></li>
						</sec:authorize>
						<sec:authorize access="hasRole('su') or ( hasRole('jd-*') and hasRole('lu-*') )">
							<li><a href='<wasp:relativeUrl value="jobsubmit/list.do"/>'><fmt:message key="menu.myJobDrafts.label" /></a></li>
						</sec:authorize>
						<sec:authorize access="hasRole('su') or hasRole('lu-*')">
							<li><a href='<wasp:relativeUrl value="jobsubmit/create.do" />'><fmt:message key="menu.submitNewJob.label" /></a></li>
						</sec:authorize>
					</ul>
				</li>
			</sec:authorize>
			<sec:authorize access="hasRole('su') or hasRole('da-*') or hasRole('ga') or hasRole('fm') or hasRole('ft')">
				<li class="main_menu-root">
					<a href="#"><fmt:message key="menu.admin.label" /></a>
					<ul>
						<sec:authorize access="hasRole('su') or hasRole('da-*') or hasRole('ga') or hasRole('fm')">
							<li><a href='<wasp:relativeUrl value="department/list.do"/>'><fmt:message key="menu.deptAdmin.label" /></a></li>
						</sec:authorize>
						<li><a href='<wasp:relativeUrl value="lab/list.do"/>'><fmt:message key="menu.labs.label" /></a></li>
						<li><a href='<wasp:relativeUrl value="job2quote/list_all.do"/>'><fmt:message key="menu.jobQuotes.label" /></a></li>
						<sec:authorize access="hasRole('su') or hasRole('da-*') or hasRole('ga') or hasRole('fm')">
							<li class="has-children">
								<a href="#"><fmt:message key="menu.reports.label" /></a>
								<ul>
									<li><a href='<wasp:relativeUrl value="reports/feesCharged.do"/>'><fmt:message key="menu.reportFeesCharged.label" /></a></li>
								</ul>
							</li>
						</sec:authorize>
						<sec:authorize access="hasRole('su') or hasRole('fm')">
							<li class="has-children">
								<a href="#"><fmt:message key="menu.tasksForOthers.label" /></a>
								<ul>
									<li><a href='<wasp:relativeUrl value="task/daapprove/list.do"/>'><fmt:message key="menu.deptAdminTasks.label" /></a></li>
									<li><a href='<wasp:relativeUrl value="task/piapprove/list.do"/>'><fmt:message key="menu.piAndLabManagerTasks.label" /></a></li>
								</ul>
							</li>
						</sec:authorize>
						
						<li class="has-children">
							<a href="#"><fmt:message key="menu.users.label" /></a>
							<ul>
								<li><a href='<wasp:relativeUrl value="user/list.do"/>'><fmt:message key="menu.regularUsers.label" /></a></li>
								<sec:authorize access="hasRole('su') or hasRole('fm')">
									<li><a href='<wasp:relativeUrl value="sysrole/list.do"/>'><fmt:message key="menu.systemUsers.label" /></a></li>
								</sec:authorize>
							</ul>
						</li>
						<sec:authorize access="hasRole('su') or hasRole('ga') or hasRole('fm')">
							<li><a href='<wasp:relativeUrl value="plugin/listAll.do"/>'><fmt:message key="menu.webPlugins.label" /></a></li>
						</sec:authorize>
						<sec:authorize access="hasRole('su') or hasRole('fm')">
							<li><a href='<wasp:relativeUrl value="workflow/list.do"/>'><fmt:message key="menu.workflows.label" /></a></li>
						</sec:authorize>
					</ul>
				</li>
			</sec:authorize>
			
			<sec:authorize access="hasRole('su') or hasRole('fm') or hasRole('ft')">
				<li class="main_menu-root">
					<a href="#"><fmt:message key="menu.facility.label" /></a>
					<ul>
						<li><a href="<wasp:relativeUrl value="resource/list.do"/>"><fmt:message key="menu.machines.label" /></a></li>
						<li class="has-children">
							<a href="#"><fmt:message key="menu.platformUnits.label" /></a>
							<ul>
								<li><a href='<wasp:relativeUrl value="facility/platformunit/list.do"/>'><fmt:message key="menu.listPlatformUnits.label" /></a></li>
								<li><a href='<wasp:relativeUrl value="facility/platformunit/createUpdatePlatformUnit.do?sampleSubtypeId=0&sampleId=0"/>'><fmt:message key="menu.newPlatformUnits.label" /></a></li>
								<li><a href='<wasp:relativeUrl value="facility/platformunit/limitPriorToAssign.do?resourceCategoryId=0"/>'><fmt:message key="menu.assignLibrariesToPlatformUnits.label" /></a></li>
							</ul>
						</li>
						<li class="has-children">
							<a href="#"><fmt:message key="menu.samples.label" /></a>
							<ul>
								<li><a href='<wasp:relativeUrl value="sample/list.do"/>'><fmt:message key="menu.allSamples.label" /></a></li>
								<li><a href='<wasp:relativeUrl value="sample/listControlLibraries.do"/>'><fmt:message key="menu.controlLibraries.label" /></a></li>
								<li><a href='<wasp:relativeUrl value="task/samplereceive/list.do"/>'><fmt:message key="menu.sampleReceiver.label" /></a></li>
							</ul>
						</li>
						<li><a href='<wasp:relativeUrl value="run/list.do"/>'><fmt:message key="menu.sequenceRuns.label" /></a></li>
						<li><a href='<wasp:relativeUrl value="batchJobStatusViewer/list.do"/>'><fmt:message key="menu.batchJobStatusViewer.label" /></a></li>
					</ul>
				</li>
			</sec:authorize>
			
			<sec:authorize access="(hasRole('ga') or hasRole('fm') or hasRole('ft')) and not hasRole('su') and not hasRole('lu-*') and not hasRole('lx-*')">
				<li class="main_menu-root">
					<a href='<wasp:relativeUrl value="job/list.do"/>'><fmt:message key="menu.jobs.label" /></a>
				</li>
			</sec:authorize>
			
			<li class="main_menu-root" onclick='javascript:openWaitDialog();'>
				<a href='<wasp:relativeUrl value="task/myTaskList.do"/>'><fmt:message key="menu.tasks.label"  /></a>
			</li>
		</ul>