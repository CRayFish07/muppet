package cn.bronzeware.muppet.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class TestJDBC {

	public static void main(String[] args) {

		long start = System.currentTimeMillis();
		try {
				Class.forName("com.mysql.jdbc.Driver");
				
				
			for(int i = 0;i<100;i++){
				
				Connection connection = DriverManager
						.getConnection("jdbc:mysql://123.56.225.214:3999/test??Unicode=true&characterEncoding=utf-8&generateSimpleParameterMetadata=true"
								, "root", "root");
				PreparedStatement ps = null;
				try {
					
					String sql = "insert into t_note(value,username,user_id) values (?,?,?)";
					 ps = connection.prepareStatement(sql);
					ps.setObject(1, "yuhaiqiang");
					ps.setObject(2, "username");
					ps.setObject(3, 2);
					ps.execute();
					
				} catch (Exception e) {
					// TODO: handle exception
				}finally{
					ps.close();
					connection.close();
				}
				
			}
			
			
			
		} 
		catch (ClassNotFoundException e) {
			// 
			e.printStackTrace();
		}
		catch (SQLException e) {
			// 
			e.printStackTrace();
		}
		long end = System.currentTimeMillis();
		System.out.println(end - start);
		
	}

}
