<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<%--  TODO: Declare style in css file (e.g. /src/main/webapp/css/base.css), not in .jsp and reuse where possible !!!! --%>

<br />

<h1><fmt:message key="jobDraft.create.label"/> -- <fmt:message key="pageTitle.jobsubmit/sample.label"/></h1>

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
		<td class="CaptionTD top-heading"><fmt:message key="jobDraft.sample_action.label"/></td>
	</tr>
	<c:choose>
		<c:when test="${empty sampleDraftList }">
			<tr><td class="DataTD value-centered td-even-number" colspan="5"><fmt:message key="jobDraft.no_draft_samples.label" /></td></tr>
		</c:when>
		<c:otherwise>
			<c:forEach items="${sampleDraftList}" var="sampleDraft" varStatus="status">	
				<tr>
					<td class="DataTD value-centered <c:if test="${status.count % 2 == 0}"> td-even-number</c:if>">${status.count}</td>
					<td class="DataTD value-centered <c:if test="${status.count % 2 == 0}"> td-even-number</c:if>">${sampleDraft.getName()}</td>
					<td class="DataTD value-centered <c:if test="${status.count % 2 == 0}"> td-even-number</c:if>">${sampleDraft.getSampleType().getName()}</td>
					<td class="DataTD value-centered <c:if test="${status.count % 2 == 0}"> td-even-number</c:if>">${sampleDraft.getSampleSubtype().getName()}</td>
					<td class="DataTD value-centered <c:if test="${status.count % 2 == 0}"> td-even-number</c:if>">
						<a  href="<wasp:relativeUrl value='jobsubmit/samples/clone/${ jobDraft.getJobDraftId() }/${ sampleDraft.getSampleDraftId() }.do' />"><fmt:message key="jobDraft.sample_clone.label"/></a>
						<a  href="<wasp:relativeUrl value='jobsubmit/samples/view/${ jobDraft.getJobDraftId() }/${ sampleDraft.getSampleDraftId() }.do' />"> | <fmt:message key="jobDraft.sample_view.label"/></a>
						<a  href="javascript:verifyRemove('<wasp:relativeUrl value="jobsubmit/samples/remove/${ jobDraft.getJobDraftId() }/${ sampleDraft.getSampleDraftId() }.do" />')"> | <fmt:message key="jobDraft.sample_remove.label"/></a>
						<a href="<wasp:relativeUrl value='jobsubmit/samples/edit/${ jobDraft.getJobDraftId() }/${ sampleDraft.getSampleDraftId() }.do' />"> | <fmt:message key="jobDraft.sample_edit.label"/></a>
					</td>
				</tr>
			</c:forEach>
		</c:otherwise>
	</c:choose>
	
	<!-- added 2-5-15 to deal with multiple sampleTypes; replaced buttons with selects -->
	<tr>
		<td colspan="5" class="value-centered button-padding">	
		    <c:choose>
				<c:when test="${empty sampleDraftList }">
					<b><fmt:message key="jobsubmitSample.addSamplesOfType.label" />:</b> 
				</c:when>
				<c:otherwise>		
					<b><fmt:message key="jobsubmitSample.addMoreSamplesOfType.label" />:</b> 
				</c:otherwise>
			</c:choose>
			<select class="FormElement ui-widget-content ui-corner-all" name="urlForAddMoreSamplesOfType"  id="urlForAddMoreSamplesOfType"  >
				<option value=""><fmt:message key="wasp.default_select.label"/></option>
					<c:forEach items="${ sampleSubtypeList }" var="sampleSubtype">
						<option value="<wasp:relativeUrl value='jobsubmit/manysamples/add/${ jobDraft.getJobDraftId() }/${ sampleSubtype.getSampleSubtypeId() }.do' />" ><c:out value="${ sampleSubtype.getSampleType().getName()  }"/> (<c:out value="${ sampleSubtype.getName()  }"/>)</option>
					</c:forEach>
			</select>	
		
		    <c:if test="${not empty sampleSubtypesOfExistingSampleDraftsList }">
		    	
		    	<c:set value="" var="librarySampleSubtypeId" />
		    	
		    	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;			
				<b><fmt:message key="jobsubmitSample.editYourSamplesOfType.label" />:</b> 
				<select class="FormElement ui-widget-content ui-corner-all" name="urlForEditYourSamplesOfType"  id="urlForEditYourSamplesOfType"  >
					<option value=""><fmt:message key="wasp.default_select.label"/></option>
					<c:forEach items="${ sampleSubtypesOfExistingSampleDraftsList }" var="sampleSubtype">
						<c:choose>
							<c:when test="${sampleSubtype.getSampleType().getIName() !='library'}">
								<option value="<c:out value="${ fn:length(adaptorSetsUsedOnThisJobDraft)  }"/>::<c:out value="${ sampleSubtype.getSampleType().getIName()  }"/>::<wasp:relativeUrl value='jobsubmit/manysamples/edit/${ jobDraft.getJobDraftId() }/${ sampleSubtype.getSampleSubtypeId() }.do' />" ><c:out value="${ sampleSubtype.getSampleType().getName()  }"/> (<c:out value="${ sampleSubtype.getName()  }"/>)</option>				
							</c:when>
							<%--THIS NEXT ONE, where length==0 is designed ONLY to handle bioanalyzer libraries, whcih have no adaptors!!! --%>
							<c:when test="${sampleSubtype.getSampleType().getIName() =='library' && fn:length(adaptorSetsUsedOnThisJobDraft)==0}">
								<c:set value="${sampleSubtype.getId() }" var="librarySampleSubtypeId" />
								<option value="0::<c:out value="${ sampleSubtype.getSampleType().getIName()  }"/>::<wasp:relativeUrl value='jobsubmit/manysamples/edit/${ jobDraft.getJobDraftId() }/${ sampleSubtype.getSampleSubtypeId() }.do' />" ><c:out value="${ sampleSubtype.getSampleType().getName()  }"/> (<c:out value="${ sampleSubtype.getName()  }"/>)</option>
							</c:when>
							<c:when test="${sampleSubtype.getSampleType().getIName() =='library' && fn:length(adaptorSetsUsedOnThisJobDraft)==1}">
								<c:set value="${sampleSubtype.getId() }" var="librarySampleSubtypeId" />
								<option value="<c:out value="${ fn:length(adaptorSetsUsedOnThisJobDraft)  }"/>::<c:out value="${ sampleSubtype.getSampleType().getIName()  }"/>::<wasp:relativeUrl value='jobsubmit/manysamples/edit/${ jobDraft.getJobDraftId() }/${ sampleSubtype.getSampleSubtypeId() }.do?theSelectedAdaptorset=${ adaptorSetsUsedOnThisJobDraft.get(0).getId()}' />" ><c:out value="${ sampleSubtype.getSampleType().getName()  }"/> (<c:out value="${ sampleSubtype.getName()  }"/>)</option>
							</c:when>
							<c:when test="${sampleSubtype.getSampleType().getIName() =='library' && fn:length(adaptorSetsUsedOnThisJobDraft) > 1}">
								<c:set value="${sampleSubtype.getId() }" var="librarySampleSubtypeId" />
								<option value="<c:out value="${ fn:length(adaptorSetsUsedOnThisJobDraft)  }"/>::<c:out value="${ sampleSubtype.getSampleType().getIName()  }"/>::urlWillBeGottenFromDialogBox" ><c:out value="${ sampleSubtype.getSampleType().getName()  }"/> (<c:out value="${ sampleSubtype.getName()  }"/>)</option>
							</c:when>									
						</c:choose>
					</c:forEach>
				</select>
				
				 <c:if test="${fn:length(adaptorSetsUsedOnThisJobDraft)>1 && not empty librarySampleSubtypeId }">
					<div id="dialog-form2" title="Select library set to edit">
						<p></p>
						<p style="font-weight:bold;color:red" id="validateTipForThisModalDialogForm"></p>  
		  				<fieldset>
		  					<fmt:message key="jobsubmitSample.YouSubmittedLibraries.label" />
		  					<fmt:message key="jobsubmitSample.PleaseSelectTheLibraries.label" />:<br />
		  					<select name="theSelectedAdaptorset" id="theSelectedAdaptorset">
		  						<option value=''><fmt:message key="wasp.default_select.label"/></option>
								<c:forEach items="${ adaptorSetsUsedOnThisJobDraft }" var="adaptorSetUsedOnThisJobDraft">
									<option value="<wasp:relativeUrl value='jobsubmit/manysamples/edit/${ jobDraft.getJobDraftId() }/${ librarySampleSubtypeId }.do?theSelectedAdaptorset=${adaptorSetUsedOnThisJobDraft.getId()}' />">
										<c:out value="${ adaptorSetUsedOnThisJobDraft.getName() }"/>
									</option>
								</c:forEach>
		  					</select>
		 				</fieldset>  
					</div>
				</c:if>
				
			</c:if>	
		</td>
		<!-- TODO: implement a way to add samples/libraries from previous jobs; this is for the future - we are not currently set up for this -->	
	</tr>
