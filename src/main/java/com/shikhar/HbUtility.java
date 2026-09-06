package com.shikhar;
import java.util.Properties;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.JdbcSettings;
import org.hibernate.cfg.SchemaToolingSettings;

import com.shikhar.entity.Budget;
import com.shikhar.entity.Category;
import com.shikhar.entity.Expense;
import com.shikhar.entity.User;


public class HbUtility {
	public final static SessionFactory sessionFactory;
	static {
		Configuration cfg=new Configuration();

        Properties p=new Properties();
        p.put(JdbcSettings.DRIVER, "com.mysql.cj.jdbc.Driver");
		p.put(JdbcSettings.URL, "jdbc:mysql://localhost:3306/expensedb?createDatabaseIfNotExist=true");
		p.put(JdbcSettings.USER, "Your_Username");
		p.put(JdbcSettings.PASS, "Your_Password");
		p.put(JdbcSettings.DIALECT, "org.hibernate.dialect.MySQL8Dialect");
		p.put(SchemaToolingSettings.HBM2DDL_AUTO, "update");
		p.put(JdbcSettings.SHOW_SQL, "true");
		p.put(JdbcSettings.FORMAT_SQL, "true");

		cfg.setProperties(p);

		cfg.addAnnotatedClass(User.class);
		cfg.addAnnotatedClass(Expense.class);
		cfg.addAnnotatedClass(Category.class);
		cfg.addAnnotatedClass(Budget.class);
        sessionFactory = cfg.buildSessionFactory();
	}
}
