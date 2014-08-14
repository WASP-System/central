/**
 * 
 */
package edu.yu.einstein.wasp.service.impl;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.WordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.explore.wasp.ParameterValueRetrievalException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.Assert;
import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.exception.MetadataRuntimeException;
import edu.yu.einstein.wasp.exception.NullResourceException;
import edu.yu.einstein.wasp.exception.SampleTypeException;
import edu.yu.einstein.wasp.grid.GridHostResolver;
import edu.yu.einstein.wasp.grid.work.GridWorkService;
import edu.yu.einstein.wasp.grid.work.WorkUnit;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleDraft;
import edu.yu.einstein.wasp.model.SampleDraftMeta;
import edu.yu.einstein.wasp.model.SampleMeta;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.plugin.supplemental.organism.Build;
import edu.yu.einstein.wasp.plugin.supplemental.organism.Genome;
import edu.yu.einstein.wasp.plugin.supplemental.organism.Organism;
import edu.yu.einstein.wasp.service.GenomeService;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.util.MetaHelper;

/**
 * @author calder
 * 
 */
@Service
@Transactional("entityManager")
public class GenomeServiceImpl implements GenomeService, InitializingBean {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private Map<Integer, Organism> organisms = new TreeMap<Integer, Organism>();

	@Autowired
	private Properties localGenomesProperties;

	@Autowired
	private GridHostResolver hostResolver;
	
	@Autowired
	private SampleService sampleService;

	public GenomeServiceImpl() {
		logger.debug("CREATED GENOME SERVICE");
	}

	public void initialize() {
		logger.debug("INITIALIZING GENOME SERVICE");
		Map<Integer, Organism> orgs = initOrganisms();
		initGenomes(orgs);
		organisms = orgs;
	}

	public Map<Integer, Organism> initOrganisms() {
		Set<Object> keys = localGenomesProperties.keySet();
		logger.debug("found " + keys.size() + " entries.");
		Map<Integer, Organism> organisms = new HashMap<Integer, Organism>();
		for (Object k : keys) {
			String key = (String) k;
			logger.debug(key);
			if (key.startsWith("genome.organism.")) {
				key = key.replace("genome.organism.", "");
				if (key.endsWith(".name")) {
					Integer id = new Integer(key.replace(".name", ""));
					Organism o = new Organism(new Integer(id));
					o.setName(localGenomesProperties.getProperty((String) k));
					organisms.put(id, o);
				} else {
					Matcher km = Pattern.compile("^(.*?)\\.(.*)$").matcher(key);
					if (!km.find()) {
						String mess = "Unable to parse organism entry " + k.toString();
						logger.error(mess);
						throw new RuntimeException(mess);
					}
					Integer id = new Integer(km.group(1));
					String attrib = km.group(2);
					Organism o = (Organism) organisms.get(id);
					try {
						Method m = o.getClass().getMethod("set" + WordUtils.capitalize(attrib), java.lang.String.class);
						m.invoke(o, localGenomesProperties.get(k));
					} catch (Exception e) {
						String mess = "Unable to find attribute " + k.toString();
						logger.error(mess);
						e.printStackTrace();
						throw new RuntimeException(mess);
					}
				}
			}
		}
		return organisms;
	}

