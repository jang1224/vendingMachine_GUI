package view.user;
import javax.swing.*;
import java.awt.*;
import controller.*;
import controller.user.VMController;
import model.user.AccountManager;

public class InputPanel extends JPanel{
    private final JLabel currentBalanceLabel = new JLabel();

    private final AccountManager currentBalance;
    private final DisplayPanel displayPanel;

    private final JPanel topPanel;
    private final JPanel numberPadPanel;
    private final JPanel cardPanel;

    public InputPanel(AccountManager currentBalance, DisplayPanel displayPanel){
        this.currentBalance = currentBalance;
        this.displayPanel = displayPanel;

        PanelDesigner.designPanel(this);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 5, 5, 5);    // 버튼끼리의 간격

        topPanel = new JPanel();
        topPanel.setOpaque(true);
        topPanel.setBackground(Color.GRAY);
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.add(Box.createVerticalGlue()); // 컴포넌트 위에 수직 접착제 추가
        // 현재 잔액 표시에 위치하겠금
        topPanel.add(currentBalanceLabel);
        topPanel.add(Box.createVerticalStrut(10)); // 간격
        // 반환 버튼
        topPanel.add(Box.createVerticalGlue()); // 컴포넌트 아래에 수직 접착제 추가
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 0.2;
        add(topPanel, gbc);

        numberPadPanel = new JPanel();
        numberPadPanel.setOpaque(true);
        numberPadPanel.setBackground(Color.GRAY);

        gbc.gridy = 1;
        gbc.weighty = 0.5;
        add(numberPadPanel, gbc);

        cardPanel = new JPanel();
        cardPanel.setOpaque(true);
        cardPanel.setBackground(Color.GRAY);

        gbc.gridy = 2;
        gbc.weighty = 0.15;

        add(cardPanel, gbc);
    }
    public void setController(VMController controller){
        topPanel.removeAll();
        numberPadPanel.removeAll();
        cardPanel.removeAll();

        // --- Top Panel 초기화 (잔액 레이블 및 반환 버튼) ---
        topPanel.add(Box.createVerticalGlue());

        // 현재 잔액 표시 초기화
        controller.initShowCurrentBalance(currentBalanceLabel, currentBalance);
        currentBalanceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        topPanel.add(currentBalanceLabel);
        topPanel.add(Box.createVerticalStrut(10));

        // 반환 버튼 초기화
        controller.initReturnChangePanel(topPanel, currentBalance, this, displayPanel);
        topPanel.add(Box.createVerticalGlue());
        // 숫자 패드 버튼 초기화
        controller.initNumberPadPanel(numberPadPanel, currentBalance, this, displayPanel);
        // 카드 버튼 초기화
        controller.initCardPanel(cardPanel, currentBalance, displayPanel);

        // 갱신
        this.revalidate();
        this.repaint();
    }

    public void updateCurrentBalance(String message){
        this.currentBalanceLabel.setText(message);
        this.currentBalanceLabel.revalidate();
        this.currentBalanceLabel.repaint();
    }
}
