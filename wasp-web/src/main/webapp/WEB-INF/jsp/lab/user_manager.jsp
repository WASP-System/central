<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
<!--  this is ed's version, replaced with code below ; February, 2013
    <p></p>
    <h1><fmt:message key="pageTitle.lab/user_manager.label"/></h1>
	<h2><fmt:message key="labuser.current.label"/></h2>
	<table class="EditTable ui-widget ui-widget-content">
		<tr class="FormData">
		  	<td class="CaptionTD top-heading"><fmt:message key="labuser.loginName.label"/></td>
		  	<td class="CaptionTD top-heading"><fmt:message key="labuser.name.label"/></td>
		  	<td class="CaptionTD top-heading"><fmt:message key="labuser.email.label"/></td>
-->
		  	<!--  <td class="CaptionTD top-heading"><fmt:message key="labuser.status.label"/></td> -->
<!--	  	<td class="CaptionTD top-heading"><fmt:message key="labuser.status_in_lab.label"/></td>
		  	<td class="CaptionTD top-heading"><fmt:message key="labuser.actions.label"/></td>
		  	<td class="CaptionTD top-heading"><fmt:message key="labuser.change_status_in_lab.label"/></td>
		</tr>
	    <c:forEach items="${labuser}" var="ul">
	      <tr class="FormData">
	      <td class="DataTD"><a href="/wasp/user/detail_ro/<c:out value="${ul.user.userId}" />.do"><c:out value="${ul.user.login}" /></a></td>
	      <td class="DataTD">
	        <c:out value="${ul.user.firstName}" />
	        <c:out value="${ul.user.lastName}" />
	      </td>
	      <td class="DataTD"><c:out value="${ul.user.email}" /></td>
-->
	      <!--  this is user.isActive and should NOT be displayed on this page dubin, 2-7-13
	      <td class="DataTD">
	        <c:if test="${ul.user.isActive == 1}" > <fmt:message key="labuser.active.label"/> 
	        </c:if>
	        <c:if test="${ul.user.isActive == 0}" > <fmt:message key="labuser.inactive.label"/>
	        </c:if>
	       </td>
	       -->
<!--       <td class="DataTD" ><c:out value="${ul.role.name}" /></td>
		  <td class="submit value" nowrap="nowrap">
		      <c:if test="${ul.role.roleName == 'lx'}">
		        <a href="<c:url value="/lab/user/role/${lab.labId}/${ul.user.userId}/lu.do"/>"><fmt:message key="labuser.status_activate.label"/></a>
		      </c:if>
		      <c:if test="${ul.role.roleName == 'lu'}">
		        <a href="<c:url value="/lab/user/role/${lab.labId}/${ul.user.userId}/lm.do"/>"><fmt:message key="labuser.status_promoteLM.label"/></a>
		        <a href="<c:url value="/lab/user/role/${lab.labId}/${ul.user.userId}/lx.do"/>"><fmt:message key="labuser.status_deactivate.label"/></a>
		      </c:if>
		      <c:if test="${ul.role.roleName == 'lm'}">
		        <a href="<c:url value="/lab/user/role/${lab.labId}/${ul.user.userId}/lu.do"/>"><fmt:message key="labuser.status_demoteLU.label"/></a>
		      </c:if>
-->
		<%--       <c:if test="${ul.role.roleName == 'lp'}"> --%>
		<%--         <a href="<c:url value="/lab/user/role/${lab.labId}/${ul.user.userId}/lu.do"/>"><fmt:message key="userPending.action_approve.label"/></a> --%>
		<%--         <a href="<c:url value="/lab/user/role/${lab.labId}/${ul.user.userId}/xx.do"/>"><fmt:message key="userPending.action_reject.label"/></a> --%>
		<%--       </c:if> --%>
<!--  		&nbsp;</td>
	      
	      <td class="DataTD" >
	      <form method="POST" action="<c:url value="/facility/platformunit/limitPriorToAssign.do" />">
	       <c:if test="${ul.role.roleName == 'lx'}">
	      <select class="FormElement ui-widget-content ui-corner-all" size="1" name="newRole" style="width:220px" onchange="this.parentNode.submit()">
	      <option value="">--Select--
	      <option value="lu">Reinstate Lab Membership
	      <option value="lm">Reinstate &amp; Upgrade To Lab Manager
	      </select>
	     </c:if>
	     <c:if test="${ul.role.roleName == 'lu'}">
	      <select class="FormElement ui-widget-content ui-corner-all" size="1" name="newRole" style="width:220px" onchange="this.parentNode.submit()">
	      <option value="">--Select--
	      <option value="lm">Upgrade To Lab Manager
	      <option value="lx">Inactivate Lab Membership
	      </select>
	     </c:if>
	     <c:if test="${ul.role.roleName == 'lm'}">
	      <select class="FormElement ui-widget-content ui-corner-all" size="1"  name="newRole" style="width:220px" onchange="this.parentNode.submit()">
	      <option value="">--Select--
	      <option value="lu">Downgrade From Manager To Lab Member
	      <option value="lx">Inactivate Lab Membership
	      </select>
	     </c:if>
	     </form>
	     &nbsp;
	     </td>
	     
	     
	      </tr>
	    </c:forEach>
	</table>
