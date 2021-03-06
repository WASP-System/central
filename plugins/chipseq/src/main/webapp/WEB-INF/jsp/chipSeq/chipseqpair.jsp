<%@ page session="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="wasp" uri="http://einstein.yu.edu/wasp" %>
<script type="text/javascript" src="<wasp:relativeUrl value='scripts/jquery/chosen.jquery.min.js' />"></script>
<head>
<link rel="stylesheet" type="text/css" href="<wasp:relativeUrl value='css/jquery/chosen.css' />">
</head>
<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<c:set var="workflowIName" value="${jobDraft.getWorkflow().getIName()}" />
<h1><fmt:message key="jobDraft.create.label"/> -- <fmt:message key="${workflowIName}.jobsubmit/chipSeq/pair.label"/></h1>

<%@ include file="/WEB-INF/jsp/jobsubmit/jobsubmitinfo.jsp" %>

<div class="instructions">
   <fmt:message key="${workflowIName}.pairing_instructions.label"/>
</div>
<c:choose>
	<c:when test="${ fn:length(ipSamples)==0 || fn:length(inputSamples)==0 || (fn:length(ipSamples) + fn:length(inputSamples))==1 }">
		<br />
		<h2><fmt:message key="chipSeq.pairingNotPossible1.label"/></h2>	
		<h3>&nbsp;&nbsp;&nbsp; * &nbsp;<fmt:message key="chipSeq.pairingNotPossible2.label"/></h3>
		<h3>&nbsp;&nbsp;&nbsp; * &nbsp;<fmt:message key="chipSeq.pairingNotPossible3.label"/></h3>	
		<h3>&nbsp;&nbsp;&nbsp; * &nbsp;<fmt:message key="chipSeq.pairingNotPossible4.label"/></h3>
		<h3>&nbsp;&nbsp;&nbsp; * &nbsp;<fmt:message key="chipSeq.pairingNotPossible5.label"/></h3>	
		<br />
		<h2><fmt:message key="chipSeq.pairingNotPossible6.label"/></h2>
		<div class="submit">
    		<input class="fm-button" type="button" value="<fmt:message key="jobDraft.finishLater.label" />" onClick="window.location='<wasp:relativeUrl value="dashboard.do"/>'" /> 
   			<input class="fm-button" type="button" value="<fmt:message key="jobDraft.continue.label" />" onClick="window.location='<wasp:relativeUrl value="${nextPage}"/>'" />
		</div>
	</c:when>
	<c:otherwise>
	<form name="pairForm" method="POST">
		<table class="EditTable ui-widget ui-widget-content">
			
			<tr>
		 		<td class="CaptionTD top-heading" align="center" colspan="3"><fmt:message key="chipSeq.pair_create_pair.label"/></td>
			</tr>
		  	<tr>
		 		<td class="CaptionTD top-heading" align="center"><fmt:message key="chipSeq.pair_ipsample.label"/></td>
				<td class="CaptionTD top-heading" align="center"><fmt:message key="chipSeq.pair_controlinput.label"/></td>
				<td class="CaptionTD top-heading" align="center"><fmt:message key="chipSeq.pair_action.label"/></td>
			</tr>
			
			
			
			<tr id="oneToMany_tr">
				<td class="DataTD value-centered">					
		  			<select name="ipSampleDraftId[]" id="oneToMany_select_ip" style="width:350px;" class="chosen-select" data-placeholder="<fmt:message key="chipSeq.pair_selectOneIP.label"/>">
		  				<%-- <option value="0"><fmt:message key="chipSeq.pair_selectIP.label"/></option>--%>
		  				<option value="0"></option>
						<c:if test="${fn:length(ipSamples)>1}">
		  					<option value="-1">All IPs (that match selected Input species)</option>
		  				</c:if>
			  			<c:forEach var="ipSample" items="${ipSamples}" >
			  				<option value="<c:out value="${ipSample.id}" />"><c:out value="${ipSample.name}" /> (<c:out value="${sampleSpeciesNameMap.get(ipSample)}" />)</option>	
			  			</c:forEach>
		  			</select>		  			
		  		</td>
		  		<td class="DataTD value-centered">
		  			<select name="inputSampleDraftId[]" id="oneToMany_select_input" style="width:350px;" class="chosen-select" multiple data-placeholder="<fmt:message key="chipSeq.pair_selectOneOrMoreInputs.label"/>">
		  				<%-- <option value="0"><fmt:message key="chipSeq.pair_selectInputControl.label"/></option>--%>
		  				<option value="0"></option>
			  			<c:forEach var="inputSample" items="${inputSamples}" >
			  				<option value="<c:out value="${inputSample.id}" />"><c:out value="${inputSample.name}" /> (<c:out value="${sampleSpeciesNameMap.get(inputSample)}" />)</option>	
			  			</c:forEach>
		  			</select>
		  		</td>
				<td class="DataTD value-centered">
		  			<div class="submit"> 
		    			<input type="submit" onclick='$("#wait_dialog-modal").dialog("open");' value="<fmt:message key="chipSeq.pair_add.label"/>" />
					</div>
		  		</td>
		  	</tr>
		  	
		  	
		  	
	  	
		  	<tr id="manyToOne_tr" >
				<td class="DataTD value-centered">					
		  			<select name="ipSampleDraftId[]" id="manyToOne_select_ip" style="width:350px;" class="chosen-select" multiple data-placeholder="<fmt:message key="chipSeq.pair_selectOneOrMoreIPs.label"/>">
		  				<option value="0"></option>
			  			<c:forEach var="ipSample" items="${ipSamples}" >
			  				<option value="<c:out value="${ipSample.id}" />"><c:out value="${ipSample.name}" /> (<c:out value="${sampleSpeciesNameMap.get(ipSample)}" />)</option>	
			  			</c:forEach>
		  			</select>		  			
		  		</td>
		  		<td class="DataTD value-centered">
		  			<select name="inputSampleDraftId[]" id="manyToOne_select_input" style="width:350px;" class="chosen-select" data-placeholder="<fmt:message key="chipSeq.pair_selectOneInput.label"/>">
		  				<option value="0"></option>
		  				<c:if test="${fn:length(inputSamples)>1}">
		  					<option value="-1">All Inputs (that match selected IP species)</option>
		  				</c:if>
			  			<c:forEach var="inputSample" items="${inputSamples}" >
			  				<option value="<c:out value="${inputSample.id}" />"><c:out value="${inputSample.name}" /> (<c:out value="${sampleSpeciesNameMap.get(inputSample)}" />)</option>	
			  			</c:forEach>
		  			</select>
		  		</td>
				<td class="DataTD value-centered">
		  			<div class="submit"> 
		    			<input  type="submit" onclick='$("#wait_dialog-modal").dialog("open");' value="<fmt:message key="chipSeq.pair_add.label"/>" />
					</div>
		  		</td>
		  	</tr>
		  	
		  	<tr>
		 		<td class="CaptionTD top-heading" style="font-size:x-small; padding-bottom: 10px;" align="center" colspan="3"><a id="toggleAnchorId" href="javascript:void(0);"><fmt:message key="chipSeq.pair_clickHereToToggle.label"/></a><br /></td>
			</tr>
		  	
		  	<c:if test="${not empty alreadyRecordedIPInputListMap}">  	
			  	<tr class="CaptionTD top-heading ">
			 		<td class="CaptionTD top-heading " align="center" colspan="3"><fmt:message key="chipSeq.pair_recorded_pairs.label"/></td>
				</tr>
			  	<tr>
			 		<td class="CaptionTD top-heading " align="center"><fmt:message key="chipSeq.pair_ipsample.label"/></td>
					<td class="CaptionTD top-heading " align="center"><fmt:message key="chipSeq.pair_controlinput.label"/></td>
					<td class="CaptionTD top-heading" align="center">Action</td>
				</tr>
				<c:set value="${1}" var="counter"/>
			  	<c:forEach var="ip" items="${ipSamples}" >	  			  		
			  		<c:set value="${alreadyRecordedIPInputListMap.get(ip) }" var="alreadyRecordedIPInputList"/>
			  		<c:forEach var="input" items="${alreadyRecordedIPInputList}" >
			  			<c:set value="${counter+1}" var="counter"/>
			  			<tr>
			  				<td class="DataTD value-centered <c:if test="${counter % 2 == 0}"> td-even-number</c:if>"  ><c:out value="${ip.name}" /> (<c:out value="${sampleSpeciesNameMap.get(ip)}" />)</td>
			  				<td class="DataTD value-centered<c:if test="${counter % 2 == 0}"> td-even-number</c:if>"><c:out value="${input.name}" /> (<c:out value="${sampleSpeciesNameMap.get(input)}" />) </td>
			  				<td class="DataTD value-centered<c:if test="${counter % 2 == 0}"> td-even-number</c:if>"><a href="<wasp:relativeUrl value="jobsubmit/chipSeq/pair/${jobDraft.getId()}/${ip.getId()}/${input.getId()}/remove.do" />"><fmt:message key="chipSeq.pair_remove.label"/></a></td>
			  			</tr>	  			
			  		</c:forEach>	  		
			  	</c:forEach>
			  	<c:if test="${counter > 2}">
			  		<c:set value="${counter+1}" var="counter"/>
			  			<tr>
			  				<td class="DataTD value-centered <c:if test="${counter % 2 == 0}"> td-even-number</c:if>">&nbsp;</td>
			  				<td class="DataTD value-centered<c:if test="${counter % 2 == 0}"> td-even-number</c:if>">&nbsp;</td>
			  				<td class="DataTD value-centered<c:if test="${counter % 2 == 0}"> td-even-number</c:if>"><a href="<wasp:relativeUrl value="jobsubmit/chipSeq/pair/${jobDraft.getId()}/removeAll.do" />"><fmt:message key="chipSeq.pair_removeAllPairs.label"/></a></td>
			  			</tr>
			  	</c:if>
		  	</c:if>
		</table>
		<div class="submit">
    		<input class="fm-button" type="button" value="<fmt:message key="jobDraft.finishLater.label" />" onClick="window.location='<wasp:relativeUrl value="dashboard.do"/>'" /> 
   			<input name="theContinueButton"  type="submit" value="<fmt:message key="jobDraft.continue.label" />"  /> 
		</div>
	</form>
	</c:otherwise>
