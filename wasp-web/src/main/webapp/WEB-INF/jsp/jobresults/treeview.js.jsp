<%@ include file="/WEB-INF/jsp/taglib.jsp"%>
<script type="text/javascript"	src="<wasp:relativeUrl value='scripts/jquery/jquery.cookie.js' />"></script>
<script type="text/javascript"	src="https://rawgithub.com/rgrove/lazyload/master/lazyload.js"></script>
<script type="text/javascript"	src="https://rawgithub.com/johnculviner/jquery.fileDownload/master/src/Scripts/jquery.fileDownload.js"></script>
<script type="text/javascript" src="http://d3js.org/d3.v3.min.js"></script>
<script type="text/javascript" src="http://extjs-public.googlecode.com/svn/tags/extjs-4.2.1/release/ext-all-dev.js"></script>
<script type="text/javascript" src="http://extjs-public.googlecode.com/svn/tags/extjs-4.2.1/release/packages/ext-theme-neptune/build/ext-theme-neptune.js"></script>
<script type="text/javascript"	src="<wasp:relativeUrl value='scripts/extjs/wasp/WaspNamespaceDefinition.js.jsp' />"></script>
<link rel="stylesheet" type="text/css" href="<wasp:relativeUrl value='css/ext-theme-neptune-all-wasp.css' />" />
<link rel="stylesheet" type="text/css" href="<wasp:relativeUrl value='css/portal.css' />" />
<link rel="stylesheet" type="text/css" href="<wasp:relativeUrl value='css/RowActions.css' />" />


<script type="text/javascript">

String.prototype.width = function (font) {
	var f = font || '13px sans-serif',
		o = $('<div>' + this + '</div>')
			.css({
				'position': 'absolute',
				'float': 'left',
				'white-space': 'nowrap',
				'visibility': 'hidden',
				'font': f
			})
			.appendTo($('body')),
		w = o.width();

	o.remove();

	return w;
};

var margin = {
	top: 20,
	right: 30,
	bottom: 20,
	left: 20
};
var treeviewWidth,
	treeviewHeight,
	i = 0,
	duration = 500,
	branch_length = 100,
	min_branch_int = 40,
	root = new Object();

var barHeight = 20,
	barWidth;

var div;

var tree = d3.layout.tree().size([treeviewHeight, 100]);

var diagonal = d3.svg.diagonal()
	.projection(function (d) {
		return [d.y, d.x];
	});

var vis;

root.myid = ${myid};
root.type = "${type}";
root.jid = -1;
root.pid = -1;

var seen = [];
var rootstr = JSON.stringify(root, function (key, val) {
	if (typeof val == "object") {
		if (seen.indexOf(val) >= 0)
			return undefined;
		seen.push(val);
	}
	return val;
});

var activeNode = {
	myid: null,
	type: null
};

Ext.require([
	'Ext.layout.container.*',
	'Ext.resizer.Splitter',
	'Ext.fx.target.Element',
	'Ext.fx.target.Component',
	'Ext.window.Window',
	'Wasp.Portal'
]);

var extPortal;

function checkForPageRedirect(responseText){
	// if timeout of login a json request will fail and an html page containing the redirection location will be provided
	// redirect current page to the provided url if so.
	var re = new RegExp("window\.location=['\"](.+?)['\"]");
  	var match = re.exec(responseText);
  	if (match == null)
  		return false;
  	window.location=match[1];
  	return true; // should never get here
}

