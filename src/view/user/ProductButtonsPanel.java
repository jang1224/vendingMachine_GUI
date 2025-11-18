package view.user;
import controller.*;
import controller.user.VMController;


import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ProductButtonsPanel extends JPanel{
    private VMController controller;
    private final JPanel productButtonPanel;

    public ProductButtonsPanel(){

        // Panel 설정
        PanelDesigner.designPanel(this);
        // 버튼 패널 레이아웃 설정
        setLayout(new GridBagLayout());

        productButtonPanel = new JPanel(new GridBagLayout());
        productButtonPanel.setBackground(Color.GRAY);

        GridBagConstraints gbcContainer = new GridBagConstraints();
        gbcContainer.fill = GridBagConstraints.BOTH;
        gbcContainer.weightx = 1.0;
        gbcContainer.weighty = 1.0;
        this.add(productButtonPanel, gbcContainer); // 여기서 한 번만 추가
    }


    public void setController(VMController controller){
        this.controller = controller;
        this.initProductButtons();
    }

    private void initProductButtons(){
        if(this.controller == null) return;

        this.productButtonPanel.removeAll();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 5, 5, 5);    // 버튼끼리의 간격
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        ArrayList<JPanel> products = controller.initProductButtons();

        int rows = 4;
        int cols = 4;  // 열 개수
        //productButtonsContainer.removeAll();
        for (int i = 0; i < products.size(); i++) {
            gbc.gridx = i % cols;
            gbc.gridy = i / rows;
            productButtonPanel.add(products.get(i), gbc);
        }

        this.add(productButtonPanel, gbc);
        this.revalidate();
        this.repaint();
    }
    public void reloadProductButtons(){
        this.initProductButtons();
    }
}
