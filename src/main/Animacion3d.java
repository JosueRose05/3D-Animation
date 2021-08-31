package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JFrame;

public class Animacion3d extends JFrame implements Runnable{
	
	Image fondo, imgb, mario, bowser, fondoIntro, fin;
	private Thread hilo;
	BufferedImage bufferImg, bufferImg2;
	Graphics gbuffer;
	Color cafe = new Color(131, 51, 32);
	Color verde = new Color(75, 138, 33);
	Color beige = new Color(238, 238, 169);
	Color negro = new Color(94, 106, 117);
	int movY = 125;
	
	int centroX = 400, centroY = 400, colorFondo, contEscena = 0;
	Transformaciones3d T = new Transformaciones3d();//Con el objeto T vamos a hacer todas las trasformaciones
	Punto3d cubo[] = new Punto3d[8];
	Punto3d reyBlanco[] = new Punto3d[21];
	Punto3d reyNegro[] = new Punto3d[21];
	Punto3d torreN1[] = new Punto3d[36];
	Punto3d torreN2[] = new Punto3d[36];
	Punto3d alfil[] = new Punto3d[5];

	public Animacion3d() {
		setTitle("Animacion3D by Josue");
        setSize(800, 800);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        setVisible(true);
        
        this.bufferImg = new BufferedImage(1,1,BufferedImage.TYPE_INT_RGB);
		this.bufferImg2 = new BufferedImage(getWidth(),getHeight(),BufferedImage.TYPE_INT_ARGB);
		colorFondo = bufferImg2.getRGB(1, 1);
		
		iniciarFiguras();
        
        hilo = new Thread(this);
		hilo.start();
	}
	
	public void paint(Graphics g) {
		if(fondo==null) {
			fondo = createImage(getWidth(), getHeight()); //Fondo es una imagen
			gbuffer = fondo.getGraphics(); //lo que esta en fondo lo hacemos un graphics
			gbuffer.setClip(0,0, getWidth(), getHeight());
			bufferImg2 = new BufferedImage(getWidth(),getHeight(),BufferedImage.TYPE_INT_ARGB);
			mallaTablero3d();
		}

		update(g);
	}
	
	public void update(Graphics g) {
		g.setClip(0,0,getWidth(), getHeight());
		imgb = createImage(getWidth(), getHeight());
		gbuffer = imgb.getGraphics();
		gbuffer.setClip(0,0, getWidth(), getHeight());
		gbuffer.drawImage(fondo, 0, 0, this);
		bufferImg2 = new BufferedImage(getWidth(),getHeight(),BufferedImage.TYPE_INT_ARGB);
		
		System.out.println(contEscena);
		contEscena++;
		if(contEscena < 355) 
			proyeccionRey(reyNegro, Color.black);
		
		if(contEscena<240) {
			proyeccionTorre(torreN1);
			proyeccionTorre(torreN2);
			proyeccionRey(reyBlanco, Color.black);
		}
		
		if(contEscena<150)
			proyeccionAlfil();
		
		medidor();
		
		if(contEscena > 24 && contEscena < 29)
			curvaJaque(0,2*3.141596, 700, Color.red, 120, 180);
		//Torre2
		if(contEscena<23)//Primer jaque
			moverTorre2(5,0,0);
		
		if(contEscena > 25 && contEscena < 84) {//tapar con alfil
			moverAlfil(5,5,0);
			if(contEscena%2==0)
				escalarAlfil(.999);	
		}
		if(contEscena >125 && contEscena < 133) {//Explosion captura alfil y jaque 2
			curvaJaque(0,2*3.141596, 700, Color.red, 120, 110);
			curvaJaque(0,2*3.141596, 700, Color.red, 120, 180);
		}

		if(contEscena > 89 && contEscena<122)
			moverTorre(5,0,0);
		
		if(contEscena > 125) {
			moverAlfil(-9,-8,0);
			transformarAlfil();
		}
		
		if(contEscena > 139 && contEscena < 151) 
			moverReyB(5,5,0);
		
		if(contEscena == 141) {
			for(int i=0; i<21; i++) {
				reyBlanco[i] = T.escalacion(reyBlanco[i], .9, .9, .9);
			}
		}
		
		if(contEscena>154 && contEscena < 163)//Tercer jaque con torre de hasta atras
			moverTorre2(5,0,0);
		
		if(contEscena>162 && contEscena < 167)
			curvaJaque(0,2*3.141596, 700, Color.red, 165, 225);
		
		if(contEscena>167 && contEscena < 180)
			moverReyB(5,0,0);
		
		if(contEscena>183 && contEscena < 199)
			moverTorre(5,0,0);
		
		if(contEscena>198 && contEscena < 207)
			curvaJaque(0,2*3.141596, 700, Color.red, 230, 230);
		
		if(contEscena>215 && contEscena < 220)
			curvaJaque(0,2*3.141596, 700, Color.red, 230, 230);
		
		if(contEscena>228 && contEscena < 235)
			curvaJaque(0,2*3.141596, 700, Color.red, 230, 230);
		
		//Hacemos solo las trasformaciones al rey blanco
		if(contEscena > 239 &&  contEscena < 250) {
			moverReyN();
		}
		if(contEscena > 249 && contEscena < 300) {//ondas de la ganacion
			curvaYei(0, 2*3.141596, 1000, Color.blue, 100, 100);
			curvaYei(0, 2*3.141596, 1000, Color.pink, 0, 0);
			curvaYei(0, 2*3.141596, 1000, Color.red, 180, 100);
			curvaYei(0, 2*3.141596, 1000, Color.yellow, 0, -100);
			curvaYei(0, 2*3.141596, 1000, Color.magenta, 170, -100);
			curvaYei(0, 2*3.141596, 1000, Color.green, 200, 50);
			curvaYei(0, 2*3.141596, 1000, Color.yellow, 180, -200);
			curvaYei(0, 2*3.141596, 1000, Color.green, 100, -220);
		}
		
		if(contEscena == 305) {
			contEscena = 0;
			iniciarFiguras();
			movY = 125;
		}
		g.drawImage(imgb, 0, 0, this);
	}
	
