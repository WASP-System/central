package edu.yu.einstein.wasp.batch;

import edu.yu.einstein.wasp.model.*;
import edu.yu.einstein.wasp.service.*;

import org.springframework.batch.item.ItemReader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;


// Generic State Reader, 
// takes in StateIds
// and forwards them to ItemProcessor

// @ // Component
public class StateReader implements ItemReader {

  private static final Log log = LogFactory.getLog(StateReader.class);

  private int index = 0;

  Integer stateId = null;
  public void setStateId(Integer stateId) {
    this.stateId = stateId;
  }
  public Integer getStateId() {
    return this.stateId;
  }

  @Override
  public Integer read() {

    if (this.stateId == null) return null;

    log.info( "Reader State: " + this.stateId );
    Integer rt = this.getStateId(); 
    setStateId(null); 

    return rt;
  }
}

