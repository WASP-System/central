/**
 * 
 */
package edu.yu.einstein.wasp.file.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author calder
 *
 */
@Controller
public class ErrorController {

	@RequestMapping(value="/error/404.html")
    public String handle404() {
    	return "errorPageTemplate";
    }
}
