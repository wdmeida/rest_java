package br.com.brejaonline.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Estoque {
	private Map<String, Cerveja> cervejas = new HashMap<String, Cerveja>();
	
	
	/*
	 * Cria algumas cervejas já no construtor para testar a aplicação.
	 */
	public Estoque() {
		Cerveja primeiraCerveja = new Cerveja("Stella Artois", 
				"A cerveja belga mais francesa do mundo :)",
				"Artois", 
				Cerveja.Tipo.LAGER);
		Cerveja segundaCerveja = new Cerveja("Erdinger Weissbier",
				"Cerveja de trigo alemã",
				"Erdinger Weissbräu",
				Cerveja.Tipo.WEIZEN);
		this.cervejas.put(primeiraCerveja.getNome(), primeiraCerveja);
		this.cervejas.put(segundaCerveja.getNome(), segundaCerveja);
	}

	public Collection<Cerveja> listarCervejas() {
		return new ArrayList<Cerveja>(this.cervejas.values());
	}
	
	public void adicionarCerveja (Cerveja cerveja) {
		this.cervejas.put(cerveja.getNome(), cerveja);
	}
	
	public Cerveja recuperarCervejaPeloNome (String nome) {
		return this.cervejas.get(nome);
	}
}//class Estoque
