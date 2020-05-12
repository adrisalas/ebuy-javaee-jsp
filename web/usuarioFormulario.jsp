<%-- 
    Document   : usuario
    Created on : 15-abr-2020, 12:59:33
    Author     : Alberto
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
          <link rel="stylesheet" type="text/css" href="formulario.css">
        <title>Usuario de Ebuy</title>
    </head>
    <%
    Account cliente = (Account)request.getAttribute("cliente");
    
    String usuarioID = "", nickname = "", email = "", pwd = "", isAdmin = "";
    
    if(cliente != null){
        usuarioID = "" + cliente.getUserId();
        nickname = cliente.getNickname();
        email = cliente.getEmail();
        pwd = cliente.getPassword();
        isAdmin = "" + cliente.getIsadmin();
    }
    
   // List<Account> listaUsuarios = (List) request.getAttribute("listaUsuarios");
    %>
    <body>
                <div class="nav">
            <input type="checkbox" id="nav-check">
            <div class="nav-header">
                <div class="nav-title">
                    <%= cliente.getNickname()%>
                </div>
            </div>

            <div class="nav-links">
                <a href="UsuariosListar" style="background-color:white;color: black;" >USUARIOS</a>
                <a href="ProductosListar" >PRODUCTOS</a>
                <a href="Exit" >Cerrar Sesion</a>
            </div>
        </div>
        <form action="UsuariosGuardar">
            <input type="hidden" name="usuarioId" value="<%= usuarioID %>" />
        <table>
            <tr>
                <td>Nickname:</td>
                <td><input type="text" name="nickname" value="<%= nickname %>" size="30" max="30" maxlength="30" /></td>                
            </tr>
            <tr>
                <td>Correo electr&oacute;nico:</td>
                <td><input type="text" name="email" value="<%= email %>" size="30" max="30" maxlength="30" /></td>                
            </tr>
            <tr>
                <td>Contraseña:</td>
                <td><input type="text" name="pwd" value="<%= pwd %>" size="25" max="25" maxlength="25" /></td>                
            </tr>
            <tr>
                <td>Admin:</td>
                <%
                    if(Integer.parseInt(isAdmin) == 1){
                %>        
                
                <td><input type="checkbox" id="isAdmin" name="isAdmin" value="<%= isAdmin %>" checked /></td>  
                
                <%    
                    } else {
                %>   
                
                <td><input type="checkbox" id="isAdmin" name="isAdmin" value="<%= isAdmin %>" /></td> 
                
                <%
                    }
                %>
            </tr>  
            
            <tr>
                <td> <input type="submit" value="Confirmar"/></td>
            </tr>
        </table>
        </form>
    </body>
</html>
