<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%-- 
    Document   : personaltab
    Created on : May 1, 2014, 10:20:59 PM
    Author     : root
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<style type="text/css">@import url(http://fonts.googleapis.com/css?family=Open+Sans:400,600,300);</style>
<div id='cssmenu'>
    <ul>
        <li class='has-sub'><a href='#'><span>Conversations</span></a>
            <ul>
            </ul>
        </li>
        <li class='has-sub'><a href='#'><span>Interests</span></a>
            <ul>
                <div class="innerListOfInterests">
                    <c:forEach var="currInt" items="${requestScope.interests}">
                        <li><div id="${currInt.ID}" class="interest">
                                <div id="goAwayButton">
                                    <a href="https://www.youtube.com/watch?v=u9Fwurrv6es"> 
                                        <img src="images/close.png" width="20px"></img>
                                    </a>
                                </div>
                                <div id="SeeButton">
                                    <a href="https://www.youtube.com/watch?v=u9Fwurrv6es"> 
                                        <img 
                                            <c:choose> 
                                                <c:when test="${currInt.users.oldChildren[0].notify}">
                                                    src="images/see.jpg"
                                                </c:when>
                                                <c:otherwise>
                                                    src="images/nosee.png"
                                                </c:otherwise>
                                            </c:choose> width="20px">
                                        </img>
                                    </a>
                                </div>
                                <div id="name"> ${currInt.name}</div>
                            </div></li>
                        </c:forEach>
                </div>
                <div class="finalButtons">
                    <div id="button1"><a href="#" class="classname">NEW</a></div>
                    <div id="button2"><a href="#" class="classname">VIEW ALL</a></div>
                </div>
            </ul>
        </li>
        <li class='has-sub'><a href='#'><span>Groups</span></a>
            <ul>
                <li><a href='#'><span>About</span></a></li>
                <li class='last'><a href='#'><span>Location</span></a></li>
            </ul>
        </li>
        <li class='last has-sub'><a href='#'><span>Events</span></a>
            <ul>
            </ul>
        </li>
    </ul>
</div>

<div style="clear:both; margin: 0 0 30px 0;">&nbsp;</div>

