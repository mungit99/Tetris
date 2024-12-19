package tetris;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import tetris.Pozicija.Smer;

public class Tabla extends Canvas implements Runnable {
	
	private static int time = 200;
	int k = 0;
	int p = 0;
	private int vrsta, kolona;
	private Thread nit;
	private boolean radi = false;
	Figura pokretna;
	Figura pripravna;
	private int poeni = 0;
	Tetris owner;
	int squareWidth;
	int squareHeight;
	int[] vrste;
	int[] kolone;
	private List<Figura> figure;

	public Tabla(int vr, int kol) {
		vrsta = vr;
		kolona = kol;
		vrste = new int[vrsta + 1];
		kolone = new int[kolona + 1];
		setBackground(Color.LIGHT_GRAY);
		figure = new ArrayList<>();
		nit = new Thread(this);
		addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_LEFT:
					try {
						if(pokretna.getPozicija().getX() > 0 &&
								!zauzeta(pokretna.getPozicija().napravi(Smer.LEVO))) 
									pokretna.setPozicija(pokretna.getPozicija().napravi(Smer.LEVO));
					} catch (Greska e1) {}
					break;
				case KeyEvent.VK_RIGHT:
					if(pokretna instanceof Jednodelni) {
						try {
							if(pokretna.getPozicija().getX() < kolona - 1 && 
									!zauzeta(pokretna.getPozicija().napravi(Smer.DESNO))) 
										pokretna.setPozicija(pokretna.getPozicija().napravi(Smer.DESNO));
						} catch (Greska e1) {}
					}
					else {
						try {
							if(pokretna.getPozicija().getX() < kolona - 2 &&
									!zauzeta(pokretna.getPozicija().napravi(Smer.DESNO).napravi(Smer.DESNO))) 
										pokretna.setPozicija(pokretna.getPozicija().napravi(Smer.DESNO));
						} catch (Greska e1) {}
					}
					break;
				case KeyEvent.VK_DOWN:
					if(pokretna instanceof Jednodelni) {
						try {
							if(pokretna.getPozicija().getY() < vrsta - 1 && 
									!zauzeta(pokretna.getPozicija().napravi(Smer.DOLE))) 
										pokretna.setPozicija(pokretna.getPozicija().napravi(Smer.DOLE));
						} catch (Greska e1) {}
					}
					else {
						try {
							if(pokretna.getPozicija().getY() < vrsta - 1 &&
									!zauzeta(pokretna.getPozicija().napravi(Smer.DESNO).napravi(Smer.DOLE)) &&
										!zauzeta(pokretna.getPozicija().napravi(Smer.DOLE))) 
											
								pokretna.setPozicija(pokretna.getPozicija().napravi(Smer.DOLE));
						} catch (Greska e1) {}
					} 
					break;
				default:
					break;
				}
			}
		});
	}
	
	public void pokreni() {
		nit.start();
	}
	
	public void dodaj(Figura f) {
		figure.add(f);
	}
	
	public synchronized void kreni() {
		radi = true;
		notifyAll();
	}
	
	public synchronized void zaustavi() {
		radi = false;
	}
	
	public void prekini() {
		nit.interrupt();
	}

	@Override
	public void run() {
		try {
			while(!nit.isInterrupted()) {
				synchronized(this) {
					while(!radi) wait();
				}
				Thread.sleep(time);
				synchronized(this) {
					try {
						pokretna.pomeri(Smer.DOLE);
					} catch (Greska e) {
						int i = vrsta - 1;
						int k = 0;
						while(i >= 0) {
							for(int j = 0; j < kolona; j++) {
								Pozicija p = new Pozicija(j, i);
								try {
									if(!zauzeta(p)) { 
										k = 0;
										i--;
										break;
									}
								}catch (Greska e1) {k++;}
							if(k == kolona) {
								int t1 = i;
								figure.removeIf(x->x.getPozicija().getY() == t1);
								poeni += 100;
								if(poeni > 0) time = 160;
								if(poeni > 100) time = 120;
								if(poeni > 200) time = 80;
								if(poeni > 300) time = 50;
								i = 0;
								for(Figura f: figure) {
									try {
										f.pomeri(Smer.DOLE);
									} catch (Greska e1) {}
								}
							}
							}
							k = 0;
						}
						pokretna = pripravna;
						try {
							if(!zauzeta(pokretna.getPozicija().napravi(Smer.DOLE)))
							pokretna.setPozicija(pokretna.getPozicija().napravi(Smer.DOLE));
						} catch (Greska e1) {
							nit.interrupt();   
						}
						double d = Math.random();
						double f = Math.random();
						Pozicija p = new Pozicija(kolona / 2, 0);
						Color c;
						if(d > 0.75) c = Color.YELLOW;
						else if(d > 0.5) c = Color.BLUE;
						else if(d > 0.25) c = Color.GREEN;
						else c = Color.RED;
						if(f > 0.5) pripravna = new Jednodelni(p, c, this);
						else pripravna = new Dvodelni(p,c, this);
						figure.add(pripravna);
					}
					repaint();
				}
			}
		} catch (InterruptedException e) {}
		
	}
	
	
	public boolean zauzeta(Pozicija p) throws Greska{
		if(p.getX() >= kolona || p.getY() >= vrsta || p.getX() < 0 || p.getY() < 0) throw new Greska();
		for(Figura f: figure) if(f.prostire(p)) throw new Greska();
		return false;
	}
		
	public int getVrsta() {
		return vrsta;
	}

	public void setVrsta(int vrsta) {
		this.vrsta = vrsta;
		vrste = new int[vrsta + 1];
	}

	public int getKolona() {
		return kolona;
	}

	public void setKolona(int kolona) {
		this.kolona = kolona;
		kolone = new int[kolona + 1];
	}
	
	public void drawLines(Graphics g) {
		   /* 
		    * modh, modw are used for resizing canvas to add one
		    * pixel to the first mod squares so that full width and
		    * height would be populated evenly to the last pixel
		    */
		

			//calculate x-axis coordinates of vertical lines
			squareWidth = getWidth() / kolona;
			int w1 = squareWidth + 1;
			int modw = getWidth() % kolona;
			int temp = 1;
			while(temp <= modw) kolone[temp] = temp++ * w1;
			while(temp <= kolona) kolone[temp] = kolone[temp++ - 1] + squareWidth;
			
			//calculate y-axis coordinates of horizontal lines 
			squareHeight = getHeight() / vrsta;
			int h1 = squareHeight + 1;
			int modh = getHeight() % vrsta;
			temp = 1;
			while(temp <= modh) vrste[temp] = temp++ * h1;
			while(temp <= vrsta) vrste[temp] = vrste[temp++ - 1] + squareHeight;
			
			//draw vertical lines
			for(int i = 0; i < kolone.length; i++) g.drawLine(kolone[i], 0, kolone[i], getHeight());
			g.drawLine(getWidth() - 1, 0, getWidth() - 1, getHeight());
			
			//draw horizontal lines
			for(int i = 0; i < vrste.length; i++) g.drawLine(0, vrste[i], getWidth(), vrste[i]);
			g.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1);
		}
	
	@Override
	public void paint(Graphics g) {
		g.setColor(Color.black);
		drawLines(g);
		for(Figura f: figure) {
			f.crtaj(g);
		}
		azurirajLabele();
	}

	private void azurirajLabele() {
		owner.poeni.setText("Poeni: " + poeni);
		owner.poeni.revalidate();
		owner.figura.setText("Figura: " + (figure.size() - 2));
		owner.figura.revalidate();
	}
	
}
