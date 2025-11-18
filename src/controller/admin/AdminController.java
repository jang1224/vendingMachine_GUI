package controller.admin;

import controller.ButtonDesigner;
import model.Inventory;
import model.Product;
import model.admin.ProductTable;
import view.admin.*;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;


public class AdminController {
    private final Inventory inventory;
    private boolean productModified = false;
    public AdminController(Inventory inventory){
        this.inventory = inventory;
    }

    public void setupButtons(AdminFrame frame, JTable table, ProductTable productTable){
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton editButton = ButtonDesigner.designButton(new JButton("수정"), 60,30);
        JButton addButton = ButtonDesigner.designButton(new JButton("추가"),60,30);
        JButton deleteButton = ButtonDesigner.designButton(new JButton("삭제"),60,30);
        JButton closeButton = ButtonDesigner.designButton(new JButton("닫기"),60,30);
        // 수정 삭제 버튼 리스너
        editButton.addActionListener(new EditButtonActionListener(frame, table, productTable));
        addButton.addActionListener(new AddButtonActionListener(frame,productTable));
        deleteButton.addActionListener(new DeleteButtonActionListener(frame, table, productTable));
        closeButton.addActionListener(new CloseButtonActionListener(frame));

        buttonPanel.add(editButton);
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(closeButton);
        frame.add(buttonPanel, BorderLayout.SOUTH);
    }

    public Product getSelectedProduct(JTable table, ProductTable productTable){
        int selectedRow = table.getSelectedRow();
        if(selectedRow == -1) return null;
        // 모델 인덱스로 변환하여 상품 객체 반환
        int modelIndex = table.convertRowIndexToModel(selectedRow);
        return productTable.getProductAt(modelIndex);
    }

    public class EditButtonActionListener implements ActionListener{
        private final AdminFrame frame;
        private final JTable table;
        private final ProductTable productTable;
        public EditButtonActionListener(AdminFrame frame, JTable table, ProductTable productTable){
            this.frame = frame;
            this.table = table;
            this.productTable = productTable;
        }
        public void actionPerformed(ActionEvent e){
            Product selectedProduct = getSelectedProduct(this.table, this.productTable);
            handleEditAction(frame, selectedProduct, productTable);
        }
    }

    public class AddButtonActionListener implements ActionListener{
        private final AdminFrame frame;

        private final ProductTable productTable;
        public AddButtonActionListener(AdminFrame frame, ProductTable productTable){
            this.frame = frame;
            this.productTable = productTable;
        }
        public void actionPerformed(ActionEvent e){
            handleAddAction(frame, productTable);
        }
    }
    public class DeleteButtonActionListener implements ActionListener{
        private final AdminFrame frame;
        private final JTable table;
        private final ProductTable productTable;
        public DeleteButtonActionListener(AdminFrame frame, JTable table, ProductTable productTable){
            this.frame = frame;
            this.table = table;
            this.productTable = productTable;
        }
        public void actionPerformed(ActionEvent e){
            Product selectedProduct = getSelectedProduct(this.table, this.productTable);
            handleDeleteAction(frame, selectedProduct, productTable);
        }
    }

