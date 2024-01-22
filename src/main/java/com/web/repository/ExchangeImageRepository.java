package com.web.repository;

import com.web.entity.Exchange;
import com.web.entity.ExchangeImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExchangeImageRepository extends JpaRepository<ExchangeImage,Long> {
}
