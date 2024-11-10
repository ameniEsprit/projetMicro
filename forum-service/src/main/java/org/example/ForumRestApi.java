package org.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/forum")
public class ForumRestApi {
    @Autowired ForumService forumService ;
    @PostMapping("/add")
    public ResponseEntity<Forum> addForum(@RequestBody Forum f) {
        Forum addedForum = forumService.addForum(f);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedForum);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Forum> updateForum(@PathVariable Long id, @RequestBody Forum forum) throws Exception {
        forum.setId(id);
        Forum updatedForum = forumService.updateForum(forum);
        return ResponseEntity.ok(updatedForum);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteForum(@PathVariable Long id) throws Exception {
        forumService.removeForum(id);
        return ResponseEntity.ok("Forum deleted successfully");
    }

    @GetMapping("/all")
    public ResponseEntity<List<Forum>> getAllForums() {
        List<Forum> forums = forumService.retrieveForums();
        return ResponseEntity.ok(forums);
    }

}
