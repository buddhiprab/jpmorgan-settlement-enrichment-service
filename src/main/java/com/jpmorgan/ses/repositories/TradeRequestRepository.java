package com.jpmorgan.ses.repositories;

import com.jpmorgan.ses.models.TradeRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TradeRequestRepository extends JpaRepository<TradeRequest, Long> {
    TradeRequest findByTradeId(String tradeId);
}
