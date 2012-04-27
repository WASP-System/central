<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
subGrid: true,
subGridUrl:'/wasp/job/subgridJSON.do',
subGridModel: [ 
    {
	    name  : [
	    	'<fmt:message key="sample.name.label"/>', 
	    	'<fmt:message key="sample.type.label"/>', 
	    	'<fmt:message key="sample.subtype.label"/>', 
	    	'<fmt:message key="sample.isreceived.label"/>'
	    ],
	    width : ['auto', 'auto', 'auto', 'auto'],
	    align : ['center', 'center', 'center', 'center']                
    }
],
  