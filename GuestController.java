package ch10;

import java.io.IOException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
//import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns="/guest")
public class GuestController extends HttpServlet {
    private static final long serialVersionUID = 1L;
       
    private GuestbookDAO dao;
    private ServletContext ctx;
    
    private final String START_PAGE = "ch10/guestList.jsp";
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        dao = new GuestbookDAO();
        ctx = getServletContext();
    }
    
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        String action = request.getParameter("action");
        dao = new GuestbookDAO();
        
        Method m;
        String view = null;
        
        if (action == null) {
            action = "listGuest";
        }
        try {
            m = this.getClass().getMethod(action, HttpServletRequest.class);
            view = (String)m.invoke(this, request);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            ctx.log("요청 action 없음!");
            request.setAttribute("error", "action 파라미터가 잘못되었습니다.");
            view = START_PAGE;
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        if (view.startsWith("redirect:/")) {
            String rview = view.substring("redirect:/".length());
            response.sendRedirect(rview);
        } else {
            RequestDispatcher dispatcher = request.getRequestDispatcher(view);
            dispatcher.forward(request, response);
        }
    }
    
    // 방명록 입력(등록)
    public String addGuest(HttpServletRequest request) {
        String writer = request.getParameter("writer");
        String email = request.getParameter("email");
        String title = request.getParameter("title");
        String password = request.getParameter("password");
        String content = request.getParameter("content");

        // 데이터가 비어있는지 확인
        if (writer == null || writer.isEmpty() || email == null || email.isEmpty() || title == null || title.isEmpty() || password == null || password.isEmpty() || content == null || content.isEmpty()) {
            request.setAttribute("error", "데이터를 모두 입력해주세요.");
            return "ch10/guestForm.jsp";
        }
        
        Guestbook g = new Guestbook();
        g.setWriter(writer);
        g.setEmail(email);
        g.setTitle(title);
        g.setPassword(password);
        g.setContent(content);

        try {
            dao.addGuestbook(g);
        } catch (Exception e) {
            e.printStackTrace();
            ctx.log("방명록 입력 과정에서 문제 발생!");
            request.setAttribute("error", "방명록이 정상적으로 등록되지 않았습니다.");
            return listGuest(request);
        }
        return "redirect:/guest?action=listGuest";
    }
    
    // 방명록 수정
    public String updateGuest(HttpServletRequest request) {
        int idx = Integer.parseInt(request.getParameter("idx"));
        String writer = request.getParameter("writer");
        String email = request.getParameter("email");
        String title = request.getParameter("title");
        String password = request.getParameter("password");
        String content = request.getParameter("content");

        // 데이터가 비어있는지 확인
        if (writer == null || writer.isEmpty() || email == null || email.isEmpty() || title == null || title.isEmpty() || password == null || password.isEmpty() || content == null || content.isEmpty()) {
            request.setAttribute("error", "데이터를 모두 입력해주세요.");
            return editGuest(request);
        }

        try {
            Guestbook existingGuestbook = dao.getGuestbook(idx);
            if (existingGuestbook != null) {
                if (password.equals(existingGuestbook.getPassword())) {
                    Guestbook updatedGuestbook = new Guestbook();
                    updatedGuestbook.setIdx(idx);
                    updatedGuestbook.setWriter(writer);
                    updatedGuestbook.setEmail(email);
                    updatedGuestbook.setTitle(title);
                    updatedGuestbook.setPassword(password);
                    updatedGuestbook.setContent(content);

                    dao.updateGuestbook(updatedGuestbook);

                    existingGuestbook.setWriter(writer);
                    existingGuestbook.setEmail(email);
                    existingGuestbook.setTitle(title);
                    existingGuestbook.setContent(content);

                    request.setAttribute("guest", existingGuestbook);

                    return "redirect:/guest?action=listGuest";
                } else {
                    request.setAttribute("error", "비밀번호가 일치하지 않습니다.");
                    return editGuest(request);
                }
            } else {
                request.setAttribute("error", "방명록을 찾을 수 없습니다.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            ctx.log("방명록을 가져오는 과정에서 문제 발생!");
            request.setAttribute("error", "방명록을 정상적으로 가져오지 못했습니다.");
        }

        return "redirect:/guest?action=listGuest";
    }

    // 방명록 목록
    public String listGuest(HttpServletRequest request) {
        List<Guestbook> list;
        try {
            list = dao.getAll();
            request.setAttribute("guestlist", list);
        } catch (Exception e) {
            e.printStackTrace();
            ctx.log("방명록 목록 생성 과정에서 문제 발생!");
            request.setAttribute("error", "방명록 목록이 정상적으로 처리되지 않았습니다.");
        } 
        return "ch10/guestList.jsp";
    }
    
    public String editGuest(HttpServletRequest request) {
        int idx = Integer.parseInt(request.getParameter("idx"));
        
        try {
            Guestbook g = dao.getGuestbook(idx);
            if (g != null) {
            	request.setAttribute("guest", g);
            } else {
            	request.setAttribute("error", "방명록을 찾을 수 없습니다.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            ctx.log("방명록을 가져오는 과정에서 문제 발생!");
            request.setAttribute("error", "방명록을 정상적으로 가져오지 못했습니다.");
        }
        
        return "ch10/guestEdit.jsp";
    }
    
 // 방명록 삭제
    public String delGuest(HttpServletRequest request) {
        int idx = Integer.parseInt(request.getParameter("idx"));

        try {
            dao.delGuestbook(idx);
        } catch (SQLException e) {
            e.printStackTrace();
            ctx.log("방명록 삭제 과정에서 문제 발생!");
            request.setAttribute("error", "방명록이 정상적으로 삭제되지 않았습니다.");
        }
        return "redirect:/guest?action=listGuest";
    }

}
