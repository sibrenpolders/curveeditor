package CurveEditor.GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import CurveEditor.Curves.Point;

public class ChoiceArea extends JPanel implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -844431215144101927L
	;
	// private Vector<Algorithm> v;
	private static final String[] bezierAlgTypeNames = { "Linear", "Bezier C0",
			"Bezier G1", "Bezier C1" };
	private static final String[] hermiteAlgTypeNames = { "Hermite Normal",
			"Cardinal", "Catmull-Rom" };

	private static final int TOGGLEBUTTONWIDTH = 250;
	private static final int TOGGLEBUTTONHEIGHT = 25;

	private String[] currentAlgTypeNames;
	private JComboBox type;
	private JPanel checkPanel;
	private JPanel container; // container zal zorgen voor de uitlijning
	private JPanel curveEditPanel;
	private JPanel pointEditPanel;
	private JPanel currentPanel;
	private JTextField x, y; // nodig om de waarde van een punt ingegeven m.b.v
	// inputfield terug te geven
	private ButtonGroup group;
	private ActionListener listener;
	private ItemListener itemListener;

	public ChoiceArea(ActionListener listener, ItemListener itemListener) {
		super(new BorderLayout());
		this.listener = listener;
		this.itemListener = itemListener;
		currentAlgTypeNames = bezierAlgTypeNames;
		init();
	}

	private void init() {
		setSize();

		add(Box.createRigidArea(new Dimension(10, 0)));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		add(Box.createRigidArea(new Dimension(5, 5)));
		createRadioPanel();
		// add(Box.createRigidArea(new Dimension(0, 5)));
		// createComboBox();
		add(Box.createRigidArea(new Dimension(5, 5)));
		createInputField();
		add(Box.createRigidArea(new Dimension(5, 5)));
		createEditPanel();
		add(Box.createRigidArea(new Dimension(5, 5)));
		createCheckBox();

		add(Box.createVerticalGlue());

		setBorder(BorderFactory.createMatteBorder(0, 0, 0, 5, Color.BLACK));
	}

	// true == curveEditPlane
	// false == pointEditPlane
	public void toggleEditPanel(boolean value) {
		if (value) {
			pointEditPanel.setVisible(false);
			curveEditPanel.setVisible(true);
			return;
		}

		pointEditPanel.setVisible(true);
		curveEditPanel.setVisible(false);
	}

	// zorgt ervoor dat zowel point als curve edit panel onzichtbaar zijn ->
	// MODE == NONE
	public void deselect() {
		pointEditPanel.setVisible(false);
		curveEditPanel.setVisible(false);
	}

	// geeft null terug indien er momenteel geen ingegeven punt is.
	public Point getInputPoint() {
		// TODO juiste error check maken ? exception ?
		String xValue = x.getText();
		String yValue = y.getText();

		if (xValue.length() != 0 && yValue.length() != 0) {
			return new Point(Integer.parseInt(xValue), Integer.parseInt(yValue));
		}

		return null;
	}

	public void setSize() {
		setBounds(0, 0, DisplaySize.CHOICEWIDTH, DisplaySize.CHOICEHEIGHT);
		// setMinimumSize( d );
		// setMaximumSize( d );
		// setPreferredSize( d );
	}

	private void createInputField() {
		JPanel inputFieldPanel = new JPanel();
		inputFieldPanel.setLayout(new BoxLayout(inputFieldPanel,
				BoxLayout.Y_AXIS));

		JPanel row = new JPanel();
		row.setLayout(new BoxLayout(row, BoxLayout.X_AXIS));
		x = createInput(row, "x");
		inputFieldPanel.add(row);

		inputFieldPanel.add(Box.createRigidArea(new Dimension(0, 5)));
		row = new JPanel();
		row.setLayout(new BoxLayout(row, BoxLayout.X_AXIS));
		y = createInput(row, "y");
		inputFieldPanel.add(row);

		inputFieldPanel.setBorder(BorderFactory.createTitledBorder("Input"));
		Dimension d = new Dimension(TOGGLEBUTTONWIDTH + 15,
				TOGGLEBUTTONHEIGHT * 4 + 20);
		inputFieldPanel.setMaximumSize(d);
		inputFieldPanel.setMinimumSize(d);
		inputFieldPanel.setPreferredSize(d);

		row = new JPanel();
		JButton button = new JButton("ADD");
		button.setToolTipText("Add control point to selected curve(s).");
		button.addActionListener(listener);
		row.add(button);
		row.add(Box.createHorizontalGlue());

		inputFieldPanel.add(row);
		add(inputFieldPanel);
	}

	private JTextField createInput(JPanel parent, String textString) {
		// label aanmaken
		JLabel label = new JLabel("<html><span style='font-weight: bolder'>"
				+ textString + " : </span></html>: ");
		Dimension d = new Dimension(20, TOGGLEBUTTONHEIGHT);
		label.setMaximumSize(d);
		label.setMinimumSize(d);
		label.setPreferredSize(d);
		parent.add(label);

		// textinputveld aanmaken
		JTextField text = new JTextField();
		d = new Dimension(TOGGLEBUTTONWIDTH - 20, TOGGLEBUTTONHEIGHT);
		text.setMaximumSize(d);
		text.setMinimumSize(d);
		text.setPreferredSize(d);

		text.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		parent.add(text);

		parent.add(Box.createRigidArea(new Dimension(5, 0)));

		return text;
	}

	private void createComboBox() {
		JPanel comboPanel = new JPanel();
		// comboPanel.setLayout(new BoxLayout(comboPanel, BoxLayout.Y_AXIS));
		// comboPanel.add(Box.createHorizontalGlue());

		type = new JComboBox(currentAlgTypeNames);
		type.setSelectedIndex(0);
		type.addActionListener(this);
		type.addActionListener(listener);
		Dimension d = new Dimension(210, TOGGLEBUTTONHEIGHT);
		type.setMaximumSize(d);
		type.setMinimumSize(d);
		type.setPreferredSize(d);
		comboPanel.add(type);

		comboPanel.add(Box.createRigidArea(new Dimension(5, 0)));

		container.add(type);
	}

	private void createRadioPanel() {
		container = new JPanel(); // zorgt voor de layout
		container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

		JPanel radioPanel = new JPanel();
		radioPanel.setLayout(new BoxLayout(radioPanel, BoxLayout.X_AXIS));
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
		radioPanel.add(Box.createHorizontalGlue());

		container.add(radioPanel);
		container.add(Box.createRigidArea(new Dimension(0, 5)));
		createComboBox();

		container.setBorder(BorderFactory.createTitledBorder("Algorithm"));
		Dimension d = new Dimension(TOGGLEBUTTONWIDTH + 15, 100);
		// container.setMaximumSize( d );
		// container.setMinimumSize( d );
		// container.setPreferredSize( d );
		container.add(Box.createRigidArea(new Dimension(5, 0)));

		add(container);
	}

	private void createCheckBox() {
		JPanel container = new JPanel(); // zorgt voor de uitlijning
		container.setLayout(new BoxLayout(container, BoxLayout.X_AXIS));

		checkPanel = new JPanel();
		checkPanel.setLayout(new BoxLayout(checkPanel, BoxLayout.Y_AXIS));

		createCheckBoxItem("Coordinates");
		createCheckBoxItem("Tangents");
		createCheckBoxItem("Numbers");

		// checkPanel toevoegen aan container + uitlijning verzorgen
		container.add(checkPanel);
		container.add(Box.createHorizontalGlue());

		Dimension d = new Dimension(TOGGLEBUTTONWIDTH + 15, 100);
		// container.setMaximumSize( d );
		// container.setMinimumSize( d );
		// container.setPreferredSize( d );
		container.add(Box.createHorizontalGlue());
		container.add(Box.createRigidArea(new Dimension(5, 0)));
		container.setBorder(BorderFactory.createTitledBorder("Show"));

		add(container);
	}

	private void createCheckBoxItem(String name) {
		JCheckBox item = new JCheckBox(name);
		item.addItemListener(itemListener);
		item.setSelected(false);
		checkPanel.add(item);
	}

	private void createCurveEditeMode() {
		curveEditPanel = new JPanel();
		curveEditPanel
				.setLayout(new BoxLayout(curveEditPanel, BoxLayout.Y_AXIS));
		group = new ButtonGroup();

		currentPanel = curveEditPanel;

		createButton("Start New Curve", "Start a new curve.", null, false);
		createButton("Select All Curves", "Select all curves.", null, false);
		createButton("Deselect All Curves", "Deselect all curves.", null, false);
		createButton("Add Control Point",
				"Add a control point to all selected curves.", null, false);
		createButton("Move Curves", "Move all selected curves.", null,
				false);
		createButton("Connect Curves (No Extra Points)",
				"Connect all selected curves (no extra points).", null, false);
		createButton("Connect Curves (C0)",
				"Connect all selected curves (C0).", null, false);
		createButton("Delete Curves", "Delete all selected curves.", null,
				false);
		// curveEditPanel.setBorder(BorderFactory.createTitledBorder("Edit"));
		container.add(curveEditPanel);
	}

	private void createButton(String name, String tooltip, String icon,
			boolean selected) {
		JButton Button = new JButton(/* new ImageIcon(icon), */name);
		// toggleButton.setName(name);
		Button.addActionListener(listener);
		Button.setToolTipText(tooltip);
		Dimension d = new Dimension(TOGGLEBUTTONWIDTH, TOGGLEBUTTONHEIGHT);
		Button.setMaximumSize(d);
		Button.setMinimumSize(d);
		Button.setPreferredSize(d);
		group.add(Button);
		currentPanel.add(Button);
	}

	private void createPointEditMode() {
		pointEditPanel = new JPanel();
		pointEditPanel
				.setLayout(new BoxLayout(pointEditPanel, BoxLayout.Y_AXIS));
		group = new ButtonGroup();
		currentPanel = pointEditPanel;

		createButton("Deselect All Control Points",
				"Deselect all control points.", null, false);
		createButton("Move Control Points",
				"Move all selected control points.", null, false);
		createButton("Delete Control Points",
				"Delete all selected control poins.", null, false);
		// pointEditPanel.setBorder(BorderFactory.createTitledBorder("Edit"));
		container.add(pointEditPanel);
	}

	private void createEditPanel() {
		container = new JPanel();
		container.setLayout(new BoxLayout(container, BoxLayout.X_AXIS));
		container.add(Box.createHorizontalGlue());
		createCurveEditeMode();
		curveEditPanel.setVisible(false);
		createPointEditMode();
		pointEditPanel.setVisible(false);

		// pointEditPanel.setMaximumSize(d);
		Dimension d = new Dimension(TOGGLEBUTTONWIDTH + 15, 100);
		// container.setMaximumSize( d );
		// container.setMinimumSize( d );
		// container.setPreferredSize( d );
		container.add(Box.createHorizontalGlue());
		container.add(Box.createRigidArea(new Dimension(5, 0)));
		container.setBorder(BorderFactory.createTitledBorder("Edit"));

		add(container);
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
