<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
<c:import url="/WEB-INF/jsp/sampleDnaToLibrary/runDetails.jsp" />
<br />
<table style="border:1px solid black;">
<tr><td class="CaptionTD" style="text-decoration:underline; text-align:center" colspan="2">Lane ${cellIndex} </td></tr>
<tr><td class="CaptionTD">Raw Reads: </td><td class="DataTD"><c:out value="100000" /></td></tr>
<tr><td class="CaptionTD">Pass Filter (PF) Reads: </td><td class="DataTD"><c:out value="50000" /> (50% of Raw Reads)</td></tr>
<tr><td class="CaptionTD">PF Reads (Expected Indexes): </td><td class="DataTD"><c:out value="49000" /> (49% of Raw Reads)</td></tr>
<tr><td class="CaptionTD">PF Reads (Unexpected Indexes): </td><td class="DataTD"><c:out value="1000" /> (1% of Raw Reads)</td></tr>
</table>
<%-- 
<br />
<table style="border:1px solid black;">
<tr><td class="CaptionTD" style="text-align:center;" colspan="8">Control Libraries </td></tr>
<tr>
	<td class="CaptionTD" style="text-align:center;">Parent</td>
	<td class="CaptionTD" style="text-align:center;">Library</td>
	<td class="CaptionTD" style="text-align:center;">Adaptor</td>
	<td class="CaptionTD" style="text-align:center;">Index</td>
	<td class="CaptionTD" style="text-align:center;">Tag</td>
	<td class="CaptionTD" style="text-align:center;">pM Applied</td>
	<td class="CaptionTD" style="text-align:center;">PF Reads</td>
	<td class="CaptionTD" style="text-align:center;">PF Reads (%)</td>
</tr>
<tr>
	<td class="DataTD" style="text-align:center;">N/A</td>
	<td class="DataTD" style="text-align:center;">phiX</td>
	<td class="DataTD" style="text-align:center;">TruSeq</td>
	<td class="DataTD" style="text-align:center;">1</td>
	<td class="DataTD" style="text-align:center;">ATCACG</td>
	<td class="DataTD" style="text-align:center;">1</td>
	<td class="DataTD" style="text-align:center;">1000</td>
	<td class="DataTD" style="text-align:center;">2%</td>
</tr>
</table>
--%>
<br />
<table class="data">
<tr class="FormData"><td class="label-centered" style="background-color:#FAF2D6" colspan="8">Control Libraries </td></tr>
<tr class="FormData">
	<td class="label-centered" style="background-color:#FAF2D6">Parent</td>
	<td class="label-centered" style="background-color:#FAF2D6">Library</td>
	<td class="label-centered" style="background-color:#FAF2D6">Adaptor</td>
	<td class="label-centered" style="background-color:#FAF2D6">Index</td>
	<td class="label-centered" style="background-color:#FAF2D6">Tag</td>
	<td class="label-centered" style="background-color:#FAF2D6">pM</td>
	<td class="label-centered" style="background-color:#FAF2D6">PF (%)</td>
	
</tr>
<tr>
	<td class="DataTD" style="text-align:center;">N/A</td>
	<td class="DataTD" style="text-align:center;">phiX</td>
	<td class="DataTD" style="text-align:center;">TruSeq</td>
	<td class="DataTD" style="text-align:center;">1</td>
	<td class="DataTD" style="text-align:center;">ATCACG</td>
	<td class="DataTD" style="text-align:center;">1</td>
	<td class="DataTD" style="text-align:center;">1000 (2%)</td>
	
</tr>
<tr class="FormData"><td class="label-centered" style="background-color:#FAF2D6" colspan="8">Data Libraries </td></tr>
<tr class="FormData">
	<td class="label-centered" style="background-color:#FAF2D6">Parent</td>
	<td class="label-centered" style="background-color:#FAF2D6">Library</td>
	<td class="label-centered" style="background-color:#FAF2D6">Adaptor</td>
	<td class="label-centered" style="background-color:#FAF2D6">Index</td>
	<td class="label-centered" style="background-color:#FAF2D6">Tag</td>
	<td class="label-centered" style="background-color:#FAF2D6">pM</td>
	<td class="label-centered" style="background-color:#FAF2D6">PF (%)</td>
	
</tr>
<tr>
	<td class="DataTD" style="text-align:center;">abc123-robdiv</td>
	<td class="DataTD" style="text-align:center;">abc123-robdiv_facLib_1_2_13</td>
	<td class="DataTD" style="text-align:center;">TruSeq</td>
	<td class="DataTD" style="text-align:center;">1</td>
	<td class="DataTD" style="text-align:center;">ATCACG</td>
	<td class="DataTD" style="text-align:center;">1</td>
	<td class="DataTD" style="text-align:center;">1000 (2%)</td>
	
</tr>
</table>