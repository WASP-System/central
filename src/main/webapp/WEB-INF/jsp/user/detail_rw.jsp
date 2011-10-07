<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
    
    <h1><fmt:message key="userDetail.page_title.label" /></h1>

    <font color="blue"><wasp:message /></font>  
    
    <!-- 
    <c:forEach var="robert" items="${dubin}">
    	<c:out value="${robert}" /><br />
    </c:forEach >  
    -->
    
    <form:form commandName="user">
    
     <table>
     	   <c:if test="${user.userId == 0}">
     	   <tr>
              <td><fmt:message key="user.login.label" />:</td>
              <td><form:input path="login" /></td>
              <td><form:errors path="login"/></td>
          </tr>
          <!-- tr>
              <td><fmt:message key="user.password.label"/>:</td>
              <td><form:password path="password" /></td>
              <td><form:errors path="password" /></td>
          </tr-->     	   
     	  </c:if>	
     	  <c:if test="${user.userId != 0}">
     	   <tr>
              <td><fmt:message key="user.login.label" />:</td>
              <td>${user.login}<form:hidden path="login"/></td>              
          </tr>
          <!-- tr>
              <td><fmt:message key="user.password.label"/>:</td>
              <td><input type="password" value=""/></td>
              <td><form:errors path="password" /></td>
          </tr-->     	   
     	   </c:if>	 
          <tr>
              <td><fmt:message key="user.firstName.label" />:</td>
              <td><form:input path="firstName" /></td>
              <td><form:errors path="firstName"/></td>
          </tr>
          <tr>
              <td><fmt:message key="user.lastName.label"/>:</td>
              <td><form:input path="lastName" /></td>
              <td><form:errors path="lastName" /></td>
          </tr>
          <tr>
              <td><fmt:message key="user.email.label"/>:</td>
              <td><form:input path="email" /></td>
              <td><form:errors path="email" /></td>
          </tr>         
          <tr>
              <td><fmt:message key="user.locale.label"/>:</td>
              <td>
              <select name=locale>
                <option value=''>-- select --</option>
                 <c:forEach var="localeEntry" items="${locales}">
                    <c:set var="localeValue" value="${localeEntry.key}"/>
                    <c:set var="localeLabel" value="${localeEntry.value}"/>        
                    <option value=${localeValue} <c:if test="${user.locale == localeValue}">selected</c:if>>${localeLabel}</option>                           
                </c:forEach>
              </select>
              </td>
              <td><form:errors path="locale" /></td>
          </tr>
          <c:set var="_area" value = "user" scope="request"/>	
		  <c:set var="_metaList" value = "${user.userMeta}" scope="request" />		
          <c:import url="/WEB-INF/jsp/meta_rw.jsp"/>
          <tr>
              <td colspan="2" align=left>
              	  <!-- input type="button" onclick='location="/wasp/user/me_ro.do"' value="Cancel" /--> 
              	  <a href="/wasp/user/me_ro.do"><input type="button" value="<fmt:message key="userDetail.cancel.label" />" /></a>
                  <input type="submit" value="<fmt:message key="userDetail.save.label" />" />
              </td>
          </tr>    
         <c:if  test="${user.userId > 0}">
         <tr><td colspan=2 align=left></br><b>Lab Users:</b></td></tr>
         <c:forEach items="${user.labUser}" var="ul">
    	  <tr>
            <td><a href="/wasp/lab/detail_ro/<c:out value="${ul.lab.departmentId}" />/<c:out value="${ul.lab.labId}" />.do"><c:out value="${ul.lab.name}" /></a></td>
            <td><span><c:out value="${ul.role.name}" /></span></td>
          </tr>
        </c:forEach>
          
		 <tr><td colspan=2 align=left></br><b>Samples:</b></td></tr>
         <c:forEach items="${user.sample}" var="sample">
    	  <tr>
            <td><a href="/wasp/sample/detail/<c:out value="${sample.sampleId}" />.do"><c:out value="${sample.name}" /></a></td>           
          </tr>
        </c:forEach>
		
         <tr><td colspan=2 align=left></br><b>Jobs:</b></td></tr>
         <c:forEach items="${user.job}" var="job">
    	  <tr>
            <td><a href="/wasp/job/detail/<c:out value="${job.jobId}" />.do"><c:out value="${job.name}" /></a></td>           
          </tr>
        </c:forEach>  
       </c:if>
     </table>
  
    </form:form>
