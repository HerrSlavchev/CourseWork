<%-- 
    Document   : index
    Created on : Apr 13, 2014, 5:07:31 PM
    Author     : root
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>IntSN</title>
        <link href="css/ddmenu.css" rel="stylesheet" type="text/css" />
        <link rel='stylesheet' type='text/css' href='css/right-panel.css' />
        <link rel='stylesheet' type='text/css' href='css/cool-button.css' />
        <script src="scripts/ddmenu.js" type="text/javascript"></script>
        <script src="scripts/mainpage.js" type="text/javascript"></script>
    </head>
    <body onload="init()" style="padding:10px 10px;">
        
        <nav id="ddmenu">
            <ul>
                <li>Me
                    <div>
                        <div class="column">
                            <a id="login">Login</a>
                            <a id="register">Register</a>
                            <a id="profile">Show profile</a>
                            <a id="logout">Logout</a>
                        </div>
                    </div>
                </li>
                <li><a id="interests">Interests</a></li>
                <li><a id="groups">Groups</a></li>
                <li><a id="events">Events</a></li>
                <li><a id="users">Users</a></li>
                <li><a id="categories">Categories</a></li>
                <li><a id="subcategories">Subcategories</a></li>
                <li><a id="regions">Regions</a></li>
                <li><a id="towns">Towns</a></li>
            </ul>
        </nav>
        <br>
        <div id="targetDiv"></div>
    </body>
</html>
