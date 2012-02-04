<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
  <font color="red"><wasp:message /></font> 
  <h1><fmt:message key="pageTitle.auth/newuser/form.label" /></h1>

  <div class="instructions"> <%@ include file="/WEB-INF/jsp/lorem.jsp" %> </div>

    <form:form commandName="userPending">
      <table class="data">
      	<tr>
          <td class="label"><fmt:message key="wasp.authentication.label" /> <fmt:message key="userPending.login.label" />:</td>
          <td class="input"><form:input path="login"  /></td>
          <td class="error"><form:errors path="login"/></td>
        </tr> 
        <tr>
          <td class="label"><fmt:message key="wasp.authentication.label" /> <fmt:message key="userPending.password.label"/>:</td>
          <td class="input"><form:password path="password" /></td>
          <td class="error"><form:errors path="password" /></td>
        </tr>
	    <c:if test="${isAuthenticationExternal == (1==1)}">
	    	<input type="hidden" name="password2" value="" />
        </c:if>
        <c:if test="${isAuthenticationExternal != (1==1)}">   
	        <tr>
	        	<td class="label"><fmt:message key="userPending.password2.label"/>:</td>
	        	<td class="input""><input type="password" name="password2" /></td>
	        	<td>&nbsp;</td>
	        </tr>     	   
		</c:if>
        <tr>
          <td class="label"><fmt:message key="userPending.firstName.label" />:</td>
          <td class="input"><form:input path="firstName" /></td>
          <td class="error"><form:errors path="firstName" /></td>
        </tr>
        <tr>
          <td class="label"><fmt:message key="userPending.lastName.label"/>:</td>
          <td class="input"><form:input path="lastName" /></td>
          <td class="error"><form:errors path="lastName" /></td>
        </tr>
        <tr>
          <td class="label"><fmt:message key="userPending.email.label"/>:</td>
          <td class="input"><form:input path="email" /></td>
          <td class="error"><form:errors path="email" /></td>
        </tr>      
        <tr>
          <td class="label"><fmt:message key="userPending.locale.label"/>:</td>
          <td class="input">
            <select name=locale>
              <option value=''><fmt:message key="wasp.default_select.label"/></option> 
              <c:forEach var="localeEntry" items="${locales}">
                <c:set var="localeValue" value="${localeEntry.key}"/>
                <c:set var="localeLabel" value="${localeEntry.value}"/>     
                <option value=${localeValue} <c:if test="${userPending.locale == localeValue}">selected</c:if>>${localeLabel}</option>
              </c:forEach>
            </select>
          </td>
          <td class="error"><form:errors path="locale" /></td>
        </tr>

          <c:set var="_area" value = "userPending" scope="request"/>

          <c:set var="_metaList" value = "${userPending.userPendingMeta}" scope="request" />
          <c:import url="/WEB-INF/jsp/meta_rw.jsp"/>
          <tr><td>&nbsp;</td><td><img src="<c:url value='/stickyCaptchaImg.png'/>" /></td><td>&nbsp;</td></tr>
          <tr>
          	<td class="label"><fmt:message key="userPending.captcha.label"/>:</td>
          	<td class="input"><input type="text" name="captcha" /></td>
          	<td class="error">${captchaError}</td>
          </tr>

       </table>
       <div class="submit">
         <input type="submit" value="<fmt:message key='userPending.submit.label'/>" /> 
       </div>
    </form:form>

