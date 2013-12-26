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

import com.sun.istack.internal.NotNull;
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
		for (int i = 0; i < groupNodes.getLength(); i++) {
			NodeList group = groupNodes.item(i).getChildNodes();
			String name = getElementValue(group, "name");
			System.out.println("-" + name);
			if (name == null)
				continue;
			int groupId = Integer.valueOf(getElementValue(group, "id"));
			System.out.println("name: " + name + ":" + groupId);
			RCGroup gorup = new RCGroup(groupId, name);

			NodeList feedNodes = (NodeList) groupNodes.item(1).getChildNodes();
			for (int j = 0; j < feedNodes.getLength(); j++) {
				NodeList feed = feedNodes.item(j).getChildNodes();
				String feedName = getElementValue(feed, "name");
				System.out.println("-" + name);
				if (feedName == null)
					continue;
				String url = getElementValue(feed, "url");
				System.out.println("name: " + name + ":" + url);
				manager.addFeed(feedName, url);
			}
			manager.addGroup(groupId, name);
		}
	}

	private String getElementValue(NodeList nodeList, @NotNull String tagName) {
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node n = nodeList.item(i);
			if (tagName.equals(n.getNodeName())) {
				return n.getFirstChild().getNodeValue();
			}
		}
		return null;
	}

	@SuppressWarnings("unused")
	private Document getDocument()
			throws Exception {
		DOMImplementation domImpl = DocumentBuilderFactory.newInstance().newDocumentBuilder().getDOMImplementation();
		Document document = domImpl.createDocument("", "rollCake", null);
		Element rootElement = document.getDocumentElement();
		Element groupRootElement = document.createElement("groupList");
		rootElement.appendChild(groupRootElement);

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
				System.out.println(feed.getTempName());
				System.out.println(feed.getName() + "::://///>>>>");
				if (feed.getName() == null)
					continue;
				feedElement.appendChild(createElementWithName(document, "name", feed.getName()));
				feedElement.appendChild(createElementWithName(document, "url", feed.getUrl().toString()));

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
