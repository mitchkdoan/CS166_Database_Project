/*
 * Template JAVA User Interface
 * =============================
 *
 * Database Management Systems
 * Department of Computer Science &amp; Engineering
 * University of California - Riverside
 *
 * Target DBMS: 'Postgres'
 *
 */


import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * This class defines a simple embedded SQL utility class that is designed to
 * work with PostgreSQL JDBC drivers.
 *
 */
public class DBProject {

   // reference to physical database connection.
   private Connection _connection = null;

   // handling the keyboard inputs through a BufferedReader
   // This variable can be global for convenience.
   static BufferedReader in = new BufferedReader(
                                new InputStreamReader(System.in));

   /**
    * Creates a new instance of DBProject
    *
    * @param hostname the MySQL or PostgreSQL server hostname
    * @param database the name of the database
    * @param username the user name used to login to the database
    * @param password the user login password
    * @throws java.sql.SQLException when failed to make a connection.
    */
   public DBProject (String dbname, String dbport, String user, String passwd) throws SQLException {

      System.out.print("Connecting to database...");
      try{
         // constructs the connection URL
         String url = "jdbc:postgresql://localhost:" + dbport + "/" + dbname;
         System.out.println ("Connection URL: " + url + "\n");

         // obtain a physical connection
         this._connection = DriverManager.getConnection(url, user, passwd);
         System.out.println("Done");
      }catch (Exception e){
         System.err.println("Error - Unable to Connect to Database: " + e.getMessage() );
         System.out.println("Make sure you started postgres on this machine");
         System.exit(-1);
      }//end catch
   }//end DBProject

   /**
    * Method to execute an update SQL statement.  Update SQL instructions
    * includes CREATE, INSERT, UPDATE, DELETE, and DROP.
    *
    * @param sql the input SQL string
    * @throws java.sql.SQLException when update failed
    */
   public void executeUpdate (String sql) throws SQLException {
      // creates a statement object
      Statement stmt = this._connection.createStatement ();

      // issues the update instruction
      stmt.executeUpdate (sql);

      // close the instruction
      stmt.close ();
   }//end executeUpdate

   /**
    * Method to execute an input query SQL instruction (i.e. SELECT).  This
    * method issues the query to the DBMS and outputs the results to
    * standard out.
    *
    * @param query the input query string
    * @return the number of rows returned
    * @throws java.sql.SQLException when failed to execute the query
    */
   public int executeQuery (String query) throws SQLException {
      // creates a statement object
      Statement stmt = this._connection.createStatement ();

      // issues the query instruction
      ResultSet rs = stmt.executeQuery (query);

      /*
       ** obtains the metadata object for the returned result set.  The metadata
       ** contains row and column info.
       */
      ResultSetMetaData rsmd = rs.getMetaData ();
      int numCol = rsmd.getColumnCount ();
      int rowCount = 0;

      // iterates through the result set and output them to standard out.
      boolean outputHeader = true;
      while (rs.next()){
	 if(outputHeader){
	    for(int i = 1; i <= numCol; i++){
		System.out.print(rsmd.getColumnName(i) + "\t");
	    }
	    System.out.println();
	    outputHeader = false;
	 }
         for (int i=1; i<=numCol; ++i)
            System.out.print (rs.getString (i) + "\t");
         System.out.println ();
         ++rowCount;
      }//end while
      stmt.close ();
      return rowCount;
   }//end executeQuery

   /**
    * Method to close the physical connection if it is open.
    */
   public void cleanup(){
      try{
         if (this._connection != null){
            this._connection.close ();
         }//end if
      }catch (SQLException e){
         // ignored.
      }//end try
   }//end cleanup

