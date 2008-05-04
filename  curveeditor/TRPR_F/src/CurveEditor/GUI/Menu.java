package CurveEditor.GUI;


import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.KeyStroke;

public class Menu extends JMenuBar {
	private static final long serialVersionUID = -2717014108067514961L;
	private JMenu menu;
	private JMenuItem menuItem;
	private ButtonGroup group;
	private ActionListener listener;

	public Menu( ActionListener listener ) {
		this.listener = listener;
		CreateMenuBar();
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
		add(menu);

	}

	// maakt een JMenuItem object aan. Deze stellen de verschillende keuzes voor die in de menubalk staan
	private void CreateMenuItem( String name, int keyEvent, String description, String icon) {
		if ( icon != null ) {
			ImageIcon a = new ImageIcon( icon );
			menuItem = new JMenuItem( name, a );
		}
		else
			menuItem = new JMenuItem( name, keyEvent );
		menuItem.setAccelerator(KeyStroke.getKeyStroke( keyEvent, ActionEvent.CTRL_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription( description );
		menu.add( menuItem );
	}
	private void makeFile( ) {
		// menu object aanmaken
		CreateMenu( "FILE", KeyEvent.VK_F, "" );		
		CreateMenuItem( "New", KeyEvent.VK_N, "Create a new file", "src/CurveEditor/GUI/icons/filenew.png" );
		menuItem.addActionListener( listener );

		CreateMenuItem("Open", KeyEvent.VK_O, "Open a file", "src/CurveEditor/GUI/icons/fileopen.png");
		menuItem.addActionListener( listener );
		
		menu.addSeparator();

		CreateMenuItem("Save", KeyEvent.VK_S, "Save a file", "src/CurveEditor/GUI/icons/filesave.png");
		menuItem.addActionListener( listener );
		
		CreateMenuItem("Save As...", KeyEvent.VK_O, "Save a file as ...", "src/CurveEditor/GUI/icons/filesaveas.png");
		menuItem.addActionListener( listener );
		
		menu.addSeparator();
		
		CreateMenuItem( "New Curve", KeyEvent.VK_C, "Start a new curve", "src/CurveEditor/GUI/icons/curvenew.png" );
		menuItem.addActionListener( listener );
		
		menu.addSeparator();
		
		CreateMenuItem( "Quit", KeyEvent.VK_Q, "Quit Curve Editor", "src/CurveEditor/GUI/icons/exit.png" );
		menuItem.addActionListener( new ActionListener( ) {
			public void actionPerformed(ActionEvent e) {
				int n = JOptionPane.showConfirmDialog( null, "Do you really want to quit?", "Quit Curve Editor", JOptionPane.YES_NO_OPTION);
				if ( n == 0 )
					System.exit(0);
			}
			
		});
	}

	private void makeEdit( ) {
		CreateMenu( "Edit", KeyEvent.VK_E, "" );

		CreateMenuItem( "Undo", KeyEvent.VK_U, "Undo last action", "src/CurveEditor/GUI/icons/undo.png");
		menuItem.addActionListener(new ActionListener( ){
			public void actionPerformed(ActionEvent e)
			{
				System.out.println( "Be gone** ");
			}
		} );

		CreateMenuItem( "Redo", KeyEvent.VK_R, "Redolast action", "src/CurveEditor/GUI/icons/redo.png" );
		menuItem.addActionListener(new ActionListener( ){
			public void actionPerformed(ActionEvent e)
			{
				System.out.println( "Come back");
			}
		} );

		menu.addSeparator();

		CreateMenuItem( "Preferrences", KeyEvent.VK_P, "Make adjustments to Curve Editor", null );
		menuItem.addActionListener( listener );
	}

	private void makeTools( ) {
		CreateMenu( "Tools", KeyEvent.VK_T, "" );
		group = new ButtonGroup( );
		
		JRadioButton button = new JRadioButton( "none" );
		button.setSelected( true );
		button.addActionListener( listener );
		button.setToolTipText( "No tools selected" );
		
		group.add( button );
		menu.add( button );
		
		button = new JRadioButton( "Deselect All" );
		button.setSelected( true );
		button.addActionListener( listener );
		button.setToolTipText( "Deselect all that was selected" );
		
		group.add( button );
		menu.add( button );
		
		JMenu menu2 = new JMenu( "Point");
		menu2.setMnemonic( KeyEvent.VK_P );
		menu2.getAccessibleContext().setAccessibleDescription( "" );
		menu2.setToolTipText( "Tools for point Manipulation" );
		menu.add( menu2 );
		createPointPanel( menu2 );
		
		menu2 = new JMenu( "Curve" );
		menu2.setMnemonic( KeyEvent.VK_C );
		menu2.getAccessibleContext().setAccessibleDescription( "" );
		menu2.setToolTipText( "Tools for curve Manipulation" );
		menu.add( menu2 );		
		createCurvePanel( menu2 );		
	}

	private void createPointPanel( JMenu menu ) {
		JPanel radioPanel = new JPanel( );
		radioPanel.setLayout( new BoxLayout( radioPanel, BoxLayout.Y_AXIS ));
		
		JRadioButton button = new JRadioButton( "Select Point" );
		button.setSelected( false );
		button.addActionListener( listener) ;
		
		group.add( button);
		radioPanel.add( button );
		
		button = new JRadioButton( "Move Point" );
		button.setSelected( false );
		button.addActionListener( listener ) ;
				
		group.add( button );		
		radioPanel.add( button );
		
		button = new JRadioButton( "Delete Point" );
		button.setSelected( false );
		button.addActionListener( listener ) ;
				
		group.add( button );		
		radioPanel.add( button );
		
		button = new JRadioButton( "Delete Point" );
		button.setSelected( false );
		button.addActionListener( listener ) ;
				
		group.add( button);		
		radioPanel.add( button );
		
		menu.add( radioPanel );			
	}
	
	private void createCurvePanel( JMenu menu ) {
		JPanel radioPanel = new JPanel( );
		radioPanel.setLayout( new BoxLayout( radioPanel, BoxLayout.Y_AXIS ));
		
		JRadioButton button = new JRadioButton( "Select Curve" );
		button.setSelected( true );
		button.addActionListener( listener) ;
		
		group.add( button );
		radioPanel.add( button );
		
		button = new JRadioButton( "Move Curve" );
		button.setSelected( false );
		button.addActionListener( listener ) ;
				
		group.add( button );		
		radioPanel.add( button );
		
		button = new JRadioButton( "Delete Curve" );
		button.setSelected( false );
		button.addActionListener( listener ) ;
				
		group.add( button);		
		radioPanel.add( button );
		
		menu.add( radioPanel );
	}

	private void makeAlgorithms( ) {
		CreateMenu( "Algorithms", KeyEvent.VK_A, "" );
		group = new ButtonGroup();
		
		JMenu menu2 = new JMenu( "Bezier" );
		menu2.setMnemonic( KeyEvent.VK_B );
		menu2.getAccessibleContext().setAccessibleDescription( "" );
		menu.add( menu2 );		
		createBezierPanel( menu2 );
		
		menu2 = new JMenu( "Hermite");
		menu2.setMnemonic( KeyEvent.VK_H );
		menu2.getAccessibleContext().setAccessibleDescription( "" );
		menu.add( menu2 );
		createHermitePanel( menu2 );
	}

	private void createBezierPanel( JMenu menu ) {
		JPanel radioPanel = new JPanel( );
		radioPanel.setLayout( new BoxLayout( radioPanel, BoxLayout.Y_AXIS ));
		
		JRadioButton algName = new JRadioButton( "Linear" );
		algName.setSelected( true );
		algName.addActionListener( listener) ;
		
		group.add( algName );
		radioPanel.add( algName );
		
		algName = new JRadioButton( "Bezier C0" );
		algName.setSelected( false );
		algName.addActionListener( listener ) ;
				
		group.add( algName );		
		radioPanel.add( algName );
		
		algName = new JRadioButton( "Bezier G1" );
		algName.setSelected( false );
		algName.addActionListener( listener ) ;
				
		group.add( algName );		
		radioPanel.add( algName );
		
		algName = new JRadioButton( "Bezier C1" );
		algName.setSelected( false );
		algName.addActionListener( listener ) ;
				
		group.add( algName );		
		radioPanel.add( algName );
		
		menu.add( radioPanel );			
	}
	
	private void createHermitePanel( JMenu menu ) {
		JPanel radioPanel = new JPanel( );
		radioPanel.setLayout( new BoxLayout( radioPanel, BoxLayout.Y_AXIS ));
		
		JRadioButton algName = new JRadioButton( "normal" );
		algName.setSelected( true );
		algName.addActionListener( listener) ;
		
		group.add( algName );
		radioPanel.add( algName );
		
		algName = new JRadioButton( "Cardinal" );
		algName.setSelected( false );
		algName.addActionListener( listener ) ;
		
		group.add( algName );
		radioPanel.add( algName );
		
		algName = new JRadioButton( "CatmullRom" );
		algName.setSelected( false );
		algName.addActionListener( listener ) ;
		
		group.add( algName );
		radioPanel.add( algName );		
		
		menu.add( radioPanel );
	}
	
	private void makeHelp( ) {
		// een glue element toevoegen zorgt ervoor dat help rechts wordt uitgelijnd
		add( Box.createHorizontalGlue() );
		CreateMenu( "Help", KeyEvent.VK_H, "" );

		CreateMenuItem( "Quick howto's", KeyEvent.VK_H,"Learn the basics", null );
		menuItem.addActionListener(new ActionListener( ){
			public void actionPerformed(ActionEvent e)
			{
				System.out.println("Quick howto");
			}
		} );

		CreateMenuItem( "Documentation", KeyEvent.VK_D,"Full guide to Curve Editor", null );
		menuItem.addActionListener(new ActionListener( ){
			public void actionPerformed(ActionEvent e)
			{
				System.out.println("Doc");
			}
		} );

		menu.addSeparator();

		CreateMenuItem( "About", KeyEvent.VK_A,"About", null );
		menuItem.addActionListener( listener );
	}


	private void preferrences( ) {
		System.out.println( "Preferrences is pressed" );
	}

	private void about() {
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