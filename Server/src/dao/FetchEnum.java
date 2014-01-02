/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dao;

/**
 *
 * @author root
 */
public enum FetchEnum {
    
    CATEGORY(FilterUtils.fetchCategory),
    CONVERSATION(FilterUtils.fetchConversation),
    EVENT(FilterUtils.fetchEvent),
    GROUP(FilterUtils.fetchGroup),
    INTEREST(FilterUtils.fetchInterest),
    MESSAGE(FilterUtils.fetchMessage),
    PUBLICATION(FilterUtils.fetchPublication),
    ;
    
    private String sql = "";
    
    FetchEnum(String sql){
        this.sql = sql;
    }
    
}
