/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ebuy.servlet;

import ebuy.dao.KeywordFacade;
import ebuy.dao.ProductFacade;
import ebuy.dao.SubcategoryFacade;
import ebuy.entity.Account;
import ebuy.entity.Product;
import java.io.IOException;
import ebuy.entity.Keyword;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author alici
 */
@WebServlet(name = "ProductosGuardar", urlPatterns = {"/ProductosGuardar"})
public class ProductosGuardar extends HttpServlet {

    @EJB
    private SubcategoryFacade subcategoryFacade;
    @EJB
    private ProductFacade productFacade;
    @EJB
    private KeywordFacade keywordFacade;

    private static final Logger LOG = Logger.getLogger(ProductosGuardar.class.getName());

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
        Account user = (Account) session.getAttribute("user");

        if (user == null) { // Se ha llamado al servlet sin haberse autenticado
            response.sendRedirect("login.jsp");
        } else {
            String str;
            Product product;
            boolean esCrearNuevo = false;

            str = request.getParameter("productId");
            if (str == null || str.isEmpty()) { // Estamos en el caso de creación de un nuevo producto
                product = new Product(0); // Aunque el id es autoincremental, hay ocasiones en las que
                // si no se le da un valor por defecto, da un error al guardarlo.
                esCrearNuevo = true;
            } else {
                product = this.productFacade.find(new Integer(str));
            }

            String titulo = new String(request.getParameter("title").getBytes("ISO-8859-1"), "UTF8");
            String descripcion = new String(request.getParameter("description").getBytes("ISO-8859-1"), "UTF8");
            String precio = new String(request.getParameter("price").getBytes("ISO-8859-1"), "UTF8");
            String foto = new String(request.getParameter("urlPhoto").getBytes("ISO-8859-1"), "UTF8");
            String cantidad = new String(request.getParameter("quantity").getBytes("ISO-8859-1"), "UTF8");
            String subcategoria = new String(request.getParameter("subcategoria").getBytes("ISO-8859-1"), "UTF8");
            String[] keywords = new String(request.getParameter("listaOculta").getBytes("ISO-8859-1"), "UTF8").split(",");
            List<String> keywordList = keywordFacade.findAllName();
            List<Keyword> keyWords = new ArrayList<>();

            for (String words : keywords) {
                Keyword key;
                if (!keywordList.contains(words)) {
                    key = new Keyword(0);
                    key.setName(words);
                    this.keywordFacade.create(key);
                } else {

                    key = keywordFacade.findByName(words).get(0);
                }

                keyWords.add(key);

            }

            product.setKeywordList(keyWords);
            product.setTitle(titulo);
            product.setDescription(descripcion);
            product.setPrice(Double.parseDouble(precio));
            product.setPhotoUrl(foto);
            product.setQuantity(Integer.parseInt(cantidad));
            product.setSubcategoryId(subcategoryFacade.find(Integer.parseInt(subcategoria)));
            product.setVendorId(user);

            long millis = System.currentTimeMillis();

            java.sql.Date date = new java.sql.Date(millis);
            java.sql.Time tiempo = new java.sql.Time(millis);
            product.setCreationDate(date);
            java.util.Date time = tiempo;
            String hora = new SimpleDateFormat("HH:mm").format(time);
            hora += ":00";
            try {
                product.setCreationTime(new SimpleDateFormat("HH:mm:ss").parse(hora));
            } catch (ParseException ex) {
                Logger.getLogger(ProductosGuardar.class.getName()).log(Level.SEVERE, null, ex);
            }

            if (esCrearNuevo) {
                this.productFacade.create(product);
            } else {
                this.productFacade.edit(product);
            }

            response.sendRedirect("MisProductos");
        }
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
