<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
<h1>
<fmt:message key="reports.feesCharged.label" />
</h1>

<form method="POST">
<!--  jquery datepicker (see run/list)-->
<div >
	<table class="EditTable ui-widget ui-widget-content">
	<tr class="FormData"><td  class="CaptionTD"><p>Start Date: <input type="text" id="datepicker"></p></td></tr>
	<tr class="FormData"><td >&nbsp;</td></tr>	
	</table>
</div>
<br />

  <div class="submit">
    <input class="fm-button" type="submit" value="Run Report" />
  </div>
</form>
	<br />
	
<!--  		
<table class="data" style="margin: 0px 0px" >	
    <tr class="FormData">
		<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;"><label>Job ID</label></td> 
		<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;"><label>Submitter<br />PI</label></td> 
		<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;"><label>Total Charge</label></td> 
		<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;"><label>Initial Seq. Facility Charge</label></td> 
		<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;"><label>Discount</label></td> 
		<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;"><label>Discounted Seq. Facility Charge</label></td> 
		<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;"><label>Computational Charge</label></td> 
	</tr>
	<c:forEach items="${jobs}" var="job" varStatus="counter">
		<c:set value="${jobMPSQuoteMap.get(job)}" var="mostRecentMpsQuote"/>
		<c:set value="${jobPIMap.get(job)}" var="PI"/>
		<tr class="FormData">
			<td class="DataTD"  style="text-align:center; white-space:nowrap;">
				<a href="<wasp:relativeUrl value="job/${job.getId()}/homepage.do" />">J<c:out value="${job.getId()}" /></a>
			</td>
			<td class="DataTD"  style="text-align:center; white-space:nowrap;"><c:out value="${job.getUser().getNameFstLst()}" /><br /><c:out value="${PI.getNameFstLst()}" /></td>
			<td class="DataTD"  style="text-align:center; white-space:nowrap;"><c:out value="${localCurrencyIcon}" /> <fmt:formatNumber type="number" groupingUsed="false" maxFractionDigits="0" value="${mostRecentMpsQuote.getTotalFinalCost()}" /></td>
			<td class="DataTD"  style="text-align:center; white-space:nowrap;"><c:out value="${localCurrencyIcon}" /> <fmt:formatNumber type="number" groupingUsed="false" maxFractionDigits="0" value="${mostRecentMpsQuote.getTotalLibraryConstructionCost() + mostRecentMpsQuote.getTotalSequenceRunCost() +  mostRecentMpsQuote.getTotalAdditionalCost() }" /></td>
			<td class="DataTD"  style="text-align:center; white-space:nowrap;">(<c:out value="${localCurrencyIcon}" /> <fmt:formatNumber type="number" groupingUsed="false" maxFractionDigits="0" value="${mostRecentMpsQuote.getTotalDiscountCost()}" />)</td>
			<td class="DataTD"  style="text-align:center; white-space:nowrap;"><c:out value="${localCurrencyIcon}" /> <fmt:formatNumber type="number" groupingUsed="false" maxFractionDigits="0" value="${mostRecentMpsQuote.getTotalLibraryConstructionCost() + mostRecentMpsQuote.getTotalSequenceRunCost() +  mostRecentMpsQuote.getTotalAdditionalCost() - mostRecentMpsQuote.getTotalDiscountCost()}" /></td>
			<td class="DataTD"  style="text-align:center; white-space:nowrap;"><c:out value="${localCurrencyIcon}" /> <fmt:formatNumber type="number" groupingUsed="false" maxFractionDigits="0" value="${mostRecentMpsQuote.getTotalComputationalCost() }" /></td>
		</tr>
	</c:forEach>
