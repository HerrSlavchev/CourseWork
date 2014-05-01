/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package utils;

import java.util.Map;
import java.util.Map.Entry;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author root
 */
public class ParameterExtractor {
    
    public static String getParameter(HttpServletRequest request, String key){
        String res = null;
        res = request.getParameter(key);
        if(res != null){
            return res;
        } else {
            Map<String, String[]> params = request.getParameterMap();
            for(Entry<String, String[]> pair : params.entrySet()){
                String name = pair.getKey();
                String[] values = pair.getValue();
                if(name.contains("-----------------------------")) { //form data from ajax, ugly stuff awaits us!
                    for(String str : values){
                        String search = "\"" + key + "\"";
                        int start = str.indexOf(search);
                        if(start != -1){
                            int realStart = start + search.length() + 4;
                            int end = str.indexOf("----", realStart) - 2;
                            res = str.substring(realStart, end);
                            return res;
                        }
                    }
                }
            }
        }
        
        return res;
    }
    
    public static String[] getParameters(HttpServletRequest request, String key){
        String[] res = null;
        res = request.getParameterValues(key);
        if(res != null){
            return res;
        }
        return res;
    }
}
