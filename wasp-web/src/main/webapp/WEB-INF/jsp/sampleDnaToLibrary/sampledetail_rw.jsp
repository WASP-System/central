<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
<wasp:message />  <br />
<h1>Update Sample Details</h1>
<table class="data">
<tr><td class="label">Job ID</td><td class="value">J<c:out value="${job.jobId}" /></td></tr>
<tr><td class="label">Job Name</td><td class="value"><c:out value="${job.name}" /></td></tr>
<tr><td class="label">Submitter</td><td class="value"><c:out value="${job.user.firstName}" /> <c:out value="${job.user.lastName}" /></td></tr>
<tr><td class="label">PI</td><td class="value"><c:out value="${job.lab.user.firstName}" /> <c:out value="${job.lab.user.lastName}" /></td></tr>
<tr><td class="label">Submitted</td><td class="value"><fmt:formatDate value="${job.createts}" type="date" /></td></tr>
<tr><td class="label">Workflow</td><td class="value"><c:out value="${job.workflow.name}" /></td></tr>
</table>
<br />
<form:form commandName="sample">
  <input type='hidden' name='jobId' value='<c:out value="${job.jobId}" />'/>

<table class="data">
  	 <tr>
      <td class="label">Sample Name</td>
      <td class="input"><form:input path="name" /><span class="requiredField">*</span></td>
      <td class="error"><form:errors path="name" /></td>
     </tr>
     <c:set var="_area" value = "sample" scope="request"/>
	 <c:set var="_metaList" value = "${sample.sampleMeta}" scope="request" />		
     <c:import url="/WEB-INF/jsp/meta_rw.jsp"/>
     <sec:authorize access="hasRole('su') or hasRole('ft')">
    <tr>
              <td colspan="2" align="left" class="submit">
              	  <input type="submit" name="submit" value="Cancel" />
                  <input type="submit" name="submit" value="Save" />
              </td>
              <td>&nbsp;</td>
          </tr>
     </sec:authorize>
</table>
</form:form>