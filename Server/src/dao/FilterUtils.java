/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dao;

import dto.domain.PersistedDTO;
import dto.filters.AbstractFilter;
import dto.filters.RegionFilter;
import dto.filters.UserFilter;
import java.util.List;

/**
 * Experimental : used to interpret Filter objects and build SELECT clauses from them.
 * @author root
 */
public class FilterUtils {
    
    public static final String CATEGORY = "cat";
    public static final String CONVERSATION = "con";
    public static final String EVENT = "evt";
    public static final String GROUP = "grp";
    public static final String INTEREST = "int";
    public static final String MESSAGE = "msg";
    public static final String PUBLICATION = "pub";
    public static final String REGION = "reg";
    public static final String SUBCATEGORY = "sbc";
    public static final String TOWN = "twn";
    public static final String USER = "usr";
    /**
     * Commonly used for select statements;
     */
    public static final String fetchCategory = ", tbl.ID AS tbl_ID, tbl.name AS tbl_name".replaceAll("tbl", CATEGORY);
    public static final String fetchConversation = ", tbl.ID AS tbl_ID, tbl.topic AS tbl_topic, tbl.timeupd AS tbl_timeupd".replaceAll("tbl", CONVERSATION);
    public static final String fetchEvent = ", tbl.ID AS tbl_ID, tbl.name AS tbl_name, tbl.datefrom AS tbl_datefrom, tbl.dateto AS tbl_dateto, tbl.timeupd AS tbl_timeupd".replaceAll("tbl", EVENT);
    public static final String fetchGroup = ", tbl.ID AS tbl_ID, tbl.name AS tbl_name, tbl.timeupd AS tbl_timeupd".replaceAll("tbl", CONVERSATION);
    public static final String fetchInterest = ", tbl.ID AS tbl_ID, tbl.name AS tbl_name, tbl.timeupd AS tbl_timeupd".replaceAll("tbl", INTEREST);
    public static final String fetchMessage = ", tbl.ID AS tbl_ID, tbl.text AS tbl_text, tbl.timeupd AS tbl_timeupd".replaceAll("tbl", MESSAGE);
    public static final String fetchPublication = ", tbl.ID AS tbl_ID, tbl.text AS tbl_text, tbl.timeupd AS tbl_timeupd".replaceAll("tbl", PUBLICATION);
    public static final String fetchRegion = ", tbl.ID AS tbl_ID, tbl.name AS tbl_name".replaceAll("tbl", REGION);
    public static final String fetchSubcategory = ", tbl.ID AS tbl_ID, tbl.name AS tbl_name".replaceAll("tbl", SUBCATEGORY);
    public static final String fetchTown = ", tbl.ID AS tbl_ID, tbl.name AS tbl_name".replaceAll("tbl", TOWN);
    public static final String fetchUser = ", tbl.ID AS tbl_ID, tbl.f_name AS tbl_f_name, tbl.l_name AS tbl_l_name, tbl.e_mail AS tbl_e_mail, tbl.timeupd AS tbl_timeupd".replaceAll("tbl", USER);
    
    public static String prepareWHEREClause(AbstractFilter af) {
        StringBuilder sb = new StringBuilder(64);
        
        sb.append(" WHERE");
        //process ids, if any
        List<Integer> ids = af.ids;
        if (ids != null && false == ids.isEmpty()) {
            String tbl = "";
            if(af instanceof RegionFilter) {
                tbl = REGION;
            } else if (af instanceof UserFilter){
                tbl = USER;
            }
            sb.append(" ");
            sb.append(tbl);
            sb.append(".ID IN");
            sb.append("(");
            for (Integer i : ids) {
                sb.append(i);
                sb.append(", ");
            }
            sb.append(")");
            String all = sb.toString();
            String fixed = all.replace(", )", ")");
            sb = new StringBuilder(64);
            sb.append(fixed);
            sb.append(" AND");
        }
        
        //process all extra "tbl.ID IN" filters
        String[] filters = {
            generateInClause(af.getCategories(), CATEGORY),
            generateInClause(af.getConversations(), CONVERSATION),
            generateInClause(af.getEvents(), EVENT),
            generateInClause(af.getGroups(), GROUP),
            generateInClause(af.getInterests(), INTEREST),
            generateInClause(af.getMessages(), MESSAGE),
            generateInClause(af.getPublications(), PUBLICATION),
            generateInClause(af.getRegions(), REGION),
            generateInClause(af.getSubCategories(), SUBCATEGORY),
            generateInClause(af.getTowns(), TOWN),
            generateInClause(af.getUsers(), USER),
        };
        
        //append appropriate results to the query
        for (String flt : filters){
            if (false == flt.isEmpty()){
                sb.append(flt);
                sb.append(" AND");
            }
        }
        
        String res = sb.toString();
        if (res.equals(" WHERE ")) { //no clauses were added
            return "";
        }
        
        String fixed = res.substring(0, res.length() - " AND".length());
        return fixed;
    }

    /**
     * 
     * @param lst - a list of objects, the IDs of which will be used as criteria
     * @param tbl - name of the table
     * @return - an empty string if the list is null or empty OR a string in the form " tbl.ID IN (id1, id2, id3, .., idx)"
     */
    public static String generateInClause(List<? extends PersistedDTO> lst, String tbl) {
        if (lst == null || lst.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder(256);
        sb.append(" ");
        sb.append(tbl);
        sb.append(".ID IN");
        sb.append("(");
        for (PersistedDTO dto : lst) {
            sb.append(dto.getID());
            sb.append(", ");
        }
        sb.append(")");
        String all = sb.toString();
        String fixed = all.replace(", )", ")");
        return fixed;
    }

    
}
