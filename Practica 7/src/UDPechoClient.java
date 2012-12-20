import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Date;

public class cliente72 {
	public static void main (String args[]) throws Exception {
		// consigue los parametros
		String host = "localhost";
		if(args.length>0) host = args[0];
		InetAddress address = InetAddress.getByName(host);
		int serverPort = 1255;
		if(args.length>1) serverPort = Integer.parseInt(args[1]);
		DatagramSocket Socket = new DatagramSocket();
		// itera en un bucle infinito lanzando hebras de echo
		// hasta que se escribe un punto "." para salir
		boolean iterar = true;
		while (iterar) {
			String linea = getTeclado();
			new hiloenviar(address, serverPort, linea,Socket).start();
			new hilorecivir(address, serverPort, linea,Socket).start();
			if(linea.equals(".")) iterar = false;
		} 
	}

	/**
	 * Obtiene una string de terminal
	 */
	private static String getTeclado() {
		String linea = null;
		BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in));
		try {
			System.out.print(">>");
			linea = teclado.readLine();
			System.out.println();
		} catch (IOException e) { e.printStackTrace(); 	}
		return linea;
	}
}

/*
 * Realiza la operación de "echo". Con los parámetros que se le
 *  pasa intenta enviar y recibir una cadea.
 */
class hilorecivir extends Thread{
	private InetAddress address;
	private int port;
	private DatagramSocket ds;
	private String mensaje;
	
	public hilorecivir(InetAddress address, int port, String mensaje, DatagramSocket s2) {
		super();
		this.address	= address;
		this.port 		= port;
		this.mensaje	= mensaje;
		//try {
			this.ds  	= s2;
		//} catch (SocketException e) { e.printStackTrace(); }
	}

	@Override
	public void run() {
		
		recibeString();
		super.run();
	}

	/**
	 * Recibe una cadena de texto.
	 */
	private void recibeString() {
		byte[] buffer = new byte[1024];
		DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
		try {
			ds.receive(dp);
			System.out.println(" recibido en bytes	:" + dp.getData());
			System.out.println(" recibido en string	:" + new String(dp.getData(), 0, dp.getLength()));
		} catch (IOException ex) { 	System.err.println(ex);
		} //finally {
			//ds.close();
		//}
	}
}

/*
 * Realiza la operación de "echo". Con los parámetros que se le
 *  pasa intenta enviar y recibir una cadea.
 */
class hiloenviar extends Thread{
	private InetAddress address;
	private int port;
	private DatagramSocket ds;
	private String mensaje;
	public hiloenviar(InetAddress address, int port, String mensaje,DatagramSocket s2) {
		super();
		this.address	= address;
		this.port 		= port;
		this.mensaje	= mensaje;
		//try {
			this.ds  	= s2;
		//} catch (SocketException e) { e.printStackTrace(); }
	}

	@Override
	public void run() {
		enviaString();
		
		super.run();
	}

	/**
	 * Envia una cadena de texto.
	 */
	private void enviaString() {
		byte[] data = this.mensaje.getBytes();
		System.out.println();
		System.out.println(" enviado en bytes	:" + data);
		System.out.println(" enviado en string	:" + mensaje);

		DatagramPacket output = new DatagramPacket(data, data.length, address, port);
		try {
			ds.send(output);
		} catch (IOException e) {e.printStackTrace();}
	}
}
