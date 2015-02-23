/*
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.mydrive.ws;

/**
 *
 * @author nguyenquanganh
 * 
 */

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;

import net.mydrive.entities.MyChunk;
import net.mydrive.entities.MyFile;
import net.mydrive.entities.MyFolder;
import net.mydrive.entities.User;
import net.mydrive.util.MyUtil;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.hibernate.Query;
import org.hibernate.Session;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.gson.JsonArray;

@Path("/command")
public class RestCall extends MyBaseServlet {

	@Context
	private HttpServletRequest request;

	@Context
	private HttpServletResponse response;
	
	@POST
	@Path("/signup")
	public boolean signUp() throws Exception {
		String username = (String) request.getAttribute("username");
		String pwd = (String) request.getAttribute("password");
		
		User u = MyUtil.getUserFromUsername(username);
		if (u != null){
			throw new Exception("User exist ");
		}
		
		u = new User();
		u.setUsername(username);
		u.setPwdEncode(pwd);
		u.setUser_uuid(UUID.randomUUID().toString());
		MyUtil.saveEntity(u);
		return true;
	}

	@POST
	@Path("/upload/{uuid}")
	public boolean uploadFile(@PathParam("uuid") String uuid) throws Exception {
		User u = getCurrentUser();

		if (ServletFileUpload.isMultipartContent(request)) {

			ServletFileUpload uploadHandler = new ServletFileUpload(
					new DiskFileItemFactory());
			JsonArray jsonArray = new JsonArray();
			try {
				List<FileItem> items = uploadHandler.parseRequest(request);
				MyFile m_file = new MyFile();
				m_file.setFile_uuid(uuid);
				m_file.setModified(new Date());
				m_file.setFile_name(items.get(0).getName());
				m_file.setFile_size(0);
				MyUtil.saveEntity(m_file);

				long range = 0;
				for (FileItem fi : items) {
					if (!fi.isFormField()) {
						MyChunk chunk = new MyChunk();
						chunk.setMyFile(m_file);
						chunk.setFiles_range(range);
						chunk.setFiles_size(fi.getSize());

						File uploadChunk = new File();
						java.io.File file = new java.io.File("./tempt");
						fi.write(file);
						Credential c = getGoogleCredential();
						Drive service = getDriveService(c);
						try {
							File returnInfo = service.files()
									.insert(uploadChunk).execute();
							chunk.setChunkUrl(returnInfo.getDownloadUrl());
						} catch (IOException e) {
							System.out.println("An error occured: " + e);
							return false;
						}

						MyUtil.saveEntity(chunk);

						range += fi.getSize();
					}
				}
				m_file.setFile_size(range);
				MyUtil.saveEntity(m_file);

				return true;
			} catch (FileUploadException e) {
				System.err.println("Error Upload File " + e);
			}
		}

		return false;
	}
	
	@GET
	@Path("/allfiles")
	public JsonArray getAllChunkOfFile() throws Exception{
		String file_uuid = request.getParameter("file_token");
		MyFile f = MyUtil.getFileFromFileUuid(file_uuid);
		if (f == null)
			throw new Exception("No file found ");
		
		JsonArray array = new JsonArray();
		for (MyChunk c : f.getList_chunk()) {
			array.add(c.toJsonObject());
		}
		
		return array;
	}

	@GET
	@Path("/folder/get")
	public String getFolderJSON() {
		User u = MyUtil.getUserFromUsername((String) request.getSession()
				.getAttribute("username"));

		return u.getMyFolder().getFoldersJSON();
	}

	@GET
	@Path("/folder/set")
	public boolean setFolderJSON() {
		try {
			User u = MyUtil.getUserFromUsername((String) request.getSession()
					.getAttribute("username"));
			MyFolder f = u.getMyFolder();
			Session session = MyUtil.getSessionFactory().openSession();
			session.beginTransaction();
			session.saveOrUpdate(f);
			session.getTransaction().commit();
			session.close();
			return true;
		} catch (Exception e) {
			System.err.println("Cannot save folder " + e);
			return false;
		}
	}

	@GET
	@Path("/upload")
	public JsonArray getAllFiles() {
		User u = MyUtil.getUserFromUsername((String) request.getSession()
				.getAttribute("username"));
		
		JsonArray array = new JsonArray();
		for (MyFile f : u.getListAllFile()) {
			array.add(f.toJsonObject());
		}

		return array;
	}

	private Credential getGoogleCredential() throws Exception {
		String gg = getCurrentUser().getListGoogleAccount().get(0)
				.getAccount_name();
		return credentialManager2.getCredentialWithRefreshToken(
				(String) request.getSession().getAttribute("username"), gg);
	}

	public User getCurrentUser() {
		Session session = MyUtil.getSessionFactory().openSession();

		Query q = session.createQuery("from User where username = :username");
		q.setParameter("username", request.getSession()
				.getAttribute("username"));
		List list = q.list();

		User u = null;
		if (list.size() > 0)
			u = (User) list.get(0);
		session.close();

		return u;
	}

	@GET
	@Path("/test")
	public String test() {
		return "it work";
	}
}