// auteur Sibren Polders
package CurveEditor.GUI;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.KeyStroke;
import CurveEditor.Exceptions.InvalidArgumentException;

public class Menu extends JMenuBar implements ActionListener {
	private static final long serialVersionUID = -2717014108067514961L;
	private JMenu menu;
	private JMenuItem menuItem;
	private ButtonGroup group;
	private ActionListener listener;
	private JDialog preferences;
	private Container prefContainer;
	private JPanel container;
	private DrawAreaProperties drawProp;
	private JMenu tools;
	private JMenu algorithms;
	private JMenu toolsSub;
	private JMenu algorithmsSub;
	private JMenu toolsSub2;
	private JMenu algorithmsSub2;
	private int thickness;
	
	public Menu( ActionListener listener, DrawAreaProperties drawProp ) {
		this.listener = listener;
		this.drawProp = drawProp;
		preferences = null;
		CreateMenuBar();
	}

	/*
	 * Zette de grootte van het menu
	 */
	public void setSize( ) {
		setBounds(0, 0, DisplaySize.SCREENWIDTH, DisplaySize.MENUHEIGHT);
		repaint();
		updateUI();
		// de menubar zijn breedte is variabel, zijn hoogte niet.
		// als de gebruiker zijn displaywindow vergroot mag dus enkel de breedte
		// worden aangepast
		// setMinimumSize( d );
		// setMaximumSize( d );
		// setPreferredSize( d );
	}

	/*
	 * event handler
	 */
	public void actionPerformed( ActionEvent e ) {
		try {
			String actionCommand = e.getActionCommand( );
			if ( actionCommand.equals( "About" ))
				about( );
			else if ( actionCommand.equals("Preferences"))
				pref( );
			else if ( actionCommand.equals( "Selected Line Color" ))
				colorSelector( DrawAreaProperties.SELECTED_LINE, actionCommand );
			else if ( actionCommand.equals( "Hoovered Line Color" ))
				colorSelector( DrawAreaProperties.HOOVERED_LINE, actionCommand );
			else if ( actionCommand.equals( "Unselected Line Color" ))
				colorSelector( DrawAreaProperties.UNSELECTED_LINE, actionCommand );
			else if ( actionCommand.equals( "Selected Point Color" ))
				colorSelector( DrawAreaProperties.SELECTED_POINT, actionCommand );
			else if ( actionCommand.equals( "Hoovered Point Color" ))
				colorSelector( DrawAreaProperties.HOOVERED_POINT, actionCommand );
			else if ( actionCommand.equals( "Unselected Point Color" ))
				colorSelector( DrawAreaProperties.UNSELECTED_POINT, actionCommand );
			else if ( actionCommand.equals( "Line" )) {				
				JComboBox tmp = (JComboBox) e.getSource();
				Integer tmp2 = (Integer)tmp.getSelectedItem( );
				drawProp.setTickness( tmp2, DrawAreaProperties.LINE );
			}
			// zal ervoor zorgen dat het pop-up menu verdwijnt
			tools.setPopupMenuVisible( false );
			algorithms.setPopupMenuVisible( false );
			toolsSub.setPopupMenuVisible( false );
			toolsSub2.setPopupMenuVisible( false );
			algorithmsSub.setPopupMenuVisible( false );
			algorithmsSub2.setPopupMenuVisible( false );
		} catch( Exception ex ) {
			;
		}
	}		

	// Zal de menuBar opstellen
	private void CreateMenuBar( ) {
		setSize( );
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
		menu.getAccessibleContext() .setAccessibleDescription( description );
		add( menu );

	}

	// maakt een JMenuItem object aan. Deze stellen de verschillende keuzes voor
	// die in de menubalk staan
	private void CreateMenuItem( String name, int keyEvent, String description,
			String icon ) {
		menuItem = new JMenuItem( name, keyEvent );

		if ( icon != null ) {
//			ImageIcon a = new ImageIcon(icon);
//			menuItem = new JMenuItem(name, a);
			URL imgURL = ClassLoader.getSystemResource( icon );
			ImageIcon imgIcon = new ImageIcon( Toolkit.getDefaultToolkit( ).getImage( imgURL ));
			menuItem.setIcon( imgIcon );
		}

		menuItem.setAccelerator( KeyStroke.getKeyStroke(keyEvent,
				ActionEvent.CTRL_MASK ));
		menuItem.getAccessibleContext( ).setAccessibleDescription( description );
		menu.add( menuItem );
	}

