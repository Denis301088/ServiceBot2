<#import "parts/common.ftlh" as c>
<#import "parts/login.ftlh" as l>


<@c.page>
    <div class="mb-4">Регистрация</div>
    <#if errorNotValidEmail??>
        <div class="alert alert-danger" role="alert">
            ${errorNotValidEmail}
        </div>
    </#if>
    <@l.login "/registration" true />
</@c.page>