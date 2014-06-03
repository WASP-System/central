
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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

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
	private static final long serialVersionUID = 8180206804994670917L;


	public FileGroup() {
		fileHandles = new HashSet<FileHandle>();
		begat = new HashSet<FileGroup>();
		derivedFrom = new HashSet<FileGroup>();
		samples = new HashSet<Sample>();
		sampleSources = new HashSet<SampleSource>();
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
	
	@Column(name="deleted")
	protected Integer deleted = 0;
	
	/**
	 * @param deleted
	 */
	public void setDeleted(Integer deleted) {
		this.deleted = deleted;
	}
	
	/**
	 * @return
	 */
	public Integer isDeleted() {
		return this.deleted;
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
	protected Set<FileHandle> fileHandles;
	
	@JsonIgnore
	public Set<FileHandle> getFileHandles() {
		return this.fileHandles;
	}
	
	public void setFileHandles(Set<FileHandle> filehandles) {
		this.fileHandles = filehandles;
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
	 * Sets FileGroups this instance is derived from. 
	 * Also automatically populates this instance with Samples and SampleSources from the derived FileGroups.
	 * @param derivedFrom
	 */
	public void setDerivedFrom(Set<FileGroup> derivedFrom) {
		if (derivedFrom != null){
			for (FileGroup fg : derivedFrom)
				addDerivedFrom(fg);
		}
	}
	
	/**
	 * Adds a fileGroups that this instance is derived from. 
	 * Also automatically populates this instance with Samples and SampleSources from the derived FileGroup.
	 * @param fileGroup
	 */
	public void addDerivedFrom(FileGroup fileGroup) {
		if (!getDerivedFrom().contains(fileGroup)) {
			getDerivedFrom().add(fileGroup);
			if (fileGroup.getSamples().size() > 0)
				getSamples().addAll(fileGroup.getSamples());
        	if (fileGroup.getSampleSources().size() > 0)
        		getSampleSources().addAll(fileGroup.getSampleSources());
        } 
    }
	
	/**
	 * 
	 */
	@ManyToMany(mappedBy="fileGroups")
	private Set<Sample> samples;

	/**
	 * WARNING: many-to-many relationship owned by Sample.
	 * Adding Samples added here without then calling fileService.addFileGroup() to persist this object will result in them NOT being persisted.
	 * Either persist with fileService.addFileGroup() or save this object and add it to the 
	 * @return the fileGroups
	 */
	public Set<Sample> getSamples() {
		return samples;
	}

	/**
	 * WARNING: many-to-many relationship owned by Sample.
	 * Adding Samples added here without then calling fileService.addFileGroup() to persist this object will result in them NOT being persisted.
	 * @param fileGroups the fileGroups to set
	 */
	public void setSamples(Set<Sample> samples) {
		this.samples = samples;
	}
	
	/**
	 * 
	 */
	@ManyToMany(mappedBy="fileGroups")
	private Set<SampleSource> sampleSources;

	/**
	 * WARNING: many-to-many relationship owned by SampleSource.
	 * Adding SampleSources added here without then calling fileService.addFileGroup() to persist this object will result in them NOT being persisted.
	 * @return the fileGroups
	 */
	public Set<SampleSource> getSampleSources() {
		return this.sampleSources;
	}

	/**
	 * WARNING: many-to-many relationship owned by SampleSource.
	 * Adding SampleSources added here without then calling fileService.addFileGroup() to persist this object will result in them NOT being persisted.
	 * @param fileGroups the fileGroups to set
	 */
	public void setSampleSources(Set<SampleSource> sampleSource) {
		this.sampleSources = sampleSource;
	}
	

}
