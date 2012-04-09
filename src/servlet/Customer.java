package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import servlet.GlobalUser;

/**
 * Servlet implementation class Customer
 */
@WebServlet("/Customer")
public class Customer extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Customer() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter pw = response.getWriter();
		HttpSession session = request.getSession(true);
		
		GlobalUser user = ((GlobalUser)session.getAttribute("user"));
		
		//If the user has not been created or is not currently logged in
		pw.print("<html>" +
				 "<head>" +
				 	"<link rel='stylesheet' type='text/css' href='mainStyle.css' />" +
				 	"<meta http-equiv='Content-Type' content='text/html; charset=ISO-8859-1'>"+
				 "</head"+
				 "<body>"+
				 "<div class='container'>"+
					"<div class='center'>");
		if (user == null) {
			pw.print(	"<h1>Login or Register </h1>" +
							"<div id='inner'>" +								
							   "<div id='inner_text'>" +
								     "<form id='login' action='Login' method='post' accept-charset='UTF-8'>" +
								     	"<fieldset >" + 
								     		"<legend>Login</legend>" + 
								     		"<input type='hidden' name='submitted' id='submitted' value='1'/>" + 
								     		"<label for='email' >Email:</label><input type='text' name='email' id='email'  maxlength=\"25\" /><br>" +
								     		"<label for='password' >Password:</label><input type='password' name='password' id='password' maxlength=\"25\" />" + 
								     			"<br />" + 
								     		"<input type='submit' name='submit' value='Login' />" +
								     	"</fieldset>" +
									 "</form>");
		} else {
//			pw.print("User: " + user.email + " is already logged in.");
			//This redirection is just temporary. If user is already logged in--this should redirect them to the order online page.
			ServletContext sc = this.getServletContext();
			RequestDispatcher rd = sc.getRequestDispatcher("/menu.jsp"); 
			rd.forward(request, response);
		}
		
		pw.println("<form id='post' action='Register' method='post'>" +
				 		"<input type='submit' name='register' value='Register' /> " +
				 	"</form>"+
				 	"<form action='index.jsp'>" +
				 		"<input type='submit' name='submit' value='Home' />" +
				 	"</form>" + 
								"</div>"+
							   "</div>" +
							"</div>" + 
						   "</div"+
						  "</body></html>");
	}

	
}
