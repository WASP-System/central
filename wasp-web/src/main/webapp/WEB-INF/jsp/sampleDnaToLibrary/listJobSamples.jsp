<%@ include file="/WEB-INF/jsp/taglib.jsp" %>


<br />
<title><fmt:message key="pageTitle.sampleDnaToLibrary/listJobSamples.label"/></title>
<h1><fmt:message key="sampleDnaToLibrary.listJobSamples.title_label" /></h1>
<c:import url="/WEB-INF/jsp/sampleDnaToLibrary/jobdetail_for_import.jsp" />
<br />
<%-- 
<div id="accordion">         
 	<h4><a href="#">User-Requested Coverage</a></h4>
    <div>
    <table class="data">
		<tr class="FormData"><td colspan="2" class="label-centered">User-Requested Coverage</td></tr>
		<c:forEach items="${jobCellList}" var="jobCell">
			 <tr class="FormData">
				<td class="label-centered" >Requested Lane <c:out value="${jobCell.cellindex}" /></td>
				<td class="value-centered" >   
				<c:forEach items="${jobCell.sampleCell}" var="sampleCell">
					<c:out value="${sampleCell.sample.name}" /><br />
				</c:forEach>
				</td>
 			</tr>
		</c:forEach>
	</table>
	<table class="data">
<tr class="FormData">
	<td class="label-centered" style="background-color:#FAF2D6">&nbsp;</td><c:forEach var="i" begin="0" end="${totalNumberCellsRequested - 1}" ><td class="label-centered" style="background-color:#FAF2D6">Lane <c:out value="${i + 1}" /></td></c:forEach>
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
</div>
--%>
<div>
<input  class="fm-button" type="button" id="requested_coverage_show_hide_button" value="Show User-Requested Coverage"  />
</div>
<div id="user_requested_coverage_data" style="display:none">
<%-- 
<table class="data">
<tr class="FormData"><td colspan="2" class="label-centered">User-Requested Coverage</td></tr>
<c:forEach items="${jobCellList}" var="jobCell">
 <tr class="FormData">
	<td class="label-centered" >Requested Lane <c:out value="${jobCell.cellindex}" /></td>
	<td class="value-centered" >   
		<c:forEach items="${jobCell.sampleCell}" var="sampleCell">
			<c:out value="${sampleCell.sample.name}" /><br />
		</c:forEach>
	</td>
 </tr>
</c:forEach>
</table>
--%>
<table class="data">
<tr class="FormData">
	<td class="label-centered" style="background-color:#FAF2D6">&nbsp;</td><c:forEach var="i" begin="0" end="${totalNumberCellsRequested - 1}" ><td class="label-centered" style="background-color:#FAF2D6">Lane <c:out value="${i + 1}" /></td></c:forEach>
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

<%-- 
<table class="data"> 
<tr class="FormData"><td class="label-centered" style="background-color:#FAF2D6">Initial Macromolecule</td><td class="label-centered" style="background-color:#FAF2D6">Libraries</td><td class="label-centered" style="background-color:#FAF2D6">FlowCells/Runs</td></tr>

<c:set var="idCounter" value="0" scope="page" />

<c:forEach items="${samplesSubmitted}" var="sampleSubmitted" varStatus="counter">

<c:set var="librariesPerSubmittedSample" value="${librariespersample[counter.index]}" scope="page" />
<c:set var="submittedSampleArrivalStatus" value="${received[counter.index]}" scope="page" />
<c:choose>
	<c:when test='${sampleSubmitted.sampleType.IName=="library"}'>
		<c:set var="submittedSampleType" value="library" scope="page" />
	</c:when>
	<c:when test='${sampleSubmitted.sampleType.IName=="dna" || sampleSubmitted.sampleType.IName=="rna"}'>
		<c:set var="submittedSampleType" value="macromolecule" scope="page" />
	</c:when>
</c:choose>	

