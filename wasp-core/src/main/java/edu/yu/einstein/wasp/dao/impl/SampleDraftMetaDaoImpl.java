
/**
 *
 * SampleDraftMetaDaoImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the SampleDraftMeta Dao Impl
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.MetaAttribute;
import edu.yu.einstein.wasp.model.MetaUtil;
import edu.yu.einstein.wasp.model.SampleDraftMeta;
import edu.yu.einstein.wasp.model.SubtypeSample;
import edu.yu.einstein.wasp.model.UserPendingMeta;
import edu.yu.einstein.wasp.service.SampleService;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class SampleDraftMetaDaoImpl extends WaspDaoImpl<SampleDraftMeta> implements edu.yu.einstein.wasp.dao.SampleDraftMetaDao {

	
	@Autowired
	private SampleService sampleService;
	/**
	 * SampleDraftMetaDaoImpl() Constructor
	 *
	 *
	 */
	public SampleDraftMetaDaoImpl() {
		super();
		this.entityClass = SampleDraftMeta.class;
	}


	/**
	 * getSampleDraftMetaBySampleDraftMetaId(final int sampleDraftMetaId)
	 *
	 * @param final int sampleDraftMetaId
	 *
	 * @return sampleDraftMeta
	 */

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public SampleDraftMeta getSampleDraftMetaBySampleDraftMetaId (final int sampleDraftMetaId) {
		
    	HashMap m = new HashMap();
		m.put("sampleDraftMetaId", sampleDraftMetaId);

		List<SampleDraftMeta> results = this.findByMap(m);

		if (results.size() == 0) {
			SampleDraftMeta rt = new SampleDraftMeta();
			return rt;
		}
		return results.get(0);
	}



	/**
	 * getSampleDraftMetaByKSampledraftId(final String k, final int sampledraftId)
	 *
	 * @param final String k, final int sampledraftId
	 *
	 * @return sampleDraftMeta
	 */

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public SampleDraftMeta getSampleDraftMetaByKSampledraftId (final String k, final int sampledraftId) {
		
    	HashMap m = new HashMap();
		m.put("k", k);
		m.put("sampledraftId", sampledraftId);

		List<SampleDraftMeta> results = this.findByMap(m);

		if (results.size() == 0) {
			SampleDraftMeta rt = new SampleDraftMeta();
			return rt;
		}
		return results.get(0);
	}



	/**
	 * updateBySampledraftId (final int sampledraftId, final List<SampleDraftMeta> metaList)
	 *
	 * @param sampledraftId
	 * @param metaList
	 *
	 */
	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public void updateBySampledraftId (final int sampledraftId, final List<SampleDraftMeta> metaList) {
		for (SampleDraftMeta m:metaList) {
			SampleDraftMeta currentMeta = getSampleDraftMetaByKSampledraftId(m.getK(), sampledraftId);
			if (currentMeta.getSampleDraftMetaId() == null){
				// metadata value not in database yet
				m.setSampledraftId(sampledraftId);
				entityManager.persist(m);
			} else if (!currentMeta.getV().equals(m.getV())){
				// meta exists already but value has changed
				currentMeta.setV(m.getV());
				entityManager.merge(currentMeta);
			} else{
				// no change to meta so do nothing
			}
		}
 	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<SubtypeSample,List<SampleDraftMeta>> getAllowableMetaFields(final int workflowId) {
		
	   String sql=
		   "select master.area,master.name,master.pos,label.attrValue as label,error.attrValue as error,\n"+ 
		   "control.attrValue as control,suffix.attrValue as suffix,constr.attrValue as 'constraint',\n"+
		   "type.attrValue as `type`,\n"+
		   "range.attrValue as `range`,\n"+
		   "master.subtypesampleid, master.subtypeName, master.arealist\n"+
		   "from \n"+
		   "(select distinct f.area,f.name,convert(f.attrValue, signed) pos, st.subtypesampleid, st.name as subtypeName, st.arealist as arealist\n"+
		   "from subtypesample st\n"+					   
		   "join uifield f on  (\n"+
		   "st.arealist regexp concat('^\\s*' , f.area , '\\s*$') or\n"+
		   "st.arealist regexp concat('^\\s*' , f.area , '\\s*,') or\n"+
		   "st.arealist regexp concat(',\\s*' , f.area , '\\s*,') or\n"+
		   "st.arealist regexp concat(',\\s*' , f.area , '\\s*$') )\n"+
		   "and f.attrName='metaposition'\n"+
		   "and f.locale='en_US'\n"+
		   "where st.subtypesampleid in (\n"+
		   "select st.subtypesampleid\n"+
		   "from workflowsubtypesample wst\n"+
		   "join subtypesample st on st.subtypesampleid = wst.subtypesampleid\n"+
		   "where wst.workflowid=:workflowid\n"+
		   ")\n"+
		   ") as master\n"+
		   "left outer join uifield label on master.area=label.area and master.name=label.name and label.attrName='label'\n"+
		   "left outer join uifield error on master.area=error.area and master.name=error.name and error.attrName='error'\n"+
		   "left outer join uifield control on master.area=control.area and master.name=control.name and control.attrName='control'\n"+
		   "left outer join uifield suffix on master.area=suffix.area and master.name=suffix.name and suffix.attrName='suffix'\n"+
		   "left outer join uifield constr on master.area=constr.area and master.name=constr.name and constr.attrName='constraint'\n"+
		   "left outer join uifield `type` on master.area=type.area and master.name=type.name and type.attrName='type'\n"+
		   "left outer join uifield `range` on master.area=range.area and master.name=range.name and range.attrName='range'\n"+
		   "order by master.subtypeSampleId,master.area,master.pos;\n";

	   
	   Map<SubtypeSample,List<SampleDraftMeta>> result=new LinkedHashMap<SubtypeSample,List<SampleDraftMeta>>();
	   Map<SubtypeSample, Map<String,List<SampleDraftMeta>> > tmp = new LinkedHashMap<SubtypeSample, Map<String,List<SampleDraftMeta>> >();
	   List<Object[]> listObj=entityManager.createNativeQuery(sql).setParameter("workflowid", workflowId).getResultList();
	   List<SubtypeSample> loggedInUserAccessibleSubtypeSamples = sampleService.getSubtypeSamplesForWorkflowByLoggedInUserRoles(workflowId);
	   for(Object[] o:listObj) {
		   
		   String area=(String)o[0];
		   String name=(String)o[1];
		   BigInteger position=(BigInteger)o[2];
		   String label=(String)o[3];
		   String error=(String)o[4];
		   String control=(String)o[5];
		   String suffix=(String)o[6];
		   String constraint=(String)o[7];
		   String metaType=(String)o[8];
		   String range=(String)o[9];
		   Integer subtypeSampleId=(Integer)o[10];
		   String subtypeName=(String)o[11];
		   String areaList=(String)o[12];
		   boolean subtypeSampleAllowed = false;
		   for (SubtypeSample sts: loggedInUserAccessibleSubtypeSamples){
			   if (sts.getSubtypeSampleId().equals(subtypeSampleId)){
				   subtypeSampleAllowed = true;
				   break;
			   }
		   }
		   if (!subtypeSampleAllowed) continue;
		   SampleDraftMeta m = new SampleDraftMeta();
		   
		   m.setK(area+"."+name);
		   
		   MetaAttribute attr = new MetaAttribute();					   
		   m.setProperty(attr);
		   
		   attr.setMetaposition(position==null?-1:position.intValue());					   
		   attr.setLabel(label);
		   attr.setError(error);
		   attr.setControl(MetaUtil.getControl(control));
		   attr.setSuffix(suffix);
		   attr.setConstraint(constraint);
		   attr.setMetaType(metaType);
		   attr.setRange(range);
		   
		   SubtypeSample subtypeNew=new SubtypeSample();
		   subtypeNew.setSubtypeSampleId(subtypeSampleId);
		   subtypeNew.setName(subtypeName);
		   subtypeNew.setAreaList(areaList);
		   
		   
		   Map<String,List<SampleDraftMeta>> areaMap = tmp.get(subtypeNew);   
		   if (areaMap == null){
			   // subtype not in the list yet so put it there with a blank list
			   areaMap = new LinkedHashMap<String,List<SampleDraftMeta>>();
			   for (String componentArea: subtypeNew.getComponentMetaAreas()){
				   areaMap.put(componentArea, new ArrayList<SampleDraftMeta>());
			   }
			   tmp.put(subtypeNew, areaMap);
		   }
		   areaMap.get(area).add(m);
	   }
	   for (SubtypeSample st : tmp.keySet()){
		   Map<String,List<SampleDraftMeta>> areaMap = tmp.get(st);
		   if (!result.containsKey(st))
			   result.put(st, new ArrayList<SampleDraftMeta>());
		   for (String componentArea : areaMap.keySet()){
			   result.get(st).addAll(areaMap.get(componentArea));
		   }
	   }
	   return result;
	}

}

