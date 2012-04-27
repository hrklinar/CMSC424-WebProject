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
 * Servlet implementation class OrderDrink
 */
@WebServlet("/OrderDrink")
public class OrderDrink extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private String total = "0.00";
    /**
     * @see HttpServlet#HttpServlet()
     */
    public OrderDrink() {
        super();
        // TODO Auto-generated constructor stub
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
		Connection conn;
		String pgWrite = "";
		
		//JavaScript Functions
		pgWrite += "<script type=\"text/javascript\">" +
				   "function chgTotal() { \n" + 
				   		"var cokeQty = parseInt(document.getElementById('Coke').value);\n" +
				   		"var dcQty = parseInt(document.getElementById('Diet Coke').value);\n" + 
				   		"var myQty = parseInt(document.getElementById('Mello Yellow').value);\n" + 
				   		"var spriteQty = parseInt(document.getElementById('Sprite').value);\n" +
				   		"var drQty = parseInt(document.getElementById('Dr. Pepper').value);\n" +
				   		"var dwQty = parseInt(document.getElementById('Dasani Water').value);\n" + 
				   		"var coronaQty = parseInt(document.getElementById('Corona').value);\n" + 
				   		"var blQty = parseInt(document.getElementById('Bud Light').value);\n" +
				   		"var budweiserQty = parseInt(document.getElementById('Budweiser').value);\n" + 
				   		"var peroniQty = parseInt(document.getElementById('Peroni').value);\n" + 
				   		"var yuQty = parseInt(document.getElementById('Yuengling').value);\n" +
				   		"var total = (cokeQty + dcQty + myQty + spriteQty + drQty) * 3.0 + dwQty*2.5 + " +
				   		"(coronaQty + blQty + budweiserQty + peroniQty + yuQty) * 5.0;\n" + 
				   		"document.getElementById('totalID').innerHTML = total.toFixed(2);\n" +
						"} </script>";
				   		
				   
		
		pgWrite += "<h1>Order Drinks</h1>";

		
		
		
		try {
			conn = DriverManager.getConnection("jdbc:oracle:thin:@ginger.umd.edu:1521:dbclass2", "dbclass235", "O2FNJud3");
			Statement statement = conn.createStatement();
		
			String query = "SELECT d.supplier, d.description, p.reg_price FROM drinks d, prices p WHERE d.drink_id = p.item_id ORDER by d.drink_id";
			System.out.println(query);
			ResultSet drinks = statement.executeQuery(query);
			
			if (drinks.next()) { //Found drinks
				
				pgWrite += "<div id='row'><form action='Order' method='post'>";
				pgWrite += "<h2>Soda</h2><center>";
				do
			    {
					String drink = drinks.getString(1).trim();
					String description = drinks.getString(2).trim();
					double price = drinks.getDouble(3);
					
					if (drink.equals("Dasani Water")) {
						pgWrite += "<br /><h2>Water</h2>";
					} else if (drink.equals("Corona")) {
						pgWrite += "<br /><h2>Beer</h2>";
					}
					
    			 	pgWrite += "<div id='row'><label for '" + drink +"' >" + drink + " (" + description + ") <b> $" + price + "0</b> </label>" + 
    			 				"<select id='" + drink +"' name='" + drink +"' onchange='chgTotal()'> <option value = '0'>0</option><option value='1'>1</option> +" +
    			 				"<option value='2'>2</option><option value='3'>3</option>" +
    			 				"<option value='4'>4</option><option value='5'>5</option><option value='6'>6</option>" + 
    			 				"<option value='7'>7</option><option value='8'>8</option><option value='9'>9</option></select></div>\n";
					
			    } while(drinks.next());
				
				pgWrite += "<div id='row'><b>Total Cost: <font id='totalID'>" + total + "</font></b>";
				
				pgWrite += "<div id='row'><input type='submit' value='Add Drink(s) to Order'></form>" +
						   "<form action='Order' method='post'><input type='submit' value='Back to Order'></form></div></center>";
				
			} else {
				System.out.println("No drinks found");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	
		writePage(pgWrite, pw);
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
}
