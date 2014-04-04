<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
<br />
<table class="data">
<c:forEach items="${submittedObjectList}" var="submittedObject" varStatus="statusSubmittedObject">
	<c:if test="${statusSubmittedObject.index>0}">
		<tr class="FormData"><td colspan="13" class="label-centered" style="height:2px;background-color:black; white-space:nowrap;"></td></tr>
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
		   		<c:set value="${cellLibraryListMap.get(library)}" var="cellList"/>
		   		
				<c:set value="${submittedObjectCellRowspan.get(submittedObject)}" var="cellRowspan"/>
	
				<c:if test="${!statusLibrary.first}"><tr></c:if>
				<td rowspan="${cellRowspan}"  class="DataTD" style="text-align:center; white-space:nowrap;">
					<a href="javascript:void(0);" onclick='showModalessDialog("<wasp:relativeUrl value="datadisplay/mps/jobs/${job.getId()}/libraries/${library.getId()}/librarydetails.do" />");' >	
					<c:out value="${library.getName()}" />
					</a>
				</td>
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
								<td class="DataTD" style="text-align:center; white-space:nowrap;">
									<a href="javascript:void(0);" onclick='showModalessDialog("<wasp:relativeUrl value="datadisplay/mps/jobs/${job.getId()}/runs/${run.getId()}/rundetails.do" />");' >
									<c:out value="${run.getName()}" />
									</a>
								</td>
								<td class="DataTD" style="text-align:center; white-space:nowrap;">
									<a href="javascript:void(0);" onclick='showModalessDialog("<wasp:relativeUrl value="datadisplay/mps/jobs/${job.getId()}/runs/${run.getId()}/cells/${cell.getId()}/celldetails.do" />");' >
									<c:out value="${laneIndex}" />
									</a>
								</td>
								<td class="DataTD" style="text-align:center; white-space:nowrap;">fqc | stats</td>
								<td class="DataTD" style="text-align:center; white-space:nowrap;">10000000</td>
								<td class="DataTD" style="text-align:center; white-space:nowrap;">9000000</td>
								<td class="DataTD" style="text-align:center; white-space:nowrap;">mm9</td>
								<td class="DataTD" style="text-align:center; white-space:nowrap;">fastq</td>
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
</table>
<br />