-->

<p></p>
<h1><fmt:message key="pageTitle.lab/user_manager.label"/></h1>
<h2><fmt:message key="labuser.userManager_principalInvestigator.label" />: <c:out value="${piName}" /></h2>

<c:set var="colSpan" value="3" />
<c:set var="ableToChangeStatus" value="false" />
<sec:authorize access="hasRole('su') or hasRole('fm') or hasRole('pi-${labId}') or hasRole('lm-${labId}')">
    <c:set var="colSpan" value="4" />
	<c:set var="ableToChangeStatus" value="true" /> 
	<sec:authorize access="hasRole('pi-${labId}')">
		<div class="instructions"><fmt:message key="labuser.userManager_piInstructions.label" /></div>
	</sec:authorize>	
</sec:authorize> 
   

<script src="/wasp/scripts/jquery/jquery-1.7.1.js" type="text/javascript"></script>
<script src="/wasp/scripts/jquery/jquery-ui-1.8.18.custom.min.js" type="text/javascript" ></script> 
<script>
$(document).ready(function() {
	$("#rightsResponsibilitiesButton").click(function() {
  		$("#rightsResponsibilities").fadeToggle("slow", "linear");
  		if($(this).prop("value")=="<fmt:message key="labuser.userManager_showRandR.label" />"){$(this).prop("value", "<fmt:message key="labuser.userManager_hideRandR.label" />");}
  		else{$(this).prop("value", "<fmt:message key="labuser.userManager_showRandR.label" />");}
 	}); 
  	//$(".wasptooltip a[title]").tooltip({ position: "top right"});
});
</script>
<input  class="button" type="button" id="rightsResponsibilitiesButton" value="<fmt:message key="labuser.userManager_showRandR.label" />"  />
<div id="rightsResponsibilities" style="display:none">	
	<c:import url="/WEB-INF/jsp/lab/userRightsResponsibilities.jsp" />
	<br />	
