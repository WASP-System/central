<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
    
      

    <h1><fmt:message key="pageTitle.user/detail_rw.label" /></h1>
    <form:form  commandName="user" cssClass="FormGrid">
     <table class="EditTable ui-widget ui-widget-content">
     	   <tr class="FormData">
              <td class="CaptionTD">
              <c:if test="${isAuthenticationExternal == true}">
	          	<fmt:message key="wasp.authentication_external.label" /> 
	          </c:if>
	          <c:if test="${isAuthenticationExternal == false}">
	          	<fmt:message key="wasp.authentication_internal.label" /> 
	          </c:if>
              <fmt:message key="user.login.label" />:</td>
              <td class="DataTD"><form:input path="login"  readonly="true" cssClass="FormElement ui-widget-content ui-corner-all" /></td>
              <td class="CaptionTD error"><form:errors path="login"/></td>
          </tr> 	   	
          <tr class="FormData">
              <td class="CaptionTD"><fmt:message key="user.firstName.label" />:</td>
              <td class="DataTD"><form:input path="firstName" cssClass="FormElement ui-widget-content ui-corner-all"/><span class="requiredField">*</span></td>
              <td class="CaptionTD error"><form:errors path="firstName" /></td>
          </tr>
          <tr class="FormData">
              <td class="CaptionTD"><fmt:message key="user.lastName.label"/>:</td>
              <td class="DataTD"><form:input path="lastName" cssClass="FormElement ui-widget-content ui-corner-all"/><span class="requiredField">*</span></td>
              <td class="CaptionTD error"><form:errors path="lastName" /></td>
          </tr>
          <tr class="FormData">
              <td class="CaptionTD"><fmt:message key="user.email.label"/>:</td>
              <td class="DataTD"><form:input path="email" cssClass="FormElement ui-widget-content ui-corner-all"/><span class="requiredField">*</span></td>
              <td class="CaptionTD error"><form:errors path="email" /></td>
          </tr>         
          <tr class="FormData">
              <td class="CaptionTD"><fmt:message key="user.locale.label"/>:</td>
              <td class="DataTD">
              <select name="locale" class="FormElement ui-widget-content ui-corner-all">
                <option value=''><fmt:message key="wasp.default_select.label"  /></option>
                 <c:forEach var="localeEntry" items="${locales}">
                    <c:set var="localeValue" value="${localeEntry.key}"/>
                    <c:set var="localeLabel" value="${localeEntry.value}"/>        
                    <option value=${localeValue} <c:if test="${user.locale == localeValue}">selected</c:if>>${localeLabel}</option>                           
                </c:forEach>
              </select><span class="requiredField">*</span>
              </td>
              <td class="CaptionTD error"><form:errors path="locale" /></td>
          </tr>
          <c:set var="_area" value = "user" scope="request"/>	
		  <c:set var="_metaList" value = "${user.userMeta}" scope="request" />		
          <c:import url="/WEB-INF/jsp/meta_rw.jsp"/>
          <tr class="FormData">
              <td colspan="3" align="left" class="submitBottom">
              	  <input type="submit" name="submit" value="<fmt:message key="userDetail.cancel.label" />" />
                  <input type="submit" name="submit" value="<fmt:message key="userDetail.save.label" />" />
              </td>
          </tr>
	</table>
    </form:form>

  
