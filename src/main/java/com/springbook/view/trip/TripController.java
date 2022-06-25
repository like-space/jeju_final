
package com.springbook.view.trip;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springbook.biz.member.MemberService;
import com.springbook.biz.member.MemberVO;
import com.springbook.biz.trip.CommentVO;
import com.springbook.biz.trip.ReviewVO;
import com.springbook.biz.trip.ScheduleVO;
import com.springbook.biz.trip.TripMemberVO;
import com.springbook.biz.trip.TripService;
import com.springbook.biz.trip.TripVO;

@Controller
@SessionAttributes("trip")
public class TripController {
	@Autowired
	private TripService tripService;
	@Autowired
	private MemberService memberService;

	// 1) ���� ����� ������ �̵�
	@RequestMapping(value = "/createTrip.do")
	public String createTripView(Model model, HttpSession session) {

		System.out.println("=====> createTrip ��Ʈ�ѷ� ����");

		MemberVO vo = (MemberVO) session.getAttribute("member");
		System.out.println("=====> �������� �������");

		memberService.getMember(vo);

		System.out.println("=====> host �̸� : " + vo.getmName());

		model.addAttribute("hostInfo", memberService.getMember(vo));

		return "create_trip.jsp";
	}

	// 2) ���� ��� ����
	@RequestMapping(value = "/insertTrip.do")
	public String insertTrip(TripVO vo, TripMemberVO tvo, HttpServletRequest request, Model model, HttpSession session)
			throws IOException {
		System.out.println("======> insertTrip ��Ʈ�ѷ� Ž");

		// 2-1) trip ���̺��� (max+1) �� ��������
		int seq = tripService.getTripSeq();
		System.out.println("====> �űԻ����� trSeq: " + seq);

		// 1207 ���� ����
		vo.setTrSeq(seq);
		model.addAttribute("trip", tripService.getTrip(vo));
		model.addAttribute("tripseq", tripService.getTripSeq());
		System.out.println("seq= " + seq);
		System.out.println("������>>>>>>>>>>>" + String.valueOf(vo.getTrDateSet()));
		System.out.println("������>>>>>>>>>>>" + vo.getTrMode());
		System.out.println("��ġ ���� >>>>>>>>>" + vo.getTrAreaSet());
		System.out.println("trSEQ>>>>>>>>>>" + seq);
		// 1207 ���� ���� ��

		// 2-2) ���� ���ε��� ������ ���� ��� ���
		String root_path = request.getSession().getServletContext().getRealPath("/");
		String attach_path = "/upload/";

		// �� ����� ������ ������ ���� ����
		File file = new File(root_path + attach_path);
		// File file = new File(attach_path);

		// file.exists() ==> ������ �����ϸ� true �� ����
		if (file.exists() == false) {
			// file.mkdir() ==> ���丮 �������ִµ� mkdir�� ������� �ϴ� ���丮�� ���� ���丮�� �������� ���� ��� ���� �Ұ�
			file.mkdir();
		}

		// ���� ���ε� ó��
		MultipartFile uploadFile = vo.getUploadFile();
		if (!uploadFile.isEmpty()) {

			String fileName = uploadFile.getOriginalFilename();

			// uploadFile.transferTo(new File("D:/" + fileName));
			// uploadFile.transferTo(new File(file + fileName));
			uploadFile.transferTo(new File(root_path + attach_path + fileName));
			System.out.println("=========>1: " + fileName);
			System.out.println("===========>2: " + file + fileName);
			System.out.println("===============>3: " + root_path + attach_path + fileName);

			vo.setTrImgName(uploadFile.getOriginalFilename());
			vo.setTrImgPath(root_path + attach_path);

		}

		// 2-3) �ű� ���� ���� �� host ���� ����
		tripService.insertTrip(vo);
		tripService.insertTripMemberH(tvo);

		System.out.println("======> check_2");
		// ȭ�� �׺���̼�(�Խñ� ��� �Ϸ� �� �Խñ� ������� �̵�)
		return ("redirect: entranceRoom.do?trSeq=" + vo.getTrSeq());
	}

