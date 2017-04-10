package com.projeto.util.restrito;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;


public class CriarFactory {
    private StringBuffer imports;
    //private String id;
    private String tabela;

    public CriarFactory(String tabela, String id) {
        this.tabela = tabela;
        //this.id = id;
    }

    public static void main(String[] args) {
        try {
            if (args.length<2) {
                System.err.println("Utilizacao: CriarFactory <NOME_TABELA> <ID_TABELA>");
                System.exit(-1);
            }

            CriarFactory dao = new CriarFactory(args[0], args[1]);
            dao.execute();

            System.out.println("FACTORY CRIADAS COM SUCESSO!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void execute() throws  SQLException, IOException {
        /* IMPORTS */
        String nomeTabela = trataNome(tabela, true);
    	
    	imports = new StringBuffer(
    			"import java.util.HashMap;\n "+
    			"import java.util.Map;\n" +
        		"import br.com.commons.atlas.dao.implementacao." + nomeTabela + "DAO;\n" +
        		"import br.com.commons.atlas.dao.interfaces." + nomeTabela + "Interface;\n");
        
        StringBuffer body = new StringBuffer(
        		"private static " + nomeTabela + "Factory instance; \n" +
        		"private static Map<String, "+nomeTabela + "Interface> instancias; \n" +
        		"private " + nomeTabela + "Factory() { \n" +
        		"instancias = new HashMap<String, "+nomeTabela + "Interface>(); \n" +        				
        		"} \n" +
        		"public static " + nomeTabela + "Factory getInstance() { \n" +
        		"if (instance == null) { \n" +
        		"instance = new " + nomeTabela + "Factory(); \n" +
        		"} \n" +
        		"return instance; \n" +
        		"} \n" +
        		"public " + nomeTabela + "Interface get" + nomeTabela + "Interface() { \n" +
        		nomeTabela + "Interface dao = (" + nomeTabela + "Interface) instancias.get(Thread.currentThread().getName()); \n" +
        		"if (dao == null) { \n" +
        		"dao = new " + nomeTabela + "DAO(); \n" +
        		"instancias.put(Thread.currentThread().getName(), dao); \n" +
        		"} \n" +
        		"return dao; \n" +
        		"} \n");

        FileWriter fw = new FileWriter(new File("./output/factory/" + nomeTabela + "Factory.java"));
        fw.write("package br.com.commons.atlas.dao.factory;\n");
        fw.write("\n" + imports.toString());
        fw.write("\n\npublic class " + nomeTabela + "Factory {\n");
        fw.write("\n" + body.toString());
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
}
