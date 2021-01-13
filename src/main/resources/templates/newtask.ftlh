<#import "parts/common.ftlh" as c>
<@c.page>

    <#if messageErrorsTasks??>
        <div class="alert alert-danger" role="alert">
            <#list messageErrorsTasks as messageErrorsTask>
                <div>${messageErrorsTask}</div>
            </#list>
        </div>
    </#if>

    <form method="post" action="/account">

        <#if messageAuthorization??>

            <div class="alert alert-<#if isErrorAuthorization>success<#else>danger</#if>" role="alert">
                ${messageAuthorization}
            </div>

        </#if>

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

<#--========================================================-->

    <form method="post" action="/newtask">


        <div>
            <div class="btn-group" data-toggle="buttons">
                <#list accounts as account>
                    <label class="btn btn-primary active">
                        <input type="radio" name="account" id="" autocomplete="off" <#if task??&&task.account??&&task.account.id==account.id>checked</#if> value="${account.id}">${account.login}
                    </label>
                </#list>
            </div>
        </div>


            <div class="btn-group" data-toggle="buttons">
                <label class="btn btn-primary">
                    <input type="radio" name="act" id="option1" autocomplete="off" <#if task??&&(task.act.name())??&&task.act.name()=="LIKE">checked<#else>checked</#if> value="LIKE"> Лайк
                </label>
                <label class="btn btn-primary">
                    <input type="radio" name="act" id="option2" autocomplete="off" <#if task??&&(task.act.name())??&&task.act.name()=="SUBSCRIPTION">checked</#if>  value="SUBSCRIPTION"> Подписка
                </label>
                <label class="btn btn-primary">
                    <input type="radio" name="act" id="option3" autocomplete="off" <#if task??&&(task.act.name())??&&task.act.name()=="LIKEPLUSSUBSCRIPTION">checked</#if>  value="LIKEPLUSSUBSCRIPTION"> Лайк + Подписка
                </label>
                <label class="btn btn-primary">
                    <input type="radio" name="act" id="option4" autocomplete="off" <#if task??&&(task.act.name())??&&task.act.name()=="UNSUBSCRIBE">checked</#if>  value="UNSUBSCRIBE">Отписка
                </label>
            </div>

        <div>Отписываться от:</div>
        <div class="btn-group" data-toggle="buttons">
            <label class="btn btn-primary active">
                <input type="radio" name="filterUnsubscribe.unsubscribeFrom" id="option1" autocomplete="off"<#if task??&&task.filterUnsubscribe??&&task.filterUnsubscribe.unsubscribeFrom.ordinal()==0>checked</#if> value="ALL"> Всех
            </label>
            <label class="btn btn-primary active">
                <input type="radio" name="filterUnsubscribe.unsubscribeFrom" id="option2" autocomplete="off"<#if task??&&task.filterUnsubscribe??&&task.filterUnsubscribe.unsubscribeFrom.ordinal()==1>checked</#if> value="RECIPROCAL"> Взаимных
            </label>
            <label class="btn btn-primary">
                <input type="radio" name="filterUnsubscribe.unsubscribeFrom" id="option3" autocomplete="off" <#if task??&&task.filterUnsubscribe??&&task.filterUnsubscribe.unsubscribeFrom.ordinal()==2>checked<#elseif !(task.filterUnsubscribe.unsubscribeFrom)??>checked</#if> value="NONRECIPROCAL"> Невзаимных
            </label>
            <label class="btn btn-primary">
                <input type="radio" name="filterUnsubscribe.unsubscribeFrom" id="option4" autocomplete="off" <#if task??&&task.filterUnsubscribe??&&task.filterUnsubscribe.unsubscribeFrom.ordinal()==3>checked</#if> value="PRIVATE_NOTACCEPTED"> Приватных непринятых
            </label>
        </div>


            <div class="form-group" >
                <label for="exampleFormControlTextarea1">Введите логин(ы) пользователя-источника (без @):</label>
                <textarea class="form-control" id="exampleFormControlTextarea1" rows="3" name="userLogins"><#if task??&&task.userLogins??>${task.userLogins}</#if></textarea>
            </div>



            <div>Наличие аватарки в профиле:</div>
            <div class="btn-group" data-toggle="buttons">
                <label class="btn btn-primary active">
                    <input type="radio" name="filter.avatar" id="option1" autocomplete="off"<#if task??&&task.filter.avatar?if_exists==true>checked<#else>checked</#if> value="true"> Да
                </label>
                <label class="btn btn-primary">
                    <input type="radio" name="filter.avatar" id="option2" autocomplete="off" <#if task??&&task.filter.avatar?if_exists==false>checked</#if> value="false"> Не важно
                </label>
            </div>



            <div class="form-group">
                <label for="exampleFormControlInput1">Количество подписок/ Лайков/ Лайков+подписок</label>
                <input type="number" class="form-control" id="exampleFormControlInput1" name="countSubscriptions" value="<#if task??&&task.countSubscriptions??>${task.countSubscriptions}</#if>" placeholder="Какое количество нужно выполнить?">
            </div>


        <div>Пропускать аккаунты, на которые уже выполнялись какие-либо действия:</div>
        <div class="btn-group" data-toggle="buttons">
            <label class="btn btn-primary ">
                <input type="radio" name="filter.skipOldAccounts" id="option1" autocomplete="off" <#if task??&&task.filter.skipOldAccounts?if_exists==true>checked<#else>checked</#if> value="true"> Да
            </label>
            <label class="btn btn-primary active">
                <input type="radio" name="filter.skipOldAccounts" id="option2" autocomplete="off" <#if task??&&task.filter.skipOldAccounts?if_exists==false>checked</#if> value="false"> Нет
            </label>
        </div>

        <div>Пропускать приватные аккаунты:</div>
        <div class="btn-group" data-toggle="buttons">
            <label class="btn btn-primary">
                <input type="radio" name="filter.privateAccount" id="option1" autocomplete="off" <#if task?? && (task.filter.privateAccount)==true>checked</#if> value="true"> Да
            </label>
            <label class="btn btn-primary active">
                <input type="radio" name="filter.privateAccount" id="option2" autocomplete="off" <#if task?? && task.filter.privateAccount==false>checked<#elseif !task??>checked</#if> value="false"> Нет
            </label>
        </div>

        <div>Пол аккаунта:</div>
        <div class="btn-group" data-toggle="buttons">
            <label class="btn btn-primary">
                <input type="radio" name="filter.gender" autocomplete="off" <#if (task.filter.gender.name())?? && task.filter.gender.name()=="MAN">checked</#if> value="MAN">Мужской
            </label>
            <label class="btn btn-primary">
                <input type="radio" name="filter.gender" autocomplete="off" <#if (task.filter.gender.name())?? && task.filter.gender.name()=="WOMEN">checked</#if> value="WOMEN">Женский
            </label>
            <label class="btn btn-primary">
                <input type="radio" name="filter.gender" autocomplete="off" <#if (task.filter.gender.name())?? && task.filter.gender.name()=="NOGENDER">checked<#else></#if><#if !(task.filter.gender)??>checked</#if> value="NOGENDER">Не учитывать
            </label>
        </div>

        <div>Пропускать коммерческие аккаунты:</div>
        <div class="btn-group" data-toggle="buttons">
            <label class="btn btn-primary ">
                <input type="radio" name="filter.commercialAccount" id="option1" autocomplete="off" <#if task??&&task.filter.commercialAccount?if_exists==true>checked<#else>checked</#if> value="true"> Да
            </label>
            <label class="btn btn-primary active">
                <input type="radio" name="filter.commercialAccount" id="option2" autocomplete="off" <#if task??&&task.filter.commercialAccount?if_exists==false>checked</#if> value="false"> Нет
            </label>
        </div>

        <div>Пропускать аккаунты иностранцев:</div>
        <div class="btn-group" data-toggle="buttons">
            <label class="btn btn-primary ">
                <input type="radio" name="filter.alienAccount" id="option1" autocomplete="off" <#if task??&&task.filter.alienAccount?if_exists==true>checked<#else>checked</#if> value="true"> Да
            </label>
            <label class="btn btn-primary active">
                <input type="radio" name="filter.alienAccount" id="option2" autocomplete="off" <#if task??&&task.filter.alienAccount?if_exists==false>checked</#if> value="false"> Нет
            </label>
        </div>


        <div>Фильтр:</div>
        <div class="form-row">
            Количество подписчиков у пользователя:
            <div class="col">
                <input type="number" class="form-control" placeholder="От" name="filter.min_subscription" value="<#if task??&&(task.filter.min_subscription)??>${task.filter.min_subscription}</#if>">
            </div>
            <div class="col">
                <input type="number" class="form-control" placeholder="До" name="filter.max_subscription" value="<#if task??&&(task.filter.min_subscription)??>${task.filter.max_subscription}</#if>">
            </div>
        </div>


        <div class="form-row">
            Количество подписок у пользователя:
            <div class="col">
                <input type="number" class="form-control" placeholder="От" name="filter.min_subscribers" value="<#if task??&&(task.filter.min_subscribers)??>${task.filter.min_subscribers}</#if>">
            </div>
            <div class="col">
                <input type="number" class="form-control" placeholder="До" name="filter.max_subscribers" value="<#if task??&&(task.filter.min_subscribers)??>${task.filter.max_subscribers}</#if>">
            </div>
        </div>

        <div class="form-row">
            Количество публикаций у пользователя:
            <div class="col">
                <input type="number" class="form-control" placeholder="От" name="filter.min_publication" value="<#if task??&&(task.filter.max_publication)??>${task.filter.min_publication}</#if>">
            </div>
            <div class="col">
                <input type="number" class="form-control" placeholder="До" name="filter.max_publication" value="<#if task??&&(task.filter.max_publication)??>${task.filter.max_publication}</#if>">
            </div>
        </div>

        <div class="form-group" >
            Последняя публикация у пользователя не старше:
            <label for="exampleFormControlTextarea1">
                <input type="number" class="form-control" id="exampleFormControlTextarea1" name="filter.date_publication" value="<#if task??&&(task.filter.date_publication)??>${task.filter.date_publication}</#if>"></textarea>
            </label>
        </div>

        <div class="form-group" >
            <label for="exampleFormControlTextarea1">Подписываться на аккаунты, содержащие в описании профиля слова:</label>
            <textarea class="form-control" id="exampleFormControlTextarea1" rows="4" name="filter.whiteWords" placeholder="Каждое слово с новой строки${"\n"}первое слово${"\n"}второе слово и т. д."><#if task??&&task.filter.whiteWords??>${task.filter.whiteWords}</#if></textarea>
        </div>

        <div class="form-group" >
            <label for="exampleFormControlTextarea1">Пропускать аккаунты, содержащие в описании профиля слова:</label>
            <textarea class="form-control" id="exampleFormControlTextarea1" rows="4" name="filter.stopWords" placeholder="Каждое слово с новой строки${"\n"}первое слово${"\n"}второе слово и т. д."><#if task??&&task.filter.stopWords??>${task.filter.stopWords}</#if></textarea>
        </div>

        <div class="form-group" >
            <label for="exampleFormControlTextarea1">Пропускать пользователей из списка:</label>
            <textarea class="form-control" id="exampleFormControlTextarea1" rows="4" name="filter.listUsersIgnore" placeholder="Каждый логин с новой строки${"\n"}ivan301088${"\n"}anna12_87 и т.д."><#if task??&&(task.filter.listUsersIgnore)??>${task.filter.listUsersIgnore}</#if></textarea>
        </div>

        <input type="hidden" name="_csrf" value="${_csrf.token}" />
        <div>
            <button type="submit" class="btn btn-primary">Запустить задание</button>
        </div>

    </form>



</@c.page>