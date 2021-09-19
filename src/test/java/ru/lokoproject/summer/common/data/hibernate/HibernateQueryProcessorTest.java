package ru.lokoproject.summer.common.data.hibernate;


import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.junit.Test;
import ru.lokoproject.summer.common.data.entity.SecondTestEntity;
import ru.lokoproject.summer.common.data.entity.TestEntity;
import ru.lokoproject.summer.common.data.query.GroupQuery;
import ru.lokoproject.summer.common.data.query.Query;
import ru.lokoproject.summer.common.data.query.SpecificQuery;
import ru.lokoproject.summer.common.data.query.jpa.hibernate.HibernateQueryProcessor;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
                .addAnnotatedClass(SecondTestEntity.class)
                .buildSessionFactory(serviceRegistry);


        Session session = sessionFactory.openSession();
        processor.setSession(session);

        initEntities(session);

//        SpecificQuery query = new SpecificQuery();
//        query.setPath("strField");
//        query.setParams(Arrays.asList("ttt"));
//        query.setType("like");
//        query.setClassName(TestEntity.class.getCanonicalName());
//
//        SpecificQuery query2 = new SpecificQuery();
//        query2.setPath("strField");
//        query2.setParams(Arrays.asList("mmm"));
//        query2.setType("like");
//        query2.setClassName(TestEntity.class.getCanonicalName());
//
//
//        processor.processQuery(query);
//
//        GroupQuery groupQuery = new GroupQuery();
//        groupQuery.setType("or");
//        groupQuery.setGroup(true);
//        groupQuery.setClassName(TestEntity.class.getCanonicalName());
//        groupQuery.setChildQueries(Arrays.asList(query, query2));
//        processor.processQuery(groupQuery);

        SpecificQuery query3 = new SpecificQuery();
        query3.setPath("secondTestEntityList.strField");
        query3.setParams(Arrays.asList("second"));
        query3.setType("like");
        query3.setClassName(TestEntity.class.getCanonicalName());
        processor.processQuery(query3);


    }

    private void initEntities(Session session) {
        TestEntity t1 = new TestEntity();
        t1.setId(1);
        t1.setStrField("ttt");
        TestEntity t2 = new TestEntity();
        t2.setId(2);
        t2.setStrField("mmm");
        TestEntity t3 = new TestEntity();
        t3.setId(3);
        t3.setStrField("ccc");

        SecondTestEntity secondTestEntity1 = new SecondTestEntity();
        secondTestEntity1.setId(1);
        secondTestEntity1.setStrField("first");
        secondTestEntity1.setIntField(11);
        SecondTestEntity secondTestEntity2 = new SecondTestEntity();
        secondTestEntity2.setId(2);
        secondTestEntity1.setIntField(22);
        secondTestEntity2.setStrField("second");
        SecondTestEntity secondTestEntity3 = new SecondTestEntity();
        secondTestEntity3.setId(3);
        secondTestEntity3.setStrField("third");
        secondTestEntity1.setIntField(33);

        session.save(secondTestEntity1);
        session.save(secondTestEntity2);
        session.save(secondTestEntity3);

        t1.setSecondTestEntity(secondTestEntity1);
        List<SecondTestEntity> secondTestEntityList = new ArrayList<>();
        secondTestEntityList.add(secondTestEntity1);
        secondTestEntityList.add(secondTestEntity2);
        t1.setSecondTestEntityList(secondTestEntityList);

        session.save(t1);
        session.save(t2);
        session.save(t3);

        session.beginTransaction();
        session.flush();
    }
}