    public class CloseButtonActionListener implements ActionListener{
        private final AdminFrame frame;
        public CloseButtonActionListener(AdminFrame frame){
            this.frame = frame;
        }
        public void actionPerformed(ActionEvent e){
            frame.dispose();    // Window 클래스에 정의된 메소드로, 네이비트 화면 자원 닫기 이벤트
        }
    }
    public void handleEditAction(AdminFrame frame, Product productToEdit, ProductTable productTable){
        if(productToEdit == null){
            showMessage(frame, "수정할 상품을 선택해야 합니다.", JOptionPane.WARNING_MESSAGE);
            return;
        }
        // 입력 필드 생성 및 초기값 설정
        JTextField nameField = new JTextField(productToEdit.getName());
        JTextField priceField = new JTextField(String.valueOf(productToEdit.getPrice()));
        JTextField quantityField = new JTextField(String.valueOf(productToEdit.getQuantity()));

        // 입력 필드를 담을 JPanel 생성
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));

        // 라벨과 필드를 Panel에 추가
        inputPanel.add(new JLabel("상품 이름"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("가격"));
        inputPanel.add(priceField);
        inputPanel.add(new JLabel("재고 수량"));
        inputPanel.add(quantityField);
        boolean continueLoop = true;
        do{
            // JOptionPane.showConfirmDialog를 사용하여 사용자 지정 Panel 표시
            int result = JOptionPane.showConfirmDialog(
                    frame,                          // 부모 컴포넌트
                    inputPanel,                     // 사용자 지정 패널
                    "상품 정보 수정",                 // 다이얼로그 제목
                    JOptionPane.OK_CANCEL_OPTION,   // 버튼 옵션
                    JOptionPane.PLAIN_MESSAGE       // 아이콘 (경고 아이콘 없이)
            );

            // 5. 사용자가 '확인(OK)'을 눌렀는지 확인
            if (result == JOptionPane.OK_OPTION) {
                String newName = nameField.getText();
                String newPriceStr = priceField.getText();
                String newQuantityStr = quantityField.getText();
                try {
                    int newPrice = Integer.parseInt(newPriceStr);
                    int newQuantity = Integer.parseInt(newQuantityStr);

                    // 메모리 업데이트
                    editProductSQL(frame, inventory, productToEdit, newName, newPrice, newQuantity);
                    refreshTable(inventory, productTable);
                    continueLoop = false;
                    this.productModified = true;
                } catch (NumberFormatException e) {
                    showMessage(frame, "가격과 수량을 숫자로 입력해야 합니다.", JOptionPane.ERROR_MESSAGE);

                } catch (Exception e) {
                    showMessage(frame, "수정 중 오류 발생" + e.getMessage(), JOptionPane.ERROR_MESSAGE);
                }
            }
            else{
                continueLoop = false;
            }
        }while(continueLoop);
    }

    private void editProductSQL(AdminFrame frame, Inventory inventory, Product productToEdit, String newName, int newPrice, int newQuantity){
        String oldName = productToEdit.getName();
        String sql = "UPDATE PRODUCTS SET name = ?, price = ?, quantity = ? WHERE name = ?";
        String DB_URL = inventory.getDB_URL();
        String DB_USER = inventory.getDB_USER();
        String DB_PASSWORD = inventory. getDB_PASSWORD();

        try(Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            PreparedStatement pstmt = connection.prepareStatement(sql)){

            pstmt.setString(1, newName);
            pstmt.setInt(2, newPrice);
            pstmt.setInt(3, newQuantity);
            pstmt.setString(4, oldName);

            int rowsAffected = pstmt.executeUpdate();   // 쿼리 실행
            if(rowsAffected > 0 ){
                productToEdit.setName(newName);
                productToEdit.setPrice(newPrice);
                productToEdit.setQuantity(newQuantity);

                if(!newName.equals(oldName)){
                    inventory.renameProduct(oldName, newName, productToEdit);
                }
                showMessage(frame,"상품 정보가 " + newName + "으로 DB에서 성공적으로 수정됐습니다.", JOptionPane.INFORMATION_MESSAGE);
            }
            else{
                showMessage(frame, "DB 수정 실패: " + oldName + " 상품을 DB에서 찾을 수 없습니다.", JOptionPane.INFORMATION_MESSAGE);
            }
        }
        catch(SQLException e){
            showMessage(frame, "DB 수정 중 오류 발생: " + e.getMessage(), JOptionPane.ERROR_MESSAGE);
        }
    }

    public void handleAddAction(AdminFrame frame, ProductTable productTable){
        // 입력 필드 생성 및 초기값 설정
        JTextField nameField = new JTextField();
        JTextField priceField = new JTextField();
        JTextField quantityField = new JTextField();
        JTextField imagePathField = new JTextField();

        // 입력 필드를 담을 JPanel 생성
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));

        // 라벨과 필드를 Panel에 추가
        inputPanel.add(new JLabel("상품 이름"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("가격"));
        inputPanel.add(priceField);
        inputPanel.add(new JLabel("재고 수량"));
        inputPanel.add(quantityField);
        inputPanel.add(new JLabel("상품 이미지 경로"));
        inputPanel.add(imagePathField);


        boolean continueLoop = true;
        do {
            int result = JOptionPane.showConfirmDialog(
                    frame,                          // 부모 컴포넌트
                    inputPanel,                     // 사용자 지정 패널
                    "상품 추가",                 // 다이얼로그 제목
                    JOptionPane.OK_CANCEL_OPTION,   // 버튼 옵션
                    JOptionPane.PLAIN_MESSAGE       // 아이콘 (경고 아이콘 없이)
            );
            if(result == JOptionPane.OK_OPTION) {
                String name = nameField.getText();
                String priceStr = priceField.getText();
                String quantityStr = quantityField.getText();
                String imagePath = imagePathField.getText();
                if(name.trim().isEmpty()){
                    showMessage(frame, "상품 이름을 입력해야 합니다.", JOptionPane.ERROR_MESSAGE);
                    continue;
                }
                if(priceStr.trim().isEmpty()){
                    showMessage(frame, "가격을 입력해야 합니다", JOptionPane.ERROR_MESSAGE);
                    continue;
                }
                if(quantityStr.trim().isEmpty()){
                    showMessage(frame, "재고 수량을 입력해야 합니다.", JOptionPane.ERROR_MESSAGE);
                    continue;
                }
                if(imagePath.trim().isEmpty()){
                    showMessage(frame, "상품 이미지 경로를 입력해야 합니다.", JOptionPane.ERROR_MESSAGE);
                    continue;
                }
                try {
                    int price = Integer.parseInt(priceStr);
                    int quantity = Integer.parseInt(quantityStr);

                    // 메모리 업데이트
                    addProductSQL(frame, inventory, name, price, quantity, imagePath);
                    refreshTable(inventory, productTable);
                    continueLoop = false;
                    this.productModified = true;
                } catch (NumberFormatException e) {
                    showMessage(frame, "가격과 수량을 숫자로 입력해야 합니다.", JOptionPane.ERROR_MESSAGE);

                } catch (Exception e) {
                    showMessage(frame, "수정 중 오류 발생" + e.getMessage(), JOptionPane.ERROR_MESSAGE);
                }
            }
            else{
                continueLoop = false;
            }
        }while(continueLoop);

    }

    public void addProductSQL(AdminFrame frame, Inventory inventory, String name, int price, int quantity, String imagePath){
        String sql = "INSERT INTO PRODUCTS (name, price, quantity, imagePath) VALUES (?, ?, ?, ?)";
        String DB_URL = inventory.getDB_URL();
        String DB_USER = inventory.getDB_USER();
        String DB_PASSWORD = inventory.getDB_PASSWORD();
        try(Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setInt(2, price);
            pstmt.setInt(3, quantity);
            pstmt.setString(4, imagePath);

            int rowsAffected = pstmt.executeUpdate();
            if(rowsAffected > 0){
                Product newProduct = new Product(name, price, quantity, imagePath);
                inventory.addProduct(newProduct);
                showMessage(frame, "상품이 DB 및 Inventory에 성공적으로 추가됐습니다", JOptionPane.INFORMATION_MESSAGE);
            }
            else{
                showMessage(frame, "DB 및 Inventory에 상품 추가를 실패했습니다.", JOptionPane.INFORMATION_MESSAGE);
            }
        }catch(SQLException e){
            showMessage(frame, "DB에서 " + name + " 추가 중 오류 발생", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void handleDeleteAction(AdminFrame frame, Product productToDelete, ProductTable productTable){
        if(productToDelete == null){
            showMessage(frame,"삭제할 상품을 선택해야 합니다.", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(frame, "정말 삭제하시겠습니까?", "삭제 확인",JOptionPane.YES_NO_OPTION);
        if(confirm == JOptionPane.YES_OPTION){
            deleteProductSQL(frame, inventory, productToDelete);
            refreshTable(inventory, productTable);
            this.productModified = true;
        }

    }

    public void deleteProductSQL(AdminFrame frame, Inventory inventory, Product productToDelete) {
        String sql = "DELETE FROM PRODUCTS WHERE name = ?";
        String DB_URL = inventory.getDB_URL();
        String DB_USER = inventory.getDB_USER();
        String DB_PASSWORD = inventory.getDB_PASSWORD();
        String productName = productToDelete.getName();
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, productName);

            int rowsAffected = pstmt.executeUpdate();
            if(rowsAffected > 0){
                inventory.removeProduct(productName);
                showMessage(frame, productName + "상품이 삭제됐습니다.", JOptionPane.INFORMATION_MESSAGE);
            }
            else{
                showMessage(frame, productName + "상품을 DB에서 찾을 수 없거나 이미 삭제됐습니다.", JOptionPane.INFORMATION_MESSAGE);
            }
        }
        catch(SQLException e){
            showMessage(frame, "DB에서" + productName + " 삭제 중 오류 발생", JOptionPane.ERROR_MESSAGE);
        }
    }
    public boolean getProductModified(){
        return this.productModified;
    }
    public void setProductModified(boolean productModified){
        this.productModified = productModified;
    }
    public void refreshTable(Inventory inventory, ProductTable productTable){
        productTable.refreshData(inventory);
    }

    public void showMessage(JFrame frame, String message, int messageType){
        JOptionPane.showMessageDialog(frame, message, "알림", messageType);
    }

}
