/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ebuy.servlet;

import ebuy.dao.ProductFacade;
import ebuy.dao.PurchasedProductFacade;
import ebuy.entity.Account;
import ebuy.entity.Product;
import ebuy.entity.PurchasedProduct;
import java.io.IOException;
import java.io.PrintWriter;
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
 * @author adrsa
 */
@WebServlet(name = "ProductosComprar", urlPatterns = {"/ProductosComprar"})
public class ProductosComprar extends HttpServlet {

    @EJB
    ProductFacade productFacade;
    
    @EJB
    PurchasedProductFacade purchasedProductFacade;
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
        String emailOrNickname, pwd, status = null, goTo = "login.jsp";
        RequestDispatcher rd;
        
        Integer productID = Integer.valueOf(request.getParameter("productID"));
        Integer quantity = Integer.valueOf(request.getParameter("quantity"));
        HttpSession session = request.getSession();
        Account user = (Account) session.getAttribute("user");
        Product product;
        
        if(user == null){ //No está registrado
            goTo = "login.jsp";
        } else if(productID==null){
            if(user.getIsadmin() == 0){
                goTo = "ProductosListar";
            } else {
                goTo = "adminMenu.jsp";
            }
        } else {
            product = this.productFacade.find(productID);
            if (product.getQuantity() >= quantity && quantity!=0){
                product.setQuantity(product.getQuantity() - quantity);
                this.productFacade.edit(product);
                
                PurchasedProduct pp = new PurchasedProduct(0);
                pp.setBuyerId(user);
                pp.setPrice(product.getPrice());
                pp.setProductId(product);
                pp.setPurchaseDate(new java.sql.Date(System.currentTimeMillis()));
                pp.setQuantity(quantity);
                this.purchasedProductFacade.create(pp);
                request.setAttribute("statusComprarOK", "Compradas " + quantity + " unidades");
            } else {
                request.setAttribute("statusComprar", "ERROR AL COMPRAR");
            }
            request.setAttribute("product", product);
            goTo="ProductoMostrar";
        }
        rd = request.getRequestDispatcher(goTo);
        rd.forward(request, response);
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
