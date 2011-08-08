package edu.yu.einstein.wasp.controller;

import java.util.Collection;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.GrantedAuthority;

import org.springframework.stereotype.Controller;

import edu.yu.einstein.wasp.model.User;
import edu.yu.einstein.wasp.service.UserService;

@Controller
public class WaspController {

  protected final Log logger = LogFactory.getLog(getClass());

  @Autowired
  protected UserService userService;

  public User getAuthenticatedUser() { 
    Authentication authentication = SecurityContextHolder.getContext()
                                .getAuthentication();

    User user = this.userService.getUserByLogin(authentication.getName());

    return user;
  }

  public String[] getRoles() {
    Authentication authentication = SecurityContextHolder.getContext()
                                .getAuthentication();

    // java.util.Collection<GrantedAuthority> col = authentication.getAuthorities();
    java.util.Collection col = authentication.getAuthorities();
    String[] roles = new String[col.size()];
    int i = 0;
    for (Iterator<GrantedAuthority> it = col.iterator(); it.hasNext();) {

      GrantedAuthority grantedAuthority = it.next();
      roles[i] = grantedAuthority.getAuthority();
      i++;
    }
    return roles;
  }


}