</c:choose>
<br />




<%-- 
<div class="instructions">
   <fmt:message key="${workflowIName}.pairing_instructions.label"/>
</div>
<c:choose>
<c:when test="${noPairingPossible == 'true' }">
	<br />
	<h2><fmt:message key="chipSeq.pairingNotPossible1.label"/></h2>	
	<h3>&nbsp;&nbsp;&nbsp; * &nbsp;<fmt:message key="chipSeq.pairingNotPossible2.label"/></h3>
	<h3>&nbsp;&nbsp;&nbsp; * &nbsp;<fmt:message key="chipSeq.pairingNotPossible3.label"/></h3>	
	<h3>&nbsp;&nbsp;&nbsp; * &nbsp;<fmt:message key="chipSeq.pairingNotPossible4.label"/></h3>	
	<br />
	<h2><fmt:message key="chipSeq.pairingNotPossible5.label"/></h2>
	<br />
	<div class="submit">
    	<input class="fm-button" type="button" value="<fmt:message key="jobDraft.finishLater.label" />" onClick="window.location='<wasp:relativeUrl value="dashboard.do"/>'" /> 
    	<input class="fm-button" type="button" value="<fmt:message key="jobDraft.continue.label" />" onClick="window.location='<wasp:relativeUrl value="${nextPage}"/>'" /> 
	</div>
