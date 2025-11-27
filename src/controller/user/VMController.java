package controller.user;
import javax.swing.*;
import javax.swing.border.BevelBorder;

import controller.ButtonDesigner;
import controller.admin.AdminController;
import controller.VMUIRefreshable;
import model.*;
import model.user.AccountManager;
import model.user.VMProcessor;
import view.user.DisplayPanel;
import view.user.InputPanel;
import view.admin.AdminFrame;
import view.user.ProductButtonsPanel;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.awt.event.*;
import java.util.Map;
import java.util.Objects;

public class VMController implements VMUIRefreshable{
    private final VMProcessor vmProcessor;
    private final Inventory inventory;
    private final AdminController adminController;
    // 관리자 창을 닫고 상품버튼을 갱신하기 위한 변수 저장
    private final AccountManager currentBalance;
    private final InputPanel inputPanel;
    private final DisplayPanel displayPanel;
    private final ProductButtonsPanel productButtonsPanel;

    private final Map<Product, JButton> productButtonMap = new HashMap<>(); // 구매가능기능을 위한 해시맵

    public VMController(VMProcessor processor,
                        Inventory inventory,
                        AdminController controller,
                        AccountManager currentBalance,
                        InputPanel inputPanel,
                        DisplayPanel displayPanel,
                        ProductButtonsPanel productButtonsPanel) {
        this.vmProcessor = processor;
        this.inventory = inventory;
        this.adminController = controller;

        this.currentBalance = currentBalance;
        this.inputPanel = inputPanel;
        this.displayPanel = displayPanel;
        this.productButtonsPanel = productButtonsPanel;
    }


    //inventory에 있는 상품들을 기반으로 버튼들을 동적생성 후 GUI에 추가
    public ArrayList<JPanel> initProductButtons() {
        HashMap<String, Product> productMap = inventory.getProductMap();
        ArrayList<JPanel> panelList = new ArrayList<>();

        productButtonMap.clear();
        // 버튼 크기 및 이미지 크기
        int buttonWidth = 140;
        int buttonHeight = 40;
        int imageHeight = 100;
        for (Product value : productMap.values()) {    // inventory에 있는 products 버튼 생성
            // 이미지 아이콘 생성
            ImageIcon icon = new ImageIcon(Objects.requireNonNull(getClass().getResource(value.getImagePath())));
            Image img = icon.getImage();
            Image resizedImg = img.getScaledInstance(buttonWidth, imageHeight, Image.SCALE_SMOOTH);
            ImageIcon resizedIcon = new ImageIcon(resizedImg);  // 조절된 이미지로 새 아이콘 생성
            // 이미지 라벨 생성
            JLabel imageLabel = new JLabel(resizedIcon);
            JButton button = ButtonDesigner.designButton(new JButton("<html><center>" + value.getName() + "<br/>( " + value.getPrice() + "원 )</center><html>"),buttonWidth,buttonHeight);
            // 버튼에 액션 리스너 추가 -> 이벤트 발생 시에 productSelection 메소드 실행
            button.addActionListener(new ButtonActionListener(value, button, currentBalance, displayPanel, inputPanel));
            // 생성된 버튼을 맵에 저장
            productButtonMap.put(value, button);
            //JPanel 생성
            JPanel productPanel = new JPanel(new BorderLayout());
            productPanel.add(imageLabel, BorderLayout.CENTER);   // 이미지는 버튼 위에
            productPanel.add(button, BorderLayout.SOUTH);    // 버튼은 패널에서 아래쪽에
            panelList.add(productPanel);
        }
        updateProductState();
        return panelList;
    }

    public void initReturnChangePanel(JPanel panel, AccountManager currentBalance, InputPanel inputPanel, DisplayPanel displayPanel) {   // 반환버튼 초기화 메소드
        JButton returnChangeButton = ButtonDesigner.designButton(new JButton("반환"),150,30);
        returnChangeButton.setMaximumSize(new Dimension(150,30));
        returnChangeButton.setMinimumSize(new Dimension(150,30));
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
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        int[] amounts = {10, 50, 100, 500, 1000, 5000, 10000, 50000};
        for (int i = 0; i < amounts.length; i++) {
            JButton button = ButtonDesigner.designButton(new JButton(amounts[i] + "원"),120,30);
            gbc.gridx = 0;
            gbc.gridy = i;
            gbc.insets = new Insets(5, 5, 5, 5);
            panel.add(button, gbc);
            button.addActionListener(new NumButtonActionListener(currentBalance, amounts[i], inputPanel, displayPanel));
        }
    }

    public void initCardPanel(JPanel panel, AccountManager currentBalance, DisplayPanel displayPanel){
        panel.setLayout(new GridBagLayout());
        JButton button = ButtonDesigner.designToggleButton(new JButton("카드"),180, 50);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.addActionListener(new CardButtonActionListener(currentBalance,displayPanel));
        panel.add(button);
    }

