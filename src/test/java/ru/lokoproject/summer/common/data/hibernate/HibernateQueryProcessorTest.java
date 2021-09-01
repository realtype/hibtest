package ru.lokoproject.summer.common.data.hibernate;


import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.junit.Test;
import ru.lokoproject.summer.common.data.entity.TestEntity;
import ru.lokoproject.summer.common.data.query.GroupQuery;
import ru.lokoproject.summer.common.data.query.Query;
import ru.lokoproject.summer.common.data.query.SpecificQuery;
import ru.lokoproject.summer.common.data.query.jpa.hibernate.HibernateQueryProcessor;

import java.util.Arrays;

public class HibernateQueryProcessorTest {

    HibernateQueryProcessor processor = new HibernateQueryProcessor();

    @Test
    public void simpleCriteriaTest(){
//        SessionFactory sessionFactory = new Configuration()
//                .configure("test-hibernate.cfg.xml").buildSessionFactory();

        Configuration cfg = new Configuration()
//                .addResource("test-mapping.hbm.xml")
                .configure("test-hibernate.cfg.xml");


        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().
                applySettings(cfg.getProperties()).build();

        SessionFactory sessionFactory = cfg
                .addPackage("ru.lokoproject.summer.common.data.entity")
                .addAnnotatedClass(TestEntity.class)
                .buildSessionFactory(serviceRegistry);


        Session session = sessionFactory.openSession();
        TestEntity t1 = new TestEntity();
        t1.setId(1);
        t1.setStrField("ttt");
        TestEntity t2 = new TestEntity();
        t2.setId(2);
        t2.setStrField("mmm");
        TestEntity t3 = new TestEntity();
        t3.setId(3);
        t3.setStrField("ccc");
        session.save(t1);
        session.save(t2);
        session.save(t3);
        session.beginTransaction();
        session.flush();
        TestEntity load = session.load(TestEntity.class, 1);
        System.out.println("end");

        SpecificQuery query = new SpecificQuery();
        query.setPath("strField");
        query.setParams(Arrays.asList("ttt"));
        query.setType("like");
        query.setClassName(TestEntity.class.getCanonicalName());

        SpecificQuery query2 = new SpecificQuery();
        query2.setPath("strField");
        query2.setParams(Arrays.asList("mmm"));
        query2.setType("like");
        query2.setClassName(TestEntity.class.getCanonicalName());

        processor.setSession(session);
        processor.processQuery(query);

        GroupQuery groupQuery = new GroupQuery();
        groupQuery.setType("or");
        groupQuery.setGroup(true);
        groupQuery.setClassName(TestEntity.class.getCanonicalName());
        groupQuery.setChildQueries(Arrays.asList(query, query2));
        processor.processQuery(groupQuery);
    }
}
