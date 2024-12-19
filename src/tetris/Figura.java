package tetris;

import java.awt.Color;
import java.awt.Graphics;

public abstract class Figura {

	private Pozicija pozicija;
	private Color boja;
	protected Tabla owner;
	boolean crt = true;
	
	public Figura(Pozicija p, Color c, Tabla t) {
		pozicija = p;
		boja = c;
		owner = t;
	}
	
	public Pozicija getPozicija() {
		return pozicija;
	}
	
	public void setPozicija(Pozicija p) {
		pozicija = p;
	}
	
	public Color getBoja() {
		return boja;
	}
	
	public abstract boolean prostire(Pozicija p);
	public abstract boolean pomeri(Pozicija.Smer s) throws Greska;
	public abstract void crtaj(Graphics g);

}
