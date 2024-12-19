package tetris;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.TextEvent;
import java.awt.event.TextListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
public class Tetris extends Frame {
	
	private static int br = 0;
	private Tabla tabla;
	private static final int w = 10, h = 20;
	TextField t1 = new TextField(2), t2 = new TextField(2);
	Label poeni = new Label("Poeni: "), figura = new Label("Figura: ");
	Button dugme = new Button("Pokreni");

	public Tetris(){
		tabla = new Tabla(w, h);
		tabla.owner = this;
		setBounds(700, 200, 700, 500);
		populateWindow();
		addListeners();
		setVisible(true);
		
	}

	private void addListeners() {
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				tabla.prekini();
				dispose();
			}
		});	
		
		dugme.addActionListener((ae) -> {
			switch(dugme.getLabel()) {
			case "Pokreni":
				t1.setEnabled(false);
				t2.setEnabled(false);
				br++;
				double d = Math.random();
				double f = Math.random();
				Pozicija p = new Pozicija(tabla.getKolona() / 2, 0);
				Pozicija p1 = new Pozicija(tabla.getKolona() / 2, 1);
				Color c1 , c2;
				if(d > 0.75) c1 = Color.YELLOW;
				else if(d > 0.5) c1 = Color.BLUE;
				else if(d > 0.25) c1 = Color.GREEN;
				else c1 = Color.RED;
				if(f > 0.75) c2 = Color.YELLOW;
				else if(f > 0.5) c2 = Color.BLUE;
				else if(f > 0.25) c2 = Color.GREEN;
				else c2 = Color.RED;
				Figura prip;
				Figura pok;
				if(f > 0.5) {
					pok = new Jednodelni(p1, c1, tabla);
				}
				else {
					pok = new Dvodelni(p1,c1, tabla);
				}
				if(d > 0.5) {
					prip = new Jednodelni(p, c2, tabla);
				}
				else {
					prip = new Dvodelni(p, c2, tabla);
				}
				int t = Integer.parseInt(t1.getText());
				int k = Integer.parseInt(t2.getText());
				tabla.dodaj(pok);
				tabla.dodaj(prip);
				tabla.pokretna = pok;
				tabla.pripravna = prip;
				tabla.setVrsta(t);
				tabla.setKolona(k);
				tabla.pokreni();
				tabla.kreni();
				tabla.requestFocus();
				dugme.setLabel("Stani");
				break;
			case "Kreni":
				tabla.kreni();
				dugme.setLabel("Stani");
				tabla.requestFocus();
				break;
			case "Stani":
				tabla.zaustavi();
				dugme.setLabel("Kreni");
				break;			
			}
		});
		
		//listeners for TextFields
		t1.addTextListener(new TextListener() {
			@Override
			public void textValueChanged(TextEvent e) {
				//remove non digits characters
				String t = t1.getText();
				t = t.replaceAll("[^0-9]", "");
				
				//remove leading zeros
				int temp = 0;
				while(temp < t.length() && t.charAt(temp) == '0') temp++;
				String text = t.substring(temp);
				
				//set rows for table and draw it again
				t1.setText(text);
				int vrsta = text.equals("") ? 20 : Integer.parseInt(text);
				if(vrsta > 100) {
					vrsta = 100;
					t1.setText("100");
				}
				tabla.setVrsta(vrsta);
				tabla.repaint();
			}
		});
		t2.addTextListener(new TextListener() {
			@Override
			public void textValueChanged(TextEvent e) {
				//remove non digits characters
				String t = t2.getText();
				t = t.replaceAll("[^0-9]", "");
				
				//remove leading zeros
				int temp = 0;
				while(temp < t.length() && t.charAt(temp) == '0') temp++;
				String text = t.substring(temp);
				
				//set columns for table and draw it again
				t2.setText(text);
				int kolona = text.equals("") ? 20 : Integer.parseInt(t2.getText());
				if(kolona > 100) {
					kolona = 100;
					t2.setText("100");
				}
				tabla.setKolona(kolona);
				tabla.repaint();
			}
		});

	}

	private void populateWindow() {
		add(tabla, BorderLayout.CENTER);
		t1.setText("10");
		t2.setText("20");
		dodajJug();
	}
	
	private void dodajJug() {
		Panel pan = new Panel(new GridLayout(2, 2));
		Panel p = new Panel();
		poeni.setAlignment(Label.CENTER);
		p.add(poeni);
		pan.add(p);
		pan.add(dugme);
		figura.setAlignment(Label.CENTER);
		Panel pe = new Panel();
		pe.add(figura);
		pan.add(pe);
		Panel pane = new Panel();
		pane.add(new Label("vrsta, kolona: "));
		pane.add(t1);
		pane.add(t2);
		pan.add(pane);
		add(pan, BorderLayout.SOUTH);
	}

	public static void main(String[] args) {
		new Tetris();
	}

}
