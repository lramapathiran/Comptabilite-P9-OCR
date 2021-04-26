package com.dummy.myerp.model.bean.comptabilite;

import static org.hamcrest.CoreMatchers.is;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class JournalComptableTest {
	
	@Test
	public void getJournalComptableByCodeTest() {
		
		JournalComptable jComptable;
		jComptable = new JournalComptable();
		List<JournalComptable> pList = new ArrayList<>();
		
		pList.add(new JournalComptable("123","Journal 1"));
		pList.add(new JournalComptable("1234","Journal 2"));
		
		
		Assert.assertEquals(pList.get(0), jComptable.getByCode(pList, "123"));
	}
	
	@Test
	public void getJournalComptableByCodeIfCodeDontMatchTest() {
		
		JournalComptable jComptable;
		jComptable = new JournalComptable();
		List<JournalComptable> pList = new ArrayList<>();
		
		pList.add(new JournalComptable("123","Journal 1"));
		pList.add(new JournalComptable("1234","Journal 2"));
		
		
		Assert.assertEquals(null, jComptable.getByCode(pList, "1253"));
	}
	
	@Test
	public void TestOnToStringMethod() {
		JournalComptable jComptable;
		jComptable = new JournalComptable("123","Journal 1");
		
		Assert.assertThat("JournalComptable{code='123', libelle='Journal 1'}", is(jComptable.toString()));
		
	}

}
