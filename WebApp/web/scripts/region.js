/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

function callback_regions() {
    if (req.readyState === 4) {
        if (req.status === 200) {
            parseMessages_regions(req.responseXML);
        }
    }
}

function parseMessages_regions(responseXML) {
    if (responseXML == null) {
        return false;
    } else {

        var root = responseXML.getElementsByTagName("root")[0];
        var regions = root.getElementsByTagName("regions")[0];

        if (regions.childNodes.length > 0) {
            for (var loop = 0; loop < regions.childNodes.length; loop++) {
                var region = regions.childNodes[loop];
                var regionID = region.getElementsByTagName("id")[0];
                var name = region.getElementsByTagName("name")[0];
                var townCount = region.getElementsByTagName("townCount")[0];
                var userCount = region.getElementsByTagName("userCount")[0];
                var eventCount = region.getElementsByTagName("eventCount")[0];
                addRow4(regionID.childNodes[0].nodeValue,
                            name.childNodes[0].nodeValue,
                            townCount.childNodes[0].nodeValue,
                            userCount.childNodes[0].nodeValue,
                            eventCount.childNodes[0].nodeValue);
            }
        }
    }
}

function show_regions(id, name, townCount, userCount, eventCount){
    
    var formid = lastid + 'form';
    
    getChildFromParent(formid, 'id').value = id;
    getChildFromParent(formid, 'name').value = name;
    getChildFromParent(formid, 'towncount').innerHTML = townCount;
    getChildFromParent(formid, 'usercount').innerHTML = userCount;
    getChildFromParent(formid, 'eventcount').innerHTML = eventCount;
}
