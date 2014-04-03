<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
<sec:authorize access="hasRole('su') or hasRole('ft')">
<br />
<a class="button" href="javascript:void(0);" onclick='showSmallModalessDialog("<wasp:relativeUrl value="/job/${job.getId()}/requests.do?onlyDisplayCellsRequested=true" />");' >View Lane Request</a>
<a class="button" href="javascript:void(0);"  onclick='loadNewPageWithAjax("<wasp:relativeUrl value="/job/${job.getId()}/samples.do" />");' >Back To: Samples, Libraries &amp; Runs</a><br />
<br /><br />
<form  method='post' name='addLibrariesToCell' id="addLibrariesToCellId" action="" 
	onsubmit='	 
				var s = document.getElementById("cellId"); 
				var sVal = s.options[s.selectedIndex].value; 
				if(sVal=="0" || sVal==""){
					alert("Please select a cell"); s.focus(); return false; 
				} 
				
				var libConcInCellPicoMArray = [];				
				var inputElementsOnThisForm = this.getElementsByTagName("input");
				for(var i = 0; i < inputElementsOnThisForm.length; i++) {
    				if(inputElementsOnThisForm[i].name.indexOf("libConcInCellPicoM_") == 0) {
        				libConcInCellPicoMArray.push(inputElementsOnThisForm[i]);
    				}
				}

				var atLeastOneTextboxWithValidValue = false;
				for(var i = 0; i < libConcInCellPicoMArray.length; i++){
					//alert("the value I typed in is " + libConcInCellPicoMArray[i].value);
					if(libConcInCellPicoMArray[i].value != ""){
						//alert("the value I typed in which is not blank is " + libConcInCellPicoMArray[i].value);
						
					 	if(libConcInCellPicoMArray[i].value.replace(/^\s+|\s+$/g, "") ==""){
							//alert("the value I typed in which is not blank is but needs help is" + libConcInCellPicoMArray[i].value);
						
							libConcInCellPicoMArray[i].value = "";
							continue;	 	 															
						}
						else{
							var regExpr = new RegExp("^[0-9]+\.?[0-9]*$");//modified from http://stackoverflow.com/questions/469357/html-text-input-allow-only-numeric-input (modified example 14)
								if (!regExpr.test(libConcInCellPicoMArray[i].value)) {
									alert("Please provide a numeric value in the indicated textbox containing " + libConcInCellPicoMArray[i].value);
						//libConcInCellPicoMArray[i].value = "";
						libConcInCellPicoMArray[i].focus();
						return false;
								}
							}
							atLeastOneTextboxWithValidValue=true;	    														
					}	 	 														
				}
				if(atLeastOneTextboxWithValidValue==false){
					alert("You must provide a concentration for at least one library");
					return false;
				}	
				 										
				postFormWithAjax("addLibrariesToCellId", "<wasp:relativeUrl value="/job/${job.getId()}/addLibrariesToCell.do" />"); 
				return false;' >

<table class="data" style="margin: 0px 0px">

