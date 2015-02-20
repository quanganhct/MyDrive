package net.mydrive.entities;

/**
 * 
 * @author nguyenquanganh
 * 
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "myuser")
public class User implements Serializable {
	private String user_uuid;

	private String username;

	private List<MyFolder> listFolders = new ArrayList<MyFolder>();

	@Id
	@Column(name = "user_uuid")
	public String getUser_uuid() {
		return user_uuid;
	}

	public void setUser_uuid(String user_uuid) {
		this.user_uuid = user_uuid;
	}

	@Column(name = "username")
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "myUser", cascade = CascadeType.ALL)
	public List<MyFolder> getListFolders() {
		return listFolders;
	}

	public void setListFolders(List<MyFolder> listFolders) {
		this.listFolders = listFolders;
	}
}
