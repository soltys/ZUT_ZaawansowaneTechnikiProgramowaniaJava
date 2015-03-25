package pl.co.soltysiak.ztpj.net.connectivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import pl.co.soltysiak.ztpj.net.model.ApplicationSettings;
import pl.co.soltysiak.ztpj.net.model.Employee;

public class Client {
	private String hostName;
	private int portNumber;

	public Client(String hostName) {
		this.hostName = hostName;
		this.portNumber = ApplicationSettings.getPort();
	}

	public Employee GetEmployeeData() throws IOException {
		Employee returnedEmployee = null;
		try (Socket clientSocket = new Socket(hostName, portNumber);
				PrintWriter out = new PrintWriter(clientSocket.getOutputStream(),
						true);
				BufferedReader in = new BufferedReader(new InputStreamReader(
						clientSocket.getInputStream()));) {
			BufferedReader stdIn = new BufferedReader(new InputStreamReader(
					System.in));
			String fromServer;
			String fromUser;
			
				
			while ((fromServer = in.readLine()) != null) {
				System.out.println("Server: " + fromServer);
				if (fromServer.equals("Bye."))
					break;
				
				return Employee.parseEmployee(fromServer);
			}
		} catch (UnknownHostException e) {
			System.err.println("Don't know about host " + hostName);
			System.exit(1);
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection to "
					+ hostName);
			System.exit(1);
		}
		return null;
	}
}