<c:forEach items="${submittedObjectList}" var="submittedObject" varStatus="statusSubmittedObject">
	 
	<c:if test="${statusSubmittedObject.index>0}">
		<tr class="FormData"><td colspan="3" class="label-centered" style="height:1px;background-color:black; white-space:nowrap;"></td></tr>
	</c:if>
	
	<c:if test="${statusSubmittedObject.first}">
	
		<tr class="FormData"><td class="label-centered" colspan="3" style="background-color:#FAF2D6; white-space:nowrap;">Select A Cell &amp; Provide Concentration For One or More Libraries</td></tr>
		<tr >
			<td colspan="3" style="text-align:center; white-space:nowrap;" >
			<br />
			<c:if test="${fn:length(addLibrariesToPlatformUnitSuccessMessage)>0}">				
				<span style="color:green;font-weight:bold"><c:out value="${addLibrariesToPlatformUnitSuccessMessage}" /></span>	<br /><br />			
			</c:if>
			<c:if test="${fn:length(addLibrariesToPlatformUnitErrorMessage)>0}">				
				<span style="color:red;font-weight:bold"><c:out value="${addLibrariesToPlatformUnitErrorMessage}" escapeXml="false" /></span><br /><br />
			</c:if>	
			<select style="font-size:x-small;" name="cellId" id="cellId" size="1" >
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
			<br /><input type='submit' value='<fmt:message key="listJobSamples.submit.label" />'/>&nbsp;<input type='reset' value='Reset'/>					
			</td>
		</tr>
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
	<c:set value="${fn:length(libraryList)}" var="sizeOfLibraryList"/>
	
	<tr>
	<%-- 
		<td class="DataTD"  style="text-align:center; white-space:nowrap;" rowspan="${submittedObjectLibraryRowspan.get(submittedObject)}"  style="text-align:center; white-space:nowrap;">
	--%>
		<td class="DataTD"  style="text-align:center; white-space:nowrap;" rowspan="${sizeOfLibraryList==0?1:sizeOfLibraryList}"  style="text-align:center; white-space:nowrap;">
			<c:choose>
				<c:when test="${submittedMacromoleculeList.contains(submittedObject)}">
					
					<label>Name:</label> <c:out value="${submittedObject.getName()}" /><br />
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
				</c:when>
				<c:otherwise>
					N/A
				</c:otherwise>
			</c:choose>
		</td>
		<c:choose>
			<c:when test="${fn:length(libraryList)==0}">
				<td class="DataTD" style="text-align:center; white-space:nowrap;">No libraries</td>
				<td class="DataTD" style="text-align:center; white-space:nowrap;">&nbsp;</td>
			</c:when>
			<c:otherwise>
				<c:forEach items="${libraryList}" var="library" varStatus="statusLibrary">
					<c:if test="${!statusLibrary.first}"><tr></c:if>	
						<td class="DataTD" style="text-align:center; white-space:nowrap;" rowspan="1">
							<label>Name:</label> <c:out value="${library.getName()}" /><br />
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
							
							<c:if test='${qcStatusMap.get(library) == "PASSED"}'>								
								<c:if test='${assignLibraryToPlatformUnitStatusMap.get(library) == true }'> 
				 					 <table class='data' style="margin: 5px 5px 5px 5px;">
											<tr class="FormData"><td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;"><fmt:message key="listJobSamples.addLibraryToPlatformUnit.label" /></td></tr>
											<tr>
												<td>				 					  
				 					  				<fmt:message key="listJobSamples.finalConcentrationPM.label" />: 
				 					 				<input type='text' name="libConcInCellPicoM_${library.getId()}"  size='3' maxlength='5'>
				 									<input type='hidden' name='libraryId' value='<c:out value="${library.getId()}" />'/>	
				 								</td>
				 							</tr>
				 						</table>
				 				</c:if>							
 	 						 </c:if> 	 						
						</td>								   				
		   				
		   				<td class="DataTD" style="text-align:center; white-space:nowrap;">
		   					<c:if test='${qcStatusMap.get(library) == "PASSED"}'>			 				
				 				<c:set value="${cellLibraryListMap.get(library)}" var="cellList"/>	
			   					<c:choose>
			   					<c:when test="${fn:length(cellList)==0}">
			   						<br />
			   						No Runs
			   					</c:when>
			   					<c:otherwise>
			   						<br />	
			   						<c:forEach items="${cellList}" var="cell" varStatus="cellLibrary">
										<c:set value="${cellRunMap.get(cell)}" var="run"/>
										<c:set value="${cellPUMap.get(cell)}" var="pu"/>
										<c:set value="${cellIndexMap.get(cell)}" var="laneIndex"/>
										
										<c:set value="${cellLibraryPMLoadedMap.get(cell)}" var="libraryPMLoadedMap" />
										
										<c:set value="${libraryPMLoadedMap.get(library)}" var="pMLoaded" />
																				
										<c:choose>
											<c:when test="${empty cellRunMap.get(cell)}">
												<sec:authorize access="hasRole('su') or hasRole('ft')">
													<a href="<wasp:relativeUrl value="/${showPlatformunitViewMap.get(pu)}" />"><c:out value="${pu.getName()}" /></a>
												</sec:authorize>
												<sec:authorize access="not hasRole('su') and not hasRole('ft')">
													<c:out value="${pu.getName()}" />
												</sec:authorize>
											</c:when>
											<c:otherwise>
												<sec:authorize access="hasRole('su') or hasRole('ft')">
													<a href="<wasp:relativeUrl value="/${showPlatformunitViewMap.get(pu)}" />"><c:out value="${run.getName()}" /></a>
												</sec:authorize>
												<sec:authorize access="not hasRole('su') and not hasRole('ft')">
													<c:out value="${run.getName()}" />
												</sec:authorize>
											</c:otherwise>
										</c:choose>																				
										(<c:out value="${pMLoaded}" /> pM; Lane <c:out value="${laneIndex}" />)											
										<br />
			   						</c:forEach>
			   					</c:otherwise>
			   					</c:choose>				
				 				
		   					</c:if>		   				    
		   				</td>	   				
					<c:if test="${!statusLibrary.last}"></tr></c:if>
				</c:forEach>
			</c:otherwise>
		</c:choose>
	</tr>
</c:forEach>
</table>
</form>
</sec:authorize>
<br />