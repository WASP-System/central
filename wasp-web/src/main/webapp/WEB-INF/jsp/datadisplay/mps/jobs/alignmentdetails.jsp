<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
<c:import url="/WEB-INF/jsp/datadisplay/mps/jobs/rundetails.jsp" />
<br />
<table style="border:1px solid black;">
<tr><td class="CaptionTD" style="text-decoration:underline; text-align:center" colspan="2">Lane ${cellIndex} </td></tr>
<tr><td class="CaptionTD">Raw Reads (RR): </td><td class="DataTD"><c:out value="100000" /></td></tr>
<tr><td class="CaptionTD">Pass Filter Reads (PF): </td><td class="DataTD"><c:out value="50000" /> (50% of RR)</td></tr>
<tr><td class="CaptionTD">PF Reads (Expected Indexes): </td><td class="DataTD"><c:out value="49000" /> (98% of PF)</td></tr>
<tr><td class="CaptionTD">PF Reads (Unexpected Indexes): </td><td class="DataTD"><c:out value="1000" /> (2% of PF)</td></tr>
<tr>
	<td class="CaptionTD">All PF Reads For Lane ${cellIndex}: </td><td class="DataTD">
		<a href="<wasp:relativeUrl value="file/fileHandle/${fileHandle.getId()}/download.do" />" >Download All</a>
	</td>
</tr>
</table>


<br />
<table class="data">
<c:forEach items="${controlLibrariesForThisCellList}" var="controlLibrary" varStatus="status">
	<c:if test="${status.first}">
		<tr class="FormData"><td class="label-centered" style="background-color:#FAF2D6" colspan="5">Control Libraries</td></tr>
		<tr class="FormData">
			<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;">Parent</td>
			<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;">Library</td>
			<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;">PF for Index<br />Aligned (%PF for Index)</td>	
			<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;">Sample's Species<br />Aligner<br />Ref.Genome<br />Ref.Genome Species</td>	
			<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;">Alignments</td>	
		</tr>
	</c:if>
	<tr>
		<td class="DataTD" style="text-align:center; ">
			<c:set value="${libraryMacromoleculeMap.get(controlLibrary)}" var="parentMacromolecule"/>
			<c:choose>
			<c:when test="${not empty parentMacromolecule }">
				<c:out value="${parentMacromolecule.getName()}" />
			</c:when>
			<c:otherwise>
				N/A
			</c:otherwise>
			</c:choose>	
		</td>
		<td class="DataTD" style="text-align:center; ">
			<c:out value="${controlLibrary.getName()}" />
		</td>
		 
		
		<td class="DataTD" style="text-align:center; white-space:nowrap;">
			<c:out value="???????" /><br /><c:out value="??????? (75)" />
		</td>
		
		<td class="DataTD" style="text-align:center; white-space:nowrap; ">
			<c:out value="${libraryOrganismMap.get(controlLibrary)}" /><br />Bowtie<br />mm9<br /><c:out value="${libraryOrganismMap.get(controlLibrary)}" />
		</td>
		<td class="DataTD" style="text-align:center; white-space:nowrap;">
			Download (bam)<br />Download (bam index)<br />Download (sam)<br />GenomeBrowser View
		</td>
	</tr>
</c:forEach>

<c:forEach items="${librariesThatPassedQCForThisCellList}" var="library" varStatus="status2">
	<c:if test="${status2.first}">
		<tr class="FormData"><td class="label-centered" style="background-color:#FAF2D6" colspan="5">Libraries</td></tr>
		<tr class="FormData">
			<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;">Parent</td>
			<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;">Library</td>
			<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;">PF for Index<br />Aligned (%PF for Index)</td>	
			<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;">Sample's Species<br />Aligner<br />Ref.Genome<br />Ref.Genome Species</td>	
			<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;">Alignments</td>	
		</tr>
	</c:if>
	<tr>
		<td class="DataTD" style="text-align:center; ">
			<c:set value="${libraryMacromoleculeMap.get(library)}" var="parentMacromolecule"/>
			<c:choose>
			<c:when test="${not empty parentMacromolecule }">
				<c:out value="${parentMacromolecule.getName()}" />
			</c:when>
			<c:otherwise>
				N/A
			</c:otherwise>
			</c:choose>	
		</td>
		<td class="DataTD" style="text-align:center; ">
			<c:out value="${library.getName()}" />
		</td> 
		
		<td class="DataTD" style="text-align:center; white-space:nowrap;">
			<c:out value="?????????" /><br /><c:out value="??????? (90)" />
		</td>
		
		<td class="DataTD" style="text-align:center; white-space:nowrap;">
			<c:out value="${libraryOrganismMap.get(library)}" /><br />Bowtie<br />mm9<br /><c:out value="${libraryOrganismMap.get(library)}" />
		</td>
		<td class="DataTD" style="text-align:center; white-space:nowrap;"> 
			Download (bam)<br />Download (bam index)<br />Download (sam)<br />GenomeBrowser View
		</td>
	</tr>
</c:forEach>
</table>