	public void putPixel(int x, int y, Color c) {
		bufferImg.setRGB(0, 0, c.getRGB());
		bufferImg2.getGraphics().drawImage(bufferImg,  x,  y,  this);//Para el relleno
		gbuffer.drawImage(bufferImg, x, y, this);
	}
	 
	public void bresenham(int x1, int y1, int x2, int y2,Color cc){
        int dx=Math.abs(x2 - x1);
        int dy=Math.abs(y2 - y1);
        int sx=(x1<x2) ? 1 : -1;
        int sy=(y1<y2) ? 1 : -1;
        int p0=dx-dy;

        while(true){
            putPixel(x1,y1,cc);
            if (x1==x2 && y1==y2)
                break;
            int e2 = 2*p0;
            if (e2>-dy){
                p0 =p0-dy;
                x1 =x1+sx;
            }
            if (e2<dx) {
                p0 = p0+dx;
                y1 = y1+sy;
            }
        }
    }
	
	public void inundacion(int x, int y, Color c) {
		if(colorFondo==bufferImg2.getRGB(x+centroX, -y+centroY)) {
			putPixel(x+centroX,-y+centroY,c);
			inundacion(x-1, y, c);
			inundacion(x+1, y, c);
			inundacion(x, y+1, c);
			inundacion(x, y-1, c);
		}
	}
		
	public void mallaTablero3d() {
		Punto3d tablero[] = new Punto3d[4];
		tablero[0] = new Punto3d(-240,240,1);
		tablero[1] = new Punto3d(240,240,1);
		tablero[2] = new Punto3d(-330,-300,1);
		tablero[3] = new Punto3d(330,-300,1);
		
		bresenham((int) tablero[0].x+centroX,(int) -tablero[0].y+centroY,(int) tablero[1].x+centroX,(int) -tablero[1].y+centroY, cafe);
		bresenham((int) tablero[0].x+centroX,(int) -tablero[0].y+centroY,(int) tablero[2].x+centroX,(int) -tablero[2].y+centroY, cafe);
		//bresenham((int) tablero[1].x+centroX,(int) -tablero[1].y+centroY,(int) tablero[3].x+centroX,(int) -tablero[3].y+centroY, cafe);
		//bresenham((int) tablero[2].x+centroX,(int) -tablero[2].y+centroY,(int) tablero[3].x+centroX,(int) -tablero[3].y+centroY, cafe);
		
		double xArriba[] = {-240, -180, -120, -60, 0, 60, 115, 180, 240};
		double xAbajo[] = {-330, -247.5, -165, -82.5, 0, 82.5, 165, 247.5, 330};
		double yS[] = {-300, -209.55, -126.35, -49.47, 21.08, 85.32, 143.2, 194.75, 240};
		int aumento = 0;
		for(int i=0;i<9;i++) {
			bresenham((int) xArriba[i]+centroX,(int) -tablero[0].y+centroY,(int) xAbajo[i] + centroX,(int) -tablero[2].y+centroY, cafe);
			bresenham(-330+centroX+aumento+i,(int) -yS[i]+centroY,330 + centroX-aumento-i,(int) -yS[i]+centroY, cafe);
			aumento+=11;
		}
		double centrosX[] = {-248, -173, -98, -23, 52, 117, 185, 240};
		for(int i=0; i<8;i++) {//i es la fila, empieza desde abajo
			if(i%2==0) {//
				inundacion((int) centrosX[i], -250, verde);
				inundacion((int) centrosX[i], -100, verde);
				inundacion((int) centrosX[i], 60, verde);
				inundacion((int) centrosX[i], 180, verde);
			}else {
				inundacion((int) centrosX[i], -200, verde);
				inundacion((int) centrosX[i], -40, verde);
				inundacion((int) centrosX[i], 100, verde);
				inundacion((int) centrosX[i], 200, verde);
			}
		}
	}
	
