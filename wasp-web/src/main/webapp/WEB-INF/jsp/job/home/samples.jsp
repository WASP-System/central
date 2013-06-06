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
					<label>Name (div):</label> <a id="sampledetail_roAnchor"  href="javascript:void(0);" onclick='loadNewPage(this, "<c:url value="/job/${job.getId()}/sample/${submittedObject.getId()}/sampledetail_ro.do" />");' ><c:out value="${submittedObject.getName()}" /></a><br />
					<label>Name (IFrame):</label> <a id="sampledetail_roAnchor1"  href="javascript:void(0);" onclick='loadIFrameAnotherWay(this, "<c:url value="/job/${job.getId()}/sample/${submittedObject.getId()}/sampledetail_ro.do" />");' ><c:out value="${submittedObject.getName()}" /></a><br />
					<label>Name (Modeless):</label> <a id="sampledetail_roAnchor2"  href="javascript:void(0);" onclick='showModalessDialog("<c:url value="/job/${job.getId()}/sample/${submittedObject.getId()}/sampledetail_ro.do" />");' ><c:out value="${submittedObject.getName()}" /></a><br />
					<label>Name (Modal):</label> <a id="sampledetail_roAnchor3"  href="javascript:void(0);" onclick='showModalDialog("<c:url value="/job/${job.getId()}/sample/${submittedObject.getId()}/sampledetail_ro.do" />");' ><c:out value="${submittedObject.getName()}" /></a><br />
					<label>Name (NewWindow):</label> <a id="sampledetail_roAnchor4"  href="javascript:void(0);" onclick='showPopupWindow("<c:url value="/job/${job.getId()}/sample/${submittedObject.getId()}/sampledetail_ro.do" />");' ><c:out value="${submittedObject.getName()}" /></a><br />
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
							<c:if test='${qcStatusMap.get(library) == "PASSED"}'><%--MUST RESPOND TO BATCH!!!!!, currently doesn't --%>							
 	 							<br />
 	 							<a class="button" href="javascript:void(0);" onclick='showModalDialog("<c:url value="/job/${job.getId()}/sample/${submittedObject.getId()}/sampledetail_ro.do" />");' >Add To PlatformUnit</a>
 	 							<br /><br />
 	 							<a id="samplesAnchor"  href="javascript:void(0);" onclick='loadNewPageWithoutMoving(this, "<c:url value="/job/${job.getId()}/samples.do" />");' >Add To PlatformUnit</a>
		
 	 						</c:if>	
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