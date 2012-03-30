<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<head>
<script>

function toggleDisplayOfUpdateForm(instruction, idCounter){
	//alert ("more testing; id = " + idCounter);
	var formDivName = "updatePicoFormDiv_" + idCounter;
	var formDiv = document.getElementById(formDivName);
	var editAnchorDivName = "editAnchorDiv_" + idCounter;
	var editAnchorDiv = document.getElementById(editAnchorDivName);
	
	if(instruction == 'show'){
		editAnchorDiv.style.display = 'none';
		formDiv.style.display = 'block';
	}
	else if(instruction == 'cancel'){
		editAnchorDiv.style.display = 'block';
		formDiv.style.display = 'none';
	}	
}
function validateUpdateForm(idCounter){
	//alert("validateUpdate; id = " + idCounter);
	var textInputName = "libConcInLanePicoM_" + idCounter;
	var textInputObject = document.getElementById(textInputName);
	var trimmed = textInputObject.value.replace(/^\s+|\s+$/g, '') ;//trim from both ends
	if(trimmed == ""){
		alert("Please provide a value");
		textInputObject.value = "";
		textInputObject.focus();
		return false;
	}
	else{
		//alert("submit");
		var theFormName = "updatePicoForm_" + idCounter;
		var theForm = document.getElementById(theFormName);
		theForm.submit();
	}
	
}

</script>
</head>

<c:set var="idCounter" value="0" scope="page" />

<table class="EditTable ui-widget ui-widget-content">
<tr class="FormData"><td class="CaptionTD">PlatformUnit:</td><td class="DataTD"><c:out value="${platformUnit.name}" /></td></tr>
<tr class="FormData"><td class="CaptionTD">Type:</td><td class="DataTD"><c:out value="${platformUnit.subtypeSample.name}" /></td></tr>
<c:forEach items="${platformUnit.sampleBarcode}" var="sampleBarcodeItem">
	<tr class="FormData"><td class="CaptionTD">Barcode:</td><td class="DataTD"><c:out value="${sampleBarcodeItem.barcode.barcode}" /></td></tr></c:forEach>
<!--  <tr class="FormData"><td class="CaptionTD">Lanes:</td><td class="DataTD"><c:out value="${platformUnit.sampleSource.size()}" /></td></tr> -->
<c:forEach items="${platformUnit.sampleMeta}" var="pusm">
	<c:if test="${fn:substringAfter(pusm.k, '.') != 'comment'}">
		<tr class="FormData"><td class="CaptionTD" style="text-transform: capitalize"><c:out value="${fn:toLowerCase(fn:substringAfter(pusm.k, '.'))}" />:</td><td class="DataTD"><c:out value="${pusm.v}" /></td></tr>
	</c:if>
</c:forEach>
<c:forEach items="${platformUnit.sampleMeta}" var="pusm">
	<c:if test="${fn:substringAfter(pusm.k, '.') == 'comment' && pusm.v != '' }">
		<tr class="FormData"><td class="CaptionTD" style="text-transform: capitalize"><c:out value="${fn:toLowerCase(fn:substringAfter(pusm.k, '.'))}" />:</td><td class="DataTD"><textarea style='font-size:9px' READONLY cols='25' rows='4' wrap='virtual'><c:out value="${pusm.v}" /></textarea></td></tr>
	</c:if>
</c:forEach>
</table>

