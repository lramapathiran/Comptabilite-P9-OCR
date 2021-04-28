package com.dummy.myerp.testbusiness.business;

import com.dummy.myerp.business.impl.manager.ComptabiliteManagerImpl;
import com.dummy.myerp.model.bean.comptabilite.*;
import com.dummy.myerp.technical.exception.FunctionalException;
import com.dummy.myerp.technical.exception.NotFoundException;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:bootstrapContext.xml")
@Transactional(propagation = Propagation.REQUIRED)
public class ComptabiliteManagerImplITTest extends BusinessTestCase{
	
	private ComptabiliteManagerImpl manager = new ComptabiliteManagerImpl();

	@Rule
	public ExpectedException exceptionRule = ExpectedException.none();

	//	vérifie que la liste des Ecritures Comptables est récupérée
	@Test
	public void getListEcritureComptableTest() {
		List<EcritureComptable> eComptableList = manager.getListEcritureComptable();
		assertFalse(eComptableList.isEmpty());
		assertTrue(eComptableList.toString(), eComptableList.stream().anyMatch(o -> o.getReference().equals("VE-2016/00002")));
	}

	//	vérifie que la liste des Comptes Comptables est récupérée
	@Test
	public void getListCompteComptableTest() {
		List<CompteComptable> cComptableList = manager.getListCompteComptable();
		assertFalse(cComptableList.isEmpty());
	}

	//	vérifie que la liste des Journaux Comptables est récupérée
	@Test
	public void getListJournalComptableTest() {
		List<JournalComptable> jComptableList = manager.getListJournalComptable();
		assertFalse(jComptableList.isEmpty());
	}

	//	vérifie que l'écriture comptable est bien récupérée par son id
	@Test
	public void getEcritureComptableByIdTest() throws NotFoundException {
		EcritureComptable eComptable = manager.getEcritureComptableById(-1);
		String reference = eComptable.getReference();
		Assert.assertEquals("AC-2016/00001",reference);
	}

	//	vérifie que la Séquence d'Ecriture Comptable est récupérée en fontion de l'année d'écriture et du code journal
	@Test
	public void getSequenceEcritureComptableByYearAndCodeTest() throws NotFoundException {
		
		int year = 2016;
		String journalCode = "AC";
		Integer expected = 40;
		SequenceEcritureComptable sec = manager.getSequenceEcritureComptableByYearAndCode(year,journalCode);
		Integer derniereValeur = sec.getDerniereValeur();
		Assert.assertEquals(expected, derniereValeur);
	}

	//	vérifie que l'Ecriture Comptables est bien insérée dans la base de données
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
        assertTrue(liste.toString(), liste.stream().anyMatch(o -> o.getReference().equals("AC-2018/00001")));
		
	}

	//	vérifie que les valeurs de l'Ecriture Comptable sont mis à jour en base de données
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

	//	vérifie qu'une écriture comptable peut bien être supprimée de la DB.
	@Test(expected = NotFoundException.class)
	public void deleteEcritureComptableTest() throws NotFoundException{

		manager.deleteEcritureComptable(-2);

		manager.getEcritureComptableById(-2);
	}

	//	vérifie que la Séquence d'Ecriture Comptable est bien insérée en DB
	@Test
	public void insertSequenceEcritureComptableTest() throws FunctionalException, NotFoundException {

		SequenceEcritureComptable sEcritureComptable;
		sEcritureComptable = new SequenceEcritureComptable(2021,1,"AC");

		//Insertion d'une séquence d'écriture correcte
		manager.insertSequenceEcritureComptable(sEcritureComptable);
		SequenceEcritureComptable sECDB = manager.getSequenceEcritureComptableByYearAndCode(2021,"AC");

		Assert.assertNotNull(sECDB);

	}

	//	vérifie que la Séquence d'Ecriture Comptable est bien mise à jour en DB
	@Test
	public void updateSequenceEcritureComptableTest() throws FunctionalException, NotFoundException {
		SequenceEcritureComptable sECDB = manager.getSequenceEcritureComptableByYearAndCode(2016,"VE");
		sECDB.setDerniereValeur(44);

		//update de la séquence d'écriture
		manager.updateSequenceEcritureComptable(sECDB);
		SequenceEcritureComptable sECDBUpdated = manager.getSequenceEcritureComptableByYearAndCode(2016,"VE");
		Integer expected = 44;
		Integer actual = sECDBUpdated.getDerniereValeur();
		Assert.assertEquals(expected, actual);

	}

	//	vérifie que la méthode checkEcritureComptable() fonctionne sans bug
	@Test
	public void checkEcritureComptableTest() throws Exception {

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

	//	vérifie que la règle de gestion 5 est bien vérifiée sur une ecriture comptzble qui ne la respecte pas au niveau de l'année
	@Test(expected = FunctionalException.class)
	public void checkEcritureComptableRG5WithYear() throws Exception {
		EcritureComptable vEcritureComptable;
		vEcritureComptable = new EcritureComptable();
		vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
		vEcritureComptable.setDate(new Date());
		vEcritureComptable.setLibelle("Libelle");
		vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
				null, new BigDecimal(123),
				null));
		vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(2),
				null, null,
				new BigDecimal(123)));

