package com.springbook.view.member;

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
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springbook.biz.email.Email;
import com.springbook.biz.email.EmailSender;
import com.springbook.biz.member.MemberService;
import com.springbook.biz.member.MemberVO;
import com.springbook.biz.member.PasswordVO;

@Controller

public class MemberController {
	@Autowired
	private MemberService memberService;

	// 1212 ���� �α���
	@RequestMapping(value = "login.do")
	public String login(MemberVO vo, @RequestParam("mId") String mId, @RequestParam("mPassword") String mPassword,
			HttpSession session, HttpServletResponse response) throws IOException {
		System.out.println("==> Mybatis�� login() ��� ó��");

		MemberVO member = memberService.existId(vo.getmId());
		try {

			System.out.println(mId);
			System.out.println(mPassword);

			int status = Integer.parseInt(member.getmAccountStatus());
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
			if (encoder.matches(mPassword, member.getmPassword())) {

				if (status < 3) {
					session.setAttribute("member", member);
					return "index.jsp";
				}
				if (status >= 3 && status < 99) {
					response.setContentType("text/html; charset=UTF-8");

					PrintWriter out = response.getWriter();
					out.println("<script>alert('�̿� ������ ȸ���Դϴ�. �����ڿ��� ���� ��Ź�帳�ϴ�.'); </script>");
					out.flush();
				} else if (status >= 99) {
					response.setContentType("text/html; charset=UTF-8");

					PrintWriter out = response.getWriter();
					out.println("<script>alert('ȸ�� Ż��� ���̵��Դϴ�. �����ڿ��� ���� ��Ź�帳�ϴ�.');</script>");
					out.flush();
				}
			} else {
				response.setContentType("text/html; charset=UTF-8");

				PrintWriter out = response.getWriter();
				out.println("<script>alert('�߸��� ��й�ȣ �Դϴ�. Ȯ�� �� �ٽ� �α��� ���ּ���');</script>");
				out.flush();
			}

		} catch (NullPointerException e) {
			response.setContentType("text/html; charset=UTF-8");

			PrintWriter out = response.getWriter();
			out.println("<script>alert('ȸ������ �ȵ� ȸ���Դϴ� ȸ������ �� �̿� ��Ź�����');</script>");
			out.flush();
		}
		return "login.jsp";
	}

	// �α׾ƿ�
	@RequestMapping(value = "/logout.do")
	// Commedn ��ü��HttepSession�� �����ϰ� �Ǹ� �����������̳ʰ� �������� ���ε� ���� ��ü�� ��Ƽ� �Ѱ���
	public String logout(HttpSession session) {
		System.out.println("�α׾ƿ� ó��");
		session.invalidate();
		return "index.jsp";
	}

	// ȸ�� ����
	// 1206 ���� ����
	@RequestMapping(value = "/insertMember.do")
	public String insertMember(MemberVO vo, HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		System.out.println("=====> insertMember.do ��Ʈ�ѷ� Ž");
		int seq = memberService.getMemberSeq();
		System.out.println("====>> �������� : " + seq);
		// 1212���� �߰�
		System.out.println(vo.getmPassword());
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String securePW = encoder.encode(vo.getmPassword());
		vo.setmPassword(securePW);

		System.out.println(vo.getmPassword());
		// 1212 ���� �߰�

		try {

			System.out.println("======> check_2");

			String root_path = request.getSession().getServletContext().getRealPath("/");
			String attach_path = "/upload/";

			File file = new File(root_path + attach_path);
			// File file = new File(attach_path);
			if (file.exists() == false) {
				file.mkdir();
			}

			MultipartFile uploadFile = vo.getUploadFile();

			if (!uploadFile.isEmpty()) {
				String fileName = uploadFile.getOriginalFilename();
				uploadFile.transferTo(new File(root_path + attach_path + fileName));

				vo.setmImgPath(root_path + attach_path);
				vo.setmImgName(uploadFile.getOriginalFilename());

			}
			memberService.insertMember(vo);
			// ȭ�� �׺���̼�(�Խñ� ��� �Ϸ� �� �Խñ� ������� �̵�)
			System.out.println("=========> check_3");
			return "redirect:index.jsp";

		} catch (DuplicateKeyException e) {
			response.setContentType("text/html; charset=UTF-8");

			PrintWriter out = response.getWriter();
			out.println("<script> alert('�ߺ��� ���̵� �ƴ� �� ���̵�� ȸ������ ��Ź�帳�ϴ�.');</script>");
			out.flush();

		} catch (Exception e) {
			System.out.println("����ó�� ����");
			return "index.jsp";
		}

		return "join.jsp";
	}

	// ���̵� ã�� ������ �̵�
	@RequestMapping(value = "findaccount.do")
	public String findIdView() {
		return "find-account.jsp";
	}

