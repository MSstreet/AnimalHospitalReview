package com.msproject.pet.web;

import com.msproject.pet.entity.BoardReply;
import com.msproject.pet.model.Header;
import com.msproject.pet.service.BoardReplyService;

import com.msproject.pet.web.dtos.BoardReplyDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("/api/reply")
@RestController
public class BoardReplyController {

    private final BoardReplyService boardReplyService;

    @PostMapping("/join")
    public BoardReply create (@RequestBody BoardReplyDto boardReplyDto) throws Exception{
        return boardReplyService.BoardReplyCreate(boardReplyDto);
    }

    @PostMapping("/sub/join")
    public BoardReply replyCreate (@RequestBody BoardReplyDto boardReplyDto) throws Exception{
        return boardReplyService.BoardSubReplyCreate(boardReplyDto);
    }

    @GetMapping("/list/{id}")
    public Header<List<BoardReplyDto>> reviewList(@PathVariable Long id,
                                              @PageableDefault(sort = "reply_idx") Pageable pageable)
    {
        return boardReplyService.getReplyList(pageable, id);
    }

    @GetMapping("/list1/{id}")
    public Header<List<BoardReplyDto>> reviewList1(@PathVariable Long id,
                                                  @PageableDefault(sort = "reply_idx") Pageable pageable)
    {
        return boardReplyService.getReplyList1(pageable, id);
    }

    @GetMapping("/{id}")
    public BoardReplyDto getReply(@PathVariable Long id){
        return boardReplyService.getReply(id);
    }

    @PostMapping("/")
    public BoardReply update(@RequestBody BoardReplyDto boardReplyDto) {
        return boardReplyService.update(boardReplyDto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        boardReplyService.delete(id);
    }
}
