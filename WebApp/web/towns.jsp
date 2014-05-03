<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%-- 
    Document   : towns
    Created on : May 4, 2014, 12:26:47 AM
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
                <div class="text">Region</div>
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
            <c:forEach var="town" items="${requestScope.towns}">
                <div id="${town.ID}" class="mainRow">
                    <div class="firstCol">
                        <div class="text">${town.name}</div>
                    </div>
                    <div class="secondCol">
                        <div class="text">${town.region.shortName}</div>
                    </div>
                    <div class="thirdCol">
                        <div class="text">${town.eventCount}</div>
                    </div>
                    <div class="fourthCol">
                        <div class="text">${town.userCount}</div>
                    </div>
                </div>
            </c:forEach>

            <!-- spam spam spam -->
        </div>
    </div>
    <br />
</div>

<div id="dynamicRight">
    <form id="townsform" action="TownServlet" method="post">

        <fieldset id="names" 
                  <c:if test="${empty sessionScope.role || sessionScope.role.name eq 'User'}">
                      disabled="true"
                  </c:if>
                  >
            <div id="region-fall">
                <div class="label"><label for="reg"><strong>Region</strong></label></div>
                <div class="textField1">
                    <select type="text" id="region" name="region" required="required">
                        <c:forEach var="curr" items="${requestScope.regions}">
                            <option value="${curr.ID}">${curr.name}</option>
                        </c:forEach>
                    </select>
                </div> <br/>
            </div>
            <div class="label"><label for="name"><strong>Name</strong></label></div>
            <div class="textField1"><input type="text" id="name" name="name" placeholder="Region name" required="required"/></div> <br />
            <div id="counts">
                <div class="count">
                    <div class="label"><strong>Events</strong></div>
                    <div id="eventCount" class="textField1">N/A</div>
                </div>
                <div class="count">
                    <div class="label"><strong>Users</strong></div>
                    <div id="userCount" class="textField1">N/A</div>
                </div>
            </div>
            <input type="text" id="id" name="id" hidden="true"/>
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