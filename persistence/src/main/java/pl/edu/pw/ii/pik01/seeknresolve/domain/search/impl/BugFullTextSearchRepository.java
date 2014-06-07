package pl.edu.pw.ii.pik01.seeknresolve.domain.search.impl;

import org.springframework.stereotype.Repository;
import pl.edu.pw.ii.pik01.seeknresolve.domain.entity.Bug;

@Repository
public class BugFullTextSearchRepository extends HibernateFullTextSearchRepository<Bug> {
    @Override
    public Class<? extends Bug> getDomainClass() {
        return Bug.class;
    }
}
