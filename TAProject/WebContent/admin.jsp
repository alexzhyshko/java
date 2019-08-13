<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<form action="admin_controller" method="post">
		<input type="text" name="goodname" placeholder="name"> <br>
		<input type="text" name="goodprice" placeholder="price"> <br>
		<input type="text" name="goodcategory" placeholder="category">
		<br>
		<textarea name="goodcharacteristics" placeholder="characteristics">
</textarea>
		<br> <input type="submit" value="submit">
	</form>
</body>
</html>