package Projektant;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;

import javax.swing.JLabel;

public class RotatedLabel extends JLabel {

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
    public void paintComponent(Graphics g) {
    	AffineTransform af = new AffineTransform();
    	af.rotate(Math.toRadians(rotation), getWidth() / 2, getHeight() / 2);
    	shape=af.createTransformedShape(new Rectangle(0, 0, getWidth(), getHeight()));
    	g.setClip(shape);
        ((Graphics2D)g).rotate(Math.toRadians(rotation), getWidth() / 2, getHeight() / 2);
        super.paintComponent(g);
       // cofnięcie obrócenia jeśli nie pochylać obramowania
       // ((Graphics2D)g).rotate(Math.toRadians(-rotation), getWidth() / 2, getHeight() / 2);
    }

}
