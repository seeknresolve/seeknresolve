package org.seeknresolve.domain.repository;

import org.seeknresolve.domain.entity.Bug;
import org.seeknresolve.domain.entity.Comment;
import org.seeknresolve.domain.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends CrudRepository<Comment, Long> {
    List<Comment> findByAuthor(User author);
    List<Comment> findByBug(Bug bug);
}
