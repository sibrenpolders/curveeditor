package CurveEditor.GUI;

import java.awt.Container;
import java.util.EventListener;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import CurveEditor.Core.Editor;

public class GUI extends Editor implements EventListener, MenuListener{
	protected ChoiceArea choice;
	protected DrawArea draw;
	protected Menu menu;

	public GUI(){		
		JFrame frame = new JFrame("Curve Editor");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        Container contentPane = frame.getContentPane();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));       
        
        menu = new Menu();
        contentPane.add( menu );
        
        draw = new DrawArea();
        contentPane.add(draw);
       
        frame.pack();
        frame.setVisible(true);
	}

	public GUI(String filename){
	
	}

	@Override
	public void menuCanceled(MenuEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void menuDeselected(MenuEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void menuSelected(MenuEvent arg0) {

	}

	public void selectCurve(){
		currentSituation.setCurrentPoint(draw.retrievePoint());
		currentSituation.setCurrentCurve(searchCurve(currentSituation.currentPoint()));
		currentSituation.setCurrentDegree(currentSituation.currentCurve().getDegree());
		currentSituation.setCurrentType(currentSituation.currentCurve().getType());

		draw.drawSelectedCurve(currentSituation.currentCurve());
		choice.refresh();

		//message
	}	
}
