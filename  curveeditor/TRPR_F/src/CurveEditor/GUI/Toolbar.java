// auteur Sibren Polders
package CurveEditor.GUI;

/*
 * Deze klasse zal een toolbar aanmaken, waar bepaalde veel gebrukte functies worden gezet.
 * Dit om het gemak van de gebruiker te verhogen.
 */
import java.awt.Color;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;

public class Toolbar extends JToolBar {
	private static final long serialVersionUID = -1280389982796296645L;
	private ActionListener listener;
	private JButton button;
	private JToggleButton toggleButton;
	private ButtonGroup group;

	public Toolbar(ActionListener listener) {
		setFloatable(false);
		this.listener = listener;
		setSize();
		setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.BLACK));
		setRollover(true);
		addButtons();
	}

	/*
	 * Zet de grootte van de toolbar, dezie info wordt uit displaysize gehaald
	 * 
	 */
	public void setSize() {
		setBounds(0, DisplaySize.MENUHEIGHT, DisplaySize.SCREENWIDTH,
				DisplaySize.TOOLBARHEIGHT);
	}

	/*
	 * Add de verschillende toobar buttons aan de toolbar
	 */
	private void addButtons() {
		group = new ButtonGroup();

		makeButton("New File", "Create a new file",
			"CurveEditor/GUI/icons/filenew.png");
		makeButton("Open File", "Open a file",
			"CurveEditor/GUI/icons/fileopen.png");
		makeButton("Save File", "Save a file",
			"CurveEditor/GUI/icons/filesave.png");

		addSeparator();		

		makeButton("New Curve", "Start a new curve",
				"CurveEditor/GUI/icons/curvenew.png");
		makeButton("Clear", "Remove all curves",
				"CurveEditor/GUI/icons/curvenew.png");

		addSeparator();

		makeButton("Select Curve", "Select a curve.",
				"CurveEditor/GUI/icons/ganttSelect.png");
		makeButton("Select Point", "Select a point.",
				"CurveEditor/GUI/icons/ganttSelecttask.png");

		add(Box.createHorizontalGlue());
	}

	/* 
	 * maakt een standaard button zodat deze aan de toolbar kan toegevoegd worden
	 */
	private void makeButton(String name, String tooltip, String icon) {
		URL imgURL = ClassLoader.getSystemResource( icon );
		ImageIcon imageIcon = new ImageIcon( imgURL );
		button = new JButton(name, imageIcon );
		button.addActionListener(listener);
		button.setToolTipText(tooltip);
		add(button);
	}
}
