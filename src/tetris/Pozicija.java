package tetris;

public class Pozicija {
	
	private int x, y;
	private boolean slobodna;
	
	public enum Smer{ LEVO, DOLE, DESNO };
	
	public Pozicija(int xx, int yy) {
		x = xx;
		y = yy;
	}
	
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	public Pozicija napravi(Smer s) {
		switch (s) {
		case LEVO:
			return new Pozicija(x - 1, y);
		case DESNO:
			return new Pozicija(x + 1, y);
		case DOLE: 
			return new Pozicija(x, y + 1);
		default:
			return new Pozicija(x, y);
		}
	}
	
	public void pomeri(Smer s) {
		switch (s) {
		case LEVO:
			x--;
			break;
		case DOLE:
			y++;
			break;
		case DESNO:
			x++;
			break;
		default: 
			break;
		}
	}
	
	public boolean jeSlobodna() {
		return slobodna;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Pozicija)) return false;
		Pozicija p = (Pozicija) obj;
		return (p.x == x && p.y == y);
	}
	
	

}
