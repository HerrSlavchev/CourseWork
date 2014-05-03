/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var targetDiv;
var personalDiv;
var req;
var isIE;

//use to manipulate the main page, container for all other subpages
function init() {

    targetDiv = document.getElementById("targetDiv");
    personalDiv = document.getElementById("personalDiv");

    //associate the showPage method with all links in the navigation menu
    document.getElementById("ddmenu").addEventListener("click", function(e) {
        // e.target is our targetted element
        if (e.target && e.target.nodeName === "A") {
            var id = e.target.id;
            showPage(id);
        }
    });

}

//common for every AJAX -- use to get object for a normal browser or IE
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

//pass the id around methods via global variable
var lastid = "";

//fetch the subpage jsp from the main controller via ajax
function showPage(id) {
    lastid = escape(id);
    var url = "MainServlet?action=" + lastid;
    req = initRequest();
    req.open("GET", url, true);
    req.onreadystatechange = callback_MainPage;
    req.send(null);
}

function callback_MainPage() {
    if (req.readyState === 4) {
        if (req.status === 200) {
            //subpage jsp returned as text
            parseMessagesMainPage(req.responseText, lastid);
        }
    }
}

function parseMessagesMainPage(response, id) {
    targetDiv.innerHTML = response;
    registerSubmit(id);
}

//prepare the common behaviour for newly loaded subpage
function registerSubmit(id) {
    
    //naming follows convention, based on id
    var formID = id + 'form';
    var fun = 'callback_' + id;
    
    var formX = document.getElementById(formID);
    /*
    var clearB = document.getElementById('clearButton');
    if (clearB !== undefined) {
        clearB.onclick = function() {
            formX.reset();
        };
    }
    */
    
    //make form submission work via AJAX
    formX.onsubmit = function(e) {

        req = initRequest();
        e.preventDefault();

        var f = e.target,
                formData = new FormData(formX);
        
        req.open("POST", f.action, true);
        req.setRequestHeader("Content-type", "application/x-www-form-urlencoded")
        req.onreadystatechange = window[fun];
        req.send(formData);
    }
}


