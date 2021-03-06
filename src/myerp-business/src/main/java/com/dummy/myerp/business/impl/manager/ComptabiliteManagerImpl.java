package com.dummy.myerp.business.impl.manager;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.TransactionStatus;
import com.dummy.myerp.business.contrat.manager.ComptabiliteManager;
import com.dummy.myerp.business.impl.AbstractBusinessManager;
import com.dummy.myerp.model.bean.comptabilite.CompteComptable;
import com.dummy.myerp.model.bean.comptabilite.EcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.JournalComptable;
import com.dummy.myerp.model.bean.comptabilite.LigneEcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.SequenceEcritureComptable;
import com.dummy.myerp.technical.exception.FunctionalException;
import com.dummy.myerp.technical.exception.NotFoundException;


/**
 * Comptabilite manager implementation.
 */
public class ComptabiliteManagerImpl extends AbstractBusinessManager implements ComptabiliteManager {

    // ==================== Attributs ====================


    // ==================== Constructeurs ====================
    /**
     * Instantiates a new Comptabilite manager.
     */
    public ComptabiliteManagerImpl() {
    }


    // ==================== Getters/Setters ====================
    @Override
    public List<CompteComptable> getListCompteComptable() {
        return getDaoProxy().getComptabiliteDao().getListCompteComptable();
    }


