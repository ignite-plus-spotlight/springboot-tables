package spring.spot.trial.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import spring.spot.trial.Entity.Nominate;
import spring.spot.trial.Entity.Nominations;
import spring.spot.trial.Entity.PostIntoMultipleEntity;
import spring.spot.trial.Service.NominationsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import spring.spot.trial.dto.PollProcessDTO;
import spring.spot.trial.dto.PopUpDTO;

import java.util.Date;
import java.util.List;
import java.util.UUID;


@RequestMapping
@ResponseBody
@RestController
@CrossOrigin("*")
public class NominationsController {
    @Autowired
    NominationsService nominationsService;

    public NominationsController (NominationsService nominationsService){
        this.nominationsService = nominationsService;
    }

    @GetMapping("/nominations")
    public List<Nominations> getAllNominations(){
        return nominationsService.getAllNominations();
    }

    @GetMapping("/nominations/{id}")
    public List<Nominations> getAllNominations(@PathVariable("id") UUID id){
        return  nominationsService.getNominationsById(id);
    }

    @GetMapping("/nominations/{id}/{nominationId}")
    public List<Nominations> getAllNominations(@PathVariable("id") UUID id,@PathVariable("nominationId") String nominationId){
        return nominationsService.getNominationsByPollIdAndNominationId(id,nominationId);
    }
    @PostMapping("/nominations")
    public Nominations createNominations(@RequestBody Nominations nominations){
        return nominationsService.createNominations(nominations);
    }


    //leader starts nominations process
    @PostMapping("/postmultiple")
    public PostIntoMultipleEntity create(@RequestBody PostIntoMultipleEntity postIntoMultipleEntity)
    {
        return nominationsService.postIntoMultipleTables(postIntoMultipleEntity);
    }

    @GetMapping("/pollprocess/{pollId}")
    public PollProcessDTO pollDisplay(@PathVariable("pollId") UUID pollId)
    {
       return nominationsService.pollProcess(pollId);
    }


    //to nominate a person
    @PostMapping("/nominate")
    public Nominations nominate(@RequestBody Nominate nominate)
    {


        UUID pollId; UUID nominationId; String employeeId; String managerId; String description;  String pollName;
        pollId = nominate.getPollId();
        employeeId = nominate.getEmployeeId();
        managerId = nominate.getManagerId();
        description = nominate.getDescription();
        pollName = nominate.getPollName();
        return nominationsService.nominate(pollId,employeeId,managerId,description,pollName);
    }

}
