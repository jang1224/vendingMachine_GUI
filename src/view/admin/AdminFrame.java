package view.admin;
import javax.swing.*;

import controller.admin.AdminController;
import controller.user.VMController;
import model.*;
import model.admin.ProductTable;

import java.awt.*;
import java.awt.event.*;

public class AdminFrame extends JFrame {
    private final AdminController adminController;
    private final VMController vmController;
    public AdminFrame(AdminController adminController, Inventory inventory, VMController vmController) {
        ProductTable productTable = new ProductTable(inventory);
        JTable table = new JTable(productTable);
        this.adminController = adminController;
        this.vmController = vmController;

        setTitle("관리자 창");
        setSize(1600, 900);
        setLayout(new BorderLayout());
        setupWindowClosingListener();   // 관리자 창을 닫으면 자판기 UI 갱신하기
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        add(new JScrollPane(table), BorderLayout.CENTER);
        // 버튼 설정
        adminController.setupButtons(this, table, productTable);
        // Window클래스의 메소드로, 컨테이너의 크기를 내부 컴포넌트들의 선호 크기에 맞게 자동 조정
        pack(); // 수동 조정인 setSize(width, height)와는 다름
        setVisible(true);
    }
    private void setupWindowClosingListener(){
        this.addWindowListener(new WindowAdapter(){
                public void windowClosed(WindowEvent e){
                    if(adminController.getProductModified()){
                        vmController.refreshUserVM();
                        adminController.setProductModified(false);
                    }
                }
        });
    }



}
