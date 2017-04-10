package com.projeto.util.app;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.projeto.util.restrito.CriarFactory;
import com.projeto.util.restrito.CriarImplementacaoDAO;
import com.projeto.util.restrito.CriarInterfaceDAO;
import com.projeto.util.restrito.CriarVO;

/**
 * Esta � a classe referente a cria��o completa de todas as classes da commons2(de uma tabela)
 * Os seguintes parametros devem ser passados>
 * <NOME DA TABELA> <ID DA TABELA(PRIMARY KEY)> <SCHEMA DA TABELA> <TIPO PRIMARY KEY>
 * Um exemplo:
 * markup_repasse id_markup med I
 * onde:
 * markup_repasse = tabela
 * id_markup      = primary key
 * med			  = schema
 * I			  = inteiro (pode ser usado V quando for Varchar)
 * 
 * Ao rodar esta classe atente-se ao uso do precisio_home na VM_ARGUMENTS, o mesmo deve passado desta maneira:
 * -DPRECISION_HOME=.
 * 
 * 
 * OBS. VERIFICAR QUE TIPO DE PRIMARY KEY � A DA TABELA A SER PROCESSADA, POIS CASO SEJA VACHAR NECESSARIO COLOCAR
 * ASPAS SIMPLES NOS METODOS DE PESQUISA
 * 
 * 
 */
public class CriarDAOCompleta {

	public static void main(String[] args) {
		
		List<String[]> listaProcessar = new ArrayList<String[]>();
		
		
//		listaProcessar.add(new String[]{"NFE_PROCESSO_DETALHE", "ID_PROCESSO_DETALHE", "ATLAS", "I"});
//		listaProcessar.add(new String[]{"NFE_PROCESSO", "ID_PROCESSO", "ATLAS", "I"});
//		listaProcessar.add(new String[]{"NFE_CONTROLE", "ID_CONTROLE", "ATLAS", "I"});
//		listaProcessar.add(new String[]{"NFE_TIPO_ENVIO", "ID_NFE_TIPO_ENVIO", "ATLAS", null});
//		listaProcessar.add(new String[]{"NFE_TIPO_SERVICO", "ID_NFE_TIPO_SERVICO", "ATLAS", null});
//		listaProcessar.add(new String[]{"NFE_ESTADO_FEDERACAO", "SG_ESTADO_FEDERACAO", "ATLAS", "V"});		
//		listaProcessar.add(new String[]{"NFE_VERSAO_APLICACAO", "ID_NFE_VERSAO_APLICACAO", "ATLAS", null});
		//listaProcessar.add(new String[]{"NFE_FILIAL", "SG_FILIAL", "ATLAS", "V"});
//		listaProcessar.add(new String[]{"NFE_WS_VERSAO_APLICACAO", "ID_NFE_TIPO_SERVICO", "ATLAS", null});
		//listaProcessar.add(new String[]{"NFE_ESTADO_FEDERACAO_WS", "SG_ESTADO_FEDERACAO", "ATLAS", "V"});
		
		//listaProcessar.add(new String[]{"AP_VIAGEM", "ID_VIAGEM", "ATLAS", "I"});
		//listaProcessar.add(new String[]{"AP_ID_CONTROLE", "ID_CONTROLE", "ATLAS", "I"});
		
		/*listaProcessar.add(new String[]{"Usuario_Oracle", "CD_USUARIO", "ATLAS", "V"});*/
		
		//listaProcessar.add(new String[]{"NFE_SERVICO", "ID_NFE_SERVICO", "ATLAS", null});
//		listaProcessar.add(new String[]{"NFE_SERVICO_ITEM", "ID_NFE_SERVICO_ITEM", "ATLAS", null});
				
		//listaProcessar.add(new String[]{"NFE_STATUS_RETORNO", "CD_RETORNO", "ATLAS", null});
		
		//listaProcessar.add(new String[]{"NFE_INUTILIZACAO", "ID_INUTILIZACAO", "ATLAS", "I"});
		
		//listaProcessar.add(new String[]{"NFE_HISTORICO_CONTIGENCIA", "ID_NFE_HISTORICO_CONTIGENCIA", "ATLAS", "I"});
		
		/*listaProcessar.add(new String[]{"CTE_AEREO", "NR_PRE_CTE", "ATLAS", null});
		listaProcessar.add(new String[]{"CTE_ALTERACAO", "NR_PRE_CTE", "ATLAS", null});
		listaProcessar.add(new String[]{"CTE_BASICO", "NR_PRE_CTE", "ATLAS", null});
		listaProcessar.add(new String[]{"CTE_DOC_ANTERIOR", "NR_PRE_CTE", "ATLAS", null});
		listaProcessar.add(new String[]{"CTE_NOTA_FISCAL", "NR_PRE_CTE", "ATLAS", null});
		listaProcessar.add(new String[]{"CTE_OBSERVACAO", "NR_PRE_CTE", "ATLAS", null});
		listaProcessar.add(new String[]{"CTE_SUBSTITUICAO", "NR_PRE_CTE", "ATLAS", null});
		listaProcessar.add(new String[]{"CTE_TRANSFERENCIA", "NR_PRE_CTE", "ATLAS", null});
		listaProcessar.add(new String[]{"CTE_UNIDADE_MEDIDA", "NR_PRE_CTE", "ATLAS", null});
		listaProcessar.add(new String[]{"CTE_VALORES", "NR_PRE_CTE", "ATLAS", null});*/
		
		//listaProcessar.add(new String[]{"CTE_CONTROLE", "ID_CONTROLE", "ATLAS", null});
		/*listaProcessar.add(new String[]{"CTE_ESTADO_FEDERACAO", "SG_ESTADO_FEDERACAO", "ATLAS", null});
		listaProcessar.add(new String[]{"CTE_ESTADO_FEDERACAO_WS", "SG_ESTADO_FEDERACAO", "ATLAS", null});
		listaProcessar.add(new String[]{"CTE_FILIAL", "SG_FILIAL", "ATLAS", null});
		listaProcessar.add(new String[]{"CTE_HISTORICO_CONTIGENCIA", "ID_CTE_HISTORICO_CONTIGENCIA", "ATLAS", null});
		*/
		//listaProcessar.add(new String[]{"CTE_INUTILIZACAO", "ID_INUTILIZACAO", "ATLAS", null});
		//listaProcessar.add(new String[]{"CTE_PROCESSO", "ID_PROCESSO", "ATLAS", null});
		/*listaProcessar.add(new String[]{"CTE_PROCESSO_DETALHE", "ID_PROCESSO_DETALHE", "ATLAS", null});
		listaProcessar.add(new String[]{"CTE_STATUS_RETORNO", "CD_RETORNO", "ATLAS", null});
		listaProcessar.add(new String[]{"CTE_TIPO_ENVIO", "ID_CTE_TIPO_ENVIO", "ATLAS", null});
		listaProcessar.add(new String[]{"CTE_TIPO_SERVICO", "ID_CTE_TIPO_SERVICO", "ATLAS", null});
		listaProcessar.add(new String[]{"CTE_VERSAO_APLICACAO", "ID_CTE_VERSAO_APLICACAO", "ATLAS", null});
		listaProcessar.add(new String[]{"CTE_WS_VERSAO_APLICACAO", "ID_CTE_TIPO_SERVICO", "ATLAS", null});
				
		*/
		//listaProcessar.add(new String[]{"CTE_STATUS_SEFAZ", "SG_ESTADO_FEDERACAO", "ATLAS", null});	
		
		//listaProcessar.add(new String[]{"NOTA_DIGITALIZACAO_ARQUIVO", "SG_FILIAL_ORIGEM", "ATLAS", "V"});
		
		//listaProcessar.add(new String[]{"MDFE_VIAGEM", "ID_MDFE_VIAGEM", "ATLAS", null});
		
		//listaProcessar.add(new String[]{"MDFE_VIAGEM_CONTROLE", "ID_MDFE_VIAGEM_CONTROLE", "ATLAS", null});	
		
		listaProcessar.add(new String[]{"CONHECTO_PRE_EDI", "NR_CGC_REMETENTE", "ATLAS", null});	
		
		
		System.out.println("");
		for (String[] string : listaProcessar) {
			
			//exemplo mascara id_mascara med  i
	       	CriarDAOCompleta dao = new CriarDAOCompleta();
	       	dao.execute(string[0], string[1], string[2], string[3]);
	       	System.out.println("");
		}
		System.out.println("Classes DAO criadas com sucesso!\n");
	}
	
