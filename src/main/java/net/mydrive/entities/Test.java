package net.mydrive.entities;

import java.util.Date;

import net.mydrive.util.MyUtil;

import org.hibernate.Session;

public class Test {
	public static void main(String [] args) {
		Session session = MyUtil.getSessionFactory().openSession();
		
		session.beginTransaction();
		
		MyFile file = new MyFile();
		file.setFile_name("aka");
		file.setFile_size(100);
		file.setFile_uuid("123321");
		file.setModified(new Date());
		
		session.save(file);
		session.getTransaction().commit();
	}
}
