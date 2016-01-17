package pl.wropol.service.lecturer;

import pl.wropol.model.Lecturer;
import pl.wropol.service.IService;

import java.util.List;

/**
 * Created by evelan on 1/15/16.
 */
public interface LecturerService extends IService<Lecturer> {

    boolean exist(String name);

    List<Lecturer> findOneByNameLike(String name);

    Lecturer findOneByName(String lecturerName);
}