<c:choose>
<c:when test='${submittedSampleType == "library"}'>
	<tr class="FormData" style="border: 2px solid #000;">
		<td class="value-centered" ><br />N/A </td>
		<td class="value-centered">
			Name: <a href="<c:url value="/sampleDnaToLibrary/librarydetail_ro/${job.jobId}/${sampleSubmitted.sampleId}.do" />"><c:out value="${sampleSubmitted.name}" /></a><br />
		 	Molecule: <c:out value="${sampleSubmitted.sampleType.name}" /><br /> 
		 	<c:forEach items="${sampleSubmitted.sampleMeta}" var="sm">
        		<c:if test="${fn:substringAfter(sm.k, 'Biomolecule.') == 'species'}">
            		Species: <c:out value="${sm.v}"/><br />
            	</c:if> 
        	</c:forEach>
        	<c:forEach items="${sampleSubmitted.sampleMeta}" var="sm">
        		<c:if test="${fn:substringAfter(sm.k, 'Library.') == 'adaptor'}">
            		Adaptor: <c:out value="${adaptors.get(sm.v).adaptorset.name}"/><br />
            		Index <c:out value="${adaptors.get(sm.v).barcodenumber}"/>: <c:out value="${adaptors.get(sm.v).barcodesequence}"/><br />
            	</c:if> 
        	</c:forEach> 
		
			<c:choose>
				<c:when test='${submittedSampleArrivalStatus=="RECEIVED"}'>	
				   Arrival Status: <c:out value="${submittedSampleArrivalStatus}" /><sec:authorize access="hasRole('su') or hasRole('ft')">&nbsp;<a href="<c:url value="/task/updatesamplereceive/${job.jobId}.do" />">[update]</a></sec:authorize>
					<c:set var="idCounter" value="${idCounter + 1}" scope="page" />
					<sec:authorize access="hasRole('su') or hasRole('ft')">
					<div id="showButton_<c:out value="${idCounter}" />" >
				 		<input class="fm-button" type="button" value="Add Library To Flow Cell" onclick='toggleDisplayOfAddLibraryForm("show", <c:out value="${idCounter}" />)' />				
					</div>
					</sec:authorize>
					<div id="addLibraryForm_<c:out value="${idCounter}" />" style="display:none">
					  <table class='data'>
						<tr class="FormData"><td class="label-centered">Add Library To Flow Cell Lane</td></tr>
						<tr><td>
							<form  method='post' name='addLibToPU' action="<c:url value="/facility/platformunit/assignAdd2.do" />" onsubmit="return validate_submit(this);">
								<input type='hidden' name='jobid' value='<c:out value="${job.jobId}" />'/>
								<c:choose>
									<c:when test='${submittedSampleType == "library"}'>
										<input type='hidden' name='librarysampleid' value='<c:out value="${sampleSubmitted.sampleId}" />'/>
									</c:when>
									<c:when test='${submittedSampleType == "macromolecule"}'>
										<input type='hidden' name='librarysampleid' value='<c:out value="${lib.sampleId}" />'/>
									</c:when>
								</c:choose>	
				 				<br />
				 				<select class="FormElement ui-widget-content ui-corner-all" name="lanesampleid" id="lanesampleid_<c:out value="${idCounter}" />" size="1" onchange="validate(this)">
									<option value="0">--SELECT A FLOW CELL LANE--</option>
									<c:forEach items="${flowCells}" var="flowCell">
										<option value="0">FlowCell: <c:out value="${flowCell.name}" /> [<c:out value="${flowCell.sampleSubtype.name}" />]</option>
										<c:forEach items="${flowCell.sampleSource}" var="cell">
											<option style="color:red; font-weight: bold;" value="<c:out value="${cell.sampleViaSource.sampleId}" />" >&nbsp;&nbsp;&nbsp;Lane: <c:out value="${cell.sampleViaSource.name}" /></option>
											<c:forEach items="${cell.sampleViaSource.sampleSource}" var="library">
											  <c:if test="${library.sampleViaSource.sampleSubtype.getIName() == 'controlLibrarySample'}">
												<option value="0">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Library (Control): <c:out value="${library.sampleViaSource.name}" />
												<c:forEach items="${library.sampleViaSource.sampleMeta}" var="sm">
        											<c:if test="${fn:substringAfter(sm.k, 'Library.') == 'adaptor'}">
        											    &nbsp;-&nbsp;<c:out value="${adaptors.get(sm.v).adaptorset.name}"/>&nbsp;[Index <c:out value="${adaptors.get(sm.v).barcodenumber}"/>&nbsp;(<c:out value="${adaptors.get(sm.v).barcodesequence}"/>)]
            										</c:if> 
		        								</c:forEach> </option>	
		        							  </c:if>							
											</c:forEach> 
											<c:forEach items="${cell.sampleViaSource.sampleSource}" var="library">
											  <c:if test="${library.sampleViaSource.sampleSubtype.getIName() != 'controlLibrarySample'}">
												<option value="0">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Library: <c:out value="${library.sampleViaSource.name}" />
												<c:forEach items="${library.sampleViaSource.sampleMeta}" var="sm">
        											<c:if test="${fn:substringAfter(sm.k, 'Library.') == 'adaptor'}">
        											    &nbsp;-&nbsp;<c:out value="${adaptors.get(sm.v).adaptorset.name}"/>&nbsp;[Index <c:out value="${adaptors.get(sm.v).barcodenumber}"/>&nbsp;(<c:out value="${adaptors.get(sm.v).barcodesequence}"/>)]
            										</c:if> 
		        								</c:forEach> </option>	
		        							  </c:if>								
											</c:forEach> 
										</c:forEach> 
									</c:forEach>
								</select>
								<br />&nbsp;Final Concentration In Lane (pM): <input type='text' name='libConcInLanePicoM' id="libConcInLanePicoM_<c:out value="${idCounter}" />" size='3' maxlength='5'>
								<br />&nbsp;<input type='submit' value='Submit'/>&nbsp;<input class="fm-button" type="button" value="Cancel" onclick='toggleDisplayOfAddLibraryForm("cancel", <c:out value="${idCounter}" />)' />
							</form>
						</td></tr>
					  </table>
					</div>	
				 </c:when>
		 	 	<c:otherwise>
		 	 		Arrival Status: <c:out value="${submittedSampleArrivalStatus}" />
		 	 		<sec:authorize access="hasRole('su') or hasRole('ft')">
		 	 		<c:if test='${submittedSampleArrivalStatus=="NOT ARRIVED"}'>	
		 	 			&nbsp;<a href="<c:url value="/task/samplereceive/list.do" />"><c:out value="[log sample]" /></a>
		 	 		</c:if>
		 	 		<c:if test='${submittedSampleArrivalStatus=="WITHDRAWN"}'>	
		 	 			&nbsp;<a href="<c:url value="/task/updatesamplereceive/${job.jobId}.do" />">[update]</a>
		 	 		</c:if>
		 	 		</sec:authorize>
		 	 	</c:otherwise>
			</c:choose>	
		</td>
		
		
		<td class="value-centered">
			<c:choose>
				<c:when test="${sampleSubmitted.sampleSourceViaSourceSampleId.size()==0}">
					<br />Not Yet Assigned<br />
				</c:when>
				<c:otherwise>					
					<c:forEach items="${sampleSubmitted.sampleSourceViaSourceSampleId}" var="ss">
						<c:forEach items="${ss.sample.sampleSourceViaSourceSampleId}" var="ss2">							
							<c:choose>
								<c:when test="${ss2.sample.run.size()==0}">
									
									<form  name='removeLib' method='post' action="<c:url value="/facility/platformunit/assignRemove.do" />" onsubmit='return confirm("Remove library from this flowcell?");'>
									<input type='hidden' name='jobId' value='<c:out value="${job.jobId}" />'/>
									<input type='hidden' name='samplesourceid' value='<c:out value="${ss.sampleSourceId}" />'/>
									<sec:authorize ifAnyGranted="su, ft"><a href="<c:url value="/facility/platformunit/showPlatformUnit/${ss2.sample.sampleId}.do" />"><c:out value="${ss2.sample.name}"/></a> (Lane <c:out value="${ss2.multiplexindex}"/>) </sec:authorize>
									<sec:authorize ifNotGranted="su, ft"><c:out value="${ss2.sample.name}"/> (Lane <c:out value="${ss2.multiplexindex}"/>) </sec:authorize>
									&nbsp;
									<sec:authorize access="hasRole('su') or hasRole('ft')"><input type='submit' value='Remove'/></sec:authorize>
									</form>
									
									
								</c:when>
								<c:otherwise>
									<c:forEach items="${ss2.sample.run}" var="aRun">
										 <sec:authorize ifAnyGranted="su, ft"><a href="<c:url value="/facility/platformunit/showPlatformUnit/${ss2.sample.sampleId}.do" />"><c:out value="${ss2.sample.name}"/> (Lane <c:out value="${ss2.multiplexindex}"/>) ---&gt; <c:out value="${aRun.name}"/></a><br/></sec:authorize>
										 <sec:authorize ifNotGranted="su, ft"><c:out value="${ss2.sample.name}"/> (Lane <c:out value="${ss2.multiplexindex}"/>) ---&gt; <c:out value="${aRun.name}"/><br/></sec:authorize>
									</c:forEach>
								</c:otherwise>
							</c:choose>							
						</c:forEach>
					</c:forEach>
				</c:otherwise>
			</c:choose>
		</td>
		
		
	</tr>
