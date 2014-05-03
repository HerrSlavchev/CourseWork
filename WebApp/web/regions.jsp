<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%-- 
    Document   : regions
    Created on : May 1, 2014, 7:13:06 PM
    Author     : root
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<div id="dynamicTable">
    <div id="tableView1">
        <div class="topPanel1">
            <div class="firstCol">
                <div class="text">Name</div>
            </div>
            <div class="secondCol">
                <div class="text">Towns</div>
            </div>
            <div class="thirdCol">
                <div class="text">Users</div>
            </div>
            <div class="fourthCol">
                <div class="text">Events</div>
            </div>
        </div>
        <div id="items" class="mainPanel1">
            <!-- spam spam spam -->
            <c:forEach var="reg" items="${requestScope.regions}">
                <div id="${reg.ID}" class="mainRow">
                    <div class="firstCol">
                        <div class="text">${reg.name}</div>
                    </div>
                    <div class="secondCol">
                        <div class="text">${reg.townCount}</div>
                    </div>
                    <div class="thirdCol">
                        <div class="text">${reg.userCount}</div>
                    </div>
                    <div class="fourthCol">
                        <div class="text">${reg.eventCount}</div>
                    </div>
                </div>
            </c:forEach>

            <!-- spam spam spam -->
        </div>
    </div>
    <br />
</div>

<div id="dynamicRight">
    <form id="regionsform" action="RegionServlet" method="post">

        <fieldset id="names" 
                  <c:if test="${empty sessionScope.role || sessionScope.role.name eq 'User'}">
                      disabled="true"
                  </c:if>
                  >
            <div class="label"><label for="name"><strong>Name</strong></label></div>
            <div class="textField1"><input type="text" id="name" name="name" placeholder="Region name" required="required"/></div> <br />
            <input type="text" id="id" name="id" hidden="true"/>
            <div id="counts">
                <div class="count">
                    <div class="label"><strong>Towns</strong></div>
                    <div id="towncount" class="textField1">N/A</div>
                </div>
                <div class="count">
                    <div class="label"><strong>Users</strong></div>
                    <div id="usercount" class="textField1">N/A</div>
                </div>
                <div class="count">
                    <div class="label"><strong>Events</strong></div>
                    <div id="eventcount" class="textField1">N/A</div>
                </div>
            </div>
        </fieldset>

        <c:if test="${not empty sessionScope.role && (sessionScope.role.name eq 'Moderator' || sessionScope.role.name eq 'Administrator')}">
            <input type="submit" name="insert" value="Insert" /> 
            <input type="submit" name="update" value="Update" /> 
            <input type="button" id="clearButton" name="clear" value="Clear" /> 
        </c:if>

        <br />
    </form>
    <br />

</div>
