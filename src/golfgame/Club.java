/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package golfgame;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Line2D;

/**
 *
 * @author Max
 */
public class Club implements GraphicsObject {
    public final int FREE = 0;
    public final int PIVOT = 1;
    public final int POWER = 2;
    public final int ANIM = 3;
    
    private final int animLength = 4;
    private long animStart;
    private long animEnd;
    private double angle;
    private int status;
    private int x,y;
    private int mousex, mousey;
    private double theta;
    private double radius;
    private Course course;
    private Shape shape;
    
    public Club(Course course){
        status = FREE;
        this.course = course;
    }
    public int getStatus(){
        return this.status;
    }
    public void setXY(int x, int y){
        if(status == FREE){
            this.x = x;
            this.y = y;
        }
        if(status == PIVOT){
            this.theta = Math.atan2(y - this.y, x - this.x);
        }
        this.mousex = x;
        this.mousey = y;
    }
    public void handleClick(){
        switch(status){
            case ANIM:
                
                break;
            case POWER:
                status = ANIM;
                animStart = 0;
                animEnd = 0;
                break;
            case PIVOT:
                status = POWER;
                break;
            case FREE:
                status = PIVOT;
                shape = new Rectangle(-5, -8, 10, 3);
                x = course.getBallX();
                y = course.getBallY();
                break;
        }
    }
    @Override
    public void draw(Graphics2D g, long interval) {
        this.x = course.getBallX();
        this.y = course.getBallY();
        if(status == ANIM){
            if(animStart == 0){
                animStart = System.nanoTime();
                animEnd = animStart + animLength * 100000000;
            }
            animStart += interval;
            if(animStart > animEnd){
                status = FREE;
                course.hitBall(radius * Math.cos(theta) * 2, radius * Math.sin(theta) * 2);
            }
        }
        
        g.setColor(new Color(30,30,30));
        g.drawLine(mousex-3, mousey-3, mousex+3, mousey+3);
        g.drawLine(mousex+3, mousey-3, mousex-3, mousey+3);
        switch(status){
            case FREE:
                break;
            case ANIM:
                double offset = Math.sin((Math.PI * (animEnd - animStart) / 100000000.0)/animLength)*radius / 4;
                shape = new Rectangle(-5, (int)(-8 - offset), 10, 3);
                break;
            case PIVOT:
                g.drawLine(x,y,(int)(30 * Math.cos(theta)) + x,(int)(30 * Math.sin(theta)) + y);
                break;
            case POWER:
                g.translate(x, y);
                g.rotate(theta - Math.PI / 2);
                g.fill(shape);
                g.rotate(Math.PI / 2 - theta);
                g.translate(-x, -y);
                double theta2 = Math.atan2(mousey - y, mousex - x);
                radius = Math.sqrt((mousey - y)*(mousey - y) + (mousex - x)*(mousex - x))*Math.cos(theta - theta2);
                if(radius < 0) radius = 0;
                g.drawLine(x,y,(int)(radius * Math.cos(theta)) + x,(int)(radius * Math.sin(theta)) + y);
                break;
        }
        if(status != FREE){
            g.translate(x, y);
            g.rotate(theta - Math.PI / 2);
            g.fill(shape);
            g.rotate(Math.PI / 2 - theta);
            g.translate(-x, -y);
        }
    }
    
}
