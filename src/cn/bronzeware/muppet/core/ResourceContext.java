package cn.bronzeware.muppet.core;

import java.util.Map;
import java.util.Map.Entry;

import cn.bronzeware.muppet.context.ContextFactory;
import cn.bronzeware.muppet.listener.Event;
import cn.bronzeware.muppet.listener.EventType;
import cn.bronzeware.muppet.listener.Listened;
import cn.bronzeware.muppet.listener.Listener;
import cn.bronzeware.muppet.listener.ListenerFactory;
import cn.bronzeware.muppet.listener.Listeners;
import cn.bronzeware.muppet.resource.Contained;
import cn.bronzeware.muppet.resource.Container;
import cn.bronzeware.muppet.resource.ResourceInfo;
import cn.bronzeware.muppet.resource.StandardContainer;
import cn.bronzeware.muppet.resource.TableInfo;

public class ResourceContext implements Contained,Listened{

	
	public Container<String, ResourceInfo> getContainer(){
		return this.container;
	}
	
	private static boolean isBooted = false;
	private static boolean isBuilded = true;

	private ContextFactory factory = null;
	private DataBaseCheck check = null;
	private EntityMappingDBXMLConfig resourceConfig ;
	private ResourceLoad resourceLoader;
	private StandardResourceBuilder resourceBuild =null;
	
	private Listeners listeners = ListenerFactory.getListeners();

	
	
	public ContextFactory getContextFactory(){
		if(factory==null){
			factory = new ContextFactory(this);
			return factory;
		}else{
			return factory;
		}
	}
	
	
	private void init(String configFilePath) throws InitException{
		if(hasStarted()){
			throw new InitException() {
				
				@Override
				public String message() {
					
					return "muppet已经启动->";
				}
			};
		}
		//初始化前事件 RESOURCE_CONTEXT_INIT_PRE
		listeners.event(EventType.RESOURCE_CONTEXT_INIT_PRE, new Event());
		XMLConfig config = new StandardXMLConfig(configFilePath);
		resourceConfig = new StandardEntityMappingDBXMLConfig(config.getXMLConfigResource());
		isBuilded = resourceConfig.isBuilded();
		check = new DataBaseCheck();
		resourceBuild = new StandardResourceBuilder(check);
		if(isBuilded){
			resolver = new StandardAnnoResolver();
		}else{
			resolver = new StandardDBCheckResolver(check);
		}
		String[] basePackets = resourceConfig.getResourcePackageNames();
		resourceLoader = new StandardResourceLoader();
		Map<String, Class<?>[]> map = resourceLoader.loadClass(basePackets);
		resolveResource(map);
		
		started();
		//RESOURCE_CONTEXT_INIT_POST
		listeners.event(EventType.RESOURCE_CONTEXT_INIT_POST, new Event());
	}
	
	
	private boolean hasStarted(){
		return isBooted;
	}
	
	private void started(){
		
		isBooted = true;
	}
	
	public static void main(String[] args){
		new ResourceContext("muppet.xml");
	}
	
	
	
	private ResourceResolve resolver ;
	private Container<String,ResourceInfo> container = new StandardContainer();
	//private ResourceBuild build = new ResourceBuild();
	
	public ResourceContext(String configFilePath) throws InitException{
		init(configFilePath);
	}
	
	

	private void resolveResource(Map<String, Class<?>[]> map) throws InitException{
		if(map!=null&&map.size()>0){
			for(Entry<String, Class<?>[]> clazzs:map.entrySet())
			{
				Class<?>[] clazz = clazzs.getValue();
				String packetName = clazzs.getKey();
				try {
					
					for (int i = 0; i < clazz.length; i++) {
						if(clazz!=null){
							
								ResourceInfo resourceInfo = resolver.resolve(clazz[i]);
								if(resourceInfo==null){
									
									continue;
								}else{
									if(resourceInfo instanceof TableInfo)
									{
										TableInfo info = (TableInfo)resourceInfo;
										
										container.set(clazz[i].getName(),info);
										if(isBuilded){
											resourceBuild.build(info);
										}else{
											
										}
										
									}
								}
								
							
						}
					}
					
				} catch (ResourceResolveException e) {
					e.printStackTrace();
					throw e;
				}
				catch (BuildException e) {
					e.printStackTrace();
					throw e;
				}
			}
		}
	}


	
	
	@Override
	public void addListener(EventType type, Listener listener) {
		
		listeners.addListener(type, listener);
	}


}
