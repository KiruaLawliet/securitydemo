package com.huawei.securitydemo.config;

import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.DefaultCsrfToken;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.security.SecureRandom;

/**
 * A {@link CsrfTokenRepository} that persists the CSRF token in a cookie named
 * "XSRF-TOKEN" and reads from the header "X-XSRF-TOKEN" following the conventions of
 * AngularJS. When using with AngularJS be sure to use {@link #withHttpOnlyFalse()}.
 *
 * @author Rob Winch
 * @since 4.1
 */
public final class CustomCookieCsrfTokenRepository implements CsrfTokenRepository {
	static final String DEFAULT_CSRF_COOKIE_NAME = "XSRF-TOKEN";

	static final String DEFAULT_CSRF_PARAMETER_NAME = "_csrf";

	static final String DEFAULT_CSRF_HEADER_NAME = "X-XSRF-TOKEN";

	private String parameterName = DEFAULT_CSRF_PARAMETER_NAME;

	private String headerName = DEFAULT_CSRF_HEADER_NAME;

	private String cookieName = DEFAULT_CSRF_COOKIE_NAME;

	private final Method setHttpOnlyMethod;

	private boolean cookieHttpOnly;

	private String cookiePath;
	
	private final int CSRFTOKEN_LENGTH = 24;
	
	private String sessionName = DEFAULT_CSRF_COOKIE_NAME;
	
	static final String DEFAULT_CSRF_EXPIRED = "CSRF_EXPIRED";
	
	private String sessionExpiredName = DEFAULT_CSRF_EXPIRED;
	
	private final char alphabet[] = "0123456789ABCDEF".toCharArray();
	
	private int csrfExpiredIntervalMinutes = 5;
	
	
	public CustomCookieCsrfTokenRepository() {
		this.setHttpOnlyMethod = ReflectionUtils.findMethod(Cookie.class, "setHttpOnly", boolean.class);
	}
	
	public CustomCookieCsrfTokenRepository(int intervalMinutes) {
		this();
		this.csrfExpiredIntervalMinutes = intervalMinutes;
	}

	@Override
	public CsrfToken generateToken(HttpServletRequest request) {
		return new DefaultCsrfToken(this.headerName, this.parameterName,
				createNewToken());
	}

	@Override
	public void saveToken(CsrfToken token, HttpServletRequest request,
			HttpServletResponse response) {
		
		String tokenValue = token == null ? "" : token.getToken();
		request.getSession(true).setAttribute(this.sessionName, tokenValue);
		
		Calendar calendar  = Calendar.getInstance();
		calendar.add(Calendar.MINUTE, this.csrfExpiredIntervalMinutes);
		request.getSession().setAttribute(this.sessionExpiredName, calendar.getTime().getTime());
		
		response.setHeader(this.headerName, tokenValue);
	}

	@Override
	public CsrfToken loadToken(HttpServletRequest request) {

		HttpSession session = request.getSession();
		String token = (String)session.getAttribute(this.sessionName);
		long expriedTime = -1;
		if (session.getAttribute(this.sessionExpiredName) != null)
		{
			expriedTime = (long)session.getAttribute(this.sessionExpiredName);
		}
		
		long now = new Date().getTime();
		if (StringUtils.isEmpty(token) || expriedTime < now) {
			return null;
		}
		return new DefaultCsrfToken(this.headerName, this.parameterName, token);
	}

	/**
	 * Sets the name of the HTTP request parameter that should be used to provide a token.
	 *
	 * @param parameterName the name of the HTTP request parameter that should be used to
	 * provide a token
	 */
	public void setParameterName(String parameterName) {
		Assert.notNull(parameterName, "parameterName is not null");
		this.parameterName = parameterName;
	}

	/**
	 * Sets the name of the HTTP header that should be used to provide the token.
	 *
	 * @param headerName the name of the HTTP header that should be used to provide the
	 * token
	 */
	public void setHeaderName(String headerName) {
		Assert.notNull(headerName, "headerName is not null");
		this.headerName = headerName;
	}

	/**
	 * Sets the name of the cookie that the expected CSRF token is saved to and read from.
	 *
	 * @param cookieName the name of the cookie that the expected CSRF token is saved to
	 * and read from
	 */
	public void setCookieName(String cookieName) {
		Assert.notNull(cookieName, "cookieName is not null");
		this.cookieName = cookieName;
	}

	/**
	 * Sets the HttpOnly attribute on the cookie containing the CSRF token.
	 * The cookie will only be marked as HttpOnly if both <code>cookieHttpOnly</code> is <code>true</code> and the underlying version of Servlet is 3.0 or greater.
	 * Defaults to <code>true</code> if the underlying version of Servlet is 3.0 or greater.
	 * NOTE: The {@link Cookie#setHttpOnly(boolean)} was introduced in Servlet 3.0.
	 *
	 * @param cookieHttpOnly <code>true</code> sets the HttpOnly attribute, <code>false</code> does not set it (depending on Servlet version)
	 * @throws IllegalArgumentException if <code>cookieHttpOnly</code> is <code>true</code> and the underlying version of Servlet is less than 3.0
	 */
	public void setCookieHttpOnly(boolean cookieHttpOnly) {
		if (cookieHttpOnly && setHttpOnlyMethod == null) {
			throw new IllegalArgumentException("Cookie will not be marked as HttpOnly because you are using a version of Servlet less than 3.0. NOTE: The Cookie#setHttpOnly(boolean) was introduced in Servlet 3.0.");
		}
		this.cookieHttpOnly = cookieHttpOnly;
	}

	/**
	 * Factory method to conveniently create an instance that has
	 * {@link #setCookieHttpOnly(boolean)} set to false.
	 *
	 * @return an instance of CustomCookieCsrfTokenRepository with
	 * {@link #setCookieHttpOnly(boolean)} set to false
	 */
	public static CustomCookieCsrfTokenRepository withHttpOnlyFalse() {
		CustomCookieCsrfTokenRepository result = new CustomCookieCsrfTokenRepository();
		result.setCookieHttpOnly(false);
		return result;
	}

	private String createNewToken() {
		SecureRandom random = new SecureRandom();
		byte buffer[] = new byte[this.CSRFTOKEN_LENGTH];
		random.nextBytes(buffer);
		
		StringBuilder csrftoken = new StringBuilder();
		for(int i=0;i<buffer.length;i++)
		{
			csrftoken.append(alphabet[(buffer[i]>>>4)&0x0f]);
			csrftoken.append(alphabet[buffer[i]&0x0f]);
		}
		return csrftoken.toString();
	}

	/**
	 * Set the path that the Cookie will be created with. This will override the default functionality which uses the
	 * request context as the path.
	 *
	 * @param path the path to use
	 */
	public void setCookiePath(String path) {
		this.cookiePath = path;
	}

	/**
	 * Get the path that the CSRF cookie will be set to.
	 *
	 * @return the path to be used.
	 */
	public String getCookiePath() {
		return this.cookiePath;
	}
}

