package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import servlet.*;
/**
 * Servlet implementation class Order
 */
@WebServlet("/Order")
public class Order extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Order() {
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
	
	private Pizza generatePizza (HttpServletRequest request) {
		String pizzaID = "";
		double total = 0.0;
		String size = request.getParameter("size");
		if (size.equals("1")) {
			pizzaID += "1";
			total = 8.0;
		} else if (size.equals("2")) {
			pizzaID += "2";
			total = 10.0;
		} else {
			pizzaID += "3";
			total = 12.0;
		}
		
		String crust = request.getParameter("crust");
		if (crust.equals("1")) {
			pizzaID += "1";
		} else {
			pizzaID += "2";
		}
		
		String[] ingredients = {request.getParameter("blackOlives"), request.getParameter("extraCheese"), request.getParameter("greenPeppers"), 
				request.getParameter("ham"), request.getParameter("mushrooms"), request.getParameter("onions"), 
				request.getParameter("pepperoni")};
		
		for (int i = 0; i < ingredients.length; i++) {
			if (ingredients[i] != null && ingredients[i].equals("checked")) {
				pizzaID += "1";
				total += 0.50; 
			} else {
				pizzaID += "0";
			}
		}
		
		int qty = Integer.parseInt(request.getParameter("quantity"));
		System.out.println(qty + " " + total);
		
		
		return new Pizza(pizzaID, qty, (float)total);
	}
	