<c:if test="${platformUnit.sampleSource.size() > 0}">

	<table class="data">
		<tr><td colspan="${platformUnit.sampleSource.size()}" class="label-centered" nowrap><c:out value="${platformUnit.name}" /></td></tr>
		<tr>
			<c:forEach items="${platformUnit.sampleSource}" var="ss1">
				<c:set var="cell" value="${ss1.sampleViaSource}" scope="page" />
					<td class="label-centered" nowrap>Lane: <c:out value="${fn:substringAfter(cell.name, '/')}" /></td>
			</c:forEach>
		</tr>
		
		<tr>
		<c:forEach items="${platformUnit.sampleSource}" var="ss1">
			<c:set var="cell" value="${ss1.sampleViaSource}" scope="page" />
			<td class="value-centered-top-small" >
			<c:choose>
				<c:when test="${cell.sampleSource.size()==0}">
				&nbsp;</c:when>
			<c:otherwise>						
				<c:set var="counter" value="1" scope="page" />
				<c:forEach items="${cell.sampleSource}" var="ss2">
					<c:set var="library" value="${ss2.sampleViaSource}" scope="page" />
					<c:if test="${counter > 1}"><hr></c:if>
					<c:out value="${library.name}" />
					<c:set var="ss2Meta" value="${ss2.sampleSourceMeta}" scope="page" />
					<c:forEach items="${ss2Meta}" var="ss2MetaItem">
						<c:if test="${ss2MetaItem.k=='jobId'}"><a href="<c:url value="/sampleDnaToLibrary/listJobSamples/${ss2MetaItem.v}.do" />"> (Job: J<c:out value="${ss2MetaItem.v}"/>)</a></c:if>
					</c:forEach>
					<br />				
					<c:set var="libraryMeta" value="${library.sampleMeta}" scope="page" />
					<c:forEach items="${libraryMeta}" var="libraryMetaItem">
						<c:if test="${fn:substringAfter(libraryMetaItem.k, 'Library.') == 'adaptor'}">
            				<c:out value="${adaptors.get(libraryMetaItem.v).adaptorset.name}"/><br />
            				Index <c:out value="${adaptors.get(libraryMetaItem.v).barcodenumber}"/>: <c:out value="${adaptors.get(libraryMetaItem.v).barcodesequence}"/><br />
            			</c:if> 
					</c:forEach>
					<c:forEach items="${ss2Meta}" var="ss2MetaItem">
						<c:set var="idCounter" value="${idCounter + 1}" scope="page" />
						<!--  	<c:if test="${ss2MetaItem.k=='libConcInLanePicoM'}">Conc. On Lane: <c:out value="${ss2MetaItem.v}"/> pM <br /></c:if>  -->
						
						<div id="editAnchorDiv_<c:out value="${idCounter}" />" >
						<c:if test="${ss2MetaItem.k=='libConcInLanePicoM'}">Conc. On Lane: <c:out value="${ss2MetaItem.v}"/> pM <a href="javascript:void(0)" onclick='toggleDisplayOfUpdateForm("show", <c:out value="${idCounter}" />);'>[Edit]</a><br />	</c:if>					
						</div>	
						
						<div id="updatePicoFormDiv_<c:out value="${idCounter}" />" style="display:none">
						<form id="updatePicoForm_<c:out value="${idCounter}" />"  method='post' action="<c:url value="/facility/platformunit/updateConcInLane.do" />" >
							<input type='hidden' name='platformUnitId' value='<c:out value="${platformUnit.sampleId}" />'/>
							<input type='hidden' name='sampleSourceMetaId' value='<c:out value="${ss2MetaItem.sampleSourceMetaId}" />'/>
							<table class="data">
								<tr><td class="value-centered-small">New Conc. (pM): <input type='text' name='libConcInLanePicoM' id="libConcInLanePicoM_<c:out value="${idCounter}" />" size='3' maxlength='5' value='<c:out value="${ss2MetaItem.v}"/>'></td></tr>
								<tr><td class="value-centered-small">
								<input type="button" value="Update" onclick='validateUpdateForm(<c:out value="${idCounter}" />)' />&nbsp;<input type="button" value="Cancel" onclick='toggleDisplayOfUpdateForm("cancel", <c:out value="${idCounter}" />)' />
								</td></tr>
							</table>
						</form>
						</div>						
						
					</c:forEach>
					<form  name='removeLib' method='post' action="<c:url value="/facility/platformunit/assignRemove.do" />" onsubmit='return confirm("Remove library from this flowcell?");'>
						<input type='hidden' name='platformUnitId' value='<c:out value="${platformUnit.sampleId}" />'/>
						<input type='hidden' name='samplesourceid' value='<c:out value="${ss2.sampleSourceId}" />'/>
						<input type='submit' value='Remove Library'/>
					</form>					
					<c:set var="counter" value="${counter + 1}" scope="page" />
				</c:forEach>
			</c:otherwise>
		</c:choose>		
		</td>
		</c:forEach>
		</tr>
	</table>
</c:if>


