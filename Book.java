/* A book IS A product that has additional information - e.g. title, author

 	 A book also comes in different formats ("Paperback", "Hardcover", "EBook")

 	 The format is specified as a specific "stock type" in get/set/reduce stockCount methods.

 */
public class Book extends Product implements Comparable<Book>
{
	private String author;
	private String title;
	private int year;

	// Stock related information NOTE: inherited stockCount variable is used for EBooks
	int paperbackStock;
	int hardcoverStock;

	public Book(String name, String id, double price, int paperbackStock, int hardcoverStock, String title, String author, int year)
	{
		super(name, id, price, 100000, Product.Category.BOOKS);
		this.title = title;
		this.author = author;
		this.year =  year;
		this.paperbackStock = paperbackStock;
		this.hardcoverStock = hardcoverStock;
	}

	public String getAuthor()
	{
		return author;
	}

	// Check if a valid format  
	public boolean validOptions(String productOptions)
	{
		return productOptions.equalsIgnoreCase("PaperBack") || productOptions.equals("EBook") || productOptions.equals("Hardcover");
	}

	// Override getStockCount() in super class.
	// This method assumes validOptions() has been called
	public int getStockCount(String productOptions)
	{
		if (productOptions == null) return super.getStockCount(productOptions);

		if (productOptions.equalsIgnoreCase("paperback"))
			return paperbackStock;
		else if (productOptions.equalsIgnoreCase("hardcover"))
			return hardcoverStock;
		else
			return super.getStockCount(productOptions);
	}

	public void setStockCount(int stockCount, String productOptions)
	{
		if (productOptions == null) return;

		if (productOptions.equalsIgnoreCase("Paperback"))
			paperbackStock = stockCount;
		else if (productOptions.equalsIgnoreCase("Hardcover"))
			hardcoverStock = stockCount;
		else
			super.setStockCount(stockCount, null);
	}

	/*
	 * When a book is ordered, reduce the stock count for the specific stock type
	 */
	public void reduceStockCount(String productOptions)
	{
		if (productOptions == null) super.reduceStockCount(null);

		if (productOptions.equalsIgnoreCase("Paperback"))
			paperbackStock--;
		else if (productOptions.equalsIgnoreCase("Hardcover"))
			hardcoverStock--;
		else
			super.reduceStockCount(null);
	}
	/*
	 * Print product information in super class and append Book specific information title and author
	 */
	public void print()
	{
		super.print(); 
		System.out.print(" Book Title: " + title +  " Author: " + author);
	}

	public int compareTo(Book otherBook)
	{
		return this.year - otherBook.year;
	}
}
