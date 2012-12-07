<script type="text/javascript" src="/wasp/scripts/jquery/jquery-1.7.1.js"></script>

<script type="text/javascript" src="/wasp/scripts/jquery/jquery-ui-1.8.18/external/jquery.cookie.js"></script>
<script type="text/javascript" src="/wasp/scripts/jquery/jquery-ui-1.8.18.custom.min.js"></script>

<script type="text/javascript" src="/wasp/scripts/d3/d3.v2.js"></script>

<script>

var margin = {top: 20, right: 120, bottom: 20, left: 120},
    width = 460 - margin.right - margin.left,
    height = 700 - margin.top - margin.bottom,
    i = 0,
    duration = 500,
    branch_length = 100,
    min_branch_int = 40,
    root;

var tree = d3.layout.tree()
    			.size([height, width]);

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


//d3.json("../data/flare.json", function(json) {
//d3.json("../data/flare100.json", function(json) {
d3.json("http://localhost:8080/wasp/jobresults/helpTag/getJSTreeJson.do?jobId=${jobId}", function(json) {
	
  if (height < json.children.length * min_branch_int) {
	  height = json.children.length * min_branch_int;
  }
  
  tree.size([height, width]);
  vis = d3.select("#treeview").append("svg")
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
    .attr('fill', '#fff');

  root = json;
  root.x0 = height / 2;
  root.y0 = 0;
  root.children.forEach(collapse);
  update(root);
});

//var g = d3.select("g");
//g.call(drag);

function update(source) {

  // Compute the new tree layout.
  var nodes = tree.nodes(root).reverse();

  // Normalize for fixed-depth.
  nodes.forEach(function(d) { d.y = d.depth * branch_length; });

  // Update the nodes
  var node = vis.selectAll("g.node")
      .data(nodes, function(d) { return d.id || (d.id = ++i); });

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

  // Transition nodes to their new position.
  var nodeUpdate = node.transition()
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
}

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
        url: '/wasp/jobresults/helpTag/getDetailsJson.do?type='+d.type+'&id='+d.myid,
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