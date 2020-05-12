<%-- 
    Document   : listadoMisProductos
    Created on : 04-abr-2020, 18:17:28
    Author     : alici
--%>

<%@page import="ebuy.entity.Keyword"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.List"%>
<%@page import="ebuy.entity.Account"%>
<%@page import="ebuy.entity.Product"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="menu.css">
        <link rel="stylesheet" type="text/css" href="botones.css">
        <link rel="stylesheet" type="text/css" href="productCard.css">
        <title>Productos</title>
    </head>
    <%
        Account user;

        // Si el usuario está dentro de la sesión quiere decir que ya hizo login
        // por lo que se le redirige a menu.jsp
        user = (Account) session.getAttribute("user");
        if (user == null || user.getIsadmin() != 0) {
            response.sendRedirect("login.jsp");
            return;
        }
        List<Product> listadoProductos = (List<Product>) request.getAttribute("listadoProductos");

    %>
    <style>

        .msgBox {
            position: fixed;
            top: 50%;
            left: 50%;
            transform: translate(-50%, -50%);
            width: 600px;
            width: 600px;
            background-color: #ff9a9a;
        }

        .msgBox p {
            line-height: 1.5;
            padding: 10px 20px;
            color: #333;
            padding-left: 82px;
            background-position: 25px center;
            background-repeat: no-repeat;
        }

     
    </style>
    <body>

        <!-- BARRA NAVEGACION -->
        <div class="nav">
            <input type="checkbox" id="nav-check">
            <div class="nav-header">
                <div class="nav-title">
                    <%= user.getNickname()%>
                </div>
            </div>

            <div class="nav-links">
                <a href="ProductosListar" >BUSCAR PRODUCTOS</a>
                <a href="MisProductos" style="background-color:white;color: black;">MIS PRODUCTOS</a>
                <a href="ProductosHistorial" >HISTORIAL</a>
                <a href="Exit" >Cerrar Sesion</a>
            </div>

        </div>
        <div class="button-crear">
            <a href="ProductosCrear" >Crear Productos</a>
        </div>

        <!-- LISTA PRODUCTOS -->   
        <%
            if (listadoProductos == null || listadoProductos.isEmpty()) {
        %>          
        <h2>Ahora mismo no hay productos</h2>
        <%
        } else {
        %><table align="center" style="border-spacing: 50px;"> <%
            int i = 0;
            for (Product producto : listadoProductos) {

                if (i % 3 == 0) { %>
            <tr>
                <% }%>
                <td>
                    <div class="product-card">
                        <div class="badge"><a href="ProductoMostrar?productID=<%=producto.getProductId() %>">Ver Producto</a></div>
                        <div class="product-tumb">
                            <img src="<%=producto.getPhotoUrl()%>" alt="">
                        </div>
                        <div class="product-details">
                            <span class="product-catagory"> 
                                CATEGORIA: <%= producto.getSubcategoryId().getCategoryId().getName()%>
                                <br/>
                                SUBCATEGORIA: <%= producto.getSubcategoryId().getName()%>
                                <br/>
                                KEYWORDS:
                                <% for (Keyword kw : producto.getKeywordList()) {
                                %>
                                <%= kw.getName()%> |                               |
                                <% }%>
                            </span>
                            <h4><a href="ProductoMostrar?productID=<%=producto.getProductId() %>"><%=producto.getTitle()%></a></h4>
                            <p><%
                                String d = producto.getDescription();
                                if (d.length() > 43) {
                                    d = d.substring(0, 40) + "...";
                                }
                                %> <%=d%></p>
                            <div class="product-bottom-details">
                                <div class="product-price"><%=producto.getPrice()%>€</div>
                                <br/>
                                <div class="product-links">
                                    <i><%=new SimpleDateFormat("dd/MM/yyyy").format(producto.getCreationDate())%></i></a>
                                    <i><%=new SimpleDateFormat("HH:mm").format(producto.getCreationTime())%></i></a>
                                </div>
                                <div class="button-editar">

                                    <a href="ProductosEditar?id=<%= producto.getProductId()%>" >Editar</a>
                                </div>
                                <div class="button-eliminar">

                                    <a class="borrar" onclick="mostrarMensaje(<%= producto.getProductId()%>, '¿Desea borrar el producto: <%= producto.getTitle()%>?')" >Eliminar</a>         
                                </div>

                            </div>
                        </div>
                    </div>

                </td>

                <%
                    i++;
                    if (i % 3 == 0) { %>
            </tr>
            <% }
                } // for %>
            <table>
                <%} //if
                %>    



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
                        boton.href = "ProductosBorrar?id=" + id;
                        boton.textContent = "Confirmar";
                        panel.appendChild(boton); //anida el elemento msg al panel

                        var botonCancelar = document.createElement('a');
                        botonCancelar.textContent = "Cancelar";
                        botonCancelar.style.float= "right";
                        panel.appendChild(botonCancelar); //anida el elemento msg al panel

                       

                        botonCancelar.onclick = function () {
                            panel.parentNode.removeChild(panel);
                        }

                        panel.style.backgroundColor = 'red';

                    }

                </script>
                </html>
