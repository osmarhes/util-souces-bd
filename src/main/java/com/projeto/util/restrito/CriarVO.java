package com.projeto.util.restrito;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.projeto.util.Conexao;


public class CriarVO  {
    private Set imports;
    private String tabela;
    private String sch;
    private String id;
    private StringBuffer gets;
    private StringBuffer sets;
    private StringBuffer variaveis;
    private String tipoId;

    public CriarVO(String tabela, String id, String sch, String tipoId) {
        this.tabela = tabela;
        this.id = id;
        this.sch = sch;
        this.tipoId = tipoId;
    }

    public static void main(String[] args) {
        try {
            if (args.length < 1) {
                System.err.println("Utilizacao: CreateVo <NOME_TABELA>");
                System.exit(-1);
            }

            CriarVO vo = new CriarVO(args[0], args[1], args[2], args[3]);
            vo.execute();

            //System.out.println("VO CRIADO COM SUCESSO!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void execute() throws  SQLException, IOException {
        imports = new HashSet();
        variaveis = new StringBuffer();
        gets = new StringBuffer();
        sets = new StringBuffer();
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

        for (int i = 1; i <= numeroColunas; i++) {
            trata(rsmd.getColumnLabel(i), rsmd.getColumnType(i), rsmd.getScale(i));
            /*System.out.println(
                rsmd.getColumnName(i) + " - " + rsmd.getColumnTypeName(i) + " - " +
                rsmd.getColumnClassName(i) + " - " + rsmd.getColumnDisplaySize(i) + " - " +
                rsmd.getScale(i) + " - " + rsmd.getColumnType(i));*/
        }

        String importsStr = "";
        Iterator iImports = imports.iterator();

        while (iImports.hasNext()) {
            importsStr += (((String) iImports.next()) + "\n");
        }

        String nomeClasse = trataNome(tabela, true);
        FileWriter fw = new FileWriter(new File("./output/vo/" + nomeClasse + ".java"));
        fw.write("package br.com.fl.dao.vo;");
        fw.write(importsStr);
        fw.write("\n\npublic class " + nomeClasse + "{");
        fw.write("\n" + variaveis.toString());
        fw.write("\n" + gets.toString());
        fw.write("\n" + sets.toString());
        fw.write("\n}");
        fw.flush();
    }

    private void trata(String nomeColuna, int tipoColuna, int casasDecimais) {
        String tipoRetorno;

        if ((tipoColuna == Types.VARCHAR) || (tipoColuna == Types.CHAR)) {
            tipoRetorno = "String";
        } else if ((tipoColuna == Types.NUMERIC) && (casasDecimais > 0)) {
            tipoRetorno = "Double";
        } else if ((tipoColuna == Types.INTEGER) || (tipoColuna == Types.NUMERIC)) {
            tipoRetorno = "Long";
        } else if (tipoColuna == Types.DATE || tipoColuna == Types.TIMESTAMP) {
            tipoRetorno = "Calendar";
            imports.add("import java.util.Calendar;");
        } else {
            tipoRetorno = "Object";
        }

        variaveis.append("private " + tipoRetorno + " " + trataNome(nomeColuna, false) + ";\n");
        gets.append(
            "public " + tipoRetorno + " get" + trataNome(nomeColuna, true) + "(){\n\treturn " +
            trataNome(nomeColuna, false) + ";\n}\n\n");
        sets.append(
            "public void set" + trataNome(nomeColuna, true) + "(" + tipoRetorno + " " +
            trataNome(nomeColuna, false) + "){\n\tthis." + trataNome(nomeColuna, false) + "=" +
            trataNome(nomeColuna, false) + ";\n}\n\n");
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
}