Ext.onReady(function () {
	extPortal = Ext.create('Wasp.Portal', {
		width: $('#content').width()
	});
//	Ext.Msg.alert('Alert',$('#content').width());

	jQuery(window).bind('resize', function () {
		extPortal.setWidth($('#content').width());
	}).trigger('resize');

	treeviewWidth = $('#content').width() * 0.3 - margin.left - margin.right;
	treeviewHeight = $('#content').height() - margin.top - margin.bottom + 500;

	barWidth = treeviewWidth * .58;
	barHeight = 20;

	div = d3.select("body").append("div")
		.attr("class", "tooltip")
		.style("opacity", 0);

	d3.json("<wasp:relativeUrl value='jobresults/getTreeJson.do?node=' />" + rootstr, function (json) {
		vis = d3.select("#treeview").append("svg:svg")
			.attr("width", treeviewWidth)
			.attr("height", treeviewHeight)
			.attr("pointer-events", "all")
			.append("svg:g")
			.attr("transform", "translate(" + margin.left + "," + margin.top + ")")
//			.call(d3.behavior.zoom().scaleExtent([1, 8]).on("zoom", zoom)).on("dblclick.zoom", null)
			.append("svg:g");


		vis.append("rect")
			.attr("width", treeviewWidth)
			.attr("height", treeviewHeight)
			.attr('fill', '#fff')
			.attr('fill-opacity', '0');

		root = json;
		root.x0 = 0; // height / 2;
		root.y0 = 0;
		//root.children.forEach(collapse);
		update(root);
		click(root);
		toggle(root);
		activeNode.myid = root.myid;
		activeNode.type = root.type;
	});
	
	d3.select('#toggle_button').on('click', function() {
		if ( root.children && root.children!="" ) {
			root.children.forEach(expandAll);
			update(root);
		} else if ( root._children && root._children!="" ) {
		}        
	});
});

function zoom() {
	vis.attr("transform", "translate(" + d3.event.translate + ")scale(" + d3.event.scale + ")");
}

function collapse(d) {
	if (d.children) {
		d._children = d.children;
		d._children.forEach(collapse);
		d.children = null;
	}
}

// Collapse/Expand/Toggle all descendents
function collapseAll(d) {
	if (d.children && d.children!="") {
		d._children = d.children;
		d.children = null;
	}
	if (d._children && d._children!="")
		d._children.forEach(collapseAll);
}
function expandAll(d) {
	if (!d.children && !d._children) {
		if (d.jid == undefined) {
			d.jid = -1;
		}
		var seen = [];
		var dstr = JSON.stringify(d, function (key, val) {
			if (key == "parent" || key == "children") { //don't stringify the parent/children nodes
				return undefined;
			} else if (typeof val == "object") {
				if (seen.indexOf(val) >= 0)
					return undefined;
				seen.push(val);
			}
			return val;
		});
		$.ajax({
			url: '<wasp:relativeUrl value="jobresults/getTreeJson.do?node=" />' + dstr,
			type: 'GET',
			dataType: 'json'
		})
		.done(function (result) {
			if (result.children != '') {
				d.children = result.children;
			}
		})
		.fail(function(jqXHR){
			checkForPageRedirect(jqXHR.responseText);
		});
	}
	if (d._children && d._children!="") {
		d.children = d._children;
		d._children = null;
	}
	if (d.children && d.children!="")
		d.children.forEach(expandAll);
}
function toggleAll(d) {
	if (d.children && d.children!="") {
		collapseAll(d);
	} else if (d._children && d._children!="") {
		expandAll(d);
	}
	update(d);
}


//var g = d3.select("g");
//g.call(drag);

function getNodeName(d) {
	if (d.name.length > 30) {
		d.fullname = d.name;
		d.name = d.fullname.substring(0, 26) + '...';
	}
	return d.name;
}

