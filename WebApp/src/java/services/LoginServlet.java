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
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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

    private ClientManagerIF managerStub = ClientManagerImpl.getInstance();
    private InterestDAIF interestStub = InterestDAImpl.getInstance();

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
        /*
        Map<String, String[]> pairs = request.getParameterMap();
        for(Entry<String, String[]> pair : pairs.entrySet()){
            System.out.println(">>" + pair.getKey());
            for(String str : pair.getValue()){
                System.out.println("<" + str);
            }
        }*/
        String eMail = ParameterExtractor.getParameter(request, "eMail");
        String password = ParameterExtractor.getParameter(request, "password");
        System.out.println("X" + eMail + "|" + password+"X");
        
        NotifiableIF client = new NotifiableImpl(request.getSession());

        User user = null;
        List<Interest> interests = null;
        Throwable exc = null;
        try {
            User u = new User(0);
            u.setE_Mail(eMail);
            u.setPassword(password);
            Result<User> res = managerStub.registerClient(client, u);
            if (res.getException() != null) {
                exc = res.getException();
            } else {
                user = res.getResult().get(0);
                InterestFilter filter = new InterestFilter();
                filter.users.add(user);
                filter.fetchUsers = true;
                Result<Interest> resInt = interestStub.fetchInterests(filter);
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
            System.out.println(exc.getMessage());
            request.setAttribute("errorMsg", exc.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        } else {
            System.out.println("sending");
            request.setAttribute("interests", interests);
            request.getRequestDispatcher("personal.jsp").forward(request, response);
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
