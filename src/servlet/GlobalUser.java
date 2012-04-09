package servlet;

public class GlobalUser {
	public String email = "";
	public String first_name = "";
	public String last_name = "";
	public String address = "";
	
	public boolean logged_in = false;

	GlobalUser () {
		
	}
	
	GlobalUser (String email, String first_name, String last_name, String address) {
		this.email = email;
		this.first_name = first_name;
		this.last_name = last_name;
		this.address = address;
		
		logged_in = true;
	}
	
	public void login(String email, String first_name, String last_name) {
		this.email = email;
		this.first_name = first_name;
		this.last_name = last_name;
	}
	
	public void logout() {
		email = "";
		first_name = "";
		last_name = "";
	}
	
	
}
