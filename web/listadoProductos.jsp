<%-- 
    Document   : adminProductosListar
    Created on : 03-abr-2020, 13:04:06
    Author     : victoria
--%>

<%@page import="ebuy.entity.Category"%>
<%@page import="ebuy.entity.Keyword"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="ebuy.entity.Product"%>
<%@page import="ebuy.entity.Subcategory"%>
<%@page import="java.util.List"%>
<%@page import="ebuy.entity.Account"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="menu.css">
        <link rel="stylesheet" type="text/css" href="productCard.css">
        <link rel="stylesheet" type="text/css" href="botones.css">
        <title>Productos</title>
    </head>
    <%
        Account user;

        // Si el usuario está dentro de la sesión quiere decir que ya hizo login
        // por lo que se le redirige a menu.jsp
        user = (Account) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        List<Product> listadoProductos = (List<Product>) request.getAttribute("listadoProductos");

        //categorias
        List<Category> listaCategorias = (List) request.getAttribute("listaCategorias");
        String str_filtro_categoria = request.getParameter("filtro_categoria");

        //palabras clave
        List<Keyword> listaKeywords = (List) request.getAttribute("listaKeywords");
        String str_filtro_keywords = request.getParameter("filtro_keyword");

        //date
        String str_filtro_date = request.getParameter("filtro_date");
        if (str_filtro_date == null) {
            str_filtro_date = "";
        }

        //time 
        String str_filtro_time = request.getParameter("filtro_time");
        if (str_filtro_time == null) {
            str_filtro_time = "";
        }

        //titulo 
        String str_filtro_titulo = request.getParameter("filtro_titulo");
        if (str_filtro_titulo == null) {
            str_filtro_titulo = "";
        }

        //descripcion 
        String str_filtro_descripcion = request.getParameter("filtro_descripcion");
        if (str_filtro_descripcion == null) {
            str_filtro_descripcion = "";
        }

    %>
    <body>

        <!-- BARRA NAVEGACION -->
        <div class="nav">
            <input type="checkbox" id="nav-check">
            <div class="nav-header">
                <div class="nav-title">
                    <%= user.getNickname()%>
                </div>
            </div>
            <%if (user.getIsadmin() == 1) { %>
            <div class="nav-links">
                <a href="UsuariosListar"  >USUARIOS</a>
                <a href="ProductosListar" style="background-color:white;color: black;">PRODUCTOS</a>
                <a href="Exit" >Cerrar Sesion</a>
            </div>
        </div>
        <% } else {%>

        <div class="nav-links">
            <a href="ProductosListar" style="background-color:white;color: black;">BUSCAR PRODUCTOS</a>
            <a href="MisProductos" >MIS PRODUCTOS</a>
            <a href="ProductosHistorial" >HISTORIAL</a>
            <a href="Exit" >Cerrar Sesion</a>
        </div>
        <% }%>

        <h1>FILTROS</h1>

        <!-- FILTROS -->   
        <form action="ProductosListar" method="post" >
            <table align="center" >
                <tr>
                    <td style="padding-left: 50px;">
                        CATEGORIA:
                        <select name="filtro_categoria">
                            <option value="">--Todas--</option>                
                            <%
                                for (Category c : listaCategorias) {
                                    String selected = "";
                                    if (str_filtro_categoria != null && !str_filtro_categoria.isEmpty()
                                            && str_filtro_categoria.equals(c.getCategoryId().toString())) {
                                        selected = "selected";
                                    }
                            %>
                            <option <%= selected%> value="<%= c.getCategoryId()%>" > <%= c.getName()%> </option>
                            <%
                                }
                            %>   
                        </select>
                        <br/>
                        PALABRAS CLAVE:
                        <select name="filtro_keyword">
                            <option value="">--Todas--</option>                
                            <%
                                for (Keyword k : listaKeywords) {
                                    String selected = "";

                                    if (str_filtro_keywords != null && !str_filtro_keywords.isEmpty()
                                            && str_filtro_keywords.equals(k.getKeywordId().toString())) {
                                        selected = "selected";
                                    }

                            %>
                            <option <%= selected%> value="<%= k.getKeywordId()%>" > <%= k.getName()%> </option>
                            <%
                                }
                            %>   
                        </select>
                    </td>
                    <td style="padding-left: 50px;">
                        Dia:
                        <input type="date" name="filtro_date" value="<%= str_filtro_date%>"/>
                        <br/>
                        Hora:
                        <input type="time" name="filtro_time" value="<%= str_filtro_time%>"/>
                    <td style="padding-left: 50px;">
                        Titulo: <input type="text" name="filtro_titulo" value="<%= str_filtro_titulo%>" /> <br/>
                        Descripcion <input type="text" name="filtro_descripcion" value="<%= str_filtro_descripcion%>" />
                    </td>

                <br/>
                </tr>
            </table>
            <br/>
            <input  class="button-filtrar" type="submit" value="Filtrar" />

        </form> 
        <hr/>            

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
                </html>
