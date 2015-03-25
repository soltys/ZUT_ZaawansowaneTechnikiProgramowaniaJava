package pl.co.soltysiak.ztpj.net.connectivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import pl.co.soltysiak.ztpj.net.model.ApplicationSettings;
import pl.co.soltysiak.ztpj.net.model.Employee;

public class Server {
	Thread serverThread ;

	public void startServer() {
		final ExecutorService clientProcessingPool = Executors
				.newFixedThreadPool(10);
		
		Runnable serverTask = new Runnable() {
			@Override
			public void run() {
				try {
					ServerSocket serverSocket = new ServerSocket(
							ApplicationSettings.getPort());
					System.out.println("Waiting for clients to connect...");
					while (true) {
						Socket clientSocket = serverSocket.accept();
						clientProcessingPool
								.submit(new ClientTask(clientSocket));
						if(Thread.currentThread().isInterrupted())
							break;
					}
				} catch (IOException e) {
					System.err.println("Unable to process client request");
					e.printStackTrace();
				}
			}
		};
		
		serverThread = new Thread(serverTask);
		serverThread.start();
		System.out.println("Server started!");
	}	
	

	private class ClientTask implements Runnable {
		private final Socket clientSocket;

		private ClientTask(Socket clientSocket) {
			this.clientSocket = clientSocket;
		}

		@Override
		public void run() {
			System.out.println("Got a client !");

			try {
				PrintWriter out = new PrintWriter(
						clientSocket.getOutputStream(), true);
				BufferedReader in = new BufferedReader(new InputStreamReader(
						clientSocket.getInputStream()));
				
				
				Employee e = new Employee();
				e.setFirstName("Network");
				e.setLastName("Dziembor");
				e.setSalary(new BigDecimal(10.00));
				e.setPosition("Handlowiec");
				e.setPhone("+4860000");
				e.setCreditCard("Visa");
				e.setCostLimit(new BigDecimal(7));
				
				out.println(e.toString());
				out.println("Bye.");
				
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			try {
				clientSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
