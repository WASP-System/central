<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<sec:authorize access="hasRole('su') or hasRole('ft') or hasRole('da')">
<%-- 
  <link rel="stylesheet" href="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css" />
  <script src="http://code.jquery.com/jquery-1.9.1.js"></script>
  <script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
--%>
  <style>
  .ui-autocomplete {
    max-height: 100px;
    overflow-y: auto;
    /* prevent horizontal scrollbar */
    overflow-x: hidden;
  }
  /* IE 6 doesn't support max-height
   * we use height instead, but this forces the menu to always be this tall
   */
  * html .ui-autocomplete {
    height: 100px;
  }
  </style>



<script type="text/javascript" src="/wasp/scripts/jquery/jquery.table.addrow.js"></script>
<script type="text/javascript">
(function($){ 
	$(document).ready(function(){
		$(".addRow").btnAddRow();
		$(".delRow").btnDelRow();
		
	/*	
		$(function() {
		    var availableTags = [
		      "ActionScript",
		      "AppleScript",
		      "Asp",
		      "BASIC",
		      "C",
		      "C++",
		      "Clojure",
		      "COBOL",
		      "ColdFusion",
		      "Erlang",
		      "Fortran",
		      "Groovy",
		      "Haskell",
		      "Java",
		      "JavaScript",
		      "Lisp",
		      "Perl",
		      "PHP",
		      "Python",
		      "Ruby",
		      "Scala",
		      "Scheme"
		    ];
		    $( "#discountReason" ).autocomplete({
		      source: availableTags
		    });
		  });
		*/
		
		
});
})(jQuery);
</script>

<%-- <c:import url="/WEB-INF/jsp/job/home/fadingMessage.jsp" /> --%>
<%-- What was used was: from http://hmkcode.com/spring-mvc-upload-file-ajax-jquery-formdata/ --%>
<%--Apparently need onsubmit='return false' to suppress hitting the event when the ENTER key is pressed with the cursor in the description input box --%>

<form  method='post' name='quoteOrInvoiceForm' id="quoteOrInvoiceFormId" action="" >
<div class="ui-widget">
<div id="container_div_for_adding_rows" >
<br />

<c:set value="${mpsQuote}" var="mpsQuote" />
<c:set value="${mpsQuote.getLocalCurrencyIcon()}" var="localCurrencyIcon" />
<input type='hidden' name="localCurrencyIcon" value="${localCurrencyIcon}"/>

<c:set value="${mpsQuote.getNumberOfLibrariesExpectedToBeConstructed()}" var="numberOfLibrariesExpectedToBeConstructed" />
<input type='hidden' name="numberOfLibrariesExpectedToBeConstructed" value="${numberOfLibrariesExpectedToBeConstructed}"/>

<c:set value="${mpsQuote.getNumberOfLanesRequested()}" var="numberOfLanesRequested" />
<input type='hidden' name="numberOfLanesRequested" value="${numberOfLanesRequested}"/>
			
<span style="padding:3px; border: 1px solid black;">
<a <%-- class="button" --%> href="javascript:void(0);" onclick='loadNewPageWithAjax("<c:url value="/job/${mpsQuote.getJob().getId()}/costManager.do" />");' >Return To Costs Page</a>
| <a <%-- class="button" --%> href="javascript:void(0);" onclick='showSmallModalessDialog("<c:url value="/job/${mpsQuote.getJob().getId()}/basic.do" />");' >View Basic Request</a>
| <a <%-- class="button" --%> href="javascript:void(0);" onclick='showSmallModalessDialog("<c:url value="/job/${mpsQuote.getJob().getId()}/requests.do?onlyDisplayCellsRequested=true" />");' >View Lane Request</a>
| <a <%-- class="button" --%> href="javascript:void(0);" onclick='sendFormViaGetAndShowModlessDialog("quoteOrInvoiceFormId", "<c:url value="/job/${mpsQuote.getJob().getId()}/previewQuote.do" />");' >Preview Quote</a>
| <a <%-- class="button" --%> href="javascript:void(0);" onclick='sendFormViaGetAndShowModlessDialog("quoteOrInvoiceFormId", "<c:url value="/job/${mpsQuote.getJob().getId()}/saveQuote.do" />");' >Save Quote</a>
</span>
<br /><br /><br />

<span style='font-weight:bold'>1. Library Constructions Expected For This Job: <c:out value="${numberOfLibrariesExpectedToBeConstructed}" />
	<c:if test="${numberOfLibrariesExpectedToBeConstructed > 0}">
		&nbsp;&nbsp;[if no charge for a library, please set its cost to 0]
	</c:if>
