package CurveEditor.GUI;

import java.util.EventListener;

import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import CurveEditor.Core.Editor;

public class GUI extends Editor implements EventListener, MenuListener{
	protected ChoiceArea choice;
	protected DrawArea draw;
	protected Menu menu;

	public GUI(){

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
