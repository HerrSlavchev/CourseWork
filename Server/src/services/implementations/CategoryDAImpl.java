/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package services.implementations;

import dto.Result;
import dto.domain.Category;
import dto.filters.CategoryFilter;
import dto.session.Session;
import java.rmi.RemoteException;
import java.util.List;
import services.server.CategoryDAIF;

/**
 *
 * @author root
 */
public class CategoryDAImpl implements CategoryDAIF{

    @Override
    public Result<Category> insertCategory(List<Category> ins, Session session) throws RemoteException {
        Throwable exc = new Exception("NOT DONE YET");
        Result res = new Result(null, exc);
        return res;
    }

    @Override
    public Result<Category> updateCategory(List<Category> upd, Session session) throws RemoteException {
        Throwable exc = new Exception("NOT DONE YET");
        Result res = new Result(null, exc);
        return res;
    }

    @Override
    public Result<Category> deleteCategory(List<Category> del, Session session) throws RemoteException {
        Throwable exc = new Exception("NOT DONE YET");
        Result res = new Result(null, exc);
        return res;
    }

    @Override
    public Result<Category> fetchCategories(CategoryFilter filter) throws RemoteException {
        Throwable exc = new Exception("NOT DONE YET");
        Result res = new Result(null, exc);
        return res;
    }
    
}
