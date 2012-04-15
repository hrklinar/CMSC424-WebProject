package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class AdminQuery
 */
@WebServlet("/AdminQuery")
public class AdminQuery extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AdminQuery() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    public void writePage(String content, PrintWriter pw)
    {
    	String writeString = "<!doctype html>"+
    						 "<html>"+
    						 "<head>"+
    						 	"<meta http-equiv='Content-Type' content='text/html; charset=UTF-8' />"+
    						 	"<link rel='stylesheet' href='adminStyles.css' title='MainStyle' />"+
    						 	"<title>HB's Pizzeria--Administration</title>"+
    						 "</head>"+
    						 "<body>"+
    						 	"<div id='header'>"+
    						 		"<div id='contentwrapper_hdr'>"+
    						 			"<div id='header_center'>"+
    						 				"<h1>HB's Pizzeria: Administration</h1>"+
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
	@SuppressWarnings("deprecation")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter pw = response.getWriter();
		String pg = "";
		HttpSession session = request.getSession(true);
		String task = (String) session.getAttribute("adminTask");
		
		/*Fields from requesting form */
		String email = request.getParameter("email_in");
		String start_date = request.getParameter("start_in");
		String end_date = request.getParameter("end_in");
		String start_time = request.getParameter("start_time");
		String end_time = request.getParameter("end_time");
		SimpleDateFormat parseDate = new java.text.SimpleDateFormat("mm/dd/yyyy");
		SimpleDateFormat parseTime = new SimpleDateFormat("HH:mm");
		Date start = null, end = null, startT = null, endT = null;
		try {
			if (start_date != null)
				start = parseDate.parse(start_date);
			if (end_date != null)
				end = parseDate.parse(end_date);
			if (start_time != null)
				startT = parseTime.parse(start_time);
			if (end_time != null)
				endT = parseTime.parse(end_time);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (task.equals("custHistory"))
		{			
			pg += start.toString() + "<br>";
			pg += end.toString() + "<br>";			
			pg += email;
		}
		else if (task.equals("twRevenue"))
		{
			pg += start.toString() + "<br>";
			pg += end.toString() + "<br>";			
			pg += startT.toString() + "<br>";
			pg += endT.toString() + "<br>";
		}
		else if (task.equals("hhReport"))
		{
			pg += start.toString();
		}
		else if (task.equals("prefCust"))
		{
			pg += "--NO INPUT--";
		}
		else if (task.equals("inactive"))
		{
			pg += "--NO INPUT--";
		}
		writePage(pg, pw);
		
	}
	
	public ResultSet executeQuery(String query)
	{
		Connection conn;
		ResultSet resultSet = null;
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
		    resultSet = statement.executeQuery(query);		    
		}
		catch(SQLException e) {
		    System.out.println("An error occurs.");
		    System.out.println(e.toString());
		}
		return resultSet;
	}

}