   /**
    * The main execution method
    *
    * @param args the command line arguments this inclues the <mysql|pgsql> <login file>
    */
   public static void main (String[] args) {
      if (args.length != 3) {
         System.err.println (
            "Usage: " +
            "java [-classpath <classpath>] " +
            DBProject.class.getName () +
            " <dbname> <port> <user>");
         return;
      }//end if
      
      Greeting();
      DBProject esql = null;
      try{
         // use postgres JDBC driver.
         Class.forName ("org.postgresql.Driver").newInstance ();
         // instantiate the DBProject object and creates a physical
         // connection.
         String dbname = args[0];
         String dbport = args[1];
         String user = args[2];
         esql = new DBProject (dbname, dbport, user, "");

         boolean keepon = true;
         while(keepon) {
            // These are sample SQL statements
				System.out.println("MAIN MENU");
				System.out.println("---------");
				System.out.println("1. Add new customer");
				System.out.println("2. Add new room");
				System.out.println("3. Add new maintenance company");
				System.out.println("4. Add new repair");
				System.out.println("5. Add new Booking"); 
				System.out.println("6. Assign house cleaning staff to a room");
				System.out.println("7. Raise a repair request");
				System.out.println("8. Get number of available rooms");
				System.out.println("9. Get number of booked rooms");
				System.out.println("10. Get hotel bookings for a week");
				System.out.println("11. Get top k rooms with highest price for a date range");
				System.out.println("12. Get top k highest booking price for a customer");
				System.out.println("13. Get customer total cost occurred for a give date range"); 
				System.out.println("14. List the repairs made by maintenance company");
				System.out.println("15. Get top k maintenance companies based on repair count");
				System.out.println("16. Get number of repairs occurred per year for a given hotel room");
				System.out.println("17. < EXIT");

            switch (readChoice()){
				   case 1: addCustomer(esql); break;
				   case 2: addRoom(esql); break;
				   case 3: addMaintenanceCompany(esql); break;
				   case 4: addRepair(esql); break;
				   case 5: bookRoom(esql); break;
				   case 6: assignHouseCleaningToRoom(esql); break;
				   case 7: repairRequest(esql); break;
				   case 8: numberOfAvailableRooms(esql); break;
				   case 9: numberOfBookedRooms(esql); break;
				   case 10: listHotelRoomBookingsForAWeek(esql); break;
				   case 11: topKHighestRoomPriceForADateRange(esql); break;
				   case 12: topKHighestPriceBookingsForACustomer(esql); break;
				   case 13: totalCostForCustomer(esql); break;
				   case 14: listRepairsMade(esql); break;
				   case 15: topKMaintenanceCompany(esql); break;
				   case 16: numberOfRepairsForEachRoomPerYear(esql); break;
				   case 17: keepon = false; break;
				   default : System.out.println("Unrecognized choice!"); break;
            }//end switch
         }//end while
      }catch(Exception e) {
         System.err.println (e.getMessage ());
      }finally{
         // make sure to cleanup the created table and close the connection.
         try{
            if(esql != null) {
               System.out.print("Disconnecting from database...");
               esql.cleanup ();
               System.out.println("Done\n\nBye !");
            }//end if
         }catch (Exception e) {
            // ignored.
         }//end try
      }//end try
   }//end main
   
   public static void Greeting(){
      System.out.println(
         "\n\n*******************************************************\n" +
         "              User Interface      	               \n" +
         "*******************************************************\n");
   }//end Greeting

