<%-- 
    Document   : register
    Created on : 19.03.2019, 18:27:36
    Author     : russa
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Registration</title>
    </head>
    
    <style>
        .window {
            border: 3px solid black;
            background: orange;
            align-items: center;
            font-family: monospace;
            font-size: large;
            size: auto;
            margin:  20%;
            padding: 10px;
        }
    </style>
    <style>
        .elem {
            padding: 4px;
            position: relative;
        }
    </style>
    <body>
        <div style="font-family: sans-serif; font-size: x-large; position: relative; left: 0px; width: 300px; border: 4px solid orange; align-items: center; padding: 10px;">
            <a href="index"> Back to main </a>
        </div>
        <form action="${pageContext.request.contextPath}/RegisterServlet" method="post">
            <div class="window">
            <div class="elem">
                <label for="e_mail"><b>E_mail: </b></label>
                <input type="text" placeholder="Enter E-Mail" name ="e_mail" reguired style="float: right">
            </div>
            <div class="elem">
                <label for="pword"><b>Password: </b></label>
                <input type="text" placeholder="Enter password"  name ="pword" reguired style="float: right">
            </div>
            <div class="elem">
                <label for="fname"><b>Name: </b></label>
                <input type="text" placeholder="Your name"  name ="fname" style="float: right">
            </div>
            <div class="elem">
                    <label for="bday"><b>Date of birth: </b></label>
                    <input type="date"  name ="bday" value="2019-01-01" min ="1920-01-01" max="2019-01-01" style="float: right">
            </div>
            <div class="elem">
                     <label for="gender"><b>Your gender: </b><label>
                     <div style="float: right">
                        <input type="radio" name="gender" value="M" checked> Male <br>
                        <input type="radio" name="gender" value="F" checked> Female <br>
                     </div>            
            </div>
            <div class ="elem" style="align-items: center; padding-top: 20px">   
                    <input type="submit" name="regbutton" value="Register" />   
            </div>
            </div>
        </form>
    </body>
</html>
