<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
<div>
<table>
<tr><td class="CaptionTD">Run Name: </td><td class="DataTD"><c:out value="${run.getName()}" /><sec:authorize access="hasRole('su') or hasRole('ft')"> [<a href="javascript:void(0);" onclick='parent.showModalessDialog("<wasp:relativeUrl value="waspIlluminaHiSeq/flowcell/${platformUnit.getId()}/show.do" />");' >Details1</a> | <a href="javascript:void(0);" title="popup"  onclick="parent.showPopupWindow('<wasp:relativeUrl value="wasp-illumina/flowcell/${platformUnit.getId()}/show.do" />');">Details2</a>]</sec:authorize></td></tr>
<tr><td class="CaptionTD">Machine: </td><td class="DataTD"><c:out value="${run.getResource().getName()}" /> [<c:out value="${run.getResourceCategory().getName()}" />]</td></tr>
<tr><td class="CaptionTD">Flowcell: </td><td class="DataTD"><c:out value="${platformUnit.getName()}" /> [<c:out value="${platformUnit.getSampleSubtype().getName()}" />]</td></tr>
<tr><td class="CaptionTD">Read Length: </td><td class="DataTD"><c:out value="${runReadLength}" /></td></tr>
<tr><td class="CaptionTD">Read Type: </td><td class="DataTD"><c:out value="${runReadType}" /></td></tr>
<tr><td class="CaptionTD">Total Lanes: </td><td class="DataTD"><c:out value="${totalLanesOnPlatformUnit}" /></td></tr>
<tr><td class="CaptionTD">Run Started: </td><td class="DataTD"><c:out value="${runStartDate}" /></td></tr>
<tr><td class="CaptionTD">Run Ended: </td><td class="DataTD"><c:out value="${runEndDate}" /></td></tr>
<tr><td class="CaptionTD">Run Status: </td><td class="DataTD"><c:out value="${run.getStatus()}" /></td></tr>
</table>
<%-- 
	<table>
	<tr><td class="CaptionTD">Run Name: </td><td class="DataTD"><c:out value="${run.getName()}" /><sec:authorize access="hasRole('su') or hasRole('ft')"> [<a href="javascript:void(0);" onclick='parent.showModalessDialog("<wasp:relativeUrl value="waspIlluminaHiSeq/flowcell/${platformUnit.getId()}/show.do" />");' >Details1</a> | <a href="javascript:void(0);" title="popup"  onclick="parent.showPopupWindow('<wasp:relativeUrl value="wasp-illumina/flowcell/${platformUnit.getId()}/show.do" />');">Details2</a>]</sec:authorize></td></tr>
	<tr><td class="CaptionTD">Machine: </td><td class="DataTD"><c:out value="${run.getResource().getName()}" /> [<c:out value="${run.getResourceCategory().getName()}" />]</td></tr>
	<tr><td class="CaptionTD">Flowcell: </td><td class="DataTD"><c:out value="${platformUnit.getName()}" /> [<c:out value="${platformUnit.getSampleSubtype().getName()}" />]</td></tr>
	<tr><td class="CaptionTD">Total Lanes: </td><td class="DataTD"><c:out value="${totalLanesOnPlatformUnit}" /></td></tr>
	<tr >
	<td colspan="2" style="margin: 0px 0px 0px 0px; padding:0px 0px 0px 0px;">
		<table style="margin: 0px 0px 0px 0px; padding:0px 0px 0px 0px;">
		  <tr>
		  	<td class="CaptionTD" style="text-align:left;">Read Length: </td><td class="DataTD" style="text-align:left;"><c:out value="${runReadLength}" /></td>
		  	<td class="CaptionTD" style="text-align:left;" >Read Type: </td><td class="DataTD" style="text-align:left;"><c:out value="${runReadType}" /></td>
		  </tr>
		  <tr>
		    <td class="CaptionTD" style="text-align:left;">Start Run: </td><td class="DataTD" style="text-align:left;"><c:out value="${runStartDate}" /></td>
		    <td class="CaptionTD" style="text-align:left;">End Run: </td><td class="DataTD" style="text-align:left;"><c:out value="${runEndDate}" /></td>
		  </tr>
		</table>
	</td>
	</tr>
	<tr><td class="CaptionTD">Run Status: </td><td class="DataTD"><c:out value="${run.getStatus()}" /></td></tr>
--%>
</div>