package com.dummy.myerp.model.bean.comptabilite;

import static org.hamcrest.CoreMatchers.is;

import org.junit.Test;

import org.junit.Assert;

public class SequenceEcritureComptableTest {
	
	@Test
	public void TestOnToStringMethod() {
		SequenceEcritureComptable sEComptable;
		sEComptable = new SequenceEcritureComptable(2021, 290);
		
		Assert.assertThat("SequenceEcritureComptable{annee=2021, derniereValeur=290}", is(sEComptable.toString()));
		
	}

}
