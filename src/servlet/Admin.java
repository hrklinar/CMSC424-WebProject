package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import servlet.AdminQuery;

/**
 * Servlet implementation class Admin
 */
@WebServlet("/Admin")
public class Admin extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Admin() {
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
    						 	"<link rel='stylesheet' href='datepicker.css' type='text/css' />"+
    						 	"<link rel='stylesheet' href='jqueryUItimer.css' type='text/css' />"+
    						 	"<link rel='stylesheet' media='all' type='text/css' href='jquery-ui-1.8.16.custom.css' />"+
    						 	"<script type='text/javascript' src='http://ajax.googleapis.com/ajax/libs/jquery/1.4.2/jquery.min.js'></script>"+    							
    							"<script type='text/javascript' src='http://ajax.googleapis.com/ajax/libs/jqueryui/1.5.3/jquery-ui.min.js'></script>"+
    							"<script type='text/javascript' src='jquery.autocomplete.js'></script>"+
    							"<script type='text/javascript' src='jquery/jquery-1.7.1.min.js'></script>"+
    							"<script type='text/javascript' src='jquery/jquery-ui-1.8.16.custom.min.js'></script>"+
    							"<script type='text/javascript' src='jquery/jquery-ui-timepicker-addon.js'></script>"+
    							"<script type='text/javascript' src='jquery/jquery-ui-sliderAccess.js'></script>"+
    						 	"<title>HB's Pizzeria--Administration</title>"+
    						 	"<!-- This function is used for my calendar -->"+
    						    "<script type='text/javascript'>"+
    						    	"$(document).ready(function(){"+
    						    		"$('#st_date').datepicker();"+
  									  "});"+
  									"$(document).ready(function(){"+
						    		    "$('#end_date').datepicker();"+
									  "});"+
									"$(document).ready(function(){"+
						    		   "$('#st_time').timepicker({});"+
									  "});"+
								    "$(document).ready(function(){"+
					    		         "$('#end_time').timepicker({});"+
								      "});"+
  								"</script>"+
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
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter pw = response.getWriter();
		HttpSession session = request.getSession(true);
		String adminName = (String)session.getAttribute("adminName");
		String adminPwd = (String)session.getAttribute("adminPwd");
		String pgWrite = "";
		
		//Form values
		String logout = request.getParameter("adminLogout");
		String custHistory = request.getParameter("custHistory");
		String twRevenue = request.getParameter("revenue");
		String hhReport = request.getParameter("HHreport");
		String prefCust = request.getParameter("prefCustReport");
		String inactive = request.getParameter("inactiveCusts");
		String retHome = request.getParameter("returnHome");
		
