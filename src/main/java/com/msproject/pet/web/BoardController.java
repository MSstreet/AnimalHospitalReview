package com.msproject.pet.web;

import com.msproject.pet.entity.BoardEntity;
import com.msproject.pet.model.Header;
import com.msproject.pet.model.SearchCondition;
import com.msproject.pet.service.BoardService;
import com.msproject.pet.web.dtos.BoardDto;
import com.msproject.pet.web.dtos.BoardListWithReplyCountDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("/api/board")
@RestController
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/list")
    public Header<List<BoardListWithReplyCountDto>> boardList(
            @PageableDefault(sort = {"idx"}) Pageable pageable,
            SearchCondition searchCondition)
    {
        return boardService.getBoardListWithReplyCount(pageable,searchCondition);
    }

    @GetMapping("/{id}")
    public BoardDto getBoard(@PathVariable Long id) {
        return boardService.getBoard(id);
    }

    @PostMapping("")
    public BoardEntity create(@RequestBody BoardDto boardDto) {
        return boardService.create(boardDto);
    }

    @PostMapping("/update")
    public BoardEntity update(@RequestBody BoardDto boardDto) {
        return boardService.update(boardDto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        boardService.delete(id);
    }


}
