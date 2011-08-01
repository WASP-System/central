package edu.yu.einstein.wasp.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;

import org.springframework.util.StringUtils;

public final class MetaProperty implements Serializable {

		private String label;

		private String error;

		private String constraint;

		private Control control;

		private int metaposition;

		private static final ResourceBundle BASE_BUNDLE=ResourceBundle.getBundle("messages", Locale.ENGLISH);
		  
		
		public String getLabel() {
			return label;
		}

		public void setLabel(String label) {
			this.label = label;
		}

		public String getError() {
			return error;
		}

		public void setError(String error) {
			this.error = error;
		}

		public String getConstraint() {
			return constraint;
		}

		public void setConstraint(String constraint) {
			this.constraint = constraint;
		}

		public int getMetaposition() {
			return metaposition;
		}

		public void setMetaposition(int metaposition) {
			this.metaposition = metaposition;
		}

		public Control getControl() {
			return control;
		}

		public void setControl(Control control) {
			this.control = control;
		}
		
		

		public static final class Control implements Serializable{
			public enum Type {
				input, select
			}

			private Type type;
			private List<Option> options;

			private String items;
			private String itemValue;
			private String itemLabel;
			
			public String getItems() {
				return items;
			}

			public void setItems(String items) {
				this.items = items;
			}

			public String getItemValue() {
				return itemValue;
			}

			public void setItemValue(String itemValue) {
				this.itemValue = itemValue;
			}

			public String getItemLabel() {
				return itemLabel;
			}

			public void setItemLabel(String itemLabel) {
				this.itemLabel = itemLabel;
			}

			public static final class Option implements Serializable {
				private String value;
				private String label;


				public String getValue() {
					return value;
				}

				public void setValue(String value) {
					this.value = value;
				}

				public String getLabel() {
					return label;
				}

				public void setLabel(String label) {
					this.label = label;
				}

				@Override
				public String toString() {
					return this.getClass() + ":Option [value=" + value + ", label=" + label
							+ "]";
				}

			}

			public Type getType() {
				return type;
			}

			public void setType(Type type) {
				this.type = type;
			}

			public List<Option> getOptions() {
				return options;
			}

			public void setOptions(List<Option> options) {
				this.options = options;
			}

			@Override
			public String toString() {
				return "Control [type=" + type + ", options=" + options + "]";
			}

		}

		@Override
		public String toString() {
			return "Property [label=" + label + ", error=" + error
					+ ", constraint=" + constraint + ", control=" + control
					+ ", metaposition=" + metaposition + "]";
		}
		
		
		 /* 1. populates "control" and "position" property of each object in the list
		  * with values from the messages_en.properties file
		  *
		  * 2. Sorts list on meta.position field 
		  */
		  