		//The administrator hasn't visited this page before
		if (adminName == null && adminPwd == null)
		{
			pgWrite += "<div class='register_box'><div id='panel'>";
			pgWrite += "<form id='adminLogin' action='adminLogin' method='post' accept-charset='UTF-8'>" +
					"<fieldset >" +
					"<legend>Administrator Login</legend>" + 
					"<input type='hidden' name='submitted' id='submitted' value='1'/>" +
					"<div id='row'><label for='adminId' >Login ID: </label>" +
					"<input type='text' name='adminId' id='adminID' maxlength=\"25\" /></div>" +
					"<div id='row'><label for='adminPwd' >Password: </label>" +
					"<input type='password' name='adminPwd' id='adminPwd' maxlength=\"25\" /></div>" +
					"<center><div id='row'><input type='submit' name='adminSubmit' value='Log on' /></div></center>" +
					"</fieldset>" +
					"</form></div></div>";
		}
		else
		{
			if (logout != null)
			{
				//the user wants to logout
				session.removeAttribute("adminName");
				session.removeAttribute("adminPwd");
				pgWrite += "<div class='register_box'><div id='err'>Logout Successful!</div><div id='panel'>";
				pgWrite += "<form id='adminLogin' action='adminLogin' method='post' accept-charset='UTF-8'>" +
						"<fieldset >" +
						"<legend>Administrator Login</legend>" + 
						"<input type='hidden' name='submitted' id='submitted' value='1'/>" +
						"<div id='row'><label for='adminId' >Login ID: </label>" +
						"<input type='text' name='adminId' id='adminID' maxlength=\"25\" /></div>" +
						"<div id='row'><label for='adminPwd' >Password: </label>" +
						"<input type='password' name='adminPwd' id='adminPwd' maxlength=\"25\" /></div>" +
						"<center><div id='row'><input type='submit' name='adminSubmit' value='Log on' /></div></center>" +
						"</fieldset>" +
						"</form></div></div>";
			}
			else if (retHome == null)
			{
				//some kind of query is request from the DB: 
				//here the forms will be made and AdminQuery will handle them.
				
				if (custHistory != null)
				{
					session.setAttribute("adminTask", "custHistory");
					pgWrite += "<div class='register_box'>";
					pgWrite += "<h2>Customer Transaction History Query</h2>";
					pgWrite += "<div id='panel'>";
					pgWrite += "<form id='custHistory' action='AdminQuery' method='post' accept-charset='UTF-8'>"+
									"<div id='row'><label for='email_in' >Customer Email: </label>" +
									"<input type='text' name='email_in' id='email_in' maxlength=\"25\" /></div>" +
									"<div id='row'><label for='start_in' >Start Date: </label>" +
									"<input type=\"text\" id=\"st_date\" name='start_in'></div>" +
									"<div id='row'><label for='end_in' >End Date: </label>" +
									"<input type=\"text\" id=\"end_date\" name='end_in'></div>" +
									"<center><div id='row'><input type='submit' name='custHistSubmit' value='Submit' /></div></center>" +
								"</form>";
					pgWrite += "</div></div>";
					pgWrite += "<form id='back' action='Admin' method='post' accept-charset='UTF-8'>" +
						     "<center><div id='row'><input type='submit' name='returnHome' value='Back' /></div></center>"+
							"</form>";
				}
				else if (twRevenue != null)
				{
					session.setAttribute("adminTask", "twRevenue");
					pgWrite += "<div class='register_box'>";
					pgWrite += "<h2>Time-Window Revenue</h2>";
					pgWrite += "<div id='panel'>";
					pgWrite += "<form id='twRevenue' action='AdminQuery' method='post' accept-charset='UTF-8'>"+									
									"<div id='row'><label for='start_in' >Start Date: </label>" +
									"<input type=\"text\" id=\"st_date\" name='start_in'>" +
									"&nbsp;&nbsp;<label for='start_time' >Start Time (24h): </label>" +
									"<input type=\"text\" id=\"st_time\" name='start_time'></div>" +
									"<div id='row'><label for='end_in' >End Date: </label>" +
									"<input type=\"text\" id=\"end_date\" name='end_in'>" +
									"&nbsp;&nbsp;<label for='end_time' >End Time (24h): </label>" +
									"<input type=\"text\" id=\"end_time\" name='end_time'></div>" +
									"<center><div id='row'><input type='submit' name='twRevenueSubmit' value='Submit' /></div></center>" +
								"</form>";
					pgWrite += "</div></div>";
					pgWrite += "<form id='back' action='Admin' method='post' accept-charset='UTF-8'>" +
						     "<center><div id='row'><input type='submit' name='returnHome' value='Back' /></div></center>"+
							"</form>";
				}
				else if (hhReport != null)
				{
					session.setAttribute("adminTask", "hhReport");
					pgWrite += "<div class='register_box'>";
					pgWrite += "<h2>Happy Hour Analysis Report</h2>";
					pgWrite += "<div id='panel'>";
					pgWrite += "<form id='twRevenue' action='AdminQuery' method='post' accept-charset='UTF-8'>"+									
									"<div id='row'><label for='start_in' >Date: </label>" +
									"<input type=\"text\" id=\"st_date\" name='start_in'></div>" +									
									"<center><div id='row'><input type='submit' name='hhReportSubmit' value='Submit' /></div></center>" +
								"</form>";
					pgWrite += "</div></div>";
					pgWrite += "<form id='back' action='Admin' method='post' accept-charset='UTF-8'>" +
						     "<center><div id='row'><input type='submit' name='returnHome' value='Back' /></div></center>"+
							"</form>";
				}
				else if (prefCust != null)
				{
					session.setAttribute("adminTask", "prefCust");
					pgWrite += "Prefferred Customers Report Query<br>";
					pgWrite += "NO INPUT";
					pgWrite += "<form id='back' action='Admin' method='post' accept-charset='UTF-8'>" +
						     "<center><div id='row'><input type='submit' name='returnHome' value='Back' /></div></center>"+
							"</form>";
				}
				else if (inactive != null)
				{
					session.setAttribute("adminTask", "inactive");
					pgWrite += "<div class='register_box'>";
					pgWrite += "Inactive Customers Report Query<br>";
					String query = "select email, first_name, last_name"+
								   "from customers"+
								   "where active ='N'"+
								   "order by last_name";
					AdminQuery aq = new AdminQuery();
					ResultSet result = aq.executeQuery(query);
					try {
						if (result.next())
						{
							pgWrite += "<table border='1'";
							pgWrite += "<tr><td>Last Name</td><td>First Name></td><td>Email</td></tr>";							
						    do
						    {						    	
						    	pgWrite += "<tr>";
						    	pgWrite += "<td>"+result.getString(3)+ "</td>";
						    	pgWrite += "<td>"+result.getString(2)+ "</td>";
						    	pgWrite += "<td>"+result.getString(1)+ "</td>";
						    	pgWrite += "</tr>";
						    }while(result.next());
						    pgWrite += "</table>";
						}
						else
						{
							pw.println("There are no inactive customers");
						}
					} catch (SQLException e) {
						System.out.println("An SQL error occurred.");
						e.printStackTrace();
					}
					pgWrite += "<form id='back' action='Admin' method='post' accept-charset='UTF-8'>" +
						     "<center><div id='row'><input type='submit' name='returnHome' value='Back' /></div></center>"+
							"</form>";
				}
			}			
			else
			{
				//the user is still logged in but back at home screen--show buttons
				pgWrite += "<div class='register_box'>";
				pgWrite += "<form id='adminButtons' action='Admin' method='post' accept-charset='UTF-8'>"+
						 "<center>"+
							"<div id='row'><input type='submit' name='custHistory' value='Customer History' /></div>"+
							"<div id='row'><input type='submit' name='revenue' value='Time-Window Revenue' /></div>"+
							"<div id='row'><input type='submit' name='HHreport' value='Happy Hour Analysis' /></div>"+
							"<div id='row'><input type='submit' name='prefCustReport' value='Preferred Customers' /></div>"+
							"<div id='row'><input type='submit' name='inactiveCusts' value='Inactive Customers Report' /></div>"+
						 "</center></form>";
				pgWrite += "</div><form id='logout' action='Admin' method='post' accept-charset='UTF-8'>" +
					     "<center><div id='row'><input type='submit' name='adminLogout' value='Log out' /></center>"+
						"</form>";
			}
			
		}
		writePage(pgWrite, pw);
	}

}
