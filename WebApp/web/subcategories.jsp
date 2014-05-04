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
                <div class="text">Category</div>
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
            <c:forEach var="subcat" items="${requestScope.subcategories}">
                <div id="${subcat.ID}" class="mainRow">
                    <div class="firstCol">
                        <div class="text">${subcat.name}</div>
                    </div>
                    <div class="secondCol">
                        <div class="text">${subcat.category.shortName}</div>
                    </div>
                    <div class="thirdCol">
                        <div class="text">${subcat.interestCount}</div>
                    </div>
                    <div class="fourthCol">
                        <div class="text">${subcat.shortDescription}</div>
                    </div>
                </div>
            </c:forEach>

            <!-- spam spam spam -->
        </div>
    </div>
    <br />
</div>

<div id="dynamicRight">
    <form id="subcategoriesform" action="SubcategoryServlet" method="post">

        <fieldset id="names" 
                  <c:if test="${empty sessionScope.role || sessionScope.role.name eq 'User'}">
                      disabled="true"
                  </c:if>
                  >
            <div id="region-fall">
                <div class="label"><label for="reg"><strong>Region</strong></label></div>
                <div class="textField1">
                    <select type="text" id="category" name="category" required="required">
                        <c:forEach var="curr" items="${requestScope.categories}">
                            <option value="${curr.ID}">${curr.name}</option>
                        </c:forEach>
                    </select>
                </div> <br/>
            </div>
            <div class="label"><label for="name"><strong>Name</strong></label></div>
            <div class="textField1"><input type="text" id="name" name="name" placeholder="Subcategory name" required="required"/></div> <br />
            <div id="counts">
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