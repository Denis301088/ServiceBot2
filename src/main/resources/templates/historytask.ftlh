<#import "parts/common.ftlh" as c>
<@c.page>

<#--    <#list tasks as task>-->
<#--        <div class="accordion" id="accordionExample">-->

<#--            <div class="card">-->
<#--                <div class="card-header" id="headingOne">-->
<#--                    <h5 class="mb-0">-->
<#--                        <a class="btn btn-link" type="button" data-toggle="collapse" data-target="#collapseOne" aria-expanded="true" aria-controls="collapseOne">-->
<#--                            Разворачиваемая панель #1-->
<#--                        </a>-->
<#--                    </h5>-->
<#--                </div>-->

<#--                <div id="collapseOne" class="collapse" aria-labelledby="headingOne" data-parent="#accordionExample">-->
<#--                    <div class="card-body">-->
<#--                        <form method="post" action="newtask">-->
<#--                            <input type="hidden" name="_csrf" value="${_csrf.token}" />-->
<#--                            <div class="form-group">-->
<#--                                <button type="submit" class="btn btn-primary">Повторить задание</button>-->
<#--                            </div>-->
<#--                        </form>-->
<#--                        <div class="form-group">-->
<#--                            <a class="btn btn-primary" href="/newtask">Редактировать задание</a>-->
<#--                        </div>-->
<#--                    </div>-->
<#--                </div>-->
<#--            </div>-->

<#--        </div>-->
<#--    </#list>-->

<#--     <#list tasks as task>-->
<#--         <p>-->
<#--&lt;#&ndash;         <table>&ndash;&gt;-->
<#--&lt;#&ndash;             <thead>&ndash;&gt;-->
<#--&lt;#&ndash;             <tr>&ndash;&gt;-->
<#--                 <th>${task.startTime}</th>-->
<#--                 <th>${task.act?ifExists}</th>-->
<#--                 <th>${task.userLogins?ifExists}</th>-->
<#--                 <th><a class="text" data-toggle="collapse" href="#collapseExample${task.id}" role="button" aria-expanded="false" aria-controls="collapseExample">Повторить задание</a></th>-->
<#--&lt;#&ndash;             </tr>&ndash;&gt;-->

<#--&lt;#&ndash;             </thead>&ndash;&gt;-->
<#--&lt;#&ndash;         </table>&ndash;&gt;-->
<#--         </p>-->
<#--         <div class="collapse" id="collapseExample${task.id}">-->
<#--             <form method="post" action="newtask">-->
<#--                 <input type="hidden" name="_csrf" value="${_csrf.token}" />-->
<#--                 <div class="form-group">-->
<#--                     <button type="submit" class="btn btn-primary">Повторить сразу</button>-->
<#--                 </div>-->
<#--             </form>-->
<#--             <div class="form-group">-->
<#--                 <a class="btn btn-primary" href="/newtask">Редактировать и повторить</a>-->
<#--             </div>-->
<#--         </div>-->
<#--     </#list>-->


    <#if messageErrorsRepeatTasks??>
           <div class="alert alert-danger" role="alert">
            ${messageErrorsRepeatTasks}
           </div>
    </#if>


    <table class="table table-sm">
        <thead class="thead-dark">
        <tr>
            <th scope="col">№ Задания</th>
            <th scope="col">Статус</th>
            <th scope="col">Аккаунт</th>
            <th scope="col">Действие</th>
            <th scope="col">Источник</th>
            <th scope="col">Начало</th>
            <th scope="col">Конец</th>
            <th scope="col">Задано</th>
            <th scope="col">Выполнено</th>
            <th scope="col"></th>
            <th scope="col"></th>
        </tr>
        </thead>

        <tbody>

            <#list tasks?ifExists as task>
                <tr>
                    <th scope="row">${task.id?string["0"]}</th>
                    <td>${task.statusTask?ifExists.toString()}</td>
                    <td>${task.account?ifExists.login?ifExists}</td>
                    <td>${task.act?ifExists.toString()}</td>
                    <td>${task.userLogins?ifExists}</td>
                    <td>${task.startTime}</td>
                    <td>${task.stopTime?ifExists}</td>
                    <td>${task.countSubscriptions?ifExists}</td>
                    <td>${task?ifExists.presentCountSubscriptions}</td>
                    <td>
                        <#if task.statusTask?ifExists.toString()=="Работает">
                            <form method="post" action="stoptask">

                                <input type="hidden" name="_csrf" value="${_csrf.token}" />
                                <div class="form-group">
                                    <input type="hidden" value="${task.id?string["0"]}" name="stopId">
                                    <button type="submit" class="btn btn-danger">Остановить</button>
                                </div>
                            </form>
                            <#else>
                        <form method="post" action="repeattask">

                            <input type="hidden" name="_csrf" value="${_csrf.token}" />
                            <div class="form-group">
                                <input type="hidden" value="${task.id?string["0"]}" name="taskId">
                                <button type="submit" class="btn btn-primary">Повторить</button>
                            </div>
                        </form>
                    </td>
                    <td>
                        <div class="form-group">
                            <a class="btn btn-primary" href="/newtask/?task=${task.id?string["0"]}">Редактировать</a>
                        </div>
                        </#if>

                    </td>
                </tr>
                <#else>
                Список заданий пуст.
            </#list>

        </tbody>
    </table>


</@c.page>