<%@ include file="/WEB-INF/jsp/taglib.jsp"%>

<div id="selectionWindow">
	<div class="ui-overlay" >
		<div class="ui-widget-shadow ui-corner-all selection_dialog_shadow" ></div>
	</div>
	<div class="selection_dialog ui-widget-content ui-corner-all">
		<div class="ui-widget-header ui-corner-all dialog_header" >Assessment of Cluster Density</div>
		<div  class="verifyQualityForm" class="center">		
			<p>Please click either 'Pass' or 'Fail' for each lane based on your interpretation of the CLUSTER DENSITY charts only, then click the 'Continue' button.</p>
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
	<p class="ui-state-default ui-corner-all ui-helper-clearfix" style="padding:4px; font-size: 14px">
		<span class="ui-icon ui-icon-info" style="float:left; margin:-2px 5px 0 0;"></span>
		Illumina OLB Stats: Cluster Density Per Lane For <c:out value="${runName}" />
	</p>
	<p>Note that the <span style="color:blue;">blue box plots represent total cluster density</span> and the <span style="color:green;">green box plots represent clusters pass filter</span>. Ideally the median of both plots for each lane should be about the same. Illumina currently recommends ~400 clusters/mm<sup>2</sup> (or 500-630 clusters/mm<sup>2</sup> on GAIIx using the TruSeq SBS V5 kit or 610-680 clusters/mm<sup>2</sup> on HiSeq2000 with TruSeq v3 Cluster and SBS kits).</p>
	<p><span style="color:red;">WARNING:</span> High cluster density combined with low cluster pass filter values indicates overloading of the lane and risks poor quality sequence data.</p>
	<div id="displayWindow" style="float:right;"><button id="showForm">Continue</button></div>
	<div id="mainImage" class="ui-widget-content ui-corner-all"><img src='<c:out value="${runReportBaseImagePath}/NumClusters%20By%20Lane.png" />' height='400' width='600'></div>
</div>	

<div id="error_dialog-modal" title="Warning" >
	<p><span class="ui-icon ui-icon-alert alert_icon" ></span><span id="warningText"></span></p>
</div>

