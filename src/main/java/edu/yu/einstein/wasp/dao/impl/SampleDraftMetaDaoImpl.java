
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

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

import org.springframework.orm.jpa.JpaCallback;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.MetaAttribute;
import edu.yu.einstein.wasp.model.MetaUtil;
import edu.yu.einstein.wasp.model.SampleDraftMeta;
import edu.yu.einstein.wasp.model.SubtypeSample;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class SampleDraftMetaDaoImpl extends WaspDaoImpl<SampleDraftMeta> implements edu.yu.einstein.wasp.dao.SampleDraftMetaDao {

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

	@SuppressWarnings("unchecked")
	@Transactional
	public SampleDraftMeta getSampleDraftMetaBySampleDraftMetaId (final int sampleDraftMetaId) {
		
    	HashMap m = new HashMap();
		m.put("sampleDraftMetaId", sampleDraftMetaId);

		List<SampleDraftMeta> results = (List<SampleDraftMeta>) this.findByMap((Map) m);

		if (results.size() == 0) {
			SampleDraftMeta rt = new SampleDraftMeta();
			return rt;
		}
		return (SampleDraftMeta) results.get(0);
	}



	/**
	 * getSampleDraftMetaByKSampledraftId(final String k, final int sampledraftId)
	 *
	 * @param final String k, final int sampledraftId
	 *
	 * @return sampleDraftMeta
	 */

	@SuppressWarnings("unchecked")
	@Transactional
	public SampleDraftMeta getSampleDraftMetaByKSampledraftId (final String k, final int sampledraftId) {
		
    	HashMap m = new HashMap();
		m.put("k", k);
		m.put("sampledraftId", sampledraftId);

		List<SampleDraftMeta> results = (List<SampleDraftMeta>) this.findByMap((Map) m);

		if (results.size() == 0) {
			SampleDraftMeta rt = new SampleDraftMeta();
			return rt;
		}
		return (SampleDraftMeta) results.get(0);
	}



	/**
	 * updateBySampledraftId (final int sampledraftId, final List<SampleDraftMeta> metaList)
	 *
	 * @param sampledraftId
	 * @param metaList
	 *
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public void updateBySampledraftId (final int sampledraftId, final List<SampleDraftMeta> metaList) {
		entityManager.createNativeQuery("delete from sampledraftmeta where sampledraftId=:sampledraftId").setParameter("sampledraftId", sampledraftId).executeUpdate();

		for (SampleDraftMeta m:metaList) {
			m.setSampledraftId(sampledraftId);
			entityManager.persist(m);
		}
 	}

	/**
	 * returns list of meta fields allowed for the given workflowId
	 * 
	 * @author Sasha Levchuk
	 */
	
	public Map<SubtypeSample,List<SampleDraftMeta>> getAllowableMetaFields(final int workflowId) {
		
	   String sql=
		   "select master.area,master.name,master.pos,label.attrValue as label,error.attrValue as error,\n"+ 
		   "control.attrValue as control,suffix.attrValue as suffix,constr.attrValue as 'constraint',\n"+
		   "master.subtypesampleid, master.subtypeName\n"+
		   "from \n"+
		   "(select distinct f.area,f.name,convert(f.attrValue, signed) pos, st.subtypesampleid, st.name as subtypeName\n"+
		   "from subtypesample st\n"+					   
		   // "join uifield f on st.iname = concat(f.area,'Sample')\n"+ 
		   "join uifield f on st.iname = f.area\n"+ 
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
		   "left outer join uifield error on master.area=error.area and master.name=error.name and label.attrName='error'\n"+
		   "left outer join uifield control on master.area=control.area and master.name=control.name and label.attrName='control'\n"+
		   "left outer join uifield suffix on master.area=suffix.area and master.name=suffix.name and label.attrName='suffix'\n"+
		   "left outer join uifield constr on master.area=constr.area and master.name=constr.name and label.attrName='constraint'\n"+
		   "order by master.pos\n";

	   
	   Map<SubtypeSample,List<SampleDraftMeta>> result=new LinkedHashMap<SubtypeSample,List<SampleDraftMeta>>();
	   
	   List<Object[]> listObj=entityManager.createNativeQuery(sql).setParameter("workflowid", workflowId).getResultList();
	   for(Object[] o:listObj) {
		   
		   String area=(String)o[0];
		   String name=(String)o[1];
		   BigInteger position=(BigInteger)o[2];
		   String label=(String)o[3];
		   String error=(String)o[4];
		   String control=(String)o[5];
		   String suffix=(String)o[6];
		   String constraint=(String)o[7];
		   Integer subtypeSampleId=(Integer)o[8];
		   String subtypeName=(String)o[9];
		   
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
		   
		   SubtypeSample subtypeNew=new SubtypeSample();
		   subtypeNew.setSubtypeSampleId(subtypeSampleId);
		   subtypeNew.setName(subtypeName);
		   
		   List<SampleDraftMeta> list=result.get(subtypeNew);
		   
		   if (list==null) {
			   list=new ArrayList<SampleDraftMeta>();
			   result.put(subtypeNew, list);
		   }
		   
		   list.add(m);
	   }
	   return result;
	}

}

