<%-- 
    Document   : producto
    Created on : 03-abr-2020, 18:49:47
    Author     : alici
--%>

<%@page import="java.util.ArrayList"%>
<%@page import="ebuy.entity.Keyword"%>
<%@page import="ebuy.entity.Category"%>
<%@page import="ebuy.entity.Subcategory"%>
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
        <link rel="stylesheet" type="text/css" href="formulario.css">
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

    %>


    <%        String productId = "",
                title = "", description = "", price = "", photoUrl = "",
                creationDate = "", quantity = "", vendor = null, subcategory = null, category = "";
        Product product = (Product) request.getAttribute("producto");
        List<Keyword> keywordProduct = null;
        List<Account> listAccount;
        List<Subcategory> listSubcategory;
        List<Category> listCategory;
        List<Keyword> listKeyword;
        String keywords = "";

        if (product != null) {
            productId = product.getProductId() + "";
            title = product.getTitle();
            description = product.getDescription();
            price = product.getPrice() + "";
            photoUrl = product.getPhotoUrl();
            quantity = product.getQuantity() + "";
            keywordProduct = product.getKeywordList();
            vendor = product.getVendorId().getUserId() + "";
            subcategory = product.getSubcategoryId().getSubcategoryId() + "";
            category = product.getSubcategoryId().getCategoryId() + "";
        }

        listAccount = (List) request.getAttribute("listAccount");
        listSubcategory = (List) request.getAttribute("listSubcategory");
        listCategory = (List) request.getAttribute("listCategory");
        listKeyword = (List) request.getAttribute("listKeyword");
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
                <a href="MisProductos" style="background-color:white;color: black;">MIS PRODUCTOS</a>
                <a href="" >HISTORIAL</a>
                <a href="Exit" >Cerrar Sesion</a>
            </div>

        </div>
        <form action="ProductosGuardar">
            <input type="hidden" name="productId" value="<%= productId%>" />
            <table>
                <tr>
                    <td>T&iacute;tulo:</td>
                    <td colspan="2"><input type="text" name="title" value="<%= title%>" size="50" max="50" maxlength="50" /></td>                
                </tr>
                <tr>
                    <td>Descripci&oacute;n:</td>
                    <td colspan="2"><textarea name="description" value="<%= description%>"  rows="10" cols="50" maxlength="1020" ><%= description%></textarea> </td>                
                </tr>
                <tr>
                    <td>Precio:</td>
                    <td colspan="2"><input type="text" name="price" value="<%= price%>" size="50" max="50" maxlength="52" /></td>                
                </tr>
                <tr>
                    <td>Foto(URL):</td>
                    <td colspan="2"><input type="text" name="urlPhoto" value="<%= photoUrl%>" size="50" max="50" maxlength="510" /></td>                
                </tr>                       
                <tr>
                    <td>Cantidad:</td>
                    <td colspan="2"><input type="text" name="quantity" value="<%= quantity%>" size="10" max="10" maxlength="10" /></td>                
                </tr>   
                <tr>
             <!--   <div class="autocomplete" style="width:300px;">-->
                    <td>Palabras claves:</td>
                    <td><input list="nuevaPalabras" id="nuevaPalabra" type="text" style="width: 120px" size="30" max="30" maxlength="30"  name="nuevaPalabra" placeholder="Palabra Claves" >
                        <datalist id="nuevaPalabras">
                            <%
                                for(Keyword k: listKeyword){
                             %>
                             <option value="<%=k.getName()%>"></option>
                             <%
                                }
                                %>
                        </datalist> <input style="margin-left:5px" type="button" onclick="añadir()" class="boton" id="btn-añadir" value="Añadir">
                    </td>
                    <td> 
                            <div class="junto" style="margin-left:0px;width:100%;height:100%;max-height:123.8px;overflow-y:scroll"><ul style="min-width:170px;min-height:123.8px;" class="lista" id="lista">

                                <% String valor = "";
                                    if (keywordProduct != null) {
                                        for (Keyword key : keywordProduct) {
                                            valor = key.getName();
                                            keywords = keywords + valor + ",";

                                %>
                                <li  id="<%=key.getName()%>"><%=valor%><span onclick='eliminar(this)'> Eliminar</span></li>
                                    <%
                                            }
                                        }
                                    %>
                            </ul> </div></td> 
                       
                    <input type="hidden" id="listaOculta" name="listaOculta" value="<%= keywords%>" />
                    <%
                        String autoayuda = "";
                        for (Keyword k : listKeyword) {
                            autoayuda += k.getName() + ",";
                        }
                    %>

                    <input type="hidden" id="autoayuda" name="autoayuda" value="<%= autoayuda%>" />
                    <script>
                     
                        function añadir()
                        {
                            var nuevoLi = document.getElementById("nuevaPalabra").value;
                            var oculto = document.getElementById("listaOculta");
                            if (nuevoLi.length > 0)
                            {
                                if (find_li(nuevoLi))
                                {
                                    var li = document.createElement('li');
                                    li.id = nuevoLi;
                                    oculto.value += nuevoLi +",";
                                    li.innerHTML = nuevoLi + "<span onclick='eliminar(this)'> Eliminar</span>";
                                    document.getElementById("lista").appendChild(li);
                                }
                            }
                            return false;
                        }

                        function find_li(contenido)
                        {
                            var el = document.getElementById("lista").getElementsByTagName("li");
                            for (var i = 0; i < el.length; i++)
                            {
                                if (el[i].id === contenido)
                                    return false;
                            }
                            return true;
                        }

                        function eliminar(elemento)
                        {
                            var id = elemento.parentNode.getAttribute("id");
                            node = document.getElementById(id); //Obtengo el li
                            node.parentNode.removeChild(node);
                        }


                        /*An array containing all the country names in the world:*/
                        const espacio = document.getElementById("nuevaPalabra");
                        espacio.addEventListener("keyup", ayudar);
                        function ayudar() {
                            var ayuda = document.getElementById("autoayuda").value;
                            ayuda = ayuda.split(",");
                            var removed = ayuda.splice(ayuda.length - 1, 1);
                            console.log(ayuda);
                            const buscador = document.getElementById("browsers");
                            while (buscador.firstChild) {
                                buscador.removeChild(buscador.firstChild);
                                console.log("estoy borrando");
                            }
                            for (var i = 0; i < ayuda.length; i++) {
                                var option = document.createElement("option");
                                option.value = ayuda[i];
                                console.log("Estoy en el bucle for");

                                buscador.appendChild(option);
                            }
                        }

                    </script>
                    </tr>
                    <tr>
                        <td>Categor&iacute;a:</td>
                        <td colspan="2">
                            <select name="categoria">
                                <%
                                    List<Subcategory> listaS = listSubcategory;
                                    for (Category cat : listCategory) {
                                        String seleccionado = "";
                                        if (category != null && category.equals(cat.getCategoryId().toString())) {
                                            seleccionado = "selected";
                                            listaS = cat.getSubcategoryList();

                                        }
                                %>

                                <option <%= seleccionado%> value="<%= cat.getCategoryId()%>"><%= cat.getName()%></option>
                                <%
                                    }

                                    String resultado = "";
                                    for (Category s : listCategory) {
                                        resultado += "C:" + s.getCategoryId();
                                        for (Subcategory c : s.getSubcategoryList()) {
                                            resultado += "S:" + c.getSubcategoryId()+ "/" + c.getName();
                                        }
                                    }

                                %>
                            </select>   

                        </td>                
                    </tr>



                    <td>Subcategor&iacute;a:</td>
                    <td colspan="2">
                        <select name="subcategoria">
                            <%                                for (Subcategory sub : listSubcategory) {
                                    String seleccionado = "";
                                    if (subcategory != null && subcategory.equals(sub.getSubcategoryId().toString())) {
                                        seleccionado = "selected";
                                    }
                            %>
                            <option <%= seleccionado%> value="<%= sub.getSubcategoryId()%>"><%= sub.getName()%></option>
                            <%
                                }

                            %>                        
                        </select>    
                    </td>                
                    </tr>                        

                    <tr>
                        <td colspan="2"><input type="submit" value="Enviar" name="Enviar"/></td>
                    </tr>                                    
            </table>
            <input type="hidden" id="pSub" name="pSub" value="<%= resultado%>" />
        </form>
        <script>
            const categoria = document.getElementsByName("categoria")[0];
            categoria.addEventListener("change", funtionCambio);
            function funtionCambio() {
                const subcategoria = document.getElementsByName("subcategoria")[0];
                var tamaño = subcategoria.options.length;
                for (var i = tamaño; i >= 0; i--) {
                    subcategoria.remove(i);
                }

                var seleccionada = categoria.value;
                var datos = document.getElementById("pSub").value.split("C:");
                for (var i = 1; i < datos.length; i++) {
                    var subDatos = datos[i].split("S:");
                    if (subDatos[0] === seleccionada) {
                        for (var j = 1; j < subDatos.length; j++) {
                            const option = document.createElement('option');
                            var total = subDatos[j].split("/");
                            var text = total[1];
                            var value = total[0];
                            option.value = value
                            option.text = text;
                            subcategoria.appendChild(option);
                        }
                    }

                }

            }


        </script>
        <script src="js/añadirElementos.js"></script>
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
    </body>
</html>