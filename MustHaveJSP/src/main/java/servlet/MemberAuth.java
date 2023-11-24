package servlet;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import membership.MemberDAO;
import membership.MemberDTO;

/**
 * Servlet implementation class MemberAuth
 */
public class MemberAuth extends HttpServlet {
	private static final long serialVersionUID = 1L;
	MemberDAO dao;
	
	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init() throws ServletException {
		ServletContext application = this.getServletContext();
		
		String driver = application.getInitParameter("OracleDriver");
		String connectUrl = application.getInitParameter("OracleURL");
		String oId = application.getInitParameter("OracleId");
		String oPass = application.getInitParameter("OraclePwd");
		
		dao = new MemberDAO(driver, connectUrl, oId, oPass);
	}

	/**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String admin_id = this.getInitParameter("admin_id");
		
		String id = request.getParameter("id");
		String pass = request.getParameter("pass");
		
		MemberDTO memberDTO = dao.getMemberDTO(id, pass);
		
		String memberName = memberDTO.getName();
		if(memberName != null) {
			request.setAttribute("authMessage", memberName + " 회원님 방가방가^^*");
		}else {
			if(admin_id.equals(id))
				request.setAttribute("authMessage", admin_id + "는 최고 관리자 입니다.");
			else
				request.setAttribute("authMessage", "귀하는 회원이 아닙니다.");
		}
		request.getRequestDispatcher("/13Servlet/MemberAuth.jsp").forward(request, response);
	}

	@Override
	public void destroy() {
		dao.close();
	}
}