//      Reference avec la mauvaise année
		vEcritureComptable.setReference("AC-" + "2029" + "/00001");

		manager.checkEcritureComptable(vEcritureComptable);
	}

	//	vérifie que la règle de gestion 5 est bien vérifiée sur une ecriture comptzble qui ne la respecte pas au niveau du code journal
	@Test(expected = FunctionalException.class)
	public void checkEcritureComptableRG5WithCode() throws Exception {
		EcritureComptable vEcritureComptable;
		vEcritureComptable = new EcritureComptable();
		vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
		vEcritureComptable.setDate(new Date());
		vEcritureComptable.setLibelle("Libelle");
		vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
				null, new BigDecimal(123),
				null));
		vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(2),
				null, null,
				new BigDecimal(123)));

//      Référence avec le mauvais code
		vEcritureComptable.setReference("AB-" + "2020" + "/00001");

		manager.checkEcritureComptable(vEcritureComptable);
	}

	//	vérifie que la violation d'écriture comptable liée au pattern de la référence d'écriture est détectée
	@Test(expected = FunctionalException.class)
	public void checkEcritureComptableViolationWithWrongpattern() throws Exception {
		EcritureComptable vEcritureComptable;
		vEcritureComptable = new EcritureComptable();
		vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
		vEcritureComptable.setDate(new Date());
		vEcritureComptable.setLibelle("Libelle");
		vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
				null, new BigDecimal(123),
				null));
		vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(2),
				null, null,
				new BigDecimal(123)));

