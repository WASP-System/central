<script type="text/javascript"	src="/wasp/scripts/jquery/jquery.cookie.js"></script>

<script type="text/javascript"	src="https://github.com/rgrove/lazyload/raw/master/lazyload.js"></script>

<script type="text/javascript" src="http://d3js.org/d3.v3.min.js"></script>

<!--script type="text/javascript" src="http://code.highcharts.com/highcharts.js"></script-->
<!--script type="text/javascript" src="http://code.highcharts.com/highcharts-more.js"></script-->
<!--script type="text/javascript" src="http://code.highcharts.com/modules/exporting.js"></script-->

<!--script type="text/javascript" src="/wasp/scripts/extjs/ext-all-dev.js"></script-->
<!--link rel="stylesheet" type="text/css"	href="/wasp/scripts/extjs/resources/css/ext-all.css" /-->

<script type="text/javascript" src="/wasp/scripts/extjs/examples/shared/include-ext.js"></script>
<!--script type="text/javascript" src="/wasp/scripts/extjs/examples/shared/options-toolbar.js"></script-->
<!--script type="text/javascript" src="/wasp/scripts/extjs/examples/shared/examples.js"></script-->

<link rel="stylesheet" type="text/css" href="/wasp/css/portal.css" />


<script type="text/javascript">
var margin = {top: 20, right: 30, bottom: 20, left: 20};
var treeviewWidth,
	treeviewHeight,
	i = 0,
	duration = 500,
	branch_length = 100,
	min_branch_int = 40,
	root=new Object();
    
var barHeight = 20,	barWidth;

var tree = d3.layout.tree().size([treeviewHeight, 100]);

var diagonal = d3.svg.diagonal()
    .projection(function(d) { return [d.y, d.x]; });

var vis;

root.myid=${myid};
root.type="${type}";
root.jid=-1;
root.pid=-1;

var seen = [];
var rootstr = JSON.stringify(root, function(key, val) {
   if (typeof val == "object") {
        if (seen.indexOf(val) >= 0)
            return undefined;
        seen.push(val);
    }
    return val; 
});
 
var activeNode = {myid: null, type: null};


Ext.require([
    'Ext.layout.container.*',
    'Ext.resizer.Splitter',
    'Ext.fx.target.Element',
    'Ext.fx.target.Component',
    'Ext.window.Window',
    'Ext.wasp.Portal'
]);

var extPortal;

