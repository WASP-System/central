<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<br /><br />
<h1><a  href="<c:url value="/sampleDnaToLibrary/listJobSamples/${job.jobId}.do" />">JobID J<c:out value="${job.jobId}" /></a>: Data By Samples  <span style="font-size:small"> (<a style="color: #801A00;" href="<c:url value="/datadisplay/mps/jobs/${job.jobId}/runs.do" />">View Data By Sequence Runs</a>)</span></h1>

<table class="data">
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
					<label>Name:</label> <c:out value="${submittedObject.getName()}" /><br />
					<label>Type:</label> <c:out value="${submittedObject.getSampleType().getName()}"/><br />
					<label>Species:</label> <c:out value="${submittedObjectOrganismMap.get(submittedObject)}" /><br />
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
							<label>Name:</label> <c:out value="${library.getName()}" /><br />
							<label>Type:</label> <c:out value="${library.getSampleType().getName()}" /><br />
							<c:if test="${submittedLibraryList.contains(library)}">
								<label>Species:</label> <c:out value="${submittedObjectOrganismMap.get(library)}" /><br />
							</c:if>
							<c:set value="${libraryAdaptorsetMap.get(library)}" var="adaptorSet"/>
							<label>Adaptor:</label> <c:out value="${adaptorSet.getName()}" /> <br />
							 <c:set value="${libraryAdaptorMap.get(library)}" var="adaptor"/>
							<label>Index:</label> <c:out value="${adaptor.getBarcodenumber()}" /> [<c:out value="${adaptor.getBarcodesequence()}" />]<br />
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








<br /><br />
<div style="overflow:auto">
<table class="data">

