package projektant;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JViewport;
import javax.swing.ScrollPaneConstants;
import javax.swing.ScrollPaneLayout;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;

public class PMainWindow extends JFrame implements KeyListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    private static final int TEXT_FIELD_HEIGHT = 20;
    private static final int SMALL_TEXT_FIELD_WIDTH = 70;    
    private static final int VERTIVAL_SPACE_BETWEEN_FIELDS = 5;  
	enum MouseMode {MOVE, RESIZE, LABEL};
	//private JFrame windowFrame;
	private static JScrollPane scrollPanel;
	protected ExtPanel content, page;
	protected JPanel menu, propertiesPanel;
	static MouseCheck mouseActionCheck;
	private static ComponentsInterface componentPointer;
	private static Band bandPointer;
	protected List<ComponentsInterface> selectionList;
	protected List<ComponentsInterface> componentList = new ArrayList<ComponentsInterface>();
	protected List<Band> bandList = new ArrayList<Band>();
	protected ComponentsInterface activeComponent;
	//private static Band activeBand;
    protected Border borderSelected, borderDefault;
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
		addRotatedLabel(0, 45, 0, 70, 15);
		addRotatedLabel(45,100, 100, 70, 15);
		addRotatedLabel(180,120, 120, 70, 15);
		content.setComponentZOrder(bandPointer,content.getComponentCount()-2);
        activeComponent=null;