	public void proyeccionRey(Punto3d rey[], Color c) {
		Punto3d rey2[] = new Punto3d[21];
		double x,y;
		double d1 = 1.5, d2 = 1, d3 = 1;
		for(int i=0;i<21;i++) {
			x = rey[i].x - ((d1/d3)*rey[i].z);
			y = rey[i].y - ((d2/d3)*rey[i].z);
			rey2[i] = new Punto3d(x,y,0);
			putPixel((int) x+400, (int) -y + 400, Color.red);
		}
		
		bresenham((int) rey2[1].x +400, (int) -rey2[1].y +400, (int) rey2[2].x +400, (int) -rey2[2].y +400, Color.black);//cara iizquierda abajo
		bresenham((int) rey2[5].x +400, (int) -rey2[5].y +400, (int) rey2[6].x +400, (int) -rey2[6].y +400, Color.black);//cara izquierda arriba
		bresenham((int) rey2[1].x +400, (int) -rey2[1].y +400, (int) rey2[5].x +400, (int) -rey2[5].y +400, Color.black);//cara izquierda izq
		bresenham((int) rey2[2].x +400, (int) -rey2[2].y +400, (int) rey2[6].x +400, (int) -rey2[6].y +400, Color.black);//medio
		double xcen, ycen;
		xcen = (rey2[1].x+rey2[2].x)/2;
		ycen = (rey2[1].y+rey2[3].y)/2;
		if(rey == reyBlanco)
			inundacion((int) xcen, (int) ycen, beige);
		else
			inundacion((int) xcen, (int) ycen, negro);
		
		
		bresenham((int) rey2[6].x +400, (int) -rey2[6].y +400, (int) rey2[7].x +400, (int) -rey2[7].y +400, Color.black);//cara derecha arriba
		bresenham((int) rey2[2].x +400, (int) -rey2[2].y +400, (int) rey2[3].x +400, (int) -rey2[3].y +400, Color.black);//cara derecha abajo
		bresenham((int) rey2[3].x +400, (int) -rey2[3].y +400, (int) rey2[7].x +400, (int) -rey2[7].y +400, Color.black);//cara derecha der
		xcen = (rey2[3].x+rey2[2].x)/2;
		ycen = (rey2[7].y+rey2[3].y)/2;
		if(rey == reyBlanco)
			inundacion((int) xcen, (int) ycen, beige);
		else
			inundacion((int) xcen, (int) ycen, negro);
		
		bresenham((int) rey2[4].x +400, (int) -rey2[4].y +400, (int) rey2[5].x +400, (int) -rey2[5].y +400, Color.black);	//arriba izq
		bresenham((int) rey2[4].x +400, (int) -rey2[4].y +400, (int) rey2[7].x +400, (int) -rey2[7].y +400, Color.black);	//arriba der
		xcen = (rey2[5].x+rey2[7].x)/2;
		ycen = (rey2[4].y+rey2[6].y)/2;
		if(rey == reyBlanco)
			inundacion((int) xcen, (int) ycen, beige);
		else
			inundacion((int) xcen, (int) ycen, negro);
		
		bresenham((int) rey2[9].x +400, (int) -rey2[9].y +400, (int) rey2[10].x +400, (int) -rey2[10].y +400, Color.black);//abajo
		bresenham((int) rey2[13].x +400, (int) -rey2[13].y +400, (int) rey2[14].x +400, (int) -rey2[14].y +400, Color.black);//arriba
		bresenham((int) rey2[9].x +400, (int) -rey2[9].y +400, (int) rey2[13].x +400, (int) -rey2[13].y +400, Color.black);
		bresenham((int) rey2[10].x +400, (int) -rey2[10].y +400, (int) rey2[14].x +400, (int) -rey2[14].y +400, Color.black);//medio
		xcen = (rey2[9].x+rey2[10].x)/2;
		ycen = rey2[13].y-5;
		if(rey == reyBlanco)
			inundacion((int) xcen, (int) ycen, beige);
		else
			inundacion((int) xcen, (int) ycen, negro);
		
		bresenham((int) rey2[10].x +400, (int) -rey2[10].y +400, (int) rey2[11].x +400, (int) -rey2[11].y +400, Color.black);//abajo
		bresenham((int) rey2[14].x +400, (int) -rey2[14].y +400, (int) rey2[15].x +400, (int) -rey2[15].y +400, Color.black);//arriba
		bresenham((int) rey2[11].x +400, (int) -rey2[11].y +400, (int) rey2[15].x +400, (int) -rey2[15].y +400, Color.black);//der
		xcen = (rey2[10].x+rey2[11].x)/2;
		ycen = rey2[14].y-1;

		if(rey == reyBlanco)
			inundacion((int) xcen, (int) ycen, beige);
		else
			inundacion((int) xcen, (int) ycen, negro);
		
		bresenham((int) rey2[0].x +400, (int) -rey2[0].y +400, (int) rey2[1].x +400, (int) -rey2[1].y +400, c);//no sirve
		bresenham((int) rey2[0].x +400, (int) -rey2[0].y +400, (int) rey2[3].x +400, (int) -rey2[3].y +400, c);	//no
		bresenham((int) rey2[0].x +400, (int) -rey2[0].y +400, (int) rey2[4].x +400, (int) -rey2[4].y +400, c);//no
		bresenham((int) rey2[8].x +400, (int) -rey2[8].y +400, (int) rey2[9].x +400, (int) -rey2[9].y +400, c);
		bresenham((int) rey2[8].x +400, (int) -rey2[8].y +400, (int) rey2[11].x +400, (int) -rey2[11].y +400, c);
		bresenham((int) rey2[12].x +400, (int) -rey2[12].y +400, (int) rey2[13].x +400, (int) -rey2[13].y +400, c);
		bresenham((int) rey2[12].x +400, (int) -rey2[12].y +400, (int) rey2[15].x +400, (int) -rey2[15].y +400, c);
		bresenham((int) rey2[8].x +400, (int) -rey2[8].y +400, (int) rey2[12].x +400, (int) -rey2[12].y +400, c);
		bresenham((int) rey2[16].x +400, (int) -rey2[16].y +400, (int) rey2[17].x +400, (int) -rey2[17].y +400, c);
		bresenham((int) rey2[19].x +400, (int) -rey2[19].y +400, (int) rey2[20].x +400, (int) -rey2[20].y +400, c);
	}
	
