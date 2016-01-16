package pl.wropol.service.lecturer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.wropol.model.Lecturer;
import pl.wropol.repository.LecturerRepository;

import java.util.List;

/**
 * Created by Rafal on 2016-01-15.
 */

@Service
public class LecturerServiceImpl implements LecturerService {

    @Autowired
    LecturerRepository repository;

    @Override
    public void save(Lecturer entity) {
        repository.save(entity);
    }

    @Override
    public List<Lecturer> findAll() {
        return repository.findAll();
    }

    @Override
    public Long count() {
        return repository.count();
    }

    @Override
    public Lecturer findOne(Long id) {
        return repository.findOne(id);
    }

    @Override
    public boolean exist(String name) {
        return null != findOneByName(name);
    }

    @Override
    public Lecturer findOneByName(String nameOfTutor) {
        return repository.findOneByName(nameOfTutor);
    }
}
