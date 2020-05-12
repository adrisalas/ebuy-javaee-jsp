/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ebuy.servlet;

import ebuy.dao.AccountFacade;
import ebuy.entity.Account;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.Level;
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
 * @author Alberto
 */
@WebServlet(name = "UsuariosEditar", urlPatterns = {"/UsuariosEditar"})
public class UsuariosEditar extends HttpServlet {
    
    private static final Logger LOG = Logger.getLogger(UsuariosEditar.class.getName());
    @EJB
    private AccountFacade accountFacade;
    
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
        Account cliente;
        String cliente_ID;
        
        Account usuario = (Account) session.getAttribute("user");
        if (usuario == null || usuario.getIsadmin() == 0) {
            response.sendRedirect("login.jsp");
        } else {
            cliente_ID = request.getParameter("id");
            if (cliente_ID == null) { 
                LOG.log(Level.SEVERE, "No se ha encontrado el usuario a editar");
                response.sendRedirect("adminMenu.jsp");
            } else {
                cliente = this.accountFacade.find(new Integer(cliente_ID));
                if(cliente == null){
                    LOG.log(Level.SEVERE, "No se ha encontrado el usuario");
                    response.sendRedirect("adminMenu.jsp");
                }else{
                    
                    request.setAttribute("cliente", cliente);
                    
                    RequestDispatcher rd = request.getRequestDispatcher("usuarioFormulario.jsp");
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
