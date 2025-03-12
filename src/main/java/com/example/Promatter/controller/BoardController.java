package com.example.Promatter.controller;

import com.example.Promatter.domain.Board;
import com.example.Promatter.service.BoardService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final String UPLOAD_DIR = "uploads";

    @PostConstruct
    public void init() {
        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
    }

    private String saveImage(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return null;
        }

        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String newFilename = UUID.randomUUID().toString() + extension;
        
        Path uploadPath = Paths.get(UPLOAD_DIR).toAbsolutePath();
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        
        Path filePath = uploadPath.resolve(newFilename);
        Files.copy(file.getInputStream(), filePath);
        
        return "/uploads/" + newFilename;
    }

    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("boards", boardService.findAll());
        return "board/list";
    }

    @GetMapping("/write")
    public String writeForm(Model model) {
        model.addAttribute("board", new Board());
        return "board/write";
    }

    @PostMapping("/write")
    public String write(@ModelAttribute Board board, @RequestParam("image") MultipartFile image) {
        try {
            String imageUrl = saveImage(image);
            board.setImageUrl(imageUrl);
            board.setOriginalImageName(image.getOriginalFilename());
            boardService.save(board);
        } catch (IOException e) {
            e.printStackTrace();
            // 에러 처리
        }
        return "redirect:/board/list";
    }

    @GetMapping("/view/{id}")
    public String view(@PathVariable Long id, Model model) {
        model.addAttribute("board", boardService.findById(id));
        return "board/view";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("board", boardService.findById(id));
        return "board/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable Long id, @ModelAttribute Board board, 
                      @RequestParam(value = "image", required = false) MultipartFile image) {
        try {
            Board existingBoard = boardService.findById(id);
            existingBoard.setTitle(board.getTitle());
            existingBoard.setContent(board.getContent());
            
            if (image != null && !image.isEmpty()) {
                String imageUrl = saveImage(image);
                existingBoard.setImageUrl(imageUrl);
                existingBoard.setOriginalImageName(image.getOriginalFilename());
            }
            
            boardService.save(existingBoard);
        } catch (IOException e) {
            e.printStackTrace();
            // 에러 처리
        }
        return "redirect:/board/view/" + id;
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        Board board = boardService.findById(id);
        if (board.getImageUrl() != null) {
            String filename = board.getImageUrl().substring(board.getImageUrl().lastIndexOf("/") + 1);
            Path imagePath = Paths.get(UPLOAD_DIR, filename);
            try {
                Files.deleteIfExists(imagePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        boardService.delete(id);
        return "redirect:/board/list";
    }
} 