<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
<br />
<title><fmt:message key="pageTitle.sampleDnaToLibrary/listJobSamples.label"/></title>
<h1><fmt:message key="pageTitle.sampleDnaToLibrary/listJobSamples.label"/></h1>
<c:import url="/WEB-INF/jsp/sampleDnaToLibrary/jobdetail_for_import.jsp" />
<br />

<div>
<input  class="fm-button" type="button" id="requested_coverage_show_hide_button" value="<fmt:message key="listJobSamples.showUserRequestedCoverage.label" />"  />
</div>
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
				<fmt:message key="listJobSamples.name.label" />: <a href="<c:url value="/sampleDnaToLibrary/sampledetail_ro/${job.jobId}/${userSubmittedMacromolecule.getSampleId()}.do" />"><c:out value="${userSubmittedMacromolecule.getName()}"/></a><br />
				<fmt:message key="listJobSamples.type.label" />: <c:out value="${userSubmittedMacromolecule.getSampleType().getName()}"/><br />
				<fmt:message key="listJobSamples.species.label" />: <c:out value="${speciesMap.get(userSubmittedMacromolecule)}"/><br />
				<fmt:message key="listJobSamples.arrivalStatus.label" />: <c:out value="${receivedStatusMap.get(userSubmittedMacromolecule)}"/>
				<sec:authorize access="hasRole('su') or hasRole('ft')">&nbsp;
				<%--  please leave, may be useful later <a href="<c:url value="/task/updatesamplereceive/${job.jobId}.do" />">[update]</a>--%>
				<c:if test='${receiveSampleStatusMap.get(userSubmittedMacromolecule)=="CREATED"}'>
					<a href="<c:url value="/task/samplereceive/list.do" />">[<fmt:message key="listJobSamples.logSample.label" />]</a>
				</c:if>
				</sec:authorize><br />
				
				<sec:authorize access="hasRole('su') or hasRole('ft')">
			<%-- <c:if test='${receivedStatusMap.get(userSubmittedMacromolecule)=="RECEIVED"}'> --%>
			<%-- 5/21/12	<c:if test='${createLibraryStatusMap.get(userSubmittedMacromolecule)=="CREATED"}'>  --%>
	  				<form method="GET" action="<c:url value="/sampleDnaToLibrary/createLibraryFromMacro.do" />">
	  					<input type='hidden' name='jobId' value='<c:out value="${job.jobId}" />'/>
	  					<input type='hidden' name='macromolSampleId' value='<c:out value="${userSubmittedMacromolecule.getSampleId()}" />'/>
	  					<select class="FormElement ui-widget-content ui-corner-all" name="adaptorsetId" size="1" onchange="if(this.options[selectedIndex].value != 0){this.parentNode.submit();}">
							<option value="0"><fmt:message key="listJobSamples.selectAdaptorSetForNewLibrary.label" />
							<c:forEach items="${adaptorsets}" var="adaptorset">
								<option value="<c:out value="${adaptorset.adaptorsetId}" />" ><c:out value="${adaptorset.name}" /> 
							</c:forEach>
						</select>
					</form>
	 	 	<%-- 5/21/12	</c:if> --%>
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
							<fmt:message key="listJobSamples.name.label" />: <a href="<c:url value="/sampleDnaToLibrary/librarydetail_ro/${job.jobId}/${facilityLibraryForThisMacromolecule.getSampleId()}.do" />"><c:out value="${facilityLibraryForThisMacromolecule.getName()}"/></a><br />
							<fmt:message key="listJobSamples.type.label" />: <c:out value="${facilityLibraryForThisMacromolecule.getSampleType().getName()}"/><br />
							<c:set var="adaptor" value="${libraryAdaptorMap.get(facilityLibraryForThisMacromolecule)}" scope="page" />
							<fmt:message key="listJobSamples.adaptor.label" />: <c:out value="${adaptor.getAdaptorset().getName()}"/><br />
							<fmt:message key="listJobSamples.index.label" /> <c:out value="${adaptor.getBarcodenumber()}"/> [<c:out value="${adaptor.getBarcodesequence()}"/>]<br />
														
							<c:set var="idCounter" value="${idCounter + 1}" scope="page" />
 							<sec:authorize access="hasRole('su') or hasRole('ft')">
							<div id="showButton_<c:out value="${idCounter}" />" >
						<%-- 5/21/12	<c:if test='${assignLibraryToPlatformUnitStatusMap.get(userSubmittedMacromolecule)=="CREATED"}'> --%>
				 				<input class="fm-button" type="button" value="<fmt:message key="listJobSamples.addLibraryToPlatformUnit.label" />" onclick='toggleDisplayOfAddLibraryForm("show", <c:out value="${idCounter}" />)' />				
						<%-- 5/21/12	</c:if> --%>
							</div>
							</sec:authorize>
							<div id="addLibraryForm_<c:out value="${idCounter}" />" style="display:none">
							<table class='data'>
								<tr class="FormData"><td class="label-centered"><fmt:message key="listJobSamples.addLibraryToPlatformUnit.label" /></td></tr>
							<tr><td>
							<form  method='post' name='addLibToPU' action="<c:url value="/facility/platformunit/assignAdd2.do" />" onsubmit="return validate_submit(this);">
								<input type='hidden' name='jobid' value='<c:out value="${job.jobId}" />'/>
				 				<input type='hidden' name='librarysampleid' value='<c:out value="${facilityLibraryForThisMacromolecule.getSampleId()}" />'/>
								<br />
				 				<select class="FormElement ui-widget-content ui-corner-all" name="lanesampleid" id="lanesampleid_<c:out value="${idCounter}" />" size="1" onchange="validate(this)">
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
								<br />&nbsp;<fmt:message key="listJobSamples.finalConcentrationPM.label" />: <input type='text' name='libConcInLanePicoM' id="libConcInLanePicoM_<c:out value="${idCounter}" />" size='3' maxlength='5'>
								<br />&nbsp;<input type='submit' value='<fmt:message key="listJobSamples.submit.label" />'/>&nbsp;<input class="fm-button" type="button" value="<fmt:message key="listJobSamples.cancel.label" />" onclick='toggleDisplayOfAddLibraryForm("cancel", <c:out value="${idCounter}" />)' />
							</form>
							</td></tr>
							</table>
							</div>
						
					</td>						
					<td>
					<c:set var="sampleSourceList" value="${facilityLibraryForThisMacromolecule.getSourceSampleId()}" scope="page" />
							<c:choose>
								<c:when test="${sampleSourceList.size() > 0}">
									<c:forEach items="${sampleSourceList}" var="sampleSource">
										<c:set var="cell" value="${sampleSource.getSample()}" scope="page" />
										<c:set var="sampleSourceList2" value="${cell.getSourceSampleId()}" scope="page" />
										<c:forEach items="${sampleSourceList2}" var="sampleSource2">
											<c:set var="laneNumber" value="${sampleSource2.getIndex()}" scope="page" />
											<c:set var="platformUnit" value="${sampleSource2.getSample()}" scope="page" />
											<c:out value="${platformUnit.getName()}"/> <fmt:message key="listJobSamples.cell.label" />: <c:out value="${laneNumber}"/> 
											<c:set var="runList" value="${platformUnit.getRun()}" scope="page" />
											<c:if test="${runList.size() > 0}">
												<br />&nbsp;&nbsp;&nbsp;---&gt; <c:out value="${runList.get(0).getName()}"/>
											</c:if>
											<sec:authorize access="hasRole('su') or hasRole('ft')"><a href="<c:url value="/facility/platformunit/showPlatformUnit/${platformUnit.getSampleId()}.do" />"> [<fmt:message key="listJobSamples.view.label" />]</a></sec:authorize>
											<br />
										</c:forEach>
									</c:forEach>
	
								</c:when>
								<c:otherwise>
									<fmt:message key="listJobSamples.noPlatformUnitsAndRuns.label" /> <br />
								</c:otherwise>
							</c:choose>
					</td>				
					</tr>					  	
					<c:set var="rowCounter" value="${rowCounter + 1}" scope="page" />
					</c:forEach>
				</c:when>
			</c:choose>					
	<c:if test="${numberLibrariesForThisMacromolecule == 0}">
		</tr>
	</c:if>	
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
			<fmt:message key="listJobSamples.name.label" />: <a href="<c:url value="/sampleDnaToLibrary/librarydetail_ro/${job.jobId}/${userSubmittedLibrary.getSampleId()}.do" />"><c:out value="${userSubmittedLibrary.getName()}"/></a><br />
			<fmt:message key="listJobSamples.type.label" />: <c:out value="${userSubmittedLibrary.getSampleType().getName()}"/><br />
			<fmt:message key="listJobSamples.species.label" />: <c:out value="${speciesMap.get(userSubmittedLibrary)}"/><br />
			<c:set var="adaptor" value="${libraryAdaptorMap.get(userSubmittedLibrary)}" scope="page" />
			<fmt:message key="listJobSamples.adaptor.label" />: <c:out value="${adaptor.getAdaptorset().getName()}"/><br />
			<fmt:message key="listJobSamples.index.label" /> <c:out value="${adaptor.getBarcodenumber()}"/> [<c:out value="${adaptor.getBarcodesequence()}"/>]<br />
			<fmt:message key="listJobSamples.arrivalStatus.label" />: <c:out value="${receivedStatusMap.get(userSubmittedLibrary)}"/><sec:authorize access="hasRole('su') or hasRole('ft')">&nbsp;<%--<a href="<c:url value="/task/updatesamplereceive/${job.jobId}.do" />">[update]</a>--%><c:if test='${receiveSampleStatusMap.get(userSubmittedLibrary)=="CREATED"}'><a href="<c:url value="/task/samplereceive/list.do" />">[<fmt:message key="listJobSamples.logSample.label" />]</a></c:if></sec:authorize><br />
			<%-- <c:if test='${receivedStatusMap.get(userSubmittedLibrary)=="RECEIVED"}'> --%>
				<c:set var="idCounter" value="${idCounter + 1}" scope="page" />
 				<sec:authorize access="hasRole('su') or hasRole('ft')">
				<div id="showButton_<c:out value="${idCounter}" />" >
			<%-- 5/21/12	<c:if test='${assignLibraryToPlatformUnitStatusMap.get(userSubmittedLibrary)=="CREATED"}'> --%>
					<input class="fm-button" type="button" value="<fmt:message key="listJobSamples.addLibraryToPlatformUnit.label" />" onclick='toggleDisplayOfAddLibraryForm("show", <c:out value="${idCounter}" />)' />				
			<%-- 5/21/12	</c:if> --%>
				</div>
				</sec:authorize>
				<div id="addLibraryForm_<c:out value="${idCounter}" />" style="display:none">
				<table class='data'>
					<tr class="FormData"><td class="label-centered"><fmt:message key="listJobSamples.addLibraryToPlatformUnit.label" /></td></tr>
					<tr><td>
					<form  method='post' name='addLibToPU' action="<c:url value="/facility/platformunit/assignAdd2.do" />" onsubmit="return validate_submit(this);">
						<input type='hidden' name='jobid' value='<c:out value="${job.jobId}" />'/>
				 		<input type='hidden' name='librarysampleid' value='<c:out value="${userSubmittedLibrary.getSampleId()}" />'/>
						<br />
				 		<select class="FormElement ui-widget-content ui-corner-all" name="lanesampleid" id="lanesampleid_<c:out value="${idCounter}" />" size="1" onchange="validate(this)">
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
						<br />&nbsp;<fmt:message key="listJobSamples.finalConcentrationPM.label" />: <input type='text' name='libConcInLanePicoM' id="libConcInLanePicoM_<c:out value="${idCounter}" />" size='3' maxlength='5'>
						<br />&nbsp;<input type='submit' value='<fmt:message key="listJobSamples.submit.label" />'/>&nbsp;<input class="fm-button" type="button" value="<fmt:message key="listJobSamples.cancel.label" />" onclick='toggleDisplayOfAddLibraryForm("cancel", <c:out value="${idCounter}" />)' />
					</form>
					</td></tr>
				</table>
				</div>
			<%-- </c:if> --%>
		</td>
		<td>
		<c:set var="sampleSourceList" value="${userSubmittedLibrary.getSourceSampleId()}" scope="page" />
		<c:choose>
			<c:when test="${sampleSourceList.size() > 0}">
				<c:forEach items="${sampleSourceList}" var="sampleSource">
					<c:set var="cell" value="${sampleSource.getSample()}" scope="page" />
					<c:set var="sampleSourceList2" value="${cell.getSourceSampleId()}" scope="page" />
					<c:forEach items="${sampleSourceList2}" var="sampleSource2">
						<c:set var="laneNumber" value="${sampleSource2.getIndex()}" scope="page" />
						<c:set var="platformUnit" value="${sampleSource2.getSample()}" scope="page" />
						<c:out value="${platformUnit.getName()}"/> <fmt:message key="listJobSamples.cell.label" />: <c:out value="${laneNumber}"/> 
						<c:set var="runList" value="${platformUnit.getRun()}" scope="page" />
						<c:if test="${runList.size() > 0}">
							<br />&nbsp;&nbsp;&nbsp;---&gt; <c:out value="${runList.get(0).getName()}"/>
						</c:if>
						<sec:authorize access="hasRole('su') or hasRole('ft')"><a href="<c:url value="/facility/platformunit/showPlatformUnit/${platformUnit.getSampleId()}.do" />"> [<fmt:message key="listJobSamples.view.label" />]</a></sec:authorize>
						<br />
					</c:forEach>
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