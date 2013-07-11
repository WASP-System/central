<script type="text/javascript"	src="/wasp/scripts/jquery/jquery.cookie.js"></script>

<script type="text/javascript" src="http://d3js.org/d3.v3.min.js"></script>

<script type="text/javascript" src="http://code.highcharts.com/highcharts.js"></script>
<script type="text/javascript" src="http://code.highcharts.com/modules/exporting.js"></script>
<script type="text/javascript" src="http://code.highcharts.com/highcharts-more.js"></script>

<!--script type="text/javascript" src="/wasp/scripts/extjs/ext-all-dev.js"></script-->
<!--link rel="stylesheet" type="text/css"	href="/wasp/scripts/extjs/resources/css/ext-all.css" /-->

<script type="text/javascript" src="/wasp/scripts/extjs/examples/shared/include-ext.js"></script>
<!--script type="text/javascript" src="/wasp/scripts/extjs/examples/shared/options-toolbar.js"></script-->
<!--script type="text/javascript" src="/wasp/scripts/extjs/examples/shared/examples.js"></script-->

<link rel="stylesheet" type="text/css" href="/wasp/css/portal.css" />


<script type="text/javascript">
var margin = {top: 20, right: 80, bottom: 20, left: 20},
    width = 460 - margin.right - margin.left,
    height = 700 - margin.top - margin.bottom,
    i = 0,
    duration = 500,
    branch_length = 100,
    min_branch_int = 40,
    root=new Object();
    
var barHeight = 20,
	barWidth = width * .6;

var tree = d3.layout.tree().size([height, 100]);

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
    extPortal = Ext.create('Ext.wasp.Portal');
