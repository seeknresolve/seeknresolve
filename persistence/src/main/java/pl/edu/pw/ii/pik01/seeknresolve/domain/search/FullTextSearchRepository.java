package pl.edu.pw.ii.pik01.seeknresolve.domain.search;

import java.util.List;

public interface FullTextSearchRepository<DOMAIN_CLASS> {
    List<DOMAIN_CLASS> queryOnFields(String query, String... fields);
}
