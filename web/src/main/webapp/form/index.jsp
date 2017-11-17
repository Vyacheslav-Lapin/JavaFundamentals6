<%@ page contentType="text/html;charset=UTF-8"%>
<html>
<head>
    <title>Title</title>
</head>
<body>
<form action="${pageContext.request.contextPath}/form-parse" method="get">
    <input type="hidden" name="command" value="forward"/>

    Enter login:<br/>
    <input name="login" title="login"/><br/>

    Enter password:<br/>
    <input type="password" name="password" title="password"/><br/>

    <input type="submit" value="Отправить"/><br/>
</form>

</body>
</html>
