package net.mydrive.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 
 * @author nguyenquanganh
 *
 */

@Entity
@Table(name = "mygoogle")
public class MyGoogleAccount implements Serializable, MyObject{
	
	private String account_name;
	
	private String refresh_token;
	
	private User myUser;

	@Id
	@Column(name = "account_name")
	public String getAccount_name() {
		return account_name;
	}

	public void setAccount_name(String account_name) {
		this.account_name = account_name;
	}

	@Column(name = "refresh_token")
	public String getRefresh_token() {
		return refresh_token;
	}

	public void setRefresh_token(String refresh_token) {
		this.refresh_token = refresh_token;
	}

	@ManyToOne
	@JoinColumn(name = "user_uuid")
	public User getMyUser() {
		return myUser;
	}

	public void setMyUser(User myUser) {
		this.myUser = myUser;
	}
	
}
