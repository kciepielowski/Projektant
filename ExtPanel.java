package Projektant;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

public class ExtPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	public static Rectangle2D selectionRectangle;
	@Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (selectionRectangle != null) {
        	((Graphics2D)g).setColor(new Color(100, 100, 200, 100));
        	((Graphics2D)g).fill(selectionRectangle);
        }
	}
	/**
	 * Ustawienie wskaźnika na prostokąt z zaznaczeniem
	 * @param Rectangle2D r	wskaźnik na prostokąt
	*/
	public void setSelectionRectangle(Rectangle2D r)
	{
		selectionRectangle=r;
		getParent().repaint(500);
	}
}
