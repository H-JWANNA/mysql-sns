package com.example.fastcampusmysql.application.controller;

import java.util.List;

import javax.validation.constraints.Positive;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.fastcampusmysql.domain.member.dto.MemberDto;
import com.example.fastcampusmysql.domain.member.dto.MemberNicknameHistoryDto;
import com.example.fastcampusmysql.domain.member.dto.RegisterMemberCommand;
import com.example.fastcampusmysql.domain.member.service.MemberReadService;
import com.example.fastcampusmysql.domain.member.service.MemberWriteService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/members")
public class MemberController {
	private final MemberWriteService memberWriteService;
	private final MemberReadService memberReadService;

	@PostMapping
	public MemberDto register(@RequestBody RegisterMemberCommand command) {
		return memberWriteService.register(command);
	}

	@GetMapping("/{id}")
	public MemberDto getMember(@PathVariable Long id) {
		return memberReadService.getMember(id);
	}

	@PostMapping("/{id}/name")
	public MemberDto changeNickname(@PathVariable @Positive Long id, @RequestBody String nickname) {
		memberWriteService.changeNickname(id, nickname);

		return memberReadService.getMember(id);
	}

	@GetMapping("/{memberId}/nickname-histories")
	public List<MemberNicknameHistoryDto> getNicknameHistories(@PathVariable @Positive Long memberId) {
		return memberReadService.getNicknameHistories(memberId);
	}
}
