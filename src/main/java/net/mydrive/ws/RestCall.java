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
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;

import net.mydrive.entities.MyChunk;
import net.mydrive.entities.MyFile;
import net.mydrive.entities.MyFolder;
import net.mydrive.entities.MyGoogleAccount;
import net.mydrive.entities.User;
import net.mydrive.util.MyUtil;
import net.mydrive.util.Pair;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.hibernate.Query;
import org.hibernate.Session;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.gson.JsonArray;

@Path("/command")
public class RestCall extends MyBaseServlet {
	private static final String KEY_SESSION_USERID = "user_id";

	@Context
	private HttpServletRequest request;

	@Context
	private HttpServletResponse response;

	@Context
	private ServletContext context;

	@POST
	@Path("/signup")
	public boolean signUp() throws Exception {
		String username = (String) request.getAttribute("username");
		String pwd = (String) request.getAttribute("password");

		User u = MyUtil.getUserFromUsername(username);
		if (u != null) {
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
		// System.out.println("Cred value: ");
		// System.out.println(request.getSession().getAttribute("cred").toString());

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
				int counter = 0;
				for (FileItem fi : items) {
					if (!fi.isFormField()) {
						MyChunk chunk = new MyChunk();
						chunk.setMyFile(m_file);
						chunk.setFiles_range(range);
						chunk.setFiles_size(fi.getSize());

						// get MyGoogleAccount correspondent with the credential
						// used to upload
						Pair<Credential, MyGoogleAccount> p = getGoogleCredentialForUpload(fi
								.getSize());
						Credential c = p.getFirst();
						MyGoogleAccount myGoogle = p.getSecond();

						Drive service = getDriveService(c);
						try {
							// upload the chunk, get the direct download url,
							// and update new free space in MyGoogleAccount
							counter++;
							File uploadChunk = new File();
							uploadChunk.setTitle(uuid + "." + counter);
							java.io.File file = new java.io.File("./tempt");
							fi.write(file);
							FileContent content = new FileContent(null, file);

							File returnInfo = service.files()
									.insert(uploadChunk, content).execute();
							chunk.setChunkUrl(returnInfo.getDownloadUrl());
							chunk.setMyGoogle(myGoogle);
							chunk.setId(returnInfo.getId());
							myGoogle.setFree_space(myGoogle.getFree_space()
									- fi.getSize());
							MyUtil.saveEntity(myGoogle);

							file.delete();
						} catch (IOException e) {
							System.out.println("An error occured: " + e);
							return false;
						}

						MyUtil.saveEntity(chunk);

						range += fi.getSize();
					}
				}
				// update size of file, by the sum of all chunks's sizes
				m_file.setFile_size(range);
				MyUtil.saveEntity(m_file);

				return true;
			} catch (FileUploadException e) {
				System.err.println("Error Upload File " + e);
			}
		} else {

		}

