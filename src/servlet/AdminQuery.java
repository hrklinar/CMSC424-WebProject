package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Formatter;
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
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter pw = response.getWriter();
		AdminQuery adminQuery = new AdminQuery();
		String pg = "";
		HttpSession session = request.getSession(true);
		String task = (String) session.getAttribute("adminTask");
		
		/*Fields from requesting form */
		String email = request.getParameter("email_in");
		String start_date = request.getParameter("start_in");

		String end_date = request.getParameter("end_in");

		String start_time = request.getParameter("start_time");
		String end_time = request.getParameter("end_time");
		
		if (task.equals("custHistory"))
		{			
			pg += "<div class='register_box'>";
			pg += "<h2>Inactive Customers Report Query</h2><br>";
			String st_todate = "to_date('"+start_date+"', 'MM/DD/YYYY')";
			String end_todate = "to_date('"+end_date+"', 'MM/DD/YYYY')";
			String format_date = "TO_CHAR(trans_date,'FXDD-MON-YYYY')";
			String t_email = email.trim();
			System.out.println(t_email);
			String myquery = "select trans_id, "+format_date+", total from transactions where status = 'C' AND email = '"+t_email+"' "+
					         "AND trans_date >= "+ st_todate +" AND trans_date <= "+end_todate;
			ResultSet result = adminQuery.executeQuery(myquery);
			try {
				if (result.next())
				{					
					do
				    {						
						String trans_id = result.getString(1);
						String date = result.getString(2);
						String total = result.getString(3);
						pg += "Transaction "+trans_id+" on "+date+". TOTAL = $"+total;
						pg += "<table border='1'";
						pg += "<tr><td><div id='table_hdr'>Item Description</div></td><td><div id='table_hdr'>Qty</div></td></tr>";
						String pizza_query = "select pizza_id, qty from trans_p where trans_id = "+trans_id;
						ResultSet pizza_results = adminQuery.executeQuery(pizza_query);
						if (pizza_results.next())
						{
							do
						    {
								String pizza_id = pizza_results.getString(1);
								String pizza_desc = parse_pizzaID(pizza_id);
								String p_qty = pizza_results.getString(2);
								pg += "<tr><td>"+pizza_desc+"</td><td>"+p_qty+"</tr>";
						    }while(pizza_results.next());							
						}
						String drink_query = "select d.supplier, t.qty from trans_d t, drinks d "+
											  "where t.trans_id = "+trans_id+" AND d.drink_id = t.drink_id";
						ResultSet drink_results = adminQuery.executeQuery(drink_query);
						if (drink_results.next())
						{
							do
						    {
								String drink = drink_results.getString(1);
								String d_qty = drink_results.getString(2);
								pg += "<tr><td>"+drink+"</td><td>"+d_qty+"</tr>";
						    }while(drink_results.next());
						}
						pg += "</table><br>";
				    }while(result.next());
				}
				else
				{
					pg += "There are no transactions that match your given input.";
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			pg += "</div><form id='back' action='Admin' method='post' accept-charset='UTF-8'>" +
				     "<center><div id='row'><input type='submit' name='returnHome' value='Back' /></div></center>"+
					"</form>";
		}
		else if (task.equals("twRevenue"))
		{
			pg += "<div class='register_box'>";
			pg += "<h2>Time Window Revenue Report</h2><br>";
			String d1 = start_date + " "+ start_time;
			String d2 = end_date + " "+ end_time;
			
			Date date1 = null, date2 = null;
			  SimpleDateFormat dtf = new SimpleDateFormat("MM/dd/yyyy HH:mm");	 
			try {
				date1 = dtf.parse(d1);
				date2 = dtf.parse(d2);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			System.out.println(start_date+":"+date1);
			System.out.println(date1.getHours()+"|"+date1.getMinutes());
			long dayDiff = computeDays(date1, date2);
			boolean prevDay = false, prevWeek = false, prevMonth = false;
			System.out.println(dayDiff);
			if (dayDiff == 0)
			{
				prevDay = true;
				prevWeek = true;
				prevMonth = true;
			}
			if (dayDiff > 0 && dayDiff < 8)
			{
				//Do NOT need to compute the previous week
				prevDay = false;
				prevWeek = true;
				prevMonth = true;
			}
			else if (dayDiff >= 8 && dayDiff <= 30)
			{
				prevDay = false;
				prevWeek = false;
				prevMonth = true;
			}
			
			/*first compute the statistics for the specified time window.*/
			String startTime, endTime;
			startTime = "to_timestamp('"+d1+"', 'MM/DD/YYYY HH24:MI')";
			endTime = "to_timestamp('"+d2+"', 'MM/DD/YYYY HH24:MI')";
			pg += "<b> Selected Time Period </b><br>";
			pg += outputTWrevenue(startTime, endTime)+"<br>";
			/*Based on elapsed days, compute previous day, week, and/or month */
			if (prevDay)
			{
				pg += "<b> Previous Day </b><br>";
				startTime = "(to_timestamp('"+d1+"', 'MM/DD/YYYY HH24:MI') - 1)";
				endTime = "(to_timestamp('"+d2+"', 'MM/DD/YYYY HH24:MI') - 1)";
				pg += outputTWrevenue(startTime, endTime)+"<br>";
			}
			if (prevWeek)
			{
				pg += "<b> Previous Week </b><br>";
				startTime = "(to_timestamp('"+d1+"', 'MM/DD/YYYY HH24:MI') - 7)";
				endTime = "(to_timestamp('"+d2+"', 'MM/DD/YYYY HH24:MI') - 7)";
				pg += outputTWrevenue(startTime, endTime)+"<br>";
			}
			if (prevMonth)
			{
				pg += "<b> Previous Month </b><br>";
				startTime = "(to_timestamp('"+d1+"', 'MM/DD/YYYY HH24:MI') - 30)";
				endTime = "(to_timestamp('"+d2+"', 'MM/DD/YYYY HH24:MI') - 30)";
				pg += outputTWrevenue(startTime, endTime)+"<br>";
			}
			
			pg += "</div><form id='back' action='Admin' method='post' accept-charset='UTF-8'>" +
				     "<center><div id='row'><input type='submit' name='returnHome' value='Back' /></div></center>"+
					"</form>";
		}
		else if (task.equals("hhReport"))
		{
			pg += "<div class='register_box'>";
			pg += "<h2>Happy Hour Report</h2><br>";			

			String format_date = "TO_CHAR(trans_date,'MM-DD-YYYY')";
			String format_time = "TO_CHAR(time,'hh24:mi')";
			String format_day = "TO_CHAR(trans_date, 'DAY') as day ";
			String queryBeforeDate = "to_date('"+start_date+"', 'MM-DD-YYYY') - 7";
			String queryAfterDate = "to_date('"+start_date+"', 'MM-DD-YYYY') + 7";
			String queryDate = "to_date('"+start_date+"', 'MM-DD-YYYY')";
			String beforeQuery = "select trans_id, "+format_date+", total, "+format_time+", "+format_day+
								 	"from transactions where trans_date >= "+queryBeforeDate+
									"AND trans_date <= "+queryDate+" AND status = 'C'";
			String afterQuery = "select trans_id, "+format_date+", total, "+format_time+", "+format_day+
									"from transactions where trans_date <= "+queryAfterDate+
									"AND trans_date > "+queryDate+" AND status = 'C'";
			ResultSet week1 = adminQuery.executeQuery(beforeQuery);
			ResultSet week2 = adminQuery.executeQuery(afterQuery);
			
			pg += adminQuery.outputHHweek(week1, start_date, "Prior to")+"<br>";
			pg += adminQuery.outputHHweek(week2, start_date, "After");			
			pg += "</div><form id='back' action='Admin' method='post' accept-charset='UTF-8'>" +
				     "<center><div id='row'><input type='submit' name='returnHome' value='Back' /></div></center>"+
					"</form>";
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
	
	public String parse_pizzaID(String pizza_id)
	{
		String pizza_desc = "";
		if (pizza_id.charAt(0) == '1')
			pizza_desc += "Small, ";
		else if (pizza_id.charAt(0) == '2')
			pizza_desc += "Medium, ";
		else if (pizza_id.charAt(0) == '3')
			pizza_desc += "Large, ";
		if (pizza_id.charAt(1) == '1')
			pizza_desc += "regular crust pizza, with: ";
		else if (pizza_id.charAt(1) == '2')
			pizza_desc += "thin crust pizza, with: ";
		if (pizza_id.charAt(2) == '1')
			pizza_desc += "black olives, ";
		if (pizza_id.charAt(3) == '1')
			pizza_desc += "extra cheese, ";
		if (pizza_id.charAt(4) == '1')
			pizza_desc += "green peppers, ";
		if (pizza_id.charAt(5) == '1')
			pizza_desc += "ham, ";
		if (pizza_id.charAt(6) == '1')
			pizza_desc += "mushrooms, ";
		if (pizza_id.charAt(7) == '1')
			pizza_desc += "onions, ";
		if (pizza_id.charAt(8) == '1')
			pizza_desc += "pepperoni, ";
		if (pizza_id.charAt(9) == '1')
			pizza_desc += "sausage.";
		return pizza_desc;
	}
	
	public String computeDay (String fullDay)
	{
		String res = null;
		if (fullDay.equals("MONDAY"))
			res = "M";
		if (fullDay.equals("TUESDAY"))
			res = "T";
		if (fullDay.equals("WEDNESDAY"))
			res = "W";
		if (fullDay.equals("THURSDAY"))
			res = "H";
		if (fullDay.equals("FRIDAY"))
			res = "F";
		if (fullDay.equals("SATURDAY"))
			res = "SA";
		if (fullDay.equals("SUNDAY"))
			res = "SU";
		return res;
	}
	
	public String outputHHweek(ResultSet weekRes, String start_date, String time)
	{
		String pg = "";
		int numPizzas;
		double pizzaRevenue;
		AdminQuery adminQuery = new AdminQuery();
		try {
			if (weekRes.next())
			{
				pg += "<b>Week "+time+" "+start_date+"</b><br>";
				pg += "<table border='1'>";
				pg += "<tr><td><div id='table_hdr'>Date</div></td><td><div id='table_hdr'>Number Pizzas Sold</div></td><td><div id='table_hdr'>Total Revenue</div></td></tr>";
				do
				{
					numPizzas = 0;
					pizzaRevenue = 0.0;
				    String daySym = computeDay(weekRes.getString(5).trim());
				    Integer trans_id = Integer.parseInt(weekRes.getString(1));
				    String t_time = weekRes.getString(4).trim();
				    String t_date = weekRes.getString(2).trim();
				    String to_timestamp = "to_timestamp('"+t_time+"', 'HH24:MI')";
				    
				    /*This query returns pizzaId, and price for a HH on a given day */
				    String hhPricequery = "select price , item_id from happyprices where "+to_timestamp+
							  				" >= start_time AND "+to_timestamp+" <= end_time AND day = '"+daySym+"'";
				    ResultSet hhResult = adminQuery.executeQuery(hhPricequery);
				    ArrayList<String> hhPrices = new ArrayList<String>();
				    ArrayList<String> hhIds = new ArrayList<String>();
				    if (hhResult.next())
				    {
				    	do
				    	{
				    		hhPrices.add(hhResult.getString(1).trim());
				    		hhIds.add(hhResult.getString(2).trim());
				    	}while (hhResult.next());
				    }
				    
				    /*This query will return the pizza_ids for a particular transaction */
				    String pizza_query = "select pizza_id, qty from trans_p where trans_id = "+trans_id;
				    ResultSet pizza_res = adminQuery.executeQuery(pizza_query);
				    if (pizza_res.next())
				    {
				    	do
				    	{
				    		String p_id = pizza_res.getString(1).trim();				    		
				    		Integer p_qty = Integer.parseInt(pizza_res.getString(2));
				    		/* retreive the HH price for this pizza ID */
				    		int i = 0;
				    		for (String id : hhIds)
				    		{
				    			//This pizza is a HH pizza
				    			if (id.equals(p_id))
				    			{
				    				numPizzas += 1;
					    			Double hhprice = Double.valueOf(hhPrices.get(i));
					    			pizzaRevenue += (p_qty * hhprice);
					    			Formatter fmt_rev = new Formatter(); 
								    fmt_rev.format("%.2f", pizzaRevenue);
								    pg += "<tr><td>"+t_date+"</td><td><center>"+numPizzas+"</center></td><td><center>$"+fmt_rev+"</center></td></tr>";
				    			}
				    			i++;
				    		}
				    	}while (pizza_res.next());
				    }
				}while (weekRes.next());
				pg += "</table>";
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return pg;
	}
	
	public static long computeDays(Date date1, Date date2) {  
		return (date2.getTime() - date1.getTime()) / (24 * 60 * 60 * 1000);  
	}
	
	public double compute_pizzaTotal(String pizza_id)
	{
		double total = 0;
		if (pizza_id.charAt(0) == '1')
			total += 8.00;
		else if (pizza_id.charAt(0) == '2')
			total += 10.00;
		else if (pizza_id.charAt(0) == '3')
			total += 12.00;
		
		if (pizza_id.charAt(2) == '1')
			total += 0.50;
		if (pizza_id.charAt(3) == '1')
			total += 0.50;
		if (pizza_id.charAt(4) == '1')
			total += 0.50;
		if (pizza_id.charAt(5) == '1')
			total += 0.50;
		if (pizza_id.charAt(6) == '1')
			total += 0.50;
		if (pizza_id.charAt(7) == '1')
			total += 0.50;
		if (pizza_id.charAt(8) == '1')
			total += 0.50;
		if (pizza_id.charAt(9) == '1')
			total += 0.50;
		System.out.println(total);
		return total;
	}
	
	public String outputTWrevenue(String startTime, String endTime)
	{
		String pg = "";
		AdminQuery aq = new AdminQuery();

		double revenue = 0.0, pizzaTotal, drinkTotal;
		String trans_query = "select trans_id, trans_date, total from transactions where status = 'C'"+ 
								" AND time >= "+startTime+" AND time <= "+endTime;
		System.out.println(trans_query);
		ResultSet transRes = aq.executeQuery(trans_query);
		try {
			if (transRes.next())
			{
				pizzaTotal = 0.0;
				drinkTotal = 0.0;				
				pg += "<table border='1'>";
				pg += "<tr><td><div id='table_hdr'>Total Revenue</div></td><td><div id='table_hdr'>Pizza Revenue</div></td><td><div id='table_hdr'>Drink Revenue</div></td></tr>";
				do
				{
					Integer tID = Integer.parseInt(transRes.getString(1).trim());
					String t_date = transRes.getString(2).trim();
					Double total = transRes.getDouble(3);
					
					String pizzaQ = "select pizza_id, qty from trans_p where trans_id ="+tID;
					ResultSet pizzas = aq.executeQuery(pizzaQ);
					if (pizzas.next())
					{
						do
						{
							String p_id = pizzas.getString(1);
							Integer qty = pizzas.getInt(2);
							double pizza_cost = aq.compute_pizzaTotal(p_id);
							pizzaTotal += (pizza_cost * qty);
						}while (pizzas.next());
					}
					String drinkQ = "select p.reg_price, t.qty from prices p, trans_d t "+
									"where t.trans_id = "+tID+" AND p.type = 'D' AND p.item_id = t.drink_id";
					ResultSet drinks = aq.executeQuery(drinkQ);
					if (drinks.next())
					{
						do
						{
							Double d_price = drinks.getDouble(1);
							Integer d_qty = drinks.getInt(2);								
							drinkTotal += (d_price * d_qty);
						}while (drinks.next());
					}						
				}while (transRes.next());
				revenue = pizzaTotal + drinkTotal;
				Formatter fmt_rev = new Formatter(); 
				fmt_rev.format("%.2f", revenue);
				Formatter fmt_piz = new Formatter();
				fmt_piz.format("%.2f", pizzaTotal);
				Formatter fmt_dri = new Formatter();
				fmt_dri.format("%.2f", drinkTotal);
				pg += "<tr><td>$"+fmt_rev+"</td><td>$"+fmt_piz+"</td><td>$"+fmt_dri+"</td></tr></table>";
			}
			else
			{
				pg += "There are no transactions for this time period.";
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pg;
	}

}