Ext.onReady(function(){
	extPortal = Ext.create('Ext.wasp.Portal', {width: $('#content').width()});
//	Ext.Msg.alert('Alert',$('#content').width());

	jQuery(window).bind('resize', function() {
			extPortal.setWidth($('#content').width());
		}).trigger('resize');

	treeviewWidth = $('#content').width() * 0.3 - margin.left - margin.right;
	treeviewHeight = $('#content').height() - margin.top - margin.bottom;
	barWidth = treeviewWidth * .5;
	barHeight = 20;
	
	d3.json("/wasp/jobresults/getTreeJson.do?node="+rootstr, function(json) {	
		vis = d3.select("#treeview").append("svg:svg")
		.attr("width", treeviewWidth)
		.attr("height", treeviewHeight)
		.attr("pointer-events", "all")
		.append("svg:g")
		.attr("transform", "translate(" + margin.left + "," + margin.top + ")")
		//.call(d3.behavior.zoom().scaleExtent([1, 8]).on("zoom", zoom)).on("dblclick.zoom", null)
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


//var g = d3.select("g");
//g.call(drag);

function update(source) {

	// Compute the new tree layout.
	var nodes = tree.nodes(root); //.reverse();

	// Normalize for fixed-depth.
//	nodes.forEach(function(d) { d.y = d.depth * branch_length; });
//	Compute the "layout".
	nodes.forEach(function(n, i) {
		n.x = i * barHeight;
	});

	// Update the nodes
	var node = vis.selectAll("g.node")
		.data(nodes, function(d) { return d.id || (d.id = ++i); });
  
	var nodeEnter = node.enter().append("svg:g")
		.attr("class", "node")
		.attr("transform", function(d) { return "translate(" + source.y0 + "," + source.x0 + ")"; })
		.style("opacity", 1e-6)
		.on("click", click)
		.on("mouseover", onMouseOver)
		.on("mouseout", onMouseOut);

	// Enter any new nodes at the parent's previous position.
	nodeEnter.append("svg:rect")
	  .attr("y", -barHeight / 2)
	  .attr("height", barHeight)
	  .attr("width", localBarWidth)
	  .style("fill", color)
	  .on("dblclick", toggle);
	
	nodeEnter.append("svg:text")
	  .attr("dy", 3.5)
	  .attr("dx", 13.5)
	  .text(function(d) { return d.name; });
	
	// Add checkbox
	nodeEnter.append("svg:circle")
    .attr("cx", 7)
	.attr("r", 4)
	.attr("fill", "white")
	.on("click", function(d) {
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
	  .attr("transform", function(d) { return "translate(" + d.y + "," + d.x + ")"; })
	  .style("opacity", 1);
	
	node.transition()
	  .duration(duration)
	  .attr("transform", function(d) { return "translate(" + d.y + "," + d.x + ")"; })
	  .style("opacity", 1)
	.select("rect")
	  .style("fill", color);
	
	// Transition exiting nodes to the parent's new position.
	node.exit().transition()
	  .duration(duration)
	  .attr("transform", function(d) { return "translate(" + source.y + "," + source.x + ")"; })
	  .style("opacity", 1e-6)
	  .remove();
	
	// Update the links
	var link = vis.selectAll("path.link")
	  .data(tree.links(nodes), function(d) { return d.target.id; });
	
	// Enter any new links at the parent's previous position.
	link.enter().insert("svg:path", "g")
	  .attr("class", "link")
	  .attr("d", function(d) {
	    var o = {x: source.x0, y: source.y0};
	    return diagonal({source: o, target: o});
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
	  .attr("d", function(d) {
	    var o = {x: source.x, y: source.y};
	    return diagonal({source: o, target: o});
	  })
	  .remove();
	
	// Stash the old positions for transition.
	nodes.forEach(function(d) {
	d.x0 = d.x;
	d.y0 = d.y;
	});

	// store the parent's id in everynode
	nodes.forEach(function(d) {
		if (d!=root)
			d.pid = d.parent.myid;
		else
			d.pid = -1;
	});
}

function printSelectedNodes() {
  var nodes = tree.nodes(root);

  var selected = [];
  nodes.forEach(function(n, i) {
    if (n.selected) {
      selected.push(n.name);
    }
  });
  console.log(selected);
}

//Toggle children
function toggle(d) {
	if (d.children) {
		d._children = d.children;
		d.children = null;
	} else {
		d.children = d._children;
		d._children = null;
	}
	update(d);
}

var tabs, tabCounter=0;
var jscssfilesadded=""; //list of external script/css files already added
var numcol = 2;

function click(d) {
	if (d.jid==undefined) {
	 d.jid = -1;
	}/*  else {
	 jid = d.jid;
	} */
	
	// parent node's id
/* 	var pid = -1;
	if (d!=root)
		d.pid = d.parent.myid; */

	var seen = [];
	var dstr = JSON.stringify(d, function(key, val) {
		if (key == "parent" || key == "children") {	//don't stringify the parent/children nodes
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
		url: '/wasp/jobresults/getDetailsJson.do?node='+dstr,
		type: 'GET',
		dataType: 'json',
		success: function (result) {
			//return;
			if (d.type!='filegroup') return;  
	      
			//remove all existing tabs from tabpanel first
			var tabpanel = Ext.getCmp('wasp-tabpanel');
			if(tabpanel===undefined) {
				// alert if the tabpanel is undefined
				extPortal.showMsg("wasp-tabpanel is not defined!");
				return;
			}
			tabpanel.removeAll();
	
			var jsList = new Array(), cssList = new Array();
			$.each(result.paneltablist, function (index, item) {
				$.each(item.panels, function(index1, item1){
					for(var i=0,len=item1.content.scriptDependencies.length; i<len; i++) {
						if (jscssfilesadded.indexOf("["+item1.content.scriptDependencies[i]+"]")==-1){ //if the file not been loaded before
							jsList.push(item1.content.scriptDependencies[i]);
							jscssfilesadded+="["+item1.content.scriptDependencies[i]+"]";
						}
					}
					for(var i=0,len=item1.content.cssDependencies.length; i<len; i++) {
						if (jscssfilesadded.indexOf("["+item1.content.cssDependencies[i]+"]")==-1){ //if the file not been loaded before
							cssList.push(item1.content.cssDependencies[i]);
							jscssfilesadded+="["+item1.content.cssDependencies[i]+"]";
						}
					}
				});
			});
	    	
			var createPortal = function(){
				var summaryPanel;
				if (result.statuslist.length > 0){
					summaryPanel = Ext.create('Ext.wasp.GridPortlet', { myData: result.statuslist, tabPanel: tabpanel });
				} else{
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
                    },{
                    	xtype: 'portalpanel',
	                    //items: [{
	                        //id: 'col-',
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
	                    //}]
                	}]
				});
				
				$.each(result.paneltablist, function (index, item) {
					var tabTitle;
					
					if ((d.type.split('-'))[0]=="filetype") {
						tabTitle = "Download "+(d.type.split('-'))[1].toUpperCase()+" files";
				        return;
					} else {
						tabTitle = item.name; //d.name+" Details";
					}
					
					
					var tabid = index;
					var tab = tabpanel.add({
						xtype: 'panel',
						id: tabid,
				        title: tabTitle,
				        layout:'card',
						activeItem: 1,
				        items: [{
				            layout: 'fit'
				        }]
					});
					var ptlpnl = tab.add({xtype: 'portalpanel'});
				    var ptlcolArray = new Array;
				    
				    numcol = item.numberOfColumns;
				    for(var i=0;i<numcol;i++) {
				    	ptlcolArray.push(ptlpnl.add({id: tabid+'-col-'+(i+1)}));
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
	   		if(cssList.length>0) {
	        	LazyLoad.css(cssList, function () {
	        		if(jsList.length>0) {
			        	LazyLoad.js(jsList, function () {
			        		createPortal();
						});
	        		} else { createPortal(); }
				});
	   		} else {
	    		if(jsList.length>0) {
		        	LazyLoad.js(jsList, function () {
		        		createPortal();
					});
	    		} else { createPortal(); }
	   		}
		}
	});

	if (d.children == '') {
		$.ajax({
			url: '/wasp/jobresults/getTreeJson.do?node='+dstr,
			type: 'GET',
			dataType: 'json',
			success: function (result) {
				if (result.children != '') {
					d.children = result.children;
					update(d);
				}
			}
		});
	}

	if (d.myid !== activeNode.myid && d.type !== activeNode.type) {
		toggle(d);
		activeNode.myid = d.myid;
		activeNode.type = d.type;
	}
}

function localBarWidth(d) {
	var lbw = barWidth;
	//alert(source.name);
	if (d.name.length>30) {
		lbw += 7 * (d.name.length-30);
	}
	return lbw;
}

function color(d) {
	return d._children ? "#3182bd" : d.children ? "#c6dbef" : "#fd8d3c";
}

function onMouseOver(d)
{
	d3.select(this).select("circle").attr("r", 5);
	d3.select(this).select("text").style("font-weight", "bold");
	if (d.parent)
	{
	}
}

function onMouseOut(d)
{
	d3.select(this).select("circle").attr("r", 4.5);
	d3.select(this).select("text").style("font-weight", "normal");
	if (d.parent)
	{
	}
}

</script>