//      Référence ave mauvais pattern non valide
		vEcritureComptable.setReference("12-" + "2022" + "/00001");

		manager.checkEcritureComptable(vEcritureComptable);
	}

	//	vérifie que la règle de gestion 2 est bien vérifiée sur une ecriture comptzble qui ne la respecte pas.
	@Test(expected = FunctionalException.class)
	public void checkEcritureComptableRG2() throws Exception {

		EcritureComptable vEcritureComptable;
		vEcritureComptable = new EcritureComptable();
		vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
		vEcritureComptable.setDate(new Date());
		vEcritureComptable.setLibelle("Libelle");
		vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
				null, new BigDecimal(123),
				null));
		vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(2),
				null, null,
				new BigDecimal(1234)));
		manager.checkEcritureComptable(vEcritureComptable);
	}

	//	vérifie que la violation d'écriture comptable liée à la présence d'une unique ligne d'écriture est détectée
	@Test
	public void checkEcritureComptableViolation() throws Exception {

		exceptionRule.expect(FunctionalException.class);
		exceptionRule.expectMessage("L'écriture comptable ne respecte pas les règles de gestion.");

		EcritureComptable vEcritureComptable;
		vEcritureComptable = new EcritureComptable();
		vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
		vEcritureComptable.setDate(new Date());
		vEcritureComptable.setLibelle("Libelle");
		vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
				null, new BigDecimal(123),
				null));

		manager.checkEcritureComptable(vEcritureComptable);
	}

	//	vérifie que la règle de gestion 3 est bien vérifiée sur une ecriture comptzble qui ne la respecte pas.
	@Test
	public void checkEcritureComptableRG3() throws Exception {

		exceptionRule.expect(FunctionalException.class);
		exceptionRule.expectMessage("L'écriture comptable doit avoir au moins deux lignes : une ligne au débit et une ligne au crédit.");

		EcritureComptable vEcritureComptable;
		vEcritureComptable = new EcritureComptable();
		vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
		vEcritureComptable.setDate(new Date());
		vEcritureComptable.setLibelle("Libelle");
		vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
				null, null,
				null));
		vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
				null, null,
				null));

		manager.checkEcritureComptable(vEcritureComptable);
	}

	//	vérifie qu'une écriture comptable sans référence est bien vérifée par la méthode checkEcritureComptable()
	@Test
	public void checkEcritureComptableWithNoReference() throws FunctionalException{

		EcritureComptable vEcritureComptable;
		vEcritureComptable = new EcritureComptable();
		vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
		vEcritureComptable.setDate(new Date());
		vEcritureComptable.setLibelle("Libelle");
		vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
				null, new BigDecimal(123),
				null));
		vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
				null, null, new BigDecimal(123)));
		vEcritureComptable.setReference(null);

		manager.checkEcritureComptable(vEcritureComptable);
	}

	//	vérifie qu'une nouvelle écriture comptable possèdant une référence déjà existante est bien détectée
	@Test
	public void checkEcritureComptableWithReferenceMatched() throws FunctionalException, NotFoundException {

		exceptionRule.expect(FunctionalException.class);
		exceptionRule.expectMessage("Une autre écriture comptable existe déjà avec la même référence.");

		EcritureComptable vEcritureComptable = manager.getEcritureComptableById(-1);
		vEcritureComptable.setId(null);
		manager.checkEcritureComptable(vEcritureComptable);
	}

	//	vérifie qu'une écriture comptable non équilibrée est détectée
	@Test(expected = FunctionalException.class)
	public void checkEcritureComptableNonEquilibre() throws FunctionalException{

		EcritureComptable vEcritureComptable;
		vEcritureComptable = new EcritureComptable();
		vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
		vEcritureComptable.setDate(new Date());
		vEcritureComptable.setLibelle("Libelle");
		vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
				null, new BigDecimal(123),
				null));
		vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
				null, new BigDecimal(123),
				null));
		vEcritureComptable.setReference("AC-2021/00002");

		manager.checkEcritureComptable(vEcritureComptable);
	}

	//	vérifie qu'une écriture comptable avec une référence déjà existante est bien détectée par 2 id non identiques
	@Test(expected = FunctionalException.class)
	public void checkEcritureComptableWithECRefIdNotMatched() throws FunctionalException{

		EcritureComptable vEcritureComptable;
		vEcritureComptable = new EcritureComptable();
		vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
		vEcritureComptable.setDate(new Date());
		vEcritureComptable.setLibelle("Libelle");
		vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
				null, new BigDecimal(123),
				null));
		vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
				null, new BigDecimal(123),
				null));

		manager.insertEcritureComptable(vEcritureComptable);

		EcritureComptable ECRef = vEcritureComptable;
		ECRef.setId(20);

		manager.checkEcritureComptable(ECRef);
	}

	//	vérifie que la méthode addReference() update la séquence d'écriture comptable lorsqu'une nouvelle écriture comptable est créée
	//	et qu'une séquence d'écriture ayant la même année que l'écriture comptable existe déjà
	@Test
	public void addReferenceWithUpdateSequenceTest() throws NotFoundException {

		EcritureComptable vEcritureComptable = manager.getEcritureComptableById(-1);
		vEcritureComptable.setReference(null);
		manager.addReference(vEcritureComptable);

		Assert.assertEquals("AC-2016/00041", vEcritureComptable.getReference());

	}

	//	vérifie que la méthode addReference() crée/insère une nouvelle séquence d'écriture comptable en DB lorsqu'une nouvelle écriture comptable est créée
	//	et qu'aucune séquence d'écriture ayant la même année que l'écriture existe.
	@Test
	public void addReferenceWithInsertSequenceTest() throws NotFoundException, FunctionalException {

		EcritureComptable vEcritureComptable;
		vEcritureComptable = new EcritureComptable();
		vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
		vEcritureComptable.setDate(new Date());
		vEcritureComptable.setLibelle("Libelle");
		vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(401),
				null, new BigDecimal(123),
				null));
		vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(401),
				null, null, new BigDecimal(123)));

		manager.insertEcritureComptable(vEcritureComptable);

		manager.addReference(vEcritureComptable);

		Assert.assertEquals("AC-2021/00001", vEcritureComptable.getReference());

	}

}
