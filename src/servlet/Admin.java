package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Formatter;

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
    						 		"<div id='header_left'>" +
    						 		"<form id='HOME' action='index.jsp' method='post' accept-charset='UTF-8'>" +
    									 "<center><div id='row'><input type='submit' name='index' value='Home' /></div></center>"+
    								"</form>"+
    						 		"</div>"+
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
		AdminQuery aq = new AdminQuery();
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
		String dispatchTicket = request.getParameter("dispatchTicket");
		
		
		
		//The administrator hasn't visited this page before
		if (adminName == null && adminPwd == null)
		{
//			System.out.println("inside nulls");
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
//				System.out.println("logout");
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
//					System.out.println("custhistory");
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
//					System.out.println("twrevenue");
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
//					System.out.println("hhreport");
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
//					System.out.println("prefcust");
					session.setAttribute("adminTask", "prefCust");					
					pgWrite += "<div class='register_box'><h2>Preferred Customers Report</h2>";
					String query = "with temp as (select email, sum(total) as total_spent "+
								    "from transactions where trans_date >= (sysdate - 30) AND status = 'C' "+
								    "group by email)"+
								    "select email, total_spent from temp "+
								    "where total_spent = (select max(total_spent) from temp)";
					ResultSet results = aq.executeQuery(query);
					try {
						if (results.next())
						{	
							pgWrite += "<table border='1'>";
							pgWrite += "<tr><td><div id='table_hdr'>Customer</div></td><td><div id='table_hdr'>Total Spent</div></td><td><div id='table_hdr'>Order Frequency</div></td><td><div id='table_hdr'>Amnt. Spent on Pizzas</div></td><td><div id='table_hdr'>Amount Spent on Drinks</div></td><td><div id='table_hdr'>Avg Spent per Delivery</div></td></tr>";
							do
							{
								int numTrans = 0;
								double totalPizza = 0;
								double totalDrink = 0;
								String email = (results.getString(1)).trim();
								String name_query = "select first_name, last_name from customers where email = '"+email+"'";
								ResultSet name_res = aq.executeQuery(name_query);
								String first_name = "", last_name = "";
								if (name_res.next())
								{
									first_name += name_res.getString(1);
									last_name += name_res.getString(2);
								}
								Double total_spent = Double.valueOf(results.getString(2));
								String trans_query = "select trans_id, total from transactions "+ 
														"where trans_date >= (sysdate - 30) AND status = 'C' AND email = '"+email+"'";
								ResultSet trans_result = aq.executeQuery(trans_query);
								if (trans_result.next())
								{
									do
								    {						    	
										Integer trans_id = Integer.parseInt(trans_result.getString(1));
										Double trans_total = Double.valueOf(trans_result.getString(2));
										numTrans++;
										String drink_query = "select drink_id, qty from trans_d where trans_id = "+trans_id;
										ResultSet drink_res = aq.executeQuery(drink_query);
										if (drink_res.next())
										{
											do
											{
												Integer d_id = Integer.parseInt(drink_res.getString(1));
												Integer d_qty = Integer.parseInt(drink_res.getString(2));
												if (d_id == 6)
													totalDrink += (2.50 * d_qty);
												else if (d_id < 6)
													totalDrink += (3.00 * d_qty);
												else if (d_id > 6)
													totalDrink += (5.00 * d_qty);
											}while(drink_res.next());
										}
										
										String pizza_query = "select pizza_id, qty from trans_p where trans_id = "+trans_id;
										ResultSet pizza_res = aq.executeQuery(pizza_query);
										if (pizza_res.next())
										{
											do
											{
												String pizza_id = pizza_res.getString(1);
												Integer p_qty = Integer.parseInt(pizza_res.getString(2));
												double pizzaPrice = aq.compute_pizzaTotal(pizza_id);
												totalPizza += (pizzaPrice * p_qty);
											}while(pizza_res.next());
										}
																				
								    }while(trans_result.next());
									double avg = (double)total_spent/(double)numTrans;
									Formatter fmt_total = new Formatter(); 
								    fmt_total.format("%.2f", total_spent);
								    Formatter fmt_totP = new Formatter(); 
								    fmt_totP.format("%.2f", totalPizza);
								    Formatter fmt_totD = new Formatter(); 
								    fmt_totD.format("%.2f", totalDrink);
								    Formatter fmt_avg = new Formatter(); 
								    fmt_avg.format("%.2f", avg);																	
									pgWrite += "<tr><td>"+first_name+" "+last_name+"</td><td>$"+ fmt_total +
												"</td><td>"+numTrans+"</td><td>$"+fmt_totP+"</td><td>$"+fmt_totD+
												"</td><td>$"+fmt_avg+"</td></tr>";
								}
								
							}while(results.next());
							pgWrite += "</table>";
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
					pgWrite += "</div><form id='back' action='Admin' method='post' accept-charset='UTF-8'>" +
						     "<center><div id='row'><input type='submit' name='returnHome' value='Back' /></div></center>"+
							"</form>";
				}
				else if (inactive != null)
				{
					session.setAttribute("adminTask", "inactive");
					pgWrite += "<div class='register_box'>";
					pgWrite += "<h2>Inactive Customers Report</h2><br>";
					String query = "select email, first_name, last_name "+
								   "from customers "+
								   "where active = 'N' "+
								   "order by last_name";
					
					ResultSet result = aq.executeQuery(query);
					try {
						if (result.next())
						{
							pgWrite += "<center><table border='1'";
							pgWrite += "<tr><td><div id='table_hdr'>Last Name</div></td><td><div id='table_hdr'>First Name</div></td><td><div id='table_hdr'>Email</div></td></tr>";							
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
							pgWrite += "There are no inactive customers";
						}
					} catch (SQLException e) {
						System.out.println("An SQL error occurred.");
						e.printStackTrace();
					}
					pgWrite += "</center></div><form id='back' action='Admin' method='post' accept-charset='UTF-8'>" +
						     "<center><div id='row'><input type='submit' name='returnHome' value='Back' /></center>"+
							"</form>";
				}
				else if (dispatchTicket != null)
				{
					session.setAttribute("adminTask", "dispatchTicket");
					String availableDrivers = "select deliv_id from deliverypersons where curr_loc = 'store'";
					ResultSet availDrivers = aq.executeQuery(availableDrivers);
					String orderQuery = "select trans_id from transactions where status = 'O' order by trans_id";
					ResultSet availOrders = aq.executeQuery(orderQuery);
					ArrayList<String> drivers = new ArrayList<String>();
					ArrayList<String> orders = new ArrayList<String>();
					
					try {
						if (availDrivers.next())
						{
							do
							{
								drivers.add(availDrivers.getString(1).trim());
							}while (availDrivers.next());
						}
						if (availOrders.next())
						{
							do
							{
								orders.add(availOrders.getString(1).trim());
							}while (availOrders.next());
						}
						if (drivers.size() > 0 && orders.size() > 0)
						{
							//enough drivers and orders to dispatch ticket
							pgWrite += "<div id='panel'>";
							pgWrite += "<form id='ticket' action='AdminQuery' method='post' accept-charset='UTF-8'>";
							pgWrite += "<b> Select a Driver ID<b><br>";
							pgWrite += "<select name='driver'>";
							for (String d : drivers)
							{
								  pgWrite += "<option>"+d+"</option>";								  
							}
							pgWrite += "</select><br><br>";
							
							pgWrite += "<b> Select an Order number<b><br>";
							pgWrite += "<select name='order'>";
							for (String order : orders)
							{
								  pgWrite += "<option>"+order+"</option>";								  
							}
							pgWrite += "</select><br><br>";
							pgWrite += "<input type='submit' name='dispatchTicket' value='Submit' />";
							pgWrite += "</form";
							pgWrite += "</div>";
						}
						else if (drivers.size() == 0)
						{
							pgWrite += "There are no drivers available.";
						}
						else if (orders.size() == 0)
						{
							pgWrite += "There are no orders available for delivery.";
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
					pgWrite += "<form id='back' action='Admin' method='post' accept-charset='UTF-8'>" +
						     "<center><div id='row'><input type='submit' name='returnHome' value='Back' /></div></center>"+
							"</form>";
				}
				else
				{
//					System.out.println("back at home screen");
					//the user is still logged in but back at home screen--show buttons
					pgWrite += "<div class='register_box'>";
					pgWrite += "<form id='adminButtons' action='Admin' method='post' accept-charset='UTF-8'>"+
							 "<center>"+
								"<div id='row'><input type='submit' name='custHistory' value='Customer History' /></div>"+
								"<div id='row'><input type='submit' name='revenue' value='Time-Window Revenue' /></div>"+
								"<div id='row'><input type='submit' name='HHreport' value='Happy Hour Analysis' /></div>"+
								"<div id='row'><input type='submit' name='prefCustReport' value='Preferred Customers' /></div>"+
								"<div id='row'><input type='submit' name='inactiveCusts' value='Inactive Customers Report' /></div>"+
								"<div id='row'><input type='submit' name='dispatchTicket' value='Dispatch Ticket' /></div>"+
							 "</center></form>";
					pgWrite += "</div><form id='logout' action='Admin' method='post' accept-charset='UTF-8'>" +
						     "<center><div id='row'><input type='submit' name='adminLogout' value='Log out' /></center>"+
							"</form>";
				}
//				System.out.println("end of retHome");
			}			
			else
			{
//				System.out.println("else");
				//the user is still logged in but back at home screen--show buttons
				pgWrite += "<div class='register_box'>";
				pgWrite += "<form id='adminButtons' action='Admin' method='post' accept-charset='UTF-8'>"+
						 "<center>"+
							"<div id='row'><input type='submit' name='custHistory' value='Customer History' /></div>"+
							"<div id='row'><input type='submit' name='revenue' value='Time-Window Revenue' /></div>"+
							"<div id='row'><input type='submit' name='HHreport' value='Happy Hour Analysis' /></div>"+
							"<div id='row'><input type='submit' name='prefCustReport' value='Preferred Customers' /></div>"+
							"<div id='row'><input type='submit' name='inactiveCusts' value='Inactive Customers Report' /></div>"+
							"<div id='row'><input type='submit' name='dispatchTicket' value='Dispatch Ticket' /></div>"+
						 "</center></form>";
				pgWrite += "</div><form id='logout' action='Admin' method='post' accept-charset='UTF-8'>" +
					     "<center><div id='row'><input type='submit' name='adminLogout' value='Log out' /></center>"+
						"</form>";
			}
			
		}
		writePage(pgWrite, pw);
	}
	
	

}
