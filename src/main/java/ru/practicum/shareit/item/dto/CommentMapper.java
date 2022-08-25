package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.model.Comment;

public class CommentMapper {

    public static CommentDtoForDb mapCommentToDtoForDb(Comment comment) {
        CommentDtoForDb commentDtoForDb = new CommentDtoForDb();
        commentDtoForDb.setText(comment.getText());
        return commentDtoForDb;
    }

    public static Comment mapDtoDbToComment(CommentDtoForDb commentDtoForDb) {
        Comment comment = new Comment();
        comment.setId(commentDtoForDb.getId());
        comment.setText(commentDtoForDb.getText());
        comment.setCreated(commentDtoForDb.getCreated());
        return comment;
    }
}
