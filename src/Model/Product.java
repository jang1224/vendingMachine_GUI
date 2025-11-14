package Model;

public class Product {

    private final String name;
    private int price;
    private int quantity = 0;
    private String imagePath;

    public Product(){
        this("이름없음",0,0, "이미지없음");
    }
    public Product(String name, int price, int count, String imagePath){
        this.name = name;
        this.price = price;
        this.quantity = count;
        this.imagePath = imagePath;
    }
    public String getName(){
        return this.name;
    }
    public int getPrice(){
        return this.price;
    }
    public int getQuantity(){
        return this.quantity;
    }
    public String getImagePath(){return this.imagePath;}
    public void setQuantity(int quantity){
        this.quantity=quantity;
    }
    public String getInformation(){
        return "상품명 : " + this.name + ", 수량 : " + this.quantity + ", 가격 : " + this.price;
    }


}
