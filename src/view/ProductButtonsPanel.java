package view;
import controller.*;
import model.AccountManager;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ProductButtonsPanel extends JPanel{
    public ProductButtonsPanel(VMController controller, AccountManager currentBalance , InputPanel inputPanel, DisplayPanel displayPanel){
        // Panel 설정
        PanelDesigner.designPanel(this);
        // 버튼 패널 레이아웃 설정
        setLayout(new GridBagLayout());
        JPanel productButtonPanel = new JPanel();
        productButtonPanel.setBackground(Color.GRAY);
        productButtonPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 5, 5, 5);    // 버튼끼리의 간격
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        ArrayList<JButton> buttons = controller.initProductButtons(currentBalance, inputPanel, displayPanel);

        int rows = 3;
        int cols = 3;  // 열 개수
        for (int i = 0; i < buttons.size(); i++) {
            gbc.gridx = i % cols;
            gbc.gridy = i / rows;
            productButtonPanel.add(buttons.get(i), gbc);
        }
        add(productButtonPanel, gbc);
    }
}