</span><br /><br />

<table class="data" style="margin: 0px 0px">
	<tr class="FormData">
		<td class="label-centered" style="background-color:#FAF2D6">Number</td>
		<td class="label-centered" style="background-color:#FAF2D6">Submitted Sample</td>
		<td class="label-centered" style="background-color:#FAF2D6">Material</td>
		<td class="label-centered" style="background-color:#FAF2D6">Library Cost</td>
	</tr>
	<c:forEach items="${mpsQuote.getLibraryCosts()}" var="libraryCost" varStatus="libraryCostStatus">
			<input type='hidden' name="submittedSampleId" value="${libraryCost.getSample().getId()}"/>
			<tr>
				<td class="DataTD"  style="text-align:center; white-space:nowrap;">
					<c:out value="${libraryCostStatus.count}" />
				</td>
				<td class="DataTD"  style="text-align:center; white-space:nowrap;">
					<input type='hidden' name="submittedSampleName" value="${libraryCost.getSampleName()}"/>
					<c:out value="${libraryCost.getSampleName()}" />
				</td>
				<td class="DataTD"  style="text-align:center; white-space:nowrap;">
					<input type='hidden' name="submittedSampleMaterial" value="${libraryCost.getMaterial()}"/>
					<c:out value="${libraryCost.getMaterial()}"/>
				</td>
				<td class="DataTD"  style="text-align:center; white-space:nowrap;">
				
					<c:choose>
					<c:when test='${libraryCost.getLibraryCost()=="" || libraryCost.getLibraryCost().matches("[0-9]+")}'>
								<c:out value="${localCurrencyIcon}" /><input style="text-align:right;" name="submittedSampleCost" type="text" maxlength="4" size="4" value="<fmt:formatNumber type="number" groupingUsed="false" maxFractionDigits="0" value="${libraryCost.getLibraryCost()}" />"/>.00
						</c:when>
						<c:otherwise>
							<c:out value="${libraryCost.getLibraryCost()}" />
							<input type='hidden' name="submittedSampleCost" value="${libraryCost.getLibraryCost()}"/></c:otherwise>
					</c:choose>					
				</td>
			</tr>
		</c:forEach>
<%--
	<c:forEach items="${submittedObjectList}" var="submittedObject" varStatus="statusSubmittedObject">
		<input type='hidden' name="submittedObjectId" value="${submittedObject.getId()}"/>
		<tr>
			<td class="DataTD"  style="text-align:center; white-space:nowrap;">
				<c:out value="${statusSubmittedObject.index + 1}" />
			</td>
			<td class="DataTD"  style="text-align:center; white-space:nowrap;">
				<c:out value="${submittedObject.getName()}" />
			</td>
			<td class="DataTD"  style="text-align:center; white-space:nowrap;">
				<c:out value="${submittedObject.getSampleType().getName()}"/>
			</td>
			<td class="DataTD"  style="text-align:center; white-space:nowrap;">
				<c:choose>
					<c:when test="${submittedMacromoleculeList.contains(submittedObject)}">
						<c:out value="${localCurrencyIcon}" /><input style="text-align:right;" name="libraryCost_${submittedObject.getId()}" type="text" maxlength="4" size="4" />.00
					</c:when>
					<c:otherwise>
						N/A
						<input type='hidden' name="libraryCost_${submittedObject.getId()}" value='0'/>			 				
					</c:otherwise>
				</c:choose>
			</td>
		</tr>
	</c:forEach>
--%>	
</table>
<br /><br />

