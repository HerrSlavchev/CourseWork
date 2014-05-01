/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var targetDiv;

var req;
var isIE;

function init(){
    targetDiv = document.getElementById("targetDiv");
    
    document.getElementById("ddmenu").addEventListener("click",function(e) {
        // e.target is our targetted element.
                    // try doing console.log(e.target.nodeName), it will result LI
        if(e.target && e.target.nodeName === "A") {
            var id = e.target.id;
            showPage(id);
        }
    });
}

function initRequest() {
    if (window.XMLHttpRequest) {
        if (navigator.userAgent.indexOf('MSIE') !== -1) {
            isIE = true;
        }
        return new XMLHttpRequest();
    } else if (window.ActiveXObject) {
        isIE = true;
        return new ActiveXObject("Microsoft.XMLHTTP");
    }
}

function showPage(id){
    var url = "MainServlet?action=" + escape(id);
    req = initRequest();
    req.open("GET", url, true);
    req.onreadystatechange = callback;
    req.send(null);
}

function callback(){
    if (req.readyState === 4) {
        if (req.status === 200) {
            parseMessages(req.responseText);
        }
    }
}

function parseMessages(response){
    targetDiv.innerHTML = response;
}
