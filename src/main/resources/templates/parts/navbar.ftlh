<#include "security.ftlh">
<#import "login.ftlh" as l>
<nav class="navbar navbar-expand-lg navbar-light bg-light">
    <a class="navbar-brand" href="/">ServiceBot</a>
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>

    <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav mr-auto">

            <@l.logout />

            <#if !curuser??>
                <div class="form-group">
                    <a class="btn btn-primary" href="/registration">Попробовать бесплатно</a>
                </div>
            </#if>

        </ul>

<#--        <div class="navbar-text mr-3"><#if user??>${name}<#else>Please, login</#if></div>-->
    </div>
</nav>
<#if curuser??>
<nav class="navbar navbar-expand-lg navbar-light bg-light">
    <div class="form-group">
        <a class="btn btn-primary" href="/newtask">Создать задание</a>
    </div>

    <div class="form-group">
        <a class="btn btn-primary" href="/history">История заданий</a>
    </div>

    <div class="form-group">
        <li class="navbar navbar-expand-lg navbar-light bg-light">
            <a class="nav-link dropdown-toggle" data-toggle="dropdown" href="#" role="button" aria-haspopup="true" aria-expanded="false">${curuser.username}</a>
            <div class="dropdown-menu">
                <a class="dropdown-item" href="/user/profile">Настройки аккаунта</a>
                <a class="dropdown-item" href="#">Продлить подписку</a>
<#--                <a class="dropdown-item" href="#">Something else here</a>-->
                    <form action="/logout" method="post">
                        <input type="hidden" name="_csrf" value="${_csrf.token}" />
                        <input class="dropdown-item" type="submit" value="Выйти">
                    </form>
            </div>
        </li>
    </div>

</nav>
</#if>