<span style='font-weight:bold'>2. Sequencing Lanes Expected For This Job: <c:out value="${numberOfLanesRequested}" /></span><br /><br />
<table  class="data" style="margin: 0px 0px">
	<tr class="FormData">
		<td class="label-centered" style="background-color:#FAF2D6">Machine</td>
		<td class="label-centered" style="background-color:#FAF2D6">ReadLength</td>
		<td class="label-centered" style="background-color:#FAF2D6">ReadType</td>
		<td class="label-centered" style="background-color:#FAF2D6">No. Lanes</td>
		<td class="label-centered" style="background-color:#FAF2D6">Cost/Lane</td>
		<td class="label-centered" style="background-color:#FAF2D6">Action</td>
	</tr>
	<c:choose>
	<c:when test="${not empty mpsQuote.getSequencingCosts()}">
		<c:forEach items="${mpsQuote.getSequencingCosts()}" var="sequencingCost" >
			<tr>
				<td align='center'>	
					<select name='resourceCategoryId' id='resourceCategoryId' size='1'>
						<option value=''>--SELECT--
						<c:forEach items="${sequencingMachines}" var="resourceCategory">
							<c:set value="" var="selected" />
							<c:if test="${resourceCategory.getId() == sequencingCost.getResourceCategory().getId()}">
								<c:set value="SELECTED" var="selected" />
							</c:if>
							<option value='<c:out value="${resourceCategory.getId()}" />' <c:out value="${selected}" /> ><c:out value="${resourceCategory.getName()}" />
						</c:forEach>
					</select>
				</td>				
				<td align='center'><input type='text' style="text-align:right;" size='4' maxlength='4' name='runCostReadLength' id='runCostReadLength' value="${sequencingCost.getReadLength()}"></td>
				<td align='center'><input type='text' style="text-align:right;" size='6' maxlength='6' name='runCostReadType' id='runCostReadType' value="${sequencingCost.getReadType()}"></td>
				<td align='center'><input type='text' style="text-align:right;" size='6' maxlength='6' name='runCostNumberLanes' id='runCostNumberLanes' value="${sequencingCost.getNumberOfLanes()}"></td>
				<td align='center'><c:out value="${localCurrencyIcon}" /><input type='text' style="text-align:right;" size='6' maxlength='6' name='runCostPricePerLane' id='runCostPricePerLane' value="<fmt:formatNumber type="number" groupingUsed="false" maxFractionDigits="0" value="${sequencingCost.getCostPerLane()}" />">.00</td>
				<td align='center'><input type="button" class="delRow" value="Delete Row"/></td>
			</tr>	
		</c:forEach>
	</c:when>
	<c:otherwise>
		<tr>
			<td align='center'>
				<select name='resourceCategoryId' id='resourceCategoryId' size='1'>
					<option value=''>--SELECT--
					<c:forEach items="${sequencingMachines}" var="resourceCategory">						
						<option value='<c:out value="${resourceCategory.getId()}" />' ><c:out value="${resourceCategory.getName()}" />
					</c:forEach>
				</select>
			</td>
			<td align='center'><input type='text' style="text-align:right;" size='4' maxlength='4' name='runCostReadLength' id='runCostReadLength' ></td>
			<td align='center'><input type='text' style="text-align:right;" size='6' maxlength='6' name='runCostReadType' id='runCostReadType'></td>
			<td align='center'><input type='text' style="text-align:right;" size='6' maxlength='6' name='runCostNumberLanes' id='runCostNumberLanes'></td>
			<td align='center'><c:out value="${localCurrencyIcon}" /><input type='text' style="text-align:right;" size='6' maxlength='6' name='runCostPricePerLane' id='runCostPricePerLane' >.00</td>
			<td align='center'><input type="button" class="delRow" value="Delete Row"/></td>
		</tr>	
	</c:otherwise>
	</c:choose>
	<tr><td colspan="6" align="center"><input style="width:300" type="button" class="addRow" value="ADD ADDITIONAL ROW"/></td></tr>
</table>
<br /><br />

