<%-- 
    Document   : productoMostrar
    Created on : 09-Apr-2020, 16:47:25
    Author     : adrsa
--%>

<%@page import="java.util.ArrayList"%>
<%@page import="ebuy.entity.Review"%>
<%@page import="ebuy.dao.ProductFacade"%>
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
        <link rel="stylesheet" type="text/css" href="productoMostrar.css">
        <link rel="stylesheet" type="text/css" href="mensajes.css">
        <link rel="stylesheet" type="text/css" href="valoracion.css">
        <link rel="stylesheet" type="text/css" href="popup.css">

        <title>Productos</title>
    </head>
    <style>table {
            border-collapse: separate;
            border-spacing: 15px 50px;

        }
    </style>
    <%
        Account user;
        Product product;
        String statusComprar;
        String statusComprarOK;

        // Si el usuario está dentro de la sesión quiere decir que ya hizo login
        // por lo que se le redirige a menu.jsp
        user = (Account) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        product = (Product) request.getAttribute("product");

        statusComprar = (String) request.getAttribute("statusComprar");
        if (statusComprar == null) {
            statusComprar = "";
        }
        statusComprarOK = (String) request.getAttribute("statusComprarOK");
        if (statusComprarOK == null) {
            statusComprarOK = "";
        }

        List<Review> listadoValoraciones = (List<Review>) request.getAttribute("listadoValoraciones");
        double mediaValoraciones = (Double) request.getAttribute("mediaValoraciones");
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

        <hr/>

        <div class="row">
            <div class="column"> 
                <h1><%=product.getTitle()%></h1>
                <div class="product-tumb">
                    <img src="<%=product.getPhotoUrl()%>" alt="">
                </div>
                <div class="product-details">
                    <span class="product-catagory"> 
                        CATEGORIA: <%= product.getSubcategoryId().getCategoryId().getName()%>
                        <br/>
                        SUBCATEGORIA: <%= product.getSubcategoryId().getName()%>
                        <br/>
                        VENDEDOR: <%= product.getVendorId().getNickname() %>
                        <br/>
                        KEYWORDS:
                        <% for (Keyword kw : product.getKeywordList()) {
                        %>
                        <%= kw.getName()%> |                               |
                        <% }%>
                        
                    </span>
                    <p><%
                        String description = product.getDescription();
                        %> <%=description%></p>
                    <div class="product-bottom-details">
                        <div class="product-price"><%=product.getPrice()%>€</div>
                        <br/>
                        <div class="product-links">
                            <i><%=new SimpleDateFormat("dd/MM/yyyy").format(product.getCreationDate())%> </i></a>
                            <i><%=new SimpleDateFormat("HH:mm").format(product.getCreationTime())%>  </i></a>
                        </div>
                    </div>
                </div>
            </div>
            <div class="column" style="margin-top: 50px;">
                <p> Quedan <b><%=product.getQuantity()%></b> unidades de este producto.</p>
                
                <% 
                    if(user.getIsadmin() != 1 && user.getUserId() != product.getVendorId().getUserId()) { 
                %>
                <form method="post" action="ProductosComprar">
                    <input type="hidden" id="product" name="productID" value="<%=product.getProductId()%>">    
                    <input type="number" id="quantity" name="quantity" required value=0 min="0" max="<%=product.getQuantity()%>">
                    <input class="button-comprar" type="submit" value="Comprar">
                </form>
                <% }
                %>
                <p style="color:red;" id="statusComprar"><%= statusComprar%></p>
                <p style="color:green;" id="statusComprarOK"><%= statusComprarOK%></p>
                
                <%  int productsFromSameVendor = 0;
                    for(Product p : product.getVendorId().getProductList()){
                        if(p.getProductId()!= product.getProductId() && productsFromSameVendor < 3){
                            if(productsFromSameVendor == 0){
                                %>
                                <h2 style="padding-top: 50px;">Productos del mismo Vendedor: </h2>
                                <%
                            }
                            %>
                            <a href="ProductoMostrar?productID=<%= p.getProductId() %>" class="button-other"><%= p.getTitle() %></a>
                            <%
                               productsFromSameVendor++; 
                        }
                    }
                %>
                        
            </div>
        </div>

        <hr/>   

        <br/>
        <h1> &nbsp; Valoraciones: </h1>

        <!-- LISTA PRODUCTOS -->   
        <%
            if (listadoValoraciones == null || listadoValoraciones.isEmpty()) {
        %>          
        <h2>Ahora mismo no hay valoraciones sobre este producto</h2>
        <%
        } else {
        %>
        <h2> &nbsp; &nbsp;Media valoraciones: <%=mediaValoraciones%></h2>
        <table cellpadding="10" align="center" style="width:80%;height:150px;overflow-y:auto;">
            <tbody>
                <%
                    int j = 0;
                    for (Review review : listadoValoraciones) {
                        String comentario = review.getComment() == null ? "" : review.getComment();
                        String valoracion = review.getStars() + "";
                %>
                <tr>

                    <td style="border: 2px solid black; border-spacing: 15px;">
                        <h4 style="text-align:right;"> Usuario: <%=review.getUserId().getNickname()%>
                            &emsp;  Fecha: <%=new SimpleDateFormat("dd/MM/yyyy").format(review.getReviewDate())%>
                        </h4>
                        <hr/>
                        <form>
                            <h2> Puntuacion:</h2> 
                            <fieldset class="rating" disabled>
                                <input type="radio" <%if (valoracion.equals("5.0")) {%>checked="true" <%}%> id="star5<%=j%>" name="rating" value="5" /><label class = "full" for="star5<%=j%>" ></label>
                                <input type="radio" <%if (valoracion.equals("4.5")) {%>checked="true" <%}%> id="star4half<%=j%>" name="rating" value="4.5" /><label class="half" for="star4half<%=j%>"></label>
                                <input type="radio" <%if (valoracion.equals("4.0")) {%>checked="true" <%}%> id="star4<%=j%>" name="rating" value="4" /><label class = "full" for="star4<%=j%>" ></label>
                                <input type="radio" <%if (valoracion.equals("3.5")) {%>checked="true" <%}%> id="star3half<%=j%>" name="rating" value="3.5" /><label class="half" for="star3half<%=j%>"></label>
                                <input type="radio" <%if (valoracion.equals("3.0")) {%>checked="true" <%}%> id="star3<%=j%>" name="rating" value="3" /><label class = "full" for="star3<%=j%>" ></label>
                                <input type="radio" <%if (valoracion.equals("2.5")) {%>checked="true" <%}%> id="star2half<%=j%>" name="rating" value="2.5" /><label class="half" for="star2half<%=j%>"></label>
                                <input type="radio" <%if (valoracion.equals("2.0")) {%>checked="true" <%}%> id="star2<%=j%>" name="rating" value="2" /><label class = "full" for="star2<%=j%>" ></label>
                                <input type="radio" <%if (valoracion.equals("1.5")) {%>checked="true" <%}%> id="star1half<%=j%>" name="rating" value="1.5" /><label class="half" for="star1half<%=j%>" ></label>
                                <input type="radio" <%if (valoracion.equals("1.0")) {%>checked="true" <%}%> id="star1<%=j%>" name="rating" value="1" /><label class = "full" for="star1<%=j%>" ></label>
                                <input type="radio" <%if (valoracion.equals("0.5")) {%>checked="true" <%}%> id="starhalf<%=j%>" name="rating" value="0.5" /><label class="half" for="starhalf<%=j%>" ></label>
                            </fieldset>
                            <br/>
                            <br/>

                            <h2> Comentario:</h2> 
                            <%if (comentario.equals("")) {%> No hay comentario <%} else {%>
                            <textarea disabled maxlength="510" name="comentario" value="<%=comentario%>" ><%=comentario%></textarea>
                            <% } %>
                        </form>
                    </td>

                </tr>
                <%
                        j++;
                    }
                %>
            </tbody>
        </table>        
        <% }%>


    </body>
</html>