//        activeBand=null;
        mouseActionCheck.mouseMode=MouseMode.MOVE;
		bandPointer.setBounds(0,0,page.getWidth(),200);
		setVisible(true);
	}
	
	private void createWindow(){
		setBounds(100, 100, 900, 300);
		setExtendedState(JFrame.MAXIMIZED_BOTH); 
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout( new BorderLayout());
	}
	
	private void createMenu(){
		menu.setBounds(0,0,40,getHeight());
		menu.setPreferredSize(new Dimension(40,getHeight()));
		menu.setBorder(BorderFactory.createRaisedBevelBorder());
		menu.setLayout(new FlowLayout());
		add(menu,BorderLayout.WEST);
		((FlowLayout)menu.getLayout()).setVgap(5);
		addMenuButton("/move.png", "Move", MouseMode.MOVE);
		addMenuButton("/resize.png", "Resize", MouseMode.RESIZE);
		addMenuButton("/label.png", "Label", MouseMode.LABEL);
	}
	
	private void addMenuButton(String icon, String tooltip, MouseMode mode){
		JButton tmpButton;
		tmpButton = new JButton("");
		ImageIcon iconPointer = new ImageIcon(PMainWindow.class.getResource(icon)); 
		tmpButton.setIcon(iconPointer);
		tmpButton.setBorder(borderSelected);
		tmpButton.setContentAreaFilled(false);
		tmpButton.setToolTipText(tooltip);
		tmpButton.addActionListener(new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent e) {
				for(Component c:  menu.getComponents()){
					if(c instanceof JButton){
						((JButton) c).setBorder(null);
					}
				}
				((JButton)e.getSource()).setBorder(borderSelected);
				mouseActionCheck.mouseMode=mode;
			    requestFocusInWindow();
			}
		});
		menu.add(tmpButton);
	}
	
	private void createPropertiesPanel(){
		propertiesPanel.setBounds(0,0,300,getHeight());
		propertiesPanel.setPreferredSize(new Dimension(300,getHeight()));
		propertiesPanel.setBorder(BorderFactory.createRaisedBevelBorder());
		propertiesPanel.setLayout(new FlowLayout());
		((FlowLayout)propertiesPanel.getLayout()).setVgap(VERTIVAL_SPACE_BETWEEN_FIELDS);
		add(propertiesPanel,BorderLayout.EAST);	
		JLabel panelHeader = new JLabel("Components properties");
		propertiesPanel.add(panelHeader);
	}
	
	protected void setPropertiesPanel(){ 
		boolean text = true,
				size = true,
				rotation = true,
				verticalAlignment = true,
			    horizontalAlignment = true,
				location = true,
				font = true,
				border = true,
				background = true,
				foreground = true;
		propertiesPanel.removeAll();
		for(ComponentsInterface l: selectionList){
			if(text)text = l.gotText();
			if(size)size = l.gotSize();
			if(rotation)rotation = l.gotRotation();
			if(verticalAlignment)verticalAlignment = l.gotVerticalAlignment();
			if(horizontalAlignment)horizontalAlignment = l.gotHorizontalAlignment();
			if(location)location = l.gotLocation();
			if(font)font = l.gotFont();
			if(border)border = l.gotBorder();
			if(background)background = l.gotBackground();
			if(foreground)foreground = l.gotForeground();
    		}
    	if (activeComponent !=null) {
	        @SuppressWarnings("rawtypes")
			Class[] parameterTypes = new Class[1];
	        Method method = null;
	        parameterTypes[0] = String.class;
			try {
				method = ComponentsInterface.class.getMethod("setName", parameterTypes);
			} catch (NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			}
			addControlPanelTextField("Name"
			         ,60, TEXT_FIELD_HEIGHT
					 ,new JTextPane() 
			         ,225, TEXT_FIELD_HEIGHT
			         ,activeComponent.getName()
			         ,method);
		if(text) {
	        parameterTypes[0] = String.class;
			try {
				method = ComponentsInterface.class.getMethod("setText", parameterTypes);
			} catch (NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			}
			addControlPanelTextField("Text"
			         ,290, TEXT_FIELD_HEIGHT
					 ,new JTextPane()
			         ,290, TEXT_FIELD_HEIGHT*4
			         ,activeComponent.getText()
			         ,method
			         ,SwingConstants.CENTER);
		}
		if(size) {
	        parameterTypes[0] = int.class;
			try {
				method = ComponentsInterface.class.getMethod("setWidth", parameterTypes);
			} catch (NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			}
			addControlPanelTextField("Width"
			         ,50, TEXT_FIELD_HEIGHT
					 ,new JTextPane()
			         ,SMALL_TEXT_FIELD_WIDTH, TEXT_FIELD_HEIGHT
			         ,Integer.toString(activeComponent.getWidth())
			         ,method);
				try {
					method = ComponentsInterface.class.getMethod("setHeight", parameterTypes);
				} catch (NoSuchMethodException | SecurityException e) {
					e.printStackTrace();
				}
				addControlPanelTextField("Height"
				         ,50, TEXT_FIELD_HEIGHT
						 ,new JTextPane()
				         ,SMALL_TEXT_FIELD_WIDTH, TEXT_FIELD_HEIGHT
				         ,Integer.toString(activeComponent.getHeight())
				         ,method);
			}
		if(location) {
	        parameterTypes[0] = int.class;
			try {
				method = ComponentsInterface.class.getMethod("setY", parameterTypes);
			} catch (NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			}
			addControlPanelTextField("Top"
			         ,50, TEXT_FIELD_HEIGHT
					 ,new JTextPane()
			         ,SMALL_TEXT_FIELD_WIDTH, TEXT_FIELD_HEIGHT
			         ,Integer.toString(activeComponent.getY())
			         ,method);
				try {
					method = ComponentsInterface.class.getMethod("setX", parameterTypes);
				} catch (NoSuchMethodException | SecurityException e) {
					e.printStackTrace();
				}
				addControlPanelTextField("Left"
				         ,50, TEXT_FIELD_HEIGHT
						 ,new JTextPane()
				         ,SMALL_TEXT_FIELD_WIDTH, TEXT_FIELD_HEIGHT
				         ,Integer.toString(activeComponent.getX())
				         ,method);
			}
		if(rotation) {
	        parameterTypes[0] = int.class;
			try {
				method = ComponentsInterface.class.getMethod("setRotation", parameterTypes);
			} catch (NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			}
			addControlPanelTextField("Rotation angle"
			         ,140, TEXT_FIELD_HEIGHT
					 ,new JTextPane()
			         ,SMALL_TEXT_FIELD_WIDTH, TEXT_FIELD_HEIGHT
			         ,Integer.toString(activeComponent.getRotation())
			         ,method);
			}
		if(font) {
	        parameterTypes[0] = String.class;
			try {
				method = ComponentsInterface.class.getMethod("setFontName", parameterTypes);
			} catch (NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			}
			String fonts[] = 
				      GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
			addControlPanelComboBoxField("Font name"
			         ,80, TEXT_FIELD_HEIGHT
					 ,new JComboBox<String>()
			         ,fonts
			         ,200, TEXT_FIELD_HEIGHT
			         ,activeComponent.getFontName()
			         ,method);
	        parameterTypes[0] = int.class;
			try {
				method = ComponentsInterface.class.getMethod("setFontSize", parameterTypes);
			} catch (NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			}
			addControlPanelTextField("Font size"
			         ,50, TEXT_FIELD_HEIGHT
					 ,new JTextPane()
			         ,SMALL_TEXT_FIELD_WIDTH, TEXT_FIELD_HEIGHT
			         ,Integer.toString(activeComponent.getFontSize())
			         ,method);

	        parameterTypes[0] = boolean.class;
			try {
				method = ComponentsInterface.class.getMethod("setFontBold", parameterTypes);
			} catch (NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			}
			addControlPanelCheckBox("Bold"
			         ,50, TEXT_FIELD_HEIGHT
					 ,new JCheckBox()
			         ,TEXT_FIELD_HEIGHT, TEXT_FIELD_HEIGHT
			         ,activeComponent.isFontBold()
			         ,method);
			try {
				method = ComponentsInterface.class.getMethod("setFontItalic", parameterTypes);
			} catch (NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			}
			addControlPanelCheckBox("Italic"
					,50, TEXT_FIELD_HEIGHT
					,new JCheckBox()
					,TEXT_FIELD_HEIGHT, TEXT_FIELD_HEIGHT
					,activeComponent.isFontItalic()
		         ,	method);
			}
		if(horizontalAlignment) {
	        parameterTypes[0] = String.class;
			try {
				method = ComponentsInterface.class.getMethod("setHorizontalAlignment", parameterTypes);
			} catch (NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			}
			String alignments[] = {"Left", "Center", "Right" };
			addControlPanelComboBoxField("Align"
			         ,80, TEXT_FIELD_HEIGHT
					 ,new JComboBox<String>()
			         ,alignments
			         ,200, TEXT_FIELD_HEIGHT
			         ,activeComponent.getHorizontalAlignment()
			         ,method);
		}
		if(verticalAlignment) {
	        parameterTypes[0] = String.class;
			try {
				method = ComponentsInterface.class.getMethod("setVerticalAlignment", parameterTypes);
			} catch (NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			}
			String alignments[] = {"Top", "Center", "Bottom"};
			addControlPanelComboBoxField("Vertical align"
			         ,80, TEXT_FIELD_HEIGHT
					 ,new JComboBox<String>()
			         ,alignments
			         ,200, TEXT_FIELD_HEIGHT
			         ,activeComponent.getVerticalAlignment()
			         ,method);
		}

		if(background) {
	        parameterTypes[0] = Color.class;
			try {
				method = ComponentsInterface.class.getMethod("setBackground", parameterTypes);
			} catch (NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			}
			Color c = new Color(activeComponent.getBackground().getRGB());
			if(Objects.isNull(c.getRGB())) c = Color.WHITE;
			addControlPanelColorChooser("Background"
			         ,140, TEXT_FIELD_HEIGHT
			         ,new JButton()
			         ,c
			         ,method);
			}
		if(foreground) {
	        parameterTypes[0] = Color.class;
			try {
				method = ComponentsInterface.class.getMethod("setForeground", parameterTypes);
			} catch (NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			}
			Color c = new Color(activeComponent.getForeground().getRGB());
			addControlPanelColorChooser("Foreground"
			         ,140, TEXT_FIELD_HEIGHT
			         ,new JButton()
			         ,c
			         ,method);
			}
		/* 
		 * TODO  border, align
		 * band properties
		 */
    	}
    	propertiesPanel.validate();
		propertiesPanel.repaint();
	}
	
	private void addControlPanelTextField(final String text
			,final int labLength, final int labHeigth
			,JTextPane field
			,final int fieldLength, final int fieldHeight
			,String fieldValue
			,Method fieldMethod){
		addControlPanelTextField(text
				,labLength, labHeigth
				,field
				,fieldLength,fieldHeight
				,fieldValue
				,fieldMethod
				,SwingConstants.LEFT);
	}
	
	private void addControlPanelTextField(final String text
								         ,final int labLength, final int labHeigth
										 ,JTextPane field
								         ,final int fieldLength, final int fieldHeight
								         ,String fieldValue
								         ,Method fieldMethod
								         ,int alighment){
		JLabel lab =new JLabel(text);
		lab.setSize(labLength, labHeigth);
		lab.setPreferredSize(lab.getSize());
		lab.setHorizontalAlignment(alighment);
			propertiesPanel.add(lab);
			field.setSize(fieldLength, fieldHeight);
			field.setPreferredSize(field.getSize());
			field.setText(fieldValue);
			propertiesPanel.add(field);
			field.addKeyListener(new KeyListener() {
				@Override
				public void keyReleased(KeyEvent e) {
					if (e.getKeyCode()==10) //Enter
					{
					String val;
					val=field.getText();
					@SuppressWarnings("rawtypes")
					Class[] parameterTypes = fieldMethod.getParameterTypes();
					val=val.substring(0, val.length() -1);
						for(ComponentsInterface l: selectionList){
							try {
								if (parameterTypes[0] == int.class){
								fieldMethod.invoke(l, Integer.parseInt(val));
								}else{
									fieldMethod.invoke(l,val);
								}
							} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
								e1.printStackTrace();
							}
						}
						field.setText(val);
						
					}
				}
				
				@Override
				public void keyTyped(KeyEvent e) {}
				
				@Override
				public void keyPressed(KeyEvent e) {}
			});	
	}
	
	private void addControlPanelComboBoxField(final String text
			,final int labLength, final int labHeigth
			,JComboBox<String> field
			,String [] fieldsValuesList
			,final int fieldLength, final int fieldHeight
			,String fieldValue
			,Method fieldMethod){
		JLabel lab =new JLabel(text);
		//JCheckBox
		lab.setSize(labLength, labHeigth);
		lab.setPreferredSize(lab.getSize());
		propertiesPanel.add(lab);
		field.setSize(fieldLength, fieldHeight);
		field.setPreferredSize(field.getSize());
		for(String value : fieldsValuesList)
		{
		field.addItem(value);
		}
		field.setSelectedItem(fieldValue);
		propertiesPanel.add(field);
		field.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
					String val;
					val = field.getSelectedItem().toString();
				//	@SuppressWarnings("rawtypes")
				//	Class[] parameterTypes = fieldMethod.getParameterTypes();
					//String val = val.substring(0, val.length() -1);
					for(ComponentsInterface l: selectionList){
						try {
							fieldMethod.invoke(l,val);
						} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
							e1.printStackTrace();
						}
					}
					field.setSelectedItem(val);
			}
		});	
	}
	
	private void addControlPanelCheckBox(final String text
			,final int labLength, final int labHeigth
			,JCheckBox field
			,final int fieldLength, final int fieldHeight
			,Boolean fieldValue
			,Method fieldMethod){
		JLabel lab =new JLabel(text);
		lab.setSize(labLength, labHeigth);
		lab.setPreferredSize(lab.getSize());
		propertiesPanel.add(lab);
		field.setSize(fieldLength, fieldHeight);
		field.setPreferredSize(field.getSize());
		field.setSelected(fieldValue);
		propertiesPanel.add(field);
		field.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Boolean val;
					val = field.isSelected();
					for(ComponentsInterface l: selectionList){
						try {
							fieldMethod.invoke(l,val);
						} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
							e1.printStackTrace();
						}
					}
					field.setSelected(val);
			}
		});	
	}
	
	private void addControlPanelColorChooser(final String text
			,final int labLength, final int labHeigth
			,JButton button
			,Color fieldValue
			,Method fieldMethod){
		button.setText(text);
		button.setSize(labLength, labHeigth);
		button.setPreferredSize(button.getSize());
		button.setBackground(fieldValue);
		if ((fieldValue.getBlue()+fieldValue.getGreen()+fieldValue.getRed())<390){
			button.setForeground(Color.WHITE);
		}else{
			button.setForeground(Color.BLACK);
		}
		propertiesPanel.add(button);
		button.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Color newColor = JColorChooser.showDialog(propertiesPanel, text, fieldValue);
					for(ComponentsInterface l: selectionList){
						try {
							fieldMethod.invoke(l,newColor);
						} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
							e1.printStackTrace();
						}
					}
					button.setBackground(newColor);
					if ((newColor.getBlue()+newColor.getGreen()+newColor.getRed())<390){
						button.setForeground(Color.WHITE);
					}else{
						button.setForeground(Color.BLACK);
					}
			}
		});	
	}
	
	private void createScrollPanel(){
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
	
	private void createPage(){
		//A4 96CPI 794 x 1123;
				page.setBackground(Color.white);
				page.setBounds(0,0,794,1123);
				page.setBorder(borderDefault);
				page.setLayout(null);
				content.add(page);
				content.setPreferredSize(new Dimension(scrollPanel.getWidth(),page.getHeight()));
	}
	
	public void addRotatedLabel(int rot, int sx, int sy, int w, int h){
		componentPointer=new RotatedLabel(rot,sx,sy,w,h);
		componentList.add(componentPointer);
		content.add((Component)componentPointer);
		content.setComponentZOrder((Component)componentPointer, 0);
		componentPointer.setBorder(borderDefault);
	}
	
	private void addListeners() {
		addKeyListener(this);
		content.addMouseListener(mouseActionCheck);
		content.addMouseMotionListener(mouseActionCheck);
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()){
		case KeyEvent.VK_F12: //pełny ekran
			setExtendedState(JFrame.MAXIMIZED_BOTH);;
			dispose(); //innaczej nie można włączyć/wyłączyć obramowania
			if(isUndecorated()==false)	
			setUndecorated(true);
			else
			setUndecorated(false);
			setVisible(true);  // narysuj ponownie
		break;
		case KeyEvent.VK_DELETE:
			activeComponent = null;
			for(ComponentsInterface l: selectionList){
				content.remove((Component) l);
				componentList.remove((Component) l);
			}
			selectionList.clear();
	        setPropertiesPanel();
	        repaint();
			break;
		default: 
			break;
		}
	}
	
	@Override
	public void keyTyped(KeyEvent e) {}
	
	@Override
	public void keyPressed(KeyEvent e) {}
}
