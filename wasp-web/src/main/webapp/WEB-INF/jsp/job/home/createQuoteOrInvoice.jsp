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
<div class="ui-widget">
<div id="container_div_for_adding_rows" >

<br />
<a class="button" href="javascript:void(0);" onclick='showSmallModalessDialog("<c:url value="/job/${job.getId()}/basic.do" />");' >View Basic Request</a>
<a class="button" href="javascript:void(0);" onclick='showSmallModalessDialog("<c:url value="/job/${job.getId()}/requests.do?onlyDisplayCellsRequested=true" />");' >View Lane Request</a>
<a class="button" href="javascript:void(0);" onclick='loadNewPageWithAjax("<c:url value="/job/${job.getId()}/costManager.do" />");' >Return To Costs Page</a>
<a class="button" href="javascript:void(0);" onclick='showModalessDialog("<c:url value="/job/${job.getId()}/previewQuote.do" />");' >Preview Quote</a>
<a class="button" href="javascript:void(0);" onclick='showModalessDialog("<c:url value="/job/${job.getId()}/saveQuote.do" />");' >Save Quote</a>
<br /><br />

<span style='font-weight:bold'>1. Additional Submitted Sample Costs Expected For This Job: </span><br /><br />
<table  class="data" style="margin: 0px 0px">
	<tr class="FormData">
		<td class="label-centered" style="background-color:#FAF2D6">Reason</td>
		<td class="label-centered" style="background-color:#FAF2D6">No. Submitted Samples</td>
		<td class="label-centered" style="background-color:#FAF2D6">Cost/Submitted Sample</td>
		<td class="label-centered" style="background-color:#FAF2D6">Action</td>
	</tr>
	<tr><td align='center'><input type='text' size='20' maxlength='44' name='additionalSubmittedSampleCostReason' id='additionalSubmittedSamplesReason'></td>
	<td align='center'><input type='text' style="text-align:right;" size='4' maxlength='4' name='additionalSubmittedSampleCostNumSubmittedSamples' id='additionalSubmittedSampleCostNumSubmittedSamples'></td>
	<td align='center'><c:out value="${localCurrencyIcon}" /><input type='text' style="text-align:right;" size='4' maxlength='4' name='additionalSubmittedSamplesCostPricePerUnit' id='additionalSubmittedSamplesCostPricePerUnit' >.00</td>
	<td align='center'><input type="button" class="delRow" value="Delete Row"/></td>
</tr>
<tr><td colspan="4" align="center"><input style="width:300" type="button" class="addRow" value="ADD ADDITIONAL ROW"/></td></tr>
</table>
<br /><br />
<c:set value="${fn:length(submittedMacromoleculeList)}" var="numberOfLibrariesThatShouldBeConstructed" />
<span style='font-weight:bold'>2. Library Constructions Expected For This Job: <c:out value="${numberOfLibrariesThatShouldBeConstructed}" /></span><br /><br />
<table class="data" style="margin: 0px 0px">
	<tr class="FormData">
		<td class="label-centered" style="background-color:#FAF2D6">Number</td>
		<td class="label-centered" style="background-color:#FAF2D6">Submitted Sample</td>
		<td class="label-centered" style="background-color:#FAF2D6">Material</td>
		<td class="label-centered" style="background-color:#FAF2D6">Library Cost</td>
	</tr>
	<c:forEach items="${submittedObjectList}" var="submittedObject" varStatus="statusSubmittedObject">
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
						<c:out value="${localCurrencyIcon}" /><input style="text-align:right;" name="libraryCost_${submittedObject.getId()}" type="text" maxlength="4" size="4" name="costPerLibrary_${submittedObject.getId()}" />.00
					</c:when>
					<c:otherwise>
						N/A
					</c:otherwise>
				</c:choose>
			</td>
		</tr>
	</c:forEach>
	
</table>
<br /><br />
<span style='font-weight:bold'>3. Additional Library Costs Expected For This Job: </span><br /><br />
<table  class="data" style="margin: 0px 0px">
	<tr class="FormData">
		<td class="label-centered" style="background-color:#FAF2D6">Reason</td>
		<td class="label-centered" style="background-color:#FAF2D6">No. Libraries</td>
		<td class="label-centered" style="background-color:#FAF2D6">Cost/Library</td>
		<td class="label-centered" style="background-color:#FAF2D6">Action</td>
	</tr>
	<tr><td align='center'><input type='text' size='20' maxlength='44' name='additionalLibCostReason' id='additionalLibCostReason'></td>
	<td align='center'><input type='text' style="text-align:right;" size='4' maxlength='4' name='additionalLibCostNumLib' id='additionalLibCostNumLib'></td>
	<td align='center'><c:out value="${localCurrencyIcon}" /><input type='text' style="text-align:right;" size='4' maxlength='4' name='additionalLibCostPricePerUnit' id='additionalLibCostPricePerUnit' >.00</td>
	<td align='center'><input type="button" class="delRow" value="Delete Row"/></td>
