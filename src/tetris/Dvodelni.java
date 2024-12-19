package tetris;

import java.awt.Color;
import java.awt.Graphics;

import tetris.Pozicija.Smer;

public class Dvodelni extends Figura {

	public Dvodelni(Pozicija p, Color c, Tabla t) {
		super(p, c, t);
	}

	@Override
	public boolean prostire(Pozicija p) {
		return(p.equals(getPozicija()) || p.equals(getPozicija().napravi(Smer.DESNO)));
	}

	@Override
	public boolean pomeri(Smer s) throws Greska {
		if(!owner.zauzeta(getPozicija().napravi(s)) && !owner.zauzeta(getPozicija().napravi(Smer.DESNO).napravi(s))) {
			getPozicija().pomeri(s);
			return true;
		}
		return false;
	}

	@Override
	public void crtaj(Graphics g) {
		g.setColor(getBoja());
		int x = getPozicija().getX();
		int y = getPozicija().getY();
		g.fillRect(owner.kolone[x], owner.vrste[y], owner.kolone[x + 2] - owner.kolone[x], owner.vrste[y + 1] - owner.vrste[y]);
	}

}
