<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<br />
	 	 								
<a class="button" href="javascript:void(0);" onclick='showSmallModalessDialog("<wasp:relativeUrl value="job/${job.getId()}/basic.do" />");' ><fmt:message key="jobHomeSamples.viewBasicRequest.label" /></a>
<a class="button" href="javascript:void(0);" onclick='showSmallModalessDialog("<wasp:relativeUrl value="job/${job.getId()}/requests.do?onlyDisplayCellsRequested=true" />");' ><fmt:message key="jobHomeSamples.viewLaneRequest.label" /></a>
<a class="button" href="javascript:void(0);" onclick='showSmallModalessDialog("<wasp:relativeUrl value="job/${job.getId()}/samplePrepComment.do" />");' ><fmt:message key="jobHomeSamples.viewSamplePrepComment.label" /></a>
<sec:authorize access="hasRole('su') or hasRole('ft')">
	<c:if test="${numberOfLibrariesAwaitingPlatformUnitPlacement>1}"> 
		<a class="button" href="javascript:void(0);" onclick='loadNewPageWithAjax("<wasp:relativeUrl value="job/${job.getId()}/addLibrariesToCell.do" />");' ><fmt:message key="jobHomeSamples.assignMultipleLibraries.label" /></a><br />
	</c:if> 
</sec:authorize>
<br /><br />					
<table class="data" style="margin: 0px 0px">
<c:forEach items="${submittedObjectList}" var="submittedObject" varStatus="statusSubmittedObject">
	 
	<c:if test="${statusSubmittedObject.index>0}">
		<tr class="FormData"><td colspan="4" class="label-centered" style="height:1px;background-color:black; white-space:nowrap;"></td></tr>
	</c:if>
	
	<c:if test="${statusSubmittedObject.first}">
		<tr class="FormData">
			<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;"></td>
			<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;"><fmt:message key="jobHomeSamples.macromolecule.label" /></td>
			<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;"><fmt:message key="jobHomeSamples.library.label" /></td>
			<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;"><fmt:message key="jobHomeSamples.runs.label" /></td>			
		</tr>	
	</c:if>
	
	<c:choose>
		<c:when test="${submittedMacromoleculeList.contains(submittedObject)}">
			<c:set value="${submittedMacromoleculeFacilityLibraryListMap.get(submittedObject)}" var="libraryList"/>
		</c:when>
		<c:otherwise>
			<c:set value="${submittedLibrarySubmittedLibraryListMap.get(submittedObject)}" var="libraryList"/>
		</c:otherwise>
	</c:choose>
	<c:set value="${fn:length(libraryList)}" var="sizeOfLibraryList"/>
	
	<tr>
	<%-- 	<td class="DataTD"  style="text-align:center; white-space:nowrap;" rowspan="${submittedObjectLibraryRowspan.get(submittedObject)}"  style="text-align:center; white-space:nowrap;">
	--%>
			<td class="DataTD"  style="vertical-align:middle; text-align:center; white-space:nowrap;" rowspan="${sizeOfLibraryList==0?1:sizeOfLibraryList}"  style="text-align:center; white-space:nowrap;">
				<c:out value="${statusSubmittedObject.count}"/>
			</td>
			<td class="DataTD"  style="text-align:center; white-space:nowrap;" rowspan="${sizeOfLibraryList==0?1:sizeOfLibraryList}"  style="text-align:center; white-space:nowrap;">
			<c:choose>
				<c:when test="${submittedMacromoleculeList.contains(submittedObject)}">					
					<label><fmt:message key="jobHomeSamples.name.label" />:</label> <a href="javascript:void(0);" onclick='loadNewPageWithAjax("<wasp:relativeUrl value="job/${job.getId()}/sample/${submittedObject.getId()}/sampledetail_ro.do" />");' ><c:out value="${submittedObject.getName()}" /></a><br />
					<label><fmt:message key="jobHomeSamples.id.label" />:</label> <c:out value="${submittedObject.getId()}"/><br />
					<label><fmt:message key="jobHomeSamples.type.label" />:</label> <c:out value="${submittedObject.getSampleType().getName()}"/><br />
					<label><fmt:message key="jobHomeSamples.species.label" />:</label> <c:out value="${submittedObjectOrganismMap.get(submittedObject)}" /><br />
					<label><fmt:message key="jobHomeSamples.arrivalStatus.label" />:</label> <c:out value="${receivedStatusMap.get(submittedObject)}" /><br />
					<c:if test='${qcStatusMap.get(submittedObject) != "NONEXISTENT" && receivedStatusMap.get(submittedObject) == "RECEIVED"}'>
						<label><fmt:message key="listJobSamples.qcStatus.label" /></label>: <c:out value="${qcStatusMap.get(submittedObject)}"/>
						<c:set value="${qcStatusCommentsMap.get(submittedObject)}" var="metaMessageList" />
						<c:if test="${metaMessageList.size()>0}">
							<fmt:formatDate value="${metaMessageList[0].getDate()}" pattern="yyyy-MM-dd" var="date" />
		  					<wasp:comment value="${metaMessageList[0].getValue()} (${date})" />
						</c:if>
						<br />		  
					</c:if>
					<sec:authorize access="hasRole('su') or hasRole('ft')">
						<c:if test='${jobStatus != "Completed" and receivedStatusMap.get(submittedObject)=="RECEIVED" && qcStatusMap.get(submittedObject)=="PASSED"}'>
							<c:choose>	 	 						
 	 							<c:when test='${sizeOfLibraryList == 0}'>
 	 								<br />	 	 								
 	 									<a class="button" style="margin-left: auto;margin-right: auto;" href="javascript:void(0);" onclick='loadNewPageWithAjax("<wasp:relativeUrl value="job/${job.getId()}/macromolecule/${submittedObject.getId()}/createLibrary.do" />");' ><fmt:message key="listJobSamples.createLibrary.label" /></a>
 	 								<br /><br /> 
 	 							</c:when>
 	 							<c:otherwise >	 	 							
 	 								<br />		 	 												
	 								<a class="button" style="margin-left: auto;margin-right: auto;" href="javascript:void(0);" onclick='loadNewPageWithAjax("<wasp:relativeUrl value="job/${job.getId()}/macromolecule/${submittedObject.getId()}/createLibrary.do" />");' ><fmt:message key="listJobSamples.createAdditionalLibrary.label" /></a>
 	 								<br /><br /> 	 	 								
 	 							</c:otherwise>
 	 						</c:choose>
	 					</c:if>				
					</sec:authorize>
				</c:when>
				<c:otherwise>
					<fmt:message key="jobHomeSamples.notApplicable.label" />
				</c:otherwise>
			</c:choose>
		</td>
		<c:choose>
			<c:when test="${fn:length(libraryList)==0}">
				<td class="DataTD" style="text-align:center; white-space:nowrap;"><fmt:message key="jobHomeSamples.noLibraries.label" /></td>
				<td class="DataTD" style="text-align:center; white-space:nowrap;"><fmt:message key="jobHomeSamples.noRuns.label" /></td>
			</c:when>
			<c:otherwise>
				<c:forEach items="${libraryList}" var="library" varStatus="statusLibrary">
					<c:if test="${!statusLibrary.first}"><tr></c:if>	
						<td class="DataTD" style="text-align:center; white-space:nowrap;" rowspan="1">
							<label><fmt:message key="jobHomeSamples.name.label" />:</label> <a id="librarydetail_roAnchor"  href="javascript:void(0);" onclick='loadNewPageWithAjax("<wasp:relativeUrl value="job/${job.getId()}/library/${library.getId()}/librarydetail_ro.do" />");' ><c:out value="${library.getName()}" /></a><br />
							<label><fmt:message key="jobHomeSamples.id.label" />:</label> <c:out value="${library.getId()}"/><br />
							<label><fmt:message key="jobHomeSamples.type.label" />:</label> <c:out value="${library.getSampleType().getName()}" /><br />
							<c:if test="${submittedLibraryList.contains(library)}">
								<label><fmt:message key="jobHomeSamples.species.label" />:</label> <c:out value="${submittedObjectOrganismMap.get(library)}" /><br />
							</c:if>
							<c:set value="${libraryAdaptorsetMap.get(library)}" var="adaptorSet"/>
							<label><fmt:message key="jobHomeSamples.adaptor.label" />:</label> <c:out value="${adaptorSet.getName()}" /> <br />
							 <c:set value="${libraryAdaptorMap.get(library)}" var="adaptor"/>
							<label><fmt:message key="jobHomeSamples.index.label" />:</label> <c:out value="${adaptor.getBarcodenumber()}" /> [<c:out value="${adaptor.getBarcodesequence()}" />]<br />
							<c:if test="${not empty receivedStatusMap.get(library)}">
								<label><fmt:message key="jobHomeSamples.arrivalStatus.label" />:</label> <c:out value="${receivedStatusMap.get(library)}" /><br />
							</c:if>
							<c:set value="${reasonForNewLibraryCommentsMap.get(library)}" var="reasonForNewLibraryMetaMessageList" />
							<c:if test="${reasonForNewLibraryMetaMessageList.size()>0}">
								<label><fmt:message key="listJobSamples.reasonForNewLibrary.label" />: </label>
								<fmt:formatDate value="${reasonForNewLibraryMetaMessageList[0].getDate()}" pattern="yyyy-MM-dd" var="date" />
		  						<wasp:comment value="${reasonForNewLibraryMetaMessageList[0].getValue()} (${date})" />
		  						<br />	
							</c:if>	
							<c:if test='${submittedLibraryList.contains(library) && qcStatusMap.get(library) != "NONEXISTENT" && receivedStatusMap.get(library) == "RECEIVED"}'>
								<label><fmt:message key="listJobSamples.qcStatus.label" /></label>: <c:out value="${qcStatusMap.get(library)}"/>
								<c:set value="${qcStatusCommentsMap.get(library)}" var="metaMessageList" />
								<c:if test="${metaMessageList.size()>0}">
									<fmt:formatDate value="${metaMessageList[0].getDate()}" pattern="yyyy-MM-dd" var="date" />
		  							<wasp:comment value="${metaMessageList[0].getValue()} (${date})" />
								</c:if>	
								<br />				  
							</c:if>	
							<c:if test='${!submittedLibraryList.contains(library) && qcStatusMap.get(library) != "NONEXISTENT"}'>
								<label><fmt:message key="listJobSamples.qcStatus.label" /></label>: <c:out value="${qcStatusMap.get(library)}"/>
								<c:set value="${qcStatusCommentsMap.get(library)}" var="metaMessageList" />
								<c:if test="${metaMessageList.size()>0}">									
									<fmt:formatDate value="${metaMessageList[0].getDate()}" pattern="yyyy-MM-dd" var="date" />
		  							<wasp:comment value="${metaMessageList[0].getValue()} (${date})" />
								</c:if>	
								<br /> 	 						  
							</c:if>								
							<sec:authorize access="hasRole('su') or hasRole('ft')">
							<c:if test='${qcStatusMap.get(library) == "PASSED"}'>	
							
								<c:if test="${fn:length(addLibraryToPlatformUnitSuccessMessage)>0 && libraryIdAssociatedWithMessage == library.getId()}">
									<br /><span style="color:green;font-weight:bold"><c:out value="${addLibraryToPlatformUnitSuccessMessage}" /></span>
								</c:if>	
								
								<c:if test='${assignLibraryToPlatformUnitStatusMap.get(library) == true}'> 
			 															 							
 	 								<form  method='post' name='addLibToCell_${library.getId()}' id="addLibToCell_${library.getId()}" action="" 
 	 									onsubmit='	var s = document.getElementById("cellId_${library.getId()}"); 
 	 												var sVal = s.options[s.selectedIndex].value; 
 	 												if(sVal=="0" || sVal==""){
 	 													alert("Please select a cell"); s.focus(); return false; 
 	 												} 
 	 												var libConcInCellPicoM = document.getElementById("libConcInCellPicoM_${library.getId()}"); 
 	 												
 	 												if(libConcInCellPicoM.value =="" || libConcInCellPicoM.value.replace(/^\s+|\s+$/g, "") ==""){ //trim from both ends
														alert("<fmt:message key="listJobSamples.valFinalConc_alert.label" />");
														libConcInCellPicoM.value = "";
														libConcInCellPicoM.focus();
														return false;
													}
													var regExpr = new RegExp("^[0-9]+\.?[0-9]*$");//modified from http://stackoverflow.com/questions/469357/html-text-input-allow-only-numeric-input (modified example 14)
    												if (!regExpr.test(libConcInCellPicoM.value)) {
    													alert("<fmt:message key="listJobSamples.numValFinalConc_alert.label" />");
														libConcInCellPicoM.value = "";
														libConcInCellPicoM.focus();
														return false;
    												}
 	 												
 	 												
 	 												postFormWithAjax("addLibToCell_${library.getId()}","<wasp:relativeUrl value="job/${job.getId()}/library/${library.getId()}/addToCell.do" />"); 
 	 												return false;' >
										<table class='data' style="margin: 0 auto;">
											<tr class="FormData"><td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;"><fmt:message key="listJobSamples.addLibraryToPlatformUnit.label" /></td></tr>
											<tr>
												<td>
													<br />
													<select style="font-size:x-small;" name="cellId" id="cellId_<c:out value="${library.getId()}" />" size="1" >
														<option value="0"><fmt:message key="listJobSamples.selectPlatformUnitCell.label" /></option>
														<c:forEach items="${availableAndCompatiblePlatformUnitListOnForm}" var="platformUnitOnForm">
															<option value="0"><fmt:message key="listJobSamples.platformUnit.label" />: <c:out value="${platformUnitOnForm.getName()}" /> [<c:out value="${platformUnitOnForm.getSampleSubtype().getName()}" />]</option>
															<c:set value="${platformUnitCellListMapOnForm.get(platformUnitOnForm)}" var="cellListOnForm" />
															<c:forEach items="${cellListOnForm}" var="cellOnForm">
																<option style="color:red; font-weight: bold;" value="<c:out value="${cellOnForm.getSampleId()}" />">&nbsp;&nbsp;&nbsp;<fmt:message key="listJobSamples.cell.label" />: <c:out value="${cellOnForm.getName()}" /></option>				
											 					<c:set value="${cellControlLibraryListMapOnForm.get(cellOnForm)}" var="controlLibraryListOnForm" />
											 					<c:forEach items="${controlLibraryListOnForm}" var="controlLibraryOnForm">
											 						<option value="0">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<fmt:message key="listJobSamples.libraryControl.label" />: <c:out value="${controlLibraryOnForm.getName()}" />									
											 						<c:set var="adaptor" value="${libraryAdaptorMapOnForm.get(controlLibraryOnForm)}" scope="page" />
																	 &nbsp;-&nbsp;<c:out value="${adaptor.getAdaptorset().getName()}"/>&nbsp;[<fmt:message key="listJobSamples.index.label" /> <c:out value="${adaptor.getBarcodenumber()}"/>&nbsp;(<c:out value="${adaptor.getBarcodesequence()}"/>)]
											 						</option>
											 					</c:forEach>
											 
											 					<c:set value="${cellLibraryWithoutControlListMapOnForm.get(cellOnForm)}" var="libraryListOnForm" />
											 					<c:forEach items="${libraryListOnForm}" var="libraryOnForm">
											 						<option value="0">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<fmt:message key="listJobSamples.library.label" />: <c:out value="${libraryOnForm.getName()}" />									
											 						<c:set var="adaptorOnForm" value="${libraryAdaptorMapOnForm.get(libraryOnForm)}" scope="page" />
											 						&nbsp;-&nbsp;<c:out value="${adaptorOnForm.getAdaptorset().getName()}"/>&nbsp;[<fmt:message key="listJobSamples.index.label" /> <c:out value="${adaptorOnForm.getBarcodenumber()}"/>&nbsp;(<c:out value="${adaptorOnForm.getBarcodesequence()}"/>)]
											 						</option>
											 					</c:forEach>
											 
															</c:forEach>
														</c:forEach>						
													</select>
													<br /><fmt:message key="listJobSamples.finalConcentrationPM.label" />: <input type='text' name='libConcInCellPicoM' id="libConcInCellPicoM_<c:out value="${library.getId()}" />" size='3' maxlength='5'>
													<c:if test="${fn:length(addLibraryToPlatformUnitErrorMessage)>0 && libraryIdAssociatedWithMessage == library.getId()}">
														<br /><span style="color:red;font-weight:bold"><c:out value="${addLibraryToPlatformUnitErrorMessage}" /></span>
													</c:if>
													<br /><input type='submit' value='<fmt:message key="listJobSamples.submit.label" />'/>&nbsp;<input type='reset' value='<fmt:message key="jobHomeSamples.reset.label" />'/>
																	
											</td>
										</tr>
									</table>
								</form> 	 							
	 						</c:if>
 	 					</c:if>
 	 					</sec:authorize>	
						</td>
						
						<c:set value="${cellLibraryListMap.get(library)}" var="cellList"/>		   				
		   				
		   				<td class="DataTD" style="text-align:center; white-space:nowrap;">
		   				    <c:choose>
			   					<c:when test="${fn:length(removeLibraryFromCellSuccessMessage)>0 && libraryIdAssociatedWithMessage == library.getId()}">
									<span style="color:green;font-weight:bold"><c:out value="${removeLibraryFromCellSuccessMessage}" /></span>
									<br />
								</c:when>	
		   						<c:when test="${fn:length(removeLibraryFromCellErrorMessage)>0 && libraryIdAssociatedWithMessage == library.getId()}">
									<span style="color:red;font-weight:bold"><c:out value="${removeLibraryFromCellErrorMessage}" /></span>
									<br />
								</c:when>	
				   				<c:when test="${fn:length(updateConcentrationToCellLibrarySuccessMessage)>0 && libraryIdAssociatedWithMessage == library.getId()}">
									<span style="color:green;font-weight:bold"><c:out value="${updateConcentrationToCellLibrarySuccessMessage}" /></span>
									<br />
								</c:when>	
		   						<c:when test="${fn:length(updateConcentrationToCellLibraryErrorMessage)>0 && libraryIdAssociatedWithMessage == library.getId()}">
									<span style="color:red;font-weight:bold"><c:out value="${updateConcentrationToCellLibraryErrorMessage}" /></span>
									<br />
								</c:when>	
		   					</c:choose>
		   				<c:choose>
		   					<c:when test="${fn:length(cellList)==0}">
		   						no runs
		   					</c:when>
		   					<c:otherwise>	
		   						<c:forEach items="${cellList}" var="cell" varStatus="cellLibrary">
									<c:set value="${cellRunMap.get(cell)}" var="run"/>
									<c:set value="${cellPUMap.get(cell)}" var="pu"/>
									<c:set value="${cellIndexMap.get(cell)}" var="laneIndex"/>
									
									<c:set value="${cellLibraryPMLoadedMap.get(cell)}" var="libraryPMLoadedMap" />
									
									<c:set value="${libraryPMLoadedMap.get(library)}" var="pMLoaded" />
									
									
									<c:choose>
										<c:when test="${empty cellRunMap.get(cell)}">
											<sec:authorize access="hasRole('su') or hasRole('ft')">
												<a href="<wasp:relativeUrl value="${showPlatformunitViewMap.get(pu)}" />"><c:out value="${pu.getName()}" /></a>
											</sec:authorize>
											<sec:authorize access="not hasRole('su') and not hasRole('ft')">
												<c:out value="${pu.getName()}" />
											</sec:authorize>
										</c:when>
										<c:otherwise>
											<sec:authorize access="hasRole('su') or hasRole('ft')">
												<a href="<wasp:relativeUrl value="${showPlatformunitViewMap.get(pu)}" />"><c:out value="${run.getName()}" /></a>
											</sec:authorize>
											<sec:authorize access="not hasRole('su') and not hasRole('ft')">
												<c:out value="${run.getName()}" />
											</sec:authorize>
										</c:otherwise>
									</c:choose>	
																	
									(<c:out value="${pMLoaded}" /> <fmt:message key="jobHomeSamples.picoMolar.label" />; <fmt:message key="jobHomeSamples.lane.label" /> <c:out value="${laneIndex}" />)
									
									<sec:authorize access="hasRole('su') or hasRole('ft')">									
									[<c:if test="${empty cellRunMap.get(cell)}"><a href="javascript:void(0);" onclick='if(confirm("Permanently remove library from this lane?")){loadNewPageWithAjax("<wasp:relativeUrl value="job/${job.getId()}/cell/${cell.getId()}/library/${library.getId()}/removeLibrary.do" />");}'>remove</a> | </c:if><a href="javascript:void(0);" onclick='var obj = document.getElementById("updatePM_DIV_${cell.getId()}_${library.getId()}"); obj.style.display="inline";'>update</a>] 
									 
									 	<div  id="updatePM_DIV_${cell.getId()}_${library.getId()}" style="display:none;">
									 	<form  style="display:inline;" method='post'  id="updatePM_${cell.getId()}_${library.getId()}" action="" 
	 	 									onsubmit='	 	 									
	 	 										var newConcentrationInPM = document.getElementById("newConcentrationInPM_${cell.getId()}_${library.getId()}"); 
	 	 										if(newConcentrationInPM.value =="" || newConcentrationInPM.value.replace(/^\s+|\s+$/g, "") ==""){ //trim from both ends
													alert("<fmt:message key="listJobSamples.valFinalConc_alert.label" />");
													newConcentrationInPM.value = "";
													newConcentrationInPM.focus();
													return false;
												}
												var regExpr = new RegExp("^[0-9]+\.?[0-9]*$");//modified from http://stackoverflow.com/questions/469357/html-text-input-allow-only-numeric-input (modified example 14)
	    										if (!regExpr.test(newConcentrationInPM.value)) {
	    											alert("<fmt:message key="listJobSamples.numValFinalConc_alert.label" />");
													newConcentrationInPM.value = "";
													newConcentrationInPM.focus();
													return false;
	    										}	    												
	 	 										postFormWithAjax("updatePM_${cell.getId()}_${library.getId()}","<wasp:relativeUrl value="job/${job.getId()}/cell/${cell.getId()}/library/${library.getId()}/updateConcentration.do" />"); 
	 												return false;' ><input type='text' name='newConcentrationInPM' id="newConcentrationInPM_${cell.getId()}_${library.getId()}"  size='3' maxlength='5'  /> <fmt:message key="jobHomeSamples.picoMolar.label" /> <input type='submit' value='<fmt:message key="jobHomeSamples.update.label" />'/><input class="button" type="button" value="<fmt:message key="jobHomeSamples.cancel.label" />" onclick='loadNewPageWithAjax("<wasp:relativeUrl value="job/${job.getId()}/samples.do" />");' />
	 	 								</form>
	 	 								</div>	
									
									</sec:authorize>
									<br />
		   						</c:forEach>
		   					</c:otherwise>
		   				</c:choose>	
		   				</td>	   				
					<c:if test="${!statusLibrary.last}"></tr></c:if>
				</c:forEach>
			</c:otherwise>
		</c:choose>
	</tr>
</c:forEach>
</table>
<br />
