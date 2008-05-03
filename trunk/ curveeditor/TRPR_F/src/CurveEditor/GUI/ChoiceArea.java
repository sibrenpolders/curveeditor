package CurveEditor.GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.util.EventListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextPane;
import javax.swing.border.Border;

import CurveEditor.Curves.Point;

public class ChoiceArea extends JPanel implements ActionListener {

	// private Vector<Algorithm> v;
	final static private String[] bezierAlgTypeNames = { "Linear", "Bezier C0",
			"Bezier G1", "Bezier C1" };
	final static private String[] hermiteAlgTypeNames = { "Hermite Normal",
			"Cardinal", "Catmull-Rom" };
	private String[] currentAlgTypeNames;
	private JComboBox type;
	private JPanel checkPanel;
	private JCheckBox box;	
	private ActionListener listener;
	private ItemListener itemListener;
	
	public ChoiceArea(ActionListener listener, ItemListener itemListener ) {
		super(new BorderLayout());
		this.listener = listener;
		this.itemListener = itemListener;
		currentAlgTypeNames = bezierAlgTypeNames;
		init();
	}

	private void init() {
		add(Box.createRigidArea(new Dimension(10, 0)));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		add(Box.createRigidArea(new Dimension(0, 5)));
		createRadioPanel();
		add(Box.createRigidArea(new Dimension(0, 5)));
		createComboBox();
		add(Box.createRigidArea(new Dimension(0, 5)));		
		createInputField();
		add(Box.createRigidArea(new Dimension(0, 5)));
		createCheckBox();
		
		add(Box.createVerticalGlue());

		setBorder(BorderFactory.createMatteBorder(0, 0, 0, 5, Color.BLACK));
	}

	private void createInputField() {
		JPanel inputFieldPanel = new JPanel();
		inputFieldPanel.setLayout(new BoxLayout(inputFieldPanel,
				BoxLayout.Y_AXIS));

		JPanel row = new JPanel();
		row.setLayout(new BoxLayout(row, BoxLayout.X_AXIS));
		createInput(row, "x");
		inputFieldPanel.add(row);

		inputFieldPanel.add(Box.createRigidArea(new Dimension(0, 5)));
		row = new JPanel();
		row.setLayout(new BoxLayout(row, BoxLayout.X_AXIS));
		createInput(row, "y");
		inputFieldPanel.add(row);

		Dimension d = new Dimension(250, 100);
		inputFieldPanel.setMaximumSize(d);
		inputFieldPanel.setMinimumSize(d);

		add(inputFieldPanel);
	}

	private void createInput(JPanel parent, String textString) {
		// label aanmaken
		JLabel label = new JLabel("<html><span style='font-weight: bolder'>"
				+ textString + ": </span></html>: ");
		Dimension d = new Dimension(20, 100);
		label.setMaximumSize(d);
		label.setMinimumSize(d);
		parent.add(label);

		// textinputveld aanmaken
		JTextPane text = new JTextPane();
		d = new Dimension(230, 100);
		text.setMaximumSize(d);
		text.setMinimumSize(d);
		text.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		parent.add(text);

		parent.add(Box.createRigidArea(new Dimension(5, 0)));
	}

	private void createComboBox() {
		JPanel comboPanel = new JPanel();
		comboPanel.setLayout(new BoxLayout(comboPanel, BoxLayout.X_AXIS));
		comboPanel.add(Box.createHorizontalGlue());

		type = new JComboBox(currentAlgTypeNames);
		type.setSelectedIndex(0);
		type.addActionListener(this);
		type.addActionListener(listener);
		Dimension d = new Dimension(210, 100);
		type.setMaximumSize(d);
		type.setMinimumSize(d);
		comboPanel.add(type);

		comboPanel.add(Box.createRigidArea(new Dimension(5, 0)));

		add(type);
	}

	private void createRadioPanel() {
		JPanel radioPanel = new JPanel();
		radioPanel.setLayout(new BoxLayout(radioPanel, BoxLayout.X_AXIS));
		radioPanel.add(Box.createHorizontalGlue());
		ButtonGroup algGroep = new ButtonGroup();

		JRadioButton algName = new JRadioButton("Bezier");
		algName.setMnemonic(KeyEvent.VK_Z);
		algName.setSelected(true);
		algName.addActionListener(this);
		algName.addActionListener(listener);

		algGroep.add(algName);
		radioPanel.add(algName);

		algName = new JRadioButton("Hermite");
		algName.setMnemonic(KeyEvent.VK_T);
		algName.setSelected(false);
		algName.addActionListener(this);
		algName.addActionListener(listener);

		algGroep.add(algName);

		radioPanel.add(algName);
		radioPanel.setBorder(BorderFactory.createTitledBorder("Algorithm"));
		Dimension d = new Dimension(250, 100);
		radioPanel.setMaximumSize(d);
		radioPanel.setMinimumSize(d);
		radioPanel.add(Box.createRigidArea(new Dimension(5, 0)));

		add(radioPanel);
	}

	private void createCheckBox( ) {
		checkPanel = new JPanel( );
		checkPanel.setLayout( new BoxLayout( checkPanel, BoxLayout.Y_AXIS ));			
		
		createCheckBoxItem( "Coordinates" );
		createCheckBoxItem( "Tangens" );
		createCheckBoxItem( "Numbers" );
		
		
		Dimension d = new Dimension(250, 100);
		checkPanel.setMaximumSize(d);
		checkPanel.setMinimumSize(d);
		checkPanel.add(Box.createHorizontalGlue());
		checkPanel.add(Box.createRigidArea(new Dimension(5, 0)));
		checkPanel.setBorder( BorderFactory.createTitledBorder( "Show" ));
		
		add( checkPanel );
	}
	
	private void createCheckBoxItem( String name ) {
		JCheckBox item = new JCheckBox( name );
		item.addItemListener( itemListener );
		item.setSelected( false );
		checkPanel.add( item );
	}
	
	private void createCurveEditeMode( ) {
		
	}
	
	private void createPointEditMode( ) {
		
	}
	
	public void refresh() {

	}

	public Point newPointInput() {
		return null;
	}

	public Point goToPoint() {
		return null;
	}

	public Point newPointClick() {
		return null;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Bezier"))
			type.setModel(new DefaultComboBoxModel(bezierAlgTypeNames));
		else if (e.getActionCommand().equals("Hermite"))
			type.setModel(new DefaultComboBoxModel(hermiteAlgTypeNames));
	}
}
