package org.example;

import org.springframework.stereotype.Service;

import java.util.List;
@Service

public interface ForumService {
    List<Forum> retrieveForums();

    List<Forum> findAllValidForums();

    Forum updateForum(Forum f) throws Exception;

    Forum getForumByid(Long idF) throws Exception;

    Forum addForum(Forum f);

    void removeForum(Long idF) throws Exception;
}

