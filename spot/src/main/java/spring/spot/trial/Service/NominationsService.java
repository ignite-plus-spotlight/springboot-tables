package spring.spot.trial.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spring.spot.trial.Entity.*;
import spring.spot.trial.Repository.*;
import spring.spot.trial.dto.NominationDTO;
import spring.spot.trial.dto.PollProcessDTO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static java.util.UUID.*;

@Service
public class NominationsService {

    @Autowired
    NominationsRepository nominationsRepository;

    @Autowired
    PollRepository pollRepository;

    @Autowired
    NominationDateRepository nominationDateRepository;

    @Autowired
    PollingDateRepository pollingDateRepository;

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    NominationsHistoryRepository nominationsHistoryRepository;

    public NominationsService(NominationsRepository nominationsRepository){
        this.nominationsRepository = nominationsRepository;
    }

    public Nominations createNominations(Nominations nominations){
        return nominationsRepository.save(nominations);
    }

    public List<Nominations> getAllNominations(){
        return nominationsRepository.findAll();
    }

    public List<Nominations> getNominationsById(String pollId){
        return  nominationsRepository.findByPollId(pollId);
    }

    public List<Nominations> getNominationsByPollIdAndNominationId(String pollId,String nominationId){
        return nominationsRepository.findByPollIdAndNominationId(pollId,nominationId);
    }

    public PostIntoMultipleEntity postIntoMultipleTables(PostIntoMultipleEntity postIntoMultipleEntity )
    {
        String pollName; String description; Date nomStart; Date nomEnd; Date pollStart; Date pollEnd;

        pollName = postIntoMultipleEntity.getPollName();
        description = postIntoMultipleEntity.getDescription();
        nomStart = postIntoMultipleEntity.getNomStart();
        nomEnd = postIntoMultipleEntity.getNomEnd();
        pollStart = postIntoMultipleEntity.getPollStart();
        pollEnd = postIntoMultipleEntity.getPollEnd();
        UUID pollId = randomUUID();

        Poll poll = new Poll();
        poll.setPollId(pollId);
        poll.setPollName(pollName);
        poll.setDescription(description);
        pollRepository.save(poll);

        NominationDate nominationDate = new NominationDate();
        nominationDate.setPollId(pollId);
        nominationDate.setNominationStartDate(nomStart);
        nominationDate.setNominationEndDate(nomEnd);
        nominationDateRepository.save(nominationDate);

        PollingDate pollingDate = new PollingDate();
        pollingDate.setPollName(pollName);
        pollingDate.setPollStartDate(pollStart);
        pollingDate.setPollEndDate(pollEnd);
        pollingDateRepository.save(pollingDate);

        return postIntoMultipleEntity;

    }

    public PollProcessDTO pollProcess(String pollId){
        PollProcessDTO pollProcessDTO = new PollProcessDTO();
        List<NominationDTO> nominationDTOS = new ArrayList<>();
        List<Nominations> nominations = nominationsRepository.findByPollId(pollId);
        for(Nominations nomination : nominations)
        {
            NominationDTO nominationDTO = new NominationDTO();
            nominationDTO.setDescription(nomination.getDescription());
            nominationDTO.setEmp_id(nomination.getEmployeeId());
            nominationDTO.setManager_id(nomination.getManagerId());
            nominationDTO.setNominationId(nomination.getNominationId());
            nominationDTO.setEmployee(employeeRepository.findByEmpId(nomination.getEmployeeId()).get(0));
            nominations.add(nomination);
        }
        pollProcessDTO.setPollId(pollId);
        pollProcessDTO.setNominationDTOS(nominationDTOS);
        return pollProcessDTO;
    }

    public Nominations nominate(UUID pollId, String nominationId, String employeeId, String managerId, String description, Date createdDate, String pollName)
    {
        Nominations nominations = new Nominations();
        nominations.setNominationId(nominationId);
        nominations.setPollId(pollId);
        nominations.setManagerId(managerId);
        nominations.setEmployeeId(employeeId);
        nominations.setDescription(description);
        nominationsRepository.save(nominations);

        NominationsHistory nominationsHistory = new NominationsHistory();
        nominationsHistory.setCreatedDate(createdDate);
        nominationsHistory.setEmployeeId(employeeId);
        nominationsHistory.setManagerId(managerId);
        nominationsHistory.setNominationId(nominationId);
        nominationsHistory.setPollName(pollName);
        nominationsHistoryRepository.save(nominationsHistory);

        return nominations;
    }
}