	// ���̵� ã�� ����
	@RequestMapping(value = "/findId.do", method = RequestMethod.POST)
	@ResponseBody
	public String findIdAction(MemberVO vo, Model model) throws JsonProcessingException {
		MemberVO member = memberService.findId(vo);
		ObjectMapper mapper = new ObjectMapper();
		HashMap<String, Object> hashMap = new HashMap<String, Object>();

		if (member == null) {
			hashMap.put("check", "0");
		} else {
			hashMap.put("check", "1");
			hashMap.put("id", member.getmId());
		}

		String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(hashMap);

		return json;
	}

	// ȸ������ ��������
	@RequestMapping(value = "/getMember.do")
	public String getMember(Model model, HttpSession session) throws IOException {

		System.out.println("======> getmember ��Ʈ�ѷ� Ž");
		try {
			MemberVO vo = (MemberVO) session.getAttribute("member");
			System.out.println("======> ���ǰ����Ծ�");
			System.out.println(memberService.getMember(vo).toString());
			model.addAttribute("memberInfo", memberService.getMember(vo));

			return "changeinfo.jsp";
		} catch (Exception e) {

		}
		return null;

	}

	// ȸ������ ����
	// ���� 1206����
	@RequestMapping(value = "/updateMember.do")
	public String updateBoard(MemberVO vo, HttpServletRequest request) throws IOException {
		System.out.println("ȸ�� ���� ���� ó��");
		System.out.println("�г��� : " + vo.getmNickname());
		System.out.println("�̸��� : " + vo.getmEmail());
		System.out.println("��й�ȣ : " + vo.getmPassword());
		System.out.println("��ȭ��ȣ : " + vo.getmTell());
		System.out.println("�ڱ�Ұ� : " + vo.getmIntroduce());
		System.out.println("�������� : " + vo.getmLicense());
		System.out.println("�̹����н� : " + vo.getmImgPath());
		System.out.println("�̹����̸� : " + vo.getmImgName());
		System.out.println(vo.getUploadFile());

		String root_path = request.getSession().getServletContext().getRealPath("/");
		String attach_path = "/upload/";

		File file = new File(root_path + attach_path);
		// File file = new File(attach_path);
		if (file.exists() == false) {
			file.mkdir();
		}

		MultipartFile uploadFile = vo.getUploadFile();

		if (uploadFile!=null) {
			String fileName = uploadFile.getOriginalFilename();
			uploadFile.transferTo(new File(root_path + attach_path + fileName));
				
			
			//vo.setmImgPath(root_path + attach_path);
			vo.setmImgName(uploadFile.getOriginalFilename());

		}
		memberService.updateMember(vo);
		return "redirect:Mypage.jsp";
	}

	// ��й�ȣ email�� ���� �ޱ�
	@Autowired
	private EmailSender emailSender;
	@Autowired
	private Email email;

