package net.mydrive.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * 
 * @author nguyenquanganh
 *
 */

@Entity
@Table(name = "mygoogle")
public class MyGoogleAccount implements Serializable, MyObject{
	
	private long free_space;
	
	private String account_name;
	
	private String refresh_token;
	
	private User myUser;

	private List<MyChunk> listChunk = new ArrayList<MyChunk>();
	
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

	@Column(name = "free_space")
	public long getFree_space() {
		return free_space;
	}

	public void setFree_space(long free_space) {
		this.free_space = free_space;
	}

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "myGoogle")
	public List<MyChunk> getListChunk() {
		return listChunk;
	}

	public void setListChunk(List<MyChunk> listChunk) {
		this.listChunk = listChunk;
	}
	
}
