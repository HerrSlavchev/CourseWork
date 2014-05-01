<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%-- 
    Document   : testjsp
    Created on : Apr 13, 2014, 4:23:14 PM
    Author     : root
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Test Page</title>
    </head>
    <body>
        <table border="1">
            <tbody>
            <c:forEach var="currReg" items="${requestScope.regions}">
                <tr>
                    <td >${currReg.name}</td>
                    <td >${currReg.townCount}</td>
                    <td >${currReg.userCount}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>

    </body>
</html>
