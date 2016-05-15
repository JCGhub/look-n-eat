package database;

public class TestDB{
	public static void main(String[] args){
		ConnectDB db = new ConnectDB();
		db.MySQLConnection("root", "", "lookneat_db");
		
		db.createTable("testtable");
		db.insertData("testtable", "El faro", "https://tripadvisor.es/elfaro");		
		
		db.closeConnection();
	}
}