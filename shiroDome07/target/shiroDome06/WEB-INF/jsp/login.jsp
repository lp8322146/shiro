<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2017/9/23
  Time: 10:15
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <form action="/check" method="get">
        <label>账号：</label><input name="name" type="text"/><br/>
        <label>密码：</label><input name="pwd" type="password"/><br/>
        <input type="submit" value="提交"/>
    </form>
</body>
</html>
