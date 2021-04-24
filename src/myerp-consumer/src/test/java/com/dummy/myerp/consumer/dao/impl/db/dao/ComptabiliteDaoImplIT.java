package com.dummy.myerp.consumer.dao.impl.db.dao;

import com.dummy.myerp.model.bean.comptabilite.CompteComptable;
import com.dummy.myerp.model.bean.comptabilite.EcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.JournalComptable;
import com.dummy.myerp.model.bean.comptabilite.LigneEcritureComptable;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Propagation;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/com/dummy/myerp/consumer/applicationContext.xml")
@Transactional(propagation = Propagation.REQUIRED)
public class ComptabiliteDaoImplIT {

    private ComptabiliteDaoImpl dao = new ComptabiliteDaoImpl();

    @Test
    public void insertEcritureComptableTest(){

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
        dao.insertEcritureComptable(vEcritureComptable);
        List<EcritureComptable> list = dao.getListEcritureComptable();
        assertTrue(list.toString(), list.stream().filter(o -> o.getReference().equals("AC-2018/00001")).findFirst().isPresent());

    }
}
