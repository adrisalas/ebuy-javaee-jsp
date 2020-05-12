<%-- 
    Document   : login
    Created on : 29-Mar-2020, 12:48:09
    Author     : adrsa
--%>

<%@page import="ebuy.entity.Account"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>E-Buy Login</title>
        <link rel="stylesheet" type="text/css" href="login.css"> 
        <link rel="stylesheet" type="text/css" href="menu.css">
    </head>
    <body>
    <%
        Account user;
        String status;
        String emailOrNickname;
        String signinstatus;
        String signinstatusok;
        String nickname;
        String email1;
        String email2;
        String password1;
        String password2;
        
        user = (Account)session.getAttribute("user");
        if (user != null) {
            if(user.getIsadmin() == 0){
                response.sendRedirect("listadoProductos.jsp");  
                return;
            } else {
                response.sendRedirect("adminMenu.jsp");  
                return;
            }
        } 
        
        //__LOGIN__
        status = (String)request.getAttribute("status");
        if (status == null) {
            status = "";
        }
        
        emailOrNickname = (String) request.getAttribute("emailOrNickname");
        if (emailOrNickname == null){
            emailOrNickname = "";
        }
        
        //__SIGNIN__
        signinstatus = (String)request.getAttribute("signinstatus");
        if (signinstatus == null) {
            signinstatus = "";
        }
        signinstatusok = (String)request.getAttribute("signinstatusok");
        if (signinstatusok == null) {
            signinstatusok = "";
        }
        nickname = (String)request.getAttribute("nickname");
        if (nickname == null) {
            nickname = "";
        }
        email1 = (String)request.getAttribute("email1");
        if (email1 == null) {
            email1 = "";
        }
        email2 = (String)request.getAttribute("email2");
        if (email2 == null) {
            email2 = "";
        }
        password1 = (String)request.getAttribute("password1");
        if (password1 == null) {
            password1 = "";
        }
        password2 = (String)request.getAttribute("password2");
        if (password2 == null) {
            password2 = "";
        }
    %>
        <div class="nav">
            <input type="checkbox" id="nav-check">
            <div class="nav-header">
                <div class="nav-title">
                    E-Buy
                </div>
            </div>
        </div>
        
        <div class="row">
            <div class="column">
                <form class="login-form" method="post" action="Authentification">
                    <h2 class="login-form__hdg">Iniciar sesión</h2>
                    <div class="login-form__field">
                            <div class="input-group">
                                <input class="input-group__input" type="text" placeholder=" " autocomplete="off" id="emailOrNickname" name="emailOrNickname" value="<%= emailOrNickname %>"/></td>                
                                <label class="input-group__label" for="emailOrNickname">Usuario</label>
                            </div>
                            <div class="input-group">
                                <input class="input-group__input" type="password" placeholder=" " autocomplete="off" name="password" id="password"/></td>                
                                <label class="input-group__label" for="password">Contrase&ntilde;a</label>
                            </div>              
                            <span style="color:red;"><%= status %></span>
                    </div>
                    <input class="login-btn" type="submit" value="Enviar"/>
                </form>
            </div>
        <div class="column">
                <form class="login-form" method="post" action="SignIn" onsubmit="return checkSignIn(this);">
                        <h2 class="login-form__hdg">Registrarse</h2>
                        <div class="login-form__field">
                            <div class="input-group">
                                <input class="input-group__input" type="text" placeholder=" " autocomplete="off" id="nickname" name="nickname" value="<%= nickname %>"/></td>                
                                <label class="input-group__label" for="nickname">Nickname</label>
                            </div>
                            <div class="input-group">
                                <input class="input-group__input" type="text" placeholder=" " autocomplete="off" id="email1" name="email1" value="<%= email1 %>"/></td>                
                                <label class="input-group__label" for="email1">Email</label>
                            </div>
                            <div class="input-group">
                                <input class="input-group__input" type="text" placeholder=" " autocomplete="off" id="email2" name="email2" value="<%= email2 %>"/></td>                
                                <label class="input-group__label" for="email2">Repite Email</label>
                            </div>
                            <div class="input-group">
                                <input class="input-group__input" type="password" placeholder=" " autocomplete="off" id="password1" name="password1" value="<%= password1 %>"/></td>                
                                <label class="input-group__label" for="password1">Contrase&ntilde;a</label>
                            </div>  
                            <div class="input-group">
                                <input class="input-group__input" type="password" placeholder=" " autocomplete="off" id="password2" name="password2" value="<%= password2 %>"/></td>                
                                <label class="input-group__label" for="password2">Repite Contrase&ntilde;a</label>
                            </div>              
                            <span style="color:red;" id="signinstatus"><%= signinstatus %></span>                    
                            <span style="color:#33A8FF;" id="signinstatus"><%= signinstatusok %></span>                    
                        </div>
                        <input  class="login-btn" type="submit" value="Registrarse"/>
                </form>
            </div>
        </div>
                        
<script type="text/javascript" language="JavaScript">
function checkSignIn(form) {
    var output = false;
    
    if(form.nickname.value == "" || form.email1.value == "" || form.email2.value == ""
            || form.password1.value == "" || form.password2.value == ""){
        document.getElementById("signinstatus").innerHTML = "ERROR. Debes rellenar todos los campos para registrarte";
    } else if (form.email1.value != form.email2.value){
        document.getElementById("signinstatus").innerHTML = "ERROR. Los email no coinciden";
    } else if(!validateEmail(form.email1.value)){
        document.getElementById("signinstatus").innerHTML = "ERROR. Email invalido";
    } else if (form.password1.value != form.password2.value){
        document.getElementById("signinstatus").innerHTML = "ERROR. Las contraseñas no coinciden";
    } else {
        output = true;
    }
    return output;
}

function validateEmail(email) {
  let re = /^[-a-z0-9~!$%^&*_=+}{\'?]+(\.[-a-z0-9~!$%^&*_=+}{\'?]+)*@([a-z0-9_][-a-z0-9_]*(\.[-a-z0-9_]+)*\.(aero|arpa|biz|com|coop|edu|gov|info|int|mil|museum|name|net|org|pro|travel|mobi|[a-z][a-z])|([0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}))(:[0-9]{1,5})?$/i;
  return re.test(email);
}
</script>         
    </body>
</html>

