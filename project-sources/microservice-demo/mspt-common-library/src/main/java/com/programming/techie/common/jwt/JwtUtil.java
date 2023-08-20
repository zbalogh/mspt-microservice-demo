package com.programming.techie.common.jwt;

import java.util.function.Function;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

public class JwtUtil
{
	private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);
	
	private static final String BEARER_PREFIX = "Bearer ";
	
	private static final String AUTHORIZATION_HEADER = "Authorization";
	
	private static final String JWT__PREFERRED_USERNAME = "preferred_username";
	
	private static final String JWT__UNIQUE_NAME = "unique_name";
	
	private static final String JWT__USER_NAME = "username";
	
	private static final String JWT__USER_ID = "user_id";
	
	private static final String JWT__USERID = "userid";
	
	/**
	 * It reads the user name from the given bearer token.
	 * If no user found, then it returns NULL value.
	 * 
	 * @param token
	 * @return
	 */
	public static String getUserNameFromBearerToken(String token)
	{
		try {
			// extract user from the token
			return extractUsername(token);
		}
		catch (Exception ex) {
			logger.error("Exception", ex);
		}
		// if error occurred then we return null value
		return null;
	}
	
	/**
	 * It reads the user name from the bearer token specified in the HTTP request header.
	 * If no user found, then it returns NULL value.
	 * 
	 * @param request
	 * @return
	 */
	public static String getUserNameFromBearerToken(HttpServletRequest request)
	{
		String token = getBearerTokenFromHttpRequest(request);
		
		if (token == null || token.isEmpty()) {
			return null;
		}
		
		return getUserNameFromBearerToken(token);
	}
	
	/**
	 * It reads the bearer token from the HTTP request header. If no token found, then it returns NULL value.
	 * 
	 * @param request
	 * @return
	 */
	public static String getBearerTokenFromHttpRequest(HttpServletRequest request)
	{
		if (request == null) {
			return null;
		}
		
		String authorizationHeader = request.getHeader(AUTHORIZATION_HEADER);
		
		if (authorizationHeader == null || authorizationHeader.isEmpty() || authorizationHeader.length() <= BEARER_PREFIX.length() || !authorizationHeader.startsWith(BEARER_PREFIX)) {
			return null;
		}
		
		String token = authorizationHeader.substring(7);
		
		return token;
	}
	
	/**
	 * It extract the user name from the token with the defined precedence of the claims: "preferred_username", "unique_name", "username", "sub".
	 * 
	 * @param token
	 * @return
	 */
	private static String extractUsername(String token)
    {
		String username = null;
		
		// 1. try to get user from the "preferred_username" claim
		username = extractClaim(token, (Claims claims) -> claims.get(JWT__PREFERRED_USERNAME, String.class) );
		
		if (username == null || username.isEmpty()) {
			// 2. try to get user from the "unique_name" claim
			username = extractClaim(token, (Claims claims) -> claims.get(JWT__UNIQUE_NAME, String.class) );
		}
		
		if (username == null || username.isEmpty()) {
			// 3. try to get user from the "username" claim
			username = extractClaim(token, (Claims claims) -> claims.get(JWT__USER_NAME, String.class) );
		}
		
		if (username == null || username.isEmpty()) {
			// 4. try to get user from the "user_id" claim
			username = extractClaim(token, (Claims claims) -> claims.get(JWT__USER_ID, String.class) );
		}
		
		if (username == null || username.isEmpty()) {
			// 5. try to get user from the "userid" claim
			username = extractClaim(token, (Claims claims) -> claims.get(JWT__USERID, String.class) );
		}
		
		if (username == null || username.isEmpty()) {
			// as last step, try to get user from the "sub" claim
			username = extractClaim(token, Claims::getSubject);
		}
		
		return username;
    }

	private static <R> R extractClaim(String token, Function<Claims, R> claimsResolver)
    {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    
    private static Claims extractAllClaims(String token)
    {
    	int i = token.lastIndexOf('.');
		String withoutSignature = token.substring(0, i+1);
		//return Jwts.parser().setSigningKey(SECRET_KEY.getBytes()).parseClaimsJws(token).getBody();
        return Jwts.parser().parseClaimsJwt(withoutSignature).getBody();
    }
    
}