<c:forEach items="${submittedObjectList}" var="submittedObject" varStatus="statusSubmittedObject">
	<c:if test="${statusSubmittedObject.index>0}">
		<tr class="FormData"><td colspan="13" class="label-centered" style="height:1px;background-color:black; white-space:nowrap;"></td></tr>
	</c:if>
	<c:if test="${statusSubmittedObject.first}">
		<tr class="FormData">
			<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;">Macromolecule</td>
			<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;">Species</td>
			<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;">Library</td>
			<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;">Adaptor</td>	
			<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;">Index-Tag</td>
			<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;">Run</td>
			<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;">Lane</td>
			<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;">Stats</td>
			<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;">PF</td>
			<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;">Aligned</td>
			<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;">RefGenome</td>
			<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;">SeqFiles</td>	
			<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;">AlignFiles</td>
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

	<c:set value="${submittedObjectLibraryRowspan.get(submittedObject) * submittedObjectCellRowspan.get(submittedObject)}" var="libraryRowspan"/>
		
	<tr>
		<td class="DataTD"  rowspan="${libraryRowspan}" style="text-align:center; white-space:nowrap;">
			<c:choose>
				<c:when test="${submittedMacromoleculeList.contains(submittedObject)}">
					<c:out value="${submittedObject.getName()}" />
				</c:when>
				<c:otherwise>
					N/A
				</c:otherwise>
			</c:choose>
		</td>
		<td class="DataTD" rowspan="${libraryRowspan}" style="text-align:center; white-space:nowrap;">
			<c:out value="${submittedObjectOrganismMap.get(submittedObject)}" />
		</td>
		
		<c:choose>
		<c:when test="${fn:length(libraryList)==0}">
			<td class="DataTD" style="text-align:center; white-space:nowrap;">&nbsp;</td>			
			<td class="DataTD" style="text-align:center; white-space:nowrap;">&nbsp;</td>			
			<td class="DataTD" style="text-align:center; white-space:nowrap;">&nbsp;</td>
			<td class="DataTD" style="text-align:center; white-space:nowrap;">&nbsp;</td>
			<td class="DataTD" style="text-align:center; white-space:nowrap;">&nbsp;</td>
			<td class="DataTD" style="text-align:center; white-space:nowrap;">&nbsp;</td>
			<td class="DataTD" style="text-align:center; white-space:nowrap;">&nbsp;</td>
			<td class="DataTD" style="text-align:center; white-space:nowrap;">&nbsp;</td>
			<td class="DataTD" style="text-align:center; white-space:nowrap;">&nbsp;</td>
			<td class="DataTD" style="text-align:center; white-space:nowrap;">&nbsp;</td>
			<td class="DataTD" style="text-align:center; white-space:nowrap;">&nbsp;</td></tr>
		</c:when>
		<c:otherwise>
		    <c:forEach items="${libraryList}" var="library" varStatus="statusLibrary">	
		   		<c:set value="${libraryCellListMap.get(library)}" var="cellList"/>
		   		
				<c:set value="${submittedObjectCellRowspan.get(submittedObject)}" var="cellRowspan"/>
	
				<c:if test="${!statusLibrary.first}"><tr></c:if>
				<td rowspan="${cellRowspan}"  class="DataTD" style="text-align:center; white-space:nowrap;"><c:out value="${library.getName()}" /></td>
				<td rowspan="${cellRowspan}"  class="DataTD" style="text-align:center; white-space:nowrap;">
					<c:set value="${libraryAdaptorsetMap.get(library)}" var="adaptorSet"/>
					<c:out value="${adaptorSet.getName()}" />					
				</td>
				<td rowspan="${cellRowspan}"  class="DataTD" style="text-align:center; white-space:nowrap;">
					<c:set value="${libraryAdaptorMap.get(library)}" var="adaptor"/>
					<c:out value="${adaptor.getBarcodenumber()}" /> - <c:out value="${adaptor.getBarcodesequence()}" />
				</td>
				<c:choose>
				<c:when test="${fn:length(cellList)==0}">
					<td class="DataTD" style="text-align:center; white-space:nowrap;">&nbsp;</td>
					<td class="DataTD" style="text-align:center; white-space:nowrap;">&nbsp;</td>
					<td class="DataTD" style="text-align:center; white-space:nowrap;">&nbsp;</td>
					<td class="DataTD" style="text-align:center; white-space:nowrap;">&nbsp;</td>
					<td class="DataTD" style="text-align:center; white-space:nowrap;">&nbsp;</td>
					<td class="DataTD" style="text-align:center; white-space:nowrap;">&nbsp;</td>				
					<td class="DataTD" style="text-align:center; white-space:nowrap;">&nbsp;</td>
					<td class="DataTD" style="text-align:center; white-space:nowrap;">&nbsp;</td></tr>			
				</c:when>
				<c:otherwise>
					<c:forEach items="${cellList}" var="cell" varStatus="cellLibrary">
						<c:if test="${!cellLibrary.first}"><tr></c:if>
						<c:set value="${cellPUMap.get(cell)}" var="pu"/>
						<c:set value="${cellRunMap.get(cell)}" var="run"/>
						<c:set value="${cellIndexMap.get(cell)}" var="laneIndex"/>
						<c:choose>
							<c:when test="${empty cellRunMap.get(cell)}">
								<td class="DataTD" style="text-align:center; white-space:nowrap;"><c:out value="${pu.getName()}" /> (not run)</td>
								<td class="DataTD" style="text-align:center; white-space:nowrap;"><c:out value="${laneIndex}" /></td>
								<td class="DataTD" style="text-align:center; white-space:nowrap;">&nbsp;</td>
								<td class="DataTD" style="text-align:center; white-space:nowrap;">&nbsp;</td>
								<td class="DataTD" style="text-align:center; white-space:nowrap;">&nbsp;</td>
								<td class="DataTD" style="text-align:center; white-space:nowrap;">&nbsp;</td>
								<td class="DataTD" style="text-align:center; white-space:nowrap;">&nbsp;</td>
								<td class="DataTD" style="text-align:center; white-space:nowrap;">&nbsp;</td>
							</c:when>						
							<c:otherwise>
								<td class="DataTD" style="text-align:center; white-space:nowrap;"><c:out value="${run.getName()}" /></td>
								<td class="DataTD" style="text-align:center; white-space:nowrap;"><c:out value="${laneIndex}" /></td>
								<td class="DataTD" style="text-align:center; white-space:nowrap;">fqc | stats</td>
								<td class="DataTD" style="text-align:center; white-space:nowrap;">10000000</td>
								<td class="DataTD" style="text-align:center; white-space:nowrap;">9000000</td>
								<td class="DataTD" style="text-align:center; white-space:nowrap;">mm9</td>
								<td class="DataTD" style="text-align:center; white-space:nowrap;">fastqc</td>
								<td class="DataTD" style="text-align:center; white-space:nowrap;">bam<br/>bam-i<br>sam</td>
							</c:otherwise>
						</c:choose>
						</tr>
					</c:forEach>
					
				</c:otherwise>
				</c:choose>
				
				
		    </c:forEach>
		</c:otherwise>
		</c:choose>
	



