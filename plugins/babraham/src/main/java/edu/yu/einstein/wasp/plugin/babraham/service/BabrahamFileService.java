package edu.yu.einstein.wasp.plugin.babraham.service;

import org.json.JSONArray;

import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.filetype.service.FileTypeService;
import edu.yu.einstein.wasp.model.FileGroup;

public interface BabrahamFileService extends FileTypeService {
    
    /**
     * Set metadata for trim_galore results
     * @param fastqGroup
     * @param json
     * @throws MetadataException 
     */
    public void setFastqTrimJSON(FileGroup fastqGroup, JSONArray json) throws MetadataException;
    
    /**
     * Get trim_galore results as JSON.  
     * @param fastqGroup
     * @return JSON array of trimmed positions, null if not set
     */
    public JSONArray getFastqTrimJSON(FileGroup fastqGroup);

}
