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
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.JobSample;
import edu.yu.einstein.wasp.model.MetaBase;
import edu.yu.einstein.wasp.model.Run;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleFile;
import edu.yu.einstein.wasp.model.SampleMeta;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.model.SubtypeSample;
import edu.yu.einstein.wasp.model.TypeSample;
import edu.yu.einstein.wasp.model.User;
import edu.yu.einstein.wasp.service.JobService;
import edu.yu.einstein.wasp.service.RunService;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.service.SubtypeSampleService;
import edu.yu.einstein.wasp.service.TypeSampleCategoryService;
import edu.yu.einstein.wasp.service.TypeSampleService;
import edu.yu.einstein.wasp.service.UserService;
import edu.yu.einstein.wasp.taglib.JQFieldTag;

@Controller
@Transactional
@RequestMapping("/sample")
public class SampleController extends WaspController {

  
  private SampleService sampleService;
  
  private TypeSampleService	typeSampleService;
  
  @Autowired
  private SubtypeSampleService	subtypeSampleService;
  
  @Autowired
  private TypeSampleCategoryService	typeSampleCategoryService;
  
  @Autowired
  private JobService jobService;
  
  @Autowired
  private UserService userService;
  
  @Autowired
  private RunService runService;
  
  @Autowired
  public void setSampleService(SampleService sampleService) {
    this.sampleService = sampleService;
  }
  public SampleService getSampleService() {
    return this.sampleService;
  }
  
  @Autowired
  public void setTypeSampleService(TypeSampleService typeSampleService) {
	  this.typeSampleService = typeSampleService;
}

  public TypeSampleService getTypeSampleService() {
	    return this.typeSampleService;
  }

  @RequestMapping("/list")
  public String list(ModelMap m) {
    //List <Sample> sampleList = this.getSampleService().findAll();
    
    m.addAttribute("_metaList", getMetaHelperWebapp().getMasterList(MetaBase.class));
	m.addAttribute(JQFieldTag.AREA_ATTR, getMetaHelperWebapp().getArea());
	m.addAttribute("_metaDataMessages", MetaHelperWebapp.getMetadataMessages(request.getSession()));
    
    //m.addAttribute("sample", sampleList);
	prepareSelectListData(m);


    return "sample/list";
  }

  /*
  @RequestMapping("/list")
  public String list(ModelMap m) {
    List <Sample> sampleList = this.getSampleService().findAll();
    
   
    
    m.addAttribute("sample", sampleList);

    return "sample/list";
  }
*/

  @RequestMapping(value="/detail/{strId}", method=RequestMethod.GET)
  public String detail(@PathVariable("strId") String strId, ModelMap m) {
    String now = (new Date()).toString();

    Integer i;
    try {
      i = new Integer(strId);
    } catch (Exception e) {
      return "default";
    }

    Sample sample = this.getSampleService().getById(i.intValue());

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
		return new MetaHelperWebapp("sample", SampleMeta.class, request.getSession());
  }
	
