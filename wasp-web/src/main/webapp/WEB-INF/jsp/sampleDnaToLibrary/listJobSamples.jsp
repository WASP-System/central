<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
<br />
<title><fmt:message key="pageTitle.sampleDnaToLibrary/listJobSamples.label"/></title>
<h1><fmt:message key="pageTitle.sampleDnaToLibrary/listJobSamples.label"/></h1>

<%--this dialogbox is not displayed until called; don't know where is best to put them, but they have to be somewhere or it doesn't work --%>
<div id="modalessDialog">
	<iframe id="modalessIframeId" name="modalessIframeId"  style="overflow-x: scroll; overflow-y: scroll" height="800" width="99%"><p>iframes not supported</p></iframe>
</div>


<%-- for divs side by side see http://stackoverflow.com/questions/5803023/how-to-place-two-divs-next-to-each-other --%>

<div style="width=100%; overflow:hidden;">

	<div style="float:left;">
	 
	   <c:import url="/WEB-INF/jsp/sampleDnaToLibrary/jobdetail_for_import.jsp" />
	 
	</div>

	<div style="padding-left:0.5cm; overflow:hidden">
	 		
		<%--<input  class="button" type="button" id="jobFiles_show_hide_button" value="<fmt:message key="listJobSamples.showJobFiles.label" />"  />
  		<div id="jobFiles" style="display:none">--%>	
		<form action="<wasp:relativeUrl value="sampleDnaToLibrary/uploadJobFile/${job.getId()}.do" />" method="POST"  enctype="multipart/form-data" onsubmit="return validateFileUploadForm(this);">
		<table class="data" style="margin: 0px 0px">
			<tr class="FormData">
				<td class="label-centered" style="background-color:#FAF2D6"><fmt:message key="listJobSamples.file_name.label"/></td>
				<td class="label-centered" style="background-color:#FAF2D6"><fmt:message key="listJobSamples.file_description.label"/></td>
				<td class="label-centered" style="background-color:#FAF2D6"><fmt:message key="listJobSamples.file_action.label"/></td>
			</tr>
			<c:forEach items="${fileGroups}" var="fileGroup">
			 	<c:set value="${fileGroupFileHandlesMap.get(fileGroup)}" var="fileHandles"/>
			 	<c:choose>
			 		<c:when test="${fn:length(fileHandles)==1}">
			 		  	<c:forEach items="${fileHandles}" var="fileHandle" >
			 		  		<tr>
			 		  			<td class="DataTD value-centered"><c:out value="${fileHandle.getFileName()}" /></td>
			 		  			<td class="DataTD value-centered"><c:out value="${fileGroup.getDescription()}" /></td>
			 		  			<!--  <a href="<wasp:url fileAccessor="${fileHandle}" />" > -->
			 		  			<td class="DataTD value-centered"><a href="<wasp:relativeUrl value="file/fileHandle/${fileHandle.getId()}/download.do" />" ><fmt:message key="listJobSamples.file_download.label"/></a> 
			 		  				<c:if test="${fileHandlesThatCanBeViewedList.contains(fileHandle)}">
		 		  					| <a href="javascript:void(0);" onclick='showModalessDialog("<wasp:relativeUrl value="file/fileHandle/${fileHandle.getId()}/view.do" />");' >View</a>
		 		  				</c:if>
			 		  			</td>
			 		  		</tr>
			 			</c:forEach>
			 		</c:when>			 		  
			 		<c:otherwise>
			 			<tr>
			 		  		<td class="DataTD value-centered"><c:out value="${fn:length(fileHandles)}" /> <fmt:message key="listJobSamples.file_download_grouped_files.label"/></td>
			 		  		<td class="DataTD value-centered"><c:out value="${fileGroup.getDescription()}" /></td>			 		  			
			 		  		<td class="DataTD value-centered"><a href="<wasp:relativeUrl value="file/fileGroup/${fileGroup.getId()}/download.do" />" ><fmt:message key="listJobSamples.file_download.label"/></a></td>
			 		  	</tr>
			 		</c:otherwise>			 		
			 	</c:choose>
			</c:forEach>
			<tr>
				<td class="DataTD value-centered"><input type="file" name="file_upload" /></td><td class="DataTD value-centered" ><input type="text" maxlength="30" name="file_description" /></td><td align="center"><input type="submit" name="file_upload_submit_button" value="<fmt:message key="listJobSamples.file_upload.label"/>" /></td>
			</tr>
		</table>
		</form>
		<%--  </div>	--%> 
		
		<br />
		<form  method='post' name='addJobViewer' action="<wasp:relativeUrl value="sampleDnaToLibrary/addJobViewer.do" />" onsubmit="return validate_email();">
			<table class="data" style="margin: 0px 0px">
			<tr  ><th colspan="2" class="label" nowrap><fmt:message key="listJobSamples.jobViewers.label" /></th></tr>
			<tr ><td ><c:out value="${job.user.firstName}" /> <c:out value="${job.user.lastName}" /></td><td><fmt:message key="jobdetail_for_import.jobSubmitter.label" /></td></tr>
			<tr ><td ><c:out value="${job.lab.user.firstName}" /> <c:out value="${job.lab.user.lastName}" /></td><td><fmt:message key="jobdetail_for_import.jobPI.label" /></td></tr>
	
			<c:forEach items="${additionalJobViewers}" var="additionalJobViewer">
				<tr><td ><c:out value="${additionalJobViewer.getFirstName()} ${additionalJobViewer.getLastName()}"/></td>
					<td>
						<c:if test='${currentWebViewerIsSuperuserSubmitterOrPI==true || currentWebViewer.getUserId() == additionalJobViewer.getUserId()}'>
							<a  href='javascript:void(0)' onclick = 'if(confirm("Do you really want to remove this viewer?")){location.href="<wasp:relativeUrl value="sampleDnaToLibrary/removeViewerFromJob/${job.jobId}/${additionalJobViewer.getUserId()}.do" />";}'><fmt:message key="listJobSamples.remove.label" /></a>
						</c:if>
					</td>
				</tr>
			</c:forEach>
			
			<c:if test='${currentWebViewerIsSuperuserSubmitterOrPI==true}'>
	 			<input type='hidden' name='jobId' value='<c:out value="${job.jobId}" />'/>			 				
				<tr ><th colspan="2"  class="label" nowrap><fmt:message key="listJobSamples.addNewViewer.label" /></th></tr>
	 			<tr><td ><fmt:message key="listJobSamples.newViewerEmailAddress.label" />: </td><td ><input type='text' name='newViewerEmailAddress' id="newViewerEmailAddress" size='20' maxlength='50'></td></tr>
				<tr><td colspan="2" align="center"><input type='submit' value='<fmt:message key="listJobSamples.submit.label" />'/></td></tr>
			</c:if>		
			</table>
		</form>
	</div>
	<div style="clear:both;"></div>
