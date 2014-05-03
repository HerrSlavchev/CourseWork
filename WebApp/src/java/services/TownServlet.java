package services;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import dto.Result;
import dto.domain.Category;
import dto.domain.Region;
import dto.domain.Town;
import dto.filters.CategoryFilter;
import dto.filters.RegionFilter;
import dto.filters.TownFilter;
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
import services.implementations.TownDAImpl;
import services.server.RegionDAIF;
import services.server.TownDAIF;
import utils.ParameterExtractor;

/**
 *
 * @author root
 */
@WebServlet(urlPatterns = {"/TownServlet"})
public class TownServlet extends HttpServlet {

    private TownDAIF stubTown = TownDAImpl.getInstance();
    private RegionDAIF stubRegion = RegionDAImpl.getInstance();
    
    private ServletContext servletContext;

    @Override
    public void init(ServletConfig config) {
        this.servletContext = config.getServletContext();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        TownFilter filterTown = new TownFilter();
        RegionFilter filterRegion = new RegionFilter();

        List<Town> towns = null;
        List<Region> regions = null;
        
        Throwable exc = null;
        try {
            Result<Town> resTown = stubTown.fetchTown(filterTown);
            if (resTown.getException() != null) {
                exc = resTown.getException();
            } else {
                towns = resTown.getResult();
            }
            Result<Region> resReg = stubRegion.fetchRegions(filterRegion);
            if (resReg.getException() != null) {
                exc = resReg.getException();
            } else {
                regions = resReg.getResult();
            }
        } catch (RemoteException eR) {
            exc = eR;
        }

        if (exc != null) {
            request.setAttribute("errorMsg", exc.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        } else {
            request.setAttribute("towns", towns);
            request.setAttribute("regions", regions);
            //System.out.println("userID:" + userID);
            request.getRequestDispatcher("towns.jsp").forward(request, response);
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
        String action = ParameterExtractor.getParameter(request, "action");
        String name = ParameterExtractor.getParameter(request, "name");
        String regionID = ParameterExtractor.getParameter(request, "region");
        
        List<Town> towns = new ArrayList<Town>();

        Throwable exc = null;
        if (insert != null || update != null) {
            action = "write";
            if (name == null) {
                exc = new Exception("Name missing!");
            } else if (regionID == null){
                exc = new Exception("Region missing!");
            } else {
                if (insert != null) {
                    String sessionCode = (String) request.getSession().getAttribute("sessionCode");
                    Town town = new Town(0);
                    List<Town> lst = new ArrayList<Town>();
                    lst.add(town);
                    town.setName(name);
                    town.setRegion(new Region(Integer.parseInt(regionID)));
                    try {
                        Result<Town> resIns = stubTown.insertTown(lst, new Session(sessionCode));
                        if (resIns.getException() != null) {
                            exc = resIns.getException();
                        } else {
                            TownFilter filter = new TownFilter();
                            filter.ids.addAll(resIns.getAutoIDs());
                            Result<Town> res = stubTown.fetchTown(filter);
                            if (res.getException() != null) {
                                exc = res.getException();
                            } else {
                                towns = res.getResult();
                            }
                        }
                    } catch (RemoteException eR) {
                        exc = eR;
                    }
                } else if (update != null) {
                    String sessionCode = (String) request.getSession().getAttribute("sessionCode");
                    String targetID = ParameterExtractor.getParameter(request, "id");
                    if (targetID == null || targetID.isEmpty()) {
                        exc = new Exception("Missing id!");
                    } else {
                        int id = Integer.parseInt(targetID);
                        TownFilter filter = new TownFilter();
                        filter.ids.add(id);
                        Result<Town> resFetch = stubTown.fetchTown(filter);
                        if (resFetch.getException() != null) {
                            exc = resFetch.getException();
                        } else if (resFetch.getResult().isEmpty()) {
                            exc = new Exception("Item not found!");
                        } else {
                            List<Town> lst = new ArrayList();
                            Town oldTown = resFetch.getResult().get(0);
                            oldTown.setName(name);
                            oldTown.setRegion(new Region(Integer.parseInt(regionID)));
                            lst.add(oldTown);
                            Result<Town> resUpd = stubTown.updateTown(lst, new Session(sessionCode));
                            if (resUpd.getException() != null) {
                                exc = resUpd.getException();
                            } else {
                                resFetch = stubTown.fetchTown(filter);
                                if (resFetch.getException() != null) {
                                    exc = resFetch.getException();
                                } else if (resFetch.getResult().isEmpty()) {
                                    exc = new Exception("Item not found!");
                                } else {
                                    towns = resFetch.getResult();
                                }
                            }
                        }
                    }
                }
            }
        } else if (action.equals("read")) {

            String targetID = ParameterExtractor.getParameter(request, "id");
            if (targetID == null || targetID.isEmpty()) {
                exc = new Exception("Missing id!");
            } else {
                int id = Integer.parseInt(targetID);
                TownFilter filter = new TownFilter();
                filter.ids.add(id);
                Result<Town> res = stubTown.fetchTown(filter);
                if (res.getException() != null) {
                    exc = res.getException();
                } else {
                    towns = res.getResult();
                }
            }
        }
        StringBuffer sb = new StringBuffer();
        sb.append("<towns>");
        for (Town item : towns) {
            Region reg = item.getRegion();
            String regName = action.equals("read") ? reg.getName() : reg.getShortName();
            sb.append("<town>");
            sb.append("<id>" + item.getID() + "</id>");
            sb.append("<name>" + item.getName() + "</name>");
            sb.append("<eventCount>" + item.getEventCount()+ "</eventCount>");
            sb.append("<userCount>" + item.getUserCount()+ "</userCount>");
            sb.append("<region>" + regName + "</region>");
            sb.append("</town>");
        }
        sb.append("</towns>");

        if (exc != null) {
            String xml = "<error>" + exc.getMessage() + "</error>";
            response.setContentType("text/xml");
            response.setHeader("Cache-Control", "no-cache");
            String answer = "<root>" + xml + "</root>";
            //System.out.println(answer);
            response.getWriter().write(answer);
        } else {
            String xml = sb.toString();

            String actionTag = "<action>" + action + "</action>";
            if (false == xml.isEmpty()) { //there are some records
                response.setContentType("text/xml");
                response.setHeader("Cache-Control", "no-cache");
                String answer = "<root>" + actionTag + xml + "</root>";
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
