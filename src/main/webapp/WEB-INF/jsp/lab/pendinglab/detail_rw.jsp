<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
  <font color="blue"><wasp:message /></font>  
  <form:form commandName="labpending">
   <table>
     	  <tr><td colspan=2 align=left></br><b>Pending Lab Details:</b></td></tr>
           <tr>
              <td><fmt:message key="labPending.name.label" />:</td>
              <td><form:input path="name" /></td>
              <td><form:errors path="name"/></td>
          </tr>
          <tr>
              <td><fmt:message key="labPending.primaryUserId.label"/>:</td>
              <td>
              <select name=primaryUserId>
                <option value='-1'>-- select --</option>
              	<c:forEach var="puser" items="${pusers}">
                	<option value="${puser.userId}" <c:if test="${puser.userId == labpending.primaryUserId}"> selected</c:if>><c:out value="${puser.lastName}, ${puser.firstName}"/></option>
               	</c:forEach>     
              </select>
              </td>
              <td><form:errors path="primaryUserId" /></td>
          </tr>
          <tr>
              <td><fmt:message key="labPending.departmentId.label"/>:</td>
              <td>
              <select name=departmentId>
                <option value='-1'>-- select --</option>
              	<c:forEach var="dept" items="${departments}">
                	<option value="${dept.departmentId}" <c:if test="${dept.departmentId == labpending.departmentId}"> selected</c:if>><c:out value="${dept.name}"/></option>
               	</c:forEach>     
              </select>
              </td>
              <td><form:errors path="departmentId" /></td>
          </tr>
          <c:set var="_area" value = "labPending" scope="request"/>	
		  <c:set var="_metaList" value = "${labpending.labPendingMeta}" scope="request" />		
          <c:import url="/WEB-INF/jsp/meta_rw.jsp"/>
          <tr>
              <td colspan="2" align=right>
                  <input type="submit" value="Save Changes" />
              </td>
          </tr>    
            	      
   </table> 
   </form:form>
