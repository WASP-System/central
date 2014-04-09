<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<%--  TODO: Declare style in css file (e.g. /src/main/webapp/css/base.css), not in .jsp and reuse where possible !!!! --%>

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
   

<script>
$(document).ready(function() {
	$("#rightsResponsibilitiesButton").click(function() {
  		$("#rightsResponsibilities").fadeToggle("slow", "linear");
  		if($(this).prop("value")=="<fmt:message key="labuser.userManager_showRandR.label" />"){$(this).prop("value", "<fmt:message key="labuser.userManager_hideRandR.label" />");}
  		else{$(this).prop("value", "<fmt:message key="labuser.userManager_showRandR.label" />");}
 	}); 
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
	 			<td colspan="${colSpan - 2}" class="DataTD" style="text-align:center"><div class="submit" ><a href="<wasp:relativeUrl value="lab/userpending/approve/${labId}/${up.userPendingId}.do"/>"><fmt:message key="lmpendingtask.approve.label" /></a> <a href="<wasp:relativeUrl value="lab/userpending/reject/${labId}/${up.userPendingId}.do"/>"><fmt:message key="lmpendingtask.reject.label" /></a></div></td>    
	 		  </tr>
	 		</c:forEach>
	 		<c:forEach items="${existinguserspendinglist}" var="lu">
	 		  <tr class="FormData">
	 			<td class="DataTD" style="text-align:center"><a href="<wasp:relativeUrl value='user/detail_ro/${lu.user.userId}.do' />">${lu.user.lastName}, ${lu.user.firstName}</a>
	 			</td>
	 			<td class="DataTD" style="text-align:center"><c:out value="${lu.user.email}" /></td>
	 			<td colspan="${colSpan - 2}" class="DataTD" style="text-align:center"><div class="submit" ><a href="<wasp:relativeUrl value="lab/labuserpending/approve/${labId}/${lu.labUserId}.do"/>"><fmt:message key="lmpendingtask.approve.label" /></a> <a href="<wasp:relativeUrl value="lab/labuserpending/reject/${labId}/${lu.labUserId}.do"/>"><fmt:message key="lmpendingtask.reject.label" /></a></div></td>    
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
	      			<a href="<wasp:relativeUrl value='user/detail_ro/${ul.user.userId}.do' />">${ul.user.lastName}, ${ul.user.firstName}</a>
		      	</td>
	      		<td class="DataTD" style="text-align:center"><c:out value="${ul.user.email}" /></td>
	      		<td class="DataTD" style="text-align:center"><c:out value="${ul.role.name}" /></td>
	      		
	      		<c:if test="${ableToChangeStatus=='true'}" >	      		
			  		<td class="DataTD" style="text-align:center">
		      			<form method="POST" action="<wasp:relativeUrl value="lab/user_manager/update.do" />">
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

