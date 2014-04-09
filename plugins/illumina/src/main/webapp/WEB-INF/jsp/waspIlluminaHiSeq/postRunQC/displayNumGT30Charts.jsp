<%@ page session="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="wasp" uri="http://einstein.yu.edu/wasp" %>

<%--  TODO: Declare style in css file (e.g. /src/main/webapp/css/base.css), not in .jsp and reuse where possible !!!! --%>

<div id="loading_dialog-modal" title="<fmt:message key="waspIlluminaPlugin.displayQc_loadingTitle.label" />"  >
	<table border="0" cellpadding="5">
	<tr>
	<td><img src="<wasp:relativeUrl value='images/spinner.gif' />" align="left" border="0" ></td>
	<td><fmt:message key="waspIlluminaPlugin.displayQc_message.label" /></td>
	</tr>
	</table>
</div>

<div id="selectionWindow">
	<div class="ui-overlay" >
		<div class="ui-widget-shadow ui-corner-all selection_dialog_shadow" ></div>
	</div>
	<div class="selection_dialog ui-widget-content ui-corner-all">
		<div class="ui-widget-header ui-corner-all dialog_header" ><fmt:message key="waspIlluminaPlugin.numGT30Qc_dialogTitle.label" /></div>
		<div  class="verifyQualityForm" class="center">		
			<p><fmt:message key="waspIlluminaPlugin.numGT30Qc_dialogMessage.label" /></p>
			<form id="qualityForm"  method="post">
			<table >
<c:forEach items="${cellIndexList}" var="index" >

				<tr>
					<td class="formLabel vcenter"><fmt:message key="waspIlluminaPlugin.displayQc_lane.label" /><c:out value="${index}" /> : </td>
					<td class="vcenter">
						<div class="radio-jquery-ui">
							<input type="radio" id="passL<c:out value="${index}" />" name="radioL<c:out value="${index}" />" size="25" value="1" 
								<c:if test="${not empty(existingQcValuesIndexed) && not empty(existingQcValuesIndexed.get(index)) && existingQcValuesIndexed.get(index).isPassedQc() == true}"> checked="checked"</c:if> /><label for="passL<c:out value="${index}" />" ><fmt:message key="waspIlluminaPlugin.displayQc_pass.label" /></label>
							<input type="radio" id="failL<c:out value="${index}" />" name="radioL<c:out value="${index}" />" size="25" value="0"  
								<c:if test="${not empty(existingQcValuesIndexed) && not empty(existingQcValuesIndexed.get(index)) && existingQcValuesIndexed.get(index).isPassedQc() == false}"> checked="checked"</c:if> /><label for="failL<c:out value="${index}" />" ><fmt:message key="waspIlluminaPlugin.displayQc_fail.label" /></label>
						</div>
					</td>
					<td class="formLabel vcenter"><fmt:message key="waspIlluminaPlugin.displayQc_comments.label" /></td>
					<td class="vcenter"><textarea style="resize: none;" name="commentsL<c:out value="${index}" />" rows="3" cols="20" maxlength="100"><c:if test="${not empty(existingQcValuesIndexed) && not empty(existingQcValuesIndexed.get(index))}"><c:out value="${existingQcValuesIndexed.get(index).getComment()}" /></c:if></textarea></td>
				</tr>
</c:forEach>
					
				</table>
				</form>
				<br />
				<center><button id="submitForm"><fmt:message key="waspIlluminaPlugin.displayQc_continue.label" /></button><button id="cancelForm"><fmt:message key="waspIlluminaPlugin.displayQc_cancel.label" /></button></center>
		</div>
	</div>
</div>

<div id="main" class="center">
	<p class="ui-state-default ui-corner-all ui-helper-clearfix qc_title" >
		<span class="ui-icon ui-icon-info infoIcon"></span>
		<fmt:message key="waspIlluminaPlugin.numGT30Qc_title.label" /><c:out value="${runName}" />
	</p>
	<div id="displayWindow" style="float:right;"><button id="showForm"><fmt:message key="waspIlluminaPlugin.displayQc_continue.label" /></button></div>
	<div id="slider_frameV">
		<div id="cycle_number">
			<label for="amountV"><fmt:message key="waspIlluminaPlugin.displayQc_cycle.label" /></label>
			<input type="text" id="amountV" style="border:0;font-weight:bold;" size="4" readonly="readonly" value="1"/>
		</div>
		<div id="sliderV" ></div>
		<div id="int" class="ui-widget-content ui-corner-all"></div>
	</div>
	<div id="qscoreFrame"  class="ui-widget-content ui-corner-all">
		<div id="qscoreSelector" class="radio-jquery-ui">
			<table align="center">
				<tr>
					<td><fmt:message key="waspIlluminaPlugin.numGT30Qc_selector.label" /></td>
					<td>
						<span id="qscoreRadio">
<c:forEach items="${cellIndexList}" var="index" >
	<input type='radio' id='qscoreRadioL<c:out value="${index}" />' name='qscoreRadio' size='10' value='<c:out value="${index}" />'<c:if test="${index == 1}"> checked='checked'</c:if> />
	<label for='qscoreRadioL<c:out value="${index}" />'><c:out value="${index}" /></label> 
</c:forEach>
						</span>
					</td>
				</tr>
			</table>
		</div>
		<div id="qscoreIframe">
			<c:set var="fileName" value="${qscoreSubFolder}/QScore_L1.png" />
			<div id="qscoreIFrameImage">
				<img id="qscoreChart" src='<wasp:url fileAccessor = "${fileHandlesByName.get(fileName)}" />'  />
			</div>
		</div>
	</div>

	
</div>	

<div id="error_dialog-modal" title="<fmt:message key="waspIlluminaPlugin.displayQc_warningTitle.label" />" >
	<p><span class="ui-icon ui-icon-alert alert_icon" ></span><span id="warningText"></span></p>
</div>



<c:forEach items="${fileHandlesByName.keySet()}" var="fileName" varStatus="status" >
	<img src='<wasp:url fileAccessor = "${fileHandlesByName.get(fileName)}" />' alt='Cycle <c:out value="${status.count}" /> Quality Plot <c:out value="${fileName}" />' class='preloadHidden' />
</c:forEach>
