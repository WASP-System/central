/**
 * 
 */
package edu.yu.einstein.wasp.plugin.babraham.service.impl;

import org.json.JSONArray;
import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.filetype.service.impl.FileTypeServiceImpl;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.plugin.babraham.service.BabrahamFileService;

/**
 * @author calder
 *
 */
@Service
public class BabrahamFileServiceImpl extends FileTypeServiceImpl implements BabrahamFileService {
    
    public static final String BABRAHAM_FILE_AREA = "Babraham file area";
    
    public static final String BABRAHAM_TRIM_GALORE_RESULTS = "Babraham trim_galore results JSON";

    /** 
     * {@inheritDoc}
     * @throws MetadataException 
     */
    @Override
    public void setFastqTrimJSON(FileGroup fastqGroup, JSONArray json) throws MetadataException {
        this.setMeta(fastqGroup, BABRAHAM_FILE_AREA, BABRAHAM_TRIM_GALORE_RESULTS, json.toString());

    }

    /** 
     * {@inheritDoc}
     */
    @Override
    public JSONArray getFastqTrimJSON(FileGroup fastqGroup) {
        return new JSONArray(this.getMeta(fastqGroup, BABRAHAM_FILE_AREA, BABRAHAM_TRIM_GALORE_RESULTS));
    }

}
