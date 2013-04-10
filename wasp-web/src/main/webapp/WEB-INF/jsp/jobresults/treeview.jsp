<%@ include file="/WEB-INF/jsp/include.jsp"%>
<%@ include file="/WEB-INF/jsp/taglib.jsp"%>

<div>
	<h1>${wf_name} Job Result</h1>
</div>

<div id="left-container">
<!-- 	<p><input type="submit" value="Show Filters" onClick="showhide(this,'filters');"/></p> -->
	<div id="filters" style="display: none;">
		Please select what to show:<br />
		Sample: <input type="checkbox" name="sports" value="sample" checked /> &nbsp;&nbsp;
		Library: <input type="checkbox" name="sports" value="library" checked /> &nbsp;&nbsp;
		Cell: <input type="checkbox" name="sports" value="cell" checked /> &nbsp;&nbsp;
		File: <input type="checkbox" name="sports" value="file" checked /> &nbsp;&nbsp;
		<p><input type="submit" value="Update" onClick="update(root);"/></p>
	</div>
	<div id="treeview">
		<p><input type="submit" value="Toggle Views" onClick="toggleview();"/></p>
	</div>
</div>

<div id="right-container">
	<div id="detailview">
<!-- 		<div id="tabs">
    		<ul>
				<li><a href='#tab1'>#1</a></li>
			</ul>
			<div id='tab1'>tab 1 content</div>
    	</div> -->
	</div>
</div>






