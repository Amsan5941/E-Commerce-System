import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;
import java.util.Scanner;

import javax.naming.InvalidNameException;

/*
 * Models a simple ECommerce system. Keeps track of products for sale, registered customers, product orders and
 * orders that have been shipped to a customer
 */
public class ECommerceSystem  
{
	ArrayList<Product>  products = new ArrayList<Product>();
	ArrayList<Customer> customers = new ArrayList<Customer>();	

	ArrayList<ProductOrder> orders   			= new ArrayList<ProductOrder>();
	ArrayList<ProductOrder> shippedOrders = new ArrayList<ProductOrder>();

	// These variables are used to generate order numbers, customer id's, product id's 
	int orderNumber = 500;
	int customerId = 900;
	int productId = 700;

	// General variable used to store an error message when something is invalid (e.g. customer id does not exist)  
	String errMsg = null;

	// Random number generator
	Random random = new Random();

	public ECommerceSystem()
	{
		// NOTE: do not modify or add to these objects!! - the TAs will use for testing
		// If you do the class Shoes bonus, you may add shoe products

		// Create some products
		//Adds the products from the text file products to the arraylist products

		try{
			File file=new File("products.txt");
			Scanner in = new Scanner(file);
			while(in.hasNextLine()){
				String category=in.nextLine().replace("\n","");
				if(category.isEmpty()|| category.isBlank()){
					continue;
				}
				Product.Category prodType=Product.Category.valueOf(category);
				String itemName=in.nextLine().replace("\n","");
				float itemPrice=Float.parseFloat(in.nextLine().replace("\n",""));
				if(prodType==Product.Category.BOOKS){
					String[] stocks=in.nextLine().replace("\n","").split(" ");
					int PaperBackStock= Integer.parseInt(stocks[0]);
					int HardCoverStock=Integer.parseInt(stocks[1]);
					String[] SplitInfo=in.nextLine().replace("\n","").split(":");
					String title=SplitInfo[0];
					String author=SplitInfo[1];
					int year=Integer.parseInt(SplitInfo[2]);

					products.add(new Book(itemName, generateProductId(), itemPrice, PaperBackStock, HardCoverStock, title, author, year));
				}
				else{
					int amount=Integer.parseInt(in.nextLine().replace("\n",""));
					products.add(new Product(itemName,generateProductId(), itemPrice, amount, prodType));
				}

			}
		}
		catch(FileNotFoundException e){
			System.out.println("No file was found");
		}
		

		// products.add(new Product("Acer Laptop", generateProductId(), 989.0, 99, Product.Category.COMPUTERS));
		// products.add(new Product("Apex Desk", generateProductId(), 1378.0, 12, Product.Category.FURNITURE));
		// products.add(new Book("Book", generateProductId(), 45.0, 4, 2, "Ahm Gonna Make You Learn", "T. McInerney", 2021));
		// products.add(new Product("DadBod Jeans", generateProductId(), 24.0, 50, Product.Category.CLOTHING));
		// products.add(new Product("Polo High Socks", generateProductId(), 5.0, 199, Product.Category.CLOTHING));
		// products.add(new Product("Tightie Whities", generateProductId(), 15.0, 99, Product.Category.CLOTHING));
		// products.add(new Book("Book", generateProductId(), 35.0, 4, 2, "How to Fool Your Prof", "D. Umbast", 1997));
		// products.add(new Book("Book", generateProductId(), 45.0, 4, 2, "How to Escape from Prison", "A. Fugitive", 1963));
		// products.add(new Book("Book", generateProductId(), 45.0, 4, 2, "How to Teach Programming", "T. McInerney", 2001));
		// products.add(new Product("Rock Hammer", generateProductId(), 10.0, 22, Product.Category.GENERAL));
		// products.add(new Book("Book", generateProductId(), 45.0, 4, 2, "Ahm Gonna Make You Learn More", "T. McInerney", 2022));
		// int[][] stockCounts = {{4, 2}, {3, 5}, {1, 4,}, {2, 3}, {4, 2}};
		// products.add(new Shoes("Prada", generateProductId(), 595.0, stockCounts));

		// Create some customers
		customers.add(new Customer(generateCustomerId(),"Inigo Montoya", "1 SwordMaker Lane, Florin"));
		customers.add(new Customer(generateCustomerId(),"Prince Humperdinck", "The Castle, Florin"));
		customers.add(new Customer(generateCustomerId(),"Andy Dufresne", "Shawshank Prison, Maine"));
		customers.add(new Customer(generateCustomerId(),"Ferris Bueller", "4160 Country Club Drive, Long Beach"));
	}

	private String generateOrderNumber()
	{
		return "" + orderNumber++;
	}

	private String generateCustomerId()
	{
		return "" + customerId++;
	}

	private String generateProductId()
	{
		return "" + productId++;
	}

