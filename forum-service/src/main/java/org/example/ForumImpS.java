package org.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ForumImpS implements ForumService {
@Autowired
    private ForumRepo forumRepository;

    @Override
    public List<Forum> retrieveForums() {
        return forumRepository.findAll();
    }

    @Override
    public Forum updateForum(Forum forum) throws Exception {
        // Vérifie si le forum existe dans la base de données
        Forum existingForum = forumRepository.findById(forum.getId())
                .orElseThrow(() -> new Exception("Forum not found with id " + forum.getId()));

        // Met à jour les attributs du forum existant
        existingForum.setDate(forum.getDate());
        existingForum.setName(forum.getName());
        existingForum.setLocation(forum.getLocation());
        existingForum.setDescription(forum.getDescription());

        // Enregistre le forum mis à jour dans la base de données
        return forumRepository.save(existingForum);
    }

    @Override
    public Forum getForumByid(Long idF) throws Exception {
        return forumRepository.findById(idF)
                .orElseThrow(() -> new Exception("Forum not found with id: " + idF));
    }

    @Override
    public Forum addForum(Forum f) {
        return forumRepository.save(f);
    }

    @Override
    public void removeForum(Long idF) throws Exception {
        if (idF == null) {
            throw new IllegalArgumentException("ID du forum ne doit pas être nul");
        }
        Forum existingForum = forumRepository.findById(idF)
                .orElseThrow(() -> new Exception("Forum not found with id: " + idF));
        forumRepository.deleteById(idF);
    }

    @Override
    public List<Forum> findAllValidForums() {
        // Implémentation personnalisée si nécessaire
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
