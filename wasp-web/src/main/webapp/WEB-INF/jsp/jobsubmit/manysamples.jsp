<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<script type="text/javascript" src="/wasp/scripts/jquery/jquery.table.addrow.js"></script>
<script type="text/javascript">
	(function($){ 
		$(document).ready(function(){
			
			var selectedAdaptorSet=$('select#adaptorset option:selected').val();
			if (!selectedAdaptorSet) {
				$('tr#row_adaptor').hide();
			}
			
			
		    $(function(){
				$('select#adaptorset').change(function() {      
						var selectedAdaptorSet=$('select#adaptorset option:selected').val();
						var options = '';
						var url = "/wasp/jobsubmit/adaptorsByAdaptorsetId.do?adaptorsetId=" + selectedAdaptorSet;
						var adaptorCount = 0;
						if (!selectedAdaptorSet) {
							$('tr#row_adaptor').hide();
							$('select#adaptor').children().remove().end();                 		            
							return;
						}
						 
						$.ajax({
							async: false,
						   	url: url,
						    dataType: "json",
						    success: function(data) {
						    	$.each(data, function (index, name) {         				    
									options += '<option value="' + index + '">' + name + '</option>\n';
									adaptorCount++;
								});
						    }
						});   
						if (adaptorCount > 1){
							options = '<option value=""><fmt:message key="wasp.default_select.label" /></option>' + options;
						}
						$('select#adaptor').html(options);
						$('tr#row_adaptor').show();
				   	});
			    });
			$(".addRow").btnAddRow();
			$(".delRow").btnDelRow();
		    $( "#dialog-form" ).dialog({
		        autoOpen: false,
		        height: 200,
		        width: 420,
		        modal: true,
		        buttons: {
		          "Apply": function() {
		        	  var costToApplyToAllSettableLibraries = $("#costToApplyToAllSettableLibraries").val();
		        	  var regex = /^([0-9])+$/;
		        	  if(regex.test(costToApplyToAllSettableLibraries)){
		        	  	$(".settableLibraryCost").val(costToApplyToAllSettableLibraries);
		        	  	$("#costToApplyToAllSettableLibraries").val("");
		        	  	$("#validateTipForLibraryCostModalDialogForm").text("");
		        	  	$( this ).dialog( "close" );
		        	  }
		        	  else{
		        		  $("#validateTipForLibraryCostModalDialogForm").text("Whole numbers only");
		        	  }
		          },
		          Cancel: function() {
		        	$("#costToApplyToAllSettableLibraries").val("");
			        $("#validateTipForLibraryCostModalDialogForm").text("");		        	
		            $( this ).dialog( "close" );
		          }
		        },
		        close: function() {
		          	$("#costToApplyToAllSettableLibraries").val("");
		        	$("#validateTipForLibraryCostModalDialogForm").text("");	 
		        	$( this ).dialog( "close" );
		        }
		      });	
		    /*	not used, but use find for discountReason and its use in an anchor, below
			$(function() {
			    var availableTags = [
			      "ActionScript",
			      "AppleScript",
			      "Asp",
			      "BASIC",
			      "C",
			      "C++",
			      "Clojure",
			      "COBOL",
			      "ColdFusion",
			      "Erlang",
			      "Fortran",
			      "Groovy",
			      "Haskell",
			      "Java",
			      "JavaScript",
			      "Lisp",
			      "Perl",
			      "PHP",
			      "Python",
			      "Ruby",
			      "Scala",
			      "Scheme"
			    ];
			    $( "#discountReason" ).autocomplete({
			      source: availableTags
			    });
			  });
			*/
		});
	})(jQuery);
</script>



<br />

<h1><fmt:message key="jobDraft.create.label"/> -- <c:out value="${heading}"/></h1>

