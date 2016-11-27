package Aopproxy;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;




public class XMLBeanFactory extends AbstractBeanFactory{
	
	private String xmlPath;
	
	public XMLBeanFactory(Resource resource)
	{
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dbBuilder = dbFactory.newDocumentBuilder();
			Document document = dbBuilder.parse(resource.getInputStream());
            NodeList beanList = document.getElementsByTagName("bean");
            for(int i = 0 ; i < beanList.getLength(); i++)
            {
            	Node bean = beanList.item(i);
            	BeanDefinition beandef = new BeanDefinition();
            	String beanClassName = bean.getAttributes().getNamedItem("class").getNodeValue();
            	String beanName = bean.getAttributes().getNamedItem("id").getNodeValue();
            	
        		beandef.setBeanClassName(beanClassName);
        		
				try {
					Class<?> beanClass = Class.forName(beanClassName);
					beandef.setBeanClass(beanClass);
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        		
        		PropertyValues propertyValues = new PropertyValues();
        		
        		NodeList propertyList = bean.getChildNodes();
            	for(int j = 0 ; j < propertyList.getLength(); j++)
            	{
            		Node property = propertyList.item(j);
            		if (property instanceof Element) {
        				Element ele = (Element) property;
        				
        				String name = ele.getAttribute("name");
        				Class<?> type;
        				try {
							type = beandef.getBeanClass().getDeclaredField(name).getType();
							
							if(ele.hasAttribute("value"))
							{
							
							Object value=ele.getAttribute("value");
	        				if(type == Integer.class)
	        				{
	        					value = Integer.parseInt((String) value);
	        				}
	        				
	        				propertyValues.AddPropertyValue(new PropertyValue(
	        						name,value));
							}
							 if(ele.hasAttribute("ref"))
							{
								
								String value=ele.getAttribute("ref");
								Object bp=null;
								
		        					 bp=this.getBean(value);
		        					 propertyValues.AddPropertyValue(new PropertyValue(
				        						name,bp));
		        				
								
							}
							
							//..........a
							NodeList n=ele.getChildNodes();
							
							for(int c=0;c<n.getLength();c++)
							{
							if(n.item(c).getNodeType()==Node.ELEMENT_NODE)
							{
								if(n.item(c).getNodeName().equals("list"))
								{
								List<Advice> l=new ArrayList<Advice>();
								
								NodeList n2=n.item(c).getChildNodes();
								for(int x=0;x<n2.getLength();x++)
								{
									if(n2.item(x).getNodeType()==Node.ELEMENT_NODE)
									{
									//System.out.println(n2.item(x).getFirstChild().getNodeValue());
									l.add((Advice) this.getBean(n2.item(x).getFirstChild().getNodeValue()));
									
									}
								}
								propertyValues.AddPropertyValue(new PropertyValue(
		        						name,l));
								}
							}
							}
						} catch (NoSuchFieldException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (SecurityException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
        				
        				
        			}
            	}
            	beandef.setPropertyValues(propertyValues);
            	
            	this.registerBeanDefinition(beanName, beandef);
            }
            
		} catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
		
	}

	@Override
	protected BeanDefinition GetCreatedBean(BeanDefinition beanDefinition) {
		
		try {
			// set BeanClass for BeanDefinition
			
			Class<?> beanClass = beanDefinition.getBeanClass();
			// set Bean Instance for BeanDefinition
			Object bean = beanClass.newInstance();	
			
			List<PropertyValue> fieldDefinitionList = beanDefinition.getPropertyValues().GetPropertyValues();
			for(PropertyValue propertyValue: fieldDefinitionList)
			{
				BeanUtil.invokeSetterMethod(bean, propertyValue.getName(), propertyValue.getValue());
			}
			
			beanDefinition.setBean(bean);
			
			return beanDefinition;
			
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
