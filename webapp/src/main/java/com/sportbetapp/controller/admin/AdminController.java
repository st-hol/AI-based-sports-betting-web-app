package com.sportbetapp.controller.admin;

import static com.sportbetapp.domain.type.SportType.FOOTBALL;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.sportbetapp.dto.betting.CreateSportEventDto;
import com.sportbetapp.dto.betting.PlayerSideDto;
import com.sportbetapp.dto.payload.UploadFileResponse;
import com.sportbetapp.dto.predicting.PredictionDto;
import com.sportbetapp.exception.CanNotPlayAgainstItselfException;
import com.sportbetapp.exception.NoPredictAnalysisDataAvailableException;
import com.sportbetapp.service.betting.SportEventService;
import com.sportbetapp.service.predicting.PredictSportEventService;
import com.sportbetapp.service.predicting.PredictionService;
import com.sportbetapp.service.predicting.StatisticUploadService;
import com.sportbetapp.service.user.UserService;
import com.sportbetapp.validator.sportevent.ChooseSportTypeValidator;
import com.sportbetapp.validator.sportevent.CreateSportEventValidator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private CreateSportEventValidator createSportEventValidator;
    @Autowired
    private ChooseSportTypeValidator chooseSportTypeValidator;
    @Autowired
    private UserService userService;
    @Autowired
    private PredictionService predictionService;
    @Autowired
    private PredictSportEventService predictSportEventService;
    @Autowired
    private StatisticUploadService statisticUploadService;
    @Autowired
    private SportEventService sportEventService;


    @GetMapping("/predict")
    public String formPrediction(Model model) {
        model.addAttribute("listOfTeams", predictionService.getAllTeamsForSportType(FOOTBALL)); //todo stub rm
        model.addAttribute("sportType", FOOTBALL);
//        model.addAttribute("user", userService.obtainCurrentPrincipleUser());
        model.addAttribute("predictionForm", new PredictionDto());
        return "/admin/predictor";
    }

    @PostMapping("/predict")
    public String createPrediction(@ModelAttribute("predictionForm") PredictionDto dto)
            throws CanNotPlayAgainstItselfException, NoPredictAnalysisDataAvailableException {
        predictionService.makePrediction(dto);
        return "redirect:/admin/predictions";
    }

    @GetMapping("/predict-sport-event")
    public String formSportEventPrediction(Model model) {
        model.addAttribute("sportEvents", sportEventService.findAll());

//        model.addAttribute("sportType", FOOTBALL);
//        model.addAttribute("user", userService.obtainCurrentPrincipleUser());
//        model.addAttribute("predictionForm", new PredictSportEventDto());
        return "/admin/predict-sport-event";
    }

    @PostMapping("/predict-sport-event/{sportEventId}")
    public String createSportEventPrediction(@PathVariable Long sportEventId)
            throws NoPredictAnalysisDataAvailableException, CanNotPlayAgainstItselfException {
        predictSportEventService.makePredictionForSportEvent(sportEventId);
        return "redirect:/admin/predict-sport-event";
    }



    @GetMapping("/upload-stats")
    public String uploadForm() {
        return "admin/upload-stats";
    }

    @ResponseBody
    @PostMapping("/upload-stats")
    public UploadFileResponse uploadFile(@RequestParam("file") MultipartFile file) {
        statisticUploadService.processStatisticFile(file);

        return new UploadFileResponse(file.getName(), file.getContentType(), file.getSize());
    }


    @GetMapping("/get-teams-by-sport")
    @ResponseBody
    public List<PlayerSideDto> getTeamsBySportType(@RequestParam("sportType") String sportType){
        return predictSportEventService.getAllTeamsForSportType(sportType);
    }

    @GetMapping("/create-sport-event")
    public String createSportEventForm(Model model){
        model.addAttribute("listOfTypes", sportEventService.findAllSportTypes());
        model.addAttribute("createSportEventForm", new CreateSportEventDto());
        return "admin/create-sport-event";
    }

    @PostMapping("/create-sport-event")
    public String registration(@ModelAttribute("createSportEventForm") CreateSportEventDto createSportEventForm,
                               BindingResult bindingResult, Model model) {
        createSportEventValidator.validate(createSportEventForm, bindingResult);
        if (bindingResult.hasErrors()) {
            log.info("createSportEventForm. form had errors.");
            model.addAttribute("listOfTypes", sportEventService.findAllSportTypes());
            return "admin/create-sport-event";
        }
        sportEventService.createNewSportEvent(createSportEventForm);
        return "redirect:/admin/create-sport-event";
    }

    @ExceptionHandler(CanNotPlayAgainstItselfException.class)
    public String handleNotEnoughBalanceException(Model model, Exception exception) {
        log.error("CanNotPlayAgainstItselfException exception: {}", exception.getMessage());
        model.addAttribute("canNotPlayAgainstItself", true);
        return formPrediction(model);
    }

    @ExceptionHandler(NoPredictAnalysisDataAvailableException.class)
    public String handleNoPredictAnalysisDataAvailableException(Model model, Exception exception) {
        log.error("CanNotPlayAgainstItselfException exception: {}", exception.getMessage());
        model.addAttribute("noPredictAnalysisDataAvailable", true);
        return formSportEventPrediction(model); // todo id
    }


}
