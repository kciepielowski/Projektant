package projektant;

import java.awt.Font;
import java.awt.Point;

import javax.swing.border.Border;
/**
 * Interfejs z funkcjami powiadanymi przez każdy komponent
 */
abstract interface ComponentsInterface {
	  public default boolean gotText() {
			return true;
	  }
	  public default boolean gotRotation(){
			return true;
	  }
	  public default boolean gotFont(){
			return true;
	  }
	  public default boolean gotSize(){
			return true;
	  }
	  public default boolean gotLocation(){
			return true;
	  }
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
	  
	  
}
