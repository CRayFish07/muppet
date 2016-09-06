package cn.bronzeware.muppet.context;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map.Entry;
import java.util.Date;
import java.util.Map;



import cn.bronzeware.muppet.converter.ObjectConvertor;
import cn.bronzeware.muppet.core.ThreadLocalTransaction;
import cn.bronzeware.muppet.datasource.DataSourceUtil;
import cn.bronzeware.muppet.entities.Note;
import cn.bronzeware.muppet.entities.User;
import cn.bronzeware.muppet.resource.Container;
import cn.bronzeware.muppet.resource.ResourceInfo;
import cn.bronzeware.muppet.sqlgenerate.ParamCanNotBeNullException;
import cn.bronzeware.muppet.sqlgenerate.Sql;
import cn.bronzeware.muppet.sqlgenerate.SqlGenerate;
import cn.bronzeware.muppet.sqlgenerate.SqlGenerateException;
import cn.bronzeware.muppet.sqlgenerate.SqlGenerateHelper;
import cn.bronzeware.muppet.transaction.Transaction;
import cn.bronzeware.muppet.util.log.Logger;

public class InsertContext  extends AbstractContext{

	
	public InsertContext(Container<String, ResourceInfo> container){
		this.container = container;
		
		this.sqlGenerateHelper  = new SqlGenerateHelper(container);
	}
	private Container<String, ResourceInfo> container;
	private SqlGenerateHelper sqlGenerateHelper;
	public static void main(String[] args) throws ContextException {
	
		/*InsertContext insertContext = new InsertContext();
		long sum = 0;
		
		for(int i = 0;i<CIRCLE;i++){
			User testEntity = new User(); 
			testEntity.setPassword("惠晨");
			testEntity.setUsername("pasd");
			testEntity.setDate(new Date(System.currentTimeMillis()));
			//insertContext.execute(testEntity,null,null);
			Note note = new Note();
			note.setPassword("yujing");
			note.setUsername("yujing");
			note.setValue("jingjing");
			long start = System.currentTimeMillis();
			insertContext.execute(note,null,null);
			long end = System.currentTimeMillis();
			sum=sum+(end-start);
		}
		
		System.out.println(sum);
		System.out.println(time);*/
	}

	

	
	public Object execute(Object object
			,String wheres 
			,Object[] wherevalues)
	throws ContextException
	{
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet results = null;
		Boolean success = false;
		try {
			Transaction transaction = ThreadLocalTransaction.get();
			connection = transaction.getConnection();
			Sql sql = sqlGenerateHelper.execute(object,null,SqlGenerate.INSERT);
			
			String sqlString = sql.getSql();
			Map<Field, Object> map = sql.getValues();
			ps = connection.prepareStatement(sqlString,Statement.RETURN_GENERATED_KEYS);
			int i = 1;
			for(Entry<Field, Object> e:map.entrySet()){
				ps.setObject(i,e.getValue());
				//System.out.println(e.getKey().getName()+e.getValue());
				i++;
			}
			System.out.println("Time:"+
					new Date(System.currentTimeMillis()).toLocaleString()
					+"  "+sqlString);
			int rows = ps.executeUpdate();
			success= (rows== 1 ?true:false);
			results = ps.getGeneratedKeys();
			int num = -1;
			if(results.next())
	          {
	             num = results.getInt(1);
	          }
			ObjectConvertor.loadField(sql.getPrimarykey(), object, num);
		} catch (SQLException e) {
			// 
			e.printStackTrace();
		} catch (ParamCanNotBeNullException e) {
			// 
			e.printStackTrace();
		} catch (SqlGenerateException e1) {
			// 
			throw new SqlGenerateContextException(e1.getMessage());
		}
		finally{
			try {
				if(results!=null){
					results.close();
				}
				
				if(ps!=null){
					ps.close();
				}
				
				/*if(connection!=null){
					connection.close();
				}*/
				
			} catch (SQLException e) {
				// 
				e.printStackTrace();
			}
		}
		return success;
	}
	

}
