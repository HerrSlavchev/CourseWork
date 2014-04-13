<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%-- 
    Document   : index
    Created on : Apr 13, 2014, 12:31:19 AM
    Author     : root
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Hello World!</h1>
        <br>
        <form action="LoginServlet" method="POST">
            E-mail: <input type="text" name="eMail" value="" /> <br>
            Password: <input type="password" name="password" value="" /> <br>
            <input type="submit" value="Login" /> 
        </form>
        <br>
        <input type="button" value="Register" action="RegisterServlet"/>
    </body>
</html>