		  public static final <T> void setAttributesAndSort(List<T> in, String prefix) {
			 List<MetaBase> list=(List<MetaBase>)in;
			 for(MetaBase m:list) {
				 
				String name=m.getK();
				
				String basename=name.substring(name.lastIndexOf(".")+1);
				
				//some old key we dont know about 
				if (getValue(prefix,basename,"metaposition")==null) continue;
				
				MetaProperty p=new MetaProperty();
				
				m.setProperty(p);
				
				int pos=-1;
						    		
				try {	    			
					pos=Integer.parseInt(getValue(prefix,basename,"metaposition"));
				} catch (Throwable e) {}
		 		
				p.setMetaposition(pos);
				
		 		if (getValue(prefix,basename,"control")!=null) {
		 			String controlStr=getValue(prefix,basename,"control");
		 			String typeStr=controlStr.substring(0,controlStr.indexOf(":"));
		 			MetaProperty.Control.Type type=MetaProperty.Control.Type.valueOf(typeStr);
		 			if (type==MetaProperty.Control.Type.select) {
		 				 				
		 				MetaProperty.Control control=new MetaProperty.Control();
		 				control.setType(MetaProperty.Control.Type.select); 				
		 				p.setControl(control);
		 				
		 				if (controlStr.startsWith("select:${")) {
		 					String [] els=StringUtils.tokenizeToStringArray(controlStr,":");
		 					if (els==null || els.length!=4) {
		 						throw new IllegalStateException(controlStr+" must match 'select:${beanName}:itemValue:itemLabel' pattern");
		 					}
		 					
		 					String beanName=els[1].replace("${","").replace("}", "");
		 					control.setItems(beanName);
		 					control.setItemValue(els[2]);
		 					control.setItemLabel(els[3]);
		 					 					
		 				} else {
		 				 
		 					List<MetaProperty.Control.Option> options=new ArrayList<MetaProperty.Control.Option>();
		 					
		 					String[] pairs=StringUtils.tokenizeToStringArray(controlStr.substring(controlStr.indexOf(":")+1),";");
		 				 				
		 					for(String el:pairs) {
		 						String [] pair=StringUtils.split(el,":");
		 						MetaProperty.Control.Option option = new MetaProperty.Control.Option();
		 						option.setValue(pair[0]);
		 						option.setLabel(pair[1]);
		 						options.add(option);
		 					}
		 					control.setOptions(options);	
		 				}
		 				
		 				 
		 				
		 			}	
		 		}
		 		
		 		
		 		p.setLabel(getValue(prefix,basename,"label"));
		 		p.setLabel(getValue(prefix,basename,"constraint"));
		 		p.setLabel(getValue(prefix,basename,"error"));		 		
		 		
			 }
			 
			 Collections.sort( list, META_POSITION_COMPARATOR);
		 }
		  
		 private static String getValue(String prefix, String name, String attribute) {
			 if (BASE_BUNDLE.containsKey(prefix+"."+name+"."+attribute)) { 
					 return BASE_BUNDLE.getString(prefix+"."+name+"."+attribute);
		 	} else {
		 		return null;
		 	}
		 }
		  
		  private final static Comparator<MetaBase> META_POSITION_COMPARATOR =  new Comparator<MetaBase>() {
				public int compare(MetaBase f1, MetaBase f2) {
		   		 
		 			if (f1==null) return -1;
		 			if (f2==null) return 1;
		 			
		 			Integer p1=f1.getPosition();
		 			Integer p2=f2.getPosition();	    		 

		 			if (p1==null) return -1;
		 			if (p2==null) return 1;

		 			
		 			return p1.compareTo(p2);
			 
		 }
		};
		
	  // returns list of unique "k" values for the given "prefix"
	  public static final Set<String> getUniqueKeys(String prefix) {
		     
		     Set<String> set = new HashSet<String>();
		     
		     Enumeration<String> en=BASE_BUNDLE.getKeys();
		     while(en.hasMoreElements()) {
		    	String k = en.nextElement();
		    	
		    	if (!k.endsWith(".metaposition")) continue;
		    	
		    	String[] path=StringUtils.tokenizeToStringArray(k,".");
		    	
		    	String name=path[1];
		    	
		    	set.add(name);

		    }
		     
		     return set;
		  }
	  
	  public static final class Country {
		  private String code;
		  private String name;
		  
		public String getCode() {
			return code;
		}
		public String getName() {
			return name;
		}
		public static List<Country> getList() {
			return list;
		}
		public Country(String code, String name) {
			  this.code=code;
			  this.name=name;
		}
		
		  
		@Override
		public String toString() {
			return "Country [code=" + code + ", name=" + name + "]";
		}

