/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

function callback_login() {
    if (req.readyState === 4) {
        if (req.status === 200) {
            if (checkError(req)) {
                parseMessages_login(req.responseText);
            }
        }
    }
}

function parseMessages_login(responseText) {
    login();
    personalDiv.innerHTML = responseText;
    applyPersonalTabBehaviour();
}

function login(){
    targetDiv.innerHTML = "";
    
    var userOptions = document.getElementById('useroptions');
    userOptions.innerHTML = '';
    
    var newA = document.createElement('a');
    newA.id = "profile";
    newA.innerHTML = 'Profile';
    userOptions.appendChild(newA);
    
    newA = document.createElement('a');
    newA.id = "logout";
    newA.innerHTML = 'Logout';
    userOptions.appendChild(newA);
    
    document.getElementById("useroptions").addEventListener("click", function(e) {
        // e.target is our targetted element
        if (e.target && e.target.nodeName === "A") {
            var id = e.target.id;
            showPage(id);
        }
    });
}

