package Aopproxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.List;

public class ProxyFactoryBean {
	
	private String proxyInterfaces;//接口名
	private Object target;//真实的对象
	private List<Advice> interceptorNames;//拦截器
	
	public String getProxyInterfaces() {
		return proxyInterfaces;
	}

	public void setProxyInterfaces(String proxyInterfaces) {
		this.proxyInterfaces = proxyInterfaces;
	}

	public Object getTarget() {
		return target;
	}

	public void setTarget(Object target) {
		this.target = target;
	}

	public List<Advice> getInterceptorNames() {
		return interceptorNames;
	}

	public void setInterceptorNames(List<Advice> interceptorNames) {
		this.interceptorNames = interceptorNames;
	}



	public Object getProxy()
	{
		Object proxyObject=null;
		if(interceptorNames.size()>0)
		{
			proxyObject=target;
			for(int i=0;i<interceptorNames.size();i++)
			{
				interceptorNames.get(i).setTargetObject(proxyObject);
				try {
					proxyObject = Proxy.newProxyInstance(this.getClass().getClassLoader(), target.getClass().getInterfaces() , (InvocationHandler) interceptorNames.get(i));
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			}
			return proxyObject;
		}
		else {
			return target;
		}
		
	}
	
}
