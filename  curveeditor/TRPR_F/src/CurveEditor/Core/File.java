package CurveEditor.Core;

import java.util.Vector;

import CurveEditor.Curves.Curve;

//tekstueel opslaan: type | orde | controlepunt1 | controlepunt2 | ... | \n
public class File {

	private static String currentFilename;

	public static String getCurrentFilename(){
		return currentFilename;
	}	

	public static void setCurrentFilename(){
	}

	public File(){

	}

	public File(String filename, Vector<Curve> curves){

	}

	public void load(String filename, Vector<Curve> curves){

	}

	public void saveAs(String filename, Vector<Curve> curves){

	}

	public void save(Vector<Curve> curves){

	}
}
