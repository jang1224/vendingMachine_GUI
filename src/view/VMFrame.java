package view;
import controller.VMController;
import model.*;
import java.awt.*;
import javax.swing.*;

public class VMFrame extends JFrame{
    private final ProductButtonsPanel productButtonPanel;
    private final DisplayPanel displayPanel;
    private final InputPanel inputPanel;

    public VMFrame(VMController controller){
        AccountManager currentBalance = new AccountManager();
        displayPanel = new DisplayPanel();
        inputPanel = new InputPanel(controller, currentBalance, displayPanel);
        productButtonPanel = new ProductButtonsPanel(controller, currentBalance, inputPanel, displayPanel);
        setTitle("자판기");
        setDefaultCloseOperation(EXIT_ON_CLOSE); // 프레임 윈도우를 닫으면 프로그램을 종료하도록 설정
        setDimension(1100,800);
//        setResizable(false);    // 창 크기 고정하기
        setBackground(Color.GRAY);


        setLayout(new GridBagLayout());  // contentPane을 확실히 BorderLayout으로 설정
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(10,10,10,10);
        gbc.gridy = 0;
        gbc.weighty = 1.0;
        // 상품버튼 패널 추가
        gbc.weightx = 0.45;
        gbc.gridx = 0;
        add(productButtonPanel,gbc);
        // 디스플레이 패널 추가
        gbc.weightx = 0.35;
        gbc.gridx = 1;
        add(displayPanel, gbc);
        // Input 패널 추가
        gbc.weightx = 0.20;
        gbc.gridx = 2;
        add(inputPanel, gbc);

        setVisible(true);   // 프레임 출력 -> 창 띄워짐


    }
    public void setDimension(int width, int height){
        int productWidth = (int)(width * 0.45);
        int inputWidth   = (int)(width * 0.25);
        int displayWidth = (int)(width * 0.30);
        setSize(width, height);
        productButtonPanel.setPreferredSize(new Dimension(productWidth, height));
        productButtonPanel.setMaximumSize(new Dimension(productWidth, height));
        productButtonPanel.setMinimumSize(new Dimension(productWidth, height));

        inputPanel.setPreferredSize(new Dimension(inputWidth, height));
        inputPanel.setMaximumSize(new Dimension(inputWidth, height));
        inputPanel.setMinimumSize(new Dimension(inputWidth, height));

        displayPanel.setPreferredSize(new Dimension(displayWidth, height));
        displayPanel.setMaximumSize(new Dimension(displayWidth, height));
        displayPanel.setMinimumSize(new Dimension(displayWidth, height));
    }

    public static void main(String[] args) {
        Inventory product = new Inventory();
        VMFrame vm = new VMFrame(new VMController(new VMProcessor(product), product));
    }
}
