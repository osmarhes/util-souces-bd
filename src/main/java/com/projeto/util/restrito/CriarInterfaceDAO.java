package com.projeto.util.restrito;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;


public class CriarInterfaceDAO  {
    private StringBuffer imports;
    private String tabela;
    private StringBuffer update;
    private StringBuffer insert;
    private StringBuffer delete;
    private StringBuffer pesquisar;

    public CriarInterfaceDAO(String tabela, String id) {
        this.tabela = tabela;
    }

    public static void main(String[] args) {
        try {
            if (args.length<2) {
                System.err.println("Utilizacao: CriarInterfaceDAO <NOME_TABELA> <ID_TABELA>");
                System.exit(-1);
            }

            CriarInterfaceDAO dao = new CriarInterfaceDAO(args[0], args[1]);
            dao.execute();

            System.out.println("INTERFACES DAOS CRIADAS COM SUCESSO!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void execute() throws  SQLException, IOException {
        /* IMPORTS */
        imports = new StringBuffer("" +
        		"import br.com.commons.atlas.dao.exception.DAOException;\n" +
        		"import br.com.commons.atlas.dao.vo." + trataNome(tabela, true) + ";");
        
        /* Cria INSERT */
        insert = new StringBuffer("public abstract int insert(" + trataNome(tabela, true) + " vo) throws DAOException; \n");
        
        /* Cria UPDATE */
        update = new StringBuffer("public abstract int update(" + trataNome(tabela, true) + " vo) throws DAOException; \n");

        /* Cria DELETE */
        delete = new StringBuffer("public abstract int delete(" + trataNome(tabela, true) + " vo) throws DAOException; \n");
        
        /* Cria PESQUISAR */
        pesquisar = new StringBuffer("public abstract " + trataNome(tabela, true) + " pesquisar(Long id) throws DAOException; \n");

        String nomeClasse = trataNome(tabela, true);
        FileWriter fw = new FileWriter(new File("./output/interfaces/" + nomeClasse + "Interface.java"));
        fw.write("package br.com.commons.atlas.dao.interfaces;\n");
        fw.write("\n" + imports.toString());
        fw.write("\n\npublic interface " + nomeClasse + "Interface {\n");
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
}
