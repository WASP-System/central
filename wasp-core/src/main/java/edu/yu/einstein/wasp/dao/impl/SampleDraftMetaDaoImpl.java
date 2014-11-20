
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


@Transactional("entityManager")
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
	@Transactional("entityManager")
	public SampleDraftMeta getSampleDraftMetaBySampleDraftMetaId (final int sampleDraftMetaId) {
		
    	HashMap<String, Integer> m = new HashMap<String, Integer>();
		m.put("id", sampleDraftMetaId);

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
	@Transactional("entityManager")
	public SampleDraftMeta getSampleDraftMetaByKSampledraftId (final String k, final int sampledraftId) {
		
    	HashMap<String, Object> m = new HashMap<String, Object>();
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
	@SuppressWarnings("unchecked")
	@Override
	public Map<SampleSubtype,List<SampleDraftMeta>> getAllowableMetaFields(final int workflowId) {
		
	   String sql=
		   "SELECT master.area, master.name, master.pos, label.attrValue AS label, error.attrValue AS error,\n"+ 
		   "control.attrValue AS control, suffix.attrValue AS suffix, constr.attrValue AS 'constraint',\n"+
		   "type.attrValue AS `type`,\n"+
		   "range.attrValue AS `range`,\n"+
		   "master.samplesubtypeid, master.sampletypeid, master.subtypeName, master.arealist\n"+
		   "FROM \n"+
		   "(SELECT DISTINCT f.area, f.name, convert(f.attrValue, signed) pos, st.id, st.sampletypeid, st.name AS subtypeName, st.arealist AS arealist\n"+
		   "FROM SampleSubtype st\n"+					   
		   "JOIN UiField f ON  (\n"+
		   "st.arealist regexp concat('^\\s*' , f.area , '\\s*$') OR\n"+
		   "st.arealist regexp concat('^\\s*' , f.area , '\\s*,') OR\n"+
		   "st.arealist regexp concat(',\\s*' , f.area , '\\s*,') OR\n"+
		   "st.arealist regexp concat(',\\s*' , f.area , '\\s*$') )\n"+
		   "AND f.attrName='metaposition'\n"+
		   "AND f.locale='en_US'\n"+
		   "WHERE st.id in (\n"+
		   "SELECT st.id samplesubtypeid\n"+
		   "FROM WorkflowSampleSubtype wst\n"+
		   "JOIN samplesubtype st on st.id = wst.samplesubtypeid\n"+
		   "WHERE wst.workflowid=:workflowid\n"+
		   ")\n"+
		   ") AS master\n"+
		   "LEFT OUTER JOIN uifield label ON master.area=label.area AND master.name=label.name AND label.attrName='label'\n"+
		   "LEFT OUTER JOIN uifield error ON master.area=error.area AND master.name=error.name AND error.attrName='error'\n"+
		   "LEFT OUTER JOIN uifield control ON master.area=control.area AND master.name=control.name AND control.attrName='control'\n"+
		   "LEFT OUTER JOIN uifield suffix ON master.area=suffix.area AND master.name=suffix.name AND suffix.attrName='suffix'\n"+
		   "LEFT OUTER JOIN uifield constr ON master.area=constr.area AND master.name=constr.name AND constr.attrName='constraint'\n"+
		   "LEFT OUTER JOIN uifield `type` ON master.area=type.area AND master.name=type.name AND type.attrName='type'\n"+
		   "LEFT OUTER JOIN uifield `range` ON master.area=range.area AND master.name=range.name AND range.attrName='range'\n"+
		   "ORDER BY master.sampleSubtypeId, master.area,master.pos;\n";

	   
	   Map<SampleSubtype,List<SampleDraftMeta>> result=new LinkedHashMap<SampleSubtype,List<SampleDraftMeta>>();
	   Map<SampleSubtype, Map<String,List<SampleDraftMeta>> > tmp = new LinkedHashMap<SampleSubtype, Map<String,List<SampleDraftMeta>> >();
	   List<Object[]> listObj=entityManager.createQuery(sql).setParameter("workflowid", workflowId).getResultList();
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