	/**
	 * Prepares page to display JQGrid table with a list of samples where TypeSampleCategory.iName="biomaterial"
	 * 
	 * @Author Natalia Volnova
	 */
  	private static List<Sample>  joinById (List <Sample> listOne, List <TypeSample> ListTwo) {
  		
  		List <Sample> finalList = new ArrayList <Sample> ();

		for (Iterator <TypeSample> it=ListTwo.iterator(); it.hasNext();) {
			TypeSample tempTypeSample = it.next();
			for (Iterator <Sample> itSample=listOne.iterator(); itSample.hasNext();) {
				Sample tempSample = itSample.next();
				
				if (tempSample.getTypeSampleId().intValue() == tempTypeSample.getTypeSampleId().intValue()) {
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

		Map<String, String> m = new HashMap<String, String>();

		List<Sample> sampleList = new ArrayList<Sample>();

		String sord = request.getParameter("sord");
		String sidx = request.getParameter("sidx");

		if (request.getParameter("_search") == null || StringUtils.isEmpty(request.getParameter("searchString"))) {
			sampleList = sidx.isEmpty() ? this.sampleService.findAll() : this.sampleService.findAllOrderBy(sidx, sord);
		} else {

			m.put(request.getParameter("searchField"), request.getParameter("searchString"));

			sampleList = this.getSampleService().findByMap(m);

			if ("ne".equals(request.getParameter("searchOper"))) {
				List<Sample> allSamples = new ArrayList<Sample>(sidx.isEmpty() ? this.sampleService.findAll() : this.sampleService.findAllOrderBy(sidx, sord));

				for (Iterator<Sample> it = sampleList.iterator(); it.hasNext();) {
					Sample excludeSample = it.next();
					allSamples.remove(excludeSample);
				
				}
				sampleList = allSamples;
			}
		}

		try {

			Map<Integer, String> allTypeSamples = new TreeMap<Integer, String>();
			for (TypeSample typeSample : (List<TypeSample>) this.getTypeSampleService().findAll()) {
				allTypeSamples.put(typeSample.getTypeSampleId(), typeSample.getName());
			}
			Map<Integer, String> allSubTypeSamples = new TreeMap<Integer, String>();
			for (SubtypeSample subtypeSample : (List<SubtypeSample>) subtypeSampleService.findAll()) {
				allSubTypeSamples.put(subtypeSample.getSubtypeSampleId(), subtypeSample.getName());
			}

			Map<Integer, String> allJobs = new TreeMap<Integer, String>();
			for (Job job : (List<Job>) jobService.findAll()) {
				allJobs.put(job.getJobId(), job.getName());
			}

			Map<Integer, String> allUsers = new TreeMap<Integer, String>();
			for (User user : (List<User>) userService.findAll()) {
				allUsers.put(user.getUserId(), user.getLastName() + ", " + user.getFirstName());
			}

			Map<Integer, String> allRuns = new TreeMap<Integer, String>();
			for (Run run : (List<Run>) runService.findAll()) {
				allRuns.put(run.getSampleId(), run.getName());
			}

			// Remove all samples whose typesamplecategory is not "biomaterial"
			List<Sample> sampleListFiltered = new ArrayList<Sample> ();
			for (Sample sample : sampleList) {
				if (sample.getTypeSample().getTypeSampleCategory().getIName().equals("biomaterial")) {
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
			sampleData.put("selId", StringUtils.isEmpty(request.getParameter("selId")) ? "" : request.getParameter("selId"));
			jqgrid.put("sampledata", sampleData);
			 
			/***** Begin Sort by User name *****/
			class SampleSubmitterNameComparator implements Comparator<Sample> {
				public int compare(Sample arg0, Sample arg1) {
					return arg0.getUser().getLastName().compareToIgnoreCase(arg1.getUser().getLastName());
				}
			}

			if (sidx.equals("submitterUserId")) {
				Collections.sort(sampleList, new SampleSubmitterNameComparator());
				if (sord.equals("desc"))
					Collections.reverse(sampleList);
			}
			/***** End Sort by User name *****/

			List<Map> rows = new ArrayList<Map>();

			int frId = pageRowNum * (pageIndex - 1);
			int toId = pageRowNum * pageIndex;
			toId = toId <= rowNum ? toId : rowNum;

			// if the selId is set, change the page index to the one contains the selId 
			if (!StringUtils.isEmpty(request.getParameter("selId"))) {
				int selId = Integer.parseInt(request.getParameter("selId"));
				int selIndex = sampleList.indexOf(sampleService.findById(selId));
				frId = selIndex;
				toId = frId + 1;

				jqgrid.put("records", "1");
				jqgrid.put("total", "1");
				jqgrid.put("page", "1");
			}

			//List<Sample> samplePage = sampleList.subList(frId, toId);
			//for (Sample sample:samplePage) {
			List<Sample> samplePage = sampleList.subList(frId, toId);
			for (Sample sample : samplePage) {

				Map cell = new HashMap();
				cell.put("id", sample.getSampleId());

				List<SampleMeta> sampleMeta = getMetaHelperWebapp().syncWithMaster(sample.getSampleMeta());

				List<String> cellList=new ArrayList<String>(Arrays.asList(new String[] {
						sample.getName(),
						(sample.getTypeSampleId() == null)? "": allTypeSamples.get(sample.getTypeSampleId()),
						(sample.getSubtypeSampleId() == null)? "": allSubTypeSamples.get(sample.getSubtypeSampleId()),
						(sample.getSubmitterJobId() == null)? "" : allJobs.get(sample.getSubmitterJobId()),
						allUsers.get(sample.getSubmitterUserId()),
						(sample.getIsActive() == 1)? "Yes":"No",
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
