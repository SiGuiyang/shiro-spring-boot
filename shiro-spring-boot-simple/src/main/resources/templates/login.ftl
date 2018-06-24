<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>

<h1>Login Page</h1>

<form method="post" action="${request.contextPath}/admin/login">
    <input type="text" name="username" />
    <input type="password" name="password">
    <input type="submit" value="submit" />
</form>
</body>
</html>