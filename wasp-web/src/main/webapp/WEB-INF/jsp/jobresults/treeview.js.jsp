<script type="text/javascript" src="/wasp/scripts/jquery/jquery.cookie.js"></script>

<script type="text/javascript" src="http://d3js.org/d3.v3.min.js"></script>

<script>
var state = 'none';

function showhide(btn,layer_ref) {
	if (state == 'block') {
		state = 'none';
		btn.value= 'Show Filters';
	} else {
		state = 'block';
		btn.value='Hide Filters';
	}
	if (document.all) { //IS IE 4 or 5 (or 6 beta)
		eval("document.all." + layer_ref + ".style.display = state");
	}
	if (document.layers) { //IS NETSCAPE 4 or below
		document.layers[layer_ref].display = state;
	}
	if (document.getElementById && !document.all) {
		hza = document.getElementById(layer_ref);
		hza.style.display = state;
	}
}

function toggleview() {
	if (root.jid == undefined || root.jid < 1) {
		root.jid = root.id;
	} else {
		root.jid = -1;
	}
	root.children = '';
	click(root);
	update(root);
}


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

//var tree = d3.layout.tree().size([height, width]);
var tree = d3.layout.tree().size([height, 100]);

var diagonal = d3.svg.diagonal()
    .projection(function(d) { return [d.y, d.x]; });


var vis;/* = d3.select("#treeview").append("svg")
    .attr("width", width + margin.right + margin.left)
    .attr("height", height + margin.top + margin.bottom)
    .attr("pointer-events", "all")
  .append("g")
    .attr("transform", "translate(" + margin.left + "," + margin.top + ")")
	.call(d3.behavior.zoom().scaleExtent([1, 8]).on("zoom", zoom))
	.append("g");

vis.append("rect")
  .attr("width", width + margin.right + margin.left)
  .attr("height", height + margin.top + margin.bottom)
  .attr('fill', '#fff');*/

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

root.myid=${myid};
root.type="${type}";
root.jid=${myid};
root.pid=-1;
var seen = [];
var rootstr = JSON.stringify(root, function(key, val) {
   if (typeof val == "object") {
        if (seen.indexOf(val) >= 0)
            return undefined;
        seen.push(val);
    }
    return val; });


//d3.json("../data/flare.json", function(json) {
//d3.json("../data/flare100.json", function(json) {
//d3.json("http://localhost:8080/wasp/jobresults/helpTag/getJSTreeJson.do?jobId=${jobId}", function(json) {
//d3.json("http://localhost:8080/wasp/jobresults/getTreeJson.do?id=${myid}&type=${type}", function(json) {
//d3.json("http://localhost:8080/wasp/jobresults/getTreeJson.do?id=${myid}&type=${type}&jid=${myid}&pid=-1", function(json) {	
d3.json("http://localhost:8080/wasp/jobresults/getTreeJson.do?node="+rootstr, function(json) {	
/*   if (height < json.children.length * min_branch_int) {
	  height = json.children.length * min_branch_int;
  }

  tree.size([height, width]);
  vis = d3.select("#treeview").append("svg")
    .attr("width", width + margin.right + margin.left)
    .attr("height", height + margin.top + margin.bottom)
    .attr("pointer-events", "all")
  .append("g")
    .attr("transform", "translate(" + margin.left + "," + margin.top + ")")
  .call(d3.behavior.zoom().scaleExtent([1, 8]).on("zoom", zoom)).on("dblclick.zoom", null)
  .append("g");
*/
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
    .attr('fill', '#fff');

  root = json;
  root.x0 = 0; // height / 2;
  root.y0 = 0;
  //root.children.forEach(collapse);
  update(root);
  click(root); click(root);
});

//var g = d3.select("g");
//g.call(drag);

function trim(root) {
	;
}

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

/*
  // Enter any new nodes at the parent's previous position.
  var nodeEnter = node.enter().append("g")
      .attr("class", "node")
      .attr("transform", function(d) { return "translate(" + source.y0 + "," + source.x0 + ")"; })
      .on("click", click)
      .on("mouseover", onMouseOver)
      .on("mouseout", onMouseOut);

  nodeEnter.append("circle")
      .attr("r", 1e-6)
      .style("fill", function(d) { return d._children ? "lightsteelblue" : "#fff"; });

  nodeEnter.append("text")
      .attr("x", function(d) { return d.children || d._children ? -10 : 10; })
      .attr("dy", ".35em")
      .attr("text-anchor", function(d) { return d.children || d._children ? "end" : "start"; })
      .text(function(d) { return d.name; })
      .style("fill-opacity", 1e-6)
      .attr("pointer-events", "all")
      .on("click", clickLabel);
*/
//Add checkbox
/* 	barHeight=20;
	barWidth=80;
	function color(d) {
	    return d._children ? "#3182bd" : d.children ? "#c6dbef" : "#fd8d3c";
	}

  nodeEnter.append("svg:rect")
  	.attr("x", function(d) { return d.children || d._children ? -10 : 10; })
  	.attr("y", -barHeight / 2)
  	.attr("height", barHeight)
  	.attr("width", barWidth)
  	.style("stroke", color)
  	.style("fill", "white")
  	.style("fill-opacity", 1e-6)
  	.on("click", function(d) {
    	if (d.selected) {
     		d.selected = false;
     		d3.select(this).attr("fill", "white");
    	} else {
     		d.selected = true;
     		d3.select(this).attr("fill", "black");
    	}
    });
 */  
