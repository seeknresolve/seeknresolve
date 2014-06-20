package org.seeknresolve.domain.repository.search;

import org.springframework.data.history.Revisions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.List;

@NoRepositoryBean
public interface SearchWithAuditRepository<T, ID extends Serializable> extends JpaRepository<T, ID> {
    List<T> queryOnFields(String query, String... fields);
    Revisions<Integer, T> getAllRevisions(ID id);
}
