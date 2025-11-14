package Controller;
import javax.swing.*;

import Model.*;
import View.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.awt.event.*;

public class VMController {
    private final VMProcessor vmProcessor;
    private final Inventory inventory;

    public VMController(VMProcessor processor, Inventory inventory) {
        this.vmProcessor = processor;
        this.inventory = inventory;
    }


    //inventory에 있는 상품들을 기반으로 버튼들을 동적생성 후 GUI에 추가
    public ArrayList<JButton> initProductButtons(AccountManager currentBalance, InputPanel inputPanel, DisplayPanel displaypanel) {

        HashMap<String, Product> productMap = inventory.getProductMap();
        ArrayList<JButton> buttonList = new ArrayList<>();
        for (Product value : productMap.values()) {    // inventory에 있는 products 버튼 생성
            JButton button = ButtonDesigner.designButton(new JButton("<html><center>" + value.getName() + "<br/>( " + value.getPrice() + "원 )</center><html>"),140,60);
            // 버튼에 액션 리스너 추가 -> 이벤트 발생 시에 productSelection 메소드 실행
            button.addActionListener(new ButtonActionListener(value, button, currentBalance, displaypanel, inputPanel));
            // 재고가 0이면 버튼 비활성화 처리하기.
            updateButtonState(value, button);
            buttonList.add(button); // GUI 패널에 버튼 추가
        }
        return buttonList;
    }

