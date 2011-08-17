<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<wasp:field name="login" object="user" />
<wasp:field name="password" object="user" />
<wasp:field name="firstName" object="user" />
<wasp:field name="lastName" object="user" />
<wasp:field name="email" object="user" />
<wasp:field name="locale" object="user" />
<wasp:field name="isActive" object="user" />

email.jq['editrules']={custom:true,custom_func:_validate_email};

isActive.jq['edittype']='checkbox';
isActive.jq['editoptions']={value:"1:0"}; 
isActive.jq['formatter']='checkbox';
isActive.jq['formatoptions']={disabled : true};
isActive.jq['align']='center';
isActive.jq['search']=false;

locale.jq['edittype']='select';
locale.jq['editoptions']={value:{}};
locale.jq['search']=false;

password.jq['hidden']=true;
password.jq['edittype']='password';
password.jq['editrules']={edithidden:true};
password.jq['formoptions']={};//{elmsuffix:'<font color=red>*</font>'};
