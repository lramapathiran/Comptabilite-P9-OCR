package com.dummy.myerp.model.bean.comptabilite;

import static org.hamcrest.CoreMatchers.is;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class JournalComptableTest {

	//	Vérifie que la méthode getJournalByCode récupère le bon journal en fonction du code journal
	@Test
	public void getJournalComptableByCodeTest() {
		
		JournalComptable jComptable;
		jComptable = new JournalComptable();
		List<JournalComptable> pList = new ArrayList<>();
		
		pList.add(new JournalComptable("123","Journal 1"));
		pList.add(new JournalComptable("1234","Journal 2"));
		
		
		Assert.assertEquals(pList.get(0), jComptable.getByCode(pList, "123"));
	}

	//	Vérifie que la méthode getJournalByCode ne retourne rien
	//	lorsque le code journal utilisé pour trouver le journal comptable dans la liste n'existe pas.
	@Test
	public void getJournalComptableByCodeIfCodeDontMatchTest() {
		
		JournalComptable jComptable;
		jComptable = new JournalComptable();
		List<JournalComptable> pList = new ArrayList<>();
		
		pList.add(new JournalComptable("123","Journal 1"));
		pList.add(new JournalComptable("1234","Journal 2"));
		
		
		Assert.assertEquals(null, jComptable.getByCode(pList, "1253"));
	}

	// Vérifie que la méthode toString() retourne le bon résultat dans la classe JournalComptable.java
	@Test
	public void TestOnToStringMethod() {
		JournalComptable jComptable;
		jComptable = new JournalComptable("123","Journal 1");
		
		Assert.assertThat("JournalComptable{code='123', libelle='Journal 1'}", is(jComptable.toString()));
		
	}

}
