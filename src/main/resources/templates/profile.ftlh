<#import "parts/common.ftlh" as c>

<@c.page>
<#--    <h5>${username}</h5>-->
<#--    ${message?ifExists}-->

    <#if message??>
        <div class="alert alert-${messageType}" role="alert">
            ${message}
        </div>
    </#if>

    <h5>Добавить аккаунт для проодвижения:</h5>

    <a class="btn btn-primary" data-toggle="collapse" href="#collapseExample" role="button" aria-expanded="false" aria-controls="collapseExample">
        Добавить аккаунт
    </a>

    <div class="collapse <#if account??>show</#if>" id="collapseExample" >
        <#--        Дополнительный компонент класса collapse-show фиксирует всхлопываемый компонент-->
        <div class="form-group mt-3">
            <form method="post" enctype="multipart/form-data">
                <div class="form-group">
                    <input type="text" class="form-control ${(loginError??)?string('is-invalid', '')}"
                           value="<#if account??>${account.login}</#if>" name="login" placeholder="Логин аккаунта без @" />
                    <#if loginError??>
                        <div class="invalid-feedback">
                            ${loginError}
                        </div>
                    </#if>
                </div>
                <div class="form-group">
                    <input type="text" class="form-control ${(passwordError??)?string('is-invalid','')}"
                           value="<#if account??>${account.password}</#if>" name="password" placeholder="Пароль">
                    <#if passwordError??>
                        <div class="invalid-feedback">
                            ${passwordError}
                        </div>
                    </#if>
                </div>
                <input type="hidden" name="_csrf" value="${_csrf.token}" />
                <#--                    <input type="hidden" name="id" value="<#if message??>${message.id}</#if>" />-->
                <div class="form-group">
                    <button type="submit" class="btn btn-primary">Добавить</button>
                </div>
            </form>
        </div>
    </div>

    </form>

    <h5>Аккаунты для продвижения:</h5>

    <form method="post">
        <div>
            <div class="btn-group" data-toggle="buttons">
                <#list accounts?if_exists as account>
                    <label class="btn btn-primary active">
                        <input type="radio" name="account" id="" autocomplete="off" <#if task??&&task.account??&&task.account.id==account.id>checked</#if> value="${account.id}"> ${account.login}
                    </label>
                </#list>
            </div>
        </div>
        <input type="hidden" name="_csrf" value="${_csrf.token}" />
        <button class="btn btn-primary" type="submit">Добавить аккаунт</button>
    </form>

    <h5>Изменить пароль от личного кабинета:</h5>
    <form method="post">

        <div class="form-group row">
            <label class="col-sm-2 col-form-label">Текущий пароль:</label>
            <div class="col-sm-6">
                <input type="password" name="password" class="form-control ${(passwordError??)?string('is-invalid', '')}" placeholder="Текущий пароль" />
                <#if passwordError??>
                    <div class="invalid-feedback">
                        ${passwordError}
                    </div>
                </#if>
            </div>
        </div>

        <div class="form-group row">
            <label class="col-sm-2 col-form-label">Новый пароль:</label>
            <div class="col-sm-6">
                <input type="password" name="password2" class="form-control ${(password2Error??)?string('is-invalid', '')}" placeholder="Новый пароль" />
                <#if password2Error??>
                    <div class="invalid-feedback">
                        ${password2Error}
                    </div>
                </#if>
            </div>
        </div>

        <div class="form-group row">
            <label class="col-sm-2 col-form-label">Новый пароль еще раз:</label>
            <div class="col-sm-6">
                <input type="password" name="password3" class="form-control ${(password3Error??)?string('is-invalid', '')}" placeholder="Новый пароль еще раз" />
                <#if password3Error??>
                    <div class="invalid-feedback">
                        ${password3Error}
                    </div>
                </#if>
            </div>
        </div>

        <input type="hidden" name="_csrf" value="${_csrf.token}" />
        <button class="btn btn-primary" type="submit">Изменить пароль</button>
    </form>

</@c.page>