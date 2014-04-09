package edu.yu.einstein.wasp.service.impl;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.GenericTypeResolver;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ParseException;
import org.springframework.security.access.expression.ExpressionUtils;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.support.WebApplicationContextUtils;

import edu.yu.einstein.wasp.Assert;
import edu.yu.einstein.wasp.service.WebAuthenticationService;

@Service
@Transactional
public class WebAuthenticationServiceImpl extends AuthenticationServiceImpl implements WebAuthenticationService {
	
	private Logger logger = LoggerFactory.getLogger(WebAuthenticationServiceImpl.class);

	public static HttpServletRequest getHttpServletRequest() {
		try {
			HttpServletRequest request =
				((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
			return request;
		} catch (Throwable e) {
			// logger.warn("could not get HttpServletRequest");
		}
		return null;
	}
	
	@Override
	public void logoutUser(){
		if (this.isAuthenticated()){
			SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
			logoutHandler.setInvalidateHttpSession(true);
			logoutHandler.logout(getHttpServletRequest(), null, null);
		}
	}
	
	/**
	 * Enables parsing of Spring security expressions against the logged in user's security context. Using the 
	 * parameter map, parameters can be substituted in the permission string. Consider "hasRole('su') or hasRole('fm') or hasRole('jv-#jobId')".
	 * If parameterMap contains the parameter "jobId" with value "3" the following will be evaluated: ""
	 * "hasRole('su') or hasRole('fm') or hasRole('jv-3')"
	 * 
	 * See://static.springsource.org/spring-security/site/docs/3.0.x/reference/el-access.html for more expression options
	 * @param permsission
	 * @return
	 * @throws IOException
	 */
    @Override
    public boolean hasPermission(String permission, Map<String, Integer> parameterMap) throws IOException {
    	Assert.assertParameterNotNull(permission, "permission must be set");
    	if (parameterMap != null && !parameterMap.isEmpty()){
    		for (String key: parameterMap.keySet()){
    			parameterMap.get(key);
    			permission = permission.replaceAll("#" + key, parameterMap.get(key).toString());
    		}
    		if (permission.contains("#"))
    			throw new IOException("not all placeholders in permission string have been resolved from parameter map");
    	}
    	return hasPermission(permission);
    }
	
	/**
	 * Enables parsing of Spring security expressions against the logged in user's security context.
	 * 
	 * Can parse a string such as "hasRole('su') or hasRole('fm') or hasRole('ft')".
	 * 
	 * See://static.springsource.org/spring-security/site/docs/3.0.x/reference/el-access.html for more expression options
	 * @param permsission
	 * @return
	 * @throws IOException
	 */
    @Override
	public boolean hasPermission(String permission) throws IOException {
    	logger.debug("Evaluating permission string: " + permission);
		if (SecurityContextHolder.getContext().getAuthentication() == null) {
			return false;
		}
		SecurityExpressionHandler<FilterInvocation> handler = getExpressionHandler();
		Expression accessExpression;
		try {
			accessExpression = handler.getExpressionParser().parseExpression(permission);
		} catch (ParseException e) {
			IOException ioException = new IOException();
			ioException.initCause(e);
			throw ioException;
		}
		return ExpressionUtils.evaluateAsBoolean(accessExpression, createExpressionEvaluationContext(handler));

	}
	
	private static SecurityExpressionHandler<FilterInvocation> getExpressionHandler() throws IOException {
		ApplicationContext appContext = WebApplicationContextUtils.getRequiredWebApplicationContext(getHttpServletRequest().getSession().getServletContext());
		for (SecurityExpressionHandler<FilterInvocation> handler : appContext.getBeansOfType(SecurityExpressionHandler.class).values()) {
			if (FilterInvocation.class.equals(GenericTypeResolver.resolveTypeArgument(handler.getClass(),
			SecurityExpressionHandler.class))) {
				return handler;
			}
		}
		throw new IOException("No visible WebSecurityExpressionHandler instance could be found in the application "
		+ "context. There must be at least one in order to support expressions in JSP 'authorize' tags.");
	}


	private static EvaluationContext createExpressionEvaluationContext(SecurityExpressionHandler<FilterInvocation> handler) {
		HttpServletRequest request = getHttpServletRequest();
		FilterInvocation f = new FilterInvocation(request.getServletPath(), request.getMethod());
		return handler.createEvaluationContext(SecurityContextHolder.getContext().getAuthentication(), f);

	}

}
