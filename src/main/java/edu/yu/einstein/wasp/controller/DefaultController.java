package edu.yu.einstein.wasp.controller;

/**
 * this controller just forwards to the view
 *
 *
 *
 *
 *
 *
 */

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.yu.einstein.wasp.service.AuthenticationService;

@Controller
public class DefaultController extends WaspController {

  @Autowired
  private AuthenticationService authenticationService;

  @RequestMapping("/**/*")
  public String def(HttpServletRequest req, ModelMap m) {
    String c = req.getContextPath();
    String s = req.getRequestURI();
    String p = req.getServletPath();

    // strips context, lead slash ("/"), spring mapping 
    String d = s.replaceFirst(c + "/", "").replaceFirst("\\.do.*$", "");
   
    m.addAttribute("c", c);
    m.addAttribute("s", s);
    m.addAttribute("p", p);
    m.addAttribute("d", d);

    m.addAttribute("roles", authenticationService.getRoles());

    return d;
  }
}
