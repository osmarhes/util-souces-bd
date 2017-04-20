package com.projeto.util.restrito;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;

import com.projeto.util.Conexao;



public class CriarImplementacaoDAO {
    private StringBuffer imports;
    private String id;
    private String sch;
    private String tabela;

    private StringBuffer update;
    private StringBuffer updateSqlColumns;
    private StringBuffer updateSqlWhere;
    private StringBuffer updateSqlStatements;
    
    private StringBuffer insert;
    private StringBuffer insertSqlColumns;
    private StringBuffer insertSqlValues;
    private StringBuffer insertSqlStatements;
    
    private StringBuffer delete;
    private StringBuffer deleteSqlStatements;
    
    private StringBuffer pesquisar;
    
    private StringBuffer simpledate;
    private String tipoId;

    public CriarImplementacaoDAO(String tabela, String id, String sch, String tipoId) {
        this.tabela = tabela;
        this.id = id;
        this.sch = sch;
        this.tipoId = tipoId;
        
        simpledate = new StringBuffer();
    }

    public static void main(String[] args) {
        try {
            if (args.length<2) {
                System.err.println("Utilizacao: CreateDao <NOME_TABELA> <ID_TABELA>");
                System.exit(-1);
            }

            CriarImplementacaoDAO dao = new CriarImplementacaoDAO(args[0], args[1], "", args[3]);
            dao.execute();

            System.out.println("DAOS CRIADAS COM SUCESSO!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void execute() throws  SQLException, IOException {
    	
        String sql = "SELECT * FROM " + tabela + " WHERE " + id ;
        
        if("V".equalsIgnoreCase(tipoId)){
        	sql += " = '-1'";
        }else{
        	sql += " = -1";
        }
        
        PreparedStatement p = Conexao.executarPrepared(sql);
        ResultSet rs = p.executeQuery();
        ResultSetMetaData rsmd = rs.getMetaData();
        int numeroColunas = rsmd.getColumnCount();
                
        /* IMPORTS */
        imports = new StringBuffer("" +
        		"import java.sql.PreparedStatement;\n" +
        		"import java.sql.ResultSet;\n" +
        		"import java.sql.SQLException;\n" +
        		"import java.sql.Types;\n" +        		
        		"import br.com.fl.util.BasicRowProcessor;\n" +
        		"import br.com.fl.gen.dao.Conexao;\n" +
        		"import br.com.fl.dao.exception.DAOException;\n" +
        		"import br.com.fl.dao.vo." + trataNome(tabela, true) + ";\n" + 
        		"import br.com.fl.dao.interfaces." + trataNome(tabela, true) + "Interface;\n");
        
        /* Cria INSERT */
        insert = new StringBuffer();
        insertSqlColumns = new StringBuffer();
        insertSqlValues = new StringBuffer();
        insertSqlStatements = new StringBuffer();

        for (int i = 1; i <= numeroColunas; i++) {
        	int tipoColuna = rsmd.getColumnType(i);
        	if (i==numeroColunas){
            	insertSqlColumns.append(rsmd.getColumnLabel(i) + "");
            	if ((rsmd.getColumnType(i)==Types.DATE || tipoColuna == Types.TIMESTAMP)){
            		insertSqlValues.append("to_date(?, 'dd-mm-yyyy hh24:mi:ss')");
            	}else{
                	insertSqlValues.append("?");            		
            	}
        	}else{
            	insertSqlColumns.append(rsmd.getColumnLabel(i) + ", ");
            	if ((rsmd.getColumnType(i)==Types.DATE || tipoColuna == Types.TIMESTAMP)){
            		insertSqlValues.append("to_date(?, 'dd-mm-yyyy hh24:mi:ss'), ");
            	}else{
                	insertSqlValues.append("?, ");
            	}        		
        	}
        	insertSqlStatements.append("stmt.setObject(idx++, " + ((rsmd.getColumnType(i)==Types.DATE || tipoColuna == Types.TIMESTAMP) ? "sdf.format(" : "") + 
        			"vo.get" + trataNome(rsmd.getColumnLabel(i), true) + "()" + ((rsmd.getColumnType(i)==Types.DATE || tipoColuna == Types.TIMESTAMP) ? ")" : "") + 
        			", " + trataRetorno(rsmd.getColumnType(i)) + ");\n");
        }
        
        insert.append("public int insert(" + trataNome(tabela, true) + " vo) throws DAOException{ \n" + 
        		"String sql = \"INSERT INTO " + tabela + " (" + insertSqlColumns.toString() + ") VALUES (" + insertSqlValues + ")\"; \n" +
        		"int ret=0;" +
        		"try { \n" ); 
        
        		if(null != tipoId && "V".equalsIgnoreCase(tipoId)){
        			insert.append("vo.set" + trataNome(id, true) + "(Conexao.getProximoId(\"" + tabela + "\").toString()); \n") ;
        		}else if(null != tipoId){
        			insert.append("vo.set" + trataNome(id, true) + "(Conexao.getProximoId(\"" + tabela + "\")); \n");
        		}
        		
        		insert.append("PreparedStatement stmt = Conexao.executarPrepared(sql); \n" + 
        		"int idx = 1; \n" + 
        		insertSqlStatements.toString() +
        		"ret = stmt.executeUpdate(); \n" +
        		"} catch (SQLException e) { \n" +
        		"throw new DAOException (e); \n" +
        		"} \n" +
        		"return ret; \n" +
        		"}"
        );
        
        /* Cria UPDATE */
        update = new StringBuffer();
        updateSqlColumns = new StringBuffer();
        updateSqlWhere = new StringBuffer();
        updateSqlStatements = new StringBuffer();

        for (int i = 1; i <= numeroColunas; i++) {
        	if (!rsmd.getColumnLabel(i).equals(id)){
            	if (i==numeroColunas){
                	if ((rsmd.getColumnType(i)==Types.DATE || rsmd.getColumnType(i) == Types.TIMESTAMP)){
                		updateSqlColumns.append(rsmd.getColumnLabel(i) + "=to_date(?, 'dd-mm-yyyy hh24:mi:ss')");
                	}else{
                		updateSqlColumns.append(rsmd.getColumnLabel(i) + "=?");                		
                	}
            	}else{
                	if ((rsmd.getColumnType(i)==Types.DATE || rsmd.getColumnType(i) == Types.TIMESTAMP)){
                		updateSqlColumns.append(rsmd.getColumnLabel(i) + "=to_date(?, 'dd-mm-yyyy hh24:mi:ss'), ");
                	}else{
                		updateSqlColumns.append(rsmd.getColumnLabel(i) + "=?, ");                		
                	}
            	}
            	updateSqlStatements.append("stmt.setObject(idx++, vo.get" + trataNome(rsmd.getColumnLabel(i), true) + "(), " + trataRetorno(rsmd.getColumnType(i)) + ");\n");        		
        	}else{
        		updateSqlWhere.append("stmt.setObject(idx++, vo.get" + trataNome(rsmd.getColumnLabel(i), true) + "(), " + trataRetorno(rsmd.getColumnType(i)) + ");\n");
        	}
        }
        
        update.append("public int update(" + trataNome(tabela, true) + " vo) throws DAOException{ \n" + 
        		"String sql = \"UPDATE " + tabela + " SET " + updateSqlColumns.toString() + " WHERE " + id + "=?\"; \n" + 
        		"int ret=0;" +
        		"try { \n" + 
        		"PreparedStatement stmt = Conexao.executarPrepared(sql); \n" + 
        		"int idx = 1; \n" + 
        		updateSqlStatements.toString() +
        		updateSqlWhere.toString() +
        		"ret = stmt.executeUpdate(); \n" +
        		"} catch (SQLException e) { \n" +
        		"throw new DAOException (e); \n" +
        		"} \n" +
        		"return ret; \n" +
        		"}"
        );

        /* Cria DELETE */
        delete = new StringBuffer();
        deleteSqlStatements = new StringBuffer();

        for (int i = 1; i <= numeroColunas; i++) {
        	if (rsmd.getColumnLabel(i).equals(id)){
        		deleteSqlStatements.append("stmt.setObject(idx++, vo.get" + trataNome(rsmd.getColumnLabel(i), true) + "(), " + rsmd.getColumnType(i) + ");\n");
        	}
        }
        
        delete.append("public int delete(" + trataNome(tabela, true) + " vo) throws DAOException{ \n" + 
        		"String sql = \"delete from " + tabela + " WHERE " + id + "=?\"; \n" +
        		"int ret=0;" +
        		"try { \n" + 
        		"PreparedStatement stmt = Conexao.executarPrepared(sql); \n" + 
        		"int idx = 1; \n" + 
        		deleteSqlStatements.toString() +
        		"ret = stmt.executeUpdate(); \n" +
        		"} catch (SQLException e) { \n" +
        		"throw new DAOException (e); \n" +
        		"} \n" +
        		"return ret; \n" +
        		"}"
        );
        
        /* Cria PESQUISAR */
        pesquisar = new StringBuffer(
        		"public " + trataNome(tabela, true) + " pesquisar(Long id) throws DAOException{ \n" +
        		"BasicRowProcessor b = new BasicRowProcessor();\n" + 
        		"String sql = \"SELECT * FROM " + tabela + " WHERE " + id + "=?\"; \n" +
        		trataNome(tabela, true) + " ret = new " + trataNome(tabela, true) + "();\n" +  
        		"try { \n" + 
        		"PreparedStatement stmt = Conexao.executarPrepared(sql); \n" + 
        		"int idx = 1; \n" + 
        		"stmt.setObject(idx++, id, Types.NUMERIC); \n" +
        		"ResultSet rs = stmt.executeQuery(); \n" +
        		"if (rs.next()) { \n" +
        		"ret = (" + trataNome(tabela, true) + ") b.toBean(rs, " + trataNome(tabela, true) + ".class); \n" +
        		"} \n" +
        		"} catch (SQLException e) { \n" +
        		"throw new DAOException (e); \n" +
        		"} \n" +
        		"return ret; \n" +
        		"}"
        );

        String nomeClasse = trataNome(tabela, true);
        FileWriter fw = new FileWriter(new File("./output/implementacao/" + nomeClasse + "DAO.java"));
        fw.write("package br.com.fl.dao.implementacao;\n");
        fw.write("\n" + imports.toString());
        fw.write("\n\npublic class " + nomeClasse + "DAO implements " + nomeClasse + "Interface {\n");
        fw.write("\n" + simpledate.toString());
        fw.write("\n" + insert.toString());
        fw.write("\n" + update.toString());
        fw.write("\n" + delete.toString());
        fw.write("\n" + pesquisar.toString());
        fw.write("\n}");
        fw.flush();
    }

    private String trataNome(String nome, boolean capitalizado) {
        String[] nomeTemp = nome.toLowerCase().split("_");

        String nomeRet = null;

        if (capitalizado) {
            nomeRet = nomeTemp[0].substring(0, 1).toUpperCase() +
                nomeTemp[0].substring(1).toLowerCase();
        } else {
            nomeRet = nomeTemp[0];
        }

        for (int i = 1; i < nomeTemp.length; i++) {
            nomeRet += (nomeTemp[i].substring(0, 1).toUpperCase() +
            nomeTemp[i].substring(1).toLowerCase());
        }

        return nomeRet;
    }
    
    private String trataRetorno(int type){
    	switch(type){
	    	case Types.VARCHAR:
	    		return "Types.VARCHAR";
	    	case Types.NUMERIC:
	    		return "Types.NUMERIC";
	    	case Types.INTEGER:
	    		return "Types.INTEGER";
	    	case Types.CHAR:
	    		return "Types.VARCHAR";
	    	case Types.DATE:
	    		simpledate = new StringBuffer("SimpleDateFormat sdf = new SimpleDateFormat(\"dd-MM-yyyy HH:mm:ss\");");
	    		return "Types.VARCHAR";
	    	case Types.TIMESTAMP:
	    		simpledate = new StringBuffer("SimpleDateFormat sdf = new SimpleDateFormat(\"dd-MM-yyyy HH:mm:ss\");");
	    		return "Types.VARCHAR";
	    	default:
	    		return "";
    	}
    }
}
