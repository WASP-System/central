package pojo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import edu.yu.einstein.wasp.dao.SampleSourceDao;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleMeta;



public class LibraryWrapper {

	private Sample parent = null;
	
	private Sample library = null;
	
	private List<String> areaList = null;
	
	private Map<Integer, List<SampleMeta>> metadataBySample = null;
	
	@Autowired
	private SampleSourceDao sampleSourceDao;
	
	public LibraryWrapper(Sample library){
		this.library = library;
		metadataBySample.put(library.getSampleId(), new ArrayList<SampleMeta>() );
		for(SampleMeta sm: library.getSampleMeta()){
			metadataBySample.get(library.getSampleId()).add(sm);
		}
  		this.parent = sampleSourceDao.getParentSampleByDerivedSampleId(library.getSampleId());
  		if (this.parent.getSampleId() != null){
  			metadataBySample.put(this.parent.getSampleId(), new ArrayList<SampleMeta>() );
  			for(SampleMeta sm: this.parent.getSampleMeta()){
  				// add parent metadata 
  				metadataBySample.get(this.parent.getSampleId()).add(sm);
  			}
  		} else {
  			this.parent = null;
  		}
	}
/*	
	public List<SampleMeta> getParentAndChildMeta(){
		List<SampleMeta> sm = new ArrayList<SampleMeta>();
		sm.addAll(c)
		return (List<SampleMeta>) this.metadataBySample.values();
	}
	
	public List<SampleMeta> getParentMeta(){
		return (List<SampleMeta>) this.metadataBySample.values();
	}
*/	
	public void setParent(Sample parent){
		this.parent = parent;
	}
	
	public Sample getParent(){
		return this.parent;
	}
	
	public void setLibrary(Sample library){
		this.library = library;
	}
	
	public Sample getLibrary(){
		return this.library;
	}

	public List<String> getAreaList(){
		return this.areaList;
	}
	
}
