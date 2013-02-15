
/**
 *
 * SampleDraft.java 
 * @author echeng (table2type.pl)
 *  
 * the SampleDraft
 *
 *
 */

package edu.yu.einstein.wasp.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

@Entity
@Audited
@Table(name="sampledraft")
public class SampleDraft extends WaspModel {

/**
	 * 
	 */
	private static final long serialVersionUID = -7802756907357416771L;

public static enum Status {
pending,
inprocess,
processed
}

	/** 
	 * sampleDraftId
	 *
	 */
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	protected Integer sampleDraftId;

	/**
	 * setSampleDraftId(Integer sampleDraftId)
	 *
	 * @param sampleDraftId
	 *
	 */
	
	public void setSampleDraftId (Integer sampleDraftId) {
		this.sampleDraftId = sampleDraftId;
	}

	/**
	 * getSampleDraftId()
	 *
	 * @return sampleDraftId
	 *
	 */
	public Integer getSampleDraftId () {
		return this.sampleDraftId;
	}




	/** 
	 * sampleTypeId
	 *
	 */
	@Column(name="sampletypeid")
	protected Integer sampleTypeId;

	/**
	 * setSampleTypeId(Integer sampleTypeId)
	 *
	 * @param sampleTypeId
	 *
	 */
	
	public void setSampleTypeId (Integer sampleTypeId) {
		this.sampleTypeId = sampleTypeId;
	}

	/**
	 * getSampleTypeId()
	 *
	 * @return sampleTypeId
	 *
	 */
	public Integer getSampleTypeId () {
		return this.sampleTypeId;
	}




	/** 
	 * sampleSubtypeId
	 *
	 */
	@Column(name="samplesubtypeid")
	protected Integer sampleSubtypeId;

	/**
	 * setSampleSubtypeId(Integer sampleSubtypeId)
	 *
	 * @param sampleSubtypeId
	 *
	 */
	
	public void setSampleSubtypeId (Integer sampleSubtypeId) {
		this.sampleSubtypeId = sampleSubtypeId;
	}

	/**
	 * getSampleSubtypeId()
	 *
	 * @return sampleSubtypeId
	 *
	 */
	public Integer getSampleSubtypeId () {
		return this.sampleSubtypeId;
	}
	
	@Column(name="sourcesampleid")
	protected Integer sourceSampleId;

	public Integer getSourceSampleId() {
		return sourceSampleId;
	}

	public void setSourceSampleId(Integer sourceSampleId) {
		this.sourceSampleId = sourceSampleId;
	}


	/** 
	 * labId
	 *
	 */
	@Column(name="labid")
	protected Integer labId;

	/**
	 * setLabId(Integer labId)
	 *
	 * @param labId
	 *
	 */
	
	public void setLabId (Integer labId) {
		this.labId = labId;
	}

	/**
	 * getLabId()
	 *
	 * @return labId
	 *
	 */
	public Integer getLabId () {
		return this.labId;
	}




	/** 
	 * UserId
	 *
	 */
	@Column(name="userid")
	protected Integer UserId;

	/**
	 * setUserId(Integer UserId)
	 *
	 * @param UserId
	 *
	 */
	
	public void setUserId (Integer UserId) {
		this.UserId = UserId;
	}

	/**
	 * getUserId()
	 *
	 * @return UserId
	 *
	 */
	public Integer getUserId () {
		return this.UserId;
	}




	/** 
	 * jobDraftId
	 *
	 */
	@Column(name="jobdraftid")
	protected Integer jobDraftId;

	/**
	 * setJobDraftId(Integer jobDraftId)
	 *
	 * @param jobDraftId
	 *
	 */
	
	public void setJobDraftId (Integer jobDraftId) {
		this.jobDraftId = jobDraftId;
	}

	/**
	 * getJobDraftId()
	 *
	 * @return jobDraftId
	 *
	 */
	public Integer getJobDraftId () {
		return this.jobDraftId;
	}




	/** 
	 * fileId
	 *
	 */
	@Column(name="filegroupid")
	protected Integer fileGroupId;

