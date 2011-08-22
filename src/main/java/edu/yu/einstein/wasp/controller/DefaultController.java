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

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.transaction.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
public class DefaultController extends WaspController {

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

    m.addAttribute("roles", getRoles());

    return d;
  }
}
