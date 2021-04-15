package com.dummy.myerp.model.bean.comptabilite;

import static org.hamcrest.CoreMatchers.is;

import java.math.BigDecimal;

import org.apache.commons.lang3.ObjectUtils;
import org.junit.Assert;
import org.junit.Test;


public class EcritureComptableTest {	
	
    private LigneEcritureComptable createLigne(Integer pCompteComptableNumero, String pDebit, String pCredit) {
        BigDecimal vDebit = pDebit == null ? null : new BigDecimal(pDebit);
        BigDecimal vCredit = pCredit == null ? null : new BigDecimal(pCredit);
        String vLibelle = ObjectUtils.defaultIfNull(vDebit, BigDecimal.ZERO)
                                     .subtract(ObjectUtils.defaultIfNull(vCredit, BigDecimal.ZERO)).toPlainString();
        LigneEcritureComptable vRetour = new LigneEcritureComptable(new CompteComptable(pCompteComptableNumero),
                                                                    vLibelle,
                                                                    vDebit, vCredit);
        return vRetour;
    }

    @Test
    public void isEquilibree() {
        EcritureComptable vEcriture;
        vEcriture = new EcritureComptable();

        vEcriture.setLibelle("Equilibrée");
        vEcriture.getListLigneEcriture().add(this.createLigne(1, "200.50", null));
        vEcriture.getListLigneEcriture().add(this.createLigne(1, "100.50", "33"));
        vEcriture.getListLigneEcriture().add(this.createLigne(2, null, "301"));
        vEcriture.getListLigneEcriture().add(this.createLigne(2, "40", "7"));
        Assert.assertTrue(vEcriture.toString(), vEcriture.isEquilibree());

        vEcriture.getListLigneEcriture().clear();
        vEcriture.setLibelle("Non équilibrée");
        vEcriture.getListLigneEcriture().add(this.createLigne(1, "10", null));
        vEcriture.getListLigneEcriture().add(this.createLigne(1, "20", "1"));
        vEcriture.getListLigneEcriture().add(this.createLigne(2, null, "30"));
        vEcriture.getListLigneEcriture().add(this.createLigne(2, "1", "2"));
        Assert.assertFalse(vEcriture.toString(), vEcriture.isEquilibree());
    }
    
    @Test
    public void getTotalDebitTest() {
    	
    	EcritureComptable vEcriture;
        vEcriture = new EcritureComptable();

        vEcriture.setLibelle("Test on method total debit");
        vEcriture.getListLigneEcriture().add(this.createLigne(1, "200.50", null));
        vEcriture.getListLigneEcriture().add(this.createLigne(1, "100.50", "33"));
        vEcriture.getListLigneEcriture().add(this.createLigne(2, null, "301"));
        vEcriture.getListLigneEcriture().add(this.createLigne(2, "40", "7"));
        
        Assert.assertEquals(vEcriture.getTotalDebit(), new BigDecimal("341.00"));
    	
    }
    
    @Test
    public void getTotalCreditTest() {
    	
    	EcritureComptable vEcriture;
        vEcriture = new EcritureComptable();

        vEcriture.setLibelle("Test on method total credit");
        vEcriture.getListLigneEcriture().add(this.createLigne(1, "200.50", null));
        vEcriture.getListLigneEcriture().add(this.createLigne(1, "100.50", "33"));
        vEcriture.getListLigneEcriture().add(this.createLigne(2, null, "301"));
        vEcriture.getListLigneEcriture().add(this.createLigne(2, "40", "7"));
        
        Assert.assertEquals(vEcriture.getTotalCredit(), new BigDecimal("341")); 
    	
    }
    
    @Test
	public void TestOnToStringMethod() {
    	
//    	JournalComptable jComptable = new JournalComptable();
//    	jComptable.setCode("AB23");
//    	jComptable.setLibelle("Journal d'écriture");
    	
    	EcritureComptable vEcriture;
        vEcriture = new EcritureComptable();
        
        vEcriture.setDate(null);
        vEcriture.setId(1);
        vEcriture.setJournal(null);        
        
        vEcriture.setLibelle("Ecriture test");
        vEcriture.setReference("AB-2000-00011");
        
        vEcriture.getListLigneEcriture().add(this.createLigne(1, "200.50", null));
        vEcriture.getListLigneEcriture().add(this.createLigne(1, "100.50", "33"));
		
		Assert.assertThat("EcritureComptable{id=1, journal=null, reference='AB-2000-00011', date=null, libelle='Ecriture test', totalDebit=301.00, totalCredit=33, "
				+ "listLigneEcriture=[\nLigneEcritureComptable{compteComptable=CompteComptable{numero=1, libelle='null'}, libelle='200.50', debit=200.50, credit=null}"
				+ "\nLigneEcritureComptable{compteComptable=CompteComptable{numero=1, libelle='null'}, libelle='67.50', debit=100.50, credit=33}\n]}", is(vEcriture.toString()));
		
	}
    
    


}