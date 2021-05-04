package com.dummy.myerp.business.impl.manager;

import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import org.mockito.junit.MockitoJUnitRunner;

import com.dummy.myerp.business.contrat.BusinessProxy;
import com.dummy.myerp.business.impl.TransactionManager;
import com.dummy.myerp.consumer.dao.contrat.ComptabiliteDao;
import com.dummy.myerp.consumer.dao.contrat.DaoProxy;
import com.dummy.myerp.model.bean.comptabilite.CompteComptable;
import com.dummy.myerp.model.bean.comptabilite.EcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.JournalComptable;
import com.dummy.myerp.model.bean.comptabilite.LigneEcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.SequenceEcritureComptable;
import com.dummy.myerp.technical.exception.FunctionalException;
import com.dummy.myerp.technical.exception.NotFoundException;

@RunWith(MockitoJUnitRunner.class)
public class ComptabiliteManagerImplTest {

    private ComptabiliteManagerImpl manager = new ComptabiliteManagerImpl();


    @Mock
    private DaoProxy pDaoProxy;
    
    @Mock
    private ComptabiliteDao comptabiliteDao;
    
    @Mock
    private EcritureComptable vEcritureComptable;
    
    @Mock
    private BusinessProxy pBusinessProxy;
    
    @Mock
    private TransactionManager pTransactionManager;
    
    // Méthode d'initialisation avant chaque test unitaire pour initialiser les proxy et le transaction manager
    @Before
    public void initConfigure() {
    	ComptabiliteManagerImpl.configure(pBusinessProxy, pDaoProxy, pTransactionManager);
    	when(pDaoProxy.getComptabiliteDao()).thenReturn(comptabiliteDao);
    }

    // Méthode d'initialisation avant chaque test unitaire pour créer une instance du bean Ecriture Comptable
    @Before
    public void initEcritureComptable() {
	    vEcritureComptable = new EcritureComptable();
    }

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    //	vérifie que la méthode checkEcritureComptableUnit() fonctionne sans bug avec un eecriture comptable respectant toutes les règles de gestion
    @Test
    public void checkEcritureComptableUnit() throws Exception {
        vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        vEcritureComptable.setDate(new Date());
        vEcritureComptable.setLibelle("Libelle");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                                                                                 null, new BigDecimal(123),
                                                                                 null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(2),
                                                                                 null, null,
                                                                                 new BigDecimal(123)));
        
