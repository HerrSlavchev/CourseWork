/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

function bindTable(){
    
    var fun = 'show_' + lastid;
    $("#dynamicTable").on("click", ".mainRow", function(event) {
        var target = event.target;
        while(target.className !== "mainRow"){
            target = target.parentNode; 
        }
        
        var firstRow = null;
        var secondtRow = null;
        var thirdRow = null;
        var forthRow = null;
        
        for (var j = 0; j < target.childNodes.length; j++) {
                var row = target.childNodes[j];
                if (row.className == "firstRow") {
                    firstRow = getContent(row);
                }
                if (row.className == "secondtRow") {
                    secondtRow = getContent(row);
                }
                if (row.className == "thirdRow") {
                    thirdRow = getContent(row);
                }
                if (row.className == "forthRow") {
                    forthRow = getContent(row);
                }
            }
            
        
        var arguments = [target.id, firstRow, secondtRow, thirdRow];
        if(forthRow !== null){
            arguments = [target.id, firstRow, secondtRow, thirdRow, forthRow];
        }
        window[fun].apply(null, Array.prototype.slice.call(arguments, 0));
        /*
        if(forthRow !== null){
            window[func].apply(null, Array.prototype.slice.call(arguments, 1));
        } else {
            window[fun](target.id, firstRow.childNodes[0].innerHTML, secondtRow.childNodes[0].innerHTML, thirdRow.childNodes[0].innerHTML);
        }
        */
    });
}

function addRow4(id, firstRow, secondtRow, thirdRow, forthRow) {

    var regDivs = document.getElementsByClassName("mainRow");
    var table = document.getElementById('items');

    var found = false;
    var prefix = '<div class="text">';
    var suffix = '</div>';
    for (var i = 0; i < regDivs.length; i++) {
        var cmp = regDivs[i];
        if (cmp.id === id) {
            for (var j = 0; j < cmp.childNodes.length; j++) {
                var row = cmp.childNodes[j];
                if (row.className == "firstRow") {
                    row.innerHTML = prefix + firstRow + suffix;
                }
                if (row.className == "secondtRow") {
                    row.innerHTML = prefix + secondtRow + suffix;
                }
                if (row.className == "thirdRow") {
                    row.innerHTML = prefix + thirdRow + suffix;
                }
                if (row.className == "forthRow") {
                    row.innerHTML = prefix + forthRow + suffix;
                }
            }
            found = true;
        }
    }

    if (false === found) {
        var divMR = document.createElement('div');
        divMR.id = id;
        divMR.className = "mainRow";

        var divFR = document.createElement('div');
        divFR.className = "firstRow";
        attachDiv("text", firstRow, divFR);

        var divSR = document.createElement('div');
        divSR.className = "secondtRow";
        attachDiv("text", secondtRow, divSR);

        var divTR = document.createElement('div');
        divTR.className = "thirdRow";
        attachDiv("text", thirdRow, divTR);

        var div4R = document.createElement('div');
        div4R.className = "forthRow";
        attachDiv("text", forthRow, div4R);

        divMR.appendChild(divFR);
        divMR.appendChild(divSR);
        divMR.appendChild(divTR);
        divMR.appendChild(div4R);

        table.appendChild(divMR);
    }
}

function attachDiv(className, innerHTML, parent){
    var newDiv = document.createElement('div');
    newDiv.className = className;
    newDiv.innerHTML = innerHTML;
    parent.appendChild(newDiv);
}

function getContent(node){
    var inner = node.innerHTML + "";
    var start = -1;
    start = inner.indexOf(">");
    start = start + 1;
    var end = inner.indexOf("</");
    
    var res = inner.substring(start, end);
    //console.log(res);
    return res;
}