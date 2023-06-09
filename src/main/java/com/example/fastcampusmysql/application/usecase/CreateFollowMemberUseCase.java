package com.example.fastcampusmysql.application.usecase;

import org.springframework.stereotype.Service;

import com.example.fastcampusmysql.domain.follow.service.FollowWriteService;
import com.example.fastcampusmysql.domain.member.service.MemberReadService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CreateFollowMemberUseCase {
	private final MemberReadService memberReadService;
	private final FollowWriteService followWriteService;

	public void execute(Long fromMemberId, Long toMemberId) {
		/*
			1. 입력 받은 memberId로 회원 조회
			2. FollowWriteService.create()
		 */
		var fromMember = memberReadService.getMember(fromMemberId);
		var toMember = memberReadService.getMember(toMemberId);

		followWriteService.create(fromMember, toMember);
	}
}
