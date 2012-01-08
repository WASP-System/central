package edu.yu.einstein.wasp.load;

import edu.yu.einstein.wasp.service.UiFieldService;

import edu.yu.einstein.wasp.service.impl.WaspMessageSourceImpl;

import edu.yu.einstein.wasp.model.*;

import java.util.Map; 
import java.util.HashMap; 
import java.util.Set; 
import java.util.List; 
import java.util.Date; 
import java.util.ArrayList; 
import java.util.Locale; 

import org.springframework.stereotype.Service;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.*;

import org.springframework.context.MessageSource;

import org.springframework.util.StringUtils;

import util.spring.PostInitialize;
import org.apache.log4j.Logger;


/**
 * base clase for all the wasp loaders
 * full of convience methods like
 *  - updateUiFields
 *
 */

@Transactional
public abstract class WaspLoadService {

  @Autowired
  private MessageSource messageSource;

  @Autowired
  protected UiFieldService uiFieldService;

  protected String iname; 
  public void setIname(String iname) {this.iname = iname; }

  protected String name; 
  public void setName(String name) {this.name = name; }

  protected List<UiField> uiFields; 
  public void setUiFields(List<UiField> uiFields) {this.uiFields = uiFields; }

  protected static final Logger log = Logger.getLogger(WaspLoadService.class);

  public WaspLoadService (){};


  /* override me.... */
  @Transactional
  @PostInitialize 
  public void postInitialize() {
  }


  /**
   * updates the UiFields for that object
   * assumes htat UIFields were truncated on container load 
   * 
   * adds them to the cached messageSource
   */ 


  @Transactional
  public void updateUiFields() {
    updateUiFields(this.iname, this.uiFields);
  }

  @Transactional
  public void updateUiFields(String area, List<UiField> uiFields) {
    // UI fields
    /*
    // this assumes truncate to start with, so clear everything out
    // and use this.uiFields so 
    // TODO: logic to do CRUD compares instead
    Map m = new HashMap();
    m.put("locale", "en_US");
    m.put("area", area);
    List<UiField> oldUiFields = uiFieldService.findByMap(m);
    for (UiField uiField: oldUiFields) {
      uiFieldService.remove(uiField);
      uiFieldService.flush(uiField);
    }
    */

    // sets up the new
    for (UiField f: uiFields) {
      String key = f.getArea() + "." + f.getName() + "."
          + f.getAttrName();
      String lang = f.getLocale().substring(0, 2);
      String cntry = f.getLocale().substring(3);

      Locale locale = new Locale(lang, cntry);

      ((WaspMessageSourceImpl) messageSource).addMessage(key, locale, f.getAttrValue());
      uiFieldService.save(f); 
    }

    edu.yu.einstein.wasp.dao.impl.DBResourceBundle.MESSAGE_SOURCE=(WaspMessageSourceImpl)messageSource; //save handle to messageSource for easy access

  }

}