   /*
    * Reads the users choice given from the keyboard
    * @int
    **/
   public static int readChoice() {
      int input;
      // returns only if a correct value is given.
      do {
         System.out.print("Please make your choice: ");
         try { // read the integer, parse it and break.
            input = Integer.parseInt(in.readLine());
            break;
         }catch (Exception e) {
            System.out.println("Your input is invalid!");
            continue;
         }//end try
      }while (true);
      return input;
   }//end readChoice

   
   public static void addCustomer(DBProject esql){
    // Given customer details add the customer in the DB 
 	int customerID;
	String phNo;
	String fName;
	String lName;
	String address;
	String DOB;
	String gender;

	do{
		System.out.print("Customer ID: ");
		try{
			customerID = Integer.parseInt(in.readLine());
			break;
		}catch(Exception e) {
			System.err.println(e.getMessage());
			continue;
		}
	}while(true);


        do{
                System.out.print("First Name: ");
                try{
                        fName = in.readLine();
			if(fName.length() <= 0 || fName.length() > 30) {
				throw new RuntimeException("Invalid input: input is null or exceeds 30 characters...");
			}
                        break;
                }catch(Exception e) {
                        System.err.println(e.getMessage());
                        continue;
                }
        }while(true);



        do{
                System.out.print("Last Name: ");
                try{
                        lName = in.readLine();
			if(lName.length() <= 0 || lName.length() > 30) {
				throw new RuntimeException("Invalid input: input is null or exceeds 30 characters...");
			}
                        break;
                }catch(Exception e) {
                        System.err.println(e.getMessage());
                        continue;
                }
        }while(true);



        do{
                System.out.print("Address: ");
                try{
                        address = in.readLine();
                        break;
                }catch(Exception e) {
                        System.err.println(e.getMessage());
                        continue;
                }
        }while(true);


        do{
                System.out.print("Phone #: ");
                try{
                        phNo = in.readLine();
                        break;
                }catch(Exception e) {
                        System.err.println(e.getMessage());
                        continue;
                }
        }while(true);


        do{
                System.out.print("Date of Birth: ");
                try{
                        DOB = in.readLine();
                        break;
                }catch(Exception e) {
                        System.err.println(e.getMessage());
                        continue;
                }
        }while(true);


        do{
                System.out.print("Gender: ");
                try{
                        gender = in.readLine();
                        break;
                }catch(Exception e) {
                        System.err.println(e.getMessage());
                        continue;
                }
        }while(true);

	try {
     		String query = "INSERT INTO Customer VALUES (" + customerID + ", \'" + fName + "\', \'" + lName + "\', \'" +
				 address + "\', " + phNo + ",\'" + DOB + "\',\'" + gender + "\')";
		esql.executeQuery(query);
	}catch(Exception e) {
		System.err.println(e.getMessage());		
	}	
   }//end addCustomer

   public static void addRoom(DBProject esql){
      // Given room details add the room in the DB
      int hotelID;
      int roomNo;
      String roomType;

        do{
                System.out.print("Hotel ID: ");
                try{
                        hotelID = Integer.parseInt(in.readLine());
                        break;
                }catch(Exception e) {
                        System.err.println(e.getMessage());
                        continue;
                }
        }while(true);

        do{
                System.out.print("Room Number: ");
                try{
                        roomNo = Integer.parseInt(in.readLine());
                        break;
                }catch(Exception e) {
                        System.err.println(e.getMessage());
                        continue;
                }
        }while(true);

        do{
                System.out.print("Room Type: ");
                try{
                        roomType = in.readLine();
                        if(roomType.length() <= 0 || roomType.length() > 10) {
                                throw new RuntimeException("Invalid input: input is null or exceeds 10 characters...");
                        }
                        break;
                }catch(Exception e) {
                        System.err.println(e.getMessage());
                        continue;
                }
        }while(true);


        try {
                String query = "INSERT INTO Room VALUES (" + hotelID + ", " + roomNo + ", \'" + roomType  + "\')";
                esql.executeQuery(query);
        }catch(Exception e) {
                System.err.println(e.getMessage());
        }
   }//end addRoom

   public static void addMaintenanceCompany(DBProject esql){
        // Given maintenance Company details add the maintenance company in the DB
        int cmpID;
        String name;
        String address;
	String isCertified;

        do{
                System.out.print("Company ID: ");
                try{
                        cmpID = Integer.parseInt(in.readLine());
                        break;
                }catch(Exception e) {
                        System.err.println(e.getMessage());
                        continue;
                }
        }while(true);

        do{
                System.out.print("Name: ");
                try{
                        name = in.readLine();
			if(name.length() <= 0 || name.length() > 10) {
				throw new RuntimeException("Invalid input: input is null or exceeds 10 characters...");
			}
                        break;
                }catch(Exception e) {
                        System.err.println(e.getMessage());
                        continue;
                }
        }while(true);

        do{
                System.out.print("Address: ");
                try{
                        address = in.readLine();
                        break;
                }catch(Exception e) {
                        System.err.println(e.getMessage());
                        continue;
                }
        }while(true);

        do{
                System.out.print("Is the company certified? (TRUE/FALSE): ");
                try{
                        isCertified = in.readLine();
                        break;
                }catch(Exception e) {
                        System.err.println(e.getMessage());
                        continue;
                }
        }while(true);

        try {
                String query = "INSERT INTO MaintenanceCompany VALUES (" + cmpID + ",\'" + name + "\', \'" + address + "\'," + isCertified +")";
                esql.executeQuery(query);
        }catch(Exception e) {
                System.err.println(e.getMessage());
        }
   }//end addMaintenanceCompany

