package View;
import Controller.*;

import javax.swing.*;
import java.awt.*;

public class DisplayPanel extends JPanel{
    private final JPanel messagePanel;
    private final JScrollPane scrollPane;
    private boolean firstMessage = true;
    public DisplayPanel(){
        messagePanel = new JPanel();
        // Panel 설정
        PanelDesigner.designPanel(this);
        messagePanel.setOpaque(true);   // 배경색 보이게 설정
        messagePanel.setBackground(Color.DARK_GRAY);
        messagePanel.setBorder(BorderFactory.createLineBorder(Color.GRAY,3));
        // 초기화면
        messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));
        JLabel startMessage = new JLabel("돈을 투입하세요", JLabel.CENTER);
        startMessage.setForeground(Color.white);    // 글자 색
        messagePanel.add(startMessage, BorderLayout.CENTER);
        // 스크롤
        scrollPane = new JScrollPane(messagePanel,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);    // 가로바 제거
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(0,0)); // 스크롤 동작은 유지, 바는 안 보이게
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
    }
    public void addMessage(String message){
        if(firstMessage) {
            this.messagePanel.removeAll();
            firstMessage = false;
        }
        JLabel label = new JLabel(message);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setFont(new Font("맑은 고딕", Font.PLAIN, ButtonDesigner.fontSize));
        label.setForeground(Color.white);
        this.messagePanel.add(label);
        this.messagePanel.revalidate(); // 새로 추가된 컴포넌트에 맞춰 레이아웃 다시 계산
        this.messagePanel.repaint(); // 화면 다시 그림
        // 새 메시지 추가 시 자동으로 맨 아래로 스크롤
        SwingUtilities.invokeLater(() ->
                scrollPane.getVerticalScrollBar().setValue(
                        scrollPane.getVerticalScrollBar().getMaximum()
                )
        );
    }
}
