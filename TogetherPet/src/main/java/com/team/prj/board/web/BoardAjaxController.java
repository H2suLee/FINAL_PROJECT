package com.team.prj.board.web;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;


import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.team.prj.board.service.BoardService;
import com.team.prj.board.service.BoardVO;

import com.team.prj.comment.service.CommentVO;

import com.team.prj.users.service.UsersService;

//@controller + @responseBody 합쳐진것 호출한페이지로 결과를돌려준다.
@RestController
public class BoardAjaxController {

	@Autowired
	private BoardService Service;

	@Autowired
	private UsersService user; // 개인회원
	
	
	@Value("${file.dir}")
	private String fileDir;

	@RequestMapping("/boardajaxSearch")
	@ResponseBody
	public List<BoardVO> ajaxSearch(String key, @RequestParam String val) {
		return Service.boardSearch(key, val);
	}

	// 댓글 등록
	@PostMapping(value = "/commentInsert.do")
	@ResponseBody
	public int commentInsert(CommentVO vo) {

		return Service.commentInsert(vo);
	}

	// 댓글 삭제
	@PostMapping(value = "/commentDelete.do")
	@ResponseBody
	public int commentDelete(CommentVO vo) {
		vo.getCommentNo();
		return Service.commentDelete(vo);
	}
	
	// 댓글 리스트
	@RequestMapping("/commentSelectList.do")
	public List<CommentVO> commentList(CommentVO vo){
		return Service.commentSelectList(vo);
	}

	
	
	
	//이미지리스트 정보를 서버에 담아줌
		@PostMapping(value="/boardPhoto", produces = MediaType.APPLICATION_JSON_VALUE)
		@ResponseBody
		 public ResponseEntity<List<BoardVO>> boardPhoto(MultipartFile[] uploadFile){
			
			///// 이미지 파일 맞는지 체크 /////
			for(MultipartFile multipartFile: uploadFile) {
				File checkfile = new File(multipartFile.getOriginalFilename());
				String type = null;
				try {
					type = Files.probeContentType(checkfile.toPath());
					System.out.println("MIME TYPE : " + type);
				} catch (IOException e) {
					e.printStackTrace();
				}
				if(!type.startsWith("image")) {
					
					List<BoardVO> list = null;
					return new ResponseEntity<>(list, HttpStatus.BAD_REQUEST);
				}
			}///// 이미지 파일 맞는지 체크 end/////
			
			

			//파일 경로를 저장하는 String 타입의 변수를 선언하고 초기화
			String uploadFolder = "C:\\Temp";

			
			//File객체를 사용해 폴더 생성
			File uploadPath = new File(uploadFolder); //물리적 저장할 위치		
			if(uploadPath.exists() == false) {
				uploadPath.mkdirs();
			}
			

			// 이미지 정보 담는 객체
			List<BoardVO> list = new ArrayList();	
			//향상된 for문
			for(MultipartFile multipartFile : uploadFile) {
				//이미지 정보 객체
				BoardVO vo = new BoardVO();		
				//파일 이름
				String uploadFileName = multipartFile.getOriginalFilename();		
				// uuid 적용 파일 이름
				String uuid = UUID.randomUUID().toString();
				uploadFileName = uuid + "_" + uploadFileName;		
				// 파일 위치, 파일 이름을 합친 File 객체
				File saveFile = new File(uploadPath, uploadFileName);			
				//포토 vo에 경로넣기
				//vo.setDir(uploadFolder+ "\\" + uploadFileName);
				vo.setAttechDir(uploadFileName);
				vo.setAttech(uploadFolder);
				// 파일 저장
				try {
					multipartFile.transferTo(saveFile);
				} catch (Exception e) {
					e.printStackTrace();
				}
				//이미지 정보가 저장된 photoVO객체를 List의 요소로 추가
				list.add(vo);

			}//for문

			ResponseEntity<List<BoardVO>> result = new ResponseEntity<List<BoardVO>>(list, HttpStatus.OK);

			return result;
		}
	
			//이미지 화면에 보여줌 
			@RequestMapping("/disPlay.do")
			public ResponseEntity<byte[]> getImage(String fileName){
				File file = new File("C:\\Temp" + fileName);
				
				ResponseEntity<byte[]> result = null;
				
				try {

					MultiValueMap<String,String> header = new LinkedMultiValueMap<String, String>();
			
					header.add("Content-type", Files.probeContentType(file.toPath()));
					
					result = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file), header, HttpStatus.OK);
					
				}catch (IOException e) {
					e.printStackTrace();
				}
				
				return result;
			}
			
		
			

}
