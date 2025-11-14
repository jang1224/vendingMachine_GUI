package Controller;

import javax.swing.*;
import java.awt.*;

public class PanelDesigner {
    static public void designPanel(JPanel panel){

        panel.setOpaque(true);
        panel.setBackground(Color.lightGray);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY,3),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
    }

}
