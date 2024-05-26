package main.GUI.HelperClasses;

import javax.swing.*;
import java.awt.*;

public class UISetUpMethods {
    public static JTextField getjTextField(JPanel p, GridBagConstraints c, int row, String headline, boolean editable) {
        JTextField field = new JTextField();
        field.setEditable(editable);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = row;
        JPanel j = new JPanel();
        JTextField t = new JTextField(headline);
        t.setPreferredSize(new Dimension(200,60));
        t.setEditable(false);
        j.add(t);
        field.setPreferredSize(new Dimension(450, 60));
        j.add(field);
        p.add(j, c);
        return field;
    }

    public static JTextField getjTextField(JPanel p, GridBagConstraints c, int row, String headline) {
        return getjTextField(p, c, row, headline,false);
    }

    public static JTextArea getjTextArea(JPanel p, GridBagConstraints c, int row, String headline, boolean big) {
        int height = big ? 180 : 60;
        JTextArea area = new JTextArea();
        area.setLineWrap(true);
        area.setEditable(false);
        area.setBackground(new Color(238,238,238));
        JScrollPane scrollPane = new JScrollPane(area);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = row;
        JPanel j = new JPanel();
        JTextField t = new JTextField(headline);
        t.setPreferredSize(new Dimension(200, height));
        t.setEditable(false);
        j.add(t);
        scrollPane.setPreferredSize(new Dimension(450, height));
        j.add(scrollPane);
        p.add(j, c);
        return area;
    }
}