function update(source) {

	// Compute the new tree layout.
	var nodes = tree.nodes(root); //.reverse();

	var d3tree_height = 0;
	// Normalize for fixed-depth.
	//	nodes.forEach(function(d) { d.y = d.depth * branch_length; });
	//	Compute the "layout".
	nodes.forEach(function (n, i) {
		n.x = i * barHeight;
		if (d3tree_height < n.x)
			d3tree_height = n.x ;
	});

	$("svg").height(d3tree_height + 2*barHeight);

	// Update the nodes
	var node = vis.selectAll("g.node")
		.data(nodes, function (d) {
			return d.id || (d.id = ++i);
		});

	var nodeEnter = node.enter().append("svg:g")
		.attr("class", "node")
		.attr("transform", function (d) {
			return "translate(" + source.y0 + "," + source.x0 + ")";
		})
		.style("opacity", 1e-6)
		.on("click", click)
		.on("mouseover", onMouseOver)
		.on("mousemove", onMouseMove)
		.on("mouseout", onMouseOut);

	// Enter any new nodes at the parent's previous position.
	nodeEnter.append("svg:rect")
		.attr("y", -barHeight / 2)
		.attr("height", barHeight)
//		.attr("width", localBarWidth)
		.attr("width", barWidth)
		.style("fill", color)
		.on("dblclick", toggle);

	nodeEnter.append("svg:text")
		.attr("dy", 3.5)
		.attr("dx", 13.5)
//		.text(function(d) { return d.name; });
		.text(getNodeName);

	// Add checkbox
	nodeEnter.append("svg:circle")
		.attr("cx", 7)
		.attr("r", 4)
		.attr("fill", "white")
		.on("click", function (d) {
			if (d.selected) {
				d.selected = false;
				d3.select(this).attr("fill", "white");
			} else {
				d.selected = true;
				d3.select(this).attr("fill", "steelblue");
			}

			printSelectedNodes();
		});

	// Transition nodes to their new position.
	nodeEnter.transition()
		.duration(duration)
		.attr("transform", function (d) {
			return "translate(" + d.y + "," + d.x + ")";
		})
		.style("opacity", 1);

	node.transition()
		.duration(duration)
		.attr("transform", function (d) {
			return "translate(" + d.y + "," + d.x + ")";
		})
		.style("opacity", 1)
		.select("rect")
		.style("fill", color);

	// Transition exiting nodes to the parent's new position.
	node.exit().transition()
		.duration(duration)
		.attr("transform", function (d) {
			return "translate(" + source.y + "," + source.x + ")";
		})
		.style("opacity", 1e-6)
		.remove();

	// Update the links
	var link = vis.selectAll("path.link")
		.data(tree.links(nodes), function (d) {
			return d.target.id;
		});

	// Enter any new links at the parent's previous position.
	link.enter().insert("svg:path", "g")
		.attr("class", "link")
		.attr("d", function (d) {
			var o = {
				x: source.x0,
				y: source.y0
			};
			return diagonal({
				source: o,
				target: o
			});
		})
		.transition()
		.duration(duration)
		.attr("d", diagonal);

	// Transition links to their new position.
	link.transition()
		.duration(duration)
		.attr("d", diagonal);

	// Transition exiting nodes to the parent's new position.
	link.exit().transition()
		.duration(duration)
		.attr("d", function (d) {
			var o = {
				x: source.x,
				y: source.y
			};
			return diagonal({
				source: o,
				target: o
			});
		})
		.remove();

	// Stash the old positions for transition.
	nodes.forEach(function (d) {
		d.x0 = d.x;
		d.y0 = d.y;
	});

	// store the parent's id in everynode
	nodes.forEach(function (d) {
		if (d != root)
			d.pid = d.parent.myid;
		else
			d.pid = -1;
	});
}

function printSelectedNodes() {
	var nodes = tree.nodes(root);

	var selected = [];
	nodes.forEach(function (n, i) {
		if (n.selected) {
			selected.push(n.name);
		}
	});
	console.log(selected);
}

//Toggle children
function toggle(d) {
	if (d.children && d.children!="") {
		d._children = d.children;
		d.children = null;
	} else {
		d.children = d._children;
		d._children = null;
	}
	update(d);
}

var tabs, tabCounter = 0;
var jscssfilesadded = ""; //list of external script/css files already added
var numcol = 2;