    @Override
    public List<JournalComptable> getListJournalComptable() {
        return getDaoProxy().getComptabiliteDao().getListJournalComptable();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<EcritureComptable> getListEcritureComptable() {
        return getDaoProxy().getComptabiliteDao().getListEcritureComptable();
    }

    @Override
    public SequenceEcritureComptable getSequenceEcritureComptableByYearAndCode(Integer year, String journalCode) throws NotFoundException {
    	return getDaoProxy().getComptabiliteDao().getSequenceEcritureComptableByYearAndCode(year, journalCode);
    }
    

    @Override
    public EcritureComptable getEcritureComptableById(int id) throws NotFoundException {
        EcritureComptable eComptable = null;
        try {
            eComptable = getDaoProxy().getComptabiliteDao().getEcritureComptable(id);
        } catch (NotFoundException nfe){
            throw new NotFoundException();
        }

        return eComptable ;
    }



    /**
     * {@inheritDoc}
     * @throws NotFoundException 
     */
    // TODO ?? tester
    @Override
    public synchronized void addReference(EcritureComptable pEcritureComptable) throws NotFoundException {
    	// TODO ?? impl??menter (travail fini)
        // Bien se r??ferer ?? la JavaDoc de cette m??thode !
        /* Le principe :
                1.  Remonter depuis la persitance la derni??re valeur de la s??quence du journal pour l'ann??e de l'??criture.
                    (table sequence_ecriture_comptable)
                2.  * S'il n'y a aucun enregistrement pour le journal pour l'ann??e concern??e :
                        1. Utiliser le num??ro 1.
                    * Sinon :
                        1. Utiliser la derni??re valeur + 1
                3.  Mettre ?? jour la r??f??rence de l'??criture avec la r??f??rence calcul??e (RG_Compta_5)
                4.  Enregistrer (insert/update) la valeur de la s??quence en persitance
                    (table sequence_ecriture_comptable)
         */
    	//      Determiner l'ann??e
		Date date = pEcritureComptable.getDate();
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		Integer year = c.get(Calendar.YEAR);
		
		Integer referenceNumber = 0;
		String journalCode = pEcritureComptable.getJournal().getCode();
		
		try {
            SequenceEcritureComptable sEComptable = getSequenceEcritureComptableByYearAndCode(year, journalCode);
            referenceNumber = sEComptable.getDerniereValeur() + 1;
            sEComptable.setDerniereValeur(referenceNumber);
            getDaoProxy().getComptabiliteDao().updateSequenceEcritureComptable(sEComptable);
		}catch (NotFoundException e) {
            //		Si aucune s??quence d'??criture ne matche pour l'ann??e recherch??e
            //		alors le num??ro pour la r??f??rence prend la valeur initiale 00001
            //		(ce qui correspond ?? la 3ieme partie du pattern soit #####)
            //		Une nouvelle s??quence d'??criture comptable est cr????e et et on utilise la m??thode INSERT.

            referenceNumber = 1;
            SequenceEcritureComptable sEComptableToCreate = new SequenceEcritureComptable(year, referenceNumber, journalCode);
            getDaoProxy().getComptabiliteDao().insertSequenceEcritureComptable(sEComptableToCreate);
        }
		
		int NumberOfDigits = String.valueOf(referenceNumber).length();
		int zeroToAdd = 5 - NumberOfDigits;
		String endRef = "";
		for(int i=0; i<zeroToAdd; i++) {
			endRef = "0" + endRef;
		}

		String anneeForReference = year.toString();
		String referenceNum = referenceNumber.toString();
		String code = pEcritureComptable.getJournal().getCode();
		
		String pReference = code + "-" + anneeForReference + "/" + endRef + referenceNum;
		
		pEcritureComptable.setReference(pReference);
		
		getDaoProxy().getComptabiliteDao().updateEcritureComptable(pEcritureComptable);	
    	
    }

    /**
     * {@inheritDoc}
     */
    // TODO ?? tester
    @Override
    public void checkEcritureComptable(EcritureComptable pEcritureComptable) throws FunctionalException {
        this.checkEcritureComptableUnit(pEcritureComptable);
        this.checkEcritureComptableContext(pEcritureComptable);
    }


    /**
     * V??rifie que l'Ecriture comptable respecte les r??gles de gestion unitaires,
     * c'est ?? dire ind??pendemment du contexte (unicit?? de la r??f??rence, exercie comptable non clotur??...)
     *
     * @param pEcritureComptable -
     * @throws FunctionalException Si l'Ecriture comptable ne respecte pas les r??gles de gestion
     */
    // TODO tests ?? compl??ter (fini)
    protected void checkEcritureComptableUnit(EcritureComptable pEcritureComptable) throws FunctionalException {
    	// ===== V??rification des contraintes unitaires sur les attributs de l'??criture
        Set<ConstraintViolation<EcritureComptable>> vViolations = getConstraintValidator().validate(pEcritureComptable);
        if (!vViolations.isEmpty()) {
            throw new FunctionalException("L'??criture comptable ne respecte pas les r??gles de gestion.",
                                          new ConstraintViolationException(
                                              "L'??criture comptable ne respecte pas les contraintes de validation",
                                              vViolations));
        }

        // ===== RG_Compta_2 : Pour qu'une ??criture comptable soit valide, elle doit ??tre ??quilibr??e
        if (!pEcritureComptable.isEquilibree()) {
            throw new FunctionalException("L'??criture comptable n'est pas ??quilibr??e.");
        }

        // ===== RG_Compta_3 : une ??criture comptable doit avoir au moins 2 lignes d'??criture (1 au d??bit, 1 au cr??dit)
        int vNbrCredit = 0;
        int vNbrDebit = 0;
        for (LigneEcritureComptable vLigneEcritureComptable : pEcritureComptable.getListLigneEcriture()) {
            if (BigDecimal.ZERO.compareTo(ObjectUtils.defaultIfNull(vLigneEcritureComptable.getCredit(),
                                                                    BigDecimal.ZERO)) != 0) {
                vNbrCredit++;
            }
            if (BigDecimal.ZERO.compareTo(ObjectUtils.defaultIfNull(vLigneEcritureComptable.getDebit(),
                                                                    BigDecimal.ZERO)) != 0) {
                vNbrDebit++;
            }
        }
        // On test le nombre de lignes car si l'??criture ?? une seule ligne
        //      avec un montant au d??bit et un montant au cr??dit ce n'est pas valable
        if (pEcritureComptable.getListLigneEcriture().size() < 2
            || vNbrCredit < 1
            || vNbrDebit < 1) {
            throw new FunctionalException(
                "L'??criture comptable doit avoir au moins deux lignes : une ligne au d??bit et une ligne au cr??dit.");
        }

        // TODO ===== RG_Compta_5 : Format et contenu de la r??f??rence
        // v??rifier que l'ann??e dans la r??f??rence correspond bien ?? la date de l'??criture, idem pour le code journal...        
        String reference = pEcritureComptable.getReference();

        if(reference != null) {
            String code = pEcritureComptable.getJournal().getCode();

//        Determiner l'ann??e
            Date date = pEcritureComptable.getDate();
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            int year = c.get(Calendar.YEAR);

            Pattern pattern = Pattern.compile("[A-Z]{1,5}-\\d{4}/\\d{5}");
            Matcher matcher = pattern.matcher(reference);

            if (matcher.find()) {
                if (!reference.contains(code) || !reference.contains(Integer.toString(year))) {
                    throw new FunctionalException(
                            "L'??criture comptable doit avoir une r??f??rence avec un format valide, il doit contenir"
                                    + " le code Journal : " + code + " et l'ann??e : " + year);
                }
            }
        }
        //FIN TODO==== RG_Compta_5        
    }


    /**
     * V??rifie que l'Ecriture comptable respecte les r??gles de gestion li??es au contexte
     * (unicit?? de la r??f??rence, ann??e comptable non clotur??...)
     *
     * @param pEcritureComptable -
     * @throws FunctionalException Si l'Ecriture comptable ne respecte pas les r??gles de gestion
     */
    protected void checkEcritureComptableContext(EcritureComptable pEcritureComptable) throws FunctionalException {
        // ===== RG_Compta_6 : La r??f??rence d'une ??criture comptable doit ??tre unique
        if (StringUtils.isNoneEmpty(pEcritureComptable.getReference())) {
            try {
                // Recherche d'une ??criture ayant la m??me r??f??rence
                EcritureComptable vECRef = getDaoProxy().getComptabiliteDao().getEcritureComptableByRef(
                    pEcritureComptable.getReference());

                // Si l'??criture ?? v??rifier est une nouvelle ??criture (id == null),
                // ou si elle ne correspond pas ?? l'??criture trouv??e (id != idECRef),
                // c'est qu'il y a d??j?? une autre ??criture avec la m??me r??f??rence
                if (pEcritureComptable.getId() == null
                    || !pEcritureComptable.getId().equals(vECRef.getId())) {
                    throw new FunctionalException("Une autre ??criture comptable existe d??j?? avec la m??me r??f??rence.");
                }
            } catch (NotFoundException vEx) {
                // Dans ce cas, c'est bon, ??a veut dire qu'on n'a aucune autre ??criture avec la m??me r??f??rence.
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void insertEcritureComptable(EcritureComptable pEcritureComptable) throws FunctionalException {
        this.checkEcritureComptable(pEcritureComptable);
        TransactionStatus vTS = getTransactionManager().beginTransactionMyERP();
        try {
            getDaoProxy().getComptabiliteDao().insertEcritureComptable(pEcritureComptable);
            getTransactionManager().commitMyERP(vTS);
            vTS = null;
        } finally {
            getTransactionManager().rollbackMyERP(vTS);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateEcritureComptable(EcritureComptable pEcritureComptable) throws FunctionalException {
//        Erreur num??ro 5 corrig??e par ajout de la m??thode checkEcritureComptable()
        this.checkEcritureComptable(pEcritureComptable);
        TransactionStatus vTS = getTransactionManager().beginTransactionMyERP();
        try {
            getDaoProxy().getComptabiliteDao().updateEcritureComptable(pEcritureComptable);
            getTransactionManager().commitMyERP(vTS);
            vTS = null;
        } finally {
            getTransactionManager().rollbackMyERP(vTS);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteEcritureComptable(Integer pId) {
        TransactionStatus vTS = getTransactionManager().beginTransactionMyERP();
        try {
            getDaoProxy().getComptabiliteDao().deleteEcritureComptable(pId);
            getTransactionManager().commitMyERP(vTS);
            vTS = null;
        } finally {
            getTransactionManager().rollbackMyERP(vTS);
        }
    }

//  AJOUT DE METHODES INSERT/UPDATE SequenceEcritureComptable POUR COMPLETER TODO dans addReference() dans ComptabiliteManagerImpl
    // ==================== SequenceEcritureComptable - INSERT ====================

    /**
     * {@inheritDoc}
     */
    @Override
    public void insertSequenceEcritureComptable(SequenceEcritureComptable pSequenceEcritureComptable) throws FunctionalException {
        TransactionStatus vTS = getTransactionManager().beginTransactionMyERP();
        try {
            getDaoProxy().getComptabiliteDao().insertSequenceEcritureComptable(pSequenceEcritureComptable);
            getTransactionManager().commitMyERP(vTS);
            vTS = null;
        } finally {
            getTransactionManager().rollbackMyERP(vTS);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateSequenceEcritureComptable(SequenceEcritureComptable pSequenceEcritureComptable) throws FunctionalException {
        TransactionStatus vTS = getTransactionManager().beginTransactionMyERP();
        try {
            getDaoProxy().getComptabiliteDao().updateSequenceEcritureComptable(pSequenceEcritureComptable);
            getTransactionManager().commitMyERP(vTS);
            vTS = null;
        } finally {
            getTransactionManager().rollbackMyERP(vTS);
        }
    }
}
