<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
<br />

<h1><fmt:message key="jobDraft.create.label"/> -- <fmt:message key="jobsubmit/samples.label"/></h1>

<%@ include file="/WEB-INF/jsp/jobsubmit/jobsubmitinfo.jsp" %>

<div class="instructions">
   <fmt:message key="jobDraft.sample_instructions.label"/>
</div>

<table class="EditTable ui-widget ui-widget-content">
	<tr>
		<td class="CaptionTD top-heading"><fmt:message key="jobDraft.sample_number.label"/></td>
		<td class="CaptionTD top-heading"><fmt:message key="jobDraft.sample_name.label"/></td>
		<td class="CaptionTD top-heading"><fmt:message key="jobDraft.sample_type.label"/></td>
		<td class="CaptionTD top-heading"><fmt:message key="jobDraft.sample_subtype.label"/></td>
		<td class="CaptionTD top-heading"><fmt:message key="jobDraft.sample_class.label"/></td>
		<!-- TODO: re-implement line below when functionality added
		  <td class="CaptionTD top-heading"><fmt:message key="jobDraft.sample_file.label"/></td> -->
		<td class="CaptionTD top-heading"><fmt:message key="jobDraft.sample_action.label"/></td>
	</tr>
	<c:choose>
		<c:when test="${empty sampleDraftList }">
			<tr><td class="DataTD value-centered white-background" colspan="7"><fmt:message key="jobDraft.no_draft_samples.label" /></td></tr>
		</c:when>
		<c:otherwise>
			<c:forEach items="${sampleDraftList}" var="sampleDraft" varStatus="status">
			<c:set var="isExistingSample" value="0" />
			<c:if test="${not empty sampleDraft.getSourceSampleId()}"><c:set var="isExistingSample" value="1" /></c:if>
				<tr>
					<td class="DataTD value-centered <c:if test="${status.count % 2 == 0}"> white-background</c:if>">${status.count}</td>
					<td class="DataTD value-centered <c:if test="${status.count % 2 == 0}"> white-background</c:if>">${sampleDraft.getName()}</td>
					<td class="DataTD value-centered <c:if test="${status.count % 2 == 0}"> white-background</c:if>">${sampleDraft.getSampleType().getName()}</td>
					<td class="DataTD value-centered <c:if test="${status.count % 2 == 0}"> white-background</c:if>">${sampleDraft.getSampleSubtype().getName()}</td>
					<td class="DataTD value-centered <c:if test="${status.count % 2 == 0}"> white-background</c:if>">
						<c:choose>
							<c:when test="${isExistingSample == 1}">Existing</c:when>
							<c:otherwise>New</c:otherwise>
						</c:choose>
					</td>
					<!-- TODO: re-implement lines below when functionality added
		  			<td class="DataTD value-centered <c:if test="${status.count % 2 == 0}"> white-background</c:if>">
					<c:if test="${ not empty sampleDraft.getFile()}">
						<a href="/wasp/file/downloadFile.do?id=<c:out value="${sampleDraft.getFile().getFileId()}" />">${sampleDraft.getFile().getFileName()}</a>
					</c:if>
					<c:if test="${ empty sampleDraft.getFile()}">
						<fmt:message key="jobDraft.no_file.label"/>
					</c:if>
					&nbsp;</td>
					-->
					<td class="DataTD value-centered <c:if test="${status.count % 2 == 0}"> white-background</c:if>">
						<a  href="/wasp/jobsubmit/samples/clone/<c:out value="${ jobDraft.getJobDraftId() }"/>/<c:out value="${ sampleDraft.getSampleDraftId() }"/>.do"><fmt:message key="jobDraft.sample_clone.label"/></a>
						<a  href="/wasp/jobsubmit/samples/view/<c:out value="${ jobDraft.getJobDraftId() }"/>/<c:out value="${ sampleDraft.getSampleDraftId() }"/>.do"> | <fmt:message key="jobDraft.sample_view.label"/></a>
						<a  href="javascript:verifyRemove('/wasp/jobsubmit/samples/remove/<c:out value="${ jobDraft.getJobDraftId() }"/>/<c:out value="${ sampleDraft.getSampleDraftId() }"/>.do')"> | <fmt:message key="jobDraft.sample_remove.label"/></a>
						<c:if test="${isExistingSample == 0}">
							<%-- Only edit new samples --%>
							<a href="/wasp/jobsubmit/samples/edit/<c:out value="${ jobDraft.getJobDraftId() }"/>/<c:out value="${ sampleDraft.getSampleDraftId() }"/>.do"> | <fmt:message key="jobDraft.sample_edit.label"/></a>
						</c:if>
					</td>
				</tr>
			</c:forEach>
		</c:otherwise>
	</c:choose>
	<tr>
		<td colspan="7" class="value-centered button-padding">
			<c:forEach items="${ sampleSubtypeList }" var="sampleSubtype">
				<a class="button" href="/wasp/jobsubmit/samples/add/<c:out value="${ jobDraft.getJobDraftId() }"/>/<c:out value="${ sampleSubtype.getSampleSubtypeId() }"/>.do">+ <c:out value="${ sampleSubtype.getName() }"/></a>
			</c:forEach>
			<!-- TODO: re-implement line below when functionality added
			<a class="button" href="/wasp/jobsubmit/samples/addExisting/<c:out value="${ jobDraft.getJobDraftId() }"/>.do">+ <fmt:message key="jobDraft.sample_add_existing.label"/></a>  
			-->	
		</td>
	</tr>
</table>

<br />
<h2><fmt:message key="jobDraft.upload_file_heading.label"/></h2>
<div class="instructions">
   <fmt:message key="jobDraft.upload_file_description.label"/>
</div>
<form method="POST"  enctype="multipart/form-data">
<table id="fileUploadTbl"  class="EditTable ui-widget ui-widget-content">
	<tr>
		<td class="CaptionTD top-heading">File</td><td class="CaptionTD top-heading">Description</td>
	</tr>
	<c:forEach items="${files}" var="file">
		<tr>
			<td class="DataTD value-centered"><c:out value="${file.getFileName()}" /></td><td class="DataTD value-centered"><c:out value="${file.getDescription()}" /></td>
		</tr>
	</c:forEach>
	<tr>
		<td class="DataTD value-centered"><input type="file" name="file_upload" onchange="addFileUploadRow()"/></td><td class="DataTD value-centered"><input type="text" name="file_description" /></td>
	</tr>
</table>
<input class="FormElement ui-widget-content ui-corner-all" type="submit" value="<fmt:message key="jobDraft.next.label"/>">
</form>





