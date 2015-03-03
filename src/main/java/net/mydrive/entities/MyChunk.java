package net.mydrive.entities;

/**
 * 
 * @author nguyenquanganh
 * 
 */

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.google.gson.JsonObject;

@Entity
@Table(name = "mychunk")
public class MyChunk implements Serializable, MyObject{

	private String Id;

	private MyFile myFile;

	private Long files_range;

	private Long files_size;
	
	private String chunkUrl;
	
	private MyGoogleAccount myGoogle;

	public MyChunk() {

	}

	@ManyToOne
	@JoinColumn(name = "file_uuid", nullable = false)
	public MyFile getMyFile() {
		return myFile;
	}

	public void setMyFile(MyFile file) {
		this.myFile = file;
	}

	@Column(name = "file_range")
	public Long getFiles_range() {
		return files_range;
	}

	public void setFiles_range(Long files_range) {
		this.files_range = files_range;
	}

	@Column(name = "file_size")
	public Long getFiles_size() {
		return files_size;
	}

	public void setFiles_size(Long files_size) {
		this.files_size = files_size;
	}

	public JsonObject toJsonObject() {
		JsonObject jo = new JsonObject();
		jo.addProperty("files_url", this.getChunkUrl());
		jo.addProperty("files_range", this.files_range);
		jo.addProperty("files_size", this.files_size);
                jo.addProperty("id", this.Id);
                // Don't foget to add this. It's the access_token of the account used for upload
                // jo.addProperty("files_access_token",blabla); 
		return jo;
	}

	@Id
	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	@Column(name = "chunkUrl")
	public String getChunkUrl() {
		return chunkUrl;
	}

	public void setChunkUrl(String chunkUrl) {
		this.chunkUrl = chunkUrl;
	}

	@ManyToOne
	@JoinColumn(name = "account_name")
	public MyGoogleAccount getMyGoogle() {
		return myGoogle;
	}

	public void setMyGoogle(MyGoogleAccount myGoogle) {
		this.myGoogle = myGoogle;
	}
}