</table>
			

			
<table class="data" style="margin: 0px 0px" >	
    <tr class="FormData">
    	<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;"><label>PI</label></td>
    	<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;"><label>Submitter</label></td>
		<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;"><label>Job ID</label></td> 
		<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;"><label>Total Charge</label></td> 
		<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;"><label>Initial Seq. Facility Charge</label></td> 
		<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;"><label>Discount</label></td> 
		<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;"><label>Discounted Seq. Facility Charge</label></td> 
		<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;"><label>Computational Charge</label></td> 
	</tr>
	<c:forEach items="${jobs}" var="job" varStatus="counter">
		<c:set value="${jobMPSQuoteMap.get(job)}" var="mostRecentMpsQuote"/>
		<c:set value="${jobMPSQuoteAsIntegerListMap.get(job)}" var="mPSQuoteAsIntegerList"/>
		<c:set value="${jobPIMap.get(job)}" var="PI"/>
		<tr class="FormData">
			<td class="DataTD"  style="text-align:center; white-space:nowrap;"><c:out value="${PI.getNameLstCmFst()}" /></td>
			<td class="DataTD"  style="text-align:center; white-space:nowrap;"><c:out value="${job.getUser().getNameLstCmFst()}" /></td>			
			<td class="DataTD"  style="text-align:center; white-space:nowrap;">
				<a href="<wasp:relativeUrl value="job/${job.getId()}/homepage.do" />">J<c:out value="${job.getId()}" /></a>
			</td>
			<td class="DataTD"  style="text-align:center; white-space:nowrap;"><c:out value="${localCurrencyIcon}" /> <fmt:formatNumber type="number" groupingUsed="false" maxFractionDigits="0" value="${mPSQuoteAsIntegerList.get(0)}" /></td>
			<td class="DataTD"  style="text-align:center; white-space:nowrap;"><c:out value="${localCurrencyIcon}" /> <fmt:formatNumber type="number" groupingUsed="false" maxFractionDigits="0" value="${mPSQuoteAsIntegerList.get(1) }" /></td>
			<td class="DataTD"  style="text-align:center; white-space:nowrap;">(<c:out value="${localCurrencyIcon}" /> <fmt:formatNumber type="number" groupingUsed="false" maxFractionDigits="0" value="${mPSQuoteAsIntegerList.get(2)}" />)</td>
			<td class="DataTD"  style="text-align:center; white-space:nowrap;"><c:out value="${localCurrencyIcon}" /> <fmt:formatNumber type="number" groupingUsed="false" maxFractionDigits="0" value="${mPSQuoteAsIntegerList.get(3)}" /></td>
			<td class="DataTD"  style="text-align:center; white-space:nowrap;"><c:out value="${localCurrencyIcon}" /> <fmt:formatNumber type="number" groupingUsed="false" maxFractionDigits="0" value="${mPSQuoteAsIntegerList.get(4) }" /></td>
		</tr>
	</c:forEach>
</table>	

