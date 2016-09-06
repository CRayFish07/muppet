package cn.bronzeware.muppet.filters;

import cn.bronzeware.muppet.context.SqlContext;


public class StandardFilterChain implements FilterChain{

	private Filter[] filters = new Filter[INIT_SIZE] ;
	private static final int  INIT_SIZE = 10;
	private static final int  INCREMENTSIZE = 10;
	private FilterChain filterChainWrapper;
	
	private static int capicity = INIT_SIZE;

	public  void addFilters(Filter[] filter){
		
		
			for(int i = 0;i<filter.length;i++){
				if(size>=capicity){
					
					Filter[] newFilters = new Filter[capicity+INCREMENTSIZE];
					System.arraycopy(filters, 0, newFilters, 0, capicity);
					capicity+=INCREMENTSIZE;
					filters = newFilters;
					
				}
				filters[size] = filter[i];
				size++;
			}
		
		
		
	}
	private int size = 0;
	private ThreadLocal<Integer> indexLocal = new ThreadLocal<Integer>(){

		@Override
		protected Integer initialValue() {
			
			return -1;
		}
		
	};
	private Integer getIndex(){
		return indexLocal.get();
	}
	
	public StandardFilterChain(){
		this.filterChainWrapper = new FilterChainWrapper(this);
	}
	
	private void setInitial(){
		indexLocal.set(-1);
	}
	
	private void indexIncrement(){
		indexLocal.set(indexLocal.get()+1);
	}
	
	
	public  void doNext(SqlContext context){
	
		
		if(getIndex()<size-1){
			indexIncrement();;
			filters[getIndex()].doFilter(filterChainWrapper,context);
		}else{
			setInitial();
		}
	}
	
	@Override
	public  void doChain(SqlContext context){
	
		this.setInitial();
		indexIncrement();
		filters[getIndex()].doFilter(this.filterChainWrapper,context);
		
	}
}
