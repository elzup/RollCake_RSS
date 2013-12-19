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
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sun.istack.internal.NotNull;
import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;
import com.sun.org.apache.xml.internal.serializer.OutputPropertiesFactory;

public class RCFiler {

	public static void saveFeedList(RCManager fm) {
		ArrayList<RCFeed> feedList = fm.getFeedList();
		ArrayList<RCGroup> groupList = fm.getGroupList();

		try {
			writeDocument(convertToDocumnet(feedList, groupList));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void loadFeedList(@NotNull RCManager fm) {
		Document document = null;
		try {
			document = readDocument();
		} catch (Exception e) {
			e.printStackTrace();
		}

		for (int i = 0; i < 5; i++)
			System.out.println(":" + document.getChildNodes().item(0).getChildNodes().item(i).getNodeName());
		NodeList feedNodes = (NodeList) document.getChildNodes().item(0).getChildNodes().item(1).getChildNodes();
		NodeList groupNodes = (NodeList) document.getChildNodes().item(0).getChildNodes().item(3).getChildNodes();

		System.out.println(feedNodes.getLength());
		System.out.println(groupNodes.getLength());

		for (int i = 0; i < feedNodes.getLength(); i++) {
			NodeList feed = feedNodes.item(i).getChildNodes();
			String name = getElementValue(feed, "name");
			System.out.println("-" + name);
			if (name == null)
				continue;
			int groupId = Integer.valueOf(getElementValue(feed, "groupId"));
			String url = getElementValue(feed, "url");
			System.out.println("name: " + name + ":" + url);
			fm.addFeed(name, url, null);
		}
		fm.runAll();

		for (int i = 0; i < groupNodes.getLength(); i++) {
			NodeList group = groupNodes.item(i).getChildNodes();
			String name = getElementValue(group, "name");
			System.out.println("-" + name);
			if (name == null)
				continue;
			int groupId = Integer.valueOf(getElementValue(group, "id"));
			System.out.println("name: " + name + ":" + groupId);
			fm.addGroup(groupId, name);
		}
	}

	private static String getElementValue(NodeList nodeList, @NotNull String tagName) {
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node n = nodeList.item(i);
			if (tagName.equals(n.getNodeName())) {
				return n.getFirstChild().getNodeValue();
			}
		}
		return null;
	}

	@SuppressWarnings("unused")
	private static Document convertToDocumnet(@NotNull ArrayList<RCFeed> feedList, @NotNull ArrayList<RCGroup> groupList)
			throws Exception {
		DOMImplementation domImpl = DocumentBuilderFactory.newInstance().newDocumentBuilder().getDOMImplementation();
		Document document = domImpl.createDocument("", "rollCake", null);
		Element rootElement = document.getDocumentElement();
		Element feedRootElement = document.createElement("feedList");
		Element groupRootElement = document.createElement("groupList");
		rootElement.appendChild(feedRootElement);
		rootElement.appendChild(groupRootElement);

		//------------------- feedList node management -------------------//
		for (RCFeed feed : feedList) {
			Element feedElement = document.createElement("feed");
			//			feedElement.setAttribute("name");
			System.out.println(feed.getTempName());
			System.out.println(feed.getName() + "::://///>>>>");
			if (feed.getName() == null)
				continue;
			feedElement.appendChild(createElementWithName(document, "name", feed.getName()));
			feedElement.appendChild(createElementWithName(document, "groupId", String.valueOf(feed.getGroupId())));
			feedElement.appendChild(createElementWithName(document, "url", feed.getUrl().toString()));
			//RC~~クラス側で生成
			feedRootElement.appendChild(feedElement);
		}



		//------------------- groupList node management -------------------//
		for (RCGroup group : groupList) {
			Element groupElement = document.createElement("group");
			//			feedElement.setAttribute("name");
			System.out.println(group.getName() + "::://///>>>>");
			if (group.getName() == null)
				continue;
			groupElement.appendChild(createElementWithName(document, "name", group.getName()));
			groupElement.appendChild(createElementWithName(document, "id", String.valueOf(group.getId())));
			//RC~~クラス側で生成
			groupRootElement.appendChild(groupElement);
		}
		return document;
	}

	private static Element createElementWithName(Document document, String tagName, String data) {
		Element e = document.createElement(tagName);
		e.appendChild(document.createTextNode(data));
		return e;
	}

	private static Document readDocument() throws Exception {
		File file = new File(RCConfig.savefile_name);
		Document documnet = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file);
		return documnet;
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
}
