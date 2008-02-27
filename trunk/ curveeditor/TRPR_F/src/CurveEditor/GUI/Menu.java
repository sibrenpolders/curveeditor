package CurveEditor.GUI;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Vector;
import CurveEditor.Algorithms.Algorithm;
import CurveEditor.Tools.Tool;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

public class Menu extends JMenuBar {
	private JMenu menu;
	
	public static String algos[];
	public static String tools[];

	public Menu(  ){
		CreateMenuBar( );
	}

	public Menu(Vector<Algorithm> algorithms){

	}

	public void addAlgorithm(Algorithm a){

	}

	public void addTool(Tool t){

	}

	public void refresh(){

	}
	
	// Zal de menuBar opstellen
	private void CreateMenuBar( ) {
		// Aanmaken van de menubar.
		makeFile( );
		makeEdit( );
		makeTools( );
		makeAlgorithms( );
		makeHelp( );
	}
	
	// maakt een JMenu object aan ( m.a.w. de categorienamen van de menubar )
	private void CreateMenu( String name, int keyEvent, String description ) {
		menu = new JMenu( name );
		menu.setMnemonic( keyEvent );
		menu.getAccessibleContext().setAccessibleDescription( description);
		this.add(menu);
	
	}
	
	// maakt een JMenuItem object aan. Deze stellen de verschillende keuzes voor die in de menubalk staan
	private void CreateMenuItem( String name, int keyEvent, String description ) {
		JMenuItem menuItem = new JMenuItem( name, keyEvent );
		menuItem.setAccelerator(KeyStroke.getKeyStroke( keyEvent, ActionEvent.CTRL_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription( description );
		menuItem.addActionListener(new ActionListener( ){
			public void actionPerformed(ActionEvent e)
			{
				// TODO functionaliteit aan toevoegen
				System.out.println("New is pressed");
			}
		} );
		menu.add( menuItem );
	}
	private void makeFile( ) {
		// menu object aanmaken
		CreateMenu( "FILE", KeyEvent.VK_F, "" );
		CreateMenuItem( "New", KeyEvent.VK_N, "" );
	}
	
	private void makeEdit( ) {
		CreateMenu( "Edit", KeyEvent.VK_E, "" );	
	}
	
	private void makeTools( ) {
		CreateMenu( "Tools", KeyEvent.VK_T, "" );	
	}
	
	private void makeAlgorithms( ) {
		CreateMenu( "Algorithms", KeyEvent.VK_A, "" );				
	}
	
	private void makeHelp( ) {
		CreateMenu( "Help", KeyEvent.VK_H, "" );
	}	
}
