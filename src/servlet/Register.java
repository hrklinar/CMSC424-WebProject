package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class Register
 */
@WebServlet("/Register")
public class Register extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Register() {
        super();
        // TODO Auto-generated constructor stub
    }

    /*This function just writes a html page with the formatting from 
     * the CSS file.
     * content = actual data we want displayed on the web page
     */
    public void writePage(String content, PrintWriter pw)
    {
    	String writeString = "<!doctype html>"+
    						 "<html>"+
    						 "<head>"+
    						 	"<meta http-equiv='Content-Type' content='text/html; charset=UTF-8' />"+
    						 	"<link rel='stylesheet' href='styles.css' title='MainStyle' />"+
    						 	"<title>HB's Pizzeria--Registration</title>"+
    						 "</head>"+
    						 "<body>"+
    						 	"<div id='header'>"+
    						 		"<div id='contentwrapper_hdr'>"+
    						 			"<div id='header_center'>"+
    						 				"<h1>HB's Pizzeria: Customer Registration</h1>"+
    						 			"</div>"+
    						 		"</div>"+
    						 		"<div id='header_left'></div>"+
    						 		"<div id='header_right'></div>"+
    						 	"</div>"+
    						 	"<div id='contentwrapper'>"+
    						 		"<div id='center_column'>";
    	writeString += content;
    	writeString +=				"</div>"+
                					"<div id='footer'>"+
                					"</div>"+
                				"</div>"+
                			"</body>"+
                		"</html>";
    	pw.println(writeString);
    }
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter pw = response.getWriter();
		HttpSession session = request.getSession(true);
