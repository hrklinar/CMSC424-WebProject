package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Formatter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class Driver
 */
@WebServlet("/Driver")
public class Driver extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Driver() {
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
		// TODO Auto-generated method stub
		response.setContentType("text/html");
		PrintWriter pw = response.getWriter();
//		pw.write("DRIVER.java");
		HttpSession session = request.getSession(true);
		String driverName = (String)session.getAttribute("driverName");
		String driverPwd = (String)session.getAttribute("driverPwd");
		String pgWrite = "";
		AdminQuery aq = new AdminQuery();
		//Form values
		String logout = request.getParameter("driverLogout");
		String update = request.getParameter("update");
		String completedOrder = request.getParameter("complete");
		
		//The driveror hasn't visited this page before
		if (driverName == null && driverPwd == null)
		{
			System.out.println("inside nulls");
			pgWrite += "<div class='register_box'><div id='panel'>";
			pgWrite += "<form id='driverLogin' action='DriverLogin' method='post' accept-charset='UTF-8'>" +
					"<fieldset >" +
					"<legend>Delivery Driver Login</legend>" + 
					"<input type='hidden' name='submitted' id='submitted' value='1'/>" +
					"<div id='row'><label for='driverId' >Login ID: </label>" +
					"<input type='text' name='driverId' id='driverID' maxlength=\"25\" /></div>" +
					"<div id='row'><label for='driverPwd' >Password: </label>" +
					"<input type='password' name='driverPwd' id='driverPwd' maxlength=\"25\" /></div>" +
					"<center><div id='row'><input type='submit' name='driverSubmit' value='Log on' /></div></center>" +
					"</fieldset>" +
					"</form></div></div>";
		}
		else
		{
			if (logout != null)
			{
				//the user wants to logout
				session.removeAttribute("driverName");
				session.removeAttribute("driverPwd");
				pgWrite += "<div class='register_box'><div id='panel'>";
				pgWrite += "<form id='driverLogin' action='DriverLogin' method='post' accept-charset='UTF-8'>" +
						"<fieldset >" +
						"<legend>Delivery Driver Login</legend>" + 
						"<input type='hidden' name='submitted' id='submitted' value='1'/>" +
						"<div id='row'><label for='driverId' >Login ID: </label>" +
						"<input type='text' name='driverId' id='driverID' maxlength=\"25\" /></div>" +
						"<div id='row'><label for='driverPwd' >Password: </label>" +
						"<input type='password' name='driverPwd' id='driverPwd' maxlength=\"25\" /></div>" +
						"<center><div id='row'><input type='submit' name='driverSubmit' value='Log on' /></div></center>" +
						"</fieldset>" +
						"</form></div></div>";
			}
			else if (update != null)
			{
				//the driver is checking to see if they have another order to deliver
				DriverLogin dl = new DriverLogin();
				pgWrite += dl.getDelivery(driverName.trim()); 
				//Update Button: attempt to retreive a new order to deliver.
//				pgWrite += "</div><form id='update' action='Driver' method='post' accept-charset='UTF-8'>" +
//					     "<center><div id='row'><input type='submit' name='update' value='Retrive New Order' /></center>"+
//						"</form>";
				//Logout Button: Exit the system.
				pgWrite  += "</div><form id='logout' action='Driver' method='post' accept-charset='UTF-8'>" +
					     "<center><div id='row'><input type='submit' name='driverLogout' value='Log out' /></center>"+
						"</form>";
			}
			else if (completedOrder != null)
			{
				//the driver is collecting money from the Customer								
				/*1- Get the total for this transaction and update total on customers table*/
				System.out.println("complete trans");
				String findTrans = "select t.trans_id, t.email, t.total from transactions t, deliveries d "+
									"where d.deliv_id = "+driverName.trim()+" AND d.trans_id = t.trans_id AND d.completed = 'U'";
				ResultSet trans_res = aq.executeQuery(findTrans);
				try {
					if (trans_res.next())
					{
						System.out.println("trans_res.next");
						Integer trans_id = trans_res.getInt(1);
						String email = trans_res.getString(2).trim();
						Double ordertotal = trans_res.getDouble(3);
						//Get customers total spent value
						String getCustTotal = "select total_spent from customers where email = '"+email+"'";
						ResultSet custTotal = aq.executeQuery(getCustTotal);
						if (custTotal.next())
						{
							Double totalSpent = custTotal.getDouble(1);
							totalSpent = totalSpent + ordertotal;
							//Update customer table with total Spent
							String updateCustomer = "update customers set total_spent = "+totalSpent+" where email = '"+email+"'";
							ResultSet updatecustRes = aq.executeQuery(updateCustomer);							
						}
						/*2- Update Transactions table so that the order is completed*/
						System.out.println("trans_Id = |"+trans_id+"|");
						String updateTrans = "update transactions set status = 'C' where trans_id = "+trans_id;
						ResultSet updateTransRes = aq.executeQuery(updateTrans);
						if (updateTransRes.next())
						{
							System.out.println(updateTransRes.rowUpdated());
								System.out.println("updated transactions");
						}
						
						/*3- Set curr_loc to store so the driver can receive another order*/
						String updateLoc = "update deliverypersons set curr_loc = 'store' where deliv_id = "+driverName.trim();
						ResultSet updateDriver = aq.executeQuery(updateLoc);
						if (updateDriver.next())
						{
							System.out.println(updateDriver.rowUpdated());
								System.out.println("updated deliveryPersons");
						}
						
						/*4- Update Deliveries table so that the order is completed*/
						System.out.println("final update");
						String updateDeliveries = "update deliveries set completed = 'C'";
						ResultSet updateDelivs = aq.executeQuery(updateDeliveries);
						if (updateDelivs.next())
						{
							System.out.println("updated deliveries table");
						}
					}
					pgWrite += "<b>Payment Successfully Received.</b><br>";
					//Update Button: try to retrieve another order
					pgWrite += "</div><form id='update' action='Driver' method='post' accept-charset='UTF-8'>" +
						     "<center><div id='row'><input type='submit' name='update' value='Retrieve New Order' /></center>"+
							"</form>";
					//Logout Button: Exit the system.
					pgWrite  += "</div><form id='logout' action='Driver' method='post' accept-charset='UTF-8'>" +
						     "<center><div id='row'><input type='submit' name='driverLogout' value='Log out' /></center>"+
							"</form>";
				} catch (SQLException e) {
					e.printStackTrace();
				}								
			}
			else
			{
				//the user is still logged in but back at home screen--show update button or new job
				DriverLogin dl = new DriverLogin();
				pgWrite += "DRIVER ELSE!";
				pgWrite += dl.getDelivery(driverName.trim());
//				pgWrite += "<div class='register_box'>";
//				pgWrite += "<form id='adminButtons' action='Admin' method='post' accept-charset='UTF-8'>"+
//						 "<center>"+
//							"<div id='row'><input type='submit' name='custHistory' value='Customer History' /></div>"+
//							"<div id='row'><input type='submit' name='revenue' value='Time-Window Revenue' /></div>"+
//							"<div id='row'><input type='submit' name='HHreport' value='Happy Hour Analysis' /></div>"+
//							"<div id='row'><input type='submit' name='prefCustReport' value='Preferred Customers' /></div>"+
//							"<div id='row'><input type='submit' name='inactiveCusts' value='Inactive Customers Report' /></div>"+							
//						 "</center></form>";
//				pgWrite += "</div><form id='logout' action='Admin' method='post' accept-charset='UTF-8'>" +
//					     "<center><div id='row'><input type='submit' name='adminLogout' value='Log out' /></center>"+
//						"</form>";
			}
			
		}
		writePage(pgWrite, pw);

	}

}
