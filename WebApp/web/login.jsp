<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%-- 
    Document   : index
    Created on : Apr 13, 2014, 12:31:19 AM
    Author     : root
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<form action="LoginServlet" method="POST">
    E-mail: <input type="text" name="eMail" value="" /> <br>
    Password: <input type="password" name="password" value="" /> <br>
    <input type="submit" value="Login" /> 
</form>
<br>