    public void initGoToAdminPanel(JPanel panel){
        panel.setLayout(new GridBagLayout());
        int desiredHeight = 110; // 원하는 패널 높이
        JButton button = ButtonDesigner.designButton(new JButton("관리자 창"), 210, 50);
        Dimension preferredSize = new Dimension(button.getPreferredSize().width, desiredHeight); // 높이만 지정
        panel.setPreferredSize(preferredSize);  // 패널에 선호 크기 강제 설정
        panel.setMinimumSize(preferredSize);
        panel.setMaximumSize(preferredSize);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.addActionListener(new GotoAdminActionListener(this.adminController));
        panel.add(button);
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

    private class CardButtonActionListener implements ActionListener{
        private final AccountManager currentBalance;
        private final DisplayPanel displayPanel;

        public CardButtonActionListener(AccountManager currentBalance, DisplayPanel displayPanel){
            this.currentBalance = currentBalance;
            this.displayPanel = displayPanel;
        }

        public void actionPerformed(ActionEvent e){
            onOffCard(currentBalance, displayPanel);
        }
    }

    private class GotoAdminActionListener implements ActionListener{
        private final AdminController controller;
        public GotoAdminActionListener(AdminController controller){
            this.controller = controller;
        }
        public void actionPerformed(ActionEvent e){
            openAdminFrame(controller);
        }
    }


    // 상품이 0개 이하면 품절로 표시하기
    private void updateProductState() {
        for(Map.Entry<Product, JButton> entry : productButtonMap.entrySet()) {
            Product product = entry.getKey();
            JButton button = entry.getValue();
            if (product.getQuantity() <= 0) {
                // 버튼 텍스트 품절로 바꾸기
                button.setText(product.getName() + " (품절)");
                button.setEnabled(false);   // 버튼 비활성화하기
                UIManager.put("Button.disabledText", Color.RED);
                // 품절 시 ButtonDesigner의 상태 초기화
                ButtonDesigner.setPurchasable(button, false);
                button.setBorder(BorderFactory.createBevelBorder(
                        BevelBorder.RAISED,
                        Color.RED.brighter(),
                        Color.RED.darker()
                ));

            } else {
                button.setEnabled(true);
                button.setText(product.getName() + "( " + product.getPrice() + "원 )");
                // 구매 가능 여부 판단
                boolean canBuy = currentBalance.getIsCard() || (currentBalance.getCurrentBalance() >= product.getPrice());

                // ButtonDesigner에게 "이 버튼은 구매 가능하다/아니다" 상태만 전달
                // ButtonDesigner가 알아서 테두리 색을 유지하고, 클릭 후에도 복구해줍니다.
                ButtonDesigner.setPurchasable(button, canBuy);

            }
        }
    }

    // 버튼 클릭 이벤트 처리 메소드
    private void productSelection(Product product, JButton button, AccountManager currentBalance, DisplayPanel displayPanel, InputPanel inputPanel) {

        // VMProcessor에게 구매 처리 요청하기
        String resultMessage = vmProcessor.processPurchase(product, currentBalance);

        // 결과 메시지 표시하기
        displayPanel.addMessage(resultMessage);

        // 재고가 0이 되면 상품 비활성화하기
        updateProductState();

        // 카드로 결제했다면 isCard를 다시 false로 바꾸기
        if(currentBalance.getIsCard()) {
            currentBalance.setIsCard(true);
        }
        else{
            // 잔액에서 상품 금액 차감하기
            inputPanel.updateCurrentBalance(currentBalance.getCurrentBalance() + "원");

        }

    }

    private void returnChange(AccountManager currentBalance, InputPanel inputPanel, DisplayPanel displayPanel) {
        String resultMessage = vmProcessor.getReturnChange(currentBalance);
        displayPanel.addMessage(resultMessage);
        inputPanel.updateCurrentBalance("0원");

        updateProductState();
    }

    private void clickNumber(AccountManager currentBalance, int num, InputPanel inputPanel, DisplayPanel displayPanel) {
        // num 플러스하기
        currentBalance.insertCoinOrCash(num);
        String currentMessage = String.valueOf(currentBalance.getCurrentBalance());
        // display에 로그 띄우기
        displayPanel.addMessage(num + "원을 투입했습니다! [총 투입금액 : " + currentMessage + "원]");
        // currentBalanceLabel 갱신하기
        inputPanel.updateCurrentBalance(currentMessage + "원");
        updateProductState();
    }

    private void onOffCard(AccountManager currentBalance, DisplayPanel displayPanel){
        if(currentBalance.getIsCard()) {
            // display에 로그 띄우기
            displayPanel.addMessage("카드를 제거했습니다!");
            // isCard를 true 바꾸기
            currentBalance.setIsCard(false);
        }
        else {
            // display에 로그 띄우기
            displayPanel.addMessage("카드를 삽입했습니다!");
            // isCard를 true 바꾸기
            currentBalance.setIsCard(true);
        }
        updateProductState();
    }
    private void openAdminFrame(AdminController controller){
        new AdminFrame(controller, inventory, this);
    }

    @Override
    public void refreshUserVM(){
        productButtonsPanel.reloadProductButtons();
        this.displayPanel.addMessage("자판기 목록이 갱신되었습니다.");
    }

}
