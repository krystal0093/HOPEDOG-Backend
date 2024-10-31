package com.example.hope_dog.controller.admin;

import com.example.hope_dog.dto.admin.*;
import com.example.hope_dog.service.admin.AdminService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/admin")
public class AdminController {
    private final AdminService adminService;

    private boolean isAdminLoggedIn(HttpSession session) {
        return session.getAttribute("adminNo") != null;
    }

    @GetMapping("/login")
    public String login() {
        return "admin/admin-login/admin-login";
    }

    @PostMapping("/login")
    public String login(@RequestParam("adminId") String adminId, @RequestParam("adminPw") String adminPw, HttpSession session) {
        log.info("로그인 시도 : {}", adminId);

        AdminSessionDTO loginInfo = adminService.findLoginInfo(adminId, adminPw);

        session.setAttribute("adminNo", loginInfo.getAdminNo());
        session.setAttribute("adminId", loginInfo.getAdminId());

        return "redirect:/admin/main";
    }

    @GetMapping("/main")
    public String main(Model model, HttpSession session) {
        if (!isAdminLoggedIn(session)) {
            return "redirect:/admin/login"; // 비로그인 시 로그인 페이지로 리다이렉트
        }
        List<AdminMemberDTO> memberList = adminService.selectMemberList();
        List<AdminReportDTO> reportList = adminService.selectReportList();
        List<AdminPostDTO> postList = adminService.selectPostList();
        List<AdminCommentDTO> commentList = adminService.selectCommentList();

        model.addAttribute("memberList", memberList);
        model.addAttribute("reportList", reportList);
        model.addAttribute("postList", postList);
        model.addAttribute("commentList", commentList);

        return "admin/admin-main/admin-main"; // 뷰 이름 반환
    }

    @GetMapping("/postList")
    public String postList(Model model, HttpSession session) {
        if (!isAdminLoggedIn(session)) {
            return "redirect:/admin/login"; // 비로그인 시 로그인 페이지로 리다이렉트
        }
        List<AdminPostDTO> postList = adminService.selectPostList();

        model.addAttribute("postList", postList);

        return "admin/admin-post/admin-post-list";
    }

    @GetMapping("/searchPostList")
    public String searchPostList(@RequestParam("keyword") String keyword, Model model, HttpSession session) {
        if (!isAdminLoggedIn(session)) {
            return "redirect:/admin/login";
        }
        System.out.println("실행");
        List<AdminPostDTO> searchedPostList = adminService.searchPostByKeyword(keyword);
        System.out.println(searchedPostList);

        model.addAttribute("searchedPostList", searchedPostList);

        return "admin/admin-post/admin-post-search-list";
    }

    @GetMapping("/postDetail")
    public String postDetail(@RequestParam("postType") String postType, @RequestParam("postNo") Long postNo, Model model, HttpSession session) {
        if (!isAdminLoggedIn(session)) {
            return "redirect:/admin/login";
        }

        AdminPostDTO post = adminService.selectPostDetail(postType, postNo);
        List<AdminCommentDTO> commentList = adminService.selectCommentListByPostNo(postType, postNo);

        model.addAttribute("post", post);
        model.addAttribute("commentList", commentList);

        return "admin/admin-post/admin-post-detail";
    }

    @GetMapping("/commentList")
    public String commentList(Model model, HttpSession session) {
        if (!isAdminLoggedIn(session)) {
            return "redirect:/admin/login"; // 비로그인 시 로그인 페이지로 리다이렉트
        }
        List<AdminCommentDTO> commentList = adminService.selectCommentList();
        List<AdminPostDTO> postList = adminService.selectPostList();

        model.addAttribute("commentList", commentList);
        model.addAttribute("postList", postList);

        return "admin/admin-comment/admin-comment-list";
    }

    @GetMapping("/searchCommentList")
    public String searchCommentList(@RequestParam("keyword") String keyword, Model model, HttpSession session) {
        if (!isAdminLoggedIn(session)) {
            return "redirect:/admin/login";
        }

        List<AdminCommentDTO> searchedCommentList = adminService.searchCommentByKeyword(keyword);
        List<AdminPostDTO> searchedPostList = adminService.searchPostByKeyword(keyword);

        model.addAttribute("searchedCommentList", searchedCommentList);
        model.addAttribute("searchedPostList", searchedPostList);

        return "admin/admin-comment/admin-comment-search-list";
    }

