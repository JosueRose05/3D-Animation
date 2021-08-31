package main;

public class Transformaciones3d {

	public Punto3d traslacion(Punto3d punto, double tx, double ty, double tz) {
		double matriz[][] = {
				{1,0,0,tx},
				{0,1,0,ty},
				{0,0,1,tz},
				{0, 0, 0, 1}
		};
		double matriz2[] = {punto.x, punto.y, punto.z, 1.0};
		double matrizR[] = {0,0,0,0};
		for(int i=0;i<4;i++) {
			for(int j=0;j<4;j++) {
				matrizR[i] += matriz[i][j] * matriz2[j];
			}			
		}
		punto.x = matrizR[0];
		punto.y = matrizR[1];
		punto.z = matrizR[2];
		return punto;
	}
	
	public Punto3d escalacion(Punto3d punto, double tx, double ty, double tz) {
		double matriz[][] = {
				{tx,0,0,0},
				{0,ty,0,0},
				{0,0,tz,0},
				{0, 0, 0, 1}
		};
		
		double matriz2[] = {punto.x, punto.y, punto.z, 1.0};
		double matrizR[] = {0,0,0,0};
		for(int i=0;i<4;i++) {
			for(int j=0;j<4;j++) {
				matrizR[i] += matriz[i][j] * matriz2[j];
			}			
		}
		punto.x = matrizR[0];
		punto.y = matrizR[1];
		punto.z = matrizR[2];
		return punto;
	}
	
	public Punto3d rotacionX(Punto3d punto, double a) {
		a = Math.toRadians(a);
		double matriz[][] = {
				{1,0,0,0},
				{0,Math.cos(a),Math.sin(a),0},
				{0,-Math.sin(a),Math.cos(a),0},
				{0, 0, 0, 1}
		};
		
		double matriz2[] = {punto.x, punto.y, punto.z, 1};
		double matrizR[] = {0,0,0,0};
		
		for(int i=0; i<4; i++) {
			for(int j=0; j<4; j++) {
				matrizR[i] += matriz[i][j] * matriz2[j];
			}
		}
		
		punto.x = matrizR[0];
		punto.y = matrizR[1];
		punto.z = matrizR[2];
		return punto;
	}
	
	
	public Punto3d rotacionY(Punto3d punto, double a) {
		a = Math.toRadians(a);
		double matriz[][] = {
				{Math.cos(a),0,-Math.sin(a),0},
				{0,1,0,0},
				{Math.sin(a),0,Math.cos(a),0},
				{0, 0, 0, 1}
		};
		
		double matriz2[] = {punto.x, punto.y, punto.z, 1};
		double matrizR[] = {0,0,0,0};
		
		for(int i=0; i<4; i++) {
			for(int j=0; j<4; j++) {
				matrizR[i] += matriz[i][j] * matriz2[j];
			}
		}
		
		punto.x = matrizR[0];
		punto.y = matrizR[1];
		punto.z = matrizR[2];
		return punto;
	}
	
	public Punto3d rotacionZ(Punto3d punto, double a) {
		a = Math.toRadians(a);
		double matriz[][] = {
				{Math.cos(a),Math.sin(a),0,0},
				{-Math.sin(a),Math.cos(a),0,0},
				{0,0,1,0},
				{0, 0, 0, 1}
		};
		
		double matriz2[] = {punto.x, punto.y, punto.z, 1};
		double matrizR[] = {0,0,0,0};
		
		for(int i=0; i<4; i++) {
			for(int j=0; j<4; j++) {
				matrizR[i] += matriz[i][j] * matriz2[j];
			}
		}
		
		punto.x = matrizR[0];
		punto.y = matrizR[1];
		punto.z = matrizR[2];
		return punto;
	}
}
