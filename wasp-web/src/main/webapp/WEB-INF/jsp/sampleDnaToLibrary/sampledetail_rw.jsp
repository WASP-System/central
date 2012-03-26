<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
  <br />
<h1>Update Sample Details</h1>
<table class="EditTable ui-widget ui-widget-content">
<tr class="FormData"><td class="CaptionTD">Job ID:</td><td class="DataTD">J<c:out value="${job.jobId}" /></td></tr>
<tr class="FormData"><td class="CaptionTD">Job Name:</td><td class="DataTD"><c:out value="${job.name}" /></td></tr>
<tr class="FormData"><td class="CaptionTD">Submitter:</td><td class="DataTD"><c:out value="${job.user.firstName}" /> <c:out value="${job.user.lastName}" /></td></tr>
<tr class="FormData"><td class="CaptionTD">PI:</td><td class="DataTD"><c:out value="${job.lab.user.firstName}" /> <c:out value="${job.lab.user.lastName}" /></td></tr>
<tr class="FormData"><td class="CaptionTD">Submitted:</td><td class="DataTD"><fmt:formatDate value="${job.createts}" type="date" /></td></tr>
<tr class="FormData"><td class="CaptionTD">Workflow:</td><td class="DataTD"><c:out value="${job.workflow.name}" /></td></tr>
<c:forEach items='${extraJobDetailsMap}' var="detail">
	<tr class="FormData"><td class="CaptionTD">  <c:out value='${detail.key}' />   </td><td class="DataTD"> <c:out value='${detail.value}' /> </td></tr>
</c:forEach>
</table>
<br />
<form:form  cssClass="FormGrid" commandName="sample">
  <input class="FormElement ui-widget-content ui-corner-all" type='hidden' name='jobId' value='<c:out value="${job.jobId}" />'/>
  <input class="FormElement ui-widget-content ui-corner-all" type='hidden' name='sampleTypeId' value='<c:out value="${sample.sampleTypeId}" />'/>	
<table class="EditTable ui-widget ui-widget-content">
  	 <tr class="FormData">
      <td class="CaptionTD">Sample Name:</td>
      <td class="DataTD"><form:input cssClass="FormElement ui-widget-content ui-corner-all" path="name" /><span class="requiredField">*</span></td>
      <td class="CaptionTD error"><form:errors path="name" /></td>
     </tr>
     <tr class="FormData"><td class="CaptionTD">Sample Type:</td><td class="DataTD"><c:out value="${sample.sampleType.name}" /></td><td class="CaptionTD error"><form:errors path="" /></td></tr>
     <c:set var="_area" value = "sample" scope="request"/>
	 <c:set var="_metaList" value = "${sample.sampleMeta}" scope="request" />		
     <c:import url="/WEB-INF/jsp/meta_rw.jsp" />
     <sec:authorize access="hasRole('su') or hasRole('ft')">
    <tr class="FormData">
        <td colspan="3" align="left" class="submitBottom">
        	<input class="FormElement ui-widget-content ui-corner-all" type="submit" name="submit" value="Cancel" />
            <input class="FormElement ui-widget-content ui-corner-all" type="submit" name="submit" value="Save" />
        </td>
    </tr>
    </sec:authorize>
</table>
</form:form>