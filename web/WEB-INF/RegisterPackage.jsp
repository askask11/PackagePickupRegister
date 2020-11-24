<%-- 
    Document   : RegisterPackage
    Created on : Aug 4, 2020, 3:59:25 AM
    Author     : jianqing
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>取件码</title>
        <%@include file="/WEB-INF/jspf/headtags.jspf" %>
        <script src="https://www.recaptcha.net/recaptcha/api.js"  async defer></script>
        <link rel="stylesheet" href="css/popup-nojs.css">
    </head>
    <body>
        <div class="jumbotron text-center">
            <br>
            <h1>查询结果</h1>
            <br>
        </div>
        <c:if test="${empty guoguo}">

            <div class="text-center container">
                抱歉，验证码错误，请重新输入。
            </div>

            <form action="index" method="GET">
                <button class="btn btn-primary center-block" style="display: block; margin: auto;">
                    返回
                </button>
            </form>

        </c:if>

        <c:if test="${guoguo!=null}">

            <br>
            <div class="container center-text">
                <h4 class="center-text">
                    取件码
                </h4> 

                <strong class="center-text center-block">
                    ${guoguo}
                </strong>
                <br>
                
                <center>快递小哥辛苦了！！！！</center>
            </div>

        </c:if>

            <br><br>
        <footer class="text-center">

            <form action="ContactMe" method="POST" target="contactframe" id="fr">
                有任何疑问？<button class="g-recaptcha btn btn-primary" 
                              data-sitekey="6LdSILoZAAAAAF2f6ntG72Dj2gG2jNEqg8Q3Gpjh" 
                              data-callback='onSubmit' 
                              data-action='submit'
                              style="display: inline;">点击查看</button>我的微信，电话：
                              <input name="token"  type="hidden" id="tkip">
            </form>
            <script>
                function onSubmit(token) {
                    document.getElementById("tkip").value = token;
                    document.getElementById("fr").submit();
                    //console.log(token);
                }
            </script>
            <div id="popupcont" class="overlay">
                <div class="popup">
                    <h2>请通过验证</h2>
                    <a class="close" href="#">&times;</a>
                    <div class="content">

                        <div class="g-recaptcha" data-sitekey="6LddIroZAAAAADmmapX87hTJ-OdGIqo24SQ_5IBt"></div>
                        <br/>
                        <input type="submit" value="Submit">



                    </div>
                </div>
            </div>



            <iframe name="contactframe" style="width: 100%;
                    height: 150px;
                    "
                    frameborder="0">
                你的浏览器版本不支持此功能
            </iframe>
        </footer>    



    </body>
</html>
