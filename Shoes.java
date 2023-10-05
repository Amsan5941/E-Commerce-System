import java.util.Scanner;

/*
 * A pair of Shoes IS A Product. 
 * 
 * For product options, shoes come in several sizes 6 7 8 9 10 11 12 and colors: Black Brown 
 * 
 * You must override the get/set/reduce stockCount methods in super class Product using the product options string
 */
public class Shoes extends Product
{
	public static enum Color {BLACK, BROWN};
	public static enum Size  {SIX, SEVEN, EIGHT, NINE, TEN};
	
	public static int sizes = 5;
	public static int colors = 2;
	private static final int SIZEOFFSET = 6; 
 
  // Stock related information NOTE: inherited stockCount variable is unused
	private int[][] stockCounts = new int[sizes][colors];
  
	
	private class SizeColor
	{
		public int size;
		public int color;
		
		public SizeColor(int size, int color)
		{
			this.size = size;
			this.color = color;
		}
	}
	
  public Shoes(String name, String id, double price, int[][] stockCounts)
  {
  	 super(name, id, price, 100000, Product.Category.SHOES);
  	 this.stockCounts = stockCounts;
  }
    
  // Check if a valid format  
  public boolean validOptions(String productOptions)
  {
  	if (productOptions == null || productOptions.equals(""))
  		return false;
  	Scanner options = new Scanner(productOptions);
  	if (!options.hasNext()) return false;
  	String size  = options.next();
  	if (!options.hasNext()) return false;
  	String color = options.next();
  	if (color == null) return false;
  	return (size.equalsIgnoreCase("6") || size.equals("7") || size.equals("8") || size.equals("9") || size.equals("10")) &&
  			   (color.equalsIgnoreCase("Black") || color.equalsIgnoreCase("Brown"));
  }
  
  private SizeColor getSizeColor(String productOptions)
  {
  	Scanner options = new Scanner(productOptions);
  	String sizeString  = options.next();
  	String colorString = options.next();
  	int size = Integer.parseInt(sizeString) - SIZEOFFSET;
  	int color;
  	if (colorString.equals("Black")) 
  		color = 0;
  	else
  		color = 1;
  	return new SizeColor(size,color);
  }
  // Override getStockCount() in super class.
  // NOTE: assumes validOptions() has already been called for productOptions
  public int getStockCount(String productOptions)
	{
  	SizeColor sc = getSizeColor(productOptions);
  	
  	return stockCounts[sc.size][sc.color];
	}
  
  // NOTE: assumes validOptions() has already been called for productOptions
  public void setStockCount(int stockCount, String productOptions)
	{
  	SizeColor sc = getSizeColor(productOptions);
  	
  	stockCounts[sc.size][sc.color] = stockCount;
	}
  
  /*
   * When Shoes are ordered, reduce the stock count for the specific stock type
   * NOTE: assumes validOptions() has already been called for productOptions
   */
  public void reduceStockCount(String productOptions)
	{
  	SizeColor sc = getSizeColor(productOptions);
  	
  	stockCounts[sc.size][sc.color]--;
	}
  /*
   * Print product information in super class and append Shoes specific information 
   */
  public void print()
  {
  	super.print(); 
  }
}



