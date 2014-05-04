package services;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import dto.Result;
import dto.domain.Category;
import dto.domain.SubCategory;
import dto.filters.CategoryFilter;
import dto.filters.SubCategoryFilter;
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
import services.implementations.SubCategoryDAImpl;
import services.server.CategoryDAIF;
import services.server.SubCategoryDAIF;
import utils.ParameterExtractor;

/**
 *
 * @author root
 */
@WebServlet(urlPatterns = {"/SubcategoryServlet"})
public class SubcategoryServlet extends HttpServlet {

    private SubCategoryDAIF stubSubcategory = SubCategoryDAImpl.getInstance();
    private CategoryDAIF stubCategory = CategoryDAImpl.getInstance();
    private ServletContext servletContext;

    @Override
    public void init(ServletConfig config) {
        this.servletContext = config.getServletContext();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        CategoryFilter filterCat = new CategoryFilter();
        SubCategoryFilter filterSub = new SubCategoryFilter();
        List<Category> categories = null;
        List<SubCategory> subcategories = null;
        Throwable exc = null;
        try {
            Result<Category> resCats = stubCategory.fetchCategories(filterCat);
            if (resCats.getException() != null) {
                exc = resCats.getException();
            } else {
                categories = resCats.getResult();
            }
            Result<SubCategory> resSubs = stubSubcategory.fetchSubCategories(filterSub);
            if (resSubs.getException() != null) {
                exc = resSubs.getException();
            } else {
                subcategories = resSubs.getResult();
            }
        } catch (RemoteException eR) {
            exc = eR;
        }

        if (exc != null) {
            request.setAttribute("errorMsg", exc.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        } else {
            request.setAttribute("categories", categories);
            request.setAttribute("subcategories", subcategories);
            //System.out.println("userID:" + userID);
            request.getRequestDispatcher("subcategories.jsp").forward(request, response);
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
        String categoryID = ParameterExtractor.getParameter(request, "category");
        
        List<SubCategory> subcategories = new ArrayList<SubCategory>();

        Throwable exc = null;
        if (insert != null || update != null) {
            action = "write";
            if (name == null) {
                exc = new Exception("Name missing!");
            } else if (categoryID == null){
                exc = new Exception("Category missing!");
            } else {
                if (insert != null) {
                    String sessionCode = (String) request.getSession().getAttribute("sessionCode");
                    SubCategory sub = new SubCategory(0);
                    List<SubCategory> lst = new ArrayList<SubCategory>();
                    lst.add(sub);
                    sub.setName(name);
                    sub.setDescription(description);
                    sub.setCategory(new Category(Integer.parseInt(categoryID)));
                    try {
                        Result<SubCategory> resIns = stubSubcategory.insertSubCategory(lst, new Session(sessionCode));
                        if (resIns.getException() != null) {
                            exc = resIns.getException();
                        } else {
                            SubCategoryFilter filter = new SubCategoryFilter();
                            filter.ids.addAll(resIns.getAutoIDs());
                            Result<SubCategory> res = stubSubcategory.fetchSubCategories(filter);
                            if (res.getException() != null) {
                                exc = res.getException();
                            } else {
                                subcategories = res.getResult();
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
                        SubCategoryFilter filter = new SubCategoryFilter();
                        filter.ids.add(id);
                        Result<SubCategory> resFetch = stubSubcategory.fetchSubCategories(filter);
                        if (resFetch.getException() != null) {
                            exc = resFetch.getException();
                        } else if (resFetch.getResult().isEmpty()) {
                            exc = new Exception("Item not found!");
                        } else {
                            List<SubCategory> lst = new ArrayList();
                            SubCategory oldSub = resFetch.getResult().get(0);
                            oldSub.setName(name);
                            oldSub.setDescription(description);
                            oldSub.setCategory(new Category(Integer.parseInt(categoryID)));
                            lst.add(oldSub);
                            Result<SubCategory> resUpd = stubSubcategory.updateSubCategory(lst, new Session(sessionCode));
                            if (resUpd.getException() != null) {
                                exc = resUpd.getException();
                            } else {
                                resFetch = stubSubcategory.fetchSubCategories(filter);
                                if (resFetch.getException() != null) {
                                    exc = resFetch.getException();
                                } else if (resFetch.getResult().isEmpty()) {
                                    exc = new Exception("Item not found!");
                                } else {
                                    subcategories = resFetch.getResult();
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
                SubCategoryFilter filter = new SubCategoryFilter();
                filter.ids.add(id);
                Result<SubCategory> res = stubSubcategory.fetchSubCategories(filter);
                if (res.getException() != null) {
                    exc = res.getException();
                } else {
                    subcategories = res.getResult();
                }
            }
        }
        StringBuffer sb = new StringBuffer();
        sb.append("<subcategories>");
        for (SubCategory item : subcategories) {
            String descr = action.equals("read") ? item.getDescription() : item.getShortDescription();
            if(descr == null || descr.isEmpty()){
                descr = " ";
            }
            Category cat = item.getCategory();
            String category = action.equals("read") ? cat.getName(): cat.getShortName();
            sb.append("<subcategory>");
            sb.append("<id>" + item.getID() + "</id>");
            sb.append("<name>" + item.getName() + "</name>");
            sb.append("<category>" + category + "</category>");
            sb.append("<interestCount>" + item.getInterestCount() + "</interestCount>");
            sb.append("<shortDescription>" + descr + "</shortDescription>");
            sb.append("</subcategory>");
        }
        sb.append("</subcategories>");

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
