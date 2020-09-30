package com.sportbetapp.controller.admin;

import static com.sportbetapp.domain.type.SportType.FOOTBALL;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.sportbetapp.dto.payload.UploadFileResponse;
import com.sportbetapp.dto.predicting.PredictionDto;
import com.sportbetapp.exception.CanNotPlayAgainstItselfException;
import com.sportbetapp.service.predicting.PredictionService;
import com.sportbetapp.service.user.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;
    @Autowired
    private PredictionService predictionService;


    @GetMapping("/predict")
    public String formPrediction(Model model) {
        model.addAttribute("listOfTeams", Arrays.asList("Arsenal", "Bournemouth", "Brighton", "Burnley",
                "Chelsea", "Crystal Palace", "Everton", "Huddersfield", "Leicester",
                "Liverpool", "Manchester City", "Manchester United", "Newcastle",
                "Southampton", "Watford", "West Ham"));

        model.addAttribute("listOfTeams", predictionService.getAllTeamsForSportType(FOOTBALL));

//        model.addAttribute("user", userService.obtainCurrentPrincipleUser());
        model.addAttribute("predictionForm", new PredictionDto());
        return "/admin/predictor";
    }

    @PostMapping("/predict")
    public String createPrediction(@ModelAttribute("predictionForm") PredictionDto dto)
            throws CanNotPlayAgainstItselfException {
        predictionService.makePrediction(dto);
        return "redirect:/admin/predictions";
    }

    @GetMapping("/upload-stats")
    public String uploadForm() {
        return "admin/upload-stats";
    }


    @ResponseBody
    @PostMapping("/upload-stats")
    public UploadFileResponse uploadFile(@RequestParam("file") MultipartFile file) {
        predictionService.processStatisticFile(file);

        return new UploadFileResponse(file.getName(), file.getContentType(), file.getSize());
    }

    @ExceptionHandler(CanNotPlayAgainstItselfException.class)
    public String handleNotEnoughBalanceException(Model model, Exception exception) {
        log.error("CanNotPlayAgainstItselfException exception: {}", exception.getMessage());
        model.addAttribute("canNotPlayAgainstItself", true);
        return formPrediction(model);
    }

}