</div>

<br />
<input  class="button" type="button" id="requested_coverage_show_hide_button" value="<fmt:message key="listJobSamples.showUserRequestedCoverage.label" />"  />
<div id="user_requested_coverage_data" style="display:none">		
	<table class="data">
	<tr class="FormData">
		<td class="label-centered" style="background-color:#FAF2D6">&nbsp;</td><c:forEach var="i" begin="0" end="${totalNumberCellsRequested - 1}" ><td class="label-centered" style="background-color:#FAF2D6"><fmt:message key="listJobSamples.cell.label" /> <c:out value="${i + 1}" /></td></c:forEach>
	</tr>
	<c:forEach items="${coverageMap.keySet()}" var="coverageItem">
		<tr class="FormData">
			<td class="label-centered" style="background-color:#FAF2D6" >
				<c:out value="${coverageItem.getName()}" />
			</td>
			<c:set var="string" value="${coverageMap.get(coverageItem)}" scope="page" />
			<c:forEach var="i" begin="0" end="${fn:length(string)-1}" step="1">
	   			<td  class="value-centered" style="text-align: center; vertical-align: middle;"> 
	   				<c:choose>
	   					<c:when test='${fn:substring(string, i, i + 1)=="0"}'><input type="checkbox" DISABLED /></c:when>
	   					<c:otherwise><input type="checkbox" DISABLED checked="checked" /></c:otherwise>
	   				</c:choose>   
	  			</td>   
			</c:forEach>
		</tr>
	</c:forEach>
	</table>
