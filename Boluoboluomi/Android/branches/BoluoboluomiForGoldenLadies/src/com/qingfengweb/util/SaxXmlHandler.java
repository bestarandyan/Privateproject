/**
 * 
 */
package com.qingfengweb.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.qingfengweb.model.ImageInfoBean;

/**
 * @author 刘星星
 * @createDate 2013、12、19
 * 
 */
public class SaxXmlHandler extends DefaultHandler {
	private final String TAG = "SaxHander";
	private List<Object> list;  
	public List<Map<String,Object>> infoList;
	// 用于标记上一次遍历的是哪个element，用于存储对应的文本
	private String preTag;
	/**
	 * 在开始解析xml文档时会执行此方法，在此方法中，可以做一些数据初始化的工作。
	 */
	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
		list = new ArrayList<Object>();
		infoList = new ArrayList<Map<String,Object>>();
	}

	/**
	 * 在开始解析一个元素的时候执行此方法，在此方法中，根据localname来判定读取的是哪个元素。
	 * 主要是用于获取元素的属性值，并且存储。
	 */
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
		if (localName.equals("template")) {
			list.add(attributes.getValue(0));
		}else if(localName.equals("field")){
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("name", attributes.getValue(0));
			map.put("x", attributes.getValue(1));
			map.put("y", attributes.getValue(2));
			map.put("width", attributes.getValue(3));
			map.put("height", attributes.getValue(4));
			map.put("font-size", attributes.getValue(5));
			map.put("type", attributes.getValue(6));
			infoList.add(map);
		}
		// 这一步很关键，否则将会导致读取文本值的错误。
		preTag = localName;  
	}

	/**
	 * 在开始解析元素文本值的时候调用此方法，在此方法中，要根据上一次解析的元素来存储文本。
	 * 空白文本也是可以被读取的，为了不被存储，必须进行和空比较。
	 */
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		super.characters(ch, start, length);
		// 获取文本内容
		String content = new String(ch, start, length).trim();  
//		Log.i(TAG, "characters::" + content);
		if (!"".equals(content.trim())) {  // 屏蔽空白文本
			
		}
	}
	
	/**
	 * 解析元素结束时调用此接口，当是实体是要添加进实体集合。
	 */
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		super.endElement(uri, localName, qName);
//		Log.i(TAG, "endElement::");
		if ("template".equals(localName)) {
			list.add(infoList);
		}

	}

	/**
	 * 结束文档解析时调用此接口。
	 */
	@Override
	public void endDocument() throws SAXException {
		super.endDocument();
//		Log.i(TAG, "endDocument::");
	}

	/**
	 * 获取解析得到的实体集合。
	 * @return
	 */
	public List<Object> getImages() {
		if (list != null) {
			return list;
		}
		return null;
	}
}
