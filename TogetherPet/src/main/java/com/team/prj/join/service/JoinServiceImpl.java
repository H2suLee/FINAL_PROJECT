package com.team.prj.join.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.team.prj.join.mapper.JoinMapper;
import com.team.prj.seller.service.SellerVO;
import com.team.prj.tutor.service.TutorVO;
import com.team.prj.users.service.UsersVO;

@Service
public class JoinServiceImpl implements JoinService {
	
	@Autowired
	private JoinMapper jm;


	@Override
	public List<UsersVO> usersList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String stateUpdate(UsersVO vo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TutorVO> tutorList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String stateUpdate(TutorVO vo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SellerVO> sellerList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String stateUpdate(SellerVO vo) {
		// TODO Auto-generated method stub
		return null;
	}

	// id 중복체크
	@Override
	public int idCheck(String id) {
		int cnt = 0;
		try {
			cnt = jm.idCheck(id);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("=====cnt: " + cnt);
		return cnt;
	}

	// 닉네임 중복체크
	@Override
	public int nickCheck(String nickname) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}


	

}