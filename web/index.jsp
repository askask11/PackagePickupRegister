<%-- 
    Document   : index
    Created on : Aug 3, 2020, 9:01:35 PM
    Author     : jianqing
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%@include file="/WEB-INF/jspf/headtags.jspf" %>
        
        <title>获取取件码</title>
    </head>
    <body>

        <div class="jumbotron text-center">
            <br>
            <h1>欢迎快递小哥 &#128230;</h1>
            <h4>&#128682; 门牌号码：1A3001</h4>
            请核对门牌号是否正确

        </div>

        <div class="container">
            <h2 class="text-center">请输入包裹上的验证码 &#128273;</h2>


            <form action="RegisterPackage" method="POST" class="needs-validation" novalidate>

                <div class="form-group">
                    <label for="pwd">验证码 &#128273;</label>
                    <input type="text" class="form-control" id="pwd" placeholder="请输入验证码" name="code" required>
                    <div class="valid-feedback">合法</div>
                    <div class="invalid-feedback">请输入包裹上的验证码</div>
                </div>
                
                <div class="form-group">
                    <label for="phone">手机号 &#128222;</label>
                    <input type="number" class="form-control" id="pwd" placeholder="请输入手机号" name="phone" required>
                    <div class="valid-feedback">合法</div>
                    <div class="invalid-feedback">请输入可供联系的手机号</div>
                </div>


                <div class="form-group form-check">
                    <label class="form-check-label">
                        <input class="form-check-input" type="checkbox" name="remember" required> 我确认已接收快件。
                        <div class="valid-feedback">感谢确认</div>
                        <div class="invalid-feedback">请确认接收以查看收件码</div>
                    </label>
                </div>
                <center>点击提交后，提交时间和你的手机将会自动记录。取件码将会在下一页显示 </center>
                <br>
                <button type="submit" class="btn btn-primary" style="display: block; margin: auto;">
                    提交
                </button>

            </form>

          <script src="js/validateform.js"></script>
          
          <footer class="text-center">
              <br><br>
              
          </footer>
        </div>
    </body>
</html>
