package CurveEditor.GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Vector;
import CurveEditor.Algorithms.Algorithm;
import CurveEditor.Tools.Tool;

import javax.swing.Box;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

public class Menu extends JMenuBar {
	/**
	 * TODO wtf is dit?
	 * 
	 * Eclipse gaf er een warning voor... 
	 * heb 't 'm zelf laten oplossen met deze bizarre regel als gevolg. :-D
	 */
	private static final long serialVersionUID = -2717014108067514961L;

	private JMenu menu;
	private JMenuItem menuItem;
	public static String algos[];
	public static String tools[];

	public Menu(  ){
		CreateMenuBar( );
	}

	/*
	 * TODO snap het nut van deze functies nog altijd niet. Het menu is toch niet dynamisch. Alles staat vast.
	 * 
	 * Da's correct natuurlijk, maar voor de programmeur is dat niet iets statisch. Als je bvb. een extra item
	 * in de balk voegt, dan moet je ook ergens anders weer toevoegen. Dus dacht ik van: voeg da één keer toe in 
	 * de vector, en 't is overal in orde. Maar je hebt gelijk, voor wat wij moeten doen is dat overkill.
	 * 
	 * public Menu(Vector<Algorithm> algorithms){
	 *	
	 * }
	 *
	 * public void addAlgorithm(Algorithm a){
	 *
	 * }
	 *
	 * public void addTool(Tool t){
	 *
	 * }
	 */
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
		menuItem = new JMenuItem( name, keyEvent );
		menuItem.setAccelerator(KeyStroke.getKeyStroke( keyEvent, ActionEvent.CTRL_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription( description );		
		menu.add( menuItem );
	}
	private void makeFile( ) {
		// menu object aanmaken
		CreateMenu( "FILE", KeyEvent.VK_F, "" );
		
		CreateMenuItem( "New", KeyEvent.VK_N, "Create a new file" );
		// java laat niet toe functies mee te geven als parameter. Dus moet de functie die aangeroepen moet worden manueel geset worden
		menuItem.addActionListener(new ActionListener( ){
			public void actionPerformed(ActionEvent e)
			{
				New( );				
			}
		} );
		
		CreateMenuItem("Open", KeyEvent.VK_O, "Open a file" );
		menuItem.addActionListener(new ActionListener( ){
			public void actionPerformed(ActionEvent e)
			{
				Open( );				
			}
		} );
		menu.addSeparator();
		
		CreateMenuItem( "Quit", KeyEvent.VK_Q, "Quit Curve Editor" );
		menuItem.addActionListener(new ActionListener( ){
			public void actionPerformed(ActionEvent e)
			{
				Quit( );				
			}
		} );
	}
	
	private void makeEdit( ) {
		CreateMenu( "Edit", KeyEvent.VK_E, "" );
		
		CreateMenuItem( "Undo", KeyEvent.VK_U, "Undo last action" );
		menuItem.addActionListener(new ActionListener( ){
			public void actionPerformed(ActionEvent e)
			{
				undo( );				
			}
		} );
		
		CreateMenuItem( "Redo", KeyEvent.VK_R, "Redolast action" );
		menuItem.addActionListener(new ActionListener( ){
			public void actionPerformed(ActionEvent e)
			{
				redo( );				
			}
		} );
		
		menu.addSeparator();
		
		CreateMenuItem( "Preferrences", KeyEvent.VK_P, "Make adjustments to Curve Editor" );
		menuItem.addActionListener(new ActionListener( ){
			public void actionPerformed(ActionEvent e)
			{
				preferrences( );				
			}
		} );
	}
	
	private void makeTools( ) {
		CreateMenu( "Tools", KeyEvent.VK_T, "" );	
	}
	
	private void makeAlgorithms( ) {
		CreateMenu( "Algorithms", KeyEvent.VK_A, "" );
		
		CreateMenuItem( "Bezier", KeyEvent.VK_B, "Use Bezier for curve calculation" );
		menuItem.addActionListener(new ActionListener( ){
			public void actionPerformed(ActionEvent e)
			{
				bezier( );				
			}
		} );
		
		CreateMenuItem( "Hermites", KeyEvent.VK_H, "Use Hermites for curve calculation" );
		menuItem.addActionListener(new ActionListener( ){
			public void actionPerformed(ActionEvent e)
			{
				hermites( );				
			}
		} );
	}
	
	private void makeHelp( ) {
		// een glue element toevoegen zorgt ervoor dat help rechts wordt uitgelijnd
		add( Box.createHorizontalGlue() );
		CreateMenu( "Help", KeyEvent.VK_H, "" );
		
		CreateMenuItem( "Quick howto's", KeyEvent.VK_H,"Learn the basics" );
		menuItem.addActionListener(new ActionListener( ){
			public void actionPerformed(ActionEvent e)
			{
				quickHowto( );				
			}
		} );
		
		CreateMenuItem( "Documentation", KeyEvent.VK_D,"Full guide to Curve Editor" );
		menuItem.addActionListener(new ActionListener( ){
			public void actionPerformed(ActionEvent e)
			{
				doc( );				
			}
		} );
		
		menu.addSeparator();
		
		CreateMenuItem( "About", KeyEvent.VK_A,"About" );
		menuItem.addActionListener(new ActionListener( ){
			public void actionPerformed(ActionEvent e)
			{
				about( );				
			}
		} );
	}	
	
	private void New( ) {
		System.out.println( "New is pressed" );
	}
	
	private void Open( ) {
		System.out.println( "Open is pressed" );
	}

	private void Quit( ) {
		int n = JOptionPane.showConfirmDialog(
			    this,
			    "Do you really want to quit?",
			    "Quit Curve Editor",
			    JOptionPane.YES_NO_OPTION);
		if ( n == 0 )
			System.exit(0);
	}
	
	private void undo( ) {
		System.out.println( "undo is pressed" );
	}

	private void redo( ) {
		System.out.println( "redo is pressed" );
	}
	
	private void preferrences( ) {
		System.out.println( "Preferrences is pressed" );
	}
	
	private void bezier( ) {
		System.out.println( "Bezier" );
	}
	
	private void hermites( ) {
		System.out.println( "Hermites " ); 
	}
	
	private void quickHowto( ) {
		System.out.println( "Quick Howto's" );
	}
	
	private void doc( ) {
		System.out.println( "doc" );
	}
	
	private void about( ) {
	}
}
