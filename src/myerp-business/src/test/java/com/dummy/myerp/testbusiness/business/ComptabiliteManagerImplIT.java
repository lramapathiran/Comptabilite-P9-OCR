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
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:bootstrapContext.xml")
@Transactional(propagation = Propagation.REQUIRED)
public class ComptabiliteManagerImplIT extends BusinessTestCase{
	
	private ComptabiliteManagerImpl manager = new ComptabiliteManagerImpl();

	@Test
	public void getListEcritureComptableTest() {
		List<EcritureComptable> eComptableList = manager.getListEcritureComptable();
		assertFalse(eComptableList.isEmpty());
		assertTrue(eComptableList.toString(), eComptableList.stream().filter(o -> o.getReference().equals("VE-2016/00002")).findFirst().isPresent());
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
		EcritureComptable eComptable = manager.getEcritureComptableById(-1);
		String reference = eComptable.getReference();
		Assert.assertEquals("AC-2016/00001",reference);
	}
	
	@Test
	public void getSequenceEcritureComptableTestByYearAndCode() throws NotFoundException {
		
		int year = 2016;
		String journalCode = "AC";
		Integer expected = 40;
		SequenceEcritureComptable sec = manager.getSequenceEcritureComptableByYearAndCode(year,journalCode);
		Integer derniereValeur = sec.getDerniereValeur();
		Assert.assertEquals(expected, derniereValeur);
	} 	
	
	@Test
	public void insertEcritureComptableTest() throws FunctionalException {
		
		EcritureComptable vEcritureComptable;
        vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        Calendar calendar = new GregorianCalendar(2018,10,23);
        vEcritureComptable.setDate(calendar.getTime());
        vEcritureComptable.setReference("AC-2018/00001");
        vEcritureComptable.setLibelle("Insertion test ecriture comptable");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(401),
                                                                                 null, new BigDecimal("90.5"),
                                                                                 null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(512),
                                                                                 null, null,
                                                                                 new BigDecimal("90.5")));
        
        //Insertion d'une écriture correcte
        manager.insertEcritureComptable(vEcritureComptable);
        List <EcritureComptable> liste = manager.getListEcritureComptable();
        assertTrue(liste.toString(), liste.stream().filter(o -> o.getReference().equals("AC-2018/00001")).findFirst().isPresent());
		
	}
	
	@Test
	public void updateEcritureComptableTest() throws NotFoundException, FunctionalException {
		
		EcritureComptable ec = manager.getEcritureComptableById(-2);
		String ecRef = ec.getReference();
		
		Assert.assertEquals("VE-2016/00002",ecRef);
		
		ec.setReference("VE-2016/00120");
		
		manager.updateEcritureComptable(ec);
		
		EcritureComptable ecUpdated = manager.getEcritureComptableById(-2);
		String ecRefUpdated = ecUpdated.getReference();
		
		Assert.assertEquals("VE-2016/00120",ecRefUpdated);		
		
	}

	@Test(expected = NotFoundException.class)
	public void deleteEcritureComptableTest() throws NotFoundException{

		manager.deleteEcritureComptable(-2);

		manager.getEcritureComptableById(-2);
	}

	@Test
	public void checkEcritureComptableInsertionTest() throws Exception {

		EcritureComptable eComptable = new EcritureComptable();
		eComptable.setJournal(new JournalComptable("AC", "Achat"));
		eComptable.setDate(new Date());
		eComptable.setLibelle("Libelle");
		eComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
				null, new BigDecimal(123),
				null));
		eComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(2),
				null, null,
				new BigDecimal(123)));

//      Modification du test unitaire ajout de la valeur reference pour vEcritureComptable
		eComptable.setReference("AC-" + "2021" + "/00001");

		manager.checkEcritureComptable(eComptable);
	}

}
