<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="utf-8"%>
<%@page import="java.util.List"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Shop</title>
<link rel="stylesheet" href="css/bootstrap.css">
<link rel="stylesheet" href="css/main.css">
<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
<link rel="stylesheet"
	href="https://use.fontawesome.com/releases/v5.8.2/css/all.css"
	integrity="sha384-oS3vJWv+0UjzBfQzYUhtDYW+Pj2yciDJxpsK1OYPAYjqT085Qq/1cq5FLXAZQ7Ay"
	crossorigin="anonymous">
</head>
<body>
	<div class="w3-sidebar w3-bar-block w3-border-right"
		style="display: none" id="mySidebar">
		<button onclick="w3_close()" class="w3-bar-item w3-large">Close&times;</button>
		<form action="Controller" method="post">
			<button class="w3-bar-item w3-button"><a href="index.html" >Home</a></button>
			<input type="submit" value="Laptop" name="category" class="w3-bar-item w3-button"> 
			<input type="submit" value="Phone" name="category" class="w3-bar-item w3-button">
			<input type="submit" value="Food" name="category" class="w3-bar-item w3-button">
		</form>
	</div>

	<div class="w3-teal">
		<button class="w3-button w3-xlarge" onclick="w3_open()">☰
			Navigate</button>
	</div>
	<div id="headerwrap">
		<div class="container">
			<div class="row centered">
				<div class="col-lg-8 col-lg-offset-2">
					<h1><font color="black">Internet-catalog</font></h1>
					<h1><font color="black">Baklashka</font></h1>
				</div>
			</div>
		</div>
	</div>
	<div id="lg">
		<div class="container">
			<div class="row centered">
				<div class="container">
					<form role="form" action="Controller" method="post">
						<div class="form-group">
							<input type="text" name="search_query" class="form-control"
								id="search" placeholder="Search..."> <input
								type="submit" class="btn btn-success" value="Search">
						</div>
					</form>
				</div>
			</div>
		</div>
		<div class="resultSet container w">
			<div class="row centered">
				<br> <br>
				<table border="1px" id="result_table">
					<c:if test="${empty sessionScope.result}">
						<h3>
							<font color="blue">Nothing found, try changing your
								request</font>
						</h3>
					</c:if>
					<c:forEach items="${sessionScope.result}" var="name">
						<tr class="resulttr" height="100px">
							<td width="1000px"><h4>
									<a href="${name}.html"><font color="white" size="4px">${name}</font></a>
								</h4></td>
						</tr>
					</c:forEach>
				</table>
			</div>
			<br> <br>
		</div>
		<div class="container wb">
			<div class="row centered">
				<br> <br>
				<div class="col-lg-2"></div>
				<div class="col-lg-10 col-lg-offset-1">
					<img src="img/munter.png" alt="" class="img-responsive">
				</div>
			</div>
		</div>

  <div id="r">
    <div class="container">
      <div class="row centered">
        <div class="col-lg-8 col-lg-offset-2">
          <h4>National University of Ukraine</h4>
          <h3><font color="white">Igor Sikorsky Kyiv Polytechnic Institute</font></h3>
          <h2><font color="white">Department of automatics and control in technical systems</font></h2>
          <h2><font color="white">FICT, IA-83</font></h2>
        </div>
      </div>
    </div>
  </div>
  <div id="f">
    <div class="container">
      <div class="row centered">
        <a href="https://instagram.com/alex_zhyshko?igshid=1xbjjktt0xngt">Саша<i class="fab fa-instagram"></i></a>
        <a href="https://www.instagram.com/vlad.s_217/">Влад<i class="fab fa-instagram"></i></a>
        <a href="https://www.instagram.com/dima_nechitaylo/">Дима<i class="fab fa-instagram"></i></a>
        <a href="https://www.instagram.com/vkrasiy/">Виталь<i class="fab fa-instagram"></i></a>
        <a href="https://www.instagram.com/pustoye__mesto/">Марта<i class="fab fa-instagram"></i></a>
      </div>
    </div>
  </div>

  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.0/jquery.min.js"></script>
  <script src="js/bootstrap.min.js"></script>
  <script>
			function w3_open() {
				document.getElementById("mySidebar").style.display = "block";
			}

			function w3_close() {
				document.getElementById("mySidebar").style.display = "none";
			}
		</script>
</body>
</html>