</div>
   
	<table class="EditTable ui-widget ui-widget-content">	
	 	<c:if test="${ableToChangeStatus=='true'}" >
	 		<c:if test="${not empty newuserspendinglist || not empty existinguserspendinglist }" >
	 			<tr class="FormData">
					<td colspan="${colSpan}" class="CaptionTD top-heading" style="font-size:large"><fmt:message key="labuser.userManager_usersRequestingToJoinThisLab.label"/></td>
				</tr>
				<tr class="FormData">
					<td colspan="1" class="CaptionTD top-heading"><fmt:message key="labuser.userManager_name.label"/></td>
					<td colspan="1" class="CaptionTD top-heading"><fmt:message key="labuser.userManager_email.label"/></td>
					<td colspan="${colSpan - 2}" class="CaptionTD top-heading"><fmt:message key="labuser.approveOrReject.label"/></td>
				</tr>
	 		
	 		<c:forEach items="${newuserspendinglist}" var="up">
	 		  <tr class="FormData">
	 			<td class="DataTD" style="text-align:center"><c:out value="${up.lastName}, ${up.firstName}" /></td>
	 			<td class="DataTD" style="text-align:center"><c:out value="${up.email}" /></td>
	 			<td colspan="${colSpan - 2}" class="DataTD" style="text-align:center"><div class="submit" ><a href="<c:url value="/lab/userpending/approve/${labId}/${up.userPendingId}.do"/>"><fmt:message key="lmpendingtask.approve.label" /></a> <a href="<c:url value="/lab/userpending/reject/${labId}/${up.userPendingId}.do"/>"><fmt:message key="lmpendingtask.reject.label" /></a></div></td>    
	 		  </tr>
	 		</c:forEach>
	 		<c:forEach items="${existinguserspendinglist}" var="lu">
	 		  <tr class="FormData">
	 			<td class="DataTD" style="text-align:center"><a href="/wasp/user/detail_ro/<c:out value="${lu.user.userId}" />.do"><c:out value="${lu.user.lastName}, ${lu.user.firstName}" /></a>
	 			</td>
	 			<td class="DataTD" style="text-align:center"><c:out value="${lu.user.email}" /></td>
	 			<td colspan="${colSpan - 2}" class="DataTD" style="text-align:center"><div class="submit" ><a href="<c:url value="/lab/labuserpending/approve/${labId}/${lu.labUserId}.do"/>"><fmt:message key="lmpendingtask.approve.label" /></a> <a href="<c:url value="/lab/labuserpending/reject/${labId}/${lu.labUserId}.do"/>"><fmt:message key="lmpendingtask.reject.label" /></a></div></td>    
	 		  </tr>
	 		</c:forEach>
	 		  <tr class="FormData">
				<td colspan="${colSpan}" class="CaptionTD top-heading" style="background:black;" ></td>
			  </tr>
			</c:if>
	 	</c:if>
		<tr class="FormData">
			<td colspan="${colSpan}" class="CaptionTD top-heading" style="font-size:large"><fmt:message key="labuser.userManager_currentAnFormerLabMembers.label"/></td>
		</tr>
		<tr class="FormData">
		  	<td class="CaptionTD top-heading"><fmt:message key="labuser.userManager_name.label"/></td>
		  	<td class="CaptionTD top-heading"><fmt:message key="labuser.userManager_email.label"/></td>
		  	<td class="CaptionTD top-heading"><fmt:message key="labuser.userManager_status.label"/></td>
    		<c:if test="${ableToChangeStatus=='true'}" >
    			<td class="CaptionTD top-heading"><fmt:message key="labuser.userManager_change_status.label"/></td>
		  	</c:if>
		</tr>
		
		<c:set var="lastRoleName" value="" />
		
		<c:forEach items="${labUserFinalList}" var="ul">
	     
	    	<c:if test="${ul.role.roleName != 'pi' && ul.role.roleName != lastRoleName}">
	      		<tr><td colspan="${colSpan}"><hr></td></tr>
	      	</c:if>
	      
	      	<c:set var="lastRoleName" value="${ul.role.roleName}" />
	      
	      	<tr class="FormData">
	      		<td class="DataTD" style="text-align:center">
	      			<a href="/wasp/user/detail_ro/<c:out value="${ul.user.userId}" />.do"><c:out value="${ul.user.lastName}" />, <c:out value="${ul.user.firstName}" /></a>
		      	</td>
	      		<td class="DataTD" style="text-align:center"><c:out value="${ul.user.email}" /></td>
	      		<td class="DataTD" style="text-align:center"><c:out value="${ul.role.name}" /></td>
	      		
	      		<c:if test="${ableToChangeStatus=='true'}" >	      		
			  		<td class="DataTD" style="text-align:center">
		      			<form method="POST" action="<c:url value="/lab/user_manager/update.do" />">
		      			    <input type="hidden" name="labId" value="${ul.lab.labId}" />
		      			     <input type="hidden" name="userId" value="${ul.user.userId}" />
		       				<c:if test="${ul.role.roleName == 'lx'}">
		      					<select class="FormElement ui-widget-content ui-corner-all" size="1" name="newRole" style="width:220px" onchange="this.parentNode.submit()">
		      						<option value=""><fmt:message key="wasp.default_select.label"/>
		      						<option value="lu"><fmt:message key="labuser.userManager_reinstate.label"/>
		     						 <option value="lm"><fmt:message key="labuser.userManager_reinstateAndSetToManager.label"/>
		      					</select>
		     				</c:if>
		    				 <c:if test="${ul.role.roleName == 'lu'}">
		      					<select class="FormElement ui-widget-content ui-corner-all" size="1" name="newRole" style="width:220px" onchange="this.parentNode.submit()">
		      						<option value=""><fmt:message key="wasp.default_select.label"/>
		      						<option value="lm"><fmt:message key="labuser.userManager_upgradeToManager.label"/>
		      						<option value="lx"><fmt:message key="labuser.userManager_inactivateLabMembership.label"/>
		      					</select>
		     				</c:if>
		    				<c:if test="${ul.role.roleName == 'lm'}">
		      					<select class="FormElement ui-widget-content ui-corner-all" size="1"  name="newRole" style="width:220px" onchange="this.parentNode.submit()">
		     						<option value=""><fmt:message key="wasp.default_select.label"/>
		      						<option value="lu"><fmt:message key="labuser.userManager_dowgradeFromManagerToLabMember.label"/>
		      						<option value="lx"><fmt:message key="labuser.userManager_inactivateLabMembership.label"/>
		      					</select>
		     				</c:if>
		     			</form>
		     			&nbsp;
		     		</td>
	     		</c:if>
	      </tr>
	   </c:forEach>
	</table>

