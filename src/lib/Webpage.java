package lib;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class Webpage { // implements Runnable
	private URL url;

	Webpage(URL url) {
		this.url = url;
	}

	public void run() {
		try {
			URLConnection urlConnection = url.openConnection();
			urlConnection.connect();
			BufferedReader fin = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "utf-8"));
			String line;
			while ((line = fin.readLine()) != null)
				System.out.println(line);
		} catch (IOException e) {
			System.err.println("I/O Error: " + e.toString());
		}
	}
}
