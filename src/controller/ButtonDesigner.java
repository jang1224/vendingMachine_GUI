package controller;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ButtonDesigner {
    static public final int fontSize = 12;

    // 색상 정의
    private static final Color COLOR_ON_HOVER = new Color(180, 180, 180); // 마우스 올렸을 때 (진한 회색)
    private static final Color COLOR_NORMAL = new Color(210, 210, 210);   // 평소 (밝은 회색)
    private static final Color TEXT_GREEN = new Color(0, 150, 0);         // 구매 가능 글자색
    private static final Color TEXT_BLACK = Color.BLACK;                  // 기본 글자색

    // 테두리 색상은 고정 (흔들림 방지)
    private static final Color BORDER_HIGHLIGHT = new Color(210, 210, 210);
    private static final Color BORDER_SHADOW = new Color(150, 150, 150);

    static public JButton designButton(JButton button, int width, int height){

        button.setContentAreaFilled(false);  // 기본 배경 채우기 비활성화
        button.setOpaque(true);              // 직접 색 칠하도록
        button.setFocusPainted(false);       // 파란 테두리 제거
        button.setPreferredSize(new Dimension(width,height));
        button.setFont(new Font("맑은 고딕", Font.BOLD, fontSize));
        button.setForeground(Color.BLACK);
        button.setBackground(new Color(210,210,210));   // lightGray 배경 설정

        // 초기 상태 설정
        button.putClientProperty("purchasable", false);
        button.putClientProperty("hovering", false);    // 마우스 올라감 여부
        updateStyle(button); // 초기 스타일 적용

        button.addMouseListener(new MouseAdapter() {    // 마우스 이벤트 리스너 등록
            @Override
            public void mousePressed(MouseEvent e) {
                if (!button.isEnabled()) return;
                button.putClientProperty("pressed", true);
                updateStyle(button);
            }
            @Override
            public void mouseReleased(MouseEvent e){
                if (!button.isEnabled()) return;
                button.putClientProperty("pressed", false);
                updateStyle(button);
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!button.isEnabled()) return;
                button.putClientProperty("hovering", true);
                updateStyle(button);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                if (!button.isEnabled()) return;
                button.putClientProperty("hovering", false);
                updateStyle(button);
            }
        });
        return button;
    }

    // Controller에서 호출하는 메서드
    static public void setPurchasable(JButton button, boolean isPurchasable) {
        Boolean current = (Boolean) button.getClientProperty("purchasable");
        if (current == null || current != isPurchasable) {
            button.putClientProperty("purchasable", isPurchasable);
            updateStyle(button);
        }
    }
    // 내부 스타일 업데이트 로직: 테두리 대신 '글자 색'을 변경
    static private void updateStyle(JButton button) {
        // 현재 상태 값들 읽어오기
        boolean isPurchasable = Boolean.TRUE.equals(button.getClientProperty("purchasable"));
        boolean isHovering = Boolean.TRUE.equals(button.getClientProperty("hovering"));
        boolean isPressed = Boolean.TRUE.equals(button.getClientProperty("pressed"));
        // 글자 색상 결정 (구매 가능하면 초록, 아니면 검정)
        if (isPurchasable) {
            button.setForeground(TEXT_GREEN);
        } else {
            button.setForeground(TEXT_BLACK);
        }

        // 배경 색상 결정 (눌렸거나 마우스가 위에 있으면 진하게, 아니면 밝게)
        if (isPressed || isHovering) {
            button.setBackground(COLOR_ON_HOVER);
        } else {
            button.setBackground(COLOR_NORMAL);
        }

        // 테두리 및 폰트 결정 (눌림 효과)
        if (isPressed) {
            button.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED, BORDER_HIGHLIGHT, BORDER_SHADOW));
            button.setFont(new Font("맑은 고딕", Font.PLAIN, fontSize));
        } else {
            button.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, BORDER_HIGHLIGHT, BORDER_SHADOW));
            button.setFont(new Font("맑은 고딕", Font.BOLD, fontSize));
        }
        button.revalidate();
        button.repaint();
    }

    static public JButton designToggleButton(JButton button, int width, int height) {
        // 기본 스타일 적용
        button.setContentAreaFilled(false);
        button.setOpaque(true);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(width, height));
        // 크기 고정
        button.setMinimumSize(new Dimension(width, height));
        button.setMaximumSize(new Dimension(width, height));
        button.setFocusPainted(false);

        // 초기 상태 off로 설정
        // "isOn"이라는 이름으로 'false' 상태를 버튼 자체에 저장합니다.
        button.putClientProperty("isOn", false);
        // off 상태로 먼저 초기화
        applyToggleStyle(button, false);

        // 리스너 추가
        // MouseListener 대신 ActionListener를 사용해야 상태가 꼬이지 않는다
        button.addActionListener(e -> {
            // 버튼에 저장된 현재 상태를 가져온다
            boolean currentState = (boolean) button.getClientProperty("isOn");

            // 상태를 반대로 뒤집는다 (OFF -> ON)
            boolean newState = !currentState;

            // 새 상태에 맞는 스타일을 적용한다
            applyToggleStyle(button, newState);

            // 뒤집힌 새 상태를 버튼에 다시 저장한다
            button.putClientProperty("isOn", newState);
        });

        return button;
    }
    static private void applyToggleStyle(JButton button, boolean isOn) {
        if (isOn) {
            button.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED, Color.WHITE, Color.GRAY));
            button.setBackground(COLOR_ON_HOVER);
            button.setFont(new Font("맑은 고딕", Font.PLAIN, fontSize));
        } else {
            button.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, COLOR_NORMAL, Color.GRAY));
            button.setBackground(COLOR_NORMAL);
            button.setFont(new Font("맑은 고딕", Font.BOLD, fontSize));
        }
    }
}
