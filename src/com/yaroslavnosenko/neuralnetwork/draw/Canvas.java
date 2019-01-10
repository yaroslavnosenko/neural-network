package com.yaroslavnosenko.neuralnetwork.draw;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.List;

public class Canvas extends JPanel {

    private List<Double[]> data;
    private List<Integer> target;
    private double maxX;
    private double maxY;
    private boolean nul = false;

    Canvas() {
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D graphics2D = (Graphics2D) g;
        for (int i = 0; i < data.size(); i++) {
            double x = (data.get(i)[0]);
            double y = (data.get(i)[1]);


            x /= maxX;
            y /= maxY;

            if (nul) {
                x *= 250;
                y *= 250;
                x += 250;
                y += 250;
            } else {
                x *= 500;
                y *= 500;
            }
            switch (target.get(i)) {
                case 0:
                    graphics2D.setColor(Color.BLACK);
                    break;
                case 1:
                    graphics2D.setColor(Color.RED);
                    break;
                case 2:
                    graphics2D.setColor(Color.GREEN);
                    break;
                case 3:
                    graphics2D.setColor(Color.YELLOW);
                    break;
                case 4:
                    graphics2D.setColor(Color.MAGENTA);
                    break;
                default:
                    graphics2D.setColor(Color.BLUE);
            }
            graphics2D.fill(new Ellipse2D.Double(x - 1, y - 1, 3, 3));
        }
        this.setBackground(Color.GREEN);
    }

    void draw(List<Double[]> data, List<Integer> target) {
        this.data = data;
        this.target = target;
        for (Double[] x : data) {
            if (x[0] > maxX) maxX = x[0];
            if (x[1] > maxY) maxY = x[1];
            if (x[0] < 0 || x[1] < 0) nul = true;
        }
        if (Math.ceil(maxX) != Math.round(maxX)) {
            maxX = Math.floor(maxX) + 0.5;
        } else {
            maxX = Math.ceil(maxX);
        }
        if (Math.ceil(maxY) != Math.round(maxY)) {
            maxY = Math.floor(maxY) + 0.5;
        } else {
            maxY = Math.ceil(maxY);
        }
        repaint();
    }

}