	private void addDrinkToTransaction (int drink_id, int qty, int trans_id) {
		Connection conn;
		String query = "";
		ResultSet resultSet;
		
		try {
			conn = DriverManager.getConnection("jdbc:oracle:thin:@ginger.umd.edu:1521:dbclass2", "dbclass235", "O2FNJud3");
			Statement statement = conn.createStatement();
			query = "SELECT qty FROM trans_d WHERE trans_id = " + trans_id + " AND drink_id = " + drink_id;
			System.out.println(query);
			resultSet = statement.executeQuery(query);
			if (resultSet.next()) {
				int newQuantity = qty + resultSet.getInt(1);
				query = "UPDATE trans_d SET qty = " + newQuantity + " WHERE trans_id = " + trans_id + 
						" AND drink_id = " + drink_id;
				System.out.println(query);
				statement.executeQuery(query); 
			} else {
				query = "INSERT INTO trans_d (trans_id, drink_id, qty) VALUES (" + trans_id + ", " + drink_id +
						", " + qty + ")";
				System.out.println(query);
				statement.executeQuery(query);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void addDrinkToOrder (HttpServletRequest request) {
		HttpSession session = request.getSession(true);
		GlobalUser user = (GlobalUser)(session.getAttribute("user"));
		Connection conn;
		int trans_id = -1;
		
		 try {
				conn = DriverManager.getConnection("jdbc:oracle:thin:@ginger.umd.edu:1521:dbclass2", "dbclass235", "O2FNJud3");
				Statement statement = conn.createStatement();
				String query = "SELECT trans_id, total FROM transactions WHERE email = '" + user.email + "' AND status = 'U'";
				System.out.println(query);
				ResultSet resultSet = statement.executeQuery(query);
				if (resultSet.next()) { //Existing transaction
					trans_id = resultSet.getInt(1);
					
					
				} else { //New transaction
					query = "INSERT INTO transactions (trans_id, email) VALUES (seq_trans.nextval, '" + user.email + "')";
					System.out.println(query);
					statement.executeQuery(query);
					
					query = "SELECT trans_id, total FROM transactions WHERE email = '" + user.email + "' AND status = 'U'";
					System.out.println(query);
					resultSet = statement.executeQuery(query);
					if (resultSet.next()) {
						trans_id = resultSet.getInt(1);
					} else {
						System.out.println("Transaction not added! Error somewhere");
					}
				}
				
				String[] drinksArray = {"Coke", "Diet Coke", "Mello Yellow", "Sprite", "Dr. Pepper", "Dasani Water", "Corona", "Bud Light",
						"Budweiser", "Peroni", "Yuengling"
				};
				
				for (int i = 0; i < drinksArray.length; i++) {
					int currQty = Integer.parseInt(request.getParameter(drinksArray[i]));
					if (currQty > 0) {
						addDrinkToTransaction(i+1, currQty, trans_id);
					}
				}
								
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}
	
	private void addPizzaToOrder (HttpServletRequest request) {
		HttpSession session = request.getSession(true);
		GlobalUser user = (GlobalUser)(session.getAttribute("user"));
		Connection conn;
		
		Pizza pizzaAdded = generatePizza(request);
		
		System.out.println("ADD PIZZA TO ORDER");
		
	    try {
			conn = DriverManager.getConnection("jdbc:oracle:thin:@ginger.umd.edu:1521:dbclass2", "dbclass235", "O2FNJud3");
			Statement statement = conn.createStatement();
			String query = "SELECT trans_id, total FROM transactions WHERE email = '" + user.email + "' AND status = 'U'";
			System.out.println(query);
			ResultSet resultSet = statement.executeQuery(query);
			if (resultSet.next()) { //Existing transaction
				int trans_id = resultSet.getInt(1);
				float total = resultSet.getFloat(2);
				//Check if pizza has already been ordered and add to quantity 
				query = "SELECT qty FROM trans_p WHERE trans_id = " + trans_id + " AND pizza_id = " + pizzaAdded.pizza_id;
				System.out.println(query);
				resultSet = statement.executeQuery(query);
				if (resultSet.next()) {
					int newQuantity = pizzaAdded.qty + resultSet.getInt(1);
					query = "UPDATE trans_p SET qty = " + newQuantity + " WHERE trans_id = " + trans_id + 
							" AND pizza_id = " + pizzaAdded.pizza_id;
					System.out.println(query);
					statement.executeQuery(query); 
				} else {
					
					if (!(statement.executeQuery("SELECT * FROM pizzas WHERE pizza_id = " + pizzaAdded.pizza_id)).next()) {
						query = "INSERT INTO pizzas (pizza_id) VALUES (" + pizzaAdded.pizza_id + ")";
						System.out.println(query);
						statement.executeQuery(query);
						query = "INSERT INTO prices (item_id, type, reg_price) VALUES (" + pizzaAdded.pizza_id + ", 'p', " + 
						(pizzaAdded.price / pizzaAdded.qty) + ")";
						statement.executeQuery(query);
					}
					query = "INSERT INTO trans_p (trans_id, pizza_id, qty) VALUES (" + trans_id + ", " + pizzaAdded.pizza_id +
							", " + pizzaAdded.qty + ")";
					System.out.println(query);
					statement.executeQuery(query);
				}
				float newTotal = total + pizzaAdded.price;
				query = "UPDATE transactions SET total = " + newTotal + " WHERE trans_id = " + trans_id;
				System.out.println(query);
				statement.executeQuery(query);
			} else { //New transaction
				query = "INSERT INTO transactions (trans_id, email, total) VALUES (seq_trans.nextval, '" + user.email +
						"', " + pizzaAdded.price + ")";
				System.out.println(query);
				statement.executeQuery(query);
				
				query = "SELECT trans_id, total FROM transactions WHERE email = '" + user.email + "' AND status = 'U'";
				System.out.println(query);
				resultSet = statement.executeQuery(query);
				if (resultSet.next()) {
					int trans_id = resultSet.getInt(1);
					
					if (!(statement.executeQuery("SELECT * FROM pizzas WHERE pizza_id = " + pizzaAdded.pizza_id)).next()) {
						query = "INSERT INTO pizzas (pizza_id) VALUES (" + pizzaAdded.pizza_id + ")";
						System.out.println(query);
						statement.executeQuery(query);
						query = "INSERT INTO prices (item_id, type, reg_price) VALUES (" + pizzaAdded.pizza_id + ", 'p', " + 
						(pizzaAdded.price / pizzaAdded.qty) + ")";
						statement.executeQuery(query);
					}
					
					query = "INSERT INTO trans_p (trans_id, pizza_id, qty) VALUES (" + trans_id + ", " + pizzaAdded.pizza_id +
							", " + pizzaAdded.qty + ")";
					System.out.println(query);
					statement.executeQuery(query);
				} else {
					System.out.println("Transaction not added! Error somewhere");
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	}
	
	private void removePizzaFromOrder (HttpServletRequest request) {
		HttpSession session = request.getSession(true);
		GlobalUser user = (GlobalUser)(session.getAttribute("user"));
		Connection conn;
		
		String pizza_id = (String)request.getParameter("pizza_id");
		
		try {
			conn = DriverManager.getConnection("jdbc:oracle:thin:@ginger.umd.edu:1521:dbclass2", "dbclass235", "O2FNJud3");
			Statement statement = conn.createStatement();
			String query = "SELECT trans_id FROM transactions WHERE email = '" + user.email + "' AND status = 'U'";
			System.out.println(query);
			ResultSet resultSet = statement.executeQuery(query);
			if (resultSet.next()) { //Existing transaction - should always be
				int trans_id = resultSet.getInt(1);
				query = "DELETE FROM trans_p WHERE trans_id = " + trans_id + " AND pizza_id = " + pizza_id;
				System.out.println(query);
				statement.executeQuery(query);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void removeDrinkFromOrder (HttpServletRequest request) {
		HttpSession session = request.getSession(true);
		GlobalUser user = (GlobalUser)(session.getAttribute("user"));
		Connection conn;
		
		String drink_id = (String)request.getParameter("drink_id");
		
		try {
			conn = DriverManager.getConnection("jdbc:oracle:thin:@ginger.umd.edu:1521:dbclass2", "dbclass235", "O2FNJud3");
			Statement statement = conn.createStatement();
			String query = "SELECT trans_id FROM transactions WHERE email = '" + user.email + "' AND status = 'U'";
			System.out.println(query);
			ResultSet resultSet = statement.executeQuery(query);
			if (resultSet.next()) { //Existing transaction - should always be
				int trans_id = resultSet.getInt(1);
				query = "DELETE FROM trans_d WHERE trans_id = " + trans_id + " AND drink_id = " + drink_id;
				System.out.println(query);
				statement.executeQuery(query);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		HttpSession session = request.getSession(true);
		PrintWriter pw = response.getWriter();
		Connection conn;
		ResultSet resultSet;
		String writeStr = "";
		String query = "";
		
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
		    GlobalUser user = (GlobalUser)(session.getAttribute("user"));
		    if (user == null) { // User null - get user info
		    	try {
				    // Connect to the Database
				    conn = DriverManager.getConnection("jdbc:oracle:thin:@ginger.umd.edu:1521:dbclass2", "dbclass235", "O2FNJud3");
				    statement = conn.createStatement();
				    query = "SELECT email, first_name, last_name, address from customers where email = '" + session.getAttribute("email") + "'";
				    System.out.println(query);
				    resultSet = statement.executeQuery(query);
				    if (resultSet.next())
				    {
					    user = new GlobalUser(resultSet.getString(1), resultSet.getString(2), 
					    		resultSet.getString(3), resultSet.getString(4));

					    session.setAttribute("user", user);
					    session.setAttribute("email", user.email);
					} 
				} catch (Exception e) {
					
				}
		    }
		    
		    //If request's size is set then a pizza has been ordered and we need to add the pizza to the order
			if (request.getParameter("size") != null && !(request.getParameter("size")).equals("")) {
				addPizzaToOrder(request);
			}
			
			//If request's coke is set then drink(s) has been ordered and we need to add them
			if (request.getParameter("Coke") != null && !(request.getParameter("Coke")).equals("")) {
				addDrinkToOrder(request);
			}
			
			//If request's pizza_id is set then remove pizza
			if (request.getParameter("pizza_id") != null && !(request.getParameter("pizza_id")).equals("")) {
				removePizzaFromOrder(request);
			}
		    
			//If request's drink_id is set then remove drink
			if (request.getParameter("drink_id") != null && !(request.getParameter("drink_id")).equals("")) {
				removeDrinkFromOrder(request);
			}
			
		    resultSet = statement.executeQuery("SELECT trans_id FROM transactions WHERE email = '" + user.email + "' AND status = 'U'");
		    double totalPrice = 0.0;

		    if (resultSet.next()) // Unordered transaction currently in system
		    {
		    	int trans_id = resultSet.getInt(1);
			    //Find pizzas associated with order
		    	query = "SELECT tp.pizza_id, tp.qty, pr.reg_price FROM trans_p tp, prices pr WHERE tp.trans_id = " + trans_id + 
		    			"  AND pr.item_id = tp.pizza_id AND pr.type = 'p' ORDER BY tp.qty DESC, pr.reg_price DESC";
		    	System.out.println(query);
		    	ResultSet pizzas = statement.executeQuery(query);
		    	
		    	ArrayList<Pizza> orderedPizzas = new ArrayList<Pizza>();
		    	
		    	writeStr += "<h1>Pizzas in Order</h1>";
		    	if (pizzas.next()) {
		    		String pizza_id; 
		    		int qty;
		    		float reg_price;
		    		
		    		 do
		    		 	{
					    	pizza_id = pizzas.getString(1);
					    	qty = pizzas.getInt(2);
					    	reg_price = pizzas.getFloat(3);
					    	
					    	orderedPizzas.add(new Pizza(pizza_id, qty, reg_price));
					    } while(pizzas.next());
		    		 

		    		 Pizza currPizza;	
		    		 for (int i = 0; i < orderedPizzas.size(); i++) {
		    			 currPizza = orderedPizzas.get(i);
		    			 String pizzaWord = "pizza";
		    			 if (currPizza.qty > 1) {
		    				 pizzaWord = "pizzas";
		    			 }
		    			 writeStr += "<div id='row'><form action='Order' method='post'>" +
		    					 	 "<label for='pizza_id'>";
		    			 writeStr += "<b>" + currPizza.qty + "x</b>   " + currPizza.size + " " + currPizza.crust + " crust " + pizzaWord +" with " + currPizza.ingredients + "   ";
		    			 writeStr += "<b>$" + Math.round(currPizza.price * 100.0)/100.0 + "</b></label>" +
		    			 				"<input type='hidden' name='pizza_id' value='" + currPizza.pizza_id + "' /> " +
		    			 				"<input type='submit' value='Remove'></form></div>";
		    			 totalPrice += currPizza.price;
		    		 }
		    		 
		    	} else { //No pizzas currently in transaction
		    		writeStr += "<div id='row'>No pizzas currently in order</div>";
		    	}
		    	
		    	writeStr += "<br /><form action='OrderPizza' method='post'><input type='submit' value='Add Pizza'></form>";
		    	
		    	//Find drinks associated with order
		    	query = "SELECT d.supplier, td.drink_id, td.qty, p.reg_price FROM trans_d td, prices p, drinks d WHERE td.trans_id = " + trans_id + 
		    			" AND p.item_id = td.drink_id AND d.drink_id = td.drink_id ORDER BY td.qty DESC, p.reg_price desc";
		    	System.out.println(query);
		    	ResultSet drinks = statement.executeQuery(query);
		    	
		    	writeStr += "<h1>Drinks in Order</h1>";
		    	if (drinks.next()) {
		    		String drink_name;
		    		int drink_id;
		    		int qty;
		    		float reg_price;
		    		
		    		do
		    			{
		    			drink_name = drinks.getString(1);
		    			drink_id = drinks.getInt(2);
		    			qty = drinks.getInt(3);
		    			reg_price = drinks.getFloat(4);
		    			
		    			float drinks_price = (reg_price * (float)qty);
		    			
		    			writeStr += "<div id='row'><form action='Order' method='post'>" +
		    						"<label for='drink_id'>";
		    			writeStr += "<b>" + qty + "x</b>   " + drink_name + "   ";
		    			writeStr += "<b>$" + drinks_price + "</b></label>" +
		    						"<input type='hidden' name='drink_id' value='" + drink_id + "' /> " + 
		    						"<input type='submit' value='Remove'></form></div>";
		    			totalPrice += drinks_price;
		    			
		    			} while (drinks.next());
		    		
		    	} else {
		    		writeStr += "<div id='row'>No drinks currently in order</div>";
		    	}
		    	
		    	writeStr += "<br /><form action='OrderDrink' method='post'><input type='submit' value='Add Drink'></form>";
		    	
			} else { // New Transaction		
					writeStr +=	"<h1>Pizzas in Order</h1>" +
							"<div id 'row'> No pizzas currently in order" +
							"<form action='OrderPizza' method='post'><input type='submit' value='Add Pizza'></form></div>" +
							"<h1>Drinks in Order</h1>" +
							"<div id 'row'>No drinks currently in order" +
							"<form action='OrderDrink' method='post'><input type='submit' value='Add Drink'></form></div>";
							
		    }
		    System.out.println("reached");
		    writeStr += "<br /><br /><div id='row'><b>Total Price: </b>$" + totalPrice + "</div>";
		    
		    writeStr += "<form action='index.jsp'><input type='submit' value='Home'></form></center>";
		    
		}
		catch(SQLException e) {
		    System.out.println("An error occured.");
		    System.out.println(e.toString());
		}
		
		
		writePage(writeStr, pw);
	}

}