<%@ include file="/WEB-INF/jsp/jobsubmit/jobsubmitinfo.jsp" %>
<div id="container_div_for_adding_rows" >
<h2>Sample Type: <c:out value="${sampleDraft.sampleType.name}" /></h2>
<h2>Sample Subtype: <c:out value="${sampleDraft.sampleSubtype.name}" /></h2>
<c:set var="colspan" value = '0' scope="request"/>
<table class="data" style="margin: 0px 0px" >
<tr class="FormData">
	<td style="background-color:#FAF2D6; font-weight:bold" nowrap>Sample Name<span style="color:red">*</span></td>
	<c:set var="colspan" value = '${colspan + 1}' scope="request"/>
     <c:set var="_area" value = "sampleDraft" scope="request"/>
	 <c:set var="_metaList" value = "${normalizedMeta}" scope="request" />		
     <c:forEach items="${_metaList}" var="_meta" varStatus="status">
		<c:if test="${_meta.property.formVisibility != 'ignore'}">
			<c:set var="_myArea">${_area}.</c:set>
			<c:set var="_myCtxArea">${_area}.</c:set>
			<c:if test="${_metaArea != null}">		
				<c:set var="_myCtxArea">${_metaArea}.</c:set>
			</c:if>
			<c:set var="labelKey" value="${_meta.property.label}" />
			<c:if test="${fn:contains(labelKey,'Average')}">
				<c:set var="labelKey" value="${fn:replace(labelKey, 'Average', 'Aver.')}" />
			</c:if>
			<c:if test="${fn:contains(labelKey,'Fragmentation')}">
				<c:set var="labelKey" value="${fn:replace(labelKey, 'Fragmentation', 'Frag.')}" />
			</c:if>
			<c:if test="${fn:contains(labelKey,'Concentration')}">
				<c:set var="labelKey" value="${fn:replace(labelKey, 'Concentration', 'Conc.')}" />
			</c:if>
			<c:if test="${fn:contains(labelKey,'Volume')}">
				<c:set var="labelKey" value="${fn:replace(labelKey, 'Volume', 'Vol.')}" />
			</c:if>
			<c:set var="id" value="${fn:substringAfter(_meta.k,'.')}" />
			<td style="background-color:#FAF2D6; font-weight:bold" nowrap>${labelKey}
				<c:if test="${not empty _meta.property.constraint}">
					<span style="color:red">*</span>
				</c:if>
				<c:if test="${not empty _meta.property.tooltip}">
					<wasp:tooltip value="${_meta.property.tooltip}" />
				</c:if>	
				<br /><a href="javascript:void(0);"  onclick='alert("test"); var ele = document.getElementById("antibody"); alert(ele.value); ele.value="robert dubin";' >copy first to all</a>		
				<br /><a href="javascript:void(0);"  onclick='alert("test"); var dateRE = /^antibody/; var dates=[]; var els=document.getElementsByTagName("*"); alert(els.length); for (var i=0; i < els.length; i++){ if ( dateRE.test(els[i].id) ){ dates.push(els[i]); } } alert(dates.length);' >copy first to all</a>		
				<br /><a href="javascript:void(0);"  onclick='alert("test"); var dateRE = /^antibody/; var dates=[]; var els=document.getElementsByTagName("*"); alert(els.length); for (var i=0; i < els.length; i++){ if ( dateRE.test(els[i].id) ){ els[i].value = "robert dubin"; } } alert(dates.length);' >copy first to all</a>		
				<br /><a href="javascript:void(0);"  onclick='var foundFirstOne = false; var valueOfFirst = ""; alert("test"); var dateRE = /^antibody/; var dates=[]; var els=document.getElementsByTagName("*"); alert(els.length); for (var i=0; i < els.length; i++){ if ( dateRE.test(els[i].id) ){ if(foundFirstOne==false){foundFirstOne=true; valueOfFirstOne = els[i].value;} els[i].value = valueOfFirstOne; } } alert(dates.length);' >copy first to all</a>		
				<br /><a href="javascript:void(0);"  onclick='var foundFirstOne = false; var valueOfFirst = ""; var idRE = /^<c:out value="${id}" />/; var dates=[]; var els=document.getElementsByTagName("*"); for (var i=0; i < els.length; i++){ if ( idRE.test(els[i].id) ){ if(foundFirstOne==false){foundFirstOne=true; valueOfFirstOne = els[i].value;} els[i].value = valueOfFirstOne; } } ' >copy first to all</a>		
			</td>
			<c:set var="colspan" value = '${colspan + 1}' scope="request"/>
		</c:if>
	</c:forEach>
	<td style="background-color:#FAF2D6; font-weight:bold" nowrap>Action</td>
	<c:set var="colspan" value = '${colspan + 1}' scope="request"/>
</tr>

