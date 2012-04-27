package servlet;

public class Pizza {
	public String pizza_id;
	public int qty;
	public float price;
	
	public String crust;
	public String size;
	
	public String ingredients;

	private String[] ingredient_list = {"black olives", "extra cheese", "green peppers", "ham", "mushrooms", "onions", "pepperoni", "sausage"}; 
	
	Pizza (String pizza_id, int qty, float price) {
		this.pizza_id = pizza_id;
		this.qty = qty;
		this.price = price * this.qty;
		
		System.out.println(this.price);
		
		parseId();
	}

	private void parseId () {
		//Determine Size
		char sizeChar = pizza_id.charAt(0);
		if (sizeChar == '1') {
			size = "Small";
		} else if (sizeChar == '2') {
			size = "Medium";
		} else {
			size = "Large";
		}
		
		if (pizza_id.charAt(1) == '1') {
			crust = "normal";
		} else {
			crust = "thin";
		}
		
		boolean first = true;
		ingredients = "no toppings";
		for (int i = 2; i < pizza_id.length(); i++) {
			if (pizza_id.charAt(i) == '1') {
				if (first) {
					ingredients = ingredient_list[i-2];
					first = false;
				} else 
					ingredients += ", " + ingredient_list[i-2];
			}
		}
	}
	
}