import java.util.Vector;


public class MonitorPool {
	private Vector<Object> v;

	public Object addObject()
	{
		Object o;

		v.add(o = new Object());

		return 0;
	}

	public Object getObject(int i)
	{
		return v.elementAt(i);
	}
}