</tr>
<tr><td colspan="4" align="center"><input style="width:300" type="button" class="addRow" value="ADD ADDITIONAL ROW"/></td></tr>
</table>
<br /><br />
<span style='font-weight:bold'>4. Sequencing Lanes Expected For This Job: <c:out value="${numberOfLanesRequested}" /></span><br /><br />
<table  class="data" style="margin: 0px 0px">
	<tr class="FormData">
		<td class="label-centered" style="background-color:#FAF2D6">Machine</td>
		<td class="label-centered" style="background-color:#FAF2D6">ReadLength</td>
		<td class="label-centered" style="background-color:#FAF2D6">ReadType</td>
		<td class="label-centered" style="background-color:#FAF2D6">No. Lanes</td>
		<td class="label-centered" style="background-color:#FAF2D6">Cost/Lane</td>
		<td class="label-centered" style="background-color:#FAF2D6">Action</td>
	</tr>
	<tr><td align='center'><input type='text' size='20' maxlength='44' name='runsMachine' id='runsMachine'></td>
	<td align='center'><input type='text' style="text-align:right;" size='4' maxlength='4' name='runsReadLength' id='runsReadLength'></td>
	<td align='center'><input type='text' style="text-align:right;" size='6' maxlength='6' name='runsReadType' id='runsReadType'></td>
	<td align='center'><input type='text' style="text-align:right;" size='6' maxlength='6' name='runsNumberLanes' id='runsNumberLanes'></td>
	<td align='center'><c:out value="${localCurrencyIcon}" /><input type='text' style="text-align:right;" size='6' maxlength='6' name='runsPricePerUnit' id='runsPricePerUnit' >.00</td>
	<td align='center'><input type="button" class="delRow" value="Delete Row"/></td>
</tr>
<tr><td colspan="6" align="center"><input style="width:300" type="button" class="addRow" value="ADD ADDITIONAL ROW"/></td></tr>
</table>
<br /><br />
<span style='font-weight:bold'>5. Additional Sequencing Costs Expected For This Job: </span><br /><br />
<table  class="data" style="margin: 0px 0px">
	<tr class="FormData">
		<td class="label-centered" style="background-color:#FAF2D6">Reason</td>
		<td class="label-centered" style="background-color:#FAF2D6">No. Lanes</td>
		<td class="label-centered" style="background-color:#FAF2D6">Cost/Lane</td>
		<td class="label-centered" style="background-color:#FAF2D6">Action</td>
	</tr>
	<tr><td align='center'><input type='text' size='20' maxlength='44' name='additionalRunsCostReason' id='additionalRunsCostReason'></td>
	<td align='center'><input type='text' style="text-align:right;" size='4' maxlength='4' name='additionalRunsCostNum' id='additionalRunsCostNum'></td>
	<td align='center'><c:out value="${localCurrencyIcon}" /><input type='text' style="text-align:right;" size='4' maxlength='4' name='additionalRunsCostPricePerUnit' id='additionalRunsCostPricePerUnit' >.00</td>
	<td align='center'><input type="button" class="delRow" value="Delete Row"/></td>
</tr>
<tr><td colspan="4" align="center"><input style="width:300" type="button" class="addRow" value="ADD ADDITIONAL ROW"/></td></tr>
</table>
<br /><br />
<span style='font-weight:bold'>5. Discounts Expected For This Job: </span><br /><br />
<table  class="data" style="margin: 0px 0px">
	<tr class="FormData">
		<td class="label-centered" style="background-color:#FAF2D6">Reason</td>
		<td class="label-centered" style="background-color:#FAF2D6">Reason</td>
		<td class="label-centered" style="background-color:#FAF2D6"><c:out value="${localCurrencyIcon}" /> Or %</td>
		<td class="label-centered" style="background-color:#FAF2D6">Discount</td>
		<td class="label-centered" style="background-color:#FAF2D6">Action</td>
	</tr>
	<tr>
		<td align='center'><input onkeydown='robtest_autocomplete(this);'  type='text' size='20' maxlength='44' name='discountReason' id='discountReason'></td>
		<td align='center'><select name='discountReason123' id='discountReason123' size='1'><option value=''>--SELECT--<option value='Institutional Cost Share'>Institutional Cost Share<option value='Departmental Cost Share'>Departmental Cost Share<option value='Center Cost Share'>Center Cost Share<option value='Facility Credit'>Facility Credit<option value='Departmental Cost Share'>Facility Discount</select></td>
		<td align='center'><select name='discountType' id='discountType' size='1'><option value=''>--SELECT--<option value='%'>%<option value='<c:out value="${localCurrencyIcon}" />'><c:out value="${localCurrencyIcon}" /></select></td>
		<td align='center'><input type='text' style="text-align:right;" size='4' maxlength='4' name='discountValue' id='discountValue'>.00</td>
		<td align='center'><input type="button" class="delRow" value="Delete Row"/></td>
	</tr>
	<tr><td colspan="5" align="center"><input style="width:300" type="button" class="addRow" value="ADD ADDITIONAL ROW"/></td></tr>
