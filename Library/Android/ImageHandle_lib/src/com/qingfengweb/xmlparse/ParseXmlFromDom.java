/**
 * 
 */
package com.qingfengweb.xmlparse;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * @author ������
 * @createDate 2013��12��20
 * xml����֮dom����
 *
 */
public class ParseXmlFromDom {
	/**
	    * ��������������ʽ��xml�ļ�
	    * @param ��template background="images/background.jpg"��
	    * @param ��field name="receiver" x="468" y="44" width="204" height="26" font-size="16" type="text" /��
		* @param  ��field name="marry_date_year" x = "484" y="115" width="85" height="26" font-size="14"  type="date" format="yyyy" /��
		* @param ��/template>
	    * @param is ������
	    * @param r ���ڵ�����
	    * @param n �ӽڵ�����
	    * @author ������
	    * @return �������Լ���  keyΪ�������� valueΪ����ֵ
	    */
	    public static List<Map<String,Object>> parseXml(InputStream is,String r,String n) {  
	    	List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		    	try {  
			    	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  
			        DocumentBuilder builder = factory.newDocumentBuilder();  
			        Document document = builder.parse(is);  
			        // ��ȡ���ڵ�  
			        Element root = document.getDocumentElement();  
			        String rootName = root.getNodeName();
			        if(rootName.equals(r)){//���ڵ�
			        	Map<String,Object> map = new HashMap<String, Object>();
			        	NamedNodeMap map1 = root.getAttributes();
			        	Node node = map1.item(0);
			        	map.put(node.getNodeName(), node.getNodeValue());
			        	list.add(map);
			        }
			        NodeList nodelist = root.getChildNodes();  
			        int size = nodelist.getLength();  
			        for (int i = 0; i < size; i++) {  
			            // ��ȡ�ض�λ�õ�node  
			            Node element2 = (Node) nodelist.item(i);  
			            String tagName = element2.getNodeName();  
			           if (tagName.equals(n)  && element2.getNodeType() == Document.ELEMENT_NODE) {  
			            	NamedNodeMap map = element2.getAttributes();
			            	Map<String,Object> nodeMap = new HashMap<String, Object>();
			            	int noteSize = map.getLength();
			            	for(int j=0;j<noteSize;j++){
			            		Node node = map.item(j);
			            		nodeMap.put(node.getNodeName(), node.getNodeValue());
			            	}
			            	list.add(nodeMap);
			            }  
			        }  
		    	 } catch (IOException e) {  
		             e.printStackTrace();  
		         } catch (ParserConfigurationException e) {
		 			// TODO Auto-generated catch block
		 			e.printStackTrace();
		 		} catch (SAXException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    return list;
	    }  
}