	// ���� roomcatagory.jsp
	@RequestMapping(value = "/getTripList.do")

	public String getTripList(TripVO vo, Model model) {
		System.out.println("�� ��� �˻� ó��");

		model.addAttribute("tripList", tripService.getTripList(vo));
		return "RoomCatagory.jsp";
	}

	// 3) ����� �����ϱ�
	@RequestMapping(value = "/entranceRoom.do")

	public String getTrip(TripVO vo, ScheduleVO svo, Model model, HttpSession session) {

		TripVO i = new TripVO();

		i = tripService.getTrip(vo);

		MemberVO mvo = (MemberVO) session.getAttribute("member");
		// System.out.println("=======> �����ϴ� ��� ����: " + mvo.toString());

		TripMemberVO tvo = new TripMemberVO();
		tvo.setTrSeq(i.getTrSeq());
		tvo.setmSeq(mvo.getmSeq());

		//System.out.println("========> trSeq: " + i.getTrSeq() + " trName: " + i.getTrName() + " ���࿡ �������� �ο�:"+ tripService.countMember(tvo));

		model.addAttribute("trip", tripService.getTrip(vo));
		model.addAttribute("members", tripService.countMember(tvo));

		// ���� ���� �ɹ� ������ �ҷ�����
		model.addAttribute("tripMemberList", tripService.getTripMemberList(vo));
		System.out.println("==========>�ɹ� ����Ʈ: " + tripService.getTripMemberList(vo));

		// ���� �濡 ������ ����� tripMemverVO �ҷ�����
		model.addAttribute("tripMember", tripService.getTripMember(tvo));

		// ���ִ� �߰� �ڵ�
		List<CommentVO> commentList = tripService.readComment(vo.getTrSeq());
		model.addAttribute("commentList", commentList);
		//System.out.println("=====> trSeq :" + model.toString());
		// ���ִ� �߰� �ڵ� END

		// ���� Ÿ�Կ� ���� jsp �б�
		if (i.getTrMode().equals("�Ϲݸ��")) {
			System.out.println(i.getTrMode());
			System.out.println("�Ϲݸ��Ž");
			// 1208 �߰�
			model.addAttribute("scheduleList", tripService.getscheduleList(svo));

			return "room-normal.jsp";

		} else if (i.getTrMode().equals("������õ���")) {
			System.out.println(i.getTrMode());
			System.out.println("���߸��Ž");
			// 1208 �߰�
			model.addAttribute("scheduleList", tripService.getscheduleList(svo));

			return "room-random.jsp";

		} else {
			System.out.println("���Ӹ��Ž");
			System.out.println(i.getTrMode());
			// 1208 �߰�
			// ���Ӹ���϶��� Ŭ����� �����츸 ���
			model.addAttribute("scheduleList", tripService.getClearedscheduleList(svo));

			List<Map<String, String>> hashMap = tripService.getClearedscheduleList(svo);
			for (int j = 0; j < hashMap.size(); j++) {
				System.out.println(hashMap.get(j).toString());
			}

			return "room-game.jsp";
		}

	}

