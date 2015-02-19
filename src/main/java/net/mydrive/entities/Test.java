package net.mydrive.entities;

import java.util.List;

import net.mydrive.util.MyUtil;

import org.hibernate.Query;
import org.hibernate.Session;

public class Test {
	public static void main(String [] args) {
		Session session = MyUtil.getSessionFactory().openSession();
		
		
//		MyFile file = new MyFile();
//		file.setFile_name("aka");
//		file.setFile_size(100);
//		file.setFile_uuid("123300");
//		file.setModified(new Date());
//		
//		session.save(file);
//		session.getTransaction().commit();
//		
//		session.beginTransaction();
//		MyChunk c1 = new MyChunk();
//		c1.setMyFile(file);
//		c1.setFiles_range((long) 100);
//		c1.setFiles_size((long) 100);
//		
//		session.save(c1);
//		
//		MyChunk c2 = new MyChunk();
//		c2.setMyFile(file);
//		c2.setFiles_range((long) 100);
//		c2.setFiles_size((long) 100);
//		
//		session.save(c2);
		
		Query r =  session.createQuery("from MyFile where file_uuid = :uuid");
		r.setParameter("uuid", "123300");
		List list = r.list();
		
		MyFile f = (MyFile)list.get(0);
		System.out.println(f.getList_chunk().size());
		
		//session.getTransaction().commit();
		
		session.close();
		MyUtil.getSessionFactory().close();

	}
}
