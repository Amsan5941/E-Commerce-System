public class CartItem extends Product {
    private Product product;
    private String productOptions;

    public CartItem(Product product, String productOptions){
        this.product=product;
        this.productOptions=productOptions;
    }

    public Product getProduct(){
        return product;
    }

    public String getProductOptions(){
        return productOptions;
    }

    public void prints(){
        product.print();
    }


}