//	Ext.Msg.alert('Alert',"${myid}");

	d3.json("http://localhost:8080/wasp/jobresults/getTreeJson.do?node="+rootstr, function(json) {	
		vis = d3.select("#treeview").append("svg:svg")
		.attr("width", width + margin.right + margin.left)
		.attr("height", height + margin.top + margin.bottom)
		.attr("pointer-events", "all")
		.append("svg:g")
		.attr("transform", "translate(" + margin.left + "," + margin.top + ")")
		//.call(d3.behavior.zoom().scaleExtent([1, 8]).on("zoom", zoom)).on("dblclick.zoom", null)
		.append("svg:g");
	
	
	  vis.append("rect")
	    .attr("width", width + margin.right + margin.left)
	    .attr("height", height + margin.top + margin.bottom)
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
//  nodes.forEach(function(d) { d.y = d.depth * branch_length; });
// Compute the "layout".
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
	  .attr("width", barWidth)
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
	   if (typeof val == "object") {
	        if (seen.indexOf(val) >= 0)
	            return undefined;
	        seen.push(val);
	    }
	    return val; });
	
	//var tabs = $('#mytabs').tabs({closable: true});
  
  $.ajax({
      url: '/wasp/jobresults/getDetailsJson.do?node='+dstr,
      type: 'GET',
      dataType: 'json',
      success: function (result) {
      	return;
    	if (d.type=='dummy') return;  
      
      	//d3.select('#detailview').select("h3").remove();
      	//d3.select('#detailview').select("tbody").remove();
      	
      	//remove all existing tabs first
      	var tabpanel = Ext.getCmp('wasp-tabpanel');
      	if(tabpanel===undefined) {
      		// alert if the tabpanel is undefined
      		extPortal.showMsg("wasp-tabpanel is not defined!");
      		return;
      	}
      	tabpanel.removeAll();

      	
/*      $.each(result, function (index, item) {
			var num_tabs = $("div#tabs ul li").length + 1;

             $("div#tabs ul").append(
                 "<li><a href='#tab" + num_tabs + "'>" + index + "</a></li>"
             );
			$("div#tabs").append(
                 "<div id='tab" + num_tabs + "'>" + item + "</div>"
             );
             $("div#tabs").tabs("refresh");
      	});
*/      

		var sdArray = new Array();	// to store all script dependencies from the plugins
		var cdArray = new Array();  // to store all css dependencies from the plugins
		
		$.each(result, function (index, item) {
			var tabTitle;
			
			if ((d.type.split('-'))[0]=="filetype") {
				tabTitle = "Download "+(d.type.split('-'))[1].toUpperCase()+" files";
				
				//var tablist = d3.select('#detailview').append("div").attr("id", "mytabs").append("ul");
				//var tabs = $("#mytabs").tabs();
				
	
		      	//$.each(result, function (index, item) {
		      		//$("#mytabs").tabs('add',item,index);
		      		//$tabs.tabs('add', '#tabs-'+tabCounter, 'Tab '+tabCounter);
		      		//$tabs.tabs('add', '#tab1', 'Tab '+tabCounter);
		      		//tabCounter++;
		      		//tabtitle=index;
		      		//tabcontent=item;
		      		//addtab();
		      	//});
		        return;
			} else {
				tabTitle = index; //d.name+" Details";
			}
			
			//tabTitle = index;
			tabCounter++;
			var tab = tabpanel.add({
				xtype: 'panel',
                title: tabTitle,
                layout:'card',
				activeItem: 1,
                items: [{
                    layout: 'fit'
                }]
			});
			var ptlpnl = tab.add({
                //id: 'wasp-portal1',
	            xtype: 'portalpanel'});
	        var ptlcol1 = ptlpnl.add({id: 'col-1'});
	        var ptlcol2 = ptlpnl.add({id: 'col-2'});
	            
	        $.each(item, function (index1, item1) {
	        	for (var i1 in item1.content.scriptDependencies){
	        		if (sdArray.indexOf(item1.content.scriptDependencies[i1])<0) {
	        			sdArray.push(item1.content.scriptDependencies[i1]);
	        		}
	        	}
	        	for (var i1 in item1.content.cssDependencies){
	        		if (cdArray.indexOf(item1.content.cssDependencies[i1])<0) {
	        			cdArray.push(item1.content.cssDependencies[i1]);
	        		}
	        	}
	        	
	            ptlcol1.add({
                    //id: 'portlet-2',
                    title: item1.title,
                    tools: extPortal.getTools(),
                    closable: false,
                    html: item1.content.htmlContent,
                    listeners: {
                        'close': Ext.bind(extPortal.onPortletClose, extPortal)
                    }
	            });
	        });
	        tabpanel.setActiveTab(tab);
			
//	        $( "<div id='tab"+tabCounter+"'></div>" ).appendTo( "#mytabs" );
	     	//d3.select('#detailview').append("h3").html(headStr);
	      	//var table = d3.select('#detailview').append("tbody");
/*	      	var table = d3.select("#tab"+tabCounter).append("tbody");
	        $.each(item, function (index1, item1) {
	         	var row = table.append("tr");
	         	row.append("td").html(index1);
	
	         	if (typeof item1 == 'string' || item1 instanceof String) {
	          		row.append("td").html(item1);
	         	} else if (item1.targetLink != undefined) {
	         		row.append("td").html('<a href="'+item1.targetLink+'">'+item1.label+'</a>');
	          	} else {
	          		var td = row.append("td");
	          		$.each(item1, function (index2, item2) {
	          			var row2 = td.append("tr");
	              		row2.append("td").html(index2);
	              		row2.append("td").html(item2);
	          		});
	          	}
	        });
	        tabs.doComponentLayout();
*/

/*	        $( "<li><a href='#tab"+tabCounter+"'>"+tabTitle+"</a></li>" )
				.appendTo( "#mytabs .ui-tabs-nav" );
		    $('#mytabs').tabs("refresh");
		    $('#mytabs').tabs("option", "active", -1);
*/		});

   		var oHead = document.getElementsByTagName('HEAD').item(0);
   		if (sdArray.length>0) {
			// get a list of existing scripts
	   		var esArray = oHead.getElementsByTagName('script');
	   		var essrcArray = new Array();
	   		for(var i=0,len=esArray.length; i<len;i++) {
	   			essrcArray.push(esArray[i].getAttribute('src'));
	   		}
			// if the plugin returns script is not in existing, add it to HEAD
	   		for(var i=0,len=sdArray.length; i<len;i++) {
	   			if (essrcArray.indexOf(sdArray[i])<0) {
					var oScript= document.createElement("script");
					oScript.type = "text/javascript";
					oScript.src = sdArray[i];
					oHead.appendChild(oScript);
	   			}
	   		}
   		}
   		if (cdArray.length>0) {
			// get a list of existing css files
	   		var elArray = oHead.getElementsByTagName('link');
	   		var elhrefArray = new Array();
	   		for(var i=0,len=elArray.length; i<len;i++) {
	   			elhrefArray.push(elArray[i].getAttribute('href'));
	   		}
			// if the plugin returns script is not in existing, add it to HEAD
	   		for(var i=0,len=cdArray.length; i<len;i++) {
	   			if (elhrefArray.indexOf(cdArray[i])<0) {
					var oScript= document.createElement("link");
					oScript.type = "text/css";
					oScript.rel = "stylesheet";
					oScript.href = cdArray[i];
					oHead.appendChild(oScript);
	   			}
	   		}
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