/*
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.mydrive.ws;

/**
 *
 * @author guillaumerebmann, nguyenquanganh
 */

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;

import net.mydrive.entities.MyChunk;
import net.mydrive.entities.MyFile;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Path("/command")
public class RestCall {

	@Context
	private HttpServletRequest request;

	@POST
	@Path("/upload/{uuid}")
	public boolean uploadFile(@PathParam("uuid") String uuid) {
		ServletFileUpload uploadHandler = new ServletFileUpload(
				new DiskFileItemFactory());
		JsonArray jsonArray = new JsonArray();
		try {
			List<FileItem> items = uploadHandler.parseRequest(request);
			MyFile m_file = new MyFile();
			m_file.setFile_uuid(uuid);
			
			long range = 0;
			for (FileItem fi : items) {
				if (!fi.isFormField()) {
/*					JsonObject jo = new JsonObject();
					jo.addProperty("files_url", "");
					jo.addProperty("files_range", range);
					jo.addProperty("files_size", fi.getSize());
					jsonArray.add(jo);
*/					
					MyChunk chunk = new MyChunk();
					chunk.setMyFile(m_file);
					range += fi.getSize();
				}
			}
			
			return true;
		} catch (FileUploadException e) {

		}

		return false;
	}
}