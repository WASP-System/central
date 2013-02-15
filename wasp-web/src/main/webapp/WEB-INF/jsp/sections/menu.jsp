<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<sec:authorize access="isAuthenticated()">
    
 	<!-- requires jquery which is already imported in base.jsp or grid.jsp -->  
	<script src="/wasp/scripts/memu/jquery.memu-0.1.min.js" type="text/javascript" ></script> 
	<link rel="stylesheet" type="text/css" media="screen" href="/wasp/scripts/memu/memu-0.1.css" />

	<style type="text/css" media="screen">
		.menu-container {
			margin: 0 auto;
			padding: 0;
			height: 30px;
			width: 100%;
			background: gray;
			border: 1px solid #222;
			/*next set of lines stolen from .instructions in base.css */
			text-shadow: 0 1px 1px rgba(255, 255, 255, 0.85);
			background: -webkit-gradient(linear, left top, left bottom, from(#ffffe6), to(#fefec1));
			background: -moz-linear-gradient(top, #ffffe6, #fefec1);
			filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#ffffe6', endColorstr='#fefec1');
			background-color: #fefec1;
			-webkit-border-radius: 10px;
			-moz-border-radius: 10px;
			-o-border-radius: 10px;
			border-radius: 10px;
			line-height: 16px;
			margin: 10px 0 10px;
			/*padding: 10px;*/
		}
	</style> 

 	 <script language="JavaScript">
		<!--
		$('.js-enabled').memu({ 
			icon: {
				inset: true,		// create an inset where there is no icon
				margin: {		// specify the margins of the icon (put them how it's right for you
					top: 4,
					right: 10
				}
			},
			width: 150,			// the width of the (drop down) items
			rootWidth: 75,			// the width of the (root) items
			height: 25			// the height of the items
		});			
		//-->
	</script>      
    
    <div class="menu-container">
    	
			<ul class="memu">
				<li class="memu-root">
					<a href='<c:url value="/dashboard.do"/>'><fmt:message key="menu.home.label" /></a>
				</li>
				<li class="memu-root">
					<a href="#"><fmt:message key="menu.user.label" /></a>
					<ul>
						<li><a href='<c:url value="/user/me_ro.do"/>'><fmt:message key="menu.myProfile.label" /></a></li>
						<sec:authorize access="not hasRole('ldap')">
							<li><a href='<c:url value="/user/mypassword.do"/>'><fmt:message key="menu.changePassword.label" /></a></li>
						</sec:authorize>
						<sec:authorize access="not hasRole('su')">						
							<li><a href='<c:url value="/lab/joinAnotherLab.do"/>'><fmt:message key="menu.joinAnotherLab.label" /></a></li>
							<sec:authorize access="not hasRole('pi-*')">
								<li><a href='<c:url value="/lab/upgradeStatusToPI.do"/>'><fmt:message key="menu.upgradeToPI.label" /></a></li>
							</sec:authorize>
						</sec:authorize>
					</ul>
				</li>
				<sec:authorize access="not hasRole('su') and ( hasRole('lu-*') or hasRole('lx-*') or hasRole('lp-*') )">
					<li class="memu-root">
						<a href="<c:url value="/lab/viewerLabList.do"/>"><fmt:message key="menu.labs.label" /></a>
						<!--  
						<ul>
							<li><a href="#">Status Bar</a></li>
						</ul>
						-->
					</li>
				</sec:authorize>
				<sec:authorize access="not hasRole('su') and (   hasRole('lu-*') or (   hasRole('lx-*') and hasRole('jv-*')   )   ) ">
					<li class="memu-root">
						<a href="#"><fmt:message key="menu.jobs.label" /></a>
						<ul>
							<sec:authorize access="hasRole('jv-*')">
								<li><a href='<c:url value="/job/list.do"/>'><fmt:message key="menu.mySubmittedJobs.label" /></a></li>
							</sec:authorize>
							<sec:authorize access="hasRole('jd-*') and hasRole('lu-*')">
								<li><a href='<c:url value="/jobsubmit/list.do"/>'><fmt:message key="menu.myJobDrafts.label" /></a></li>
							</sec:authorize>
							<sec:authorize access="hasRole('lu-*')">
								<li><a href='<c:url value="/jobsubmit/create.do" />'><fmt:message key="menu.submitNewJob.label" /></a></li>
							</sec:authorize>
						</ul>
					</li>
				</sec:authorize>
				<sec:authorize access="hasRole('su') or hasRole('da-*') or hasRole('ga') or hasRole('fm') or hasRole('ft')">
					<li class="memu-root">
						<a href="#"><fmt:message key="menu.admin.label" /></a>
						<ul>
							<sec:authorize access="hasRole('su') or hasRole('da-*') or hasRole('ga') or hasRole('fm')">
								<li><a href='<c:url value="/department/list.do"/>'><fmt:message key="menu.deptAdmin.label" /></a></li>
							</sec:authorize>
							<li><a href='<c:url value="/lab/list.do"/>'><fmt:message key="menu.labs.label" /></a></li>
							<sec:authorize access="not hasRole('da-*')">
								<li class="has-children">
									<a href="#"><fmt:message key="menu.plugins.label" /></a>
									<ul>
										<li><a href='#'><fmt:message key="menu.bisulfateseq.label" /></a></li>
										<li><a href='<c:url value="/wasp-chipseq/description.do"/>'><fmt:message key="menu.chipseq.label" /></a></li>
										<li><a href='#'><fmt:message key="dashboard.helptag.label" /></a></li>
										<li><a href='<c:url value="/wasp-illumina/description.do"/>'><fmt:message key="menu.illumina.label" /></a></li>								
									</ul>
								</li>
							</sec:authorize>
							<sec:authorize access="not hasRole('da-*')">
								<li><a href='<c:url value="/job2quote/list_all.do"/>'><fmt:message key="menu.jobQuotes.label" /></a></li>
							</sec:authorize>
							<sec:authorize access="hasRole('da-*')">
								<li><a href='<c:url value="/job2quote/list.do"/>'><fmt:message key="menu.jobQuotes.label" /></a></li>
							</sec:authorize>
							<sec:authorize access="hasRole('su') or hasRole('ga') or hasRole('fm')">
								<li class="has-children">
									<a href="#"><fmt:message key="menu.tasksForOthers.label" /></a>
									<ul>
										<li><a href='<c:url value="/task/daapprove/list.do"/>'><fmt:message key="menu.deptAdminTasks.label" /></a></li>
										<li><a href='<c:url value="/task/piapprove/list.do"/>'><fmt:message key="menu.piAndLabManagerTasks.label" /></a></li>
									</ul>
								</li>
							</sec:authorize>
							<sec:authorize access="not hasRole('da-*')">
								<li class="has-children">
									<a href="#"><fmt:message key="menu.users.label" /></a>
									<ul>
										<li><a href='<c:url value="/user/list.do"/>'><fmt:message key="menu.regularUsers.label" /></a></li>
										<li><a href='<c:url value="/sysrole/list.do"/>'><fmt:message key="menu.systemUsers.label" /></a></li>
									</ul>
								</li>
								<li><a href='<c:url value="/workflow/list.do"/>'><fmt:message key="menu.workflows.label" /></a></li>
							</sec:authorize>
						</ul>
					</li>
				</sec:authorize>
				
				<sec:authorize access="hasRole('su') or hasRole('ga') or hasRole('fm') or hasRole('ft')">
					<li class="memu-root">
						<a href="#"><fmt:message key="menu.facility.label" /></a>
						<ul>
							<li><a href="<c:url value="/resource/list.do"/>"><fmt:message key="menu.machines.label" /></a></li>
							<li class="has-children">
								<a href="#"><fmt:message key="menu.platformUnits.label" /></a>
								<ul>
									<li><a href='<c:url value="/facility/platformunit/list.do"/>'><fmt:message key="menu.listPlatformUnits.label" /></a></li>
									<li><a href='<c:url value="/facility/platformunit/createUpdatePlatformUnit.do?sampleSubtypeId=0&sampleId=0"/>'><fmt:message key="menu.newPlatformUnits.label" /></a></li>
									<li><a href='<c:url value="/facility/platformunit/limitPriorToAssign.do?resourceCategoryId=0"/>'><fmt:message key="menu.assignLibrariesToPlatformUnits.label" /></a></li>
								</ul>
							</li>
							<li class="has-children">
								<a href="#"><fmt:message key="menu.samples.label" /></a>
								<ul>
									<li><a href='<c:url value="/sample/list.do"/>'><fmt:message key="menu.allSamples.label" /></a></li>
									<li><a href='<c:url value="/sample/listControlLibraries.do"/>'><fmt:message key="menu.controlLibraries.label" /></a></li>
									<li><a href='<c:url value="/task/samplereceive/list.do"/>'><fmt:message key="menu.sampleReceiver.label" /></a></li>
								</ul>
							</li>
							<li><a href='<c:url value="/run/list.do"/>'><fmt:message key="menu.sequenceRuns.label" /></a></li>
						</ul>
					</li>
				</sec:authorize>
				
				<sec:authorize access="hasRole('su') or hasRole('ga') or hasRole('fm') or hasRole('ft')">
					<li class="memu-root">
						<a href='<c:url value="/job/list.do"/>'><fmt:message key="menu.jobs.label" /></a>
					</li>
				</sec:authorize>
				
				<li class="memu-root">
					<a href='<c:url value="/task/myTaskList.do"/>'><fmt:message key="menu.tasks.label" /></a>
				</li>
				
				<li class="memu-root">
						<a href='<c:url value="/j_spring_security_logout"/>'><fmt:message key="menu.logout.label" /></a>
				</li>
				
			</ul>
			
		 </div>
</sec:authorize>