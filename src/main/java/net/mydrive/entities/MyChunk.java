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
@Table(name = "MyChunk")
public class MyChunk implements Serializable{

	private Integer Id;

	private MyFile myFile;

	private Long files_range;

	private Long files_size;

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
		jo.addProperty("files_url", this.myFile.getFile_uuid());
		jo.addProperty("files_range", this.files_range);
		jo.addProperty("files_size", this.files_size);
		return jo;
	}

	@Id
	@GeneratedValue
	public Integer getId() {
		return Id;
	}

	public void setId(Integer id) {
		Id = id;
	}
}