</div>

<br />

<c:set var="idCounter" value="1000" scope="page" />
<table class="data"> 

<c:if test="${macromoleculeSubmittedSamplesList.size() > 0}">
	<tr class="FormData">
		<td class="label-centered" style="background-color:#FAF2D6"><fmt:message key="listJobSamples.initialMacromolecules.label" /></td>
		<td class="label-centered" style="background-color:#FAF2D6"><fmt:message key="listJobSamples.libraries.label" /></td>
		<td class="label-centered" style="background-color:#FAF2D6"><fmt:message key="listJobSamples.platformUnitsAndRuns.label" /></td>
	</tr>
	<c:forEach items="${macromoleculeSubmittedSamplesList}" var="userSubmittedMacromolecule">
		<c:set value="${facilityLibraryMap.get(userSubmittedMacromolecule)}" var="facilityLibrariesForThisMacromoleculeList" scope="page" />
		<c:set var="numberLibrariesForThisMacromolecule" value="${facilityLibrariesForThisMacromoleculeList.size()}" scope="page" /> 
		<c:set var="rowSpan" value="${facilityLibrariesForThisMacromoleculeList.size()}" scope="page" />
		<c:if test="${rowSpan == 0}">
			<c:set var="rowSpan" value="${rowSpan + 1}" scope="page" />
		</c:if>
		<tr>
			<td rowspan="${rowSpan}">
				<fmt:message key="listJobSamples.name.label" />: <a href="<wasp:relativeUrl value="sampleDnaToLibrary/sampledetail_ro/${job.jobId}/${userSubmittedMacromolecule.getSampleId()}.do" />"><c:out value="${userSubmittedMacromolecule.getName()}"/></a><br />
				<fmt:message key="listJobSamples.type.label" />: <c:out value="${userSubmittedMacromolecule.getSampleType().getName()}"/><br />
				<fmt:message key="listJobSamples.organism.label" />: <c:out value="${organismMap.get(userSubmittedMacromolecule)}"/><br />
				<fmt:message key="listJobSamples.arrivalStatus.label" />: <c:out value="${receivedStatusMap.get(userSubmittedMacromolecule)}"/>
				<sec:authorize access="hasRole('su') or hasRole('ft')">
				&nbsp;
				<%--  please leave, may be useful later <a href="<wasp:relativeUrl value="task/updatesamplereceive/${job.jobId}.do" />">[update]</a>--%>
				<%-- 
				<c:if test='${receiveSampleStatusMap.get(userSubmittedMacromolecule) == true}'>
					<a href="<wasp:relativeUrl value="task/samplereceive/list.do" />">[<fmt:message key="listJobSamples.logSample.label" />]</a>
				</c:if>
				--%>
				</sec:authorize>
				
				<c:if test='${qcStatusMap.get(userSubmittedMacromolecule) != "NONEXISTENT" && receivedStatusMap.get(userSubmittedMacromolecule) == "RECEIVED"}'>
				  <div>
					<fmt:message key="listJobSamples.qcStatus.label" />: <c:out value="${qcStatusMap.get(userSubmittedMacromolecule)}"/>
					<c:set value="${qcStatusCommentsMap.get(userSubmittedMacromolecule)}" var="metaMessageList" />
					<c:if test="${metaMessageList.size()>0}">
						<%-- <c:forEach items="${metaMessageList}" var="metaMessage">--%>
							<fmt:formatDate value="${metaMessageList[0].getDate()}" pattern="yyyy-MM-dd" var="date" />
		  					<wasp:comment value="${metaMessageList[0].getValue()} (${date})" />
						<%--</c:forEach>--%>
					</c:if>
				  </div>
				</c:if>
				<sec:authorize access="hasRole('su') or hasRole('ft')">
				<c:if test='${receivedStatusMap.get(userSubmittedMacromolecule)=="RECEIVED"}'>
					<c:if test='${not empty createLibraryStatusMap.get(userSubmittedMacromolecule) and createLibraryStatusMap.get(userSubmittedMacromolecule) == true}'>
						<input class="fm-button" type="button" value="<fmt:message key="listJobSamples.createLibrary.label" />"  onClick="window.location='<wasp:relativeUrl value="sampleDnaToLibrary/createLibraryFromMacro/${job.jobId}/${userSubmittedMacromolecule.sampleId}.do"/>'" />
			 	 	</c:if>
		 	 	</c:if>
				</sec:authorize>				
			</td>
			<c:choose>
				<c:when test="${numberLibrariesForThisMacromolecule == 0}">
					<td><fmt:message key="listJobSamples.noLibrariesCreated.label" /></td>	
					<td><fmt:message key="listJobSamples.noLibrariesCreated.label" /></td>
				</c:when>
				<c:when test="${numberLibrariesForThisMacromolecule > 0}">
					<c:set var="rowCounter" value="0" scope="page" />
					<c:forEach items="${facilityLibrariesForThisMacromoleculeList}" var="facilityLibraryForThisMacromolecule">	
 					<c:if test="${rowCounter > 0}">
					  		<tr>
					 </c:if>	
					<td>
							<fmt:message key="listJobSamples.name.label" />: <a href="<wasp:relativeUrl value="sampleDnaToLibrary/librarydetail_ro/${job.jobId}/${facilityLibraryForThisMacromolecule.getSampleId()}.do" />"><c:out value="${facilityLibraryForThisMacromolecule.getName()}"/></a><br />
							<fmt:message key="listJobSamples.type.label" />: <c:out value="${facilityLibraryForThisMacromolecule.getSampleType().getName()}"/><br />
							<c:set var="adaptor" value="${libraryAdaptorMap.get(facilityLibraryForThisMacromolecule)}" scope="page" />
							<fmt:message key="listJobSamples.adaptor.label" />: <c:out value="${adaptor.getAdaptorset().getName()}"/><br />
							<fmt:message key="listJobSamples.index.label" /> <c:out value="${adaptor.getBarcodenumber()}"/> [<c:out value="${adaptor.getBarcodesequence()}"/>]<br />
							<c:if test='${qcStatusMap.get(facilityLibraryForThisMacromolecule) != "NONEXISTENT"}'>
							  <div>
								<fmt:message key="listJobSamples.qcStatus.label" />: <c:out value="${qcStatusMap.get(facilityLibraryForThisMacromolecule)}"/>						
								<c:set value="${qcStatusCommentsMap.get(facilityLibraryForThisMacromolecule)}" var="metaMessageList" />
								<c:if test="${metaMessageList.size()>0}">
									<%-- <c:forEach items="${metaMessageList}" var="metaMessage"> --%>
										<fmt:formatDate value="${metaMessageList[0].getDate()}" pattern="yyyy-MM-dd" var="date" />
		  								<wasp:comment value="${metaMessageList[0].getValue()} (${date})" />
									<%--</c:forEach>--%>
								</c:if>
								</div>
							</c:if>
							<c:set var="idCounter" value="${idCounter + 1}" scope="page" />
 							<sec:authorize access="hasRole('su') or hasRole('ft')">
							<div id="showButton_<c:out value="${idCounter}" />" >

						<c:if test='${assignLibraryToPlatformUnitStatusMap.get(facilityLibraryForThisMacromolecule) == true}'> 
				 				<input class="fm-button" type="button" value="<fmt:message key="listJobSamples.addLibraryToPlatformUnit.label" />" onclick='toggleDisplayOfAddLibraryForm("show", <c:out value="${idCounter}" />)' />				
						</c:if> 

							</div>
							</sec:authorize>
							<div id="addLibraryForm_<c:out value="${idCounter}" />" style="display:none">
							<table class='data'>
								<tr class="FormData"><td class="label-centered"><fmt:message key="listJobSamples.addLibraryToPlatformUnit.label" /></td></tr>
							<tr><td>
							<form  method='post' name='addLibToPU' action="<wasp:relativeUrl value="facility/platformunit/assignAdd2.do" />" onsubmit="return validate_submit(this);">
								<input type='hidden' name='jobid' value='<c:out value="${job.jobId}" />'/>
				 				<input type='hidden' name='librarysampleid' value='<c:out value="${facilityLibraryForThisMacromolecule.getSampleId()}" />'/>
								<br />
				 				<select class="FormElement ui-widget-content ui-corner-all" name="cellsampleid" id="cellsampleid_<c:out value="${idCounter}" />" size="1" onchange="validate(this)">
								<option value="0"><fmt:message key="listJobSamples.selectPlatformUnitCell.label" /></option>
								<c:forEach items="${availableAndCompatibleFlowCells}" var="flowCell">
								<option value="0"><fmt:message key="listJobSamples.platformUnit.label" />: <c:out value="${flowCell.getName()}" /> [<c:out value="${flowCell.getSampleSubtype().getName()}" />]</option>
								<c:set var="sampleSourceList" value="${flowCell.getSampleSource()}" scope="page" />
								<c:forEach items="${sampleSourceList}" var="sampleSource">
									<c:set var="cell" value="${sampleSource.getSourceSample()}" scope="page" />
									<option style="color:red; font-weight: bold;" value="<c:out value="${cell.getSampleId()}" />">&nbsp;&nbsp;&nbsp;<fmt:message key="listJobSamples.cell.label" />: <c:out value="${cell.getName()}" /></option>
									<c:set var="sampleSourceList2" value="${cell.getSampleSource()}" scope="page" />
									<c:forEach items="${sampleSourceList2}" var="sampleSource2">
										<c:set var="library" value="${sampleSource2.getSourceSample()}" scope="page" />
									  	<c:if test="${library.getSampleSubtype().getIName() == 'controlLibrarySample'}">
											<option value="0">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<fmt:message key="listJobSamples.libraryControl.label" />: <c:out value="${library.getName()}" />
											<c:set var="adaptor" value="${libraryAdaptorMap.get(library)}" scope="page" />
											&nbsp;-&nbsp;<c:out value="${adaptor.getAdaptorset().getName()}"/>&nbsp;[<fmt:message key="listJobSamples.index.label" /> <c:out value="${adaptor.getBarcodenumber()}"/>&nbsp;(<c:out value="${adaptor.getBarcodesequence()}"/>)]
			 								</option>	
		        					  </c:if>					
									</c:forEach> 
									<c:forEach items="${sampleSourceList2}" var="sampleSource2">
										<c:set var="library" value="${sampleSource2.getSourceSample()}" scope="page" />
									  	<c:if test="${library.getSampleSubtype().getIName() != 'controlLibrarySample'}">
											<option value="0">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<fmt:message key="listJobSamples.library.label" />: <c:out value="${library.getName()}" />
											<c:set var="adaptor" value="${libraryAdaptorMap.get(library)}" scope="page" />
											&nbsp;-&nbsp;<c:out value="${adaptor.getAdaptorset().getName()}"/>&nbsp;[<fmt:message key="listJobSamples.index.label" /> <c:out value="${adaptor.getBarcodenumber()}"/>&nbsp;(<c:out value="${adaptor.getBarcodesequence()}"/>)]
			 								</option>	
		        					  </c:if>					
									</c:forEach>
								</c:forEach> 
							</c:forEach>
							</select>
								<br />&nbsp;<fmt:message key="listJobSamples.finalConcentrationPM.label" />: <input type='text' name='libConcInCellPicoM' id="libConcInCellPicoM_<c:out value="${idCounter}" />" size='3' maxlength='5'>
								<br />&nbsp;<input type='submit' value='<fmt:message key="listJobSamples.submit.label" />'/>&nbsp;<input class="button" type="button" value="<fmt:message key="listJobSamples.cancel.label" />" onclick='toggleDisplayOfAddLibraryForm("cancel", <c:out value="${idCounter}" />)' />
							</form>
							</td></tr>
							</table>
							</div>
						
					</td>						
					<td>
					
					<c:set var="cellWrapperList" value="${cellsByLibrary.get(facilityLibraryForThisMacromolecule)}" scope="page" />
					<c:choose>
						<c:when test="${cellWrapperList.size() > 0}">
							<c:forEach items="${cellWrapperList}" var="cellWrapper">
								<c:set var="cell" value="${cellWrapper.getCell()}" scope="page" />
								<c:set var="cellNumber" value="${cellWrapper.getIndex()}" scope="page" />
								<c:set var="platformUnit" value="${cellWrapper.getPlatformUnit()}" scope="page" />
								<c:out value="${platformUnit.getName()}"/> <fmt:message key="listJobSamples.cell.label" />: <c:out value="${cellNumber}"/> 
								<c:set var="runList" value="${platformUnit.getRun()}" scope="page" />
								<c:if test="${runList.size() > 0}">
									<br />&nbsp;&nbsp;&nbsp;---&gt; <c:out value="${runList.get(0).getName()}"/>
								</c:if>
								<sec:authorize access="hasRole('su') or hasRole('ft')"><a href="<wasp:relativeUrl value="${showPlatformunitViewMap.get(platformUnit)}" />"> [<fmt:message key="listJobSamples.view.label" />]</a></sec:authorize>
								<br />
							</c:forEach>			
						</c:when>
						<c:otherwise>
							<fmt:message key="listJobSamples.noPlatformUnitsAndRuns.label" /><br />
						</c:otherwise>
					</c:choose>
					</td>				
					</tr>					  	
					<c:set var="rowCounter" value="${rowCounter + 1}" scope="page" />
					</c:forEach>
				</c:when>
			</c:choose>				
	</c:forEach>
