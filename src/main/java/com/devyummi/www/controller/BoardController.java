package com.devyummi.www.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.devyummi.www.domain.board.dto.BoardRequestDTO;
import com.devyummi.www.domain.board.service.BoardService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class BoardController {

	private final BoardService boardService;

	// 글 생성 : 페이지 응답
	@GetMapping("/board/create")
	public String createPage() {
		return "createBoard";
	}

	// 글 생성 : 수행
	@PostMapping("/board/create")
	public String createProcess(BoardRequestDTO dto) {
		boardService.createOneBoard(dto);
		return "redirect:/board/read";
	}

	// 글 목록 : 페이지 응답
	@GetMapping("/board/read")
	public String readPage(Model model) {
		model.addAttribute("BOARDLIST", boardService.readAllBoards());
		return "readBoard";
	}

	// 글 읽기 : 페이지 응답
	@GetMapping("/board/read/{id}")
	public String readIdPage(@PathVariable("id") Long id, Model model) {
		model.addAttribute("BOARD", boardService.readOneBoard(id));
		return "readIdBoard";
	}

	// 글 수정 : 페이지 응답
	@GetMapping("/board/update/{id}")
	public String updatePage(@PathVariable("id") Long id, Model model) {
		// 접근 권한 확인
		if (!boardService.isAccess(id)) {
			return "redirect:/board/read";
		}

		model.addAttribute("BOARD", boardService.readOneBoard(id));
		return "updateBoard";
	}

	// 글 수정 : 수행
	@PostMapping("/board/update/{id}")
	public String updateProcess(@PathVariable("id") Long id, BoardRequestDTO dto) {
		// 접근 권한 확인
		if (!boardService.isAccess(id)) {
			return "redirect:/board/read";
		}

		boardService.updateOneBoard(id, dto);
		return "redirect:/board/read/" + id;
	}

	// 글 삭제 : 수행
	@PostMapping("/board/delete/{id}")
	public String deleteProcess(@PathVariable("id") Long id) {
		// 접근 권한 확인
		if (boardService.isAccess(id)) {
			boardService.deleteOneBoard(id);
		}

		return "redirect:/board/read";
	}
}
