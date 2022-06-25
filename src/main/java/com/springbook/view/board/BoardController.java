package com.springbook.view.board;

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
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.springbook.biz.board.BoardFileVO;
import com.springbook.biz.board.BoardListVO;
import com.springbook.biz.board.BoardService;
import com.springbook.biz.board.BoardVO;
import com.springbook.biz.board.FreeCommentVO;
import com.springbook.biz.common.BoardFileUtils;
import com.springbook.biz.common.Criteria;
import com.springbook.biz.common.PageVO;
import com.springbook.biz.member.MemberVO;

@Controller
//board�� model ����� ��ü�� ������ HttpSession ������ �����ҿ��� ������ Ű ��(board)�� ����
@SessionAttributes("board")
public class BoardController {
	@Autowired
	private BoardService boardService;

	@RequestMapping("/dataTransform.do")
	// @ResponseBody : �޼ҵ� ���� ���� ���� �ٵ�� ����
	// �޼ҵ� ��� �� ��ü�� ���� �ٵ� ��
	@ResponseBody
	public BoardListVO dataTransform(BoardVO vo) {
		vo.setSearchCondition("TITLE");
		vo.setSearchKeyword("");
		List<BoardVO> boardList = boardService.getBoardList(vo);
		BoardListVO boardListVO = new BoardListVO();
		boardListVO.setBoardList(boardList);
		return boardListVO;
	}

	// @ModelAttribute : 1. Command ��ü �̸� ����
	// 2. View(JSP)���� ����� ������ ����
	@ModelAttribute("conditionMap")
	public Map<String, String> searchConditionMap() {
		Map<String, String> conditionMap = new HashMap<String, String>();
		conditionMap.put("����", "TITLE");
		conditionMap.put("����", "CONTENT");
		// ���� ���� ReqeustServlet ������ �����ҿ� ����
		// conditionMap�̶�� Ű ������ �����Ͱ� ����
		return conditionMap;
	}

	@RequestMapping(value = "/insertBoard.do")
	// Command ��ü : ����ڰ� ������ �����͸� ������ VO�� �ٷ� ����
	// ����� �Է� ���� �������� �ڵ尡 ������� ������ ����ȭ ����
	// ����� �Է� input�� name �Ӽ��� VO ��������� �̸��� �������ִ� ���� �߿�
	public String insertBoard(BoardVO vo, HttpServletRequest request, MultipartHttpServletRequest mhsr)
			throws IOException {

		int seq = boardService.getBoardSeq();

		BoardFileUtils fileUtils = new BoardFileUtils();
		List<BoardFileVO> fileList = fileUtils.parseFileInfo(seq, request, mhsr);

		if (CollectionUtils.isEmpty(fileList) == false) {
			boardService.insertBoardFileList(fileList);
		}

		HttpSession session = request.getSession();
		MemberVO member = (MemberVO) session.getAttribute("member");

		System.out.println(member.getmNickname());
		vo.setWriter(member.getmNickname());

		vo.setmSeq(member.getmSeq());

		boardService.insertBoard(vo);

		System.out.println("�� ��� ó��");
		System.out.println("�Ϸù�ȣ : " + vo.getSeq());
		System.out.println("���� : " + vo.getTitle());
		System.out.println("�ۼ��� �̸� : " + vo.getWriter());
		System.out.println("���� : " + vo.getContent());
		System.out.println("����� : " + vo.getRegDate());
		System.out.println("��ȸ�� : " + vo.getCnt());

		// ȭ�� �׺���̼�(�Խñ� ��� �Ϸ� �� �Խñ� ������� �̵�)
		return "redirect:getBoardList.do";
	}

