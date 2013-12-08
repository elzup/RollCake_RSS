import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;
import com.sun.org.apache.xml.internal.serializer.OutputPropertiesFactory;

public class RCFiler {

	public static void outputFeedList(ArrayList<RCFeed> feedList) {
		try {
			writeDocument(convertToDocumnet(feedList));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static Document convertToDocumnet(ArrayList<RCFeed> feedList) throws Exception {
		DOMImplementation domImpl = DocumentBuilderFactory.newInstance().newDocumentBuilder().getDOMImplementation();
		Document document = domImpl.createDocument("", "feedList", null);

		Element rootElement = document.getDocumentElement();

		for (RCFeed feed : feedList) {
			Element feedElement = document.createElement("feed");
//			feedElement.setAttribute("name");
			if (feed.getName() == null) continue;
			feedElement.appendChild(createElementWithName(document, "name", feed.getName()));
			feedElement.appendChild(createElementWithName(document, "groupId", String.valueOf(feed.getGroupId())));
			feedElement.appendChild(createElementWithName(document, "url", feed.getUrl().getPath()));
			//Element confElement = document.createElement("config");
			//confElement.appendChild(createElementWithName(document, "isSimple", (feed.isSimple() ? "1" : "0")));
			//feedElement.appendChild(confElement);
			//RC~~クラス側で生成
			rootElement.appendChild(feedElement);
		}

		// ルート要素を追加する        
		//		Element rootElement = document.createElement("items");
		//		rootElement.appendChild(createElementWithName(document, "tagName", "データ"));
		//		document.appendChild(rootElement);

		return document;
	}

	private static Element createElementWithName(Document document, String tagName, String data) {
		Element e = document.createElement(tagName);
		e.appendChild(document.createTextNode(data));
		//		e.setAttribute("id", id);
		return e;
	}

	private static void writeDocument(Document document) throws Exception {
		File f = new File(RCConfig.savefile_name);
		FileOutputStream fos = new FileOutputStream(f);
		StreamResult result = new StreamResult(fos);
		TransformerFactory tff = TransformerFactoryImpl.newInstance();
		tff.setAttribute(TransformerFactoryImpl.INDENT_NUMBER, 2);

		Transformer tf = tff.newInstance().newTransformer();
		tf.setOutputProperty(OutputKeys.ENCODING, RCConfig.savefile_encoding);
		tf.setOutputProperty(OutputKeys.INDENT, "yes");
		tf.setOutputProperty(OutputPropertiesFactory.S_KEY_INDENT_AMOUNT, "2");

		DOMSource source = new DOMSource(document);

		tf.transform(source, result);
		fos.close();
	}

	public ArrayList<RCFeed> inputFeedList() {
		ArrayList<RCFeed> feedList = new ArrayList<RCFeed>();

		return feedList;
	}
}
