package edu.yu.einstein.test.service.impl;

import edu.yu.einstein.wasp.exception.LoginNameException;
import edu.yu.einstein.wasp.model.User;
import edu.yu.einstein.wasp.service.AuthenticationService;

public class StubAuthenticationServiceImpl implements AuthenticationService {

	@Override
	public User getAuthenticatedUser() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getRoles() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isExternallyAuthenticated() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAuthenticationSetExternal() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isAuthenticatedGuest() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasRole(String theRole) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isSuperUser() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean authenticate(String name, String password) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isLoginNameWellFormed(String login) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isLoginAlreadyInUse(String login, String email)
			throws LoginNameException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void logoutUser() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isAuthenticated() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean authenticates(String name, String password) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean hasRoleInRoleArray(String[] roleArray) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean hasRoleInRoleArray(String[] rolesToCompare,
			String[] rolesBaseline) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String encodePassword(String s) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean validatePassword(String s) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean matchPassword(String s1, String s2) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String getRandomPassword(int length) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isFacilityMember() {
		// TODO Auto-generated method stub
		return true;
	}

}
