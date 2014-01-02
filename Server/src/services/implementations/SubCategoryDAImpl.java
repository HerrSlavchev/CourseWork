/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package services.implementations;

import dto.Result;
import dto.domain.SubCategory;
import dto.filters.SubCategoryFilter;
import dto.session.Session;
import java.rmi.RemoteException;
import java.util.List;
import services.server.SubCategoryDAIF;

/**
 *
 * @author root
 */
public class SubCategoryDAImpl implements SubCategoryDAIF{

    @Override
    public Result<SubCategory> insertSubCategory(List<SubCategory> ins, Session session) throws RemoteException {
        Throwable exc = new Exception("NOT DONE YET");
        Result res = new Result(null, exc);
        return res;
    }

    @Override
    public Result<SubCategory> updateSubCategory(List<SubCategory> upd, Session session) throws RemoteException {
        Throwable exc = new Exception("NOT DONE YET");
        Result res = new Result(null, exc);
        return res;
    }

    @Override
    public Result<SubCategory> deleteSubCategory(List<SubCategory> del, Session session) throws RemoteException {
        Throwable exc = new Exception("NOT DONE YET");
        Result res = new Result(null, exc);
        return res;
    }

    @Override
    public Result<SubCategory> fetchSubCategories(SubCategoryFilter filter) throws RemoteException {
        Throwable exc = new Exception("NOT DONE YET");
        Result res = new Result(null, exc);
        return res;
    }
    
}
