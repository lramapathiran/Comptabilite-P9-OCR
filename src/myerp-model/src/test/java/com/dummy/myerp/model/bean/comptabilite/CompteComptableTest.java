package com.dummy.myerp.model.bean.comptabilite;

import static org.hamcrest.CoreMatchers.is;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CompteComptableTest {
	
	@Before
	public void init() {
		
		CompteComptable cComptable;
		cComptable = new CompteComptable();
		List<CompteComptable> pList = new ArrayList<>();
		
		for (int i=0; i<4; i++) {
			cComptable = new CompteComptable(i,"Compte n°" + i);
			pList.add(cComptable);
		}
	}
	
	@Test
	public void getCompteComptableByNumeroTest() {
		
		CompteComptable cComptable;
		cComptable = new CompteComptable();
		List<CompteComptable> pList = new ArrayList<>();
		
		for (int i=0; i<4; i++) {
			cComptable = new CompteComptable(i,"Compte n°" + i);
			pList.add(cComptable);
		}
		
		Assert.assertEquals(pList.get(1), cComptable.getByNumero(pList, 1));	
		
	}
	
	@Test
	public void getCompteComptableByNumeroIfNumberDontMatchTest() {
		
		CompteComptable cComptable;
		cComptable = new CompteComptable();
		List<CompteComptable> pList = new ArrayList<>();
		
		for (int i=0; i<4; i++) {
			cComptable = new CompteComptable(i,"Compte n°" + i);
			pList.add(cComptable);
		}
		
		Assert.assertEquals(null, cComptable.getByNumero(pList, 6));	
		
	}
	
	@Test
	public void TestOnToStringMethod() {
		CompteComptable cComptable;
		cComptable = new CompteComptable(123,"Compte Client Nexus");
		
		Assert.assertThat("CompteComptable{numero=123, libelle='Compte Client Nexus'}", is(cComptable.toString()));
		
	}

}
