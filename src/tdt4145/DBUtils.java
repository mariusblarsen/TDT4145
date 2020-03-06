package tdt4145;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class DBUtils {
	// __USE_CASES__
    // Use case 1: Finne navnet på alle rollene en gitt skuespiller har.
    public static void findRoles(Scanner scanner, Connection conn) {
    	System.out.println("Finne navnet på alle rollene til en skuespiller.\n----------------");
    	scanner.nextLine(); // flush enter-keypress
    	System.out.print("Navn på skuespiller>>");
    	String skuespiller = scanner.nextLine();
    	try {
    		Statement stmt = conn.createStatement();
    		String query = "SELECT DISTINCT Rolle FROM Skuespiller NATURAL JOIN Person WHERE Person.Navn = '"+skuespiller+"';"; 
    		ResultSet rs = stmt.executeQuery(query);
    		// Check for match:
    		if (rs.next()==false) {
    			System.out.println("Fant ikke skuespiller med navn " + skuespiller + "\nMente du:");
    			printClosestMatch(scanner, stmt, rs, "navn", skuespiller);
    		}
    		else {
    			// Print result to user:
    			rs.beforeFirst();
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
    	System.out.print("Navn på skuespiller>>");
    	String skuespiller = scanner.nextLine();
    	
    	try {
    		Statement stmt = conn.createStatement();
    		// TODO:
    		String query = "SELECT DISTINCT MedieProdukt.Tittel FROM (MedieProdukt INNER JOIN Film ON Film.FilmID = MedieProdukt.MedieID) NATURAL JOIN (Skuespiller INNER JOIN Person ON Person.PersonID = Skuespiller.PersonID) WHERE Person.Navn = '"+skuespiller+"' ;";
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
        			System.out.println("Tittel: " + rs.getString("Tittel"));
        		}
        	}
    	}
    	catch (Exception e) {
    		System.out.println("Error med database:\n"+e);
    	}
    	
    }
    
    // Use case 3: Finne hvilke filmselskap som lager flest filmer inne hver sjanger (grøssere, familie, o.l.).
    public static void mostMovies(Scanner scanner, Connection conn) {
    	System.out.println("Finne hvilke filmselskap som lager flest filmer innen sjanger .\n----------------");
    	// Get user input:
    	scanner.nextLine(); // flush enter-keypress
    	System.out.print("Navn på sjanger>>");
    	String genre = scanner.nextLine();
    	
    	try {
    		// Statement and query:
    		Statement stmt = conn.createStatement();
    		// TODO: Query
    		String query = "Select Sjanger.Navn, UtgivelsesSelskap.URL, Count(*) as ProductsInGenere From UtgivelsesSelskap natural join (MedieProdukt inner join Film on Film.FilmID = MedieProdukt.MedieID) natural join (MedieSjanger natural join Sjanger) Where Sjanger.Navn = '"+genre+"' Group by SjangerID, SelskapsID Order by ProductsInGenere DESC;";
    		ResultSet rs = stmt.executeQuery(query);
        	// Check for match:
        	if (rs.next() == false) { // OBS! This will lose first row from rs if not empty, handled later.
        		System.out.println("Fant ikke sjangeren " + genre + ", mente du:");
        		printClosestMatch(scanner, stmt, rs, "Navn", genre);
        	}
        	else {
        		rs.beforeFirst(); // Reset cursor to not lose data from if(rs.next==false)
        		System.out.println("Selskap(ene) med flest filmer innen "+genre+":");
        		rs.next();
        		System.out.println(rs.getString("URL"));
        	}
    	}
    	catch (Exception e) {
    		System.out.println("Error med database:\n"+e);
    	}
    }

    // Use case 4: Sette inn en ny film med regissør, manusforfattere, skuespillere og det som hører med.
    public static void insertNewMovie(Scanner scanner, Connection conn) {
    	System.out.println("Sett inn en ny film.\n----------------");
    	// TODO: get from user
    	System.out.println("SelskapsID: ");
    	Integer selskapsID = scanner.nextInt();
    	System.out.println("Tittel: ");
    	String title = scanner.nextLine();
    	System.out.println("Length in minutes: ");
    	Integer len = scanner.nextInt();
    	System.out.println("Release Year: ");
    	Integer releaseYear = scanner.nextInt();
    	System.out.println("Release Date: ");
    	String releaseDate = scanner.nextLine();
    	System.out.println("story: ");
    	String story = scanner.nextLine();
    	String query = "INSERT INTO MedieProdukt (SelskapsID, Tittel, Lengde, UtgivelsesÅr, LanseringsDato, Storyline) VALUES ( '"+selskapsID+"', '"+title+"', '"+len+"', '"+releaseYear+"', '"+releaseDate+"', '"+story+"');";
    	
    	try {
    		// Statement and query:
    		PreparedStatement stmt = conn.prepareStatement(query); 
            stmt.execute();
    	}
    	catch (Exception e) {
    		System.out.println("Error med database:\n"+e);
    	}
    	
    	listActors(scanner, conn);
    	
    	System.err.println("Skriv inn personer som deltok i filmproduksjonen"
    			+ "Skriv inn ID på personen."
    			+ "For å avslutte, skriv '-1'");
    	
    	
    	
    	Integer input = scanner.nextInt();
    	try {
			Statement stmt = conn.createStatement();
			String query2 = "select * from Person";
			ResultSet rs = stmt.executeQuery(query2);
			List<Integer> nameList = new ArrayList<Integer>();
			while (rs.next()) {
				nameList.add(rs.getInt("PersonID"));
			}
			
			
			while (input >= 0) {
	    		if (Arrays.asList(nameList).contains(input) ){
	    		}
	    		else {
	    			System.out.println("Your person was not in the system. Please add your person");
	    			addActor(scanner,conn);
	    		}
	    		try {
	    			String query3 = "INSERT INTO Skuespiller VALUES('"+input+"');"
	    			PreparedStatement stmt = conn.prepareStatement(query3); 
	                stmt.execute();
	    			
	    		}
	    		catch (Exception e) {
	    			
	    		}
			} 
    	}
		catch (Exception e) {
	    	System.out.println("Error med database:\n"+e);
	    }
    	
    	
    }

    

    // Use case 5: Sette inn ny anmeldelse av en episode av en serie.
    public static void insertReview(Scanner scanner, Connection conn) {
    	System.out.println("Finne navnet på alle rollene til en skuespiller.\n----------------");
    }
    
	// __TEST_METHODS__
    public static void listActors(Scanner scanner, Connection conn) {
    	try {
    		System.out.println("Test: Hent skuespillere\n----------");
    		Statement stmt = conn.createStatement();
    		String query = "select * from Person";
    		ResultSet rs = stmt.executeQuery(query);
    		while (rs.next()) {
        		System.out.println("ID: " + rs.getInt("PersonID") + ", Navn: " + rs.getString("navn") + ", født: " + rs.getString("FødselsÅr") + " i "+ rs.getString("FødselsLand"));
    		}
    		
    	}
    	catch (Exception e) {
    		System.out.println("Error med database:\n"+e);
    	}
    	
    }


    public static void addActor(Scanner scanner, Connection conn) {
    	System.out.println("Legg til skuespiller \n -------");
    	System.out.println("Vennligst oppgi navn, fødselsår og fødselsland. ");
    	
    	scanner.nextLine(); // Flush enter-whitespace
    	
    	System.out.println("Navn: ");
   		String navn = scanner.nextLine();

    	System.out.println("Fødselsår: ");
    	Integer faar  = scanner.nextInt();

    	System.out.println("Fødselsland: ");
    	String fland = scanner.next(); 

    	

    	// Send to SQL:
    	try {
    		String query = "INSERT INTO Person (Navn, FødselsÅr, FødselsLand) VALUES ( '"+navn+"', '"+faar+"', '"+fland+"');";
            PreparedStatement stmt= conn.prepareStatement(query); 
            stmt.execute();
        	System.out.println("Skuespiller \n Navn: " + navn + 
        			"\n Fødselsår: " + faar +
        			"\n Fødselsland: " + fland +
        			"\nLagt til i DB");
        	
        } catch (Exception e) { 
            System.out.println("db error during prepare of insert into Person: "+e);
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
		String query = "FINN NAVN PÅ ROLLER MED SAMME FORBOKSTAV"; // TODO:
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
