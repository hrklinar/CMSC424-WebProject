<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" type="text/css" href="mainStyle.css" />
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<title>HB's Pizzeria</title>
</head>
<body>	
	<div class="container">		
		<div class="center">
			<h1> Welcome to HB's Pizzeria!! <br>
				<div id="myImage" />
			</h1>						
			<div id="inner">								
					<div id='menu'>
						<form method="post" action="menu.jsp">
	                        <input type="submit" name="button" value="Menu" />
	              		</form>
					</div>					
					<div id='order'>
						<%
		              	String email = (String)session.getAttribute("email");
		              	String customerForm = "";
		              	if (email == null || email.equals("")) {
		              		customerForm = "<form method='post' action=\"Customer\">";
		              		customerForm += "<input type=\"submit\" name=\"button\" value=\"Log in/Register\" />";
		              	} else {
		              		customerForm = "<form method='post' action=\"Order\">";
		              		customerForm += "<input type=\"submit\" name=\"button\" value=\"Log in/Register\" />";
		              	}
		              	customerForm += "</form>";
		                %>
	                
		                <%= customerForm %>
					</div>
					<div id='contact'>
						<form method="post" action="contact.html">
	                        <input type="submit" name="button" value="Contact Us" />                        
	                	</form>
					</div>
					<div id='driver'>
						<form method="post" action="Driver">
	                        <input type="submit" name="button" value="Delivery Drivers" />                        
	                	</form>
					</div>
					<div id='admin'>
						<form method="post" action="Admin">
	                        <input type="submit" name="button" value="Administrative" />                        
	                	</form>
					</div>
					
					<br>	 
			</div>
			<div id='inner_bot'>					
					This website was created by Heather Klinar and Brendan Fruin at the University of Maryland. <br>
					Last update: 05-01-2012.
				</div>
		</div>
	</div>
</body>
</html>