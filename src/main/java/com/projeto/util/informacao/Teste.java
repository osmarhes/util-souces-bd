package com.projeto.util.informacao;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


public class Teste {
	public void main(String args[]){
		Map mapa = new HashMap();
		
		Set chaves = mapa.keySet();
		
		Iterator iChaves = chaves.iterator();
		while (iChaves.hasNext()){
			String chave = (String)iChaves.next();
			Object valor = mapa.get(chave);
		}
	}

}
