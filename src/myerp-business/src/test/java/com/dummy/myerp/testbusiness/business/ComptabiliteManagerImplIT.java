package com.dummy.myerp.testbusiness.business;

import org.junit.runner.RunWith;
import org.springframework.transaction.annotation.Transactional;

import com.dummy.myerp.business.impl.manager.ComptabiliteManagerImpl;
import com.dummy.myerp.model.bean.comptabilite.CompteComptable;
import com.dummy.myerp.model.bean.comptabilite.EcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.JournalComptable;
import com.dummy.myerp.model.bean.comptabilite.LigneEcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.SequenceEcritureComptable;
import com.dummy.myerp.technical.exception.FunctionalException;
import com.dummy.myerp.technical.exception.NotFoundException;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.springframework.transaction.annotation.Propagation;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:bootstrapContext.xml")
@Transactional(propagation = Propagation.REQUIRED)
public class ComptabiliteManagerImplIT extends BusinessTestCase{
	
	private ComptabiliteManagerImpl manager = new ComptabiliteManagerImpl();
		
	@Test
	public void getListEcritureComptableTest() {
		List<EcritureComptable> eComptableList = manager.getListEcritureComptable();
		assertFalse(eComptableList.isEmpty());
	} 
	
	@Test
	public void getListCompteComptableTest() {
		List<CompteComptable> cComptableList = manager.getListCompteComptable();
		assertFalse(cComptableList.isEmpty());
	} 
	
	@Test
	public void getListJournalComptableTest() {
		List<JournalComptable> jComptableList = manager.getListJournalComptable();
		assertFalse(jComptableList.isEmpty());
	} 
	
	@Test
	public void getEcritureComptableByIdtest() throws NotFoundException {
		EcritureComptable ec = manager.getEcritureComptableById(-1);
		String reference = ec.getReference();
		Assert.assertEquals("AC-2016/00001",reference);
	}
	
	
	
	@Test
	public void getListSequenceEcritureComptableTest() throws NotFoundException {
		
//		SequenceEcritureComptable sEComptableToCompare = new SequenceEcritureComptable(2016,40,"AC");
		int year = 2016;
		SequenceEcritureComptable sec = manager.getSequenceEcritureComptableByYear(year);
		String code = sec.getJournalCode();
		Assert.assertEquals("AC", code);
	} 	
	
//	@Test
//	public void insertEcritureComptableTest() throws FunctionalException {
//		
//		EcritureComptable vEcritureComptable;
//        vEcritureComptable = new EcritureComptable();
//        vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
//        Calendar calendar = new GregorianCalendar(2018,10,23);
//        vEcritureComptable.setDate(calendar.getTime());
//        vEcritureComptable.setReference("AC-2018/00001");
//        vEcritureComptable.setLibelle("Insertion test ecriture comptable");
//        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(401),
//                                                                                 null, new BigDecimal("90.5"),
//                                                                                 null));
//        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(512),
//                                                                                 null, null,
//                                                                                 new BigDecimal("90.5")));
//        
//        //Insertion d'une écriture correcte
//        manager.insertEcritureComptable(vEcritureComptable);
//        List <EcritureComptable> liste = manager.getListEcritureComptable();
//        assertTrue(liste.toString(), liste.stream().filter(o -> o.getReference().equals("AC-2018/00001")).findFirst().isPresent());
//		
//	}

}
