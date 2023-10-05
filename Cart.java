import java.util.ArrayList;

public class Cart {
    private ArrayList<CartItem> Items;

    public Cart(){
        this.Items=new ArrayList<CartItem>();
    }

    public void addItemToCart(Product product, String productOptions){
        Items.add(new CartItem(product, productOptions));
    }
    
    public ArrayList<CartItem> getCart(){
        return Items;
    }

    public void removeFromCart(String productId){
        for(CartItem item: Items){
            if(item.getProduct().getId().equals(productId)){
                Items.remove(item);
                break;
            }

        }

    }

    public void printCart(){
        for(CartItem item: Items){
            item.getProduct().print();
        }
    }

    public ArrayList<String> orderCart(){
        ArrayList<String> cartOrders= new ArrayList<>();
        while(Items.size()>0){
            cartOrders.add(Items.get(0).getProduct().getId());
            cartOrders.add(Items.get(0).getProductOptions());
            Items.remove(0);
        }
        return cartOrders;
    }
}