<tr>
	<td><input type="text" class="FormElement ui-widget-content ui-corner-all"   name='sampleName' id='sampleName' ></td>
	<c:forEach items="${_metaList}" var="_meta" varStatus="status">
		<c:if test="${_meta.property.formVisibility != 'ignore'}">
			<c:set var="_myArea">${_area}.</c:set>
			<c:set var="_myCtxArea">${_area}.</c:set>
			<c:if test="${_metaArea != null}">		
				<c:set var="_myCtxArea">${_metaArea}.</c:set>
			</c:if>
			<c:set var="labelKey" value="${_meta.property.label}" />
			<c:set var="id" value="${fn:substringAfter(_meta.k,'.')}" />
	
			
				<td class="DataTD">
				<c:choose>
					<c:when test="${not empty _meta.property.control}">
					
				    <%-- this tag will define selectItems/itemValue/itemLabel request attributes --%>
				    <wasp:metaSelect control="${_meta.property.control}"/>
	       			<select class="FormElement ui-widget-content ui-corner-all" name="${_area}Meta_${_meta.k}" id="${id}" class="FormElement ui-widget-content ui-corner-all">
						<c:if test= "${fn:length(selectItems) > 1 && _meta.property.formVisibility != 'immutable'}">
							<option value=''><fmt:message key="wasp.default_select.label"/></option>
						</c:if>
						<c:set var="useDefault" value="0" />
						<c:if test="${not empty _meta.property.defaultVal}">
							<c:set var="useDefault" value="1" />
							<c:forEach var="option" items="${selectItems}">
								<c:if test="${option[itemValue] == _meta.v}" >
									<c:set var="useDefault" value="0" />
								</c:if>
							</c:forEach>
						</c:if>
						<c:forEach var="option" items="${selectItems}">
							<c:if test="${fn:length(selectItems) == 1 || option[itemValue] == _meta.v || _meta.property.formVisibility != 'immutable' }">
								<option value="${option[itemValue]}"<c:if test="${fn:length(selectItems) == 1 || option[itemValue] == _meta.v || (useDefault==1 && option[itemValue] == _meta.property.defaultVal)}"> selected</c:if>>
								<c:out value="${option[itemLabel]}"/></option>
							</c:if>
						</c:forEach>																									
					</select>
										 						
					</c:when>
					<c:otherwise>
						<c:set var="inputVal" value="${_meta.v}" />
						<c:if test="${(empty inputVal) &&  (not empty _meta.property.defaultVal)}">
							<c:set var="inputVal" value="${_meta.property.defaultVal}" />
						</c:if>
						<input type="text" class="FormElement ui-widget-content ui-corner-all" size="10" maxlength="25" name="${_area}Meta_${_meta.k}" id="${id}"  value="${inputVal}" <c:if test= "${_meta.property.formVisibility == 'immutable'}"> readonly="readonly"</c:if> />
					</c:otherwise>
				</c:choose>
				<%-- 
				<c:if test="${not empty _meta.property.constraint}">
					<span class="requiredField">*</span>
				</c:if>
				<c:if test="${not empty _meta.property.tooltip}">
					<wasp:tooltip value="${_meta.property.tooltip}" />
				</c:if>
				--%>
				</td>		
			
				<%--<td class="CaptionTD error"><form:errors path="${_area}Meta[${status.index}].k" /> </td>	  --%>				 
			
		</c:if>			 
	</c:forEach>
	<td align='center'><input type="button" class="delRow" value="Delete Row"/></td>
</tr>
<tr><td colspan="${colspan}" align="center"><input style="width:300" type="button" class="addRow" value="ADD ADDITIONAL ROW"/></td></tr>
</table>
</div>
<%--
<form:form cssClass="FormGrid" commandName="sampleDraft">
<form:hidden path='sampleSubtypeId' />
<form:hidden path='sampleTypeId' />
<table class="EditTable ui-widget ui-widget-content">
  	 <tr class="FormData">
      <td class="CaptionTD"><fmt:message key="jobDraft.sample_name.label"/>:</td>
      <td class="DataTD"><form:input cssClass="FormElement ui-widget-content ui-corner-all" path="name" /><span class="requiredField">*</span></td>
      <td class="CaptionTD error"><form:errors path="name" /></td>
     </tr>
     <tr class="FormData"><td class="CaptionTD"><fmt:message key="jobDraft.sample_type.label"/>:</td><td class="DataTD"><c:out value="${sampleDraft.sampleType.name}" /></td><td class="CaptionTD error"><form:errors path="" /></td></tr>
     <tr class="FormData"><td class="CaptionTD"><fmt:message key="jobDraft.sample_subtype.label"/>:</td><td class="DataTD"><c:out value="${sampleDraft.sampleSubtype.name}" /></td><td class="CaptionTD error"><form:errors path="" /></td></tr>
     <c:set var="_area" value = "sampleDraft" scope="request"/>
	 <c:set var="_metaList" value = "${normalizedMeta}" scope="request" />		
     <c:import url="/WEB-INF/jsp/meta_rw.jsp" />
     <tr class="FormData">
        <td colspan="3" align="left" class="submitBottom">
        	<input type="submit" name="submit" value="<fmt:message key="jobDraft.cancel.label"/>" />
            <input type="submit" name="submit" value="<fmt:message key="jobDraft.save.label"/>" />
        </td>
    </tr>
</table>
</form:form>
 --%>