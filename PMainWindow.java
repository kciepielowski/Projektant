package Projektant;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.ScrollPaneLayout;
import javax.swing.border.Border;
import javax.swing.BorderFactory;
import javax.swing.JComponent;

public class PMainWindow extends JPanel implements KeyListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static JFrame windowFrame;
	private static JScrollPane scrollPanel;
	private static ExtPanel content,page;
	private static JPanel menu;
	static MouseCheck mouseActionCheck;
	private static JComponent componentPointer;
	private static List<JComponent> selectionList;
	private static List<JComponent> componentList = new ArrayList<JComponent>();
	private static JComponent activeComponent;
    private static Rectangle2D selectionRectangle;
    private static Border borderSelected,borderDefault;
    	
    private class MouseCheck extends MouseAdapter {
	        private boolean mDrag = false;
	    	private int sX,sY,pX,pY;
	    	public MouseCheck(){
	    		sX=0;
	    		sY=0;
	    		pX=0;
	    		pY=0;
	    	}
public void ComponentMove(JComponent c, int X, int Y){
	c.setLocation(c.getX()+X,c.getY()+Y);
}
	        /**
	         * Sprawdza czy trafiono w komponent 
	         * @param c Wskaźnik na komponent
	         * @param X Położenie X
	         * @param Y Położenie Y
	         * @return zwraca true lub false
	         */
public boolean MouseIn(JComponent c, int X, int Y){
	try{
	if(((RotatedLabel)c).shape.getBounds().contains(X-c.getX(), Y-c.getY()))
	{return true;
			}}
	catch(ClassCastException e){
	if(c.getBounds().contains(X, Y))return true;
	}
	return false;
}
/**
 * Sprawdza czy trafiono w komponent
 * @param c Wskaźnik na komponent
 * @param p Punkt
 * @return zwraca true lub false
 */
public boolean MouseIn(JComponent c, Point p) {
return MouseIn(c,p.x,p.y);
}
	        @Override
	        public void mousePressed(MouseEvent e) {
	        	activeComponent=null;
	        	for(Component c:componentList){
                if(MouseIn((JComponent)c, e.getPoint())){
                	pX=e.getX();
                	pY=e.getY();
                	activeComponent=(JComponent)c;
                	activeComponent.setBorder(borderSelected);
                	content.repaint();
                	if(!selectionList.contains((JComponent)c)){
                		for(Object l: selectionList.toArray()){
		        		((JComponent)l).setBorder(borderDefault);
		        		}
	            		selectionList.clear();
                		selectionList.add((JComponent)c);
                		}
                }
	        	}
	        	if(activeComponent==null){
                	sX=e.getX();
                	sY=e.getY();
                	pX=sX;
                	pY=sY;
                	selectionRectangle = new Rectangle2D.Double(sY,sX, 0, 0);
                }  	
	        }
	        @Override
	        public void mouseReleased(MouseEvent e) {
            	if(selectionList.size()>0 && activeComponent==null)
            		activeComponent=selectionList.get(selectionList.size()-1);
	            if (mDrag) {
	            	selectionRectangle = new Rectangle2D.Double(sY,sX, 0, 0);
	            	page.setSelectionRectangle(selectionRectangle);
	            	content.repaint();
	            } else {
	            	if(activeComponent!=null) {
	            		for(Object c: selectionList.toArray()){
		        		((JComponent)c).setBorder(borderDefault);
		        		content.repaint();
		        		}
	            		selectionList.clear();
		        	}
	            }
	            mDrag = false;
	        }

	        @Override
	        public void mouseDragged(MouseEvent e) {
	        	mDrag = true;
	        	if(activeComponent !=null){
	        		for(Object c: selectionList.toArray()){
	        		ComponentMove((JComponent)c,e.getX()-pX,e.getY()-pY);
	        		content.repaint(); //full repaint
	        		}
	        		pX=e.getX();
	        		pY=e.getY();
	        	} else{
	        		selectionRectangle.setRect(
	        						e.getX()>sX?sX:e.getX(),
	        						e.getY()>sY?sY:e.getY(), 
	        						e.getX()>sX?(e.getX()-sX):(sX-e.getX()),
	    	        				e.getY()>sY?(e.getY()-sY):(sY-e.getY()));
	        		page.setSelectionRectangle(selectionRectangle);
	        		content.repaint();
	        		selectionList.clear();
	        		activeComponent=null;
	        		for(Component c:componentList){
	                	if(selectionRectangle.contains(c.getLocation())){
	                		selectionList.add((JComponent)c);
	                		((JComponent)c).setBorder(borderSelected);
	        			}else {((JComponent)c).setBorder(borderDefault);content.repaint();}
		        	}
	        	}
	        }
    } //koniec wew. klasy
	 
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					windowInitialize();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public PMainWindow() {
		mouseActionCheck = new MouseCheck();
		selectionList = new ArrayList<JComponent>();
		menu = new JPanel();
		content=new ExtPanel();
		page=new ExtPanel();
		scrollPanel=new JScrollPane();
		borderSelected =BorderFactory.createDashedBorder(Color.blue, 5, 5);
		borderDefault =BorderFactory.createDashedBorder(Color.lightGray, 1, 3);	
		addListeners();
	}
	private static void windowInitialize(){
		windowFrame = new JFrame("Test przeciągania");
		windowFrame.setBounds(100, 100, 900, 300);
		windowFrame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
		//usunięcie obramowania
		//WindowFrame.setUndecorated(true);
		//WindowFrame.setLocationRelativeTo(null);
		windowFrame.setContentPane(new PMainWindow());
		windowFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		windowFrame.setLayout( new BorderLayout());
		scrollPanel.setLayout(new ScrollPaneLayout());
		menu.setBounds(0,0,70,windowFrame.getHeight());
		menu.setPreferredSize(new Dimension(70,windowFrame.getHeight()));
		menu.setBorder(BorderFactory.createRaisedBevelBorder());
		windowFrame.add(menu,BorderLayout.WEST);
		//A4 96CPI 794 x 1123;
		page.setBackground(Color.white);
		page.setBounds(0,0,794,1123);
		page.setBorder(borderDefault);
		page.setLayout(null);
		content.setBackground(Color.lightGray);
		content.setLayout(null);
		content.add(page);
		content.setPreferredSize(new Dimension(scrollPanel.getWidth(),page.getHeight()));
		scrollPanel.setViewportView(content);
		scrollPanel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPanel.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		windowFrame.add(scrollPanel);
		componentPointer=new RotatedLabel("test",0,45,0,70,15);
		componentList.add(componentPointer);
		content.add(componentPointer);
		componentPointer= new RotatedLabel("test2", 45,100, 100, 70, 15);
		componentList.add( componentPointer);
		content.add(componentPointer);
		content.addMouseListener(mouseActionCheck);
		content.addMouseMotionListener(mouseActionCheck);
		content.setComponentZOrder(page,content.getComponentCount()-1);
		for(Component c:componentList){
			content.setComponentZOrder(c,0);
			((JComponent)c).setBorder(borderDefault);
    	}
        activeComponent=null;
		windowFrame.setVisible(true);
		
	}
	private void addListeners()
	{
		windowFrame.addKeyListener(this);
	}
	@Override
	public void keyReleased(KeyEvent e) {
		//System.out.println(e.getKeyCode());
		if (e.getKeyCode()==123) //F12 pełny ekran
		{
			windowFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);

			windowFrame.dispose();
			if(windowFrame.isUndecorated()==false)	
			windowFrame.setUndecorated(true);
			else
			windowFrame.setUndecorated(false);

			windowFrame.setVisible(true);
		}
	}
	@Override
	public void keyTyped(KeyEvent e) {}
	@Override
	public void keyPressed(KeyEvent e) {}
}
