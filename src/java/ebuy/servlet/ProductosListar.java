/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ebuy.servlet;

import ebuy.dao.CategoryFacade;
import ebuy.dao.KeywordFacade;
import ebuy.dao.ProductFacade;
import ebuy.entity.Account;
import ebuy.entity.Category;
import ebuy.entity.Keyword;
import ebuy.entity.Product;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author victoria
 */
@WebServlet(name = "ProductosListar", urlPatterns = {"/ProductosListar"})
public class ProductosListar extends HttpServlet {

    @EJB
    private ProductFacade productFacade;
    @EJB
    private CategoryFacade categoryFacade;

    @EJB
    private KeywordFacade keywordFacade;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Account usuario;

        usuario = (Account) session.getAttribute("user");
        // Si el usuario no está en la sesión quiere decir que no ha hecho "login"
        // y que se está haciendo un acceso ilegal a la aplicación.
        if (usuario == null) {
            response.sendRedirect("login.jsp");
        } else {
            HashMap<Product, Integer> productos = new HashMap<>();
            int numFiltros = 0;
            List<Product> listadoProductos;

            //CATEGORIAS
            List<Category> listaCategorias = this.categoryFacade.findAll();
            String str_filtro_categoria = request.getParameter("filtro_categoria");
            boolean hayFiltroCategoria = (str_filtro_categoria != null && !str_filtro_categoria.isEmpty());

            if (hayFiltroCategoria) {
                listadoProductos = this.productFacade.findByCategoria(Integer.parseInt(str_filtro_categoria));
                numFiltros++;
                insertarEnHashMap(productos, listadoProductos);
            }

            //KEYWORDS
            List<Keyword> listaKeyword = this.keywordFacade.findAll();
            String str_filtro_keyword = request.getParameter("filtro_keyword");
            boolean hayFiltroKeyword = (str_filtro_keyword != null && !str_filtro_keyword.isEmpty());

            if (hayFiltroKeyword) {
                listadoProductos = this.productFacade.findByKeyword(Integer.parseInt(str_filtro_keyword));
                numFiltros++;
                insertarEnHashMap(productos, listadoProductos);
            }

            //DATE
            String str_filtro_date = request.getParameter("filtro_date");
            boolean hayFiltroDate = (str_filtro_date != null && !str_filtro_date.isEmpty());

            if (hayFiltroDate) {
                try {
                    listadoProductos = this.productFacade.findByDate(new SimpleDateFormat("yyyy-MM-dd").parse(str_filtro_date));
                    numFiltros++;
                    insertarEnHashMap(productos, listadoProductos);
                } catch (ParseException ex) {
                    Logger.getLogger(ProductosListar.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

            //TIME
            String str_filtro_time = request.getParameter("filtro_time");

            boolean hayFiltroTime = (str_filtro_time != null && !str_filtro_time.isEmpty());

            if (hayFiltroTime) {
                try {
                    str_filtro_time += ":00";
                    listadoProductos = this.productFacade.findByTime(new SimpleDateFormat("HH:mm:ss").parse(str_filtro_time));
                    numFiltros++;
                    insertarEnHashMap(productos, listadoProductos);
                } catch (ParseException ex) {
                    Logger.getLogger(ProductosListar.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

            //TITULO
            String str_filtro_titulo = request.getParameter("filtro_titulo");
            boolean hayFiltroTitulo = (str_filtro_titulo != null && !str_filtro_titulo.isEmpty());

            if (hayFiltroTitulo) {
                listadoProductos = this.productFacade.findByTitulo(str_filtro_titulo);
                numFiltros++;
                insertarEnHashMap(productos, listadoProductos);
            }

            //DESCRIPCION
            String str_filtro_descripcion = request.getParameter("filtro_descripcion");
            boolean hayFiltroDescripcion = (str_filtro_descripcion != null && !str_filtro_descripcion.isEmpty());

            if (hayFiltroDescripcion) {
                listadoProductos = this.productFacade.findByDescripcion(str_filtro_descripcion);
                numFiltros++;
                insertarEnHashMap(productos, listadoProductos);
            }

            if (numFiltros == 0) {
                listadoProductos = this.productFacade.allProductsOrderByDate();
            } else {
                listadoProductos = obtenerListaFinal(productos, numFiltros);
                Collections.sort(listadoProductos, Comparator.comparing(Product::getCreationDate)
                        .thenComparing(Product::getCreationTime).reversed());

            }

            request.setAttribute("listaKeywords", listaKeyword);
            request.setAttribute("listaCategorias", listaCategorias);

            request.setAttribute("listadoProductos", listadoProductos);

            RequestDispatcher rd = request.getRequestDispatcher("listadoProductos.jsp");
            rd.forward(request, response);
        }
    }

    private void insertarEnHashMap(HashMap<Product, Integer> productos, List<Product> filtrado) {
        for (Product p : filtrado) {
            if (!productos.containsKey(p)) {
                productos.put(p, 1);
            } else {
                productos.put(p, productos.get(p) + 1);
            }
        }
    }

    private List<Product> obtenerListaFinal(HashMap<Product, Integer> productos, int numFiltros) {
        List<Product> productosFiltro = new ArrayList<>();
        Set<Product> set = productos.keySet();

        Iterator<Product> it = set.iterator();

        while (it.hasNext()) {
            Product p = it.next();
            if (productos.get(p) == numFiltros) {
                productosFiltro.add(p);
            }

        }
        return productosFiltro;
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
