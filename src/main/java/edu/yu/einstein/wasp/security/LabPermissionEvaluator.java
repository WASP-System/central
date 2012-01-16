package edu.yu.einstein.wasp.security;

import java.io.Serializable;

import org.apache.log4j.Logger;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;


public class LabPermissionEvaluator implements PermissionEvaluator {

  protected static Logger logger = Logger.getLogger(LabPermissionEvaluator.class.getName());



  public boolean hasPermission(Authentication authentication, int domainObjectId, Object permission) {
    return true;
  }

  public boolean hasPermission(Authentication authentication, Object domainObject, Object permission) {

    logger.error(domainObject);

    if ("LM".equals(permission)) {
      return true;
    }
    return false;
  }

  public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
    return true;
  }
}
        
