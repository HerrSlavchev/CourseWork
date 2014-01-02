/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package services.server;

import dto.Result;
import dto.domain.Category;
import dto.filters.CategoryFilter;
import dto.session.Session;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 *
 * @author root
 */
public interface CategoryDAIF extends Remote{
    
    public Result<Category> insertCategory(List<Category> ins, Session session) throws RemoteException;
    public Result<Category> updateCategory(List<Category> upd, Session session) throws RemoteException;
    public Result<Category> deleteCategory(List<Category> del, Session session) throws RemoteException;
    public Result<Category> fetchCategories(CategoryFilter filter) throws RemoteException;
}
