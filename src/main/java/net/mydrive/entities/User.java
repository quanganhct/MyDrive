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
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "myuser")
public class User implements Serializable, MyObject {
	private String user_uuid;

	private String username;

	private String pwdEncode;
	
//	private List<MyFolder> listFolders = new ArrayList<MyFolder>();
	
	private MyFolder myFolder;
	
	private List<MyGoogleAccount> listGoogleAccount = new ArrayList<MyGoogleAccount>();
	
	private List<MyFile> listAllFile = new ArrayList<MyFile>();

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
//
//	@OneToMany(fetch = FetchType.LAZY, mappedBy = "myUser", cascade = CascadeType.ALL)
//	public List<MyFolder> getListFolders() {
//		return listFolders;
//	}
//
//	public void setListFolders(List<MyFolder> listFolders) {
//		this.listFolders = listFolders;
//	}

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "myUser")
	public List<MyGoogleAccount> getListGoogleAccount() {
		return listGoogleAccount;
	}

	public void setListGoogleAccount(List<MyGoogleAccount> listGoogleAccount) {
		this.listGoogleAccount = listGoogleAccount;
	}

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "myUser")
	public MyFolder getMyFolder() {
		return myFolder;
	}

	public void setMyFolder(MyFolder myFolder) {
		this.myFolder = myFolder;
	}

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "myUser")
	public List<MyFile> getListAllFile() {
		return listAllFile;
	}

	public void setListAllFile(List<MyFile> listAllFile) {
		this.listAllFile = listAllFile;
	}

	@Column(name = "pwdEncode")
	public String getPwdEncode() {
		return pwdEncode;
	}

	public void setPwdEncode(String pwdEncode) {
		this.pwdEncode = pwdEncode;
	}
}
