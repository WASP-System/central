<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
<!--  
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
    <c:set var="colSpan" value="3" />
    <c:set var="ableToChangeStatus" value="false" />
    <sec:authorize access="hasRole('su') or hasRole('fm') or hasRole('pi-${labId}') or hasRole('lm-${labId}')">
        <c:set var="colSpan" value="4" />
    	<c:set var="ableToChangeStatus" value="true" />
    </sec:authorize>  
	<table class="EditTable ui-widget ui-widget-content">	
		<tr class="FormData">
			<td colspan="${colSpan}" class="CaptionTD top-heading"><fmt:message key="labuser.userManager_currentAnFormerLabMembers.label"/></td>
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

