<%-- 
    Document   : adminMenu
    Created on : 29-Mar-2020, 12:15:16
    Author     : adrsa
--%>

<%@page import="java.util.List"%>
<%@page import="ebuy.entity.Account"%>
<%@page import="ebuy.servlet.*"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="menu.css">
        <link rel="stylesheet" type="text/css" href="productCard.css">
        <link rel="stylesheet" type="text/css" href="adminMenu.css">
        <title>E-buy - Admin Menu</title>
    </head>
    <%
        Account user;

        // Si el usuario está dentro de la sesión quiere decir que ya hizo login
        // por lo que se le redirige a menu.jsp
        user = (Account) session.getAttribute("user");
        if (user == null || user.getIsadmin() == 0) {
            response.sendRedirect("login.jsp");
            return;
        }
    %>

    <body>

        <div class="nav">
            <input type="checkbox" id="nav-check">
            <div class="nav-header">
                <div class="nav-title">
                    <%= user.getNickname()%>
                </div>
            </div>

            <div class="nav-links">
                <a href="UsuariosListar" style="background-color:white;color: black;" >USUARIOS</a>
                <a href="ProductosListar" >PRODUCTOS</a>
                <a href="Exit" >Cerrar Sesion</a>
            </div>
        </div>
        <%
            List<Account> listaUsuarios = (List) request.getAttribute("listaUsuarios");
        %>
        <br/>
        <table class="center" align="center" style="width:80%;overflow-y:auto;">
            <tr>
                <th>ID</th>
                <th>NICKNAME</th>
                <th>EMAIL</th>
                <th>CONTRASEÑA</th>         
                <th></th>
                <th>ADMIN</th>
                <th>EDITAR</th>
                <th>BORRAR</th>
            </tr>
            <%
                for (Account cuenta : listaUsuarios) {
            %>
            <tr>
                <td><%= cuenta.getUserId()%></td>
                <td><%= cuenta.getNickname()%></td>
                <td><%= cuenta.getEmail()%></td>
                <td><%= cuenta.getPassword()%><td>         
                <td><%= cuenta.getIsadmin()%></td>
                <td><a href="UsuariosEditar?id=<%= cuenta.getUserId()%>">Editar</a></td>
                <td><a class="borrar" onclick="mostrarMensaje(<%= cuenta.getUserId()%>, '¿Desea borrar el cliente: <%= cuenta.getNickname()%>?')">Borrar</a></td>    
            </tr>
            <%
                }
            %>
        </table>        

    </body>
    <script>
        var b = document.querySelector('.borrar'); //referencia al botom

        function mostrarMensaje(id, msgText) {
            var html = document.querySelector('html');

            var panel = document.createElement('div');
            panel.setAttribute('class', 'msgBox'); // le agregamos el atributo class="msgBox"
            html.appendChild(panel);

            var msg = document.createElement('p');
            msg.textContent = msgText;
            panel.appendChild(msg); //anida el elemento msg al panel

            var boton = document.createElement('a');
            boton.href = "UsuariosBorrar?id=" + id;
            boton.textContent = "Confirmar";
            panel.appendChild(boton); //anida el elemento msg al panel

            var botonCancelar = document.createElement('a');
            botonCancelar.textContent = "Cancelar";
            botonCancelar.style.float = "right";
            panel.appendChild(botonCancelar); //anida el elemento msg al panel



            botonCancelar.onclick = function () {
                panel.parentNode.removeChild(panel);
            }

            panel.style.backgroundColor = 'red';

        }

    </script>
</html>
