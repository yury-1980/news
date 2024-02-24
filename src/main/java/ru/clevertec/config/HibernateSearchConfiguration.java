package ru.clevertec.config;


import jakarta.persistence.EntityManager;
import org.hibernate.search.mapper.orm.massindexing.MassIndexer;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

@Configuration

public class HibernateSearchConfiguration {

    @Autowired
    private EntityManager entityManager;

    @Bean
    @Transactional
    public SearchSession getSearchSession() throws InterruptedException {
        SearchSession searchSession = org.hibernate.search.mapper.orm.Search.session(entityManager);
        MassIndexer indexer = searchSession.massIndexer()
                .threadsToLoadObjects(7);
        indexer.startAndWait();

        return searchSession;
    }

//    @Bean
//    public FullTextEntityManager fullTextEntityManager() throws InterruptedException {
////        SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
//        return Search.getFullTextEntityManager((javax.persistence.EntityManager) entityManager);
//    }
//
//
//
//
//    @PostConstruct
//    public void initializeHibernateSearch() {
//        try {
//            FullTextEntityManager fullTextEntityManager = fullTextEntityManager();
//            fullTextEntityManager.createIndexer().startAndWait();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }
}
