package edu.yu.einstein.wasp.controller;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.ResourceBundle;
import java.util.Locale;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;

import edu.yu.einstein.wasp.model.User;
import edu.yu.einstein.wasp.service.UserService;

import edu.yu.einstein.wasp.model.MetaAttribute;
import edu.yu.einstein.wasp.model.MetaAttribute.Country;
import edu.yu.einstein.wasp.model.MetaAttribute.State;

@Controller
public class WaspController {

  protected final Logger logger = Logger.getLogger(getClass());

  protected static final Map<String, String> LOCALES=new TreeMap<String, String>();
  static { LOCALES.put("en_US","English");
                LOCALES.put("iw_IL","Hebrew");
                LOCALES.put("ru_RU","Russian");
                LOCALES.put("ja_JA","Japanese");
                }

  @Autowired
  protected UserService userService;

  @Autowired
  HttpServletRequest request;

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


  protected void prepareSelectListData(ModelMap m) {
    m.addAttribute("countries", Country.getList());
    m.addAttribute("states", State.getList());
    m.addAttribute("locales", LOCALES);
  }

  protected String getMessage(String key) {
    String message=(String)getBundle().getObject(key);
    return message;
  }

  protected ResourceBundle getBundle() {
    Locale locale=(Locale)request.getSession().getAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME);

    if (locale==null) locale=Locale.ENGLISH;

    ResourceBundle bundle=ResourceBundle.getBundle("messages",locale);

    return bundle;
  }


}
