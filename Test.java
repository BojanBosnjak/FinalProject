package Ketpa;

import java.util.HashMap;

public class Test {

	public static void main(String[] args) {
		Recnik r = new Recnik("jdbc:sqlite:C:\\Users\\User\\Desktop\\mamatvoja\\Dictionary.db");//win
		//Recnik r =  new Recnik("jdbc:sqlite:Fajl/Dictionary.db"); //ubuntu
		Knjiga knj = new Knjiga();
		
		try {
			knj.getReci();
		} catch (Exception e) {
			e.printStackTrace();
		}
		r.connect();
		r.recnikUHM();
		
		knj.poredjenje();
		try {
			knj.reciFajl();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			r.reciFajl();
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		knj.dvadeset();
		podudaranje(r, knj);
		
		r.disconnect();

	
	}
	public static void podudaranje(Recnik r, Knjiga k) {
		for(String recRecnik : r.reciRecnik) {
			int brojac =0;
			for(String recKnjiga: k.knjiga) {
				if(recKnjiga.equals(recRecnik)) {
					brojac++;
				}
			}
			if (brojac!=0) {
				System.out.println(recRecnik +": "+ brojac);
			}
			
		}
	}
}