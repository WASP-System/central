package edu.yu.einstein.wasp.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * Holds attributes for a *meta" object
 * @author  Sasha Levchuk
 *
 */
public final class MetaAttribute implements Serializable {

	
		/**
	 * 
	 */
	private static final long serialVersionUID = -5376828098218595820L;
	
		public static enum MetaType {
			INTEGER,
			STRING, // default
			NUMBER // float, double or int
		}
			
		public static enum FormVisibility {
			editable,
			immutable,
			ignore
		}
		
		private String label;

		private String error;

		private String constraint;
		
		private String suffix;
		
		private String range;
		
		private String tooltip;
		
		private String defaultVal;
		
		private String onClick;
		
		private String onChange;
		
		private String onRender;
		
		private MetaType metaType = MetaType.STRING;

		private FormVisibility formVisibility;
		
		private Control control;

		private Integer metaposition;

		public void setFormVisibility(FormVisibility formVisibility){
			this.formVisibility = formVisibility;
		}
		
		public FormVisibility getFormVisibility(){
			return this.formVisibility;
		}
		
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
		
		public void setRange(String range){
			if (range == null){
				this.range = null;
			} else {
				this.range = range;
			}
		}
		
		
		public String getRange(){
			return range;
		}
		
		public String getTooltip() {
			return tooltip;
		}

		public void setTooltip(String tooltip) {
			this.tooltip = tooltip;
		}
		
		public String getDefaultVal() {
			return defaultVal;
		}

		public void setDefaultVal(String defaultVal) {			
			this.defaultVal = defaultVal;
		}
		
	
		public void setMetaType(String metaType){
			try{
				this.metaType = MetaType.valueOf(metaType);
			} catch(IllegalArgumentException e){
				this.metaType = MetaType.STRING;
			} catch(NullPointerException e){
				this.metaType = MetaType.STRING;
			}
		}
		
		public MetaType getMetaType(){
			return metaType;
		}
		
		public void setConstraint(String constraint) {
			this.constraint = constraint;
		}

		public Integer getMetaposition() {
			return metaposition;
		}

		public void setMetaposition(Integer metaposition) {
			this.metaposition = metaposition;
		}

		public Control getControl() {
			return control;
		}

		public void setControl(Control control) {
			this.control = control;
		}
		
		public String getSuffix() {
			return suffix;
		}

		public void setSuffix(String suffix) {
			this.suffix = suffix;
		}
		
		public String getOnClick() {
			return onClick;
		}

		public void setOnClick(String onClick) {
			this.onClick = onClick;
		}

		public String getOnChange() {
			return onChange;
		}

		public void setOnChange(String onChange) {
			this.onChange = onChange;
		}

		public String getOnRender() {
			return onRender;
		}

		public void setOnRender(String onRender) {
			this.onRender = onRender;
		}

		/**
		 * 
		 * Represents a control element such as a select box. An object of this class will typically either use the options attribute OR
		 * the set of items, itemValue and itemLabel attributes depending on whether the items are provided in a bean (former situation)
		 * or as set of semicolon delimited item:value pairs
		 *
		 */
		public static final class Control implements Serializable{
			/**
			 * 
			 */
			private static final long serialVersionUID = 2993903667298872860L;

			public enum Type {
				input, select
			}

			private Type type;
			// options typically derived from parsing a string such as 'select:item1Value:item1Label;item2Value:item2Label'
			private List<Option> options;
			
			// The next 3 attributes are typically set after parsing control string e.g. 'select:${beanName}:itemValue:itemLabel'
			private String items; // beanName
			private String itemValue; // itemValue
			private String itemLabel; // itemLabel
			
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
			
			/**
			 * 
			 * Represents a control element (e.g. select box) option which has a value and a label
			 *
			 */
			public static final class Option implements Serializable {
				/**
				 * 
				 */
				private static final long serialVersionUID = -436008295959041019L;
				private String value;
				private String label;
				
				public Option(){}
				
				public Option(String value, String label){
					setValue(value);
					setLabel(label);
				}


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
				public boolean equals(Object obj){
					if (this == obj)
						return true;
					if (obj == null || this.getClass() != obj.getClass())
						return false;
					Option op = (Option) obj;
					if (op.label.equals(this.label) && op.value.equals(this.value))
						return true;
					return false;
				}
				
