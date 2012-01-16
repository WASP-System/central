package edu.yu.einstein.wasp.security;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;

/**
 * Custom authentication for WASP via the Wasp database. Specifies how Spring security looks up users and authorities
 * from the WASP database 
 * @author ASMcLellan
 *
 */
public class WaspJdbcDaoImpl extends JdbcDaoImpl {

	
	public WaspJdbcDaoImpl(){
		super();
		this.setAuthoritiesByUsernameQuery(
				"SELECT u.login username, case ab.roleid " +
				"when 1 then " +
				"case r.domain " +
					"when 'system' then provr.rolename " +
					"when 'department' then concat(provr.rolename, '-', du.departmentid) " +
					"when 'lab' then concat(provr.rolename, '-', lu.labid) " +
					"when 'job' then concat(provr.rolename, '-', ju.jobid) " +
					"when 'jobdraft' then concat(provr.rolename, '-', jd.jobdraftid) " +
					"when 'user' then concat(provr.rolename, '-', us.userid) " +
				"end " +
				"when 2 then " +
					"concat(provr.rolename, '-*') " +
				"end authority " +
				"FROM " +
					"user u " +
					"inner join role ab on (roleid in (1,2)) " +
					"inner join role r on (1 = 1) " +
					"inner join roleset rs on (r.roleid = rs.parentroleid) " +
					"inner join role provr on (rs.childroleid = provr.roleid) " +
					"left outer join userrole ur " +
					"on (u.userid = ur.userid and r.roleid = ur.roleid) " +
					"left outer join departmentuser du " +
					"on (u.userid = du.userid) " +
					"left outer join labuser lu " +
					"on (u.userid = lu.userid and r.roleid = lu.roleid) " +
					"left outer join jobuser ju " +
					"on (u.userid = ju.userid and r.roleid = ju.roleid) " +
					"left outer join jobdraft jd " +
					"on (u.userid = jd.userid and status = 'PENDING') " +
					"left outer join user us " +
					"on (u.userid = us.userid) " +
				"where " +
					"u.login = ? and " +
					"case r.domain " +
						"when 'system' then ifnull(ur.userid, 0) " +
						"when 'department' then ifnull(du.userid, 0) " +
						"when 'lab' then ifnull(lu.userid, 0) " +
						"when 'job' then ifnull(ju.userid, 0) " +
						"when 'jobdraft' then ifnull(jd.userid, 0) " +
						"when 'user' then ifnull(us.userid, 0) " +
					"end " +
					"group by 1, 2");
		
		this.setUsersByUsernameQuery("SELECT login username, password, isactive enabled FROM user WHERE login = ?");

	}
	
	/**
	 * obtains a list of WASP roles for the specified user from the WASP database. If the user is not in the WASP 
	 * database they are defaulted to have the single role of authenticated guest (role 'ag')
	 * @param username
	 * @return List<GrantedAuthority>
	 */
	public List<GrantedAuthority> getUserWaspAuthorities(String username){
		List<GrantedAuthority> authorities = this.loadUserAuthorities(username);
		if (authorities.isEmpty())
			authorities.add(new SimpleGrantedAuthority("ag"));
		authorities.add(new SimpleGrantedAuthority("ldap"));
		return authorities;
	}
}
