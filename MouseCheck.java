package projektant;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;

import projektant.PMainWindow.MouseMode;

public class MouseCheck extends MouseAdapter {
	final PMainWindow parent;
	protected MouseMode mouseMode;
    protected Rectangle2D selectionRectangle;
	
	
    private boolean mDrag = false;
	private int sX,sY,pX,pY;
	public MouseCheck(PMainWindow p){
		sX=0;
		sY=0;
		pX=0;
		pY=0;
		parent=p;
	}
public void componentMove(ComponentsInterface c, int X, int Y){
c.setLocation(c.getX()+X,c.getY()+Y);
}
public void componentResize(ComponentsInterface c, int X, int Y){
c.setSize(c.getWidth()+X,c.getHeight()+Y);
}

    /**
     * Sprawdza czy trafiono w komponent 
     * @param c Wskaźnik na komponent
     * @param X Położenie X
     * @param Y Położenie Y
     * @return zwraca true lub false
     */
public boolean mouseIn(ComponentsInterface c, int X, int Y){
if(c.contains(X, Y))return true;
return false;
}
/**
* Sprawdza czy trafiono w komponent
* @param c Wskaźnik na komponent
* @param p Punkt
* @return zwraca true lub false
*/
public boolean mouseIn(ComponentsInterface c, Point p) {
return mouseIn(c,p.x,p.y);
}
    @Override
    public void mousePressed(MouseEvent e) {
    	parent.activeComponent=null;
    	for(ComponentsInterface c:parent.componentList){
        if(mouseIn(c, e.getPoint())){
        	pX=e.getX();
        	pY=e.getY();
        	parent.activeComponent=c;
        	parent.activeComponent.setBorder(parent.borderSelected);
        	if(!parent.selectionList.contains(c)){
        		for(ComponentsInterface l: parent.selectionList){
        		l.setBorder(parent.borderDefault);
        		}
        		parent.selectionList.clear();
        		parent.selectionList.add(c);
        		}
        }
    	}
    	if(parent.activeComponent==null){
        	sX=e.getX();
        	sY=e.getY();
        	pX=sX;
        	pY=sY;
        	selectionRectangle = new Rectangle2D.Double(sY,sX, 0, 0);
        	for(ComponentsInterface c: parent.selectionList){
        		c.setBorder(parent.borderDefault);
        		}
        		parent.selectionList.clear();
        }  	
		parent.setPropertiesPanel();
    }
    @Override
    public void mouseReleased(MouseEvent e) {
    	if(parent.selectionList.size()>0 && parent.activeComponent==null)
    		parent.activeComponent=parent.selectionList.get(parent.selectionList.size()-1);
        if (mDrag) {
        	selectionRectangle = new Rectangle2D.Double(sY,sX, 0, 0);
        	parent.page.setSelectionRectangle(selectionRectangle);
    		switch(mouseMode) {
    		case LABEL:
    			if( e.getX() > sX && e.getY() > sY){
    			  parent.addRotatedLabel(0, sX, sY, e.getX()-sX,e.getY()-sY);
    			}
    			break;
    		default:
    			break;
    		}
    	} 

        parent.setPropertiesPanel();
        mDrag = false;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    	mDrag = true;
    	if(parent.activeComponent != null){
    		for(ComponentsInterface c: parent.selectionList){
    		switch(mouseMode) {
    		case MOVE:
    		componentMove(c,e.getX()-pX,e.getY()-pY);
    		break;
    		case RESIZE:
    			componentResize(c,e.getX()-pX,e.getY()-pY);
    			break;
    		case LABEL:
    			break;
    		default:
    			break;
    		}
    		}
    		if(parent.selectionList.isEmpty()==false)parent.content.repaint();
    		pX=e.getX();
    		pY=e.getY();
		} else {
    		pX=e.getX();
    		pY=e.getY();
				selectionRectangle.setRect(pX > sX ? sX : pX,
                                           pY > sY ? sY : pY, 
                                           pX > sX ? (pX - sX) : (sX - pX),
                                           pY > sY ? (pY - sY) : (sY - pY));
				parent.page.setSelectionRectangle(selectionRectangle);
				parent.selectionList.clear();
				parent.activeComponent = null;
				for (ComponentsInterface c : parent.componentList) {
					if (selectionRectangle.contains(c.getLocation())) {
						parent.selectionList.add(c);
						c.setBorder(parent.borderSelected);
					} else {
						c.setBorder(parent.borderDefault);
					}
				}
		}
    }
}
