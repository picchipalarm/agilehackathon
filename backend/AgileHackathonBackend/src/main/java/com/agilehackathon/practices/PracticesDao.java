package com.agilehackathon.practices;

import com.agilehackathon.model.Practice;

import java.util.ArrayList;
import java.util.List;

public class PracticesDao {

    private List<Practice> practices = new ArrayList<>();

    public PracticesDao() {
        practices.add(new Practice("Dr. Quinn Surgery", 1, "Hoxton", "https://www.google.co.uk/maps/place/Hoxton+Square/@51.5273296,-0.0808067,17z/data=!3m1!4b1!4m2!3m1!1s0x48761cbadbc045ff:0x54292b8ccb0589c2"));
        practices.add(new Practice("Doctor Dolittle", 2, "Old Street", "https://www.google.co.uk/maps/place/Old+St/@51.5254642,-0.0879389,17z/data=!3m1!4b1!4m2!3m1!1s0x48761ca8abba80d9:0xd6cf02f1c545d61e"));
    }

    public List<Practice> findAllPractices(){
        return practices;
    }

    public Practice findPracticeById(Integer practiceId) {
        return null;
    }
}
