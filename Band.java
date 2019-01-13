package Projektant;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

public class Band extends ExtPanel{
	private static final long serialVersionUID = 1L;
	private String name;
	protected List<ComponentsInterface> componentsList= new ArrayList<ComponentsInterface>();
	public Band(String n) {
		super();
		name=n;
	}
	@Override
	protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.setColor(new Color(100,100,100,100));
			g.drawString(name, 5, getHeight()-5);
			}
	
	public void setHeight(int h) {
		setSize(getWidth(), h);
		getParent().repaint(2000);	
	}
	public void add(ComponentsInterface c)
	{
		componentsList.add(c);
	}
	public void removeAll()
	{
		componentsList.clear();
	}
	public void remove(ComponentsInterface c)
	{
		componentsList.remove(c);
	}
}
