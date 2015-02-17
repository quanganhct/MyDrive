package net.mydrive.entities;

public class MyChunk {
	private String files_url;
	private Integer files_range;
	private Long files_size;
	public String getFiles_url() {
		return files_url;
	}
	public void setFiles_url(String files_url) {
		this.files_url = files_url;
	}
	public Integer getFiles_range() {
		return files_range;
	}
	public void setFiles_range(Integer files_range) {
		this.files_range = files_range;
	}
	public Long getFiles_size() {
		return files_size;
	}
	public void setFiles_size(Long files_size) {
		this.files_size = files_size;
	}
}