				public int hashCode(){
					int hash = 7;
					hash = 31 * hash + (null == label ? 0 : label.hashCode());
					hash = 31 * hash + (null == value ? 0 : value.hashCode());
					return hash;
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
		
	  /**
	   * 
	   * A country representation where each country name is given a two letter code
	   *
	   */
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
			list.add(new Country("AG", "ANTIGUA"));
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
			list.add(new Country("BO", "BOLIVIA"));
			list.add(new Country("BQ", "BONAIRE"));
			list.add(new Country("BA", "BOSNIA"));
			list.add(new Country("BW", "BOTSWANA"));
			list.add(new Country("BV", "BOUVET ISLAND"));
			list.add(new Country("BR", "BRAZIL"));
			list.add(new Country("IO", "BRITISH INDIAN"));
			list.add(new Country("BN", "BRUNEI DARUSSALAM"));
			list.add(new Country("BG", "BULGARIA"));
			list.add(new Country("BF", "BURKINA FASO"));
			list.add(new Country("BI", "BURUNDI"));
			list.add(new Country("KH", "CAMBODIA"));
			list.add(new Country("CM", "CAMEROON"));
			list.add(new Country("CA", "CANADA"));
			list.add(new Country("CV", "CAPE VERDE"));
			list.add(new Country("KY", "CAYMAN ISLANDS"));
			list.add(new Country("CF", "CENTRAL AFRICAN REP."));
			list.add(new Country("TD", "CHAD"));
			list.add(new Country("CL", "CHILE"));
			list.add(new Country("CN", "CHINA"));
			list.add(new Country("CX", "CHRISTMAS ISLAND"));
			list.add(new Country("CC", "COCOS ISLANDS"));
			list.add(new Country("CO", "COLOMBIA"));
			list.add(new Country("KM", "COMOROS"));
			list.add(new Country("CG", "CONGO"));			
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
			list.add(new Country("DO", "DOMINICAN"));
			list.add(new Country("EC", "ECUADOR"));
			list.add(new Country("EG", "EGYPT"));
			list.add(new Country("SV", "EL SALVADOR"));
			list.add(new Country("GQ", "GUINEA"));
			list.add(new Country("ER", "ERITREA"));
			list.add(new Country("EE", "ESTONIA"));
			list.add(new Country("ET", "ETHIOPIA"));
			list.add(new Country("FK", "FALKLAND ISLANDS"));
			list.add(new Country("FO", "FAROE ISLANDS"));
			list.add(new Country("FJ", "FIJI"));
			list.add(new Country("FI", "FINLAND"));
			list.add(new Country("FR", "FRANCE"));
			list.add(new Country("GF", "FRENCH GUIANA"));
			list.add(new Country("PF", "FRENCH POLYNESIA"));			
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
			list.add(new Country("HM", "HEARD ISLAND"));
			list.add(new Country("VA", "VATICAN"));
			list.add(new Country("HN", "HONDURAS"));
			list.add(new Country("HK", "HONG KONG"));
			list.add(new Country("HU", "HUNGARY"));
			list.add(new Country("IS", "ICELAND"));
			list.add(new Country("IN", "INDIA"));
			list.add(new Country("ID", "INDONESIA"));
			list.add(new Country("IR", "IRAN"));
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
			list.add(new Country("KP", "KOREA"));			
			list.add(new Country("KW", "KUWAIT"));
			list.add(new Country("KG", "KYRGYZSTAN"));
			list.add(new Country("LA", "LAO"));
			list.add(new Country("LV", "LATVIA"));
			list.add(new Country("LB", "LEBANON"));
			list.add(new Country("LS", "LESOTHO"));
			list.add(new Country("LR", "LIBERIA"));
			list.add(new Country("LY", "LIBYAN"));
			list.add(new Country("LI", "LIECHTENSTEIN"));
			list.add(new Country("LT", "LITHUANIA"));
			list.add(new Country("LU", "LUXEMBOURG"));
			list.add(new Country("MO", "MACAO"));
			list.add(new Country("MK", "MACEDONIA"));
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
			list.add(new Country("FM", "MICRONESIA"));
			list.add(new Country("MD", "MOLDOVA"));
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
			list.add(new Country("MP", "NORTHERN MARIANA ISL"));
			list.add(new Country("NO", "NORWAY"));
			list.add(new Country("OM", "OMAN"));
			list.add(new Country("PK", "PAKISTAN"));
			list.add(new Country("PW", "PALAU"));			
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
			list.add(new Country("BL", "SAInteger BARTHELEMY"));
			list.add(new Country("SH", "SAInteger HELENA"));
			list.add(new Country("KN", "SAInteger KITTS AND NEVIS"));
			list.add(new Country("LC", "SAInteger LUCIA"));
			list.add(new Country("MF", "SAInteger MARTIN"));
			list.add(new Country("PM", "SAInteger PIERRE"));
			list.add(new Country("VC", "SAInteger VINCENT"));
			list.add(new Country("WS", "SAMOA"));
			list.add(new Country("SM", "SAN MARINO"));
			list.add(new Country("ST", "SAO TOME"));
			list.add(new Country("SA", "SAUDI ARABIA"));
			list.add(new Country("SN", "SENEGAL"));
			list.add(new Country("RS", "SERBIA"));
			list.add(new Country("SC", "SEYCHELLES"));
			list.add(new Country("SL", "SIERRA LEONE"));
			list.add(new Country("SG", "SINGAPORE"));
			list.add(new Country("SX", "SInteger MAARTEN"));
			list.add(new Country("SK", "SLOVAKIA"));
			list.add(new Country("SI", "SLOVENIA"));
			list.add(new Country("SB", "SOLOMON"));
			list.add(new Country("SO", "SOMALIA"));
			list.add(new Country("ZA", "SOUTH AFRICA"));
			list.add(new Country("GS", "SOUTH GEORGIA"));
			list.add(new Country("ES", "SPAIN"));
			list.add(new Country("LK", "SRI LANKA"));
			list.add(new Country("SD", "SUDAN"));
			list.add(new Country("SR", "SURINAME"));
			list.add(new Country("SJ", "SVALBARD"));
			list.add(new Country("SZ", "SWAZILAND"));
			list.add(new Country("SE", "SWEDEN"));
			list.add(new Country("CH", "SWITZERLAND"));
			list.add(new Country("SY", "SYRIA"));
			list.add(new Country("TW", "TAIWAN"));
			list.add(new Country("TJ", "TAJIKISTAN"));
			list.add(new Country("TZ", "TANZANIA"));
			list.add(new Country("TH", "THAILAND"));
			list.add(new Country("TL", "TIMOR-LESTE"));
			list.add(new Country("TG", "TOGO"));
			list.add(new Country("TK", "TOKELAU"));
			list.add(new Country("TO", "TONGA"));
			list.add(new Country("TT", "TRINIDAD"));
			list.add(new Country("TN", "TUNISIA"));
			list.add(new Country("TR", "TURKEY"));
			list.add(new Country("TM", "TURKMENISTAN"));
			list.add(new Country("TC", "TURKS"));
			list.add(new Country("TV", "TUVALU"));
			list.add(new Country("UG", "UGANDA"));
			list.add(new Country("UA", "UKRAINE"));
			list.add(new Country("AE", "UAE"));
			list.add(new Country("GB", "UNITED KINGDOM"));
			list.add(new Country("UY", "URUGUAY"));
			list.add(new Country("UZ", "UZBEKISTAN"));
			list.add(new Country("VU", "VANUATU"));
			list.add(new Country("VE", "VENEZUELA"));
			list.add(new Country("VN", "VIET NAM"));			
			list.add(new Country("VI", "VIRGIN ISLANDS"));
			list.add(new Country("WF", "WALLIS AND FUTUNA"));
			list.add(new Country("EH", "WESTERN SAHARA"));
			list.add(new Country("YE", "YEMEN"));
			list.add(new Country("ZM", "ZAMBIA"));
			list.add(new Country("ZW", "ZIMBABWE"));
		}
	} 
	  /**
	   * 
	   * A state representation where each state name is given a two letter code
	   *
	   */
	  public static final class State {
		  private String code;
		  private String name;
		  
