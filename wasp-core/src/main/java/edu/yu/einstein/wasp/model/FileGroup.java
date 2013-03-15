
/**
 *
 * FileHandle.java 
 * @author echeng (table2type.pl)
 *  
 * the FileHandle
 *
 *
 */

package edu.yu.einstein.wasp.model;

import java.net.URI;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

@Entity
@Audited
@Table(name="filegroup")
public class FileGroup extends WaspModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1239304568632087021L;
	
	
	public FileGroup() {
		filehandles = new HashSet<FileHandle>();
		begat = new HashSet<FileGroup>();
		derivedFrom = new HashSet<FileGroup>();
	}

	/**
	 * setFileGroupId(Integer filegroupid)
	 *
	 * @param fileGroupId
	 *
	 */
	@Deprecated
	public void setFileGroupId (Integer fileGroupId) {
		setId(fileGroupId);
	}

	/**
	 * getFileGroupId()
	 *
	 * @return filegroupid
	 *
	 */
	@Deprecated
	public Integer getFileGroupId () {
		return getId();
	}
	
	/** 
	 * FileGroupMeta
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="filegroupid", insertable=false, updatable=false)
	protected List<FileGroupMeta> fileGroupMeta;


	/** 
	 * getFileGroupMeta()
	 *
	 * @return FileGroupMeta
	 *
	 */
	@JsonIgnore
	public List<FileGroupMeta> getFileGroupMeta() {
		return this.fileGroupMeta;
	}


	/** 
	 * setFileGroupMeta
	 *
	 * @param fileGroupMeta
	 *
	 */
	public void setFileGroupMeta (List<FileGroupMeta> fileGroupMeta) {
		this.fileGroupMeta = fileGroupMeta;
	}
	
	/** 
	 * fileTypeid
	 *
	 */
	@Column(name="filetypeid")
	protected Integer fileTypeId;

	/**
	 * setFileTypeId(Integer fileTypeId)
	 *
	 * @param fileTypeId
	 *
	 */
	
	public void setFileTypeId (Integer fileTypeId) {
		this.fileTypeId = fileTypeId;
	}

	/**
	 * getFileTypeId()
	 *
	 * @return fileTypeId
	 *
	 */
	public Integer getFileTypeId () {
		return this.fileTypeId;
	}
	
	/** 
	 * softwaregeneratedbyid
	 *
	 */
	@Column(name="softwaregeneratedbyid")
	protected Integer softwareGeneratedById;

	/**
	 * setSoftwareGeneratedById(Integer softwareGeneratedById)
	 *
	 * @param softwareGeneratedById
	 *
	 */
	
	public void setSoftwareGeneratedById (Integer softwareGeneratedById) {
		this.softwareGeneratedById = softwareGeneratedById;
	}

	/**
	 * setSoftwareGeneratedById()
	 *
	 * @return softwareGeneratedById
	 *
	 */
	public Integer getSoftwareGeneratedById () {
		return this.softwareGeneratedById;
	}

	/** 
	 * description
	 *
	 */
	@Column(name="description", length=2048)
	protected String description;

	/**
	 * setDescription(String description)
	 *
	 * @param description
	 *
	 */
	
	public void setDescription (String description) {
		this.description = description;
	}

	/**
	 * getDescription()
	 *
	 * @return description
	 *
	 */
	public String getDescription () {
		return this.description;
	}

	/** 
	 * isArchived default 0
	 *
	 */
	@Column(name="isarchived")
	protected Integer isArchived = 0;

	/**
	 * setIsArchived(Integer isArchived)
	 *
	 * @param isArchived
	 *
	 */
	
	public void setIsArchived (Integer isArchived) {
		this.isArchived = isArchived;
	}

	/**
	 * getIsArchived()
	 *
	 * @return isArchived
	 *
	 */
	public Integer getIsArchived () {
		return this.isArchived;
	}


	/** 
	 * isActive default 0
	 *
	 */
	@Column(name="isactive")
	protected Integer isActive = 0;

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
	 * sampleDraft
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="filegroupid", insertable=false, updatable=false)
	protected List<SampleDraft> sampleDraft;


	/** 
	 * getSampleDraft()
	 *
	 * @return sampleDraft
	 *
	 */
	@JsonIgnore
	public List<SampleDraft> getSampleDraft() {
		return this.sampleDraft;
	}


	/** 
	 * setSampleDraft
	 *
	 * @param sampleDraft
	 *
	 */
	public void setSampleDraft (List<SampleDraft> sampleDraft) {
		this.sampleDraft = sampleDraft;
	}

	/** 
	 * jobFile
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="filegroupid", insertable=false, updatable=false)
	protected List<JobFile> jobFile;


	/** 
	 * getJobFile()
	 *
	 * @return jobFile
	 *
	 */
	@JsonIgnore
	public List<JobFile> getJobFile() {
		return this.jobFile;
	}


	/** 
	 * setJobFile
	 *
	 * @param jobFile
	 *
	 */
	public void setJobFile (List<JobFile> jobFile) {
		this.jobFile = jobFile;
	}
	
	/** 
	 * jobDraftFile
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="filegroupid", insertable=false, updatable=false)
	protected List<JobDraftFile> jobDraftFile;


	/** 
	 * getJobDraftFile()
	 *
	 * @return jobDraftFile
	 *
	 */
	@JsonIgnore
	public List<JobDraftFile> getJobDraftFile() {
		return this.jobDraftFile;
	}


	/** 
	 * setJobDraftFile
	 *
	 * @param jobDraftFile
	 *
	 */
	public void setJobDraftFile (List<JobDraftFile> jobDraftFile) {
		this.jobDraftFile = jobDraftFile;
	}

	/**
	 * software
	 *
	 */
	@NotAudited
	@ManyToOne
	@JoinColumn(name="softwaregeneratedbyid", insertable=false, updatable=false)
	protected Software softwareGeneratedBy;

	/**
	 * setSoftwareGeneratedBy (Software software)
	 *
	 * @param software
	 *
	 */
	public void setSoftwareGeneratedBy (Software software) {
		this.softwareGeneratedBy = software;
		this.softwareGeneratedById = software.getId();
	}

	/**
	 * getSoftwareGeneratedBy ()
	 *
	 * @return softwareGeneratedBy
	 *
	 */
	
	public Software getSoftwareGeneratedBy () {
		return this.softwareGeneratedBy;
	}
	
	/**
	 * fileType
	 *
	 */
	@NotAudited
	@ManyToOne
	@JoinColumn(name="filetypeid", insertable=false, updatable=false)
	protected FileType fileType;

	/**
	 * setFileType (FileType fileType)
	 *
	 * @param fileType
	 *
	 */
	public void setFileType (FileType fileType){
		this.fileType = fileType;
		this.fileTypeId = fileType.getId();
	}

	/**
	 * getFileType ()
	 *
	 * @return fileType
	 *
	 */
	
	public FileType getFileType () {
		return this.fileType;
	}

	
	@ManyToMany
    @JoinTable(name="groupfile", joinColumns={@JoinColumn(name="groupid")}, inverseJoinColumns={@JoinColumn(name="fileid")})
	protected Set<FileHandle> filehandles;
	
	@JsonIgnore
	public Set<FileHandle> getFileHandles() {
		return this.filehandles;
	}
	
	public void setFileHandles(Set<FileHandle> filehandles) {
		this.filehandles = filehandles;
	}
	
	public void addFileHandle(FileHandle fileHandle) {
        if (!getFileHandles().contains(fileHandle)) {
            getFileHandles().add(fileHandle);
        }
    }
	
	

	@ManyToMany
	@JoinTable(name="filegroup_rel",
	 joinColumns=@JoinColumn(name="filegroupid"),
	 inverseJoinColumns=@JoinColumn(name="childfilegroupid"))
	private Set<FileGroup> begat;
	
	/**
	 * @return the begat
	 */
	public Set<FileGroup> getBegat() {
		return begat;
	}

	/**
	 * @param begat the begat to set
	 */
	public void setBegat(Set<FileGroup> begat) {
		this.begat = begat;
	}
	
	public void addBegat(FileGroup begat) {
		if (!getBegat().contains(begat)) {
            getBegat().add(begat);
        }
        if (!begat.getDerivedFrom().contains(this)) {
        	begat.getDerivedFrom().add(this);
        }
	}
	

	@ManyToMany
	@JoinTable(name="filegroup_rel",
	 joinColumns=@JoinColumn(name="childfilegroupid"),
	 inverseJoinColumns=@JoinColumn(name="filegroupid"))
	private Set<FileGroup> derivedFrom;
	
	/**
	 * @return the derivedFrom
	 */
	public Set<FileGroup> getDerivedFrom() {
		return derivedFrom;
	}

	/**
	 * @param derivedFrom the derivedFrom to set
	 */
	public void setDerivedFrom(Set<FileGroup> derivedFrom) {
		this.derivedFrom = derivedFrom;
	}
	
	public void addDerivedFrom(FileGroup fileGroup) {
        if (!getDerivedFrom().contains(fileGroup)) {
            getDerivedFrom().add(fileGroup);
        }
    }
	
	/**
	 * 
	 */
	@ManyToMany(mappedBy="fileGroups")
	private Set<Sample> samples = new HashSet<Sample>();

	/**
	 * @return the fileGroups
	 */
	public Set<Sample> getSamples() {
		return samples;
	}

	/**
	 * @param fileGroups the fileGroups to set
	 */
	public void setSamples(Set<Sample> samples) {
		this.samples = samples;
	}
	
	/**
	 * 
	 */
	@ManyToMany(mappedBy="fileGroups")
	private Set<SampleSource> sampleSources = new HashSet<SampleSource>();

	/**
	 * @return the fileGroups
	 */
	public Set<SampleSource> getSampleSources() {
		return this.sampleSources;
	}

	/**
	 * @param fileGroups the fileGroups to set
	 */
	public void setSampleSources(Set<SampleSource> sampleSource) {
		this.sampleSources = sampleSource;
	}
	

}
