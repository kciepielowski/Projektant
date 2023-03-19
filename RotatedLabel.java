package projektant;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JTextPane;
import javax.swing.border.Border;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BoxView;
import javax.swing.text.ComponentView;
import javax.swing.text.Element;
import javax.swing.text.IconView;
import javax.swing.text.LabelView;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.ParagraphView;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.text.StyledEditorKit;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;


enum VerticalAlignment {TOP, CENTER, BOTTOM};

public class RotatedLabel extends JTextPane implements ComponentsInterface {

	private static final long serialVersionUID = 1L;
	private int rotation;
	private String fieldName = null;
	public static int count = 1;
	private VerticalAlignment valigmnet = VerticalAlignment.TOP;
	private static List<Integer> freeNumber = new ArrayList<Integer>();
	public Shape shape;
	public boolean gotText() {return true;}
	public boolean gotRotation(){return true;}
	public boolean gotFont(){return true;}
	public boolean gotSize(){return true;}
	public boolean gotLocation(){return true;}
	public boolean gotBorder(){return true;}
	public boolean gotBackground(){return true;}
	public boolean gotForeground(){return true;}
	public boolean gotVerticalAlignment(){return true;}
	public boolean gotHorizontalAlignment(){return true;}
	
	public RotatedLabel(int rot, int sx,int sy,int w, int h) {
        super();
        String text = "Label";
        if(freeNumber.size() > 0)
        {
        	Collections.sort(freeNumber);
    		text += String.valueOf(freeNumber.get(0));
    		freeNumber.remove(0);
        }
        else{
		    text += String.valueOf(count++);
        }
        rotation=rot;
    	setBounds(sx, sy, w, h);
    	shape= new Rectangle(sx, sy, w, h);
        setEditorKit(new VerticalAlignmentEditorKit(valigmnet));
    	super.setText(text);
    	super.setEditable(false);
    	this.fieldName = text;
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
    	if(rotation!=0){
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
	
	@Override
	public String getFontName() {
	   Font font = super.getFont();
	   return font.getName();
	}
	
	@Override
	public void setFontName(String name) {
		   Font newFont = new Font(name, super.getFont().getStyle(), super.getFont().getSize());
		   super.setFont(newFont);
	}
	
	@Override
	public int getFontSize() {
	   Font font = super.getFont();
	   return font.getSize();
	}
	
	@Override
	public void setFontSize(int size) {
		   Font newFont = new Font(super.getFont().getName(), super.getFont().getStyle(), size);
		   super.setFont(newFont);
	}
	
	@Override
	public boolean isFontBold() {
	   int fontStyle = super.getFont().getStyle();
	   return (fontStyle == Font.BOLD || fontStyle == (Font.BOLD + Font.ITALIC));
	}
	
	@Override
	public void setFontBold(boolean bold) {
		int oldFontStyle = super.getFont().getStyle();
		int newFontStyle = Font.BOLD;
		if(bold) {
		    switch(oldFontStyle){
		    case Font.ITALIC:
		  	    newFontStyle = Font.ITALIC + Font.BOLD;
		  	    break;
	        default:
			    newFontStyle = Font.BOLD;
			    break;
		    }
		}else {
		    switch(oldFontStyle){
		    case Font.ITALIC + Font.BOLD:
		  	    newFontStyle = Font.ITALIC;
		  	    break;
	        default:
			    newFontStyle = Font.PLAIN;
			    break;
		    }
		}
		   Font newFont = new Font(super.getFont().getName(), newFontStyle, super.getFont().getSize());
		   super.setFont(newFont);
	}
	
	@Override
	public boolean isFontItalic() {
	   int fontStyle = super.getFont().getStyle();
	   return (fontStyle == Font.ITALIC || fontStyle == (Font.BOLD + Font.ITALIC));
	}
	
	@Override
	public void setFontItalic(boolean italic) {
		int oldFontStyle = super.getFont().getStyle();
		int newFontStyle = Font.ITALIC;
		if(italic){
		    switch(oldFontStyle){
		    case Font.BOLD:
		  	    newFontStyle = Font.ITALIC + Font.BOLD;
		  	    break;
	        default:
			    newFontStyle = Font.ITALIC;
			    break;
		    }
		}else{
		    switch(oldFontStyle){
		    case Font.ITALIC + Font.BOLD:
		  	    newFontStyle = Font.BOLD;
		  	    break;
	        default:
			    newFontStyle = Font.PLAIN;
			    break;
		    }
		}
		   Font newFont = new Font(super.getFont().getName(), newFontStyle, super.getFont().getSize());
		   super.setFont(newFont);
	}
	
	@Override
	public String getHorizontalAlignment() {
		MutableAttributeSet set = new SimpleAttributeSet(this.getParagraphAttributes());
		int alignment = StyleConstants.getAlignment(set);
		String result = "Right";
		if (alignment == StyleConstants.ALIGN_LEFT) result = "Left";
		else if (alignment == StyleConstants.ALIGN_CENTER) result = "Center";
		else if (alignment == StyleConstants.ALIGN_RIGHT) result = "Right";
		return result;
	}
	
	@Override
	public void setHorizontalAlignment(String alignment) {
		int result = 2;
		StyledDocument doc = this.getStyledDocument();
		MutableAttributeSet set = new SimpleAttributeSet(this.getParagraphAttributes());
		if ( "Left".equals(alignment) ) result = StyleConstants.ALIGN_LEFT;
		else if ("Center".equals(alignment)) result = StyleConstants.ALIGN_CENTER;
		else if ("Right".equals(alignment)) result = StyleConstants.ALIGN_RIGHT;
		StyleConstants.setAlignment(set,result);
		//this.selectAll();
	//	this.setCharacterAttributes(set, false);
	    doc.setParagraphAttributes(0, doc.getLength(), set, false);
	}
	
	@Override
	public String getVerticalAlignment() {
		String result = "Top";
		if (valigmnet == VerticalAlignment.BOTTOM) result = "Bottom";
		else if (valigmnet == VerticalAlignment.CENTER) result = "Center";
		else if (valigmnet == VerticalAlignment.TOP) result = "Top";
		return result;
	}
	
	@Override
	public void setVerticalAlignment(String alignment) {
		if ( "Bottom".equals(alignment) ) valigmnet = VerticalAlignment.BOTTOM;
		else if ("Center".equals(alignment)) valigmnet = VerticalAlignment.CENTER;
		else if ("Top".equals(alignment)) valigmnet = VerticalAlignment.TOP;
		String text = super.getText();
		String aligment = getHorizontalAlignment();
        setEditorKit(new VerticalAlignmentEditorKit(valigmnet));
        super.setText(text);
        setHorizontalAlignment(aligment);
	}
	
}

class VerticalAlignmentEditorKit extends StyledEditorKit {
	private static final long serialVersionUID = 1L;
	private VerticalAlignment V_ALIGMENT;
	public VerticalAlignmentEditorKit(){
		super();
		V_ALIGMENT = VerticalAlignment.TOP;
	}
	
	public VerticalAlignmentEditorKit(VerticalAlignment valigment){
		super();
		V_ALIGMENT = valigment;
	}	
	
	public ViewFactory getViewFactory() {
        return new StyledViewFactory(V_ALIGMENT);
    }

    class StyledViewFactory implements ViewFactory {
    	private VerticalAlignment V_ALIGMENT;
    	public StyledViewFactory(VerticalAlignment valigment){
    		V_ALIGMENT = valigment;
    	}
        public View create(Element elem) {
            String kind = elem.getName();
            if (kind != null) {
                if (kind.equals(AbstractDocument.ContentElementName)) {
                    return new LabelView(elem);
                } else if (kind.equals(AbstractDocument.ParagraphElementName)) {
                    return new ParagraphView(elem);
                } else if (kind.equals(AbstractDocument.SectionElementName)) {
                    return new CenteredBoxView(elem, View.Y_AXIS, V_ALIGMENT);
                } else if (kind.equals(StyleConstants.ComponentElementName)) {
                    return new ComponentView(elem);
                } else if (kind.equals(StyleConstants.IconElementName)) {
                    return new IconView(elem);
                }
            }
            return new LabelView(elem);
        }
    }
}

class CenteredBoxView extends BoxView {
	private VerticalAlignment V_ALIGMENT = VerticalAlignment.TOP;
    public CenteredBoxView(Element elem, int axis, VerticalAlignment valigment) {
        super(elem, axis);
        V_ALIGMENT = valigment;
    }

    protected void layoutMajorAxis(int targetSpan, int axis, int[] offsets, int[] spans) {

        super.layoutMajorAxis(targetSpan, axis, offsets, spans);
        int textBlockHeight = 0;
        int offset = 0;

        for (int i = 0; i < spans.length; i++) {

            textBlockHeight += spans[i];
        }
        if(V_ALIGMENT == VerticalAlignment.TOP){
        	offset = 0;
        }else
        {
        	if(V_ALIGMENT == VerticalAlignment.BOTTOM)
        	{
                offset = targetSpan - textBlockHeight;
        	}
        	else if(V_ALIGMENT == VerticalAlignment.CENTER)
        	{
                offset = (targetSpan - textBlockHeight)/2;
        	}
            for (int i = 0; i < offsets.length; i++) {
                offsets[i] += offset;
            }
        }

    }
}
