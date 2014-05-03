/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

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

    var targetImg = "img_" + id;
    document.getElementById(targetImg).src = src;
}
function removeInterest(id) {

}
