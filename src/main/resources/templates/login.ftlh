<#import "parts/common.ftlh" as c>
<#import "parts/login.ftlh" as l>
<@c.page>

<#if message??>
  <div class="alert alert-${messageType}" role="alert">
${message}
  </div>
  <#else>
  <#if Session?? && Session.SPRING_SECURITY_LAST_EXCEPTION??>
      <div class="alert alert-danger" role="alert">
          Неверные Email или пароль.
      </div>
  </#if>
</#if>
<@l.login "/login" false/>

</@c.page>