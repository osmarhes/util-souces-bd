package com.projeto.util;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;


public final class Conexao {

	private static Map<String, ConnectionPreparedStatement> mapConnectionPreparedStatement = new TreeMap<String, ConnectionPreparedStatement>();

	{
		try {
			getConexao();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static Connection getConexao () throws SQLException {

		ConnectionPreparedStatement connectionPreparedStatement = mapConnectionPreparedStatement.get(Thread.currentThread().getName());

		if(null == connectionPreparedStatement){
			connectionPreparedStatement = new ConnectionPreparedStatement();

			mapConnectionPreparedStatement.put(Thread.currentThread().getName(), connectionPreparedStatement);
		}

		Connection con = connectionPreparedStatement.getConnection();

		if (con == null || con.isClosed()) {
			try {
				Properties properties = new Properties();
				String driver = properties.getProperty("bd.drive");

				String strCon = properties.getProperty("bd.url");

				String user = properties.getProperty("bd.usuario");
				String pass = properties.getProperty("bd.senha");

				Class.forName(driver);

				con = DriverManager.getConnection(strCon, user, pass);

				connectionPreparedStatement.setConnection(con);

				connectionPreparedStatement.setPreparedStatements(new HashMap<String, PreparedStatement>());

				mapConnectionPreparedStatement.put(Thread.currentThread().getName(), connectionPreparedStatement);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return con;
	}

	public static PreparedStatement executarPrepared (String query) throws SQLException  {		
		return getPreparedStatement(query, true);
	}


	public static PreparedStatement getPreparedStatement(String query, boolean mantemNaMemoria)
			throws SQLException {
		PreparedStatement p = null;
		Connection c = getConexao(); 
		if (mantemNaMemoria) {

			ConnectionPreparedStatement connectionPreparedStatement = mapConnectionPreparedStatement.get(Thread.currentThread().getName());

			String chave  = Thread.currentThread().getName() + "_XX_" + query;

			p = connectionPreparedStatement.getPreparedStatements().get(chave);
			if (p == null) {
				p = c.prepareStatement(query);
				p.setQueryTimeout(0);
				connectionPreparedStatement.getPreparedStatements().put(chave, p);
			}
		} else 
			p = c.prepareStatement(query);
		return p;
	}


	public static void fecharConexao() throws SQLException {

		ConnectionPreparedStatement connectionPreparedStatement = mapConnectionPreparedStatement.get(Thread.currentThread().getName());

		Connection con = connectionPreparedStatement.getConnection();

		if (con != null && !con.isClosed()){			
			
			tratarMapaConexao(connectionPreparedStatement);
			
			mapConnectionPreparedStatement.remove(Thread.currentThread().getName());
			
			con.close();
		}
	}
	
	
	private static void tratarMapaConexao(ConnectionPreparedStatement connectionPreparedStatement) throws SQLException{
		
		Map<String, PreparedStatement> mapaStatements = connectionPreparedStatement.getPreparedStatements();
		
		Set<String> chaveSet = mapaStatements.keySet();
		
		for (String chave : chaveSet) {
			PreparedStatement ps = mapaStatements.get(chave);
			ps.close();
		}
	}

	public static void fecharConexoesPorThread() throws SQLException {

		Set<String> setConexoes = mapConnectionPreparedStatement.keySet();

		for (String chave : setConexoes) {
			
			ConnectionPreparedStatement connectionPreparedStatement = mapConnectionPreparedStatement.get(chave);
			
			Connection con = connectionPreparedStatement.getConnection();
			
			if (con != null && !con.isClosed()){
				
				tratarMapaConexao(connectionPreparedStatement);
				mapConnectionPreparedStatement.remove(Thread.currentThread().getName());
				con.commit();
				con.close();				
			}
		}		
	}

	public static void commit() throws SQLException {

		ConnectionPreparedStatement connectionPreparedStatement = mapConnectionPreparedStatement.get(Thread.currentThread().getName());

		Connection con = connectionPreparedStatement.getConnection();

		if (con != null && !con.isClosed()){
			con.commit();
		}
	}

	public static void rollback() throws SQLException {

		ConnectionPreparedStatement connectionPreparedStatement = mapConnectionPreparedStatement.get(Thread.currentThread().getName());

		Connection con = connectionPreparedStatement.getConnection();

		if (con != null && !con.isClosed()){
			con.rollback();
		}
	}

	/** @return A data atual da base de dados */
	public static Date getDataHoraAtual() {
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		String sql = "select TO_CHAR(sysdate, 'DD/MM/YYYY HH24:MI:SS') from dual";
		Date data = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = executarPrepared(sql);
			rs = ps.executeQuery();;
			if (rs.next())
				data = df.parse(rs.getString(1));
		} catch (Exception e) {
			data = new Date();
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (SQLException e) {
			} finally { rs = null; }
		}
		return data;
	}

	public static void rodarProcedure(String sql) throws SQLException{

		Connection c = getConexao(); 

		StringBuilder sb = new StringBuilder();

		sb.append("{ call ");
		sb.append(sql);
		sb.append(" }");

		CallableStatement proc = c.prepareCall(sb.toString());

		proc.execute();
	}

} class ConnectionPreparedStatement{

	private Map<String, PreparedStatement> _statements = new TreeMap<String, PreparedStatement>();

	private Connection connection;


	public void setPreparedStatements(Map<String, PreparedStatement> statements) {
		_statements = statements;
	}

	public Map<String, PreparedStatement> getPreparedStatements() {
		return _statements;
	}

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

}