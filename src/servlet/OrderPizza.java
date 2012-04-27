package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class OrderPizza
 */
@WebServlet("/OrderPizza")
public class OrderPizza extends HttpServlet {
	private String total = "10.00";
	
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public OrderPizza() {
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
		System.out.println("DO POST PIZZA");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter pw = response.getWriter();

		String pgWrite = "<h1>Order Pizza</h1>";
		pgWrite += "<script type=\"text/javascript\">" +
					"function changeSize(rdio, size) {" +
					"var curr;" +
					"if (size == 1) { curr = 8.00; }" +
					"else if (size == 2) { curr = 10.00; }" +
					"else { curr = 12.00; }" +
					"document.getElementById('sizeVar').value = curr;" + 
					"var ingrPrice = parseFloat(document.getElementById('ingredientVar').value);" +
					"var quantity = parseFloat(document.getElementById('quantity').value);" + 
					"document.getElementById('totalID').innerHTML = ((curr + ingrPrice) * quantity).toFixed(2);" +
					"}" + 
					"function changeTotal(chkbox) {" +
					"var curr = parseFloat(document.getElementById('ingredientVar').value);" +
					"if (chkbox.checked) { curr += .50; }" +
					"else { curr -= .50; }" +
					"document.getElementById('ingredientVar').value = curr;" + 
					"var sizePrice = parseFloat(document.getElementById('sizeVar').value);" +
					"var quantity = parseFloat(document.getElementById('quantity').value);" + 
					"document.getElementById('totalID').innerHTML = ((curr + sizePrice) * quantity).toFixed(2);" +
					"} " +
					"function changeQty() {" +
					"var sizePrice = parseFloat(document.getElementById('sizeVar').value);" +
					"var ingrPrice = parseFloat(document.getElementById('ingredientVar').value);" +
					"var quantity = parseFloat(document.getElementById('quantity').value);" + 
					"document.getElementById('totalID').innerHTML = ((ingrPrice + sizePrice) * quantity).toFixed(2);" +
					"} </script>";
		
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
			
		    //Size Selection
			pgWrite +=  "<div id='row'><form action='Order' method='post'>" +
						"<div id='row'><label for='size' >Size: </label>" +
						"<input type='radio' name='size' value='1' onClick='changeSize(this, 1)' />Small" +
						"<input type='radio' name='size' value='2' checked='checked' onClick='changeSize(this, 2)' />Medium" +
						"<input type='radio' name='size' value='3' onClick='changeSize(this, 3)' />Large </div>" +
						
						//Crust Selection
						"<div id='row'><label for='crust' >Crust: </label>" +
						"<input type='radio' name='crust' value='1'  checked='checked'/>Normal" +
						"<input type='radio' name='crust' value='2'/>Thin </div>" +  
						
						//Ingredients
						"<div id='row'><label for 'ingredients' >Ingredients: </label>" + 
						"<input type='checkbox' name='blackOlives' value='checked' onChange=\"changeTotal(this)\" />Black Olives" + 
						"<input type='checkbox' name='extraCheese' value='checked' onChange=\"changeTotal(this)\" />Extra Cheese" + 
						"<input type='checkbox' name='greenPeppers' value='checked' onChange=\"changeTotal(this)\" />Green Peppers" + 
						"<input type='checkbox' name='ham' value='checked' onChange=\"changeTotal(this)\" />Ham" + 
						"<input type='checkbox' name='mushrooms' value='checked' onChange=\"changeTotal(this)\" />Mushrooms" + 
						"<input type='checkbox' name='onions' value='checked' onChange=\"changeTotal(this)\" />Onions" + 
						"<input type='checkbox' name='pepperoni' value='checked' onChange=\"changeTotal(this)\" />Pepperoni" + 
						
						//Quantity
						"<div id='row'><label for 'quantity' >Quantity: </label>" + 
						"<select id='quantity' name='quantity' onchange='changeQty()'> <option value='1'>1</option><option value='2'>2</option><option value='3'>3</option>" +
						"<option value='4'>4</option><option value='5'>5</option><option value='6'>6</option>" + 
						"<option value='7'>7</option><option value='8'>8</option><option value='9'>9</option></select></div>";

						//Total 
			pgWrite += "<div id='row'><b>Cost of Pizza: <font id='totalID'>" + total + "</font></b>" +
					   "<input type='hidden' id='ingredientVar' value='0.00' />" + 
					   "<input type='hidden' id='sizeVar' value='10.00' />";
			
			pgWrite += "<div id='row'><input type='submit' value='Add Pizza to Order'></form>" +
					   "<form action='Order' method='post'><input type='submit' value='Back to Order'></form></div>";
			
		} catch (Exception e) {
			
		}
		System.out.println("ORDER PIZZA");
		writePage(pgWrite, pw);
	}

}
