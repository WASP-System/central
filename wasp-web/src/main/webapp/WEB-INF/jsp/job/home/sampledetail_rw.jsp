<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
<br />
<a class="button" href="javascript:void(0);"  onclick='loadNewPageWithAjax("<wasp:relativeUrl value="job/${job.getId()}/samples.do" />");' >Back To: Samples, Libraries &amp; Runs</a>
<br /><br /><br />
<form:form cssClass="FormGrid" commandName="sample" method='post' name='editMacromoleculeForm' id='editMacromoleculeFormId' action="" >
	<form:hidden path='sampleSubtypeId' />
	<form:hidden path='sampleTypeId' />
	<table class="EditTable ui-widget ui-widget-content">
	  	 <tr class="FormData">
	      <td class="CaptionTD"><fmt:message key="sampledetail_rw.sampleName.label" />:</td>
	      <td class="DataTD"><form:input cssClass="FormElement ui-widget-content ui-corner-all" path="name" /><span class="requiredField">*</span></td>
	      <td class="CaptionTD error"><form:errors path="name" /></td>
	     </tr>
	     <tr class="FormData"><td class="CaptionTD"><fmt:message key="sampledetail_rw.sampleType.label" />:</td><td class="DataTD"><c:out value="${sample.sampleType.name}" /></td><td class="CaptionTD error"><form:errors path="" /></td></tr>
	     <c:set var="_area" value = "sample" scope="request"/>
		 <c:set var="_metaList" value = "${normalizedSampleMeta}" scope="request" />		
	      <c:import url="/WEB-INF/jsp/meta_rw.jsp" />
	     <sec:authorize access="hasRole('su') or hasRole('ft')">	     
		    <tr class="FormData">
		        <td colspan="3" align="left" class="submitBottom">	
		        	<a class="button" href="javascript:void(0);"  onclick='loadNewPageWithAjax("<wasp:relativeUrl value="job/${job.getId()}/sample/${sample.getId()}/sampledetail_ro.do" />");' ><fmt:message key="sampledetail_rw.cancel.label" /></a>
		        	<a class="button" href="javascript:void(0);"  onclick='loadNewPageWithAjax("<wasp:relativeUrl value="job/${job.getId()}/sample/${sample.getId()}/sampledetail_rw.do" />");' >Reset</a>
					<a class="button" href="javascript:void(0);"  onclick='postFormWithAjaxJson("editMacromoleculeFormId","<wasp:relativeUrl value="job/${job.getId()}/sample/${sample.getId()}/sampledetail_rw.do" />"); return false;' ><fmt:message key="sampledetail_rw.save.label" /></a>
		        </td>
		    </tr>
	    </sec:authorize>
	</table>
</form:form>	
<br />