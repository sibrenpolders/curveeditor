package CurveEditor.GUI;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Menu extends JMenuBar {
	private static final long serialVersionUID = -2717014108067514961L;
	
	private JMenu menu;
	private JMenuItem menuItem;
	private MenuItemPressed mip;


	public Menu(){
		CreateMenuBar( );
	}
	
	public Menu(MenuItemPressed mip){
		this.mip = mip;
		CreateMenuBar( );
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
	private void CreateMenuItem( String name, int keyEvent, String description, String icon) {
		if ( icon != null ) {
			ImageIcon a = new ImageIcon( icon );
			System.out.println( icon + " " + a.getIconHeight() ); 
			menuItem = new JMenuItem( name, a );
		}
		else
			menuItem = new JMenuItem( name, keyEvent );
		menuItem.setAccelerator(KeyStroke.getKeyStroke( keyEvent, ActionEvent.CTRL_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription( description );
//		if ( icon != null )
//		menuItem.setIcon( new ImageIcon( icon ) );
		menu.add( menuItem );
	}
	private void makeFile( ) {
		// menu object aanmaken
		CreateMenu( "FILE", KeyEvent.VK_F, "" );		
		CreateMenuItem( "New", KeyEvent.VK_N, "Create a new file", "src/CurveEditor/GUI/icons/filenew.png" );
		// java laat niet toe functies mee te geven als parameter. Dus moet de functie die aangeroepen moet worden manueel geset worden
		menuItem.addActionListener(new ActionListener( ){
			public void actionPerformed(ActionEvent e)
			{
				mip.toggleNewFileSelected();
			}
		} );

		CreateMenuItem("Open", KeyEvent.VK_O, "Open a file", "src/CurveEditor/GUI/icons/fileopen.png");
		menuItem.addActionListener( new Open( mip ) );
		menu.addSeparator();

		CreateMenuItem("Save", KeyEvent.VK_S, "Save a file", "src/CurveEditor/GUI/icons/filesave.png");
		menuItem.addActionListener(new Save( mip ) );
		
		CreateMenuItem("Save As...", KeyEvent.VK_O, "Save a file as ...", "src/CurveEditor/GUI/icons/filesaveas.png");
		menuItem.addActionListener(new SaveAs( mip ) );
		
		CreateMenuItem( "Quit", KeyEvent.VK_Q, "Quit Curve Editor", "src/CurveEditor/GUI/icons/exit.png" );
		menuItem.addActionListener( new Quit( ) );
	}

	private void makeEdit( ) {
		CreateMenu( "Edit", KeyEvent.VK_E, "" );

		CreateMenuItem( "Undo", KeyEvent.VK_U, "Undo last action", "src/CurveEditor/GUI/icons/undo.png");
		menuItem.addActionListener(new ActionListener( ){
			public void actionPerformed(ActionEvent e)
			{
				mip.toggleUndoSelected();				
			}
		} );

		CreateMenuItem( "Redo", KeyEvent.VK_R, "Redolast action", "src/CurveEditor/GUI/icons/redo.png" );
		menuItem.addActionListener(new ActionListener( ){
			public void actionPerformed(ActionEvent e)
			{
				mip.toggleRedoSelected();		
			}
		} );

		menu.addSeparator();

		CreateMenuItem( "Preferrences", KeyEvent.VK_P, "Make adjustments to Curve Editor", null );
		menuItem.addActionListener(new ActionListener( ){
			public void actionPerformed(ActionEvent e)
			{
				new Preferrences( );				
			}
		} );
	}

	private void makeTools( ) {
		CreateMenu( "Tools", KeyEvent.VK_T, "" );	
	}

	private void makeAlgorithms( ) {
		CreateMenu( "Algorithms", KeyEvent.VK_A, "" );

		CreateMenuItem( "Bezier", KeyEvent.VK_B, "Use Bezier for curve calculation", null );
		menuItem.addActionListener(new ActionListener( ){
			public void actionPerformed(ActionEvent e)
			{
				mip.toggleBezierSelected();
			}
		} );

		CreateMenuItem( "Hermites", KeyEvent.VK_H, "Use Hermites for curve calculation", null );
		menuItem.addActionListener(new ActionListener( ){
			public void actionPerformed(ActionEvent e)
			{
				mip.toggleHermitesSelected();
			}
		} );
	}

	private void makeHelp( ) {
		// een glue element toevoegen zorgt ervoor dat help rechts wordt uitgelijnd
		add( Box.createHorizontalGlue() );
		CreateMenu( "Help", KeyEvent.VK_H, "" );

		CreateMenuItem( "Quick howto's", KeyEvent.VK_H,"Learn the basics", null );
		menuItem.addActionListener(new ActionListener( ){
			public void actionPerformed(ActionEvent e)
			{
				new QuickHowto( );				
			}
		} );

		CreateMenuItem( "Documentation", KeyEvent.VK_D,"Full guide to Curve Editor", null );
		menuItem.addActionListener(new ActionListener( ){
			public void actionPerformed(ActionEvent e)
			{
				new Doc( );				
			}
		} );

		menu.addSeparator();

		CreateMenuItem( "About", KeyEvent.VK_A,"About", null );
		menuItem.addActionListener(new ActionListener( ){
			public void actionPerformed(ActionEvent e)
			{
				new About( );				
			}
		} );
	}		
}

/* 
 * Deze class' zijn geschreven om te gebruiken in new ActionListener() { ..}.
 * Vermits dit een inline gedefinieerde klasse is. Daardoor kan de klasse opgeroepen door het new commando geen functies aanroepen van 
 * de klasse Menu. Daarom is het handig om voor de acties die moeten uitgevoerd worden aparte class' aan te maken zodat er een 
 * hogere abstractie is. Hierdoor worden de functies individueel ook kleiner.  
 */

class Open extends JFileChooser implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8210240788153180142L;
	private MenuItemPressed mip;
	
	public Open( MenuItemPressed mip ) {
		this.mip = mip;
	}

	public void actionPerformed(ActionEvent e) {
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"CurveEditor files (*.ce)", "ce");
		
		setFileFilter(filter);
		int returnVal = showOpenDialog(null);
		if(returnVal == JFileChooser.APPROVE_OPTION)
				mip.toggleOpenSelected( getSelectedFile().getName());
	}
}

class SaveAs extends JFileChooser implements ActionListener {
	private static final long serialVersionUID = -2814628877230702040L;
	protected MenuItemPressed mip;
	
	public SaveAs( MenuItemPressed mip ) {
		this.mip = mip;
	}

	protected void schowChooser() {
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"CurveEditor files (*.ce)", "ce");
		
		setFileFilter(filter);
		
		int returnVal = showSaveDialog(null);	
		if(returnVal == JFileChooser.APPROVE_OPTION)
			mip.toggleSaveSelected(getSelectedFile().getName() );
	}
	public void actionPerformed(ActionEvent e) {
		schowChooser();
	}
}

