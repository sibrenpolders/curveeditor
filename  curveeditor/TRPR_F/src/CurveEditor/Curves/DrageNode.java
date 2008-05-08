package CurveEditor.Curves;

public class DrageNode {
	private Point p;
	private DrageNode next;
	
	public DrageNode( ) {
		next = null;
	}
	
	public DrageNode( Point p ) {
		this.p = p;
	}
	
	DrageNode next(){
		return next;
	}
	
	void setNext( DrageNode next ) {
		this.next = next;
	}
	
	void setNext( Point p ) {
		next = new DrageNode( p );
	}
	
	void increase( int x, int y ) {
		p.increaseX( x );
		p.increaseY( y );
	}
}
