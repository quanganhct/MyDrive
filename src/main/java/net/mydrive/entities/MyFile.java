package net.mydrive.entities;

/**
 * 
 * @author nguyenquanganh
 * 
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.google.gson.JsonObject;

@Entity
@Table(name="MyFile")
public class MyFile implements Serializable{
	
	private	String file_uuid;
		
	private List<MyChunk> list_chunk = new ArrayList<MyChunk>();

	private long file_size;
	
	private String file_name;
	
	private Date modified;
		
	@Id
	@Column(name="file_uuid")
	public String getFile_uuid() {
		return file_uuid;
	}


	public void setFile_uuid(String file_uuid) {
		this.file_uuid = file_uuid;
	}

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "myFile")
	public List<MyChunk> getList_chunk() {
		return list_chunk;
	}


	public void setList_chunk(List<MyChunk> list_chunk) {
		this.list_chunk = list_chunk;
	}


	public long getFile_size() {
		return file_size;
	}


	public void setFile_size(long file_size) {
		this.file_size = file_size;
	}


	public String getFile_name() {
		return file_name;
	}


	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}


	public Date getModified() {
		return modified;
	}


	public void setModified(Date modified) {
		this.modified = modified;
	}
	
	public JsonObject toJsonObject() {
		JsonObject jo = new JsonObject();
		jo.addProperty("name", this.file_name);
		
		return jo;
	}
}