class Save extends SaveAs {
	private static final long serialVersionUID = 303696639663548530L;

	public Save( MenuItemPressed mip ) {
		super( mip );
	}
	
	public void actionPerformed( ActionEvent e ) {
		if ( null != mip.getFileName()) // Er is reeds een fileName opgegeven, dus gewoon saven
			mip.toggleSaveSelected();
		else 
			schowChooser();
	}
}

class Quit implements ActionListener {
	public void actionPerformed(ActionEvent e) {
		int n = JOptionPane.showConfirmDialog(
				null,
				"Do you really want to quit?",
				"Quit Curve Editor",
				JOptionPane.YES_NO_OPTION);
		if ( n == 0 )
			System.exit(0);
	}
}

class Undo {
public Undo() {
		System.out.println( "undo is pressed" );
	}
}

class Redo {
	public Redo() {
		System.out.println( "redo is pressed" );
	}
}

class Preferrences {
	public Preferrences( ) {
		System.out.println( "Preferrences is pressed" );
	}
}

class Bezier {
	public Bezier( ) {
		System.out.println( "Bezier" );
	}
}

class Hermites {
	public Hermites() {
		System.out.println( "Hermites " );
	}
}

class QuickHowto {
	public QuickHowto( ) {
		System.out.println( "Quick Howto's" );
	}
}

class Doc {
	public Doc( ) {
		System.out.println( "doc" );
	}
}

class About {
	public About() {
		Box hbox = Box.createHorizontalBox();		
		hbox.add( new JLabel( new ImageIcon( "src/CurveEditor/GUI/icons/about.jpg") ) );
		hbox.add(new JLabel( "<html>" +
				"<p style='font-family: arial;font-weight: bold;margin-left: 20px'>Coding and developing</p><br />" +
				"<p style='font-family: arial;font-weight: lighter;margin-left: 20px'>" +
				"Sibrand Staessens<br />" +
				"Sibren Polders<br />" +
				"</p><br />" +
				"<p style='font-family: arial;font-weight: lighter;margin-left: 20px'>" +
				"Thanks to: <br />" +
				"William van Haevere (ofzo)" +
				"</p>" +
		"</html>" ) );
		JOptionPane.showMessageDialog(null, hbox,"CurveEditor - about", JOptionPane.PLAIN_MESSAGE );
	}
}