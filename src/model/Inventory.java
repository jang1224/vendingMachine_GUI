package model;
import java.sql.*;
import java.util.HashMap;

import static java.sql.DriverManager.getConnection;

public class Inventory {
    private final String DB_URL = "jdbc:mysql://localhost:3306/vending_machine";
    private final String DB_USER = "root";  // 여기에 MySQL 사용자명
    private final String DB_PASSWORD = "your password"; // 여기에 root 비밀번호
    private final HashMap<String, Product> productMap = new HashMap<>();
    public Inventory(){
        loadProductsFromDB();
    }
    public String[] getInfoDB(){
        return new String[]{DB_URL, DB_USER, DB_PASSWORD};
    }
    private void loadProductsFromDB(){
        String sql = "SELECT * FROM products";
        try(Connection connection = getConnection(DB_URL, DB_USER, DB_PASSWORD);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql)){

            while(resultSet.next()){
                String name = resultSet.getString("name");
                int price = resultSet.getInt("price");
                int quantity = resultSet.getInt("quantity");
                String imagePath = resultSet.getString("imagePath");

                Product product = new Product(name, price, quantity, imagePath);
                productMap.put(name, product);
            }
        } catch (SQLException e){
            System.out.println("DB 로딩 실패" + e.getMessage());
        }
    }

    public void decreaseProduct(Product product){
        int quantity = product.getQuantity() - 1;   // 수량 1감소
        product.setQuantity(quantity);
        String sql = "UPDATE products SET quantity = ? WHERE name = ?";

        try (Connection connection = getConnection(DB_URL, DB_USER, DB_PASSWORD);
            PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setInt(1, quantity);
            preparedStatement.setString(2, product.getName());
            preparedStatement.executeUpdate(); // SQL 실행
        } catch (SQLException e) {
            System.out.println("DB 업데이트 실패 : " + e.getMessage());
        }
    }
    public void removeProduct(String name){
        productMap.remove(name);
    }
    public void renameProduct(String oldName, String newName, Product product){
        if(productMap.containsKey(oldName)){
            productMap.remove(oldName);
            productMap.put(newName,product);
        }
    }
    public void addProduct(Product product){
        productMap.put(product.getName(), product);
    }
    public HashMap<String, Product> getProductMap(){
        return productMap;
    }
    public String getDB_URL(){
        return DB_URL;
    }
    public String getDB_USER(){
        return DB_USER;
    }
    public String getDB_PASSWORD(){
        return DB_PASSWORD;
    }

}
