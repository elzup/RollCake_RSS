import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;

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

import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;
import com.sun.org.apache.xml.internal.serializer.OutputPropertiesFactory;

public class RCFiler {

	private String filename;
	private RCManager manager;

	public RCFiler(String filename, RCManager manager) {
		this.filename = filename;
		this.manager = manager;
	}

	public void saveFeedList() {
		try {
			writeDocument(getDocument());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void loadFeedList() {
		Document document = null;
		try {
			document = readDocument();
		} catch (Exception e) {
			e.printStackTrace();
		}

		NodeList groupNodes = (NodeList) document.getChildNodes().item(0).getChildNodes().item(1).getChildNodes();
		NodeList crawlNodes = (NodeList) document.getChildNodes().item(0).getChildNodes().item(3).getChildNodes();
		for (int i = 0; i < crawlNodes.getLength(); i++) {
			NodeList log = crawlNodes.item(i).getChildNodes();
			String text = getElementValue(log, "text");
			System.out.println(text);
			if (text == null)
				continue;
			manager.addCrawledHistory(text);
		}
		for (int i = 0; i < groupNodes.getLength(); i++) {
			NodeList group = groupNodes.item(i).getChildNodes();
			String name = getElementValue(group, "name");
			if (name == null)
				continue;
			//			System.out.println("-" + name);
			int groupId = Integer.valueOf(getElementValue(group, "id"));
			//			System.out.println("name: " + name + ":" + groupId);
			manager.addGroup(groupId, name);

			for (int j = 0; j < group.getLength(); j++)
				group.item(j).getChildNodes().getLength();

			NodeList feedNodes = (NodeList) getElement(group, "feedList");
			for (int j = 0; j < feedNodes.getLength(); j++) {
				NodeList feed = feedNodes.item(j).getChildNodes();
				String feedName = getElementValue(feed, "name");
				if (feedName == null)
					continue;
				String url = getElementValue(feed, "url");
				String color = getElementValue(feed, "color");
				//				System.out.println("name: " + name + ":" + url);
				manager.addFeed(feedName, url, groupId, new Color(new Integer(color)));
			}
		}
		manager.runAll();
	}

	private Node getElement(NodeList nodeList, String tagName) {
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node n = nodeList.item(i);
			if (tagName.equals(n.getNodeName())) {
				return n;
			}
		}
		return null;
	}

	private String getElementValue(NodeList nodeList, String tagName) {
		Node n = getElement(nodeList, tagName);
		if (n == null)
			return null;
		return n.getFirstChild().getNodeValue();
	}

	private Document getDocument() throws Exception {
		DOMImplementation domImpl = DocumentBuilderFactory.newInstance().newDocumentBuilder().getDOMImplementation();
		Document document = domImpl.createDocument("", "rollCake", null);
		Element rootElement = document.getDocumentElement();
		Element groupRootElement = document.createElement("groupList");
		Element crawlRootElement = document.createElement("crawlLogList");
		rootElement.appendChild(groupRootElement);
		rootElement.appendChild(crawlRootElement);

		for (String text: this.manager.getCrawledHistory()) {
			//			feedElement.setAttribute("name");
			Element logElement = document.createElement("log");
			if ("".equals(text))
				continue;
			logElement.appendChild(createElementWithName(document, "text", text));
			crawlRootElement.appendChild(logElement);
		}
		for (RCGroup group : this.manager.getGroupList()) {
			Element groupElement = document.createElement("group");
			//			feedElement.setAttribute("name");
			if (group.getName() == null)
				continue;
			groupElement.appendChild(createElementWithName(document, "name", group.getName()));
			groupElement.appendChild(createElementWithName(document, "id", String.valueOf(group.getId())));

			Element feedRootElement = document.createElement("feedList");
			for (RCFeed feed : group.getFeedList()) {
				Element feedElement = document.createElement("feed");
				//			feedElement.setAttribute("name");
				if (feed.getName() == null)
					continue;
				feedElement.appendChild(createElementWithName(document, "name", feed.getName()));
				feedElement.appendChild(createElementWithName(document, "url", feed.getUrl().toString()));
				feedElement.appendChild(createElementWithName(document, "color", "" + feed.getColor().getRGB()));

				feedRootElement.appendChild(feedElement);
			}
			groupElement.appendChild(feedRootElement);

			//RC~~クラス側で生成
			groupRootElement.appendChild(groupElement);
		}
		//------------------- feedList node management -------------------//
		return document;
	}

	private Element createElementWithName(Document document, String tagName, String data) {
		Element e = document.createElement(tagName);
		e.appendChild(document.createTextNode(data));
		return e;
	}

	private Document readDocument() throws Exception {
		File file = new File(filename);
		Document documnet = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file);
		return documnet;
	}

	private void writeDocument(Document document) throws Exception {
		File f = new File(filename);
		FileOutputStream fos = new FileOutputStream(f);
		StreamResult result = new StreamResult(fos);
		TransformerFactory tff = TransformerFactoryImpl.newInstance();
		tff.setAttribute(TransformerFactoryImpl.INDENT_NUMBER, 2);
		@SuppressWarnings("static-access")
		Transformer tf = tff.newInstance().newTransformer();
		tf.setOutputProperty(OutputKeys.ENCODING, RCConfig.savefile_encoding);
		tf.setOutputProperty(OutputKeys.INDENT, "yes");
		tf.setOutputProperty(OutputPropertiesFactory.S_KEY_INDENT_AMOUNT, "2");
		DOMSource source = new DOMSource(document);
		tf.transform(source, result);
		fos.close();
	}
}
