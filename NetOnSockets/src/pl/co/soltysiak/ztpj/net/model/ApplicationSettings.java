package pl.co.soltysiak.ztpj.net.model;

public class ApplicationSettings {
	public static int getPort(){
		return 64777;
	}
	
	public static String getConnectionString(){
		return "jdbc:sqlserver://IO\\SQLEXPRESS:1433;databaseName=Company;integratedSecurity=true;";
	}
}
