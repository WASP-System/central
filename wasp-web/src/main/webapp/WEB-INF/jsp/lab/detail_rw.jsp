<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
  <p><wasp:message /></p>  

  <h1><fmt:message key="pageTitle.lab/detail_rw.label" /> ${lab.name}</h1>

  <div class="instructions"> <%@ include file="/WEB-INF/jsp/lorem.jsp" %> </div>

  <form:form commandName="lab">
    <table class="data">
      <tr>
        <td class="label"><fmt:message key="lab.name.label" />:</td>
        <td class="input"><form:input path="name" /></td>
        <td class="error"><form:errors path="name"/></td>
      </tr>
      <tr>
        <td class="label"><fmt:message key="lab.primaryUserId.label"/>:</td>
        <td class="input">
        <select name=primaryUserId>
        <option value='-1'><fmt:message key="wasp.default_select.label"/></option>
        <c:forEach var="puser" items="${pusers}">
        <option value="${puser.userId}" <c:if test="${puser.userId == lab.primaryUserId}"> selected</c:if>><c:out value="${puser.lastName}, ${puser.firstName}"/></option>
        </c:forEach>     
        </select>
        </td>
        <td class="error"><form:errors path="primaryUserId" /></td>
      </tr>
      <tr>
        <td class="label"><fmt:message key="lab.departmentId.label"/>:</td>
        <td class="input">
        <select name=departmentId>
        <option value='-1'><fmt:message key="wasp.default_select.label"/></option>
        <c:forEach var="dept" items="${departments}">
        <option value="${dept.departmentId}" <c:if test="${dept.departmentId == lab.departmentId}"> selected</c:if>><c:out value="${dept.name}"/></option>
        </c:forEach>     
        </select>
        </td>
        <td class="error"><form:errors path="departmentId" /></td>
      </tr>

      <c:set var="_area" value = "lab" scope="request"/>	
      <c:set var="_metaList" value = "${lab.labMeta}" scope="request" />		
      <c:import url="/WEB-INF/jsp/meta_rw.jsp"/>
    </table> 
    <div class="submit">
      <input type="submit" name="submit" value="<fmt:message key="labDetail.cancel.label" />" />
      <input type="submit" name="submit" value ="<fmt:message key="labDetail.save.label" />" />
    </div>
    </form:form>

    <table class="data list">            	      
    <c:forEach items="${labuser}" var="ul">
      <tr><td class="action">
      <a href="/wasp/user/detail/<c:out value="${ul.user.userId}" />.do"><c:out value="${ul.user.login}" /></a></td>
	<td>
        <c:out value="${ul.user.firstName}" />
        <c:out value="${ul.user.lastName}" />
	</td>
	<td>
          <span><c:out value="${ul.role.name}" /></span>
	</td>
      </p>
    </c:forEach>
    </table>

    <c:if test="${!  empty( job ) }">   
    <h1>Jobs</h1>
    <table class="data list">            	      
    <c:forEach items="${job}" var="j">
      <tr><td class="action">
          <a href="/wasp/job/detail/<c:out value="${j.jobId}" />.do">
          <c:out value="${j.name}" />
          </a>
      </td>
    </c:forEach>
    </c:if>

