<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
   
   <%--  TODO: Declare style in css file (e.g. /src/main/webapp/css/base.css), not in .jsp and reuse where possible !!!! --%>
   
  <h1><fmt:message key="piPending.form_header.label" /></h1>
  
  <div class="instructions"> 
  	<fmt:message key="piPending.form_instructions.label" />
  	<br /><br />
  	<c:choose>
		<c:when test="${isAuthenticationExternal == true && isHostInstitute == true}">
	  		<div id="passwordInstructions"><fmt:message key="piPending.form_instructions_external.label" /></div>
	 	</c:when>
  		<c:otherwise">
	  		<div id="passwordInstructions"><fmt:message key="piPending.password_instructions.label" /></div>
	 	</c:otherwise>
	</c:choose>
  </div>
  <c:set var="actionUrl"><wasp:relativeUrl value='auth/newpi/form.do' /></c:set>
  <form:form  cssClass="FormGrid" commandName="userPending" action="${actionUrl}">
    <table class="EditTable ui-widget ui-widget-content">
      <tr class="FormData">
        <td class="CaptionTD">
        <c:choose>
	        <c:when test="${isAuthenticationExternal == true && isHostInstitute == true}">
	          	<fmt:message key="wasp.authentication_external.label" /> 
	        </c:when>
	        <c:otherwise>
	         	<fmt:message key="wasp.authentication_internal.label" /> 
	        </c:otherwise>>
        </c:choose>
        <fmt:message key="piPending.login.label" />:</td>
        <td class="DataTD"><form:input cssClass="FormElement ui-widget-content ui-corner-all" path="login"  /><span class="requiredField">*</span></td>
        <td class="CaptionTD error"><form:errors path="login"/></td>
      </tr> 
      <c:if test="${isAuthenticationExternal == false || isHostInstitute == false}">
	        <tr>
	          <td >&nbsp;</td>
	          <td ><div style="color:black;font-size:11px;font-weight:bold;"><fmt:message key="userPending.login_instructions_above.label" /></div></td>
	          <td >&nbsp;</td>
	        </tr>
	  </c:if>
      <tr class="FormData">
        <td class="CaptionTD"><fmt:message key="piPending.password.label"/>:</td>
        <td class="DataTD"><form:password path="password" cssClass="FormElement ui-widget-content ui-corner-all" onFocus="var x = document.getElementById('passwordInstructions'); x.style.color='red';x.style.fontWeight='bold'; var y = document.getElementById('passwordInstructions2'); y.style.color='red';"  /><span class="requiredField" >*</span></td>
        <td class="CaptionTD error"><form:errors path="password" />
      <c:choose>
		<c:when test="${isAuthenticationExternal == true && isHostInstitute == true}">  	   
	      <input class="FormElement ui-widget-content ui-corner-all" type="hidden" name="password2" value="" /></td></tr> 
      	</c:when>
      	<c:otherwise>   
      	  </td></tr> 
	      <tr>
	          <td >&nbsp;</td>
	          <td ><div id="passwordInstructions2" style="color:black;font-size:11px;font-weight:bold;"><fmt:message key="piPending.password_instructions_above.label" /></div></td>
	          <td >&nbsp;</td>
	      </tr> 
	      <tr class="FormData">
			<td class="CaptionTD"><fmt:message key="piPending.password2.label"/>:</td>
		        <td class="DataTD"><input class="FormElement ui-widget-content ui-corner-all" type="password" name="password2" /><span class="requiredField">*</span></td>
			<td class="CaptionTD error">&nbsp;</td>
	      </tr>     	   
     	 </c:otherwise>
      </c:choose>
      <tr class="FormData">
        <td class="CaptionTD"><fmt:message key="piPending.firstName.label" />:</td>
        <td class="DataTD"><form:input cssClass="FormElement ui-widget-content ui-corner-all" path="firstName" /><span class="requiredField">*</span></td>
        <td class="CaptionTD error"><form:errors path="firstName" /></td>
      </tr>
      <tr class="FormData">
        <td class="CaptionTD"><fmt:message key="piPending.lastName.label"/>:</td>
        <td class="DataTD"><form:input cssClass="FormElement ui-widget-content ui-corner-all" path="lastName" /><span class="requiredField">*</span></td>
        <td class="CaptionTD error"><form:errors path="lastName" /></td>
      </tr>
      <tr class="FormData">
        <td class="CaptionTD"><fmt:message key="piPending.email.label"/>:</td>
        <td class="DataTD"><form:input cssClass="FormElement ui-widget-content ui-corner-all" path="email" /><span class="requiredField">*</span></td>
        <td class="CaptionTD error"><form:errors path="email" /></td>
      </tr>         
      <tr class="FormData">
        <td class="CaptionTD"><fmt:message key="piPending.locale.label"/>:</td>
        <td class="DataTD">
          <select class="FormElement ui-widget-content ui-corner-all" name=locale>
          <option value=''><fmt:message key="piPending.select_default.label"/></option>
          <c:forEach var="localeEntry" items="${locales}">
          <c:set var="localeValue" value="${localeEntry.key}"/>
          <c:set var="localeLabel" value="${localeEntry.value}"/>     
          <option value='${localeValue}' <c:if test="${userPending.locale == localeValue}">selected</c:if>>${localeLabel}</option>
          </c:forEach>
          </select><span class="requiredField">*</span>
        </td>
        <td class="CaptionTD error">&nbsp;</td>
      </tr>

      <c:set var="_area" value = "userPending" scope="request"/>
      <c:set var="_metaArea" value = "piPending" scope="request"/>
          
     <c:set var="_metaList" value = "${userPending.userPendingMeta}" scope="request" />
     <c:import url="/WEB-INF/jsp/meta_rw.jsp"/>
     <tr class="FormData">
          	<td class="CaptionTD"><fmt:message key="piPending.captcha.label"/>:</td>
          	<td class="DataTD"><img src="<wasp:relativeUrl value='/stickyCaptchaImg.png'/>" alt='Captcha Image'/><br /><input class="FormElement ui-widget-content ui-corner-all" type="text" name="captcha" /><span class="requiredField">*</span></td>
          	<td class="CaptionTD error">${captchaError}</td>
      </tr>
    </table>
    <div class="submit">
    	<input class="FormElement ui-widget-content ui-corner-all" type="submit" value="<fmt:message key='piPending.submit.label'/>" /> 
    </div>
    </form:form>


