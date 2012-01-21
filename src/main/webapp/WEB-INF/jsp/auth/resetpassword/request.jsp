<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

  <font color="red"><wasp:message /></font>  
     
  <h1><fmt:message key="pageTitle.auth/resetpassword/request.label" /></h1>

  <div class="instructions"> <%@ include file="/WEB-INF/jsp/lorem.jsp" %> </div>

  <form name="f" action="<c:url value='/auth/resetpassword/request.do'/>" method="POST">
     <table class="data">
       <tr><td class="label"><fmt:message key="auth.resetpasswordRequest_user.label" /></td><td class="input"><input type='text' name='username' value='<c:out value="${username}" default="" />' /></td></tr>
        <tr><td>&nbsp;</td><td class="input"><img src="<c:url value='/stickyCaptchaImg.png'/>" /></td></tr>
        <tr><td class="label"><fmt:message key="auth.resetpasswordRequest_captcha.label" /></td><td class="input"><input type='text' name='captcha_text' value=''/></td></tr>
      </table>
    <div>
      <input name="submit" type="submit" value="<fmt:message key="auth.resetpasswordRequest_submit.label" />"/>
    </div>
  </form>


<div class="bottomtxt"> <%@ include file="/WEB-INF/jsp/lorem.jsp" %> </div>

