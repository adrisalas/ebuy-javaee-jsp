/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ebuy.servlet;

import ebuy.dao.AccountFacade;
import ebuy.entity.Account;
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
 * @author Alberto
 */
@WebServlet(name = "UsuariosGuardar", urlPatterns = {"/UsuariosGuardar"})
public class UsuariosGuardar extends HttpServlet {
    
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
        
        if (session.getAttribute("user") == null){
            response.sendRedirect("login.jsp");
        } else {
            Account usuario;
            String str;
            
            str = request.getParameter("usuarioId");
            usuario = this.accountFacade.find(Integer.parseInt(str));
            
            str = new String(request.getParameter("nickname").getBytes("ISO-8859-1"),"UTF8");
            usuario.setNickname(str);
            str = new String(request.getParameter("email").getBytes("ISO-8859-1"),"UTF8");
            usuario.setEmail(str);
            str = new String(request.getParameter("pwd").getBytes("ISO-8859-1"),"UTF8");
            usuario.setPassword(str);
            
            str = request.getParameter("isAdmin"); 
            
            if(str == null){
                str = "0";
            } else {
                str = "1";
            }
            
            usuario.setIsadmin(new Short(str));
            
            this.accountFacade.edit(usuario);
            response.sendRedirect("UsuariosListar");
            
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
