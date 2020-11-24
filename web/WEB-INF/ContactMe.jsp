<%-- 
    Document   : ContactMe
    Created on : Aug 4, 2020, 11:58:22 AM
    Author     : jianqing
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>
            联系我</title>
    </head>
    <body>
        <c:set var="testfail" value="false"></c:set>
        <c:if test="${(gc!=null && gc.isSuccess() && gc.getScore() >0.5 ) || (param.code!=null && param.code.equals(captcha.getBody()))}">
        <center><h1>联系方式</h1>
            我的手机:18927461962<br>
            我的微信:932646988</center>


    </c:if>

    <c:if test="${empty gc ||!gc.isSuccess()}">
        <center>抱歉，您没有通过身份验证，无权访问此页面。</center>
        </c:if>

    <c:if test="${(gc!= null && gc.getScore() <= 0.5) || (param.code!=null && !param.code.equals(captcha.getBody()))}">
        <center>二次验证</center>
        <img src="Captcha" onclick="this.src = 'Captcha';" style="cursor: pointer;">

        <c:if test="${(param.code!=null && !param.code.equals(captcha.getBody()))}">
            <center>验证失败，请重试。</center>
        </c:if>
        
        <form action="ContactMe" method="POST" class="center-block text-center">
            <input name="code" type="text">
            <button type="submit">提交</button>
            请输入图片中的验证码，点击图片更换。
        </form>

    </c:if>


</body>
</html>
