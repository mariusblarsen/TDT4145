package tdt4145;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class DBUtils {
	// __USE_CASES__
    // Use case 1: Finne navnet p� alle rollene en gitt skuespiller har.
    public static void findRoles(Scanner scanner, Connection conn) {
    	System.out.println("Finne navnet p� alle rollene til en skuespiller.\n----------------");
    	scanner.nextLine(); // flush enter-keypress
    	System.out.print("Navn p� skuespiller>>");
    	String skuespiller = scanner.nextLine();
    	try {
    		Statement stmt = conn.createStatement();
    		String query = "FINN NAVN P� ROLLENE TIL EN SKUESPILLER"; // TODO: Skriv query
    		ResultSet rs = stmt.executeQuery(query);
    		// Check for match:
    		if (rs.next()==false) {
    			System.out.println("Fant ikke skuespiller med navn " + skuespiller + "\nMente du:");
    			printClosestMatch(scanner, stmt, rs, "navn", skuespiller);
    		}
    		else {
    			// Print result to user:
        		while (rs.next()) {
            		System.out.println("Rolle: " + rs.getString("Rolle"));
        		}
    		}
    	}
    	catch (Exception e) {
    		System.out.println("Error med database:\n"+e);
    	}
    }
    
    // Use case 2: Finne hvilke filmer som en gitt skuespiller opptrer i.
    // TODO: Query
    // TODO: Print result
    // TODO: Copy logic from above if no actor is found.
    public static void findMovies(Scanner scanner, Connection conn) {
    	System.out.println("Finn hvilke filmer skuespilleren opptrer i.\n----------------");
    	// Get input from user:
    	scanner.nextLine(); // flush enter-keypress
    	System.out.print("Navn p� skuespiller>>");
    	String skuespiller = scanner.nextLine();
    	
    	try {
    		Statement stmt = conn.createStatement();
    		// TODO:
    		String query = "select * from Person where Navn='"+skuespiller+"';";
    		ResultSet rs = stmt.executeQuery(query);
        	// Check for match:
        	if (rs.next() == false) { // OBS! This will lose first row from rs if not empty, handled later.
        		System.out.println("Fant ikke skuespiller med navn " + skuespiller + ", mente du:");
        		printClosestMatch(scanner, stmt, rs, "navn", skuespiller);
        	}
        	else {
        		rs.beforeFirst(); // Reset cursor to not lose data from if(rs.next==false)
        		while(rs.next()) {
        			// TODO: Print result
        			System.out.println("Actor was found");        			
        		}
        	}
    	}
    	catch (Exception e) {
    		System.out.println("Error med database:\n"+e);
    	}
    	
    }
    
    // Use case 3: Finne hvilke filmselskap som lager flest filmer inne hver sjanger (gr�ssere, familie, o.l.).
    public static void mostMovies(Scanner scanner, Connection conn) {
    	System.out.println("Finne hvilke filmselskap som lager flest filmer innen sjanger .\n----------------");
    	// Get user input:
    	scanner.nextLine(); // flush enter-keypress
    	System.out.print("Navn p� sjanger>>");
    	String genre = scanner.nextLine();
    	
    	try {
    		// Statement and query:
    		Statement stmt = conn.createStatement();
    		// TODO: Query
    		String query = "FINNE HVILKET SELSKAP SOM HAR FLEST FILMER;";
    		ResultSet rs = stmt.executeQuery(query);
        	// Check for match:
        	if (rs.next() == false) { // OBS! This will lose first row from rs if not empty, handled later.
        		System.out.println("Fant ikke sjangeren " + genre + ", mente du:");
        		printClosestMatch(scanner, stmt, rs, "Navn", genre);
        	}
        	else {
        		rs.beforeFirst(); // Reset cursor to not lose data from if(rs.next==false)
        		System.out.println("Selskap(ene) med flest filmer innen "+genre+":");
        		while(rs.next()) {
        			// TODO: Print result
        			System.out.println(rs.getString("URL"));
        		}
        	}
    	}
    	catch (Exception e) {
    		System.out.println("Error med database:\n"+e);
    	}
    }

    // Use case 4: Sette inn en ny film med regiss�r, manusforfattere, skuespillere og det som h�rer med.
    public static void insertNewMovie(Scanner scanner, Connection conn) {
    	System.out.println("Sett inn en ny film.\n----------------");
    	// TODO: get from user
    	Integer selskapsID;
    	String title;
    	Integer len;
    	Integer releaseYear;
    	String releaseDate;
    	String story;
    	String query = "INSERT INTO MedieProdukt (SelskapsID, Tittel, Lengde, Utgivelses�r, LanseringsDato, Storyling) VALUES ( '"+selskapsID+"', '"+title+"', '"+len+"', '"+releaseYear+"', '"+releaseDate+"', '"+story+"');";
    	
    }

    // Use case 5: Sette inn ny anmeldelse av en episode av en serie.
    public static void insertReview(Scanner scanner, Connection conn) {
    	System.out.println("Finne navnet p� alle rollene til en skuespiller.\n----------------");
    }
    
	// __TEST_METHODS__
    public static void listActors(Scanner scanner, Connection conn) {
    	try {
    		System.out.println("Test: Hent skuespillere\n----------");
    		Statement stmt = conn.createStatement();
    		String query = "select * from Person";
    		ResultSet rs = stmt.executeQuery(query);
    		while (rs.next()) {
        		System.out.println("Navn: " + rs.getString("navn") + ", f�dt: " + rs.getString("F�dsels�r") + " i "+ rs.getString("F�dselsLand"));
    		}
    		
    	}
    	catch (Exception e) {
    		System.out.println("Error med database:\n"+e);
    	}
    	
    }


    public static void addActor(Scanner scanner, Connection conn) {
    	System.out.println("Legg til skuespiller \n -------");
    	System.out.println("Vennligst oppgi navn, f�dsels�r og f�dselsland. ");
    	
    	scanner.nextLine(); // Flush enter-whitespace
    	
    	System.out.println("Navn: ");
   		String navn = scanner.nextLine();

    	System.out.println("F�dsels�r: ");
    	Integer faar  = scanner.nextInt();

    	System.out.println("F�dselsland: ");
    	String fland = scanner.next(); 

    	

    	// Send to SQL:
    	try {
    		String query = "INSERT INTO Person (Navn, F�dsels�r, F�dselsLand) VALUES ( '"+navn+"', '"+faar+"', '"+fland+"');";
            PreparedStatement stmt= conn.prepareStatement(query); 
            stmt.execute();
        	System.out.println("Skuespiller \n Navn: " + navn + 
        			"\n F�dsels�r: " + faar +
        			"\n F�dselsland: " + fland +
        			"\nLagt til i DB");
        	
        } catch (Exception e) { 
            System.out.println("db error during prepare of insert into Peron: "+e);
        }    	
    	
    }
 
    /**
     * @param scanner: For print to console
     * @param conn: For SQL
     * @param rs: ResultSet to check
     * @param column: The column we are looking at
     * @param input: Input from user
     */
    private static void printClosestMatch(Scanner scanner, Statement stmt, ResultSet rs, String column, String input) {
    	String firstLetter = String.valueOf(input.charAt(0));
		String query = "FINN NAVN P� ROLLER MED SAMME FORBOKSTAV"; // TODO:
		try {
			ResultSet columnName = stmt.executeQuery(query);
			while (rs.next()) {
	    		System.out.println(columnName.getString(column));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
    }

}
