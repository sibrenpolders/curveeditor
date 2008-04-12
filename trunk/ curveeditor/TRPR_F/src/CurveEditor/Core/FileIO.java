package CurveEditor.Core;

import CurveEditor.Curves.Point;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.SAXException;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

import CurveEditor.Curves.Curve;

//tekstueel opslaan: type | orde | controlepunt1 | controlepunt2 | ... | \n
public class FileIO {

	private String currentFilename;
	private org.w3c.dom.Document dom;
	
	public String getCurrentFilename(){
		return currentFilename;
	}	

	public static void setCurrentFilename(){
	}

	public FileIO(){

	}

	public FileIO(String filename, Vector<Curve> curves){
	}

	public void load(String filename, Vector<Curve> curves){
		parseXmlFile(filename);
		parseDocument(curves);
        
	}
	
	/*
	 * zal de xml file omzetten naar zijn DOM representatie ( zodat deze later kan gemanipuleerd worden
	 */
	
	private void parseXmlFile( String filename) {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try {
                DocumentBuilder db = dbf.newDocumentBuilder();

                // parse m.b.v de documentbuilder om de DOM representatie van de xml te verkrijgen
                dom = db.parse(filename);
        }catch(ParserConfigurationException pce) {
                pce.printStackTrace();
        }catch(SAXException se) {
                se.printStackTrace();
        }catch(IOException ioe) {
                ioe.printStackTrace();
        }
	}
	
	private void parseDocument( Vector<Curve> curves ) {
		// Het root element van de xml file eruitnemen
		Element docEle = dom.getDocumentElement();
		
		// een 'NodeList' van curve opvragen
		// de tag curve bevat de informatie die we willen inladen.		
		NodeList nl = docEle.getElementsByTagName("curve");
		
		// de nodeList afgaan en voor elke tag curve een Object Curve aanmaken
		if(nl != null && nl.getLength() > 0) {
			for(int i = 0 ; i < nl.getLength();i++) {
				// neem de i-de curve uit de dom representatie
				Element el = (Element)nl.item(i);
				
				// maak een Curve object aan
				Curve c = getCurve(el);
				
				// toevoegen aan de lijst
				curves.add(c);
			}
		}
	}

	// zal een curve creeren m.b.v. het opgegeven DOM element
	private Curve getCurve( Element e ) {
		String type = getTextValue(e,"type");
		char typeC;
		if ( type != null && type.equals("bezier"))
			typeC = 'b';
		else if ( type != null && type.equals("hermites"))
			typeC = 'h';
		else // error TODO Exception handle?
			return null;
		
		short deg = (short)getIntValue(e, "degree");
		
		Curve c = new Curve(typeC, deg);
				
		// inlezen zolang er een nieuwe tag point is.
		// er kunnen meerdere point tags zijn per curve
		NodeList nl = e.getElementsByTagName("point");
		
		// de nodeList afgaan en voor elke tag point een Object Point aanmaken
		if(nl != null && nl.getLength() > 0) {
			for(int i = 0 ; i < nl.getLength();i++) {
				// neem de i-de point uit de dom representatie
				Element el = (Element)nl.item(i);
				
				// maak een Point object aan
				Point p = getPoint(el);
				
				// toevoegen aan de lijst
				c.addInput(p);
			}
		}
		
		return c;
	}
	private Point getPoint( Element e ) {
		int dim = Integer.parseInt(e.getAttribute("dim"));
		int x = getIntValue(e, "x");
		int y = getIntValue(e, "y");
		int z;
		
		if (dim == 3) {
			z = getIntValue(e, "z");
			return null;
			// return c.addInput(new Point3D( x, y, z));
		}
		else
			return new Point(x, y);
			
	}
	
	private String getTextValue( Element e, String tagName ) {
		String textVal = null;
		
		NodeList nl = e.getElementsByTagName(tagName);
		
		if(nl != null && nl.getLength() > 0) {
			// Neem het eerstvolgende element met tagName als waarde
			Element el = (Element)nl.item(0);
			textVal = el.getFirstChild().getNodeValue();
		}

		return textVal;
	}
	
	private int getIntValue( Element e, String atr ) {
		return Integer.parseInt(getTextValue(e,atr));
	}
	
	public void save(String filename, List<Curve> curves){
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
		DocumentBuilder db = dbf.newDocumentBuilder();

		// maak een nieuwe DOM instantie aan
		dom = db.newDocument();

		createDOMTree(curves);
		printToFile(filename);
		
		}catch(ParserConfigurationException pce) {
			System.out.println("Error while trying to instantiate DocumentBuilder " + pce);
			System.exit(1);
		}		
	}
	
	private void createDOMTree(List<Curve> curves) {
		// root element aanmaken <curveEditor>
		Element rootEle = dom.createElement("curveEditor");
		dom.appendChild(rootEle);

		System.out.println(curves.size());
		for ( int i = 0; i < curves.size(); ++i) {
			// voor elke curve een <curve> aanmaken
			Element e = createCurveElement(curves.get(i));
			rootEle.appendChild(e);
		}
	}
	
	private Element createCurveElement(Curve c) {
		// curve element aanmaken
		Element curveEle = dom.createElement("curve");

		// type element aanmaken
		Element typeEle = dom.createElement("type");
		Text typeText = dom.createTextNode(c.getTypeAsString());
		typeEle.appendChild(typeText);
		curveEle.appendChild(typeEle);

		// degree element aanmaken
		Element degreeEle = dom.createElement("Degree");
		Text degreeText = dom.createTextNode("" + c.getDegree());
		degreeEle.appendChild(degreeText);
		curveEle.appendChild(degreeEle);

		// point element aanmaken
		Vector<Point> vp = c.getInput();
		for ( int i = 0; i < vp.size(); ++i) {
			System.out.println(i);
			Element pointEle = createPointElement(vp.get(i));
			curveEle.appendChild(pointEle);
		}
		
		return curveEle;
	}
	
	private Element createPointElement( Point vp ) {
		// Point aanmaken
		Element pointEle = dom.createElement("point");
		pointEle.setAttribute("dim", "2");
		
		// x aanmaken
		Element xEle = dom.createElement("x");
		Text xText = dom.createTextNode("" + vp.X());
		xEle.appendChild(xText);
		pointEle.appendChild(xEle);
		
		// x aanmaken
		Element yEle = dom.createElement("y");
		Text yText = dom.createTextNode("" + vp.Y());
		xEle.appendChild(yText);
		pointEle.appendChild(yEle);
		
		return pointEle;
	}
	
	private void printToFile(String filename) {
		try
		{
			System.out.println( filename);
			TransformerFactory tFactory =
                TransformerFactory.newInstance();
            Transformer transformer = tFactory.newTransformer();
 
            DOMSource source = new DOMSource(dom);
            StreamResult result = new StreamResult(new File(filename + ".xml"));
            transformer.transform(source, result);            

		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
