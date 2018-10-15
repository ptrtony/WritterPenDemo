package com.hedi.update;

import java.io.InputStream;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author coolszy
 * @date 2012-4-26
 * @blog http://blog.92coding.com
 */
public class ParseXmlService {
	public HashMap<String, String> parseXml(InputStream inStream)
			throws Exception {
		
		
		HashMap<String, String> hashMap = new HashMap<String, String>();

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		DocumentBuilder builder = factory.newDocumentBuilder();

		Document document = builder.parse(inStream);
		
		Element root = document.getDocumentElement();
		
		NodeList childNodes = root.getChildNodes();
		NodeList list = document.getElementsByTagName("android");
		 for(int i = 0; i < list.getLength(); i++)  
	        {  
			 Element element = (Element)list.item(i);  
			 hashMap.put("version",(element.getElementsByTagName("version").item(0).getFirstChild().getNodeValue()));
			 hashMap.put("appName",(element.getElementsByTagName("appName").item(0).getFirstChild().getNodeValue()));
			 hashMap.put("url",(element.getElementsByTagName("url").item(0).getFirstChild().getNodeValue()));
			 hashMap.put("updateinfo",(element.getElementsByTagName("updateinfo").item(0).getFirstChild().getNodeValue()));
			 hashMap.put("forceupdate",(element.getElementsByTagName("forceupdate").item(0).getFirstChild().getNodeValue()));
	        }
		for (int j = 0; j < childNodes.getLength(); j++) {

			Node childNode = (Node) childNodes.item(j);
			if (childNode.getNodeType() == Node.ELEMENT_NODE) {
				Element childElement = (Element) childNode;

//				if ("android".equals(childElement.getNodeName())) {
//					if("version".equals(childElement.getNodeName())){
//						hashMap.put("version", childElement.getNodeValue());
//					}
//					
//				}

//				else if (("appName".equals(childElement.getNodeName()))) {
//					hashMap.put("appName", childElement.getFirstChild()
//							.getNodeValue());
//				} else if (("url".equals(childElement.getNodeName()))) {
//					hashMap.put("url", childElement.getFirstChild()
//							.getNodeValue());
//				} else if (("updateinfo".equals(childElement.getNodeName()))) {
//					hashMap.put("updateinfo", childElement.getFirstChild()
//							.getNodeValue());
//				} else if (("forceupdate".equals(childElement.getNodeName()))) {
//					hashMap.put("forceupdate", childElement.getFirstChild()
//							.getNodeValue());
		//		} else 
					if (("appointcarnum".equals(childElement.getNodeName()))) {
					hashMap.put("appointcarnum", childElement.getFirstChild()
							.getNodeValue());
				}else if (("appointinfo".equals(childElement.getNodeName()))) {
					hashMap.put("appointinfo", childElement.getFirstChild()
							.getNodeValue());
				}else if (("parkingmonthinfo".equals(childElement.getNodeName()))) {
					hashMap.put("parkingmonthinfo", childElement.getFirstChild()
							.getNodeValue());
				}else if (("checkmember".equals(childElement.getNodeName()))) {
					hashMap.put("checkmember", childElement.getFirstChild()
							.getNodeValue());
				}
				else if (("headImg".equals(childElement.getNodeName()))) {
					hashMap.put("headImg", childElement.getFirstChild()
							.getNodeValue());
				}
			}
		}
		return hashMap;
	}
}
