/**
 * 
 */
package com.qingfengweb.util;

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
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.content.Context;
/**
 * @author 刘星星
 * @createDate 2013、10、12
 *
 */
public class DomAnalysisXml {
	/**  
     * 参数fileName：为xml文档路径
     * 获取百宝箱中的图例
     */
    public static List<Map<String,Object>> getTreasureImgFromXml(Context context,String fileName){
        List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
        DocumentBuilderFactory factory=null;
        DocumentBuilder builder=null;
        Document document=null;
        InputStream inputStream=null;
        //首先找到xml文件
        factory=DocumentBuilderFactory.newInstance();
        try {
            //找到xml，并加载文档
            builder=factory.newDocumentBuilder();
            inputStream=context.getResources().getAssets().open(fileName);
            document=builder.parse(inputStream);
            //找到根Element
             Element root=document.getDocumentElement();
             NodeList nodes=root.getElementsByTagName("icon");
            //遍历根节点所有子节点,rivers 下所有river
             Map<String,Object> map=new HashMap<String, Object>();
             for(int i=0;i<nodes.getLength();i++){
                     //获取river元素节点
                     Element riverElement=(Element)(nodes.item(i));
                     //获取river中name属性值
                     map.put(riverElement.getAttribute("id"), riverElement.getAttribute("url"));
//                     river.setId(riverElement.getAttribute("id"));
//                     river.setUrl(riverElement.getAttribute("url"));
                     //获取river下introduction标签
//                     Element introduction=(Element)riverElement.getElementsByTagName(INTRODUCTION).item(0);
//                     river.setIntroduction(introduction.getFirstChild().getNodeValue());
//                     Element imageUrl=(Element)riverElement.getElementsByTagName(IMAGEURL).item(0);
//                     river.setImageurl(imageUrl.getFirstChild().getNodeValue());
             }
             list.add(map);
        }catch (IOException e){
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
         catch (ParserConfigurationException e) {
            e.printStackTrace();
        }finally{
            try {
                inputStream.close();
            } catch (IOException e) {   
                e.printStackTrace();
            }
        }
        return list;
    }
}
