package view;
import model.*;
import javax.swing.*;
import java.awt.*;
import controller.*;

public class InputPanel extends JPanel{
    private final JLabel currentBalanceLabel = new JLabel();
    public InputPanel(VMController controller, AccountManager currentBalance, DisplayPanel displayPanel){
        PanelDesigner.designPanel(this);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 5, 5, 5);    // 버튼끼리의 간격

        JPanel topPanel = new JPanel();
        topPanel.setOpaque(true);
        topPanel.setBackground(Color.GRAY);
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.add(Box.createVerticalGlue()); // 컴포넌트 위에 수직 접착제 추가
        // 현재 잔액 표시
        controller.initShowCurrentBalance(currentBalanceLabel, currentBalance);
        currentBalanceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);  // label을 topPanel 가운데에 위치하겠금
        topPanel.add(currentBalanceLabel);
        topPanel.add(Box.createVerticalStrut(10)); // 간격
        // 반환 버튼
        controller.initReturnChangePanel(topPanel, currentBalance, this, displayPanel);
        topPanel.add(Box.createVerticalGlue()); // 컴포넌트 아래에 수직 접착제 추가
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 0.2;
        add(topPanel, gbc);

        JPanel numberPadPanel = new JPanel(new GridLayout());
        numberPadPanel.setOpaque(true);
        numberPadPanel.setBackground(Color.DARK_GRAY);
        controller.initNumberPadPanel(numberPadPanel, currentBalance, this, displayPanel);
        gbc.gridy = 1;
        gbc.weighty = 0.8;
        add(numberPadPanel, gbc);
    }

    public void updateCurrentBalance(String message){
        this.currentBalanceLabel.setText(message);
        this.currentBalanceLabel.revalidate();
        this.currentBalanceLabel.repaint();
    }
}