	/**
	 * setFileId(Integer fileId)
	 *
	 * @param fileId
	 *
	 */
	
	public void setFileGroupId (Integer fileGroupId) {
		this.fileGroupId = fileGroupId;
	}

	/**
	 * getFileId()
	 *
	 * @return fileId
	 *
	 */
	public Integer getFileGroupId () {
		return this.fileGroupId;
	}




	/** 
	 * name
	 *
	 */
	@Column(name="name")
	@NotEmpty
	protected String name;

	/**
	 * setName(String name)
	 *
	 * @param name
	 *
	 */
	
	public void setName (String name) {
		this.name = name;
	}

	/**
	 * getName()
	 *
	 * @return name
	 *
	 */
	public String getName () {
		return this.name;
	}




	/** 
	 * status
	 *
	 */
	@Column(name="status")
	protected String status;

	/**
	 * setStatus(String status)
	 *
	 * @param status
	 *
	 */
	
	public void setStatus (String status) {
		this.status = status;
	}

	/**
	 * getStatus()
	 *
	 * @return status
	 *
	 */
	public String getStatus () {
		return this.status;
	}




	/** 
	 * lastUpdTs
	 *
	 */
	@Column(name="lastupdts")
	protected Date lastUpdTs;

	/**
	 * setLastUpdTs(Date lastUpdTs)
	 *
	 * @param lastUpdTs
	 *
	 */
	
	public void setLastUpdTs (Date lastUpdTs) {
		this.lastUpdTs = lastUpdTs;
	}

	/**
	 * getLastUpdTs()
	 *
	 * @return lastUpdTs
	 *
	 */
	public Date getLastUpdTs () {
		return this.lastUpdTs;
	}




	/** 
	 * lastUpdUser
	 *
	 */
	@Column(name="lastupduser")
	protected Integer lastUpdUser;

	/**
	 * setLastUpdUser(Integer lastUpdUser)
	 *
	 * @param lastUpdUser
	 *
	 */
	
	public void setLastUpdUser (Integer lastUpdUser) {
		this.lastUpdUser = lastUpdUser;
	}

	/**
	 * getLastUpdUser()
	 *
	 * @return lastUpdUser
	 *
	 */
	public Integer getLastUpdUser () {
		return this.lastUpdUser;
	}




	/**
	 * sampleType
	 *
	 */
	@NotAudited
	@ManyToOne
	@JoinColumn(name="sampletypeid", insertable=false, updatable=false)
	protected SampleType sampleType;

	/**
	 * setSampleType (SampleType sampleType)
	 *
	 * @param sampleType
	 *
	 */
	public void setSampleType (SampleType sampleType) {
		this.sampleType = sampleType;
		this.sampleTypeId = sampleType.sampleTypeId;
	}

	/**
	 * getSampleType ()
	 *
	 * @return sampleType
	 *
	 */
	
	public SampleType getSampleType () {
		return this.sampleType;
	}


	/**
	 * sampleSubtype
	 *
	 */
	@NotAudited
	@ManyToOne
	@JoinColumn(name="samplesubtypeid", insertable=false, updatable=false)
	protected SampleSubtype sampleSubtype;

	/**
	 * setSampleSubtype (SampleSubtype sampleSubtype)
	 *
	 * @param sampleSubtype
	 *
	 */
	public void setSampleSubtype (SampleSubtype sampleSubtype) {
		this.sampleSubtype = sampleSubtype;
		this.sampleSubtypeId = sampleSubtype.sampleSubtypeId;
	}

	/**
	 * getSampleSubtype ()
	 *
	 * @return sampleSubtype
	 *
	 */
	
	public SampleSubtype getSampleSubtype () {
		return this.sampleSubtype;
	}


	/**
	 * jobDraft
	 *
	 */
	@NotAudited
	@ManyToOne
	@JoinColumn(name="jobdraftid", insertable=false, updatable=false)
	protected JobDraft jobDraft;

