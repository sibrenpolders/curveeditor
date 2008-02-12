package CurveEditor;

import java.util.Vector;

public abstract class Tool
{

	protected String type;

	public String getType()
	{
		return type;
	}

	public void setType(String type ){
		this.type = type;
	}


	public void run(java.util.Vector algorithms, java.util.Vector curves){

	}
}
