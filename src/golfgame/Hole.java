/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package golfgame;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 *
 * @author Max
 */
public class Hole implements GraphicsObject {
    private double x;
    private double y;
    private Course course;
    private boolean check = true;
    public Hole(int x, int y,Course course){
        this.x = x;
        this.y = y;
        this.course = course;
    }
    public int getX(){
        return (int)this.x;
    }
    public int getY(){
        return (int)this.y;
    }
    @Override
    public void draw(Graphics2D g, long interval) {
        if(check && Math.abs(course.getBallX() - x) < 8 && Math.abs(course.getBallY()- y) < 8){
            GolfGame._gg.gamePanel.ballInHole();
            check = false;
        }
        g.setColor(new Color(30,10,0));
        g.fillOval((int)x - 10, (int)y - 10, 20, 20);
    }
    
}
