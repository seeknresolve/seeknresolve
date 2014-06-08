package pl.edu.pw.ii.pik01.seeknresolve.domain.search;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.List;

@NoRepositoryBean
public interface FullTextSearchRepository<T, ID extends Serializable> extends JpaRepository<T, ID> {
    List<T> queryOnFields(String query, String... fields);
}