</c:if>

<c:if test="${librarySubmittedSamplesList.size() > 0}">
	<tr class="FormData">
		<td colspan="2" class="label-centered" style="background-color:#FAF2D6"><fmt:message key="listJobSamples.userSubmittedLibraries.label" /></td>
		<td class="label-centered" style="background-color:#FAF2D6"><fmt:message key="listJobSamples.platformUnitsAndRuns.label" /></td>
	</tr>
	<c:forEach items="${librarySubmittedSamplesList}" var="userSubmittedLibrary">
	<tr>
		<td colspan="2">
			<fmt:message key="listJobSamples.name.label" />: <a href="<wasp:relativeUrl value="sampleDnaToLibrary/librarydetail_ro/${job.jobId}/${userSubmittedLibrary.getSampleId()}.do" />"><c:out value="${userSubmittedLibrary.getName()}"/></a><br />
			<fmt:message key="listJobSamples.type.label" />: <c:out value="${userSubmittedLibrary.getSampleType().getName()}"/><br />
			<fmt:message key="listJobSamples.organism.label" />: <c:out value="${organismMap.get(userSubmittedLibrary)}"/><br />
			<c:set var="adaptor" value="${libraryAdaptorMap.get(userSubmittedLibrary)}" scope="page" />
			<fmt:message key="listJobSamples.adaptor.label" />: <c:out value="${adaptor.getAdaptorset().getName()}"/><br />
			<fmt:message key="listJobSamples.index.label" /> <c:out value="${adaptor.getBarcodenumber()}"/> [<c:out value="${adaptor.getBarcodesequence()}"/>]<br />
			<fmt:message key="listJobSamples.arrivalStatus.label" />: <c:out value="${receivedStatusMap.get(userSubmittedLibrary)}"/>
			<sec:authorize access="hasRole('su') or hasRole('ft')">&nbsp;<%--<a href="<wasp:relativeUrl value="task/updatesamplereceive/${job.jobId}.do" />">[update]</a>--%><%-- <c:if test='${receiveSampleStatusMap.get(userSubmittedLibrary) == true}'><a href="<wasp:relativeUrl value="task/samplereceive/list.do" />">[<fmt:message key="listJobSamples.logSample.label" />]</a></c:if>--%></sec:authorize><br />
			<c:if test='${qcStatusMap.get(userSubmittedLibrary) != "NONEXISTENT" && receivedStatusMap.get(userSubmittedLibrary)=="RECEIVED"}'>
			  <div>
				<fmt:message key="listJobSamples.qcStatus.label" />: <c:out value="${qcStatusMap.get(userSubmittedLibrary)}"/>
				<c:set value="${qcStatusCommentsMap.get(userSubmittedLibrary)}" var="metaMessageList" />
					<c:if test="${metaMessageList.size()>0}">
						<%-- <c:forEach items="${metaMessageList}" var="metaMessage"> --%>
							<fmt:formatDate value="${metaMessageList[0].getDate()}" pattern="yyyy-MM-dd" var="date" />
		  					<wasp:comment value="${metaMessageList[0].getValue()} (${date})" />
						<%--</c:forEach>--%>
					</c:if>
			   </div>
			</c:if>
			<c:if test='${receivedStatusMap.get(userSubmittedLibrary)=="RECEIVED"}'>
				<c:set var="idCounter" value="${idCounter + 1}" scope="page" />
 				<sec:authorize access="hasRole('su') or hasRole('ft')">
				<div id="showButton_<c:out value="${idCounter}" />" >
			<c:if test='${receivedStatusMap.get(userSubmittedLibrary)=="RECEIVED"}'>	
			  <c:if test='${assignLibraryToPlatformUnitStatusMap.get(userSubmittedLibrary) == true}'> 
					<input class="button" type="button" value="<fmt:message key="listJobSamples.addLibraryToPlatformUnit.label" />" onclick='toggleDisplayOfAddLibraryForm("show", <c:out value="${idCounter}" />)' />				
			  </c:if> 
			</c:if>  
				</div>
				</sec:authorize>
				<div id="addLibraryForm_<c:out value="${idCounter}" />" style="display:none">
				<table class='data'>
					<tr class="FormData"><td class="label-centered"><fmt:message key="listJobSamples.addLibraryToPlatformUnit.label" /></td></tr>
					<tr><td>
					<form  method='post' name='addLibToPU' action="<wasp:relativeUrl value="facility/platformunit/assignAdd2.do" />" onsubmit="return validate_submit(this);">
						<input type='hidden' name='jobid' value='<c:out value="${job.jobId}" />'/>
				 		<input type='hidden' name='librarysampleid' value='<c:out value="${userSubmittedLibrary.getSampleId()}" />'/>
						<br />
				 		<select class="FormElement ui-widget-content ui-corner-all" name="cellsampleid" id="cellsampleid_<c:out value="${idCounter}" />" size="1" onchange="validate(this)">
							<option value="0"><fmt:message key="listJobSamples.selectPlatformUnitCell.label" /></option>
							<c:forEach items="${availableAndCompatibleFlowCells}" var="flowCell">
								<option value="0"><fmt:message key="listJobSamples.platformUnit.label" />: <c:out value="${flowCell.getName()}" /> [<c:out value="${flowCell.getSampleSubtype().getName()}" />]</option>
								<c:set var="sampleSourceList" value="${flowCell.getSampleSource()}" scope="page" />
								<c:forEach items="${sampleSourceList}" var="sampleSource">
									<c:set var="cell" value="${sampleSource.getSourceSample()}" scope="page" />
									<option style="color:red; font-weight: bold;" value="<c:out value="${cell.getSampleId()}" />">&nbsp;&nbsp;&nbsp;<fmt:message key="listJobSamples.cell.label" />: <c:out value="${cell.getName()}" /></option>
									<c:set var="sampleSourceList2" value="${cell.getSampleSource()}" scope="page" />
									<c:forEach items="${sampleSourceList2}" var="sampleSource2">
										<c:set var="library" value="${sampleSource2.getSourceSample()}" scope="page" />
									  	<c:if test="${library.getSampleSubtype().getIName() == 'controlLibrarySample'}">
											<option value="0">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<fmt:message key="listJobSamples.libraryControl.label" />: <c:out value="${library.getName()}" />
											<c:set var="adaptor" value="${libraryAdaptorMap.get(library)}" scope="page" />
											&nbsp;-&nbsp;<c:out value="${adaptor.getAdaptorset().getName()}"/>&nbsp;[<fmt:message key="listJobSamples.index.label" /> <c:out value="${adaptor.getBarcodenumber()}"/>&nbsp;(<c:out value="${adaptor.getBarcodesequence()}"/>)]
			 								</option>	
		        					  </c:if>					
									</c:forEach> 
									<c:forEach items="${sampleSourceList2}" var="sampleSource2">
										<c:set var="library" value="${sampleSource2.getSourceSample()}" scope="page" />
									  	<c:if test="${library.getSampleSubtype().getIName() != 'controlLibrarySample'}">
											<option value="0">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<fmt:message key="listJobSamples.library.label" />: <c:out value="${library.getName()}" />
											<c:set var="adaptor" value="${libraryAdaptorMap.get(library)}" scope="page" />
											&nbsp;-&nbsp;<c:out value="${adaptor.getAdaptorset().getName()}"/>&nbsp;[<fmt:message key="listJobSamples.index.label" /> <c:out value="${adaptor.getBarcodenumber()}"/>&nbsp;(<c:out value="${adaptor.getBarcodesequence()}"/>)]
			 								</option>	
		        					  </c:if>					
									</c:forEach>
								</c:forEach> 
							</c:forEach>
						</select>
						<br />&nbsp;<fmt:message key="listJobSamples.finalConcentrationPM.label" />: <input type='text' name='libConcInCellPicoM' id="libConcInCellPicoM_<c:out value="${idCounter}" />" size='3' maxlength='5'>
						<br />&nbsp;<input type='submit' value='<fmt:message key="listJobSamples.submit.label" />'/>&nbsp;<input class="button" type="button" value="<fmt:message key="listJobSamples.cancel.label" />" onclick='toggleDisplayOfAddLibraryForm("cancel", <c:out value="${idCounter}" />)' />
					</form>
					</td></tr>
				</table>
				</div>
			</c:if> 
		</td>
		<td>
		<c:set var="cellWrapperList" value="${cellsByLibrary.get(userSubmittedLibrary)}" scope="page" />
		<c:choose>
			<c:when test="${cellWrapperList.size() > 0}">
				<c:forEach items="${cellWrapperList}" var="cellWrapper">
					<c:set var="cell" value="${cellWrapper.getCell()}" scope="page" />
					<c:set var="cellNumber" value="${cellWrapper.getIndex()}" scope="page" />
					<c:set var="platformUnit" value="${cellWrapper.getPlatformUnit()}" scope="page" />
					<c:out value="${platformUnit.getName()}"/> <fmt:message key="listJobSamples.cell.label" />: <c:out value="${cellNumber}"/> 
					<c:set var="runList" value="${platformUnit.getRun()}" scope="page" />
					<c:if test="${runList.size() > 0}">
						<br />&nbsp;&nbsp;&nbsp;---&gt; <c:out value="${runList.get(0).getName()}"/>
					</c:if>
					<sec:authorize access="hasRole('su') or hasRole('ft')"><a href="<wasp:relativeUrl value="${showPlatformunitViewMap.get(platformUnit)}" />"> [<fmt:message key="listJobSamples.view.label" />]</a></sec:authorize>
					<br />
				</c:forEach>			
			</c:when>
			<c:otherwise>
				<fmt:message key="listJobSamples.noPlatformUnitsAndRuns.label" /><br />
			</c:otherwise>
		</c:choose>
		</td>
	</tr>
	</c:forEach>
</c:if>

</table>
