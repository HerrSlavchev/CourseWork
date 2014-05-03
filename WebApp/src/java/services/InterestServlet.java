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
import dto.session.Session;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import services.implementations.ClientManagerImpl;
import services.implementations.InterestDAImpl;
import services.server.ClientManagerIF;
import services.server.InterestDAIF;
import utils.ParameterExtractor;

/**
 *
 * @author root
 */
@WebServlet(urlPatterns = {"/InterestServlet"})
public class InterestServlet extends HttpServlet {

    private ClientManagerIF stubClientManager = ClientManagerImpl.getInstance();
    private InterestDAIF stubInterest = InterestDAImpl.getInstance();

    private ServletContext servletContext;

    @Override
    public void init(ServletConfig config) {
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

        String action = ParameterExtractor.getParameter(request, "action");
        String id = ParameterExtractor.getParameter(request, "id");

        String sessionCode = (String) request.getSession().getAttribute("sessionCode");

        Integer userID = null;
        if (sessionCode != null) {
            userID = ClientManagerImpl.getID(new Session(sessionCode));
        }
        List<Interest> interests = new ArrayList();
        Throwable exc = null;
        try {
            if (action == null || action.isEmpty()) {
                exc = new Exception("No action specified!");
            } else if (id == null || id.isEmpty()) {
                exc = new Exception("No interest specified!");
            } else if (action.equals("changeSubscription")) {
                InterestFilter filter = new InterestFilter();
                filter.ids.add(Integer.parseInt(id));
                filter.fetchUsers = true;
                Result<Interest> resInt = stubInterest.fetchInterests(filter);
                if (resInt.getException() != null) {
                    exc = resInt.getException();
                } else {
                    Interest intr = resInt.getResult().get(0);
                    
                    User usr = null;
                    for (User cmp : intr.getUsers().getOldChildren()) {
                        if (cmp.getID() == userID) {
                            usr = cmp;
                        }
                    }
                    boolean isNotified = usr.isNotify();
                    usr.setModified(true);
                    usr.setNotify(!isNotified);
                    List<Interest> lst = new ArrayList<Interest>();
                    lst.add(intr);
                    Result<Interest> resMod = stubInterest.updateInterest(lst, new Session(sessionCode));
                    resInt = stubInterest.fetchInterests(filter);
                    if (resInt.getException() != null) {
                        exc = resInt.getException();
                    } else {
                        interests = resInt.getResult();
                    }
                }
            }
        } catch (RemoteException eR) {
            exc = eR;
        }

        StringBuffer sb = new StringBuffer();
        sb.append("<interests>");
        for (Interest item : interests) {
            User usr = null;
            for (User cmp : item.getUsers().getOldChildren()) {
                if (cmp.getID() == userID) {
                    usr = cmp;
                }
            }
            boolean isNotified = usr.isNotify();
            sb.append("<interest>");
            sb.append("<id>" + item.getID() + "</id>");
            sb.append("<subscribed>" + isNotified + "</subscribed>");
            sb.append("</interest>");
        }
        sb.append("</interests>");

        if (exc != null) {
            exc.printStackTrace();
        }
        String xml = sb.toString();

        if (false == xml.isEmpty()) { //there are some records
            response.setContentType("text/xml");
            response.setHeader("Cache-Control", "no-cache");
            String answer = "<root>" + xml + "</root>";
            //System.out.println(answer);
            response.getWriter().write(answer);
        } else { //no records found
            //nothing to show
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
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
