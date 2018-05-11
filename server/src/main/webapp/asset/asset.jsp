<%@ page pageEncoding="UTF-8"%> 
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<% session.setAttribute("rootPath", request.getContextPath());%>
<head>
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="description"
	content="A front-end template that helps you build fast, modern mobile web apps.">
<meta name="viewport" content="width=device-width, initial-scale=1">

<!-- Add to homescreen for Chrome on Android -->
<meta name="mobile-web-app-capable" content="yes">

<!-- Add to homescreen for Safari on iOS -->
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<meta name="apple-mobile-web-app-title" content="Material Design Lite">
<link href="http://localhost:5230/lokou/asset/css/icon.css?family=Material+Icons" rel="stylesheet">
<link rel="stylesheet" href="http://localhost:5230/lokou/asset/css/material.teal-green.min.css">
<link rel="stylesheet" href="http://localhost:5230/lokou/asset/css/style.css">			
<script src="http://localhost:5230/lokou/asset/js/material.min.js"></script>
<script src="${rootPath}/asset/js/constant.js" ></script>
<script src="${rootPath}/asset/js/popper.js" ></script>
<script src="${rootPath}/asset/js/jquery-3.3.1.min.js" ></script>
<script src="${rootPath}/asset/js/sha1.js" ></script>
<style>
div[class*="loko-text-area"]{
 /* width:50%;
  height:59%;*/
}
.lokou_login_content{
 width:100%;
 background:url("${rootPath}/asset/image/background.jpg") no-repeat;
}
.lokou_login_panel{
}
</style>
</head>