	// 4) ���࿡ �����ϱ�
	@RequestMapping(value = "/attendTrip.do")
	public String attendTrip(TripMemberVO vo, HttpServletRequest request, Model model, HttpSession session,
			HttpServletResponse response) throws IOException, Exception {

		System.out.println("=====> 1) attendTrip ��Ʈ�ѷ� Ž");

		MemberVO mvo = (MemberVO) session.getAttribute("member");
		System.out.println("=====> 2) ���ǿ��� ������ MemberVO" + mvo.toString());

		TripVO tvo = (TripVO) session.getAttribute("trip");
		System.out.println("=====> 3) ���ǿ��� ������ TripVO" + tvo.toString());

		vo.setTrSeq(tvo.getTrSeq());
		vo.setmSeq(mvo.getmSeq());
		vo.setTmName(mvo.getmName());
		vo.setTmId(mvo.getmId());
		vo.setTmRole("g");

		int i = vo.getmSeq();
		//String message = "";
		
		System.out.println("=====> #1): " + vo.toString());
		System.out.println("=====> #2) ���ǿ��� ������ TripVO: " + i);

		int trSeq = tvo.getTrSeq();
		System.out.println("=====> 4) ���� �Ϸù�ȣ:" + trSeq);

		int tcnt = tvo.getTrPersonnelSet();
		System.out.println("=====> 5) ������ �ο�:" + tcnt);

		int cnt = tripService.countMember(vo);
		System.out.println("=====> 6) �������� �ο�:" + cnt);

		// ���� ���� ���� ���� Ȯ��

		// 1) ���� �ߺ� üũ -> �����ϰ��� �ϴ� �濡 ������ ���� �ִ��� �ߺ� �˻�
		try {
			int check = 0;
			check = tripService.checkContain(vo);
			System.out.println("=====> 7) �ο��ߺ� ����:" + check);

			if (check != 0) {

				System.out.println("========> �̹������ؼ� ��������");
				//message="�̹� ������ �����Դϴ�.";
				model.addAttribute("message", "�̹� ������ �����Դϴ�.");
			}

			// 2) ���� ���� �ο� ��ȸ -> host �� ������ �����ο����� ������ �ο��� ���� ��� ���࿡ ���� ����
		} catch (NullPointerException e) {

			if (tcnt > cnt) {

				System.out.println("=====> ���� ����");
				//message="���࿡ �����Ͽ����ϴ�.";
				model.addAttribute("message", "���࿡ �����Ͽ����ϴ�.");
				tripService.insertTripMembers(vo);
			} else {

				System.out.println("========> �ο��� �������� ��������");
				//message="���� �����ο��� �ʰ� �Ͽ����ϴ�.";
				model.addAttribute("message", "���� �����ο��� �ʰ� �Ͽ����ϴ�.");
			}

		}

		return ("redirect: entranceRoom.do?trSeq=" + trSeq);
	}

	// 5) ���� ���� ��� �ϱ�
	@RequestMapping(value = "/exitTrip.do")
	public String exitTrip(TripMemberVO vo, HttpSession session, HttpServletResponse response)
			throws IOException, Exception {

		System.out.println("=====> exitTrip ��Ʈ�ѷ� ���� ");

		MemberVO mvo = (MemberVO) session.getAttribute("member");
		TripVO tvo = (TripVO) session.getAttribute("trip");

		vo.setTrSeq(tvo.getTrSeq());
		vo.setmSeq(mvo.getmSeq());

		tripService.exitTrip(vo);

		System.out.println("=====> ���� ���� ��� ");

		// �ݵ�� utf-8 ���� ���� �� PrintWriter out ��ü ���� �� ��.
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.println("<script> alert('���� ������ ��� �Ͽ����ϴ�.');</script>");
		out.flush();

		return "getTripList.do";

	}

	// 6) ���� ��Ű��
	@RequestMapping(value = "/exileTrip.do")
	public String exileTrip(TripMemberVO vo, HttpSession session, HttpServletResponse response)
			throws IOException, Exception {

		System.out.println("=====> exileTrip ��Ʈ�ѷ� ���� ");

		TripVO tvo = (TripVO) session.getAttribute("trip");
		System.out.println("=====> 3) ���ǿ��� ������ TripVO" + tvo.toString());

		vo.setTrSeq(tvo.getTrSeq());
		tripService.exitTrip(vo);

		System.out.println("=====> ������ �������� ����");

		// �ݵ�� utf-8 ���� ���� �� PrintWriter out ��ü ���� �� ��.
		/*
		 * response.setContentType("text/html; charset=UTF-8"); PrintWriter out =
		 * response.getWriter();
		 * out.println("<script> alert('�����ڸ� ���� ���� �Ͽ����ϴ�.');</script>"); out.flush();
		 */

		int trSeq = tvo.getTrSeq();
		System.out.println("=====> 4) ���� �Ϸù�ȣ:" + trSeq);

		return ("entranceRoom.do?trSeq=" + trSeq);
	}

