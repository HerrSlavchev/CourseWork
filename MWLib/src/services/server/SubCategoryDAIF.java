/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package services.server;

import dto.Result;
import dto.domain.SubCategory;
import dto.filters.SubCategoryFilter;
import dto.session.Session;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 *
 * @author root
 */
public interface SubCategoryDAIF extends Remote{
    
    public Result<SubCategory> insertSubCategory(List<SubCategory> ins, Session session) throws RemoteException;
    public Result<SubCategory> updateSubCategory(List<SubCategory> upd, Session session) throws RemoteException;
    public Result<SubCategory> deleteSubCategory(List<SubCategory> del, Session session) throws RemoteException;
    public Result<SubCategory> fetchSubCategories(SubCategoryFilter filter) throws RemoteException;
}
