package edu.yu.einstein.wasp.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.yu.einstein.wasp.controller.util.MetaHelperWebapp;
import edu.yu.einstein.wasp.dao.JobDao;
import edu.yu.einstein.wasp.dao.RunDao;
import edu.yu.einstein.wasp.dao.SampleDao;
import edu.yu.einstein.wasp.dao.SampleSubtypeDao;
import edu.yu.einstein.wasp.dao.SampleTypeCategoryDao;
import edu.yu.einstein.wasp.dao.SampleTypeDao;
import edu.yu.einstein.wasp.dao.UserDao;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.JobSample;
import edu.yu.einstein.wasp.model.MetaBase;
import edu.yu.einstein.wasp.model.Run;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleFile;
import edu.yu.einstein.wasp.model.SampleMeta;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.model.SampleSubtype;
import edu.yu.einstein.wasp.model.SampleType;
import edu.yu.einstein.wasp.model.User;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.taglib.JQFieldTag;
import edu.yu.einstein.wasp.util.MetaHelper;

@Controller
@Transactional
@RequestMapping("/sample")
public class SampleController extends WaspController {

  
  private SampleDao sampleDao;
  
  private SampleTypeDao	sampleTypeDao;
  
  @Autowired
  private SampleSubtypeDao	sampleSubtypeDao;
  
  @Autowired
  private SampleTypeCategoryDao	sampleTypeCategoryDao;
  
  @Autowired
  private JobDao jobDao;
  
  @Autowired
  private UserDao userDao;
  
  @Autowired
  private RunDao runDao;
  
  @Autowired
  private SampleService sampleService;
  
  @Autowired
  public void setSampleDao(SampleDao sampleDao) {
    this.sampleDao = sampleDao;
  }
  public SampleDao getSampleDao() {
    return this.sampleDao;
  }
  
  @Autowired
  public void setSampleTypeDao(SampleTypeDao sampleTypeDao) {
	  this.sampleTypeDao = sampleTypeDao;
}

  public SampleTypeDao getSampleTypeDao() {
	    return this.sampleTypeDao;
  }

  @RequestMapping("/list")
  public String list(ModelMap m) {
    //List <Sample> sampleList = this.getSampleDao().findAll();
    
    m.addAttribute("_metaList", getMetaHelperWebapp().getMasterList(MetaBase.class));
	m.addAttribute(JQFieldTag.AREA_ATTR, getMetaHelperWebapp().getArea());
	m.addAttribute("_metaDataMessages", MetaHelper.getMetadataMessages(request.getSession()));
    
    //m.addAttribute("sample", sampleList);
	prepareSelectListData(m);


    return "sample/list";
  }

  @RequestMapping(value="/detail/{strId}", method=RequestMethod.GET)
  public String detail(@PathVariable("strId") String strId, ModelMap m) {
    String now = (new Date()).toString();

    Integer i;
    try {
      i = new Integer(strId);
    } catch (Exception e) {
      return "default";
    }

    Sample sample = this.getSampleDao().getById(i.intValue());

    List<SampleMeta> sampleMetaList = sample.getSampleMeta();
    sampleMetaList.size();

    List<JobSample> jobSampleList = sample.getJobSample();
    jobSampleList.size();

    List<SampleFile> sampleFileList = sample.getSampleFile();
    sampleFileList.size();

    List<SampleSource> parentSampleList = sample.getSampleSource();
    parentSampleList.size();

    List<SampleSource> childSampleList = sample.getSampleSourceViaSourceSampleId();
    childSampleList.size();

    m.addAttribute("now", now);
    m.addAttribute("sample", sample);
    m.addAttribute("samplemeta", sampleMetaList);
    m.addAttribute("jobsample", jobSampleList);
    m.addAttribute("samplefile", sampleFileList);
    m.addAttribute("parentsample", parentSampleList);
    m.addAttribute("childsample", childSampleList);

    return "sample/detail";
  }

