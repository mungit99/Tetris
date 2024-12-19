package tetris;

import java.awt.Color;
import java.awt.Graphics;

import tetris.Pozicija.Smer;

public class Jednodelni extends Figura {
	int k;

	public Jednodelni(Pozicija p, Color c, Tabla t) {
		super(p, c, t);
	}

	@Override
	public boolean prostire(Pozicija p) {
		return (p.equals(getPozicija()));
	}

	@Override
	public boolean pomeri(Smer s) throws Greska {
		if(!owner.zauzeta(getPozicija().napravi(s))) {
			
			getPozicija().pomeri(s);
			return true;
		}
		throw new Greska();
	}

	@Override
	public void crtaj(Graphics g) {
		g.setColor(getBoja());
		g.fillOval(owner.kolone[getPozicija().getX()], owner.vrste[getPozicija().getY()], owner.squareWidth, owner.squareHeight);
	}

}
