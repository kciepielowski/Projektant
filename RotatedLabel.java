package Projektant;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;

import javax.swing.JLabel;
import javax.swing.border.Border;

public class RotatedLabel extends JLabel implements BuildInFunctions {

	private static final long serialVersionUID = 1L;
	private int rotation;
	public Shape shape;

	public RotatedLabel(String text, int rot, int sx,int sy,int w, int h) {
        super(text);
        rotation=rot;
    	setBounds(sx, sy, w, h);
    	shape= new Rectangle(sx, sy, w, h);
    }
	@Override
	public boolean contains(int x, int y) {
		return shape.contains(x-getX(), y-getY());
	}
	@Override
	public void setBorder(Border border) {
		super.setBorder(border);
		if(rotation!=0)getParent().repaint(2000);
	}
	@Override
	public void setLocation(int x, int y) {
		super.setLocation(x, y);
		if(rotation!=0)getParent().repaint(2000);
	}
	@Override
	public void setRotation(int rot) {

        rotation=rot;
		getParent().repaint(2000);
	}
	@Override
    public int getRotation() {
		return rotation;
	}
    @Override
    public void paintComponent(Graphics g) {
    	if(rotation!=0)
    	{
    	AffineTransform af = new AffineTransform();
    	af.rotate(Math.toRadians(rotation), getWidth() / 2, getHeight() / 2);
    	shape=af.createTransformedShape(new Rectangle(0, 0, getWidth(), getHeight()));
    	g.setClip(shape);
        ((Graphics2D)g).rotate(Math.toRadians(rotation), getWidth() / 2, getHeight() / 2);
    	} else shape=getVisibleRect();
    	super.paintComponent(g);
       // cofnięcie obrócenia jeśli nie pochylać obramowania
       // ((Graphics2D)g).rotate(Math.toRadians(-rotation), getWidth() / 2, getHeight() / 2);
    }
	@Override
	public void setWidth(int w) {
		setSize(w, getHeight());
		getParent().repaint(2000);
		
	}
	public void setHeight(int h) {
		setSize(getWidth(), h);
		getParent().repaint(2000);
		
	}

}
