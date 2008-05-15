package CurveEditor.Core;

import CurveEditor.Curves.Point;
import CurveEditor.Curves.Curve;
import CurveEditor.Exceptions.InvalidArgumentException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
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

//tekstueel opslaan: type | orde | controlepunt1 | controlepunt2 | ... | \n
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
		// parseDocument(curves);
	}

	public void save(String filename, Vector<Curve> curves) {
		try {
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

	/*
	 * zal de xml file omzetten naar zijn DOM representatie ( zodat deze later
	 * kan gemanipuleerd worden
	 * 
	 * private void parseXmlFile( String filename) { DocumentBuilderFactory dbf =
	 * DocumentBuilderFactory.newInstance();
	 * 
	 * try { DocumentBuilder db = dbf.newDocumentBuilder(); // parse m.b.v de
	 * documentbuilder om de DOM representatie van de xml te verkrijgen dom =
	 * db.parse(filename); }catch(ParserConfigurationException pce) {
	 * pce.printStackTrace(); }catch(SAXException se) { se.printStackTrace();
	 * }catch(IOException ioe) { ioe.printStackTrace(); } }
	 * 
	 * private void parseDocument( Vector<Curve> curves ) { // Het root element
	 * van de xml file eruitnemen Element docEle = dom.getDocumentElement(); //
	 * een 'NodeList' van curve opvragen // de tag curve bevat de informatie die
	 * we willen inladen. NodeList nl = docEle.getElementsByTagName("curve"); //
	 * de nodeList afgaan en voor elke tag curve een Object Curve aanmaken if(nl !=
	 * null && nl.getLength() > 0) { for(int i = 0 ; i < nl.getLength();i++) { //
	 * neem de i-de curve uit de dom representatie Element el =
	 * (Element)nl.item(i); // maak een Curve object aan Curve c = getCurve(el); //
	 * toevoegen aan de lijst curves.add(c); } } } // zal een curve creeren
	 * m.b.v. het opgegeven DOM element private Curve getCurve( Element e ) {
	 * String type = getTextValue(e,"type"); char typeC; if ( type != null &&
	 * type.equals("bezier")) typeC = 'b'; else if ( type != null &&
	 * type.equals("hermites")) typeC = 'h'; else // error TODO Exception
	 * handle? return null;
	 * 
	 * short deg = (short)getIntValue(e, "degree");
	 * 
	 * Curve c = new Curve(typeC, deg); // inlezen zolang er een nieuwe tag
	 * point is. // er kunnen meerdere point tags zijn per curve NodeList nl =
	 * e.getElementsByTagName("point"); // de nodeList afgaan en voor elke tag
	 * point een Object Point aanmaken if(nl != null && nl.getLength() > 0) {
	 * for(int i = 0 ; i < nl.getLength();i++) { // neem de i-de point uit de
	 * dom representatie Element el = (Element)nl.item(i); // maak een Point
	 * object aan Point p = getPoint(el); // toevoegen aan de lijst
	 * c.addInput(p); } }
	 * 
	 * return c; }
	 * 
	 * private Point getPoint( Element e ) { int dim =
	 * Integer.parseInt(e.getAttribute("dim")); int x = getIntValue(e, "x"); int
	 * y = getIntValue(e, "y"); int z;
	 * 
	 * if (dim == 3) { z = getIntValue(e, "z"); return null; // return
	 * c.addInput(new Point3D( x, y, z)); } else return new Point(x, y); }
	 * 
	 * private String getTextValue( Element e, String tagName ) { String textVal =
	 * null;
	 * 
	 * NodeList nl = e.getElementsByTagName(tagName);
	 * 
	 * if(nl != null && nl.getLength() > 0) { // Neem het eerstvolgende element
	 * met tagName als waarde Element el = (Element)nl.item(0); textVal =
	 * el.getFirstChild().getNodeValue(); }
	 * 
	 * return textVal; }
	 * 
	 * private int getIntValue( Element e, String atr ) { return
	 * Integer.parseInt(getTextValue(e,atr)); }
	 * 
	 * private void createDOMTree(List<Curve> curves) { // root element
	 * aanmaken <curveEditor> Element rootEle =
	 * dom.createElement("curveEditor"); dom.appendChild(rootEle);
	 * 
	 * System.out.println(curves.size()); for ( int i = 0; i < curves.size();
	 * ++i) { // voor elke curve een <curve> aanmaken Element e =
	 * createCurveElement(curves.get(i)); rootEle.appendChild(e); } }
	 * 
	 * private Element createCurveElement(Curve c) { // curve element aanmaken
	 * Element curveEle = dom.createElement("curve"); // type element aanmaken
	 * Element typeEle = dom.createElement("type"); Text typeText =
	 * dom.createTextNode(c.getTypeAsString()); typeEle.appendChild(typeText);
	 * curveEle.appendChild(typeEle); // degree element aanmaken Element
	 * degreeEle = dom.createElement("Degree"); Text degreeText =
	 * dom.createTextNode("" + c.getDegree());
	 * degreeEle.appendChild(degreeText); curveEle.appendChild(degreeEle); //
	 * point element aanmaken Vector<Point> vp = c.getInput(); for ( int i = 0;
	 * i < vp.size(); ++i) { System.out.println(i); Element pointEle =
	 * createPointElement(vp.get(i)); curveEle.appendChild(pointEle); }
	 * 
	 * return curveEle; }
	 * 
	 * private Element createPointElement( Point vp ) { // Point aanmaken
	 * Element pointEle = dom.createElement("point");
	 * pointEle.setAttribute("dim", "2"); // x aanmaken Element xEle =
	 * dom.createElement("x"); Text xText = dom.createTextNode("" + vp.X());
	 * xEle.appendChild(xText); pointEle.appendChild(xEle); // x aanmaken
	 * Element yEle = dom.createElement("y"); Text yText = dom.createTextNode("" +
	 * vp.Y()); xEle.appendChild(yText); pointEle.appendChild(yEle);
	 * 
	 * return pointEle; }
	 * 
	 * private void printToFile(String filename) { try { System.out.println(
	 * filename); TransformerFactory tFactory =
	 * TransformerFactory.newInstance(); Transformer transformer =
	 * tFactory.newTransformer();
	 * 
	 * DOMSource source = new DOMSource(dom); StreamResult result = new
	 * StreamResult(new File(filename + ".xml")); transformer.transform(source,
	 * result); } catch (TransformerConfigurationException e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); } catch
	 * (TransformerException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } }
	 */
}
