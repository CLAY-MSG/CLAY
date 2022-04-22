package xyz.sgmi.clay.dao;

import xyz.sgmi.clay.domain.SmsRecord;
import org.springframework.data.repository.CrudRepository;

/**
 * 短信记录的Dao
 * @author MSG
 */
public interface SmsRecordDao extends CrudRepository<SmsRecord, Long> {


}