	public void proyeccionTorre(Punto3d torre[]) {//Solo hay torres negras, por lo que seran del mismo color
		Punto3d torre2[] = new Punto3d[36];
		double x,y;
		double d1 = 1, d2 = 1, d3 = 1;
		for(int i=0;i<36;i++) {
			x = torre[i].x - ((d1/d3)*torre[i].z);
			y = torre[i].y - ((d2/d3)*torre[i].z);
			x*=2;
			y*=2;
			torre2[i] = new Punto3d(x,y,0);
			putPixel((int) x+400, (int) -y + 400, Color.red);
		}
		
		bresenham((int) torre2[1].x +400, (int) -torre2[1].y +400, (int) torre2[2].x +400, (int) -torre2[2].y +400, Color.black);//izq abajo
		bresenham((int) torre2[5].x +400, (int) -torre2[5].y +400, (int) torre2[6].x +400, (int) -torre2[6].y +400, Color.black);//izq arriba
		bresenham((int) torre2[1].x +400, (int) -torre2[1].y +400, (int) torre2[5].x +400, (int) -torre2[5].y +400, Color.black);//izq izq
		bresenham((int) torre2[2].x +400, (int) -torre2[2].y +400, (int) torre2[6].x +400, (int) -torre2[6].y +400, Color.black);//medio
		
		double xcen, ycen;
		xcen = (torre2[1].x+torre2[2].x)/2;
		ycen = (torre2[1].y+torre2[5].y)/2;
		inundacion((int) xcen, (int) ycen, negro);
		
		bresenham((int) torre2[2].x +400, (int) -torre2[2].y +400, (int) torre2[3].x +400, (int) -torre2[3].y +400, Color.black);//der abajo
		bresenham((int) torre2[6].x +400, (int) -torre2[6].y +400, (int) torre2[7].x +400, (int) -torre2[7].y +400, Color.black);//der arriba
		bresenham((int) torre2[3].x +400, (int) -torre2[3].y +400, (int) torre2[7].x +400, (int) -torre2[7].y +400, Color.black);//der der
		xcen = (torre2[3].x+torre2[2].x)/2;
		ycen = (torre2[6].y+torre2[2].y)/2;
		inundacion((int) xcen, (int) ycen, negro);
		
		bresenham((int) torre2[4].x +400, (int) -torre2[4].y +400, (int) torre2[5].x +400, (int) -torre2[5].y +400, Color.black);	//arriba izq
		bresenham((int) torre2[4].x +400, (int) -torre2[4].y +400, (int) torre2[7].x +400, (int) -torre2[7].y +400, Color.black);
		xcen = (torre2[5].x+torre2[7].x)/2;
		ycen = torre2[4].y-10;
		inundacion((int) xcen, (int) ycen, negro);
		
		bresenham((int) torre2[0].x +400, (int) -torre2[0].y +400, (int) torre2[1].x +400, (int) -torre2[1].y +400, Color.black);
		bresenham((int) torre2[0].x +400, (int) -torre2[0].y +400, (int) torre2[3].x +400, (int) -torre2[3].y +400, Color.black);
		bresenham((int) torre2[0].x +400, (int) -torre2[0].y +400, (int) torre2[4].x +400, (int) -torre2[4].y +400, Color.black);
		bresenham((int) torre2[8].x +400, (int) -torre2[8].y +400, (int) torre2[9].x +400, (int) -torre2[9].y +400, Color.black);
		bresenham((int) torre2[9].x +400, (int) -torre2[9].y +400, (int) torre2[10].x +400, (int) -torre2[10].y +400, Color.black);
		bresenham((int) torre2[11].x +400, (int) -torre2[11].y +400, (int) torre2[12].x +400, (int) -torre2[12].y +400, Color.black);
		bresenham((int) torre2[13].x +400, (int) -torre2[13].y +400, (int) torre2[12].x +400, (int) -torre2[12].y +400, Color.black);
		bresenham((int) torre2[13].x +400, (int) -torre2[13].y +400, (int) torre2[14].x +400, (int) -torre2[14].y +400, Color.black);
		bresenham((int) torre2[11].x +400, (int) -torre2[11].y +400, (int) torre2[14].x +400, (int) -torre2[14].y +400, Color.black);
		bresenham((int) torre2[11].x +400, (int) -torre2[11].y +400, (int) torre2[4].x +400, (int) -torre2[4].y +400, Color.black);
		bresenham((int) torre2[8].x +400, (int) -torre2[8].y +400, (int) torre2[12].x +400, (int) -torre2[12].y +400, Color.black);
		bresenham((int) torre2[9].x +400, (int) -torre2[9].y +400, (int) torre2[13].x +400, (int) -torre2[13].y +400, Color.black);
		bresenham((int) torre2[10].x +400, (int) -torre2[10].y +400, (int) torre2[14].x +400, (int) -torre2[14].y +400, Color.black);
		
		bresenham((int) torre2[16].x +400, (int) -torre2[16].y +400, (int) torre2[17].x +400, (int) -torre2[17].y +400, Color.black);
		bresenham((int) torre2[17].x +400, (int) -torre2[17].y +400, (int) torre2[15].x +400, (int) -torre2[15].y +400, Color.black);
		bresenham((int) torre2[21].x +400, (int) -torre2[21].y +400, (int) torre2[19].x +400, (int) -torre2[19].y +400, Color.black);
		bresenham((int) torre2[19].x +400, (int) -torre2[19].y +400, (int) torre2[20].x +400, (int) -torre2[20].y +400, Color.black);
		bresenham((int) torre2[20].x +400, (int) -torre2[20].y +400, (int) torre2[18].x +400, (int) -torre2[18].y +400, Color.black);
		bresenham((int) torre2[18].x +400, (int) -torre2[18].y +400, (int) torre2[21].x +400, (int) -torre2[21].y +400, Color.black);
		bresenham((int) torre2[5].x +400, (int) -torre2[5].y +400, (int) torre2[21].x +400, (int) -torre2[21].y +400, Color.black);
		bresenham((int) torre2[16].x +400, (int) -torre2[16].y +400, (int) torre2[19].x +400, (int) -torre2[19].y +400, Color.black);
		bresenham((int) torre2[17].x +400, (int) -torre2[17].y +400, (int) torre2[20].x +400, (int) -torre2[20].y +400, Color.black);
		bresenham((int) torre2[15].x +400, (int) -torre2[15].y +400, (int) torre2[18].x +400, (int) -torre2[18].y +400, Color.black);
		
		bresenham((int) torre2[22].x +400, (int) -torre2[22].y +400, (int) torre2[23].x +400, (int) -torre2[23].y +400, Color.black);
		bresenham((int) torre2[23].x +400, (int) -torre2[23].y +400, (int) torre2[24].x +400, (int) -torre2[24].y +400, Color.black);
		bresenham((int) torre2[25].x +400, (int) -torre2[25].y +400, (int) torre2[26].x +400, (int) -torre2[26].y +400, Color.black);
		bresenham((int) torre2[26].x +400, (int) -torre2[26].y +400, (int) torre2[27].x +400, (int) -torre2[27].y +400, Color.black);
		bresenham((int) torre2[27].x +400, (int) -torre2[27].y +400, (int) torre2[28].x +400, (int) -torre2[28].y +400, Color.black);
		bresenham((int) torre2[25].x +400, (int) -torre2[25].y +400, (int) torre2[28].x +400, (int) -torre2[28].y +400, Color.black);
		bresenham((int) torre2[6].x +400, (int) -torre2[6].y +400, (int) torre2[25].x +400, (int) -torre2[25].y +400, Color.black);
		bresenham((int) torre2[22].x +400, (int) -torre2[22].y +400, (int) torre2[26].x +400, (int) -torre2[26].y +400, Color.black);
		bresenham((int) torre2[23].x +400, (int) -torre2[23].y +400, (int) torre2[27].x +400, (int) -torre2[27].y +400, Color.black);
		bresenham((int) torre2[24].x +400, (int) -torre2[24].y +400, (int) torre2[28].x +400, (int) -torre2[28].y +400, Color.black);
		
		bresenham((int) torre2[29].x +400, (int) -torre2[29].y +400, (int) torre2[30].x +400, (int) -torre2[30].y +400, Color.black);
		bresenham((int) torre2[30].x +400, (int) -torre2[30].y +400, (int) torre2[31].x +400, (int) -torre2[31].y +400, Color.black);
		bresenham((int) torre2[7].x +400, (int) -torre2[7].y +400, (int) torre2[32].x +400, (int) -torre2[32].y +400, Color.black);
		bresenham((int) torre2[29].x +400, (int) -torre2[29].y +400, (int) torre2[33].x +400, (int) -torre2[33].y +400, Color.black);
		bresenham((int) torre2[30].x +400, (int) -torre2[30].y +400, (int) torre2[34].x +400, (int) -torre2[34].y +400, Color.black);
		bresenham((int) torre2[31].x +400, (int) -torre2[31].y +400, (int) torre2[35].x +400, (int) -torre2[35].y +400, Color.black);
		bresenham((int) torre2[32].x +400, (int) -torre2[32].y +400, (int) torre2[33].x +400, (int) -torre2[33].y +400, Color.black);
		bresenham((int) torre2[33].x +400, (int) -torre2[33].y +400, (int) torre2[34].x +400, (int) -torre2[34].y +400, Color.black);
		bresenham((int) torre2[33].x +400, (int) -torre2[33].y +400, (int) torre2[35].x +400, (int) -torre2[35].y +400, Color.black);
		bresenham((int) torre2[35].x +400, (int) -torre2[35].y +400, (int) torre2[32].x +400, (int) -torre2[32].y +400, Color.black);
	}
	
