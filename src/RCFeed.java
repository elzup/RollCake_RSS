import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import lib.Feed;

import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class RCFeed extends Feed {
	private String name;
	private String tempName;
	private Color color;

	private boolean isSimple = false;
	private boolean isRun;
	//	private String url;

	private ArrayList<RCItem> itemList;

	public RCFeed() {
		// TODO Constracter
		super();
		this.itemList = new ArrayList<RCItem>();
		this.isRun = false;
	}

	public boolean setURL(String url) {
		try {
			this.url = new URL(url);
		} catch (MalformedURLException e) {
			return false;
		}
		return true;
	}

	//------------------- getter,setter -------------------//
	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public String getTempName() {
		return this.tempName;
	}

	@Deprecated
	public void setUrl(URL url) {
		this.url = url;
	}

	public URL getUrl() {
		return this.url;
	}

	public void setColor(Color col) {
		this.color = col;
	}

	public Color getColor() {
		return this.color;
	}

	public void setSimple(boolean b) {
		this.isSimple = b;
	}

	public void setSimple() {
		this.setSimple(true);
	}

	public boolean isSimple() {
		return this.isSimple;
	}

	public ArrayList<RCItem> getRCItemList() {
		return itemList;
	}

	//------------------- getter,setter end -------------------//
	/** URLで指示されたフィードを取得し DOM tree を構築、itemList を生成 */
	@Override
	public void run() {
		if (this.isRun)
			return;
		this.isRun = true;
		this.itemList = new ArrayList<RCItem>();
		BufferedReader in = null;
		try {
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64)");
			connection.connect();
			InputStream inputStream = connection.getInputStream();
			InputStreamReader reader = new InputStreamReader(inputStream,
					encoding);
			in = new BufferedReader(reader);
		} catch (IOException e) {
			System.err.println("接続エラー: " + e);
			System.exit(1);
		}

		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			document = builder.parse(new InputSource(in));
		} catch (ParserConfigurationException e) {
			System.err.println("DocumentBuilderFactory生成エラー:" + e);
		} catch (SAXException e) {
			System.err.println("構文エラー:" + e);
		} catch (IOException e) {
			System.err.println("入出力エラー:" + e);
		}

		try {
			in.close();
		} catch (IOException e) {
			System.err.println("接続終了エラー: " + e);
		}

		try {
			// root要素 (rdf:RDF または rss または feed ) を findItems に渡す
			findItems(document.getDocumentElement());
		} catch (DOMException e) {
			System.err.println("DOMエラー:" + e);
		}
	}

	public void reRun() {
		this.reSet();
		this.run();
	}

	public void reSet() {
		this.isRun = false;
	}

	@Override
	protected void findItems(Node node) {
		for (Node current = node.getFirstChild(); current != null; current = current.getNextSibling()) {
			if (current.getNodeType() == Node.ELEMENT_NODE) {
				String nodeName = current.getNodeName();
				if ("item".equals(nodeName) || "entry".equals(nodeName)) {
					RCItem item = new RCItem(current);
					if (this.isSimple)
						item.compact();
					itemList.add(item);
				} else if ("title".equals(nodeName)) {
					this.tempName = current.getFirstChild().getNodeValue();
				}
				else
					findItems(current);
			}
		}
	}

}