	  public static final List<Country> list = new ArrayList<Country>();
		static {
			list.add(new Country("US", "UNITED STATES"));
			list.add(new Country("AF", "AFGHANISTAN"));
			list.add(new Country("AX", "ALAND ISLANDS"));
			list.add(new Country("AL", "ALBANIA"));
			list.add(new Country("DZ", "ALGERIA"));
			list.add(new Country("AS", "AMERICAN SAMOA"));
			list.add(new Country("AD", "ANDORRA"));
			list.add(new Country("AO", "ANGOLA"));
			list.add(new Country("AI", "ANGUILLA"));
			list.add(new Country("AQ", "ANTARCTICA"));
			list.add(new Country("AG", "ANTIGUA AND BARBUDA"));
			list.add(new Country("AR", "ARGENTINA"));
			list.add(new Country("AM", "ARMENIA"));
			list.add(new Country("AW", "ARUBA"));
			list.add(new Country("AU", "AUSTRALIA"));
			list.add(new Country("AT", "AUSTRIA"));
			list.add(new Country("AZ", "AZERBAIJAN"));
			list.add(new Country("BS", "BAHAMAS"));
			list.add(new Country("BH", "BAHRAIN"));
			list.add(new Country("BD", "BANGLADESH"));
			list.add(new Country("BB", "BARBADOS"));
			list.add(new Country("BY", "BELARUS"));
			list.add(new Country("BE", "BELGIUM"));
			list.add(new Country("BZ", "BELIZE"));
			list.add(new Country("BJ", "BENIN"));
			list.add(new Country("BM", "BERMUDA"));
			list.add(new Country("BT", "BHUTAN"));
			list.add(new Country("BO", "BOLIVIA, PLURINATIONAL STATE OF"));
			list.add(new Country("BQ", "BONAIRE, SINT EUSTATIUS AND SABA"));
			list.add(new Country("BA", "BOSNIA AND HERZEGOVINA"));
			list.add(new Country("BW", "BOTSWANA"));
			list.add(new Country("BV", "BOUVET ISLAND"));
			list.add(new Country("BR", "BRAZIL"));
			list.add(new Country("IO", "BRITISH INDIAN OCEAN TERRITORY"));
			list.add(new Country("BN", "BRUNEI DARUSSALAM"));
			list.add(new Country("BG", "BULGARIA"));
			list.add(new Country("BF", "BURKINA FASO"));
			list.add(new Country("BI", "BURUNDI"));
			list.add(new Country("KH", "CAMBODIA"));
			list.add(new Country("CM", "CAMEROON"));
			list.add(new Country("CA", "CANADA"));
			list.add(new Country("CV", "CAPE VERDE"));
			list.add(new Country("KY", "CAYMAN ISLANDS"));
			list.add(new Country("CF", "CENTRAL AFRICAN REPUBLIC"));
			list.add(new Country("TD", "CHAD"));
			list.add(new Country("CL", "CHILE"));
			list.add(new Country("CN", "CHINA"));
			list.add(new Country("CX", "CHRISTMAS ISLAND"));
			list.add(new Country("CC", "COCOS (KEELING) ISLANDS"));
			list.add(new Country("CO", "COLOMBIA"));
			list.add(new Country("KM", "COMOROS"));
			list.add(new Country("CG", "CONGO"));
			list.add(new Country("CD", "CONGO, THE DEMOCRATIC REPUBLIC OF THE"));
			list.add(new Country("CK", "COOK ISLANDS"));
			list.add(new Country("CR", "COSTA RICA"));
			list.add(new Country("CI", "COTE D'IVOIRE"));
			list.add(new Country("HR", "CROATIA"));
			list.add(new Country("CU", "CUBA"));
			list.add(new Country("CW", "CURACAO"));
			list.add(new Country("CY", "CYPRUS"));
			list.add(new Country("CZ", "CZECH REPUBLIC"));
			list.add(new Country("DK", "DENMARK"));
			list.add(new Country("DJ", "DJIBOUTI"));
			list.add(new Country("DM", "DOMINICA"));
			list.add(new Country("DO", "DOMINICAN REPUBLIC"));
			list.add(new Country("EC", "ECUADOR"));
			list.add(new Country("EG", "EGYPT"));
			list.add(new Country("SV", "EL SALVADOR"));
			list.add(new Country("GQ", "EQUATORIAL GUINEA"));
			list.add(new Country("ER", "ERITREA"));
			list.add(new Country("EE", "ESTONIA"));
			list.add(new Country("ET", "ETHIOPIA"));
			list.add(new Country("FK", "FALKLAND ISLANDS (MALVINAS)"));
			list.add(new Country("FO", "FAROE ISLANDS"));
			list.add(new Country("FJ", "FIJI"));
			list.add(new Country("FI", "FINLAND"));
			list.add(new Country("FR", "FRANCE"));
			list.add(new Country("GF", "FRENCH GUIANA"));
			list.add(new Country("PF", "FRENCH POLYNESIA"));
			list.add(new Country("TF", "FRENCH SOUTHERN TERRITORIES"));
			list.add(new Country("GA", "GABON"));
			list.add(new Country("GM", "GAMBIA"));
			list.add(new Country("GE", "GEORGIA"));
			list.add(new Country("DE", "GERMANY"));
			list.add(new Country("GH", "GHANA"));
			list.add(new Country("GI", "GIBRALTAR"));
			list.add(new Country("GR", "GREECE"));
			list.add(new Country("GL", "GREENLAND"));
			list.add(new Country("GD", "GRENADA"));
			list.add(new Country("GP", "GUADELOUPE"));
			list.add(new Country("GU", "GUAM"));
			list.add(new Country("GT", "GUATEMALA"));
			list.add(new Country("GG", "GUERNSEY"));
			list.add(new Country("GN", "GUINEA"));
			list.add(new Country("GW", "GUINEA-BISSAU"));
			list.add(new Country("GY", "GUYANA"));
			list.add(new Country("HT", "HAITI"));
			list.add(new Country("HM", "HEARD ISLAND AND MCDONALD ISLANDS"));
			list.add(new Country("VA", "VATICAN CITY STATE"));
			list.add(new Country("HN", "HONDURAS"));
			list.add(new Country("HK", "HONG KONG"));
			list.add(new Country("HU", "HUNGARY"));
			list.add(new Country("IS", "ICELAND"));
			list.add(new Country("IN", "INDIA"));
			list.add(new Country("ID", "INDONESIA"));
			list.add(new Country("IR", "IRAN, ISLAMIC REPUBLIC OF"));
			list.add(new Country("IQ", "IRAQ"));
			list.add(new Country("IE", "IRELAND"));
			list.add(new Country("IM", "ISLE OF MAN"));
			list.add(new Country("IL", "ISRAEL"));
			list.add(new Country("IT", "ITALY"));
			list.add(new Country("JM", "JAMAICA"));
			list.add(new Country("JP", "JAPAN"));
			list.add(new Country("JE", "JERSEY"));
			list.add(new Country("JO", "JORDAN"));
			list.add(new Country("KZ", "KAZAKHSTAN"));
			list.add(new Country("KE", "KENYA"));
			list.add(new Country("KI", "KIRIBATI"));
			list.add(new Country("KP", "KOREA, DEMOCRATIC PEOPLE'S REPUBLIC OF"));
			list.add(new Country("KR", "KOREA, REPUBLIC OF"));
			list.add(new Country("KW", "KUWAIT"));
			list.add(new Country("KG", "KYRGYZSTAN"));
			list.add(new Country("LA", "LAO PEOPLE'S DEMOCRATIC REPUBLIC"));
			list.add(new Country("LV", "LATVIA"));
			list.add(new Country("LB", "LEBANON"));
			list.add(new Country("LS", "LESOTHO"));
			list.add(new Country("LR", "LIBERIA"));
			list.add(new Country("LY", "LIBYAN ARAB JAMAHIRIYA"));
			list.add(new Country("LI", "LIECHTENSTEIN"));
			list.add(new Country("LT", "LITHUANIA"));
			list.add(new Country("LU", "LUXEMBOURG"));
			list.add(new Country("MO", "MACAO"));
			list.add(new Country("MK",
					"MACEDONIA, THE FORMER YUGOSLAV REPUBLIC"));
			list.add(new Country("MG", "MADAGASCAR"));
			list.add(new Country("MW", "MALAWI"));
			list.add(new Country("MY", "MALAYSIA"));
			list.add(new Country("MV", "MALDIVES"));
			list.add(new Country("ML", "MALI"));
			list.add(new Country("MT", "MALTA"));
			list.add(new Country("MH", "MARSHALL ISLANDS"));
			list.add(new Country("MQ", "MARTINIQUE"));
			list.add(new Country("MR", "MAURITANIA"));
			list.add(new Country("MU", "MAURITIUS"));
			list.add(new Country("YT", "MAYOTTE"));
			list.add(new Country("MX", "MEXICO"));
			list.add(new Country("FM", "MICRONESIA, FEDERATED STATES OF"));
			list.add(new Country("MD", "MOLDOVA, REPUBLIC OF"));
			list.add(new Country("MC", "MONACO"));
			list.add(new Country("MN", "MONGOLIA"));
			list.add(new Country("ME", "MONTENEGRO"));
			list.add(new Country("MS", "MONTSERRAT"));
			list.add(new Country("MA", "MOROCCO"));
			list.add(new Country("MZ", "MOZAMBIQUE"));
			list.add(new Country("MM", "MYANMAR"));
			list.add(new Country("NA", "NAMIBIA"));
			list.add(new Country("NR", "NAURU"));
			list.add(new Country("NP", "NEPAL"));
			list.add(new Country("NL", "NETHERLANDS"));
			list.add(new Country("NC", "NEW CALEDONIA"));
			list.add(new Country("NZ", "NEW ZEALAND"));
			list.add(new Country("NI", "NICARAGUA"));
			list.add(new Country("NE", "NIGER"));
			list.add(new Country("NG", "NIGERIA"));
			list.add(new Country("NU", "NIUE"));
			list.add(new Country("NF", "NORFOLK ISLAND"));
			list.add(new Country("MP", "NORTHERN MARIANA ISLANDS"));
			list.add(new Country("NO", "NORWAY"));
			list.add(new Country("OM", "OMAN"));
			list.add(new Country("PK", "PAKISTAN"));
			list.add(new Country("PW", "PALAU"));
			list.add(new Country("PS", "PALESTINIAN TERRITORY, OCCUPIED"));
			list.add(new Country("PA", "PANAMA"));
			list.add(new Country("PG", "PAPUA NEW GUINEA"));
			list.add(new Country("PY", "PARAGUAY"));
			list.add(new Country("PE", "PERU"));
			list.add(new Country("PH", "PHILIPPINES"));
			list.add(new Country("PN", "PITCAIRN"));
			list.add(new Country("PL", "POLAND"));
			list.add(new Country("PT", "PORTUGAL"));
			list.add(new Country("PR", "PUERTO RICO"));
			list.add(new Country("QA", "QATAR"));
			list.add(new Country("RE", "REUNION"));
			list.add(new Country("RO", "ROMANIA"));
			list.add(new Country("RU", "RUSSIAN FEDERATION"));
			list.add(new Country("RW", "RWANDA"));
			list.add(new Country("BL", "SAINT BARTHELEMY"));
			list.add(new Country("SH",
					"SAINT HELENA, ASCENSION AND TRISTAN DA CUNHA"));
			list.add(new Country("KN", "SAINT KITTS AND NEVIS"));
			list.add(new Country("LC", "SAINT LUCIA"));
			list.add(new Country("MF", "SAINT MARTIN (FRENCH PART)"));
			list.add(new Country("PM", "SAINT PIERRE AND MIQUELON"));
			list.add(new Country("VC", "SAINT VINCENT AND THE GRENADINES"));
			list.add(new Country("WS", "SAMOA"));
			list.add(new Country("SM", "SAN MARINO"));
			list.add(new Country("ST", "SAO TOME AND PRINCIPE"));
			list.add(new Country("SA", "SAUDI ARABIA"));
			list.add(new Country("SN", "SENEGAL"));
			list.add(new Country("RS", "SERBIA"));
			list.add(new Country("SC", "SEYCHELLES"));
			list.add(new Country("SL", "SIERRA LEONE"));
			list.add(new Country("SG", "SINGAPORE"));
			list.add(new Country("SX", "SINT MAARTEN (DUTCH PART)"));
			list.add(new Country("SK", "SLOVAKIA"));
			list.add(new Country("SI", "SLOVENIA"));
			list.add(new Country("SB", "SOLOMON ISLANDS"));
			list.add(new Country("SO", "SOMALIA"));
			list.add(new Country("ZA", "SOUTH AFRICA"));
			list.add(new Country("GS",
					"SOUTH GEORGIA AND THE SOUTH SANDWICH ISLANDS"));
			list.add(new Country("ES", "SPAIN"));
			list.add(new Country("LK", "SRI LANKA"));
			list.add(new Country("SD", "SUDAN"));
			list.add(new Country("SR", "SURINAME"));
			list.add(new Country("SJ", "SVALBARD AND JAN MAYEN"));
			list.add(new Country("SZ", "SWAZILAND"));
			list.add(new Country("SE", "SWEDEN"));
			list.add(new Country("CH", "SWITZERLAND"));
			list.add(new Country("SY", "SYRIAN ARAB REPUBLIC"));
			list.add(new Country("TW", "TAIWAN, PROVINCE OF CHINA"));
			list.add(new Country("TJ", "TAJIKISTAN"));
			list.add(new Country("TZ", "TANZANIA, UNITED REPUBLIC OF"));
			list.add(new Country("TH", "THAILAND"));
			list.add(new Country("TL", "TIMOR-LESTE"));
			list.add(new Country("TG", "TOGO"));
			list.add(new Country("TK", "TOKELAU"));
			list.add(new Country("TO", "TONGA"));
			list.add(new Country("TT", "TRINIDAD AND TOBAGO"));
			list.add(new Country("TN", "TUNISIA"));
			list.add(new Country("TR", "TURKEY"));
			list.add(new Country("TM", "TURKMENISTAN"));
			list.add(new Country("TC", "TURKS AND CAICOS ISLANDS"));
			list.add(new Country("TV", "TUVALU"));
			list.add(new Country("UG", "UGANDA"));
			list.add(new Country("UA", "UKRAINE"));
			list.add(new Country("AE", "UNITED ARAB EMIRATES"));
			list.add(new Country("GB", "UNITED KINGDOM"));
			list.add(new Country("US", "UNITED STATES"));
			list.add(new Country("UM", "UNITED STATES MINOR OUTLYING ISLANDS"));
			list.add(new Country("UY", "URUGUAY"));
			list.add(new Country("UZ", "UZBEKISTAN"));
			list.add(new Country("VU", "VANUATU"));
			list.add(new Country("VE", "VENEZUELA, BOLIVARIAN REPUBLIC OF"));
			list.add(new Country("VN", "VIET NAM"));
			list.add(new Country("VG", "VIRGIN ISLANDS, BRITISH"));
			list.add(new Country("VI", "VIRGIN ISLANDS, U.S."));
			list.add(new Country("WF", "WALLIS AND FUTUNA"));
			list.add(new Country("EH", "WESTERN SAHARA"));
			list.add(new Country("YE", "YEMEN"));
			list.add(new Country("ZM", "ZAMBIA"));
			list.add(new Country("ZW", "ZIMBABWE"));
		}
	} 
	  
	}