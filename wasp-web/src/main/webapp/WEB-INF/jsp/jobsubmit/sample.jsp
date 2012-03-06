<%-- 
Body of the "sample drafts" page

@author: Sasha Levchuk
--%>
<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
<br />

<h1><fmt:message key="jobDraft.create.label"/> -- <fmt:message key="${jobDraft.getWorkflow().getIName()}.jobsubmit/samples.label"/></h1>

<span id="waspMessage"><wasp:message /></span>
<span id="statusMessage"></span>
<span id="statusUpload"></span>
<span id="uploadError"></span>

<%@ include file="/WEB-INF/jsp/jobsubmit/jobsubmitinfo.jsp" %>

<div class="instructions">
   <fmt:message key="jobDraft.sample_instructions.label"/>
</div>


<script>
//build structure to hold meta fields allowed for the current workflowId
var _validMetaFields={};
<c:forEach items="${_metaBySubtypeList}" var="_entry" varStatus="_substatus">
	<c:set var="_subtype" value="${_entry.key}"/>
	<c:set var="_validMetaFields" value="${_entry.value}"/>

	_validMetaFields.subtypeSampleId_${_subtype.subtypeSampleId}=[];

	<c:forEach items="${_validMetaFields}" var="_validMeta">

		_validMetaFields.subtypeSampleId_${_subtype.subtypeSampleId}.push('${_validMeta.k}');

	</c:forEach>
</c:forEach>

//build structure to hold list of samples by subtype
var _jobsBySampleSubtype=<wasp:json object="${_jobsBySampleSubtype}"/>;

