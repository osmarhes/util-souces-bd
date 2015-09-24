package com.projeto.util.restrito;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.projeto.util.Conexao;
import com.projeto.util.app.CriarDAOCompleta;

/**
 * 	N�O ULTILIZAR ESTA CLASSE, AO PASSO QUE ELA CRIA TODOS OS PADR�ES DENOVO, DE TODAS AS TABELAS DO PRECISION
 * OU SEJA
 * NAO USEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE!
 *
 */
public class CriarDAOSPrecision  {

	public static void main(String[] args) {
		System.out.println("Esta classe gerar� tudo novamente , ou seja , nao temos a necessidade de usa-la");
		CriarDAOSPrecision p= new CriarDAOSPrecision();
		p.criarDaos();
	}
	
	public void criarDaos(){
        String sql = "select t.table_name tabela, i.column_name id, t.owner sch " +
        		"from all_tables t, all_ind_columns i " +
        		"where owner = billing " +        		
        		"and i.column_name like 'ID%' " +
        		"and t.table_name=i.table_name " +
        		"group by t.table_name, i.column_name, t.owner ";
        		//"having count(*) = 1"
        
        PreparedStatement ps;
		try {
			ps = Conexao.executarPrepared(sql);
	        ResultSet rs = ps.executeQuery();
	        
	        while(rs.next()){
	        	CriarDAOCompleta daoCompleta = new CriarDAOCompleta();
	        	String tabela = rs.getString("tabela");
	        	String id = rs.getString("id");
	        	String sch = rs.getString("sch");
	        	daoCompleta.execute(tabela, id, sch, null);
	        }
	        
	
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
