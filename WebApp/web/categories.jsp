<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%-- 
    Document   : categories
    Created on : May 3, 2014, 6:30:16 PM
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
                <div class="text">Subcategories</div>
            </div>
            <div class="thirdCol">
                <div class="text">Interests</div>
            </div>
            <div class="fourthCol">
                <div class="text">Description</div>
            </div>
        </div>
        <div id="items" class="mainPanel1">
            <!-- spam spam spam -->
            <c:forEach var="cat" items="${requestScope.categories}">
                <div id="${cat.ID}" class="mainRow">
                    <div class="firstCol">
                        <div class="text">${cat.name}</div>
                    </div>
                    <div class="secondCol">
                        <div class="text">${cat.subCategoryCount}</div>
                    </div>
                    <div class="thirdCol">
                        <div class="text">${cat.interestCount}</div>
                    </div>
                    <div class="fourthCol">
                        <div class="text">${cat.shortDescription}</div>
                    </div>
                </div>
            </c:forEach>

            <!-- spam spam spam -->
        </div>
    </div>
    <br />
</div>

<div id="dynamicRight">
    <form id="categoriesform" action="CategoriesServlet" method="post">

        <fieldset id="names" 
                  <c:if test="${empty sessionScope.role || sessionScope.role.name eq 'User'}">
                      disabled="true"
                  </c:if>
                  >
            <div class="label"><label for="name"><strong>Name</strong></label></div>
            <div class="textField1"><input type="text" id="name" name="name" placeholder="Category name" required="required"/></div> <br />
            <div id="counts">
                <div class="count">
                    <div class="label"><strong>Subcats</strong></div>
                    <div id="subCategoryCount" class="textField1">N/A</div>
                </div>
                <div class="count">
                    <div class="label"><strong>Interests</strong></div>
                    <div id="interestCount" class="textField1">N/A</div>
                </div>
            </div>
            <div id="descr">
                <div class="label"><label for="description"><strong>Description</strong></label></div>
                <textarea name="description" id="description"></textarea> <br />
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
