package com.boluomi.children.util;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.boluomi.children.model.ImageInfoBean;

import android.util.Log;

/**
 * 此类主要负责对xml文档进行解析，继承DefaultHandler接口。 
 * 主要实现几个常用接口即可完成xml的解析。
 * 2012年12月6日
 * @author Bestar(刘星星)
 * 
 */
public class SaxHander extends DefaultHandler {
	private final String TAG = "SaxHander";

	private List<ImageInfoBean> images;  

	private ImageInfoBean image;

	// 用于标记上一次遍历的是哪个element，用于存储对应的文本
	private String preTag;

	/**
	 * 在开始解析xml文档时会执行此方法，在此方法中，可以做一些数据初始化的工作。
	 */
	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
//		Log.i(TAG, "startDocument::");
		images = new ArrayList<ImageInfoBean>();
	}

	/**
	 * 在开始解析一个元素的时候执行此方法，在此方法中，根据localname来判定读取的是哪个元素。
	 * 主要是用于获取元素的属性值，并且存储。
	 */
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
//		Log.i(TAG, "startElement::" + localName);
		super.startElement(uri, localName, qName, attributes);
		if ("Image".equals(localName)) {
			image = new ImageInfoBean();
			//因为person只有一个属性，可以这样去获取，若有多个时，可以采用循环的方式。
			image.setId(attributes.getValue(0));  
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
			if ("shouyaox".equals(preTag)) {
				image.setShouyaox(content);
			} else if ("shouyaoy".equals(preTag)) {
				image.setShouyaoy(content);
			} else if ("xinlangx".equals(preTag)) {
				image.setXinlangx(content);
			} else if ("xinlangy".equals(preTag)) {
				image.setXinlangy(content);
			}else if ("xinniangx".equals(preTag)) {
				image.setXinniangx(content);
			}else if ("xinniangy".equals(preTag)) {
				image.setXinniangy(content);
			}else if ("timex".equals(preTag)) {
				image.setTimex(content);
			} else if ("timey".equals(preTag)) {
				image.setTimey(content);
			} else if ("dianmingx".equals(preTag)) {
				image.setDianmingx(content);
			} else if ("dianmingy".equals(preTag)) {
				image.setDianmingy(content);
			} else if ("addressx".equals(preTag)) {
				image.setAddressx(content);
			} else if ("addressy".equals(preTag)) {
				image.setAddressy(content);
			} else if ("phonex".equals(preTag)) {
				image.setPhonex(content);
			}else if ("phoney".equals(preTag)) {
				image.setPhoney(content);
			}else if("yearx".equals(preTag)){
				image.setYearx(content);
			}else if ("yeary".equals(preTag)) {
				image.setYeary(content);
			} else if ("yuex".equals(preTag)) {
				image.setYuex(content);
			}  else if ("yuey".equals(preTag)) {
				image.setYuey(content);
			}  else if ("rix".equals(preTag)) {
				image.setRix(content);
			} else if ("riy".equals(preTag)) {
				image.setRiy(content);
			} else if ("longliyuex".equals(preTag)) {
				image.setLongliyuex(content);
			}  else if ("longliyuey".equals(preTag)) {
				image.setLongliyuey(content);
			}  else if ("longlirix".equals(preTag)) {
				image.setLonglirix(content);
			}  else if ("longliriy".equals(preTag)) {
				image.setLongliriy(content);
			}  else if ("weekx".equals(preTag)) {
				image.setWeekx(content);
			}  else if ("weeky".equals(preTag)) {
				image.setWeeky(content);
			} 
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
		if ("Image".equals(localName)) {
			images.add(image);
			image = null;
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
	public List<ImageInfoBean> getImages() {
		if (images != null) {
			return images;
		}
		return null;
	}
}