	/*
	 * Maakt de menu optie file aan.
	 * Deze functie zal de verschillende menu items en functionaliteit aanmaken van de menu optie file 
	 */
	private void makeFile( ) {
		// menu object aanmaken
		CreateMenu( "File", KeyEvent.VK_F, "" );
		CreateMenuItem( "New File", KeyEvent.VK_N, "Create a new file",
		"CurveEditor/GUI/icons/filenew.png" );
		menuItem.addActionListener( listener );

		CreateMenuItem( "Open File", KeyEvent.VK_O, "Open a file",
		"CurveEditor/GUI/icons/fileopen.png" );
		menuItem.addActionListener( listener );

		menu.addSeparator( );

		CreateMenuItem( "Save File", KeyEvent.VK_S, "Save a file",
		"CurveEditor/GUI/icons/filesave.png" );		
		menuItem.addActionListener( listener );

		CreateMenuItem( "Save As...", KeyEvent.VK_O, "Save a file as ...",
		"CurveEditor/GUI/icons/filesaveas.png" );
		menuItem.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_S, ActionEvent.SHIFT_MASK + ActionEvent.CTRL_MASK ));
		menuItem.addActionListener( listener );

		menu.addSeparator( );

		CreateMenuItem( "Screen Shot", KeyEvent.VK_M, "Make a screen shot", 
				"CurveEditor/GUI/icons/camera.png" );		
		menuItem.addActionListener( listener );

		menu.addSeparator( );

		CreateMenuItem( "New Curve", KeyEvent.VK_C, "Start a new curve",
		"CurveEditor/GUI/icons/curvenew.png" );
		menuItem.addActionListener( listener );

		menu.addSeparator( );

		CreateMenuItem( "Quit Program", KeyEvent.VK_Q, "Quit Curve Editor",
		"CurveEditor/GUI/icons/exit.png" );
		menuItem.addActionListener( new ActionListener( ) {
			public void actionPerformed( ActionEvent e ) {
				int n = JOptionPane.showConfirmDialog(null,
						"Do you really want to quit?", "Quit Curve Editor",
						JOptionPane.YES_NO_OPTION );
				if ( n == 0 )
					System.exit( 0 );
			}

		});
	}

	/*
	 * Maakt de menu optie edit aan.
	 * Deze functie zal de verschillende menu items en functionaliteit aanmaken van de menu optie edit 
	 */
	private void makeEdit( ) {
		CreateMenu( "Edit", KeyEvent.VK_E, "" );

		CreateMenuItem( "Undo", KeyEvent.VK_U, "Undo last action.",
		"CurveEditor/GUI/icons/undo.png" );
		menuItem.addActionListener( listener );

		CreateMenuItem( "Redo", KeyEvent.VK_R, "Redo last action.",
		"CurveEditor/GUI/icons/redo.png" );
		menuItem.addActionListener( listener );

		menu.addSeparator( );

		CreateMenuItem( "Preferences", KeyEvent.VK_P,
				"Make adjustments to Curve Editor", 
				"CurveEditor/GUI/icons/edit.png" );
		menuItem.addActionListener( listener );
		menuItem.addActionListener( this );
	}

	/*
	 * Maakt de menu optie tools aan.
	 * Deze functie zal de verschillende menu items en functionaliteit aanmaken van de menu optie tools 
	 */
	private void makeTools( ) {
		CreateMenu( "Tools", KeyEvent.VK_T, "" );
		tools = menu;
		group = new ButtonGroup( );

		JRadioButton button = new JRadioButton( "none" );
		button.setSelected( true );
		button.addActionListener( listener );
		button.setToolTipText( "No tools selected" );

		group.add( button );
		menu.add( button );

		button = new JRadioButton( "Path Simulation" );
		button.setSelected( false );
		button.addActionListener( listener );
		button.addActionListener( this );
		button.setToolTipText( "Simulate a path" );

		group.add( button );
		menu.add( button );

		JMenu menu2 = new JMenu( "Point" );
		toolsSub = menu2;
		toolsSub.addActionListener( this );
		menu2.getAccessibleContext( ).setAccessibleDescription( "" );
		menu2.setToolTipText( "Tools for point Manipulation" );
		menu.add( menu2 );
		createPointPanel( menu2 );

		menu2 = new JMenu( "Curve" );
		toolsSub2 = menu2;
		toolsSub.addActionListener( this );
		menu2.getAccessibleContext( ).setAccessibleDescription( "" );
		menu2.setToolTipText( "Tools for curve Manipulation" );
		menu.add( menu2 );
		createCurvePanel( menu2 );
	}

	/*
	 * Creert het submenu Point van de menu optie tools
	 * Deze functie zal de verschillende menu items en functionaliteit aanmaken van het submenu point 
	 */
	private void createPointPanel( JMenu menu ) {
		JPanel radioPanel = new JPanel( );
		radioPanel.setLayout( new BoxLayout( radioPanel, BoxLayout.Y_AXIS ));

		JRadioButton button = new JRadioButton( "Select Point" );
		button.setSelected( false );
		button.addActionListener( listener );
		button.addActionListener( this );
		
		group.add( button );
		radioPanel.add( button );

		button = new JRadioButton( "Move Point" );
		button.setSelected( false );
		button.addActionListener( listener );
		button.addActionListener( this );
		
		group.add( button );
		radioPanel.add( button );

		button = new JRadioButton( "Deselect All Points" );
		button.setSelected( false );
		button.addActionListener( listener );
		button.setToolTipText( "Deselect all that was selected" );
		button.addActionListener( this );
		
		group.add( button );
		radioPanel.add( button );

		button = new JRadioButton( "Delete Point" );
		button.setSelected( false );
		button.addActionListener( listener );
		button.addActionListener( this );
		
		group.add( button );
		radioPanel.add( button );

		button = new JRadioButton( "Deselect Point" );
		button.setSelected( false );
		button.addActionListener( listener );
		button.addActionListener( this );
		
		group.add( button );
		radioPanel.add( button );

		menu.add( radioPanel );
	}

	/*
	 * Creert het submenu Curve van de menu optie tools
	 * Deze functie zal de verschillende menu items en functionaliteit aanmaken van het submenu Curve 
	 */
	private void createCurvePanel( JMenu menu ) {
		JPanel radioPanel = new JPanel( );
		radioPanel.setLayout( new BoxLayout(radioPanel, BoxLayout.Y_AXIS ));

		JRadioButton button = new JRadioButton( "Select Curve" );
		button.setSelected( true );
		button.addActionListener( listener );
		button.addActionListener( this );
		button.addActionListener( this );
		
		group.add( button );
		radioPanel.add( button );

		button = new JRadioButton( "Move Curve" );
		button.setSelected( false );
		button.addActionListener( listener );
		button.addActionListener( this );
		
		group.add( button );
		radioPanel.add( button );

		button = new JRadioButton( "Connect Curve" );
		button.setSelected( false );
		button.addActionListener( listener );
		button.addActionListener( this );
		
		group.add(button);
		radioPanel.add(button);

		button = new JRadioButton( "Select All Curves" );
		button.setSelected( false );
		button.addActionListener( listener );
		button.setToolTipText( "Select everything" );
		button.addActionListener( this );
		
		group.add( button );
		radioPanel.add( button );

		button = new JRadioButton( "Deselect All Curves" );
		button.setSelected( true );
		button.addActionListener( listener );
		button.setToolTipText( "Deselect all that was selected" );
		button.addActionListener( this );
		
		group.add( button );
		radioPanel.add( button );

		button = new JRadioButton( "Delete Curve" );
		button.setSelected( false );
		button.addActionListener( listener );
		button.addActionListener( this );
		
		group.add( button );
		radioPanel.add( button );

		menu.add( radioPanel );
	}

	/*
	 * Maakt de menu optie algorithms aan.
	 * Deze functie zal de verschillende menu items en functionaliteit aanmaken van de menu optie algorithms
	 */
	private void makeAlgorithms( ) {
		CreateMenu( "Algorithms", KeyEvent.VK_A, "" );
		group = new ButtonGroup( );
		algorithms = menu;
		algorithms.addActionListener( this );
		
		JMenu menu2 = new JMenu( "Bezier" );
		algorithmsSub = menu2;
		algorithmsSub.addActionListener( this );
		menu2.setMnemonic( KeyEvent.VK_B );
		menu2.getAccessibleContext( ).setAccessibleDescription( "" );
		menu.add( menu2 );
		createBezierPanel( menu2 );

		menu2 = new JMenu( "Hermite" );
		algorithmsSub2 = menu2;
		algorithmsSub2.addActionListener( this );
		menu2.setMnemonic( KeyEvent.VK_H );
		menu2.getAccessibleContext( ).setAccessibleDescription( "" );
		menu.add( menu2 );
		createHermitePanel( menu2 );
	}

	/*
	 * Creert het submenu Bezier van de menu optie Algorithms
	 * Deze functie zal de verschillende menu items en functionaliteit aanmaken van het submenu Bezier 
	 */
	private void createBezierPanel( JMenu menu ) {
		JPanel radioPanel = new JPanel( );
		radioPanel.setLayout( new BoxLayout( radioPanel, BoxLayout.Y_AXIS ));

		JRadioButton algName = new JRadioButton( "Linear" );
		algName.setSelected( true );
		algName.addActionListener( listener );
		algName.addActionListener( this );
		
		group.add( algName );
		radioPanel.add( algName );

		algName = new JRadioButton( "Bezier C0" );
		algName.setSelected( false );
		algName.addActionListener( listener );
		algName.addActionListener( this );
		
		group.add( algName );
		radioPanel.add(algName);

		algName = new JRadioButton("Bezier G1");
		algName.setSelected(false);
		algName.addActionListener(listener);
		algName.addActionListener( this );
		
		group.add(algName);
		radioPanel.add(algName);

		algName = new JRadioButton("Bezier C1");
		algName.setSelected(false);
		algName.addActionListener(listener);
		algName.addActionListener( this );
		
		group.add(algName);
		radioPanel.add(algName);

		menu.add(radioPanel);
	}

	/*
	 * Creert het submenu Bezier van de menu optie Algorithms
	 * Deze functie zal de verschillende menu items en functionaliteit aanmaken van het submenu Bezier 
	 */
	private void createHermitePanel( JMenu menu ) {
		JPanel radioPanel = new JPanel( );
		radioPanel.setLayout( new BoxLayout(radioPanel, BoxLayout.Y_AXIS ));

		JRadioButton algName = new JRadioButton( "Normal" );
		algName.setSelected( true );
		algName.addActionListener( listener );
		algName.addActionListener( this );
		
		group.add( algName );
		radioPanel.add( algName );

		algName = new JRadioButton( "Cardinal" );
		algName.setSelected( false );
		algName.addActionListener( listener );
		algName.addActionListener( this );
		
		group.add( algName );
		radioPanel.add( algName );

		algName = new JRadioButton( "CatmullRom" );
		algName.setSelected( false );
		algName.addActionListener( listener );
		algName.addActionListener( this );
		
		group.add( algName );
		radioPanel.add( algName );

		algName = new JRadioButton( "Kochanek Bartels" );
		algName.setSelected( false );
		algName.addActionListener( listener );
		algName.addActionListener( this );
		
		group.add( algName );
		radioPanel.add( algName );

		menu.add( radioPanel );
	}

	/*
	 * Deze functie zal het system command opgegeven door "string" uitvoeren
	 */
	
	void runCommand( String string ) {
	 try {
		 Process p = Runtime.getRuntime( ).exec( string );
	 }
	 catch ( IOException e ) {
		 e.printStackTrace( );
	 }
	}
	
	/*
	 * Maakt de menu optie help aan.
	 * Deze functie zal de verschillende menu items en functionaliteit aanmaken van de menu optie help
	 */
	private void makeHelp( ) {
		// een glue element toevoegen zorgt ervoor dat help rechts wordt
		// uitgelijnd
		add(Box.createHorizontalGlue( ));
		CreateMenu( "Help", KeyEvent.VK_H, "" );
		add( Box.createRigidArea( new Dimension( 10, 0 )));

		CreateMenuItem( "Quick Howto's", KeyEvent.VK_F1, "Quick Howto's", 
		"CurveEditor/GUI/icons/fork.png" );
		menuItem.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_F1, 0 ));
		menuItem.addActionListener( new ActionListener( ) {
			public void actionPerformed( ActionEvent e ) {
				runCommand( "firefox http://student.uhasselt.be/~0522971/trpr/inhoud.html#start" );
			}
		});

		CreateMenuItem("Documentation", KeyEvent.VK_F2,
				"Full guide to Curve Editor", 
		"CurveEditor/GUI/icons/help.png" );
		menuItem.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_F2, 0 ));
		menuItem.addActionListener( new ActionListener( ) {
			public void actionPerformed( ActionEvent e ) {
				runCommand( "firefox http://student.uhasselt.be/~0522971/trpr/help.html" );
			}
		});

		menu.addSeparator();

		CreateMenuItem("About", KeyEvent.VK_F3, "About", 
				"CurveEditor/GUI/icons/info.png" );
		menuItem.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_F3, 0 ));
		menuItem.addActionListener( this );
	}

	/*
	 * Zal het message dialoog About aanmaken
	 * te bereiken via de menu optie help
	 */
	private void about() {
		Box hbox = Box.createHorizontalBox();
		URL imgURL = ClassLoader.getSystemResource( "CurveEditor/GUI/icons/monkey.jpg" );
		ImageIcon imgIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(imgURL));
		hbox.add( new JLabel( imgIcon ));
		hbox.add(new JLabel(
				"<html>"
				+ "<p style='font-family: arial;font-weight: bold;margin-left: 20px'>Coding and developing</p><br />"
				+ "<p style='font-family: arial;font-weight: lighter;margin-left: 20px'>"
				+ "Sibrand Staessens<br />"
				+ "Sibren Polders<br />"
				+ "</p><br />"
				+ "<p style='font-family: arial;font-weight: bold;margin-left: 20px'>Special thanks to: </p><br />"
				+ "<p style='font-family: arial;font-weight: lighter;margin-left: 20px'>"
				+ "William van Haevere" + "</p>"
				+ "</html>"));
		JOptionPane.showMessageDialog( null, hbox, "CurveEditor - About",
				JOptionPane.PLAIN_MESSAGE );
	}

	/*
	 * Zal de messagediallog voor preferences initialiseren
	 */
	private void initPref( ) {
		preferences = new JDialog( );
		preferences.setTitle("Curve Editor - Preferences");
		preferences.setModal(true);
		preferences.setResizable( false );
		prefContainer = preferences.getContentPane();
		prefContainer.setLayout( new BoxLayout( prefContainer, BoxLayout.Y_AXIS ));
		Dimension d = new Dimension( 450, 200 );
		prefContainer.setSize( d );
		prefContainer.setMaximumSize( d );
		prefContainer.setMinimumSize( d );
		prefContainer.setPreferredSize( d );
	}
	
	/*
	 * Zal een message dialoog voor preferences aanmaken
	 */
	private void pref() throws InvalidArgumentException {
		Dimension dX = new Dimension( 5, 0 );
		Dimension dY = new Dimension( 0, 5 );	

		initPref( );
		
		JPanel hbox = new JPanel( );
		hbox.setLayout( new BoxLayout( hbox, BoxLayout.X_AXIS ));
		hbox.add( Box.createRigidArea( dX ));
		
		container = new JPanel( );
		container.setLayout( new BoxLayout( container, BoxLayout.Y_AXIS ));
		container.setBorder( BorderFactory.createTitledBorder( "Change Colors" ));
		makeColor( "Selected Line Color", DrawAreaProperties.SELECTED_LINE );
		makeColor( "Hoovered Line Color", DrawAreaProperties.HOOVERED_LINE );
		makeColor( "Unselected Line Color", DrawAreaProperties.UNSELECTED_LINE );
		container.add( Box.createRigidArea( dY ));
		makeColor( "Selected Point Color", DrawAreaProperties.SELECTED_POINT);
		makeColor( "Hoovered Point Color", DrawAreaProperties.HOOVERED_POINT );
		makeColor( "Unselected Point Color", DrawAreaProperties.UNSELECTED_POINT );
		container.add( Box.createVerticalGlue() );
		hbox.add( container );

		container = new JPanel( );
		container.setLayout( new BoxLayout( container, BoxLayout.Y_AXIS ));
		container.setBorder( BorderFactory.createTitledBorder( "Thickness" ));
		makeThickness( "Line", DrawAreaProperties.LINE );
		container.add( Box.createRigidArea( new Dimension( 0, 5 )));
		makeThickness( "Point", DrawAreaProperties.POINT );
		container.add( Box.createVerticalGlue() );
		hbox.add( container );
		hbox.add( Box.createRigidArea( dX ));
		
		prefContainer.add( hbox );
		
		hbox = new JPanel( );
		hbox.setLayout( new BoxLayout( hbox, BoxLayout.X_AXIS ));
		hbox.add( Box.createHorizontalGlue( ));

		JButton button = new JButton( "Ok" );
		button.addActionListener( new ActionListener( ) {
			public void actionPerformed(ActionEvent e) {
				drawProp.makeAdjustments();
				preferences.dispose();				
			}			
		});
		
		button.addActionListener( listener );
		Dimension d = new Dimension( 80, 20 );
		button.setSize( d );
		button.setMaximumSize( d );
		button.setMaximumSize( d );
		button.setPreferredSize( d );

		hbox.add( button);

		hbox.add( Box.createRigidArea( new Dimension( 5, 0 )));

		button = new JButton( "Cancel" );
		button.addActionListener( new ActionListener( ) {
			public void actionPerformed(ActionEvent e) {
				drawProp.cancelAdjustments();
				preferences.dispose();
			}

		});
		button.setSize( d );
		button.setMaximumSize( d );
		button.setMaximumSize( d );
		button.setPreferredSize( d );
		hbox.add( button);

		hbox.add( Box.createRigidArea( new Dimension( 5, 0 )));

		prefContainer.add( hbox );

		preferences.pack( );
		preferences.setVisible( true );
	}

	/*
	 * Deze functie maakt een selectieveld aan in het dialoog Preferences
	 * meer bepaald maakt het de verschillende kleurselectie mogelijk
	 * Hij zorgt hiervoor dat er een label wordt ingeladen samen met een button die doorverwijst naar
	 * een kleurselectie panel. De button heeft dezelfde kleur als het overeenkomstige label aanduid
	 */
	private void makeColor( String string, int colorOf ) throws InvalidArgumentException {
		JPanel container2 = new JPanel( );
		container2.setLayout( new BoxLayout( container2, BoxLayout.X_AXIS ));
		
		container2.add( Box.createRigidArea( new Dimension( 5, 0 ) ));
		
		JLabel label = new JLabel(
				"<html>"
				+ "<p style='font-family: arial;font-weight:lighter;'>"
				+ string + " : "
				+ "</p>"
				+ "</html>" );
		Dimension d = new Dimension( 150, 20 );
		label.setSize( d );
		label.setPreferredSize( d );
		label.setMinimumSize( d );
		label.setMaximumSize( d );
		container2.add( label );
		
		container2.add( Box.createRigidArea( new Dimension( 5, 0 ) ));
		
		JButton button = new JButton( "" );
		d = new Dimension( 30, 20 );
		button.setSize( d );
		button.setMinimumSize( d );
		button.setMaximumSize( d );
		button.setPreferredSize( d );
		button.setActionCommand( string );
		button.setBackground( drawProp.getColor( colorOf ));
		button.setBorder( BorderFactory.createLineBorder( Color.white, 5 ));
		button.addActionListener( this );
		container2.add( button );
		
		container2.add( Box.createRigidArea( new Dimension( 5, 0 ) ));
		container2.add( Box.createHorizontalGlue() );
		
		container.add( container2 );
	}

	/*
	 * Deze functie maakt een selectieveld aan in het dialoog Preferences
	 * meer bepaald maakt het de lijn en punt dikte selectie mogelijk
	 * Hij zorgt hiervoor dat er een label wordt ingeladen combobox.
	 */
	private void makeThickness( String string, int thicknessOf  ) {
		thickness = thicknessOf;
		JPanel container2 = new JPanel( );
		container2.setLayout( new BoxLayout( container2, BoxLayout.X_AXIS ));
		
		container2.add( Box.createRigidArea( new Dimension( 5, 0 )));
		
		JLabel label = new JLabel(
				"<html>"
				+ "<p style='font-family: arial;font-weight: lighter'>" + string + " Thickness: </p>"
				+ "</html>" );

		Dimension d = new Dimension( 150, 20 );
		label.setSize( d );
		label.setPreferredSize( d );
		label.setMinimumSize( d );
		label.setMaximumSize( d );
		container2.add( label );
		
		container2.add(Box.createRigidArea(new Dimension(5, 0)));
		
		JComboBox comboBox = new JComboBox( DrawAreaProperties.THICKNESS_CHOICES );
		// comboPanel.setLayout(new BoxLayout(comboPanel, BoxLayout.Y_AXIS));
		// comboPanel.add(Box.createHorizontalGlue());

		comboBox.setSelectedItem( new Integer( drawProp.getTickness( thicknessOf )));
		comboBox.setActionCommand( string );
		comboBox.addActionListener( this );
		if ( thicknessOf == DrawAreaProperties.LINE )
			// inline action listeer toevoegen
			comboBox.addActionListener( new ActionListener( ) {
				public void actionPerformed( ActionEvent e ) {
					JComboBox tmp = (JComboBox) e.getSource();
					Integer tmp2 = (Integer)tmp.getSelectedItem( );
					drawProp.setTickness( tmp2, DrawAreaProperties.LINE );
				}			
			});
		else if ( thicknessOf == DrawAreaProperties.POINT )
			// inline action listeer toevoegen
			comboBox.addActionListener( new ActionListener( ) {
				public void actionPerformed( ActionEvent e ) {
					JComboBox tmp = (JComboBox) e.getSource();
					Integer tmp2 = (Integer)tmp.getSelectedItem( );
				
					drawProp.setTickness( tmp2, DrawAreaProperties.POINT );
				}			
			});
		d = new Dimension( 50, 20 );
		comboBox.setMaximumSize( d );
		comboBox.setMinimumSize( d );
		comboBox.setPreferredSize( d );
		container2.add( comboBox );
		
		container2.add( Box.createRigidArea( new Dimension(5, 0 )));
		
		container.add( container2 );
	}

	/*
	 * Geeft een selectie dialoog weer om kleuren te kunnen kiezen.
	 * Het click event wordt hier intern afgehandeld om zo onnodige omslachtigheid te voorkomen
	 */
	private void colorSelector( int colorOf, String string ) throws InvalidArgumentException {
		Color newColor = JColorChooser.showDialog( this, "CurveEditor - Color Chooser", drawProp.getColor( colorOf ));
		
		if ( newColor != null )
			switch(colorOf) {
			case DrawAreaProperties.SELECTED_LINE:
			case DrawAreaProperties.HOOVERED_LINE:
			case DrawAreaProperties.UNSELECTED_LINE:
			case DrawAreaProperties.SELECTED_POINT:
			case DrawAreaProperties.HOOVERED_POINT:
			case DrawAreaProperties.UNSELECTED_POINT:
			case DrawAreaProperties.BACKGROUND:
				drawProp.setColor( colorOf, newColor );
				break;
			default:
				throw new InvalidArgumentException( "Menu: void lineColorSelector( int ): No such  option: " + 
						colorOf );
			}
	}
}
