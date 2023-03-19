package projektant;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;

import javax.swing.border.Border;
/**
 * Interfejs z funkcjami powiadanymi przez każdy komponent
 */
abstract interface ComponentsInterface {
	
	  public default boolean gotText(){return false;}
	  public default boolean gotRotation(){return false;}
	  public default boolean gotFont(){return false;}
	  public default boolean gotSize(){return false;}
	  public default boolean gotLocation(){return false;}
	  public default boolean gotBorder(){return false;}
	  public default boolean gotBackground(){return false;}
	  public default boolean gotForeground(){return false;}
	  public default boolean gotVerticalAlignment(){return false;}
	  public default boolean gotHorizontalAlignment(){return false;}
	  
	  public abstract void setName(String s);
	  public abstract String getName();
	  public default void setText(String text) {}
	  public default String getText(){
			return null;
	  }
	  public default void setRotation(int rot){}
	  public abstract int getWidth();
	  public abstract void setWidth(int w);
	  public abstract int getHeight();
	  public abstract void setHeight(int h);
	  public default int getRotation(){
		  	return 0;
	  }
	  public default void setFont(Font font){}
	  public default void setSize(int width, int heigth){}
	  public default void setLocation(int x, int y){}
	  public abstract int getX();
	  public abstract int getY();
	  public abstract void setX(int x);
	  public abstract void setY(int y); 
	  public abstract boolean contains(int x, int y);
	  public abstract void setBorder(Border b);
	  public abstract Point getLocation();
	  public abstract String getFontName();
	  public abstract void setFontName(String name);
	  public abstract int getFontSize();
	  public abstract void setFontSize(int size);
	  public abstract boolean isFontBold();
	  public abstract void setFontBold(boolean bold);
	  public abstract boolean isFontItalic();
	  public abstract void setFontItalic(boolean italic);
	  public abstract Color getBackground();
	  public abstract void setBackground(Color color);
	  public abstract Color getForeground();
	  public abstract void setForeground(Color color);
	  public abstract String getVerticalAlignment();
	  public abstract void setVerticalAlignment(String alignment);
	  public abstract String getHorizontalAlignment();
	  public abstract void setHorizontalAlignment(String alignment);
	  
	  
}