	public void initGenomes(Map<Integer, Organism> organisms) {
		Map<String, Genome> genomes = new HashMap<String, Genome>();
		Map<String, Build> builds = new HashMap<String, Build>();
		Set<String> genomesWithADefaultBuild = new HashSet<String>();
		Set<Integer> organismsWithADefaultGenome = new HashSet<Integer>();
		Set<Object> keys = localGenomesProperties.keySet();
		for (Object k : keys) {
			String key = (String) k;
			if (key.startsWith("genome.reference.")) {
				key = key.replace("genome.reference.", "");
				Matcher km = Pattern.compile("^(.*?)\\.(.*?)\\.(.*?)\\.([^\\.]*)").matcher(key);
				if (!km.find()) {
					String mess = "Unable to parse reference entry " + k.toString();
					logger.error(mess);
					throw new RuntimeException(mess);
				}
				Integer orgId = new Integer(km.group(1));
				String genomeName = km.group(2);
				String buildVersion = km.group(3);
				String attrib = km.group(4);
				logger.debug("init genomes: " + orgId + ":" + genomeName + ":" + buildVersion + ":" + attrib);
				if (!key.contains(".build.")) {
					// genome properties
					if (attrib.equals("name")) {
						String name = (String) localGenomesProperties.get(k);
						if (!name.equals(genomeName)) {
							String mess = "Genome with name " + genomeName + " does not match name value " + name;
							logger.error(mess);
							throw new RuntimeException(mess);
						}
						if (genomes.containsKey(name)) {
							String mess = "Genome with name " + name + " already exists";
							logger.debug(mess);
						} else {
							logger.debug("creating genome " + genomeName);
							Genome g = new Genome(name);
							Organism o = organisms.get(orgId);
							g.setOrganism(o);
							o.getGenomes().put(name, g);
							genomes.put(name, g);
						}
					} else if (attrib.equals("default")){
						boolean defaultValue = Boolean.parseBoolean((String) localGenomesProperties.get(k));
						if (defaultValue == true){
							if (organismsWithADefaultGenome.contains(orgId))
								throw new RuntimeException("More than one default genome specified for organism " + orgId.toString());
							organismsWithADefaultGenome.add(orgId);
						}
						genomes.get(genomeName).setDefault(defaultValue);
					} else {
						Genome g = genomes.get(genomeName);
						try {
							Method m = g.getClass().getMethod("set" + WordUtils.capitalize(attrib), java.lang.String.class);
							m.invoke(g, localGenomesProperties.get(k));
						} catch (Exception e) {
							String mess = "Unable to find attribute " + k.toString();
							logger.error(mess);
							e.printStackTrace();
							throw new RuntimeException(mess);
						}
					}
				} else {
					// build properties
					String buildName = genomeName + "." + buildVersion;
					String[] fields = key.split("\\.");
					logger.debug("fields: " + key + ":" + fields.length + ":" + fields.toString());
					if (fields[4].equals("name")) {
						String name = (String) localGenomesProperties.get(k);
						if (!name.equals(buildVersion)) {
							String mess = "Genome build with name " + genomeName + " and version " + buildVersion +
									" does not match value " + name;
							logger.error(mess);
							throw new RuntimeException(mess);
						}
						if (builds.containsKey(buildName)) {
							String mess = "Build with name " + buildName + " already exists";
							logger.error(mess);
							throw new RuntimeException(mess);
						}
						logger.debug("creating genome build " + name);
						Build b = new Build(name);
						Genome g = genomes.get(genomeName);
						b.setGenome(g);
						g.getBuilds().put(name, b);
						builds.put(buildName, b);
					} else if (fields.length == 5 && fields[4].equals("default")){
						boolean defaultValue = Boolean.parseBoolean((String) localGenomesProperties.get(k));
						if (defaultValue == true){
							if (genomesWithADefaultBuild.contains(genomeName))
								throw new RuntimeException("More than one default build specified for genome " + genomeName);
							genomesWithADefaultBuild.add(genomeName);
						}
						builds.get(buildName).setDefault(defaultValue);
					} else {
						Build b = builds.get(buildName);

						if (fields.length == 5) {
							// this is a build attribute
							try {
								Method m = b.getClass().getMethod("set" + WordUtils.capitalize(fields[4]), java.lang.String.class);
								m.invoke(b, localGenomesProperties.get(k));
							} catch (Exception e) {
								String mess = "Unable to find attribute " + k.toString();
								logger.error(mess);
								e.printStackTrace();
								throw new RuntimeException(mess);
							}
						} else if (fields.length >= 6) {
							// this is build metadata, currently Map<String,String> in the form dbsnp.filename=filename.vcf.gz
							String newKey = fields[4];
							for (int i=5; i < fields.length; i++)
								newKey += "." + fields[i];
							b.addMetadata(newKey, localGenomesProperties.get(k).toString());
							logger.debug("set build metatata on " + b.toString() + " : " + newKey + "=" + b.getMetadata(newKey));
						} else {
							String mess = "Don't know how to parse " + key;
							logger.error(mess);
							throw new RuntimeException(mess);
						}
					}
				}
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<Organism> getOrganisms() {
		return new TreeSet<Organism>(this.organisms.values());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Organism getOrganismById(Integer organismId) {
		return this.organisms.get(organismId);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean exists(GridWorkService workService, Build build, String index) {
		// TODO Auto-generated method stub
		// this is to get through at the moment
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Build getBuild(String delimitedParameterString) throws ParameterValueRetrievalException{
		Assert.assertParameterNotNull(delimitedParameterString, "jobParameterString cannot be null");
		String[] valueComponents = delimitedParameterString.split(DELIMITER, 3);
		if (valueComponents == null || valueComponents.length != 3)
			throw new ParameterValueRetrievalException("Did not get expected 3 components for genome string");
		Integer organismId = Integer.parseInt(valueComponents[0]);
		String genome = valueComponents[1];
		String build = valueComponents[2];
		return getBuild(organismId, genome, build);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDelimitedParameterString(Build build) {
		Assert.assertParameterNotNull(build, "build cannot be null");
		return build.getGenome().getOrganism().getNcbiID().toString() + DELIMITER +  build.getGenome().getName() + DELIMITER + build.getName();
	}
	
	/**
	 * {@inheritDoc}
	 * @throws ParameterValueRetrievalException 
	 * @throws SampleTypeException 
	 */
	@Override
	public String getDelimitedParameterString(Integer cellLibraryId) throws ParameterValueRetrievalException, SampleTypeException {
		return getDelimitedParameterString(getBuild(sampleService.getLibrary(sampleService.getCellLibraryBySampleSourceId(cellLibraryId))));
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 */
	@Override
	public void setBuild(SampleDraft sampleDraft, Build build) throws MetadataException {
		Assert.assertParameterNotNull(sampleDraft, "sampleDraft cannot be null");
		Assert.assertParameterNotNull(sampleDraft.getId(), "sampleDraft must be a valid entity");
		Assert.assertParameterNotNull(build, "build cannot be null");
		SampleDraftMeta sampleDraftMeta = new SampleDraftMeta();
		sampleDraftMeta.setK(GENOME_AREA + "." + GENOME_STRING_META_KEY);
		sampleDraftMeta.setV(getDelimitedParameterString(build));
		sampleDraftMeta.setSampleDraftId(sampleDraft.getId());
		sampleService.getSampleDraftMetaDao().setMeta(sampleDraftMeta);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 */
	@Override
	@Transactional("entityManager")
	public void setBuildToAllSampleDrafts(Set<SampleDraft> sampleDraftSet, Build build){
		Assert.assertParameterNotNull(sampleDraftSet, "sampleDraftSet cannot be null");
		try{
			for (SampleDraft sampleDraft : sampleDraftSet){
				setBuild(sampleDraft, build);
			}
		} catch (MetadataException e){
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 */
	@Override
	public void setBuild(Sample sample, Build build) throws MetadataException {
		Assert.assertParameterNotNull(sample, "sample cannot be null");
		Assert.assertParameterNotNull(sample.getId(), "sample must be a valid entity");
		Assert.assertParameterNotNull(build, "build cannot be null");
		SampleMeta sampleMeta = new SampleMeta();
		sampleMeta.setK(GENOME_AREA + "." + GENOME_STRING_META_KEY);
		sampleMeta.setV(getDelimitedParameterString(build));
		sampleMeta.setSampleId(sample.getId());
		sampleService.getSampleMetaDao().setMeta(sampleMeta);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 */
	@Override
	@Transactional("entityManager")
	public void setBuildToAllSamples(Set<Sample> sampleSet, Build build){
		Assert.assertParameterNotNull(sampleSet, "sampleSet cannot be null");
		try{
			for (Sample sample : sampleSet){
				setBuild(sample, build);
			}
		} catch (MetadataException e){
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Build getBuild(SampleDraft sampleDraft) throws ParameterValueRetrievalException{
		Assert.assertParameterNotNull(sampleDraft, "sampleDraft cannot be null");
		Assert.assertTrue(sampleService.isBiomolecule(sampleDraft), "Sample must be a biomolecule");
		String genomeString = null;
		List<SampleDraftMeta> sampleDraftMetaList = sampleDraft.getSampleDraftMeta();
		if (sampleDraftMetaList == null)
			sampleDraftMetaList = new ArrayList<SampleDraftMeta>();
		try{
			genomeString = (String) MetaHelper.getMetaValue(GENOME_AREA, GENOME_STRING_META_KEY, sampleDraftMetaList);
		} catch(MetadataException e) {
			// not found
			return null;
		}
		return getBuild(genomeString);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Build getBuild(Sample sample) throws ParameterValueRetrievalException{
		Assert.assertParameterNotNull(sample, "sample cannot be null");
		Assert.assertTrue(sampleService.isBiomolecule(sample), "Sample must be a biomolecule");
		sample = sampleService.getSampleDao().merge(sample); // ensure attached
		while (sample.getParent() != null)
			sample = sample.getParent();
		logger.debug("getting genome build for sample with id=" + sample.getId());
		String genomeString = null;
		List<SampleMeta> sampleMetaList = sample.getSampleMeta();
		if (sampleMetaList == null)
			sampleMetaList = new ArrayList<SampleMeta>();
		logger.debug("Got " + sampleMetaList.size() + " items of metadata for Sample id=" + sample.getId());
		try{
			genomeString = (String) MetaHelper.getMetaValue(GENOME_AREA, GENOME_STRING_META_KEY, sampleMetaList);
			logger.debug("Got genome string " + genomeString + " for Sample id=" + sample.getId());
		} catch(Exception e) {
			throw new ParameterValueRetrievalException(e);
		}
		return getBuild(genomeString);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		initialize();
	}
	
	@Override
	public Map<String, Build> getBuilds(Integer organism, String genome) throws ParameterValueRetrievalException {
		return organisms.get(organism).getGenomes().get(genome).getBuilds();
	}

	@Override
	public Build getBuild(Integer organism, String genome, String build) throws ParameterValueRetrievalException {
		return this.getBuilds(organism, genome).get(build);
	}
	
	@Override
	public Map<Integer, Organism> getOrganismMap(){
		return organisms;
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public String getRemoteBuildPath(Build build) {
		String path = "${" + WorkUnit.METADATA_ROOT + "}/" + 
				build.getGenome().getOrganism().getNcbiID() +"/"+
				build.getGenome().getName() + "/" + 
				build.getName() + "/";
		return path;
	}
	
	/** 
	 * {@inheritDoc}
	 */
	@Override
	public Set<Organism> getOrganismsPlusOther(){
		Set<Organism> organisms = getOrganisms();
		Organism other = new Organism(0);
		Genome oGenome = new Genome("Other");
		Build oBuild = new Build("Other");
		Map<String,Build> oBuilds = new HashMap<String,Build>();
		oBuilds.put("Other", oBuild);
		oGenome.setBuilds(oBuilds);
		Map<String,Genome> oGenomes = new HashMap<String,Genome>();
		oGenomes.put("Other", oGenome);
		other.setGenomes(oGenomes);
		other.setCommonName("Other");
		other.setName("Other");
		other.setAlias("Other");
		organisms.add(other);
		return organisms;
	}
	
	/** 
	 * {@inheritDoc}
	 */
	@Override
	public Build getGenomeBuild(SampleSource cellLibrary) {
		Build build = null;
		try {
			Sample library = sampleService.getLibrary(cellLibrary);
			logger.debug("looking for genome build associated with sample: " + library.getId());
			build = getBuild(library);
			if (build == null) {
				String mess = "cell library does not have associated genome build metadata annotation";
				logger.error(mess);
				throw new NullResourceException(mess);
			}
			logger.debug("genome build: " + build.getGenome().getName() + "::" + build.getName());
		} catch (ParameterValueRetrievalException e) {
			logger.error(e.toString());
			e.printStackTrace();
		}
		return build;
	}
	
	/** 
	 * {@inheritDoc}
	 */
	@Override
	public String getReferenceGenomeFastaFile(Build build) {
		String folder = build.getMetadata("fasta.folder");
		String filename = build.getMetadata("fasta.filename");
		if (folder == null || folder.isEmpty() || filename == null || filename.isEmpty())
			throw new MetadataRuntimeException("failed to locate reference genome fasta file");
		return getRemoteBuildPath(build) + "/" + folder + "/" + filename;
	}

}