<span style='font-weight:bold'>3. Additional Costs Expected For This Job: </span><br /><br />
<table  class="data" style="margin: 0px 0px">
	<tr class="FormData">
		<td class="label-centered" style="background-color:#FAF2D6">Reason</td>
		<td class="label-centered" style="background-color:#FAF2D6">Units</td>
		<td class="label-centered" style="background-color:#FAF2D6">Cost/Unit</td>
		<td class="label-centered" style="background-color:#FAF2D6">Action</td>
	</tr>
	<c:choose>
		<c:when test="${not empty mpsQuote.getAdditionalCosts()}">
			<c:forEach items="${mpsQuote.getAdditionalCosts()}" var="additionalCost">
				<tr>
					<td align='center'><input type='text' size='20' maxlength='44' name='additionalCostReason' id='additionalCostReason' value="${additionalCost.getReason()}"></td>
					<td align='center'><input type='text' style="text-align:right;" size='4' maxlength='4' name='additionalCostUnits' id='additionalCostUnits' value="${additionalCost.getNumberOfUnits()}"></td>
					<td align='center'><c:out value="${localCurrencyIcon}" /><input type='text' style="text-align:right;" size='6' maxlength='6' name='additionalCostPricePerUnit' id='additionalCostPricePerUnit' value="<fmt:formatNumber type="number" groupingUsed="false" maxFractionDigits="0" value="${additionalCost.getCostPerUnit()}" />">.00</td>
					<td align='center'><input type="button" class="delRow" value="Delete Row"/></td>				
				</tr>
			</c:forEach>
		</c:when>
		<c:otherwise>
			<tr>
				<td align='center'><input type='text' size='20' maxlength='44' name='additionalCostReason' id='additionalCostReason'></td>
				<td align='center'><input type='text' style="text-align:right;" size='4' maxlength='4' name='additionalCostUnits' id='additionalCostUnits'></td>
				<td align='center'><c:out value="${localCurrencyIcon}" /><input type='text' style="text-align:right;" size='4' maxlength='4' name='additionalCostPricePerUnit' id='additionalCostPricePerUnit' >.00</td>
				<td align='center'><input type="button" class="delRow" value="Delete Row"/></td>
			</tr>
		</c:otherwise>
	</c:choose>
	<tr><td colspan="4" align="center"><input style="width:300" type="button" class="addRow" value="ADD ADDITIONAL ROW"/></td></tr>
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
	<c:choose>
		<c:when test="${not empty mpsQuote.getDiscounts()}">
			<c:forEach items="${mpsQuote.getDiscounts()}" var="discount">
				<tr>
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
					<td align='center'><input type="button" class="delRow" value="Delete Row"/></td>
				</tr>
			</c:forEach>
		</c:when>
		<c:otherwise>
			<tr>
				<%-- <td align='center'><input onkeydown='robtest_autocomplete(this);'  type='text' size='20' maxlength='44' name='discountReason' id='discountReason'></td>--%>
				<td align='center'>
					<select name='discountReason' id='discountReason' size='1'>
						<option value=''>--SELECT--
							<c:forEach items="${discountReasons}" var="discountReason">
								<option value='<c:out value="${discountReason}" />'  ><c:out value="${discountReason}" />
							</c:forEach>
					</select>
				</td>
				<td align='center'>
					<select name='discountType' id='discountType' size='1'>
					<option value=''>--SELECT--
						<c:forEach items="${discountTypes}" var="discountType">
							<option value='<c:out value="${discountType}" />'  ><c:out value="${discountType}" />
						</c:forEach>
					</select>
				</td>
				<td align='center'><input type='text' style="text-align:right;" size='4' maxlength='4' name='discountValue' id='discountValue'>.00</td>
				<td align='center'><input type="button" class="delRow" value="Delete Row"/></td>
			</tr>
		</c:otherwise>
	</c:choose>	
	<tr><td colspan="5" align="center"><input style="width:300" type="button" class="addRow" value="ADD ADDITIONAL ROW"/></td></tr>
</table>
<sup>*</sup><span style="font-size:small;color:red">Please select any particular discount/credit reason only once</span>
<br /><br />
<span style='font-weight:bold'>5. Comments For This Job: </span><br /><br />
<table  class="data" style="margin: 0px 0px">
	<tr class="FormData">
		<td class="label-centered" style="background-color:#FAF2D6">Comment</td>
		<td class="label-centered" style="background-color:#FAF2D6">Action</td>		
	</tr>
	<c:choose>
		<c:when test="${not empty mpsQuote.getComments()}">
			<c:forEach items="${mpsQuote.getComments()}" var="comment">
				<tr>
					<td align='center'><textarea id="comments" name="comments" cols="60" rows="4" >${comment.getComment()}</textarea><br /></td>
					<td align='center'><input type="button" class="delRow" value="Delete Row"/></td>
				</tr>
			</c:forEach>
		</c:when>
		<c:otherwise>
			<tr>
				<td align='center'><textarea id="comments" name="comments" cols="60" rows="4"></textarea><br /></td>
				<td align='center'><input type="button" class="delRow" value="Delete Row"/></td>
			</tr>
		</c:otherwise>
	</c:choose>	
	<tr><td colspan="2" align="center"><input style="width:300" type="button" class="addRow" value="ADD ADDITIONAL ROW"/></td></tr>
</table>
<br /><br />
<span style="padding:3px; border: 1px solid black;">
<a href="javascript:void(0);" onclick='$("html, body").animate({ scrollTop: 0 }, "fast");' >Return To Top Of Page</a>
| <a <%-- class="button" --%> href="javascript:void(0);" onclick='sendFormViaGetAndShowModlessDialog("quoteOrInvoiceFormId", "<c:url value="/job/${mpsQuote.getJob().getId()}/previewQuote.do" />");' >Preview Quote</a>
| <a <%-- class="button" --%> href="javascript:void(0);" onclick='sendFormViaGetAndShowModlessDialog("quoteOrInvoiceFormId", "<c:url value="/job/${mpsQuote.getJob().getId()}/saveQuote.do" />");' >Save Quote</a>
</span>
<br /><br />
</div>
</div>
</form>
</sec:authorize>