   public static void addRepair(DBProject esql){
    // Given repair details add repair in the DB
	int rID;
	int hotelID;
	int roomNo;
	int mCompany;
	String repairDate;
	String description;
	String repairType;
	 
	do{
                System.out.print("Repair ID: ");
                try{
                        rID = Integer.parseInt(in.readLine());
                        break;
                }catch(Exception e) {
                        System.err.println(e.getMessage());
                        continue;
                }
        }while(true);

        do{
                System.out.print("Hotel ID: ");
                try{
                        hotelID = Integer.parseInt(in.readLine());
                        break;
                }catch(Exception e) {
                        System.err.println(e.getMessage());
                        continue;
                }
        }while(true);

        do{
                System.out.print("Room Number: ");
                try{
                        roomNo = Integer.parseInt(in.readLine());
                        break;
                }catch(Exception e) {
                        System.err.println(e.getMessage());
                        continue;
                }
        }while(true);

        do{
                System.out.print("Company ID: ");
                try{
                        mCompany = Integer.parseInt(in.readLine());
                        break;
                }catch(Exception e) {
                        System.err.println(e.getMessage());
                        continue;
                }
        }while(true);

	do{
                System.out.print("Repair Date: ");
                try{
                        repairDate = in.readLine();
                        break;
                }catch(Exception e) {
                        System.err.println(e.getMessage());
                        continue;
                }
        }while(true);

        do{
                System.out.print("Description of repair: ");
                try{
                        description = in.readLine();
                        break;
                }catch(Exception e) {
                        System.err.println(e.getMessage());
                        continue;
                }
        }while(true);

        do{
                System.out.print("Repair Type: ");
                try{
                        repairType = in.readLine();
                        if(repairType.length() <= 0 || repairType.length() > 10) {
                                throw new RuntimeException("Invalid input: input is null or exceeds 10 characters...");
                        }
                        break;
                }catch(Exception e) {
                        System.err.println(e.getMessage());
                        continue;
                }
        }while(true);


        try {
		String query = "INSERT INTO Repair VALUES (" + rID + "," + hotelID + "," + roomNo + "," + mCompany + 
					",\'" + repairDate + "\',\'" + description + "\',\'" + repairType + "\')";
                esql.executeQuery(query);
        }catch(Exception e) {
                System.err.println(e.getMessage());
        }
   
   }//end addRepair

   public static void bookRoom(DBProject esql){
      // Given hotelID, roomNo and customer Name create a booking in the DB 
        int bID;
        int customer;
        int hotelID;
        int roomNo;
        int noOfPeople;
        int price;
        String bookingDate;
  
        do{
            System.out.print("Booking ID: ");
            try{
              bID = Integer.parseInt(in.readLine());
              break;
            }catch(Exception e) {
              System.err.println(e.getMessage());
              continue;
            }
        }while(true);
  
  
        do{
            System.out.print("Customer ID: ");
            try{
              customer = Integer.parseInt(in.readLine());
              break;
            }catch(Exception e) {
              System.err.println(e.getMessage());
              continue;
            }
        }while(true);
  
  
        do{
            System.out.print("Hotel ID: ");
            try{
              hotelID = Integer.parseInt(in.readLine());
              break;
            }catch(Exception e) {
              System.err.println(e.getMessage());
              continue;
            }
        }while(true);
  
  
        do{
            System.out.print("Room Number: ");
            try{
              roomNo = Integer.parseInt(in.readLine());
              break;
            }catch(Exception e) {
              System.err.println(e.getMessage());
              continue;
            }
        }while(true);
   
        do{
               System.out.print("Booking Date: ");
               try{
                       bookingDate = in.readLine();
                       break;
               }catch(Exception e) {
                       System.err.println(e.getMessage());
                       continue;
               }
       }while(true);
    

        do{
            System.out.print("Number of People: ");
            try{
              noOfPeople = Integer.parseInt(in.readLine());
              break;
            }catch(Exception e) {
              System.err.println(e.getMessage());
              continue;
            }
        }while(true);
  
        do{
            System.out.print("Price: ");
            try{
              price = Integer.parseInt(in.readLine());
              break;
            }catch(Exception e) {
              System.err.println(e.getMessage());
              continue;
            }
        }while(true);
  
        try {
               String query = "INSERT INTO Booking VALUES (" + bID + ", " + customer + ", " + hotelID + 
                                  ", " + roomNo + ",\'" + bookingDate + "\'," + noOfPeople + "," + price + ")";
		esql.executeQuery(query);
        }catch(Exception e) {
                System.err.println(e.getMessage());   
        } 
   }//end bookRoom

