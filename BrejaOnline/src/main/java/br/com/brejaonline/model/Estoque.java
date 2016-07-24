package br.com.brejaonline.model;

import java.util.ArrayList;
import java.util.Collection;

public class Estoque {
	private Collection<Cerveja> cervejas = new ArrayList<Cerveja>();
	
	
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
		this.cervejas.add(primeiraCerveja);
		this.cervejas.add(segundaCerveja);
	}

	public Collection<Cerveja> listarCervejas() {
		return new ArrayList<Cerveja>(this.cervejas);
	}
	
	public void adicionarCerveja (Cerveja cerveja) {
		this.cervejas.add(cerveja);
	}
}
