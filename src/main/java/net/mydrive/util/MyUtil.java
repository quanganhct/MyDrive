package net.mydrive.util;

import java.util.List;

import net.mydrive.entities.MyFile;
import net.mydrive.entities.MyGoogleAccount;
import net.mydrive.entities.MyObject;
import net.mydrive.entities.User;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

/**
 * 
 * @author nguyenquanganh
 *
 */
public class MyUtil {

	private static SessionFactory sessionFactory = buildSessionFactory();
	private static ServiceRegistry serviceRegistry;

	private static SessionFactory buildSessionFactory() {
		try {
			Configuration configuration = new Configuration();
			configuration.configure();
			serviceRegistry = new ServiceRegistryBuilder().applySettings(
					configuration.getProperties()).buildServiceRegistry();
			sessionFactory = configuration.buildSessionFactory(serviceRegistry);
			return sessionFactory;
		} catch (Exception e) {
			System.err.println("Initial SessionFactory creation failed." + e);
			throw new ExceptionInInitializerError(e);
		}
	}

	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public static void shutdown() {
		getSessionFactory().close();
	}

	public static boolean saveEntity(MyObject o) {
		try {
			Session s = getSessionFactory().openSession();
			s.beginTransaction();
			s.saveOrUpdate(o);
			s.getTransaction().commit();
			s.close();

			return true;
		} catch (Exception e) {
			System.err.println("Cannot save Object : " + e);
			return false;
		}
	}

	public static void saveListEntity(List<MyObject> l) {
		try {
			Session s = getSessionFactory().openSession();
			s.beginTransaction();
			for (MyObject o : l) {
				s.saveOrUpdate(o);
			}
			s.getTransaction().commit();
			s.close();

		} catch (Exception e) {
			System.err.println("Cannot save Object : " + e);
		}
	}

	public static List<MyGoogleAccount> getListAccountAvailable() {
		Session s = getSessionFactory().openSession();
		Query q = s.createQuery("from MyGoogleAccount where myUser IS NULL");
		List<MyGoogleAccount> result = q.list();
		s.close();

		return result;
	}

	public static User getUserFromUserId(String id) {
		Session s = getSessionFactory().openSession();
		Query q = s.createQuery("from User where user_uuid = :id");
		q.setParameter("id", id);
		List result = q.list();
		if (result.size() == 0)
			return null;
		User u = (User) result.get(0);
		u.getListGoogleAccount().size();
		u.getListAllFile().size();
		u.getMyFolder();
		s.close();

		return u;
	}

	public static MyFile getFileFromFileUuid(String uuid) {
		Session s = getSessionFactory().openSession();
		Query q = s.createQuery("from MyFile where file_uuid = :uuid");
		q.setParameter("uuid", uuid);
		List result = q.list();
		if (result.size() == 0)
			return null;
		MyFile f = (MyFile) result.get(0);
		f.getList_chunk().size();
		s.close();

		return f;
	}

	public static boolean deleteEntity(MyObject o) {
		try {
			Session s = getSessionFactory().openSession();
			s.beginTransaction();
			s.delete(o);
			s.getTransaction().commit();
			s.close();
			return true;
		} catch (Exception e) {
			System.err.println("Cannot delete Object " + e);
			return false;
		}
	}

	public static MyGoogleAccount getGoogleAccount(String accountName) {
		Session s = getSessionFactory().openSession();
		Query q = s
				.createQuery("from MyGoogleAccount where account_name = :acc");
		q.setParameter("acc", accountName);
		List result = q.list();
		if (result.size() == 0)
			return null;
		MyGoogleAccount g = (MyGoogleAccount) result.get(0);
		s.close();
		return g;
	}

	public static void resetAll() {
		Session s = getSessionFactory().openSession();
		Query q1 = s.createQuery("DELETE from User");
		Query q2 = s.createQuery("DELETE from MyChunk");
		Query q3 = s.createQuery("DELETE from MyFile");
		Query q4 = s.createQuery("DELETE from MyFolder");
		Query q5 = s.createQuery("DELETE from MyGoogleAccount");
		s.beginTransaction();
		q1.executeUpdate();
		q2.executeUpdate();
		q3.executeUpdate();
		q4.executeUpdate();
		q5.executeUpdate();
		s.getTransaction().commit();
		
		MyGoogleAccount g1 = new MyGoogleAccount();
		g1.setAccount_name("puf.dreamteam1");
		g1.setFree_space(15000000000L);
		g1.setRefresh_token("1/4QDjxnb15FpsC-EJz1w7sPKnc9GslrFI6zhzb6WxpHw");
		
		MyGoogleAccount g2 = new MyGoogleAccount();
		g2.setAccount_name("puf.dreamteam2");
		g2.setFree_space(15000000000L);
		g2.setRefresh_token("1/wCS3tr-7xd0WJMa-veGpL4IO7Lhkt3GOJkElfxEduhk");
		
		saveEntity(g1);
		saveEntity(g2);
	}
}
