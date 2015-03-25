package pl.co.soltysiak.ztpj.net;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;

import pl.co.soltysiak.ztpj.net.connectivity.Client;
import pl.co.soltysiak.ztpj.net.connectivity.Server;
import pl.co.soltysiak.ztpj.net.model.ApplicationSettings;
import pl.co.soltysiak.ztpj.net.model.Employee;

public class Program {

	private Connection conn = null;
	private Statement stmt = null;
	private PreparedStatement pstmt = null;
	private String getAllEmployeeQuery = "select *  from Employee order by ID";
	private String deleteSingleEmployeeQuery = "DELETE FROM Employee WHERE Id=(?)";
	private String getSingleEmployeeQuery = "select *  from Employee where id = (?)";
	private String insertEmployeeQuery = "insert into Employee ( FirstName, LastName, Salary, Position, Phone, CreditCard, CostLimit ) values ( ?, ?, ?, ?, ?, ?, ? )";
	private Server serverInstance = null;
	private Client clientInstance = null;

	public static void main(String[] args) throws Exception {
		(new Program()).start();
	}

	private void start() throws Exception {
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			String url = ApplicationSettings.getConnectionString();
			conn = DriverManager.getConnection(url, "sa", "password");
			int menuChoice = 0;
			do {
				menuChoice = getMenuChoice();
				if (menuChoice == 1) {
					showEmployers();
				} else if (menuChoice == 2) {
					insertEmployee();
				} else if (menuChoice == 3) {
					deleteEmployee();
				} else if (menuChoice == 4) {
					clientInstance = new Client("127.0.0.1");
					Employee e = clientInstance.GetEmployeeData();
					
					PreparedStatement insertStatement = (PreparedStatement) conn
							.prepareStatement(insertEmployeeQuery);
					
					insertStatement.setString(1, e.getFirstName());
					insertStatement.setString(2, e.getLastName());
					insertStatement.setBigDecimal(3, e.getSalary());
					insertStatement.setString(4, e.getPosition());
					insertStatement.setString(5, e.getPhone());
					insertStatement.setString(6, e.getCreditCard());
					insertStatement.setBigDecimal(7, e.getCostLimit());

					insertStatement.executeUpdate();
				} else if (menuChoice == 5) {
					serverInstance = new Server();
					serverInstance.startServer();
				} else if (menuChoice == 0) {
					System.out.println("Goodbye!");
				}
			} while (menuChoice != 0);

		} catch (Exception ex) {
			System.err.println("ERROR: failed to load HSQLDB JDBC driver.");
			ex.printStackTrace();
			return;
		} finally {
			if (stmt != null) {
				stmt.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
	}

	private void deleteEmployee() throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		int employeeIdDelete = -1;
		String line = "";
		boolean badInput = false;
		do {
			System.out.print("Podaj identyfikator \t:\t");
			badInput = false;

			line = br.readLine();

			try {
				employeeIdDelete = Integer.parseInt(line);
			} catch (NumberFormatException ex) {
				badInput = true;
			}

		} while (badInput);

		PreparedStatement singleEmployeeStatement = (PreparedStatement) conn
				.prepareStatement(getSingleEmployeeQuery);

		singleEmployeeStatement.setInt(1, employeeIdDelete);
		ResultSet rs = singleEmployeeStatement.executeQuery();

		while (rs.next()) {
			Employee employee = new Employee(rs);
			System.out.println(employee.toString());
		}

		System.out.print("[Enter] - zatwierdz [Q] - porzuæ");
		boolean readChar = false;
		do {
			readChar = false;

			line = br.readLine();
			if (line.trim().equals("")) {
				line = "\n";
			}
			int keyValue = line.charAt(0);
			switch (keyValue) {
			case 10:
			case 13:
				readChar = true;
				break;
			case 81: // Q
			case 113: // q
				readChar = true;
				return;

			}
		} while (!readChar);

		PreparedStatement deleteEmployeeStatement = (PreparedStatement) conn
				.prepareStatement(deleteSingleEmployeeQuery);
		deleteEmployeeStatement.setInt(1, employeeIdDelete);
		deleteEmployeeStatement.execute();

	}

