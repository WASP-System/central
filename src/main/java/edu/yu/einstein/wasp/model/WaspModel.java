/**
 *
 * WaspModel.java
 * @author echeng
 *
 * this is the base class,
 * every class in the system should extend this.
 *
 *
 */

package edu.yu.einstein.wasp.model;

import java.io.Serializable;

import org.apache.log4j.Logger;

public abstract class WaspModel implements Serializable {

  // generic logger included with every class.
  protected static Logger logger = Logger.getLogger(WaspModel.class.getName());


}
