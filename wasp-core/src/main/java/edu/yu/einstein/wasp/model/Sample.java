
/**
 *
 * Sample.java 
 * @author echeng (table2type.pl)
 *  
 * the Sample
 *
 *
 */

package edu.yu.einstein.wasp.model;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Audited
@Table(name="sample")
public class Sample extends WaspModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -324610621097231467L;

	/**
	 * setSampleId(Integer sampleId)
	 *
	 * @param sampleId
	 *
	 */
	@Deprecated
	public void setSampleId (Integer sampleId) {
		setId(sampleId);
	}

	/**
	 * getSampleId()
	 *
	 * @return sampleId
	 *
	 */
	@Deprecated
	public Integer getSampleId () {
		return getId();
	}

	/** 
	 * parentId
	 *
	 */
	@Column(name="parentid")
	protected Integer parentId;

	/**
	 * setParentId(Integer parentId)
	 *
	 * @param parentId
	 *
	 */
	
	public void setParentId (Integer parentId) {
		this.parentId = parentId;
	}

	/**
	 * getParentId()
	 *
	 * @return parentId
	 *
	 */
	public Integer getParentId () {
		return this.parentId;
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




	/** 
	 * submitterLabId
	 *
	 */
	@Column(name="submitter_labid")
	protected Integer submitterLabId;

	/**
	 * setSubmitterLabId(Integer submitterLabId)
	 *
	 * @param submitterLabId
	 *
	 */
	
	public void setSubmitterLabId (Integer submitterLabId) {
		this.submitterLabId = submitterLabId;
	}

	/**
	 * getSubmitterLabId()
	 *
	 * @return submitterLabId
	 *
	 */
	public Integer getSubmitterLabId () {
		return this.submitterLabId;
	}




	/** 
	 * submitterUserId
	 *
	 */
	@Column(name="submitter_userid")
	protected Integer submitterUserId;

	/**
	 * setSubmitterUserId(Integer submitterUserId)
	 *
	 * @param submitterUserId
	 *
	 */
	
	public void setSubmitterUserId (Integer submitterUserId) {
		this.submitterUserId = submitterUserId;
	}

	/**
	 * getSubmitterUserId()
	 *
	 * @return submitterUserId
	 *
	 */
	public Integer getSubmitterUserId () {
		return this.submitterUserId;
	}




	/** 
	 * submitterJobId
	 *
	 */
	@Column(name="submitter_jobid")
	protected Integer submitterJobId;

	/**
	 * setSubmitterJobId(Integer submitterJobId)
	 *
	 * @param submitterJobId
	 *
	 */
	
	public void setSubmitterJobId (Integer submitterJobId) {
		this.submitterJobId = submitterJobId;
	}

	/**
	 * getSubmitterJobId()
	 *
	 * @return submitterJobId
	 *
	 */
	public Integer getSubmitterJobId () {
		return this.submitterJobId;
	}




	/** 
	 * isReceived
	 *
	 */
	@Column(name="isreceived")
	protected Integer isReceived = 0;

	/**
	 * setIsReceived(Integer isReceived)
	 *
	 * @param isReceived
	 *
	 */
	
	public void setIsReceived (Integer isReceived) {
		this.isReceived = isReceived;
	}

	/**
	 * getIsReceived()
	 *
	 * @return isReceived
	 *
	 */
	public Integer getIsReceived () {
		return this.isReceived;
	}




	/** 
	 * receiverUserId
	 *
	 */
	@Column(name="receiver_userid")
	protected Integer receiverUserId;

	/**
	 * setReceiverUserId(Integer receiverUserId)
	 *
	 * @param receiverUserId
	 *
	 */
	
	public void setReceiverUserId (Integer receiverUserId) {
		this.receiverUserId = receiverUserId;
	}

	/**
	 * getReceiverUserId()
	 *
	 * @return receiverUserId
	 *
	 */
	public Integer getReceiverUserId () {
		return this.receiverUserId;
	}




	/** 
	 * receiveDts
	 *
	 */
	@Column(name="receivedts")
	protected Date receiveDts;

	/**
	 * setReceiveDts(Date receiveDts)
	 *
	 * @param receiveDts
	 *
	 */
	
	public void setReceiveDts (Date receiveDts) {
		this.receiveDts = receiveDts;
	}

	/**
	 * getReceiveDts()
	 *
	 * @return receiveDts
	 *
	 */
	public Date getReceiveDts () {
		return this.receiveDts;
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
	 * isGood
	 *
	 */
	@Column(name="isgood")
	protected Integer isGood;

	/**
	 * setIsGood(Integer isGood)
	 *
	 * @param isGood
	 *
	 */
	
	public void setIsGood (Integer isGood) {
		this.isGood = isGood;
	}

	/**
	 * getIsGood()
	 *
	 * @return isGood
	 *
	 */
	public Integer getIsGood () {
		return this.isGood;
	}




	/** 
	 * isActive
	 *
	 */
	@Column(name="isactive")
	protected Integer isActive = 1;

	/**
	 * setIsActive(Integer isActive)
	 *
	 * @param isActive
	 *
	 */
	
	public void setIsActive (Integer isActive) {
		this.isActive = isActive;
	}

	/**
	 * getIsActive()
	 *
	 * @return isActive
	 *
	 */
	public Integer getIsActive () {
		return this.isActive;
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
		this.sampleTypeId = sampleType.getId();
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
		this.sampleSubtypeId = sampleSubtype.getId();
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
	 * parent
	 *
	 */
	@NotAudited
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="parentid", insertable=false, updatable=false)
	protected Sample parent;

	/**
	 * setParent (Sample parent)
	 *
	 * @param sampleSubtype
	 *
	 */
	public void setParent (Sample parent) {
		this.parent = parent;
		this.parentId = parent.getId();
	}
	
	/** 
	 * children
	 *
	 */
	@NotAudited
	@OneToMany(mappedBy = "parentId")
	protected List<Sample> children;


	/** 
	 * getChildren()
	 *
	 * @return children
	 *
	 */
	@JsonIgnore
	public List<Sample> getChildren() {
		return this.children;
	}


	/** 
	 * setChildren
	 *
	 * @param sampleMeta
	 *
	 */
	public void setChildren (List<Sample> children) {
		this.children = children;
	}
	

	/**
	 * getParent()
	 * 
	 * If the sample is a library generated from a sample, the parent is that sample.  else null.
	 *
	 * @return Sample
	 *
	 */
	
	public Sample getParent() {
		return this.parent;
	}


	/**
	 * job
	 *
	 */
	@NotAudited
	@ManyToOne
	@JoinColumn(name="submitter_jobid", insertable=false, updatable=false)
	protected Job job;

	/**
	 * setJob (Job job)
	 *
	 * @param job
	 *
	 */
	public void setJob (Job job) {
		this.job = job;
		this.submitterJobId = job.getId();
	}

	/**
	 * getJob ()
	 *
	 * @return job
	 *
	 */
	
	public Job getJob () {
		return this.job;
	}


	/**
	 * lab
	 *
	 */
	@NotAudited
	@ManyToOne
	@JoinColumn(name="submitter_labid", insertable=false, updatable=false)
	protected Lab lab;

	/**
	 * setLab (Lab lab)
	 *
	 * @param lab
	 *
	 */
	public void setLab (Lab lab) {
		this.lab = lab;
		this.submitterLabId = lab.getId();
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
	@JoinColumn(name="submitter_userid", insertable=false, updatable=false)
	protected User user;

	/**
	 * setUser (User user)
	 *
	 * @param user
	 *
	 */
	public void setUser (User user) {
		this.user = user;
		this.submitterUserId = user.getId();
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
	 * sampleMeta
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="sampleid", insertable=false, updatable=false)
	protected List<SampleMeta> sampleMeta;


	/** 
	 * getSampleMeta()
	 *
	 * @return sampleMeta
	 *
	 */
	@JsonIgnore
	public List<SampleMeta> getSampleMeta() {
		return this.sampleMeta;
	}


	/** 
	 * setSampleMeta
	 *
	 * @param sampleMeta
	 *
	 */
	public void setSampleMeta (List<SampleMeta> sampleMeta) {
		this.sampleMeta = sampleMeta;
	}
	


	/** 
	 * sampleSource
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="sampleid", insertable=false, updatable=false)
	protected List<SampleSource> sampleSource;


	/** 
	 * getSampleSource()
	 *
	 * @return sampleSource
	 *
	 */
	@JsonIgnore
	public List<SampleSource> getSampleSource() {
		return this.sampleSource;
	}


	/** 
	 * setSampleSource
	 *
	 * @param sampleSource
	 *
	 */
	public void setSampleSource (List<SampleSource> sampleSource) {
		this.sampleSource = sampleSource;
	}



	/** 
	 * sourceSample
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="source_sampleid", insertable=false, updatable=false)
	protected List<SampleSource> sourceSample;


	/** 
	 * getSourceSample()
	 *
	 * @return sourceSample
	 *
	 */
	@JsonIgnore
	public List<SampleSource> getSourceSample() {
		return this.sourceSample;
	}


	/** 
	 * setSourceSample
	 *
	 * @param sampleSource
	 *
	 */
	public void setSourceSample (List<SampleSource> sampleSource) {
		this.sourceSample = sampleSource;
	}



	/** 
	 * sampleBarcode
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="sampleid", insertable=false, updatable=false)
	protected List<SampleBarcode> sampleBarcode;


	/** 
	 * getSampleBarcode()
	 *
	 * @return sampleBarcode
	 *
	 */
	@JsonIgnore
	public List<SampleBarcode> getSampleBarcode() {
		return this.sampleBarcode;
	}


	/** 
	 * setSampleBarcode
	 *
	 * @param sampleBarcode
	 *
	 */
	public void setSampleBarcode (List<SampleBarcode> sampleBarcode) {
		this.sampleBarcode = sampleBarcode;
	}



	/** 
	 * sampleLab
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="sampleid", insertable=false, updatable=false)
	protected List<SampleLab> sampleLab;


	/** 
	 * getSampleLab()
	 *
	 * @return sampleLab
	 *
	 */
	@JsonIgnore
	public List<SampleLab> getSampleLab() {
		return this.sampleLab;
	}


	/** 
	 * setSampleLab
	 *
	 * @param sampleLab
	 *
	 */
	public void setSampleLab (List<SampleLab> sampleLab) {
		this.sampleLab = sampleLab;
	}



	/** 
	 * jobSample
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="sampleid", insertable=false, updatable=false)
	protected List<JobSample> jobSample;


	/** 
	 * getJobSample()
	 *
	 * @return jobSample
	 *
	 */
	@JsonIgnore
	public List<JobSample> getJobSample() {
		return this.jobSample;
	}


	/** 
	 * setJobSample
	 *
	 * @param jobSample
	 *
	 */
	public void setJobSample (List<JobSample> jobSample) {
		this.jobSample = jobSample;
	}



	/** 
	 * sampleJobCellSelection
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="sampleid", insertable=false, updatable=false)
	protected List<SampleJobCellSelection> sampleJobCellSelection;


	/** 
	 * getSampleCell()
	 *
	 * @return sampleJobCellSelection
	 *
	 */
	@JsonIgnore
	public List<SampleJobCellSelection> getSampleJobCellSelection() {
		return this.sampleJobCellSelection;
	}


	/** 
	 * setSampleCell
	 *
	 * @param sampleJobCellSelection
	 *
	 */
	public void setSampleJobCellSelection (List<SampleJobCellSelection> sampleJobCellSelection) {
		this.sampleJobCellSelection = sampleJobCellSelection;
	}


	/** 
	 * run
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="sampleid", insertable=false, updatable=false)
	protected List<Run> run;


	/** 
	 * getRun()
	 *
	 * @return run
	 *
	 */
	@JsonIgnore
	public List<Run> getRun() {
		return this.run;
	}


	/** 
	 * setRun
	 *
	 * @param run
	 *
	 */
	public void setRun (List<Run> run) {
		this.run = run;
	}



	/** 
	 * runCell
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="sampleid", insertable=false, updatable=false)
	protected List<RunCell> runCell;


	/** 
	 * getRunCell()
	 *
	 * @return runCell
	 *
	 */
	@JsonIgnore
	public List<RunCell> getRunCell() {
		return this.runCell;
	}


	/** 
	 * setRunCell
	 *
	 * @param runCell
	 *
	 */
	public void setRunCell (List<RunCell> runCell) {
		this.runCell = runCell;
	}


	/**
	 * 
	 */
	@ManyToMany
	@JoinTable(name="samplefilegroup", joinColumns={@JoinColumn(name="sampleid")}, inverseJoinColumns={@JoinColumn(name="filegroupid")})
	private Set<FileGroup> fileGroups = new LinkedHashSet<FileGroup>();

	/**
	 * @return the fileGroups
	 */
	public Set<FileGroup> getFileGroups() {
		return fileGroups;
	}

	/**
	 * @param fileGroups the fileGroups to set
	 */
	public void setFileGroups(Set<FileGroup> fileGroups) {
		this.fileGroups = fileGroups;
	}


	

}