	/**
	 * setJobDraft (JobDraft jobDraft)
	 *
	 * @param jobDraft
	 *
	 */
	public void setJobDraft (JobDraft jobDraft) {
		this.jobDraft = jobDraft;
		this.jobDraftId = jobDraft.jobDraftId;
	}

	/**
	 * getJobDraft ()
	 *
	 * @return jobDraft
	 *
	 */
	
	public JobDraft getJobDraft () {
		return this.jobDraft;
	}


	/**
	 * lab
	 *
	 */
	@NotAudited
	@ManyToOne
	@JoinColumn(name="labid", insertable=false, updatable=false)
	protected Lab lab;

	/**
	 * setLab (Lab lab)
	 *
	 * @param lab
	 *
	 */
	public void setLab (Lab lab) {
		this.lab = lab;
		this.labId = lab.labId;
	}

	/**
	 * getLab ()
	 *
	 * @return lab
	 *
	 */
	
	public Lab getLab () {
		return this.lab;
	}


	/**
	 * user
	 *
	 */
	@NotAudited
	@ManyToOne
	@JoinColumn(name="userid", insertable=false, updatable=false)
	protected User user;

	/**
	 * setUser (User user)
	 *
	 * @param user
	 *
	 */
	public void setUser (User user) {
		this.user = user;
		this.UserId = user.UserId;
	}

	/**
	 * getUser ()
	 *
	 * @return user
	 *
	 */
	
	public User getUser () {
		return this.user;
	}


	/**
	 * file
	 *
	 */
	@NotAudited
	@ManyToOne
	@JoinColumn(name="filegroupid", insertable=false, updatable=false)
	protected FileGroup fileGroup;

	/**
	 * setFile (FileHandle file)
	 *
	 * @param file
	 *
	 */
	public void setFile (FileGroup fileGroup) {
		this.fileGroup = fileGroup;
		this.fileGroupId = fileGroup.fileGroupId;
	}

	/**
	 * getFile ()
	 *
	 * @return file
	 *
	 */
	
	public FileGroup getFileGroup () {
		return this.fileGroup;
	}


	/** 
	 * sampleDraftMeta
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="sampledraftid", insertable=false, updatable=false)
	protected List<SampleDraftMeta> sampleDraftMeta;


	/** 
	 * getSampleDraftMeta()
	 *
	 * @return sampleDraftMeta
	 *
	 */
	@JsonIgnore
	public List<SampleDraftMeta> getSampleDraftMeta() {
		return this.sampleDraftMeta;
	}


	/** 
	 * setSampleDraftMeta
	 *
	 * @param sampleDraftMeta
	 *
	 */
	public void setSampleDraftMeta (List<SampleDraftMeta> sampleDraftMeta) {
		this.sampleDraftMeta = sampleDraftMeta;
	}


	/**
	* sampleDraftJobDraftCellSelection
	*
	*/
	@NotAudited
	@OneToMany
	@JoinColumn(name="sampledraftid", insertable=false, updatable=false)
	protected List<SampleDraftJobDraftCellSelection> sampleDraftJobDraftCellSelection;

	/**
	* getSampleDraftCell()
	*
	* @return sampleDraftJobDraftCellSelection
	*
	*/
	@JsonIgnore
	public List<SampleDraftJobDraftCellSelection> getSampleDraftJobDraftCellSelection() {
		return this.sampleDraftJobDraftCellSelection;
	}
	
	
	/**
	* setSampleDraftCell
	*
	* @param sampleDraftJobDraftCellSelection
	*
	*/
	public void setSampleDraftJobDraftCellSelection (List<SampleDraftJobDraftCellSelection> sampleDraftJobDraftCellSelection) {
		this.sampleDraftJobDraftCellSelection = sampleDraftJobDraftCellSelection;
	}
	



	@Transient
	private CommonsMultipartFile fileData;
	public CommonsMultipartFile getFileData() {
		return fileData;
	}

	public void setFileData(CommonsMultipartFile fileData) {
		this.fileData = fileData;
	}
}
