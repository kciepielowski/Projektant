package Projektant;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JViewport;
import javax.swing.ScrollPaneConstants;
import javax.swing.ScrollPaneLayout;
import javax.swing.border.Border;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;

public class PMainWindow extends JFrame implements KeyListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	enum MouseMode {MOVE,RESIZE};
	//private JFrame windowFrame;
	private static JScrollPane scrollPanel;
	private static ExtPanel content,page;
	private static JPanel menu, propertiesPanel;
	private static JButton buttonMove,buttonResize;
	static MouseCheck mouseActionCheck;
	private static JComponent componentPointer;
	private static List<JComponent> selectionList;
	private static List<JComponent> componentList = new ArrayList<JComponent>();
	private static JComponent activeComponent;
    private static Rectangle2D selectionRectangle;
    private static Border borderSelected,borderDefault;
	private static MouseMode mouseMode;
	private JTextPane prText,prRotation,prWidth,prHeight;//,prLeft,prTop;
    	
    private class MouseCheck extends MouseAdapter {
	        private boolean mDrag = false;
	    	private int sX,sY,pX,pY;
	    	public MouseCheck(){
	    		sX=0;
	    		sY=0;
	    		pX=0;
	    		pY=0;
	    	}
public void componentMove(JComponent c, int X, int Y){
	((BuildInFunctions)c).setLocation(c.getX()+X,c.getY()+Y);
}
public void componentResize(JComponent c, int X, int Y){
	((BuildInFunctions)c).setSize(c.getWidth()+X,c.getHeight()+Y);
}

	        /**
	         * Sprawdza czy trafiono w komponent 
	         * @param c Wskaźnik na komponent
	         * @param X Położenie X
	         * @param Y Położenie Y
	         * @return zwraca true lub false
	         */
public boolean mouseIn(JComponent c, int X, int Y){
	if(c.contains(X, Y))return true;
	return false;
}
/**
 * Sprawdza czy trafiono w komponent
 * @param c Wskaźnik na komponent
 * @param p Punkt
 * @return zwraca true lub false
 */
public boolean mouseIn(JComponent c, Point p) {
return mouseIn(c,p.x,p.y);
}
	        @Override
	        public void mousePressed(MouseEvent e) {
	        	activeComponent=null;
	        	for(Component c:componentList){
                if(mouseIn((JComponent)c, e.getPoint())){
                	pX=e.getX();
                	pY=e.getY();
                	activeComponent=(JComponent)c;
                	activeComponent.setBorder(borderSelected);
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
	            } else {
	            	if(activeComponent!=null) {
	            		for(Object c: selectionList.toArray()){
		        		((JComponent)c).setBorder(borderDefault);
		        		}
	            		selectionList.clear();
		        	}
	            }

        		setPropertiesPanel();
	            mDrag = false;
	        }

	        @Override
	        public void mouseDragged(MouseEvent e) {
	        	mDrag = true;
	        	if(activeComponent !=null){
	        		for(Object c: selectionList.toArray()){
	        		switch(mouseMode) {
	        		case MOVE:
	        		componentMove((JComponent)c,e.getX()-pX,e.getY()-pY);
	        		break;
	        		case RESIZE:
	        			componentResize((JComponent)c,e.getX()-pX,e.getY()-pY);
	        			break;
	        		default:
	        			break;
	        		}
	        		}
	        		if(selectionList.isEmpty()==false)content.repaint();
	        		pX=e.getX();
	        		pY=e.getY();
	        	} else{
	        		selectionRectangle.setRect(
	        						e.getX()>sX?sX:e.getX(),
	        						e.getY()>sY?sY:e.getY(), 
	        						e.getX()>sX?(e.getX()-sX):(sX-e.getX()),
	    	        				e.getY()>sY?(e.getY()-sY):(sY-e.getY()));
	        		page.setSelectionRectangle(selectionRectangle);
	        		selectionList.clear();
	        		activeComponent=null;
	        		propertiesPanel.removeAll();
	        		for(Component c:componentList){
	                	if(selectionRectangle.contains(c.getLocation())){
	                		selectionList.add((JComponent)c);
	            //    		System.out.println(c.getClass());
	                		((JComponent)c).setBorder(borderSelected);
	        			}else {((JComponent)c).setBorder(borderDefault);}
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

					PMainWindow window = new PMainWindow(); //konstruktor
					window.windowInitialize();
					window.addListeners();
					
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
		propertiesPanel = new JPanel();
		content=new ExtPanel();
		page=new ExtPanel();
		scrollPanel=new JScrollPane();
		borderSelected =BorderFactory.createDashedBorder(Color.blue, 5, 5);
		borderDefault =BorderFactory.createDashedBorder(Color.lightGray, 1, 3);	
		setFocusable(true);
        requestFocusInWindow();

	}
	private void windowInitialize(){
		createWindow();
		createMenu();
		createPropertiesPanel();
		createScrollPanel();
		createPage();
		addRotatedLabel("test",0,45,0,70,15);
		addRotatedLabel("test2", 45,100, 100, 70, 15);
		addRotatedLabel("test3", 180,120, 120, 70, 15);
		//addListeners();
		content.setComponentZOrder(page,content.getComponentCount()-1);
        activeComponent=null;
        mouseMode=MouseMode.MOVE;
		setVisible(true);
	}
	private void createWindow()
	{
		setBounds(100, 100, 900, 300);
		setExtendedState(JFrame.MAXIMIZED_BOTH); 
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout( new BorderLayout());
	}
	private void createMenu()
	{
		
		menu.setBounds(0,0,40,getHeight());
		menu.setPreferredSize(new Dimension(40,getHeight()));
		menu.setBorder(BorderFactory.createRaisedBevelBorder());
		menu.setLayout(new FlowLayout());
		add(menu,BorderLayout.WEST);
		((FlowLayout)menu.getLayout()).setVgap(2);
		buttonMove = new JButton("");
		ImageIcon iconPointer = new ImageIcon(PMainWindow.class.getResource("/move.png")); 
		buttonMove.setIcon(iconPointer);
		buttonMove.setBorder(borderSelected);
		buttonMove.setContentAreaFilled(false);
		buttonMove.setToolTipText("Move");
		buttonMove.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {

				buttonMove.setBorder(borderSelected);
				buttonResize.setBorder(null);
				mouseMode=MouseMode.MOVE;
			}
		});
		menu.add(buttonMove);
		buttonResize = new JButton("");
		iconPointer = new ImageIcon(PMainWindow.class.getResource("/resize.png"));
		buttonResize.setIcon(iconPointer);
		buttonResize.setToolTipText("Resize");
		buttonResize.setBorder(null);
		buttonResize.setContentAreaFilled(false);
		buttonResize.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				buttonResize.setBorder(borderSelected);
				buttonMove.setBorder(null);
				mouseMode=MouseMode.RESIZE;
			}
		});
		menu.add(buttonResize);
			
		
	}
	private void createPropertiesPanel()
	{
		propertiesPanel.setBounds(0,0,300,getHeight());
		propertiesPanel.setPreferredSize(new Dimension(300,getHeight()));
		propertiesPanel.setBorder(BorderFactory.createRaisedBevelBorder());
		propertiesPanel.setLayout(new FlowLayout());
		((FlowLayout)propertiesPanel.getLayout()).setVgap(2);
		add(propertiesPanel,BorderLayout.EAST);	
		JLabel panelHeader = new JLabel("Components properties");
		propertiesPanel.add(panelHeader);
		
	}
	private void setPropertiesPanel()
	{ 
		boolean text=true,
				size=true,
				rotation=true,
				location=true,
				font=true;
		propertiesPanel.removeAll();
		for(Object l: selectionList.toArray()){
			if(text)text=((BuildInFunctions) l).gotText();
			if(size)size=((BuildInFunctions) l).gotSize();
			if(rotation)rotation=((BuildInFunctions) l).gotRotation();
			if(location)location=((BuildInFunctions) l).gotLocation();
			if(font)font=((BuildInFunctions) l).gotFont();
    		}
    	if (activeComponent !=null) {
    		
    	
		if(text) {
		JLabel lab =new JLabel("Text");
		lab.setSize(40, 15);
		lab.setPreferredSize(lab.getSize());
			propertiesPanel.add(lab);
			prText=new JTextPane();
			prText.setSize(200, 15);
			prText.setPreferredSize(prText.getSize());
			prText.setText(((BuildInFunctions)activeComponent).getText());
			propertiesPanel.add(prText);
			prText.addKeyListener(new KeyListener() {
				@Override
				public void keyReleased(KeyEvent e) {
					if (e.getKeyCode()==10) //Enter
					{

						String txt;
						txt=prText.getText();
						txt=txt.substring(0, txt.length() -1);
						for(Object l: selectionList.toArray()){
							((BuildInFunctions) l).setText(txt);
							}	
						prText.setText(txt);
					}
				}
				@Override
				public void keyTyped(KeyEvent e) {}
				@Override
				public void keyPressed(KeyEvent e) {}
				
			});
		}
		if(size) {
			JLabel lab =new JLabel("Width");
			lab.setSize(50, 15);
			lab.setPreferredSize(lab.getSize());
				propertiesPanel.add(lab);
				prWidth=new JTextPane();
				prWidth.setSize(50, 15);
				prWidth.setPreferredSize(prWidth.getSize());
				prWidth.setText(Integer.toString(((BuildInFunctions)activeComponent).getWidth()));
				propertiesPanel.add(prWidth);
				prWidth.addKeyListener(new KeyListener() {
					@Override
					public void keyReleased(KeyEvent e) {
						if (e.getKeyCode()==10) //Enter
						{
							String w;
						w=prWidth.getText();
						w=w.substring(0, w.length() -1);
							for(Object l: selectionList.toArray()){
								((BuildInFunctions) l).setWidth(Integer.parseInt(w));
							}
							prWidth.setText(w);
							
						}
					}
					@Override
					public void keyTyped(KeyEvent e) {}
					@Override
					public void keyPressed(KeyEvent e) {}
					
				});
				lab =new JLabel("Heigth");
				lab.setSize(50, 15);
				lab.setPreferredSize(lab.getSize());
					propertiesPanel.add(lab);
					prHeight=new JTextPane();
					prHeight.setSize(50, 15);
					
					prHeight.setPreferredSize(prHeight.getSize());
					prHeight.setText(Integer.toString(((BuildInFunctions)activeComponent).getHeight()));
					propertiesPanel.add(prHeight);
					prHeight.addKeyListener(new KeyListener() {
						@Override
						public void keyReleased(KeyEvent e) {
							if (e.getKeyCode()==10) //Enter
							{
								String h;
							h=prHeight.getText();
							h=h.substring(0, h.length() -1);
								for(Object l: selectionList.toArray()){
									((BuildInFunctions) l).setHeight(Integer.parseInt(h));
								}
								prHeight.setText(h);
								
							}
						}
						@Override
						public void keyTyped(KeyEvent e) {}
						@Override
						public void keyPressed(KeyEvent e) {}
						
					});
			}
		if(rotation) {
			JLabel lab =new JLabel("Rotation angle");
			lab.setSize(130, 15);
			lab.setPreferredSize(lab.getSize());
				propertiesPanel.add(lab);
				prRotation=new JTextPane();
				prRotation.setSize(50, 15);
				prRotation.setPreferredSize(prRotation.getSize());
				prRotation.setText(Integer.toString(((BuildInFunctions)activeComponent).getRotation()));
				propertiesPanel.add(prRotation);
				prRotation.addKeyListener(new KeyListener() {
					@Override
					public void keyReleased(KeyEvent e) {
						if (e.getKeyCode()==10) //Enter
						{
							String rot;
						rot=prRotation.getText();
						rot=rot.substring(0, rot.length() -1);
							for(Object l: selectionList.toArray()){
								((BuildInFunctions) l).setRotation(Integer.parseInt(rot));
							}
						prRotation.setText(rot);
							
						}
					}
					@Override
					public void keyTyped(KeyEvent e) {}
					@Override
					public void keyPressed(KeyEvent e) {}
					
				});
			}
		//,prHeigth,prLeft,prTop
    	}
		propertiesPanel.repaint();
    	
	}
	private void createScrollPanel()
	{
		scrollPanel.setLayout(new ScrollPaneLayout());
		content.setBackground(Color.lightGray);
		content.setLayout(null);
		scrollPanel.setViewportView(content);
		//ustawienie trybu przewijania na przemalowywanie wszystkich komponentów,
		//innaczej obrócone labelki się rozwalają
		scrollPanel.getViewport().setScrollMode(JViewport.SIMPLE_SCROLL_MODE);
		scrollPanel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPanel.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		add(scrollPanel);
	}
	private void createPage()
	{
		//A4 96CPI 794 x 1123;
				page.setBackground(Color.white);
				page.setBounds(0,0,794,1123);
				page.setBorder(borderDefault);
				page.setLayout(null);
				content.add(page);
				content.setPreferredSize(new Dimension(scrollPanel.getWidth(),page.getHeight()));
	}
	private void addRotatedLabel(String text, int rot, int sx, int sy, int w, int h)
	{
		componentPointer=new RotatedLabel(text,rot,sx,sy,w,h);
		componentList.add(componentPointer);
		content.add(componentPointer);
		content.setComponentZOrder(componentPointer, 0);
	}
	private void addListeners() {
		addKeyListener(this);
		content.addMouseListener(mouseActionCheck);
		content.addMouseMotionListener(mouseActionCheck);
	}
	@Override
	public void keyReleased(KeyEvent e) {
		//System.out.println(e.getKeyCode());
		if (e.getKeyCode()==123) //F12 pełny ekran
		{
			setExtendedState(JFrame.MAXIMIZED_BOTH);

			dispose();
			if(isUndecorated()==false)	
			setUndecorated(true);
			else
			setUndecorated(false);
			setVisible(true);
		}
	}
	@Override
	public void keyTyped(KeyEvent e) {}
	@Override
	public void keyPressed(KeyEvent e) {}
}