</c:forEach>
<%-- 
<tr class="FormData">

	<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;">Macromolecule</td>
	<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;">Library</td>
	<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;">Species</td>
	<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;">Adaptor</td>	
	<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;">Index-Tag</td>
	<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;">Run (length:type)</td>
	<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;">Lane</td>	
	<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;">Stats</td>
	<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;">PF</td>
	<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;">Aligned</td>
	<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;">RefGenome</td>
	<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;">SeqFiles</td>	
	<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;">AlignFiles</td>
</tr>

<tr>
	<td class="DataTD" style="text-align:center; white-space:nowrap;">rob's first DNA sample</td>
	<td class="DataTD" style="text-align:center; white-space:nowrap;">rob's first DNA sample_lib1</td>
	<td class="DataTD" style="text-align:center; white-space:nowrap;">Mus musculus</td>
	<td class="DataTD" style="text-align:center; white-space:nowrap;">TruSeq Indexed DNA</td>	
	<td class="DataTD" style="text-align:center; white-space:nowrap;">1-AAGCCT</td>
	<td class="DataTD" style="text-align:center; white-space:nowrap;">130408_SN7001401_0117_AX123YY (100:Single)</td>
	<td class="DataTD" style="text-align:center; white-space:nowrap;">1</td>	
	<td class="DataTD" style="text-align:center; white-space:nowrap;">fqc | stats</td>
	<td class="DataTD" style="text-align:center; white-space:nowrap;">10000000</td>
	<td class="DataTD" style="text-align:center; white-space:nowrap;">9876543</td>
	<td class="DataTD" style="text-align:center; white-space:nowrap;">mm9</td>	
	<td class="DataTD" style="text-align:center; white-space:nowrap;">fastq</td>	
	<td class="DataTD" style="text-align:center; white-space:nowrap;">bam<br/>bam-i<br>sam</td>
</tr>

<tr>
	<td class="DataTD" style="text-align:center; white-space:nowrap;">the second DNA sample</td>
	<td class="DataTD" style="text-align:center; white-space:nowrap;">the second DNA sample_lib1</td>
	<td class="DataTD" style="text-align:center; white-space:nowrap;">Mus musculus</td>
	<td class="DataTD" style="text-align:center; white-space:nowrap;">TruSeq Indexed DNA</td>	
	<td class="DataTD" style="text-align:center; white-space:nowrap;">2-AGCTAT</td>
	<td class="DataTD" style="text-align:center; white-space:nowrap;">130408_SN7001401_0117_AX123YY (100:Single)</td>
	<td class="DataTD" style="text-align:center; white-space:nowrap;">1</td>	
	<td class="DataTD" style="text-align:center; white-space:nowrap;">fqc | stats</td>
	<td class="DataTD" style="text-align:center; white-space:nowrap;">12430000</td>
	<td class="DataTD" style="text-align:center; white-space:nowrap;">10002500</td>
	<td class="DataTD" style="text-align:center; white-space:nowrap;">mm9</td>	
	<td class="DataTD" style="text-align:center; white-space:nowrap;">fastq</td>	
	<td class="DataTD" style="text-align:center; white-space:nowrap;">bam<br/>bam-i<br>sam</td>
</tr>
--%>		
</table>
</div>