	public void proyeccionAlfil() {
		Punto3d alifl2[] = new Punto3d[5];
		double x,y;
		double d1 = 10.5, d2 = 10, d3 = 10.2;
		for(int i=0;i<5;i++) {
			x = alfil[i].x - ((d1/d3)*alfil[i].z);
			y = alfil[i].y - ((d2/d3)*alfil[i].z);
			alifl2[i] = new Punto3d(x,y,0);
			putPixel((int) x+centroX, (int) -y + centroY, Color.red);
		}
	
		double xcen, ycen;
		//Obtener el centro de cada triangulo
		xcen = (alifl2[1].x+alifl2[2].x)/2;
		ycen = (alifl2[1].y+alifl2[4].y)/2;
		
		bresenham((int) alifl2[1].x +centroX, (int) -alifl2[1].y +centroY, (int) alifl2[2].x +centroX, (int) -alifl2[2].y +centroY, Color.black);//cara izquierda
		bresenham((int) alifl2[1].x +centroX, (int) -alifl2[1].y +centroY, (int) alifl2[4].x +centroX, (int) -alifl2[4].y +centroY, Color.black);//cara izquierda
		bresenham((int) alifl2[2].x +centroX, (int) -alifl2[2].y +centroY, (int) alifl2[4].x +centroX, (int) -alifl2[4].y +centroY, Color.black);//cara izquierda y derecha
		if(contEscena < 127)
			inundacion((int) xcen+20, (int) ycen, beige);
		
		bresenham((int) alifl2[3].x +centroX, (int) -alifl2[3].y +centroY, (int) alifl2[4].x +centroX, (int) -alifl2[4].y +centroY, Color.black);//cara derecha
		bresenham((int) alifl2[2].x +centroX, (int) -alifl2[2].y +centroY, (int) alifl2[3].x +centroX, (int) -alifl2[3].y +centroY, Color.black);//cara derecha
		xcen = (alifl2[3].x+alifl2[2].x)/2;
		ycen = alifl2[3].y;
		if(contEscena < 127)
			inundacion((int) xcen, (int) ycen, beige);
		
		
		bresenham((int) alifl2[0].x +centroX, (int) -alifl2[0].y +centroY, (int) alifl2[1].x +centroX, (int) -alifl2[1].y +centroY, Color.black);
		bresenham((int) alifl2[0].x +centroX, (int) -alifl2[0].y +centroY, (int) alifl2[3].x +centroX, (int) -alifl2[3].y +centroY, Color.black);
		bresenham((int) alifl2[0].x +centroX, (int) -alifl2[0].y +centroY, (int) alifl2[4].x +centroX, (int) -alifl2[4].y +centroY, Color.black);
		
	}
	
