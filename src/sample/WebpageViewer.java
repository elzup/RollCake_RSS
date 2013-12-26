package sample;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class WebpageViewer {
	public static void main(String[] args) { // args[0]: URL
		try {
			URL url = new URL("http://twitter.com/seiun_net/status/407371042592526336");
			//	    URL url = new URL(args[0]);
			Webpage page = new Webpage(url);
			page.run();
		} catch (MalformedURLException e) {
			System.err.println("Wrong URL: " + args[0]);
		}
	}
}

class Webpage { // implements Runnable
	private URL url;

	Webpage(URL url) {
		this.url = url;
	}

	public void run() {
		try {
			URLConnection urlConnection = url.openConnection();
			urlConnection.connect();
			BufferedReader fin = new BufferedReader(
					new InputStreamReader(
							urlConnection.getInputStream(), "utf-8"));
			String line;
			while ((line = fin.readLine()) != null)
				System.out.println(line);
		} catch (IOException e) {
			System.err.println("I/O Error: " + e.toString());
		}
	}
}
