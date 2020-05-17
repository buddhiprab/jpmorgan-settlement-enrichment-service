package com.jpmorgan.ses.repositories;

import com.jpmorgan.ses.models.SettlementMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SettlementMessageRepository extends JpaRepository<SettlementMessage, Long> {
}
