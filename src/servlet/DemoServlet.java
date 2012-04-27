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

/**
 * Servlet implementation class DemoServlet
 */
@WebServlet("/DemoServletPath")
public class DemoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DemoServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		String st = request.getParameter("name");
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
		    resultSet = statement.executeQuery("SELECT * from customers where first_name = '"+st+"'");
		    if (resultSet.next())
		    {
			    do
			    {
			    	pw.println(resultSet.getString(1)+"<br>");
			    	pw.println(resultSet.getString(2)+"<br>");
			    	pw.println(resultSet.getString(3)+"<br>");
			    	pw.println(resultSet.getString(4)+"<br>");

			    }while(resultSet.next());
		    }
		    else
		    {
		    	pw.println("No state matches your given input.");
		    }
		    pw.println("<form action='index.jsp'><input type='submit' value='Back'></form>");
		}
		catch(SQLException e) {
		    System.out.println("An error occurs.");
		    System.out.println(e.toString());
		}
	}

}
