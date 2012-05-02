package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class DriverLogin
 */
@WebServlet("/DriverLogin")
public class DriverLogin extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DriverLogin() {
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
		String id = request.getParameter("driverId");
		String pwd = request.getParameter("driverPwd");
		PrintWriter pw = response.getWriter();
		AdminQuery aq = new AdminQuery();
		String pg = "";

		System.out.println("ID=|"+id+"|Pwd=|"+pwd);
		if (id == null || pwd == null)
		{
			pg += "<div class='register_box'><div id='panel'>";
			pg += "<form id='driverLogin' action='DriverLogin' method='post' accept-charset='UTF-8'>" +
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
		else if (id != null && pwd != null && id.length() > 0 && pwd.length() > 0)
		{
			//both fields were entered, so lookup driver id in database.
//			Integer driverID = Integer.parseInt(id.trim());
			id = id.trim();
			pwd = pwd.trim();
			String driverQuery = "select password from deliverypersons where deliv_id = "+id;
			ResultSet driverRes = aq.executeQuery(driverQuery);
			try {
				if (driverRes.next())
				{
					//a password was retrieved from the DB
					String dbPwd = driverRes.getString(1).trim();
					if (dbPwd.equals(pwd))
					{
						//Login information is correct--
						session.setAttribute("driverName", id);
						session.setAttribute("Pwd", pwd);
						pg += getDelivery(id);
//						//Complete Delivery Button--Process payment
//						pg  += "</div><form id='completeOrder' action='Driver' method='post' accept-charset='UTF-8'>" +
//							     "<center><div id='row'><input type='submit' name='complete' value='Process Payment' /></center>"+
//								"</form>";
						pg += "</div><form id='logout' action='Driver' method='post' accept-charset='UTF-8'>" +
							     "<center><div id='row'><input type='submit' name='driverLogout' value='Log out' /></center>"+
								"</form>";
					}
					else
					{
						pg += "<div class='register_box'><div id='err'>Invalid Login Information</div><div id='panel'>";
						pg += "<form id='driverLogin' action='DriverLogin' method='post' accept-charset='UTF-8'>" +
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
				}
				else
				{
					//invalid login information
					pg += "<div class='register_box'><div id='err'>Invalid Login Information</div><div id='panel'>";
					pg += "<form id='driverLogin' action='DriverLogin' method='post' accept-charset='UTF-8'>" +
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
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else
		{		
			//at least one of the fields is empty
			pg += "<div class='register_box'><div id='err'>Invalid Login Information</div><div id='panel'>";
			pg += "<form id='driverLogin' action='DriverLogin' method='post' accept-charset='UTF-8'>" +
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
		writePage(pg, pw);
	}
	
	public String getDelivery(String driverID)
	{
		AdminQuery aq = new AdminQuery();
		String result = "";
		String query = "select trans_id from deliveries where deliv_id = "+driverID+" "+
						"AND completed = 'U'";
		ResultSet jobResults = aq.executeQuery(query);
		try {
			if (jobResults.next())
			{
				//there is a job waiting for the driver
				Integer trans_id = jobResults.getInt(1);
				//get the transaction information
				String orderQuery = "select c.first_name, c.last_name, c.address, t.total "+
									"from transactions t, customers c where t.trans_id = "+trans_id+" AND t.email = c.email";
				ResultSet orderResults = aq.executeQuery(orderQuery);
				if (orderResults.next())
				{
					String f_name = orderResults.getString(1).trim();
					String l_name = orderResults.getString(2).trim();
					String address = orderResults.getString(3).trim();
					result += "<h2>Order for "+f_name+" "+l_name+"</h2>";
					result += "<center><b>Address : "+address + "</b><br><br>";
					//Query trans_p to get the pizza ordered
					result += "<table border = 1>";
					result += "<tr><td><div id='table_hdr'>Item</div></td><td><div id='table_hdr'>Quantity</div></td></tr>";
					String pizzaQ = "select pizza_id, qty from trans_p where trans_id = "+trans_id;
					ResultSet pizzaResults = aq.executeQuery(pizzaQ);
					if (pizzaResults.next())
					{
						do
						{
							String pizza_id = pizzaResults.getString(1).trim();
							Integer p_qty = pizzaResults.getInt(2);
							String pizzaDesc = aq.parse_pizzaID(pizza_id);
							result += "<tr><td>"+pizzaDesc+"</td><td>"+p_qty+"</td></tr>";
						}while (pizzaResults.next());
					}
					String drinkQ = "select d.supplier, t.qty from drinks d, trans_d t "+
										"where t.trans_id = "+trans_id+" AND d.drink_id = t.drink_id";
					ResultSet drinkResults = aq.executeQuery(drinkQ);
					if (drinkResults.next())
					{
						do
						{
							String d_name = drinkResults.getString(1).trim();
							Integer d_qty = drinkResults.getInt(2);
							result += "<tr><td>"+d_name+"</td><td>"+d_qty+"</td></tr>";
						}while (drinkResults.next());
					}
					result += "</table></center>";
				}
				//Complete Order Button: try to retrieve another order
				result  += "</div><form id='completeOrder' action='Driver' method='post' accept-charset='UTF-8'>" +
					     "<center><div id='row'><input type='submit' name='complete' value='Process Payment' /></center>"+
						"</form>";
			}
			else
			{
				result += "<center>You currently do not have any orders that need to be delivered.</center>";
				result += "</div><form id='update' action='Driver' method='post' accept-charset='UTF-8'>" +
					     "<center><div id='row'><input type='submit' name='update' value='Retrieve New Order' /></center>"+
						"</form>";
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		return result;
	}

}
