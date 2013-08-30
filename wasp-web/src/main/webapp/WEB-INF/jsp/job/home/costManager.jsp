<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<c:import url="/WEB-INF/jsp/job/home/fadingMessage.jsp" />
<%-- What was used was: from http://hmkcode.com/spring-mvc-upload-file-ajax-jquery-formdata/ --%>
<%--Apparently need onsubmit='return false' to suppress hitting the event when the ENTER key is pressed with the cursor in the description input box --%>

<sec:authorize access="hasRole('su') or hasRole('ft') or hasRole('da')">
	<br /><a class="button" href="javascript:void(0);" onclick='loadNewPageWithAjax("<c:url value="/job/${job.getId()}/createQuoteOrInvoice.do" />");' >Create Quote / Invoice</a><br />
</sec:authorize>
<c:if test="${not empty mostRecentQuote }">
	<br /><h2>Most Recent Quote: <c:out value="${localCurrencyIcon}" /> <c:out value="${mostRecentQuote}" /></h2>
</c:if>
<br />
<a href="javascript:void(0);" onclick='$("#createNewQuote").css("display", "block");'>New Quote</a>
 | <a href="javascript:void(0);" onclick='$("#updateCurrentQuote").css("display", "block");'>Update Current Quote</a>
 

<br />
<form id="fileUploadFormId" action="<c:url value="/job/${job.getId()}/fileUploadManager.do" />" method="POST"  enctype="multipart/form-data" onsubmit='return false;' >
	<table class="data" style="margin: 0px 0px">
		<%-- 
		<tr class="FormData">
			<td colspan="3" class="label-centered" style="background-color:#FAF2D6"><fmt:message key="listJobSamples.fileUploadUploadNewFile.label"/></td>
		</tr>
		<tr class="FormData">
			<td class="label-centered" style="background-color:#FAF2D6"><fmt:message key="listJobSamples.fileUploadSelectFileToUpload.label"/></td>
			<td class="label-centered" style="background-color:#FAF2D6"><fmt:message key="listJobSamples.fileUploadProvideBriefDescription.label"/></td>
			<td class="label-centered" style="background-color:#FAF2D6"><fmt:message key="listJobSamples.file_action.label"/></td>
		</tr>
		<tr>
			<td class="DataTD value-centered"><input type="file" name="file_upload" /></td>
			<td class="DataTD value-centered" ><input type="text" maxlength="30" name="file_description" /></td>
			<td align="center">
				<input type="reset" name="reset" value="<fmt:message key="listJobSamples.file_reset.label" />" />
				--%>
				<%--I don't know why, but having uploadJqueryForm("fileUploadFormId") wired through onsubmit via a regular submit button makes for Major problems!!!! use the button below --%>
				<%--
				<a class="button" href="javascript:void(0);"  onclick='uploadJqueryForm("fileUploadFormId")' ><fmt:message key="listJobSamples.file_upload.label" /></a>
			</td>
		</tr>
		<c:if test="${fn:length(errorMessage)>0}">
				<tr><td colspan="3" align="center" style="color:red;font-weight:bold"><c:out value="${errorMessage}" /></td></tr>
		</c:if>
		--%>
		<c:choose>
			<c:when test="${empty fileGroups }">
				<tr class="FormData">
					<td class="label-centered" style="background-color:#FAF2D6">Job Quotes / Invoices</td>
					<td class="label-centered" style="background-color:#FAF2D6"><fmt:message key="listJobSamples.file_description.label"/></td>
					<td class="label-centered" style="background-color:#FAF2D6"><fmt:message key="listJobSamples.file_action.label"/></td>
				</tr>
				<tr class="FormData">
					<td colspan="3" class="DataTD value-centered">No Quotes Or Invoices Have Been Generated</td>
				</tr>			
			</c:when>
			<c:otherwise>
				<c:forEach items="${fileGroups}" var="fileGroup" varStatus="fileGroupCounter">
					<c:if test="${fileGroupCounter.first}">
						<tr class="FormData">
							<td class="label-centered" style="background-color:#FAF2D6">Job Quotes / Invoices</td>
							<td class="label-centered" style="background-color:#FAF2D6"><fmt:message key="listJobSamples.file_description.label"/></td>
							<td class="label-centered" style="background-color:#FAF2D6"><fmt:message key="listJobSamples.file_action.label"/></td>
						</tr>		
					</c:if>
				 	<c:set value="${fileGroupFileHandlesMap.get(fileGroup)}" var="fileHandles"/>
				 	<c:choose>
				 		<c:when test="${fn:length(fileHandles)==1}">
				 		  	<c:forEach items="${fileHandles}" var="fileHandle" >
				 		  		<tr>
				 		  			<td class="DataTD value-centered"><c:out value="${fileHandle.getFileName()}" /></td>
				 		  			<td class="DataTD value-centered"><c:out value="${fileGroup.getDescription()}" /></td>
				 		  			<!--  <a href="<wasp:url fileAccessor="${fileHandle}" />" > -->
				 		  			<td class="DataTD value-centered"><a href="<c:url value="/file/fileHandle/${fileHandle.getId()}/download.do" />" ><fmt:message key="listJobSamples.file_download.label"/></a> 
				 		  				<c:if test="${fileHandlesThatCanBeViewedList.contains(fileHandle)}">
			 		  					| <a href="javascript:void(0);" onclick='parent.showModalessDialog("<c:url value="/file/fileHandle/${fileHandle.getId()}/view.do" />");' ><fmt:message key="listJobSamples.file_view.label"/></a>
			 		  				<!-- no longer wanted	| <a href="javascript:void(0);" onclick='doGetWithAjax("<c:url value="/job/${job.getId()}/createQuote.do" />");' >CreateQuote</a> -->
			 		  				</c:if>
				 		  			</td>
				 		  		</tr>
				 			</c:forEach>
				 		</c:when>			 		  
				 		<c:otherwise>
				 			<tr>
				 		  		<td class="DataTD value-centered"><c:out value="${fn:length(fileHandles)}" /> <fmt:message key="listJobSamples.file_download_grouped_files.label"/></td>
				 		  		<td class="DataTD value-centered"><c:out value="${fileGroup.getDescription()}" /></td>			 		  			
				 		  		<td class="DataTD value-centered"><a href="<c:url value="/file/fileGroup/${fileGroup.getId()}/download.do" />" ><fmt:message key="listJobSamples.file_download.label"/></a></td>
				 		  	</tr>
				 		</c:otherwise>			 		
				 	</c:choose>
				</c:forEach>
			</c:otherwise>
		</c:choose>		
	</table>