	@RequestMapping("/findPWD.do")
	public String sendEmailAction(MemberVO vo, Model model, HttpServletResponse response) throws Exception {

		System.out.println("====> �̸��� ��Ʈ�ѷ� Ž");
		// ��й�ȣã�Ⱑ null�̸� 3 -> �ٽ� find-account��
		if (memberService.findPassword(vo) == null) {

			model.addAttribute("check", 3);

			response.setContentType("text/html; charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.println("<script> alert('�ش� ȸ���� �������� �ʽ��ϴ�.');</script>");
			out.flush();

			return "find-account.jsp";

			// ��й�ȣ ã�Ⱑ null�� �ƴϸ�
		} else {

			MemberVO memberPassword = memberService.findPassword(vo);

			String pw = memberPassword.getmPassword();

			model.addAttribute("check", 4);

			String id = memberPassword.getmId();
			String e_mail = memberPassword.getmEmail();

			model.addAttribute("check", 4);
//				email.setContent("��й�ȣ�� " + pw + " �Դϴ�.");
//				email.setReceiver(e_mail);
//				email.setSubject("��������" + id + "�� ��й�ȣ ã�� �����Դϴ�.");
//				emailSender.SendEmail(email);
			model.addAttribute("findPwd", memberService.findPassword(vo));

			return "find-password.jsp";
		}
	}

	// ���̵� �ߺ� üũ
	@RequestMapping(value = "/id_check.do")
	@ResponseBody
	public String idCheck(@RequestParam("id") String id
	/* HttpServletResponse response */) throws IOException {
		MemberVO vo = memberService.idChk(id);

		// response.setContentType("text/html;charset=UTF-8");
		String msg = "";
		if (vo != null) {
			msg = "fail";
		} else {
			msg = "success";
		}
		return msg;
		// PrintWriter writer = response.getWriter();
		// writer.println(msg);
	}

	// ���������� ȸ�� Ż��
	// ȸ������ ����
	@RequestMapping(value = "/Withdrawal.do")
	public String Withdrawal(MemberVO vo, HttpServletRequest request, Model model, HttpSession session)
			throws IOException {
		System.out.println("ȸ�� Ż�� ó��");
		System.out.println("ī�װ� : " + vo.getmAccountStatus());

		int seq = vo.getmSeq();

		memberService.Withdrawal(vo);
		session.invalidate();
		return "index.jsp";
	}

	// ȸ������ ����
	@RequestMapping(value = "/memberBan.do")
	public String memberBan(MemberVO vo, HttpServletRequest request, Model model) throws IOException {
		System.out.println("MemberBan ��Ʈ�ѷ��� Ž");
		System.out.println("ȸ�� ���� ó��");
		System.out.println("ī�װ� : " + vo.getmAccountStatus());
		System.out.println(vo.getmEmail());
		System.out.println(vo.getmSeq());
		int seq = vo.getmSeq();

		// model.addAttribute("MemberBan", memberService.getMember(vo));

		memberService.memberBan(vo);
		return "getMemberList.do";
	}

	// ȸ������ ����
	@RequestMapping(value = "/memberKeep.do")
	public String memberKeep(MemberVO vo, HttpServletRequest request, Model model) throws IOException {
		System.out.println("MemberKeep ��Ʈ�ѷ��� Ž");
		System.out.println("ȸ�� ���� ó��");
		System.out.println("ī�װ� : " + vo.getmAccountStatus());
		System.out.println(vo.getmEmail());
		System.out.println(vo.getmSeq());
		int seq = vo.getmSeq();

		// model.addAttribute("MemberBan", memberService.getMember(vo));

		memberService.memberKeep(vo);
		return "getMemberList.do";
	}

	// Member ��� �ҷ�����
	@RequestMapping(value = "/getMemberList.do", method = RequestMethod.GET)
	public String getMemberList(MemberVO vo, Model model) {
		System.out.println("�� ��� �˻� ó��");
		String A = vo.getmAccountStatus();
		System.out.println("mAccountStatus =" + A);
		System.out.println(vo.getmGender());
		System.out.println(vo.getmId());
		System.out.println(vo.getmSeq());

		model.addAttribute("MemberList", memberService.getMemberList(vo));
		return "admin-MemberList.jsp";
	}

	// ȸ�� ��й�ȣ �����ϱ�
	@RequestMapping(value = "/updatePassword.do")
	public String updateBoard(MemberVO vo, HttpServletResponse response) throws IOException {
		System.out.println("��� ���� ����");

		MemberVO member = memberService.existId(vo.getmId());

		System.out.println("��й�ȣ : " + vo.getmPassword());
		System.out.println(" ���̵� :" + vo.getmId());
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String securePW = encoder.encode(vo.getmPassword());
		member.setmPassword(securePW);

		System.out.println(securePW);

		memberService.updatePassword(member);
		// �ݵ�� utf-8 ���� ���� �� PrintWriter out ��ü ���� �� ��.
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.println("<script> alert('��й�ȣ�� ����Ǿ����ϴ�.');</script>");
		out.flush();

		return "index.jsp";
	}

	// ȸ������ ��������
	@RequestMapping(value = "/getMembPw.do")
	public String getMembPw(Model model, HttpSession session) throws IOException {

		System.out.println("======> getMembPw.do ��Ʈ�ѷ�");
		try {
			MemberVO vo = (MemberVO) session.getAttribute("member");
			model.addAttribute("memberInfo", memberService.getMember(vo));

			return "changepassword.jsp";
		} catch (Exception e) {

		}
		return null;

	}

	//��й�ȣ �����ϱ⿡�� �����ϱ�
	@RequestMapping(value = "/updatePW.do")
	public String updateMemPw(PasswordVO vo, HttpServletResponse response) throws IOException {
		System.out.println("======> updatePW.do ��Ʈ�ѷ�");
		System.out.println(vo.getmPassword());
		System.out.println(vo.getNewmPassword());
		System.out.println(vo.getNewmPasswordConfirm());
		System.out.println(vo.getmId());
		
		MemberVO member = memberService.existId(vo.getmId());
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		
		if (encoder.matches(vo.getmPassword(), member.getmPassword())){
			if(vo.getNewmPassword().equals(vo.getNewmPasswordConfirm())) {
				String securePW = encoder.encode(vo.getNewmPassword());
				member.setmPassword(securePW);
				memberService.updatePassword(member);
			}else {
				response.setContentType("text/html; charset=UTF-8");
				PrintWriter out = response.getWriter();
				out.println("<script> alert('���ο� ��й�ȣ Ȯ���� ��ġ���� �ʽ��ϴ�. �ٽ�Ȯ�����ּ���');</script>");
				out.flush();
				return "getMembPw.do";
			}
		}else {
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.println("<script> alert('���� ��й�ȣ�� ��ġ���� �ʽ��ϴ�. �ٽ� Ȯ�����ּ���');</script>");
			out.flush();
			return "getMembPw.do";
		}
		
		return "index.jsp";
	}

}