	public void iniciarFiguras() {
		//Torre negra 1 arriba
		torreN1[0] = new Punto3d(0,0,0);
		torreN1[1] = new Punto3d(10,0,0);
		torreN1[2] = new Punto3d(10,10,0);
		torreN1[3] = new Punto3d(0,10,0);
		torreN1[4] = new Punto3d(0,0,10);
		torreN1[5] = new Punto3d(10,0,10);
		torreN1[6] = new Punto3d(10,10,10);
		torreN1[7] = new Punto3d(0,10,10);
		
		torreN1[8] = new Punto3d(3,0,10);
		torreN1[9] = new Punto3d(3,3,10);
		torreN1[10] = new Punto3d(0,3,10);
		torreN1[11] = new Punto3d(0,0,13);
		torreN1[12] = new Punto3d(3,0,13);
		torreN1[13] = new Punto3d(3,3,13);
		torreN1[14] = new Punto3d(0,3,13);
		
		torreN1[15] = new Punto3d(7,0,10);
		torreN1[16] = new Punto3d(10,3,10);
		torreN1[17] = new Punto3d(7,3,10);
		torreN1[18] = new Punto3d(7,0,13);
		torreN1[19] = new Punto3d(10,3,13);
		torreN1[20] = new Punto3d(7,3,13);
		torreN1[21] = new Punto3d(10,0,13);
		
		torreN1[22] = new Punto3d(10,7,10);
		torreN1[23] = new Punto3d(7,7,10);
		torreN1[24] = new Punto3d(7,10,10);
		torreN1[25] = new Punto3d(10,10,13);
		torreN1[26] = new Punto3d(10,7,13);
		torreN1[27] = new Punto3d(7,7,13);
		torreN1[28] = new Punto3d(7,10,13);
		
		torreN1[29] = new Punto3d(0,7,10);
		torreN1[30] = new Punto3d(3,7,10);
		torreN1[31] = new Punto3d(3,10,10);
		torreN1[32] = new Punto3d(0,10,13);
		torreN1[33] = new Punto3d(0,7,13);
		torreN1[34] = new Punto3d(3,7,13);
		torreN1[35] = new Punto3d(3,10,13);
		for(int i=0; i<36; i++) {
			torreN1[i] = T.escalacion(torreN1[i], 1.2, 1.2, 1.2);
			torreN1[i] = T.rotacionX(torreN1[i], 10);
			torreN1[i] = T.rotacionY(torreN1[i], 110);
			torreN1[i] = T.rotacionZ(torreN1[i], 80);
			torreN1[i] = T.traslacion(torreN1[i], -115, 55, 0);
		}
		
		//Torre negra 2
		torreN2[0] = new Punto3d(0,0,0);
		torreN2[1] = new Punto3d(10,0,0);
		torreN2[2] = new Punto3d(10,10,0);
		torreN2[3] = new Punto3d(0,10,0);
		torreN2[4] = new Punto3d(0,0,10);
		torreN2[5] = new Punto3d(10,0,10);
		torreN2[6] = new Punto3d(10,10,10);
		torreN2[7] = new Punto3d(0,10,10);
		
		torreN2[8] = new Punto3d(3,0,10);
		torreN2[9] = new Punto3d(3,3,10);
		torreN2[10] = new Punto3d(0,3,10);
		torreN2[11] = new Punto3d(0,0,13);
		torreN2[12] = new Punto3d(3,0,13);
		torreN2[13] = new Punto3d(3,3,13);
		torreN2[14] = new Punto3d(0,3,13);
		
		torreN2[15] = new Punto3d(7,0,10);
		torreN2[16] = new Punto3d(10,3,10);
		torreN2[17] = new Punto3d(7,3,10);
		torreN2[18] = new Punto3d(7,0,13);
		torreN2[19] = new Punto3d(10,3,13);
		torreN2[20] = new Punto3d(7,3,13);
		torreN2[21] = new Punto3d(10,0,13);
		
		torreN2[22] = new Punto3d(10,7,10);
		torreN2[23] = new Punto3d(7,7,10);
		torreN2[24] = new Punto3d(7,10,10);
		torreN2[25] = new Punto3d(10,10,13);
		torreN2[26] = new Punto3d(10,7,13);
		torreN2[27] = new Punto3d(7,7,13);
		torreN2[28] = new Punto3d(7,10,13);
		
		torreN2[29] = new Punto3d(0,7,10);
		torreN2[30] = new Punto3d(3,7,10);
		torreN2[31] = new Punto3d(3,10,10);
		torreN2[32] = new Punto3d(0,10,13);
		torreN2[33] = new Punto3d(0,7,13);
		torreN2[34] = new Punto3d(3,7,13);
		torreN2[35] = new Punto3d(3,10,13);
		for(int i=0; i<36; i++) {
			torreN2[i] = T.escalacion(torreN2[i], 1.5, 1.5, 1.5);
			torreN2[i] = T.rotacionX(torreN2[i], 10);
			torreN2[i] = T.rotacionY(torreN2[i], 110);
			torreN2[i] = T.rotacionZ(torreN2[i], 80);
			torreN2[i] = T.traslacion(torreN2[i], -55, -135, 0);
			
		}
		
		//Rey Blanco
		reyBlanco[0] = new Punto3d(0,0,0);
		reyBlanco[1] = new Punto3d(5,0,0);
		reyBlanco[2] = new Punto3d(5,5,0);
		reyBlanco[3] = new Punto3d(0,5,0);
		
		reyBlanco[4] = new Punto3d(0,0,5);
		reyBlanco[5] = new Punto3d(5,0,5);
		reyBlanco[6] = new Punto3d(5,5,5);
		reyBlanco[7] = new Punto3d(0,5,5);
		
		reyBlanco[8] = new Punto3d(1.5,1.5,5);
		reyBlanco[9] = new Punto3d(3.5,1.5,5);
		reyBlanco[10] = new Punto3d(3.5,3.5,5);
		reyBlanco[11] = new Punto3d(1.5,3.5,5);
		
		reyBlanco[12] = new Punto3d(1.5,1.5,7);
		reyBlanco[13] = new Punto3d(3.5,1.5,7);
		reyBlanco[14] = new Punto3d(3.5,3.5,7);
		reyBlanco[15] = new Punto3d(1.5,3.5,7);
		
		reyBlanco[16] = new Punto3d(2.5,2.5,7);
		reyBlanco[17] = new Punto3d(2.5,2.5,10);
		reyBlanco[18] = new Punto3d(2.5,2.5,9);
		reyBlanco[19] = new Punto3d(2.5,2,9);
		reyBlanco[20] = new Punto3d(2.5,3,9);
		for(int i=0; i<21; i++) {
			reyBlanco[i] = T.escalacion(reyBlanco[i], 4.8, 4.8, 4.8);
			reyBlanco[i] = T.rotacionX(reyBlanco[i], 10);
			reyBlanco[i] = T.rotacionY(reyBlanco[i], 110);
			reyBlanco[i] = T.rotacionZ(reyBlanco[i], 80);
			reyBlanco[i] = T.traslacion(reyBlanco[i],100,170, 0);
		}
		
		//Rey Negro
		reyNegro[0] = new Punto3d(20,20,20);
		reyNegro[1] = new Punto3d(25,20,20);
		reyNegro[2] = new Punto3d(25,25,20);
		reyNegro[3] = new Punto3d(20,25,20);
		
		reyNegro[4] = new Punto3d(20,20,25);
		reyNegro[5] = new Punto3d(25,20,25);
		reyNegro[6] = new Punto3d(25,25,25);
		reyNegro[7] = new Punto3d(20,25,25);
		
		reyNegro[8] = new Punto3d(21.5,21.5,25);
		reyNegro[9] = new Punto3d(23.5,21.5,25);
		reyNegro[10] = new Punto3d(23.5,23.5,25);
		reyNegro[11] = new Punto3d(21.5,23.5,25);
		
		reyNegro[12] = new Punto3d(21.5,21.5,27);
		reyNegro[13] = new Punto3d(23.5,21.5,27);
		reyNegro[14] = new Punto3d(23.5,23.5,27);
		reyNegro[15] = new Punto3d(21.5,23.5,27);
		
		reyNegro[16] = new Punto3d(22.5,22.5,27);
		reyNegro[17] = new Punto3d(22.5,22.5,30);
		reyNegro[18] = new Punto3d(22.5,22.5,29);
		reyNegro[19] = new Punto3d(22.5,22,29);
		reyNegro[20] = new Punto3d(22.5,23,29);
		for(int i=0; i<21; i++) {
			reyNegro[i] = T.escalacion(reyNegro[i], 4.8, 4.8, 4.8);
			reyNegro[i] = T.rotacionX(reyNegro[i], 10);
			reyNegro[i] = T.rotacionY(reyNegro[i], 110);
			reyNegro[i] = T.rotacionZ(reyNegro[i], 80);
			reyNegro[i] = T.traslacion(reyNegro[i],-20,110, 0);
		}
		
		alfil[0] = new Punto3d(0,0,0);
		alfil[1] = new Punto3d(10,0,0);
		alfil[2] = new Punto3d(10,10,0);
		alfil[3] = new Punto3d(0,10,0);
		alfil[4] = new Punto3d(5,5,10);
		for(int i=0; i<5; i++) {
			alfil[i] = T.escalacion(alfil[i],3.3,3.3,3.3);
			alfil[i] = T.rotacionX(alfil[i], 10);
			alfil[i] = T.rotacionY(alfil[i], 110);
			alfil[i] = T.rotacionZ(alfil[i], 80);
			alfil[i] = T.traslacion(alfil[i],-190,-180, 0);
		}
	}

