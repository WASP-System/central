<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<h1><fmt:message key="pageTitle.sysrole/list.label" /></h1>

<div class="instructions">
  <fmt:message key="sysrole.list_current.label" />
</div>

<table class="EditTable ui-widget ui-widget-content">
<tr class="FormData">
  	<th class="top-heading"><fmt:message key="sysrole.list_name.label" /></th>
  	<th class="top-heading"><fmt:message key="sysrole.list_role.label" /></th>
  	<th class="top-heading"><fmt:message key="sysrole.list_action.label" /></th>
</tr>

  <c:forEach items="${userRoleMap}" var="rs">
	  <c:forEach items="${rs.value}" var="r">
	  	<tr class="FormData">
		    <td class="DataTD"><c:out value="${rs.key}" /></td>
		    <td class="DataTD"><c:out value="${r.role.name}" /></td>
		    <c:choose>
		    <c:when test='${rs.key == "User, Super (super)" && r.role.name == "Super User" }'>
		    	<td class="DataTD value-centered"><fmt:message key="sysrole.list_unchangeable.label" /></td>
		    </c:when>
		    <c:otherwise>
		    	<td class="submit value-centered"><a href="<wasp:relativeUrl value="/sysrole/remove/${r.userId}/${r.role.roleName}.do" />"><fmt:message key="sysrole.list_remove.label" /></a></td>
		    </c:otherwise>
		    </c:choose>
	    </tr>
	  </c:forEach>
	</c:forEach>
  </table>

<div>
<br />
<h1><fmt:message key="sysrole.list_create.label" /></h1>
<div class="instructions">
  <fmt:message key="sysrole.list_add_role.label" />
</div>

  <form method="POST" action="<wasp:relativeUrl value="/sysrole/add.do"/>" onsubmit='return validate();'>
  <table class="EditTable ui-widget ui-widget-content">
    <tr class="FormData">
    	<td class="CaptionTD"><fmt:message key="sysrole.list_sysuser_role.label" />: </td>
    	<td class="DataTD"><select class="FormElement ui-widget-content ui-corner-all" name="roleName">
		    <option value="" SELECTED><fmt:message key="wasp.default_select.label"/></option>
		    <c:forEach items="${role}" var="r">
		      <c:if test="${r.roleName != 'su' }"><%--2-15-13: do not permit any user (other than Super User) to be granted superuser status --%>
		      	<option value="<c:out value="${r.roleName}"/>"><c:out value="${r.name}"/></option>
		      </c:if>
		    </c:forEach>
		    </select>
		</td>
    </tr>
    <tr class="FormData">
	    <td class="CaptionTD"><fmt:message key="sysrole.list_sysuser_name.label" />: </td>
	    <td class="CaptionTD"><input class="FormElement ui-widget-content ui-corner-all" type="text" id="userHook" name="userHook" /></td>
    </tr>
 
  </table>
  <div class="submit">
    <input class="FormElement ui-widget-content ui-corner-all" type="submit" value="<fmt:message key="sysrole.list_submit.label" />" />
  </div>
</form>

  
</div>






