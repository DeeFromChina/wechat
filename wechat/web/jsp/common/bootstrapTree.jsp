<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<%@include file="../common/common.jsp"%>
<title></title>
<meta name="viewport" content="width=device-width, initial-scale=1" />
<link href="<%=baseCss %>common/bootstrapTree.css?randomId=<%=Math.random()%>" rel="stylesheet">
</head>
<script src="<%=baseJs %>common/bootstrapTree.js?randomId=<%=Math.random()%>" type="text/javascript"></script>
<body onload="init()">
	<div id="bootstrapTree">
	</div>
</body>
</html>
    