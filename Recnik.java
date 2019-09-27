package Ketpa;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

public class Recnik {
	// stavio sam string - string jer trazim rec i njen opis
	static HashMap<String, String> mapaRecnik = new HashMap<String, String>();
	Set<String> reciRecnik = new HashSet<String>();
	String connectionString;
	Connection con;

	// moze i bez konstruktora jer imam samo jedan recnik, nemam ih vise
	public Recnik(String connStr) {
		connectionString = connStr;
	}

	public void connect() {
		try {
			con = DriverManager.getConnection(connectionString);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void disconnect() {
		try {
			if (con != null && !con.isClosed()) {
				con.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public String napraviTabelu() {
		// trebace mi tabela za nove reci
		// to sto sam stavio samo jedno ime za kolonu je jer ne planiram da unosim vise
		// kolona
		Scanner sc = new Scanner(System.in);
		System.out.println("Unesite ime tabele: ");
		String imeTabele = sc.next();
		System.out.println("Unesite ime kolone: ");
		String imeKolone = sc.next();
		try {
			String upit = "CREATE TABLE " + imeTabele + "(" + imeKolone + " TEXT);";
			Statement stm = con.createStatement();
			stm.executeUpdate(upit);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "";
	}

	public String insertInto() {
		// metoda za unos podataka u tabelu
		Scanner sc = new Scanner(System.in);
		System.out.println("Unesite ime tabele: ");
		String imeTabele = sc.next();
		System.out.println("Unesite ime kolone: ");
		String imeKolone = sc.next();
		System.out.println("Unesite vrednost: ");
		String vrednost = sc.next();
		try {
			String upit = "INSERT INTO " + imeTabele + "(" + imeKolone + ") VALUES(" + vrednost + ");";
			Statement stm = con.createStatement();
			stm.executeUpdate(upit);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "";
	}

	public String obrisiTabelu() {
		// posto sam u medjuvremenu pravio test tabele, napravio sam ovu da ih brisem
		Scanner sc = new Scanner(System.in);
		System.out.println("Unesite ime tabele za brisanje: ");
		String imeTabele = sc.next();
		try {
			String upit = "DROP TABLE " + imeTabele;
			Statement stm = con.createStatement();
			stm.executeUpdate(upit);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "";
	}

	public void recnikUHM() {
		// ovde sam ubacio reci iz baze Recnik i definicije HASHMAP
		try {
			String upit = "SELECT word, definition FROM entries";
			Statement stm = con.createStatement();
			ResultSet eres = stm.executeQuery(upit);

			while (eres.next()) {
				String rec = eres.getString("word");
				String opis = eres.getString("definition");

				mapaRecnik.put(rec.toLowerCase(), opis);
				reciRecnik.add(rec.toLowerCase());
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static boolean recPostoji(String rec) {
		// ovo ce trebati za poredjenje recnika i knjige
		if (mapaRecnik.containsKey(rec))
			return true;
		else
			return false;
	}

	public void listaUDBA() throws Exception {
		List<String> noveReci = Knjiga.jedinstveneReci.stream().distinct().collect(Collectors.toList());
		String upit = null;
		Scanner sc = new Scanner(System.in);
		System.out.println("Unesite naziv tabele koju zelite da proverite: ");
		String tabela = sc.next();

		upit = "SELECT name FROM sqlite_master WHERE type='table'AND name = '" + tabela + "'";
		Statement st = con.createStatement();
		ResultSet eres = st.executeQuery(upit);

		if (eres.next() == true) {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < noveReci.size(); i++) {
				if (i == noveReci.size() - 1) {
					sb.append("('" + noveReci.get(i) + "');");
					continue;
				} else {
					sb.append("('" + noveReci.get(i) + "')\n");
				}
			}
			System.out.println("Unesite ime tabele: ");
			tabela = sc.next();
			System.out.println("Unesite ime kolone: ");
			String kolona = sc.next();
			upit = "INSERT INTO " + tabela + " " + kolona + " VALUES " + sb.toString();
			st.executeUpdate(upit);
		} else {
			upit = napraviTabelu();
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < noveReci.size(); i++) {
				if (i == noveReci.size() - 1) {
					sb.append("('" + noveReci.get(i) + "');");
					continue;
				} else {
					sb.append("('" + noveReci.get(i) + "'),\n");
				}
				upit = insertInto() + sb.toString();
				st.executeUpdate(upit);
			}
		}

	}

	public void listauDB() throws Exception {
		List<String> noveReci = Knjiga.jedinstveneReci.stream().distinct().collect(Collectors.toList());
		String upit = null;

		upit = "SELECT name FROM sqlite_master WHERE type='table' AND name='Reci'";
		Statement st = con.createStatement();
		ResultSet eres = st.executeQuery(upit);
		if (eres.next() == true) {
			// ubacivalje u tabelu
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < noveReci.size(); i++) {
				if (i == noveReci.size() - 1) {
					sb.append("('" + noveReci.get(i) + "');");
					continue;
				} else {
					sb.append("('" + noveReci.get(i) + "'),\n");
				}
			}
			upit = "INSERT INTO Reci (Rec) VALUES " + sb.toString();
			st.executeUpdate(upit);

		} else {
			// create table
			upit = "CREATE TABLE Reci (\n" + "	Rec	TEXT UNIQUE,\n" + "	PRIMARY KEY(Rec)\n" + ");";
			st.executeUpdate(upit);
			// and then insert into table
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < noveReci.size(); i++) {
				if (i == noveReci.size() - 1) {
					sb.append("('" + noveReci.get(i) + "');");
					continue;
				} else {
					sb.append("('" + noveReci.get(i) + "'),\n");
				}
			}
			upit = "INSERT INTO Reci (Rec) VALUES " + sb.toString();
			st.executeUpdate(upit);

		}

	}

	public void reciFajl() throws Exception {

		List<String> r = new ArrayList<>(mapaRecnik.keySet());
		Collections.sort(r);
		FileWriter writer = new FileWriter("SavTekst.txt");
		for (String str : r) {
			str = str.replace("'", "");
			str = str.replace(",", "");
			str = str.replace(".", "");
			str = str.replace("-", "");
			writer.write(str + System.lineSeparator());

		}
		writer.close();
	}

}