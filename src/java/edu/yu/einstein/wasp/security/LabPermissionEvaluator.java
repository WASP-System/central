package edu.yu.einstein.wasp.security;

import java.io.Serializable;
import org.springframework.security.core.Authentication;
import org.springframework.security.access.PermissionEvaluator;

import org.apache.log4j.Logger;


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
        
