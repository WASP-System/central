
/**
 *
 * AcctQuoteMeta.java 
 * @author echeng (table2type.pl)
 *  
 * the AcctQuoteMeta
 *
 *
 */

package edu.yu.einstein.wasp.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

@Entity
@Audited
@Table(name="acct_quotemeta")
public class AcctQuoteMeta extends MetaBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3927428785556020796L;
	/** 
	 * quotemetaId
	 *
	 */
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	protected Integer quotemetaId;

	/**
	 * setQuotemetaId(Integer quotemetaId)
	 *
	 * @param quotemetaId
	 *
	 */
	
	public void setQuotemetaId (Integer quotemetaId) {
		this.quotemetaId = quotemetaId;
	}

	/**
	 * getQuotemetaId()
	 *
	 * @return quotemetaId
	 *
	 */
	public Integer getQuotemetaId () {
		return this.quotemetaId;
	}




	/** 
	 * quoteId
	 *
	 */
	@Column(name="quoteid")
	protected Integer quoteId;

	/**
	 * setQuoteId(Integer quoteId)
	 *
	 * @param quoteId
	 *
	 */
	
	public void setQuoteId (Integer quoteId) {
		this.quoteId = quoteId;
	}

	/**
	 * getQuoteId()
	 *
	 * @return quoteId
	 *
	 */
	public Integer getQuoteId () {
		return this.quoteId;
	}




	/**
	 * acctQuote
	 *
	 */
	@NotAudited
	@ManyToOne
	@JoinColumn(name="quoteid", insertable=false, updatable=false)
	protected AcctQuote acctQuote;

	/**
	 * setAcctQuote (AcctQuote acctQuote)
	 *
	 * @param acctQuote
	 *
	 */
	public void setAcctQuote (AcctQuote acctQuote) {
		this.acctQuote = acctQuote;
		this.quoteId = acctQuote.quoteId;
	}

	/**
	 * getAcctQuote ()
	 *
	 * @return acctQuote
	 *
	 */
	
	public AcctQuote getAcctQuote () {
		return this.acctQuote;
	}


}
