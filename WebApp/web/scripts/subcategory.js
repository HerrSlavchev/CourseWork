/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

function callback_subcategories() {
    if (req.readyState === 4) {
        if (req.status === 200) {
            parseMessages_subcategories(req.responseXML);
        }
    }
}

function parseMessages_subcategories(responseXML) {
    if (responseXML == null) {
        return false;
    } else {

        var root = responseXML.getElementsByTagName("root")[0];
        
        
        
        var error = root.getElementsByTagName("error");
        if(error.length !== 0){
            alert(error[0].childNodes[0].nodeValue);
            return false;
        } 
        
        var action = root.getElementsByTagName("action")[0].childNodes[0].nodeValue;
        var subcategories = root.getElementsByTagName("subcategories")[0];

        
        if (subcategories.childNodes.length > 0) {
            for (var loop = 0; loop < subcategories.childNodes.length; loop++) {
                var subcategory = subcategories.childNodes[loop];
                var subcategoryID = subcategory.getElementsByTagName("id")[0];
                var name = subcategory.getElementsByTagName("name")[0];
                var category = subcategory.getElementsByTagName("category")[0];
                var interestCount = subcategory.getElementsByTagName("interestCount")[0];
                var shortDescription = subcategory.getElementsByTagName("shortDescription")[0];
                if(action === "write"){
                    addRow4(subcategoryID.childNodes[0].nodeValue,
                            name.childNodes[0].nodeValue,
                            category.childNodes[0].nodeValue,
                            interestCount.childNodes[0].nodeValue,
                            shortDescription.childNodes[0].nodeValue);
                } else if (action === "read"){
                    show_subcategory(subcategoryID.childNodes[0].nodeValue,
                            name.childNodes[0].nodeValue,
                            category.childNodes[0].nodeValue,
                            interestCount.childNodes[0].nodeValue,
                            shortDescription.childNodes[0].nodeValue);
                }
                
            }
        }
    }
}

function show_subcategories(id, name, category, interestCount, shortDescription){
    
    var idxDescr = shortDescription.indexOf("...");
    var cmpDescr = shortDescription.length - 3;
    var idxCat = category.indexOf("...");
    var cmpCat = category.length - 3;
    if(idxDescr !== cmpDescr && idxCat !== cmpCat){
        show_subcategory(id, name, category, interestCount, shortDescription);
        return;
    }
    
    
    var url = "SubcategoryServlet?action=read&id=" + escape(id);
    req = initRequest();
    req.open("POST", url, true);
    req.onreadystatechange = callback_subcategories;
    req.send(null);

}

function show_subcategory(id, name, category, interestCount, description){
    
    var formid = lastid + 'form';
    
    getChildFromParent(formid, 'id').value = id;
    getChildFromParent(formid, 'name').value = name;
    selectOption('category', category);
    getChildFromParent(formid, 'interestCount').innerHTML = interestCount;
    getChildFromParent(formid, 'description').value = description;
}
