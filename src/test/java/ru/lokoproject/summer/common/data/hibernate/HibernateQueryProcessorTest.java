package ru.lokoproject.summer.common.data.hibernate;


import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.junit.Test;
import ru.lokoproject.summer.common.data.entity.TestEntity;

public class HibernateQueryProcessorTest {

    @Test
    public void simpleCriteriaTest(){
//        SessionFactory sessionFactory = new Configuration()
//                .configure("test-hibernate.cfg.xml").buildSessionFactory();

        Configuration cfg = new Configuration()
                .addResource("test-mapping.hbm.xml")
                .configure("test-hibernate.cfg.xml");


        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().
                applySettings(cfg.getProperties()).build();

        SessionFactory sessionFactory = cfg.buildSessionFactory(serviceRegistry);

        Session session = sessionFactory.openSession();
        TestEntity t1 = new TestEntity();
        t1.setId(1);
        t1.setStrField("ttt");
        session.save(t1);
        session.flush();
        TestEntity load = session.load(TestEntity.class, 1);
        System.out.println("end");
    }
}
