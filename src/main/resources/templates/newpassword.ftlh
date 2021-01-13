<#import "parts/common.ftlh" as c>
<@c.page>
    <#if messageEmail??>
        <div class="alert alert-${messageType}" role="alert">
            ${messageEmail}
        </div>
    </#if>
Восстановление пароля к аккаунту

    <form action="newpass" method="post">
        <div class="form-group row">
            <label class="col-sm-2 col-form-label">E-mail:</label>
            <div class="col-sm-6">
                <input type="text" name="username" value="<#if user??>${user.username}</#if>"
                       class="form-control ${(usernameError??)?string('is-invalid', '')}"
                       placeholder="E-mail" />
                <#if usernameError??>
                    <div class="invalid-feedback">
                        ${usernameError}
                    </div>
                </#if>
            </div>
        </div>
        <input type="hidden" name="_csrf" value="${_csrf.token}" />
        <div>
            <button type="submit" class="btn btn-primary">Отправить письмо</button>
        </div>
    </form>
</@c.page>