	// ModelAttribute�� ���ǿ� board��� �̸����� ����� ��ü�� �ִ��� ã�Ƽ� Command��ü�� �����
	@RequestMapping(value = "/updateBoard.do")
	public String updateBoard(BoardVO vo, BoardFileVO fvo, HttpServletRequest request, MultipartHttpServletRequest mhsr)
			throws IOException {
		System.out.println("�� ���� ó��");
		System.out.println("�Ϸù�ȣ : " + vo.getSeq());
		System.out.println("���� : " + vo.getTitle());
		System.out.println("�ۼ��� �̸� : " + vo.getWriter());
		System.out.println("���� : " + vo.getContent());
		System.out.println("����� : " + vo.getRegDate());
		System.out.println("��ȸ�� : " + vo.getCnt());

		int seq = vo.getSeq();

		HttpSession session = request.getSession();
		BoardVO board = (BoardVO) session.getAttribute("board");

		BoardFileUtils fileUtils = new BoardFileUtils();
		List<BoardFileVO> fileList = fileUtils.parseFileInfo(board.getSeq(), request, mhsr);

		// �Ѱ���� BoardVO seq�� Comment DB�� seq�� �Ѱܹް���� ��Ȳ
		System.out.println("board�� seq �Ѿ����? :" + board.getSeq());

		vo.setSeq(board.getSeq());
		fvo.setSeq(board.getSeq());

		if (fvo.getFilePath() != null) {
			boardService.deleteBoardFileList(board.getSeq());

			if (CollectionUtils.isEmpty(fileList) == false) {
				boardService.insertBoardFileList(fileList);
			}

		}

		boardService.updateBoard(vo);

		return "redirect:getBoard.do?seq=" + board.getSeq();

	}

	@RequestMapping(value = "/deleteBoard.do")
	public String deleteBoard(BoardVO vo, MemberVO mvo, HttpServletRequest request) {
		System.out.println("�� ���� ó��");

		HttpSession session = request.getSession();
		BoardVO board = (BoardVO) session.getAttribute("board");
		System.out.println("board�� seq �Ѿ����? :" + board.getWriter());

		MemberVO member = (MemberVO) session.getAttribute("member");
		System.out.println("board�� seq �Ѿ����? :" + member.getmId());

		if (member.getmNickname().equals(board.getWriter())) {

			boardService.deleteBoard(vo);
		}

		else {
			return "redirect:getBoard.do?seq=" + board.getSeq();
		}
		return "getBoardList.do";
	}

	@RequestMapping(value = "/getBoard.do")
	public String getBoard(BoardVO vo, Model model, HttpServletRequest request, FreeCommentVO fvo, Criteria cri) {
		System.out.println("�� �� ��ȸ ó��");

		// Model ��ü�� RequestServlet ������ �����ҿ� ����
		// RequestServlet ������ �����ҿ� �����ϴ� �Ͱ� �����ϰ� ����
		// request.setAttribute("board", boardDAO.getBoard(vo)) ==
		// model.addAttribute("board", boardDAO.getBoard(vo))
		boardService.updateCnt(vo);

		System.out.println("��� ���� ��� �˻� ó��");
		int total = boardService.selectCommentCount(fvo);
		cri.setAmount(10);
		model.addAttribute("pageMaker", new PageVO(cri, total));

		model.addAttribute("freeCommentList", boardService.freeCommentList(fvo, cri));

		model.addAttribute("board", boardService.getBoard(vo));
		model.addAttribute("fileList", boardService.getBoardFileList(vo.getSeq()));
		/*
		 * model.addAttribute("commentlist", boardService.getCommentList(vo.getSeq()));
		 */

		return "admin-freeBoardcomment.jsp";

	}

	@RequestMapping(value = "/getUpdateBoard.do")
	public String getUpdateBoard(BoardVO vo, HttpServletRequest request, Model model) {
		System.out.println("�� �� ��ȸ ó��");

		model.addAttribute("board", boardService.getBoard(vo));
		model.addAttribute("fileList", boardService.getBoardFileList(vo.getSeq()));

		System.out.println("�Ϸù�ȣ : " + vo.getSeq());
		return "admin-writeupdate.jsp";
	}

	@RequestMapping(value = "/getBoardCategory.do")

	public String getBoardCategory(BoardVO vo, Model model) {

		model.addAttribute("boardList", boardService.getBoardCategory(vo));

		return "admin-freeBoard.jsp";
	}

	@RequestMapping(value = "/getMyBoardList.do")
	// @RequestParam : Command ��ü�� VO�� ���ΰ��� ���� ����� �Է������� ���� �޾Ƽ� ó��
	// value = ȭ�����κ��� ���޵� �Ķ���� �̸�(jsp�� input�� name�Ӽ� ��)
	// required = ���� ���� ����
	public String getMyBoardList(BoardVO vo, Model model, HttpServletRequest request) {
		System.out.println("�� ��� �˻� ó��");

		System.out.println("�Ϸù�ȣ : " + vo.getSeq());
		System.out.println("���� : " + vo.getTitle());
		System.out.println("�ۼ��� �̸� : " + vo.getWriter());
		System.out.println("���� : " + vo.getContent());
		System.out.println("����� : " + vo.getRegDate());
		System.out.println("��ȸ�� : " + vo.getCnt());

		HttpSession session = request.getSession();
		MemberVO member = (MemberVO) session.getAttribute("member");

		System.out.println(member.getmNickname());
		vo.setWriter(member.getmNickname());

		model.addAttribute("boardList", boardService.getMyBoardList(vo));

		return "admin-freeBoard.jsp";
	}

