package cn.bronzeware.muppet.context;

public class StandardSqlContext implements SqlContext{

	
	private SqlRequest sqlRequest;
	
	
	private SqlResponse sqlResponse;
	
	
	public StandardSqlContext(SqlRequest sqlRequest,SqlResponse sqlResponse){
		
		this.sqlRequest  = sqlRequest;
		this.sqlResponse = sqlResponse;
		
	}
	
	@Override
	public SqlRequest getSqlRequest() {
		
		return sqlRequest;
	}

	@Override
	public SqlResponse getSqlResponse() {
		
		return this.sqlResponse;
	}

}