	// 7) ���� ���� ��Ű��
	@RequestMapping(value = "/tripStatus.do")
	public String tripStatus(TripVO vo, HttpSession session, HttpServletResponse response)
			throws IOException, Exception {

		System.out.println("=====> tripStatus ��Ʈ�ѷ� ���� ");

		TripVO tvo = (TripVO) session.getAttribute("trip");

		vo.setTrSeq(tvo.getTrSeq());

		tripService.changeTripStatus(vo);

		System.out.println("=====> tripStatus ���� �Ϸ� ");

		int trSeq = tvo.getTrSeq();

		return ("entranceRoom.do?trSeq=" + trSeq);

	}

	// ��� �߰��ϱ�
	@RequestMapping(value = "/insertComment.do")
	@ResponseBody
	public String insertComment(CommentVO vo, HttpServletRequest request, HttpServletResponse response,
			HttpSession session) throws IOException {
		System.out.println("********************************************");
		System.out.println("=====> insertComment.do ��Ʈ�ѷ� Ž");

		MemberVO mvo = (MemberVO) session.getAttribute("member");
		System.out.println("======> ���ǿ��� ������ MemberVO" + vo.toString());

		TripVO tvo = (TripVO) session.getAttribute("trip");
		System.out.println("======> ���ǿ��� ������ TripVO" + tvo.toString());

		vo.setTrSeq(tvo.getTrSeq());
		vo.setmSeq(mvo.getmSeq());
		tripService.insertComment(vo);

		return null;
	}

	// ���� ��õ��� �ٽ� ��õ�ϱ�
	@RequestMapping(value = "/reinsertSchedule.do")
	public String reinsertschedule(ScheduleVO vo, Model model, HttpSession session) {
		System.out.println("=====> reinsertSchedule.do ��Ʈ�ѷ� Ž");

		TripVO tvo = (TripVO) session.getAttribute("trip");
		System.out.println("======> ���ǿ��� ������ TripVO" + tvo.toString());

		vo.setTrSeq(tvo.getTrSeq());
		System.out.println(vo.getTrSeq());

		tripService.reinsertschedule(tvo);

		return ("redirect: entranceRoom.do?trSeq=" + vo.getTrSeq());
	}

	// ���Ӹ�� ������ϱ� Ŭ��
	@RequestMapping(value = "/addGameModeSchedule.do", produces = "application/text; charset=UTF-8")
	@ResponseBody
	public String addGameModeSchedule(ScheduleVO vo) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		HashMap<String, Object> map = new HashMap<String, Object>();

		map.put("addGameModeSchedule", tripService.addGameModeSchedule(vo));
		tripService.updateTrStatus(vo);

