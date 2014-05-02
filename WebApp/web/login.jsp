<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%-- 
    Document   : index
    Created on : Apr 13, 2014, 12:31:19 AM
    Author     : root
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<form id="loginform" action="LoginServlet" method="POST">
    <div id="names">
        <div class="label"><label for="eMail"><strong>E-Mail</strong></label></div>
        <div class="textField1"><input type="text" id="eMail" name="eMail" placeholder="Your e-mail" required="required" /></div> 
        <br/>
        <div class="label"><label for="password"><strong>Password</strong></label></div>
        <div class="textField1"><input type="password" id="password" name="password" required="required" /></div>
    </div>
    <input type="submit" value="Login"/> 
</form>