    public void initReturnChangePanel(JPanel panel, AccountManager currentBalance, InputPanel inputPanel, DisplayPanel displayPanel) {   // 반환버튼 초기화 메소드
        JButton returnChangeButton = ButtonDesigner.designButton(new JButton("반환"),120,30);
        returnChangeButton.setMaximumSize(new Dimension(150,50));
        returnChangeButton.setMinimumSize(new Dimension(150,50));
        returnChangeButton.addActionListener(new ReturnActionListener(currentBalance, inputPanel, displayPanel));
        returnChangeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(returnChangeButton);
    }
    // 현재금액 panel
    public void initShowCurrentBalance(JLabel label, AccountManager currentBalance){
        label.setFont(new Font("맑은 고딕", Font.BOLD, ButtonDesigner.fontSize));
        label.setHorizontalAlignment(SwingConstants.RIGHT); // 라벨 내부의 텍스트 오른쪽 정렬
        label.setOpaque(true);
        label.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.darkGray, 3),           // 바깥 테두리
                BorderFactory.createEmptyBorder(10, 10, 10, 10)          // 내부 여백
        ));
        Dimension labelSize = new Dimension(150, 50);
        // InputPanel의 GridBagLayout이 gbc.fill = BOTH로 topPanel을 늘리고,
        // topPanel의 BoxLayout이 다시 그 안의 currentBalanceLabel을 늘리고 있어서 크기 설정이 안 먹는 것
        // BoxLayout이 강제로 크기를 늘리지 못하도록 min과 max를 setPreferredSize한 값도 동일하게 넣어준다.
        label.setMinimumSize(labelSize);
        label.setPreferredSize(labelSize);
        label.setMaximumSize(labelSize);
        label.setText(currentBalance.getCurrentBalance() + "원");
    }

    public void initNumberPadPanel(JPanel panel, AccountManager currentBalance, InputPanel inputPanel, DisplayPanel displayPanel) {  // 숫자패드 초기화 메소드
        JPanel numberPadPanel = new JPanel();
        numberPadPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        int[] amounts = {10, 50, 100, 500, 1000, 5000, 10000, 50000};
        for (int i = 0; i < amounts.length; i++) {
            JButton button = ButtonDesigner.designButton(new JButton(amounts[i] + "원"),120,30);
            if(amounts[i] < 1000){
                    // 동전모양으로 설정
            }

            gbc.gridx = 0;
            gbc.gridy = i;
            gbc.insets = new Insets(5, 5, 5, 5);
            numberPadPanel.add(button, gbc);
            button.addActionListener(new NumButtonActionListener(currentBalance, amounts[i], inputPanel, displayPanel));
        }
        panel.add(numberPadPanel);
    }

    // ActionListener를 상속받은 ButtonActionListener 클래스
    private class ButtonActionListener implements ActionListener {
        private final Product product;
        private final JButton button;
        private final AccountManager currentBalance;
        private final DisplayPanel displayPanel;
        private final InputPanel inputPanel;

        public ButtonActionListener(Product product, JButton button, AccountManager currentBalance, DisplayPanel displayPanel, InputPanel inputPanel) {
            this.product = product;
            this.button = button;
            this.currentBalance = currentBalance;
            this.displayPanel = displayPanel;
            this.inputPanel = inputPanel;
        }

        public void actionPerformed(ActionEvent e) {
            productSelection(product, button, currentBalance, displayPanel, inputPanel);
        }
    }

    private class ReturnActionListener implements ActionListener {
        private final AccountManager currentBalance;
        private final InputPanel inputPanel;
        private final DisplayPanel displayPanel;
        public ReturnActionListener(AccountManager currentBalance, InputPanel inputPanel, DisplayPanel displayPanel) {
            this.currentBalance = currentBalance;
            this.inputPanel = inputPanel;
            this.displayPanel = displayPanel;
        }

        public void actionPerformed(ActionEvent e) {
            returnChange(currentBalance, inputPanel, displayPanel);
        }
    }

    private class NumButtonActionListener implements ActionListener {
        private final AccountManager currentBalance;
        private final int num;
        private final InputPanel inputPanel;
        private final DisplayPanel displayPanel;

        public NumButtonActionListener(AccountManager currentBalance, int num, InputPanel inputPanel, DisplayPanel displayPanel) {
            this.currentBalance = currentBalance;
            this.num = num;
            this.inputPanel = inputPanel;
            this.displayPanel = displayPanel;
        }

        public void actionPerformed(ActionEvent e){
            clickNumber(currentBalance, num, inputPanel, displayPanel);
        }
    }


    // 상품이 0개 이하면 품절로 표시하기
    public void updateButtonState(Product product, JButton button) {
        if (product.getQuantity() <= 0) {
            button.setEnabled(false);   // 버튼 비활성화하기
            button.setText(product.getName() + " (품절)"); // 버튼 텍스트 품절로 바꾸기
        }
    }

    // 버튼 클릭 이벤트 처리 메소드
    public void productSelection(Product product, JButton button, AccountManager currentBalance, DisplayPanel displayPanel, InputPanel inputPanel) {

        // VMProcessor에게 구매 처리 요청하기
        String resultMessage = vmProcessor.processPurchase(product, currentBalance);

        // 결과 메시지 표시하기
        displayPanel.addMessage(resultMessage);

        // 잔액에서 상품 금액 차감하기
        inputPanel.updateCurrentBalance(currentBalance.getCurrentBalance() + "원");

        // 재고가 0이 되면 버튼 비활성화하기
        updateButtonState(product, button);

    }

    public void returnChange(AccountManager currentBalance, InputPanel inputPanel, DisplayPanel displayPanel) {
        String resultMessage = vmProcessor.getReturnChange(currentBalance);
        displayPanel.addMessage(resultMessage);
        inputPanel.updateCurrentBalance("0원");
    }

    public void clickNumber(AccountManager currentBalance, int num, InputPanel inputPanel, DisplayPanel displayPanel) {
        // num 플러스하기
        currentBalance.insertCoinOrCash(num);
        String currentMessage = String.valueOf(currentBalance.getCurrentBalance());
        // display에 로그 띄우기
        displayPanel.addMessage(num + "원을 투입했습니다! [총 투입금액 : " + currentMessage + "원]");
        // currentBalanceLabel 갱신하기
        inputPanel.updateCurrentBalance(currentMessage + "원");
    }
}