   public static void assignHouseCleaningToRoom(DBProject esql){
      // Given Staff SSN, HotelID, roomNo Assign the staff to the room 
  	int hotelID;
        int roomNo;
        int staffID;
  
        do{
                System.out.print("Hotel ID: ");
                try{
                        hotelID = Integer.parseInt(in.readLine());
                        break;
                }catch(Exception e) {
                        System.err.println(e.getMessage());
                        continue;
                }
        }while(true);

        do{
                System.out.print("Room Number: ");
                try{
                        roomNo = Integer.parseInt(in.readLine());
                        break;
                }catch(Exception e) {
                        System.err.println(e.getMessage());
                        continue;
                }
        }while(true);

        do{
                System.out.print("Staff ID: ");
                try{
                        staffID = Integer.parseInt(in.readLine());
                        break;
                }catch(Exception e) {
                        System.err.println(e.getMessage());
                        continue;
                }
        }while(true);

        try {
                String query = "UPDATE Assigned SET roomNo = " + roomNo + " WHERE hotelID = " + hotelID + " and staffID = " + staffID;
                esql.executeQuery(query);
        }catch(Exception e) {
                System.err.println(e.getMessage());
        }
   }//end assignHouseCleaningToRoom
   
   public static void repairRequest(DBProject esql){
      // Given a hotelID, Staff SSN, roomNo, repairID , date create a repair request in the DB
        int hotelID;
        int ssn;
        int roomNo;

        int reqID;
        int managerID;
        int repairID;
        String requestDate;
        String description;

          do{
                  System.out.print("Hotel ID: ");
                  try{
                        hotelID = Integer.parseInt(in.readLine());
                        break;
                  }catch(Exception e) {
                        System.err.println(e.getMessage());
                        continue;
                  }
          }while(true);

          do{
                  System.out.print("Staff SSN: ");
                  try{
                        ssn = Integer.parseInt(in.readLine());
                        break;
                  }catch(Exception e) {
                        System.err.println(e.getMessage());
                        continue;
                  }
          }while(true);

          do{
                  System.out.print("Room Number: ");
                  try{
                        roomNo = Integer.parseInt(in.readLine());
                        break;
                  }catch(Exception e) {
                        System.err.println(e.getMessage());
                        continue;
                  }
          }while(true);

          do{
                  System.out.print("Repair ID: ");
                  try{
                        repairID = Integer.parseInt(in.readLine());
                        break;
                  }catch(Exception e) {
                        System.err.println(e.getMessage());
                        continue;
                  }
          }while(true);

          do{
                  System.out.print("Request Date (YYYY-MM-DD): ");
                  try{
                        requestDate = in.readLine();
                        break;
                  }catch(Exception e) {
                        System.err.println(e.getMessage());
                        continue;
                  }
          }
          while(true);

          do{
                  System.out.print("Repair Request Description: ");
                  try{
                        description = in.readLine();
                        break;
                  }catch(Exception e) {
                        System.err.println(e.getMessage());
                        continue;
                  }
          }
          while(true);

          ResultSet rs = esql.executeQuery("SELECT h.manager FROM Hotel h WHERE h.hID = " + hotelID);
          managerID = ((Number) rs.getObject(1)).intValue();

          rs = esql.executeQuery("SELECT MAX(r.reqID) FROM Request");
          reqID = ((Number) rs.getObject(1)).intValue() + 1;

      try {
            String query = "INSERT INTO Request VALUES (" + reqID + ", " + managerID + ", " + repairID + ", \'" +
                                requestDate + "\', \'" + description + "\')";
            esql.executeQuery(query);
      }catch(Exception e) {
          System.err.println(e.getMessage());
      }
   }//end repairRequest
   
