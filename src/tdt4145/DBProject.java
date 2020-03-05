
package tdt4145;

import java.sql.*;
import java.util.Properties;
import java.util.Scanner;

public class DBProject {
	
	// TODO: Define strings
	private String url;
	private String username;
	private String password;
	
    protected Connection conn;
    
    public DBProject() {
    }
    
    private void checkDriver() {
    	try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public Connection connect() {
    	// Define private variables:
    	url = "jdbc:mysql://mysql.stud.ntnu.no/henrfj_gr13?autoReconnect=true&useSSL=false";
    	username = "henrfj_film";
    	password = "gr13";
    	
        try {
            Properties p = new Properties();
            p.put("user", username);
            p.put("password", password);
            conn = DriverManager.getConnection(url, p);            
        } catch (Exception e)
        {
            throw new RuntimeException("Unable to connect", e);
        }
        return conn;
    }
    
    // __TEST_METHODS__
    private void listActors(Scanner scanner, Connection conn) {
    	try {
    		System.out.println("Test: Hent skuespillere\n----------");
    		Statement stmt = conn.createStatement();
    		String query = "select * from Person";
    		ResultSet rs = stmt.executeQuery(query);
    		while (rs.next()) {
        		System.out.println("Navn: " + rs.getString("navn") + ", født: " + rs.getString("FødselsÅr") + " i "+ rs.getString("FødselsLand"));
    		}

    		
    	}
    	catch (Exception e) {
    		System.out.println("Error med database:\n"+e);
    	}
    	
    }


    private void addActor(Scanner scanner, Connection conn) {
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
            System.out.println("db error during prepare of insert into Peron: "+e);
        }    	
    	
    }
    
    // __USE_CASES__
    // Use case 1: Finne navnet på alle rollene en gitt skuespiller har.
    private void findRoles(Scanner scanner, Connection conn) {
    	System.out.println("Finne navnet på alle rollene til en skuespiller.\n----------------");
    }
    
    // Use case 2: Finne hvilke filmer som en gitt skuespiller opptrer i.
    private void findMovies(Scanner scanner, Connection conn) {
    	System.out.println("Finn hvilke filmer skuespilleren opptrer i.\n----------------");
    }
    
    // Use case 3: Finne hvilke filmselskap som lager flest filmer inne hver sjanger (grøssere, familie, o.l.).
    private void corpWithMostInGenre(Scanner scanner, Connection conn) {
    	System.out.println("Finne hvilke filmselskap som lager flest filmer innen sjanger .\n----------------");
    }

    // Use case 4: Sette inn en ny film med regissør, manusforfattere, skuespillere og det som hører med.
    private void insertNewMovie(Scanner scanner, Connection conn) {
    	System.out.println("Sett inn en ny film.\n----------------");
    }

    // Use case 5: Sette inn ny anmeldelse av en episode av en serie.
    private void insertReview(Scanner scanner, Connection conn) {
    	System.out.println("Finne navnet på alle rollene til en skuespiller.\n----------------");
    }
    
    

    public static void main(String[] args) {
		DBProject dbproject = new DBProject();
		dbproject.checkDriver();
		
		// Spawn connection and scanner object
		Connection con = dbproject.connect();
		Scanner scanner = new Scanner(System.in);

		// main
		boolean cont = true;
		System.out.println("Velkommen til programmet vårt");
		System.out.println("Velg funksjon:\n"
				+ " 1:\tFinne navnet på alle roller til en skuespiller\n"
				+ " 2:\tFinne hvilke filmer en skuespiller opptrer i\n"
				+ " 3:\tFinne hvilke filmselskap som har flest filmer innen en sjanger\n"
				+ " 4:\tSette inn en ny film\n"
				+ " 5:\tSette inn annmeldelse.\n"
				+ " 6 eller 7 for test-funksjoner \n"
				+ "Eller 0 for å avslutte");
		while (cont) {
			System.out.println("Meny");
			System.out.print(">>");
			int valg = -1; // force default in switch if input not correct.
			try {
				valg = scanner.nextInt();
			}catch(Exception e) {
				scanner.nextLine(); // Flush incorrect input
				System.out.println("OBS! Må være heltall 0-7");
			}

			switch (valg) {
				case 0:
					cont = false;
					break;
				case 1:
					dbproject.findRoles(scanner, con);
					break;
				case 2:
					dbproject.findMovies(scanner, con);
					break;
				case 3:
					dbproject.corpWithMostInGenre(scanner, con);
					break;
				case 4:
					dbproject.insertNewMovie(scanner, con);
					break;
				case 5:
					dbproject.insertReview(scanner, con);
					break;
				case 6:
					dbproject.listActors(scanner, con);
					break;
				case 7:
					dbproject.addActor(scanner, con);
					break;
				default:
					System.out.println("Ikke gyldig valg");
					break;
			}
		}
		System.out.println("Program avsluttet.");
	}
}

