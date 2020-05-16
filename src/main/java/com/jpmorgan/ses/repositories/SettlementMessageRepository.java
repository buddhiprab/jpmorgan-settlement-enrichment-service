package com.jpmorgan.ses.repositories;

import com.jpmorgan.ses.models.SettlementMessage;
import com.jpmorgan.ses.models.SsiData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SettlementMessageRepository extends JpaRepository<SettlementMessage, Long> {
}
