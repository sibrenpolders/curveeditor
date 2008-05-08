package CurveEditor.Curves;

public class Drag {
	private DrageNode head;
	private DrageNode tail;
	
	public Drag( DrageNode p ) {
		head = p;
		tail = head;
	}
	
	public void add( DrageNode p ) {
		tail.setNext( p );
		tail = tail.next();
	}
	
	public void increase( int x, int y ) {
		DrageNode tmp = head;
		
		while( tmp.next() != null ) {
			tmp.increase( x, y );
		}
	}
}
