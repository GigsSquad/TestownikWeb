package pl.wropol.service.lecturer;

import pl.wropol.model.Lecturer;
import pl.wropol.service.IService;

/**
 * Created by evelan on 1/15/16.
 */
public interface LecturerService extends IService<Lecturer> {

    boolean exist(String name);

    Lecturer findOneByName(String name);
}