</c:when>
<c:otherwise>
<form method="POST">
<table class="EditTable ui-widget ui-widget-content">
  	<tr >
 		<td class="CaptionTD top-heading" align="center"><fmt:message key="chipSeq.pair_ipsample.label"/></td>
 		<td class="CaptionTD top-heading" align="center"><fmt:message key="chipSeq.pair_ipsample_species.label"/></td>
 		<td class="CaptionTD top-heading" align="center"><fmt:message key="chipSeq.pair_controlinput.label"/></td>
 	</tr>
  	<c:forEach var="ip" items="${ipSamples}" varStatus="status">
  		<tr >
  			<td class="DataTD value-centered <c:if test="${status.count % 2 == 0}"> td-even-number</c:if>">
  				<c:out value="${ip.name}" />
  			</td>
  			<td class="DataTD value-centered <c:if test="${status.count % 2 == 0}"> td-even-number</c:if>">
  				<c:out value="${sampleSpeciesNameMap.get(ip)}" />
  			</td>
  			<td class="DataTD value-centered <c:if test="${status.count % 2 == 0}"> td-even-number</c:if>">
  				<select name="controlIdForIP_<c:out value="${ip.id}" />">
  					<option value="0"><fmt:message key="chipSeq.pair_none.label"/></option>
	  				<c:forEach var="input" items="${inputSamples}" >
	  					<c:if test="${sampleOrganismMap.get(ip) == sampleOrganismMap.get(input) }">
	  						<c:choose>
	  							<c:when test="${not empty selectedTestControlMap.get(ip) && selectedTestControlMap.get(ip).id==input.id}"><option selected value="<c:out value="${input.id}" />"><c:out value="${input.name}" /> (<c:out value="${sampleSpeciesNameMap.get(input)}" />)</option></c:when>
	  							<c:otherwise>	<option value="<c:out value="${input.id}" />"><c:out value="${input.name}" /> (<c:out value="${sampleSpeciesNameMap.get(input)}" />)</option></c:otherwise>
	  						</c:choose>
	  					</c:if>	
	  				</c:forEach>
  				</select>
  			</td>
  		</tr>
 	 </c:forEach>