</table>
<%-- <select name='user_conc_by_fluorometry[]' id='user_conc_by_fluorometry[]' size='1'><option value=''>--SELECT--<option value='YES'>YES <option value='NO'>NO </select>
<table >
	<tr class="FormData">
		<td colspan="2" class="label-centered" style="background-color:#FAF2D6">Library Construction</td>
	</tr>
	<tr>
		<td class="DataTD value-centered">Number Of Library Constructions</td>
		<td class="DataTD value-centered" ><input style="text-align:right;" type="text" maxlength="4" size="4" name="numberOfLibraryConstructions" /></td>
	</tr>
	<tr>
		<td class="DataTD value-centered">Cost Per Library (<c:out value="${localCurrencyIcon}" />)</td>
		<td class="DataTD value-centered" ><input style="text-align:right;" type="text" maxlength="4" size="4" name="costPerLibrary" /></td>
	</tr>
</table>
<br /><br />

<br /><br />
<table  border="1">
<tr><th style='text-align:center; background:#CCCCCC'>Library Name<sup>*</sup></th><th style='text-align:center; background:#CCCCCC'>Library Size<sup>**</sup> (bp)</th><th style='text-align:center; background:#CCCCCC'>Conc. (ng/&micro;l)</th><th style='text-align:center; background:#CCCCCC'>Conc. By Fluorometry?<sup>***</sup></th><th style='text-align:center; background:#CCCCCC'>Volume (&micro;l)</th><th style='text-align:center; background:#CCCCCC'>Buffer</th><th style='text-align:center; background:#CCCCCC'>Action</th></tr>
<tr><td align='center'><input type='text' size='20' maxlength='44' name='library_name[]' id='library_name[]'></td>
	<td align='center'><input type='text' size='12' maxlength='20' name='library_size[]' id='library_size[]'></td>
	<td align='center'><input type='text' size='10' maxlength='10' name='concentration[]' id='concentration[]'  onKeyPress='return nan_to_null(this,event)'></td>
	<td align='center'><select name='user_conc_by_fluorometry[]' id='user_conc_by_fluorometry[]' size='1'><option value=''>--SELECT--<option value='YES'>YES <option value='NO'>NO </select></td>
	<td align='center'><input type='text' size='8' maxlength='10' name='volume[]' id='volume[]' onKeyPress='return nan_to_null(this,event)'></td>
	<td align='center'><input type='text' size='8' maxlength='20' name='buffer[]' id='buffer[]'></td>
	<td align='center'><input type="button" class="delRow" value="Delete Row"/></td>
</tr>
<tr><td colspan="7" align="center"><input style="width:300" type="button" class="addRow" value="ADD ANOTHER LIBRARY"/></td></tr>
</table>
<br />
<table  border="1">
<tr><th style='text-align:center; background:#CCCCCC'>Library Name<sup>*</sup></th><th style='text-align:center; background:#CCCCCC'>Library Size<sup>**</sup> (bp)</th><th style='text-align:center; background:#CCCCCC'>Conc. (ng/&micro;l)</th><th style='text-align:center; background:#CCCCCC'>Conc. By Fluorometry?<sup>***</sup></th><th style='text-align:center; background:#CCCCCC'>Volume (&micro;l)</th><th style='text-align:center; background:#CCCCCC'>Buffer</th><th style='text-align:center; background:#CCCCCC'>Action</th></tr>
<tr><td align='center'><input type='text' size='20' maxlength='44' name='library_name[]' id='library_name[]'></td>
	<td align='center'><input type='text' size='12' maxlength='20' name='library_size[]' id='library_size[]'></td>
	<td align='center'><input type='text' size='10' maxlength='10' name='concentration[]' id='concentration[]'  onKeyPress='return nan_to_null(this,event)'></td>
	<td align='center'><select name='user_conc_by_fluorometry[]' id='user_conc_by_fluorometry[]' size='1'><option value=''>--SELECT--<option value='YES'>YES <option value='NO'>NO </select></td>
	<td align='center'><input type='text' size='8' maxlength='10' name='volume[]' id='volume[]' onKeyPress='return nan_to_null(this,event)'></td>
	<td align='center'><input type='text' size='8' maxlength='20' name='buffer[]' id='buffer[]'></td>
	<td align='center'><input type="button" class="delRow" value="Delete Row"/></td>
</tr>
<tr><td colspan="7" align="center"><input style="width:300" type="button" class="addRow" value="ADD ANOTHER LIBRARY"/></td></tr>
</table>
--%>
</div>
</div>
</sec:authorize>