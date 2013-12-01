package lib;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

// XML DOM Tree を表示するプログラム
// 使い方: java DocumentTreeViewer url [encoding]
//         (encoding は指定しないと utf-8)

public class DocumentTreeViewer {

	private static String deb_url = "http://rss.rssad.jp/rss/itmnews/2.0/news_bursts.xml";

	public static void main(String[] args) {
		String encoding = "utf-8"; // 文字コードの初期値は utf-8
		String url = deb_url;
		if (args.length > 0)
			url = args[0];
		if (args.length > 1) // 第2引数があったらそれを文字コードとして指定
			encoding = args[1];
		// Document Tree を生成 (URL, 文字コードを指定)
		DocumentTree tree = new DocumentTree(url, encoding);
		// Tree を表示
		tree.show();
	}
}

// XML DOM Tree を表すクラス
class DocumentTree {
	private Document document;
	private int depth; // 階層

	public DocumentTree(String urlString, String encoding) {
		document = null;
		try {
			// まずは接続し、データを読む Reader を生成
			URL url = new URL(urlString);
			URLConnection connection = url.openConnection();
			connection.connect();
			InputStream inputStream = connection.getInputStream();
			InputStreamReader reader = new InputStreamReader(inputStream,
					encoding);
			BufferedReader in = new BufferedReader(reader);
			// Reader を使ってストリームを読み、それを解析して Document を生成
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			document = builder.parse(new InputSource(in));
			in.close();

		} catch (ParserConfigurationException e) {
			System.err.println("DocumentBuilderFactory生成エラー:" + e);
		} catch (SAXException e) {
			System.err.println("構文エラー:" + e);
		} catch (IOException e) {
			System.err.println("入出力エラー:" + e);
		}
	}

	/** DOM Tree の内容を表示 */
	public void show() {
		depth = 0;
		try {
			// root 要素を与えて showTree を呼び出す
			showTree(document.getDocumentElement());
		} catch (DOMException e) {
			System.err.println("DOMエラー:" + e);
		}
	}

	/** 引数 node 以下の tree を表示 */
	private void showTree(Node node) {
		for (Node current = node.getFirstChild(); current != null; current = current.getNextSibling()) {
			if (current.getNodeType() == Node.ELEMENT_NODE) { // ノードは要素?
				String nodeName = current.getNodeName();
				// 中括弧 { } を使って階層の深さを表現
				System.out.println(indent() + nodeName + " {");
				depth++;
				showTree(current); // さらに子ノードを見る (再帰)
				depth--;
				System.out.println(indent() + "}");
			}
			else if (current.getNodeType() == Node.TEXT_NODE // ノードはテキスト?
					&& current.getNodeValue().trim().length() != 0)
				System.out.println(indent() + current.getNodeValue());
			else if (current.getNodeType() == Node.CDATA_SECTION_NODE) // ノードはCDATA?
				System.out.println(indent() + current.getNodeValue());
			// HTMLタグなどを含む
			; // 上記以外のノードでは何もしない
		}
	}

	// 階層 depth にあわせた字下げ用の空白を生成
	private String indent() {
		String indent = "";
		for (int i = 0; i < depth; i++)
			indent += "  ";
		return indent;
	}
}
