package pl.wropol.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.wropol.model.Lecturer;
import pl.wropol.repository.LecturerRepository;

import java.util.List;

/**
 * Created by Rafal on 2016-01-15.
 */

@Service
public class LecturerService {

    @Autowired
    LecturerRepository repository;

    public List<Lecturer> findAll(){
        return repository.findAll();
    }
    public Lecturer findOne(Long id){
        return repository.findOne(id);
    }

}