	public String getErrorMessage()
	{
		return errMsg;
	}

	public void printAllProducts()
	{
		for (Product p : products)
			p.print();
	}

	public void printAllBooks()
	{
		for (Product p : products)
		{
			if (p.getCategory() == Product.Category.BOOKS)
				p.print();
		}
	}

	public ArrayList<Book> booksByAuthor(String author)
	{
		ArrayList<Book> books = new ArrayList<Book>();
		for (Product p : products)
		{
			if (p.getCategory() == Product.Category.BOOKS)
			{
				Book book = (Book) p;
				if (book.getAuthor().equals(author))
					books.add(book);
			}
		}
		return books;
	}

	public void printAllOrders()
	{
		for (ProductOrder o : orders)
			o.print();
	}

	public void printAllShippedOrders()
	{
		for (ProductOrder o : shippedOrders)
			o.print();
	}

	public void printCustomers()
	{
		for (Customer c : customers)
			c.print();
	}
	/*
	 * Given a customer id, print all the current orders and shipped orders for them (if any)
	 */
	public boolean printOrderHistory(String customerId) throws UnknownCustomerException
	{
		// Make sure customer exists
		int index = customers.indexOf(new Customer(customerId));
		if (index == -1)
		{
			throw new UnknownCustomerException("Customer " + customerId + " Not Found");

		}	
		System.out.println("Current Orders of Customer " + customerId);
		for (ProductOrder order: orders)
		{
			if (order.getCustomer().getId().equals(customerId))
				order.print();
		}
		System.out.println("\nShipped Orders of Customer " + customerId);
		for (ProductOrder order: shippedOrders)
		{
			if (order.getCustomer().getId().equals(customerId))
				order.print();
		}
		return true;
	}

	public String orderProduct(String productId, String customerId, String productOptions) throws UnknownCustomerException, UnknownProductException, UnknownProductOptionException, OutOfStockException
	{
		// Get customer
		int index = customers.indexOf(new Customer(customerId));
		if (index == -1)
		{
			throw new UnknownCustomerException ("Customer " + customerId + " Not Found");
		}
		Customer customer = customers.get(index);

		// Get product 
		index = products.indexOf(new Product(productId));
		if (index == -1)
		{
			throw new UnknownProductException("Product " + productId + " Not Found");
		}
		Product product = products.get(index);

		// Check if the options are valid for this product (e.g. Paperback or Hardcover or EBook for Book product)
		if (!product.validOptions(productOptions))
		{
			throw new UnknownProductOptionException("Product " + product.getName() + " ProductId " + productId + " Invalid Options: " + productOptions);
		}
		// Is it in stock?
		if (product.getStockCount(productOptions) == 0)
		{
			throw new OutOfStockException("Product " + product.getName() + " ProductId " + productId + " Invalid Options: " + productOptions);
			
		}
		// Create a ProductOrder
		ProductOrder order = new ProductOrder(generateOrderNumber(), product, customer, productOptions);
		product.reduceStockCount(productOptions);

		// Add to orders and return
		orders.add(order);

		return order.getOrderNumber();
	}

	/*
	 * Create a new Customer object and add it to the list of customers
	 */

	public boolean createCustomer(String name, String address) throws CustomerNameException, CustomerAddressException
	{
		// Check to ensure name is valid
		if (name == null || name.equals(""))
		{
			throw new CustomerNameException( "Invalid Customer Name " + name);
		
		}
		// Check to ensure address is valid
		if (address == null || address.equals(""))
		{
		throw new CustomerAddressException("Invalid Customer Address " + address);
			
		}
		Customer customer = new Customer(generateCustomerId(), name, address);
		customers.add(customer);
		return true;
	}

	public ProductOrder shipOrder(String orderNumber) throws UnknownOrderNumber
	{
		// Check if order number exists
		int index = orders.indexOf(new ProductOrder(orderNumber,null,null,""));
		if (index == -1)
		{
			throw new UnknownOrderNumber("Order " + orderNumber + " Not Found");
			
		}
		ProductOrder order = orders.get(index);
		orders.remove(index);
		shippedOrders.add(order);
		return order;
	}

	/*
	 * Cancel a specific order based on order number
	 */
	public boolean cancelOrder(String orderNumber) throws UnknownOrderNumber
	{
		// Check if order number exists
		int index = orders.indexOf(new ProductOrder(orderNumber,null,null,""));
		if (index == -1)
		{
			throw new UnknownOrderNumber("Order " + orderNumber + " Not Found");
			
		}
		ProductOrder order = orders.get(index);
		orders.remove(index);
		return true;
	}

	// Sort products by increasing price
	public void sortByPrice()
	{
		Collections.sort(products, new PriceComparator());
	}

