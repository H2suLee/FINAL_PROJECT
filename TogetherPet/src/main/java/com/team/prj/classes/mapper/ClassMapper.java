package com.team.prj.classes.mapper;

import java.util.List;

import com.team.prj.classes.service.ClassOptionVO;
import com.team.prj.classes.service.ClassVO;

public interface ClassMapper {
	List<ClassVO> classSelectList(); //클래스 목록
	ClassVO classSelect(ClassVO vo); //클래스 단건조회
	int classInsert(ClassVO vo); //클래스 입력
	int classUpdate(ClassVO vo); //클래스 수정
	int classDelete(ClassVO vo); //클래스 삭제
	List<ClassVO> classSearch(ClassVO vo); //클래스 검색
	
	
	List<ClassOptionVO> classDateOption(String sdate, int no);
	//클래스 단건조회에서 날짜픽
}
