/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

function callback_categories() {
    if (req.readyState === 4) {
        if (req.status === 200) {
            if (checkError(req)) {
                parseMessages_categories(req.responseXML);
            }
        }
    }
}

function parseMessages_categories(responseXML) {
    if (responseXML == null) {
        return false;
    } else {

        var root = responseXML.getElementsByTagName("root")[0];



        var error = root.getElementsByTagName("error");
        if (error.length !== 0) {
            alert(error[0].childNodes[0].nodeValue);
            return false;
        }

        var action = root.getElementsByTagName("action")[0].childNodes[0].nodeValue;
        var categories = root.getElementsByTagName("categories")[0];


        if (categories.childNodes.length > 0) {
            for (var loop = 0; loop < categories.childNodes.length; loop++) {
                var category = categories.childNodes[loop];
                var categoryID = category.getElementsByTagName("id")[0];
                var name = category.getElementsByTagName("name")[0];
                var subCategoryCount = category.getElementsByTagName("subCategoryCount")[0];
                var interestCount = category.getElementsByTagName("interestCount")[0];
                var shortDescription = category.getElementsByTagName("shortDescription")[0];
                if (action === "write") {
                    addRow4(categoryID.childNodes[0].nodeValue,
                            name.childNodes[0].nodeValue,
                            subCategoryCount.childNodes[0].nodeValue,
                            interestCount.childNodes[0].nodeValue,
                            shortDescription.childNodes[0].nodeValue);
                } else if (action === "read") {
                    show_category(categoryID.childNodes[0].nodeValue,
                            name.childNodes[0].nodeValue,
                            subCategoryCount.childNodes[0].nodeValue,
                            interestCount.childNodes[0].nodeValue,
                            shortDescription.childNodes[0].nodeValue);
                }

            }
        }
    }
}

function show_categories(id, name, subCategoryCount, interestCount, shortDescription) {

    var idx = shortDescription.indexOf("...");
    var cmp = shortDescription.length - 3;
    if (idx !== cmp) {
        show_category(id, name, subCategoryCount, interestCount, shortDescription);
        return;
    }


    var url = "CategoryServlet?action=read&id=" + escape(id);
    req = initRequest();
    req.open("POST", url, true);
    req.onreadystatechange = callback_categories;
    req.send(null);

}

function show_category(id, name, subCategoryCount, interestCount, description) {

    var formid = lastid + 'form';

    getChildFromParent(formid, 'id').value = id;
    getChildFromParent(formid, 'name').value = name;
    getChildFromParent(formid, 'subCategoryCount').innerHTML = subCategoryCount;
    getChildFromParent(formid, 'interestCount').innerHTML = interestCount;
    getChildFromParent(formid, 'description').value = description;
}