	public void moverTorre(double x, double y, double z) {
		for(int i=0; i<36; i++) 
			torreN1[i] = T.traslacion(torreN1[i], x, y, z);
	}
	
	public void moverTorre2(double x, double y, double z) {
		for(int i=0; i<36; i++) 
			torreN2[i] = T.traslacion(torreN2[i], x, y, z);
	}
	
	public void moverAlfil(double x, double y, double z) {
		for(int i=0; i<5; i++) 
			alfil[i] = T.traslacion(alfil[i], x, y, z);
	}
	
	public void escalarAlfil(double d) {
		for(int i=0; i<5; i++) 
			alfil[i] = T.escalacion(alfil[i], d,d,d);
	}
	
	public void transformarAlfil() {
		for(int i=0; i<5; i++) 
			alfil[i] = T.rotacionX(alfil[i], 10);
	}
	
	public void moverReyB(double x, double y, double z) {
		for(int i=0; i<21; i++) {
			reyBlanco[i] = T.traslacion(reyBlanco[i], x,y,z);
		}
	}
	
	public void moverReyN() {
		for(int i=0; i<21; i++) {
			reyNegro[i] = T.traslacion(reyNegro[i], 3,-21,0);
			reyNegro[i] = T.escalacion(reyNegro[i], 1.09,1.09,1.09);
		}
	}
	
