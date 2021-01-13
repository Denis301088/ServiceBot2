<#import "parts/common.ftlh" as c>
<@c.page>

<div class="form-row">
    <form method="post" action="/account">
        <div class="row">
            Добавить аккаунт:
            <div class="col">
                <input type="text" class="form-control" placeholder="Логин аккаунта без @" name="login">
            </div>
            <div class="col">
                <input type="text" class="form-control" placeholder="Пароль" name="password">
            </div>
            <input type="hidden" name="_csrf" value="${_csrf.token}" />
            <div>
                <button type="submit" class="btn btn-primary">Добавить</button>
            </div>
        </div>

    </form>
</div>


</@c.page>