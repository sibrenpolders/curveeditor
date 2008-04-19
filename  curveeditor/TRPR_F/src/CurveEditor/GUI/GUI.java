package CurveEditor.GUI;

import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import CurveEditor.Core.Editor;
import CurveEditor.Curves.Curve;
import CurveEditor.Curves.Point;

public class GUI extends Editor implements MenuListener, MouseListener, ActionListener {
	protected ChoiceArea choice;
	protected DrawArea draw;
	private JMenu menu;
	private JMenuItem menuItem;
	private JMenuBar menuBar;
	
	public GUI() {
		super();
		JFrame frame = new JFrame("Curve Editor");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setTitle("Curve Editor");
		Container contentPane = frame.getContentPane();
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));

		CreateMenuBar();
		contentPane.add(menuBar);
		
		JPanel screen = new JPanel();
		screen.setLayout( new BoxLayout( screen, BoxLayout.X_AXIS ) );

		screen.add( Box.createRigidArea( new Dimension( 10, 0 )));
		
		choice = new ChoiceArea( new ChoiceAreaListener( ) );
		screen.add( choice );
		
		draw = new DrawArea(this.curves, this.selectedCurves);
		draw.addMouseListener(this);		
		screen.add(draw);			
		
		screen.add( Box.createRigidArea( new Dimension( 10, 0 )));
		screen.add( Box.createHorizontalGlue() );
		
		contentPane.add( screen );
		
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

		CreateMenuBar();
		contentPane.add(menuBar);

		draw = new DrawArea(this.curves, this.selectedCurves);
		draw.addMouseListener(this);
		contentPane.add(draw);

		frame.pack();
		frame.setVisible(true);
	}

	public void testMethod() {
		setCurrentAlgorithm('H', (short)1);
		selectedCurves.add(new Curve(currentAlgorithm.getType(),
				currentAlgorithm.getDegree()));
		selectedCurves.get(0).addInput(new Point(0, 0));
		selectedCurves.get(0).addInput(new Point(50, 50));
		selectedCurves.get(0).addInput(new Point(50, 120));
		selectedCurves.get(0).addInput(new Point(100, 50));
		
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
				Curve c = selectedCurves.get(i);
				c.addInput(new Point(e.getX(), e.getY()));
				// Bij Hermiet ( type == 'H' ) is het 2de ingegeven punt
				// telkens de tangens. Dus er moet niet getekend worden voordat deze is ingegeven
				if ( c.getType() != 'H' || c.getInput().size() % 2 == 0 )
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

	// Zal de menuBar opstellen
	private void CreateMenuBar( ) {
		// Aanmaken van de menubar.
		menuBar = new JMenuBar();
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
		menuBar.add(menu);

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
		menuItem.addActionListener(new ActionListener( ){
			public void actionPerformed(ActionEvent e)
			{
				reset( );
				draw.reset(curves, selectedCurves);	
			}
		} );

		CreateMenuItem("Open", KeyEvent.VK_O, "Open a file", "src/CurveEditor/GUI/icons/fileopen.png");
		menuItem.addActionListener( this );
		menu.addSeparator();

		CreateMenuItem("Save", KeyEvent.VK_S, "Save a file", "src/CurveEditor/GUI/icons/filesave.png");
		menuItem.addActionListener(this);
		
		CreateMenuItem("Save As...", KeyEvent.VK_O, "Save a file as ...", "src/CurveEditor/GUI/icons/filesaveas.png");
		menuItem.addActionListener(this);
		
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
		menuItem.addActionListener(this);
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
				System.out.println("Bezier");
			}
		} );

		CreateMenuItem( "Hermite", KeyEvent.VK_H, "Use Hermite for curve calculation", null );
		menuItem.addActionListener(new ActionListener( ){
			public void actionPerformed(ActionEvent e)
			{
				System.out.println( "Hermite" );
			}
		} );
	}

	private void makeHelp( ) {
		// een glue element toevoegen zorgt ervoor dat help rechts wordt uitgelijnd
		menuBar.add( Box.createHorizontalGlue() );
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
		menuItem.addActionListener(this);
	}

	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if ( cmd.equals("Open") )
			open();
		else if ( cmd.equals( "Save") )
			save();
		else if ( cmd.equals("Save As ...") )
			saveAs();
		else if ( cmd.equals("About") )
			about();
		else if ( cmd.equals("Preferrences") )
			preferrences();
	}		

	private void open( ) {
		JFileChooser jfc = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"CurveEditor files (*.xml)", "xml");
		
		jfc.setFileFilter(filter);
		if( JFileChooser.APPROVE_OPTION == jfc.showOpenDialog(draw) )
				file.load( jfc.getSelectedFile().getAbsolutePath(), curves );
}

	private void save( ) {
		// checken of er reeds een bestandsnaam gekent is waarnaar opgeslagen moet worden.
		String fileName = file.getCurrentFilename();
		
		if ( null == fileName )
			saveAs();
		else
			file.save( fileName, curves );
	}

	private void saveAs( ) {
		JFileChooser jfc = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter( "CurveEditor files (*.xml)", "xml" );
		jfc.setFileFilter(filter);
		if( JFileChooser.APPROVE_OPTION == jfc.showSaveDialog(draw) )
			file.save( jfc.getSelectedFile().getAbsolutePath(), curves );
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

	private class ChoiceAreaListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String actionCommand = e.getActionCommand();
			
			if ( actionCommand.equals( "Bezier" ))
				currentAlgorithm = getAlgorithm('b', (short) 1);
			else if ( actionCommand.equals( "Hermite" ))
				currentAlgorithm = getAlgorithm('h', (short) 1);
		}
	}
}