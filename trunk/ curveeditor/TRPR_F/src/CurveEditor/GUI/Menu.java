package CurveEditor.GUI;

import java.util.Vector;

import javax.swing.JFrame;

import CurveEditor.Algorithms.Algorithm;
import CurveEditor.Tools.Tool;


public class Menu extends JMenuBar {
	private ContentPane pane;
	private JMenu menu;
	
	public static String algos[];
	public static String tools[];

	public Menu( ContentPane pane ){
		this.pane = pane;
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
	
	private void CreateMenuBar( ) {
		// Aanmaken van de menubar.
		menuBar = new JMenuBar( );
		makeFile( )
		makeEdit( );
		makeTools( );
		makeAlgorithms( );
		makeHelp( );
	}
	
	private void CreateMenu( String name, static int keyEvent, String description ) {
		menu = new JMenu( name );
		menu.setMnemonic( keyEvent );
		menu.getAccessibleContext().setAccessibleDescription( description);
		menuBar.add(menu);
	
	}
	
	private void CreateMenuItem( String name, static int keyEvent, String description ) {
		JMenuItem menuItem = new JMenuItem( name, keyEvent );
		menuItem.setAccelerator(KeyStroke.getKeyStroke( keyEvent, ActionEvent.CTRL_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription( description );
		menuItem.addActionListener(new ActionListener( ){
			public void actionPerformed(ActionEvent e)
			{
				System.out.println("New is pressed");
			}
		} );
		menu.add( menuItem );
	}
	private void makeFile( ) {
		// menu object aanmaken
		CreateMenu( "FILE", KeyEvent.VK_N, "" );
		CreateMenuItem( "New", KeyEvent.VK_N, "" );
	}
	
	private void makeEdit( ) {
		menu = new JMenu( "Edit" );
		menu.setMnemonic(KeyEvent.VK_E);
		menu.getAccessibleContext().setAccessibleDescription(
				"");
		menuBar.add(menu);		
	}
	
	private void makeTools( ) {
		menu = new JMenu( "Tools" );
		menu.setMnemonic(KeyEvent.VK_T);
		menu.getAccessibleContext().setAccessibleDescription(
				"");
		menuBar.add(menu);
	}
	
	private void makeAlgorithms( ) {
		menu = new JMenu( "Algorithms" );
		menu.setMnemonic(KeyEvent.VK_A);
		menu.getAccessibleContext().setAccessibleDescription(
				"");
		menuBar.add(menu);
	}
	
	private void makeHelp( ) {
		menu = new JMenu( "Help" );
		menu.setMnemonic(KeyEvent.VK_H);
		menu.getAccessibleContext().setAccessibleDescription(
				"");
		menuBar.add(menu);
	}	
}
