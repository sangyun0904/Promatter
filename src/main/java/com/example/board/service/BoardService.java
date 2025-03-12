package com.example.board.service;

import com.example.board.domain.Board;
import com.example.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {

    private final BoardRepository boardRepository;

    public List<Board> findAll() {
        return boardRepository.findAll();
    }

    public Board findById(Long id) {
        return boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid board Id:" + id));
    }

    @Transactional
    public Board save(Board board) {
        return boardRepository.save(board);
    }

    @Transactional
    public void delete(Long id) {
        boardRepository.deleteById(id);
    }
} 