		return false;
	}

	@GET
	@Path("/allfiles")
	public JsonArray getAllChunkOfFile() throws Exception {
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
	public String getFolderJSON() throws Exception {
		// User u = MyUtil.getUserFromUsername((String) request.getSession()
		// .getAttribute("username"));

		User u = MyUtil.getUserFromUsername("root");
		if (u == null)
			throw new Exception("No User");

		return u.getMyFolder().getFoldersJSON();
	}

	@GET
	@Path("/folder/set")
	public boolean setFolderJSON() {
		try {

			User u = MyUtil.getUserFromUsername((String) request.getSession()
					.getAttribute("username"));
			String json = request.getParameter("folderJSON");
			MyFolder f = u.getMyFolder();
			f.setFoldersJSON(json);
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

	// look for the credential correspondent with the drive space thats enough
	// to contain chunk. Return in pair, the credential and MyGoogleAccount
	private Pair<Credential, MyGoogleAccount> getGoogleCredentialForUpload(
			long file_size) throws Exception {
		User u = getCurrentUser();
		List<Credential> list = new ArrayList<Credential>();
		for (int i = 0; i < u.getListGoogleAccount().size(); i++) {
			String gg = getCurrentUser().getListGoogleAccount().get(i)
					.getAccount_name();
			System.out.println("Google Acc : " + gg);

			MyGoogleAccount acc = MyUtil.getGoogleAccount(gg);

			if (acc.getFree_space() >= file_size) {
				// if (credentialManager2 == null)
				// throw new Exception("credential manager null");
				Credential cr = ((CredentialManager2) request.getSession()
						.getAttribute("CredentialManager2"))
						.getCredentialWithRefreshToken(
								request,
								(String) request.getSession().getAttribute(
										KEY_SESSION_USERID), gg);

				return new Pair<Credential, MyGoogleAccount>(cr, acc);
			}
		}

		return null;
	}

	public User getCurrentUser() {
		User u = MyUtil.getUserFromUsername((String) request.getSession()
				.getAttribute("username"));

		return u;
	}

	@GET
	@Path("/freespace")
	public long getFreeSpace() {
		User u = getCurrentUser();
		long result = 0L;
		if (u.getListGoogleAccount().size() == 0) {
			return 0L;
		}
		for (int i = 0; i < u.getListGoogleAccount().size(); i++) {
			result += u.getListGoogleAccount().get(i).getFree_space();
		}
		return result;
	}

	@GET
	@Path("/test")
	public String test() {
		request.getSession();
		User u1 = new User();
		u1.setUsername("root");
		u1.setUser_uuid("123456");
		MyUtil.saveEntity(u1);

		request.getSession().setAttribute("username", "root");
		request.getSession()
				.setAttribute(KEY_SESSION_USERID, u1.getUser_uuid());

		MyFolder f = new MyFolder();
		f.setFolder_uuid("1234");
		f.setFoldersJSON("{ folder: [{ id: 0, name: null, parent: null, files: [] }]}");
		f.setMyUser(u1);
		MyUtil.saveEntity(f);
		manuelInit(new NetHttpTransport());
		u1 = getCurrentUser();
		initializeUserCredentialManager(u1);
		return "it work";
	}

	@DELETE
	@Path("/delete/{file_uuid}")
	public boolean deleteFile(@PathParam("file_uuid") String uuid)
			throws Exception {
		MyFile file = MyUtil.getFileFromFileUuid(uuid);
		if (file.getMyUser().getUser_uuid() != request.getSession()
				.getAttribute(KEY_SESSION_USERID)) {
			return false;
		}

		User u = getCurrentUser();

		List<MyChunk> listChunk = file.getList_chunk();
		for (MyChunk c : listChunk) {
			MyGoogleAccount g = c.getMyGoogle();
			Credential cr = ((CredentialManager2) request.getSession()
					.getAttribute("CredentialManager2"))
					.getCredentialWithRefreshToken(request, u.getUser_uuid(),
							g.getAccount_name());

			Drive service = getDriveService(cr);
			service.files().delete(c.getId()).execute();
			MyUtil.deleteEntity(c);
		}
		MyUtil.deleteEntity(file);

		return false;
	}

	private void initializeUserCredentialManager(User u) {

		for (MyGoogleAccount g : u.getListGoogleAccount()) {
			((CredentialManager2) request.getSession().getAttribute(
					"CredentialManager2")).save(request, u.getUser_uuid(),
					g.getAccount_name(), g.getRefresh_token());
			System.out.println(g.getAccount_name() + " : "
					+ g.getRefresh_token());
		}
	}

	private GoogleClientSecrets getClientSecret() {
		try {
			Reader reader = new InputStreamReader(
					context.getResourceAsStream(CLIENT_SECRETS_FILE_PATH));
			return GoogleClientSecrets.load(JSON_FACTORY, reader);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new RuntimeException("No client secret");
		}
	}

	public void manuelInit(HttpTransport hp) {
		try {
			credentialManager = new CredentialManager(getClientSecret(), hp,
					JSON_FACTORY);

			credentialManager2 = new CredentialManager2(getClientSecret(), hp,
					JSON_FACTORY);
			request.getSession().setAttribute("CredentialManager2",
					credentialManager2);
			System.out.println("cred not null");
			System.out.println(getClientSecret() + " " + hp + " "
					+ JSON_FACTORY);

		} catch (Exception e) {
			System.err.println("cannot initialize cred store " + e);
		}
	}

}