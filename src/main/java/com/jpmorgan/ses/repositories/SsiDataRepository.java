package com.jpmorgan.ses.repositories;

import com.jpmorgan.ses.models.SsiData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/*created by Buddhi*/

@Repository
public interface SsiDataRepository extends JpaRepository<SsiData, Long> {
    SsiData findBySsiCode(String ssiCode);
}