	public void roatarReyN() {
		for(int i=0; i<21; i++) {
			reyNegro[i] = T.rotacionX(reyNegro[i], 1);
			reyNegro[i] = T.rotacionY(reyNegro[i], 1);
			reyNegro[i] = T.rotacionZ(reyNegro[i], 1);
		}
	}
		
	public void curvaJaque(double x1, double x2, double np, Color c, double ax, double ay) {//curva de jaque
		double y, x;
		double incremento = (x2-x1)/(np-1);
		for(double i=0;i<x2;i+= incremento) {
			x = ax+20*(Math.cos(i) + .5*Math.cos(7*i) + (.33333)*Math.sin(17*i));
			y = ay+20*(Math.sin(i) + .5*Math.sin(7*i) + (.33333)*Math.cos(17*i));
			putPixel((int) Math.round(x)+centroX, (int) -Math.round(y)+centroX, c);
		}
	}
	
	public void curvaYei(double x1, double x2, double np, Color c, double xa, double ya) {
		double incremento = (x2-x1)/(np-1);
		double y, x;
		for(double i=0;i<np;i++) {
			y = incremento*i*5;
			x = xa+y * Math.cos((4*y)/5);
			putPixel((int) Math.round(x)+300, (int) -Math.round(y)+300-(int) ya, c);
		}
	}
	
	public void medidor() {
		bresenham(-330+centroX, 200+centroY, -350+centroX, 200+centroY, Color.black);
		bresenham(-330+centroX, -200+centroY, -350+centroX, -200+centroY, Color.black);
		bresenham(-350+centroX, -200+centroY, -350+centroX, 200+centroY, Color.black);
		bresenham(-330+centroX, -200+centroY, -330+centroX, 200+centroY, Color.black);
		bresenham(-330+centroX, movY+centroY, -350+centroX, movY+centroY, Color.black);
		inundacion(-340, -50, Color.black);
		if(contEscena > 90 && contEscena%2 == 0 && contEscena < 160)
			movY+=2;
	}
	
	public static void main(String[] args) {
		new Animacion3d();
	}

	@Override
	public void run() {
		while(true) {
			try {
				hilo.sleep(60);	//bajarlo a 90
				repaint();
			}catch(InterruptedException ex) {
				
			}
		}
	}
}//Fin main
