package services;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import dto.Result;
import dto.domain.Interest;
import dto.domain.User;
import dto.filters.InterestFilter;
import dto.rolemanagement.Role;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.List;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import services.client.NotifiableIF;
import services.implementations.ClientManagerImpl;
import services.implementations.InterestDAImpl;
import services.server.ClientManagerIF;
import services.server.InterestDAIF;
import utils.ParameterExtractor;
import webdao.NotifiableImpl;

/**
 *
 * @author root
 */
@WebServlet(urlPatterns = {"/LoginServlet"})
public class LoginServlet extends HttpServlet {

    private ClientManagerIF stubClientManager = ClientManagerImpl.getInstance();
    private InterestDAIF stubInterest = InterestDAImpl.getInstance();

    private ServletContext servletContext;
    @Override
    public void init(ServletConfig config){
        this.servletContext = config.getServletContext();
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
        
        String eMail = ParameterExtractor.getParameter(request, "eMail");
        String password = ParameterExtractor.getParameter(request, "password");
        
        NotifiableIF client = new NotifiableImpl(request.getSession());

        Integer userID = null;
        Role role = null;
        User user = null;
        List<Interest> interests = null;
        Throwable exc = null;
        try {
            User u = new User(0);
            u.setE_Mail(eMail);
            u.setPassword(password);
            Result<User> res = stubClientManager.registerClient(client, u);
            if (res.getException() != null) {
                exc = res.getException();
            } else {
                user = res.getResult().get(0);
                userID = user.getID();
                role = user.getRole();
                InterestFilter filter = new InterestFilter();
                filter.users.add(user);
                filter.fetchUsers = true;
                Result<Interest> resInt = stubInterest.fetchInterests(filter);
                if(resInt.getException() != null){
                    exc = resInt.getException();
                } else {
                    interests = resInt.getResult();
                }
            }
        } catch (RemoteException eR) {
            exc = eR;
        }

        if (exc != null) {
            String xml = "<error>" + exc.getMessage() + "</error>";
            response.setContentType("text/xml");
            response.setHeader("Cache-Control", "no-cache");
            String answer = "<root>" + xml + "</root>";
            //System.out.println(answer);
            response.getWriter().write(answer);
        } else {
            request.setAttribute("interests", interests);
            request.setAttribute("userID", userID);
            request.getSession().setAttribute("role", role);
            //System.out.println("userID:" + userID);
            request.getRequestDispatcher("personaltab.jsp").forward(request, response);
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
