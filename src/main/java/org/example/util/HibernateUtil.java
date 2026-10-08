package org.example.util;

import jakarta.persistence.Entity;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.reflections.Reflections;

import java.util.Set;

public class HibernateUtil {
    private static SessionFactory sessionFactory;
    static {
        try {
            StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml").build();
            MetadataSources sources = new MetadataSources(registry);
            Reflections reflections = new Reflections("org.example.model");
            Set<Class<?>> entityClasses = reflections.getTypesAnnotatedWith(Entity.class);

            for (Class<?> entityClass : entityClasses) {
                sources.addAnnotatedClass(entityClass);
                System.out.println("Terbaca otomatis : "+entityClass.getName());
            }
            Metadata metadata = sources.getMetadataBuilder().build();
            sessionFactory = metadata.getSessionFactoryBuilder().build();
        } catch (Exception e){
            e.printStackTrace();
            throw new ExceptionInInitializerError("Gagal membangun Session Factory: "+ e.getMessage());
        }
    }

    public static SessionFactory getSessionFactory(){
        return sessionFactory;
    }

    public static void shutdown(){
        if (sessionFactory != null){
            sessionFactory.close();
        }
    }
}
