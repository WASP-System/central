<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

  <wasp:message />
     
  <h1><fmt:message key="pageTitle.auth/resetpassword/request.label" /></h1>

  <div class="instructions"> <fmt:message key="auth.resetpassword_start_instructions.label" /> </div>

  <form name="f" action="<c:url value='/auth/resetpassword/request.do'/>" method="POST">
     <table class="data">
       <tr>
       		<td class="label"><fmt:message key="auth.resetpasswordRequest_user.label" /></td>
       		<td class="input"><input type='text' name='username' value='<c:out value="${username}" default="" />' /><span class="requiredField">*</span></td>
       </tr>
        <tr>
        	<td class="label"><fmt:message key="auth.resetpasswordRequest_captcha.label" /></td>
        	<td class="input"><img src="<c:url value='/stickyCaptchaImg.png'/>" /><br /><input type='text' name='captcha_text' value=''/><span class="requiredField">*</span></td>
        </tr>
      </table>
    <div>
      <input name="submit" type="submit" value="<fmt:message key="auth.resetpasswordRequest_submit.label" />"/>
    </div>
  </form>


