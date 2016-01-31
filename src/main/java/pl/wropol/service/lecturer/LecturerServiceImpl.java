package pl.wropol.service.lecturer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
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
    @Cacheable(cacheNames = "lecturers")
    public List<Lecturer> findAll() {
        return repository.findAll();
    }

    @Override
    @Cacheable(cacheNames = "countLecturers")
    public Long count() {
        return repository.count();
    }

    @Override
    @Cacheable(cacheNames = "lecturer", key = "#id")
    public Lecturer findOne(Long id) {
        return repository.findOne(id);
    }

    @Override
    @Cacheable(cacheNames = "lecturer", key = "#name")
    public boolean exist(String name) {
        return null != findOneByNameLike(name);
    }

    @Override
    @Cacheable(cacheNames = "lecturers", key="#nameOfTutor")
    public List<Lecturer> findOneByNameLike(String nameOfTutor) {
        return repository.findOneByNameLike(nameOfTutor);
    }

    @Override
    @Cacheable(cacheNames = "lecturer", key="#lecturerName")
    public Lecturer findOneByName(String lecturerName) {
        Lecturer lecturer = repository.findAllByName(lecturerName);
        if (lecturer != null) {
            lecturer.incrementViews();
            save(lecturer);
        }
        return lecturer;
    }
}
