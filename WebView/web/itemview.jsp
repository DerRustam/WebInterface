<%-- 
    Document   : itemView
    Created on : 19.03.2019, 23:42:23
    Author     : russa
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page language="java" 
        contentType="text/html"
        pageEncoding="UTF-8"
        %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><var>software_name</var></title>
    </head>
    <body>
        <div>
        <div id="t_Account">
            <table>
               <thead>
                    <tr>
                        <th>id</th>
                        <th>owner_name</th>
                        <th>date_of_birth</th>
                        <th>gender</th>
                        <th>e_mail</th>
                        <th>password</th>
                    </tr>
               </thead>
               <tbody>
                   <c:forEach items="${list}" var="record">
                            <tr>
                                <td><c:out value="${record.getAccount_id()}" /></td>
                                <td><c:out value="${record.getOwner_name()}" /></td>
                                <td><c:out value="${record.getDate_of_birth()}" /></td>
                                <td><c:out value="${record.getGender()}" /></td>
                                <td><c:out value="${record.getE_mail()}" /></td>
                                <td><c:out value="${record.getPassword()}" /></td>
                            </tr>
                    </c:forEach>
               </tbody>
            </table>
        </div>
    
        <div id="t_Digital_Copy">
            <table>
               <tr>
                   <th>product_key</th>
                   <th>software_id</th>
                   <th>purchase_id</th>
               </tr>
               <c:forEach items="${list}" var="record">
                <tr>
                   <td>${record.product_key}</td>
                   <td>${record.software_id}</td>
                   <td>${record.purchase_id}</td>
                </tr>
               </c:forEach>
               <tr>
               </tr>
            </table>
        </div>
        <div id="t_Order">
        <table>
               <tr>
                   <th>account_id</th>
                   <th>order_datetime</th>
                   <th>summary_price</th>
                   <th>pay_code</th>
               </tr>
               <c:forEach items="${list}" var="record">
                <tr>
                   <td>${record.account_id}</td>
                   <td>${record.order_datetime}</td>
                   <td>${record.summary_price}</td>
                   <td>${record.pay_code}</td>
                </tr>
               </c:forEach>
               <tr>
               </tr>
        </table>
        </div>
        <div id="t_Product_Feedback">
        <table>
               <tr>
                   <th>account_id</th>
                   <th>software_id</th>
                   <th>message</th>
               </tr>
               <c:forEach items="${list}" var="record">
                <tr>
                   <td>${record.account_id}</td>
                   <td>${record.software_id}</td>
                   <td>${record.message}</td>
                </tr>
               </c:forEach>
               <tr>
               </tr>
        </table>
        </div>
        <div id="t_Publisher">
        <table>
               <tr>
                   <th>publisher_id</th>
                   <th>publisher_name</th>
               </tr>
               <c:forEach items="${list}" var="record">
                <tr>
                   <td>${record.publisher_id}</td>
                   <td>${record.publisher_name}</td>
                </tr>
               </c:forEach>
               <tr>
               </tr>
        </table>
        </div>
        <div id="t_Purchase">
        <table>
               <tr>
                   <th>purchase_id</th>
                   <th>order_id</th>
                   <th>count</th>
                   <th>price_single</th>
               </tr>
               <c:forEach items="${list}" var="record">
                <tr>
                   <td>${record.purchase_id}</td>
                   <td>${record.order_id}</td>
                   <td>${record.count}</td>
                   <td>${record.price_single}</td>
                </tr>
               </c:forEach>
               <tr>
               </tr>
        </table>
        </div>
        <div id="t_Software">
        <table>
               <tr>
                   <th>software_id</th>
                   <th>class_id</th>
                   <th>publisher_id</th>
                   <th>title</th>
                   <th>release_date</th>
                   <th>esrb</th>
                   <th>actual_price</th>
               </tr>
               <c:forEach items="${list}" var="record">
                <tr>
                   <td>${record.software_id}</td>
                   <td>${record.class_id}</td>
                   <td>${record.publisher_id}</td>
                   <td>${record.title}</td>
                   <td>${record.release_date}</td>
                   <td>${record.esrb}</td>
                   <td>${record.actual_price}</td>
                </tr>
               </c:forEach>
               <tr>
               </tr>
        </table>
        </div>
        <div id="t_Software_Class">
        <table>
               <tr>
                   <th>class_id</th>
                   <th>class_name</th>
                   <th>class_parent_id</th>
               </tr>
               <c:forEach items="${list}" var="record">
                <tr>
                   <td>${record.class_id}</td>
                   <td>${record.class_name}</td>
                   <td>${record.class_parent_id}</td>
                </tr>
               </c:forEach>
               <tr>
               </tr>
        </table>
        </div>
        </div>    
    </body>
</html>
