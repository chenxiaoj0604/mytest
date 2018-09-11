package com.mytest.utils;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.security.AnyTypePermission;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import javax.annotation.processing.FilerException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Qiang
 * @date 2018/6/28 12:36
 * @description XML解析工具类
 */
public class XmlUtils {

    /**
     * xml文档Document转对象
     *
     * @param document
     * @param clazz
     * @return
     */
    public static Object getObject(Document document, Class<?> clazz, String root) {
        Object obj = null;
        //获取根节点
        Element rootElt = document.getRootElement();
        Element element = rootElt.element(root);
        try {
            //创建对象
            obj = clazz.newInstance();
            List<Element> properties = element.elements();
            for (Element pro : properties) {
                //获取属性名(首字母大写)
                String propertyname = pro.getName();
                propertyname = (new StringBuilder()).append(Character.toUpperCase(propertyname.charAt(0))).append(propertyname.substring(1)).toString();
                String propertyvalue = pro.getText();
                Method m = obj.getClass().getMethod("set" + propertyname, String.class);
                m.invoke(obj, propertyvalue);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }

    /**
     * xml字符串转对象
     *
     * @param xmlString
     * @param clazz
     * @return
     */
    public static Object getObject(String xmlString, Class<?> clazz, String root) throws FilerException {
        Document document = null;
        try {
            document = DocumentHelper.parseText(xmlString);
        } catch (DocumentException e) {
            throw new FilerException("获取Document异常" + xmlString);
        }
        return getObject(document, clazz, root);
    }

    /**
     * xml字符串转map
     *
     * @param xmlStr
     * @param root
     * @return
     * @throws DocumentException
     */
    public static Map<String, Object> xmlStr2Map(String xmlStr, String root) {
        if (StringUtils.isEmpty(xmlStr)) {
            return null;
        }
        Map<String, Object> map = new HashMap<>();
        Document document = null;
        try {
            document = DocumentHelper.parseText(xmlStr);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        Element rootElement = document.getRootElement();
        Element parentElement = rootElement.element(root);
        List<Element> elements = parentElement.elements();
        if (elements != null && elements.size() > 0) {
            for (Element element : elements) {
                map.put(element.getName(), element.getTextTrim());
            }
        }
        return map;
    }

    public static Map<String, String> xmlStr2Map(String xmlStr) {
        if (StringUtils.isEmpty(xmlStr)) {
            return Collections.emptyMap();
        }
        if (StringUtils.isNotEmpty(xmlStr)){
            xmlStr = xmlStr.replaceAll("\\s+", " ");
            xmlStr = xmlStr.trim();
        }
        Map<String, String> map = new HashMap<>();
        Document document = null;
        try {
            document = DocumentHelper.parseText(xmlStr);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        Element rootElement = document.getRootElement();
        List<Element> elements = rootElement.elements();
        if (elements.size() > 0){
           elements.forEach(element -> {
                if (element != null){
                    List<Element> childElements =  element.elements();
                    if (childElements != null && childElements.size() > 0){
                        for (Element childElement : childElements){
                            if (childElement != null) {
                                map.put(childElement.getName(), childElement.getTextTrim());
                            }
                        }
                    }
                }
           });
        }

        return map;
    }


    /**
     * 对象转换为xml字符串
     * @param object
     * @return
     */
    public static String object2XmlStr(Object object) throws JAXBException {
        //创建输出流
        StringWriter stringWriter = new StringWriter();
        try {
            //获得转换的上下文对象
            JAXBContext jaxbContext = JAXBContext.newInstance(object.getClass());
            //获得Marshaller对象
            Marshaller marshaller = jaxbContext.createMarshaller();
            //格式化xml输出的格式
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,
                    Boolean.TRUE);
            //对为null的字段进行监听
            marshaller.setListener(new MarshallerListener());
            //将对象转换成输出流形式的xml
            marshaller.marshal(object, stringWriter);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return stringWriter.toString();
    }


    private static XStream getXStreamInstance(){
        XStream xStream = new XStream(new DomDriver("UTF-8"));
        // 设置默认的安全校验
        XStream.setupDefaultSecurity(xStream);
        // 使用本地的类加载器
        xStream.setClassLoader(XmlUtils.class.getClassLoader());
        // 允许所有的类进行转换
        xStream.addPermission(AnyTypePermission.ANY);
        return xStream;
    }


    /**
     * xml字符串转Object
     * @param xmlStr
     * @param cls
     * @param <T>
     * @return
     */
    public static <T> T xml2Object(String xmlStr,Class<T> cls){
        if (StringUtils.isNotEmpty(xmlStr)){
            xmlStr = xmlStr.replaceAll("\\s+", " ");
            xmlStr = xmlStr.trim();
        }
        XStream xStream = getXStreamInstance();
        xStream.processAnnotations(cls);
        return (T)xStream.fromXML(xmlStr);
    }


}
