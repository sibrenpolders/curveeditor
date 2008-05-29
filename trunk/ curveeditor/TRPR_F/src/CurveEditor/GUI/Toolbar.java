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

	public void setSize() {
		setBounds(0, DisplaySize.MENUHEIGHT, DisplaySize.SCREENWIDTH,
				DisplaySize.TOOLBARHEIGHT);
	}

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

	protected void makeToggleButton(String name, String tooltip, String icon,
			boolean selected) {
		URL imgURL = ClassLoader.getSystemResource( icon );
		ImageIcon imageIcon = new ImageIcon( imgURL );
		toggleButton = new JToggleButton( imageIcon, selected);
		toggleButton.setName(name);
		toggleButton.addActionListener(listener);
		toggleButton.setToolTipText(tooltip);
		group.add(toggleButton);
		add(toggleButton);
	}

	private void makeButton(String name, String tooltip, String icon) {
		button = new JButton(name, new ImageIcon(icon));
		button.addActionListener(listener);
		button.setToolTipText(tooltip);
		add(button);
	}
}
