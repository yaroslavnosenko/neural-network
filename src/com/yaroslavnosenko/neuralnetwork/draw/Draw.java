package com.yaroslavnosenko.neuralnetwork.draw;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class Draw {

    public Draw() {
    }

    public void draw(String title, List<Double[]> data, List<Integer> target) {
        JFrame frame = new JFrame();
        frame.setTitle(title);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);

        JPanel container = new JPanel();
        container.setPreferredSize(new Dimension(520, 520));
        container.setLayout(new GridBagLayout());
        frame.add(container);
        frame.pack();

        Canvas canvas = new Canvas();
        canvas.setPreferredSize(new Dimension(500, 500));
        container.add(canvas);
        canvas.draw(data, target);
    }

}
