<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
</br>
<span style="color:blue;font-size:200%" id='statusMessage'></span>
<span style="color:blue;font-size:200%" id='uploadStatus'></span>
<span style="color:red;font-size:200%;" id='uploadError'></span>

<a href="<c:url value="/jobsubmit/cells/${jobdraftId}.do" />">Cell Assignment</a>
</br>
</br>

<script>



var _validMetaFields={};
<c:forEach items="${_metaBySubtypeList}" var="_entry" varStatus="_substatus">
<c:set var="_subtype" value="${_entry.key}"/>
<c:set var="_validMetaFields" value="${_entry.value}"/>

_validMetaFields.subtypeSampleId_${_subtype.subtypeSampleId}=[];

<c:forEach items="${_validMetaFields}" var="_validMeta">

_validMetaFields.subtypeSampleId_${_subtype.subtypeSampleId}.push('${_validMeta.k}');

</c:forEach>
</c:forEach>


var _jobsBySampleSubtype=<wasp:json object="${_jobsBySampleSubtype}"/>;


var grid=$("#grid_id");      // your jqGrid (the <table> element)
var orgEditGridRow = grid.jqGrid.editGridRow; // save original function
$.jgrid.extend ({editGridRow : function(rowid, p){
    $.extend(p,
             { // modify some parameters of editGridRow
    	beforeShowForm: function(form) {
                	                	
                	var subtypeSampleId;
                	
                	var _select=document.getElementById(form[0].id).subtypeSampleId;
                	
                	_select.disabled="disabled";
                	
                	if (p.subtypeSampleId) {//add
                		
                		subtypeSampleId=p.subtypeSampleId;
                	
                		for (var i=0; i < _select.length; i++) {
                			if ( _select[i].value == subtypeSampleId) {
                				_select[i].selected = true;
                				break;
                			}
                		}
                		
                	} else {//edit
                		
                		subtypeSampleId = _select.options[_select.selectedIndex].value;                		                		
                		
                	}
               		var _myValidMetaFields=_validMetaFields['subtypeSampleId_'+subtypeSampleId];
                	//alert(subtypeSampleId);
               		//alert(_myValidMetaFields);
                	for(var i in colModel) {
                		var k = colModel[i].name;
                		
                		if (k.indexOf('.')==-1) continue;//static field
                		
                		if ($.inArray(k, _myValidMetaFields) == -1) {//element is not valid for given subtype
                			
                			var jqName='#tr_'+k.replace(".","\\.");
                			
                			$(jqName, form).hide();
                		}                 	
					}
                	
                	
                	var _jobs4Subtype=_jobsBySampleSubtype[subtypeSampleId];
    				
                	populateSelect($('#jobId').get(0), _jobs4Subtype);

                	$('#jobId').change(function () {      
                		var _val=$("#jobId option:selected").val();
                		
                		if (!_val) return;
                		                		      				    
                		 var options = '<option value="">--select--</option>';
            			 
                		 $.getJSON("/wasp/jobsubmit/samplesByJobId.do",{jobId: _val, ajax: 'true'}, function(data, textStatus, jqXHR){
                		
                			 $.each(data, function (index, name) {                				    
                				 options += '<option value="' + index + '">' + name + '</option>';
                			  });
                			                 			  
                		   })
                		   
                		   $("#jobSampleId").html(options);
                		                    	                		    
                      }).change();
                	
                	$('#jobSampleId').change(function () {      
                		var _val=$("#jobSampleId option:selected").val();
                		
                		if (!_val) return;
                		
                		var currentVars={};
                		
                		$.each($("input, select"), function(i,v) {
     					    var theTag = v.tagName;                					   
     					    var theElement = $(v);
     					    
     					    if (theElement.attr('id').indexOf('.')==-1) return;//not a meta field
     					    
     					    var curVarName=theElement.attr('id').split(".")[1];
     					    currentVars[curVarName]=theElement;
     					    theElement.val('');    					    
     					});
                		                	
                		
                		$.getJSON("/wasp/jobsubmit/sampleMetaBySampleId.do",{sampleId: _val, ajax: 'true'}, function(data, textStatus, jqXHR) {
                			 
                			 $.each(data, function (index, name) {
                				 
                				 var histVarName=getSuffix(index);
                				 
                				 if (histVarName==null) return;
                				 
                				 var curEl=currentVars[histVarName];
                				
                				 if (curEl) {
                					 curEl.val(name);           					    	
                				 }                			 
                		    })
                		 })
                      }).change();
                	
                 }
             });
    orgEditGridRow.call (this,rowid, p);
}});

function getSuffix(str) {
	 if (!str || str.indexOf('.')==-1) return null;//not a meta field	   
	 return str.split(".")[1];
}
function populateSelect(el, items) {
    el.options.length = 0;   
    el.options[0] = new Option('--select--', '');

    $.each(items, function (index,value) {
    	//alert(odump(this));
        el.options[el.options.length] = new Option(value, index);
    });
}



</script>

<table id="grid_id"></table> 
<div id="gridpager"></div>


