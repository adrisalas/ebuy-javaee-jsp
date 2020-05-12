/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package ebuy.servlet;

import ebuy.dao.ProductFacade;
import ebuy.dao.ReviewFacade;
import ebuy.entity.Account;
import ebuy.entity.Product;
import ebuy.entity.Review;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
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
@WebServlet(name = "ProductoMostrar", urlPatterns = {"/ProductoMostrar"})
public class ProductoMostrar extends HttpServlet {

    @EJB
    ProductFacade pFacade;
    @EJB
    private ReviewFacade reviewFacade;

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
        HttpSession session = request.getSession();
        Account user = (Account) session.getAttribute("user");
        Product product;

        if (user == null) { //No está registrado
            goTo = "login.jsp";
        } else if (productID == null) {
            if (user.getIsadmin() == 0) {
                goTo = "ProductosListar";
            } else {
                goTo = "adminMenu.jsp";
            }
        } else {
            product = pFacade.find(productID);
            request.setAttribute("product", product);
            List<Review> listadoValoraciones = this.reviewFacade.findByProductoId(productID);
            request.setAttribute("listadoValoraciones", listadoValoraciones);
            double mediaValoraciones= this.reviewFacade.obtenerMediaValoraciones(productID);
            request.setAttribute("mediaValoraciones", mediaValoraciones);
            goTo = "productoMostrar.jsp";

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
