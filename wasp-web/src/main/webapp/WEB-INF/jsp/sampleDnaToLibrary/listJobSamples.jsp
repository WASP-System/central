<%@ include file="/WEB-INF/jsp/taglib.jsp" %>


<br />
<title><fmt:message key="pageTitle.sampleDnaToLibrary/listJobSamples.label"/></title>
<h1><fmt:message key="sampleDnaToLibrary.listJobSamples.title_label" /></h1>
<c:import url="/WEB-INF/jsp/sampleDnaToLibrary/jobdetail_for_import.jsp" />
<br />
<hr />
<br />

<table class="data"> 
<tr class="FormData"><td class="label-centered">Initial Macromolecule</td><td class="label-centered">Libraries</td></tr>

<c:set var="idCounter" value="0" scope="page" />

<c:forEach items="${samplesSubmitted}" var="sample" varStatus="counter">

<c:set var="librariesPerSubmittedSample" value="${librariespersample[counter.index]}" scope="page" />
<c:set var="submittedSampleArrivalStatus" value="${received[counter.index]}" scope="page" />
<c:choose>
	<c:when test='${sample.typeSample.IName=="library"}'>
		<c:set var="submittedSampleType" value="library" scope="page" />
	</c:when>
	<c:when test='${sample.typeSample.IName=="dna" || sample.typeSample.IName=="rna"}'>
		<c:set var="submittedSampleType" value="macromolecule" scope="page" />
	</c:when>
</c:choose>	

<c:choose>
<c:when test='${submittedSampleType == "library"}'>
	<tr class="FormData">
		<td class="value-centered" ><br />N/A </td>
		<td class="value-centered">
			Name: <a href="<c:url value="/sampleDnaToLibrary/librarydetail_ro/${job.jobId}/${sample.sampleId}.do" />"><c:out value="${sample.name}" /></a><br />
		 	Molecule: <c:out value="${sample.typeSample.name}" /><br /> 
		 	<c:forEach items="${sample.sampleMeta}" var="sm">
        		<c:if test="${fn:substringAfter(sm.k, 'Biomolecule.') == 'species'}">
            		Species: <c:out value="${sm.v}"/><br />
            	</c:if> 
        	</c:forEach>
        	<c:forEach items="${sample.sampleMeta}" var="sm">
        		<c:if test="${fn:substringAfter(sm.k, 'Library.') == 'adaptor'}">
            		Adaptor: <c:out value="${adaptors.get(sm.v).adaptorset.name}"/><br />
            		Index <c:out value="${adaptors.get(sm.v).barcodenumber}"/>: <c:out value="${adaptors.get(sm.v).barcodesequence}"/><br />
            	</c:if> 
        	</c:forEach> 
		
			<c:choose>
				<c:when test='${submittedSampleArrivalStatus=="RECEIVED"}'>			
					<c:set var="idCounter" value="${idCounter + 1}" scope="page" />
					<div id="showButton_<c:out value="${idCounter}" />" >
				 		<input class="fm-button" type="button" value="Add Library To Flow Cell" onclick='toggleDisplayOfAddLibraryForm("show", <c:out value="${idCounter}" />)' />				
					</div>
					<div id="addLibraryForm_<c:out value="${idCounter}" />" style="display:none">
					  <table class='data'>
						<tr class="FormData"><td class="label-centered">Add Library To Flow Cell Lane</td></tr>
						<tr><td>
							<form  method='post' name='addLibToPU' action="<c:url value="/facility/platformunit/assignAdd2.do" />" onsubmit="return validate_submit(this);">
								<input type='hidden' name='jobid' value='<c:out value="${job.jobId}" />'/>
								<c:choose>
									<c:when test='${submittedSampleType == "library"}'>
										<input type='hidden' name='librarysampleid' value='<c:out value="${sample.sampleId}" />'/>
									</c:when>
									<c:when test='${submittedSampleType == "macromolecule"}'>
										<input type='hidden' name='librarysampleid' value='<c:out value="${lib.sampleId}" />'/>
									</c:when>
								</c:choose>	
				 				<br />
				 				<select class="FormElement ui-widget-content ui-corner-all" name="lanesampleid" size="1" onchange="validate(this)">
									<option value="0">--SELECT A FLOW CELL LANE--</option>
									<c:forEach items="${flowCells}" var="flowCell">
										<option value="0" />FlowCell: <c:out value="${flowCell.name}" /></option>
										<c:forEach items="${flowCell.sampleSource}" var="cell">
											<option style="color:red; font-weight: bold;" value="<c:out value="${cell.sampleViaSource.sampleId}" />" >&nbsp;&nbsp;&nbsp;Lane: <c:out value="${cell.sampleViaSource.name}" /></option>
											<c:forEach items="${cell.sampleViaSource.sampleSource}" var="library">
												<option value="0" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Library: <c:out value="${library.sampleViaSource.name}" />
												<c:forEach items="${library.sampleViaSource.sampleMeta}" var="sm">
        											<c:if test="${fn:substringAfter(sm.k, 'Library.') == 'adaptor'}">
            											&nbsp;[Index: <c:out value="${adaptors.get(sm.v).barcodenumber}"/>, <c:out value="${adaptors.get(sm.v).barcodesequence}"/>]
            										</c:if> </option>
		        								</c:forEach> 							
								
											</c:forEach> 
										</c:forEach> 
									</c:forEach>
								</select>
								<br />&nbsp;Final Concentration In Lane (pM): <input type='text' name='libConcInLanePicoM' size='3' maxlength='5'>
								<br />&nbsp;<input type='submit' value='Submit'/>&nbsp;<input class="fm-button" type="button" value="Cancel" onclick='toggleDisplayOfAddLibraryForm("cancel", <c:out value="${idCounter}" />)' />
							</form>
						</td></tr>
					  </table>
					</div>	
				 </c:when>
		 	 	<c:otherwise>
		 	 		Status: <c:out value="${submittedSampleArrivalStatus}" />
		 	 		<c:if test='${submittedSampleArrivalStatus=="NOT ARRIVED"}'>	
		 	 			<a href="<c:url value="/task/samplereceive/list.do" />">
		 	 				<c:out value=" (log sample)" />
		 	 			</a>
		 	 		</c:if>
		 	 	</c:otherwise>
			</c:choose>	
		</td>
	</tr>
