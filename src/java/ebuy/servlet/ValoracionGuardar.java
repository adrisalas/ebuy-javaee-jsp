/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ebuy.servlet;

import ebuy.dao.PurchasedProductFacade;
import ebuy.dao.ReviewFacade;
import ebuy.entity.Account;
import ebuy.entity.PurchasedProduct;
import ebuy.entity.Review;
import java.io.IOException;
import javax.ejb.EJB;
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
@WebServlet(name = "ValoracionGuardar", urlPatterns = {"/ValoracionGuardar"})
public class ValoracionGuardar extends HttpServlet {

    @EJB
    private ReviewFacade reviewFacade;
    
     @EJB
    private PurchasedProductFacade purchasedFacade;
    
    
    
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
        response.setContentType("text/html;charset=UTF-8");
           HttpSession session = request.getSession();
            Account usuario;

        usuario = (Account) session.getAttribute("user");
        // Si el usuario no está en la sesión quiere decir que no ha hecho "login"
        // y que se está haciendo un acceso ilegal a la aplicación.
        if (usuario == null) {
            response.sendRedirect("login.jsp");
        }else {
            String str;
            
            Review review;
            boolean esCrearNuevo = false;
          
            str = request.getParameter("reviewId");
            if (str == null || str.isEmpty()) { // Estamos en el caso de creación de un nuevo cliente
                review = new Review(0); // Aunque el id es autoincremental, hay ocasiones en las que               // si no se le da un valor por defecto, da un error al guardarlo.
                esCrearNuevo = true;
            } else {
                review = this.reviewFacade.find(new Integer(str));
            }
            
            str = request.getParameter("compraId");
            PurchasedProduct compra=this.purchasedFacade.find(new Integer(str));
            review.setPurchaseId(compra);
            
            str = new String(request.getParameter("comentario").getBytes("ISO-8859-1"),"UTF8");
            review.setComment(str);
            
            str=request.getParameter("rating");
            if(str == null || str.isEmpty() || str.equalsIgnoreCase("")){
                str = "0.0";
            }
            review.setStars(new Double(str));
            
            review.setUserId(usuario);
             
            long millis=System.currentTimeMillis();
            java.sql.Date date = new java.sql.Date(millis); 
            review.setReviewDate(date);
                       

            if (esCrearNuevo) {
                this.reviewFacade.create(review);
                compra.setReview(review);
                this.purchasedFacade.edit(compra);
            } else {
                this.reviewFacade.edit(review);
            }
            
            response.sendRedirect("ProductosHistorial");            
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
