<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2016/8/5
  Time: 22:51
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title></title>
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
</head>
<body>
    <form action="${pageContext.request.contextPath }/servlet/UploadServletDemo1" method="post" enctype="multipart/form-data">
    姓名：<input type="text" name="username"/><br>
    照片 ：<input type="file" name="photo1"><br>
    <input type="submit" value="提交"/>
</form>
</body>
</html>