function click(d) {
	if (d.jid == undefined) {
		d.jid = -1;
	}
	/*  else {
	 jid = d.jid;
	} */

	// parent node's id
	/* 	var pid = -1;
	if (d!=root)
		d.pid = d.parent.myid; */

	var seen = [];
	var dstr = JSON.stringify(d, function (key, val) {
		if (key == "parent" || key == "children") { //don't stringify the parent/children nodes
			return undefined;
		} else if (typeof val == "object") {
			if (seen.indexOf(val) >= 0)
				return undefined;
			seen.push(val);
		}
		return val;
	});

	//var tabs = $('#mytabs').tabs({closable: true});

	$.ajax({
		url: '<wasp:relativeUrl value="jobresults/getDetailsJson.do?node=" />' + dstr,
		type: 'GET',
		dataType: 'json'
		})
		.done(function (result) {

			var tabpanel = Ext.getCmp('wasp-tabpanel');
			if (tabpanel === undefined) {
				// alert if the tabpanel is undefined
				extPortal.showMsg("wasp-tabpanel is not defined!");
				return;
			}

			if (d.type.indexOf('filetype') > -1) {
				//remove all existing tabs from tabpanel first
				tabpanel.removeAll();

				var filePanel = Ext.create('Wasp.FileDownloadGridPortlet', {
					fgListStr: result.fgliststr
				});

				//test
				//$.fileDownload('http://phoenix.einstein.yu.edu:8080/wasp-file/get/file/c7d5237e-ab84-4837-a618-6ec17ac6add3');
				//test

				var summarytab = tabpanel.add({
					id: 'file-tab',
					xtype: 'panel',
					title: 'File Download',
					layout: 'card',
					activeItem: 1,
					items: [{
						layout: 'fit'
					}, {
						xtype: 'portalpanel',
						//items: [{
						//id: 'col-',
						items: [{
							//id: 'portlet-',
							xtype: 'portlet',
							title: 'File Download Panel',
							//tools: extPortal.getTools(),
							//frame: false,
							closable: false,
							collapsible: false,
							draggable: false,
							items: filePanel
						}]
						//}]
					}]
				});
			} else if (d.type=='job' || d.type=='filegroup') {
				//remove all existing tabs from tabpanel first
				tabpanel.removeAll();

				var jsList = new Array(),
					cssList = new Array();
				$.each(result.paneltablist, function (index, item) {
					$.each(item.panels, function (index1, item1) {
						for (var i = 0, len = item1.content.scriptDependencies.length; i < len; i++) {
							if (jscssfilesadded.indexOf("[" + item1.content.scriptDependencies[i] + "]") == -1) { //if the file not been loaded before
								jsList.push(item1.content.scriptDependencies[i]);
								jscssfilesadded += "[" + item1.content.scriptDependencies[i] + "]";
							}
						}
						for (var i = 0, len = item1.content.cssDependencies.length; i < len; i++) {
							if (jscssfilesadded.indexOf("[" + item1.content.cssDependencies[i] + "]") == -1) { //if the file not been loaded before
								cssList.push(item1.content.cssDependencies[i]);
								jscssfilesadded += "[" + item1.content.cssDependencies[i] + "]";
							}
						}
					});
				});

				var createPortal = function () {
					// if the node clicked is filegroup, create an extra summary tab in the portal
					if (d.type=='filegroup') {
						var summaryPanel;
						if (result.statuslist.length > 0) {
							summaryPanel = Ext.create('Wasp.PluginSummaryGridPortlet', {
								statusData: result.statuslist,
								tabPanel: tabpanel
							});
						} else {
							summaryPanel = {
								html: '<div class="noPlugin">No registered plugins handle this data.</div>'
							}
						}
						var summarytab = tabpanel.add({
							id: 'summary-tab',
							xtype: 'panel',
							title: 'Summary',
							layout: 'card',
							activeItem: 1,
							items: [{
								layout: 'fit'
							}, {
								xtype: 'portalpanel',
								items: [{
									//id: 'portlet-',
									xtype: 'portlet',
									title: 'Completion Status for Plugins Handling this Data',
									//tools: extPortal.getTools(),
									//frame: false,
									closable: false,
									collapsible: false,
									draggable: false,
									items: summaryPanel
								}]
							}]
						});
					}

					$.each(result.paneltablist, function (index, item) {
						var tabTitle;

//						if ((d.type.split('-'))[0] == "filetype") {
//							tabTitle = "Download " + (d.type.split('-'))[1].toUpperCase() + " files";
//							return;
//						} else {
							tabTitle = item.name; //d.name+" Details";
//						}


						var tabid = index;
						var tab = tabpanel.add({
							xtype: 'panel',
							id: tabid,
							title: tabTitle,
							layout: 'card',
							activeItem: 1,
							items: [{
								layout: 'fit'
							}]
						});
						var ptlpnl = tab.add({
							xtype: 'portalpanel'
						});
						var ptlcolArray = new Array;

						numcol = item.numberOfColumns;
						for (var i = 0; i < numcol; i++) {
							ptlcolArray.push(ptlpnl.add({
								id: tabid + '-col-' + (i + 1)
							}));
						}

						var colid = 0;
						$.each(item.panels, function (index1, item1) {
							ptlcolArray[colid++].add({
								title: item1.title,
								tools: extPortal.getTools(),
								closable: false,
								html: item1.content.htmlCode,
								listeners: {
									'render': Ext.bind(new Function("portlet", item1.execOnRenderCode), extPortal),
									'resize': Ext.bind(new Function("portlet", item1.execOnResizeCode), extPortal),
									'expand': Ext.bind(new Function("portlet", item1.execOnExpandCode), extPortal)
								}
							});
							colid %= numcol;
						});
					});

					tabpanel.setActiveTab(0);
				};

				// load all css then all js then create portal
				if (cssList.length > 0) {
					LazyLoad.css(cssList, function () {
						if (jsList.length > 0) {
							LazyLoad.js(jsList, function () {
								createPortal();
							});
						} else {
							createPortal();
						}
					});
				} else {
					if (jsList.length > 0) {
						LazyLoad.js(jsList, function () {
							createPortal();
						});
					} else {
						createPortal();
					}
				}
			} else {
				return;
			}
		})
		.fail(function(jqXHR){
			checkForPageRedirect(jqXHR.responseText);
		});

	if (!d.children && !d._children) {
		$.ajax({
			url: '<wasp:relativeUrl value="jobresults/getTreeJson.do?node=" />' + dstr,
			type: 'GET',
			dataType: 'json'
			})
			.done(function (result) {
				if (result.children != '') {
					d.children = result.children;
					update(d);
				}
			})
			.fail(function(jqXHR){
				checkForPageRedirect(jqXHR.responseText);
			});
	}

	toggle(d);

	if (d.myid !== activeNode.myid && d.type !== activeNode.type) {
		activeNode.myid = d.myid;
		activeNode.type = d.type;
	}
}

function localBarWidth(d) {
	var lbw = barWidth;
	//alert(source.name);
	if (d.name.length > 30) {
		lbw += 7 * (d.name.length - 30);
	}
	return lbw;
}

function color(d) {
	return d._children ? "#3182bd" : d.children ? "#c6dbef" : "#fd8d3c";
}

function onMouseOver(d) {
	d3.select(this).select("circle").attr("r", 5);
	d3.select(this).select("text").style("font-weight", "bold");
	if (d.parent) {}

	if (d.fullname) {
		div.transition()
			.duration(200)
			.style("opacity", .8);
	}
}

function onMouseMove(d) {
	if (d.fullname) {
		div.text(d.fullname)
		//.style("width", x(txt))
		.style("left", (d3.event.pageX) + "px")
			.style("top", (d3.event.pageY + 18) + "px");
	}
}

function onMouseOut(d) {
	d3.select(this).select("circle").attr("r", 4.5);
	d3.select(this).select("text").style("font-weight", "normal");
	if (d.parent) {}

	div.transition()
		.duration(100)
		.style("opacity", 0);
}
</script>