   public static void numberOfAvailableRooms(DBProject esql){
      // Given a hotelID, get the count of rooms available 
        int hotelID;
        
        do{
                System.out.print("Hotel ID: ");
                try{
                        hotelID = Integer.parseInt(in.readLine());
                        break;
                }catch(Exception e) {
                        System.err.println(e.getMessage());
                        continue;
                }
        }while(true);
        
        try {
                String query = "SELECT COUNT(*) FROM Room r WHERE r.roomNo IN (SELECT r.roomNo FROM Room r WHERE r.hotelID = " +
                                    hotelID + ") AND r.roomNo NOT IN (SELECT r.roomNo FROM Room r, Booking b WHERE r.roomNo = b.roomNo AND b.hotelID = " +
                                        hotelID + ")";
                int rowCount = esql.executeQuery(query);
                System.out.println("total row(s):" + rowCount);
	}catch(Exception e) {
                System.err.println(e.getMessage());
        }
   }//end numberOfAvailableRooms
   
   public static void numberOfBookedRooms(DBProject esql){
      // Given a hotelID, get the count of rooms booked
        int hotelID;
        
        do{
                System.out.print("Hotel ID: ");
                try{
                        hotelID = Integer.parseInt(in.readLine());
                        break;
                }catch(Exception e) {
                        System.err.println(e.getMessage());
                        continue;
                }
        }while(true);

        try {
                String query = "SELECT COUNT(*) FROM Booking b WHERE b.hotelID = " + hotelID;
                int rowCount = esql.executeQuery(query);
		System.out.println("Total row(s): " + rowCount);
        }catch(Exception e) {
                System.err.println(e.getMessage());
        }
   }//end numberOfBookedRooms
   
   public static void listHotelRoomBookingsForAWeek(DBProject esql){
	  // Given a hotelID, date - list all the rooms available for a week(including the input date) 
      // Your code goes here.
      // ...
      // ...
   }//end listHotelRoomBookingsForAWeek
   
   public static void topKHighestRoomPriceForADateRange(DBProject esql){
	  // List Top K Rooms with the highest price for a given date range
      // Your code goes here.
      // ...
      // ...
   }//end topKHighestRoomPriceForADateRange
   
   public static void topKHighestPriceBookingsForACustomer(DBProject esql){
	  // Given a customer Name, List Top K highest booking price for a customer 
      // Your code goes here.
      // ...
      // ...
   }//end topKHighestPriceBookingsForACustomer
   
   public static void totalCostForCustomer(DBProject esql){
	  // Given a hotelID, customer Name and date range get the total cost incurred by the customer
      // Your code goes here.
      // ...
      // ...
   }//end totalCostForCustomer
   
   public static void listRepairsMade(DBProject esql){
      // Given a Maintenance company name list all the repairs along with repairType, hotelID and roomNo
        String name;
        
        do{
                System.out.print("Maintenance Company Name: ");
                try{
                        name = in.readLine();
                        if(name.length() <= 0 || name.length() > 30) {
                                throw new RuntimeException("Invalid input: input is null or exceeds 30 characters...");
                        }
                        break;
                }catch(Exception e) {
                        System.err.println(e.getMessage());
                        continue;
                }
        }while(true);

        try {
                String query = "SELECT r.repairType, r.hotelID, r.roomNo FROM Repair r, MaintenanceCompany m WHERE r.mCompany = m.cmpID AND m.name = " + name;
                esql.executeQuery(query);
        }catch(Exception e) {
                System.err.println(e.getMessage());
        }
   }//end listRepairsMade
   
   public static void topKMaintenanceCompany(DBProject esql){
	  // List Top K Maintenance Company Names based on total repair count (descending order)
      // Your code goes here.
      // ...
      // ...
   }//end topKMaintenanceCompany
   
   public static void numberOfRepairsForEachRoomPerYear(DBProject esql){
	  // Given a hotelID, roomNo, get the count of repairs per year
      // Your code goes here.
      // ...
      // ...
   }//end listRepairsMade

}//end DBProject
