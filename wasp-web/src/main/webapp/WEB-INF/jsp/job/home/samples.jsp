<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<table class="data" style="margin: 0px 0px">
<c:forEach items="${submittedObjectList}" var="submittedObject" varStatus="statusSubmittedObject">
	 
	<c:if test="${statusSubmittedObject.index>0}">
		<tr class="FormData"><td colspan="3" class="label-centered" style="height:1px;background-color:black; white-space:nowrap;"></td></tr>
	</c:if>
	
	<c:if test="${statusSubmittedObject.first}">
		<tr class="FormData">
			<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;">Macromolecule</td>
			<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;">Library</td>
			<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;">Runs</td>			
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
	
	<tr>
		<td class="DataTD"  style="text-align:center; white-space:nowrap;" rowspan="${submittedObjectLibraryRowspan.get(submittedObject)}"  style="text-align:center; white-space:nowrap;">
			<c:choose>
				<c:when test="${submittedMacromoleculeList.contains(submittedObject)}">
					
					<%-- 
					<label>Name (div):</label> <a id="sampledetail_roAnchor"  href="javascript:void(0);" onclick='loadNewPage(this, "<c:url value="/job/${job.getId()}/sample/${submittedObject.getId()}/sampledetail_ro.do" />");' ><c:out value="${submittedObject.getName()}" /></a><br />
					<label>Name (Modeless):</label> <a id="sampledetail_roAnchor2"  href="javascript:void(0);" onclick='showModalessDialog("<c:url value="/job/${job.getId()}/sample/${submittedObject.getId()}/sampledetail_ro.do" />");' ><c:out value="${submittedObject.getName()}" /></a><br />
					<label>Name (Modal):</label> <a id="sampledetail_roAnchor3"  href="javascript:void(0);" onclick='showModalDialog("<c:url value="/job/${job.getId()}/sample/${submittedObject.getId()}/sampledetail_ro.do" />");' ><c:out value="${submittedObject.getName()}" /></a><br />
					<label>Name (NewWindow):</label> <a id="sampledetail_roAnchor4"  href="javascript:void(0);" onclick='showPopupWindow("<c:url value="/job/${job.getId()}/sample/${submittedObject.getId()}/sampledetail_ro.do" />");' ><c:out value="${submittedObject.getName()}" /></a><br />
					
					<label>Name (NewWindow):</label> <a id="sampledetail_roAnchor4"  href="javascript:void(0);" onclick='showPopupWindow("<c:url value="/wasp-illumina/flowcell/7/show.do" />");' >see a flowcell</a><br />
					--%>
					<label>Name:</label> <a id="sampledetail_roAnchor1"  href="javascript:void(0);" onclick='loadIFrameAnotherWay(this, "<c:url value="/job/${job.getId()}/sample/${submittedObject.getId()}/sampledetail_ro.do" />");' ><c:out value="${submittedObject.getName()}" /></a><br />
					<label>Type:</label> <c:out value="${submittedObject.getSampleType().getName()}"/><br />
					<label>Species:</label> <c:out value="${submittedObjectOrganismMap.get(submittedObject)}" /><br />
					<label>Arrival Status:</label> <c:out value="${receivedStatusMap.get(submittedObject)}" /><br />
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
						<c:if test='${receivedStatusMap.get(submittedObject)=="RECEIVED" && qcStatusMap.get(submittedObject)=="PASSED"}'>
							<c:if test='${not empty createLibraryStatusMap.get(submittedObject) and createLibraryStatusMap.get(submittedObject) == true}'>
								<%-- <input class="fm-button" type="button" value="<fmt:message key="listJobSamples.createLibrary.label" />"  onClick="window.location='<c:url value="/sampleDnaToLibrary/createLibraryFromMacro/${job.jobId}/${userSubmittedMacromolecule.sampleId}.do"/>'" />
 	 							<a class="button" href="javascript:void(0);" onclick='parent.loadIFrameAnotherWay(this, "<c:url value="/job/${job.getId()}/sample/${submittedObject.getId()}/sampledetail_ro.do" />");' ><fmt:message key="listJobSamples.createLibrary.label" /></a>
	 							
 	 							--%>
 	 							<br />
 	 							<a class="button" href="javascript:void(0);" onclick='parent.loadIFrameAnotherWay(this, "<c:url value="/job/${job.getId()}/macromolecule/${submittedObject.getId()}/createLibrary.do" />");' ><fmt:message key="listJobSamples.createLibrary.label" /></a>
 	 							<br /><br />
 	 							
 	 						</c:if>
	 					</c:if>
					</sec:authorize>
				</c:when>
				<c:otherwise>
					N/A
				</c:otherwise>
			</c:choose>
		</td>
		<c:choose>
			<c:when test="${fn:length(libraryList)==0}">
				<td class="DataTD" style="text-align:center; white-space:nowrap;">no libraries</td>
				<td class="DataTD" style="text-align:center; white-space:nowrap;">no runs</td>
			</c:when>
			<c:otherwise>
				<c:forEach items="${libraryList}" var="library" varStatus="statusLibrary">
					<c:if test="${!statusLibrary.first}"><tr></c:if>	
						<td class="DataTD" style="text-align:center; white-space:nowrap;" rowspan="1">
							<label>Name:</label> <a id="librarydetail_roAnchor"  href="javascript:void(0);" onclick='loadIFrameAnotherWay(this, "<c:url value="/job/${job.getId()}/library/${library.getId()}/librarydetail_ro.do" />");' ><c:out value="${library.getName()}" /></a><br />
							<label>Type:</label> <c:out value="${library.getSampleType().getName()}" /><br />
							<c:if test="${submittedLibraryList.contains(library)}">
								<label>Species:</label> <c:out value="${submittedObjectOrganismMap.get(library)}" /><br />
							</c:if>
							<c:set value="${libraryAdaptorsetMap.get(library)}" var="adaptorSet"/>
							<label>Adaptor:</label> <c:out value="${adaptorSet.getName()}" /> <br />
							 <c:set value="${libraryAdaptorMap.get(library)}" var="adaptor"/>
							<label>Index:</label> <c:out value="${adaptor.getBarcodenumber()}" /> [<c:out value="${adaptor.getBarcodesequence()}" />]<br />
							<c:if test="${not empty receivedStatusMap.get(library)}">
								<label>Arrival Status:</label> <c:out value="${receivedStatusMap.get(library)}" /><br />
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
							<c:if test='${qcStatusMap.get(library) == "PASSED"}'><%--MUST RESPOND TO BATCH!!!!!, currently doesn't --%>	
									<c:if test="${fn:length(addLibraryToPlatformUnitSuccessMessage)>0 && libraryIdAssociatedWithMessage == library.getId()}">
										<br /><span style="color:green;font-weight:bold"><c:out value="${addLibraryToPlatformUnitSuccessMessage}" /></span>
									</c:if>														 							
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
 	 												
 	 												
 	 												postFormWithoutMoving("addLibToCell_${library.getId()}","<c:url value="/job/${job.getId()}/library/${library.getId()}/add.do" />"); 
 	 												return false;' >
										<table class='data' style="margin: 0 auto;">
										<tr class="FormData"><td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;"><fmt:message key="listJobSamples.addLibraryToPlatformUnit.label" /></td></tr>
										<tr>
										<td>
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
											<br /><input type='submit' value='<fmt:message key="listJobSamples.submit.label" />'/>&nbsp;<input type='reset' value='Reset'/>
																	
										</td>
										</tr>
										</table>
									</form> 	 							
 	 							
 	 							<%-- <a class="button" href="javascript:void(0);" onclick='showModalDialog("<c:url value="/job/${job.getId()}/sample/${submittedObject.getId()}/sampledetail_ro.do" />");' >Add To PlatformUnit</a>
 	 							<br /><br />
 	 							<a id="samplesAnchor"  href="javascript:void(0);" onclick='loadNewPageWithoutMoving(this, "<c:url value="/job/${job.getId()}/samples.do" />");' >Add To PlatformUnit</a>
								<br /><br />--%>
								<%--
								<div id="rob_${library.getId()}">
 	 								<a class="button" href="javascript:void(0);" onclick='if(this.innerHTML=="Close"){this.parentNode.childNodes[6].style.display="none"; this.innerHTML="Add To PlatformUnit"; }else{this.innerHTML="Close"; this.parentNode.childNodes[6].style.display="block";} /* var x = this.parentNode.id; alert(x); var y = this.parentNode.childNodes[3]; y.style.display="none";*/ ' >Add To PlatformUnit</a>
 	 								<br /><br />
 	 								<div id="les_${library.getId()}" style="display:none;">
									 
										<form  method='post' name='addLibToCell_${library.getId()}' id="addLibToCell_${library.getId()}" action="" onsubmit='var s = document.getElementById("cellId_${library.getId()}"); var sVal = s.options[s.selectedIndex].value; if(sVal=="0" || sVal==""){alert("Please select a cell"); return false; } postFormWithoutMoving("addLibToCell_${library.getId()}","<c:url value="/job/${job.getId()}/library/${library.getId()}/add.do" />"); return false;' >
										<table class='data'>
										<tr class="FormData"><td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;"><fmt:message key="listJobSamples.addLibraryToPlatformUnit.label" /></td></tr>
										<tr>
										<td>
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
											<br />&nbsp;<fmt:message key="listJobSamples.finalConcentrationPM.label" />: <input type='text' name='libConcInCellPicoM' id="libConcInCellPicoM_<c:out value="${library.getId()}" />" size='3' maxlength='5'>
											<br />&nbsp;<input type='submit' value='<fmt:message key="listJobSamples.submit.label" />'/>&nbsp;<input class="button" type="button" value="<fmt:message key="listJobSamples.cancel.label" />" onclick='alert("cancel button and the anchor id may be " + this.parentNode.childNodes[2].id); this.parentNode.childNodes[3].style.display="none";' />
																	
										</td>
										</tr>
										</table>
										</form>	
																		
 	 								</div>
								</div>
								--%>
 	 						</c:if>
 	 						</sec:authorize>	
						</td>
						
						<c:set value="${libraryCellListMap.get(library)}" var="cellList"/>		   				
		   				
		   				<td class="DataTD" style="text-align:center; white-space:nowrap;">
		   				<c:choose>
		   					<c:when test="${fn:length(cellList)==0}">
		   						no runs
		   					</c:when>
		   					<c:otherwise>	
		   						<c:forEach items="${cellList}" var="cell" varStatus="cellLibrary">
									<c:set value="${cellRunMap.get(cell)}" var="run"/>
									<c:set value="${cellPUMap.get(cell)}" var="pu"/>
									<c:set value="${cellIndexMap.get(cell)}" var="laneIndex"/>
									<c:choose>
										<c:when test="${empty cellRunMap.get(cell)}">
											<c:out value="${pu.getName()}" />
										</c:when>
										<c:otherwise>
											<c:out value="${run.getName()}" />
										</c:otherwise>
									</c:choose>										
									(Lane: <c:out value="${laneIndex}" />)
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