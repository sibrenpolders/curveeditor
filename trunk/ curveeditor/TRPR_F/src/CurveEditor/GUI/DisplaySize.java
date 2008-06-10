// auteur Sibren Polders
package CurveEditor.GUI;

import java.awt.Dimension;

/*
 * Deze klasse omvat als het ware een dynamische display, specifiek voor onze GUI.
 * A.h.v. de totale canvasdimensies worden de dimensies van de verschillende 
 * GUI-elementen berekend. Deze kunnen dan bij een resize herberekend worden en 
 * geset worden als de dimensies van die GUI-elementen.
 */
public final class DisplaySize {
	// De standaard breedtes en hoogtes van de GUI-elementen.
	public static int DRAWWIDTH = 600; // standaard breedte voor DrawArea
	public static int DRAWHEIGHT = 600; // standaard hoogte voor DrawArea
	// standaard breedte voor ChoiceArea
	public static final int CHOICEWIDTH = 275;
	public static int CHOICEHEIGHT = DRAWHEIGHT; // hoogte voor ChoiceArea
	// breedte voor de totale GUI
	public static int SCREENWIDTH = DRAWWIDTH + CHOICEWIDTH;
	public static final int MENUHEIGHT = 20; // standaard hoogte voor Menu
	// hoogte voor Toolbar
	public static final int TOOLBARHEIGHT = MENUHEIGHT + 5;

	public static int SCREENHEIGHT = CHOICEHEIGHT + MENUHEIGHT + TOOLBARHEIGHT;

	// Huidige schermgrootte van de applicatie.
	private Dimension frameSize;

	public Dimension frameSize() {
		return frameSize;
	}

	public void setCurrentSize(Dimension d) {
		frameSize = d;
	}

	public DisplaySize() {
	}

	// Gegeven een nieuwe schermgrootte van de applicatie,
	// herbereken de dimensies van de GUI-elementen.
	public void recalculate(Dimension d) {
		// De hoogte van zowel Menu als Toolbar zijn vast. Een verandering van
		// de hoogte van het canvas heeft dus geen invloed op deze 2 elementen.
		// Bij ChoiceArea is de breedte vast.
		SCREENWIDTH = (int) d.getWidth();
		SCREENHEIGHT = (int) d.getHeight();
		DRAWWIDTH = SCREENWIDTH - CHOICEWIDTH;
		DRAWHEIGHT = SCREENHEIGHT - MENUHEIGHT - TOOLBARHEIGHT;
		CHOICEHEIGHT = SCREENHEIGHT - MENUHEIGHT - TOOLBARHEIGHT;
	}

	// Verander de schermgrootte en herbereken de dimensies
	// van alle GUI-elementen.
	public boolean changeFrameSize(Dimension d) {
		if (d != null && (frameSize == null || !frameSize.equals(d))) {
			setCurrentSize(d);
			recalculate(frameSize);
			return true;
		}

		return false;
	}
}
