package edu.yu.einstein.wasp.integration.test.stubs;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.exception.LoginNameException;
import edu.yu.einstein.wasp.model.User;
import edu.yu.einstein.wasp.service.AuthenticationService;


@Service
public class StubAuthenticationServiceImpl implements AuthenticationService {

	@Override
	public User getAuthenticatedUser() {
		// Auto-generated method stub
		return null;
	}

	@Override
	public String[] getRoles() {
		// Auto-generated method stub
		return null;
	}

	@Override
	public boolean isExternallyAuthenticated() {
		// Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAuthenticationSetExternal() {
		// Auto-generated method stub
		return false;
	}

	@Override
	public boolean isAuthenticatedGuest() {
		// Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasRole(String theRole) {
		// Auto-generated method stub
		return true;
	}

	@Override
	public boolean isSuperUser() {
		// Auto-generated method stub
		return true;
	}

	@Override
	public boolean authenticate(String name, String password) {
		// Auto-generated method stub
		return true;
	}

	@Override
	public boolean isLoginNameWellFormed(String login) {
		// Auto-generated method stub
		return true;
	}

	@Override
	public boolean isLoginAlreadyInUse(String login, String email)
			throws LoginNameException {
		// Auto-generated method stub
		return false;
	}

	@Override
	public void logoutUser() {
		// Auto-generated method stub

	}

	@Override
	public boolean isAuthenticated() {
		// Auto-generated method stub
		return true;
	}

	@Override
	public boolean authenticates(String name, String password) {
		// Auto-generated method stub
		return true;
	}

	@Override
	public boolean hasRoleInRoleArray(String[] roleArray) {
		// Auto-generated method stub
		return true;
	}

	@Override
	public boolean hasRoleInRoleArray(String[] rolesToCompare,
			String[] rolesBaseline) {
		// Auto-generated method stub
		return true;
	}

	@Override
	public String encodePassword(String s) {
		// Auto-generated method stub
		return null;
	}

	@Override
	public boolean validatePassword(String s) {
		// Auto-generated method stub
		return true;
	}

	@Override
	public boolean matchPassword(String s1, String s2) {
		// Auto-generated method stub
		return true;
	}

	@Override
	public String getRandomPassword(int length) {
		// Auto-generated method stub
		return null;
	}

	@Override
	public boolean isFacilityMember() {
		// Auto-generated method stub
		return true;
	}

	@Override
	public boolean isOnlyDepartmentAdministrator() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasPermission(String permission,
			Map<String, Integer> parameterMap) throws IOException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasPermission(String permsission) throws IOException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Integer getRoleValue(String role) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Integer> idsOfDepartmentsManagedByCurrentUser() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Integer> idsOfLabsManagedByCurrentUser() {
		// TODO Auto-generated method stub
		return null;
	}

}