</c:when>
<c:when test='${submittedSampleType == "macromolecule"}'>
	<tr class="FormData">
		<td rowspan = "${librariesPerSubmittedSample}" class="value-centered">
			Name: <a href="<c:url value="/sampleDnaToLibrary/sampledetail_ro/${job.jobId}/${sample.sampleId}.do" />"><c:out value="${sample.name}" /></a><br />
	  		Molecule: <c:out value="${sample.typeSample.name}" /><br /> 
	  		<c:forEach items="${sample.sampleMeta}" var="sm">
        		<c:if test="${fn:substringAfter(sm.k, 'Biomolecule.') == 'species'}">
            		Species: <c:out value="${sm.v}"/><br />
           		</c:if> 
        	</c:forEach> 
        		  
	  		<c:choose>
	  			<c:when test='${submittedSampleArrivalStatus=="RECEIVED"}'>
	  				<form method="GET" action="<c:url value="/sampleDnaToLibrary/createLibraryFromMacro.do" />">
	  					<input class="FormElement ui-widget-content ui-corner-all" type='hidden' name='jobId' value='<c:out value="${job.jobId}" />'/>
	  					<input class="FormElement ui-widget-content ui-corner-all" type='hidden' name='macromolSampleId' value='<c:out value="${sample.sampleId}" />'/>
	  					<select class="FormElement ui-widget-content ui-corner-all" name="adaptorsetId" size="1" onchange="if(this.options[selectedIndex].value != 0){this.parentNode.submit();}">
							<option value="0">--ADAPTORS FOR NEW LIBRARY--
							<c:forEach items="${adaptorsets}" var="adaptorset">
								<option value="<c:out value="${adaptorset.adaptorsetId}" />" ><c:out value="${adaptorset.name}" /> 
							</c:forEach>
						</select>
					</form>
	 	 		</c:when>
	 	 		<c:otherwise>
	 	 			Status: <c:out value="${submittedSampleArrivalStatus}" />
	 	 			<c:if test='${submittedSampleArrivalStatus=="NOT ARRIVED"}'>	
		 	 			<a href="<c:url value="/task/samplereceive/list.do" />">
		 	 				<c:out value=" (log sample)" />
		 	 			</a>
		 	 		</c:if>
	 	 		</c:otherwise>
	  		</c:choose>					  
		</td>
		<c:choose>
			<c:when test="${sample.sampleSourceViaSourceSampleId.size()==0}">
				<td>&nbsp;</td>
			</c:when>
			<c:otherwise>
			<c:set var="i" value="0" scope="page" /> 
			<c:forEach items="${sample.sampleSourceViaSourceSampleId}" var="samplesource">
				<c:set var="lib" value="${samplesource.sample}"/> 
				<c:if test="${i > 0}">
					<tr>
				</c:if>
				<td class="value-centered">
						
				Name: <a href="<c:url value="/sampleDnaToLibrary/librarydetail_ro/${job.jobId}/${lib.sampleId}.do" />"><c:out value="${lib.name}" /></a><br />
				Molecule: <c:out value="${lib.typeSample.name}" /><br />
				<c:forEach items="${lib.sampleMeta}" var="sm">
        			<c:if test="${fn:substringAfter(sm.k, 'Biomolecule.') == 'species'}">
            			Species: <c:out value="${sm.v}"/><br />
            		</c:if> 
        		</c:forEach>
        		<c:forEach items="${lib.sampleMeta}" var="sm">
        			<c:if test="${fn:substringAfter(sm.k, 'Library.') == 'adaptor'}">
            			Adaptor: <c:out value="${adaptors.get(sm.v).adaptorset.name}"/><br />
            			Index <c:out value="${adaptors.get(sm.v).barcodenumber}"/>: <c:out value="${adaptors.get(sm.v).barcodesequence}"/><br />
            		</c:if> 
		        </c:forEach> 
		        
 				<c:set var="idCounter" value="${idCounter + 1}" scope="page" />
				<div id="showButton_<c:out value="${idCounter}" />" >
				 	<input class="fm-button" type="button" value="Add Library To Flow Cell" onclick='toggleDisplayOfAddLibraryForm("show", <c:out value="${idCounter}" />)' />				
				</div>
				<div id="addLibraryForm_<c:out value="${idCounter}" />" style="display:none">
					<table class='data'>
						<tr class="FormData"><td class="label-centered">Add Library To Flow Cell Lane</td></tr>
						<tr><td>
							<form  method='post' name='addLibToPU' action="<c:url value="/facility/platformunit/assignAdd2.do" />" onsubmit="return validate_submit(this);">
								<input type='hidden' name='jobid' value='<c:out value="${job.jobId}" />'/>
				 				<c:choose>
									<c:when test='${submittedSampleType == "library"}'>
										<input type='hidden' name='librarysampleid' value='<c:out value="${sample.sampleId}" />'/>
									</c:when>
									<c:when test='${submittedSampleType == "macromolecule"}'>
										<input type='hidden' name='librarysampleid' value='<c:out value="${lib.sampleId}" />'/>
									</c:when>
								</c:choose>	
		
				 				<br />
				 				<select class="FormElement ui-widget-content ui-corner-all" name="lanesampleid" size="1" onchange="validate(this)">
									<option value="0">--SELECT A FLOW CELL LANE--</option>
									<c:forEach items="${flowCells}" var="flowCell">
										<option value="0" />FlowCell: <c:out value="${flowCell.name}" /></option>
										<c:forEach items="${flowCell.sampleSource}" var="cell">
											<option style="color:red; font-weight: bold;" value="<c:out value="${cell.sampleViaSource.sampleId}" />" >&nbsp;&nbsp;&nbsp;Lane: <c:out value="${cell.sampleViaSource.name}" /></option>
											<c:forEach items="${cell.sampleViaSource.sampleSource}" var="library">
												<option value="0" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Library: <c:out value="${library.sampleViaSource.name}" />
												<c:forEach items="${library.sampleViaSource.sampleMeta}" var="sm">
        											<c:if test="${fn:substringAfter(sm.k, 'Library.') == 'adaptor'}">
            											&nbsp;[Index: <c:out value="${adaptors.get(sm.v).barcodenumber}"/>, <c:out value="${adaptors.get(sm.v).barcodesequence}"/>]
            										</c:if> </option>
		        								</c:forEach> 							
											</c:forEach> 
										</c:forEach> 
									</c:forEach>
								</select>
								<br />&nbsp;Final Concentration In Lane (pM): <input type='text' name='libConcInLanePicoM' size='3' maxlength='5'>
								<br />&nbsp;<input type='submit' value='Submit'/>&nbsp;<input class="fm-button" type="button" value="Cancel" onclick='toggleDisplayOfAddLibraryForm("cancel", <c:out value="${idCounter}" />)' />
							</form>
						</td></tr>
					</table>
				</div>	
				</td>
				<c:if test="${i > 0}">
				<!--  	<c:out value="</tr>" /> -->
				</tr>
				</c:if>	
				<c:set var="i" value="${i + 1}" scope="page" />
			</c:forEach>
			</c:otherwise>
			</c:choose>
	</tr>	
</c:when>
</c:choose>
</c:forEach>
</table>