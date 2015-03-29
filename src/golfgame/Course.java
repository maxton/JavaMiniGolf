/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package golfgame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

/**
 *
 * @author Max
 */
public class Course implements GraphicsObject {
    private Font font;
    private Polygon courseShape;
    private Ball ball;
    private Hole hole;
    private int par;
    private int strokes = 0;
    private int num;
    public Course(int coursenum) throws IOException{
        num = coursenum;
        font = new Font("Verdana",Font.BOLD,16);
        courseShape = new Polygon();
            InputStream in = getClass().getResourceAsStream("course"+coursenum+".txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            LineNumberReader  lnr = new LineNumberReader(new InputStreamReader(getClass().getResourceAsStream("course"+coursenum+".txt")));
            int lines = 0;
            while (lnr.readLine() != null){
                lines++;
    	    }
            int vertices = lines - 3;
            
            String line = br.readLine();
            par = Integer.parseInt(line);
            line = br.readLine();
            String[] ints = line.split(" ");
            ball = new Ball(Integer.parseInt(ints[0]),Integer.parseInt(ints[1]),this);
            line = br.readLine();
            ints = line.split(" ");
            hole = new Hole(Integer.parseInt(ints[0]),Integer.parseInt(ints[1]),this);
            int[] xpoints = new int[vertices];
            int[] ypoints = new int[vertices];
            for(int cur = 0; (line = br.readLine()) != null; cur++){
                ints = line.split(" ");
                xpoints[cur] = Integer.parseInt(ints[0]);
                ypoints[cur] = Integer.parseInt(ints[1]);
            }
            br.close();
            lnr.close();
            courseShape = new Polygon(xpoints,ypoints,vertices);
    }
    public Polygon getPolygon(){
        return this.courseShape;
    }
    public int getBallX(){
        return ball.getX();
    }
    public int getBallY(){
        return ball.getY();
    }
    public void hitBall(double x, double y){
        ball.setXVelocity(x*2);
        ball.setYVelocity(y*2);
        strokes++;
    }
    public void ballToHole(){
        ball.setXVelocity(0);
        ball.setYVelocity(0);
        ball.moveTo(hole.getX(),hole.getY());
    }
    public int getPar(){
        return par;
    }
    public int getStrokes(){
        return strokes;
    }
    @Override
    public void draw(Graphics2D g, long interval) {
        g.setColor(new Color(0,128,0));
        g.fillRect(0, 0, 800, 600);
        g.setColor(new Color(0,216,0));
        g.fillPolygon(courseShape);
        hole.draw(g,interval);
        ball.draw(g,interval);
        g.setFont(font);
        g.drawString("Hole: "+(num+1)+" Par: "+par+" Strokes: "+strokes, 3, 13);
    }
    
}
