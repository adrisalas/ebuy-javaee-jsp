/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ebuy.servlet;

import ebuy.dao.AccountFacade;
import ebuy.entity.Account;
import java.io.IOException;
import java.io.PrintWriter;
import javax.ejb.EJB;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author adrsa
 */
@WebServlet(name = "SignIn", urlPatterns = {"/SignIn"})
public class SignIn extends HttpServlet {

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
     String goTo="login.jsp";
        String signinstatus = "";
        String signinstatusok = "";
        String emailRegex = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
        
        String nickname = new String(request.getParameter("nickname").getBytes("ISO-8859-1"),"UTF8");
        request.setAttribute("nickname", nickname);
        String email1 = new String(request.getParameter("email1").getBytes("ISO-8859-1"),"UTF8");
        request.setAttribute("email1", email1);
        String email2 = new String(request.getParameter("email2").getBytes("ISO-8859-1"),"UTF8");
        request.setAttribute("email2", email2);
        String password1 = new String(request.getParameter("password1").getBytes("ISO-8859-1"),"UTF8");
        request.setAttribute("password1", password1);
        String password2 = new String(request.getParameter("password2").getBytes("ISO-8859-1"),"UTF8");
        request.setAttribute("password2", password2);
        
        Account user;
        RequestDispatcher rd;
        if(nickname.length() > 20){
            signinstatus = "ERROR. El nickname no debes tener mas de 20 carácteres";
            request.setAttribute("signinstatus", signinstatus);
        } else if(email1.length() > 50){
            signinstatus = "ERROR. El email no debe tener mas de 50 carácteres";
            request.setAttribute("signinstatus", signinstatus);
        } else if(password1.length() > 20){
            signinstatus = "ERROR. La contraseña no debes tener mas de 20 carácteres";
            request.setAttribute("signinstatus", signinstatus);
        } else if (nickname.equals("") || email1.equals("") || email2.equals("") || password1.equals("") || password2.equals("")){
            signinstatus = "ERROR. Debes rellenar todos los campos para registrarte";
            request.setAttribute("signinstatus", signinstatus);
        } else if(!email1.matches(emailRegex)){
            signinstatus = "ERROR. Email invalido";
            request.setAttribute("signinstatus", signinstatus);
        }else if(!email1.equalsIgnoreCase(email2)){
            signinstatus = "ERROR. Los email no coinciden";
            request.setAttribute("signinstatus", signinstatus);
        } else if (!password1.equals(password2)){
            signinstatus = "ERROR. Las contraseñas no coinciden";
            request.setAttribute("signinstatus", signinstatus);
        } else {
            user = this.accountFacade.findByEmail(email1);
            if (user != null){
                signinstatus = "ERROR. El email ya tiene una cuenta asociada.";
                request.setAttribute("signinstatus", signinstatus);
            } else {
                user = this.accountFacade.findByNickname(nickname);
                if(user != null){
                    signinstatus = "ERROR. El nickname no esta disponible";
                    request.setAttribute("signinstatus", signinstatus);
                } else {   
                    signinstatus = "";
                    request.setAttribute("signinstatus", signinstatus);
                    signinstatusok = "Registrado con exito";
                    request.setAttribute("signinstatusok", signinstatusok);
                    request.removeAttribute("nickname");
                    request.removeAttribute("email1");
                    request.removeAttribute("email2");
                    request.removeAttribute("password1");
                    request.removeAttribute("password2");
                    
                    Account newUser = new Account();
                    newUser.setEmail(email1);
                    short s = 0;
                    newUser.setIsadmin(s);
                    newUser.setNickname(nickname);
                    newUser.setPassword(password1);
                    accountFacade.create(newUser);
                }
            }
            
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
