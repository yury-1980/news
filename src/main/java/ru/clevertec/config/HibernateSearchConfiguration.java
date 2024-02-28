package ru.clevertec.config;


import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import org.hibernate.search.mapper.orm.massindexing.MassIndexer;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

@Configuration
@AllArgsConstructor
public class HibernateSearchConfiguration {

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
}
