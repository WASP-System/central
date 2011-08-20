package edu.yu.einstein.wasp.service.impl;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Service;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;

import edu.yu.einstein.wasp.service.PasswordEncoderService;


@Service
public class PasswordEncoderServiceImpl implements PasswordEncoderService {
	
  public String encodePassword(String s) {
    PasswordEncoder encoder = new ShaPasswordEncoder();
    String hashedPassword = encoder.encodePassword(s, null);

    return hashedPassword;
  }
	
}

