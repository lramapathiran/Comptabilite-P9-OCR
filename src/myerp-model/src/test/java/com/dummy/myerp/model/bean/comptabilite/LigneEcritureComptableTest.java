package com.dummy.myerp.model.bean.comptabilite;

import static org.hamcrest.CoreMatchers.is;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

class LigneEcritureComptableTest {

	@Test
	public void TestOnToStringMethod() {
		LigneEcritureComptable lEComptable;
		lEComptable = new LigneEcritureComptable();
		
		CompteComptable cComptable = new CompteComptable(1,"Compte n° 1");
		
		lEComptable.setCompteComptable(cComptable);
		lEComptable.setCredit(new BigDecimal("200.00"));
		lEComptable.setDebit(new BigDecimal("50"));
		lEComptable.setLibelle("Ligne d'écriture 1");
		
		Assert.assertThat("LigneEcritureComptable{compteComptable=CompteComptable{numero=1, libelle='Compte n° 1'}, "
				+ "libelle='Ligne d'écriture 1', debit=50, credit=200.00}", is(lEComptable.toString()));
		
	}

}
