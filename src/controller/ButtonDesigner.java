package controller;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ButtonDesigner {
    static public final int fontSize = 12;
    static public JButton designButton(JButton button, int width, int height){

        button.setContentAreaFilled(false);  // 기본 배경 채우기 비활성화
        button.setOpaque(true);              // 직접 색 칠하도록
        button.setFocusPainted(false);       // 파란 테두리 제거
        button.setPreferredSize(new Dimension(width,height));
        button.setFont(new Font("맑은 고딕", Font.BOLD, fontSize));
        button.setForeground(Color.BLACK);
        button.setBackground(new Color(210,210,210));   // lightGray 배경 설정
        button.setFocusPainted(false);  // 포거스 파란색 테두리 제거
        button.setOpaque(true); // 배경색이 보이도록
        button.setBorder(BorderFactory.createBevelBorder(
                BevelBorder.RAISED,
                new Color(210,210,210), // 왼쪽 위쪽 밝은 면
                new Color(150,150,150)  // 오른쪽 아래쪽 어두운 면
        ));
        button.addMouseListener(new MouseAdapter() {    // 마우스 이벤트 리스너 등록
            @Override
            public void mousePressed(MouseEvent e) {
                if (!button.isEnabled()) return;
                button.setBorder(BorderFactory.createBevelBorder(   // 테두리가 들어간 모양으로 반전하기
                        BevelBorder.LOWERED,
                        new Color(255,255,255),
                        new Color(120,120,120)
                ));
                button.setBackground(new Color(180,180,180));   // 눌림 시 부드러운 파랑으로 색상 설정
                button.setFont(new Font("맑은 고딕", Font.PLAIN, fontSize));  // 눌림 시 폰트를 살짝 가볍게 해주기
            }
            @Override
            public void mouseReleased(MouseEvent e){
                if (!button.isEnabled()) return;
                button.setBorder(BorderFactory.createBevelBorder(
                        BevelBorder.RAISED,
                        new Color(255, 255, 255),
                        new Color(150, 150, 150)
                ));
                button.setBackground(new Color(180,180,180));
                button.setFont(new Font("맑은 고딕", Font.BOLD, fontSize));
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!button.isEnabled()) return;
                //  마우스를 버튼 위로 올렸을 때 (하이라이트)
                button.setBackground(new Color(180, 180, 180));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                if (!button.isEnabled()) return;
                //  마우스가 버튼 영역을 벗어났을 때 (기본 상태로 복귀)
                button.setBackground(new Color(210, 210, 210));
            }
        });
        return button;
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
            // on 상태
            button.setBorder(BorderFactory.createBevelBorder(
                    BevelBorder.LOWERED, // 들어간 모양의 버튼의 테두리
                    new Color(255, 255, 255),
                    new Color(120, 120, 120)
            ));
            button.setBackground(new Color(180, 180, 180)); // 어두운 배경
            button.setFont(new Font("맑은 고딕", Font.PLAIN, fontSize));
        } else {
            // off 상태
            button.setBorder(BorderFactory.createBevelBorder(
                    BevelBorder.RAISED, // '나온' 테두리
                    new Color(210, 210, 210),
                    new Color(150, 150, 150)
            ));
            button.setBackground(new Color(210, 210, 210)); // 밝은 배경
            button.setFont(new Font("맑은 고딕", Font.BOLD, fontSize));
        }
    }
}
