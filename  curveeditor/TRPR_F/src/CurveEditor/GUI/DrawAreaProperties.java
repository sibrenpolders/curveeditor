package CurveEditor.GUI;

import java.awt.Color;

import CurveEditor.Exceptions.InvalidArgumentException;

public class DrawAreaProperties {
	public static final int SELECTED_LINE = 0;
	public static final int HOOVERED_LINE = 1;
	public static final int UNSELECTED_LINE = 2;
	public static final int TANGENS = 3;
	public static final int SELECTED_POINT = 4;
	public static final int UNSELECTED_POINT = 5;
	public static final int HOOVERED_POINT = 6;
	public static final int BACKGROUND = 7;
	public static final Integer THICKNESS_CHOICES[] = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
	public static final int LINE = 8;
	public static final int POINT = 9;
	
	private Color selectedLineColor;
	private Color hooveredLineColor;
	private Color unselectedLineColor;
	private Color tangensColor;
	private Color selectedPointColor;
	private Color unselectedPointColor;
	private Color hooveredPointColor;
	private Color backgroundColor;
	
	private Color selectedLineColorTmp;
	private Color hooveredLineColorTmp;
	private Color unselectedLineColorTmp;
	private Color tangensColorTmp;
	private Color selectedPointColorTmp;
	private Color unselectedPointColorTmp;
	private Color hooveredPointColorTmp;
	private Color backgroundColorTmp;
	
	private int lineThicknessTmp;
	private int lineThickness;
	
	private int pointThicknessTmp;
	private int pointThickness;
	
	public DrawAreaProperties( ) {
		selectedLineColor = Color.red;
		hooveredLineColor = Color.magenta;
		unselectedLineColor = Color.black;
		tangensColor = Color.blue;
		
		selectedPointColor = Color.green;
		hooveredPointColor = Color.yellow;
		unselectedPointColor = Color.black;
		
		backgroundColor = Color.white;
	
		lineThickness= 0;
		pointThickness= 2;
		
		// hulpvariabele initialiseren
		selectedLineColorTmp = selectedLineColor;
		hooveredLineColorTmp = hooveredLineColor;
		unselectedLineColorTmp = unselectedLineColor;
		tangensColorTmp = tangensColor;
		
		selectedPointColorTmp = selectedPointColor;
		hooveredPointColorTmp = hooveredPointColor;
		unselectedPointColorTmp = unselectedPointColor;
		
		backgroundColorTmp = backgroundColor;
		
		lineThicknessTmp = lineThickness;
		pointThicknessTmp = pointThickness;
	}
	
	public Color getColor( int getColorOf ) throws InvalidArgumentException {
		switch( getColorOf ) {
		case SELECTED_LINE:
			return selectedLineColor;
		case HOOVERED_LINE:
			return hooveredLineColor;
		case UNSELECTED_LINE:
			return unselectedLineColor;
		case TANGENS:
			return tangensColor;
		case SELECTED_POINT:
			return selectedPointColor;
		case UNSELECTED_POINT:
			return unselectedPointColor;
		case HOOVERED_POINT:
			return hooveredPointColor;
		case BACKGROUND:
			return backgroundColor;
		default:
			throw new InvalidArgumentException( "DrawAreaProperties: Color getColor( int ): No such  option: " + 
					getColorOf );
		}
	}

	/*
	 * Deze functie zal een nieuwe kleur zetten voor 1 van de onderdelen.
	 * De nieuwe kleur wordt tijdelijk opgeslagen in een hulpvariabele en zal pas definitief worden als
	 * de functie makeAdjustments( ) wordt aangeroepen, dit is voor het gebruiksgemak te verhogen.
	 * Als de gebruiker nu op cancel drukt dan zal cancelAdjustments( ) worden aangeroepen die alle waarde laat staan
	 * zoals ze zijn, en de tmpVariabele terug reset
	 */
	public void setColor( int setColorOf, Color newColor ) throws InvalidArgumentException {
		switch( setColorOf ) {
		case SELECTED_LINE:
			selectedLineColorTmp = newColor;
			break;
		case HOOVERED_LINE:
			hooveredLineColorTmp = newColor;
			break;
		case UNSELECTED_LINE:
			unselectedLineColorTmp = newColor;
			break;
		case TANGENS:
			tangensColorTmp = newColor;
			break;
		case SELECTED_POINT:
			selectedPointColorTmp = newColor;
			break;
		case UNSELECTED_POINT:
			unselectedPointColorTmp = newColor;
			break;
		case HOOVERED_POINT:
			hooveredPointColorTmp = newColor;
			break;
		case BACKGROUND:
			backgroundColorTmp = newColor;
			break;
		default:
			throw new InvalidArgumentException( "DrawAreaProperties: void setColor( int, Color ): No such  option: " + 
					setColorOf );
		}
	}
	
	/*
	 * Deze functie zal de veranderingen uitgevoerd door te gebruiker definitief maken
	 */
	public void makeAdjustments( ) {
		selectedLineColor = selectedLineColorTmp;
		hooveredLineColor= hooveredLineColorTmp;
		unselectedLineColor= unselectedLineColorTmp;
		tangensColor = tangensColorTmp;
		
		selectedPointColor= selectedPointColorTmp;
		hooveredPointColor= hooveredPointColorTmp;
		unselectedPointColor= unselectedPointColorTmp;
				
		backgroundColor= backgroundColorTmp;
		
		lineThickness= lineThicknessTmp;
		pointThickness= pointThicknessTmp;
	}
	
	/*
	 * Deze functie zal de veranderingen uitgevoerd door te gebruiker anuleren
	 */
	public void cancelAdjustments( ) {
		selectedLineColorTmp = selectedLineColor;
		hooveredLineColorTmp = hooveredLineColor;
		unselectedLineColorTmp = unselectedLineColor;
		tangensColorTmp = tangensColor;
		
		selectedPointColorTmp = selectedPointColor;
		hooveredPointColorTmp = hooveredPointColor;
		unselectedPointColorTmp = unselectedPointColor;
		
		backgroundColorTmp = backgroundColor;
		
		lineThicknessTmp = lineThickness;
		pointThicknessTmp = pointThickness;
	}
	
	public int getTickness( int thicknessOf ) {
		switch ( thicknessOf ) {
		case LINE:
			return lineThickness;
		case POINT:
			return pointThickness;
		default:
			return -1;
		}
	}
	
	public void setTickness( int t, int thicknessOf ) {
		switch ( thicknessOf ) {
		case LINE:
			lineThicknessTmp = t;
			System.out.println( "P:" + t );
			break;
		case POINT:
			pointThicknessTmp = t;
			break;		
		}		
	}
}
