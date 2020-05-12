<%-- 
    Document   : historial
    Created on : 15-abr-2020, 21:13:34
    Author     : victoria
--%>

<%@page import="java.util.HashMap"%>
<%@page import="ebuy.entity.Review"%>
<%@page import="ebuy.entity.Review"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="ebuy.entity.Keyword"%>
<%@page import="ebuy.entity.Product"%>
<%@page import="java.util.List"%>
<%@page import="ebuy.entity.PurchasedProduct"%>
<%@page import="ebuy.entity.Account"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="menu.css">
        <link rel="stylesheet" type="text/css" href="productCard.css">
        <link rel="stylesheet" type="text/css" href="botones.css">
        <link rel="stylesheet" type="text/css" href="mensajes.css">
        <link rel="stylesheet" type="text/css" href="valoracion.css">
        <link rel="stylesheet" type="text/css" href="popup.css">
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
        List<PurchasedProduct> listadoCompras = (List<PurchasedProduct>) request.getAttribute("listadoCompras");
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


            <div class="nav-links">
                <a href="ProductosListar" >BUSCAR PRODUCTOS</a>
                <a href="MisProductos" >MIS PRODUCTOS</a>
                <a href="" style="background-color:white;color: black;">HISTORIAL</a>
                <a href="Exit" >Cerrar Sesion</a>
            </div>


            <!-- LISTA PRODUCTOS -->   
            <%
                if (listadoCompras == null || listadoCompras.isEmpty()) {
            %>          
            <div class="oaerror info">
                <strong>Historial vacío</strong> - Aún no se han realizado compras
            </div>

            <%
            } else {
            %>
            <div class="titulo">Historial de compras realizadas:</div>
            <table align="center" style="border-spacing: 50px 5px;margin-top: 0;"> 
                <%
                    int i = 0;
                    for (PurchasedProduct compra : listadoCompras) {
                        Product producto = compra.getProductId();

                        if (i % 3 == 0) { %>
                <tr>
                    <% }%>
                    <td>
                        <div class="product-card">
                            <div class="badge" style="top: 25px;"><a href="ProductoMostrar?productID=<%=producto.getProductId()%>">Ver Producto</a></div>
                            <div class="valoracion" ><a href="#popup<%=i%>">Ver Valoracion</a></div>
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
                                <h4><a href="ProductoMostrar?productID=<%=producto.getProductId()%>"><%=producto.getTitle()%></a></h4>
                                <p><%
                                    String d = producto.getDescription();
                                    if (d.length() > 43) {
                                        d = d.substring(0, 40) + "...";
                                    }
                                    %> <%=d%></p>

                                <div class="final-detalles">

                                    <div class="detalles-compra">
                                        <div  class="texto">  Detalles de la compra</div><br/>
                                        Precio: <%=compra.getPrice()%>€ 
                                        <br/>Fecha: <%=new SimpleDateFormat("dd/MM/yyyy").format(compra.getPurchaseDate())%>
                                        <br/>Cantidad: <%= compra.getQuantity()%></div>
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
            </table>
            <%} //if
            %>    

            <%
                for (int j = 0; j < listadoCompras.size(); j++) {
                    PurchasedProduct compra = listadoCompras.get(j);
                    Product producto = compra.getProductId();

                  
                       Review review=compra.getReview();
                  
                  
                    String comentario = "";
                    String valoracion = "";
                    String reviewId = "";

                    
                    if ( review!= null) {
                        reviewId = review.getReviewId() + "";
                        comentario = review.getComment();
                        valoracion = review.getStars() + "";
                         System.out.println("review:"+compra.getReview().getComment()+"estrellas:"+compra.getReview().getStars());
                    }

            %>

            <div id="popup<%=j%>" class="overlay">
                <div class="popup">

                    <h1><%=producto.getTitle() + " | " + new SimpleDateFormat("dd/MM/yyyy").format(compra.getPurchaseDate())%> </h1>

                    <a class="close" href="#">&times;</a>
                    <div class="content">
                        <form action="ValoracionGuardar">
                            <input type="hidden" name="compraId" value="<%= compra.getPurchaseId()%>" />
                            <input type="hidden" name="reviewId" value="<%= reviewId%>" />
                            <h2> Puntuacion:</h2>  
                            <fieldset class="rating">
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
                            <hr/>
                            <h2> Comentario:</h2>  
                            <textarea placeholder="Opcional" maxlength="510" name="comentario" value="<%=comentario%>" ><%=comentario%></textarea>


                            <% String texto = "Enviar";
                                if (compra.getReview() != null) {
                                    texto = "Modificar";
                                }
                            %>


                            <input class="button-valorar" type="submit" name="Enviar" value="<%=texto%> valoracion"/>
                        </form>
                    </div>
                </div>
            </div>
            <% }%>
    </body>

</html>