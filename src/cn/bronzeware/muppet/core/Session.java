package cn.bronzeware.muppet.core;

import java.sql.SQLException;
import java.util.List;

import cn.bronzeware.muppet.transaction.Transaction;

public interface Session {

	public boolean close();
	
	
	public Transaction beginTransaction() throws SQLException;
	
	
	public  boolean update(Object object,String wheres ,Object[] wherevalues);
	
	
	public boolean updateByPrimaryKey(Object object);
	
	public boolean insert(Object object);
	
	
	public boolean delete(Object object,String wheres ,Object[] wherevalues);
	
	
	public boolean deleteByPrimaryKey(Object object);
	
	public <T> List<T>  query(Class<T> clazz
			,String wheres 
			,Object[] wherevalues);
	
	public <T> List<T>  query(String queryString,Object[] values,Class<T> clazz);
	
}
