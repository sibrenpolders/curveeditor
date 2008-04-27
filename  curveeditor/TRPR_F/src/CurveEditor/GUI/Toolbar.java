package CurveEditor.GUI;

import java.awt.Color;
import java.awt.event.ActionListener;

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
	
	public Toolbar( ActionListener listener ) {
		setFloatable( false );
		this.listener = listener;
		setBorder( BorderFactory.createMatteBorder(1, 0, 1, 0, Color.BLACK ));
		setRollover( true );
		addButtons( );
	}

	private void addButtons( ) {
		makeButton( "New", "Create a new file", "src/CurveEditor/GUI/icons/filenew.png" );
		makeButton( "Open", "Open a file", "src/CurveEditor/GUI/icons/fileopen.png" );
		makeButton( "Save", "Save a file", "src/CurveEditor/GUI/icons/filesave.png" );
		
		addSeparator();

		group = new ButtonGroup( );
		makeToggleButton("normal", "Draw a curve or select a point", "src/CurveEditor/GUI/icons/ganttSelect.png", true );
		makeToggleButton("Select", "select a point or a group of points", "src/CurveEditor/GUI/icons/ganttSelecttask.png", false );
		
		addSeparator( );
		
		makeButton( "New Curve", "Start a new curve", "src/CurveEditor/GUI/icons/curvenew.png" );
		
		add( Box.createHorizontalGlue() );
	}

	private void makeToggleButton(String name, String tooltip, String icon, boolean selected ) {
		toggleButton = new JToggleButton( new ImageIcon( icon ), selected );
		toggleButton.addActionListener( listener );
		toggleButton.setToolTipText( tooltip );
		group.add( toggleButton );
		add( toggleButton );
	}

	private void makeButton( String name, String tooltip, String icon ) {
		button = new JButton( name, new ImageIcon( icon ) );
		button.addActionListener( listener );
		button.setToolTipText( tooltip );
		add( button );
	}
}