	@RequestMapping(value = "/deleteFile.do")
	@ResponseBody
	public void deleteFile(BoardFileVO vo) {
		boardService.deleteFile(vo);
	}

	@RequestMapping(value = "insertBoardComment.do")
	public String insertBoardComment(FreeCommentVO vo, HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		System.out.println(vo.getFmNickname());
		System.out.println(vo.getFmComment());
		try {
			// ������ �������� ���� ��
			HttpSession session = request.getSession();

			// ���ǿ� memberVO�� ����
			MemberVO member = (MemberVO) session.getAttribute("member");
			System.out.println(member.getmNickname());

			// boardVO �� ���ǿ� ����
			BoardVO board = (BoardVO) session.getAttribute("board");

			// �Ѱ���� BoardVO seq�� Comment DB�� seq�� �Ѱܹް���� ��Ȳ
			System.out.println("board�� seq �Ѿ����? :" + board.getSeq());

			// commentVO�� fm�г��ӿ� memberVO ������ �г����� ����
			vo.setFmNickname(member.getmNickname());

			// ������ ���seq�� insert�� ���seq�� ��ġ
			vo.setmSeq(member.getmSeq());

			// commentseq = boardseq ó��
			vo.setSeq(board.getSeq());

			boardService.insertBoardComment(vo);

			// ȭ�� �׺���̼�(�Խñ� ��� �Ϸ� �� �Խñ� ������� �̵�)
//      return "getBoard.do";

			return "redirect:getBoard.do?seq=" + board.getSeq();

		} catch (Exception e) {
			// �ݵ�� utf-8 ���� ���� �� PrintWriter out ��ü ���� �� ��.
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.println("<script> alert('�α����� ���ּ���.');</script>");
			out.flush();
			return "login.jsp";
		}
	}

	@RequestMapping(value = "/deleteComment.do")
	public String deleteComment(BoardVO vo, FreeCommentVO fvo, MemberVO mvo, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		System.out.println("�� ���� ó��");

		HttpSession session = request.getSession();
		BoardVO board = (BoardVO) session.getAttribute("board");

		// �α��� ���� ȣ��
		MemberVO member = (MemberVO) session.getAttribute("member");

//      if(fvo.getFmNickname() == member.getmNickname()) {
//         boardService.deleteComment(fvo);
//      }
//      

		// �Ѱ���� BoardVO seq�� Comment DB�� seq�� �Ѱܹް���� ��Ȳ
		System.out.println("board�� seq �Ѿ����? :" + board.getSeq());
		System.out.println("memberVO seq:" + mvo.getmSeq());
		System.out.println("memberVO NickName" + mvo.getmNickname());
		System.out.println("����seq" + member.getmSeq());
		// commentseq = boardseq ó��
		vo.setSeq(board.getSeq());

		if ((mvo.getmSeq()) == member.getmSeq()) {
			System.out.println("���� ó�� �Ǿ����ϴ�.");
			boardService.deleteComment(fvo);
		} else {
			// �ݵ�� utf-8 ���� ���� �� PrintWriter out ��ü ���� �� ��.
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.println("<script> alert('�ۼ��ڸ� �����Ҽ� �ֽ��ϴ�.');</script>");
			out.flush();
		}
		return "getBoard.do?seq=" + board.getSeq();
	}

	@RequestMapping(value = "/getBoardList.do")

	public String getBoardList(BoardVO vo, Model model, Criteria cri) {
		System.out.println("�� ��� �˻� ó��");

		int total = boardService.selectBoardCount(vo);
		cri.setAmount(10);
		model.addAttribute("pageMaker", new PageVO(cri, total));

		model.addAttribute("boardList", boardService.getBoardList(vo, cri));

		return "admin-freeBoard.jsp";
	}
}