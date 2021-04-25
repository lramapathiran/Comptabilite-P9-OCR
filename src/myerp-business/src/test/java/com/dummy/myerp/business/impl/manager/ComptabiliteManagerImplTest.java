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
    
    
    @Before
    public void initConfigure() {
    	ComptabiliteManagerImpl.configure(pBusinessProxy, pDaoProxy, pTransactionManager);
    	when(pDaoProxy.getComptabiliteDao()).thenReturn(comptabiliteDao);
    }
    
    @Before
    public void initEcritureComptable() {
	    vEcritureComptable = new EcritureComptable();
    }

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();
    
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
    
//    Tests unitaire avec mockito

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
    
    @Test
    public void checkEcritureComptableContextWithNoReference() throws FunctionalException{
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
    
  @Test(expected = FunctionalException.class)
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
       
       when(pDaoProxy.getComptabiliteDao().getEcritureComptableByRef(pReference)).thenReturn(null);
       
       manager.checkEcritureComptableContext(vEcritureComptable);
  }

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