	private class PriceComparator implements Comparator<Product>
	{
		public int compare(Product a, Product b)
		{
			if (a.getPrice() > b.getPrice()) return 1;
			if (a.getPrice() < b.getPrice()) return -1;	
			return 0;
		}
	}

	// Sort products alphabetically by product name
	public void sortByName()
	{
		Collections.sort(products, new NameComparator());
	}

	private class NameComparator implements Comparator<Product>
	{
		public int compare(Product a, Product b)
		{
			return a.getName().compareTo(b.getName());
		}
	}

	// Sort products alphabetically by product name
	public void sortCustomersByName()
	{
		Collections.sort(customers);
	}

	public void addToCart(String productId, String customerId, String productOptions) throws UnknownProductException, UnknownCustomerException, UnknownProductOptionException, OutOfStockException
	{
		int index = customers.indexOf(new Customer(customerId));
		if (index == -1)
		{
			throw new UnknownCustomerException("Customer " + customerId + " Not Found");
		}
		
		// Get product 
		index = products.indexOf(new Product(productId));
		if (index == -1)
		{
			throw new UnknownProductException("Product " + productId + " Not Found");
			
	
		Product product= this.getProduct(productId);

		// Check if the options are valid for this product (e.g. Paperback or Hardcover or EBook for Book product)
		if (!product.validOptions(productOptions))
		{
			throw new UnknownProductOptionException("Product " + product.getName() + " ProductId " + productId + " Invalid Options: " + productOptions);
			
		}
		// Is it in stock?
		if (product.getStockCount(productOptions) == 0)
		{
			throw new OutOfStockException("Product " + product.getName() + " ProductId " + productId + " Out of Stock");
			
		}
		Customer customer=this.customers.get(this.customers.indexOf(new Customer(customerId)));
		customer.getCart().addItemToCart(product,productOptions);
	}
	}

	public void remCartItem(String customerId, String productId) throws UnknownCustomerException, UnknownProductException
	{
		int index = customers.indexOf(new Customer(customerId));
		if (index == -1)
		{
			throw new UnknownCustomerException("Customer " + customerId + " Not Found");
		}
		

		index = products.indexOf(new Product(productId));
		if (index == -1)
		{
			throw new UnknownProductException ("Product " + productId + " Not Found");
			
		}

		Customer customer=this.customers.get(this.customers.indexOf(new Customer(customerId)));
		customer.getCart().removeFromCart(productId);
		System.out.println("Item has been removed from cart");

	}

	public void printCart(String customerId) throws UnknownCustomerException 
	{
		int index = customers.indexOf(new Customer(customerId));
		if (index == -1)
		{
			throw new UnknownCustomerException("Customer " + customerId + " Not Found");
		}
		Customer customer=this.customers.get(this.customers.indexOf(new Customer(customerId)));
		customer.getCart().printCart();
		
	}

	public void orderItems(String CustomerID) throws UnknownCustomerException
	{
		Customer currentcust=null;
		for(int i=0; i<customers.size();i++){
			if(customers.get(i).getId().equals(CustomerID)){
				currentcust=customers.get(i);
				break;
			}
		}
		if(currentcust==null){
			throw new UnknownCustomerException("Customer"+customerId+"Not Found");
		}
		ArrayList<CartItem>c=currentcust.createCart.getCart();
		if(c.size()==0){
			System.out.println( "There are no products in the cart");
		}
		for (int i=0;i<c.size();i++){
			Product p=c.get(i).getProduct();
			String orderNumber=generateOrderNumber();
			ProductOrder cartOrder= new ProductOrder(orderNumber, p , currentcust,c.get(i).getProductOptions());
			p.reduceStockCount(c.get(i).getProductOptions());
			orders.add(cartOrder);
		}
		System.out.println( "The order has been placed");
	}
}

class UnknownCustomerException extends RuntimeException{
	public UnknownCustomerException(){}
	public UnknownCustomerException(String message){
		super(message);
	}
}

class UnknownProductException extends RuntimeException{
	public UnknownProductException(){}
	public UnknownProductException(String message){
		super(message);
	}
}

class UnknownProductOptionException extends RuntimeException{
	public UnknownProductOptionException(){}
	public UnknownProductOptionException(String message){
		super(message);
	}
}

class OutOfStockException extends RuntimeException{
	public OutOfStockException(){}
	public OutOfStockException(String message){
		super(message);
	}
}

class CustomerNameException extends RuntimeException{
	public CustomerNameException(){}
	public CustomerNameException(String message){
		super(message);
	}
}

class CustomerAddressException extends RuntimeException{
	public CustomerAddressException(){}
	public CustomerAddressException(String message){
		super(message);
	}
}

class UnknownOrderNumber extends RuntimeException{
	public UnknownOrderNumber(){}
	public UnknownOrderNumber(String message){
		super(message);
	}
}