  private final MetaHelperWebapp getMetaHelperWebapp() {
		return new MetaHelperWebapp(SampleMeta.class, request.getSession());
  }
	
	/**
	 * Prepares page to display JQGrid table with a list of samples where SampleTypeCategory.iName="biomaterial"
	 * 
	 * @Author Natalia Volnova
	 */
  	private static List<Sample>  joinById (List <Sample> listOne, List <SampleType> ListTwo) {
  		
  		List <Sample> finalList = new ArrayList <Sample> ();

		for (Iterator <SampleType> it=ListTwo.iterator(); it.hasNext();) {
			SampleType tempSampleType = it.next();
			for (Iterator <Sample> itSample=listOne.iterator(); itSample.hasNext();) {
				Sample tempSample = itSample.next();
				
				if (tempSample.getSampleTypeId().intValue() == tempSampleType.getSampleTypeId().intValue()) {
					finalList.add(tempSample);
				}
			}
		}
  		
  		return finalList;
  	}
  	
  	/**
  	 * 
  	 * @param response
  	 * @return
  	 */

	@RequestMapping(value = "/listJSON", method = RequestMethod.GET)
	public String getListJSON(HttpServletResponse response) {

		// result
		Map<String, Object> jqgrid = new HashMap<String, Object>();

		List<Sample> sampleList = new ArrayList<Sample>();

		String sord = request.getParameter("sord");
		String sidx = request.getParameter("sidx");
		String search = request.getParameter("_search");
		String searchField = request.getParameter("searchField");
		String searchString = request.getParameter("searchString");
		String selId = request.getParameter("selId");

		if (!StringUtils.isEmpty(selId)) {

			sampleList.add(this.sampleDao.getSampleBySampleId(Integer.parseInt(selId)));
		
		} else if (!StringUtils.isEmpty(search) && !StringUtils.isEmpty(searchField) && !StringUtils.isEmpty(searchString) ) {
		
			Map<String, String> m = new HashMap<String, String>();

			m.put(searchField, searchString);

			if (sidx.isEmpty()) {
				sampleList = this.sampleDao.findByMap(m);
			} else {
				List<String> sidxList =  new ArrayList<String>();
				sidxList.add(sidx);
				sampleList = this.sampleDao.findByMapDistinctOrderBy(m, null, sidxList, sord);
			}

			if ("ne".equals(request.getParameter("searchOper"))) {
				List<Sample> allSamples = new ArrayList<Sample>(sidx.isEmpty() ? 
						this.sampleDao.findAll() : this.sampleDao.findAllOrderBy(sidx, sord));

				for (Iterator<Sample> it = sampleList.iterator(); it.hasNext();) {
					Sample excludeSample = it.next();
					allSamples.remove(excludeSample);

				}
				sampleList = allSamples;
			}
			
		} else {
			
			sampleList = sidx.isEmpty() ? this.sampleDao.findAll() : this.sampleDao.findAllOrderBy(sidx, sord);
		}

		try {

			Map<Integer, String> allSampleTypes = new TreeMap<Integer, String>();
			for (SampleType sampleType : this.getSampleTypeDao().findAll()) {
				allSampleTypes.put(sampleType.getSampleTypeId(), sampleType.getName());
			}
			Map<Integer, String> allSubSampleTypes = new TreeMap<Integer, String>();
			for (SampleSubtype sampleSubtype : sampleSubtypeDao.findAll()) {
				allSubSampleTypes.put(sampleSubtype.getSampleSubtypeId(), sampleSubtype.getName());
			}

			Map<Integer, String> allJobs = new TreeMap<Integer, String>();
			for (Job job : jobDao.findAll()) {
				allJobs.put(job.getJobId(), job.getName());
			}

			Map<Integer, String> allUsers = new TreeMap<Integer, String>();
			for (User user : userDao.findAll()) {
				allUsers.put(user.getUserId(), user.getLastName() + ", " + user.getFirstName());
			}

			Map<Integer, String> allRuns = new TreeMap<Integer, String>();
			for (Run run : runDao.findAll()) {
				allRuns.put(run.getSampleId(), run.getName());
			}

			// Remove all samples whose sampletypecategory is not "biomaterial"
			List<Sample> sampleListFiltered = new ArrayList<Sample> ();
			for (Sample sample : sampleList) {
				if (sample.getSampleType().getSampleTypeCategory().getIName().equals("biomaterial")) {
					sampleListFiltered.add(sample);
				
				}
			}
			sampleList = sampleListFiltered;
			
			int pageIndex = Integer.parseInt(request.getParameter("page"));		// index of page
			int pageRowNum = Integer.parseInt(request.getParameter("rows"));	// number of rows in one page
			int rowNum = sampleList.size();										// total number of rows
			int pageNum = (rowNum + pageRowNum - 1) / pageRowNum;				// total number of pages
			
			jqgrid.put("records", rowNum + "");
			jqgrid.put("total", pageNum + "");
			jqgrid.put("page", pageIndex + "");

			Map<String, String> sampleData = new HashMap<String, String>();
			sampleData.put("page", pageIndex + "");
			sampleData.put("selId", StringUtils.isEmpty(selId) ? "" : selId);
			jqgrid.put("sampledata", sampleData);
			 
			/***** Begin Sort by User last name *****/
			class SampleSubmitterNameComparator implements Comparator<Sample> {
				@Override
				public int compare(Sample arg0, Sample arg1) {
					return arg0.getUser().getLastName().compareToIgnoreCase(arg1.getUser().getLastName());
				}
			}

			if (sidx.equals("submitterUserId")) {
				Collections.sort(sampleList, new SampleSubmitterNameComparator());
				if (sord.equals("desc"))
					Collections.reverse(sampleList);
			}
			/***** End Sort by User last name *****/

			List<Map> rows = new ArrayList<Map>();

			int frId = pageRowNum * (pageIndex - 1);
			int toId = pageRowNum * pageIndex;
			toId = toId <= rowNum ? toId : rowNum;

			// if the selId is set, change the page index to the one contains the selId 
//			if (!StringUtils.isEmpty(request.getParameter("selId"))) {
//				int selId = Integer.parseInt(request.getParameter("selId"));
//				int selIndex = sampleList.indexOf(sampleDao.findById(selId));
//				frId = selIndex;
//				toId = frId + 1;
//
//				jqgrid.put("records", "1");
//				jqgrid.put("total", "1");
//				jqgrid.put("page", "1");
//			}

			//List<Sample> samplePage = sampleList.subList(frId, toId);
			//for (Sample sample:samplePage) {
			List<Sample> samplePage = sampleList.subList(frId, toId);
			for (Sample sample : samplePage) {

				Map cell = new HashMap();
				cell.put("id", sample.getSampleId());

				List<SampleMeta> sampleMeta = getMetaHelperWebapp().syncWithMaster(sample.getSampleMeta());

				List<String> cellList=new ArrayList<String>(Arrays.asList(new String[] {
						sample.getName(),
						(sample.getSampleTypeId() == null)? "": allSampleTypes.get(sample.getSampleTypeId()),
						(sample.getSampleSubtypeId() == null)? "": allSubSampleTypes.get(sample.getSampleSubtypeId()),
						(sample.getSubmitterJobId() == null)? "" : allJobs.get(sample.getSubmitterJobId()),
						allUsers.get(sample.getSubmitterUserId()),
						sampleService.convertReceiveSampleStatusForWeb(sampleService.getReceiveSampleStatus(sample)),
						allRuns.get(sample.getSampleId())
				}));

				for (SampleMeta meta : sampleMeta) {
					cellList.add(meta.getV());
				}

				cell.put("cell", cellList);

				rows.add(cell);

				jqgrid.put("rows", rows);

			}
			return outputJSON(jqgrid, response);

		} catch (Throwable e) {
			throw new IllegalStateException("Can't marshall to JSON " + sampleList, e);
		}
	}

}
