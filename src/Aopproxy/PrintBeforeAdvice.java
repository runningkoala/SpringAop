package Aopproxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class PrintBeforeAdvice implements MethodBeforeAdvice,InvocationHandler{

	private Object targetObject;
	public PrintBeforeAdvice() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		before(method,args,proxy);
		return method.invoke(getTargetObject(), args);
	}

	@Override
	public void before(Method method, Object[] args, Object target)
			throws Throwable {
		// TODO Auto-generated method stub
		System.out.println("Before..................");
	}

	public Object getTargetObject() {
		return targetObject;
	}

	public void setTargetObject(Object targetObject) {
		this.targetObject = targetObject;
	}

	




}
