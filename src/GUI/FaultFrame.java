package GUI;

import javax.swing.*;
import java.awt.*;

public class FaultFrame extends JFrame {
    public FaultFrame(Exception e){
        setSize(new Dimension(500, 250));
        JTextArea a = new JTextArea();
        a.setSize(new Dimension(480,230));
        ScrollTextField scrolltext = new ScrollTextField(a);
        scrolltext.setText(e.toString());
        add(scrolltext);
        setLocation(new Point(800,400));
        setVisible(true);
    }
}
