package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class adminLogin
 */
@WebServlet("/adminLogin")
public class adminLogin extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public adminLogin() {
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
		String id = request.getParameter("adminId");
		String pwd = request.getParameter("adminPwd");
		PrintWriter pw = response.getWriter();
		String pg = "";

		if (id.equals("adminUser") && pwd.equals("BrendanHeather"))
		{
			//successful Login--set session variables
			session.setAttribute("adminName", id);
			session.setAttribute("adminPwd", pwd);
			
			pg += "<div class='register_box'>";
			pg += "<form id='adminButtons' action='Admin' method='post' accept-charset='UTF-8'>"+
					 "<center>"+
						"<div id='row'><input type='submit' name='custHistory' value='Customer History' /></div>"+
						"<div id='row'><input type='submit' name='revenue' value='Time-Window Revenue' /></div>"+
						"<div id='row'><input type='submit' name='HHreport' value='Happy Hour Analysis' /></div>"+
						"<div id='row'><input type='submit' name='prefCustReport' value='Preferred Customers' /></div>"+
						"<div id='row'><input type='submit' name='inactiveCusts' value='Inactive Customers Report' /></div>"+
					 "</center></form>";
			pg += "</div><form id='logout' action='Admin' method='post' accept-charset='UTF-8'>" +
				     "<center><div id='row'><input type='submit' name='adminLogout' value='Log out' /></center>"+
					"</form>";
		}
		else
		{
			//incorrect login credentials--try again
			pg += "<div class='register_box'><div id='panel'>";
			pg += "<div id='err'> Incorrect login credentials</div>";
			pg += "<form id='adminLogin' action='adminLogin' method='post' accept-charset='UTF-8'>" +
					"<fieldset >" +
					"<legend>Administrator Login</legend>" + 
					"<input type='hidden' name='submitted' id='submitted' value='1'/>" +
					"<div id='row'><label for='adminId' >Login ID: </label>" +
					"<input type='text' name='adminID' id='adminID' maxlength=\"25\" /></div>" +
					"<div id='row'><label for='adminPwd' >Password: </label>" +
					"<input type='password' name='adminPwd' id='adminPwd' maxlength=\"25\" /></div>" +
					"<center><div id='row'><input type='submit' name='adminSubmit' value='Log on' /></div></center>" +
					"</fieldset>" +
					"</form></div></div>";
		}
		writePage(pg, pw);
	}

}
