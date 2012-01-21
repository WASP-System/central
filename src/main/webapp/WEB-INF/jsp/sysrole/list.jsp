<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<font color="red"><wasp:message /></font></div>
<h1><fmt:message key="pageTitle.sysrole/list.label" /></h1>

<div class="instructions">
  <fmt:message key="sysrole.list_current.label" />
</div>

<table class="data tab-data">
<tr>
  	<th class="label">Name (Login Name)</th>
  	<th class="label">Role</th>
  	<th class="label">Action</th>
</tr>

  <c:forEach items="${userRoleMap}" var="rs">
	  <c:forEach items="${rs.value}" var="r">
	  	<tr>
		    <td class="value"><c:out value="${rs.key}" /></td>
		    <td class="value"><c:out value="${r.role.name}" /></td>
		    <td class="action"><a href="<c:url value="/sysrole/remove/${r.userId}/${r.role.roleName}.do" />">Remove Role</a></td>
	    </tr>
	  </c:forEach>
	</c:forEach>
  </table>
</div>

<div>

<h1><fmt:message key="sysrole.list_create.label" /></h1>
<div class="instructions">
  <%@ include file="/WEB-INF/jsp/lorem.jsp" %>
</div>

  <form method="POST" action="<c:url value="/sysrole/add.do"/>" onsubmit='return validate();'>
  <table class="data">
    <tr>
    	<td class="label"><fmt:message key="sysrole.list_sysuser_role.label" />: </td>
    	<td class="input"><select name="roleName">
		    <option value="" SELECTED><fmt:message key="wasp.default_select.label"/></option>
		    <c:forEach items="${role}" var="r">
		      <option value="<c:out value="${r.roleName}"/>">
		         <c:out value="${r.name}"/>
		      </option>
		    </c:forEach>
		    </select>
		</td>
    </tr>
    <tr>
	    <td class="label"><fmt:message key="sysrole.list_sysuser_name.label" />: </td>
	    <td class="input"><input type="text" id="userHook" name="userHook" /></td>
    </tr>
 
  </table>
  <div class="submit">
    <input type="submit" value="<fmt:message key="sysrole.list_submit.label" />" />
  </div>
</form>

  
</div>