	public void execute(String tabela, String id, String schema, String tipoId){
		try{
			new File("./output/").mkdir();
			limparOutPut("./output/factory/");
			limparOutPut("./output/implementacao/");
			limparOutPut("./output/interfaces/");
			limparOutPut("./output/vo/");
			System.out.println("Tabela " + tabela );
			System.out.println("\t Criando VO...");
		    CriarVO vo = new CriarVO(tabela, id, schema, tipoId);
		    vo.execute();
		    
		    System.out.println("\t Criando Interface...");
		    CriarInterfaceDAO interf = new CriarInterfaceDAO(tabela, id);
		    interf.execute();
		
		    System.out.println("\t Criando Factory...");
		    CriarFactory factory = new CriarFactory(tabela, id);
		    factory.execute();
		    
		    System.out.println("\t Criando Implementa��o da DAO...");
		    CriarImplementacaoDAO dao = new CriarImplementacaoDAO(tabela, id, schema, tipoId);
		    dao.execute();
		
		    
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	private void limparOutPut(String diretorio){
		
		File dir = new File(diretorio);
		
		if(!dir.exists()){
			dir.mkdir();
		}
		
		if(dir.isDirectory()){
			File[] listFiles = dir.listFiles();
			
			for (File file : listFiles) {
				file.delete();
			}
		}else if(dir.isFile()){
			dir.delete();
		}		
		
	}
	
	
}