		String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(map);
		return json;
	}

	// �������� �ø��� ������
	@RequestMapping(value = "/updateClearTrschedule.do")
	public String updateClearTrschedule(ScheduleVO vo, Model model, HttpServletRequest request) {
		System.out.println("=====> updateClearTrschedule.do ��Ʈ�ѷ� Ž");

		System.out.println(">>>>>>>>>>>trclearfilename" + vo.getTrClearFilename());

		// 2-2) ���� ���ε��� ������ ���� ��� ���
		String root_path = request.getSession().getServletContext().getRealPath("/");
		String attach_path = "/upload/";

		// �� ����� ������ ������ ���� ����
		File file = new File(root_path + attach_path);
		// File file = new File(attach_path);

		// file.exists() ==> ������ �����ϸ� true �� ����
		if (file.exists() == false) {
			// file.mkdir() ==> ���丮 �������ִµ� mkdir�� ������� �ϴ� ���丮�� ���� ���丮�� �������� ���� ��� ���� �Ұ�
			file.mkdir();
		}

		// ���� ���ε� ó��
		MultipartFile uploadFile = vo.getUploadFile();
		if (!uploadFile.isEmpty()) {

			String fileName = uploadFile.getOriginalFilename();

			// uploadFile.transferTo(new File("D:/" + fileName));
			// uploadFile.transferTo(new File(file + fileName));
			try {
				uploadFile.transferTo(new File(root_path + attach_path + fileName));
			} catch (IllegalStateException e) {
				System.out.println("ffff");
				e.printStackTrace();
			} catch (IOException e) {
				System.out.println("ffff");
				e.printStackTrace();
			}
			System.out.println("=========>1: " + fileName);
			System.out.println("===========>2: " + file + fileName);
			System.out.println("===============>3: " + root_path + attach_path + fileName);

			vo.setTrClearFilename(uploadFile.getOriginalFilename());
			// vo.setTrImgPath(root_path + attach_path);

		}

		tripService.updateClearTrschedule(vo);

		return ("redirect: entranceRoom.do?trSeq=" + vo.getTrSeq());
	}

	// �� ���� ����

	@RequestMapping(value = "/getMyTripList.do")

	public String getMyTripList(TripVO vo, TripMemberVO tvo, Model model, HttpServletRequest request) {
		System.out.println("�� ��� �˻� ó��");
		System.out.println("mSeq===========================" + tvo.getmSeq());
		// HttpSession session = request.getSession();
		// MemberVO member = (MemberVO)session.getAttribute("member");
		//
		// member.getmSeq();
		// System.out.println("======>" + member.getmSeq());
		// tvo.setmSeq(member.getmSeq());

		model.addAttribute("tripList", tripService.getMyTripList(tvo.getmSeq()));
		model.addAttribute("tripMemberList", tripService.getMyTripMember(vo));
		// model.addAttribute("tripPlaceList", tripService.getMyTripPlace(vo));
		return "trable.jsp";
	}

	// ���� ���� �ۼ������� ����
	@RequestMapping(value = "/accessReview.do")
	public String accessReview(TripMemberVO vo, HttpServletRequest request, HttpServletResponse response,
			HttpSession session, Model model) throws IOException, Exception {

		System.out.println("=====> accessReview ��Ʈ�ѷ� ���� ");

		MemberVO mvo = (MemberVO) session.getAttribute("member");
		TripVO tvo = (TripVO) session.getAttribute("trip");

		System.out.println("====> ������������ ������ ��� ����: " + mvo.toString());
		System.out.println("====> ������������ ���� ����: " + tvo.toString());
		System.out.println("====> ���� Ÿ�� ���� : " + vo.toString());

		MemberVO mvo2 = new MemberVO();
		mvo2.setmSeq(vo.getmSeq());

		// ���� �ߺ��˻��ϱ�
		ReviewVO rvo = new ReviewVO();
		rvo.setmSeq(vo.getmSeq());
		rvo.setTrSeq(tvo.getTrSeq());
		rvo.setrWriterSeq(mvo.getmSeq());

		TripVO tvo2 = tripService.getTrip(tvo);

		int checkRveiw = 0;
		checkRveiw = tripService.checkReview(rvo);
		System.out.println("======> ���䳲������ �ִ��� Ȯ�� : " + checkRveiw);

		if (checkRveiw == 0 && tvo2.getTrStatus() == 3) {

			System.out.println("========> ���� ���� ����");

			// model.addAttribute("reviewTarget", tripService.getTripMember(vo));
			model.addAttribute("reviewTarget", memberService.getMember(mvo2));
			model.addAttribute("trip", tvo);

			return "personalreview.jsp";

		} else if (checkRveiw != 0) {
			System.out.println("========> ���並 �������� ����");

			// �ݵ�� utf-8 ���� ���� �� PrintWriter out ��ü ���� �� ��.
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.println("<script> alert('�̹� ���並 ����̽��ϴ�.');</script>");
			out.flush();

		} else {

			System.out.println("========> ������ �Ϸ� ���� ����");
			// �ݵ�� utf-8 ���� ���� �� PrintWriter out ��ü ���� �� ��.
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.println("<script> alert('������ �Ϸ�� �� ���並 ����� �� �ֽ��ϴ�.');</script>");
			out.flush();

		}

		return ("entranceRoom.do?trSeq=" + tvo.getTrSeq());

	}

	// ���� ����/�Ű� �μ�Ʈ�ϱ�
	@RequestMapping(value = "/insertReview.do")
	public String insertReview(ReviewVO vo, HttpServletRequest request, HttpServletResponse response,
			HttpSession session) throws IOException, Exception {

		System.out.println("=====> insertReview ��Ʈ�ѷ� ���� ");

		MemberVO mvo = (MemberVO) session.getAttribute("member");
		TripVO tvo = (TripVO) session.getAttribute("trip");

		System.out.println("====> ������������ ������ ��� ����: " + mvo.toString());
		System.out.println("====> ������������ ���� ����: " + tvo.toString());
		System.out.println("====> ���� Ÿ�� ����: " + vo.toString());

		vo.setTrSeq(tvo.getTrSeq());
		vo.setrWriterSeq(mvo.getmSeq());

		if (vo.getrReviewType().equals("complain")) {

			tripService.updateMemberStatus(vo);
			System.out.println("========> �Ű��ϱ� ��� ����");
		}

		tripService.insertReview(vo);
		System.out.println("========> �����ۼ� ��� ����");

		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.println("<script> alert('�����ۼ��� �Ϸ� �Ͽ����ϴ�.');</script>");
		out.flush();

		return ("entranceRoom.do?trSeq=" + tvo.getTrSeq());

	}

	// complain �ۼ������� �̵�
	@RequestMapping(value = "/complain.do")
	public String complain(TripMemberVO vo, HttpServletRequest request, HttpServletResponse response,
			HttpSession session, Model model) throws IOException, Exception {

		System.out.println("=====> complain ��Ʈ�ѷ� ���� ");

		MemberVO mvo = (MemberVO) session.getAttribute("member");
		TripVO tvo = (TripVO) session.getAttribute("trip");

		System.out.println("====> ������������ ������ ��� ����: " + mvo.toString());
		System.out.println("====> ������������ ���� ����: " + tvo.toString());
		System.out.println("====> ���� Ÿ�� ���� : " + vo.toString());

		MemberVO mvo2 = new MemberVO();
		mvo2.setmSeq(vo.getmSeq());

		model.addAttribute("reviewTarget", memberService.getMember(mvo2));
		model.addAttribute("trip", tvo);

		return "complain.jsp";

	}

	// ���� ���� ���� (12.12 ����ȣ �߰� / 12.13 ����ȣ ����)
	@RequestMapping(value = "/myReviews.do")
	public String myReviews(ReviewVO vo, HttpServletRequest request, HttpServletResponse response, HttpSession session,
			Model model) throws IOException, Exception {

		System.out.println("=====> myReviews ��Ʈ�ѷ� ���� ");

		MemberVO mvo = (MemberVO) session.getAttribute("member");

		System.out.println("====> ������������ ������ ��� ����: " + mvo.toString());
		System.out.println("====> ���� Ÿ�� ���� : " + vo.toString());

		// ���� ���� ��ȸ
	      if (vo.getmSeq() == 0) {
	         vo.setmSeq(mvo.getmSeq());
	         MemberVO mvo1 = memberService.getMember(mvo);

	         model.addAttribute("myInfo", mvo1);
	         model.addAttribute("myReviewList", tripService.getmyReviewList(vo));
			// ���� ���� ��ȸ
		} else {
			MemberVO targetmSeq = new MemberVO();
			targetmSeq.setmSeq(vo.getmSeq());

			MemberVO mvo2 = memberService.getMember(targetmSeq);

			model.addAttribute("myInfo", mvo2);
			model.addAttribute("myReviewList", tripService.getmyReviewList(vo));
		}

		return "myReviews.jsp";

	}
}