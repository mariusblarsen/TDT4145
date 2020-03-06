
package tdt4145;

import java.sql.*;
import java.util.Properties;
import java.util.Scanner;

public class DBProject {
	
	private String url;
	private String username;
	private String password;
	
    protected Connection conn;
    
    public DBProject() {
    	DBProject.checkDriver();
    }
    public static void main(String[] args) {
		DBProject dbproject = new DBProject();
		
		// Spawn connection and scanner object
		Connection con = dbproject.connect();
		Scanner scanner = new Scanner(System.in);

		// main logic
		boolean cont = true;
		System.out.println("Velkommen til programmet vårt");
		System.out.println("Velg funksjon:\n"
				+ " 1:\tFinne navnet på alle roller til en skuespiller\n"
				+ " 2:\tFinne hvilke filmer en skuespiller opptrer i\n"
				+ " 3:\tFinne hvilke filmselskap som har flest filmer innen en sjanger\n"
				+ " 4:\tSette inn en ny film\n"
				+ " 5:\tSette inn annmeldelse.\n"
				+ "Eller 0 for å avslutte");
		while (cont) {
			System.out.println(".\n.");
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
					DBUtils.findRoles(scanner, con);
					break;
				case 2:
					DBUtils.findMovies(scanner, con);
					break;
				case 3:
					DBUtils.mostMovies(scanner, con);
					break;
				case 4:
					DBUtils.insertNewMovie(scanner, con);
					break;
				case 5:
					DBUtils.insertReview(scanner, con);
					break;
				default:
					System.out.println("Ikke gyldig valg");
					break;
			}
		}
		System.out.println("Program avsluttet.");
	}
    
    private static void checkDriver() {
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
    
    
    
    
    
    

    
}
