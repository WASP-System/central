<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
<wasp:message />  <br />
<h1>Create New Library</h1>
<table class="data">
<tr><td class="label">Job ID</td><td class="value">J<c:out value="${job.jobId}" /></td></tr>
<tr><td class="label">Job Name</td><td class="value"><c:out value="${job.name}" /></td></tr>
<tr><td class="label">Submitter</td><td class="value"><c:out value="${job.user.firstName}" /> <c:out value="${job.user.lastName}" /></td></tr>
<tr><td class="label">PI</td><td class="value"><c:out value="${job.lab.user.firstName}" /> <c:out value="${job.lab.user.lastName}" /></td></tr>
<tr><td class="label">Submitted</td><td class="value"><fmt:formatDate value="${job.createts}" type="date" /></td></tr>
<tr><td class="label">Workflow</td><td class="value"><c:out value="${job.workflow.name}" /></td></tr>
<c:if test="${otherAdaptorsets.size() > 0}">
	<tr><td colspan="2">
		<form method="GET" action="<c:url value="/sampleDnaToLibrary/createLibraryFromMacro.do" />">
	  		<input type='hidden' name='jobId' value='<c:out value="${job.jobId}" />'/>
	  		<input type='hidden' name='macromolSampleId' value='<c:out value="${macromoleculeSample.sampleId}" />'/>
	  		
				<select name="adaptorsetId" size="1" onchange="if(this.options[selectedIndex].value != 0){this.parentNode.submit();}">
				<option value="0">--SELECT NEW ADAPTOR SET--
				<c:forEach items="${otherAdaptorsets}" var="adaptorset">
					<option value="<c:out value="${adaptorset.adaptorsetId}" />" ><c:out value="${adaptorset.name}" /> 
				</c:forEach>
				</select>
			
		</form>
	</td></tr>
</c:if>
</table>

<br /> 

<form:form commandName="library">
 <input type='hidden' name='jobId' value='<c:out value="${job.jobId}" />'/>
 <input type='hidden' name='macromolSampleId' value='<c:out value="${macromoleculeSample.sampleId}" />'/>
 <input type='hidden' name='adaptorsetId' value='<c:out value="${selectedAdaptorset.adaptorsetId}" />'/>
 
<table class="data">
  	<tr><td class="label">Primary Sample Name</td><td colspan="2" class="value"><c:out value="${macromoleculeSample.name}" /></td></tr>
  	<tr><td class="label">Primary Sample Type</td><td colspan="2" class="value"><c:out value="${macromoleculeSample.typeSample.name}" /></td></tr>
    <c:forEach items="${macromoleculeSample.sampleMeta}" var="msm">
    	<c:if test="${fn:substringAfter(msm.k, 'Biomolecule.') == 'species'}">
            <tr><td class="label">Primary Sample Species</td><td colspan="2" class="value"><c:out value="${msm.v}"/></td></tr>
        </c:if> 
    </c:forEach> 
   	<tr><td colspan="3" class="label-centered">NEW LIBRARY DETAILS</td></tr>
  
  	 <tr>
      <td class="label">Library Name</td>
      <td class="input"><form:input path="name" /><span class="requiredField">*</span></td>
      <td class="error"><form:errors path="name" /></td>
     </tr>
     <tr><td class="label">Sample Type</td><td class="input">Library</td><td>&nbsp;</td></tr>
     <c:set var="_area" value = "sample" scope="request"/>
	 <c:set var="_metaList" value = "${library.sampleMeta}" scope="request" />		
     <c:import url="/WEB-INF/jsp/meta_rw_mod.jsp"/>
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