</c:when>
<c:when test='${submittedSampleType == "macromolecule"}'>
	<tr class="FormData" style="border: 2px solid #000;">
		<c:if test="${librariesPerSubmittedSample == 0}">  <!-- needed since rowspan cannot be zero; see three rows down -->
			<c:set var="librariesPerSubmittedSample" value="1" scope="page" />
		</c:if>
		<td rowspan = "${librariesPerSubmittedSample}" class="value-centered">
			Name: <a href="<c:url value="/sampleDnaToLibrary/sampledetail_ro/${job.jobId}/${sampleSubmitted.sampleId}.do" />"><c:out value="${sampleSubmitted.name}" /></a><br />
	  		Molecule: <c:out value="${sampleSubmitted.sampleType.name}" /><br /> 
	  		<c:forEach items="${sampleSubmitted.sampleMeta}" var="sm">
        		<c:if test="${fn:substringAfter(sm.k, 'Biomolecule.') == 'species'}">
            		Species: <c:out value="${sm.v}"/><br />
           		</c:if> 
        	</c:forEach> 
        		  
	  		<c:choose>
	  			<c:when test='${submittedSampleArrivalStatus=="RECEIVED"}'>
	  			   Arrival Status: <c:out value="${submittedSampleArrivalStatus}" /><sec:authorize access="hasRole('su') or hasRole('ft')">&nbsp;<a href="<c:url value="/task/updatesamplereceive/${job.jobId}.do" />">[update]</a></sec:authorize>
	  			   <sec:authorize access="hasRole('su') or hasRole('ft')">
	  				<form method="GET" action="<c:url value="/sampleDnaToLibrary/createLibraryFromMacro.do" />">
	  					<input type='hidden' name='jobId' value='<c:out value="${job.jobId}" />'/>
	  					<input type='hidden' name='macromolSampleId' value='<c:out value="${sampleSubmitted.sampleId}" />'/>
	  					<select class="FormElement ui-widget-content ui-corner-all" name="adaptorsetId" size="1" onchange="if(this.options[selectedIndex].value != 0){this.parentNode.submit();}">
							<option value="0">--ADAPTORS FOR NEW LIBRARY--
							<c:forEach items="${adaptorsets}" var="adaptorset">
								<option value="<c:out value="${adaptorset.adaptorsetId}" />" ><c:out value="${adaptorset.name}" /> 
							</c:forEach>
						</select>
					</form>
					</sec:authorize>
	 	 		</c:when>
	 	 		<c:otherwise>
	 	 			Arrival Status: <c:out value="${submittedSampleArrivalStatus}" />
	 	 			<sec:authorize access="hasRole('su') or hasRole('ft')">
	 	 			<c:if test='${submittedSampleArrivalStatus=="NOT ARRIVED"}'>	
		 	 			&nbsp;<a href="<c:url value="/task/samplereceive/list.do" />"><c:out value="[log sample]" /></a>
		 	 		</c:if>
		 	 		<c:if test='${submittedSampleArrivalStatus=="WITHDRAWN"}'>	
		 	 			&nbsp;<a href="<c:url value="/task/updatesamplereceive/${job.jobId}.do" />">[update]</a>
		 	 		</c:if>
		 	 		</sec:authorize>
	 	 		</c:otherwise>
	  		</c:choose>					  
		</td>
		<c:choose>
			<c:when test="${sampleSubmitted.sampleSourceViaSourceSampleId.size()==0}">
				<td class="value-centered">No Libraries Created</td>
				<td class="value-centered">No Libraries Created</td>
			</c:when>
			<c:otherwise>
			<c:set var="i" value="0" scope="page" /> 
			<c:forEach items="${sampleSubmitted.sampleSourceViaSourceSampleId}" var="samplesource">
				<c:set var="lib" value="${samplesource.sample}"/> 
				<c:if test="${i > 0}">
					<tr style="border: 2px solid #000;">
				</c:if>
				<td class="value-centered">
						
				Name: <a href="<c:url value="/sampleDnaToLibrary/librarydetail_ro/${job.jobId}/${lib.sampleId}.do" />"><c:out value="${lib.name}" /></a><br />
				Molecule: <c:out value="${lib.sampleType.name}" /><br />
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
 				<sec:authorize access="hasRole('su') or hasRole('ft')">
				<div id="showButton_<c:out value="${idCounter}" />" >
				 	<input class="fm-button" type="button" value="Add Library To Flow Cell" onclick='toggleDisplayOfAddLibraryForm("show", <c:out value="${idCounter}" />)' />				
				</div>
				</sec:authorize>
				<div id="addLibraryForm_<c:out value="${idCounter}" />" style="display:none">
					<table class='data'>
						<tr class="FormData"><td class="label-centered">Add Library To Flow Cell Lane</td></tr>
						<tr><td>
							<form  method='post' name='addLibToPU' action="<c:url value="/facility/platformunit/assignAdd2.do" />" onsubmit="return validate_submit(this);">
								<input type='hidden' name='jobid' value='<c:out value="${job.jobId}" />'/>
				 				<c:choose>
									<c:when test='${submittedSampleType == "library"}'>
										<input type='hidden' name='librarysampleid' value='<c:out value="${sampleSubmitted.sampleId}" />'/>
									</c:when>
									<c:when test='${submittedSampleType == "macromolecule"}'>
										<input type='hidden' name='librarysampleid' value='<c:out value="${lib.sampleId}" />'/>
									</c:when>
								</c:choose>	
		
				 				<br />
				 				<select class="FormElement ui-widget-content ui-corner-all" name="lanesampleid" id="lanesampleid_<c:out value="${idCounter}" />" size="1" onchange="validate(this)">
									<option value="0">--SELECT A FLOW CELL LANE--</option>
									<c:forEach items="${flowCells}" var="flowCell">
										<option value="0">FlowCell: <c:out value="${flowCell.name}" /> [<c:out value="${flowCell.sampleSubtype.name}" />]</option>
										<c:forEach items="${flowCell.sampleSource}" var="cell">
											<option style="color:red; font-weight: bold;" value="<c:out value="${cell.sampleViaSource.sampleId}" />">&nbsp;&nbsp;&nbsp;Lane: <c:out value="${cell.sampleViaSource.name}" /></option>
											<c:forEach items="${cell.sampleViaSource.sampleSource}" var="library">
											  <c:if test="${library.sampleViaSource.sampleSubtype.getIName() == 'controlLibrarySample'}">
												<option value="0">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Library (Control): <c:out value="${library.sampleViaSource.name}" />
												<c:forEach items="${library.sampleViaSource.sampleMeta}" var="sm">
        											<c:if test="${fn:substringAfter(sm.k, 'Library.') == 'adaptor'}">
        											    &nbsp;-&nbsp;<c:out value="${adaptors.get(sm.v).adaptorset.name}"/>&nbsp;[Index <c:out value="${adaptors.get(sm.v).barcodenumber}"/>&nbsp;(<c:out value="${adaptors.get(sm.v).barcodesequence}"/>)]
            										</c:if>  
		        								</c:forEach> </option>	
		        							  </c:if>					
											</c:forEach> 
											<c:forEach items="${cell.sampleViaSource.sampleSource}" var="library">
											  <c:if test="${library.sampleViaSource.sampleSubtype.getIName() != 'controlLibrarySample'}">
												<option value="0">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Library: <c:out value="${library.sampleViaSource.name}" />
												<c:forEach items="${library.sampleViaSource.sampleMeta}" var="sm">
        											<c:if test="${fn:substringAfter(sm.k, 'Library.') == 'adaptor'}">
        											    &nbsp;-&nbsp;<c:out value="${adaptors.get(sm.v).adaptorset.name}"/>&nbsp;[Index <c:out value="${adaptors.get(sm.v).barcodenumber}"/>&nbsp;(<c:out value="${adaptors.get(sm.v).barcodesequence}"/>)]
            										</c:if>  
		        								</c:forEach> </option>	
		        							  </c:if>					
											</c:forEach>
										</c:forEach> 
									</c:forEach>
								</select>
								<br />&nbsp;Final Concentration In Lane (pM): <input type='text' name='libConcInLanePicoM' id="libConcInLanePicoM_<c:out value="${idCounter}" />" size='3' maxlength='5'>
								<br />&nbsp;<input type='submit' value='Submit'/>&nbsp;<input class="fm-button" type="button" value="Cancel" onclick='toggleDisplayOfAddLibraryForm("cancel", <c:out value="${idCounter}" />)' />
							</form>
						</td></tr>
					</table>
				</div>	
				</td>
				
				
				
				<td class="value-centered">
					<c:choose>
						<c:when test="${lib.sampleSourceViaSourceSampleId.size()==0}">
							<br />Not Yet Assigned<br />
						</c:when>
						<c:otherwise>				
							<c:forEach items="${lib.sampleSourceViaSourceSampleId}" var="ss">
								<c:forEach items="${ss.sample.sampleSourceViaSourceSampleId}" var="ss2">
									<c:choose>
										<c:when test="${ss2.sample.run.size()==0}">											
											
											<form  name='removeLib' method='post' action="<c:url value="/facility/platformunit/assignRemove.do" />" onsubmit='return confirm("Remove library from this flowcell?");'>
												<input type='hidden' name='jobId' value='<c:out value="${job.jobId}" />'/>
												<input type='hidden' name='samplesourceid' value='<c:out value="${ss.sampleSourceId}" />'/>
												<sec:authorize ifAnyGranted="su, ft"><a href="<c:url value="/facility/platformunit/showPlatformUnit/${ss2.sample.sampleId}.do" />"><c:out value="${ss2.sample.name}"/></a> (Lane <c:out value="${ss2.multiplexindex}"/>) </sec:authorize>
												<sec:authorize ifNotGranted="su, ft"><c:out value="${ss2.sample.name}"/> (Lane <c:out value="${ss2.multiplexindex}"/>) </sec:authorize>
												&nbsp;
												<sec:authorize access="hasRole('su') or hasRole('ft')"><input type='submit' value='Remove'/></sec:authorize>
											</form>											
											
										</c:when>
										<c:otherwise>
											<c:forEach items="${ss2.sample.run}" var="aRun">
										 		<sec:authorize ifAnyGranted="su, ft"><a href="<c:url value="/facility/platformunit/showPlatformUnit/${ss2.sample.sampleId}.do" />"><c:out value="${ss2.sample.name}"/> (Lane <c:out value="${ss2.multiplexindex}"/>) ---&gt; <c:out value="${aRun.name}"/></a><br/></sec:authorize>
												<sec:authorize ifNotGranted="su, ft"><c:out value="${ss2.sample.name}"/> (Lane <c:out value="${ss2.multiplexindex}"/>) ---&gt; <c:out value="${aRun.name}"/><br/></sec:authorize>
											</c:forEach>
										</c:otherwise>
									</c:choose>	
								</c:forEach>
							</c:forEach>
						</c:otherwise>
					</c:choose>
				</td>
				
				
				
				<c:if test="${i > 0}">
				 
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
--%>





