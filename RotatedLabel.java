package projektant;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.border.Border;

public class RotatedLabel extends JLabel implements ComponentsInterface {

	private static final long serialVersionUID = 1L;
	private int rotation;
	private String fieldName = null;
	public static int count = 1;
	private static List<Integer> freeNumber = new ArrayList<Integer>();
	public Shape shape;
	
	public RotatedLabel(int rot, int sx,int sy,int w, int h) {
        super("");
        String text = "Label";
        if(freeNumber.size() > 0)
        {

    		text += String.valueOf(freeNumber.get(0));
    		freeNumber.remove(0);
        }
        else{
		text += String.valueOf(count++);
        }
        rotation=rot;
    	setBounds(sx, sy, w, h);
    	shape= new Rectangle(sx, sy, w, h);
    	super.setText(text);
    	this.fieldName = text;
    }
	
	public RotatedLabel(String name, String text, int rot, int sx,int sy,int w, int h) {
        super(text);
        rotation=rot;
    	setBounds(sx, sy, w, h);
    	shape= new Rectangle(sx, sy, w, h);
    	this.fieldName = name;
    }

	@Override
	protected void finalize() throws Throwable {
		if(this.fieldName.startsWith("Label")){
			String number = this.fieldName.substring(5);
			try
			{
				freeNumber.add(Integer.parseInt(number));
			}catch(Exception e)
			{
				// nie udało się dodac;
			}
		}
		super.finalize();
	}
	@Override
	public String getName() {
		return fieldName;
	}
	@Override
	public void setName(String fieldName) {
		if(!this.fieldName.equals(fieldName))
		{
		  if(this.fieldName.startsWith("Label")){
			String number = this.fieldName.substring(5);
			try
			{
				freeNumber.add(Integer.parseInt(number));
			}catch(Exception e)
			{
				// nie udało się dodac;
			}
		  }
		  this.fieldName = fieldName;
		}
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
	@Override
	public void setHeight(int h) {
		setSize(getWidth(), h);
		getParent().repaint(2000);
	}
	@Override
    public int getX() {
		return getLocation().x;
	}
	@Override
    public int getY() {
		return getLocation().y;
	}
	@Override
	public void setX(int x) {
		setLocation(x, getY());
		getParent().repaint(2000);
	}
	@Override
	public void setY(int y) {
		setLocation(getX(), y);
		getParent().repaint(2000);
	}
}
