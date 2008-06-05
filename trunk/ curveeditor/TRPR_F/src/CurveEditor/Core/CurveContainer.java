package CurveEditor.Core;

import java.util.Vector;
import CurveEditor.Curves.Curve;
import CurveEditor.Curves.Point;
import CurveEditor.Exceptions.*;

/*
 * Deze klasse bevat de voorstelling van de huidige curves. 
 * De output- en inputpunten van de verschillende curves worden hier bijgehouden.
 * Bij het herberekenen van de outputpunten, verplaatsen/toevoegen/verwijderen van
 * inputpunten moet deze container terug bijgewerkt worden om terug met de curve-
 * voorstelling overeen te komen, uiteraard.
 * Deze klasse wordt gebruikt wanneer men vlug wil nagaan óf en tót welke curve een
 * input- of outputpunt behoort (bij hoovering, selecteren e.d.). 
 */
public class CurveContainer {

	// Welk gebiedje moet afgezocht worden bij een gegeven pixel ?
	private static short SEARCH_RANGE = 3;

	// Houdt bij of en op welke curve een x,y-outputpunt ligt.
	private Curve[][] curves;
	// Houdt bij of en op welke curve een x,y-inputpunt ligt.
	private Curve[][] controlPoints;
	// De dimensies van de container.
	private int maxX, maxY;

	public final int maxX() {
		return maxX;
	}

	public final int maxY() {
		return maxY;
	}

	// Constructor die m.b.v. de meegegeven dimensies
	// een voorstelling van de curveverzameling aanmaakt.
	public CurveContainer(int Maxx, int Maxy) throws InvalidArgumentException {
		if (Maxx <= 0 || Maxy <= 0)
			throw new InvalidArgumentException(
					"CurveContainer.java - CurveContainer(int, int): Invalid Argument.");
		else {
			this.maxX = Maxx;
			this.maxY = Maxy;

			curves = new Curve[Maxx][Maxy];
			controlPoints = new Curve[Maxx][Maxy];

			for (int x = 0; x < Maxx; ++x)
				for (int y = 0; y < Maxy; ++y) {
					curves[x][y] = null;
					controlPoints[x][y] = null;
				}
		}
	}

	// De dimensies van de voorstelling veranderen en
	// de vorige gegevens naar de nieuwe voorstelling kopiëren.
	public final void resize(int MaxX, int MaxY)
			throws InvalidArgumentException {
		if (MaxX < 0 || MaxY < 0)
			throw new InvalidArgumentException(
					"CurveContainer.java - resize(int, int): Invalid Argument.");
		else {
			// de oude bijhouden
			Curve[][] tmp = curves;
			Curve[][] tmp2 = controlPoints;

			// nieuwe aanmaken
			curves = new Curve[MaxX][MaxY];
			controlPoints = new Curve[MaxX][MaxY];

			int minX = (this.maxX < MaxX) ? this.maxX : MaxX;
			int minY = (this.maxY < MaxY) ? this.maxY : MaxY;

			int x = 0, y = 0;

			// De beschikbare oude gegevens in de nieuwe voorstelling
			// kopiëren.
			for (; x < minX; ++x)
				for (y = 0; y < minY; ++y) {
					curves[x][y] = tmp[x][y];
					controlPoints[x][y] = tmp2[x][y];
				}
			// De rest met null-pointers opvullen.
			for (x = 0; x < MaxX; ++x)
				for (y = 0; y < MaxY; ++y) {
					if (y >= minY || x >= minX) {
						curves[x][y] = null;
						controlPoints[x][y] = null;
					}
				}

			this.maxX = MaxX;
			this.maxY = MaxY;
		}
	}

	// De huidige gegevens verwijderen.
	public final void clear() {
		curves = new Curve[maxX][maxY];
		controlPoints = new Curve[maxX][maxY];

		for (int x = 0; x < maxX; ++x)
			for (int y = 0; y < maxY; ++y) {
				curves[x][y] = null;
				controlPoints[x][y] = null;
			}
	}

	// Een curve aan de container toevoegen.
	public final void addCurve(Curve c) {
		if (c != null) {

			// Alle outputpunten toevoegen.
			for (int i = 0; i < c.getOutput().size(); ++i)
				// Checken of het punt wel binnen de dimensies valt.
				if (isValidPoint(c.getOutput().get(i)))
					curves[c.getOutput().get(i).X()][c.getOutput()
							.get(i).Y()] = c;

			addControlPoints(c); // input toevoegen
		}
	}

	// Een curve uit de container verwijderen.
	public void deleteCurve(Curve c) {
		if (c != null) {
			// Alle outputpunten verwijderen.
			for (int i = 0; i < c.getOutput().size(); ++i)
				// Checken of het punt wel binnen de dimensies valt.
				if (isValidPoint(c.getOutput().get(i)))
					curves[c.getOutput().get(i).X()][c.getOutput()
							.get(i).Y()] = null;

			deleteControlPoints(c); // input verwijderen
		}
	}

	// Checken of het gegeven punt wel binnen de dimensies van
	// de container valt.
	private final boolean isValidPoint(Point p) {
		return p != null && p.X() < maxX && p.Y() < maxY && p.Y() >= 0
				&& p.X() >= 0;
	}

	// Zoek a.h.v. een gegeven punt de bijhorende curve, gebruik
	// makende van het default zoekbereik.
	public final Curve searchCurve(Point p) {
		return searchCurve_(p, SEARCH_RANGE);
	}

