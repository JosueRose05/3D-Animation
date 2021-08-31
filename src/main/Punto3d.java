package main;

public class Punto3d {

	public double x,y,z;
	
	public Punto3d(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public double[] getXY() {
		double XY[] = {x, y};
		return XY;
	}
	
}

