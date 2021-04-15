package com.dummy.myerp.business.impl.manager;

import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.dummy.myerp.business.contrat.BusinessProxy;
import com.dummy.myerp.business.impl.TransactionManager;
import com.dummy.myerp.consumer.dao.contrat.ComptabiliteDao;
import com.dummy.myerp.consumer.dao.contrat.DaoProxy;
import com.dummy.myerp.model.bean.comptabilite.CompteComptable;
import com.dummy.myerp.model.bean.comptabilite.EcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.JournalComptable;
import com.dummy.myerp.model.bean.comptabilite.LigneEcritureComptable;
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
    public void checkEcritureComptableUnitRG5WithWrongpattern() throws Exception {
        vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        vEcritureComptable.setDate(new Date());
        vEcritureComptable.setLibelle("Libelle");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                                                                                 null, new BigDecimal(123),
                                                                                 null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(2),
                                                                                 null, null,
                                                                                 new BigDecimal(123)));
        
//      Référence avec le mauvais pattern
        vEcritureComptable.setReference("ABCDRE-" + "202200" + "/00001");
        
        manager.checkEcritureComptableUnit(vEcritureComptable);
    }

    @Test(expected = FunctionalException.class)
    public void checkEcritureComptableUnitViolation() throws Exception {
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

    @Test(expected = FunctionalException.class)
    public void checkEcritureComptableUnitRG3() throws Exception {
        vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        vEcritureComptable.setDate(new Date());
        vEcritureComptable.setLibelle("Libelle");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                                                                                 null, new BigDecimal(123),
                                                                                 null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                                                                                 null, new BigDecimal(123),
                                                                                 null));
        manager.checkEcritureComptableUnit(vEcritureComptable);
    }
    
//    Tests unitaire avec mockito
    
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
    
//    @Test
//    public void addReferenceWithUpdateSequenceTest() {
//    	
//    	vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
//	    vEcritureComptable.setDate(new Date());
//	    vEcritureComptable.setLibelle("Libelle");
//	    vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
//	                                                                               null, new BigDecimal(123),
//	                                                                               null));
//	    vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
//	                                                                               null, new BigDecimal(123),
//	                                                                         null));
//	    
//
//	    List<SequenceEcritureComptable> sequenceMockitoList = new ArrayList<>();
//	    sequenceMockitoList.add(new SequenceEcritureComptable(2021, 00002, "AC"));
//	    sequenceMockitoList.add(new SequenceEcritureComptable(2009, 00002, "AB"));
//	    
//	    
//    	when(pDaoProxy.getComptabiliteDao().getListSequenceEcritureComptable()).thenReturn(sequenceMockitoList);
//    	manager.addReference(vEcritureComptable);
//    	
//    	verify(comptabiliteDao).getListSequenceEcritureComptable();
//    	Assert.assertEquals("AC-2021/00003", vEcritureComptable.getReference());
//    	
//    }
//    
//    @Test
//    public void addReferenceWithInsertSequenceTest() {
//    	
//    	vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
//	    vEcritureComptable.setDate(new Date());
//	    vEcritureComptable.setLibelle("Libelle");
//	    vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
//	                                                                               null, new BigDecimal(123),
//	                                                                               null));
//	    vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
//	                                                                               null, new BigDecimal(123),
//	                                                                         null));
//	    
//
//	    List<SequenceEcritureComptable> sequenceMockitoList = new ArrayList<>();
//	    sequenceMockitoList.add(new SequenceEcritureComptable(2009, 00002, "AB"));
//	    sequenceMockitoList.add(new SequenceEcritureComptable(2011, 00002, "AC"));
//	    
//	    
//    	when(pDaoProxy.getComptabiliteDao().getListSequenceEcritureComptable()).thenReturn(sequenceMockitoList);
//    	manager.addReference(vEcritureComptable);
//    	
//    	verify(comptabiliteDao).getListSequenceEcritureComptable();
//    	Assert.assertEquals("AC-2021/00001", vEcritureComptable.getReference());
//    	
//    }


}
