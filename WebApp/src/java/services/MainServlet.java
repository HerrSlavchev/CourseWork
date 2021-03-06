package services;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import dto.session.Session;
import java.io.IOException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import services.implementations.ClientManagerImpl;
import services.server.ClientManagerIF;

/**
 *
 * @author root
 */
@WebServlet(urlPatterns = {"/MainServlet"})
public class MainServlet extends HttpServlet {
    
    private ClientManagerIF stubClientManager = ClientManagerImpl.getInstance();
    
    private ServletContext servletContext;
    @Override
    public void init(ServletConfig config){
        this.servletContext = config.getServletContext();
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
        //processRequest(request, response);
        String action = request.getParameter("action");
        if (action == null){
            return;
        }
        if (action.equals("login")){
            request.getRequestDispatcher("login.jsp").forward(request, response);
        } else if (action.equals("regions")){
            request.getRequestDispatcher("RegionServlet").forward(request, response);
        } else if (action.equals("towns")){
            request.getRequestDispatcher("TownServlet").forward(request, response);
        } else if (action.equals("categories")){
            request.getRequestDispatcher("CategoryServlet").forward(request, response);
        } else if (action.equals("subcategories")){
            request.getRequestDispatcher("SubcategoryServlet").forward(request, response);
        } else if (action.equals("logout")){
            String sessionCode = (String) request.getSession().getAttribute("sessionCode");
            stubClientManager.removeClient(new Session(sessionCode));
            
            String xml = "<success>" + true + "</success>";
            response.setContentType("text/xml");
            response.setHeader("Cache-Control", "no-cache");
            String answer = "<root>" + xml + "</root>";
            //System.out.println(answer);
            response.getWriter().write(answer);
        }
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
       // processRequest(request, response);
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
