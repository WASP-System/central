package edu.yu.einstein.wasp.security;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;


public class LabPermissionEvaluator implements PermissionEvaluator {

  protected static Logger logger = LoggerFactory.getLogger(LabPermissionEvaluator.class.getName());

  public boolean hasPermission(Authentication authentication, int domainObjectId, Object permission) {
    return true;
  }

  @Override
public boolean hasPermission(Authentication authentication, Object domainObject, Object permission) {

    logger.error(domainObject.toString());

    if ("LM".equals(permission)) {
      return true;
    }
    return false;
  }

  @Override
public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
    return true;
  }
}
        
