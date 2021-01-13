<#import "parts/common.ftlh" as c>
<@c.page>

    <form method="post">
        <ul class="nav nav-pills">


            <li class="nav-item" value="1">
<#--                <input href="#" class="btn btn-primary btn-lg active" type="radio" value="Link" name="Link">-->
                <a class="nav-link active" id="home-tab" data-toggle="tab" href="#home" role="tab" aria-controls="home" aria-selected="true">Home</a>

            </li>
            <li class="nav-item" value="2">
                <input href="#" class="btn btn-primary btn-lg active" type="radio" value="Active" name="Active">
            </li>

<#--            value="2" name="Подписка"-->
<#--            value="1" name="Лайк"-->
        </ul>
        <div>
            <button type="submit" class="btn btn-primary">Save task</button>
        </div>



    </form>

</@c.page>