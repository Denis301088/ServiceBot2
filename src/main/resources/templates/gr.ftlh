<#import "parts/common.ftlh" as c>
<@c.page>

<#--    role="tablist"-->
    <form method="post">

        <ul class="nav nav-pills mb-3" id="pills-tab">


            <li class="nav-item">

                <#--                                <input class="nav-link" data-toggle="pill" href="#like" type="radio" value="1" name="Лайк">-->
                <a class="nav-link active" id="home-tab" data-toggle="tab" href="#like" role="tab" aria-controls="like" aria-selected="true">Like
                    <input href="#" class="btn btn-primary btn-lg active" type="radio" value="Active" name="Active">
                </a>

                <#--                                    <input type="hidden" value="1" name="Лайк">-->
                <#--                               </a>-->
            </li>


            <#--                                 value="2" name="Подписка"&ndash;&gt;-->

            <li class="nav-item">

                <button class="nav-link" data-toggle="pill" href="#sub" aria-controls="sub" aria-selected="false" type="button" value="2" name="Подписка">Подписка</button>
                <#--                                <input type="hidden" value="2" name="Подписка">-->
            </li>


            <#--            <li class="nav-item">-->
            <#--                <a class="nav-link" id="pills-contact-tab" data-toggle="pill" href="#like" role="tab" aria-controls="like" aria-selected="false">Лайк+подписка</a>-->
            <#--            </li>-->
            <#--            <li class="nav-item">-->
            <#--                <a class="nav-link" id="pills-contact-tab" data-toggle="pill" href="#unsubscribe" role="tab" aria-controls="unsubscribe" aria-selected="false">Отписка</a>-->
            <#--            </li>-->

        </ul>



        <#--1)-->
        <div class="tab-content" id="pills-tabContent">
            <div class="tab-content" id="pills-tabContent">
                <div class="tab-pane fade" id="like" role="tabpanel" aria-labelledby="pills-home-tab">
                    <ul class="nav nav-pills mb-3" id="" role="tablist">
                        <ul class="nav nav-pills mb-3" id="pills-tab" role="tablist">
                            <li class="nav-item">
                                <a class="nav-link active" id="" data-toggle="pill" href="#user_audience" role="tab" aria-controls="user_audience" aria-selected="false" name="source">Аудитория пользователей</a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link" id="" data-toggle="pill" href="#user_list" role="tab" aria-controls="user_list" aria-selected="false" name="source">Список пользователей</a>
                            </li>
                        </ul>
                    </ul>

                    <div class="tab-content" id="">
                        <div class="tab-pane fade show active" id="user_audience" role="tabpanel" aria-labelledby="pills-profile-tab">
                            <div class="form-group">
                                <label for="exampleFormControlTextarea1">Введите логин(ы) пользователя-источника (без @):</label>
                                <textarea class="form-control" id="exampleFormControlTextarea1" rows="3"></textarea>
                            </div>
                        </div>


                        <div class="tab-pane fade" id="user_list" role="tabpanel" aria-labelledby="pills-profile-tab">
                            <div class="form-group" name="text">
                                <label for="exampleFormControlTextarea1">Введите список пользователей:</label>
                                <textarea class="form-control" id="exampleFormControlTextarea1" rows="3" name="text"></textarea>
                            </div>
                        </div>

                        <#--Фильтр-->
                        <#--                <div class="tab-content" id="pills-tabContent">-->
                        <#--                    <div class="tab-pane fade" id="" role="tabpanel" aria-labelledby="pills-home-tab">-->
                        <ul class="nav nav-pills mb-3" id="" role="tablist">
                            <ul class="nav nav-pills mb-3" id="pills-tab" role="tablist">
                                <li class="nav-item">
                                    <a class="nav-link active" id="" data-toggle="pill" href="" role="tab" aria-controls="" aria-selected="false" name="sourceUsers">Подписчиков</a>
                                </li>
                                <li class="nav-item">
                                    <a class="nav-link" id="" data-toggle="pill" href="" role="tab" aria-controls="" aria-selected="false" name="sourceUsers">Подписок</a>
                                </li>
                            </ul>
                        </ul>

                        <div class="form-group">
                            <label for="exampleFormControlInput1">Количество подписок/ Лайков/ Лайков+подписок</label>
                            <input type="email" class="form-control" id="exampleFormControlInput1" name="countSubscriptions" placeholder="Какое количество нужно выполнить?">
                        </div>
                        <#--                    </div>-->
                        <#--                </div>-->
                        <#--                    <ul class="nav nav-pills mb-3" id="pills-tab" role="tablist">-->
                        <#--                        <li class="nav-item">-->
                        <#--                            <a class="nav-link" id="" data-toggle="pill" href="#user_audience1" role="tab" aria-controls="user_audience1" aria-selected="false">Без фильтра</a>-->
                        <#--                        </li>-->
                        <#--                        <li class="nav-item">-->
                        <#--                            <a class="nav-link" id="" data-toggle="pill" href="#user_list1" role="tab" aria-controls="user_list1" aria-selected="false">Настроить фильтр</a>-->
                        <#--                        </li>-->
                        <#--                    </ul>-->
                    </div>
                </div>





                <#--                    </div>-->
                <#--                </div>-->
                <#--                    <ul class="nav nav-pills mb-3" id="pills-tab" role="tablist">-->
                <#--                        <li class="nav-item">-->
                <#--                            <a class="nav-link" id="" data-toggle="pill" href="#user_audience1" role="tab" aria-controls="user_audience1" aria-selected="false">Без фильтра</a>-->
                <#--                        </li>-->
                <#--                        <li class="nav-item">-->
                <#--                            <a class="nav-link" id="" data-toggle="pill" href="#user_list1" role="tab" aria-controls="user_list1" aria-selected="false">Настроить фильтр</a>-->
                <#--                        </li>-->
                <#--                    </ul>-->
                <#--                        </div>-->
            </div>

            <#--  =========================================================================================================================================-->

        </div>




        <div class="tab-pane fade" id="unsubscribe" role="tabpanel" aria-labelledby="pills-home-tab">
            <ul class="nav nav-pills mb-3" id="" role="tablist">
                <ul class="nav nav-pills mb-3" id="pills-tab" role="tablist">
                    <li class="nav-item">
                        <a class="nav-link" id="" data-toggle="pill" href="#user_audience1" role="tab" aria-controls="user_audience1" aria-selected="false">Взаимных</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" id="" data-toggle="pill" href="#user_list1" role="tab" aria-controls="user_list1" aria-selected="false">Невзаимных</a>
                    </li>
                </ul>
            </ul>
        </div>
        <div>
            <button type="submit" class="btn btn-primary">Save task</button>
        </div>
        </div>
        </div>

    </form>



</@c.page>