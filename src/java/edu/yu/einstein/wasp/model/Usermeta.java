/**
 *
 * Usermeta.java 
 * @author echeng (table2type.pl)
 *  
 * the Usermeta object
 *
 *
 */

package edu.yu.einstein.wasp.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

@Entity
@Audited
@Table(name = "usermeta")
public class Usermeta extends WaspModel {

	public Usermeta() {

	}

	public Usermeta(String k, Integer pos) {
		this.k = k;
		this.position = pos==null?-1:pos;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected int usermetaId;

	public void setUsermetaId(int usermetaId) {
		this.usermetaId = usermetaId;
	}

	public int getUsermetaId() {
		return this.usermetaId;
	}

	@Column(name = "userid")
	protected int UserId;

	public void setUserId(int UserId) {
		this.UserId = UserId;
	}

	public int getUserId() {
		return this.UserId;
	}

	@Column(name = "k")
	protected String k;

	public void setK(String k) {
		this.k = k;
	}

	public String getK() {
		return this.k;
	}

	@Column(name = "v")
	protected String v;

	public void setV(String v) {
		this.v = v;
	}

	public String getV() {
		return this.v;
	}

	@Column(name = "position")
	protected int position;

	public void setPosition(int position) {
		this.position = position;
	}

	public int getPosition() {
		return this.position;
	}

	@Column(name = "lastupdts")
	protected Date lastUpdTs;

	public void setLastUpdTs(Date lastUpdTs) {
		this.lastUpdTs = lastUpdTs;
	}

	public Date getLastUpdTs() {
		return this.lastUpdTs;
	}

	@Column(name = "lastupduser")
	protected int lastUpdUser;

	public void setLastUpdUser(int lastUpdUser) {
		this.lastUpdUser = lastUpdUser;
	}

	public int getLastUpdUser() {
		return this.lastUpdUser;
	}

	@NotAudited
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "userid", insertable = false, updatable = false)
	protected User user;

	public void setUser(User user) {
		this.user = user;
		this.UserId = user.UserId;
	}

	public User getUser() {
		return this.user;
	}

	@Transient
	private Property property;

	public Property getProperty() {
		return property;
	}

	public void setProperty(Property property) {
		this.property = property;
	}

	public static class Property {

		private String label;

		private String error;

		private String constraint;

		private Control control;

		private int metaposition;

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

		public static class Control {
			public enum Type {
				input, select
			}

			private Type type;
			private List<Option> options;

			public static class Option {
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
					return "Option [value=" + value + ", label=" + label + "]";
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

	}

	@Override
	public String toString() {
		return "Usermeta [usermetaId=" + usermetaId + ", UserId=" + UserId
				+ ", k=" + k + ", v=" + v + ", position=" + position
				+ ", lastUpdTs=" + lastUpdTs + ", lastUpdUser=" + lastUpdUser
				+ ", property=" + property + "]";
	}

}
