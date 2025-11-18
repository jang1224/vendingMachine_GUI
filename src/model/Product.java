package model;

public class Product {

    private String name;
    private int price;
    private int quantity;
    private String imagePath;

//    public Product(){
//        this("이름없음",0,0, "이미지없음");
//    }
    public Product(String name, int price, int count, String imagePath){
        this(name, price, count);
        this.imagePath = imagePath;
    }
    public Product(String name, int price, int count){
        this.name = name;
        this.price = price;
        this.quantity = count;
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
    public void setPrice(int price){
        this.price = price;
    }
    public void setName(String name){
        this.name = name;
    }
    public void setQuantity(int quantity){
        this.quantity=quantity;
    }
//    public String getInformation(){
//        return "상품명 : " + this.name + ", 수량 : " + this.quantity + ", 가격 : " + this.price;
//    }


}