//      Modification du test unitaire ajout de la valeur reference pour vEcritureComptable
        vEcritureComptable.setReference("AC-" + "2021" + "/00001");
        
        manager.checkEcritureComptableUnit(vEcritureComptable);
    }

    //	vérifie que la règle de gestion 5 est bien vérifiée sur une ecriture comptzble qui ne la respecte pas au niveau de l'année
    @Test(expected = FunctionalException.class)
    public void checkEcritureComptableUnitRG5WithYear() throws Exception {
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
        
        manager.checkEcritureComptableUnit(vEcritureComptable);
    }


    //	vérifie que la règle de gestion 5 est bien vérifiée sur une ecriture comptzble qui ne la respecte pas au niveau du code journal
    @Test(expected = FunctionalException.class)
    public void checkEcritureComptableUnitRG5WithCode() throws Exception {
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
        
        manager.checkEcritureComptableUnit(vEcritureComptable);
    }

    //	vérifie que la violation d'écriture comptable liée au pattern de la référence d'écriture est détectée
    @Test(expected = FunctionalException.class)
    public void checkEcritureComptableUnitViolationWithWrongpattern() throws Exception {

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
        
        manager.checkEcritureComptableUnit(vEcritureComptable);
    }

    //	vérifie que la règle de gestion 2 est bien vérifiée sur une ecriture comptzble qui ne la respecte pas.
    @Test(expected = FunctionalException.class)
    public void checkEcritureComptableUnitRG2() throws Exception {
        vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        vEcritureComptable.setDate(new Date());
        vEcritureComptable.setLibelle("Libelle");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                                                                                 null, new BigDecimal(123),
                                                                                 null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(2),
                                                                                 null, null,
                                                                                 new BigDecimal(1234)));
        manager.checkEcritureComptableUnit(vEcritureComptable);
    }

    //	vérifie que la violation d'écriture comptable liée à la présence d'une unique ligne d'écriture est détectée
    @Test
    public void checkEcritureComptableUnitViolation() throws Exception {

        exceptionRule.expect(FunctionalException.class);
        exceptionRule.expectMessage("L'écriture comptable ne respecte pas les règles de gestion.");

        vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        vEcritureComptable.setDate(new Date());
        vEcritureComptable.setLibelle("Libelle");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                                                                                 null, new BigDecimal(123),
                                                                                 null));

        manager.checkEcritureComptableUnit(vEcritureComptable);
    }

    //	vérifie que la règle de gestion 3 est bien vérifiée sur une ecriture comptzble qui ne la respecte pas.
    @Test
    public void checkEcritureComptableUnitRG3() throws Exception {

        exceptionRule.expect(FunctionalException.class);
        exceptionRule.expectMessage("L'écriture comptable doit avoir au moins deux lignes : une ligne au débit et une ligne au crédit.");

        vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        vEcritureComptable.setDate(new Date());
        vEcritureComptable.setLibelle("Libelle");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                null, null,
                null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                null, null,
                null));

        manager.checkEcritureComptableUnit(vEcritureComptable);
    }

    //	vérifie que la méthode checkEcritureComptable() fonctionne sans bug pour la vérification
    //	d'une écriture comptable avec une référence non préexistante
    @Test
    public void checkEcritureComptable() throws NotFoundException, FunctionalException {
        vEcritureComptable.setJournal(new JournalComptable("BC", "Achat"));
        vEcritureComptable.setDate(new Date());
        vEcritureComptable.setLibelle("Libelle");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                null, new BigDecimal(123),
                null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(2),
                null, null,
                new BigDecimal(123)));

        vEcritureComptable.setReference("BC-" + "2021" + "/11111");

        String pReference = vEcritureComptable.getReference();

        when(pDaoProxy.getComptabiliteDao().getEcritureComptableByRef(pReference)).thenThrow(NotFoundException.class);

        manager.checkEcritureComptable(vEcritureComptable);
    }

    //	vérifie que la méthode checkEcritureComptableContext() fonctionne sans bug pour la vérification
    //	d'une écriture comptable n'ayant aucune référence
    @Test
    public void checkEcritureComptableContextWithNoReference() throws NotFoundException, FunctionalException{
    	 vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
         vEcritureComptable.setDate(new Date());
         vEcritureComptable.setLibelle("Libelle");
         vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                                                                                  null, new BigDecimal(123),
                                                                                  null));
         vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                                                                                  null, new BigDecimal(123),
                                                                                  null));
         vEcritureComptable.setReference(null);

         manager.checkEcritureComptableContext(vEcritureComptable);
    }

    //	vérifie que la méthode checkEcritureComptableContext() fonctionne sans bug pour la vérification
    //	d'une écriture comptable ayant une référence non pré-existante en DB
    @Test
    public void checkEcritureComptableContextWithReferenceNoMatched() throws NotFoundException, FunctionalException{
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
        String pReference = vEcritureComptable.getReference();

        when(pDaoProxy.getComptabiliteDao().getEcritureComptableByRef(pReference)).thenThrow(NotFoundException.class);

        manager.checkEcritureComptableContext(vEcritureComptable);
    }

    //	vérifie qu'une nouvelle écriture comptable possèdant une référence déjà existante est bien détectée
    @Test(expected = FunctionalException.class)
    public void checkEcritureComptableContextWithReferenceMatched() throws NotFoundException, FunctionalException{
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
         String pReference = vEcritureComptable.getReference();
         
         when(pDaoProxy.getComptabiliteDao().getEcritureComptableByRef(pReference)).thenReturn(vEcritureComptable);
         
         manager.checkEcritureComptableContext(vEcritureComptable);
    }

    //	vérifie qu'une écriture comptable avec une référence déjà existante est bien détectée par 2 id non identiques
    @Test(expected = FunctionalException.class)
    public void checkEcritureComptableContextWithECRefIdNotMatched() throws NotFoundException, FunctionalException{
      vEcritureComptable.setId(-11);
      vEcritureComptable.setReference("AC-2016/00001");
      String pReference = vEcritureComptable.getReference();

      EcritureComptable EComptableDB = new EcritureComptable();
      EComptableDB.setId(-1);
      EComptableDB.setReference(pReference);

      when(pDaoProxy.getComptabiliteDao().getEcritureComptableByRef(pReference)).thenReturn(EComptableDB);

      manager.checkEcritureComptableContext(vEcritureComptable);
  }

    //	vérifie que la méthode addReference() update la séquence d'écriture comptable lorsqu'une nouvelle écriture comptable est créée
    //	et qu'une séquence d'écriture ayant la même année que l'écriture comptable existe déjà
    @Test
    public void addReferenceWithUpdateSequenceTest() throws NotFoundException {

    	vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
	    vEcritureComptable.setDate(new Date());
	    vEcritureComptable.setLibelle("Libelle");
	    vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
	                                                                               null, new BigDecimal(123),
	                                                                               null));
	    vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
	                                                                               null, new BigDecimal(123),
	                                                                         null));

        Date date = vEcritureComptable.getDate();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        Integer year = c.get(Calendar.YEAR);

        String code = vEcritureComptable.getJournal().getCode();


	    SequenceEcritureComptable sEcritureComptable = new SequenceEcritureComptable(2021, 00002, "AC");

    	when(pDaoProxy.getComptabiliteDao().getSequenceEcritureComptableByYearAndCode(year,code)).thenReturn(sEcritureComptable);
    	manager.addReference(vEcritureComptable);

    	verify(comptabiliteDao).getSequenceEcritureComptableByYearAndCode(year,code);
    	Assert.assertEquals("AC-2021/00003", vEcritureComptable.getReference());

    }

    //	vérifie que la méthode addReference() crée/insère une nouvelle séquence d'écriture comptable en DB lorsqu'une nouvelle écriture comptable est créée
    //	et qu'aucune séquence d'écriture ayant la même année que l'écriture existe.
    @Test
    public void addReferenceWithInsertSequenceTest() throws NotFoundException {

    	vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
	    vEcritureComptable.setDate(new Date());
	    vEcritureComptable.setLibelle("Libelle");
	    vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
	                                                                               null, new BigDecimal(123),
	                                                                               null));
	    vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
	                                                                               null, new BigDecimal(123),
	                                                                         null));

        Date date = vEcritureComptable.getDate();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        Integer year = c.get(Calendar.YEAR);

        String code = vEcritureComptable.getJournal().getCode();

        when(pDaoProxy.getComptabiliteDao().getSequenceEcritureComptableByYearAndCode(year,code)).thenThrow(NotFoundException.class);
        manager.addReference(vEcritureComptable);

        verify(comptabiliteDao).getSequenceEcritureComptableByYearAndCode(year,code);
    	Assert.assertEquals("AC-2021/00001", vEcritureComptable.getReference());

    }




}
