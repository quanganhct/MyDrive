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
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * 
 * @author nguyenquanganh
 *
 */

@Entity
@Table(name = "myfolder")
public class MyFolder implements Serializable, MyObject{

	private String folder_uuid;
	
	private String foldersJSON;

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


//	@OneToMany(fetch = FetchType.LAZY, mappedBy = "myFolder", cascade = CascadeType.ALL)
//	public List<MyFile> getListFiles() {
//		return listFiles;
//	}
//
//	public void setListFiles(List<MyFile> listFiles) {
//		this.listFiles = listFiles;
//	}

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_uuid")
	public User getMyUser() {
		return myUser;
	}

	public void setMyUser(User myUser) {
		this.myUser = myUser;
	}

	@Column(name = "foldersJSON", columnDefinition="TEXT")
	public String getFoldersJSON() {
		return foldersJSON;
	}

	public void setFoldersJSON(String foldersJSON) {
		this.foldersJSON = foldersJSON;
	}

}
