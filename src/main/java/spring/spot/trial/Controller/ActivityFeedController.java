package spring.spot.trial.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import spring.spot.trial.Entity.ActivityFeed;
import spring.spot.trial.Entity.EmpLikes;
import spring.spot.trial.Service.ActivityService;

import java.util.List;
import java.util.UUID;

@CrossOrigin("*")
@RestController
@RequestMapping
@ResponseBody
public class ActivityFeedController {

    @Autowired
    ActivityService activityService;

    public ActivityFeedController(ActivityService activityService) {
        this.activityService = activityService;
    }

    //universal
    //used to view all the awards won by the employees
    @GetMapping("activityfeed")
    public List<ActivityFeed> getAllActivity()
    {
        return activityService.getAll();
    }

    //likes
    @PostMapping("likes/{yourId}")
    public ActivityFeed likes(@PathVariable("yourId") String id,@RequestBody ActivityFeed activityFeed){
        return activityService.likes(activityFeed,id);
    }
}