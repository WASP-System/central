
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
import edu.yu.einstein.wasp.model.SampleSubtype;
import edu.yu.einstein.wasp.service.SampleService;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class SampleDraftMetaDaoImpl extends WaspMetaDaoImpl<SampleDraftMeta> implements edu.yu.einstein.wasp.dao.SampleDraftMetaDao {

	
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
	 * getSampleDraftMetaByKSampledraftId(final String k, final int sampleDraftId)
	 *
	 * @param final String k, final int sampleDraftId
	 *
	 * @return sampleDraftMeta
	 */

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public SampleDraftMeta getSampleDraftMetaByKSampledraftId (final String k, final int sampledraftId) {
		
    	HashMap m = new HashMap();
		m.put("k", k);
		m.put("sampleDraftId", sampledraftId);

		List<SampleDraftMeta> results = this.findByMap(m);

		if (results.size() == 0) {
			SampleDraftMeta rt = new SampleDraftMeta();
			return rt;
		}
		return results.get(0);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<SampleSubtype,List<SampleDraftMeta>> getAllowableMetaFields(final int workflowId) {
		
	   String sql=
		   "select master.area,master.name,master.pos,label.attrValue as label,error.attrValue as error,\n"+ 
		   "control.attrValue as control,suffix.attrValue as suffix,constr.attrValue as 'constraint',\n"+
		   "type.attrValue as `type`,\n"+
		   "range.attrValue as `range`,\n"+
		   "master.samplesubtypeid, master.sampletypeid, master.subtypeName, master.arealist\n"+
		   "from \n"+
		   "(select distinct f.area,f.name,convert(f.attrValue, signed) pos, st.samplesubtypeid, st.sampletypeid, st.name as subtypeName, st.arealist as arealist\n"+
		   "from samplesubtype st\n"+					   
		   "join uifield f on  (\n"+
		   "st.arealist regexp concat('^\\s*' , f.area , '\\s*$') or\n"+
		   "st.arealist regexp concat('^\\s*' , f.area , '\\s*,') or\n"+
		   "st.arealist regexp concat(',\\s*' , f.area , '\\s*,') or\n"+
		   "st.arealist regexp concat(',\\s*' , f.area , '\\s*$') )\n"+
		   "and f.attrName='metaposition'\n"+
		   "and f.locale='en_US'\n"+
		   "where st.samplesubtypeid in (\n"+
		   "select st.samplesubtypeid\n"+
		   "from workflowSampleSubtype wst\n"+
		   "join samplesubtype st on st.samplesubtypeid = wst.samplesubtypeid\n"+
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
		   "order by master.sampleSubtypeId,master.area,master.pos;\n";

	   
	   Map<SampleSubtype,List<SampleDraftMeta>> result=new LinkedHashMap<SampleSubtype,List<SampleDraftMeta>>();
	   Map<SampleSubtype, Map<String,List<SampleDraftMeta>> > tmp = new LinkedHashMap<SampleSubtype, Map<String,List<SampleDraftMeta>> >();
	   List<Object[]> listObj=entityManager.createNativeQuery(sql).setParameter("workflowid", workflowId).getResultList();
	   List<SampleSubtype> loggedInUserAccessibleSampleSubtypes = sampleService.getSampleSubtypesForWorkflowByLoggedInUserRoles(workflowId);
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
		   Integer sampleSubtypeId=(Integer)o[10];
		   Integer sampleTypeId=(Integer)o[11];
		   String subtypeName=(String)o[12];
		   String areaList=(String)o[13];
		   boolean sampleSubtypeAllowed = false;
		   for (SampleSubtype sts: loggedInUserAccessibleSampleSubtypes){
			   if (sts.getSampleSubtypeId().equals(sampleSubtypeId)){
				   sampleSubtypeAllowed = true;
				   break;
			   }
		   }
		   if (!sampleSubtypeAllowed) continue;
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
		   
		   SampleSubtype subtypeNew=new SampleSubtype();
		   subtypeNew.setSampleSubtypeId(sampleSubtypeId);
		   subtypeNew.setName(subtypeName);
		   subtypeNew.setAreaList(areaList);
		   subtypeNew.setSampleTypeId(sampleTypeId);
		   
		   
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
	   for (SampleSubtype st : tmp.keySet()){
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

