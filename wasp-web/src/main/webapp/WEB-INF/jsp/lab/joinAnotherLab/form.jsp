<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<h1><fmt:message key="pageTitle.lab/joinAnotherLab/form.label" /></h1>
  <div class="instructions"><fmt:message key="lab.joinAnotherLabInstructions.label" /></div>
  <form name="f" action="<wasp:relativeUrl value='/lab/joinAnotherLab.do'/>" onsubmit="return validate();" method="POST">
  <table class="EditTable ui-widget ui-widget-content"><tr class="FormData">
    <td class="CaptionTD"><fmt:message key="lab.joinAnotherLab_emailOfPI.label" />: </td>
    <td class="DataTD"><br /><input class="FormElement ui-widget-content ui-corner-all" type='text' id='piEmail' name='piEmail' value=''/><span class="requiredField">*</span></td>
  </tr></table>
  <div class="submit">
    <input class="FormElement ui-widget-content ui-corner-all" type="submit" value="<fmt:message key="lab.joinAnotherLab_submit.label" />" />
  </div>
  </form>