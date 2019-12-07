<%@page import="control_pack.ServiceController"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device=width, initial-scale=1.0">
        <title>Store</title>
        <meta name="author" content="Dobrovoimaster">
        <meta name="description" content="Store">
        <link href="http://fonts.googleapis.com/css?family=Open+Sans:400,600,700&amp;subset=latin,cyrillic" rel="stylesheet" type="text/css">
        <link rel="stylesheet" type="text/css" href="all.css" />
        <style>
            *,
            *:before,
            *:after {
                margin: 0;
                padding: 0;
                box-sizing: border-box;
            }

            html,
            body {
                height: 100%;
            }

            body {
                font: 14px/1 "Open Sans", sans-serif;
                color: #555;
                background: #fafafa;
            }

            table {
                overflow: auto;
                border-spacing: 0;
                width: 100%;
            }

            table tr th, table tr td
            { background: #3B3B3B;
              color: #FFF;
              padding: 7px 4px;
              text-align: center;}

            table tr td
            { background: #E5E5DB;
              color: #47433F;
              border-top: 1px solid #FFF;}

            .table_buttons_bar{
                display: flex;
                position: relative;
                border: #009933 solid 3px;
                border-radius: 5px 5px 5px 5px;
                width: 100%;
            }
            
            .table_buttons_bar button{
                background-color: rgb(65,117,203);
                border: #006600 solid;
                border-radius: 3px 3px 0 0;
                color: white;
                width: 150px;
                padding: 4px;
                text-align: center;
                text-decoration: none;
                display: inline-block;
                font-size: 16px;
                float: left;
                margin: 10px 8px 10px 8px;
            }
            
            .table_buttons_bar button:hover{
                color: #383838;
                background-color: rgb(65,117,255);
            }

            .modal{
                display: none;
                position: relative;
                width: 100%;
                height: 100%;
                overflow: visible;
                border: #E5E5DB solid 3px;
                border-radius: 4px 4px 4px 4px;
            }
            
            .modal .crud_bar{
                margin: 0px 4px 8px 4px;
                border: #3B3B3B solid 3px;
                border-radius: 2px 2px 2px 2px;
                position: relative;
                height: 140px;
                width: 140px;
            }

            .crud_bar button, #dialog-button-place button{
                background-color: #E5E5DB;
                border: #383838 solid 2px;
                color: black;
                width: 120px;
                padding: 7px;
                margin: 5px 5px 5px 5px;
                border-radius: 3px 3px 3px 3px;
            }
            
            .crud_bar button:hover, #dialog-button-place button:hover{
                border-color: #555;
                background-color: #47433F;
                color: #E5E5DB;
            }
            
            #db_table {
                position: relative;
                border: #383838 solid 4px;
                overflow: auto;
                position: relative;
                width: 100%;
                border-radius: 5px 5px 5px 5px;
            }
            
            #dialog-form-container {
                display: none;
                position: fixed;
                top: 0;
                left: 0;
                z-index: 9999;
                width: 100%;
                height: 100%;
                text-align: center;
            }
            
            #dialog-form {
                display: inline-block;
                height: 500px;
                width: 500px;
                border: 3px #3B3B3B solid;
                border-radius: 3px 3px 3px 3px;
                background: #fafafa;
            }
            
            #dialog-description p{
                margin-top: 10px;
                font-size: 18px;
                color: rgba(15,15,15,1);
            }
            
            #dialog-button-place{
                position: relative;
            }
            
            #dialog-fields-place {
                font-size: 18px;
                margin: 20px;
            }
            
            #dialog-status-place{
                margin: 20px;
                font-size: 15px;
                color: red;
            }
            
            #dialog-fields-place ul, #dialog-status-place ul{
                list-style-type: none;
            }
            
            #dialog-fields-place ul li{
                height: 30px;
                padding: 7px;
                margin-bottom: 10px;
            }
            
            #dialog-status-place ul li{
                height: 17px;
                padding: 3px;
            }
            
            #dialog-fields-place ul li input {
                font-size: 16px;
                
                margin-right: 15px;
                float: right;
            }
            
            #dialog-fields-place ul li label{
                padding-right: 10px;
                padding-left: 10px;
            }
            #l_rad{
                float: right;
            }
            
            #cover-div {
                position: fixed;
                top: 0;
                left: 0;
                z-index: 9000;
                width: 100%;
                height: 100%;
                background-color: gray;
                opacity: 0.5;
            }
            
            
            .container {
                max-width: 1400px;
                margin: 0 auto;
                padding: 25px 10px 0;
            }

            h1 {
                padding: 50px 0;
                font-weight: 400;
                text-align: center;
            }

            .tabs {
                min-width: 320px;
                max-width: 90%;
                padding: 0px;
                margin: 0 auto;
            }

            .tabs > section {
                display: none;
                padding: 15px;
                background: #fff;
                border: 1px solid #ddd;
            }

            .tabs > section > p {
                margin: 0 0 5px;
                line-height: 1.5;
                color: #383838;
                -webkit-animation-duration: 1s;
                animation-duration: 1s;
                -webkit-animation-fill-mode: both;
                animation-fill-mode: both;
                -webkit-animation-name: fadeIn;
                animation-name: fadeIn;
            }

            @-webkit-keyframes fadeIn {
                from {
                    opacity: 0;
                }
                to {
                    opacity: 1;
                }
            }

            @keyframes fadeIn {
                from {
                    opacity: 0;
                }
                to {
                    opacity: 1;
                }
            }

            .tabs > input {
                display: none;
                position: absolute;
            }

            .tabs > label {
                display: inline-block;
                margin: 0 0 -1px;
                padding: 15px 25px;
                font-weight: 600;
                text-align: center;
                color: #aaa;
                border: 0px solid #ddd;
                border-width: 1px 1px 1px 1px;
                background: #f1f1f1;
                border-radius: 3px 3px 0 0;
            }

            .tabs > label:hover {
                color: #888;
                cursor: pointer;
            }

            .tabs > input:checked + label {
                color: #555;
                border-top: 1px solid #009933;
                border-bottom: 1px solid #fff;
                background: #fff;
            }

            #tab_list:checked ~ #content-tab_list,
            #tab_crud:checked ~ #content-tab_crud{
                display: block;
            }

            @media screen and (max-width: 680px) {
                .tabs > label {
                    font-size: 0;
                }
                .tabs > label:before {
                    margin: 0;
                    font-size: 18px;
                }
            }

            @media screen and (max-width: 400px) {
                .tabs > label {
                    padding: 15px;
                }
            }   
        </style>
        
        <script type='text/javascript' src="JS/moment.js"></script>
        <script src="http://code.jquery.com/jquery-latest.min.js"></script>
        <script type="text/javascript" src="JS/script.js"></script>
        <%@ page import="control_pack.ServiceController" %>
        <%@ page import="DTO.SoftwareDTO" %>
        <%@ page import="EntityPack.*" %>
        <%@ page import="java.util.ArrayList" %>
    </head>
    <body onload="initBaseVars()">
        <div class="container">
            <h1>
                <p>Software Store</p>
                <p>Rustam Sagitov 6404</p>     
            </h1>
            <div class="tabs">
                <input id="tab_list" type="radio" name="tabs" onchange="ProductListSelect()" checked>
                <label for="tab_list" title="List of products">
                    List of Products
                </label>
                <input id="tab_crud" type="radio" name="tabs">
                <label for="tab_crud" title="CRUD">
                    CRUD
                </label>
                <section id="content-tab_list">
                    <p>Products:</p>
                    <div id="content-tab_list_div">
                        
                    </div>  
                </section>
                
                <section id="content-tab_crud">
                    <div class="table_buttons_bar">
                        
                            <button type="button" onclick="clickAccount()">Account</button>
                            <button type="button" onclick="clickDigitalCopy()">Digital Copy</button>
                            <button type="button" onclick="clickOrder()">Order</button>
                            <button type="button" onclick="clickProductFeedback()">Product Feedback</button>
                            <button type="button" onclick="clickPublisher()" >Publisher</button>
                            <button type="button" onclick="clickPurchase()">Purchase</button>
                            <button type="button" onclick="clickSoftware()">Software</button>
                            <button type="button" onclick="clickSoftwareClass()">Software Class</button>
                        
                    </div>
                    <div id="modal_window" class="modal">
                        <div class="crud_bar">
                            <button type="button" onclick="clickCreate()">Create</button>
                            <button type="button" onclick="clickUpdate()">Update</button>
                            <button type="button" onclick="clickDelete()">Delete</button>
                        </div>
                        <div id ="db_table">
                            
                        </div>
                    </div>
                </section>
            </div> 
        </div>
                        <div id="dialog-form-container">
                            <form id="dialog-form">
                                <div id="dialog-description">
                                    
                                </div>
                                <div id="dialog-fields-place">
                                    
                                </div>
                                <div id="dialog-button-place">
                                    <button type="button" id="b_execute" onclick="clickExecute()">OK</button>
                                    <button type="button" onclick="onAbortOp()">Close</button>
                                </div>
                                <div id="dialog-status-place">
                                </div>
                            </form>
                        </div>
    </body>
</html>