	private void insertEmployee() throws Exception {
		PreparedStatement insertStatement = (PreparedStatement) conn
				.prepareStatement(insertEmployeeQuery);

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		boolean boss = false;
		System.out.println("[D]yrektor/[H]andlowiec");
		boolean readChar = false;
		do {
			readChar = false;

			String line = br.readLine();
			if (line.trim().equals("")) {
				line = "\n";
			}
			int keyValue = line.charAt(0);
			switch (keyValue) {
			case 72:
			case 104:
				boss = false;
				readChar = true;
				break;
			case 68: // D
			case 100: // d
				boss = true;
				readChar = true;
				break;
			}
		} while (!readChar);

		String firstName = GetInputFor("First name");
		String lastName = GetInputFor("Last name");
		BigDecimal salary = GetDecimalFor("Salary");
		String phone = GetInputFor("Phone");
		String creditCard = GetInputFor("Credit card");
		BigDecimal costLimit = GetDecimalFor("Cost limit");

		insertStatement.setString(1, firstName);
		insertStatement.setString(2, lastName);
		insertStatement.setBigDecimal(3, salary);
		insertStatement.setString(4, boss ? "Dyrektor" : "Handlowiec");
		insertStatement.setString(5, phone);
		insertStatement.setString(6, creditCard);
		insertStatement.setBigDecimal(7, costLimit);

		insertStatement.executeUpdate();
	}

	private BigDecimal GetDecimalFor(String fieldName) throws Exception {

		BigDecimal bigDecimal = new BigDecimal(0);

		boolean successfulParse = true;
		do {
			successfulParse = true;
			try {

				String line = GetInputFor(fieldName);

				DecimalFormatSymbols symbols = new DecimalFormatSymbols();
				symbols.setGroupingSeparator(',');
				symbols.setDecimalSeparator('.');
				String pattern = "#,##0.0#";
				DecimalFormat decimalFormat = new DecimalFormat(pattern,
						symbols);
				decimalFormat.setParseBigDecimal(true);

				// parse the string
				bigDecimal = (BigDecimal) decimalFormat.parse(line);

			} catch (ParseException ex) {
				successfulParse = false;
			}
		} while (!successfulParse);
		return bigDecimal;
	}

	private String GetInputFor(String fieldName) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String line = "";
		boolean badInput = false;
		do {
			System.out.print(fieldName + "\t:\t");
			badInput = false;

			line = br.readLine();
			if (line.trim().equals("")) {
				badInput = true;
			}
		} while (badInput);
		return line;
	}

	private void showEmployers() throws Exception {
		stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(getAllEmployeeQuery);
		printEmployee(rs);

	}

	private int getMenuChoice() throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		clearConsole();
		System.out.println("MENU");
		System.out.println("\t 1. Lista pracownikow");
		System.out.println("\t 2. Dodaj pracownikow");
		System.out.println("\t 3. Usun pracownikow");
		System.out.println("\t 4. Wyœlij dane");
		System.out.println("\t 5. Uruchom serwer");

		System.out.println("\t 0. Wyjscie");

		boolean repeat = false;
		do {
			String line = br.readLine();
			if (line.trim().equals("")) {
				line = "\n";
			}
			int keyValue = line.charAt(0);
			repeat = false;
			switch (keyValue) {
			case 48:
				return 0;
			case 49:
			case 50:
			case 51:
			case 52:
			case 53:
			case 54:
			case 55:
				return keyValue - 48;
			default:
				repeat = true;
				System.out.println("Z³y wybór");
			}
		} while (repeat);
		throw new Exception("cos poszlo bardzo nie tak w menu, hakier?");
	}

	public final static void clearConsole() {
		try {
			final String os = System.getProperty("os.name");

			if (os.contains("Windows")) {
				Runtime.getRuntime().exec("cls");
			} else {
				Runtime.getRuntime().exec("clear");
			}
		} catch (final Exception e) {
			// Handle any exceptions.
		}
	}

	private void printEmployee(ResultSet rs) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		while (rs.next()) {
			Employee emp = new Employee(rs);
			System.out.println(emp.toString());

			System.out.println("[enter] next [q] quit");
			boolean readChar = false;
			do {
				readChar = false;

				String line = br.readLine();
				if (line.trim().equals("")) {
					line = "\n";
				}
				int keyValue = line.charAt(0);
				switch (keyValue) {
				case 10:
				case 13:
					readChar = true;
					break;
				case 81: // Q
				case 113: // q
					readChar = true;
					return;

				}
			} while (!readChar);
		}

	}
}
