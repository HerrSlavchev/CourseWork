package services;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import dto.Result;
import dto.domain.User;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import services.client.NotifiableIF;
import services.implementations.ClientManagerImpl;
import services.implementations.ServiceExposer;
import services.server.ClientManagerIF;
import webdao.NotifiableImpl;

/**
 *
 * @author root
 */
@WebServlet(urlPatterns = {"/LoginServlet"})
public class LoginServlet extends HttpServlet {

    ClientManagerIF stub = ClientManagerImpl.getInstance();
    
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
        String eMail = request.getParameter("eMail");
        String password = request.getParameter("password");
        
        
        NotifiableIF client = new NotifiableImpl(request.getSession());
        
        User user = null;
        Throwable exc = null;
        try {
            User u = new User(0);
            u.setE_Mail(eMail);
            u.setPassword(password);
            Result<User> res = stub.registerClient(client, u);
            if (res.getException() != null){
                exc = res.getException();
            } else {
                user = res.getResult().get(0);
            }
        } catch (Exception e){
            exc = e;
        }
        
        if(exc != null) {
            request.setAttribute("errorMsg", exc.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        } else {
            String sc = ((String) request.getSession().getAttribute("sessionCode"));
            if (sc == null){
                sc = "noone";
            }
            request.setAttribute("errorMsg", "Welcome, " + sc);
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
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