</table>
<div class="submit">
    <input class="fm-button" type="button" value="<fmt:message key="jobDraft.finishLater.label" />" onClick="window.location='<wasp:relativeUrl value="dashboard.do"/>'" /> 
    <input type="submit" onClick="return confirmPairing();" value="<fmt:message key="jobDraft.continue.label" />" />
</div>
</form>

</c:otherwise>
</c:choose>
--%>


<%-- 
<form method="POST">
<table class="data">
	<tr class="row">
		<td class="label">&nbsp;</td>
		<td colspan="${fn:length(ipSamples) + 1 }" align="center" class="label"><fmt:message key="${workflowIName}.test.label"/></td>
		<td class="noBorder" rowspan="2">&nbsp;</td>
	</tr>
	<tr class="row">
		<td rowspan="${fn:length(inputSamples) + 1 }" valign="middle" class="label"><fmt:message key="${workflowIName}.control.label"/></td>
		<td class="label">&nbsp;</td>
		<c:forEach var="s" items="${ipSamples}">
			<td class="label"><c:out value="${s.name}" /></td>
		</c:forEach>
	</tr>
	<c:forEach var="sControl" items="${inputSamples}" varStatus="statusControl">
		<tr class="row">
			<td class="label"><c:out value="${sControl.name}" /></td>
			<c:forEach var="sTest" items="${ipSamples}" varStatus="statusTest">
				<c:choose>
					<c:when test="${sControl.id == sTest.id }" ><!-- should no longer ever be true; this is for backward compatibility only; generally no longer used -->
						<td class="input-centered" >&nbsp;</td>
					</c:when>
					<c:otherwise>
						<c:choose>
							<c:when test="${sampleOrganismMap.get(sControl) == sampleOrganismMap.get(sTest)}"><!-- confirm sample pairs are from same organism -->
								<td name="rowcolumn_${statusControl.count}_${statusTest.count}" class="input-centered" >
					      			<c:set var="key" value="testVsControl_${sTest.sampleDraftId}_${sControl.sampleDraftId}" />
					      			<c:set var="checked" value="" />
					      			<c:if test="${fn:contains(selectedSamplePairs, key)}">
					        			<c:set var="checked" value="CHECKED" />
					      			</c:if>
					     			<input type="checkbox" value="1" ${checked} name="${key}" id="rowcolumn_${statusControl.count}_${statusTest.count}">
					     		</td>
							</c:when>
							<c:otherwise>
								<td class="input-centered" >&nbsp;</td>
							</c:otherwise>
						</c:choose>
						
					</c:otherwise>
				</c:choose>
			</c:forEach>
			<td class="noBorder"><input id="row_${statusControl.count}_select_all" type="button" value="select all" onclick="toggleRow(${statusControl.count}, ${fn:length(ipSamples)})"></td>
		</tr>
	</c:forEach>
	<tr>
		<td class="noBorder">&nbsp;</td>
		<td class="noBorder">&nbsp;</td>
		<c:forEach begin="1" end="${fn:length(ipSamples)}" step="1" varStatus="status">
			<td><input id="col_${status.count}_select_all" type="button" value="select all" onclick="toggleCol(${status.count}, ${fn:length(inputSamples)})"></td>
		</c:forEach>
		<td class="noBorder">&nbsp;</td>
	</tr>
</table>

<div class="submit">
    <input class="fm-button" type="button" value="<fmt:message key="jobDraft.finishLater.label" />" onClick="window.location='<wasp:relativeUrl value="dashboard.do"/>'" /> 
    <input type="submit" value="<fmt:message key="jobDraft.continue.label" />" />
</div>
</form>
--%>