    @GetMapping("/noticeList")
    public String noticeList(Model model, HttpSession session) {
        if (!isAdminLoggedIn(session)) {
            return "redirect:/admin/login"; // 비로그인 시 로그인 페이지로 리다이렉트
        }
        List<AdminNoticeDTO> noticeList = adminService.selectNoticeList();

        model.addAttribute("noticeList", noticeList);

        return "admin/admin-notice/admin-notice-list";
    }

    @GetMapping("/searchNoticeList")
    public String searchNoticeList(@RequestParam("keyword") String keyword ,Model model, HttpSession session) {
        if (!isAdminLoggedIn(session)) {
            return "redirect:/admin/login";
        }

        List<AdminNoticeDTO> noticeList = adminService.searchNoticeByKeyword(keyword);

        model.addAttribute("noticeList", noticeList);

        return "admin/admin-notice/admin-notice-search-list";
    }

    @GetMapping("/noticeDetail")
    public String noticeDetail(@RequestParam("noticeNo") Long noticeNo, Model model, HttpSession session) {
        if (!isAdminLoggedIn(session)) {
            return "redirect:/admin/login";
        }

        AdminNoticeDTO notice = adminService.selectNoticeDetail(noticeNo);

        model.addAttribute("notice", notice);

        return "admin/admin-notice/admin-notice-detail";
    }

    @GetMapping("/memberList")
    public String memberList(Model model, HttpSession session) {
        if (!isAdminLoggedIn(session)) {
            return "redirect:/admin/login"; // 비로그인 시 로그인 페이지로 리다이렉트
        }
        List<AdminMemberDTO> memberList = adminService.selectMemberList();

        model.addAttribute("memberList", memberList);
        model.addAttribute("currentPage", "general");

        return "admin/admin-member/admin-member-list";
    }

    @GetMapping("/centerMemberList")
    public String centerMemberList(Model model, HttpSession session) {
        if (!isAdminLoggedIn(session)) {
            return "redirect:/admin/login"; // 비로그인 시 로그인 페이지로 리다이렉트
        }
        List<AdminCenterMemberDTO> centerMemberList = adminService.selectPassedCenterMemberList();

        model.addAttribute("centerMemberList", centerMemberList);
        model.addAttribute("currentPage", "center");

        return "admin/admin-member/admin-center-member-list";
    }

    @PostMapping("/deleteMembers")
    @ResponseBody
    public String deleteMembers(@RequestBody List<Long> itemList, HttpSession session) {
        if (!isAdminLoggedIn(session)) {
            return "redirect:/admin/login";
        }
        adminService.deleteMember(itemList);

        return "redirect:/admin/memberList";
    }

    @PostMapping("/deleteCenterMembers")
    @ResponseBody
    public String deleteCenterMembers(@RequestBody List<Long> itemList, HttpSession session) {
        if (!isAdminLoggedIn(session)) {
            return "redirect:/admin/login";
        }
        adminService.deleteCenterMember(itemList);

        return "redirect:/admin/centerMemberList";
    }

    @GetMapping("/searchMemberList")
    public String searchMemberList(@RequestParam("keyword") String keyword, Model model, HttpSession session) {
        if (!isAdminLoggedIn(session)) {
            return "redirect:/admin/login"; // 비로그인 시 로그인 페이지로 리다이렉트
        }
        List<AdminMemberDTO> memberSearchedList = adminService.searchMemberByKeyword(keyword);

        model.addAttribute("memberSearchedList", memberSearchedList);
        model.addAttribute("currentPage", "general");

        return "admin/admin-member/admin-member-search-list";
    }

    @GetMapping("/searchCenterMemberList")
    public String searchCenterMemberList(@RequestParam("keyword") String keyword, Model model, HttpSession session) {
        if (!isAdminLoggedIn(session)) {
            return "redirect:/admin/login"; // 비로그인 시 로그인 페이지로 리다이렉트
        }
        List<AdminCenterMemberDTO> centerMemberSearchedList = adminService.searchPassedCenterMemberByKeyword(keyword);

        model.addAttribute("centerMemberSearchedList", centerMemberSearchedList);
        model.addAttribute("currentPage", "center");

        return "admin/admin-member/admin-center-member-search-list";
    }

