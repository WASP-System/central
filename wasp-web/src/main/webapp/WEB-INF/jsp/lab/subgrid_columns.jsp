<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
subGrid: true,
subGridUrl:'<wasp:relativeUrl value="lab/subgridJSON.do" />',
subGridModel: [ 
    {
	    name  : ['<fmt:message key="user.lab_member_list.label"/>'],
	    width : ['auto'],
	    align : ['center']                
    }
],
  