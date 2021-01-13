<#assign
known = Session.SPRING_SECURITY_CONTEXT??
>

<#if known>
    <#assign
    curuser = Session.SPRING_SECURITY_CONTEXT.authentication.principal
    name = curuser.getUsername()
<#--    isAdmin = user.isAdmin()-->
    currentUserId=curuser.getId()
    >
<#else>
    <#assign
<#--    user=null-->
    name = "unknown"
<#--    isAdmin = false-->
    currentUserId=-1
    >
</#if>