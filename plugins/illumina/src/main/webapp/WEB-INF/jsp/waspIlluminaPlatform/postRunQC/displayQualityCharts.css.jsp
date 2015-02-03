<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="wasp" uri="http://einstein.yu.edu/wasp" %>

<style>
	
	.infoIcon {
		float:left; 
		margin:-2px 5px 0 0;
	}
	
	.preloadHidden {
		display:none;
	}
	
	.errorHeader{
		background-color:red;
		background:red;
	}
	
	.fail{
		color:red;
		font-weight:bold;
	}
	
	.pass{
		color:green;
		font-weight:bold;
	}

	.center
	{
		margin-left:auto;
		margin-right:auto;
		width:1180px;
	}
	
	.selection_dialog
	{
		position: absolute; 
		left:40%; 
		margin-left: -150px; 
		top:0px; 
		width: 550px; 
		height: 660px; 
		padding: 2px;
		z-index:20;
	}
	
	.selection_dialog_shadow
	{
		width: 550px; 
		height: 660px; 
		position: absolute; 
		left:40%; 
		margin-left: -144px; 
		top:12px;
		padding: 2px;
		z-index:10;
	}
	
	.alert_icon {
		float: left; 
		margin-right: .3em;
	}
	
	.dialog_header{
		font-size: 14px; 
		padding:4px;
	}
	
	.qc_title{
		margin-top:10px; 
		padding:4px; 
		font-size: 14px;
	}
	
	.verifyQualityForm{
		padding: 10px;
	}
	
	#main {
		padding-top:20px;
	}
	
	#displayWindow{
		float:left; 
		margin-left: 20px;
	}
	
	#amountV {
		border:0;
		font-weight:bold;
	}
	
	#amountH {
		border:0;
		font-weight:bold;
	}
	

	td.formLabel {
		text-align: right;
		color: #191975;
		font-size: 14px;
	}
	
	td.vcenter {
		vertical-align:middle;
		border-bottom: solid 1px;
		
	}
	

	#sliderH_frame {
		font-family: Arial, Verdana, "Times New Roman", Times, serif;
		font-size: 12px;
		color: #525252;
		margin-left: 15px;
		margin-right: 15px;
		margin-top: 15px;
		
	}
	
	#sliderV_frame {
		font-family: Arial, Verdana, "Times New Roman", Times, serif;
		font-size: 12px;
		color: #525252;
		margin-left: 15px;
		margin-right: 15px;
		margin-top: 7px;
		float:left;
	}
	
	#sliderH .ui-slider-range { background: #CCCCCC; }
	#sliderH .ui-slider-handle { border-color: #525252; }
	#sliderV .ui-slider-range { background: #CCCCCC; }
	#sliderV .ui-slider-handle { border-color: #525252; }
	
	#sliderH {
		width: 800px;
		float:left;
		margin-top:10px;
		margin-right:10px;
	}
	
	#sliderV {
		clear:left;
		height: 685px;
		float:left;
		margin-top:15px;
		margin-right:10px;
	}
	
	#cycle_number{
		float: left;
		width: 150px;
		margin-top:8px;
		
	}
	
	#intA, #intC, #intG, #intT {
		float: left;
		width: 260px;
		height: 695px;
		margin: 15px;
	}
	<c:if test="${chartSubFolder.equals('NumGT30')}">
	#int{
		background-image:url(<c:set var="fileName" value="${chartSubFolder}/Chart_1.png" /><wasp:url fileAccessor = "${fileHandlesByName.get(fileName)}" />);
		background-size: 260px;
		background-repeat: no-repeat;
		width: 260px;
		height: 695px;
		float:left;
		margin-left: 15px;
		margin-top: 15px;
	}
	</c:if>
	
	<c:if test="${not empty (chartSubFolder) && not chartSubFolder.equals('NumGT30')}">
	#intA{
		clear: left;
		background-image:url(<c:set var="fileName" value="${chartSubFolder}/Chart_1_a.png" /><wasp:url fileAccessor = "${fileHandlesByName.get(fileName)}" />);
		background-size: 260px;
		background-repeat: no-repeat;
	}
	
	#intC{
		background-image:url(<c:set var="fileName" value="${chartSubFolder}/Chart_1_c.png" /><wasp:url fileAccessor = "${fileHandlesByName.get(fileName)}" />);
		background-size: 260px;
		background-repeat: no-repeat;
	}
	
	#intT{
		background-image:url(<c:set var="fileName" value="${chartSubFolder}/Chart_1_t.png" /><wasp:url fileAccessor = "${fileHandlesByName.get(fileName)}" />);
		background-size: 260px;
		background-repeat: no-repeat;
	}
	
	#intG{
		background-image:url(<c:set var="fileName" value="${chartSubFolder}/Chart_1_g.png" /><wasp:url fileAccessor = "${fileHandlesByName.get(fileName)}" />);
		background-size: 260px;
		background-repeat: no-repeat;
	}
	</c:if>
	
	#loading_dialog-modal{
		visibility:hidden;
		padding:15px;
		font-size: 14px;
	}
	
	#loading_dialog-modal td{
		vertical-align: middle;
	}
	
	#error_dialog-modal
	{
		padding:15px;
		font-size: 14px;
		z-index:100;
		visibility:hidden;
	}
	
	#qscoreFrame{
		float:left;
		margin-left: 20px;
		margin-top: 15px;
		padding: 15px;
		width:815px;
		height:450px;
	}
	
	#qscoreIframe{
		overflow-x:auto; 
	}
	
	#qscoreSelector{
		margin-bottom:15px;
	}
	
	#mainImage{
		position: relative; 
		width: 800px;
		height: 400px;
		margin-left:auto;
		margin-right:auto;
		padding: 15px;
	}
	
	#validationTable {
		border-collapse: collapse;
		margin-left:auto;
		margin-right:auto;
	}
	
	#validationTable th{
		border:solid;
		border-width:2px;
		border-color:grey;
		padding:5px;
		text-align:center;
		font-size:12px;
	}
	
	#validationTable td{
		border:solid;
		border-width:1px;
		border-color:grey;
		color: #191975;
		font-size: 14px;
		padding:5px;
		text-align:center;
		font-size:12px;
		vertical-align: middle;
		white-space:nowrap;
	}
	
	td.fixedWidth{
		width:120px;
	}
	
	#selectionWindow {
		visibility:hidden;
	}
	
	#main {
		visibility:hidden;
	}
	
	
</style>