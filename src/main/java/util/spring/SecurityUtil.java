package util.spring;

import java.io.IOException;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.ApplicationContext;
import org.springframework.core.GenericTypeResolver;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ParseException;
import org.springframework.security.access.expression.ExpressionUtils;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.FilterInvocation;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class SecurityUtil {

	public static boolean isAuthorized(String permsission) throws IOException {

		if (SecurityContextHolder.getContext().getAuthentication() == null) {

			return false;

		}

		SecurityExpressionHandler<FilterInvocation> handler = getExpressionHandler();

		Expression accessExpression;

		try {

			accessExpression = handler.getExpressionParser().parseExpression(permsission);

		} catch (ParseException e) {

			IOException ioException = new IOException();

			ioException.initCause(e);

			throw ioException;

		}

		return ExpressionUtils.evaluateAsBoolean(accessExpression, createExpressionEvaluationContext(handler));

	}

	private static SecurityExpressionHandler<FilterInvocation> getExpressionHandler() throws IOException {

		ApplicationContext appContext = WebApplicationContextUtils.getRequiredWebApplicationContext(getRequest().getSession().getServletContext());

		Map<String, SecurityExpressionHandler> handlers = appContext.getBeansOfType(SecurityExpressionHandler.class);

		for (SecurityExpressionHandler h : handlers.values()) {

			if (FilterInvocation.class.equals(GenericTypeResolver.resolveTypeArgument(h.getClass(),

			SecurityExpressionHandler.class))) {

				return h;

			}

		}

		throw new IOException("No visible WebSecurityExpressionHandler instance could be found in the application "

		+ "context. There must be at least one in order to support expressions in JSP 'authorize' tags.");

	}

	private static HttpServletRequest getRequest() {

		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		return request;

	}

	private static EvaluationContext createExpressionEvaluationContext(SecurityExpressionHandler<FilterInvocation> handler) {

		HttpServletRequest request = getRequest();

		FilterInvocation f = new FilterInvocation(getRequest().getServletPath(), request.getMethod());

		return handler.createEvaluationContext(SecurityContextHolder.getContext().getAuthentication(), f);

	}

}