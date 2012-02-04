package edu.yu.einstein.wasp.batch;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.item.ItemReader;


// Generic Filename Reader, 
// takes in filename
// and forwards them to ItemProcessor

// @ // Component
public class FilenameReader implements ItemReader {

  private static final Log log = LogFactory.getLog(StateReader.class);

  private int index = 0;

  String filename = null;
  public void setFilename(String filename) {
    this.filename = filename;
  }
  public String getFilename() {
    return this.filename;
  }

  @Override
  public String read() {

    if (this.filename == null) return null;

    log.info( "Reader Filename: " + this.filename );
    String rt = this.getFilename(); 
    setFilename(null); 

    return rt;
  }
}