/*   nodeEnter.append("svg:circle")
  	.attr("x", function(d) { return d.children || d._children ? -50 : 50; })
  	.attr("r", 5)
  	.attr("fill", "white")
  	.on("click", function(d) {
    	if (d.selected) {
     		d.selected = false;
     		d3.select(this).attr("fill", "white");
    	} else {
     		d.selected = true;
     		d3.select(this).attr("fill", "black");
    	}
    	//printSelectedNodes();
  	});
 */

 // Transition nodes to their new position.
/*  var nodeUpdate = node.transition()
      .duration(duration)
      .attr("transform", function(d) { return "translate(" + d.y + "," + d.x + ")"; });

  nodeUpdate.select("circle")
      .attr("r", 4.5)
      .style("fill", function(d) { return d._children ? "lightsteelblue" : "#fff"; });

  nodeUpdate.select("text")
      .style("fill-opacity", 1);

  // Transition exiting nodes to the parent's new position.
  var nodeExit = node.exit().transition()
      .duration(duration)
      .attr("transform", function(d) { return "translate(" + source.y + "," + source.x + ")"; })
      .remove();

  nodeExit.select("circle")
      .attr("r", 1e-6);

  nodeExit.select("text")
      .style("fill-opacity", 1e-6);

  // Update the links
  var link = vis.selectAll("path.link")
      .data(tree.links(nodes), function(d) { return d.target.id; });

  // Enter any new links at the parent's previous position.
  link.enter().insert("path", "g")
      .attr("class", "link")
      .attr("d", function(d) {
        var o = {x: source.x0, y: source.y0};
        return diagonal({source: o, target: o});
      });

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
*/
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
  
  $.ajax({
      url: '/wasp/jobresults/getDetailsJson.do?node='+dstr,
      type: 'GET',
      dataType: 'json',
      success: function (result) {
    	if (d.type=='dummy') return;  
      
      	d3.select('#detailview').select("h3").remove();
      	d3.select('#detailview').select("tbody").remove();
      	
      	//remove all tabs first
      	/*for (var i = $("div#tabs ul li").length  - 1; i >= 0; i--) {
			$('div#tabs').tabs('remove', i);
		} */
      	
/*      	$.each(result, function (index, item) {
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
		var headStr;
		if ((d.type.split('-'))[0]=="filetype") {
			headStr = "Download "+(d.type.split('-'))[1].toUpperCase()+" files";
		} else {
			headStr = d.type.toUpperCase()+" Details";
		}
     	d3.select('#detailview').append("h3").html(headStr);
      	var table = d3.select('#detailview').append("tbody");
        $.each(result, function (index, item) {
         	var row = table.append("tr");
         	row.append("td").html(index);

         	if (typeof item == 'string' || item instanceof String) {
          		row.append("td").html(item);
         	} else if (item.targetLink != undefined) {
         		row.append("td").html('<a href="'+item.targetLink+'">'+item.label+'</a>');
          	} else {
          		var td = row.append("td");
          		$.each(item, function (index2, item2) {
          			var row2 = td.append("tr");
              		row2.append("td").html(index2);
              		row2.append("td").html(item2);
          		});
          	}
        });

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

  toggle(d);
}

function color(d) {
	  return d._children ? "#3182bd" : d.children ? "#c6dbef" : "#fd8d3c";
	}

/*
// Toggle children on click.
function click(d) {
  if (labelClick) {
	  labelClick = false;
	  return;
  }
  if (d.children) {
    d._children = d.children;
    d.children = null;
  } else {
    d.children = d._children;
    d._children = null;
  }
  // Collapse all sibling nodes' children
  if (d.parent) {
	  d.parent.children.forEach(function(e) {
		  if(e!=d && e.children) {
			  collapse(e);
		  }
	  });
  }
  
  update(d);
}

var labelClick = false;

function clickLabel(d) {
  labelClick = true;
  $.ajax({
        url: '/wasp/jobresults/getDetailsJson.do?type='+d.type+'&id='+d.myid,
        type: 'GET',
        dataType: 'json',
        success: function (result) {
        	d3.select('#detailview').select("h3").remove();
        	d3.select('#detailview').select("tbody").remove();
        	
        	d3.select('#detailview').append("h3").html(d.type.toUpperCase()+" Details");
        	var table = d3.select('#detailview').append("tbody");
            $.each(result, function (index, item) {
           		var row = table.append("tr");
           		row.append("td").html(index);

           		if (typeof item == 'string' || item instanceof String) {
            		row.append("td").html(item);
            	} else {
            		var td = row.append("td");
            		$.each(item, function (index2, item2) {
            			var row2 = td.append("tr");
                		row2.append("td").html(index2);
                		row2.append("td").html(item2);
            		});
            	}
            });
        }
  });
}
*/
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