<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
  <p></p>  

  <h1><fmt:message key="pageTitle.lab/detail_rw.label" /> ${lab.name}</h1>

  <form:form  cssClass="FormGrid" commandName="lab">
    <input type="hidden" name='primaryUserId' value="${lab.primaryUserId}" />
    <table class="EditTable ui-widget ui-widget-content">
      <tr class="FormData">
        <td class="CaptionTD"><fmt:message key="lab.name.label" />:</td>
        <td class="DataTD"><form:input cssClass="FormElement ui-widget-content ui-corner-all" path="name" /></td>
        <td class="CaptionTD error"><form:errors path="name"/></td>
      </tr>  
      <tr class="FormData">
        <td class="CaptionTD"><fmt:message key="lab.primaryUserId.label"/>:</td>
        <td class="DataTD">
        <c:out value="${puserFullName}"/>
        </td> 
        <td>&nbsp;</td>            
      </tr>
      <!--  The PI's name should not be editable
      <tr class="FormData">
        <td class="CaptionTD"><fmt:message key="lab.primaryUserId.label"/>:</td>
        <td class="DataTD">
        <select disabled class="FormElement ui-widget-content ui-corner-all" name=primaryUserId>
        <option value='-1'><fmt:message key="wasp.default_select.label"/></option>
        <c:forEach var="puser" items="${pusers}">
        <option value="${puser.userId}" <c:if test="${puser.userId == lab.primaryUserId}"> selected</c:if>><c:out value="${puser.lastName}, ${puser.firstName}"/></option>
        </c:forEach>     
        </select>
        </td>
        <td class="CaptionTD error"><form:errors path="primaryUserId" /></td>
      </tr>
      -->
      <tr class="FormData">
        <td class="CaptionTD"><fmt:message key="lab.departmentId.label"/>:</td>
        <td class="DataTD">
        <select class="FormElement ui-widget-content ui-corner-all" name=departmentId>
        <option value='-1'><fmt:message key="wasp.default_select.label"/></option>
        <c:forEach var="dept" items="${departments}">
        <option value="${dept.departmentId}" <c:if test="${dept.departmentId == lab.departmentId}"> selected</c:if>><c:out value="${dept.name}"/></option>
        </c:forEach>     
        </select>
        </td>
        <td class="CaptionTD error"><form:errors path="departmentId" /></td>
      </tr>

      <c:set var="_area" value = "lab" scope="request"/>	
      <c:set var="_metaList" value = "${lab.labMeta}" scope="request" />		
      <c:import url="/WEB-INF/jsp/meta_rw.jsp"/>
    </table> 
    <div class="submit">
      <input class="FormElement ui-widget-content ui-corner-all" type="submit" name="submit" value="<fmt:message key="labDetail.cancel.label" />" />
      <input class="FormElement ui-widget-content ui-corner-all" type="submit" name="submit" value ="<fmt:message key="labDetail.save.label" />" />
    </div>
    </form:form>

    <table class="EditTable ui-widget ui-widget-content">            	      
    <c:forEach items="${labuser}" var="ul">
      <tr class="FormData"><td class="action">
      <a href="<wasp:relativeUrl value='user/detail/${ul.user.userId}.do' />">${ul.user.login}</a></td>
	<td class="DataTD">
        <c:out value="${ul.user.firstName}" />
        <c:out value="${ul.user.lastName}" />
	</td>
	<td class="DataTD">
          <span><c:out value="${ul.role.name}" /></span>
	</td>

    </c:forEach>
    </table>

    <c:if test="${!  empty( job ) }">   
    <h1>Jobs</h1>
    <table class="EditTable ui-widget ui-widget-content">            	      
    <c:forEach items="${job}" var="j">
      <tr class="FormData"><td class="action"><td class="DataTD">
          <a href="<wasp:relativeUrl value='job/detail/${j.jobId}.do' />">
          <c:out value="${j.name}" />
          </a>
      </td>
    </c:forEach>
    </table>
    </c:if>

