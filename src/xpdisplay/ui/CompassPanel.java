/*
 * Copyright (c) Duncan Jauncey 2013.   Free for non-commercial use.
 */

package xpdisplay.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import xpdisplay.util.GuiUtils;

public class CompassPanel extends javax.swing.JPanel {
    private int angle;
    
    public CompassPanel() {
        initComponents();
    }
    
    public void setAngle(int degrees) {
        angle = degrees;
        repaint();
    }
    
    public void paintComponent(Graphics gOld) {
        Graphics2D g = (Graphics2D)gOld;
        int w = getWidth();
        int h = getHeight();

        g.setColor(Color.BLACK);
        g.fillRect(0,0,w,h);        
        
        if( w < h ) {
            h = w;
        } else {
            w = h;
        }
        
        int hw = w/2;
        int hh = h/2;        
        

        
        GuiUtils.enableAntialiasing(g);
        g.setStroke(new BasicStroke(1.0f));

        g.setColor(Color.WHITE);
        g.translate(getWidth()/2,getHeight()/2);
        
        g.drawOval(-1,-1,3,3);
        
        g.setStroke(new BasicStroke(2.0f));
        g.drawOval(-hw+5,-hh+5,w-10,h-10);
        
        double theta = Math.toRadians(angle);
        g.rotate(theta);
        g.drawLine(0,0,0,-hh);
        g.rotate(-theta);
        
        g.translate(-hw,-hh);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    
}