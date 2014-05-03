package services;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import dto.Result;
import dto.domain.Category;
import dto.domain.Region;
import dto.filters.CategoryFilter;
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
import services.implementations.CategoryDAImpl;
import services.server.CategoryDAIF;
import utils.ParameterExtractor;

/**
 *
 * @author root
 */
@WebServlet(urlPatterns = {"/CategoriesServlet"})
public class CategoriesServlet extends HttpServlet {

    private CategoryDAIF stubCategory = CategoryDAImpl.getInstance();

    private ServletContext servletContext;

    @Override
    public void init(ServletConfig config) {
        this.servletContext = config.getServletContext();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        CategoryFilter filter = new CategoryFilter();

        List<Category> categories = null;
        Throwable exc = null;
        try {
            Result<Category> res = stubCategory.fetchCategories(filter);
            if (res.getException() != null) {
                exc = res.getException();
            } else {
                categories = res.getResult();
            }
        } catch (RemoteException eR) {
            exc = eR;
        }

        if (exc != null) {
            request.setAttribute("errorMsg", exc.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        } else {
            request.setAttribute("categories", categories);
            //System.out.println("userID:" + userID);
            request.getRequestDispatcher("categories.jsp").forward(request, response);
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
        String description = ParameterExtractor.getParameter(request, "description");
        List<Category> categories = new ArrayList<Category>();

        Throwable exc = null;
        if (insert != null || update != null) {
            action = "write";
            if (name == null) {
                exc = new Exception("Name missing!");
            } else {
                if (insert != null) {
                    String sessionCode = (String) request.getSession().getAttribute("sessionCode");
                    Category reg = new Category(0);
                    List<Category> lst = new ArrayList<Category>();
                    lst.add(reg);
                    reg.setName(name);
                    reg.setDescription(description);
                    try {
                        Result<Category> resIns = stubCategory.insertCategory(lst, new Session(sessionCode));
                        if (resIns.getException() != null) {
                            exc = resIns.getException();
                        } else {
                            CategoryFilter filter = new CategoryFilter();
                            filter.ids.addAll(resIns.getAutoIDs());
                            Result<Category> res = stubCategory.fetchCategories(filter);
                            if (res.getException() != null) {
                                exc = res.getException();
                            } else {
                                categories = res.getResult();
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
                        CategoryFilter filter = new CategoryFilter();
                        filter.ids.add(id);
                        Result<Category> resFetch = stubCategory.fetchCategories(filter);
                        if (resFetch.getException() != null) {
                            exc = resFetch.getException();
                        } else if (resFetch.getResult().isEmpty()) {
                            exc = new Exception("Item not found!");
                        } else {
                            List<Category> lst = new ArrayList();
                            Category oldCat = resFetch.getResult().get(0);
                            oldCat.setName(name);
                            oldCat.setDescription(description);
                            lst.add(oldCat);
                            Result<Category> resUpd = stubCategory.updateCategory(lst, new Session(sessionCode));
                            if (resUpd.getException() != null) {
                                exc = resUpd.getException();
                            } else {
                                resFetch = stubCategory.fetchCategories(filter);
                                if (resFetch.getException() != null) {
                                    exc = resFetch.getException();
                                } else if (resFetch.getResult().isEmpty()) {
                                    exc = new Exception("Item not found!");
                                } else {
                                    categories = resFetch.getResult();
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
                CategoryFilter filter = new CategoryFilter();
                filter.ids.add(id);
                Result<Category> res = stubCategory.fetchCategories(filter);
                if (res.getException() != null) {
                    exc = res.getException();
                } else {
                    categories = res.getResult();
                }
            }
        }
        StringBuffer sb = new StringBuffer();
        sb.append("<categories>");
        for (Category item : categories) {
            String descr = action.equals("read") ? item.getDescription() : item.getShortDescription();
            sb.append("<category>");
            sb.append("<id>" + item.getID() + "</id>");
            sb.append("<name>" + item.getName() + "</name>");
            sb.append("<subCategoryCount>" + item.getSubCategoryCount() + "</subCategoryCount>");
            sb.append("<interestCount>" + item.getInterestCount() + "</interestCount>");
            sb.append("<shortDescription>" + descr + "</shortDescription>");
            sb.append("</category>");
        }
        sb.append("</categories>");

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
