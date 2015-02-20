package net.mydrive.entities;

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
@Table(name = "myfolder")
public class MyFolder {

	private String folder_uuid;
	
	private String folder_url;

	private List<MyFile> listFiles = new ArrayList<MyFile>();
	
	private User myUser;

	@Id
	@Column(name = "folder_uuid")
	public String getFolder_uuid() {
		return folder_uuid;
	}

	public void setFolder_uuid(String folder_uuid) {
		this.folder_uuid = folder_uuid;
	}

	@Column(name = "folder_url")
	public String getFolder_url() {
		return folder_url;
	}

	public void setFolder_url(String folder_url) {
		this.folder_url = folder_url;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "myFolder", cascade = CascadeType.ALL)
	public List<MyFile> getListFiles() {
		return listFiles;
	}

	public void setListFiles(List<MyFile> listFiles) {
		this.listFiles = listFiles;
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
