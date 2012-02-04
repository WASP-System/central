<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<font color="blue"><wasp:message /></font>

<form:form commandName="sample">

  <table><tr>
    <td><fmt:message key="sample.name.label" />:</td>
    <td><form:input path="name" /></td>
    <td><form:errors path="name"/></td>
    </tr>


 <c:set var="_area" value = "sample" scope="request"/>
 <c:set var="_metaArea" value = "platformunit" scope="request"/>

 <c:set var="_metaList" value = "${sample.sampleMeta}" scope="request" />
 <c:import url="/WEB-INF/jsp/meta_rw.jsp"/>

  </table>

  <input type="submit">
</form:form>
