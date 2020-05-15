package com.jpmorgan.ses.repositories;

import com.jpmorgan.ses.models.SsiData;
import com.jpmorgan.ses.models.TradeRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SsiDataRepository extends JpaRepository<SsiData, Long> {
    public List<SsiData> findBySsiCode(String ssiCode);
}
