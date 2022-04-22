package xyz.sgmi.clay.dao;

import org.springframework.data.repository.CrudRepository;
import xyz.sgmi.clay.domain.MessageTemplate;

/**
 * 消息模板Dao
 * @author MSG
 */
public interface MessageTemplateDao extends CrudRepository<MessageTemplate, Long> {

}