		public String getCode() {
			return code;
		}
		public String getName() {
			return name;
		}
		public static List<State> getList() {
			return list;
		}
		public State(String code, String name) {
			  this.code=code;
			  this.name=name;
		}
		
		  
		@Override
		public String toString() {
			return "State [code=" + code + ", name=" + name + "]";
		}

	    public static final List<State> list = new ArrayList<State>();
		static {
			list.add(new State("AL","Alabama"));
			list.add(new State("AK","Alaska"));
			list.add(new State("AZ","Arizona"));
			list.add(new State("AR","Arkansas"));
			list.add(new State("CA","California"));
			list.add(new State("CO","Colorado"));
			list.add(new State("CT","Connecticut"));
			list.add(new State("DE","Delaware"));
			list.add(new State("DC","District of Columbia"));
			list.add(new State("FL","Florida"));
			list.add(new State("GA","Georgia"));
			list.add(new State("HI","Hawaii"));
			list.add(new State("ID","Idaho"));
			list.add(new State("IL","Illinois"));
			list.add(new State("IN","Indiana"));
			list.add(new State("IA","Iowa"));
			list.add(new State("KS","Kansas"));
			list.add(new State("KY","Kentucky"));
			list.add(new State("LA","Louisiana"));
			list.add(new State("ME","Maine"));
			list.add(new State("MD","Maryland"));
			list.add(new State("MA","Massachusetts"));
			list.add(new State("MI","Michigan"));
			list.add(new State("MN","Minnesota"));
			list.add(new State("MS","Mississippi"));
			list.add(new State("MO","Missouri"));
			list.add(new State("MT","Montana"));
			list.add(new State("NE","Nebraska"));
			list.add(new State("NV","Nevada"));
			list.add(new State("NH","New Hampshire"));
			list.add(new State("NJ","New Jersey"));
			list.add(new State("NM","New Mexico"));
			list.add(new State("NY","New York"));
			list.add(new State("NC","North Carolina"));
			list.add(new State("ND","North Dakota"));
			list.add(new State("OH","Ohio"));
			list.add(new State("OK","Oklahoma"));
			list.add(new State("OR","Oregon"));
			list.add(new State("PA","Pennsylvania"));
			list.add(new State("RI","Rhode Island"));
			list.add(new State("SC","South Carolina"));
			list.add(new State("SD","South Dakota"));
			list.add(new State("TN","Tennessee"));
			list.add(new State("TX","Texas"));
			list.add(new State("UT","Utah"));
			list.add(new State("VT","Vermont"));
			list.add(new State("VA","Virginia"));
			list.add(new State("WA","Washington"));
			list.add(new State("WV","West Virginia"));
			list.add(new State("WI","Wisconsin"));
			list.add(new State("WY","Wyoming"));

		}
	} 
		 
}