-->
<br />
<c:if test="${not empty  labList}">
<table class="data" style="margin: 0px 0px" >	

	<tr class="FormData"><td colspan="9" class="label-centered" style="height:2px;background-color:black; white-space:nowrap;"></td></tr>
	<tr class="FormData">
    	<td colspan="9" class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;"><label>Report Summary</label></td>
	<tr class="FormData">
    	<td colspan="4" class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;"><label></label></td>
    	<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;"><label>Total Charge</label></td> 
		<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;"><label>Initial Seq. Facility Charge</label></td> 
		<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;"><label>Discount</label></td> 
		<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;"><label>Discounted Seq. Facility Charge</label></td> 
		<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;"><label>Computational Charge</label></td> 
	</tr>	
	<tr class="FormData">
		<td colspan="4"  class="DataTD"  style="text-align:center; white-space:nowrap;background-color:LightGray"></td>
		<td class="DataTD"  style="text-align:center; white-space:nowrap;background-color:LightGray"><c:out value="${localCurrencyIcon}" /> <fmt:formatNumber type="number" groupingUsed="false" maxFractionDigits="0" value="${reportGrandTotalsAsIntegerList.get(0)}" /></td>
		<td class="DataTD"  style="text-align:center; white-space:nowrap;background-color:LightGray"><c:out value="${localCurrencyIcon}" /> <fmt:formatNumber type="number" groupingUsed="false" maxFractionDigits="0" value="${reportGrandTotalsAsIntegerList.get(1) }" /></td>
		<td class="DataTD"  style="text-align:center; white-space:nowrap;background-color:LightGray">(<c:out value="${localCurrencyIcon}" /> <fmt:formatNumber type="number" groupingUsed="false" maxFractionDigits="0" value="${reportGrandTotalsAsIntegerList.get(2)}" />)</td>
		<td class="DataTD"  style="text-align:center; white-space:nowrap;background-color:LightGray"><c:out value="${localCurrencyIcon}" /> <fmt:formatNumber type="number" groupingUsed="false" maxFractionDigits="0" value="${reportGrandTotalsAsIntegerList.get(3)}" /></td>
		<td class="DataTD"  style="text-align:center; white-space:nowrap;background-color:LightGray"><c:out value="${localCurrencyIcon}" /> <fmt:formatNumber type="number" groupingUsed="false" maxFractionDigits="0" value="${reportGrandTotalsAsIntegerList.get(4) }" /></td>
	</tr>
	<tr class="FormData"><td colspan="9" class="label-centered" style="height:2px;background-color:black; white-space:nowrap;"></td></tr>
	
 	<c:forEach items="${labList}" var="lab" >
	
		 <tr class="FormData">
	    	<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;"><label>PI</label></td>
	    	<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;"><label>Submitter</label></td>
			<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;"><label>Job ID</label></td>
			<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;"><label>Workflow<br />Submitted<br />Completed</label></td> 
			<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;"><label>Total Charge</label></td> 
			<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;"><label>Initial Seq. Facility Charge</label></td> 
			<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;"><label>Discount</label></td> 
			<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;"><label>Discounted Seq. Facility Charge</label></td> 
			<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;"><label>Computational Charge</label></td> 
		</tr>
		
		<c:set value="${labJobListMap.get(lab)}" var="jobList"/>
		
		<c:forEach items="${jobList}" var="job" varStatus="counter">
		
			<c:set value="${jobMPSQuoteMap.get(job)}" var="mostRecentMpsQuote"/>
		
			<c:set value="${jobMPSQuoteAsIntegerListMap.get(job)}" var="mPSQuoteAsIntegerList"/>
			<c:set value="${jobPIMap.get(job)}" var="PI"/>
			<tr class="FormData">
				<td class="DataTD"  style="text-align:center; white-space:nowrap;"><c:out value="${PI.getNameLstCmFst()}" /></td>
				<td class="DataTD"  style="text-align:center; white-space:nowrap;"><c:out value="${job.getUser().getNameLstCmFst()}" /></td>			
				<td class="DataTD"  style="text-align:center; white-space:nowrap;">
					<a href="<wasp:relativeUrl value="job/${job.getId()}/homepage.do" />">J<c:out value="${job.getId()}" /></a>
				</td>
				<td class="DataTD"  style="text-align:center; white-space:nowrap;"><c:out value="${job.getWorkflow().getName()}" /><br /><c:out value="${jobStartDateAsStringMap.get(job)}" /><br /><c:out value="${jobCompletionDateAsStringMap.get(job)}" /></td>			
				<td class="DataTD"  style="text-align:center; white-space:nowrap;"><c:out value="${localCurrencyIcon}" /> <fmt:formatNumber type="number" groupingUsed="false" maxFractionDigits="0" value="${mPSQuoteAsIntegerList.get(0)}" /></td>
				<td class="DataTD"  style="text-align:center; white-space:nowrap;"><c:out value="${localCurrencyIcon}" /> <fmt:formatNumber type="number" groupingUsed="false" maxFractionDigits="0" value="${mPSQuoteAsIntegerList.get(1) }" /></td>
				<td class="DataTD"  style="text-align:center; white-space:nowrap;">(<c:out value="${localCurrencyIcon}" /> <fmt:formatNumber type="number" groupingUsed="false" maxFractionDigits="0" value="${mPSQuoteAsIntegerList.get(2)}" />)</td>
				<td class="DataTD"  style="text-align:center; white-space:nowrap;"><c:out value="${localCurrencyIcon}" /> <fmt:formatNumber type="number" groupingUsed="false" maxFractionDigits="0" value="${mPSQuoteAsIntegerList.get(3)}" /></td>
				<td class="DataTD"  style="text-align:center; white-space:nowrap;"><c:out value="${localCurrencyIcon}" /> <fmt:formatNumber type="number" groupingUsed="false" maxFractionDigits="0" value="${mPSQuoteAsIntegerList.get(4) }" /></td>
			</tr>
		</c:forEach>
		
		<c:set value="${labGrandTotalsAsIntegerListMap.get(lab)}" var="grandTotalsForLabAsIntegerList"/>
		<tr class="FormData">
			<td class="DataTD"  style="text-align:center; white-space:nowrap;background-color:LightGray"><c:out value="${PI.getNameLstCmFst()}" /></td>
			<td class="DataTD"  style="text-align:center; white-space:nowrap;background-color:LightGray"></td>			
			<td class="DataTD"  style="text-align:center; white-space:nowrap;background-color:LightGray"></td>
			<td class="DataTD"  style="text-align:center; white-space:nowrap;background-color:LightGray"></td>
			<td class="DataTD"  style="text-align:center; white-space:nowrap;background-color:LightGray"><c:out value="${localCurrencyIcon}" /> <fmt:formatNumber type="number" groupingUsed="false" maxFractionDigits="0" value="${grandTotalsForLabAsIntegerList.get(0)}" /></td>
			<td class="DataTD"  style="text-align:center; white-space:nowrap;background-color:LightGray"><c:out value="${localCurrencyIcon}" /> <fmt:formatNumber type="number" groupingUsed="false" maxFractionDigits="0" value="${grandTotalsForLabAsIntegerList.get(1) }" /></td>
			<td class="DataTD"  style="text-align:center; white-space:nowrap;background-color:LightGray">(<c:out value="${localCurrencyIcon}" /> <fmt:formatNumber type="number" groupingUsed="false" maxFractionDigits="0" value="${grandTotalsForLabAsIntegerList.get(2)}" />)</td>
			<td class="DataTD"  style="text-align:center; white-space:nowrap;background-color:LightGray"><c:out value="${localCurrencyIcon}" /> <fmt:formatNumber type="number" groupingUsed="false" maxFractionDigits="0" value="${grandTotalsForLabAsIntegerList.get(3)}" /></td>
			<td class="DataTD"  style="text-align:center; white-space:nowrap;background-color:LightGray"><c:out value="${localCurrencyIcon}" /> <fmt:formatNumber type="number" groupingUsed="false" maxFractionDigits="0" value="${grandTotalsForLabAsIntegerList.get(4) }" /></td>
		</tr>
	</c:forEach>
	
	<tr class="FormData"><td colspan="9" class="label-centered" style="height:2px;background-color:black; white-space:nowrap;"></td></tr>
	<tr class="FormData">
    	<td colspan="9" class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;"><label>Report Summary</label></td>
	<tr class="FormData">
    	<td colspan="4" class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;"><label></label></td>
    	<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;"><label>Total Charge</label></td> 
		<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;"><label>Initial Seq. Facility Charge</label></td> 
		<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;"><label>Discount</label></td> 
		<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;"><label>Discounted Seq. Facility Charge</label></td> 
		<td class="label-centered" style="background-color:#FAF2D6; white-space:nowrap;"><label>Computational Charge</label></td> 
	</tr>	
	<tr class="FormData">
		<td colspan="4"  class="DataTD"  style="text-align:center; white-space:nowrap;background-color:LightGray"></td>
		<td class="DataTD"  style="text-align:center; white-space:nowrap;background-color:LightGray"><c:out value="${localCurrencyIcon}" /> <fmt:formatNumber type="number" groupingUsed="false" maxFractionDigits="0" value="${reportGrandTotalsAsIntegerList.get(0)}" /></td>
		<td class="DataTD"  style="text-align:center; white-space:nowrap;background-color:LightGray"><c:out value="${localCurrencyIcon}" /> <fmt:formatNumber type="number" groupingUsed="false" maxFractionDigits="0" value="${reportGrandTotalsAsIntegerList.get(1) }" /></td>
		<td class="DataTD"  style="text-align:center; white-space:nowrap;background-color:LightGray">(<c:out value="${localCurrencyIcon}" /> <fmt:formatNumber type="number" groupingUsed="false" maxFractionDigits="0" value="${reportGrandTotalsAsIntegerList.get(2)}" />)</td>
		<td class="DataTD"  style="text-align:center; white-space:nowrap;background-color:LightGray"><c:out value="${localCurrencyIcon}" /> <fmt:formatNumber type="number" groupingUsed="false" maxFractionDigits="0" value="${reportGrandTotalsAsIntegerList.get(3)}" /></td>
		<td class="DataTD"  style="text-align:center; white-space:nowrap;background-color:LightGray"><c:out value="${localCurrencyIcon}" /> <fmt:formatNumber type="number" groupingUsed="false" maxFractionDigits="0" value="${reportGrandTotalsAsIntegerList.get(4) }" /></td>
	</tr>
</table>
<br /><br />

</c:if>