</form>
<br />
<div id="createNewQuote" style="display:none">

	<c:set value="${1}" var="rowCounter" />	
	<c:set value="${mpsQuote}" var="mpsQuote" />
	
	<span style='font-weight:bold'>1. Library Constructions Expected For This Job: <c:out value="${numberOfLibrariesExpectedToBeConstructed}" />
		<c:if test="${numberOfLibrariesExpectedToBeConstructed > 0}">
			&nbsp;&nbsp;[if no charge for a library, please set its cost to 0]
		</c:if>
	</span>
	<br /><br />
	<table class="data" style="margin: 0px 0px">
		<tr class="FormData">
			<td class="label-centered" style="background-color:#FAF2D6">Number</td>
			<td class="label-centered" style="background-color:#FAF2D6">Submitted Sample</td>
			<td class="label-centered" style="background-color:#FAF2D6">Material</td>
			<td class="label-centered" style="background-color:#FAF2D6">Library Cost</td>
		</tr>		
		<c:forEach items="${mpsQuote.getSubmittedSamples()}" var="submittedSample" >
			<input type='hidden' name="submittedSampleId" value="${submittedSample.getSampleId()}"/>
			<tr>
				<td class="DataTD"  style="text-align:center; white-space:nowrap;">
					<c:out value="${submittedSample.getNumber()}" />
				</td>
				<td class="DataTD"  style="text-align:center; white-space:nowrap;">
					<c:out value="${submittedSample.getSampleName()}" />
				</td>
				<td class="DataTD"  style="text-align:center; white-space:nowrap;">
					<c:out value="${submittedSample.getMaterial()}"/>
				</td>
				<td class="DataTD"  style="text-align:center; white-space:nowrap;">
				
					<c:choose>
						<c:when test='${submittedSample.getCost()=="N/A"}'>
							<c:out value="${submittedSample.getCost()}" />
							<input type='hidden' name="libraryCost_${submittedSample.getSampleId()}" value="${submittedSample.getCost()}"/>
						</c:when>
						<c:otherwise>
							<c:out value="${localCurrencyIcon}" /><input style="text-align:right;" name="libraryCost_${submittedSample.getSampleId()}" type="text" maxlength="4" size="4" value="<fmt:formatNumber type="number" groupingUsed="false" maxFractionDigits="0" value="${submittedSample.getCost()}" />"/>.00
						</c:otherwise>
					</c:choose>					
				</td>
			</tr>
		</c:forEach>			
	</table>
	<br /><br />
	<span style='font-weight:bold'>2. Sequencing Lanes Expected For This Job: <c:out value="${numberOfLanesRequested}" /></span><br /><br />
	<table class="data" style="margin: 0px 0px">
		<tr class="FormData">
			<td class="label-centered" style="background-color:#FAF2D6">Machine</td>
			<td class="label-centered" style="background-color:#FAF2D6">ReadLength</td>
			<td class="label-centered" style="background-color:#FAF2D6">ReadType</td>
			<td class="label-centered" style="background-color:#FAF2D6">No. Lanes</td>
			<td class="label-centered" style="background-color:#FAF2D6">Cost/Lane</td>
			<td class="label-centered" style="background-color:#FAF2D6">Action</td>
		</tr>
		<c:forEach items="${mpsQuote.getSequenceRuns()}" var="sequenceRun" >
			<tr id="sequenceRunRow_${rowCounter}">
				<td align='center'><input type='text' size='20' maxlength='44' name='runCostMachine' id='runCostMachine' value="${sequenceRun.getMachine()}"></td>
				<td align='center'><input type='text' style="text-align:right;" size='4' maxlength='4' name='runCostReadLength' id='runCostReadLength' value="${sequenceRun.getReadLength()}"></td>
				<td align='center'><input type='text' style="text-align:right;" size='6' maxlength='6' name='runCostReadType' id='runCostReadType' value="${sequenceRun.getReadType()}"></td>
				<td align='center'><input type='text' style="text-align:right;" size='6' maxlength='6' name='runCostNumberLanes' id='runCostNumberLanes' value="${sequenceRun.getNumberOfLanes()}"></td>
				<%-- <td align='center'><c:out value="${localCurrencyIcon}" /><input type='text' style="text-align:right;" size='6' maxlength='6' name='runCostPricePerLane' id='runCostPricePerLane' value="${sequenceRun.getCostPerLane()}">.00</td>--%>
				<td align='center'><c:out value="${localCurrencyIcon}" /><input type='text' style="text-align:right;" size='6' maxlength='6' name='runCostPricePerLane' id='runCostPricePerLane' value="<fmt:formatNumber type="number" groupingUsed="false" maxFractionDigits="0" value="${sequenceRun.getCostPerLane()}" />">.00</td>
				<td align='center'><span style="font-weight:bold;color:red;"><c:out value="${sequenceRun.getError()}" /><c:if test="${not empty sequenceRun.getError()}">&nbsp;</c:if></span><input type="button" onclick='$("#sequenceRunRow_${rowCounter}").remove();' value="Delete"/></td>
			</tr>
			<c:set value="${rowCounter + 1}" var="rowCounter" />		
		</c:forEach>
		<tr id="sequenceRunTableVeryLastRow"><td colspan="6" align="center"><input id="sequenceRunAddRowButton" style="width:300" type="button"  value="ADD ADDITIONAL ROW"/></td></tr>
	</table>
	<br /><br />
	<span style='font-weight:bold'>3. Additional Costs Expected For This Job: </span>
	<br /><br />
	<table class="data" style="margin: 0px 0px">
		<tr class="FormData">
			<td class="label-centered" style="background-color:#FAF2D6">Reason</td>
			<td class="label-centered" style="background-color:#FAF2D6">Units</td>
			<td class="label-centered" style="background-color:#FAF2D6">Cost/Unit</td>
			<td class="label-centered" style="background-color:#FAF2D6">Action</td>
		</tr>
		<c:forEach items="${mpsQuote.getAdditionalCosts()}" var="additionalCost">
			<tr id="additionalCostRow_${rowCounter}">
				<td align='center'><input type='text' size='20' maxlength='44' name='additionalCostReason' id='additionalCostReason' value="${additionalCost.getReason()}"></td>
				<td align='center'><input type='text' style="text-align:right;" size='4' maxlength='4' name='additionalCostUnits' id='additionalCostUnits' value="${additionalCost.getNumberOfUnits()}"></td>
				<td align='center'><c:out value="${localCurrencyIcon}" /><input type='text' style="text-align:right;" size='6' maxlength='6' name='additionalCostPricePerUnit' id='additionalCostPricePerUnit' value="<fmt:formatNumber type="number" groupingUsed="false" maxFractionDigits="0" value="${additionalCost.getCostPerUnit()}" />">.00</td>
				<td align='center'><span style="font-weight:bold;color:red;"><c:out value="${additionalCost.getError()}" /><c:if test="${not empty additionalCost.getError()}">&nbsp;</c:if></span><input type="button" onclick='$("#additionalCostRow_${rowCounter}").remove();' value="Delete"/></td>
			</tr>
			<c:set value="${rowCounter + 1}" var="rowCounter" />
		</c:forEach>
		<tr id="additionalCostTableVeryLastRow"><td colspan="4" align="center"><input id="additionalCostAddRowButton" style="width:300" type="button"  value="ADD ADDITIONAL ROW"/></td></tr>
	</table>
	<br /><br />


	<span style='font-weight:bold'>4. Discounts/Credits Expected For This Job: </span><br /><br />
	<table  class="data" style="margin: 0px 0px">
		<tr class="FormData">
			<td class="label-centered" style="background-color:#FAF2D6">Reason<sup>*</sup></td>
			<td class="label-centered" style="background-color:#FAF2D6"><c:out value="${localCurrencyIcon}" /> Or %</td>
			<td class="label-centered" style="background-color:#FAF2D6">Discount</td>
			<td class="label-centered" style="background-color:#FAF2D6">Action</td>
		</tr>
		<c:forEach items="${mpsQuote.getDiscounts()}" var="discount">
			<tr id="discountRow_${rowCounter}">
				<%-- <td align='center'><input onkeydown='robtest_autocomplete(this);'  type='text' size='20' maxlength='44' name='discountReason' id='discountReason'></td>--%>
				<td align='center'>
					<select name='discountReason' id='discountReason' size='1'>
						<option value=''>--SELECT--
						<c:forEach items="${discountReasons}" var="discountReason">
							<c:set value="" var="selected" />
							<c:if test="${discountReason == discount.getReason()}">
								<c:set value="SELECTED" var="selected" />
							</c:if>
							<option value='<c:out value="${discountReason}" />' <c:out value="${selected}" /> ><c:out value="${discountReason}" />
						</c:forEach>
					</select>
				</td>
				<td align='center'>
					<select name='discountType' id='discountType' size='1'>
						<option value=''>--SELECT--
						<c:forEach items="${discountTypes}" var="discountType">
							<c:set value="" var="selected" />
							<c:if test="${discountType == discount.getType()}">
								<c:set value="SELECTED" var="selected" />
							</c:if>
							<option value='<c:out value="${discountType}" />' <c:out value="${selected}" /> ><c:out value="${discountType}" />
						</c:forEach>					
					</select>
				</td>
				<td align='center'><input type='text' style="text-align:right;" size='4' maxlength='4' name='discountValue' id='discountValue' value="<fmt:formatNumber type="number" groupingUsed="false" maxFractionDigits="0" value="${discount.getValue()}" />">.00</td>
				<td align='center'><span style="font-weight:bold;color:red;"><c:out value="${discount.getError()}" /><c:if test="${not empty discount.getError()}">&nbsp;</c:if></span><input type="button" onclick='$("#discountRow_${rowCounter}").remove();' value="Delete"/></td>
			</tr>
			<c:set value="${rowCounter + 1}" var="rowCounter" />
		</c:forEach>
		<tr id="discountTableVeryLastRow"><td colspan="4" align="center"><input id="discountAddRowButton" style="width:300" type="button"  value="ADD ADDITIONAL ROW"/></td></tr>
	</table>
	<sup>*</sup><span style="font-size:small;color:red">Please select any particular discount/credit reason only once</span>
	<br /><br />

	<span style='font-weight:bold'>5. Comments For This Job: </span><br /><br />
	<table  class="data" style="margin: 0px 0px">
		<tr class="FormData">
			<td class="label-centered" style="background-color:#FAF2D6">Comment</td>
			<td class="label-centered" style="background-color:#FAF2D6">Action</td>		
		</tr>
		<c:forEach items="${mpsQuote.getComments()}" var="comment">
			<tr id="commentRow_${rowCounter}">
				<td align='center'><textarea id="comments" name="comments" cols="60" rows="4" >${comment.getComment()}</textarea><br /></td>
				<td align='center'><span style="font-weight:bold;color:red;"><c:out value="${comment.getError()}" /><c:if test="${not empty comment.getError()}">&nbsp;</c:if></span><input type="button" onclick='$("#commentRow_${rowCounter}").remove();' value="Delete"/></td>
			</tr>
			<c:set value="${rowCounter + 1}" var="rowCounter" />
		</c:forEach>
		<tr id="commentTableVeryLastRow"><td colspan="2" align="center"><input id="commentAddRowButton" style="width:300" type="button"  value="ADD ADDITIONAL ROW"/></td></tr>
	</table>
	<br /><br />

	<%-- this next script section MUST be at bottom, so that assignment of rowCounter is accurate --%>
	<script type="text/javascript">
		var localCurrencyIcon = "<c:out value="${localCurrencyIcon}" />";
		var rowCounter = "<c:out value="${rowCounter}" />"; 
		
		var discountReasons = "<c:out value="${discountReasons}" />";
		discountReasons = discountReasons.indexOf('[') == 0 ? discountReasons.substring(1) : discountReasons;
		discountReasons = discountReasons.indexOf(']') == discountReasons.length-1 ? discountReasons.substring(0, discountReasons.length-1) : discountReasons;
		var discountReasonsSplitOnComma = discountReasons.split(",");
		var discountReasonsAsOptions = "";
		for(var i = 0; i < discountReasonsSplitOnComma.length; i++){
			discountReasonsAsOptions += "<option value="+discountReasonsSplitOnComma[i]+">"+discountReasonsSplitOnComma[i];
		} 
		
		var discountTypes = "<c:out value="${discountTypes}" />";
		discountTypes = discountTypes.indexOf('[') == 0 ? discountTypes.substring(1) : discountTypes;
		discountTypes = discountTypes.indexOf(']') == discountTypes.length-1 ? discountTypes.substring(0, discountTypes.length-1) : discountTypes;
		var discountTypesSplitOnComma = discountTypes.split(",");
		var discountTypesAsOptions = "";
		for(var i = 0; i < discountTypesSplitOnComma.length; i++){
			discountTypesAsOptions += "<option value="+discountTypesSplitOnComma[i]+">"+discountTypesSplitOnComma[i];
		} 
		

	$(document).ready(function(){
		
		$("#sequenceRunAddRowButton").bind('click', function(){
			$("#sequenceRunTableVeryLastRow").before("<tr id=sequenceRunRow_"+rowCounter+" class=FormData>"+
							"<td align=center><input type=text size=20 maxlength=44 name=runCostMachine id=runCostMachine ></td>"+
							"<td align=center><input type=text style=text-align:right; size=4 maxlength=4 name=runCostReadLength id=runCostReadLength></td>"+
							"<td align=center><input type=text style=text-align:right; size=6 maxlength=6 name=runCostReadType id=runCostReadType ></td>"+
							"<td align=center><input type=text style=text-align:right; size=6 maxlength=6 name=runCostNumberLanes id=runCostNumberLanes></td>"+
							"<td align=center>"+localCurrencyIcon+"<input type='text' style=text-align:right; size=6 maxlength=6 name=runCostPricePerLane id=runCostPricePerLane>.00</td>"+
							"<td align=center><input type=button  onclick='$(\"#sequenceRunRow_"+rowCounter+"\").remove();' value='Delete' /></td>"+
							"</tr>");
			rowCounter++;
		});
		$("#additionalCostAddRowButton").bind('click', function(){
			$("#additionalCostTableVeryLastRow").before("<tr id=additionalCostRow_"+rowCounter+" class=FormData>"+
							"<td align=center><input type=text size=20 maxlength=44 name=additionalCostReason id=additionalCostReason ></td>"+ 
							"<td align=center><input type=text style=text-align:right; size=4 maxlength=4 name=additionalCostUnits id=additionalCostUnits></td>"+
							"<td align=center>"+localCurrencyIcon+"<input type='text' style=text-align:right; size=6 maxlength=6 name=additionalCostPricePerUnit id=additionalCostPricePerUnit>.00</td>"+
							"<td align=center><input type=button  onclick='$(\"#additionalCostRow_"+rowCounter+"\").remove();' value='Delete' /></td>"+
							"</tr>");
			rowCounter++;
		});
		$("#discountAddRowButton").bind('click', function(){
			$("#discountTableVeryLastRow").before("<tr id=discountRow_"+rowCounter+" class=FormData>"+
							"<td align=center><select name=discountReason id=discountReason size=1><option value=''>--SELECT--"+
							discountReasonsAsOptions+
							"</select></td>"+ 
							"<td align=center><select name=discountType id=discountType size=1><option value=''>--SELECT--"+
							discountTypesAsOptions+
							"</select></td>"+ 
							"<td align=center><input type='text' style=text-align:right; size=4 maxlength=4 name=discountValue id=discountValue>.00</td>"+
							"<td align=center><input type=button  onclick='$(\"#discountRow_"+rowCounter+"\").remove();' value='Delete' /></td>"+
							"</tr>");
			rowCounter++;
		});
		$("#commentAddRowButton").bind('click', function(){
			$("#commentTableVeryLastRow").before("<tr id=commentRow_"+rowCounter+" class=FormData>"+
							"<td align=center><textarea id=comments name=comments cols=60 rows=4 ></textarea><br /></td>"+
							"<td align=center><input type=button  onclick='$(\"#commentRow_"+rowCounter+"\").remove();' value='Delete' /></td>"+
							"</tr>");
			rowCounter++;
		});
		
	});  <%-- --%> 

</script>
	
</div>
<%-- 
<div id="updateCurrentQuote" style="display:none">
 b<br />b<br />b<br />a<br />a<br />a<br />a<br />a<br />a<br />a<br />a<br />a<br />a<br />a<br />a<br />a<br />a<br />a<br />a<br />
</div>
--%>