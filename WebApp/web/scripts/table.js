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
        
        var firstCol = null;
        var secondCol = null;
        var thirdCol = null;
        var fourthCol = null;
        
        for (var j = 0; j < target.childNodes.length; j++) {
                var row = target.childNodes[j];
                if (row.className == "firstCol") {
                    firstCol = getContent(row);
                }
                if (row.className == "secondCol") {
                    secondCol = getContent(row);
                }
                if (row.className == "thirdCol") {
                    thirdCol = getContent(row);
                }
                if (row.className == "fourthCol") {
                    fourthCol = getContent(row);
                }
            }
            
        
        var arguments = [target.id, firstCol, secondCol, thirdCol];
        if(fourthCol !== null){
            arguments = [target.id, firstCol, secondCol, thirdCol, fourthCol];
        }
        window[fun].apply(null, Array.prototype.slice.call(arguments, 0));
        
    });
}

function addRow4(id, firstCol, secondCol, thirdCol, fourthCol) {

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
                if (row.className == "firstCol") {
                    row.innerHTML = prefix + firstCol + suffix;
                }
                if (row.className == "secondCol") {
                    row.innerHTML = prefix + secondCol + suffix;
                }
                if (row.className == "thirdCol") {
                    row.innerHTML = prefix + thirdCol + suffix;
                }
                if (row.className == "fourthCol") {
                    row.innerHTML = prefix + fourthCol + suffix;
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
        divFR.className = "firstCol";
        attachDiv("text", firstCol, divFR);

        var divSR = document.createElement('div');
        divSR.className = "secondCol";
        attachDiv("text", secondCol, divSR);

        var divTR = document.createElement('div');
        divTR.className = "thirdCol";
        attachDiv("text", thirdCol, divTR);

        var div4R = document.createElement('div');
        div4R.className = "fourthCol";
        attachDiv("text", fourthCol, div4R);

        divMR.appendChild(divFR);
        divMR.appendChild(divSR);
        divMR.appendChild(divTR);
        divMR.appendChild(div4R);

        table.appendChild(divMR);
    }
}

