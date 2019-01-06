package Projektant;

import java.awt.Font;

abstract interface BuildInFunctions {
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
			  public default void setText(String text) {};
			  public default String getText(){
				return null;};
			  public default void setRotation(int rot){};
			  public abstract int getWidth();
			  public abstract void setWidth(int w);
			  public abstract int getHeight();
			  public abstract void setHeight(int h);
			  public default int getRotation(){
				  return 0;
			  };
			  public default void setFont(Font font){};
			  public default void setSize(int width, int heigth){};
			  public default void setLocation(int x, int y){}
			
}
