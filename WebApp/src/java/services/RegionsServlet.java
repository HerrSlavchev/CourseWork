package services;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import dto.Result;
import dto.domain.Region;
import dto.filters.RegionFilter;
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
import services.implementations.RegionDAImpl;
import services.server.RegionDAIF;
import utils.ParameterExtractor;

/**
 *
 * @author root
 */
@WebServlet(urlPatterns = {"/RegionsServlet"})
public class RegionsServlet extends HttpServlet {

    private RegionDAIF stubRegion = RegionDAImpl.getInstance();

    private ServletContext servletContext;

    @Override
    public void init(ServletConfig config) {
        this.servletContext = config.getServletContext();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RegionFilter filter = new RegionFilter();

        List<Region> regions = null;
        Throwable exc = null;
        try {
            Result<Region> res = stubRegion.fetchRegions(filter);
            if (res.getException() != null) {
                exc = res.getException();
            } else {
                regions = res.getResult();
            }
        } catch (RemoteException eR) {
            exc = eR;
        }

        if (exc != null) {
            request.setAttribute("errorMsg", exc.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        } else {
            request.setAttribute("regions", regions);
            //System.out.println("userID:" + userID);
            request.getRequestDispatcher("regions.jsp").forward(request, response);
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

        String update = ParameterExtractor.getParameter(request, "update");
        String insert = ParameterExtractor.getParameter(request, "insert");
        String name = ParameterExtractor.getParameter(request, "name");
        List<Region> regions = new ArrayList<Region>();
        Throwable exc = null;
        if (insert != null || update != null) {
            if (name == null) {
                exc = new Exception("Name missing!");
            } else {
                if (insert != null) {
                    String sessionCode = (String) request.getSession().getAttribute("sessionCode");
                    Region reg = new Region(0);
                    List<Region> lst = new ArrayList<Region>();
                    lst.add(reg);
                    reg.setName(name);
                    try {
                        Result<Region> resIns = stubRegion.insertRegion(lst, new Session(sessionCode));
                        if (resIns.getException() != null) {
                            exc = resIns.getException();
                        } else {
                            RegionFilter filter = new RegionFilter();
                            filter.ids.addAll(resIns.getAutoIDs());
                            Result<Region> res = stubRegion.fetchRegions(filter);
                            if (res.getException() != null) {
                                exc = res.getException();
                            } else {
                                regions = res.getResult();
                            }
                        }
                    } catch (RemoteException eR) {
                        exc = eR;
                    }
                } else if (update != null) {
                    String sessionCode = (String) request.getSession().getAttribute("sessionCode");
                    String targetID = ParameterExtractor.getParameter(request, "id");
                    if(targetID == null || targetID.isEmpty()){
                        exc = new Exception("Missing id!");
                    } else {
                        int id = Integer.parseInt(targetID);
                        RegionFilter filter = new RegionFilter();
                        filter.ids.add(id);
                        Result<Region> resFetch = stubRegion.fetchRegions(filter);
                        if(resFetch.getException() != null){
                            exc = resFetch.getException();
                        } else if (resFetch.getResult().isEmpty()) {
                            exc = new Exception("Item not found!");
                        } else {
                            List<Region> lst = new ArrayList();
                            Region oldReg = resFetch.getResult().get(0);
                            oldReg.setName(name);
                            lst.add(oldReg);
                            Result<Region> resUpd = stubRegion.updateRegion(lst, new Session(sessionCode));
                            if (resUpd.getException() != null) {
                                exc = resUpd.getException();
                            } else {
                                resFetch = stubRegion.fetchRegions(filter);
                                if (resFetch.getException() != null) {
                                    exc = resFetch.getException();
                                } else if (resFetch.getResult().isEmpty()) {
                                    exc = new Exception("Item not found!");
                                } else {
                                    regions = resFetch.getResult();
                                }
                            }
                        }
                    }
                }
            }
        }
        StringBuffer sb = new StringBuffer();
        sb.append("<regions>");
        for (Region item : regions) {

            sb.append("<region>");
            sb.append("<id>" + item.getID() + "</id>");
            sb.append("<name>" + item.getName() + "</name>");
            sb.append("<townCount>" + item.getTownCount() + "</townCount>");
            sb.append("<userCount>" + item.getUserCount() + "</userCount>");
            sb.append("<eventCount>" + item.getEventCount() + "</eventCount>");
            sb.append("</region>");
        }
        sb.append("</regions>");

        if (exc != null) {
            String xml = "<error>" + exc.getMessage() + "</error>";
            response.setContentType("text/xml");
            response.setHeader("Cache-Control", "no-cache");
            String answer = "<root>" + xml + "</root>";
            //System.out.println(answer);
            response.getWriter().write(answer);
        } else {
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
