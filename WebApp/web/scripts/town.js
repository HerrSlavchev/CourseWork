/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


function callback_towns() {
    if (req.readyState === 4) {
        if (req.status === 200) {
            if (checkError(req)) {
                parseMessages_towns(req.responseXML);
            }
        }
    }
}

function parseMessages_towns(responseXML) {
    if (responseXML == null) {
        return false;
    } else {

        var root = responseXML.getElementsByTagName("root")[0];
        
        
        
        var error = root.getElementsByTagName("error");
        if(error.length !== 0){
            alert(error[0].childNodes[0].nodeValue);
            return false;
        } 
        
        var action = root.getElementsByTagName("action")[0].childNodes[0].nodeValue;
        var towns = root.getElementsByTagName("towns")[0];

        
        if (towns.childNodes.length > 0) {
            for (var loop = 0; loop < towns.childNodes.length; loop++) {
                var town = towns.childNodes[loop];
                var townID = town.getElementsByTagName("id")[0];
                var name = town.getElementsByTagName("name")[0];
                var region = town.getElementsByTagName("region")[0];
                var eventCount = town.getElementsByTagName("eventCount")[0];
                var userCount = town.getElementsByTagName("userCount")[0];
                
                if(action === "write"){
                    addRow4(townID.childNodes[0].nodeValue,
                            name.childNodes[0].nodeValue,
                            region.childNodes[0].nodeValue,
                            eventCount.childNodes[0].nodeValue,
                            userCount.childNodes[0].nodeValue
                            );
                } else if (action === "read"){
                    show_town(townID.childNodes[0].nodeValue,
                            name.childNodes[0].nodeValue,
                            region.childNodes[0].nodeValue,
                            eventCount.childNodes[0].nodeValue,
                            userCount.childNodes[0].nodeValue
                            );
                }
                
            }
        }
    }
}

function show_towns(id, name, region, eventCount, userCount){
    
    var idx = region.indexOf("...");
    var cmp = region.length - 3;
    if(idx !== cmp){
        show_town(id, name, region, eventCount, userCount);
        return;
    }
    
    
    var url = "TownServlet?action=read&id=" + escape(id);
    req = initRequest();
    req.open("POST", url, true);
    req.onreadystatechange = callback_towns;
    req.send(null);

}

function show_town(id, name, region, eventCount, userCount){
    
    var formid = lastid + 'form';
    
    getChildFromParent(formid, 'id').value = id;
    getChildFromParent(formid, 'name').value = name;
    getChildFromParent(formid, 'eventCount').innerHTML = eventCount;
    getChildFromParent(formid, 'userCount').innerHTML = userCount;
    selectOption('region', region);
    //getChildFromParent(formid, 'region').value = region;
}
