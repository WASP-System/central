<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
<wasp:message />  <br />
<h1>Update Library</h1>
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
<c:if test="${otherAdaptorsets != null && otherAdaptorsets.size() > 0}">
	<tr class="FormData"><td colspan="2">
		<form method="GET" action="<c:url value="/sampleDnaToLibrary/createLibraryFromMacro.do" />">
	  		<input class="FormElement ui-widget-content ui-corner-all" type='hidden' name='jobId' value='<c:out value="${job.jobId}" />'/>
	  		<input class="FormElement ui-widget-content ui-corner-all" type='hidden' name='macromolSampleId' value='<c:out value="${macromoleculeSample.sampleId}" />'/>
	  		
				<select class="FormElement ui-widget-content ui-corner-all" name="adaptorsetId" size="1" onchange="if(this.options[selectedIndex].value != 0){this.parentNode.submit();}">
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

<form:form  cssClass="FormGrid" commandName="library">
<table class="EditTable ui-widget ui-widget-content">
 <input class="FormElement ui-widget-content ui-corner-all" type='hidden' name='jobId' value='<c:out value="${job.jobId}" />'/>
 <input class="FormElement ui-widget-content ui-corner-all" type='hidden' name='libraryId' value='<c:out value="${library.sampleId}" />'/>

<c:if test="${parentMacromolecule != null && parentMacromolecule.sampleId > 0}">

  	<tr class="FormData"><td class="CaptionTD">Primary Sample Name:</td><td colspan="2" class="DataTD"><c:out value="${parentMacromolecule.name}" /></td></tr>
  	<tr class="FormData"><td class="CaptionTD">Primary Sample Type:</td><td colspan="2" class="DataTD"><c:out value="${parentMacromolecule.typeSample.name}" /></td></tr>
    <c:forEach items="${parentMacromolecule.sampleMeta}" var="msm">
    	<c:if test="${fn:substringAfter(msm.k, 'Biomolecule.') == 'species'}">
            <tr class="FormData"><td class="CaptionTD">Primary Sample Species:</td><td colspan="2" class="DataTD"><c:out value="${msm.v}"/></td></tr>
        </c:if> 
    </c:forEach> 
</c:if>

 	<tr class="FormData"><td colspan="3" class="label-centered">LIBRARY DETAILS</td></tr>

 
	 <tr class="FormData">
      <td class="CaptionTD">Library Name:</td>
      <td class="DataTD"><form:input cssClass="FormElement ui-widget-content ui-corner-all" path="name" /><span class="requiredField">*</span></td>
      <td class="CaptionTD error"><form:errors path="name" /></td>
     </tr>
     <tr class="FormData"><td class="CaptionTD">Sample Type:</td><td class="DataTD">Library</td><td>&nbsp;</td></tr>
     <c:set var="_area" value = "sample" scope="request"/>
	 <c:set var="_metaList" value = "${library.sampleMeta}" scope="request" />		
     <c:import url="/WEB-INF/jsp/meta_rw.jsp"/>
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