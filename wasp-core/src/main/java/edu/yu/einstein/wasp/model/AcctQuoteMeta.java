
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
	 * setQuoteMetaId(Integer quoteMetaId)
	 *
	 * @param quoteMetaId
	 *
	 */
	@Deprecated
	public void setQuoteMetaId (Integer quoteMetaId) {
		setId(quoteMetaId);
	}

	/**
	 * getQuoteMetaId()
	 *
	 * @return quoteMetaId
	 *
	 */
	@Deprecated
	public Integer getQuoteMetaId () {
		return getId();
	}




	/** 
	 * acctQuoteId
	 *
	 */
	@Column(name="quoteid")
	protected Integer acctQuoteId;

	/**
	 * setQuoteId(Integer acctQuoteId)
	 *
	 * @param acctQuoteId
	 *
	 */
	
	public void setAcctQuoteId (Integer acctQuoteId) {
		this.acctQuoteId = acctQuoteId;
	}

	/**
	 * getAcctQuoteId()
	 *
	 * @return acctQuoteId
	 *
	 */
	public Integer getAcctQuoteId () {
		return this.acctQuoteId;
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
		this.acctQuoteId = acctQuote.getId();
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
