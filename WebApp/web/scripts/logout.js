/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
function callback_logout() {
    if (req.readyState === 4) {
        if (req.status === 200) {
            if (checkError(req)) {
                parseMessages_logout(req.responseXML);
            }
        }
    }
}

function parseMessages_logout(responseXML) {
    if(responseXML !== null){
        var root = responseXML.getElementsByTagName("root")[0];
        var success = root.getElementsByTagName("success");
        if(success.length !== 0){
            logout();
        } 
    }
}




function logout(){
    
    console.log("logout");
    targetDiv.innerHTML = "";
    personalDiv.innerHTML = "";
    
    var userOptions = document.getElementById('useroptions');
    userOptions.innerHTML = '';
    
    
    var newA = document.createElement('a');
    newA.id = "login";
    newA.innerHTML = 'Login';
    userOptions.appendChild(newA);
    
    newA = document.createElement('a');
    newA.id = "register";
    newA.innerHTML = 'Register';
    userOptions.appendChild(newA);
    
    document.getElementById("useroptions").addEventListener("click", function(e) {
        // e.target is our targetted element
        if (e.target && e.target.nodeName === "A") {
            var id = e.target.id;
            showPage(id);
        }
    });
}