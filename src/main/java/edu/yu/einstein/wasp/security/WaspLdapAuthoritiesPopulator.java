package edu.yu.einstein.wasp.security;

import java.util.Collection;

import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
import org.springframework.security.ldap.userdetails.LdapAuthoritiesPopulator;

/**
 * A custom WASP class required for LDAP authentication. When a user authenticates successfully the 
 * LdapAuthenticationProvider needs to load the user's granted roles into the context. However,
 * in WASP we have custom roles obtained from the WASP database (not the LDAP server). This class
 * permits retrieval of these roles from the WASP database by userName.
 * 
 * @author ASMcLellan
 *
 */
public class WaspLdapAuthoritiesPopulator implements LdapAuthoritiesPopulator {
	
	private WaspJdbcDaoImpl jdbcDaoImpl;
	
	public WaspJdbcDaoImpl getWaspJdbcDaoImpl(){
		return jdbcDaoImpl;
	}
	
	public void setWaspJdbcDaoImpl(WaspJdbcDaoImpl jdbcDaoImpl){
		this.jdbcDaoImpl = jdbcDaoImpl;
	}
	
	public WaspLdapAuthoritiesPopulator(WaspJdbcDaoImpl jdbcDaoImpl){
		this.jdbcDaoImpl = jdbcDaoImpl;
	}

	@Override
	public Collection<? extends GrantedAuthority> getGrantedAuthorities(DirContextOperations context, String userName) {
		return jdbcDaoImpl.getUserWaspAuthorities(userName);
	}

}