	// Zoek a.h.v. een gegeven punt de bijhorende curve, gebruik
	// makende van het meegegeven zoekbereik.
	public final Curve searchCurve(Point p, short range) {
		return searchCurve_(p, range);
	}

	// Zoek a.h.v. een gegeven punt en het meegegeven zoekbereik de bijhorende
	// curve.
	private Curve searchCurve_(Point p, short range) {
		if (isValidPoint(p)) {

			// de min- en maxwaarden bepalen, die binnen de dimensies
			// van de container liggen
			int max_x = p.X() + range < maxX ? p.X() + range : maxX - 1;
			int min_x = p.X() - range >= 0 ? p.X() - range : 0;
			int max_y = p.Y() + range < maxY ? p.Y() + range : maxY - 1;
			int min_y = p.Y() - range >= 0 ? p.Y() - range : 0;

			for (int i = min_x; i <= max_x; ++i)
				for (int j = min_y; j <= max_y; ++j)
					if (curves[i][j] != null)
						return curves[i][j];
		}

		return null;
	}

	// De inputpunten van een meegegeven curve aan de container toevoegen.
	private void addControlPoints(Curve c) {
		if (c != null) {
			for (int i = 0; i < c.getNbInputPoints(); ++i) {
				if (isValidPoint(c.getInput().elementAt(i))) {
					controlPoints[c.getInput().elementAt(i).X()][c.getInput()
							.elementAt(i).Y()] = c;
				}

			}
		}
	}

	// De inputpunten van een meegegeven curve uit de container verwijderen.
	private void deleteControlPoints(Curve c) {
		for (int i = 0; i < c.getNbInputPoints(); ++i) {
			if (isValidPoint(c.getInput().elementAt(i)))
				controlPoints[c.getInput().elementAt(i).X()][c.getInput()
						.elementAt(i).Y()] = null;
		}
	}

	// Zoek a.h.v. een gegeven punt als inputpunt de bijhorende curve, gebruik
	// makende van het meegegeven zoekbereik.
	public Curve searchCurvesByControlPoint(Point p, short range) {
		return searchCurvesByControlPoint_(p, range);

	}

	// Zoek a.h.v. een gegeven punt als inputpunt de bijhorende curve, gebruik
	// makende van het default zoekbereik.
	public Curve searchCurvesByControlPoint(Point p) {
		return searchCurvesByControlPoint_(p, SEARCH_RANGE);
	}

	// Zoek a.h.v. een gegeven mogelijk inputpunt en het meegegeven zoekbereik
	// de bijhorende curve.
	private Curve searchCurvesByControlPoint_(Point p, short range) {
		if (isValidPoint(p)) {

			// de min- en maxwaarden bepalen, die binnen de dimensies
			// van de container liggen
			int max_x = p.X() + range < maxX ? p.X() + range : maxX - 1;
			int min_x = p.X() - range >= 0 ? p.X() - range : 0;
			int max_y = p.Y() + range < maxY ? p.Y() + range : maxY - 1;
			int min_y = p.Y() - range >= 0 ? p.Y() - range : 0;

			for (int i = min_x; i <= max_x; ++i)
				for (int j = min_y; j <= max_y; ++j)
					if (controlPoints[i][j] != null)
						return controlPoints[i][j];
		}

		return null;
	}

	// Zoek a.h.v. een gegeven punt alle beschikbare inputpunten in het
	// meegegeven zoekbereik.
	public final Vector<Point> searchControlPoint(Point p, short range) {
		return searchControlPoint_(p, range);

	}

	// Zoek a.h.v. een gegeven punt alle beschikbare inputpunten in het
	// default zoekbereik.
	public final Vector<Point> searchControlPoint(Point p) {
		return searchControlPoint_(p, SEARCH_RANGE);
	}

	// Zoek a.h.v. het gegeven punt en het meegegeven zoekbereik
	// alle beschikbare inputpunten in dát zoekbereik.
	private final Vector<Point> searchControlPoint_(Point p, short range) {
		if (isValidPoint(p)) {
			Vector<Point> result = new Vector<Point>();

			// de min- en maxwaarden bepalen, die binnen de dimensies
			// van de container liggen
			int max_x = p.X() + range < maxX ? p.X() + range : maxX - 1;
			int min_x = p.X() - range >= 0 ? p.X() - range : 0;
			int max_y = p.Y() + range < maxY ? p.Y() + range : maxY - 1;
			int min_y = p.Y() - range >= 0 ? p.Y() - range : 0;

			for (int i = min_x; i <= max_x; ++i)
				for (int j = min_y; j <= max_y; ++j)
					if (controlPoints[i][j] != null) {
						int k;
						if ((k = controlPoints[i][j]
								.containsInputPointi(new Point(i, j))) != -1)
							result.add(controlPoints[i][j].getInput().get(k));
					}
			if (result.size() > 0)
				return result;
			else
				return null;
		} else
			return null;
	}

	// Controleren of een gegeven punt een inputpunt is van een curve.
	// null wordt teruggegeven indien dit niet het geval is, anders
	// het inputpunt zelf.
	public final Point isControlPoint(Point p) {
		if (isValidPoint(p)) {
			if (controlPoints[p.X()][p.Y()] != null) {
				Vector<Point> input = controlPoints[p.X()][p.Y()].getInput();
				for (int i = 0; i < input.size(); ++i)
					if (p.equals(input.get(i)))
						return input.get(i);
			}
		}
		return null;
	}

	// Het inputpoint op de meegegeven locatie uit de container verwijderen.
	public final void deleteControlPoint(int x, int y) {
		if (isValidPoint(new Point(x, y)))
			controlPoints[x][y] = null;
	}

}