//re-define standard "edit" function to implement page-specific logic,
//i.e. the functionality to select historic samples 
var grid=$("#grid_id");      
var orgEditGridRow = grid.jqGrid.editGridRow; // save original function
$.jgrid.extend ({editGridRow : function(rowid, p){
    $.extend(p,
             { 
    		 beforeShowForm: function(form) {
    				
                	var subtypeSampleId;
                	
                	//disable the list of subtypes (because the subtype is already selected by the "typeful" add button)
                	var _select=document.getElementById(form[0].id).subtypeSampleId;
                	
                	_select.disabled="disabled";
                		
                	// the sample is considered not "cloned" initially
            		$('#cloned').attr('disabled','disabled');
            		$('#cloned').val('No');
            		
                	
                	if (p.subtypeSampleId) {//adding the sample
                		
                		subtypeSampleId=p.subtypeSampleId;
                	
                		//pre-select the subtype 
                		for (var i=0; i < _select.length; i++) {
                			if ( _select[i].value == subtypeSampleId) {
                				_select[i].selected = true;
                				break;
                			}
                		}
                		
                	} else {//edit
                		
                		subtypeSampleId = _select.options[_select.selectedIndex].value;
                	
                		var isCloned='Yes'==$('#cloned').val();
                		
                		//disable all fields if the sampe was "cloned"
                		if (isCloned) {
                			 disableAllFields();
                		}						
                	}
                	
                	//get the list of valid meta fields for the given subtype
                	
               		var _myValidMetaFields=_validMetaFields['subtypeSampleId_'+subtypeSampleId];
                
               		//hide meta fields not valid for the given subtype
                	for(var i in colModel) {
                		var k = colModel[i].name;
                		
                		if (k.indexOf('.')==-1) continue;//static field
                		
                		if ($.inArray(k, _myValidMetaFields) == -1) {//element is not valid for given subtype
                			
                			var jqName='#tr_'+k.replace(".","\\.");
                			$(jqName, form).hide();
                		}                 	
					}
               		
               		// disable status and clonedoption
					(document.getElementById(form[0].id).status).disabled="disabled";
					(document.getElementById(form[0].id).cloned).disabled="disabled";
                	
                	
                
               		//implement dynamic population of sub-selects
                	var _jobs4Subtype=_jobsBySampleSubtype[subtypeSampleId];
    				
                	populateSelect($('#jobId').get(0), _jobs4Subtype);
                	
                	$('#jobId').change(function () {      
                		var _val=$("#jobId option:selected").val();
                		
                		if (!_val) {
                			$('#sourceSampleId').children().remove().end();
                			enableAllFields();                    		            
                			return;
                		}
                		                		      				    
                		 var options = '<option value=""><fmt:message key="wasp.default_select.label" /></option>';
            			 
                		 $.getJSON("/wasp/jobsubmit/samplesByJobId.do",{jobId: _val, ajax: 'true'}, function(data, textStatus, jqXHR){
                		
                			 $.each(data, function (index, name) {                				    
                				 options += '<option value="' + index + '">' + name + '</option>';
                			  });
                			                 			  
                		   })
                		   
                		   $("#sourceSampleId").html(options);
                		                    	                		    
                      });
                	
                	$('#sourceSampleId').change(function () {      
                		var _val=$("#sourceSampleId option:selected").val();
                		var _label=$("#sourceSampleId option:selected").text();
                		if (!_val) {
                			enableAllFields();
                			return;
                		}
                		
                		var currentVars={};
                		
                		$('#name').val(_label);
                		$('#name').attr('disabled','disabled');
                		
                		$.each($("input, select"), function(i,v) {
     					    var theTag = v.tagName;                					   
     					    var theElement = $(v);
     					    
     					   if (typeof(theElement.attr('id')) == 'undefined' || theElement.attr('id') == null) {
     						  //alert('undef id for '+v.tagName);   
     						  return;
     					   } 
     					    
     					    if (theElement.attr('id').indexOf('.')==-1) return;//not a meta field
     					    
     					    var curVarName=theElement.attr('id').split(".")[1];
     					    
     					    currentVars[curVarName]=theElement;
     					  
     					    theElement.val('');	
     					    theElement.attr('disabled','disabled');
     					    
     					    $('#cloned').val('Yes');
     					    $('#cloned').attr('disabled','disabled');
     					});
                		                	
                		
                		$.getJSON("/wasp/jobsubmit/sampleMetaBySampleId.do",{sampleId: _val, ajax: 'true'}, function(data, textStatus, jqXHR) {
                			 
                			 $.each(data, function (index, name) {
                				 
                				 var histVarName=getSuffix(index);
                				 
                				 if (histVarName==null) return;
                				 
                				 var curEl=currentVars[histVarName];
                				
                				 if (curEl) {      
                					 //alert("setting value of element "+$('#chipseqLibrarySample'+histVarName).attr('id')+" to  "+name);
                					 //$('#chipseqLibrarySample'+histVarName).val(name);    
                					 curEl.val(name);           					    	
                				 }                			 
                		    })
                		 })
                      });
                	
                 }
             });
   			 orgEditGridRow.call(this,rowid, p);
}});

//disables all fields on the form
function disableAllFields() {
	
	$.each($("input, select"), function(i,v) {
		   	
		    var theElement = $(v);		    
		    
		    theElement.attr('disabled','disabled');
		});
}

//enables all fields on the form
function enableAllFields() {
	$('#name').attr('disabled',null);
	$('#name').val('');
	
	$('#cloned').val('No');
	
	$.each($("input, select"), function(i,v) {
		   	
		    var theElement = $(v);
		    
		    if (theElement.attr('id').indexOf('.')==-1) return;//not a meta field
		  		    
		    theElement.val('');
		    theElement.attr('disabled',null);
		});
}


function getSuffix(str) {
	 if (!str || str.indexOf('.')==-1) return null;//not a meta field	   
	 return str.split(".")[1];
}

function populateSelect(el, items) {
    el.options.length = 0;   
    el.options[0] = new Option('<fmt:message key="wasp.default_select.label" />', '');

    $.each(items, function (index,value) {    	
        el.options[el.options.length] = new Option(value, index);
    });
}



</script>

<table id="grid_id"></table> 
<div id="gridpager"></div>

<form method="POST">
<input class="FormElement ui-widget-content ui-corner-all" type="submit" value="<fmt:message key="jobDraft.next.label"/>">
</form>



