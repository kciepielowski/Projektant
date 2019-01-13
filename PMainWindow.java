package Projektant;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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

public class PMainWindow extends JFrame implements KeyListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	enum MouseMode {MOVE,RESIZE};
	//private JFrame windowFrame;
	private static JScrollPane scrollPanel;
	protected ExtPanel content,page;
	protected JPanel menu, propertiesPanel;
	private static JButton buttonMove,buttonResize;
	static MouseCheck mouseActionCheck;
	private static ComponentsInterface componentPointer;
	private static Band bandPointer;
	protected List<ComponentsInterface> selectionList;
	protected List<ComponentsInterface> componentList = new ArrayList<ComponentsInterface>();
	protected List<Band> bandList = new ArrayList<Band>();
	protected ComponentsInterface activeComponent;
	//private static Band activeBand;
    protected Border borderSelected,borderDefault;
	private JTextPane prText,prRotation,prWidth,prHeight;//,prLeft,prTop;
    		 
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
		mouseActionCheck = new MouseCheck(this);
		selectionList = new ArrayList<ComponentsInterface>();
		menu = new JPanel();
		propertiesPanel = new JPanel();
		content=new ExtPanel();
		page=new ExtPanel();
		scrollPanel=new JScrollPane();
		borderSelected =BorderFactory.createDashedBorder(Color.blue, 5, 5);
		borderDefault =BorderFactory.createDashedBorder(Color.lightGray, 1, 3);	
		setFocusable(true);
		content.setFocusable(true);
        requestFocusInWindow();

	}
	private void windowInitialize(){
		createWindow();
		createMenu();
		createPropertiesPanel();
		createScrollPanel();
		createPage();
		bandPointer = new Band("Band name");
		bandPointer.setBackground(Color.white);
		bandPointer.setBorder(borderDefault);
		bandPointer.setLayout(null);
		bandList.add(bandPointer);
		content.add(bandPointer);
		addRotatedLabel("test",0,45,0,70,15);
		addRotatedLabel("test2", 45,100, 100, 70, 15);
		addRotatedLabel("test3", 180,120, 120, 70, 15);
		content.setComponentZOrder(bandPointer,content.getComponentCount()-2);
        activeComponent=null;
//        activeBand=null;
        mouseActionCheck.mouseMode=MouseMode.MOVE;
		bandPointer.setBounds(0,0,page.getWidth(),200);
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
				mouseActionCheck.mouseMode=MouseMode.MOVE;
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
				mouseActionCheck.mouseMode=MouseMode.RESIZE;
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
	protected void setPropertiesPanel()
	{ 
		boolean text=true,
				size=true,
				rotation=true,
				location=true,
				font=true;
		propertiesPanel.removeAll();
		for(ComponentsInterface l: selectionList){
			if(text)text=l.gotText();
			if(size)size=l.gotSize();
			if(rotation)rotation=l.gotRotation();
			if(location)location=l.gotLocation();
			if(font)font=l.gotFont();
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
			prText.setText(activeComponent.getText());
			propertiesPanel.add(prText);
			prText.addKeyListener(new KeyListener() {
				@Override
				public void keyReleased(KeyEvent e) {
					if (e.getKeyCode()==10) //Enter
					{

						String txt;
						txt=prText.getText();
						txt=txt.substring(0, txt.length() -1);
						for(ComponentsInterface l: selectionList){
							l.setText(txt);
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
				prWidth.setText(Integer.toString(activeComponent.getWidth()));
				propertiesPanel.add(prWidth);
				prWidth.addKeyListener(new KeyListener() {
					@Override
					public void keyReleased(KeyEvent e) {
						if (e.getKeyCode()==10) //Enter
						{
							String w;
						w=prWidth.getText();
						w=w.substring(0, w.length() -1);
							for(ComponentsInterface l: selectionList){
								l.setWidth(Integer.parseInt(w));
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
					prHeight.setText(Integer.toString(activeComponent.getHeight()));
					propertiesPanel.add(prHeight);
					prHeight.addKeyListener(new KeyListener() {
						@Override
						public void keyReleased(KeyEvent e) {
							if (e.getKeyCode()==10) //Enter
							{
								String h;
							h=prHeight.getText();
							h=h.substring(0, h.length() -1);
								for(ComponentsInterface l: selectionList){
									l.setHeight(Integer.parseInt(h));
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
				prRotation.setText(Integer.toString(activeComponent.getRotation()));
				propertiesPanel.add(prRotation);
				prRotation.addKeyListener(new KeyListener() {
					@Override
					public void keyReleased(KeyEvent e) {
						if (e.getKeyCode()==10) //Enter
						{
							String rot;
						rot=prRotation.getText();
						rot=rot.substring(0, rot.length() -1);
							for(ComponentsInterface l: selectionList){
								l.setRotation(Integer.parseInt(rot));
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
		/* 
		 * TODO prLeft,prTop, border, align, font, name
		 * band properties
		 */
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
		content.add((Component)componentPointer);
		content.setComponentZOrder((Component)componentPointer, 0);
	}
	private void addListeners() {
		addKeyListener(this);
		content.addKeyListener(this); // dodanie do samego okna nie zawsze działa
		content.addMouseListener(mouseActionCheck);
		content.addMouseMotionListener(mouseActionCheck);
	}
	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode()==KeyEvent.VK_F12) //pełny ekran
		{
			setExtendedState(JFrame.MAXIMIZED_BOTH);
			dispose(); //innaczej nie można włączyć/wyłączyć obramowania
			if(isUndecorated()==false)	
			setUndecorated(true);
			else
			setUndecorated(false);
			setVisible(true);  // narysuj ponownie
		}
	}
	@Override
	public void keyTyped(KeyEvent e) {}
	@Override
	public void keyPressed(KeyEvent e) {}
}