//		pw.print("<html><h1>Register</h1>");
		
		GlobalUser user = ((GlobalUser)session.getAttribute("user"));
		String pgWrite = "";
		if (user == null || !user.logged_in) {
			String email = request.getParameter("email");
			
			if (email == null) {
				pgWrite += "<div class='register_box'><div id='panel'>";
				pgWrite += "<form id='register' action='Register' method='post' accept-charset='UTF-8'>" +
						"<fieldset >" +
						"<legend>Register</legend>" + 
						"<input type='hidden' name='submitted' id='submitted' value='1'/>" +
						"<div id='row'><label for='email' >Email: </label>" +
						"<input type='text' name='email' id='email' maxlength=\"25\" /></div>" +
						"<div id='row'><label for='password' >Password: </label>" +
						"<input type='password' name='password' id='password' maxlength=\"25\" /></div>" +
						
						"<div id='row'><label for='first_name' >First Name: </label>" +
						"<input type='text' name='first_name' id='first_name' maxlength=\"25\" />" +
						"&nbsp; &nbsp;<label for='last_name' > Last Name: </label>" +
						"<input type='text' name='last_name' id='last_name' maxlength=\"25\" /></div>" +
						
						"<div id='row'><label for='address' >Address: </label>" + 
						"<input type='text' name='address' id='address' maxlength=\"68\" size =\"60\" /></div>" +
						
						"<div id='row'><label for='city' >City:</label>" +
						"<input type='text' name='city' id='city' maxlength=\"30\" />" +
						"&nbsp; &nbsp;<label for='statecode' >State: </label>" +
						"<input type='text' name='statecode' id='statecode' maxlength=\"2\" size=\"2\" /> </div>" + 
						
						"<center><div id='row'><input type='submit' name='submit' value='Register' /></div></center>" +
						"</fieldset>" +
						"</form></div></div>";
//				pw.print("<form id='register' action='Register' method='post' accept-charset='UTF-8'>" +
//				"<fieldset >" +
//				"<legend>Register</legend>" + 
//				"<input type='hidden' name='submitted' id='submitted' value='1'/>" +
//				"<label for='email' >Email: </label>" +
//				"<input type='text' name='email' id='email' maxlength=\"25\" />" +
//				"<br />" +
//				"<label for='password' >Password: </label>" +
//				"<input type='password' name='password' id='password' maxlength=\"25\" />" +
//				"<br />" + 
//				"<label for='first_name' >First Name: </label>" +
//				"<input type='text' name='first_name' id='first_name' maxlength=\"25\" />" +
//				"<label for='last_name' > Last Name: </label>" +
//				"<input type='text' name='last_name' id='last_name' maxlength=\"25\" />" +
//				"<br />" + 
//				"<label for='address' >Address: </label>" + 
//				"<input type='text' name='address' id='address' maxlength=\"68\" size =\"60\" />" +
//				"<br />" + 
//				"<label for='city' >City:</label>" +
//				"<input type='text' name='city' id='city' maxlength=\"30\" />" +
//				"<label for='statecode' >State Code: </label>" +
//				"<input type='text' name='statecode' id='statecode' maxlength=\"2\" size=\"2\" />" + 
//				"<br />" + 
//				"<input type='submit' name='submit' value='Register' />" +
//				"</fieldset>" +
//				"</form>");
			} else { //Page has been called before
				boolean valid_info = true;
				
				String password = request.getParameter("password");
				String first_name = request.getParameter("first_name");
				String last_name = request.getParameter("last_name");
				String address = request.getParameter("address");
				String city = request.getParameter("city");
				String state_code = request.getParameter("statecode");
				
				if (email.equals("")) {
					pgWrite += "<font color='red'>Email field left blank.</font><br />";
//					pw.println("<font color='red'>Email field left blank.</font><br />");
				} else if (email.indexOf('@') < 0) {
					pgWrite += "<font color='red'>Invalid email. Emails must be of the form name@domain</font><br />";
//					pw.println("<font color='red'>Invalid email. Emails must be of the form name@domain</font><br />");
					valid_info = false;
				}
				
				if (password.length() < 5) {
					pgWrite += "<font color='red'>Password must be greater than five characters.</font><br />";
//					pw.println("<font color='red'>Password must be greater than five characters.</font><br />");
					valid_info = false;
				}
				
				if (valid_info) {
					Connection conn;
					ResultSet resultSet;
					try {
					    // Load the Oracle JDBC driver
					    Class.forName("oracle.jdbc.driver.OracleDriver");
					}
					catch(ClassNotFoundException e) {
					    System.out.println("Driver not found.");
					    System.out.println(e.toString());
					}
					try {
					    // Connect to the Database
					    conn = DriverManager.getConnection("jdbc:oracle:thin:@ginger.umd.edu:1521:dbclass2", "dbclass235", "O2FNJud3");
					    Statement statement = conn.createStatement();
					    resultSet = statement.executeQuery("SELECT * from customers where email = '"+email+"'");
					    
					    if (resultSet.next()) { //Email already in database
					    	
					    	pgWrite += "<div class='register_box'>";
					    	pgWrite += "<center><div id='r1'>Email already in use.</row>";
					    	pgWrite += "<form id='post' action='Login' method='post'>" + 
									"<input type='submit' name='login' value='Login' /></center>";
					    	pgWrite += "<div id='panel'>";
							pgWrite += "<form id='register' action='Register' method='post' accept-charset='UTF-8'>" +
									"<fieldset >" +
									"<legend>Register</legend>" + 
									"<input type='hidden' name='submitted' id='submitted' value='1'/>" +
									"<div id='row'><label for='email' >Email: </label>" +
									"<input type='text' name='email' id='email' maxlength=\"25\" /></div>" +
									"<div id='row'><label for='password' >Password: </label>" +
									"<input type='password' name='password' id='password' maxlength=\"25\" /></div>" +
									
									"<div id='row'><label for='first_name' >First Name: </label>" +
									"<input type='text' name='first_name' id='first_name' maxlength=\"25\" />" +
									"&nbsp; &nbsp;<label for='last_name' > Last Name: </label>" +
									"<input type='text' name='last_name' id='last_name' maxlength=\"25\" /></div>" +
									
									"<div id='row'><label for='address' >Address: </label>" + 
									"<input type='text' name='address' id='address' maxlength=\"68\" size =\"60\" /></div>" +
									
									"<div id='row'><label for='city' >City:</label>" +
									"<input type='text' name='city' id='city' maxlength=\"30\" />" +
									"&nbsp; &nbsp;<label for='statecode' >State: </label>" +
									"<input type='text' name='statecode' id='statecode' maxlength=\"2\" size=\"2\" /> </div>" + 
									
									"<center><div id='row'><input type='submit' name='submit' value='Register' /></div></center>" +
									"</fieldset>" +
									"</form></div></div>";
					    	
//					    	pw.print("Email already in use.<br />");
//					    	pw.print("<form id='post' action='Login' method='post'>" + 
//									"<input type='submit' name='login' value='Login' /><br /><br />");
//					    	
//					    	pw.print("<form id='register' action='Register' method='post' accept-charset='UTF-8'>" +
//									"<fieldset >" +
//									"<legend>Register</legend>" + 
//									"<input type='hidden' name='submitted' id='submitted' value='1'/>" +
//									"<label for='email' >Email: </label>" +
//									"<input type='text' name='email' id='email' maxlength=\"25\" />" +
//									"<br />" +
//									"<label for='password' >Password: </label>" +
//									"<input type='password' name='password' id='password' maxlength=\"25\" />" +
//									"<br />" + 
//									"<label for='first_name' >First Name: </label>" +
//									"<input type='text' name='first_name' id='first_name' maxlength=\"25\" value='" + first_name + "' />" +
//									"<label for='last_name' > Last Name: </label>" +
//									"<input type='text' name='last_name' id='last_name' maxlength=\"25\" value='" + last_name + "' />" +
//									"<br />" + 
//									"<label for='address' >Address: </label>" + 
//									"<input type='text' name='address' id='address' maxlength=\"68\" size =\"60\" value='" + address + "' />" +
//									"<br />" + 
//									"<label for='city' >City:</label>" +
//									"<input type='text' name='city' id='city' maxlength=\"30\" value='" + city + "' />" +
//									"<label for='statecode' >State Code: </label>" +
//									"<input type='text' name='statecode' id='statecode' maxlength=\"2\" size=\"2\" value='" + state_code
//									+ "' />" +
//									"<br />" + 
//									"<input type='submit' name='submit' value='Register' />" +
//									"</fieldset>" +
//									"</form>");
					    } else {
					    	address = address + " " + city + ", " + state_code;
					    	String query = "INSERT INTO customers (email, password, first_name, last_name, address) VALUES ('" +
					    			email + "', '" + password + "', '" + first_name + "', '" + last_name + "', '" + address + "')";
					    	System.out.println(query);
					    	statement.executeQuery(query);
					    	pgWrite += first_name + " " + last_name + ", you have successfully registered. You may now proceed in " +
					    			"placing an order.";
					    	//Log the user in
					    	GlobalUser newUser = new GlobalUser(email, first_name, last_name, address);
					    	session.setAttribute("user", newUser);
					    	session.setAttribute("email", email);
					    	pgWrite += "<form action='OrderOnline.jsp'> <input type='submit' value='Order Online' /> </form>";
					    	pgWrite += "<form action='index.jsp'> <input type='submit' value='Home' /> </form>";
//					    	pw.print(first_name + " " + last_name + ", you have successfully registered. You may now proceed in " +
//					    			"placing an order.");
//					    	pw.print("<form action='index.jsp'> <input type='submit' value='Home' /> </form>");
					    }
					} catch (SQLException e) {
					    System.out.println("An error occurs.");
					    System.out.println(e.toString());
					}
				} else {
					pgWrite += "<div class='register_box'><div id='panel'>";
					pgWrite += "<form id='register' action='Register' method='post' accept-charset='UTF-8'>" +
							"<fieldset >" +
							"<legend>Register</legend>" + 
							"<input type='hidden' name='submitted' id='submitted' value='1'/>" +
							"<div id='row'><label for='email' >Email: </label>" +
							"<input type='text' name='email' id='email' maxlength=\"25\" /></div>" +
							"<div id='row'><label for='password' >Password: </label>" +
							"<input type='password' name='password' id='password' maxlength=\"25\" /></div>" +
							
							"<div id='row'><label for='first_name' >First Name: </label>" +
							"<input type='text' name='first_name' id='first_name' maxlength=\"25\" />" +
							"&nbsp; &nbsp;<label for='last_name' > Last Name: </label>" +
							"<input type='text' name='last_name' id='last_name' maxlength=\"25\" /></div>" +
							
							"<div id='row'><label for='address' >Address: </label>" + 
							"<input type='text' name='address' id='address' maxlength=\"68\" size =\"60\" /></div>" +
							
							"<div id='row'><label for='city' >City:</label>" +
							"<input type='text' name='city' id='city' maxlength=\"30\" />" +
							"&nbsp; &nbsp;<label for='statecode' >State: </label>" +
							"<input type='text' name='statecode' id='statecode' maxlength=\"2\" size=\"2\" /> </div>" + 
							
							"<center><div id='row'><input type='submit' name='submit' value='Register' /></div></center>" +
							"</fieldset>" +
							"</form></div></div>";
//					pw.print("<form id='register' action='Register' method='post' accept-charset='UTF-8'>" +
//							"<fieldset >" +
//							"<legend>Register</legend>" + 
//							"<input type='hidden' name='submitted' id='submitted' value='1'/>" +
//							"<label for='email' >Email: </label>" +
//							"<input type='text' name='email' id='email' maxlength=\"25\" value='" + email + "' />" +
//							"<br />" +
//							"<label for='password' >Password: </label>" +
//							"<input type='password' name='password' id='password' maxlength=\"25\" />" +
//							"<br />" + 
//							"<label for='first_name' >First Name: </label>" +
//							"<input type='text' name='first_name' id='first_name' maxlength=\"25\" value='" + first_name + "' />" +
//							"<label for='last_name' > Last Name: </label>" +
//							"<input type='text' name='last_name' id='last_name' maxlength=\"25\" value='" + last_name + "' />" +
//							"<br />" + 
//							"<label for='address' >Address: </label>" + 
//							"<input type='text' name='address' id='address' maxlength=\"68\" size =\"60\" value='" + address + "' />" +
//							"<br />" + 
//							"<label for='city' >City:</label>" +
//							"<input type='text' name='city' id='city' maxlength=\"30\" value='" + city + "' />" +
//							"<label for='statecode' >State Code: </label>" +
//							"<input type='text' name='statecode' id='statecode' maxlength=\"2\" size=\"2\" value='" + state_code
//							+ "' />" +
//							"<br />" + 
//							"<input type='submit' name='submit' value='Register' />" +
//							"</fieldset>" +
//							"</form>");
				}
			}
//			pgWrite += "</html>";
			writePage(pgWrite, pw);
//			pw.print("</html>");
		}
	}

}
