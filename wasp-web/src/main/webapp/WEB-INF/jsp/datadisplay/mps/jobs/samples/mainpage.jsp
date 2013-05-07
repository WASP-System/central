<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<br /><br />
<h1><a  href="<c:url value="/sampleDnaToLibrary/listJobSamples/${job.jobId}.do" />">JobID J<c:out value="${job.jobId}" /></a>: Sample View</h1>

	<br /><br />
<div style="overflow:auto">
<table class="data">
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
		
</table>
</div>