package cn.bronzeware.muppet.test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import cn.bronzeware.util.reflect.ReflectUtil;

public class TestProxy {

	public class Invo1 implements InvocationHandler{

		@Override
		public Object invoke(Object proxy, Method method, Object[] args)
				throws Throwable {
			System.out.println("before"+proxy.getClass().getName()+
					"methodName:"+method.getName()+args[0]);
			
			Object result = method.invoke(proxy, args);
			System.out.println("after");
			return result;
		}
		
	}
	
	public static void main(String[] args){
	
	/*	A a = (A) ReflectUtil.getClassProxy(new A(), new TestProxy().new Invo1());
		System.out.println(a.Interface1("于海强"));
		A b = (A) ReflectUtil.getClassProxy(new A());
		System.out.println(b.Interface1("于海强"));*/
	}
}
