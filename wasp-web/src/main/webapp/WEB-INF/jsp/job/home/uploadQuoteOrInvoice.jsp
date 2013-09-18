<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<c:import url="/WEB-INF/jsp/job/home/fadingMessage.jsp" />
<%-- What was used was: from http://hmkcode.com/spring-mvc-upload-file-ajax-jquery-formdata/ --%>
<%--Apparently need onsubmit='return false' to suppress hitting the event when the ENTER key is pressed with the cursor in the description input box --%>
<br />
<span style="padding:3px; border: 1px solid black;">
	<a <%-- class="button" --%> href="javascript:void(0);" onclick='loadNewPageWithAjax("<c:url value="/job/${job.getId()}/costManager.do" />");' >Return To Costs Page</a>
</span>
<br /><br />
<form id="quoteOrInvoiceUploadFormId" action="<c:url value="/job/${job.getId()}/uploadQuoteOrInvoice.do" />" method="POST"  enctype="multipart/form-data" onsubmit='return false;' >
	<table class="data" style="margin: 0px 0px">
		<tr class="FormData">
			<td colspan="4" class="label-centered" style="background-color:#FAF2D6">Upload A Quote Or Invoice</td>
		</tr>
		<tr class="FormData">
			<td class="label-centered" style="background-color:#FAF2D6"><fmt:message key="listJobSamples.fileUploadSelectFileToUpload.label"/></td>
			<td class="label-centered" style="background-color:#FAF2D6"><fmt:message key="listJobSamples.fileUploadProvideBriefDescription.label"/></td>
			<td class="label-centered" style="background-color:#FAF2D6">Total Cost<sup>*</sup></td>
			
			<td class="label-centered" style="background-color:#FAF2D6"><fmt:message key="listJobSamples.file_action.label"/></td>
		</tr>
		<tr>
			<td class="DataTD value-centered"><input type="file" name="file_upload" /></td>
			<td class="DataTD value-centered" >
				<input type="radio" name="file_description" value="Quote" checked>Quote &nbsp;<input type="radio" name="file_description" value="Invoice" disabled>Invoice
			</td>
			<td class="DataTD value-centered" >
					<c:out value="${localCurrencyIcon}" /><input style="text-align:right;" name="totalCost"  type="text" maxlength="6" size="6" value="" />.00
			</td>
			<td align="center">
				<input type="reset" name="reset" value="<fmt:message key="listJobSamples.file_reset.label" />" />
				<%--I don't know why, but having uploadJqueryForm("fileUploadFormId") wired through onsubmit via a regular submit button makes for Major problems!!!! use the button below --%>
				<a class="button" href="javascript:void(0);"  onclick='uploadJqueryForm("quoteOrInvoiceUploadFormId")' ><fmt:message key="listJobSamples.file_upload.label" /></a>
				<a class="button" href="javascript:void(0);"  onclick='loadNewPageWithAjax("<c:url value="/job/${job.getId()}/costManager.do" />");' ><fmt:message key="sampledetail_rw.cancel.label" /></a>
			</td>
		</tr>
		<c:if test="${fn:length(errorMessage)>0}">
				<tr><td colspan="4" align="center" style="color:red;font-weight:bold"><c:out value="${errorMessage}" /></td></tr>
		</c:if>
		
	</table>
	<sup>*</sup><span style="font-size:small;color:red">For Total Cost, whole numbers only</span>
	
</form>
<br />
