<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
  <p><font color="red"><wasp:message /></font></p>  
  <form:form commandName="lab">
   <table>
     	  <tr><td colspan=2 align=left></br><b><fmt:message key="pageTitle.lab/detail_rw.label" />:</b></td></tr>
           <tr>
              <td><fmt:message key="lab.name.label" />:</td>
              <td><form:input path="name" /></td>
              <td><form:errors path="name"/></td>
          </tr>
          <tr>
              <td><fmt:message key="lab.primaryUserId.label"/>:</td>
              <td>
              <select name=primaryUserId>
                <option value='-1'><fmt:message key="wasp.default_select.label"/></option>
              	<c:forEach var="puser" items="${pusers}">
                	<option value="${puser.userId}" <c:if test="${puser.userId == lab.primaryUserId}"> selected</c:if>><c:out value="${puser.lastName}, ${puser.firstName}"/></option>
               	</c:forEach>     
              </select>
              </td>
              <td><form:errors path="primaryUserId" /></td>
          </tr>
          <tr>
              <td><fmt:message key="lab.departmentId.label"/>:</td>
              <td>
              <select name=departmentId>
                <option value='-1'><fmt:message key="wasp.default_select.label"/></option>
              	<c:forEach var="dept" items="${departments}">
                	<option value="${dept.departmentId}" <c:if test="${dept.departmentId == lab.departmentId}"> selected</c:if>><c:out value="${dept.name}"/></option>
               	</c:forEach>     
              </select>
              </td>
              <td><form:errors path="departmentId" /></td>
          </tr>
          <c:set var="_area" value = "lab" scope="request"/>	
		  <c:set var="_metaList" value = "${lab.labMeta}" scope="request" />		
          <c:import url="/WEB-INF/jsp/meta_rw.jsp"/>
          <tr>
              <td colspan="2" align=right>
                  <input type="submit" name="submit" value="<fmt:message key="labDetail.cancel.label" />" />
                  <input type="submit" name="submit" value ="<fmt:message key="labDetail.save.label" />" />
              </td>
              <td>&nbsp;</td>
          </tr>    
            	      
   </table> 
    <c:forEach items="${labuser}" var="ul">
      <p>
      <a href="/wasp/user/detail/<c:out value="${ul.user.userId}" />.do"><c:out value="${ul.user.login}" /></a>
        <c:out value="${ul.user.firstName}" />
        <c:out value="${ul.user.lastName}" />
      <span><c:out value="${ul.role.name}" /></span>
      </p>
    </c:forEach>

    <c:if test="${!  empty( job ) }">   
      <h2>Jobs</h2>
      <c:forEach items="${job}" var="j">
        <div>
          <a href="/wasp/job/detail/<c:out value="${j.jobId}" />.do">
          <c:out value="${j.name}" />
          </a>
        </div>
      </c:forEach>
    </c:if>

   </form:form>
