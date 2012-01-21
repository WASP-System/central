<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
    
    <font color="red"><wasp:message /></font>  

    <h1><fmt:message key="pageTitle.user/detail_rw.label" /></h1>

    <div class="instructions">
      <%@ include file="/WEB-INF/jsp/lorem.jsp" %>
    </div>

    <form:form commandName="user">
     <table class="data">
     	   <tr>
              <td class="label"><fmt:message key="wasp.authentication.label" /> <fmt:message key="user.login.label" /></td>
              <td class="input"><form:input path="login"  readonly="true" /></td>
              <td class="error"><form:errors path="login"/></td>
          </tr> 	   	
          <tr>
              <td class="label"><fmt:message key="user.firstName.label" /></td>
              <td class="input"><form:input path="firstName" /></td>
              <td class="error"><form:errors path="firstName" /></td>
          </tr>
          <tr>
              <td class="label"><fmt:message key="user.lastName.label"/></td>
              <td class="input"><form:input path="lastName" /></td>
              <td class="error"><form:errors path="lastName" /></td>
          </tr>
          <tr>
              <td class="label"><fmt:message key="user.email.label"/></td>
              <td class="input"><form:input path="email" /></td>
              <td class="error"><form:errors path="email" /></td>
          </tr>         
          <tr>
              <td class="label"><fmt:message key="user.locale.label"/></td>
              <td class="input">
              <select name=locale>
                <option value=''><fmt:message key="wasp.default_select.label"/></option>
                 <c:forEach var="localeEntry" items="${locales}">
                    <c:set var="localeValue" value="${localeEntry.key}"/>
                    <c:set var="localeLabel" value="${localeEntry.value}"/>        
                    <option value=${localeValue} <c:if test="${user.locale == localeValue}">selected</c:if>>${localeLabel}</option>                           
                </c:forEach>
              </select>
              </td>
              <td class="error"><form:errors path="locale" /></td>
          </tr>
          <c:set var="_area" value = "user" scope="request"/>	
		  <c:set var="_metaList" value = "${user.userMeta}" scope="request" />		
          <c:import url="/WEB-INF/jsp/meta_rw.jsp"/>
          <tr>
              <td colspan="2" align="left" class="submit">
              	  <input type="submit" name="submit" value="<fmt:message key="userDetail.cancel.label" />" />
                  <input type="submit" name="submit" value="<fmt:message key="userDetail.save.label" />" />
              </td>
              <td>&nbsp;</td>
          </tr>
	</table>
    </form:form>


 
<c:if  test="${user.userId > 0}">
	<table class="data list">
         <tr><td colspan=2 align=left></br><b><fmt:message key="user.labusers.label" /></b></td></tr>
         <c:forEach items="${user.labUser}" var="ul">
    	  <tr>
            <td><a href="/wasp/lab/detail_ro/<c:out value="${ul.lab.departmentId}" />/<c:out value="${ul.lab.labId}" />.do"><c:out value="${ul.lab.name}" /></a></td>
            <td><span><c:out value="${ul.role.name}" /></span></td>
          </tr>
        </c:forEach>
          
</table>
	<table class="data list">
		 <tr><td colspan=2 align=right></br><b><fmt:message key="user.samples.label" /></b></td></tr>
         <c:forEach items="${user.sample}" var="sample">
    	  <tr>
            <td><a href="/wasp/sample/detail/<c:out value="${sample.sampleId}" />.do"><c:out value="${sample.name}" /></a></td>           
          </tr>
        </c:forEach>

</table>
	<table class="data list">
		
         <tr><td colspan=2 align=left></br><b><fmt:message key="user.jobs.label" /></b></td></tr>
         <c:forEach items="${user.job}" var="job">
    	  <tr>
            <td><a href="/wasp/job/detail/<c:out value="${job.jobId}" />.do"><c:out value="${job.name}" /></a></td>           
          </tr>
        </c:forEach>  
     </table>
     </c:if>
  