</table>

<br />
<h2><fmt:message key="jobDraft.upload_file_heading.label"/></h2>
<div class="instructions">
   <fmt:message key="jobDraft.upload_file_description.label"/>
</div>
<form method="POST"  enctype="multipart/form-data" onsubmit='return validate(this)'>
	<table id="fileUploadTbl"  class="EditTable ui-widget ui-widget-content">
		<tr>
			<td class="CaptionTD top-heading"><fmt:message key="jobDraft.file.label"/></td><td class="CaptionTD top-heading"><fmt:message key="jobDraft.file_description.label"/></td><td class="CaptionTD top-heading"><fmt:message key="jobDraft.file_action.label"/></td>
		</tr>
		<c:forEach items="${fileGroups}" var="fileGroup">
		 	<c:set value="${fileGroupFileHandlesMap.get(fileGroup)}" var="fileHandles"/>
		 	<c:forEach items="${fileHandles}" var="fileHandle" >
				<tr>
					<td class="DataTD value-centered"><!--<wasp:url	fileAccessor="${fileHandle}" ></wasp:url>--><c:out value="${fileHandle.getFileName()}" /></td>
					<td class="DataTD value-centered"><c:out value="${fileGroup.getDescription()}" /> </td>
					<td class="DataTD value-centered"><a  href="<wasp:relativeUrl value='jobsubmit/file/${ jobDraft.getId() }/${ fileGroup.getId() }/${ fileHandle.getId() }/delete.do'/>"><fmt:message key="jobDraft.file_remove.label"/></a> | <a href="<wasp:relativeUrl value='file/fileHandle/${fileHandle.getId()}/download.do'/>"><fmt:message key="jobDraft.file_download.label"/></a></td>
				</tr>
			</c:forEach>
		</c:forEach>
		<tr>
			<td class="DataTD value-centered"><input type="file" name="file_upload" onchange="addFileUploadRow()"/></td><td class="DataTD value-centered" ><input class="FormElement ui-widget-content ui-corner-all" type="text" maxlength="30" name="file_description" /></td><td class="DataTD value-centered"><fmt:message key="jobDraft.file_not_applicable.label"/></td>
		</tr>
	</table>
	<input class="fm-button" type="button" value="<fmt:message key="jobDraft.terminateDiscard.label" />" onClick="if(confirm('<fmt:message key="jobDraft.terminateDiscardThisJobDraft.label" />')){window.location='<wasp:relativeUrl value="jobsubmit/terminateJobDraft/${jobDraft.jobDraftId}.do"/>'}" /> 
	<input class="fm-button" type="button" value="<fmt:message key="jobDraft.finishLater.label" />" onClick="window.location='<wasp:relativeUrl value="dashboard.do"/>'" /> 
	<input class="FormElement ui-widget-content ui-corner-all" type="submit" value="<fmt:message key="jobDraft.continue.label"/>">
</form>





