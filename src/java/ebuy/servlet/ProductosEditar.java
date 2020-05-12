/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ebuy.servlet;

import ebuy.dao.AccountFacade;
import ebuy.dao.CategoryFacade;
import ebuy.dao.KeywordFacade;
import ebuy.dao.ProductFacade;
import ebuy.dao.SubcategoryFacade;
import ebuy.entity.Account;
import ebuy.entity.Category;
import ebuy.entity.Keyword;
import ebuy.entity.Product;
import ebuy.entity.Subcategory;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
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
 * @author alici
 */
@WebServlet(name = "ProductosEditar", urlPatterns = {"/ProductosEditar"})
public class ProductosEditar extends HttpServlet {
    private static final Logger LOG = Logger.getLogger(ProductosEditar.class.getName());
    @EJB
    private SubcategoryFacade subCategoryFacade;
    @EJB
    private AccountFacade accountFacade;
    @EJB
    private CategoryFacade categoryFacade;
    @EJB
    private ProductFacade productFacade;
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
        String str;
        Product producto;
        
        if (session.getAttribute("user")==null) { // Se ha llamado al servlet sin haberse autenticado
            response.sendRedirect("login.jsp");            
        } else {        
            str = request.getParameter("id");
            if (str == null) {
                LOG.log(Level.SEVERE, "No se ha encontrado el producto a editar");
                response.sendRedirect("listadoMisProductos.jsp");            
            } else {
                producto = this.productFacade.find(new Integer(str));
                if (producto == null) { //Esta situación no debería darse
                    LOG.log(Level.SEVERE, "No se ha encontrado el cliente a editar");
                    response.sendRedirect("listadoMisProductos.jsp");
                } else {
            
                    List<Subcategory> listSubcategory = this.subCategoryFacade.findAll();
                    List<Category> listCategory = this.categoryFacade.findAll();
                    List<Account> listAccount = this.accountFacade.findAll();
                    List<Keyword> listKeyword= this.keywordFacade.findAll();
                    
                    request.setAttribute("listCategory",listCategory);
                    request.setAttribute("producto", producto);
                    request.setAttribute("listSubcategory", listSubcategory);
                    request.setAttribute("listAccount", listAccount);
                    request.setAttribute("listKeyword",listKeyword);
                    
                    RequestDispatcher rd = request.getRequestDispatcher("producto.jsp");
                    rd.forward(request, response);
                }       
            }
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
