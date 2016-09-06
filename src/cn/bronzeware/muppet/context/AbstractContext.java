package cn.bronzeware.muppet.context;

import cn.bronzeware.muppet.filters.FilterChain;
import cn.bronzeware.muppet.filters.FilterChainWrapper;
import cn.bronzeware.muppet.filters.StandardFilterChain;

public abstract class AbstractContext implements Context{

	private StandardFilterChain standardFilterChain ;
	
	
	
	public AbstractContext(){
		standardFilterChain =  new StandardFilterChain(); 
		
	}
	
	
	public void doChain(SqlContext context){
		standardFilterChain.doChain(context);
	}
}
