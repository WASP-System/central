<%@ include file="/WEB-INF/jsp/taglib.jsp"%>



<div id="loading_dialog-modal" title="Page Loading"  >
	<table border="0" cellpadding="5">
	<tr>
	<td><img src="/wasp/images/wasp-illumina/postRunQC/spinner.gif" align="left" border="0" ></td>
	<td>Please be patient while this page loads. All Focus Quality Charts are being pre-loaded so this may take a few seconds.</td>
	</tr>
	</table>
</div>

<div id="selectionWindow">
	<div class="ui-overlay" >
		<div class="ui-widget-shadow ui-corner-all selection_dialog_shadow" ></div>
	</div>
	<div class="selection_dialog ui-widget-content ui-corner-all">
		<div class="ui-widget-header ui-corner-all dialog_header" >Assessment of Lane Focus Quality</div>
		<div  class="verifyQualityForm" class="center">		
			<p>Please click either 'Pass' or 'Fail' for each lane based on your interpretation of the LANE FOCUS QUALITY charts only, then click the 'Continue' button.</p>
			<form id="qualityForm"  method="post">
			<table >
<c:forEach items="${cellIndexList}" var="index" >

				<tr>
					<td class="formLabel vcenter">Lane <c:out value="${index}" /> : </td>
					<td class="vcenter">
						<div id="radioL<c:out value="${index}" />">
							<input type="radio" id="passL<c:out value="${index}" />" name="radioL<c:out value="${index}" />" size="25" value="1" 
								<c:if test="${not empty(existingQcValuesIndexed.get(index).isPassedQc()) && existingQcValuesIndexed.get(index).isPassedQc() == true}"> checked="checked"</c:if> /><label for="passL<c:out value="${index}" />" >Pass</label>
							<input type="radio" id="failL<c:out value="${index}" />" name="radioL<c:out value="${index}" />" size="25" value="0"  
								<c:if test="${(not empty(existingQcValuesIndexed.get(index).isPassedQc())) && existingQcValuesIndexed.get(index).isPassedQc() == false}"> checked="checked"</c:if> /><label for="failL<c:out value="${index}" />" >Fail</label>
						</div>
					</td>
					<td class="formLabel vcenter">Comments: </td>
					<td class="vcenter"><textarea style="resize: none;" name="commentsL<c:out value="${index}" />" rows="3" cols="20" maxlength="100"><c:if test="${not empty(existingQcValuesIndexed.get(index).getComment())}"><c:out value="${existingQcValuesIndexed.get(index).getComment()}" /></c:if></textarea></td>
				</tr>
</c:forEach>
					
				</table>
				</form>
				<br />
				<center><button id="submitForm">Continue</button><button id="cancelForm">Cancel</button></center>
		</div>
	</div>
</div>

<div id="main" class="center">
	<p class="ui-state-default ui-corner-all ui-helper-clearfix qc_title" >
		Illumina OLB Stats: Focus Quality For <c:out value="${runName}" />
	</p>
	<div id="slider_frameH">
		<div id="cycle_number">
			<label for="amountH">Cycle Number: </label>
			<input type="text" id="amountH" size="4" readonly="readonly" value="1"/>
		</div>
		<div id="sliderH" ></div>
		<div id="displayWindow" ><button id="showForm">Continue</button></div>
	</div>
	<div id="intA" class="ui-widget-content ui-corner-all"></div>
	<div id="intC" class="ui-widget-content ui-corner-all"></div>
	<div id="intT" class="ui-widget-content ui-corner-all"></div>
	<div id="intG" class="ui-widget-content ui-corner-all"></div>
	
</div>	

<div id="error_dialog-modal" title="Warning" >
	<p><span class="ui-icon ui-icon-alert alert_icon" ></span><span id="warningText"></span></p>
</div>



<c:forEach items="${imageFileUrlList}" var="fileUrl" varStatus="status" >
	<img src='<c:out value="${fileUrl}" />' alt='Cycle <c:out value="${status.count}" /> FWHM Plot <c:out value="${fileUrl}" />' class='preloadHidden' />
</c:forEach>
