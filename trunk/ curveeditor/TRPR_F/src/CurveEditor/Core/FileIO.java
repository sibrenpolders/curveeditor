package CurveEditor.Core;

import CurveEditor.Curves.Point;
import CurveEditor.Curves.Curve;
import CurveEditor.Exceptions.InvalidArgumentException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.EmptyStackException;
import java.util.Stack;
import java.util.Vector;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class FileIO extends DefaultHandler {

	private String currentFilename;
	private String tempVal;
	private Vector<Curve> curves;
	private Curve curve;
	private Point point;
	private int indentLevel = 0;
	private PrintWriter pw;
	private TransformerHandler hd;
	private int lineNumber = 0;

	public String getCurrentFilename() {
		return currentFilename;
	}

	public static void setCurrentFilename() {
	}

	public FileIO() {

	}

	public FileIO(String filename, Vector<Curve> curves) {
	}

	public void load(String filename, Vector<Curve> curves) {
		this.curves = curves;
		parseXmlFile(filename);
	}

	public void save(String filename, Vector<Curve> curves) {
		try {
			if (filename.endsWith(".xml") || filename.endsWith(".XML"))
				pw = new PrintWriter(new File(filename));
			else if (filename.indexOf(".") != -1) {
				pw = new PrintWriter(new File(filename.substring(0, filename
						.indexOf("."))
						+ ".xml"));
			} else
				pw = new PrintWriter(new File(filename + ".xml"));

			StreamResult streamResult = new StreamResult(pw);
			SAXTransformerFactory tf = (SAXTransformerFactory) TransformerFactory
					.newInstance();
			hd = tf.newTransformerHandler();
			Transformer serializer = hd.getTransformer();
			serializer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");//
			// TODO DTD online zetten of relatief pad zoeken
			// serializer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM,"");
			serializer.setOutputProperty(OutputKeys.METHOD, "xml");
			serializer.setOutputProperty(OutputKeys.INDENT, "yes");
			hd.setResult(streamResult);
			hd.startDocument();
			// hd.processingInstruction("xml-stylesheet","type=\"text/xsl\"
			// href=\"mystyle.xsl\"");
			hd.startElement("", "", "curveEditor", null);
			String curTitle;

			++indentLevel;
			for (int i = 0; i < curves.size(); ++i) {
				hd.startElement("", "", "curve", null);
				hd.startElement("", "", "type", null);
				curTitle = curves.get(i).getTypeAsString();
				hd.characters(curTitle.toCharArray(), 0, curTitle.length());
				hd.endElement("", "", "type");
				Vector<Point> vp = curves.get(i).getInput();
				// AttributesImpl atts = new AttributesImpl();
				for (int j = 0; j < vp.size(); ++j) {
					hd.startElement("", "", "point", null);
					writeCo("" + vp.get(j).X(), "x");
					writeCo("" + vp.get(j).Y(), "y");
					// writeCo( "" + vp.get(j).Z(), "x" );
					hd.endElement("", "", "point");
				}
				hd.endElement("", "", "curve");
			}
			hd.endElement("", "", "curveEditor");
			hd.endDocument();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
	}

	private void writeCo(String title, String cor) throws SAXException {
		hd.startElement("", "", cor, null);
		hd.characters(title.toCharArray(), 0, title.length());
		hd.endElement("", "", cor);
	}

	private void parseXmlFile(String filename) {
		SAXParserFactory spf = SAXParserFactory.newInstance();
		try {

			SAXParser sp = spf.newSAXParser();

			// parse het document en zet dit object als event listener
			sp.parse(new InputSource(filename), this);
		} catch (SAXException se) {
			se.printStackTrace();
		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (IOException ie) {
			ie.printStackTrace();
		} catch (Exception e) {
			System.out.println(e);
		}

	}

	// Event Handlers
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		// reset
		tempVal = "";
		if (qName.equalsIgnoreCase("Curve"))
			// create a new instance of employee
			curve = new Curve();
		else if (qName.equalsIgnoreCase("Point")) {
			point = new Point();
		}

		++lineNumber; // volgende regel
	}

	public void characters(char[] ch, int start, int length)
			throws SAXException {
		tempVal = new String(ch, start, length);
	}

	public void endElement(String uri, String localName, String qName)
			throws SAXException, NumberFormatException,
			IndexOutOfBoundsException {

		if (qName.equalsIgnoreCase("Curve"))
			// nieuwe curve aan de vector toevoegen
			curves.add(curve);

		else if (qName.equalsIgnoreCase("Type")) {
			char c = 0;
			c = tempVal.charAt(0);
			curve.setType(c);
		}

		// else if (qName.equalsIgnoreCase("Degree"))
		// curve.setDegree(Short.parseShort(tempVal));

		else if (qName.equalsIgnoreCase("Point"))
			try {
				curve.addInput(point);
			} catch (InvalidArgumentException e) {
				e.printStackTrace();
			}
		else if (qName.equalsIgnoreCase("X"))
			point.setX(Integer.parseInt(tempVal));

		else if (qName.equalsIgnoreCase("Y"))
			point.setY(Integer.parseInt(tempVal));

		// else if (qName.equalsIgnoreCase("Z"))
		// point.setZ(Integer.parseInt(tempVal));

		++lineNumber; // volgende regel
	}

	private Stack<byte[]> stack = new Stack<byte[]>();
	private Stack<byte[]> stackRedo = new Stack<byte[]>();

	public void push(Vector<Curve> curves, Vector<Curve> selectedCurves)
			throws InvalidArgumentException {
		if (curves == null || selectedCurves == null)
			throw new InvalidArgumentException(
					"FileIO.java - push(Vector, Vector): Invalid Argument.");
		pushCurve(curves, stack);
		pushCurve(selectedCurves, stack);
		stackRedo.clear();
	}

	public void pushNew(Vector<Curve> curves, Vector<Curve> selectedCurves)
			throws InvalidArgumentException {
		if (curves == null || selectedCurves == null)
			throw new InvalidArgumentException(
					"FileIO.java - push(Vector, Vector): Invalid Argument.");
		pushCurve(curves, stackRedo);
		pushCurve(selectedCurves, stackRedo);
	}

	public void undo(Vector<Curve> curves, Vector<Curve> selectedCurves)
			throws EmptyStackException, InvalidArgumentException {
		if (stack.size() > 1) {
			byte[] selCur = stack.pop();
			byte[] cur = stack.pop();
			stack.push(cur);
			stack.push(selCur);

			stackRedo.push(cur);
			stackRedo.push(selCur);
		}

		pop(curves, selectedCurves, stack);
	}

	public void redo(Vector<Curve> curves, Vector<Curve> selectedCurves)
			throws EmptyStackException, InvalidArgumentException {
		if (stackRedo.size() > 1) {
			byte[] selCur = stackRedo.pop();
			byte[] cur = stackRedo.pop();
			stackRedo.push(cur);
			stackRedo.push(selCur);

			stack.push(cur);
			stack.push(selCur);
		}
		pop(curves, selectedCurves, stackRedo);
	}

	private void pop(Vector<Curve> curves, Vector<Curve> selectedCurves,
			Stack<byte[]> stack1) throws InvalidArgumentException,
			EmptyStackException {
		if (curves == null || selectedCurves == null)
			throw new InvalidArgumentException(
					"FileIO.java - pop(Vector, Vector): Invalid Argument.");
		if (stack1.size() <= 1)
			throw new EmptyStackException();

		popCurve(selectedCurves, stack1);
		popCurve(curves, stack1);
	}

	private void pushCurve(Vector<Curve> v, Stack<byte[]> stack1) {
		try {
			ByteArrayOutputStream temp;

			pw = new PrintWriter(temp = new ByteArrayOutputStream());
			StreamResult streamResult = new StreamResult(pw);
			SAXTransformerFactory tf = (SAXTransformerFactory) TransformerFactory
					.newInstance();
			hd = tf.newTransformerHandler();
			Transformer serializer = hd.getTransformer();
			serializer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			serializer.setOutputProperty(OutputKeys.METHOD, "xml");
			serializer.setOutputProperty(OutputKeys.INDENT, "yes");
			hd.setResult(streamResult);
			hd.startDocument();
			hd.startElement("", "", "curveEditor", null);
			String curTitle;

			++indentLevel;
			for (int i = 0; i < v.size(); ++i) {
				hd.startElement("", "", "curve", null);
				hd.startElement("", "", "type", null);
				curTitle = v.get(i).getTypeAsString();
				hd.characters(curTitle.toCharArray(), 0, curTitle.length());
				hd.endElement("", "", "type");
				Vector<Point> vp = v.get(i).getInput();
				for (int j = 0; j < vp.size(); ++j) {
					hd.startElement("", "", "point", null);
					writeCo("" + vp.get(j).X(), "x");
					writeCo("" + vp.get(j).Y(), "y");
					hd.endElement("", "", "point");
				}
				hd.endElement("", "", "curve");
			}
			hd.endElement("", "", "curveEditor");
			hd.endDocument();

			stack1.push(temp.toByteArray());
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
	}

	private void popCurve(Vector<Curve> v, Stack<byte[]> stack1) {
		Vector<Curve> prev = curves;
		this.curves = v;
		SAXParserFactory spf = SAXParserFactory.newInstance();
		try {
			v.clear();
			SAXParser sp = spf.newSAXParser();
			sp.parse(new ByteArrayInputStream(stack1.pop()), this);
			curves = prev;
		} catch (SAXException se) {
			se.printStackTrace();
		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (IOException ie) {
			ie.printStackTrace();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
