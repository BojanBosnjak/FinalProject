package Ketpa;

import java.io.*;

import java.sql.*;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;
import static java.util.Map.Entry.*;

public class Knjiga {
	static List<String> knjiga = new ArrayList<String>();
	static List<String> jedinstveneReci = new ArrayList<String>();
	static HashMap<String, Integer> reciUKnjizi = new HashMap<String, Integer>();

	public static void getReci() throws Exception {
		String linija = null;
		// ovde sam prebacio reci iz knjige u listu knjiga
		FileReader fr = new
		FileReader("C:\\Users\\User\\Desktop\\mamatvoja\\knjiga"); 
		// "src/Fajl/knjiga" ubuntu
		//FileReader fr = new FileReader("Fajl/knjiga");
		// obratiti paznju na ubuntu - linux
		BufferedReader br = new BufferedReader(fr);

		while ((linija = br.readLine()) != null) {
			// izbacuje sve karaktere koji mogu da prave problem \\s+ ako ima 2 razmaka, ili
			// novi red. Splituje pa splituje
			String[] reci = linija.replaceAll("[^a-zA-Z ]", "").split("\\s+");
			for (int i = 0; i < reci.length; i++) {
				if (!reci[i].equals(null) && !reci[i].equals("") && !reci[i].equals(" ")
						&& !reci[i].contentEquals("\n")) {
					knjiga.add(reci[i].toLowerCase());
				}
			}
		}
		br.close();
	}

	public static void poredjenje() {
		for (String reci : knjiga) {
			if (!Recnik.mapaRecnik.containsKey(reci)) {
				jedinstveneReci.add(reci);
			}
		}
		// ovde ih leksikografski sortiramo
		Collections.sort(jedinstveneReci);
	}

	public HashMap<String, Integer> mapaKnjiga() throws Exception {
		String line;
		FileReader fr = new FileReader("C:\\Users\\User\\Desktop\\mamatvoja\\knjiga");
		BufferedReader br = new BufferedReader(fr);

		while ((line = br.readLine()) != null) {
			String string[] = line.toLowerCase().split(" ");
			for (String s : string) {
				// proveravamo da li rec vec postoji
				if (reciUKnjizi.containsKey(s)) {
					int counter = reciUKnjizi.get(s);
					reciUKnjizi.put(s, ++counter);
					// uvecavamo brojac za svaku rec koja se ponavlja
				} else

					reciUKnjizi.put(s, 1);
			}
		}
		return reciUKnjizi;
	}

	public void ponavljanjeBr() throws Exception {
		String linija, rec = "";
		int brojac = 0, maxBr = 0;
		ArrayList<String> reci = new ArrayList<String>();

		// Opens file in read mode
		FileReader file = new FileReader("C:\\Users\\User\\Desktop\\mamatvoja\\knjiga");
		BufferedReader br = new BufferedReader(file);

		// Reads each line
		while ((linija = br.readLine()) != null) {
			String string[] = linija.toLowerCase().split(" ");
			// Adding all words generated in previous step into words
			for (String s : string) {
				reci.add(s);
			}
		}

		// Determine the most repeated word in a file
		for (int i = 0; i < reci.size(); i++) {
			brojac = 1;
			// Count each word in the file and store it in variable count
			for (int j = i + 1; j < reci.size(); j++) {
				if (reci.get(i).equals(reci.get(j))) {
					brojac++;
				}
			}
			// If maxCount is less than count then store value of count in maxCount
			// and corresponding word to variable word
			if (brojac > maxBr) {
				maxBr = brojac;
				rec = reci.get(i);
			}
		}

		System.out.println("Rec koja se najvise puta ponavlja je: " + rec + "\nPonavlja se: " + maxBr + " puta.");
		br.close();
	}

	private Map<String, Boolean> poredjenje(Map<String, Integer> prva, Map<String, Integer> druga) {
		return prva.entrySet().stream()
				.collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue().equals(druga.get(e.getKey()))));
	}

	public void dvadeset() {
		for (String s : knjiga) {
			// iteriram kroz reci iz knjige
			if (reciUKnjizi.containsKey(s)) {
				reciUKnjizi.put(s, reciUKnjizi.get(s) + 1);
				// ako rec postoji, zapamti je. Ako postoji vise puta zabeleziti broj
			} else {
				reciUKnjizi.put(s, 1);
				// u suprotnom, zabelezi da se nalazi samo 1
			}
		}
		ArrayList<String> rezultat = new ArrayList<String>();
		// napravili smo novu listu
		List<Entry<String, Integer>> list = new ArrayList<>(reciUKnjizi.entrySet());
		// hashmap smo pretvorili u listu i dodali komparator u obrnutom redosledu jer
		// standardno ide od 0 do n
		list.sort(Entry.comparingByValue(Comparator.reverseOrder()));

		for (int i = 0; i < 20; i++) {
			// rezultat.add(list.get(i).getKey());
			System.out.println(list.get(i).getKey() + " " + list.get(i).getValue());
			// iteriramo kroz novu listu i vracamo vrednosti u konzoli
		}

	}

	public void reciFajl() throws Exception {
		List<String> jedinstveneReci = new ArrayList<String>();
		Collections.sort(jedinstveneReci);
		FileWriter writer = new FileWriter("SavTekst.txt");
		for (String str : jedinstveneReci) {
			str = str.replace("'", "");
			str = str.replace(",", "");
			str = str.replace(".", "");
			str = str.replace("-", "");
			writer.write(str + System.lineSeparator());
		}
		writer.close();
	}
}