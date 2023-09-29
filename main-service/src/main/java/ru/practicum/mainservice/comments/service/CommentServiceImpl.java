package ru.practicum.mainservice.comments.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.mainservice.comments.dto.CommentDto;
import ru.practicum.mainservice.comments.dto.CommentUpdateDto;
import ru.practicum.mainservice.comments.dto.NewCommentDto;
import ru.practicum.mainservice.comments.mapper.CommentMapper;
import ru.practicum.mainservice.comments.model.Comment;
import ru.practicum.mainservice.comments.repository.CommentRepository;
import ru.practicum.mainservice.events.model.Event;
import ru.practicum.mainservice.events.model.State;
import ru.practicum.mainservice.events.repository.EventRepository;
import ru.practicum.mainservice.exception.ConflictException;
import ru.practicum.mainservice.exception.NotFoundException;
import ru.practicum.mainservice.pagination.OffsetBasedPageRequest;
import ru.practicum.mainservice.users.model.User;
import ru.practicum.mainservice.users.repository.UserRepository;

import java.util.List;
import java.util.Objects;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, CommentMapper commentMapper, EventRepository eventRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.commentMapper = commentMapper;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public CommentDto createComment(NewCommentDto newCommentDto) {
        Event event = eventRepository.findById(newCommentDto.getEventId()).orElseThrow(() -> new NotFoundException("Event not found"));
        validateComment(event, newCommentDto.getAuthorId());
        User author = userRepository.findById(newCommentDto.getAuthorId()).orElseThrow(() -> new NotFoundException("User not found"));
        Comment comment = commentMapper.getModelFromNewCommentDto(newCommentDto, author, event);
        Comment created = commentRepository.save(comment);
        return commentMapper.getDtoFromModel(created);
    }

    @Override
    @Transactional
    public CommentDto updateComment(CommentUpdateDto commentUpdateDto, Long userId) {
        Comment comment = commentRepository.findById(commentUpdateDto.getId()).orElseThrow(() -> new NotFoundException("Comment Not Found"));
        if (!Objects.equals(comment.getAuthor().getId(), userId)) {
            throw new ConflictException("Comment not available to user");
        }
        comment.setMessage(commentUpdateDto.getMessage());
        Comment updated = commentRepository.save(comment);
        return commentMapper.getDtoFromModel(updated);
    }

    @Override
    @Transactional
    public void deleteComment(Long userId, Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NotFoundException("Comment not found"));
        if (!Objects.equals(comment.getAuthor().getId(), userId)) {
            throw new ConflictException("Comment not available for user");
        }
        commentRepository.delete(comment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentDto> getEventComments(Long eventId, Long offset, Integer limit) {
        if (!eventRepository.existsById(eventId)) {
            throw new NotFoundException("Event not found");
        }
        Sort sort = Sort.by(Sort.Direction.DESC, "created");
        OffsetBasedPageRequest offsetBasedPageRequest = new OffsetBasedPageRequest(offset, limit, sort);
        List<Comment> comments = commentRepository.findAll(offsetBasedPageRequest).getContent();
        return commentMapper.getDtoListFromModelList(comments);
    }

    private void validateComment(Event event, Long authorId) {
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new NotFoundException("Event has not been published yet");
        }
        if (Objects.equals(event.getInitiator().getId(), authorId)) {
            throw new ConflictException("User can't comment his own events");
        }
    }
}
