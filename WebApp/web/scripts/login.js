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
    personalDiv.innerHTML = responseText;
    targetDiv.innerHTML = "";
    applyPersonalTabBehaviour();
}

