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
                markRegion(regionID.childNodes[0].nodeValue,
                            name.childNodes[0].nodeValue,
                            townCount.childNodes[0].nodeValue,
                            userCount.childNodes[0].nodeValue,
                            eventCount.childNodes[0].nodeValue);
            }
        }
    }
}

function markRegion(id, name, townCount, userCount, eventCount){
    
    personalDiv.innerHTML = id + ":" + name + ":" + townCount + ":" + userCount + ":" + eventCount;
    var regDivs = document.getElementsByClassName("mainRow");
    var table = document.getElementById('items');
    
    var found = false;
    var prefix = '<div class="text">';
    var suffix = '</div>';
    for(var i = 0; i < regDivs.length; i++){
        var cmp = regDivs[i];
        if (cmp.id === id) {
            for (var j = 0; i < cmp.childNodes.length; j++) {
                var row = cmp.childNodes[j];
                if (row.className == "firstRow") {
                    row.innerHTML = prefix + name + suffix;
                }
                if (row.className == "secondtRow") {
                    row.innerHTML = prefix + townCount + suffix;
                }
                if (row.className == "thirdRow") {
                    row.innerHTML = prefix + userCount + suffix;
                }
                if (row.className == "forthRow") {
                    row.innerHTML = prefix + eventCount + suffix;
                }
            }
            found = true;
        }
    }
    
    if(false === found){
        var divMR = document.createElement('div');
        divMR.id = id;
        divMR.className = "mainRow";
        
        var divFR = document.createElement('div');
        divFR.className = "firstRow";
        var divFRT = document.createElement('div');
        divFRT.className = "text";
        divFRT.innerHTML = name;
        divFR.appendChild(divFRT);
        
        var divSR = document.createElement('div');
        divSR.className = "secondtRow";
        var divSRT = document.createElement('div');
        divSRT.className = "text";
        divSRT.innerHTML = townCount;
        divSR.appendChild(divSRT);
        
        var divTR = document.createElement('div');
        divTR.className = "thirdRow";
        var divTRT = document.createElement('div');
        divTRT.className = "text";
        divTRT.innerHTML = userCount;
        divTR.appendChild(divTRT);
        
        var div4R = document.createElement('div');
        div4R.className = "forthRow";
        var div4RT = document.createElement('div');
        div4RT.className = "text";
        div4RT.innerHTML = eventCount;
        div4R.appendChild(div4RT);
        
        divMR.appendChild(divFR);
        divMR.appendChild(divSR);
        divMR.appendChild(divTR);
        divMR.appendChild(div4R);
        
        table.appendChild(divMR);
    }
}
