package com.team.prj.admin.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.team.prj.admin.service.ProfitVO;
import com.team.prj.admin.service.adminVO;

public interface adminMapper {
	List<adminVO> adminSelectList(); // 관리자 목록?
	adminVO adminSelect(adminVO vo); // 관리자 단건조회
	int adminUpdate(adminVO vo); // 관리자 수정 ?
}
