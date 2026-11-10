package com.frank.apisocialnetwork.controller;

import com.frank.apisocialnetwork.dto.InputCommentDTO;
import com.frank.apisocialnetwork.dto.OutPutCommentDTO;
import com.frank.apisocialnetwork.service.CommentService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
//@CrossOrigin(origins = "https://lfsol.cloud")
@AllArgsConstructor
@RestController
@RequestMapping(path = "comment")
public class CommentController {

    private CommentService commentService;

    @PostMapping(path = "add")
    public ResponseEntity<OutPutCommentDTO> addComment(@RequestBody InputCommentDTO comment) {

        OutPutCommentDTO commentSaved = commentService.addComment(comment);

        return new ResponseEntity<>(commentSaved, HttpStatus.CREATED);
    }


    @GetMapping(path = "publication/{postId}")
    public ResponseEntity<List<OutPutCommentDTO>> getAllCommentByPostId(@PathVariable Integer postId) {

        return commentService.getAllCommentByPostId(postId);
    }


}