<br />
<c:set var="idCounter" value="1000" scope="page" />
<table class="data"> 

<c:if test="${macromoleculeSubmittedSamplesList.size() > 0}">
	<tr class="FormData">
		<td class="label-centered" style="background-color:#FAF2D6">Initial Macromolecule</td><td class="label-centered" style="background-color:#FAF2D6">Libraries</td><td class="label-centered" style="background-color:#FAF2D6">FlowCells/Runs</td>
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
				Name: <a href="<c:url value="/sampleDnaToLibrary/sampledetail_ro/${job.jobId}/${userSubmittedMacromolecule.getSampleId()}.do" />"><c:out value="${userSubmittedMacromolecule.getName()}"/></a><br />
				Type: <c:out value="${userSubmittedMacromolecule.getSampleType().getName()}"/><br />
				Species: <c:out value="${speciesMap.get(userSubmittedMacromolecule)}"/><br />
				Arrival Status: <c:out value="${receivedStatusMap.get(userSubmittedMacromolecule)}"/><sec:authorize access="hasRole('su') or hasRole('ft')">&nbsp;<a href="<c:url value="/task/updatesamplereceive/${job.jobId}.do" />">[update]</a></sec:authorize><br />
				
				<sec:authorize access="hasRole('su') or hasRole('ft')">
				<c:if test='${receivedStatusMap.get(userSubmittedMacromolecule)=="RECEIVED"}'>
	  				<form method="GET" action="<c:url value="/sampleDnaToLibrary/createLibraryFromMacro.do" />">
	  					<input type='hidden' name='jobId' value='<c:out value="${job.jobId}" />'/>
	  					<input type='hidden' name='macromolSampleId' value='<c:out value="${userSubmittedMacromolecule.getSampleId()}" />'/>
	  					<select class="FormElement ui-widget-content ui-corner-all" name="adaptorsetId" size="1" onchange="if(this.options[selectedIndex].value != 0){this.parentNode.submit();}">
							<option value="0">--ADAPTORS FOR NEW LIBRARY--
							<c:forEach items="${adaptorsets}" var="adaptorset">
								<option value="<c:out value="${adaptorset.adaptorsetId}" />" ><c:out value="${adaptorset.name}" /> 
							</c:forEach>
						</select>
					</form>
	 	 		</c:if>
				</sec:authorize>				
			</td>
			<c:choose>
				<c:when test="${numberLibrariesForThisMacromolecule == 0}">
					<td>No Libraries Created</td>	
					<td>No Libraries Created</td>
				</c:when>
				<c:when test="${numberLibrariesForThisMacromolecule > 0}">
					<c:set var="rowCounter" value="0" scope="page" />
					<c:forEach items="${facilityLibrariesForThisMacromoleculeList}" var="facilityLibraryForThisMacromolecule">	
 					<c:if test="${rowCounter > 0}">
					  		<tr>
					 </c:if>	
					<td>
							Name: <a href="<c:url value="/sampleDnaToLibrary/librarydetail_ro/${job.jobId}/${facilityLibraryForThisMacromolecule.getSampleId()}.do" />"><c:out value="${facilityLibraryForThisMacromolecule.getName()}"/></a><br />
							Type: <c:out value="${facilityLibraryForThisMacromolecule.getSampleType().getName()}"/><br />
							<c:set var="adaptor" value="${libraryAdaptorMap.get(facilityLibraryForThisMacromolecule)}" scope="page" />
							Adaptor: <c:out value="${adaptor.getAdaptorset().getName()}"/><br />
							Index <c:out value="${adaptor.getBarcodenumber()}"/> [<c:out value="${adaptor.getBarcodesequence()}"/>]<br />
														
							<c:set var="idCounter" value="${idCounter + 1}" scope="page" />
 							<sec:authorize access="hasRole('su') or hasRole('ft')">
							<div id="showButton_<c:out value="${idCounter}" />" >
				 				<input class="fm-button" type="button" value="Add Library To Flow Cell" onclick='toggleDisplayOfAddLibraryForm("show", <c:out value="${idCounter}" />)' />				
							</div>
							</sec:authorize>
							<div id="addLibraryForm_<c:out value="${idCounter}" />" style="display:none">
							<table class='data'>
								<tr class="FormData"><td class="label-centered">Add Library To Flow Cell Lane</td></tr>
							<tr><td>
							<form  method='post' name='addLibToPU' action="<c:url value="/facility/platformunit/assignAdd2.do" />" onsubmit="return validate_submit(this);">
								<input type='hidden' name='jobid' value='<c:out value="${job.jobId}" />'/>
				 				<input type='hidden' name='librarysampleid' value='<c:out value="${facilityLibraryForThisMacromolecule.getSampleId()}" />'/>
								<br />
				 				<select class="FormElement ui-widget-content ui-corner-all" name="lanesampleid" id="lanesampleid_<c:out value="${idCounter}" />" size="1" onchange="validate(this)">
								<option value="0">--SELECT A FLOW CELL LANE--</option>
								<c:forEach items="${availableAndCompatibleFlowCells}" var="flowCell">
								<option value="0">FlowCell: <c:out value="${flowCell.getName()}" /> [<c:out value="${flowCell.getSampleSubtype().getName()}" />]</option>
								<c:set var="sampleSourceList" value="${flowCell.getSampleSource()}" scope="page" />
								<c:forEach items="${sampleSourceList}" var="sampleSource">
									<c:set var="cell" value="${sampleSource.getSampleViaSource()}" scope="page" />
									<option style="color:red; font-weight: bold;" value="<c:out value="${cell.getSampleId()}" />">&nbsp;&nbsp;&nbsp;Lane: <c:out value="${cell.getName()}" /></option>
									<c:set var="sampleSourceList2" value="${cell.getSampleSource()}" scope="page" />
									<c:forEach items="${sampleSourceList2}" var="sampleSource2">
										<c:set var="library" value="${sampleSource2.getSampleViaSource()}" scope="page" />
									  	<c:if test="${library.getSampleSubtype().getIName() == 'controlLibrarySample'}">
											<option value="0">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Library (Control): <c:out value="${library.getName()}" />
											<c:set var="adaptor" value="${libraryAdaptorMap.get(library)}" scope="page" />
											&nbsp;-&nbsp;<c:out value="${adaptor.getAdaptorset().getName()}"/>&nbsp;[Index <c:out value="${adaptor.getBarcodenumber()}"/>&nbsp;(<c:out value="${adaptor.getBarcodesequence()}"/>)]
			 								</option>	
		        					  </c:if>					
									</c:forEach> 
									<c:forEach items="${sampleSourceList2}" var="sampleSource2">
										<c:set var="library" value="${sampleSource2.getSampleViaSource()}" scope="page" />
									  	<c:if test="${library.getSampleSubtype().getIName() != 'controlLibrarySample'}">
											<option value="0">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Library: <c:out value="${library.getName()}" />
											<c:set var="adaptor" value="${libraryAdaptorMap.get(library)}" scope="page" />
											&nbsp;-&nbsp;<c:out value="${adaptor.getAdaptorset().getName()}"/>&nbsp;[Index <c:out value="${adaptor.getBarcodenumber()}"/>&nbsp;(<c:out value="${adaptor.getBarcodesequence()}"/>)]
			 								</option>	
		        					  </c:if>					
									</c:forEach>
								</c:forEach> 
							</c:forEach>
							</select>
								<br />&nbsp;Final Concentration In Lane (pM): <input type='text' name='libConcInLanePicoM' id="libConcInLanePicoM_<c:out value="${idCounter}" />" size='3' maxlength='5'>
								<br />&nbsp;<input type='submit' value='Submit'/>&nbsp;<input class="fm-button" type="button" value="Cancel" onclick='toggleDisplayOfAddLibraryForm("cancel", <c:out value="${idCounter}" />)' />
							</form>
							</td></tr>
							</table>
							</div>
						
					</td>						
					<td>
					<c:set var="sampleSourceList" value="${facilityLibraryForThisMacromolecule.getSampleSourceViaSourceSampleId()}" scope="page" />
							<c:choose>
								<c:when test="${sampleSourceList.size() > 0}">
									<c:forEach items="${sampleSourceList}" var="sampleSource">
										<c:set var="cell" value="${sampleSource.getSample()}" scope="page" />
										<c:set var="sampleSourceList2" value="${cell.getSampleSourceViaSourceSampleId()}" scope="page" />
										<c:forEach items="${sampleSourceList2}" var="sampleSource2">
											<c:set var="laneNumber" value="${sampleSource2.getMultiplexindex()}" scope="page" />
											<c:set var="platformUnit" value="${sampleSource2.getSample()}" scope="page" />
											<c:out value="${platformUnit.getName()}"/> Lane: <c:out value="${laneNumber}"/> 
											<c:set var="runList" value="${platformUnit.getRun()}" scope="page" />
											<c:if test="${runList.size() > 0}">
												<br />&nbsp;&nbsp;&nbsp;---&gt; <c:out value="${runList.get(0).getName()}"/>
											</c:if>
											<sec:authorize access="hasRole('su') or hasRole('ft')"><a href="<c:url value="/facility/platformunit/showPlatformUnit/${platformUnit.getSampleId()}.do" />"> [view]</a></sec:authorize>
											<br />
										</c:forEach>
									</c:forEach>
	
								</c:when>
								<c:otherwise>
									No FlowCell / Run <br />
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
		<td colspan="2" class="label-centered" style="background-color:#FAF2D6">User-Submitted Library</td><td class="label-centered" style="background-color:#FAF2D6">FlowCells/Runs</td>
	</tr>
	<c:forEach items="${librarySubmittedSamplesList}" var="userSubmittedLibrary">
	<tr>
		<td colspan="2">
			Name: <a href="<c:url value="/sampleDnaToLibrary/librarydetail_ro/${job.jobId}/${userSubmittedLibrary.getSampleId()}.do" />"><c:out value="${userSubmittedLibrary.getName()}"/></a><br />
			Type: <c:out value="${userSubmittedLibrary.getSampleType().getName()}"/><br />
			Species: <c:out value="${speciesMap.get(userSubmittedLibrary)}"/><br />
			<c:set var="adaptor" value="${libraryAdaptorMap.get(userSubmittedLibrary)}" scope="page" />
			Adaptor: <c:out value="${adaptor.getAdaptorset().getName()}"/><br />
			Index <c:out value="${adaptor.getBarcodenumber()}"/> [<c:out value="${adaptor.getBarcodesequence()}"/>]<br />
			Arrival Status: <c:out value="${receivedStatusMap.get(userSubmittedLibrary)}"/><sec:authorize access="hasRole('su') or hasRole('ft')">&nbsp;<a href="<c:url value="/task/updatesamplereceive/${job.jobId}.do" />">[update]</a></sec:authorize><br />

			<c:if test='${receivedStatusMap.get(userSubmittedLibrary)=="RECEIVED"}'>
				<c:set var="idCounter" value="${idCounter + 1}" scope="page" />
 				<sec:authorize access="hasRole('su') or hasRole('ft')">
				<div id="showButton_<c:out value="${idCounter}" />" >
					<input class="fm-button" type="button" value="Add Library To Flow Cell" onclick='toggleDisplayOfAddLibraryForm("show", <c:out value="${idCounter}" />)' />				
				</div>
				</sec:authorize>
				<div id="addLibraryForm_<c:out value="${idCounter}" />" style="display:none">
				<table class='data'>
					<tr class="FormData"><td class="label-centered">Add Library To Flow Cell Lane</td></tr>
					<tr><td>
					<form  method='post' name='addLibToPU' action="<c:url value="/facility/platformunit/assignAdd2.do" />" onsubmit="return validate_submit(this);">
						<input type='hidden' name='jobid' value='<c:out value="${job.jobId}" />'/>
				 		<input type='hidden' name='librarysampleid' value='<c:out value="${userSubmittedLibrary.getSampleId()}" />'/>
						<br />
				 		<select class="FormElement ui-widget-content ui-corner-all" name="lanesampleid" id="lanesampleid_<c:out value="${idCounter}" />" size="1" onchange="validate(this)">
							<option value="0">--SELECT A FLOW CELL LANE--</option>
							<c:forEach items="${availableAndCompatibleFlowCells}" var="flowCell">
								<option value="0">FlowCell: <c:out value="${flowCell.getName()}" /> [<c:out value="${flowCell.getSampleSubtype().getName()}" />]</option>
								<c:set var="sampleSourceList" value="${flowCell.getSampleSource()}" scope="page" />
								<c:forEach items="${sampleSourceList}" var="sampleSource">
									<c:set var="cell" value="${sampleSource.getSampleViaSource()}" scope="page" />
									<option style="color:red; font-weight: bold;" value="<c:out value="${cell.getSampleId()}" />">&nbsp;&nbsp;&nbsp;Lane: <c:out value="${cell.getName()}" /></option>
									<c:set var="sampleSourceList2" value="${cell.getSampleSource()}" scope="page" />
									<c:forEach items="${sampleSourceList2}" var="sampleSource2">
										<c:set var="library" value="${sampleSource2.getSampleViaSource()}" scope="page" />
									  	<c:if test="${library.getSampleSubtype().getIName() == 'controlLibrarySample'}">
											<option value="0">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Library (Control): <c:out value="${library.getName()}" />
											<c:set var="adaptor" value="${libraryAdaptorMap.get(library)}" scope="page" />
											&nbsp;-&nbsp;<c:out value="${adaptor.getAdaptorset().getName()}"/>&nbsp;[Index <c:out value="${adaptor.getBarcodenumber()}"/>&nbsp;(<c:out value="${adaptor.getBarcodesequence()}"/>)]
			 								</option>	
		        					  </c:if>					
									</c:forEach> 
									<c:forEach items="${sampleSourceList2}" var="sampleSource2">
										<c:set var="library" value="${sampleSource2.getSampleViaSource()}" scope="page" />
									  	<c:if test="${library.getSampleSubtype().getIName() != 'controlLibrarySample'}">
											<option value="0">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Library: <c:out value="${library.getName()}" />
											<c:set var="adaptor" value="${libraryAdaptorMap.get(library)}" scope="page" />
											&nbsp;-&nbsp;<c:out value="${adaptor.getAdaptorset().getName()}"/>&nbsp;[Index <c:out value="${adaptor.getBarcodenumber()}"/>&nbsp;(<c:out value="${adaptor.getBarcodesequence()}"/>)]
			 								</option>	
		        					  </c:if>					
									</c:forEach>
								</c:forEach> 
							</c:forEach>
						</select>
						<br />&nbsp;Final Concentration In Lane (pM): <input type='text' name='libConcInLanePicoM' id="libConcInLanePicoM_<c:out value="${idCounter}" />" size='3' maxlength='5'>
						<br />&nbsp;<input type='submit' value='Submit'/>&nbsp;<input class="fm-button" type="button" value="Cancel" onclick='toggleDisplayOfAddLibraryForm("cancel", <c:out value="${idCounter}" />)' />
					</form>
					</td></tr>
				</table>
				</div>
			</c:if>
		</td>
		<td>
		<c:set var="sampleSourceList" value="${userSubmittedLibrary.getSampleSourceViaSourceSampleId()}" scope="page" />
		<c:choose>
			<c:when test="${sampleSourceList.size() > 0}">
				<c:forEach items="${sampleSourceList}" var="sampleSource">
					<c:set var="cell" value="${sampleSource.getSample()}" scope="page" />
					<c:set var="sampleSourceList2" value="${cell.getSampleSourceViaSourceSampleId()}" scope="page" />
					<c:forEach items="${sampleSourceList2}" var="sampleSource2">
						<c:set var="laneNumber" value="${sampleSource2.getMultiplexindex()}" scope="page" />
						<c:set var="platformUnit" value="${sampleSource2.getSample()}" scope="page" />
						<c:out value="${platformUnit.getName()}"/> Lane: <c:out value="${laneNumber}"/> 
						<c:set var="runList" value="${platformUnit.getRun()}" scope="page" />
						<c:if test="${runList.size() > 0}">
							<br />&nbsp;&nbsp;&nbsp;---&gt; <c:out value="${runList.get(0).getName()}"/>
						</c:if>
						<sec:authorize access="hasRole('su') or hasRole('ft')"><a href="<c:url value="/facility/platformunit/showPlatformUnit/${platformUnit.getSampleId()}.do" />"> [view]</a></sec:authorize>
						<br />
					</c:forEach>
				</c:forEach>			
			</c:when>
			<c:otherwise>
				No FlowCell / Run <br />
			</c:otherwise>
		</c:choose>
		</td>
	</tr>
	</c:forEach>
</c:if>

</table>