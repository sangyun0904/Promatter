package com.example.Promatter.repository;

import com.example.Promatter.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
} 