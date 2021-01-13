<#import "parts/common.ftlh" as c>

<@c.page>

<form action="/newpass/form" method="post">

    <div class="mb-4">Смена пароля для пользователя
    <h5><#if user??>${user.username}</#if></h5>
    </div>
<#--    <#if errorNotValidEmail??>-->
<#--        <div class="alert alert-danger" role="alert">-->
<#--            ${errorNotValidEmail}-->
<#--        </div>-->
<#--    </#if>-->
    <input type="hidden" name="username" value="${user.username}" />
    <div class="form-group row">
        <label class="col-sm-2 col-form-label">Новый пароль:</label>
        <div class="col-sm-6">
            <input type="password" name="password"
                   class="form-control ${(passwordError??)?string('is-invalid', '')}"
                   placeholder="Пароль" />
            <#if passwordError??>
                <div class="invalid-feedback">
                    ${passwordError}
                </div>
            </#if>
        </div>
    </div>

    <div class="form-group row">
        <label class="col-sm-2 col-form-label">Подтвердите пароль:</label>
        <div class="col-sm-6">
            <input type="password" name="password2"
                   class="form-control ${(password2Error??)?string('is-invalid', '')}"
                   placeholder="Подтвердите пароль" />
            <#if password2Error??>
                <div class="invalid-feedback">
                    ${password2Error}
                </div>
            </#if>
        </div>
    </div>

    <input type="hidden" name="_csrf" value="${_csrf.token}" />
    <div>
        <button type="submit" class="btn btn-primary">Сменить пароль</button>
    </div>
</form>

</@c.page>