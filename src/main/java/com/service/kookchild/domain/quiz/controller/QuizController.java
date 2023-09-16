package com.service.kookchild.domain.quiz.controller;

import com.service.kookchild.domain.quiz.dto.*;
import com.service.kookchild.domain.quiz.service.QuizService;
import com.service.kookchild.domain.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/quiz")
public class QuizController {

    private final QuizService quizService;

    @GetMapping("")
    public ResponseEntity getTodayQuiz(Authentication authentication){
        String email = getEmail(authentication);
        QuizDTO todayQuizDTO = quizService.getTodayQuiz(email);
        return ResponseEntity.ok(todayQuizDTO);
    }

    @GetMapping("/{quizId}")
    public ResponseEntity getTodayQuizDetail(Authentication authentication, @PathVariable long quizId){
        String email = getEmail(authentication);
        TodayQuizDetailDTO todayQuizDetailDTO = quizService.getTodayQuizDetail(email, quizId);
        return ResponseEntity.ok(todayQuizDetailDTO);
    }

    @PostMapping("")
    public ResponseEntity checkQuizAnswer(Authentication authentication, @RequestBody QuizAnswerDTO quizAnswerDTO){
        String email = getEmail(authentication);
        QuizResultDTO quizResultDTO = quizService.checkQuizAnswer(email, quizAnswerDTO);
        return ResponseEntity.ok(quizResultDTO);
    }

    @GetMapping("/{quizId}/explanation")
    public ResponseEntity explainQuiz(@PathVariable Long quizId) {
        QuizExplanationResponseDTO quizExplanationResponseDTO = quizService.explainQuiz(quizId);
        return ResponseEntity.ok(quizExplanationResponseDTO);
    }

    @GetMapping("/history")
    public ResponseEntity getHistoryQuizList(Authentication authentication,
                                             @RequestParam(value = "search", required = false) String search){
        String email = getEmail(authentication);
        HistoryQuizListDTO historyQuizListDTO = quizService.getHistoryQuizList(email, search);
        return ResponseEntity.ok(historyQuizListDTO);
    }

    @GetMapping("/history/{quizId}")
    public ResponseEntity getHistoryQuizDetail(Authentication authentication, @PathVariable long quizId){
        String email = getEmail(authentication);
        QuizDetailDTO quizDetailDTO = quizService.getHistoryQuizDetail(email, quizId);
        return ResponseEntity.ok(quizDetailDTO);
    }

    @GetMapping("/parent")
    public ResponseEntity getChildQuizList(Authentication authentication){
        String email = getEmail(authentication);
        QuizParentListDTO quizParentListDTO = quizService.getChildQuizList(email);
        return ResponseEntity.ok(quizParentListDTO);
    }

    public String getEmail(Authentication authentication) {
        CustomUserDetails principal = (CustomUserDetails)authentication.getPrincipal();
        return principal.getEmail();
    }
}
