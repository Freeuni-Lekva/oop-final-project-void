<%
    String username = (String) session.getAttribute("username");
    if (username == null) {
        response.sendRedirect("login.jsp");
        return;
    }
    Boolean isAdmin = (Boolean) session.getAttribute("is_admin");
    if (isAdmin == null) isAdmin = false;
%>
<html>
<head>
    <title>Welcome</title>
</head>
<body>
<h2>Welcome, <%= username %>!</h2>

<% if (isAdmin) { %>
<p><strong>You are an admin.</strong></p>
<% } else { %>
<p>You are a regular user.</p>
<% } %>

<form action="logout" method="get">
    <input type="submit" value="Logout">
</form>
</body>
</html>