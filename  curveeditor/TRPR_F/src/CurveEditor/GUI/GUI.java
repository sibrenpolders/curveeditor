package CurveEditor.GUI;

import java.awt.Container;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.EventListener;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import CurveEditor.Core.Editor;
import CurveEditor.Curves.Curve;
import CurveEditor.Curves.Point;

public class GUI extends Editor implements MenuListener, MouseListener, Runnable {
	protected ChoiceArea choice;
	protected DrawArea draw;
	protected Menu menu;
	protected MenuItemPressed mip;
	
	public GUI() {
		super();
		JFrame frame = new JFrame("Curve Editor");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setTitle("Curve Editor");
		Container contentPane = frame.getContentPane();
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));

		mip = new MenuItemPressed();
		menu = new Menu(mip);
		menu.addMouseListener(this);
		contentPane.add(menu);

		draw = new DrawArea(this.curves, this.selectedCurves);
		draw.addMouseListener(this);
		contentPane.add(draw);
		
		frame.pack();
		frame.setVisible(true);		
		testMethod();
	}

	public GUI(String filename) {
		super(filename);

		JFrame frame = new JFrame("Curve Editor");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setTitle("Curve Editor");
		Container contentPane = frame.getContentPane();
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));

		menu = new Menu();
//		menu.add
		contentPane.add(menu);

		draw = new DrawArea(this.curves, this.selectedCurves);
		draw.addMouseListener(this);
		contentPane.add(draw);

		frame.pack();
		frame.setVisible(true);
	}

	public void testMethod() {
		selectedCurves.add(new Curve(currentAlgorithm.getType(),
				currentAlgorithm.getDegree()));
		selectedCurves.get(0).addInput(new Point(0, 0));
		selectedCurves.get(0).addInput(new Point(50, 50));
		selectedCurves.get(0).addInput(new Point(99, 120));

		getAlgorithm(selectedCurves.get(0).getType(),
				selectedCurves.get(0).getDegree()).calculateCurve(
				selectedCurves.get(0));

		draw.repaint();

		setMode(MODE.ADD_INPUT);
	}

	public void menuCanceled(MenuEvent arg0) {

	}

	public void menuDeselected(MenuEvent arg0) {

	}

	public void menuSelected(MenuEvent arg0) {

	}

	public void mouseClicked(MouseEvent e) {
		if (mode == Editor.MODE.ADD_INPUT) {
			for (int i = 0; i < selectedCurves.size(); ++i) {
				selectedCurves.get(i).addInput(new Point(e.getX(), e.getY()));
				this.getAlgorithm(selectedCurves.get(i).getType(),
						selectedCurves.get(i).getDegree()).calculateCurve(
						selectedCurves.get(i));
			}
			draw.repaint();
		} else if (mode == Editor.MODE.SELECT_CURVE) {
			// zoek curve en voeg toe, of zo
		} else if (mode == Editor.MODE.NEW_CURVE) {
			startNewCurve();
			selectedCurves.get(0).addInput(new Point(e.getX(), e.getY()));
			setMode(MODE.ADD_INPUT);
			draw.repaint();
		}
	}

	public void mouseEntered(MouseEvent arg0) {
		draw.setCursor(new Cursor(Cursor.HAND_CURSOR));
	}

	public void mouseExited(MouseEvent arg0) {
		draw.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}

	public void mousePressed(MouseEvent e) {
		// kunnen we gebruiken voor curves te "slepen", bijvoorbeeld
	}

	public void mouseReleased(MouseEvent e) {

	}

	public void propertyChange(PropertyChangeEvent evt) {
		
	}

	// heb hiervooer en thread opgestart. Het probleem was dat ik maar geen goede listener vond
	// om de gekozen opties door te geven van zodra er op een menuItem geklikt werd.
	// een mewnu.add<eventlistener>( ) functie werkt niet omdat ik niet de events van 
	// de klasse menu moet opvangen maar de event van de objecten menuItem die zich in de klasse
	// menu zitten ( dus m.a.w. ik kan niet de techniek toepassen die gij gebruikt voor draw.
	// verder werkt PropertyChangeListener enkel op Beans
	// en ChangeListener werkt enkel voor JTab* en JSliders etc.
	// Dus om verdere tijdverspilling tegenen te gaan heb ik een thread opgestart die
	// gaat kijken of de gebruiker op een menuItem heeft geklikt en dan iets doet met het object
	// Momenteel is enkel NewFile (slordig) geimplementeerd. Maar de bedoeling is duidelijk.
	public void run() {
		try {
		while( true) {
			synchronized (mip) {
				mip.wait( );
				if ( mip.isNewFile()) {
					reset( );
					draw.reset(curves, selectedCurves);
					mip.toggleNewFile();
				}
			}
		}
		} catch( InterruptedException ie) {}
	}
}