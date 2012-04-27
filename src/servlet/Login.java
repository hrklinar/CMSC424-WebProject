package servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import servlet.GlobalUser;
/**
 * Servlet implementation class Login
 */
@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    public void writePage(String content, PrintWriter pw)
    {
    	String writeString = "<!doctype html>"+
    						 "<html>"+
    						 "<head>"+
    						 	"<meta http-equiv='Content-Type' content='text/html; charset=UTF-8' />"+
    						 	"<link rel='stylesheet' href='styles.css' title='MainStyle' />"+
    						 	"<title>HB's Pizzeria--Online Ordering</title>"+
    						 "</head>"+
    						 "<body>"+
    						 	"<div id='header'>"+
    						 		"<div id='contentwrapper_hdr'>"+
    						 			"<div id='header_center'>"+
    						 				"<h1>HB's Pizzeria: Online Ordering</h1>"+
    						 			"</div>"+
    						 		"</div>"+
    						 		"<div id='header_left'>"+    						 			
    						 		"</div>"+
    						 		"<div id='header_right'>"+
    						 		"</div>"+
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
		response.setContentType("text/html");
		HttpSession session = request.getSession(true);
		String email = request.getParameter("email");
		String pass = request.getParameter("password");
		PrintWriter pw = response.getWriter();
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
		    resultSet = statement.executeQuery("SELECT email, first_name, last_name, address from customers where email = '" + email + "' AND " +
		    		"password = '" + pass + "'");
		    String writeStr = "";
		    if (resultSet.next())
		    {
			    GlobalUser user = new GlobalUser(resultSet.getString(1), resultSet.getString(2), 
			    		resultSet.getString(3), resultSet.getString(4));
			    writeStr += "<center>You have successfully logged in as " + user.first_name + " " + user.last_name + "(" + user.email + ").";
			    writeStr += "<form action='Order' method='post'><input type='submit' value='Order Online'></form>";

			    session.setAttribute("user", user);
			    session.setAttribute("email", user.email);
			} else {
				writeStr += "<center><font color='red'>The username or password you entered was incorrect.</font>";				

		    }
		    writeStr += "<form action='index.jsp'><input type='submit' value='Home'></form></center>";
		    writePage(writeStr, pw);
		}
		catch(SQLException e) {
		    System.out.println("An error occured.");
		    System.out.println(e.toString());
		}
	}

}
