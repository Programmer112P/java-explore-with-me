package ru.practicum.mainservice.comments.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.mainservice.comments.dto.CommentDto;
import ru.practicum.mainservice.comments.dto.NewCommentDto;
import ru.practicum.mainservice.comments.model.Comment;
import ru.practicum.mainservice.events.mapper.EventMapper;
import ru.practicum.mainservice.events.model.Event;
import ru.practicum.mainservice.users.mapper.UserMapper;
import ru.practicum.mainservice.users.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CommentMapper {

    private final EventMapper eventMapper;
    private final UserMapper userMapper;

    @Autowired
    public CommentMapper(EventMapper eventMapper, UserMapper userMapper) {
        this.eventMapper = eventMapper;
        this.userMapper = userMapper;
    }

    public Comment getModelFromNewCommentDto(NewCommentDto newCommentDto, User author, Event event) {
        return Comment.builder()
                .message(newCommentDto.getMessage())
                .author(author)
                .event(event)
                .created(LocalDateTime.now())
                .build();
    }

    public CommentDto getDtoFromModel(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .message(comment.getMessage())
                .author(userMapper.getShortDtoFromEntity(comment.getAuthor()))
                .event(eventMapper.getEventShortDtoFromModel(comment.getEvent()))
                .created(comment.getCreated())
                .build();
    }

    public List<CommentDto> getDtoListFromModelList(List<Comment> comments) {
        return comments.stream().map(this::getDtoFromModel).collect(Collectors.toList());
    }
}