    @GetMapping("/memberDetail")
    public String memberDetail(@RequestParam("memberNo") Long memberNo, Model model, HttpSession session) {
        if (!isAdminLoggedIn(session)) {
            return "redirect:/admin/login";
        }
        AdminMemberDTO member = adminService.selectMemberByNo(memberNo);

        model.addAttribute("member", member);

        return "admin/admin-member/admin-member-detail";
    }

    @GetMapping("/centerMemberDetail")
    public String centerMemberDetail(@RequestParam("centerMemberNo") Long centerMemberNo, Model model, HttpSession session) {
        if (!isAdminLoggedIn(session)) {
            return "redirect:/admin/login";
        }
        AdminCenterMemberDTO centerMember = adminService.selectPassedCenterMemberByNo(centerMemberNo);

        model.addAttribute("centerMember", centerMember);

        return "admin/admin-member/admin-center-member-detail";
    }

    @GetMapping("/reportList")
    public String reportList(Model model, HttpSession session) {
        if (!isAdminLoggedIn(session)) {
            return "redirect:/admin/login"; // 비로그인 시 로그인 페이지로 리다이렉트
        }
        List<AdminReportDTO> reportList = adminService.selectReportList();

        model.addAttribute("reportList", reportList);

        return "admin/admin-report/admin-report-list";
    }

    @GetMapping("/centerApplyList")
    public String centerApplyList(Model model, HttpSession session) {
        if (!isAdminLoggedIn(session)) {
            return "redirect:/admin/login"; // 비로그인 시 로그인 페이지로 리다이렉트
        }
        List<AdminCenterMemberDTO> centerMemberList = adminService.selectCenterMemberList();

        model.addAttribute("centerMemberList", centerMemberList);

        return "admin/admin-center-apply/admin-center-apply-list";
    }

    @GetMapping("/centerApplyDetail")
    public String centerApplyDetail(@RequestParam("centerMemberNo") Long centerMemberNo, Model model, HttpSession session) {
        if (!isAdminLoggedIn(session)) {
            return "redirect:/admin/login";
        }

        AdminCenterMemberDTO centerMember = adminService.selectNotPassedCenterMemberByNo(centerMemberNo);

        model.addAttribute("centerMember", centerMember);

        return "admin/admin-center-apply/admin-center-apply-detail";
    }

    @GetMapping("/ATPList")
    public String ATPList(Model model, HttpSession session) {
        if (!isAdminLoggedIn(session)) {
            return "redirect:/admin/login";
        }
        List<AdminAdoptRequestDTO> adoptRequestList = adminService.selectAdoptRequestList();
        List<AdminProtectRequestDTO> protectRequestList = adminService.selectProtectRequestList();

        model.addAttribute("adoptRequestList", adoptRequestList);
        model.addAttribute("protectRequestList", protectRequestList);

        return "admin/admin-ATP/admin-ATP-list";
    }

    @GetMapping("/volunList")
    public String volunList(Model model, HttpSession session) {
        if (!isAdminLoggedIn(session)) {
            return "redirect:/admin/login";
        }
        List<AdminVolunRequestDTO> volunRequestList = adminService.selectVolunRequestList();

        model.addAttribute("volunRequestList", volunRequestList);

        return "admin/admin-volun/admin-volun-list";
    }

    @GetMapping("/noteboxIn")
    public String noteboxIn(Model model, HttpSession session) {
        if (!isAdminLoggedIn(session)) {
            return "redirect:/admin/login";
        }
        List<AdminNoteReceiveDTO> noteReceiveList = adminService.selectNoteReceiveList();

        model.addAttribute("noteReceiveList", noteReceiveList);

        return "admin/admin-notebox/admin-notebox-in";
    }

    @GetMapping("/noteboxOut")
    public String noteboxOut(Model model, HttpSession session) {
        if (!isAdminLoggedIn(session)) {
            return "redirect:/admin/login";
        }
        List<AdminNoteSendDTO> noteSendList = adminService.selectNoteSendList();

        model.addAttribute("noteSendList", noteSendList);

        return "admin/admin-notebox/admin-notebox-out";
    }
}