/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package golfgame;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Transparency;

/**
 *
 * @author Max
 */
public class Ball implements GraphicsObject {
    private final int BALL_RADIUS = 4;
    private double x;
    private double y;
    private double vx;
    private double vy;
    private double ax;
    private double ay;
    private double frix;
    private int lastI;
    private Course course;
    private double apothem;
    
    
    public Ball(int x, int y,Course course){
        this.x = x;
        this.y = y;
        vx = 0;
        vy = 0;
        ax = 0;
        ay = 0;
        frix = 0.8;
        lastI = -1;
        this.course = course;
    }
    public void setXVelocity(double vx){
        this.vx = vx;
        this.lastI = -1;
    }
    public void setYVelocity(double vy){
        this.vy = vy;
    }
    public int getX(){
        return (int)this.x;
    }
    public int getY(){
        return (int)this.y;
    }
    private void checkCollision(long interval, Graphics2D g){
        int[] xcoords = course.getPolygon().xpoints;
        int[] ycoords = course.getPolygon().ypoints;
        double x1,y1,x2,y2;
        double vmag = Math.sqrt(vy*vy + vx*vx);
        double vdir = Math.atan2(vy,vx);
        for(int i = 0; i < xcoords.length; i++){
            
            x1 = xcoords[i]; y1 = ycoords[i];
            if(i+1 < xcoords.length){
                x2 = xcoords[i+1]; y2 = ycoords[i+1];
            }
            else{
                x2 = xcoords[0]; y2 = ycoords[0];
            }
            double area = Math.abs((x2 - x1)*(y - y1) - (x - x1)*(y2-y1));
            double lineln = Math.sqrt((x2-x1)*(x2-x1) + (y2-y1)*(y2-y1));
            apothem = area/lineln;
            double linedir = Math.atan2(y2-y1, x2-x1);
            double wallNormal = linedir + Math.PI/2;
            double reflection = Math.PI + (vdir + (2.0 * (Math.PI * 2.0 - (vdir - wallNormal))));
            // This took a lot of trial and error. But it works. Finally
            int midX = (int)((x1 + x2) / 2.0);
            int midY = (int)((y1 + y2) / 2.0);
            /* for debugging purposes  
            g.setColor(Color.CYAN);
            g.drawLine(midX, midY,(int)(40 * Math.cos(vdir + Math.PI)) + midX, (int)(40 * Math.sin(vdir + Math.PI)) + midY);
            g.setColor(Color.BLACK);
            g.drawLine(midX, midY,(int)(40 * Math.cos(wallNormal)) + midX, (int)(40 * Math.sin(wallNormal)) + midY);
            g.setColor(Color.RED);
            g.drawLine(midX, midY,(int)(40 * Math.cos(reflection)) + midX, (int)(40 * Math.sin(reflection)) + midY);
            g.setColor(Color.black);
            /**/
            if(i != lastI){
                if(apothem < BALL_RADIUS && 
                        (
                            (
                                ((x1 < x && x < x2) || (x2 < x && x < x1)) && 
                                ((y1 < y && y < y2) || (y2 < y && y < y1))
                            )
                            ||
                            ((x1 < x && x < x2) || (x2 < x && x < x1))
                            ||
                            ((y1 < y && y < y2) || (y2 < y && y < y1))
                        )
                ){ //collision
                    vx = vmag*Math.cos(reflection);
                    vy = vmag*Math.sin(reflection);
                    lastI = i;
                    break;
                }
            }
        }
    }
    public void moveTo(int x, int y){
        this.x = x;
        this.y = y;
    }
    @Override
    public void draw(Graphics2D g, long interval) {
        this.x += vx * (interval / 1000000000.0);
        this.y += vy * (interval / 1000000000.0);
        this.vx += ax * (interval / 1000000000.0);
        this.vy += ay * (interval / 1000000000.0);
        vx *= 1 - (frix * (interval / 1000000000.0));
        vy *= 1 - (frix * (interval / 1000000000.0));
        if(Math.sqrt(vx*vx + vy*vy) < 20){
            vx = 0;
            vy = 0;
        }
        if(vx != 0 || vy != 0) 
            checkCollision(interval,g);
        g.setColor(Color.white);
        g.fillOval((int)x - 4, (int)y - 4, 8, 8);
        
    }
    
}
