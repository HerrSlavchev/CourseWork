/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var targetDiv;
var personalDiv;
var req;
var isIE;

var callbacks;
function init() {

    targetDiv = document.getElementById("targetDiv");
    personalDiv = document.getElementById("personalDiv");

    document.getElementById("ddmenu").addEventListener("click", function(e) {
        // e.target is our targetted element
        if (e.target && e.target.nodeName === "A") {
            var id = e.target.id;
            showPage(id);
        }
    });

    callbacks['login'] = callback_login;
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

var lastid = "";
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
            parseMessagesMainPage(req.responseText, lastid);
        }
    }
}

function parseMessagesMainPage(response, id) {
    targetDiv.innerHTML = response;
    registerSubmit(id);
}

function registerSubmit(id) {
    var formID = id + 'form';
    var fun = 'callback_' + id;
    var form = document.getElementById(formID);
    form.onsubmit = function(e) {

        req = initRequest();
        e.preventDefault();

        var f = e.target,
                formData = new FormData(form);

        req.open("POST", f.action, true);
        req.setRequestHeader("Content-type", "application/x-www-form-urlencoded")
        req.onreadystatechange = window[fun];
        req.send(formData);
    }
}

function callback_login() {
    if (req.readyState === 4) {
        if (req.status === 200) {
            parseMessages_login(req.responseText);
        }
    }
}

function parseMessages_login(response) {
    personalDiv.innerHTML = response;
    targetDiv.innerHTML = "";
    applyPersonalTabBehaviour();
}

function applyPersonalTabBehaviour() {

    $('#cssmenu > ul > li > a').click(function() {
        $('#cssmenu li').removeClass('active');
        $(this).closest('li').addClass('active');
        var checkElement = $(this).next();
        if ((checkElement.is('ul')) && (checkElement.is(':visible'))) {
            $(this).closest('li').removeClass('active');
            checkElement.slideUp('normal');
        }
        if ((checkElement.is('ul')) && (!checkElement.is(':visible'))) {
            $('#cssmenu ul ul:visible').slideUp('normal');
            checkElement.slideDown('normal');
        }
        if ($(this).closest('li').find('ul').children().length == 0) {
            return true;
        } else {
            return false;
        }
    });

    $("#cssmenu").on("click", ".interest_unsubscribe", function(event) {
        var target = event.target;
        var realTarget = target.parentNode.parentNode.parentNode;
        removeInterest(realTarget.id);
    });

    $("#cssmenu").on("click", ".notification_unsubscribe", function(event) {
        var target = event.target;
        var realTarget = target.parentNode.parentNode.parentNode;
        changeSubscription(realTarget.id);
    });
}

function changeSubscription(id) {
    lastid = escape(id);
    var url = "InterestServlet?action=changeSubscription&id=" + escape(id);
    req = initRequest();
    req.open("POST", url, true);
    req.onreadystatechange = callback_subscription;
    req.send(null);
}

function callback_subscription() {
    if (req.readyState === 4) {
        if (req.status === 200) {
            parseMessages_subscription(req.responseXML);
        }
    }
}

function parseMessages_subscription(responseXML) {
    if (responseXML == null) {
        return false;
    } else {

        var root = responseXML.getElementsByTagName("root")[0];
        var interests = root.getElementsByTagName("interests")[0];

        if (interests.childNodes.length > 0) {
            for (var loop = 0; loop < interests.childNodes.length; loop++) {
                var interest = interests.childNodes[loop];
                var interestID = interest.getElementsByTagName("id")[0];
                var subscribed = interest.getElementsByTagName("subscribed")[0];
                markInterest(interestID.childNodes[0].nodeValue, subscribed.childNodes[0].nodeValue);
            }
        }
    }
}

function markInterest(id, subscribed) {

    var src = "";
    if (subscribed === 'true') {
        src = "images/see.jpg";
    } else {
        src = "images/nosee.png";
    }
    
    var targetImg = "img_"+id;
    document.getElementById(targetImg).src = src;
}
function removeInterest(id) {

}